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

import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution}
 *
 * @author Jakob Zwiener
 */
public class ExecutionTest {

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution#store(Execution)} and {@link de.uni_potsdam.hpi.metanome.results_db.Execution#retrieve(Algorithm, java.util.Date)}
     * <p/>
     * Executions should be storable and retrievable by id.
     */
    @Test
    public void testPersistence() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        // Store prerequisite objects in the database.
        Algorithm algorithm = new Algorithm("some file path");
        Algorithm.store(algorithm);

        // Expected values
        Date begin = new Date();
        Execution expectedExecution = new Execution(algorithm, begin);

        // Execute functionality
        Execution.store(expectedExecution);
        Execution actualExecution = Execution.retrieve(algorithm, begin);

        // Check result
        assertEquals(expectedExecution, actualExecution);

        // Cleanup
        HibernateUtil.clear();
    }

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution#store(Execution)} and
     * {@link de.uni_potsdam.hpi.metanome.results_db.Execution#retrieve(Algorithm, java.util.Date)}
     *
     * After roundtripping an execution all its {@link de.uni_potsdam.hpi.metanome.results_db.Input}s should be retrievable from it.
     */
    @Test
    public void testPersistenceMultipleInputs() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        // Store prerequisite objects in the database
        Algorithm algorithm = new Algorithm("some path");
        Algorithm.store(algorithm);
        Input input1 = new Input();
        Input.store(input1);
        Input input2 = new Input();
        Input.store(input2);
        Collection<Input> inputs = new ArrayList<>();
        inputs.add(input1);
        inputs.add(input2);

        // Expected values
        Date begin = new Date();
        Execution expectedExecution = new Execution(algorithm, begin);
        expectedExecution.setInputs(inputs);

        // Execute functionality
        Execution.store(expectedExecution);
        Execution actualExecution = Execution.retrieve(algorithm, begin);
        Collection<Input> actualInputs = actualExecution.getInputs();

        // Check result
        assertEquals(expectedExecution, actualExecution);
        assertEquals(inputs, actualInputs);

        // Cleanup
        HibernateUtil.clear();
    }

    /**
     * Test method for {@link Execution#equals(Object)} and {@link Execution#hashCode()}
     */
    @Test
    public void testEqualsAndHashCode() {
        // Setup
        Algorithm algorithm = new Algorithm("some algorithm file name");
        Date begin = new Date();
        Execution execution = new Execution(algorithm, begin);
        Execution equalExecution = new Execution(algorithm, begin);
        Execution notEqualExecution = new Execution(new Algorithm("some other file name"), new Date(197));

        // Execute functionality
        // Check result
        new EqualsAndHashCodeTester<Execution>()
                .performBasicEqualsAndHashCodeChecks(execution, equalExecution, notEqualExecution);
    }

    /**
     * Test method for {@link Execution#addInput(Input)}
     * <p/>
     * After adding an input to the execution the list of inputs should contain the added input.
     */
    @Test
    public void testAddInput() {
        // Setup
        Execution execution = new Execution(mock(Algorithm.class), mock(Date.class));
        // Expected values
        Input input1 = new Input();
        input1.setId(42);
        Input input2 = new Input();
        input2.setId(23);

        // Execute functionality
        // Check result
        Collection<Input> actualInputs = execution.getInputs();
        assertTrue(actualInputs.isEmpty());

        execution.addInput(input1);
        actualInputs = execution.getInputs();
        assertTrue(actualInputs.contains(input1));

        execution.addInput(input2);
        actualInputs = execution.getInputs();
        assertEquals(2, actualInputs.size());
        assertTrue(actualInputs.contains(input1));
        assertTrue(actualInputs.contains(input2));
    }

}
