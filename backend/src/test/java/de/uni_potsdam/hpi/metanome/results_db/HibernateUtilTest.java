package de.uni_potsdam.hpi.metanome.results_db;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jakob Zwiener
 *
 * Test for {@link HibernateUtil}
 */
public class HibernateUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link HibernateUtil#getSessionFactory()}
	 * 
	 * {@link SessionFactory} should be singleton.
	 */
	@Test
	public void testGetSessionFactory() {
		// Check result
		assertSame(HibernateUtil.getSessionFactory(), HibernateUtil.getSessionFactory());
	}
	
	/**
	 * Test method for {@link HibernateUtil#openNewSession()}
	 * 
	 * Fresh sessions should be connected and open.
	 */
	@Test
	public void testOpenNewSession() {
		// Check result
		Session session = HibernateUtil.openNewSession();
		assertTrue(session.isConnected());
		assertTrue(session.isOpen());
	}
}
