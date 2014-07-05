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

package de.uni_potsdam.hpi.metanome.example_indirect_interfaces_algorithm;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.ProgressReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.example_indirect_interfaces_algorithm.IndirectInterfacesAlgorithm}
 */
public class IndirectInterfacesAlgorithmTest {

  /**
   * Test method for {@link IndirectInterfacesAlgorithm#execute()} <p/> The algorithm should only
   * return a result if the result receiver, the progress receiver and the input are set.
   */
  @Test
  public void testExecute() throws AlgorithmExecutionException {
    // Setup
    IndirectInterfacesAlgorithm algorithm = new IndirectInterfacesAlgorithm();
    // Expected values
    UniqueColumnCombinationResultReceiver
        resultReceiver =
        mock(UniqueColumnCombinationResultReceiver.class);

    // Execute functionality
    // Check result
    algorithm.execute();
    verify(resultReceiver, never()).receiveResult(any(UniqueColumnCombination.class));

    algorithm.setResultReceiver(resultReceiver);
    algorithm.execute();
    verify(resultReceiver, never()).receiveResult(any(UniqueColumnCombination.class));

    algorithm.setProgressReceiver(mock(ProgressReceiver.class));
    algorithm.execute();
    verify(resultReceiver, never()).receiveResult(any(UniqueColumnCombination.class));

    algorithm
        .setRelationalInputConfigurationValue("identifier", mock(RelationalInputGenerator.class));
    algorithm.execute();
    verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));
  }
}
