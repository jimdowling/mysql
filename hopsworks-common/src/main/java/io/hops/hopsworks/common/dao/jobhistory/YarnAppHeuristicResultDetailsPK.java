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

package io.hops.hopsworks.common.dao.jobhistory;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class YarnAppHeuristicResultDetailsPK implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Column(name = "yarn_app_heuristic_result_id")
  private int yarnAppHeuristicResultId;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 128)
  @Column(name = "name")
  private String name;

  public YarnAppHeuristicResultDetailsPK() {
  }

  public YarnAppHeuristicResultDetailsPK(int yarnAppHeuristicResultId,
          String name) {
    this.yarnAppHeuristicResultId = yarnAppHeuristicResultId;
    this.name = name;
  }

  public int getYarnAppHeuristicResultId() {
    return yarnAppHeuristicResultId;
  }

  public void setYarnAppHeuristicResultId(int yarnAppHeuristicResultId) {
    this.yarnAppHeuristicResultId = yarnAppHeuristicResultId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) yarnAppHeuristicResultId;
    hash += (name != null ? name.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof YarnAppHeuristicResultDetailsPK)) {
      return false;
    }
    YarnAppHeuristicResultDetailsPK other
            = (YarnAppHeuristicResultDetailsPK) object;
    if (this.yarnAppHeuristicResultId != other.yarnAppHeuristicResultId) {
      return false;
    }
    if ((this.name == null && other.name != null) || (this.name != null
            && !this.name.equals(other.name))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "se.kth.bbc.jobs.jobhistory.YarnAppHeuristicResultDetailsPK[ yarnAppHeuristicResultId="
            + yarnAppHeuristicResultId + ", name=" + name + " ]";
  }

}
