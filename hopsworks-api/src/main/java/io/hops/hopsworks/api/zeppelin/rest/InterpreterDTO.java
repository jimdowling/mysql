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

package io.hops.hopsworks.api.zeppelin.rest;

import io.hops.hopsworks.api.zeppelin.util.LivyMsg;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.zeppelin.interpreter.InterpreterSetting;

@XmlRootElement
public class InterpreterDTO {

  private String id;
  private String name;
  private String group;
  private List<LivyMsg.Session> sessions;
  private boolean notRunning;
  private boolean defaultInterpreter;

  public InterpreterDTO() {
  }

  public InterpreterDTO(String id, String name, String group, boolean notRunning) {
    this.id = id;
    this.name = name;
    this.group = group;
    this.notRunning = notRunning;
    sessions = new ArrayList<>();
  }

  public InterpreterDTO(InterpreterSetting interpreter, boolean notRunning) {
    this.id = interpreter.getId();
    this.name = interpreter.getName();
    this.group = null;
    this.notRunning = notRunning;
    sessions = new ArrayList<>();
  }

  public InterpreterDTO(InterpreterSetting interpreter, boolean notRunning,
      List<LivyMsg.Session> runningLivySessions) {
    this.id = interpreter.getId();
    this.name = interpreter.getName();
    this.group = null;
    this.notRunning = notRunning;
    sessions = runningLivySessions;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public List<LivyMsg.Session> getSessions() {
    return sessions;
  }

  public void setSessions(List<LivyMsg.Session> sessions) {
    this.sessions = sessions;
  }

  public boolean isNotRunning() {
    return notRunning;
  }

  public void setNotRunning(boolean notRunning) {
    this.notRunning = notRunning;
  }

  public boolean isDefaultInterpreter() {
    return defaultInterpreter;
  }

  public void setDefaultInterpreter(boolean defaultInterpreter) {
    this.defaultInterpreter = defaultInterpreter;
  }

}
