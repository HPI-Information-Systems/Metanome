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
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("executions")
public class ExecutionResource implements Resource<Execution> {

  /**
   * Adds an execution to the database.
   * @param execution the execution
   * @return the stored execution
   */
  @POST
  @Path("/store")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public Execution store(Execution execution) {
    try {
      HibernateUtil.store(execution);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }

    return execution;
  }

  /**
   * Deletes the execution, which has the given id, from the database.
   * @param id the id of the execution, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(long id) {
    try {
      Execution execution = (Execution) HibernateUtil.retrieve(Execution.class, id);
      HibernateUtil.delete(execution);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Retrieves an execution from the database.
   *
   * @param id the execution's id
   * @return the execution
   */
  @GET
  @Path("/get/{id}")
  @Produces("application/json")
  @Override
  public Execution get(@PathParam("id") long id) {
    try {
      return (Execution) HibernateUtil.retrieve(Execution.class, id);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all executions in the database
   */
  @Override
  public List<Execution> getAll() {
    List<Execution> executions = null;

    try {
      executions = HibernateUtil.queryCriteria(Execution.class);
    } catch (EntityStorageException e) {
      // Algorithm should implement Entity, so the exception should not occur.
      e.printStackTrace();
    }

    return executions;
  }

  /**
   * Retrieves all executions, which belong to the given algorithm.
   *
   * @param name the name of the algorithm
   * @return all matching executions
   */
  @GET
  @Path("/algorithm/{name}")
  @Produces("application/json")
  public List<Execution> getExecutionsForAlgorithm(@PathParam("name") String name) {
    List<Execution> executions = new ArrayList<>();

    try {
      List<Execution> all = HibernateUtil.queryCriteria(Execution.class);
      // Filter all executions for those, which belong to the requested algorithm
      for (Execution execution : all) {
        if (execution.getAlgorithm().getName().equals(name)) {
          executions.add(execution);
        }
      }
    } catch (EntityStorageException e) {
      // Algorithm should implement Entity, so the exception should not occur.
      e.printStackTrace();
    }

    return executions;
  }

}
