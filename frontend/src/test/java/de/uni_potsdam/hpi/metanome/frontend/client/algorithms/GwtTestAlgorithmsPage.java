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

package de.uni_potsdam.hpi.metanome.frontend.client.algorithms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

public class GwtTestAlgorithmsPage extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage}
   * <p/>
   * When a new AlgorithmsPage is created, an edit form should be present, and service as well as
   * parent set.
   */
  @Test
  public void testSetup() {
    boolean editFormPresent = false;
    BasePage basePage = new BasePage();

    // Create the page
    AlgorithmsPage algorithmPage = new AlgorithmsPage(basePage);

    // Check for edit form
    for (Iterator<Widget> i = algorithmPage.iterator(); i.hasNext();) {
      if (i.next() instanceof AlgorithmEditForm) {
        editFormPresent = true;
      }
    }
    assertTrue(editFormPresent);

    assertNotNull(algorithmPage.algorithmService);
    assertEquals(basePage, algorithmPage.basePage);
  }

  /**
   * Test method for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   * <p/>
   * After failure is called on the constructed callback, the tab should be in error.
   */
  @Test
  public void testRetrieveCallbackFailure() {
    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    TabWrapper tab = new TabWrapper();
    algorithmsPage.setErrorReceiver(tab);

    // Construct and execute failure on the callback
    AsyncCallback<List<Algorithm>> callback = algorithmsPage.getRetrieveCallback(new FlexTable());
    callback.onFailure(new Throwable());

    assertTrue(tab.isInError());
  }

  /**
   * Test method for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   * <p/>
   * After success is called on the constructed callback, the UI element given as argument should
   * contain all the elements of the result.
   */
  @Test
  public void testRetrieveCallbackSuccess() {
    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    FlexTable list = new FlexTable();

    // Create a list of algorithms as result
    LinkedList<Algorithm> result = new LinkedList<Algorithm>();
    Algorithm a1 = new Algorithm("fileName");
    result.add(a1);

    AsyncCallback<List<Algorithm>> callback = algorithmsPage.getRetrieveCallback(list);
    callback.onSuccess(result);

    assertEquals(result.size(), list.getRowCount());
  }

  /**
   * Test method for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   * <p/>
   * After failure is called on the constructed callback, the tab should be in error.
   */
  @Test
  public void testAddCallbackFailure() {
    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    TabWrapper tab = new TabWrapper();
    algorithmsPage.setErrorReceiver(tab);

    // Construct and execute failure on the callback
    AsyncCallback<Void> callback = algorithmsPage.getAddCallback(new Algorithm("fileName"));
    callback.onFailure(new Throwable());

    assertTrue(tab.isInError());
  }

  /**
   * Test method for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage#getRetrieveCallback(FlexTable)}
   * <p/>
   * After success is called on the constructed callback, the UI element given as argument should
   * contain all the elements of the result.
   */
  @Test
  public void testAddCallbackSuccess() {
    AlgorithmsPage algorithmsPage = new AlgorithmsPage(new BasePage());
    int uccCount = algorithmsPage.uccList.getRowCount();

    // Create a list of algorithms as result
    Algorithm a1 = new Algorithm("fileName");
    a1.setUcc(true);

    AsyncCallback<Void> callback = algorithmsPage.getAddCallback(a1);
    callback.onSuccess(null);

    assertEquals(uccCount + 1, algorithmsPage.uccList.getRowCount());
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
