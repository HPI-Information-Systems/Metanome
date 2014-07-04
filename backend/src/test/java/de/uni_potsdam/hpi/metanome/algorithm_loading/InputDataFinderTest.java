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

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder}
 */
public class InputDataFinderTest {

  InputDataFinder inputDataFinder;

  @Before
  public void setUp() {
    inputDataFinder = new InputDataFinder();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder#retrieveCsvFiles(String)}
   * <p/> When run on the a folder without any .csv files, the finder should not return any files.
   * Otherwise it should return the correct number of csv files.
   */
  @Test
  public void testRetrieveCsvFiles() throws UnsupportedEncodingException {
    //Setup
    String
        pathToAlgorithmsFolder =
        Thread.currentThread().getContextClassLoader().getResource("algorithms").getPath();
    String
        pathToCsvFolder =
        Thread.currentThread().getContextClassLoader().getResource("inputData").getPath();

    //Execute
    File[] csvsInAlgorithmsFolder = inputDataFinder.retrieveCsvFiles(pathToAlgorithmsFolder);
    File[] csvsInCsvFolder = inputDataFinder.retrieveCsvFiles(pathToCsvFolder);

    //Check
    assertEquals(0, csvsInAlgorithmsFolder.length);
    assertEquals(2, csvsInCsvFolder.length);
  }

  /**
   * Test method for {@link InputDataFinder#getAvailableCsvs()}
   *
   * The method should retrieve the correct number of csv input files.
   */
  @Test
  public void testRetrieveAllCsvFiles() throws IOException, ClassNotFoundException {
    //Execute
    File[] actualCsvs = inputDataFinder.getAvailableCsvs();

    //Check
    assertEquals(2, actualCsvs.length);
  }
}
