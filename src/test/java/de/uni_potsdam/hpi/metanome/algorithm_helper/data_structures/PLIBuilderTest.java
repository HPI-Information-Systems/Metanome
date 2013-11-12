package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PLIBuilder;
import de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;


public class PLIBuilderTest {
	protected PLIBuilderFixture fixture;
	
	@Before
	public void setUp() throws Exception {
		
		fixture = new PLIBuilderFixture();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	public void testCalculatePLI() throws InputIterationException {
		// Setup
		PLIBuilder builder = new PLIBuilder(fixture.getSimpleRelationalInput());
		// Expected values
		List<PositionListIndex> expectedPLIList = fixture.getExpectedPLIList();
		PositionListIndex[] expectedPLIArray = expectedPLIList.toArray(new PositionListIndex[expectedPLIList.size()]);
		
		assertThat(builder.getPLIList(), IsIterableContainingInAnyOrder.containsInAnyOrder(expectedPLIArray));
	}
}
