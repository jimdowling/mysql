/*
 * This file is part of HopsWorks
 *
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved.
 *
 * HopsWorks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HopsWorks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with HopsWorks.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.hops.hopsworks.common.security;

import io.hops.hopsworks.common.dao.certificates.CertsFacade;
import io.hops.hopsworks.common.dao.project.ProjectFacade;
import io.hops.hopsworks.common.dao.user.UserFacade;
import io.hops.hopsworks.common.exception.CryptoPasswordNotFoundException;
import io.hops.hopsworks.common.hdfs.HdfsUsersController;
import io.hops.hopsworks.common.util.Settings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.net.HopsSSLSocketFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.ssl.FileBasedKeyStoresFactory;
import org.apache.hadoop.security.ssl.SSLFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class BaseHadoopClientsService {
  
  @EJB
  private CertificateMaterializer certificateMaterializer;
  @EJB
  private UserFacade userFacade;
  @EJB
  private ProjectFacade projectFacade;
  @EJB
  private CertsFacade certsFacade;
  @EJB
  protected Settings settings;
  
  private Configuration sslConf;
  private String superKeystorePath;
  private String superKeystorePassword;
  private String superTrustStorePath;
  private String superTrustStorePassword;
  private String superuser;
  private final Pattern projectGenericUserPatter =
      Pattern.compile("(\\w*)" + Settings.PROJECT_GENERIC_USER_SUFFIX);
  
  private final Logger LOG = Logger.getLogger(
      BaseHadoopClientsService.class.getName());
  
  public BaseHadoopClientsService() {
  }
  
  @PostConstruct
  public void init() {
    String confDir = settings.getHadoopConfDir();
    File coreSite = new File(confDir, "core-site.xml");
    if (!coreSite.exists()) {
      handleMissingConf("core-site.xml", confDir);
    }
    
    Configuration conf = new Configuration();
    conf.addResource(new Path(coreSite.getAbsolutePath()));
    
    sslConf = new Configuration(false);
    String hadoopConfDir = settings.getHadoopConfDir();
    File serverSSLConf = new File(hadoopConfDir, conf.get(SSLFactory
        .SSL_SERVER_CONF_KEY, "ssl-server.xml"));
    sslConf.addResource(new Path(serverSSLConf.getAbsolutePath()));
    superKeystorePath = sslConf.get(
        FileBasedKeyStoresFactory.resolvePropertyName(SSLFactory.Mode.SERVER,
            FileBasedKeyStoresFactory.SSL_KEYSTORE_LOCATION_TPL_KEY));
    superKeystorePassword = sslConf.get(
        FileBasedKeyStoresFactory.resolvePropertyName(SSLFactory.Mode.SERVER,
            FileBasedKeyStoresFactory.SSL_KEYSTORE_PASSWORD_TPL_KEY));
    superTrustStorePath = sslConf.get(
        FileBasedKeyStoresFactory.resolvePropertyName(SSLFactory.Mode.SERVER,
            FileBasedKeyStoresFactory.SSL_TRUSTSTORE_LOCATION_TPL_KEY));
    superTrustStorePassword = sslConf.get(
        FileBasedKeyStoresFactory.resolvePropertyName(SSLFactory.Mode.SERVER,
            FileBasedKeyStoresFactory.SSL_TRUSTSTORE_PASSWORD_TPL_KEY));
    try {
      superuser = UserGroupInformation.getLoginUser().getUserName();
    } catch (IOException ex) {
      throw new IllegalStateException("Could not identify login user");
    }
  }
  
  public String getSuperKeystorePath() {
    return superKeystorePath;
  }
  
  public String getSuperKeystorePassword() {
    return superKeystorePassword;
  }
  
  public String getSuperTrustStorePath() {
    return superTrustStorePath;
  }
  
  public String getSuperTrustStorePassword() {
    return superTrustStorePassword;
  }
  
  public String getProjectSpecificUserCertPassword(String username)
    throws CryptoPasswordNotFoundException {
    if (username != null) {
      CertificateMaterializer.CryptoMaterial cryptoMaterial;
      Matcher pguMatcher = projectGenericUserPatter.matcher(username);
      if (pguMatcher.matches()) {
        String pguUsername = pguMatcher.group(1);
        cryptoMaterial = certificateMaterializer.getUserMaterial(pguUsername);
      } else if (username.matches(HopsSSLSocketFactory.USERNAME_PATTERN)) {
        String[] project_username = username.split(HdfsUsersController
            .USER_NAME_DELIMITER, 2);
        cryptoMaterial = certificateMaterializer
            .getUserMaterial(project_username[1], project_username[0]);
      } else {
        throw new RuntimeException("User <" + username +"> is neither project" +
            " specific, nor project generic!");
      }
      
      return cryptoMaterial.getPassword();
    }
    
    throw new RuntimeException("Username cannot be null!");
  }
  
  public void configureTlsForProjectSpecificUser(String username, String
      transientDir, Configuration conf) throws CryptoPasswordNotFoundException {
    String password = getProjectSpecificUserCertPassword(username);
    
    String prefix = Paths.get(transientDir, username).toString();
    String kstorePath = prefix + HopsSSLSocketFactory.KEYSTORE_SUFFIX;
    String tstorePath = prefix + HopsSSLSocketFactory.TRUSTSTORE_SUFFIX;
    HopsSSLSocketFactory.setTlsConfiguration(kstorePath, password,
        tstorePath, password, conf);
  }
  
  public void materializeCertsForNonSuperUser(String username) {
    // Make sure it's a normal, non superuser
    if (username != null) {
      Matcher pguMatcher = projectGenericUserPatter.matcher(username);
      if (pguMatcher.matches()) {
        String pguUsername = pguMatcher.group(1);
        try {
          certificateMaterializer.materializeCertificates(pguUsername);
        } catch (IOException ex) {
          throw new RuntimeException("Error while materializing project " +
              "generic user certificates " + ex.getMessage(), ex);
        }
      } else if (username.matches(HopsSSLSocketFactory.USERNAME_PATTERN)) {
        String[] tokens = username.split(HdfsUsersController.USER_NAME_DELIMITER, 2);
        if (tokens.length == 2) {
          try {
            certificateMaterializer.materializeCertificates(tokens[1], tokens[0]);
          } catch (IOException ex) {
            throw new RuntimeException("Error while materializing " +
                "user certificates " + ex.getMessage(), ex);
          }
        }
      } else {
        throw new RuntimeException("User <" + username +"> is neither project" +
            " specific, nor project generic!");
      }
    }
  }
  
  public void removeNonSuperUserCertificate(String username) {
    if (username != null) {
      Matcher pguMatcher = projectGenericUserPatter.matcher(username);
      if (pguMatcher.matches()) {
        String pguUsername = pguMatcher.group(1);
        certificateMaterializer.removeCertificate(pguUsername);
      } else if (username.matches(HopsSSLSocketFactory.USERNAME_PATTERN)) {
        String[] tokens = username.split(HdfsUsersController.USER_NAME_DELIMITER, 2);
        if (tokens.length == 2) {
          certificateMaterializer.removeCertificate(tokens[1], tokens[0]);
        }
      } else {
        throw new RuntimeException("User <" + username +"> is neither project" +
            " specific, nor project generic!");
      }
    }
  }
  
  private void handleMissingConf(String confName, String confDir)
      throws IllegalStateException {
    LOG.log(Level.SEVERE, "Unable to locate {0} in {1}",
        new Object[]{confName, confDir});
    throw new IllegalStateException(
        "Unable to locate " + confName + " in " + confDir);
  }
}
