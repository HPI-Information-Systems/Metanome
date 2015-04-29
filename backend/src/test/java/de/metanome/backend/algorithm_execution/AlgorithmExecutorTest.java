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

package de.metanome.backend.algorithm_execution;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.algorithms.testing.example_basic_stat_algorithm.BasicStatAlgorithm;
import de.metanome.algorithms.testing.example_relational_input_algorithm.ExampleAlgorithm;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.configuration.ConfigurationValueFileInputGenerator;
import de.metanome.backend.configuration.ConfigurationValueInteger;
import de.metanome.backend.configuration.ConfigurationValueListBox;
import de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator;
import de.metanome.backend.configuration.ConfigurationValueString;
import de.metanome.backend.input.file.FileFixture;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.ExecutionResource;
import de.metanome.backend.resources.FileInputResource;
import de.metanome.backend.resources.ResultResource;
import de.metanome.backend.result_receiver.CloseableOmniscientResultReceiver;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.Result;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor}
 */
public class AlgorithmExecutorTest {

  private CloseableOmniscientResultReceiver resultReceiver;
  private ProgressCache progressCache;
  private FileGenerator fileGenerator;
  private AlgorithmExecutor executor;
  private AlgorithmResource resource;

  @Before
  public void setUp() throws UnsupportedEncodingException {
    resultReceiver = mock(CloseableOmniscientResultReceiver.class);
    progressCache = mock(ProgressCache.class);
    fileGenerator = new TempFileGenerator();
    resource = new AlgorithmResource();

    executor = new AlgorithmExecutor(resultReceiver, progressCache, fileGenerator);
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm,
   * java.util.List, String, Boolean)} Tests the execution of an fd algorithm. The elapsed time should be
   * greater than 0ns.
   */
  @Test
  public void testExecuteFunctionalDependencyAlgorithm()
      throws AlgorithmLoadingException, AlgorithmExecutionException, IllegalArgumentException,
             SecurityException, ClassNotFoundException, InstantiationException,
             IllegalAccessException, InvocationTargetException, NoSuchMethodException,
             EntityStorageException, IOException {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString("pathToOutputFile", "path/to/file"));
    String[] selectedValues = {"second"};
    configs.add(new ConfigurationValueListBox("column names", selectedValues));
    Algorithm algorithm = new Algorithm("example_fd_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    Execution
        execution =
        executor.executeAlgorithmWithValues(algorithm, configs, null, "identifier", false);

    // Check result
    verify(resultReceiver).receiveResult(isA(FunctionalDependency.class));
    assertTrue(0 <= execution.getEnd() - execution.getBegin());

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm,
   * java.util.List, String, Boolean)} Tests the execution of an od algorithm. The elapsed time should be
   * greater than 0ns.
   */
  @Test
  public void testExecuteOrderDependencyAlgorithm()
      throws AlgorithmLoadingException, AlgorithmExecutionException, IllegalArgumentException,
             SecurityException, IOException, ClassNotFoundException, InstantiationException,
             IllegalAccessException, InvocationTargetException, NoSuchMethodException,
             EntityStorageException {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString(
        de.metanome.algorithms.testing.example_od_algorithm.ExampleAlgorithm.FILE_NAME,
        "path/to/file"));
    Algorithm algorithm = new Algorithm("example_od_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    Execution
        execution =
        executor.executeAlgorithmWithValues(algorithm, configs, null, "identifier", false);

    // Check result
    verify(resultReceiver).receiveResult(isA(OrderDependency.class));
    assertTrue(0 <= execution.getEnd() - execution.getBegin());

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm,
   * java.util.List, String, Boolean)} Tests the execution of an ind algorithm.
   */
  @Test
  public void testExecuteInclusionDependency()
      throws AlgorithmLoadingException, AlgorithmExecutionException, IllegalArgumentException,
             SecurityException, IOException, ClassNotFoundException, InstantiationException,
             IllegalAccessException, InvocationTargetException, NoSuchMethodException,
             EntityStorageException {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString(
        de.metanome.algorithms.testing.example_ind_algorithm.ExampleAlgorithm.STRING_IDENTIFIER,
        "table1"));
    configs.add(new ConfigurationValueInteger(
        de.metanome.algorithms.testing.example_ind_algorithm.ExampleAlgorithm.INTEGER_IDENTIFIER,
        7));
    configs.add(new ConfigurationValueFileInputGenerator(
        de.metanome.algorithms.testing.example_ind_algorithm.ExampleAlgorithm.CSV_FILE_IDENTIFIER,
        mock(FileInputGenerator.class)));
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithmWithValues(algorithm, configs, null, "identifier", false);

    // Check result
    verify(resultReceiver).receiveResult(isA(InclusionDependency.class));

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm,
   * java.util.List, String, Boolean)}
   *
   * The {@link de.metanome.algorithms.testing.example_relational_input_algorithm.ExampleAlgorithm}
   * should be executable by generating a {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}
   * from a file.
   */
  @Test
  public void testRelationalInputAlgorithm()
      throws AlgorithmExecutionException, AlgorithmLoadingException, EntityStorageException,
             FileNotFoundException, UnsupportedEncodingException {
    HibernateUtil.clear();

    // Setup
    String path = new FileFixture("some file content").getTestData("some file name").getPath();
    List<ConfigurationRequirement> requirements = new ArrayList<>();
    ConfigurationRequirementRelationalInput
        requirementRelationalInput =
        new ConfigurationRequirementRelationalInput(
            ExampleAlgorithm.RELATIONAL_INPUT_IDENTIFIER);
    requirementRelationalInput.checkAndSetSettings(new ConfigurationSettingFileInput(path));
    requirements.add(requirementRelationalInput);

    Algorithm algorithm = new Algorithm("example_relational_input_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    // Check result
    executor.executeAlgorithm(algorithm, requirements, "identifier", false);

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithmWithValues(de.metanome.backend.results_db.Algorithm,
   * java.util.List, java.util.List, String, Boolean)} Tests the execution of an Ucc algorithm.
   */
  @Test
  public void testExecuteUniqueColumnCombinationsAlgorithm()
      throws AlgorithmLoadingException, AlgorithmExecutionException, IllegalArgumentException,
             SecurityException, IOException, ClassNotFoundException, InstantiationException,
             IllegalAccessException, InvocationTargetException, NoSuchMethodException,
             EntityStorageException {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString(
        de.metanome.algorithms.testing.example_ucc_algorithm.ExampleAlgorithm.STRING_IDENTIFIER,
        "path/to/file1", "path/to/file2"));

    Algorithm algorithm = new Algorithm("example_ucc_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithmWithValues(algorithm, configs, null, "identifier", false);

    // Check result
    verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));
    // After finishing the progress should be 1;
    verify(progressCache).updateProgress(1);

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithmWithValues(de.metanome.backend.results_db.Algorithm,
   * java.util.List, java.util.List, String, Boolean)} Tests the execution of an holistic algorithm.
   */
  @Test
  public void testExecuteHolisticAlgorithm()
      throws AlgorithmLoadingException, AlgorithmExecutionException, IllegalArgumentException,
             SecurityException, IOException, ClassNotFoundException, InstantiationException,
             IllegalAccessException, InvocationTargetException, NoSuchMethodException,
             EntityStorageException {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString("pathToOutputFile", "path/to/file1", "path/to/file1"));

    Algorithm algorithm = new Algorithm("example_holistic_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithmWithValues(algorithm, configs, null, "identifier", false);

    // Check result
    verify(resultReceiver).receiveResult(isA(FunctionalDependency.class));
    verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithmWithValues(de.metanome.backend.results_db.Algorithm,
   * java.util.List, java.util.List, String, Boolean)} Algorithms that do not implement the metanome
   * interfaces directly should still be executable.
   */
  @Test
  public void testExecuteIndirectInterfaceAlgorithm()
      throws IllegalAccessException, IOException, InstantiationException,
             AlgorithmExecutionException, NoSuchMethodException, InvocationTargetException,
             ClassNotFoundException, EntityStorageException, AlgorithmLoadingException {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configurationValues = new LinkedList<>();
    configurationValues.add(new ConfigurationValueRelationalInputGenerator("identifier", mock(
        RelationalInputGenerator.class)));

    Algorithm algorithm = new Algorithm("example_indirect_interfaces_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithmWithValues(algorithm, configurationValues, null, "identifier", false);

    // Check result
    verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithmWithValues(de.metanome.backend.results_db.Algorithm,
   * java.util.List, java.util.List, String, Boolean)} When executing an {@link
   * de.metanome.algorithm_integration.Algorithm} an {@link de.metanome.backend.results_db.Execution}
   * should be saved in the results database.
   */
  @Test
  public void testExecutionStoredInDatabase()
      throws IllegalAccessException, IOException, InstantiationException,
             AlgorithmExecutionException, NoSuchMethodException, InvocationTargetException,
             ClassNotFoundException, EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    List<ConfigurationValue> configurationValues = new LinkedList<>();
    configurationValues.add(new ConfigurationValueRelationalInputGenerator("identifier", mock(
        RelationalInputGenerator.class)));

    Algorithm algorithm = new Algorithm("example_indirect_interfaces_algorithm.jar");
    algorithm = resource.store(algorithm);

    FileInput expectedInput = new FileInput("some_file");
    FileInputResource fileInputResource = new FileInputResource();
    fileInputResource.store(expectedInput);

    List<Input> inputs = new ArrayList<>();
    inputs.add(expectedInput);

    // Execute functionality
    executor.executeAlgorithmWithValues(algorithm, configurationValues, inputs, "identifier", false);
    ExecutionResource executionResource = new ExecutionResource();
    List<Execution> actualExecutions = executionResource.getAll();

    // Check result
    assertFalse(actualExecutions.isEmpty());
    Execution actualExecution = actualExecutions.get(0);
    assertEquals(algorithm, actualExecution.getAlgorithm());
    // The execution should not be older than 5 seconds.
    assertTrue(new Date().getTime() - actualExecution.getBegin() < 5000);
    assertTrue(new Date().getTime() - actualExecution.getEnd() < 5000);
    // The execution should have taken between 0 and 3 seconds.
    assertTrue(actualExecution.getEnd() - actualExecution.getBegin() < 3000);
    assertTrue(actualExecution.getEnd() - actualExecution.getBegin() >= 0);

    assertTrue(actualExecution.getInputs().size() == 1);
    assertTrue(actualExecution.getInputs().contains(expectedInput));

    ResultResource resultResource = new ResultResource();
    List<Result> results = resultResource.getAll();

    assertTrue(results.size() > 0);
    assertEquals(results.get(0).getExecution(), actualExecution);

    assertTrue(actualExecution.getResults().size() > 0);

    // TODO assert other execution fields

    // Cleanup
    HibernateUtil.clear();
  }

  @Test(expected = Exception.class)
  public void testExecutionWithWrongFileName()
      throws IllegalAccessException, IOException, InstantiationException,
             AlgorithmExecutionException, NoSuchMethodException, EntityStorageException,
             InvocationTargetException, ClassNotFoundException {
    // Setup
    HibernateUtil.clear();

    List<ConfigurationValue> configurationValues = new LinkedList<>();
    configurationValues.add(new ConfigurationValueRelationalInputGenerator("identifier", mock(
        RelationalInputGenerator.class)));

    Algorithm algorithm = new Algorithm("wrong_algorithm.jar");

    // Execute functionality
    executor.executeAlgorithmWithValues(algorithm, configurationValues, null, "identifier", false);

    // Setup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm,
   * java.util.List, String, Boolean)} Tests the execution of a basic statistics algorithm that requires
   * several {@link de.metanome.algorithm_integration.input.FileInputGenerator}s to run.
   */
  @Test
  public void testExecuteBasicStatisticsAlgorithmWithFileInputGenerator()
      throws AlgorithmExecutionException, AlgorithmLoadingException, IOException,
             EntityStorageException {
    HibernateUtil.clear();

    // Setup
    // Build file input specification
    int numberOfInputs = 5;
    List<ConfigurationRequirement> configurationRequirements = new LinkedList<>();
    ConfigurationRequirementFileInput
        specification =
        new ConfigurationRequirementFileInput(BasicStatAlgorithm.INPUT_FILE_IDENTIFIER,
                                              numberOfInputs);

    // Build input files
    String expectedStatisticValue = "some value";
    String expectedOtherFileName = "some other file name";
    FileFixture fileFixture = new FileFixture("some file content");
    expectedOtherFileName = fileFixture.getTestData(expectedOtherFileName).getAbsolutePath();
    expectedStatisticValue = fileFixture.getTestData(expectedStatisticValue).getAbsolutePath();

    // Build mock configuration settings
    ConfigurationSettingFileInput[] settings = new ConfigurationSettingFileInput[numberOfInputs];
    for (int i = 0; i < numberOfInputs - 1; i++) {
      ConfigurationSettingFileInput
          configurationSetting =
          mock(ConfigurationSettingFileInput.class);
      when(configurationSetting.isAdvanced()).thenReturn(false);
      when(configurationSetting.getFileName()).thenReturn(expectedOtherFileName);
      settings[i] = configurationSetting;
    }
    // Last setting determines algorithm's result
    ConfigurationSettingFileInput lastSetting = mock(ConfigurationSettingFileInput.class);
    when(lastSetting.isAdvanced()).thenReturn(false);
    when(lastSetting.getFileName()).thenReturn(expectedStatisticValue);
    settings[4] = lastSetting;
    specification.checkAndSetSettings(settings);

    configurationRequirements.add(specification);

    Algorithm algorithm = new Algorithm("example_basic_stat_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithm(algorithm, configurationRequirements, "identifier", false);

    // Check result
    ArgumentCaptor<BasicStatistic> captor = ArgumentCaptor.forClass(BasicStatistic.class);
    verify(resultReceiver).receiveResult(captor.capture());
    assertEquals(expectedStatisticValue, captor.getValue().getStatisticValue());

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#close()} <p/>
   * When closing the executor all attached result receiver should be closed.
   */
  @Test
  public void testClose() throws IOException {
    // Execute functionality
    executor.close();

    // Check result
    verify(resultReceiver).close();
  }

}
