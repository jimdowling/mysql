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

package io.hops.hopsworks.common.dao.certificates;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "projectgenericuser_certs",
        catalog = "hopsworks",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ProjectGenericUserCerts.findAll",
          query = "SELECT s FROM ProjectGenericUserCerts s"),
  @NamedQuery(name = "ProjectGenericUserCerts.findByProjectGenericUsername",
          query
          = "SELECT s FROM ProjectGenericUserCerts s WHERE s.projectGenericUsername = :projectGenericUsername")})

public class ProjectGenericUserCerts implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 20)
  @Column(name = "project_generic_username")
  private String projectGenericUsername;
  @Lob
  @Column(name = "pgu_key")
  private byte[] key;
  @Lob
  @Column(name = "pgu_cert")
  private byte[] cert;
  @NotNull
  @Column(name = "cert_password")
  private String certificatePassword;

  public ProjectGenericUserCerts() {
  }

  public ProjectGenericUserCerts(String projectGenericUsername) {
    this.projectGenericUsername = projectGenericUsername;
  }

  public String getProjectGenericUsername() { return projectGenericUsername; }

  public void setProjectGenericUsername(String projectGenericUsername) {
    this.projectGenericUsername= projectGenericUsername;
  }

  public byte[] getKey() { return key; }

  public void setKey(byte[] key) { this.key = key; }

  public byte[] getCert() { return cert; }

  public void setCert(byte[] cert) { this.cert = cert; }

  public String getCertificatePassword() {
    return certificatePassword;
  }
  
  public void setCertificatePassword(String certificatePassword) {
    this.certificatePassword = certificatePassword;
  }
  
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (projectGenericUsername != null ? projectGenericUsername.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ProjectGenericUserCerts)) {
      return false;
    }
    ProjectGenericUserCerts other = (ProjectGenericUserCerts) object;
    if ((this.projectGenericUsername == null && other.projectGenericUsername != null)
            || (this.projectGenericUsername != null && !this.projectGenericUsername.equals(
                    other.projectGenericUsername))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "se.kth.hopsworks.certificates.ProjectGenericUserCerts[ projectGenericUsername="
            + projectGenericUsername + " ]";
  }

}
