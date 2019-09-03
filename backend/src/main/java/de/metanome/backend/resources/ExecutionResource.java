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
import de.metanome.backend.result_receiver.ResultReader;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Result;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.*;

@Path("executions")
public class ExecutionResource {

  /**
   * Deletes the execution, which has the given id, from the database.
   *
   * @param id the id of the execution, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  public void delete(@PathParam("id") long id) {
    try {
      Execution execution = (Execution) HibernateUtil.retrieve(Execution.class, id);
      Set<Result> results = execution.getResults();
      HibernateUtil.delete(execution);
      // delete result files from disk
      for (Result result : results) {
        File file = new File(result.getFileName());
        if (file.exists()) {
          file.delete();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
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
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  public Execution get(@PathParam("id") long id) {
    try {
      return (Execution) HibernateUtil.retrieve(Execution.class, id);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all executions in the database
   */
  @GET
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public List<Execution> getAll() {
    try {
      List<Execution> results = (List<Execution>) HibernateUtil.queryCriteria(Execution.class);
      for (Execution result : results) result.setResults(new HashSet<>());
      return results;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Reads counter results from file.
   *
   * @param id the execution's id
   * @return the updated result
   */
  @GET
  @Path("/count-results/{executionId}")
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  public Map<String, Integer> readCounterResult(@PathParam("executionId") long id) {
    try {
      Map<String, Integer> results = new HashMap<>();
      Execution execution = (Execution) HibernateUtil.retrieve(Execution.class, id);

      for (Result result : execution.getResults()) {
        results.put(result.getType().getName(),
          ResultReader.readCounterResultFromFile(result.getFileName()));
      }
      return results;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

}
