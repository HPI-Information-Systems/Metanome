package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * @author Jakob Zwiener
 * 
 * Test for {@link BasicStatistic}
 */
public class BasicStatisticTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link BasicStatistic#sendResultTo(OmniscientResultReceiver)}
	 * 
	 * The {@link BasicStatistic} should be sendable to the {@link OmniscientResultReceiver}.
	 * 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testSendResultTo() throws CouldNotReceiveResultException {
		// Setup
		OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
		BasicStatistic statistic = new BasicStatistic("Min", mock(Object.class), mock(ColumnIdentifier.class));
		
		// Execute functionality 
		statistic.sendResultTo(resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(statistic);
	}

}
