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

import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.server.FinderServiceImpl}
 *
 * @author Jakob Zwiener
 */
public class FinderServiceImplTest {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.FinderServiceImpl#listAlgorithms(Class)}
   * <p/> When no interface is specified all stored algorithms should be retrieved by the service.
   */
  @Test
  public void testListAlgorithms() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FinderServiceImpl finderService = new FinderServiceImpl();

    // Expected values
    Algorithm[]
        expectedAlgorithms =
        {new Algorithm("some file name 1"), new Algorithm("some file name 2"),
         new Algorithm("some file name 3")};
    for (Algorithm algorithm : expectedAlgorithms) {
      algorithm.store();
    }

    // Execute functionality
    // Finds algorithms of all or no interfaces
    List<Algorithm> actualAlgorithms = finderService.listAlgorithms(null);

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithms));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link FinderServiceImpl#listUniqueColumnCombinationsAlgorithms()}, {@link
   * FinderServiceImpl#listInclusionDependencyAlgorithms()}, {@link FinderServiceImpl#listFunctionalDependencyAlgorithms()}
   * and {@link FinderServiceImpl#listBasicStatisticsAlgorithms()} <p/> Stored algorithms that
   * implement certain interfaces should be retrievable by the service.
   */
  @Test
  public void testListAlgorithmFileNamesInterface() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FinderServiceImpl finderService = new FinderServiceImpl();

    // Expected values
    Algorithm expectedIndAlgorithm = new Algorithm("ind algorithm")
        .setInd(true)
        .store();

    Algorithm expectedUccAlgorithm = new Algorithm("ucc algorithm")
        .setUcc(true)
        .store();

    Algorithm expectedFdAlgorithm = new Algorithm("fd algorithm")
        .setFd(true)
        .store();

    Algorithm expectedBasicStatAlgorithm = new Algorithm("basic stat algorithm")
        .setBasicStat(true)
        .store();

    Algorithm otherAlgorithm = new Algorithm("other algorithm")
        .store();

    // Execute functionality
    List<Algorithm> actualIndAlgorithms = finderService.listInclusionDependencyAlgorithms();
    List<Algorithm> actualUccAlgorithms = finderService.listUniqueColumnCombinationsAlgorithms();
    List<Algorithm> actualFdAlgorithms = finderService.listFunctionalDependencyAlgorithms();
    List<Algorithm> actualBasicStatAlgorithms = finderService.listBasicStatisticsAlgorithms();
    List<Algorithm> actualAllAlgorithms = finderService.listAllAlgorithms();

    // Check result
    assertThat(actualIndAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedIndAlgorithm));
    assertThat(actualUccAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedUccAlgorithm));
    assertThat(actualFdAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedFdAlgorithm));
    assertThat(actualBasicStatAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedBasicStatAlgorithm));
    assertThat(actualAllAlgorithms, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedIndAlgorithm, expectedUccAlgorithm, expectedFdAlgorithm,
                            expectedBasicStatAlgorithm, otherAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

}
