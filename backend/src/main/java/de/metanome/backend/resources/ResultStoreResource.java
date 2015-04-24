/*
 * Copyright 2015 by the Metanome project
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

import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.result_postprocessing.result_store.ResultsStoreHolder;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("result_store")
public class ResultStoreResource {

  /**
   * Returns the count of persisted results of the given type.
   *
   * @param executionID Execution ID of the last execution
   * @param type        The type of the result
   * @return Returns the count of persisted results for given execution and type
   */
  @GET
  @Path("/count/{type}/{executionId}")
  @Produces("application/json")
  public Integer count(@PathParam("type") String type, @PathParam("executionId") long executionID) {
    try {
      return (ResultsStoreHolder.getStore(type)).count(
          executionID);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns all persisted results of the given type.
   *
   * @param executionID Execution ID of the last execution
   * @param type        The type of the result
   * @return Returns all persisted results or null if the results of the wrong execution are
   * persisted
   */
  @GET
  @Path("/getAll/{type}/{executionId}")
  @Produces("application/json")
  public List<Result> getAll(@PathParam("type") String type,
                             @PathParam("executionId") long executionID) {
    try {
      return (List<Result>) ResultsStoreHolder.getStore(type).list(
          executionID);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns a sublist of persisted results sorted in given way
   *
   * @param executionID  Execution ID of the last execution
   * @param type         The type of the result
   * @param sortProperty Name of the sort property
   * @param ascending    Should the sort be performed in ascending or descending manner?
   * @param start        Inclusive start index
   * @param end          Exclusive end index
   * @return Returns a sublist of persisted IND results sorted in given way or null if the results
   * of the wrong execution are persisted
   */
  @GET
  @Path("/getAllFromTo/{type}/{executionId}/{sortProperty}/{sortOrder}/{start}/{end}")
  @Produces("application/json")
  public List<Result> getAllFromTo(@PathParam("type") String type,
                                   @PathParam("executionId") long executionID,
                                   @PathParam("sortProperty") String sortProperty,
                                   @PathParam("sortOrder") boolean ascending,
                                   @PathParam("start") int start,
                                   @PathParam("end") int end) {
    try {
      return (List<Result>) ResultsStoreHolder.getStore(type).subList(
          executionID, sortProperty, ascending, start, end);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
}

