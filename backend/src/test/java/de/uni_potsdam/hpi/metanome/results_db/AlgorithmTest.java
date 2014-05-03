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

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.*;
import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm}
 *
 * @author Jakob Zwiener
 */
public class AlgorithmTest {

    /**
     * Test method for {@link Algorithm#Algorithm(String, java.util.List)}
     *
     * The algorithm should have the appropriate algorithm types set, based on the implemented interfaces.
     */
    @Test
    public void testConstructorWithInterfaces() {
        // Setup
        List<Class<?>> algorithmInterfaces = new LinkedList<>();
        algorithmInterfaces.add(UniqueColumnCombinationsAlgorithm.class);
        algorithmInterfaces.add(InclusionDependencyAlgorithm.class);
        algorithmInterfaces.add(ProgressEstimatingAlgorithm.class);
        algorithmInterfaces.add(FunctionalDependencyAlgorithm.class);
        algorithmInterfaces.add(BasicStatisticsAlgorithm.class);
        // Expected values
        String expectedFileName = "some file name";

        // Execute functionality
        Algorithm actualAlgorithm = new Algorithm(expectedFileName, algorithmInterfaces);

        // Check result
        assertEquals(expectedFileName, actualAlgorithm.getFileName());
        assertTrue(actualAlgorithm.isInd());
        assertTrue(actualAlgorithm.isFd());
        assertTrue(actualAlgorithm.isUcc());
        assertTrue(actualAlgorithm.isBasicStat());
    }

    /**
     * Test method for {@link Algorithm#store(Algorithm)} and {@link Algorithm#retrieve(String)}
     * <p/>
     * Algorithms should be storable and retrievable by id.
     */
    @Test
    public void testPersistence() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        // Expected values
        String expectedFileName = "someFileName";
        Algorithm expectedAlgorithm = new Algorithm(expectedFileName);

        // Execute functionality
        Algorithm.store(expectedAlgorithm);
        Algorithm actualAlgorithm = Algorithm.retrieve(expectedFileName);

        // Check result
        assertEquals(expectedAlgorithm, actualAlgorithm);

        // Cleanup
        HibernateUtil.clear();
    }

    /**
     * Test method for {@link Algorithm#retrieveAll()}
     */
    @Test
    public void testRetrieveAll() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        Algorithm expectedAlgorithm1 = new Algorithm("some file name 1");
        Algorithm.store(expectedAlgorithm1);
        Algorithm expectedAlgorithm2 = new Algorithm("some file name 2");
        Algorithm.store(expectedAlgorithm2);

        // Execute functionality
        Collection<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

        // Check result
        assertThat(actualAlgorithms, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm1, expectedAlgorithm2));

        // Cleanup
        HibernateUtil.clear();
    }

    /**
     * Test method for {@link Algorithm#retrieveAll()}
     *
     * When the table has never been accessed the list of algorithms should still be retrievable and empty.
     */
    @Test
    public void testRetrieveAllTableEmpty() {
        // Setup
        HibernateUtil.clear();

        // Execute functionality
        Collection<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

        // Check result
        assertTrue(actualAlgorithms.isEmpty());

        // Cleanup
        HibernateUtil.clear();
    }


    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm#equals(Object)} and
     * {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm#hashCode()}
     */
    @Test
    public void testEqualsHashCode() {
        // Setup
        // Expected values
        String expectedFileName1 = "someFileName1";
        String expectedFileName2 = "someFileName2";
        Algorithm algorithm = new Algorithm(expectedFileName1);
        Algorithm algorithmEqual = new Algorithm(expectedFileName1);
        Algorithm algorithmNotEqual = new Algorithm(expectedFileName2);

        // Execute functionality
        // Check result
        new EqualsAndHashCodeTester<Algorithm>()
                .performBasicEqualsAndHashCodeChecks(algorithm, algorithmEqual, algorithmNotEqual);
    }
}
