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

package io.hops.hopsworks.common.dao.user.activity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import io.hops.hopsworks.common.dao.project.Project;
import io.hops.hopsworks.common.dao.user.Users;

@Entity
@Table(name = "hopsworks.activity")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Activity.findAll",
          query = "SELECT u FROM Activity u ORDER BY u.timestamp DESC"),
  @NamedQuery(name = "Activity.findById",
          query = "SELECT u FROM Activity u WHERE u.id = :id"),
  @NamedQuery(name = "Activity.findByFlag",
          query = "SELECT u FROM Activity u WHERE u.flag = :flag"),
  @NamedQuery(name = "Activity.findByActivity",
          query = "SELECT u FROM Activity u WHERE u.activity = :activity"),
  @NamedQuery(name = "Activity.findByUser",
          query
          = "SELECT u FROM Activity u WHERE u.user = :user ORDER BY u.timestamp DESC"),
  @NamedQuery(name = "Activity.findByTimestamp",
          query = "SELECT u FROM Activity u WHERE u.timestamp = :timestamp"),
  @NamedQuery(name = "Activity.findByProject",
          query
          = "SELECT u FROM Activity u WHERE u.project = :project ORDER BY u.timestamp DESC"),
  @NamedQuery(name = "Activity.countAll",
          query = "SELECT COUNT(u) FROM Activity u"),
  @NamedQuery(name = "Activity.countPerProject",
          query
          = "SELECT COUNT(u) FROM Activity u WHERE u.project = :project")})
public class Activity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 5)
  @Column(name = "flag")
  private String flag;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;

  @Size(max = 128)
  @Column(name = "activity")
  private String activity;

  @Basic(optional = false)
  @NotNull
  @Column(name = "created")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timestamp;

  @JoinColumn(name = "project_id",
          referencedColumnName = "id")
  @ManyToOne(optional = false)
  private Project project;

  @JoinColumn(name = "user_id",
          referencedColumnName = "uid")
  @ManyToOne(optional = false)
  private Users user;

  public Activity() {
  }

  public Activity(Integer id) {
    this.id = id;
  }

  public Activity(Integer id, Date timestamp) {
    this.id = id;
    this.timestamp = timestamp;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Activity)) {
      return false;
    }
    Activity other = (Activity) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.
            equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "se.kth.bbc.activity.Activity[ id=" + id + " ]";
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }

}
