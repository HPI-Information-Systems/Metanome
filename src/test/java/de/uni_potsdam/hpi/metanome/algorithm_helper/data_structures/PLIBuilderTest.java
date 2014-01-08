package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.TreeSet;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;


/**
 * Test for {@link PLIBuilder}
 */
public class PLIBuilderTest {
	
	protected PLIBuilderFixture fixture;
	protected PLIBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		fixture = new PLIBuilderFixture();
		
		builder = new PLIBuilder(fixture.getSimpleRelationalInput());
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link PLIBuilder#getPLIList()}
	 * 
	 * Tests that {@link PositionListIndex}es are build correclty.
	 * 
	 * @throws InputIterationException
	 */
	@Test 
	public void testCalculatePLI() throws InputIterationException {
		// Setup
		// Expected values
		List<PositionListIndex> expectedPLIList = fixture.getExpectedPLIList();
		PositionListIndex[] expectedPLIArray = expectedPLIList.toArray(new PositionListIndex[expectedPLIList.size()]);
		
		// Execute functionality
		List<PositionListIndex> actualPLIList = builder.getPLIList();
		
		// Check result
		assertThat(actualPLIList, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedPLIArray));
	}
	
	/**
	 * Test method for {@link PLIBuilder#getDistinctSortedColumns()}
	 * 
	 * Creates the distinct sorted columns from the raw plis.
	 * 
	 * @throws InputIterationException
	 */
	@Test
	public void testGetDistinctSortedColumns() throws InputIterationException {
		// Setup
		// Expected values
		List<TreeSet<String>> expectedDistinctSortedColumns = fixture.getExpectedDistinctSortedColumns();
		
		// Execute functionality
		List<TreeSet<String>> actualDistinctSortedColumns = builder.getDistinctSortedColumns();
		
		// Check result
		assertEquals(expectedDistinctSortedColumns, actualDistinctSortedColumns);
	}
}
