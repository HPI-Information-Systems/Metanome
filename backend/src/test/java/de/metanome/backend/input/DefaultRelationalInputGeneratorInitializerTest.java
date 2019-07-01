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
package de.metanome.backend.input;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator;
import de.metanome.backend.input.file.FileFixture;
import de.metanome.backend.results_db.AlgorithmType;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test method for {@link de.metanome.backend.input.DefaultRelationalInputGeneratorInitializer}
 *
 * @author Jakob Zwiener
 */
public class DefaultRelationalInputGeneratorInitializerTest {

  /**
   * Test method for {@link de.metanome.backend.input.DefaultRelationalInputGeneratorInitializer#DefaultRelationalInputGeneratorInitializer(de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput)}
   * and {@link DefaultRelationalInputGeneratorInitializer#getConfigurationValue()}
   * <p/>
   * The Initializer should correctly build {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}s
   * from files.
   */
  @Test
  public void testInitialization()
    throws AlgorithmConfigurationException, FileNotFoundException, UnsupportedEncodingException,
    InputGenerationException, InputIterationException {
    // Setup
    String expectedIdentifier = "some identifier";
    ConfigurationRequirementRelationalInput
      requirement =
      new ConfigurationRequirementRelationalInput(
        expectedIdentifier, 2);
    String expectedFileName1 = "some file name";
    String expectedFileName2 = "some other file name";
    String
      expectedFilePath1 =
      new FileFixture("some file content").getTestData(expectedFileName1).getPath();
    String
      expectedFilePath2 =
      new FileFixture("some other file content").getTestData(expectedFileName2).getPath();
    requirement.checkAndSetSettings(new ConfigurationSettingFileInput(expectedFilePath1),
      new ConfigurationSettingFileInput(expectedFilePath2));

    // Execute functionality
    DefaultRelationalInputGeneratorInitializer
      initializer = new DefaultRelationalInputGeneratorInitializer(requirement);
    ConfigurationValueRelationalInputGenerator actualValue = initializer.getConfigurationValue();

    // Check result
    RelationalInputParameterAlgorithm algorithm = mock(RelationalInputParameterAlgorithm.class);
    Set<Class<?>> interfaces = new HashSet<>();
    interfaces.add(AlgorithmType.RELATIONAL_INPUT.getAlgorithmClass());

    // Let the values be set on the mock algorithm and verify that the correct values have been set.
    actualValue.triggerSetValue(algorithm, interfaces);

    ArgumentCaptor<RelationalInputGenerator>
      captor = ArgumentCaptor.forClass(RelationalInputGenerator.class);
    verify(algorithm).setRelationalInputConfigurationValue(eq(expectedIdentifier),
      captor.capture(), captor.capture());
    List<RelationalInputGenerator> actualValueList = captor.getAllValues();

    assertEquals(actualValueList.get(0).generateNewCopy().relationName(), expectedFileName1);
    assertEquals(actualValueList.get(1).generateNewCopy().relationName(), expectedFileName2);
  }

}
