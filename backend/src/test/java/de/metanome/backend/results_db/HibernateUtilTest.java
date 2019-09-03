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
package de.metanome.backend.results_db;

import de.metanome.backend.constants.Constants;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for {@link HibernateUtil}
 *
 * @author Jakob Zwiener
 */
public class HibernateUtilTest {

  /**
   * Test method for {@link HibernateUtil#getSessionFactory()} <p/> {@link SessionFactory} should be
   * singleton.
   */
  @Test
  public void testGetSessionFactory() {
    // Check result
    assertSame(HibernateUtil.getSessionFactory(), HibernateUtil.getSessionFactory());
  }

  /**
   * Test method for {@link HibernateUtil#openNewSession()} <p/> Fresh sessions should be connected
   * and open.
   */
  @Test
  public void testOpenNewSession() {
    // Check result
    Session session = HibernateUtil.openNewSession();
    assertTrue(session.isConnected());
    assertTrue(session.isOpen());
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#store(Object)} <p/> When
   * trying to store Objects missing the {@link javax.persistence.Entity} annotation an {@link
   * de.metanome.backend.results_db.EntityStorageException} will be thrown.
   */
  @Test
  public void testStoreFailNonEntity() {
    // Setup
    HibernateUtil.clear();
    Object actualObject = new Object();

    // Execute functionality
    // Check result
    try {
      HibernateUtil.store(actualObject);
      fail("Exception was not thrown.");
    } catch (EntityStorageException e) {
      // Intentionally left blank
    }

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#retrieve(Class,
   * java.io.Serializable)} <p/> When trying to retrieve entities of a class that is missing the
   * {@link javax.persistence.Entity} annotation an {@link de.metanome.backend.results_db.EntityStorageException}
   * will be thrown.
   */
  @Test
  public void testRetrieveFailNonEntity() {
    // Setup
    HibernateUtil.clear();

    // Execute functionality
    // Check result
    try {
      HibernateUtil.retrieve(Object.class, "someId");
      fail("Exception was not thrown.");
    } catch (EntityStorageException e) {
      // Intentionally left blank
    }

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#store(Object)} and {@link
   * de.metanome.backend.results_db.HibernateUtil#retrieve(Class, java.io.Serializable)} <p/>
   * Entities should be storable and retrievable.
   */
  @Test
  public void testDbRoundtrip() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_ind_algorithm.jar");

    // Execute functionality
    HibernateUtil.store(expectedAlgorithm);
    Algorithm
      actualAlgorithm =
      (Algorithm) HibernateUtil.retrieve(Algorithm.class, expectedAlgorithm.getId());

    // Check result
    assertEquals(expectedAlgorithm, actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for  {@link de.metanome.backend.results_db.HibernateUtil#delete(Object)}
   * <p/>
   * Entities should be deletable from the database.
   */
  @Test
  public void testDelete() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("some file");
    HibernateUtil.store(expectedAlgorithm);

    // Check precondition
    Algorithm actualAlgorithm =
      (Algorithm) HibernateUtil.retrieve(Algorithm.class, expectedAlgorithm.getId());
    assertEquals(expectedAlgorithm, actualAlgorithm);

    // Execute functionality
    HibernateUtil.delete(expectedAlgorithm);

    // Check result
    assertNull(HibernateUtil.retrieve(Algorithm.class, expectedAlgorithm.getId()));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for  {@link de.metanome.backend.results_db.HibernateUtil#delete(Object)}
   * <p/>
   * When trying to delete entities of a class that is missing the {@link javax.persistence.Entity}
   * annotation an {@link de.metanome.backend.results_db.EntityStorageException} should be thrown.
   */
  @Test
  public void testDeleteNonEntity() {
    // Setup
    HibernateUtil.clear();

    // Execute functionality
    // Check result
    try {
      HibernateUtil.delete("some string");
      fail("Exception was not thrown.");
    } catch (EntityStorageException e) {
      // Intentionally left blank
    }

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for  {@link de.metanome.backend.results_db.HibernateUtil#delete(Object)}
   * <p/>
   * Deleting entities that have not been stored yet should just be transparent (no exception,
   * nothing deleted).
   */
  @Test
  public void testDeleteNotStored() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFileName = "testFileName";
    Algorithm expectedAlgorithm = new Algorithm(expectedFileName);

    // Execute functionality
    HibernateUtil.delete(expectedAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#executeNamedQuery(String)}
   */
  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testExecuteNamedQuery() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm1 = new Algorithm("some path 1");
    Algorithm expectedAlgorithm2 = new Algorithm("some path 2");

    HibernateUtil.store(expectedAlgorithm1);
    HibernateUtil.store(expectedAlgorithm2);

    // Execute functionality
    List<Algorithm> actualAlgorithms = (List<Algorithm>) HibernateUtil.executeNamedQuery("get all");

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInOrder.contains(expectedAlgorithm1, expectedAlgorithm2));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> When querying for entities without adding any
   * criteria, all entities of the correct type should be returned.
   */
  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testQueryCriteriaNoCriterion() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm[] expectedAlgorithms = {new Algorithm("some path 1"), new Algorithm("some path 2")};
    for (Algorithm expectedAlgorithm : expectedAlgorithms) {
      HibernateUtil.store(expectedAlgorithm);
    }

    // Execute functionality
    List<Algorithm> actualAlgorithms = (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class);

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithms));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> The resulting entities should match the criteria.
   */
  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testQueryCriteria() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("some path")
      .setFd(true);
    Algorithm otherAlgorithm = new Algorithm("some other path");

    HibernateUtil.store(expectedAlgorithm);
    HibernateUtil.store(otherAlgorithm);

    // Execute functionality
    Criterion onlyFdAlgorithms = Restrictions.eq(AlgorithmType.FD.getAbbreviation(), true);
    List<Algorithm>
      actualAlgorithms =
      (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class, onlyFdAlgorithms);

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> All results should match all the supplied criteria
   * at once.
   */
  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testQueryCriteriaConjunction() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("some path")
      .setFd(true)
      .setUcc(true);
    Algorithm otherAlgorithm = new Algorithm("some other path")
      .setFd(true);

    HibernateUtil.store(expectedAlgorithm);
    HibernateUtil.store(otherAlgorithm);

    // Execute functionality
    Criterion onlyFdAlgorithms = Restrictions.eq(AlgorithmType.FD.getAbbreviation(), true);
    Criterion onlyUccAlgorithms = Restrictions.eq(AlgorithmType.UCC.getAbbreviation(), true);
    List<Algorithm>
      actualAlgorithms =
      (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class, onlyFdAlgorithms, onlyUccAlgorithms);

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> When a class is queried that is missing the {@link
   * javax.persistence.Entity} annotation an {@link de.metanome.backend.results_db.EntityStorageException}
   * should be thrown.
   */
  @Test
  public void testQueryCriteriaNonPersistentClass() {
    // Execute functionality
    try {
      HibernateUtil.queryCriteria(Object.class);
      fail("Exception was not thrown.");
    } catch (EntityStorageException e) {
      // Intentionally left blank
    }
  }

  /**
   * Test method for {@link HibernateUtil#shutdown()} <p/> After shutting down the database the
   * session factory should be closed and a new one created on the next start.
   */
  @Test
  public void testShutdown() {
    // Setup
    SessionFactory originalSessionFactory = HibernateUtil.getSessionFactory();
    assertFalse(originalSessionFactory.isClosed());

    // Execute functionality
    HibernateUtil.shutdown();
    SessionFactory newOpenSessionFactory = HibernateUtil.getSessionFactory();

    // Check result
    assertTrue(originalSessionFactory.isClosed());
    assertNotSame(newOpenSessionFactory, originalSessionFactory);
  }

  /**
   * Test method for {@link HibernateUtil#clear()} <p/> After clear the previously written entites
   * should no longer be present.
   */
  @Test
  public void testClear() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();
    long expectedAlgorithmId = 0;
    Algorithm expectedAlgorithm = new Algorithm("some file");
    expectedAlgorithm.setId(0);

    // Execute functionality
    HibernateUtil.store(expectedAlgorithm);
    HibernateUtil.clear();

    // Check result
    Algorithm
      actualAlgorithm =
      (Algorithm) HibernateUtil.retrieve(Algorithm.class, expectedAlgorithmId);
    assertNull(actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.HibernateUtil#update(Object)}
   * <p/>
   * Entities should be updateable.
   */
  @Test
  public void testUpdate() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String oldName = "some name";
    String newName = "some new name";
    Algorithm expectedAlgorithm = new Algorithm("example_ind_algorithm.jar");
    expectedAlgorithm.setName(oldName);

    // Execute functionality
    HibernateUtil.store(expectedAlgorithm);

    // Check result
    assertTrue(expectedAlgorithm.getName().equals(oldName));

    expectedAlgorithm.setName(newName);
    HibernateUtil.update(expectedAlgorithm);

    // Check result
    assertTrue(expectedAlgorithm.getName().equals(newName));

    // Cleanup
    HibernateUtil.clear();
  }

}
