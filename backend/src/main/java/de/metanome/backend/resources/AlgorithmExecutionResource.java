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


import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSetting;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.algorithm_execution.AlgorithmExecutor;
import de.metanome.backend.algorithm_execution.ProcessExecutor;
import de.metanome.backend.algorithm_execution.ProgressCache;
import de.metanome.backend.algorithm_execution.TempFileGenerator;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import de.metanome.backend.result_receiver.ResultReader;
import de.metanome.backend.result_receiver.ResultCache;
import de.metanome.backend.result_receiver.ResultCounter;
import de.metanome.backend.result_receiver.ResultPrinter;
import de.metanome.backend.result_receiver.ResultReceiver;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.ExecutionSetting;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.ResultType;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
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
  public Execution executeAlgorithm(AlgorithmExecutionParams params) {
    //Todo: make execution interruptable - save id to table or something like this - and make it killable via frontend using call
    /* AlgorithmExecutor executor;

    try {
      executor = buildExecutor(params);
    } catch (FileNotFoundException e) {
      throw new WebException("Could not generate result file", Response.Status.BAD_REQUEST);
    } catch (UnsupportedEncodingException e) {
      throw new WebException("Could not build temporary file generator", Response.Status.BAD_REQUEST);
    }

    AlgorithmResource algorithmResource = new AlgorithmResource();
    Algorithm algorithm = algorithmResource.get(params.getAlgorithmId());*/

    long timeOut = 10;
    Execution execution = null;
    String executionIdentifier = params.getExecutionIdentifier();
    try {
      //Todo create/save ExecutionSetting (delete duplicate code from AlgorithmExecutor) - Pass ID to new process - get ExecutionSetting - execute
      //Todo create/save execution with executionSettings - return execution id - return execution in here in parent process
      //Todo Execution Id - return Exec

      DefaultConfigurationFactory configurationFactory = new DefaultConfigurationFactory();
      List<ConfigurationValue> parameterValues = new LinkedList<>();
      List<Input> inputs = new ArrayList<>();

      FileInputResource fileInputResource = new FileInputResource();
      TableInputResource tableInputResource = new TableInputResource();
      DatabaseConnectionResource databaseConnectionResource = new DatabaseConnectionResource();

      for (ConfigurationRequirement requirement : params.getRequirements()) {
        parameterValues.add(requirement.build(configurationFactory));

        for (ConfigurationSetting setting : requirement.getSettings()) {
          if (setting instanceof ConfigurationSettingFileInput) {
            inputs.add(fileInputResource.get(((ConfigurationSettingFileInput) setting).getId()));
          } else if (setting instanceof ConfigurationSettingDatabaseConnection) {
            inputs.add(databaseConnectionResource
                           .get(((ConfigurationSettingDatabaseConnection) setting).getId()));
          } else if (setting instanceof ConfigurationSettingTableInput) {
            inputs.add(tableInputResource.get(((ConfigurationSettingTableInput) setting).getId()));
          }
        }
      }
      ExecutionSetting executionSetting = new ExecutionSetting(parameterValues, inputs, executionIdentifier);
      try {
        HibernateUtil.store(executionSetting);
      } catch (EntityStorageException e) {
        e.printStackTrace();
      }
      int exitCode = ProcessExecutor.exec(AlgorithmExecutor.class, String.valueOf(params.getAlgorithmId()),
                                          executionIdentifier, timeOut);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (AlgorithmConfigurationException e) {
      e.printStackTrace();
    }
    //Todo: comment steps - and/or defer steps - here: retrieve execution that was stored during the corresponding execution process
    try {
      ArrayList<Criterion> criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("identifier", executionIdentifier));
      execution =
          (Execution) HibernateUtil
              .queryCriteria(Execution.class,
                             criteria.toArray(new Criterion[criteria.size()])).get(0);
    } catch (EntityStorageException e) {
      // ExecutionSetting should implement Entity, so the exception should not occur.
      e.printStackTrace();
    }
    /*try {


    Execution execution = null;
    try {
      execution = executor.executeAlgorithm(algorithm, params.getRequirements(), params.getExecutionIdentifier());
    } catch (AlgorithmLoadingException | AlgorithmExecutionException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }

    try {
      executor.close();
    } catch (IOException e) {
      throw new WebException("Could not close algorithm executor", Response.Status.BAD_REQUEST);
    }*/

    return execution;
  }

  @GET
  @Path("/result_cache/{identifier}")
  @Produces("application/json")
  public List<Result> getCacheResults(@PathParam("identifier") String executionIdentifier) {
    try {
      return AlgorithmExecutionCache.getResultCache(executionIdentifier).fetchNewResults();
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

  @GET
  @Path("/result_counter/{identifier}")
  @Produces("application/json")
  public EnumMap<ResultType, Integer> getCounterResults(@PathParam("identifier") String executionIdentifier) {
    try {
      return AlgorithmExecutionCache.getResultCounter(executionIdentifier).getResults();
    } catch (Exception e) {
      throw new WebException("Could not find any progress to the given identifier",
                             Response.Status.BAD_REQUEST);
    }
  }

  @GET
  @Path("/result_printer/{identifier}")
  @Produces("application/json")
  public List<Result> getPrinterResults(@PathParam("identifier") String executionIdentifier) {
    try {
      return AlgorithmExecutionCache.getResultPrinter(executionIdentifier).getResults();
    } catch (Exception e) {
      throw new WebException("Could not find any progress to the given identifier",
                             Response.Status.BAD_REQUEST);
    }
  }

  @GET
  @Path("/read_result/{file_name}/{type}")
  @Produces("application/json")
  public List<Result> readResultFromFile(@PathParam("file_name") String fileName, @PathParam("type") String type) {
    try {
      return ResultReader.readResultsFromFile(fileName, type);
    } catch (Exception e) {
      throw new WebException("Could not read result file",
                             Response.Status.BAD_REQUEST);
    }
  }


  /**
   * Builds an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor} with stacked {@link de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver}s to write
   * result files and cache results for the frontend.
   *
   * @param params all parameters for executing the algorithm
   * @return an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor}
   * @throws java.io.FileNotFoundException        when the result files cannot be opened
   * @throws java.io.UnsupportedEncodingException when the temp files cannot be opened
   */
  protected AlgorithmExecutor buildExecutor(AlgorithmExecutionParams params)
      throws FileNotFoundException, UnsupportedEncodingException {
    String identifier = params.getExecutionIdentifier();
    FileGenerator fileGenerator = new TempFileGenerator();
    ProgressCache progressCache = new ProgressCache();

    ResultReceiver resultReceiver = null;
    if (params.getCacheResults()) {
      resultReceiver = new ResultCache(identifier);
      AlgorithmExecutionCache.add(identifier, (ResultCache) resultReceiver);
    } else if (params.getCountResults()) {
      resultReceiver = new ResultCounter(identifier);
      AlgorithmExecutionCache.add(identifier, (ResultCounter) resultReceiver);
    } else if (params.getWriteResults()) {
      resultReceiver = new ResultPrinter(identifier);
      AlgorithmExecutionCache.add(identifier, (ResultPrinter) resultReceiver);
    }

    AlgorithmExecutionCache.add(identifier, progressCache);

    AlgorithmExecutor executor = new AlgorithmExecutor(resultReceiver, progressCache, fileGenerator);
    executor.setResultPathPrefix(resultReceiver.getOutputFilePathPrefix());

    return executor;
  }

}
