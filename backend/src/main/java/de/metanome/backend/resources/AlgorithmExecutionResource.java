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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSetting;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.backend.algorithm_execution.AlgorithmExecution;
import de.metanome.backend.algorithm_execution.ProcessRegistry;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.helper.DatabaseConnectionGeneratorMixIn;
import de.metanome.backend.helper.ExecutionResponse;
import de.metanome.backend.helper.FileInputGeneratorMixIn;
import de.metanome.backend.helper.RelationalInputGeneratorMixIn;
import de.metanome.backend.helper.TableInputGeneratorMixIn;
import de.metanome.backend.result_postprocessing.ResultPostProcessor;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.ExecutionSetting;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("algorithm-execution")
public class AlgorithmExecutionResource {

  private static final Class<?> algorithmExecutionClass = AlgorithmExecution.class;

  /**
   * Stops the algorithm with the given identifier.
   *
   * @param executionIdentifier the execution identifier.
   */
  @POST
  @Path("/stop/{identifier}")
  public void stopExecution(@PathParam("identifier") String executionIdentifier) {
    try {
      Process process = ProcessRegistry.getInstance().get(executionIdentifier);
      ProcessRegistry.getInstance().remove(executionIdentifier);
      process.destroy();
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Executes an algorithm.
   *
   * @param params all parameters to execute the algorithm
   * @return the resulting execution
   */
  @POST
  @Consumes(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  public Execution executeAlgorithm(AlgorithmExecutionParams params) {
    //TODO: Why is the executionIdentifier i.e. unique ID managed by the frontend?
    String executionIdentifier = params.getExecutionIdentifier();

    // Build the execution setting and store it.
    ExecutionSetting executionSetting = null;
    try {
      executionSetting = buildExecutionSetting(params);
      HibernateUtil.store(executionSetting);
    } catch (Exception e) {
      e.printStackTrace();
      String message = "Could not build execution setting";
      if (e.getMessage() != null) {
        message += ": " + e.getMessage();
      }
      throw new WebException(message, Response.Status.BAD_REQUEST);
    }

    String exceptionMessage = "";
    try {
      // Start the process, which executes the algorithm
      Process process =
        executeAlgorithm(String.valueOf(params.getAlgorithmId()),
          executionIdentifier,
          params.getMemory());
      ProcessRegistry.getInstance().put(executionIdentifier, process);

      // Forward messages from the process to the console output
      InputStreamReader isr = new InputStreamReader(process.getInputStream());
      BufferedReader br = new BufferedReader(isr);
      String lineRead;
      while ((lineRead = br.readLine()) != null) {
        if (lineRead.contains("Exception") || lineRead.contains("Caused by: ")) {
          exceptionMessage = lineRead;
        }
        System.out.println(lineRead);
      }

      try {
        if (process.exitValue() != 0) {
          exceptionMessage = "Error in algorithm execution. " +  exceptionMessage;
        }
      } catch (IllegalThreadStateException e) {
        // The process has not exit, but it should be.
        process.destroy();
        exceptionMessage = "Error in algorithm execution. " + exceptionMessage;
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    Execution execution;
    try {
      // The algorithm execution was successful
      // Get the execution from hibernate
      ArrayList<Criterion> criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("identifier", executionIdentifier));
      criteria.add(Restrictions.eq("running",false));
      execution = (Execution) HibernateUtil.queryCriteria(Execution.class,
        criteria.toArray(
          new Criterion[criteria.size()]))
        .get(0);
    } catch (Exception e) {
      // The execution process got killed - execution object is created anyway (with abortion flag set)


      try {
        ArrayList<Criterion> criteria = new ArrayList<>();
        criteria.add(Restrictions.eq("identifier", executionIdentifier));
        criteria.add(Restrictions.eq("running",true));
        execution = (Execution) HibernateUtil.queryCriteria(Execution.class,
                  criteria.toArray(new Criterion[criteria.size()])).get(0);

        execution = execution
                  .setRunning(false)
                  .setExecutionSetting(executionSetting)
                  .setAborted(true)
                  .setInputs(AlgorithmExecution.parseInputs(executionSetting.getInputsJson()));

        HibernateUtil.update(execution);
      } catch (IndexOutOfBoundsException e1) {
        throw new WebException(exceptionMessage, Response.Status.BAD_REQUEST);
      } catch (EntityStorageException e1) {
        e1.printStackTrace();
        throw new WebException("Could not store execution.", Response.Status.BAD_REQUEST);
      }

      // throw new WebException, because the algorithm was not successful!
      throw new WebException(exceptionMessage, Response.Status.BAD_REQUEST);
    }

    // Execute the result post processing
    if (!executionSetting.getCountResults()) {
      try {
        ResultPostProcessor.extractAndStoreResultsDataIndependent(execution);
      } catch (Exception e) {
        e.printStackTrace();
        String message = "Could not execute result post processing";
        if (e.getMessage() != null) {
          message += ": " + e.getMessage();
        }
        throw new WebException(message, Response.Status.BAD_REQUEST);
      }
    }

    // Set ExecutionResponse
    ExecutionResponse executionResponse = new ExecutionResponse()
            .setAlgorithm(execution.getAlgorithm().getName())
            .setIdentifier(execution.getIdentifier())
            .setStarted(execution.getBegin());

    return execution;
  }

  /**
   * Builds {@link de.metanome.backend.results_db.ExecutionSetting} to persist information in AlgorithmExecutionParams to Database
   *
   * @param params the algorithm execution parameter
   * @return an {@link de.metanome.backend.results_db.ExecutionSetting}
   * @throws AlgorithmConfigurationException if configuration requirements could not be converted
   */
  protected ExecutionSetting buildExecutionSetting(AlgorithmExecutionParams params) throws AlgorithmConfigurationException {
    ExecutionSetting executionSetting = null;

    DefaultConfigurationFactory configurationFactory = new DefaultConfigurationFactory();
    List<ConfigurationValue>
      parameterValues = new LinkedList<>();
    List<Input> inputs = new ArrayList<>();
    FileInputResource fileInputResource = new FileInputResource();
    TableInputResource tableInputResource = new TableInputResource();
    DatabaseConnectionResource databaseConnectionResource = new DatabaseConnectionResource();

    // build configuration values
    for (ConfigurationRequirement<?> requirement : params.getRequirements()) {
      // no value was set in the frontend
      // do not create a configuration value, so that the value can not be set
      // on the algorithm
      if (requirement.getSettings().length == 0) {
        continue;
      }

      // convert the requirement and add it to the parameters
      try {
        parameterValues.add(requirement.build(configurationFactory));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      // add inputs
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

    // convert configuration values to json strings
    List<String> parameterValuesJson = configurationValuesToJson(parameterValues);

    // convert inputs to json strings
    List<String> inputsJson = inputsToJson(inputs);

    // create a new execution setting object
    executionSetting =
      new ExecutionSetting(parameterValuesJson, inputsJson, params.getExecutionIdentifier())
        .setCacheResults(params.getCacheResults())
        .setWriteResults(params.getWriteResults())
        .setCountResults(params.getCountResults());

    return executionSetting;
  }

  /**
   * Converts a list of ConfigurationValues to their Json representations
   *
   * @param parameterValues all parameters to execute the algorithm
   * @return the resulting list of Json Strings
   */
  public List<String> configurationValuesToJson(List<ConfigurationValue> parameterValues) {
    JsonConverter<ConfigurationValue>
      jsonConverter = new JsonConverter<>();
    jsonConverter.addMixIn(FileInputGenerator.class, FileInputGeneratorMixIn.class);
    jsonConverter.addMixIn(TableInputGenerator.class, TableInputGeneratorMixIn.class);
    jsonConverter.addMixIn(DatabaseConnectionGenerator.class, DatabaseConnectionGeneratorMixIn.class);
    jsonConverter.addMixIn(RelationalInputGenerator.class, RelationalInputGeneratorMixIn.class);
    return jsonConverter.toJsonStrings(parameterValues);
  }

  public List<String> inputsToJson(List<Input> inputs) {
    JsonConverter<Input> jsonConverter = new JsonConverter<Input>();
    return jsonConverter.toJsonStrings(inputs);
  }

  /**
   * starts execution of Algorithm in separate Process
   *
   * @param algorithmId         id of algorithm to be executed
   * @param executionIdentifier identifier for the upcoming algorithm execution
   * @param memory              memory argument for the process running the algorithm execution
   * @return resulting process object for the algorithm execution
   */
  private Process executeAlgorithm(String algorithmId, String executionIdentifier,
                                   String memory) throws IOException,
    InterruptedException {
    /**
     * NOTE: Dpeneding on the Java ApplicationServer paths have to be adjusted
     */
    String javaHome = System.getProperty("java.home");
    String javaBin = javaHome +
      Constants.FILE_SEPARATOR + "bin" +
      Constants.FILE_SEPARATOR + "java";
    String myPath = System.getProperty("java.class.path");
    String className = algorithmExecutionClass.getCanonicalName();

    try {
      URL baseUrl = algorithmExecutionClass.getProtectionDomain().getCodeSource().getLocation();
      File file = new File(baseUrl.toURI());
      String webinf = file.getAbsoluteFile().getParentFile().getParent() + Constants.FILE_SEPARATOR;
      String classesFolder = webinf + "classes";
      String parentPathWildCard = webinf + "lib" + Constants.FILE_SEPARATOR + "*";
      myPath += File.pathSeparator + parentPathWildCard + File.pathSeparator + classesFolder;
    } catch (URISyntaxException ex) {
      ex.printStackTrace();
    }
    
    ProcessBuilder builder;
    if (!memory.equals("")) {
      builder = new ProcessBuilder(
        javaBin, "-Xmx" + memory + "m", "-Xms" + memory + "m", "-classpath", myPath, className,
        algorithmId, executionIdentifier);
    } else {
      builder = new ProcessBuilder(
        javaBin, "-classpath", myPath, className, algorithmId, executionIdentifier);
    }
    builder.redirectErrorStream(true);

    return builder.start();
  }

}
