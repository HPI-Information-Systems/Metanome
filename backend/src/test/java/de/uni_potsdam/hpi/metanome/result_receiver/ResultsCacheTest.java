package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

/**
 * @author Jakob Zwiener
 *
 * Test for {@link ResultsCache}
 */
public class ResultsCacheTest {

	protected ResultsCache resultsCache;
	protected BasicStatistic expectedStatistic;
	protected FunctionalDependency expectedFd;
	protected InclusionDependency expectedInd;
	protected UniqueColumnCombination expectedUcc;
	
	@Before
	public void setUp() throws Exception {
		resultsCache = new ResultsCache();
		expectedStatistic = mock(BasicStatistic.class);
		expectedFd = mock(FunctionalDependency.class);
		expectedInd = mock(InclusionDependency.class);
		expectedUcc = mock(UniqueColumnCombination.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ResultsCache#getNewResults()}
	 * 
	 * Should return all results once. After receiving the list it should be cleared and only filled by new results.
	 */
	@Test
	public void testGetNewResults() {
		// Execute functionality
		resultsCache.receiveResult(expectedStatistic);
		resultsCache.receiveResult(expectedFd);
		
		ImmutableList<Result> actualResults = resultsCache.getNewResults();
		
		// Check result
		assertEquals(2, actualResults.size());
		// Results should be in correct order.
		assertSame(expectedStatistic, actualResults.get(0));
		assertSame(expectedFd, actualResults.get(1));
		// No new results should be fetched.
		assertEquals(0, resultsCache.getNewResults().size());
		
		// Add new results
		resultsCache.receiveResult(expectedInd);
		resultsCache.receiveResult(expectedUcc);
		
		// New results should be in list
		actualResults = resultsCache.getNewResults();
		assertEquals(2, actualResults.size());
		// Results should be in correct order.
		assertSame(expectedInd, actualResults.get(0));
		assertSame(expectedUcc, actualResults.get(1));
		
		// No new results should be fetched.
		assertEquals(0, resultsCache.getNewResults().size());
	}

}
