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

package io.hops.hopsworks.admin.user.profile;

import io.hops.hopsworks.admin.lims.ClientSessionState;
import io.hops.hopsworks.admin.lims.MessagesController;
import io.hops.hopsworks.common.dao.user.UserFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import io.hops.hopsworks.common.dao.user.security.Address;
import io.hops.hopsworks.common.dao.user.security.Organization;
import io.hops.hopsworks.common.dao.user.Users;
import io.hops.hopsworks.common.dao.user.security.audit.AccountsAuditActions;
import io.hops.hopsworks.common.dao.user.security.audit.AccountAuditFacade;
import io.hops.hopsworks.common.dao.user.security.audit.UserAuditActions;
import io.hops.hopsworks.common.dao.user.security.audit.Userlogins;
import io.hops.hopsworks.common.user.UsersController;

@ManagedBean
@ViewScoped
public class ProfileManager implements Serializable {

  public static final String DEFAULT_GRAVATAR = "resources/images/icons/default-icon.jpg";

  private static final long serialVersionUID = 1L;
  @EJB
  private UserFacade userFacade;
  @EJB
  protected UsersController usersController;

  @EJB
  private AccountAuditFacade auditManager;

  @ManagedProperty(value = "#{clientSessionState}")
  private ClientSessionState sessionState;

  private Users user;
  private Address address;
  private Userlogins login;
  private Organization organization;

  private boolean editable;

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public Userlogins getLogin() {
    return login;
  }

  public void setLogin(Userlogins login) {
    this.login = login;
  }

  public void setUser(Users user) {
    this.user = user;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Address getAddress() {
    return address;
  }

  public void setSessionState(ClientSessionState sessionState) {
    this.sessionState = sessionState;
  }

  public Users getUser() {
    if (user == null) {
      user = userFacade.findByEmail(sessionState.getLoggedInUsername());
      address = user.getAddress();
      organization = user.getOrganization();
      login = auditManager.getLastUserLogin(user);
    }

    return user;
  }

  public List<String> getCurrentGroups() {
    List<String> list = usersController.getUserRoles(user);
    return list;
  }

  public void updateUserInfo() {
    HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.
        getCurrentInstance().getExternalContext().getRequest();
    try {
      userFacade.update(user);
      MessagesController.addInfoMessage("Success", "Profile updated successfully.");
      auditManager.registerAccountChange(sessionState.getLoggedInUser(), AccountsAuditActions.PROFILEUPDATE.name(),
              UserAuditActions.SUCCESS.name(), "", getUser(), httpServletRequest);
    } catch (RuntimeException ex) {
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update", null);
      FacesContext.getCurrentInstance().addMessage(null, msg);
      auditManager.registerAccountChange(sessionState.getLoggedInUser(), AccountsAuditActions.PROFILEUPDATE.name(),
              UserAuditActions.FAILED.name(), "", getUser(), httpServletRequest);
      return;
    }
  }

  /**
   * Update organization info.
   */
  public void updateUserOrg() {
    HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.
        getCurrentInstance().getExternalContext().getRequest();
    try {
      user.setOrganization(organization);
      userFacade.update(user);
      MessagesController.addInfoMessage("Success", "Profile updated successfully.");
      auditManager.registerAccountChange(sessionState.getLoggedInUser(), AccountsAuditActions.PROFILEUPDATE.name(),
          UserAuditActions.SUCCESS.name(), "Update Organization", getUser(), httpServletRequest);
    } catch (RuntimeException ex) {
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update", null);
      FacesContext.getCurrentInstance().addMessage(null, msg);
      auditManager.registerAccountChange(sessionState.getLoggedInUser(), AccountsAuditActions.PROFILEUPDATE.name(),
          UserAuditActions.FAILED.name(), "Update Organization", getUser(), httpServletRequest);
    }
  }

  /**
   * Update the user address in the profile and register the audit logs.
   */
  public void updateAddress() {
    HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.
        getCurrentInstance().getExternalContext().getRequest();
    try {
      user.setAddress(address);
      userFacade.update(user);
      MessagesController.addInfoMessage("Success", "Address updated successfully.");
      auditManager.registerAccountChange(sessionState.getLoggedInUser(), AccountsAuditActions.PROFILEUPDATE.name(),
          UserAuditActions.SUCCESS.name(), "Update Address", getUser(), httpServletRequest);
    } catch (RuntimeException ex) {
      MessagesController.addSecurityErrorMessage("Update failed.");
      auditManager.registerAccountChange(sessionState.getLoggedInUser(), AccountsAuditActions.PROFILEUPDATE.name(),
          UserAuditActions.FAILED.name(), "Update Address", getUser(), httpServletRequest);
    }
  }

}
