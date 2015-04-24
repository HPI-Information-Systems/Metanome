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

package de.metanome.backend.result_postprocessing;


import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.result_store.FunctionalDependencyResultStore;
import de.metanome.backend.result_postprocessing.result_store.InclusionDependencyResultsStore;
import de.metanome.backend.result_postprocessing.result_store.ResultsStoreHolder;
import de.metanome.backend.result_receiver.ResultReader;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.ResultType;

import java.io.IOException;

/**
 * Starting point for the result post processing.
 * The results are extracted from disk and hold in memory for further analyses.
 */
public class ResultPostProcessor {

  /**
   * Loads the results of a algorithm run from hard disk and stores them.
   *
   * @param execution Execution containing the algorithm results file path
   */
  public static void extractAndStoreResults(Execution execution) throws IOException {
    ResultsStoreHolder.clearStores();

    for (de.metanome.backend.results_db.Result result : execution.getResults()) {
      String fileName = result.getFileName();
      String resultTypeName = result.getType().getName();

      storeResults(fileName,
                   resultTypeName,
                   execution.getId());
    }
  }

  /**
   * Reads the results from the given file and stores them in a result store.
   *
   * @param fileName    the file name
   * @param name        the name of the result type
   * @param executionId the execution id
   */
  private static void storeResults(String fileName, String name,
                                   long executionId) throws IOException {

    if (name.equals(ResultType.CUCC.getName())) {

    } else if (name.equals(ResultType.OD.getName())) {

    } else if (name.equals(ResultType.IND.getName())) {
      InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();
      ResultReader<InclusionDependency>
          resultReader =
          new ResultReader<>(InclusionDependency.class);
      resultsStore.store(resultReader.readResultsFromFile(fileName), executionId);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.FD.getName())) {
      FunctionalDependencyResultStore resultsStore = new FunctionalDependencyResultStore();
      ResultReader<FunctionalDependency>
          resultReader =
          new ResultReader<>(FunctionalDependency.class);
      resultsStore.store(resultReader.readResultsFromFile(fileName), executionId);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.UCC.getName())) {

    } else if (name.equals(ResultType.STAT.getName())) {

    }


  }


}
