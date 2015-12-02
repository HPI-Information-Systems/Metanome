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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.result_postprocessing.ResultPostProcessor;
import de.metanome.backend.result_postprocessing.result_store.ResultsStoreHolder;
import de.metanome.backend.result_postprocessing.results.RankingResult;
import de.metanome.backend.results_db.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Path("result-store")
public class ResultStoreResource {

  /**
   * Returns the count of persisted results of the given type.
   *
   * @param type The type of the result
   * @return Returns the count of persisted results for given type
   */
  @GET
  @Path("/count/{type}")
  @Produces("application/json")
  public Integer count(@PathParam("type") String type) {
    try {
      return (ResultsStoreHolder.getStore(type)).count();
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns a sublist of persisted results sorted in given way
   *
   * @param type         The type of the result
   * @param sortProperty Name of the sort property
   * @param ascending    Should the sort be performed in ascending or descending manner?
   * @param start        Inclusive start index
   * @param end          Exclusive end index
   * @return Returns a sublist of persisted results sorted in given way
   */
  @GET
  @Path("/get-from-to/{type}/{sortProperty}/{sortOrder}/{start}/{end}")
  @Produces("application/json")
  @SuppressWarnings("unchecked")
  public List<RankingResult> getAllFromTo(@PathParam("type") String type,
                                          @PathParam("sortProperty") String sortProperty,
                                          @PathParam("sortOrder") boolean ascending,
                                          @PathParam("start") int start,
                                          @PathParam("end") int end) {
    try {
      return (List<RankingResult>) ResultsStoreHolder.getStore(type).subList(
        sortProperty, ascending, start, end);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }


  /**
   * Loads the results of the given execution into the result store.
   *
   * @param id Execution id of the execution
   */
  @POST
  @Path("/load-execution/{executionId}/{dataIndependent}")
  public void loadExecution(@PathParam("executionId") long id,
                            @PathParam("dataIndependent") boolean dataIndependent) {
    try {
      Execution execution = (Execution) HibernateUtil.retrieve(Execution.class, id);
      if (dataIndependent) {
        ResultPostProcessor.extractAndStoreResultsDataIndependent(execution);
      } else {
        ResultPostProcessor.extractAndStoreResultsDataDependent(execution);
      }
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Loads the results, which belong to the given file input, to the result store.
   *
   * @param id the id of the file input
   */
  @GET
  @Path("/load-results/{id}/{dataIndependent}")
  @Produces("application/json")
  public List<String> loadResults(@PathParam("id") long id,
                                  @PathParam("dataIndependent") boolean dataIndependent) {
    try {
      FileInput fileInput = (FileInput) HibernateUtil.retrieve(FileInput.class, id);
      Set<de.metanome.backend.results_db.Result> results = getResults(fileInput);
      Collection<Input> inputs = new ArrayList<>();
      inputs.add(fileInput);

      if (dataIndependent) {
        ResultPostProcessor.extractAndStoreResultsDataIndependent(results, inputs);
      } else {
        ResultPostProcessor.extractAndStoreResultsDataDependent(results, inputs);
      }
      return getTypes(results);
    } catch (EntityStorageException | IOException | AlgorithmConfigurationException |
      InputIterationException | InputGenerationException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  private List<String> getTypes(Set<de.metanome.backend.results_db.Result> results) {
    List<String> types = new ArrayList<>();
    for (de.metanome.backend.results_db.Result result : results) {
      types.add(result.getType().getName());
    }
    return types;
  }

  /**
   * @param input file input
   * @return set of results, which belong to the given file input
   */
  @SuppressWarnings("unchecked")
  protected Set<de.metanome.backend.results_db.Result> getResults(FileInput input)
    throws EntityStorageException {
    Set<de.metanome.backend.results_db.Result> results = new HashSet<>();
    List<ResultType> types = new ArrayList<>();
    List<Execution> all = (List<Execution>) HibernateUtil.queryCriteria(Execution.class);

    // Filter all executions for those, which belong to the requested file input
    for (Execution execution : all) {
      if (execution.getInputs().contains(input)) {
        for (de.metanome.backend.results_db.Result result : execution.getResults()) {
          if (!types.contains(result.getType())) {
            results.add(result);
            types.add(result.getType());
          }
        }
      }
    }

    return results;
  }
}

