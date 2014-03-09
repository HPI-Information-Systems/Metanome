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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link HibernateUtil}
 *
 * @author Jakob Zwiener
 */
public class HibernateUtilTest {

    /**
     * Test method for {@link HibernateUtil#getSessionFactory()}
     * <p/>
     * {@link SessionFactory} should be singleton.
     */
    @Test
    public void testGetSessionFactory() {
        // Check result
        assertSame(HibernateUtil.getSessionFactory(), HibernateUtil.getSessionFactory());
    }

    /**
     * Test method for {@link HibernateUtil#openNewSession()}
     * <p/>
     * Fresh sessions should be connected and open.
     */
    @Test
    public void testOpenNewSession() {
        // Check result
        Session session = HibernateUtil.openNewSession();
        assertTrue(session.isConnected());
        assertTrue(session.isOpen());
    }

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#store(Object)}
     * <p/>
     * When trying to store Objects missing the {@link javax.persistence.Entity} annotation an
     * {@link de.uni_potsdam.hpi.metanome.results_db.EntityStorageException} will be thrown.
     */
    @Test
    public void testStoreFailNonEntity() {
        // Setup
        Object actualObject = new Object();

        // Execute functionality
        // Check result
        try {
            HibernateUtil.store(actualObject);
            fail("Exception was not thrown.");
        } catch (EntityStorageException e) {
            // Intentionally left blank
        }
    }

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#retrieve(Class, java.io.Serializable)}
     * <p/>
     * When trying to retrive entites of a class that is misiing the {@link javax.persistence.Entity} annotation an
     * {@link de.uni_potsdam.hpi.metanome.results_db.EntityStorageException} will be thrown.
     */
    @Test
    public void testRetrieveFailNonEntity() {
        // Execute functionality
        // Check result
        try {
            HibernateUtil.retrieve(Object.class, "someId");
            fail("Exception was not thrown.");
        } catch (EntityStorageException e) {
            // Intentionally left blank
        }
    }

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#store(Object)} and
     * {@link de.uni_potsdam.hpi.metanome.results_db.HibernateUtil#retrieve(Class, java.io.Serializable)}
     * <p/>
     * Entities should be storable and retrievable.
     */
    @Test
    public void testDbRoundtrip() throws EntityStorageException {
        // Setup
        // Expected values
        String expectedFileName = "testFileName";
        Algorithm expectedAlgorithm = new Algorithm(expectedFileName);

        // Execute functionality
        HibernateUtil.store(expectedAlgorithm);
        Algorithm actualAlgorithm = (Algorithm) HibernateUtil.retrieve(Algorithm.class, "testFileName");

        // Check result
        assertEquals(expectedAlgorithm, actualAlgorithm);
    }
}
