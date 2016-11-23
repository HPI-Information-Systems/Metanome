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
package de.metanome.backend.algorithm_loading;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.metanome.algorithms.testing.example_ucc_algorithm.ExampleAlgorithm;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link AlgorithmJarLoader}
 */
public class AlgorithmJarLoaderTest {

  /**
   * A valid algorithm jar should be loadable and of correct class.
   */
  @Test
  public void loadAlgorithm()
    throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
    IllegalArgumentException, SecurityException, InvocationTargetException,
    NoSuchMethodException, AlgorithmExecutionException {
    // Setup
    AlgorithmJarLoader loader = new AlgorithmJarLoader();

    // Execute functionality
    Algorithm algorithm = loader.loadAlgorithm("example_ucc_algorithm.jar");

    // Check result
    assertNotNull(algorithm);
    assertTrue(algorithm instanceof UniqueColumnCombinationsAlgorithm);

    assertEquals(ExampleAlgorithm.AUTHORS, algorithm.getAuthors());
    assertEquals(ExampleAlgorithm.DESCRIPTION, algorithm.getDescription());

    UniqueColumnCombinationsAlgorithm uccAlgorithm = (UniqueColumnCombinationsAlgorithm) algorithm;
    uccAlgorithm.setResultReceiver(mock(OmniscientResultReceiver.class));

    algorithm.execute();
  }
}
