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

package de.metanome.frontend.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.algorithm_execution.AlgorithmExecutor;
import de.metanome.backend.algorithm_execution.ProgressCache;
import de.metanome.backend.algorithm_execution.TempFileGenerator;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.result_receiver.ResultPrinter;
import de.metanome.backend.result_receiver.ResultsCache;
import de.metanome.backend.result_receiver.ResultsHub;
import de.metanome.frontend.client.services.ExecutionService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Service Implementation for service that triggers algorithm execution
 */
public class ExecutionServiceImpl extends RemoteServiceServlet implements ExecutionService {

  private static final long serialVersionUID = -2758103927345131933L;

  protected HashMap<String, ResultsCache> currentResultReceiver = new HashMap<>();
  protected HashMap<String, ProgressCache> currentProgressCaches = new HashMap<>();

  /**
   * Builds an {@link AlgorithmExecutor} with stacked {@link OmniscientResultReceiver}s to write
   * result files and cache results for the frontend.
   *
   * @param executionIdentifier the identifier associated with this execution
   * @return an {@link AlgorithmExecutor}
   * @throws FileNotFoundException        when the result files cannot be opened
   * @throws UnsupportedEncodingException when the temp files cannot be opened
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
    currentResultReceiver.put(executionIdentifier, resultsCache);
    currentProgressCaches.put(executionIdentifier, progressCache);
    return executor;
  }

  @Override
  public long executeAlgorithm(String algorithmFileName, String executionIdentifier,
                               List<ConfigurationRequirement> parameters)
      throws AlgorithmLoadingException,
             AlgorithmExecutionException {
    AlgorithmExecutor executor;

    try {
      executor = buildExecutor(executionIdentifier);
    } catch (FileNotFoundException e) {
      throw new AlgorithmExecutionException("Could not generate result file (" + e.getMessage() + ").", e);
    } catch (UnsupportedEncodingException e) {
      throw new AlgorithmExecutionException("Could not build temporary file generator (" + e.getMessage() + ").", e);
    }
    long executionTime = executor.executeAlgorithm(algorithmFileName, parameters);
    try {
      executor.close();
    } catch (IOException e) {
      throw new AlgorithmExecutionException("Could not close algorithm executor (" + e.getMessage() + ").", e);
    }

    return executionTime;
  }

  @Override
  public ArrayList<Result> fetchNewResults(String executionIdentifier) {
    // FIXME return exception when algorithm name is not in map
    return currentResultReceiver.get(executionIdentifier).getNewResults();
  }

  @Override
  public float fetchProgress(String executionIdentifier) {
    // FIXME return exception when algorithm name is not in map
    return currentProgressCaches.get(executionIdentifier).getProgress();
  }
}
