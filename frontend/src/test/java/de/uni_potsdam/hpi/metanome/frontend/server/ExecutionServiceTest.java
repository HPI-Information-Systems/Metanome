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

package de.uni_potsdam.hpi.metanome.frontend.server;

import static junit.framework.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExecutionServiceTest {

    ExecutionServiceImpl executionService = new ExecutionServiceImpl();
//    InputParameterString stringParam = new InputParameterString("test");
//    InputParameterBoolean boolParam = new InputParameterBoolean("boolean");
//    InputParameterCsvFile csvParam = new InputParameterCsvFile("inputFile");
//    InputParameterSQLIterator sqlParam = new InputParameterSQLIterator("db connection");

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    /**
     * Test the conversion of InputParameters (frontend) to ConfigurationValues (backend)
     *
     * @throws AlgorithmConfigurationException
     */
//    @Test
//    public void testConvertToInputParameter() throws AlgorithmConfigurationException {
//        //Setup
//        csvParam.setFileNameValue(Thread.currentThread().getContextClassLoader().getResource("inputData").getPath() + "/inputA.csv");
//
//        //Execute
//        ConfigurationValue confString = executionService.convertToConfigurationValue(stringParam);
//        ConfigurationValue confBool = executionService.convertToConfigurationValue(boolParam);
//        ConfigurationValue confCsv = executionService.convertToConfigurationValue(csvParam);
//
//        //Check
//        assertTrue(confString instanceof ConfigurationValueString);
//        assertTrue(confBool instanceof ConfigurationValueBoolean);
//        assertTrue(confCsv instanceof ConfigurationValueRelationalInputGenerator);
//    }

    /**
     * Make sure an AlgorithmConfigurationException is thrown when trying to build a CsvFileGenerator on an invalid path
     * TODO test positive case as well
     */
//    @Test
//    public void testBuildCsvFileGenerator() {
//        //Setup
//        csvParam.setFileNameValue("some/file/path");
//
//        //Execute
//        try {
//            executionService.buildCsvFileGenerator(csvParam);
//            fail("Expected ConfigurationException due to unavailable file was not thrown.");
//        } catch (AlgorithmConfigurationException e) {
//            //Test successful.
//        }
//    }

    /**
     * Make sure an AlgorithmConfigurationException is thrown when trying to build an SqlIterator on an invalid DB connection
     * TODO test positive case as well
     */
//    @Test
//    public void testBuildSqlIteratorGenerator() {
//        try {
//            executionService.convertToConfigurationValue(sqlParam);
//            fail("Expected ConfigurationException due to failed DB connection was not thrown.");
//        } catch (AlgorithmConfigurationException e) {
//            //Test successful.
//        }
//    }

    /**
     * Test method for {@link ExecutionServiceImpl#fetchProgress(String)}
     * <p/>
     * When fetching the current progress for an execution the correct progress should be returned.
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testFetchProgress() throws FileNotFoundException, UnsupportedEncodingException {
        // Setup
        // Expected values
        String expectedExecutionIdentifier = "executionIdentifier";
        executionService.buildExecutor(expectedExecutionIdentifier);
        float expectedProgress = 0.42f;

        // Execute functionality
        executionService.currentProgressCaches.get(expectedExecutionIdentifier).updateProgress(expectedProgress);
        float actualProgress = executionService.fetchProgress(expectedExecutionIdentifier);

        // Check result
        assertEquals(expectedProgress, actualProgress);
    }

}
