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

package io.hops.hopsworks.dela.old_dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HDFSEndpoint {

  private String xmlPath;
  private String user;

  public HDFSEndpoint() {
  }

  public HDFSEndpoint(String xmlPath, String user) {
    this.xmlPath = xmlPath;
    this.user = user;
  }

  public String getXmlPath() {
    return xmlPath;
  }

  public void setXmlPath(String xmlPath) {
    this.xmlPath = xmlPath;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "HDFSEndpoint{" + "xmlPath=" + xmlPath + ", user=" + user + '}';
  }

}
