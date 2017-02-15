/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.visualization.UniqueColumnCombination;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureGeneral;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UniqueColumnCombinationVisualizationTest {

  TableInformation tableInformation;
  List<UniqueColumnCombinationResult> results;
  String tableName = FileFixtureGeneral.TABLE_NAME;

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

    tableInformation = new TableInformation(relationalInputGenerator, false, new BitSet());

    ColumnIdentifier column1 = new ColumnIdentifier(tableName, "column1");
    ColumnIdentifier column2 = new ColumnIdentifier(tableName, "column2");
    ColumnIdentifier column4 = new ColumnIdentifier(tableName, "column4");

    UniqueColumnCombination ucc1 = new UniqueColumnCombination(column1);
    UniqueColumnCombination ucc2 = new UniqueColumnCombination(column2, column4);

    UniqueColumnCombinationResult result1 = new UniqueColumnCombinationResult(ucc1);
    UniqueColumnCombinationResult result2 = new UniqueColumnCombinationResult(ucc2);

    results = new ArrayList<>();
    results.add(result1);
    results.add(result2);
  }

  @Test
  public void testSetUp() {
    // Execute functionality
    UniqueColumnCombinationVisualization visualization =
        new UniqueColumnCombinationVisualization(results, tableInformation);

    // Check
    assertNotNull(visualization.results);
    assertNotNull(visualization.columnUniqueness);
  }

  @Test
  public void testCreateVisualizationResult() {
    // Set up
    UniqueColumnCombinationVisualization visualization =
        new UniqueColumnCombinationVisualization(results, tableInformation);
    UniqueColumnCombinationResult result = results.get(1);
    result.setRandomness(0.3f);

    // Execute functionality
    UniqueColumnCombinationVisualizationData data = visualization.createVisualizationResult(result);

    // Check
    assertEquals(result.getColumnCombination(), data.getColumnCombination());
    assertEquals(0.5, data.getMinUniqueness(), 0.00);
    assertEquals(0.75, data.getMaxUniqueness(), 0.00);
    assertEquals(0.625, data.getAvgUniqueness(), 0.00);
    assertEquals(0.25, data.getMinDistance(), 0.00);
    assertEquals(0.25, data.getMaxDistance(), 0.00);
    assertEquals(0.25, data.getMedianDistance(), 0.00);
    assertEquals(2.0, data.getColumnCount(), 0.00);
    assertEquals(result.getRandomness(), data.getRandomness(), 0.00);
  }

}
