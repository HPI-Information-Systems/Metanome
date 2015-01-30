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

package de.metanome.backend.algorithm_loading;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AlgorithmAnalyzerTest {

  @Test
  public void analyzeInclusionDependencyAlgorithmTest()
      throws IllegalAccessException, InvocationTargetException, IOException, InstantiationException,
             NoSuchMethodException, ClassNotFoundException {
    // Setup
    String algorithmFileName = "example_ind_algorithm.jar";

    // Execute
    AlgorithmAnalyzer analyzer = new AlgorithmAnalyzer(algorithmFileName);

    // Check
    HashSet<AlgorithmType> expectedTypes = new HashSet<>();
    expectedTypes.add(AlgorithmType.inclusionDependency);
    expectedTypes.add(AlgorithmType.tempFile);
    expectedTypes.add(AlgorithmType.fileInput);

    testTypes(expectedTypes, analyzer);
  }

  @Test
  public void analyzeFunctionalDependencyAlgorithmTest()
      throws IllegalAccessException, InvocationTargetException, IOException, InstantiationException,
             NoSuchMethodException, ClassNotFoundException {
    // Setup
    String algorithmFileName = "example_fd_algorithm.jar";

    // Execute
    AlgorithmAnalyzer analyzer = new AlgorithmAnalyzer(algorithmFileName);

    // Check
    HashSet<AlgorithmType> expectedTypes = new HashSet<>();
    expectedTypes.add(AlgorithmType.functionalDependency);
    expectedTypes.add(AlgorithmType.fileInput);
    expectedTypes.add(AlgorithmType.databaseConnection);

    testTypes(expectedTypes, analyzer);
  }

  @Test
  public void analyzeOrderDependencyAlgorithmTest()
      throws IllegalAccessException, InvocationTargetException, IOException, InstantiationException,
             NoSuchMethodException, ClassNotFoundException {
    // Setup
    String algorithmFileName = "example_od_algorithm.jar";

    // Execute
    AlgorithmAnalyzer analyzer = new AlgorithmAnalyzer(algorithmFileName);

    // Check
    HashSet<AlgorithmType> expectedTypes = new HashSet<>();
    expectedTypes.add(AlgorithmType.orderDependency);

    testTypes(expectedTypes, analyzer);
  }

  private void testTypes(HashSet<AlgorithmType> expectedTypes,
                         AlgorithmAnalyzer analyzer) {
    for (AlgorithmType type : AlgorithmType.values()) {
      if (expectedTypes.contains(type)) {
        assertTrue(analyzer.hasType(type));
      } else {
        assertFalse(analyzer.hasType(type));
      }
    }
  }

}
