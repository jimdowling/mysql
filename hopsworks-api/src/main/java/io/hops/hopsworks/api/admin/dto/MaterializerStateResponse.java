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

package io.hops.hopsworks.api.admin.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@XmlRootElement
public class MaterializerStateResponse implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private List<CryptoMaterial> materializedState;
  private Set<String> scheduledRemovals;
  
  public MaterializerStateResponse(
      List<CryptoMaterial> materializedState, Set<String> scheduledRemovals) {
    this.materializedState = materializedState;
    this.scheduledRemovals = scheduledRemovals;
  }
  
  public MaterializerStateResponse() {
  }
  
  public List<CryptoMaterial> getMaterializedState() {
    return materializedState;
  }
  
  public void setMaterializedState(
      List<CryptoMaterial> materializedState) {
    this.materializedState = materializedState;
  }
  
  public Set<String> getScheduledRemovals() {
    return scheduledRemovals;
  }
  
  public void setScheduledRemovals(Set<String> scheduledRemovals) {
    this.scheduledRemovals = scheduledRemovals;
  }
  
  public static class CryptoMaterial {
    private String user;
    private Integer references;
    
    public CryptoMaterial(String user, Integer references) {
      this.user = user;
      this.references = references;
    }
    
    public CryptoMaterial() {
    }
    
    public String getUser() {
      return user;
    }
    
    public void setUser(String user) {
      this.user = user;
    }
    
    public Integer getReferences() {
      return references;
    }
    
    public void setReferences(Integer references) {
      this.references = references;
    }
  }
}

