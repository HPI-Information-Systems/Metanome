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

package de.uni_potsdam.hpi.metanome.results_db;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution}
 *
 * @author Jakob Zwiener
 */
public class ExecutionTest {

    /**
     * TODO docs
     * <p/>
     * Executions should be storable and retrievable by id.
     */
    @Test
    public void testPersistance() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        // Expected values
        Algorithm expectedAlgorithm = new Algorithm("some file path");
        Date expectedDate = new Date();
        Execution expectedExecution = new Execution(expectedAlgorithm, expectedDate);

        // Execute functionality
        Algorithm.store(expectedAlgorithm);
        Execution.store(expectedExecution);
        Execution actualExecution = Execution.retrieve(expectedAlgorithm, expectedDate);

        // Check result
        assertEquals(expectedExecution, actualExecution);

        // Cleanup
        HibernateUtil.clear();
    }

}
