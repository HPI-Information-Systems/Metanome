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

package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

/**
 * Test for {@link InclusionDependency}
 * 
 * @author Jakob Zwiener
 */
public class InclusionDependencyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link InclusionDependency#sendResultTo(OmniscientResultReceiver)}
	 * 
	 * The {@link InclusionDependency} should be sendable to the {@link OmniscientResultReceiver}.
	 * 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testSendResultTo() throws CouldNotReceiveResultException {
		// Setup
		OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
		InclusionDependency ind = new InclusionDependency(mock(ColumnCombination.class), mock(ColumnCombination.class));
		
		// Execute functionality 
		ind.sendResultTo(resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(ind);
	}

	/**
	 * Test method for {@link InclusionDependency#InclusionDependency(ColumnCombination, ColumnCombination)}
	 * 
	 * A {@link InclusionDependency} should store referenced and dependant.
	 */
	@Test
	public void testConstructor() {
		// Setup
		// Expected values
		ColumnCombination expectedDependant = new ColumnCombination(
				new ColumnIdentifier("table2", "column2"),
				new ColumnIdentifier("table2", "column27"));
		ColumnCombination expectedReferenced = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"),
				new ColumnIdentifier("table1", "column4"));
		// Execute functionality
		InclusionDependency ind = new InclusionDependency(expectedDependant, expectedReferenced);
		
		// Check result
		assertEquals(expectedDependant, ind.getDependant());
		assertEquals(expectedReferenced, ind.getReferenced());
	}
	
	/**
	 * Test method for {@link InclusionDependency#toString()}
	 * 
	 * A {@link InclusionDependency} should return a human readable string representation.
	 */
	@Test
	public void testToString() {
		// Setup
		ColumnCombination expectedDependant = new ColumnCombination(
				new ColumnIdentifier("table2", "column2"),
				new ColumnIdentifier("table2", "column27"));
		ColumnCombination expectedReferenced = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"),
				new ColumnIdentifier("table1", "column4"));
		InclusionDependency ind = new InclusionDependency(expectedDependant, expectedReferenced);
		// Expected values
		String expectedStringRepresentation = expectedDependant + InclusionDependency.IND_SEPARATOR + expectedReferenced;
		
		// Execute functionality
		// Check result
		assertEquals(expectedStringRepresentation, ind.toString());
	}
	
	/**
	 * Test method for {@link InclusionDependency#equals(Object)} and {@link InclusionDependency#hashCode()}
	 * 
	 * {@link InclusionDependency}s containing equals dependant and referenced should be equal and have same hash codes.
	 */
	@Test
	public void testEqualsHashCode() {
		// Setup
		InclusionDependency expectedInd = new InclusionDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")),
				new ColumnCombination(
						new ColumnIdentifier("table2", "column47")));
		InclusionDependency expectedEqualInd = new InclusionDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")),
				new ColumnCombination(
						new ColumnIdentifier("table2", "column47"))); 
		InclusionDependency expectedNotEqualDependantInd = new InclusionDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column4")),
				new ColumnCombination(
						new ColumnIdentifier("table2", "column47")));
		InclusionDependency expectedNotEqualReferencedInd = new InclusionDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")),
				new ColumnCombination(
						new ColumnIdentifier("table5", "column47")));
		
		// Execute functionality
		// Check result
		assertEquals(expectedInd, expectedInd);
		assertEquals(expectedInd.hashCode(), expectedInd.hashCode());
		assertNotSame(expectedInd, expectedEqualInd);
		assertEquals(expectedInd, expectedEqualInd);
		assertEquals(expectedInd.hashCode(), expectedEqualInd.hashCode());
		assertNotEquals(expectedInd, expectedNotEqualDependantInd);
		assertNotEquals(expectedInd.hashCode(), expectedNotEqualDependantInd.hashCode());
		assertNotEquals(expectedInd, expectedNotEqualReferencedInd);
		assertNotEquals(expectedInd.hashCode(), expectedNotEqualReferencedInd.hashCode());
	}
	
	/**
	 * Tests that the instances of {@link InclusionDependency} are serializable in GWT.
	 */
	@Test
	public void testGwtSerialization() {
		GwtSerializationTester.checkGwtSerializability(new InclusionDependency(mock(ColumnCombination.class), mock(ColumnCombination.class)));
	}
	
}
