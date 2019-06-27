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

import de.metanome.backend.constants.Constants;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.metanome.backend.algorithm_loading.InputDataFinder}
 */
public class InputDataFinderTest {

  InputDataFinder inputDataFinder;

  @Before
  public void setUp() {
    inputDataFinder = new InputDataFinder();
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_loading.InputDataFinder#retrieveCsvTsvFiles(String, boolean)}
   * <p/> When run on the a folder without any .csv files, the finder should not return any files.
   * Otherwise it should return the correct number of csv files.
   */
  @Test
  public void testRetrieveFiles() throws UnsupportedEncodingException {
    //Setup
    String
      pathToAlgorithmsFolder =
      Thread.currentThread().getContextClassLoader().getResource(Constants.ALGORITHMS_RESOURCE_NAME).getPath();
    String
      pathToCsvFolder =
      Thread.currentThread().getContextClassLoader().getResource(Constants.INPUTDATA_RESOURCE_NAME).getPath();

    //Execute
    File[] filesInAlgorithmsFolder = inputDataFinder.retrieveCsvTsvFiles(pathToAlgorithmsFolder, true);
    File[] filesInInputFolder = inputDataFinder.retrieveCsvTsvFiles(pathToCsvFolder, true);

    //Check
    assertEquals(0, filesInAlgorithmsFolder.length);
    assertEquals(10, filesInInputFolder.length);
  }

  /**
   * Test method for {@link InputDataFinder#getAvailableFiles(boolean)}
   * <p/>
   * The method should retrieve the correct number of csv input files.
   */
  @Test
  public void testRetrieveAllFiles() throws IOException, ClassNotFoundException {
    //Execute
    File[] actualDirFiles = inputDataFinder.getAvailableFiles(true);
    File[] actualFiles = inputDataFinder.getAvailableFiles(false);
    //Check
    assertEquals(10, actualDirFiles.length);
    assertEquals(9, actualFiles.length);
  }
}
