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

package de.metanome.frontend.client.algorithms;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GwtTestAlgorithmsPage extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmsPage}
   *
   * When a new AlgorithmsPage is created, an edit form should be present, and service as well as
   * parent set.
   */
  public void testSetup() {
    // Setup
    TestHelper.resetDatabaseSync();

    boolean editFormPresent = false;
    BasePage basePage = new BasePage();

    // Create the page
    AlgorithmsPage algorithmPage = new AlgorithmsPage(basePage);

    // Check for edit form
    for (Iterator<Widget> i = algorithmPage.iterator(); i.hasNext(); ) {
      if (i.next() instanceof AlgorithmEditForm) {
        editFormPresent = true;
      }
    }
    assertTrue(editFormPresent);

    assertNotNull(algorithmPage.algorithmService);
    assertEquals(basePage, algorithmPage.basePage);

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   *
   * After failure is called on the constructed callback, the tab should be in error.
   */
  public void testRetrieveCallbackFailure() {
    // Setup
    TestHelper.resetDatabaseSync();

    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    TabWrapper tab = new TabWrapper();
    algorithmsPage.setMessageReceiver(tab);

    // Construct and execute failure on the callback
    AsyncCallback<List<Algorithm>> callback = algorithmsPage.getRetrieveCallback(new FlexTable());
    callback.onFailure(new Throwable());

    assertTrue(tab.isInError());

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   *
   * After success is called on the constructed callback, the UI element given as argument should
   * contain all the elements of the result.
   */
  public void testRetrieveCallbackSuccess() {
    // Setup
    TestHelper.resetDatabaseSync();

    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    FlexTable list = new FlexTable();

    // Create a list of algorithms as result
    LinkedList<Algorithm> result = new LinkedList<Algorithm>();
    Algorithm a1 = new Algorithm("fileName");
    result.add(a1);

    AsyncCallback<List<Algorithm>> callback = algorithmsPage.getRetrieveCallback(list);
    callback.onSuccess(result);

    assertEquals(result.size(), list.getRowCount());

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   *
   * After failure is called on the constructed callback, the tab should be in error.
   */
  public void testAddCallbackFailure() {
    // Setup
    TestHelper.resetDatabaseSync();

    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    TabWrapper tab = new TabWrapper();
    algorithmsPage.setMessageReceiver(tab);

    // Construct and execute failure on the callback
    AsyncCallback<Algorithm> callback = algorithmsPage.getAddCallback();
    callback.onFailure(new Throwable());

    assertTrue(tab.isInError());

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   *
   * After success is called on the constructed callback, the UI element given as argument should
   * contain all the elements of the result.
   */
  public void testAddCallbackSuccess() {
    // Setup
    TestHelper.resetDatabaseSync();

    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    int uccCount = algorithmsPage.uccList.getRowCount();

    // Create a list of algorithms as result
    Algorithm a1 = new Algorithm("fileName");
    a1.setUcc(true);

    AsyncCallback<Algorithm> callback = algorithmsPage.getAddCallback();
    callback.onSuccess(a1);

    assertEquals(uccCount + 1, algorithmsPage.uccList.getRowCount());

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmsPage#deleteAlgorithm(de.metanome.backend.results_db.Algorithm)}
   */
  public void testDeleteAlgorithm() {
    // Setup
    TestHelper.resetDatabaseSync();

    final AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    final int[] uccCount = new int[1];
    final int[] fdCount = new int[1];

    Timer setUpTimer = new Timer() {
      @Override
      public void run() {
        uccCount[0] = algorithmsPage.uccList.getRowCount();
        fdCount[0] = algorithmsPage.fdList.getRowCount();

        // Create a list of algorithms as result
        LinkedList<Algorithm> uccAlgorithms = new LinkedList<Algorithm>();
        LinkedList<Algorithm> fdAlgorithms = new LinkedList<Algorithm>();
        Algorithm a1 = new Algorithm("fileName1");
        a1.setName("algorithm1");
        a1.setUcc(true);
        a1.setFd(true);
        uccAlgorithms.add(a1);
        fdAlgorithms.add(a1);
        Algorithm a2 = new Algorithm("fileName2");
        a2.setName("algorithm2");
        a2.setUcc(true);
        uccAlgorithms.add(a2);

        algorithmsPage.addAlgorithmsToTable(uccAlgorithms, algorithmsPage.uccList);
        algorithmsPage.addAlgorithmsToTable(fdAlgorithms, algorithmsPage.fdList);

        assertEquals(uccCount[0] + 2, algorithmsPage.uccList.getRowCount());
        assertEquals(fdCount[0] + 1, algorithmsPage.fdList.getRowCount());

        // Execute
        algorithmsPage.deleteAlgorithm(a1);
      }
    };

    Timer checkTimer = new Timer() {
      @Override
      public void run() {
        // Check
        assertEquals(uccCount[0] + 1, algorithmsPage.uccList.getRowCount());
        assertEquals(fdCount[0], algorithmsPage.fdList.getRowCount());

        finishTest();
      }
    };

    checkTimer.schedule(3000);
    setUpTimer.schedule(1000);

    delayTestFinish(8000);

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }

}
