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

package io.hops.hopsworks.dela;

import io.hops.hopsworks.common.dao.dataset.Dataset;
import io.hops.hopsworks.common.dao.dataset.DatasetPermissions;
import io.hops.hopsworks.common.dao.dataset.DatasetFacade;
import io.hops.hopsworks.common.dao.log.operation.OperationType;
import io.hops.hopsworks.common.dao.project.Project;
import io.hops.hopsworks.common.dao.user.Users;
import io.hops.hopsworks.common.dataset.DatasetController;
import io.hops.hopsworks.common.exception.AppException;
import io.hops.hopsworks.common.hdfs.DistributedFsService;
import io.hops.hopsworks.common.hdfs.HdfsUsersController;
import io.hops.hopsworks.dela.exception.ThirdPartyException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;
import org.apache.hadoop.fs.Path;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class DelaDatasetController {

  private Logger logger = Logger.getLogger(DelaDatasetController.class.getName());

  @EJB
  private DatasetController datasetCtrl;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private HdfsUsersController hdfsUsersBean;
  @EJB
  private DistributedFsService dfs;

  public Dataset uploadToHops(Dataset dataset, String publicDSId) {
    dataset.setPublicDsState(Dataset.SharedState.HOPS);
    dataset.setPublicDsId(publicDSId);
    dataset.setEditable(DatasetPermissions.OWNER_ONLY);
    datasetFacade.merge(dataset);
    datasetCtrl.logDataset(dataset, OperationType.Update);
    return dataset;
  }
  
  public Dataset unshareFromHops(Dataset dataset) {
    dataset.setPublicDsState(Dataset.SharedState.PRIVATE);
    dataset.setPublicDsId(null);
    dataset.setEditable(DatasetPermissions.GROUP_WRITABLE_SB);
    datasetFacade.merge(dataset);
    datasetCtrl.logDataset(dataset, OperationType.Update);
    return dataset;
  }
  
  public Dataset download(Project project, Users user, String publicDSId, String name)
    throws ThirdPartyException {
    Dataset dataset;
    try {
      dataset = createDataset(user, project, name, "");
    } catch (IOException | AppException e) {
      throw new ThirdPartyException(Response.Status.EXPECTATION_FAILED.getStatusCode(), e.getMessage(),
        ThirdPartyException.Source.LOCAL, "");
    }
    dataset.setPublicDsState(Dataset.SharedState.HOPS);
    dataset.setPublicDsId(publicDSId);
    dataset.setEditable(DatasetPermissions.OWNER_ONLY);
    datasetFacade.merge(dataset);
    datasetCtrl.logDataset(dataset, OperationType.Update);
    return dataset;
  }

  public Dataset updateDescription(Dataset dataset, String description) {
    dataset.setDescription(description);
    datasetFacade.merge(dataset);
    datasetCtrl.logDataset(dataset, OperationType.Update);
    return dataset;
  }

  public void delete(Project project, Dataset dataset) throws ThirdPartyException {
    if (dataset.isShared()) {
      //remove the entry in the table that represents shared ds
      //but leave the dataset in hdfs b/c the user does not have the right to delete it.
      hdfsUsersBean.unShareDataset(project, dataset);
      return;
    }
    try {
      Path path = datasetCtrl.getDatasetPath(dataset);
      boolean result = datasetCtrl.deleteDatasetDir(dataset, path, dfs.getDfsOps());
      if (!result) {
        throw new ThirdPartyException(Response.Status.EXPECTATION_FAILED.getStatusCode(), "dataset delete",
          ThirdPartyException.Source.LOCAL, "exception");
      }
    } catch (IOException ex) {
      throw new ThirdPartyException(Response.Status.EXPECTATION_FAILED.getStatusCode(), "dataset delete",
        ThirdPartyException.Source.LOCAL, "exception");
    }
  }

  public Dataset createDataset(Users user, Project project, String name, String description)
    throws IOException, AppException {
    int templateId = -1;
    boolean defaultDataset = false;
    boolean searchable = true;

    datasetCtrl.createDataset(user, project, name, description, templateId, searchable, defaultDataset, 
      dfs.getDfsOps());
    Dataset dataset = datasetFacade.findByNameAndProjectId(project, name);
    return dataset;
  }
  
  public List<Dataset> getLocalPublicDatasets() {
    return datasetFacade.findAllDatasetsByState(Dataset.SharedState.HOPS.state, false);
  }
}
