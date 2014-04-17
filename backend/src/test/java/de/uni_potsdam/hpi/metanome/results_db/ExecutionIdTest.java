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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link de.uni_potsdam.hpi.metanome.results_db.ExecutionId}
 *
 * @author Jakob Zwiener
 */
public class ExecutionIdTest {

    /**
     * Test method for {@link ExecutionId#equals(Object)} and {@link ExecutionId#hashCode()}
     */
    @Test
    public void testEqualsHashCode() {
        // Setup
        String expectedAlgorithmFilePath = "some algorithm";
        Date date = new Date();
        ExecutionId executionId = new ExecutionId(new Algorithm(expectedAlgorithmFilePath), date);
        ExecutionId equalExecutionId = new ExecutionId(new Algorithm(expectedAlgorithmFilePath), new Date(date.getTime()));
        ExecutionId notEqualExecutionId = new ExecutionId(new Algorithm("some other algorithm"), new Date(97123));

        // Execute functionality
        // Check result

        // Reflexivity
        assertEquals(executionId, executionId);
        assertEquals(executionId.hashCode(), executionId.hashCode());

        assertNotSame(executionId, equalExecutionId);
        assertEquals(executionId, equalExecutionId);
        assertEquals(executionId.hashCode(), equalExecutionId.hashCode());

        assertNotEquals(executionId, null);

        assertNotEquals(executionId, notEqualExecutionId);
        assertNotEquals(executionId.hashCode(), notEqualExecutionId.hashCode());
    }

    /**
     * Test method for {@link ExecutionId#getAlgorithm()} and {@link de.uni_potsdam.hpi.metanome.results_db.ExecutionId#setAlgorithm(Algorithm)}
     */
    @Test
    public void testGetAndSetAlgorithm() {
        // Setup
        ExecutionId executionId = new ExecutionId(mock(Algorithm.class), mock(Date.class));
        // Expected values
        Algorithm expectedAlgorithm = mock(Algorithm.class);

        // Execute functionality
        executionId.setAlgorithm(expectedAlgorithm);
        Algorithm actualAlgorithm = executionId.getAlgorithm();

        // Check result
        assertEquals(expectedAlgorithm, actualAlgorithm);
    }

    /**
     * Test method for {@link ExecutionId#getBegin()} and {@link de.uni_potsdam.hpi.metanome.results_db.ExecutionId#setBegin(java.util.Date)}
     */
    @Test
    public void testGetAndSetBegin() {
        // Setup
        ExecutionId executionId = new ExecutionId(mock(Algorithm.class), mock(Date.class));
        // Expected values
        Date expectedBegin = mock(Date.class);

        // Execute functionality
        executionId.setBegin(expectedBegin);
        Date actualBegin = executionId.getBegin();

        // Check result
        assertEquals(expectedBegin, actualBegin);
    }
}
