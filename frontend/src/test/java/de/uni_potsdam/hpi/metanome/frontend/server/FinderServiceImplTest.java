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

import de.uni_potsdam.hpi.metanome.results_db.Algorithm;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.HibernateUtil;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.server.FinderServiceImpl}
 *
 * @author Jakob Zwiener
 */
public class FinderServiceImplTest {

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.FinderServiceImpl#listAlgorithmFileNames(Class)}
     * <p/>
     * When no Interface is specified all stored algorithm file names should be retrieved by the service.
     *
     * @throws EntityStorageException
     */
    @Test
    public void testListAlgorithmFileNames() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        FinderServiceImpl finderService = new FinderServiceImpl();

        // Expected values
        Algorithm[] expectedAlgorithms = {new Algorithm("some file name 1"), new Algorithm("some file name 2"), new Algorithm("some file name 3")};
        for (Algorithm algorithm : expectedAlgorithms) {
            Algorithm.store(algorithm);
        }

        // Execute functionality
        // Finds algorithms of all or no interfaces
        List<String> actualAlgorithmFileNames = Arrays.asList(finderService.listAlgorithmFileNames(null));

        // Check result
        assertEquals(expectedAlgorithms.length, actualAlgorithmFileNames.size());
        for (Algorithm expectedAlgorithm : expectedAlgorithms) {
            assertTrue(actualAlgorithmFileNames.contains(expectedAlgorithm.getFileName()));
        }

        // Cleanup
        HibernateUtil.clear();
    }

    /**
     * Test method for {@link FinderServiceImpl#listUniqueColumnCombinationsAlgorithmFileNames()}, {@link FinderServiceImpl#listInclusionDependencyAlgorithmFileNames()}, {@link FinderServiceImpl#listFunctionalDependencyAlgorithmFileNames()} and {@link FinderServiceImpl#listBasicStatisticsAlgorithmFileNames()}
     * <p/>
     * File names of stored algorithms that implement certain interfaces should be retrievable by the service.
     *
     * @throws EntityStorageException
     */
    @Test
    public void testListAlgorithmFileNamesInterface() throws EntityStorageException {
        // Setup
        HibernateUtil.clear();

        FinderServiceImpl finderService = new FinderServiceImpl();

        // Expected values
        Algorithm expectedIndAlgorithm = new Algorithm("ind algorithm");
        expectedIndAlgorithm.setInd(true);
        Algorithm.store(expectedIndAlgorithm);

        Algorithm expectedUccAlgorithm = new Algorithm("ucc algorithm");
        expectedUccAlgorithm.setUcc(true);
        Algorithm.store(expectedUccAlgorithm);

        Algorithm expectedFdAlgorithm = new Algorithm("fd algorithm");
        expectedFdAlgorithm.setFd(true);
        Algorithm.store(expectedFdAlgorithm);

        Algorithm expectedBasicStatAlgorithm = new Algorithm("basic stat algorithm");
        expectedBasicStatAlgorithm.setBasicStat(true);
        Algorithm.store(expectedBasicStatAlgorithm);

        Algorithm otherAlgorithm = new Algorithm("other algorithm");
        Algorithm.store(otherAlgorithm);

        // Execute functionality
        String[] actualIndFileNames = finderService.listInclusionDependencyAlgorithmFileNames();
        String[] actualUccFileNames = finderService.listUniqueColumnCombinationsAlgorithmFileNames();
        String[] actualFdFileNames = finderService.listFunctionalDependencyAlgorithmFileNames();
        String[] actualBasicStatFileNames = finderService.listBasicStatisticsAlgorithmFileNames();

        // Check result
        assertThat(Arrays.asList(actualIndFileNames), IsIterableContainingInAnyOrder.containsInAnyOrder(expectedIndAlgorithm.getFileName()));
        assertThat(Arrays.asList(actualUccFileNames), IsIterableContainingInAnyOrder.containsInAnyOrder(expectedUccAlgorithm.getFileName()));
        assertThat(Arrays.asList(actualFdFileNames), IsIterableContainingInAnyOrder.containsInAnyOrder(expectedFdAlgorithm.getFileName()));
        assertThat(Arrays.asList(actualBasicStatFileNames), IsIterableContainingInAnyOrder.containsInAnyOrder(expectedBasicStatAlgorithm.getFileName()));

        // Cleanup
        HibernateUtil.clear();
    }

}