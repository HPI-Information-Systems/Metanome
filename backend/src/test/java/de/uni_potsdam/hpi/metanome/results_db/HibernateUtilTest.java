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

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#store(Object)} <p/>
   * When trying to store Objects missing the {@link javax.persistence.Entity} annotation an {@link
   * de.uni_potsdam.hpi.metanome.results_db.EntityStorageException} will be thrown.
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
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#retrieve(Class,
   * java.io.Serializable)} <p/> When trying to retrive entites of a class that is misiing the
   * {@link javax.persistence.Entity} annotation an {@link de.uni_potsdam.hpi.metanome.results_db.EntityStorageException}
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
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#store(Object)} and
   * {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#retrieve(Class,
   * java.io.Serializable)} <p/> Entities should be storable and retrievable.
   */
  @Test
  public void testDbRoundtrip() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFileName = "testFileName";
    Algorithm expectedAlgorithm = new Algorithm(expectedFileName);

    // Execute functionality
    HibernateUtil.store(expectedAlgorithm);
    Algorithm actualAlgorithm = (Algorithm) HibernateUtil.retrieve(Algorithm.class, "testFileName");

    // Check result
    assertEquals(expectedAlgorithm, actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#executeNamedQuery(String)}
   */
  @Test
  public void testExecuteNamedQuery() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm1 = new Algorithm("some path 1")
        .store();
    Algorithm expectedAlgorithm2 = new Algorithm("some path 2")
        .store();

    // Execute functionality
    List<Algorithm> actualAlgorithms = (List<Algorithm>) HibernateUtil.executeNamedQuery("get all");

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInOrder.contains(expectedAlgorithm1, expectedAlgorithm2));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> When querying for entities without adding any
   * criteria, all entities of the correct type should be returned.
   */
  @Test
  public void testQueryCriteriaNoCriterion() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm[] expectedAlgorithms = {new Algorithm("some path 1"), new Algorithm("some path 2")};
    for (Algorithm expectedAlgorithm : expectedAlgorithms) {
      expectedAlgorithm.store();
    }

    // Execute functionality
    List<Algorithm> actualAlgorithms = HibernateUtil.queryCriteria(Algorithm.class);

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithms));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> The resulting entities should match the criteria.
   */
  @Test
  public void testQueryCriteria() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("some path")
        .setFd(true)
        .store();
    Algorithm otherAlgorithm = new Algorithm("some other path")
        .store();

    // Execute functionality
    Criterion onlyFdAlgorithms = Restrictions.eq("fd", true);
    List<Algorithm>
        actualAlgorithms =
        HibernateUtil.queryCriteria(Algorithm.class, onlyFdAlgorithms);

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> All results should match all the supplied criteria
   * at once.
   */
  @Test
  public void testQueryCriteriaConjunction() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("some path")
        .setFd(true)
        .setUcc(true)
        .store();
    Algorithm otherAlgorithm = new Algorithm("some other path")
        .setFd(true)
        .store();

    // Execute functionality
    Criterion onlyFdAlgorithms = Restrictions.eq("fd", true);
    Criterion onlyUccAlgorithms = Restrictions.eq("ucc", true);
    List<Algorithm>
        actualAlgorithms =
        HibernateUtil.queryCriteria(Algorithm.class, onlyFdAlgorithms, onlyUccAlgorithms);

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#queryCriteria(Class,
   * org.hibernate.criterion.Criterion...)} <p/> When a class is queried that is missing the {@link
   * javax.persistence.Entity} annotation an {@link de.uni_potsdam.hpi.metanome.results_db.EntityStorageException}
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
    String expectedAlgorithmId = "some alorithm";
    Algorithm expectedAlgorithm = new Algorithm(expectedAlgorithmId);

    // Execute functionality
    expectedAlgorithm.store();
    HibernateUtil.clear();

    // Check result
    Algorithm actualAlgorithm = Algorithm.retrieve(expectedAlgorithmId);
    assertNull(actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

}
