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
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class UserCertsPK implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 100)
  @Column(name = "projectname")
  private String projectname;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 10)
  @Column(name = "username")
  private String username;

  public UserCertsPK() {
  }

  public UserCertsPK(String projectname, String username) {
    this.projectname = projectname;
    this.username = username;
  }

  public String getProjectname() {
    return projectname;
  }

  public void setProjectname(String projectname) {
    this.projectname = projectname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (projectname != null ? projectname.hashCode() : 0);
    hash += (username != null ? username.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof UserCertsPK)) {
      return false;
    }
    UserCertsPK other = (UserCertsPK) object;
    if ((this.projectname == null && other.projectname != null)
            || (this.projectname != null && !this.projectname.equals(
                    other.projectname))) {
      return false;
    }
    if (this.username != other.username) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "se.kth.hopsworks.certificates.UserCertsPK[ projectname="
            + projectname + ", username=" + username + " ]";
  }

}
