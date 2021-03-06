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

package io.hops.hopsworks.api.metadata.wscomm.message;

import io.hops.hopsworks.common.dao.metadata.EntityIntf;
import io.hops.hopsworks.common.dao.metadata.Field;
import io.hops.hopsworks.common.dao.metadata.MTable;
import io.hops.hopsworks.common.dao.metadata.Metadata;
import io.hops.hopsworks.common.dao.metadata.RawData;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents a message request for stored metadata filtered by table id and
 * inode id
 */
public class FetchMetadataMessage extends MetadataMessage {

  /**
   * Default constructor. Vital for the class loader
   */
  public FetchMetadataMessage() {
    super();
    this.TYPE = "FetchMetadataMessage";
  }

  /**
   * Used to send custom messages
   *
   * @param sender the message sender
   * @param message the actual message
   */
  public FetchMetadataMessage(String sender, String message) {
    this();
    this.sender = sender;
    this.message = message;
  }

  @Override
  public void init(JsonObject obj) {
    super.init(obj);
  }

  @Override
  public String encode() {
    String value = Json.createObjectBuilder()
            .add("sender", this.sender)
            .add("type", this.TYPE)
            .add("status", this.status)
            .add("message", this.message)
            .build()
            .toString();

    return value;
  }

  @Override
  public void setAction(String action) {
    this.action = action;
  }

  @Override
  public String getAction() {
    return this.action;
  }

  @Override
  public List<EntityIntf> parseSchema() {
    return super.parseSchema();
  }

  @Override
  public String buildSchema(List<EntityIntf> entities) {
    JsonObjectBuilder builder = Json.createObjectBuilder();

    MTable table = (MTable) entities.get(0);
    builder.add("table", table.getName());

    JsonArrayBuilder fields = Json.createArrayBuilder();

    List<Field> f = table.getFields();

    for (Field fi : f) {
      JsonObjectBuilder field = Json.createObjectBuilder();
      field.add("id", fi.getId());
      field.add("name", fi.getName());

      JsonArrayBuilder rd = Json.createArrayBuilder();
      List<RawData> data = fi.getRawData();

      for (RawData raw : data) {
        //JsonObjectBuilder rawdata = Json.createObjectBuilder();
        //rawdata.add("rawid", raw.getId());
        JsonArrayBuilder inner = Json.createArrayBuilder();

        List<Metadata> metadata = raw.getMetadata();
        for (Metadata m : metadata) {
          //load the actual metadata
          rd.add(m.getData());
        }
        //rd.add(inner);
      }
      field.add("data", rd);
      fields.add(field);
    }

    builder.add("fields", fields);

    return builder.build().toString();
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public void setMessage(String msg) {
    this.message = msg;
  }

  @Override
  public String getSender() {
    return this.sender;
  }

  @Override
  public void setSender(String sender) {
    this.sender = sender;
  }

  @Override
  public String getStatus() {
    return this.status;
  }

  @Override
  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "{\"sender\": \"" + this.sender + "\", "
            + "\"type\": \"" + this.TYPE + "\", "
            + "\"status\": \"" + this.status + "\", "
            + "\"action\": \"" + this.action + "\", "
            + "\"message\": \"" + this.message + "\"}";
  }
}
