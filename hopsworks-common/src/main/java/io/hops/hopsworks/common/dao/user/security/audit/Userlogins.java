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

package io.hops.hopsworks.common.dao.user.security.audit;

import io.hops.hopsworks.common.dao.user.Users;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "hopsworks.userlogins")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Userlogins.findAll",
          query = "SELECT u FROM Userlogins u"),
  @NamedQuery(name = "Userlogins.findByLoginId",
          query = "SELECT u FROM Userlogins u WHERE u.loginId = :loginId"),
  @NamedQuery(name = "Userlogins.findByIp",
          query = "SELECT u FROM Userlogins u WHERE u.ip = :ip"),
  @NamedQuery(name = "Userlogins.findByUserAgent",
          query = "SELECT u FROM Userlogins u WHERE u.userAgent = :userAgent"),
  @NamedQuery(name = "Userlogins.findByAction",
          query = "SELECT u FROM Userlogins u WHERE u.action = :action"),
  @NamedQuery(name = "Userlogins.findByOutcome",
          query = "SELECT u FROM Userlogins u WHERE u.outcome = :outcome"),
  @NamedQuery(name = "Userlogins.findByUser",
          query = "SELECT u FROM Userlogins u WHERE u.user = :user"),
  @NamedQuery(name = "Userlogins.findByLoginDate",
          query = "SELECT u FROM Userlogins u WHERE u.loginDate = :loginDate"),
  @NamedQuery(name = "Userlogins.findUserLast",
    query = "SELECT u FROM Userlogins u WHERE u.user = :user ORDER BY u.loginDate DESC")})
public class Userlogins implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "login_id")
  private Long loginId;
  @Size(max = 16)
  @Column(name = "ip")
  private String ip;
  @Size(max = 255)
  @Column(name = "useragent")
  private String userAgent;
  @Size(max = 80)
  @Column(name = "action")
  private String action;
  @Size(max = 20)
  @Column(name = "outcome")
  private String outcome;
  @JoinColumn(name = "uid",
          referencedColumnName = "uid")
  @ManyToOne
  private Users user;
  @Column(name = "login_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date loginDate;

  public Userlogins() {
  }

  public Userlogins(String ip, String userAgent, Users user,
                    String action, String outcome, Date loginDate) {
    this.ip = ip;
    this.userAgent = userAgent;
    this.action = action;
    this.outcome = outcome;
    this.user = user;
    this.loginDate = loginDate;
  }

  public Userlogins(Long loginId) { this.loginId = loginId; }

  public Long getLoginId() { return loginId; }

  public void setLoginId(Long loginId) { this.loginId = loginId; }

  public String getIp() { return ip; }

  public void setIp(String ip) { this.ip = ip; }

  public String getUserAgent() { return userAgent; }

  public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

  public String getAction() { return action; }

  public void setAction(String action) { this.action = action; }

  public String getOutcome() { return outcome; }

  public void setOutcome(String outcome) { this.outcome = outcome; }

  public Users getUser() { return user; }

  public void setUser(Users user) { this.user = user; }

  public Date getLoginDate() { return loginDate; }

  public void setLoginDate(Date loginDate) { this.loginDate = loginDate; }


  @Override
  public int hashCode() {
    int hash = 0;
    hash += (loginId != null ? loginId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Userlogins)) {
      return false;
    }
    Userlogins other = (Userlogins) object;
    if ((this.loginId == null && other.loginId != null) || (this.loginId != null
            && !this.loginId.equals(other.loginId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Userlogins{" +
        "loginId=" + loginId +
        ", ip='" + ip + '\'' +
        ", userAgent='" + userAgent + '\'' +
        ", action='" + action + '\'' +
        ", outcome='" + outcome + '\'' +
        ", user=" + user +
        ", loginDate=" + loginDate +
        '}';
  }
}
