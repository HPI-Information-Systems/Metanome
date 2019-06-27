/**
 * Copyright 2014-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.metanome.backend.resources;

import de.metanome.backend.constants.Constants;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.TableInput;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("table-inputs")
public class TableInputResource implements Resource<TableInput> {

  /**
   * Stores the TableInput in the database.
   *
   * @return the TableInput
   */
  @POST
  @Path(Constants.STORE_RESOURCE_PATH)
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Consumes(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Override
  public TableInput store(TableInput table) {
    try {
      HibernateUtil.store(table);
      return table;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Deletes the TableInput, which has the given id, from the database.
   *
   * @param id the id of the TableInput, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(@PathParam("id") long id) {
    try {
      TableInput tableInput = (TableInput) HibernateUtil.retrieve(TableInput.class, id);
      HibernateUtil.delete(tableInput);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * retrieves a TableInput from the Database
   *
   * @param id the id of the TableInput
   * @return the retrieved TableInput
   */
  @GET
  @Path("/get/{id}")
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Override
  public TableInput get(@PathParam("id") long id) {
    try {
      return (TableInput) HibernateUtil.retrieve(TableInput.class, id);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all TableInputs in the database
   */
  @GET
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  @Override
  public List<TableInput> getAll() {
    try {
      return (List<TableInput>) HibernateUtil.queryCriteria(TableInput.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Updates a table input in the database.
   *
   * @param tableInput the table input
   * @return the updated table input
   */
  @POST
  @Path("/update")
  @Consumes(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Override
  public TableInput update(TableInput tableInput) {
    try {
      HibernateUtil.update(tableInput);
      return tableInput;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
}


