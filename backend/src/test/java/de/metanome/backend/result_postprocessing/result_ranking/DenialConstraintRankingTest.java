/**
 * Copyright 2017 by Metanome Project
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
package de.metanome.backend.result_postprocessing.result_ranking;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import de.metanome.algorithm_integration.Predicate;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.DenialConstraint;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureGeneral;
import de.metanome.backend.result_postprocessing.results.DenialConstraintResult;

public class DenialConstraintRankingTest {
  List<DenialConstraintResult> denialConstraintResults;

  @Before
  public void setUp() throws Exception {
    final FileFixtureGeneral fileFixture = new FileFixtureGeneral();
    RelationalInputGenerator relationalInputGenerator = new RelationalInputGenerator() {
      @Override
      public RelationalInput generateNewCopy() throws InputGenerationException {
        try {
          return fileFixture.getTestData();
        } catch (InputIterationException e) {
          return null;
        }
      }
      @Override
      public void close() throws Exception {}
    };
    
    DenialConstraint dc1 = new DenialConstraint(mock(Predicate.class));
    DenialConstraint dc2 = new DenialConstraint(mock(Predicate.class));

    DenialConstraintResult result1 = new DenialConstraintResult(dc1);
    DenialConstraintResult result2 = new DenialConstraintResult(dc2);

    denialConstraintResults = new ArrayList<>();
    denialConstraintResults.add(result1);
    denialConstraintResults.add(result2);
  }

  @Test
  public void testInitialization() throws Exception {
    // Execute functionality
    DenialConstraintResultRanking ranking = new DenialConstraintResultRanking(denialConstraintResults, null);

    // Check
    assertNotNull(ranking.results);
  }
}
