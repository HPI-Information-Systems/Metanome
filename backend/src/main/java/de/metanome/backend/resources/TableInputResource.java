/*
 * Copyright 2014 by the Metanome project
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

import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.TableInput;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("tableInputs")
public class TableInputResource implements Resource<TableInput> {

  /**
   * Stores the TableInput in the database.
   *
   * @return the TableInput
   */
  @POST
  @Path("/store")
  @Produces("application/json")
  @Consumes("application/json")
  @Override
  public TableInput store(TableInput table) {
    try {
      HibernateUtil.store(table);
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return table;
  }

  /**
   * Deletes the TableInput, which has the given id, from the database.
   * @param id the id of the TableInput, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(@PathParam("id") long id) {
    try {
      TableInput tableInput = (TableInput) HibernateUtil.retrieve(TableInput.class, id);
      HibernateUtil.delete(tableInput);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * retrieves a TableInput from the Database
   * @param id the id of the TableInput
   * @return the retrieved TableInput
   */
  @GET
  @Path("/get/{id}")
  @Produces("application/json")
  @Override
  public TableInput get(@PathParam("id") long id) {
    try {
      return (TableInput) HibernateUtil.retrieve(TableInput.class, id);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all TableInputs in the database
   */
  @GET
  @Produces("application/json")
  @Override
  public List<TableInput> getAll() {
    try {
      return HibernateUtil.queryCriteria(TableInput.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Updates a table input in the database.
   * @param tableInput the table input
   * @return the updated table input
   */
  @POST
  @Path("/update")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public TableInput update(TableInput tableInput) {
    try {
      HibernateUtil.update(tableInput);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
    return tableInput;
  }
}


