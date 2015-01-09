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


import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.algorithm_execution.AlgorithmExecutor;
import de.metanome.backend.algorithm_execution.ProgressCache;
import de.metanome.backend.algorithm_execution.TempFileGenerator;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.result_receiver.ResultPrinter;
import de.metanome.backend.result_receiver.ResultsCache;
import de.metanome.backend.result_receiver.ResultsHub;
import de.metanome.backend.results_db.Algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("algorithm_execution")
public class AlgorithmExecutionResource {

  /**
   * Executes an algorithm.
   * @param params all parameters to execute the algorithm
   * @return the execution time
   */
  @POST
  @Consumes("application/json")
  @Produces("application/json")
  public long executeAlgorithm(AlgorithmExecutionParams params) {
    AlgorithmExecutor executor;

    try {
      executor = buildExecutor(params.executionIdentifier);
    } catch (FileNotFoundException e) {
      throw new WebException("Could not generate result file", Response.Status.BAD_REQUEST);
    } catch (UnsupportedEncodingException e) {
      throw new WebException("Could not build temporary file generator", Response.Status.BAD_REQUEST);
    }

    AlgorithmResource algorithmResource = new AlgorithmResource();
    Algorithm algorithm = algorithmResource.get(params.algorithmId);

    long executionTime = 0;
    try {
      executionTime = executor.executeAlgorithm(algorithm, params.requirements);
    } catch (AlgorithmLoadingException | AlgorithmExecutionException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }

    try {
      executor.close();
    } catch (IOException e) {
      throw new WebException("Could not close algorithm executor", Response.Status.BAD_REQUEST);
    }

    return executionTime;
  }

  @GET
  @Path("/fetch_results/{identifier}")
  @Produces("application/json")
  public List<Result> fetchNewResults(@PathParam("identifier") String executionIdentifier) {
    try {
      return AlgorithmExecutionCache.getResultsCache(executionIdentifier).getNewResults();
    } catch (Exception e) {
      throw new WebException("Could not find any results to the given identifier",
                             Response.Status.BAD_REQUEST);
    }
  }

  @GET
  @Path("/fetch_progress/{identifier}")
  @Produces("application/json")
  public float fetchProgress(@PathParam("identifier") String executionIdentifier) {
    try {
      return AlgorithmExecutionCache.getProgressCache(executionIdentifier).getProgress();
    } catch (Exception e) {
      throw new WebException("Could not find any progress to the given identifier",
                             Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Builds an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor} with stacked {@link de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver}s to write
   * result files and cache results for the frontend.
   *
   * @param executionIdentifier the identifier associated with this execution
   * @return an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor}
   * @throws java.io.FileNotFoundException        when the result files cannot be opened
   * @throws java.io.UnsupportedEncodingException when the temp files cannot be opened
   */
  protected AlgorithmExecutor buildExecutor(String executionIdentifier)
      throws FileNotFoundException, UnsupportedEncodingException {
    ResultPrinter resultPrinter = new ResultPrinter(executionIdentifier, "results");
    ResultsCache resultsCache = new ResultsCache();
    ResultsHub resultsHub = new ResultsHub();
    resultsHub.addSubscriber(resultPrinter);
    resultsHub.addSubscriber(resultsCache);

    FileGenerator fileGenerator = new TempFileGenerator();
    ProgressCache progressCache = new ProgressCache();

    AlgorithmExecutor executor = new AlgorithmExecutor(resultsHub, progressCache, fileGenerator);
    AlgorithmExecutionCache.add(executionIdentifier, resultsCache, progressCache);

    return executor;
  }

}
