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

package io.hops.hopsworks.common.jobs.tensorflow;

import com.google.common.base.Strings;
import javax.xml.bind.annotation.XmlRootElement;
import io.hops.hopsworks.common.jobs.MutableJsonObject;
import io.hops.hopsworks.common.jobs.jobhistory.JobType;
import io.hops.hopsworks.common.jobs.yarn.YarnJobConfiguration;

/**
 * Contains Spark-specific run information for a Spark job, on top of Yarn
 * configuration.
 */
@XmlRootElement
public class TensorFlowJobConfiguration extends YarnJobConfiguration {

  private String appPath;
  private String args;
  private int numOfGPUs = 0;
  private int numOfPs = 0;
  private int numOfWorkers = 2;
  private int workerMemory = 1024;
  private int workerVCores = 1;

  private String anacondaDir;

  protected static final String KEY_APP_PATH = "JARPATH";
  protected static final String KEY_MAINCLASS = "MAINCLASS";
  protected static final String KEY_ARGS = "ARGS";
  protected static final String KEY_NUM_PS = "NUM_PS";
  protected static final String KEY_NUM_WORKERS = "NUM_WORKERS";
  protected static final String KEY_WORKER_MEMORY = "NUM_WORKER_MEMORY";
  protected static final String KEY_WORKER_VCORES = "NUM_WORKER_VCORES";
  protected static final String KEY_GPUS = "NUM_GPUS";

  protected static final String KEY_PYTHON_DIR = "PYTHON";

  public TensorFlowJobConfiguration() {
    super();
  }

  public String getAppPath() {
    return appPath;
  }

  /**
   * Set the path to the main executable jar. No default value.
   * <p/>
   * @param appPath
   */
  public void setAppPath(String appPath) {
    this.appPath = appPath;
  }

  public String getArgs() {
    return args;
  }

  /**
   * Set the arguments to be passed to the job. No default value.
   * <p/>
   * @param args
   */
  public void setArgs(String args) {
    this.args = args;
  }

  public String getAnacondaDir() {
    return anacondaDir;
  }

  public void setAnacondaDir(String anacondaDir) {
    this.anacondaDir = anacondaDir;
  }

  public int getNumOfGPUs() {
    return numOfGPUs;
  }

  public void setNumOfGPUs(int numOfGPUs) {
    this.numOfGPUs = numOfGPUs;
  }

  public int getNumOfPs() {
    return numOfPs;
  }

  public void setNumOfPs(int numOfPs) {
    this.numOfPs = numOfPs;
  }

  public int getNumOfWorkers() {
    return numOfWorkers;
  }

  public void setNumOfWorkers(int numOfWorkers) {
    this.numOfWorkers = numOfWorkers;
  }

  public int getWorkerMemory() {
    return workerMemory;
  }

  public void setWorkerMemory(int workerMemory) {
    this.workerMemory = workerMemory;
  }

  public int getWorkerVCores() {
    return workerVCores;
  }

  public void setWorkerVCores(int workerVCores) {
    this.workerVCores = workerVCores;
  }

  @Override
  public JobType getType() {
    return JobType.TENSORFLOW;
  }

  @Override
  public MutableJsonObject getReducedJsonObject() {
    MutableJsonObject obj = super.getReducedJsonObject();
    //First: fields that are possibly null or empty:
    if (!Strings.isNullOrEmpty(args)) {
      obj.set(KEY_ARGS, args);
    }

    if (!Strings.isNullOrEmpty(appPath)) {
      obj.set(KEY_APP_PATH, appPath);
    }
    obj.set(KEY_NUM_PS, "" + numOfPs);
    obj.set(KEY_NUM_WORKERS, "" + numOfWorkers);
    obj.set(KEY_WORKER_MEMORY, "" + workerMemory);
    obj.set(KEY_WORKER_VCORES, "" + workerVCores);
    obj.set(KEY_GPUS, "" + numOfGPUs);
    obj.set(KEY_TYPE, JobType.TENSORFLOW.name());
    obj.set(KEY_PYTHON_DIR, getAnacondaDir() + "/bin/python");
    return obj;
  }

  @Override
  public void updateFromJson(MutableJsonObject json) throws
      IllegalArgumentException {
    //First: make sure the given object is valid by getting the type and AdamCommandDTO
    JobType type;
    String jsonArgs, jsonApppath, jsonNumPs, jsonNumWorkers, jsonWorkerMemory, jsonWorkerVCores, jsonGPUs;
    try {
      String jsonType = json.getString(KEY_TYPE);
      type = JobType.valueOf(jsonType);
      if (type != JobType.TENSORFLOW) {
        throw new IllegalArgumentException("JobType must be:" + JobType.TENSORFLOW);
      }
      //First: fields that can be null or empty
      jsonArgs = json.getString(KEY_ARGS, null);
      jsonApppath = json.getString(KEY_APP_PATH, null);
      jsonNumPs = json.getString(KEY_NUM_PS);
      jsonNumWorkers = json.getString(KEY_NUM_WORKERS);
      jsonWorkerMemory = json.getString(KEY_WORKER_MEMORY);
      jsonWorkerVCores = json.getString(KEY_WORKER_VCORES);
      jsonGPUs = json.getString(KEY_GPUS);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Cannot convert object into TensorFlowJobConfiguration.", e);
    }
    //Second: allow all superclasses to check validity. To do this: make sure that 
    //the type will get recognized correctly.
    json.set(KEY_TYPE, JobType.YARN.name());
    super.updateFromJson(json);
    //Third: we're now sure everything is valid: actually update the state
    this.args = jsonArgs;
    this.appPath = jsonApppath;
    this.numOfPs = Integer.parseInt(jsonNumPs);
    this.numOfWorkers = Integer.parseInt(jsonNumWorkers);
    this.workerMemory = Integer.parseInt(jsonWorkerMemory);
    this.workerVCores = Integer.parseInt(jsonWorkerVCores);
    this.numOfGPUs = Integer.parseInt(jsonGPUs);
  }

}
