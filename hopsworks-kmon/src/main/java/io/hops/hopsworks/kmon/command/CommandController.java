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

package io.hops.hopsworks.kmon.command;

import io.hops.hopsworks.common.dao.command.CommandEJB;
import io.hops.hopsworks.common.dao.command.Command;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class CommandController {

  @EJB
  private CommandEJB commandEJB;
  @ManagedProperty("#{param.hostname}")
  private String hostname;
  @ManagedProperty("#{param.service}")
  private String service;
  @ManagedProperty("#{param.group}")
  private String group;
  @ManagedProperty("#{param.cluster}")
  private String cluster;
  private static final Logger logger = Logger.getLogger(CommandController.class.
          getName());

  public CommandController() {
  }

  @PostConstruct
  public void init() {
    logger.info("init CommandController");
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public void setCluster(String cluster) {
    this.cluster = cluster;
  }

  public String getCluster() {
    return cluster;
  }

  public List<Command> getRecentCommandsByCluster() {
    List<Command> commands = commandEJB.findRecentByCluster(cluster);
    return commands;
  }

  public List<Command> getRunningCommandsByCluster() {
    List<Command> commands = commandEJB.findRunningByCluster(cluster);
    return commands;
  }

  public List<Command> getRecentCommandsByClusterService() {
    List<Command> commands = commandEJB.findRecentByClusterService(cluster,
            group);
    return commands;
  }

  public List<Command> getRunningCommandsByClusterService() {
    List<Command> commands = commandEJB.findRunningByClusterService(cluster,
            group);
    return commands;
  }

  public List<Command> getRecentCommandsByInstance() {
    List<Command> commands = commandEJB.findRecentByClusterService(cluster,
            group);
    return commands;
  }
}
