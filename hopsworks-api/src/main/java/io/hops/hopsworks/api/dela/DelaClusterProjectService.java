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

package io.hops.hopsworks.api.dela;

import io.hops.hopsworks.api.dela.dto.InodeIdDTO;
import io.hops.hopsworks.api.filter.AllowedProjectRoles;
import io.hops.hopsworks.api.filter.NoCacheResponse;
import io.hops.hopsworks.api.util.JsonResponse;
import io.hops.hopsworks.common.dao.dataset.Dataset;
import io.hops.hopsworks.common.dao.dataset.DatasetFacade;
import io.hops.hopsworks.common.dao.hdfs.inode.Inode;
import io.hops.hopsworks.common.dao.hdfs.inode.InodeFacade;
import io.hops.hopsworks.common.dao.project.Project;
import io.hops.hopsworks.common.dao.project.ProjectFacade;
import io.hops.hopsworks.dela.cluster.ClusterDatasetController;
import io.hops.hopsworks.dela.exception.ThirdPartyException;
import io.swagger.annotations.Api;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RolesAllowed({"HOPS_ADMIN", "HOPS_USER"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@TransactionAttribute(TransactionAttributeType.NEVER)
@Api(value = "Dela Cluster Project Service",
  description = "Dela Cluster Project Service")
public class DelaClusterProjectService {
  private final static Logger logger = Logger.getLogger(DelaClusterProjectService.class.getName());
  
  @EJB
  private NoCacheResponse noCacheResponse;
  @EJB
  private ProjectFacade projectFacade;
  @EJB
  private InodeFacade inodeFacade;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private ClusterDatasetController clusterCtrl;
  
  private Project project;
  private Integer projectId;
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @AllowedProjectRoles({AllowedProjectRoles.DATA_OWNER})
  public Response share(@Context SecurityContext sc, InodeIdDTO inodeId) throws ThirdPartyException {
    Inode inode = getInode(inodeId.getId());
    Dataset dataset = getDatasetByInode(inode);
    clusterCtrl.shareWithCluster(dataset);
    JsonResponse json = new JsonResponse();
    json.setSuccessMessage("Dataset transfer is started - published");
    return successResponse(json);
  }
  
  @DELETE
  @Path("/{inodeId}")
  @Produces(MediaType.APPLICATION_JSON)
  @AllowedProjectRoles({AllowedProjectRoles.DATA_OWNER})
  public Response removePublic(@Context SecurityContext sc, @PathParam("inodeId") Integer inodeId) 
    throws ThirdPartyException {
    Inode inode = getInode(inodeId);
    Dataset dataset = getDatasetByInode(inode);
    clusterCtrl.unshareFromCluster(dataset);
    JsonResponse json = new JsonResponse();
    json.setSuccessMessage("Dataset is now private");
    return successResponse(json);
  }
  
  private Inode getInode(Integer inodeId) throws ThirdPartyException {
    if (inodeId == null) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), "inode not found",
        ThirdPartyException.Source.LOCAL, "bad request");
    }
    Inode inode = inodeFacade.findById(inodeId);
    if (inode == null) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), "inode not found",
        ThirdPartyException.Source.LOCAL, "bad request");
    }
    return inode;
  }
  
  private Dataset getDatasetByInode(Inode inode) throws ThirdPartyException {
    Dataset dataset = datasetFacade.findByProjectAndInode(this.project, inode);
    if (dataset == null) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), "dataset not found",
        ThirdPartyException.Source.LOCAL, "bad request");
    }
    return dataset;
  }
  
  private Response successResponse(Object content) {
    return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(content).build();
  }
  
  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
    this.project = projectFacade.find(projectId);
  }

  public Integer getProjectId() {
    return projectId;
  }
}
