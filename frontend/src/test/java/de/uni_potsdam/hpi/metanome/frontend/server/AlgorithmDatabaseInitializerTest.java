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

import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmFinder;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;
import de.uni_potsdam.hpi.metanome.results_db.AlgorithmContentEquals;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.HibernateUtil;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import javax.servlet.ServletContextEvent;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link AlgorithmDatabaseInitializer}
 *
 * @author Jakob Zwiener
 */
public class AlgorithmDatabaseInitializerTest {

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.AlgorithmDatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
     * <p/>
     * All algorithm jars should be represented by algorithms in the database with correct file names as key.
     * Algorithms should have the correct types assigned.
     */
    @Test
    public void testContextInitialized() throws IOException, ClassNotFoundException {
        // Setup
        HibernateUtil.clear();

        AlgorithmFinder jarFinder = new AlgorithmFinder();
        AlgorithmDatabaseInitializer initializer = new AlgorithmDatabaseInitializer();

        // Expected values
        String[] algorithmFileNames = jarFinder.getAvailableAlgorithmFileNames(null);
        Algorithm[] expectedAlgorithms = new Algorithm[algorithmFileNames.length];
        for (int i = 0; i < algorithmFileNames.length; i++) {
            expectedAlgorithms[i] = buildExpectedAlgorithm(jarFinder, algorithmFileNames[i]);
        }

        // Execute functionality
        initializer.contextInitialized(mock(ServletContextEvent.class));
        List<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

        // Check result
        assertThat(actualAlgorithms, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithms));
        // Check algorithm fields
        for (Algorithm expectedAlgorithm : expectedAlgorithms) {
            // Get the matching algorithm based on the file name (equals method).
            Algorithm actualAlgorithm = actualAlgorithms.get(actualAlgorithms.indexOf(expectedAlgorithm));
            assertTrue(AlgorithmContentEquals.contentEquals(expectedAlgorithm, actualAlgorithm));
        }

        // Cleanup
        HibernateUtil.clear();
    }

    protected Algorithm buildExpectedAlgorithm(AlgorithmFinder jarFinder, String algorithmFileName) throws IOException, ClassNotFoundException {
        Set<Class<?>> algorithmInterfaces = jarFinder.getAlgorithmInterfaces(algorithmFileName);

        return new Algorithm(algorithmFileName, algorithmInterfaces);
    }

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.AlgorithmDatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
     * <p/>
     * If the algorithm table is already populated it should not be initialized.
     */
    @Test
    public void testContextInitializedNotEmpty() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        AlgorithmDatabaseInitializer initializer = new AlgorithmDatabaseInitializer();
        // Expected values
        Algorithm expectedAlgorithm = new Algorithm("some file name");
        Algorithm.store(expectedAlgorithm);

        // Execute functionality
        initializer.contextInitialized(mock(ServletContextEvent.class));
        List<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

        // Check result
        assertThat(actualAlgorithms, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

        // Cleanup
        HibernateUtil.clear();
    }
}