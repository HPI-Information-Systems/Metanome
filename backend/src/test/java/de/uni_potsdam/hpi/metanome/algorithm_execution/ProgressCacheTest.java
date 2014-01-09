package de.uni_potsdam.hpi.metanome.algorithm_execution;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ProgressCache}
 * 
 * @author Jakob Zwiener
 */
public class ProgressCacheTest {

	protected ProgressCache progressCache;
	
	protected float delta = 0.0001f;
	
	@Before
	public void setUp() throws Exception {
		progressCache = new ProgressCache();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ProgressCache#updateProgress(float)}
	 * 
	 * The progress should be updated and retrievable through {@link ProgressCache#getProgress()}.
	 */
	@Test
	public void testUpdateProgress() {
		// Setup
		// Expected values
		float expectedProgressUpdate1 = 3/7f;
		float expectedProgressUpdate2 = 7/8f;
		
		
		// Execute functionality 
		// Check result
		assertTrue(progressCache.updateProgress(expectedProgressUpdate1));
		assertEquals(expectedProgressUpdate1, progressCache.getProgress(), delta);
		assertTrue(progressCache.updateProgress(expectedProgressUpdate2));
		assertEquals(expectedProgressUpdate2, progressCache.getProgress(), delta);		
	}
	
	/**
	 * Test method for {@link ProgressCache#updateProgress(float)}
	 * 
	 * Progress values greater than 1 or smaller than 0 should not be stored and false be returned.
	 */
	@Test
	public void testUpdateProgressInvalidValues() {
		// Setup
		// Expected values
		List<Float> validValues = new LinkedList<Float>();
		validValues.add(1f);
		validValues.add(0f);
		validValues.add(0.5f);
		
		float expectedLastValidValue = 0.42f; 
		
		List<Float> invalidValues = new LinkedList<Float>();
		invalidValues.add(1.1f);
		invalidValues.add(-0.1f);
		invalidValues.add(23f);
		
		// Execute functionality
		// Check result
		for (Float progress : validValues) {
			assertTrue(progressCache.updateProgress(progress));
		}
		
		assertTrue(progressCache.updateProgress(expectedLastValidValue));
		
		for (Float progress : invalidValues) {
			assertFalse(progressCache.updateProgress(progress));
		}
		
		assertEquals(expectedLastValidValue, progressCache.getProgress(), delta);
	}
	
	/**
	 * Test method for {@link ProgressCache#getProgress()}
	 * 
	 * Progress should be zero initially.
	 */
	@Test
	public void testGetProgressInitial() {		
		// Check result
		assertEquals(0, progressCache.getProgress(), delta);
	}

}
