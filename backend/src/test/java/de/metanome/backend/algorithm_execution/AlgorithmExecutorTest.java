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
package de.metanome.backend.algorithm_execution;

import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.ConditionalInclusionDependency;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.algorithms.testing.example_basic_stat_algorithm.BasicStatAlgorithm;
import de.metanome.algorithms.testing.example_relational_input_algorithm.ExampleAlgorithm;
import de.metanome.backend.configuration.ConfigurationValueListBox;
import de.metanome.backend.configuration.ConfigurationValueCheckBox;
import de.metanome.backend.configuration.ConfigurationValueFileInputGenerator;
import de.metanome.backend.configuration.ConfigurationValueInteger;
import de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator;
import de.metanome.backend.configuration.ConfigurationValueString;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.input.file.FileFixture;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.ExecutionResource;
import de.metanome.backend.resources.FileInputResource;
import de.metanome.backend.result_receiver.CloseableOmniscientResultReceiver;
import de.metanome.backend.results_db.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor}
 */
public class AlgorithmExecutorTest {

  private CloseableOmniscientResultReceiver resultReceiver;
  private FileGenerator fileGenerator;
  private AlgorithmExecutor executor;
  private AlgorithmResource resource;
  private ExecutionSetting genericExecutionSetting;

  @Before
  public void setUp() throws UnsupportedEncodingException {
    resultReceiver = mock(CloseableOmniscientResultReceiver.class);
    fileGenerator = new TempFileGenerator();
    resource = new AlgorithmResource();
    genericExecutionSetting = new ExecutionSetting(null, null, null);

    executor = new AlgorithmExecutor(resultReceiver, fileGenerator);
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Tests the execution of an fd algorithm. The elapsed time should be greater than
   * 0ns.
   */

  @Test
  public void testExecuteFunctionalDependencyAlgorithm()
    throws Exception {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString("pathToOutputFile", "path/to/file"));
    String[] selectedValue = {"second"};
    configs.add(new ConfigurationValueListBox("column names", selectedValue));
    String[][] selectedValues = { {"second", "third"} };
    configs.add(new ConfigurationValueCheckBox("column names", selectedValues));
    Algorithm algorithm = new Algorithm("example_fd_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    Execution
      execution = executor.executeAlgorithm(algorithm, configs, null, "identifier",
      genericExecutionSetting);

    // Check result
    verify(resultReceiver).receiveResult(isA(FunctionalDependency.class));
    assertTrue(0 <= execution.getEnd() - execution.getBegin());

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Tests the execution of an od algorithm. The elapsed time should be greater than
   * 0ns.
   */

  @Test
  public void testExecuteOrderDependencyAlgorithm()
    throws Exception {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString(de.metanome.algorithms.testing.example_od_algorithm.ExampleAlgorithm.FILE_NAME, "path/to/file"));
    Algorithm algorithm = new Algorithm("example_od_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    Execution execution = executor.executeAlgorithm(algorithm, configs, null, "identifier",
      genericExecutionSetting);

    // Check result
    verify(resultReceiver).receiveResult(isA(OrderDependency.class));
    assertTrue(0 <= execution.getEnd() - execution.getBegin());

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Tests the execution of an ind algorithm.
   *
   * @Test public void testExecuteInclusionDependency()
   * throws AlgorithmLoadingException, AlgorithmExecutionException, IllegalArgumentException,
   * SecurityException, IOException, ClassNotFoundException, InstantiationException,
   * IllegalAccessException, InvocationTargetException, NoSuchMethodException,
   * EntityStorageException {
   * HibernateUtil.clear();
   * <p/>
   * // Setup
   * List<ConfigurationValue> configs = new ArrayList<>();
   * configs.add(new ConfigurationValueString(
   * de.metanome.algorithms.testing.example_ind_algorithm.ExampleAlgorithm.STRING_IDENTIFIER, "table1"));
   * configs.add(new ConfigurationValueInteger(
   * de.metanome.algorithms.testing.example_ind_algorithm.ExampleAlgorithm.INTEGER_IDENTIFIER, 7));
   * configs.add(new ConfigurationValueFileInputGenerator(
   * de.metanome.algorithms.testing.example_ind_algorithm.ExampleAlgorithm.CSV_FILE_IDENTIFIER,
   * mock(FileInputGenerator.class)));
   * Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
   * algorithm = resource.store(algorithm);
   * <p/>
   * // Execute functionality
   * executor.executeAlgorithm(algorithm, configs, null, "identifier", genericExecutionSetting);
   * <p/>
   * // Check result
   * verify(resultReceiver).receiveResult(isA(InclusionDependency.class));
   * <p/>
   * HibernateUtil.clear();
   * }
   * <p/>
   * /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * <p/>
   * The {@link de.metanome.algorithms.testing.example_relational_input_algorithm.ExampleAlgorithm}
   * should be executable by generating a {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}
   * from a file.
   */


  @Test
  public void testRelationalInputAlgorithm()
    throws Exception {
    HibernateUtil.clear();
    DefaultConfigurationFactory configurationFactory = new DefaultConfigurationFactory();
    // Setup
    String path = new FileFixture("some file content").getTestData("some file name").getPath();
    List<ConfigurationValue> configurationValues = new ArrayList<>();
    ConfigurationRequirementRelationalInput
      requirementRelationalInput =
      new ConfigurationRequirementRelationalInput(
        ExampleAlgorithm.RELATIONAL_INPUT_IDENTIFIER);
    requirementRelationalInput.checkAndSetSettings(new ConfigurationSettingFileInput(path));

    //usually input parsing would/should happen here as well (compare AlgorithmExecutionResource)
    configurationValues.add(requirementRelationalInput.build(configurationFactory));

    Algorithm algorithm = new Algorithm("example_relational_input_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    // Check result
    executor.executeAlgorithm(algorithm, configurationValues, null, "identifier", genericExecutionSetting);

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Tests the execution of an Ucc algorithm.
   */

  @Test
  public void testExecuteUniqueColumnCombinationsAlgorithm()
    throws Exception {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString(de.metanome.algorithms.testing.example_ucc_algorithm.ExampleAlgorithm.STRING_IDENTIFIER, "path/to/file1", "path/to/file2"));

    Algorithm algorithm = new Algorithm("example_ucc_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithm(algorithm, configs, null, "identifier", genericExecutionSetting);

    // Check result
    verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));
    // After finishing the progress should be 1;
    //verify(progressCache).updateProgress(1);

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Tests the execution of an holistic algorithm.
   */

  @Test
  public void testExecuteHolisticAlgorithm()
    throws Exception {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString("pathToOutputFile", "path/to/file1", "path/to/file1"));

    Algorithm algorithm = new Algorithm("example_holistic_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithm(algorithm, configs, null, "identifier", genericExecutionSetting);

    // Check result
    verify(resultReceiver).receiveResult(isA(FunctionalDependency.class));
    verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));

    HibernateUtil.clear();
  }
  
  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Tests the execution of an conditional inclusion dependency algorithm.
   */

  @Test
  public void testExecuteConditionalInclusionDependencyAlgorithm()
    throws Exception { 
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configs = new ArrayList<>();
    configs.add(new ConfigurationValueString(de.metanome.algorithms.testing.example_cid_algorithm.ExampleAlgorithm.STRING_IDENTIFIER, "path/to/file1"));
    configs.add(new ConfigurationValueInteger(de.metanome.algorithms.testing.example_cid_algorithm.ExampleAlgorithm.INTEGER_IDENTIFIER, 7));
    configs.add(new ConfigurationValueFileInputGenerator(de.metanome.algorithms.testing.example_cid_algorithm.ExampleAlgorithm.CSV_FILE_IDENTIFIER, mock(FileInputGenerator.class)));
    
    Algorithm algorithm = new Algorithm("example_cid_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    Execution execution = executor.executeAlgorithm(algorithm, configs, null, "identifier", genericExecutionSetting);

    // Check result
    verify(resultReceiver).receiveResult(isA(ConditionalInclusionDependency.class));
    assertTrue(0 <= execution.getEnd() - execution.getBegin());

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Algorithms that do not implement the metanome interfaces directly should
   * still be executable.
   */

  @Test
  public void testExecuteIndirectInterfaceAlgorithm()
    throws Exception {
    HibernateUtil.clear();

    // Setup
    List<ConfigurationValue> configurationValues = new LinkedList<>();
    configurationValues.add(new ConfigurationValueRelationalInputGenerator("identifier", mock(
      RelationalInputGenerator.class)));

    Algorithm algorithm = new Algorithm("example_indirect_interfaces_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithm(algorithm, configurationValues, null, "identifier",
      genericExecutionSetting);

    // Check result
    verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));

    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * When executing an {@link de.metanome.algorithm_integration.Algorithm} an
   * {@link de.metanome.backend.results_db.Execution} should be saved in the results database.
   */

  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testExecutionStoredInDatabase()
    throws Exception {
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
    executor.executeAlgorithm(algorithm, configurationValues, inputs, "identifier", genericExecutionSetting);
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

    List<Result> results = (List<Result>) HibernateUtil.queryCriteria(Result.class);

    assertTrue(results.size() > 0);
    assertEquals(results.get(0).getExecution(), actualExecution);

    // Cleanup
    HibernateUtil.clear();
  }

  @Test(expected = Exception.class)
  public void testExecutionWithWrongFileName()
    throws Exception {
    // Setup
    HibernateUtil.clear();

    List<ConfigurationValue> configurationValues = new LinkedList<>();
    configurationValues.add(new ConfigurationValueRelationalInputGenerator("identifier", mock(
      RelationalInputGenerator.class)));

    Algorithm algorithm = new Algorithm("wrong_algorithm.jar");

    // Execute functionality
    executor.executeAlgorithm(algorithm, configurationValues, null, "identifier", genericExecutionSetting);

    // Setup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor#executeAlgorithm(de.metanome.backend.results_db.Algorithm, java.util.List, java.util.List, String, ExecutionSetting)}
   * Tests the execution of a basic statistics algorithm that requires several
   * {@link de.metanome.algorithm_integration.input.FileInputGenerator}s to run.
   */

  @Test
  public void testExecuteBasicStatisticsAlgorithmWithFileInputGenerator()
    throws Exception {
    HibernateUtil.clear();

    // Setup
    // Build file input specification
    int numberOfInputs = 5;
    List<ConfigurationValue> configurationValues = new LinkedList<>();
    DefaultConfigurationFactory configurationFactory = new DefaultConfigurationFactory();
    ConfigurationRequirementFileInput
      specification =
      new ConfigurationRequirementFileInput(BasicStatAlgorithm.INPUT_FILE_IDENTIFIER,
        numberOfInputs);

    // Build input files
    String expectedFilePath = "some value";
    String expectedOtherFileName = "some other file name";
    FileFixture fileFixture = new FileFixture("some file content");
    expectedOtherFileName = fileFixture.getTestData(expectedOtherFileName).getAbsolutePath();
    expectedFilePath = fileFixture.getTestData(expectedFilePath).getAbsolutePath();

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
    when(lastSetting.getFileName()).thenReturn(expectedFilePath);
    settings[4] = lastSetting;
    specification.checkAndSetSettings(settings);

    configurationValues.add(specification.build(configurationFactory));

    //usually input parsing would/should happen here as well (compare AlgorithmExecutionResource)

    Algorithm algorithm = new Algorithm("example_basic_stat_algorithm.jar");
    algorithm = resource.store(algorithm);

    // Execute functionality
    executor.executeAlgorithm(algorithm, configurationValues, null, "identifier", genericExecutionSetting);

    // Check result
    ArgumentCaptor<BasicStatistic> captor = ArgumentCaptor.forClass(BasicStatistic.class);
    verify(resultReceiver, times(2)).receiveResult(captor.capture());
    assertEquals(BasicStatAlgorithm.STATISTIC_VALUE_MIN, captor.getValue().getStatisticMap().get(BasicStatAlgorithm.STATISTIC_NAME_MIN).getValue());

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
