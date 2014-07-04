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

package de.uni_potsdam.hpi.metanome.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResultsHubTest {

  protected ResultsHub resultsHub;
  protected CloseableOmniscientResultReceiver resultReceiver1;
  protected CloseableOmniscientResultReceiver resultReceiver2;

  @Before
  public void setUp() throws Exception {
    // Setup
    resultsHub = new ResultsHub();
    resultReceiver1 = mock(CloseableOmniscientResultReceiver.class);
    resultReceiver2 = mock(CloseableOmniscientResultReceiver.class);
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link ResultsHub#addSubscriber(CloseableOmniscientResultReceiver)} <p/> Added
   * subscribers should be in the set of subscribers.
   */
  @Test
  public void testAddSubscriber() {
    // Execute functionality
    // Check result
    assertTrue(resultsHub.getSubscriber().isEmpty());
    resultsHub.addSubscriber(resultReceiver1);
    resultsHub.addSubscriber(resultReceiver2);
    assertEquals(2, resultsHub.getSubscriber().size());
  }

  /**
   * Test method for {@link ResultsHub#addSubscriber(CloseableOmniscientResultReceiver)} <p/> After
   * removing a {@link OmniscientResultReceiver} it should no longer be among the subscribers.
   */
  @Test
  public void testRemoveSubscriber() {
    // Setup
    resultsHub.addSubscriber(resultReceiver1);
    resultsHub.addSubscriber(resultReceiver2);

    // Execute functionality
    // Check result
    assertTrue(resultsHub.getSubscriber().contains(resultReceiver1));
    resultsHub.removeSubscriber(resultReceiver1);
    assertFalse(resultsHub.getSubscriber().contains(resultReceiver1));
  }

  /**
   * Test method for {@link ResultsHub#receiveResult(BasicStatistic)} <p/> All subscribed {@link
   * OmniscientResultReceiver} should receive the {@link BasicStatistic}s received by the {@link
   * ResultsHub}.
   */
  @Test
  public void testReceiveResultBasicStatistic() throws CouldNotReceiveResultException {
    // Setup
    resultsHub.addSubscriber(resultReceiver1);
    // Expected values
    BasicStatistic basicStatistic1 = mock(BasicStatistic.class);
    BasicStatistic basicStatistic2 = mock(BasicStatistic.class);

    // Execute functionality
    // Check result
    resultsHub.receiveResult(basicStatistic1);
    // The first result should only be received by the subscribed receiver.
    verify(resultReceiver1).receiveResult(basicStatistic1);
    resultsHub.addSubscriber(resultReceiver2);
    // The second result should be received by both receivers.
    resultsHub.receiveResult(basicStatistic2);
    verify(resultReceiver1).receiveResult(basicStatistic2);
    verify(resultReceiver2).receiveResult(basicStatistic2);
  }

  /**
   * Test method for {@link ResultsHub#receiveResult(FunctionalDependency)} <p/> All subscribed
   * {@link OmniscientResultReceiver} should receive the {@link FunctionalDependency}s received by
   * the {@link ResultsHub}.
   */
  @Test
  public void testReceiveResultFunctionalDependency() throws CouldNotReceiveResultException {
    // Setup
    resultsHub.addSubscriber(resultReceiver1);
    // Expected values
    FunctionalDependency fd1 = mock(FunctionalDependency.class);
    FunctionalDependency fd2 = mock(FunctionalDependency.class);

    // Execute functionality
    // Check result
    resultsHub.receiveResult(fd1);
    // The first result should only be received by the subscribed receiver.
    verify(resultReceiver1).receiveResult(fd1);
    resultsHub.addSubscriber(resultReceiver2);
    // The second result should be received by both receivers.
    resultsHub.receiveResult(fd2);
    verify(resultReceiver1).receiveResult(fd2);
    verify(resultReceiver2).receiveResult(fd2);
  }

  /**
   * Test method for {@link ResultsHub#receiveResult(InclusionDependency)} <p/> All subscribed
   * {@link OmniscientResultReceiver} should receive the {@link InclusionDependency}s received by
   * the {@link ResultsHub}.
   */
  @Test
  public void testReceiveResultInclusionDependency() throws CouldNotReceiveResultException {
    // Setup
    resultsHub.addSubscriber(resultReceiver1);
    // Expected values
    InclusionDependency ind1 = mock(InclusionDependency.class);
    InclusionDependency ind2 = mock(InclusionDependency.class);

    // Execute functionality
    // Check result
    resultsHub.receiveResult(ind1);
    // The first result should only be received by the subscribed receiver.
    verify(resultReceiver1).receiveResult(ind1);
    resultsHub.addSubscriber(resultReceiver2);
    // The second result should be received by both receivers.
    resultsHub.receiveResult(ind2);
    verify(resultReceiver1).receiveResult(ind2);
    verify(resultReceiver2).receiveResult(ind2);
  }

  /**
   * Test method for {@link ResultsHub#receiveResult(UniqueColumnCombination)} <p/> All subscribed
   * {@link OmniscientResultReceiver} should receive the {@link UniqueColumnCombination}s received
   * by the {@link ResultsHub}.
   */
  @Test
  public void testReceiveResultUniqueColumnCombination() throws CouldNotReceiveResultException {
    // Setup
    resultsHub.addSubscriber(resultReceiver1);
    // Expected values
    UniqueColumnCombination ucc1 = mock(UniqueColumnCombination.class);
    UniqueColumnCombination ucc2 = mock(UniqueColumnCombination.class);

    // Execute functionality
    // Check result
    resultsHub.receiveResult(ucc1);
    // The first result should only be received by the subscribed receiver.
    verify(resultReceiver1).receiveResult(ucc1);
    resultsHub.addSubscriber(resultReceiver2);
    // The second result should be received by both receivers.
    resultsHub.receiveResult(ucc2);
    verify(resultReceiver1).receiveResult(ucc2);
    verify(resultReceiver2).receiveResult(ucc2);
  }

  /**
   * Test method for {@link ResultsHub#close()} <p/> On close all subscribers should be closed.
   */
  @Test
  public void testClose() throws IOException {
    // Setup
    resultsHub.addSubscriber(resultReceiver1);
    resultsHub.addSubscriber(resultReceiver2);

    // Execute functionality
    resultsHub.close();

    // Check result
    verify(resultReceiver1).close();
    verify(resultReceiver2).close();
  }
}
