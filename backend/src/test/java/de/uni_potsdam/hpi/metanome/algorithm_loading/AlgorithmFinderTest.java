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

package de.uni_potsdam.hpi.metanome.algorithm_loading;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO docs
 */
public class AlgorithmFinderTest {

  /**
   * A valid algorithm jar should be loadable and of correct class.
   */
  @Test
  public void getAlgorithmType()
      throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
             IllegalArgumentException, SecurityException, InvocationTargetException,
             NoSuchMethodException {
    // Setup
    String
        jarFilePath =
        Thread.currentThread().getContextClassLoader()
            .getResource("algorithms/example_ucc_algorithm.jar").getFile();
    File file = new File(URLDecoder.decode(jarFilePath, "utf-8"));

    // Execute functionality
    Set<Class<?>> algorithmInterfaces = new AlgorithmFinder().getAlgorithmInterfaces(file);

    // Check result
    assertNotNull(algorithmInterfaces);
    assertNotEquals(0, algorithmInterfaces.size());
    assertTrue(algorithmInterfaces.contains(UniqueColumnCombinationsAlgorithm.class));
  }

  /**
   * A valid algorithm jar should be loadable and of correct class.
   */
  @Test
  public void getAlgorithmTypeByFileName()
      throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
             IllegalArgumentException, SecurityException, InvocationTargetException,
             NoSuchMethodException {
    // Execute functionality
    Set<Class<?>>
        algorithmInterfaces =
        new AlgorithmFinder().getAlgorithmInterfaces("example_ucc_algorithm.jar");

    // Check result
    assertNotNull(algorithmInterfaces);
    assertNotEquals(0, algorithmInterfaces.size());
    assertTrue(algorithmInterfaces.contains(UniqueColumnCombinationsAlgorithm.class));
  }

  /**
   * TODO docs
   */
  @Test
  public void retrieveAllJarFiles() throws IOException, ClassNotFoundException {
    // Setup
    AlgorithmFinder algoFinder = new AlgorithmFinder();

    //Execute
    String[] algos = algoFinder.getAvailableAlgorithmFileNames(null);

    //Check
    assertTrue(algos.length > 0);
  }

  /**
   * Test method for {@link AlgorithmFinder#getAvailableAlgorithmFileNames(Class)} <p/> Should
   * return the algorithms implementing the asked interface. Should not fail the whole search only,
   * because one algorithm does not have the bootstrap class parameter set correclty.
   */
  @Test
  public void testRetrieveUniqueColumnCombinationJarFiles()
      throws IOException, ClassNotFoundException {
    // Setup
    AlgorithmFinder algoFinder = new AlgorithmFinder();

    //Execute
    String[]
        algos =
        algoFinder.getAvailableAlgorithmFileNames(UniqueColumnCombinationsAlgorithm.class);

    //Check
    assertEquals(3, algos.length); //TODO determine number of expected algorithms dynamically
    //TODO make sure no wrong algorithms are returned
  }
}
