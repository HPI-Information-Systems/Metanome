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

import com.fasterxml.jackson.core.JsonProcessingException;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSetting;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.backend.algorithm_execution.AlgorithmExecutor;
import de.metanome.backend.algorithm_execution.ProcessExecutor;
import de.metanome.backend.algorithm_execution.ProcessRegistry;
import de.metanome.backend.algorithm_execution.ProgressCache;
import de.metanome.backend.algorithm_execution.TempFileGenerator;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import de.metanome.backend.helper.FileInputGeneratorMixIn;
import de.metanome.backend.helper.RelationalInputGeneratorMixIn;
import de.metanome.backend.helper.TableInputGeneratorMixIn;
import de.metanome.backend.result_postprocessing.ResultPostProcessor;
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

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

  @GET
  @Path("/stop/{identifier}")
  @Produces("application/json")
  public void stopExecution(@PathParam("identifier") String executionIdentifier) {
    Process process = ProcessRegistry.getInstance().get(executionIdentifier);
    ProcessRegistry.getInstance().remove(executionIdentifier);
    process.destroy();
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
   * Executes an algorithm.
   *
   * @param params all parameters to execute the algorithm
   * @return the resulting execution
   */
  @POST
  @Consumes("application/json")
  @Produces("application/json")
  public Execution executeAlgorithm(AlgorithmExecutionParams params) {

    String executionIdentifier = params.getExecutionIdentifier();
    ExecutionSetting executionSetting = buildExecutionSetting(params);
    try {
      HibernateUtil.store(executionSetting);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
    HibernateUtil.shutdown();
    try {
      Process
          process =
          ProcessExecutor.exec(AlgorithmExecutor.class, String.valueOf(params.getAlgorithmId()),
                               executionIdentifier, params.getMemory(), params.getCountResults());
      ProcessRegistry.getInstance().put(executionIdentifier,
                                        process);
      InputStreamReader isr = new InputStreamReader(process.getInputStream());
      BufferedReader br = new BufferedReader(isr);
      String lineRead;
      while ((lineRead = br.readLine()) != null) {
        System.out.println(lineRead);
      }
      System.out.println("hello Again!");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Execution execution = null;
    try {
      ArrayList<Criterion> criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("identifier", executionIdentifier));
      execution =
          (Execution) HibernateUtil
              .queryCriteria(Execution.class,
                             criteria.toArray(new Criterion[criteria.size()])).get(0);
    } catch (EntityStorageException | IndexOutOfBoundsException e) { // in case execution process is killed
      AlgorithmResource algorithmResource = new AlgorithmResource();
      Algorithm algorithm = algorithmResource.get(params.getAlgorithmId());
      execution = new Execution(algorithm)
          .setExecutionSetting(executionSetting)
          .setAborted(true)
          .setInputs(parseInputs(executionSetting));
      ExecutionResource executionResource = new ExecutionResource();
      executionResource.store(execution);
    }
    if (!executionSetting.getCountResults()) {
      try {
        ResultPostProcessor.extractAndStoreResults(execution);
      } catch (Exception e) {
        throw new WebException("Could not execute result post processing: " + e.getMessage(),
                               Response.Status.BAD_REQUEST);
      }
    }
    System.out.println("Done");
    return execution;
  }

  protected ExecutionSetting buildExecutionSetting(AlgorithmExecutionParams params) {
    ExecutionSetting executionSetting = null;
    try {
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
      //Todo: designated Method
      JsonConverter<ConfigurationValue> jsonConverter = new JsonConverter<ConfigurationValue>();
      jsonConverter.addMixIn(FileInputGenerator.class, FileInputGeneratorMixIn.class);
      jsonConverter.addMixIn(TableInputGenerator.class, TableInputGeneratorMixIn.class);
      jsonConverter.addMixIn(RelationalInputGenerator.class, RelationalInputGeneratorMixIn.class);
      List<String> parameterValuesJson = new ArrayList<String>();
      try {
        for (ConfigurationValue config : parameterValues) {
          parameterValuesJson.add(jsonConverter.toJsonString(config));
        }
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }

      List<String> inputsJson = new ArrayList<String>();
      JsonConverter<Input> jsonConverterInput = new JsonConverter<Input>();
      for (Input input : inputs) {
        try {
          inputsJson.add(jsonConverterInput.toJsonString(input));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }
      executionSetting =
          new ExecutionSetting(parameterValuesJson, inputsJson, params.getExecutionIdentifier())
              .setCacheResults(params.getCacheResults())
              .setWriteResults(params.getWriteResults())
              .setCountResults(params.getCountResults());
    } catch (AlgorithmConfigurationException e) {
      e.printStackTrace();
    }
    return executionSetting;
  }

  /**
   * Builds an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor} with stacked {@link
   * de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver}s to write result
   * files and cache results for the frontend.
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

    AlgorithmExecutor
        executor =
        new AlgorithmExecutor(resultReceiver, progressCache, fileGenerator);
    executor.setResultPathPrefix(resultReceiver.getOutputFilePathPrefix());

    return executor;
  }

  public static List<Input> parseInputs(ExecutionSetting setting) {
    JsonConverter<Input> jsonConverterInput = new JsonConverter<Input>();
    List<Input> inputs = new ArrayList<Input>();
    for (String json : setting.getInputsJson()) {
      try {
        inputs.add(jsonConverterInput.fromJsonString(json, Input.class));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    return inputs;
  }

}
