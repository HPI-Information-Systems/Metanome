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

package de.uni_potsdam.hpi.metanome.frontend.client.runs;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;
import de.uni_potsdam.hpi.metanome.results_db.AlgorithmContentEquals;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.client.runs.AlgorithmChooser}
 *
 * @author Jakob Zwiener
 */
public class GwtTestAlgorithmChooser extends GWTTestCase {

    /**
     * Test that one can add an algorithm after the JarChooser's initial construction
     */
    public void testAddAlgorithm() {
        // Setup
        LinkedList<Algorithm> algorithms = new LinkedList<>();
        Algorithm expectedAlgorithm1 = new Algorithm("file name 1");
        expectedAlgorithm1.setName("name 1");
        algorithms.add(expectedAlgorithm1);
        Algorithm expectedAlgorithm2 = new Algorithm("file name 2");
        expectedAlgorithm2.setName("name 2");

        AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

        // Execute functionality
        jarChooser.addAlgorithm(expectedAlgorithm2);

        // Check result
        assertTrue(AlgorithmContentEquals.contentEquals(expectedAlgorithm1, jarChooser.algorithms.get(expectedAlgorithm1.getName())));
        assertTrue(AlgorithmContentEquals.contentEquals(expectedAlgorithm2, jarChooser.algorithms.get(expectedAlgorithm2.getName())));
    
        // Execute duplicate insert
        int previousCount = jarChooser.getListItemCount();
        jarChooser.addAlgorithm(expectedAlgorithm1);
    
        // Check that the algorithm was not added again
        assertEquals(previousCount, jarChooser.getListItemCount());
    }

    /**
     * Ensure that when an algorithm is chosen and submitted, the method adding a ParameterTable is called.
     */
    public void testSubmit() {
        // Setup
        LinkedList<Algorithm> algorithms = new LinkedList<>();
        Algorithm expectedAlgorithm1 = new Algorithm("example_ucc_algorithm.jar");
        expectedAlgorithm1.setName("name 1");
        algorithms.add(expectedAlgorithm1);
        Algorithm expectedAlgorithm2 = new Algorithm("file name 2");
        expectedAlgorithm2.setName("name 2");

        TabWrapper tabWrapper = new TabWrapper();
        RunConfigurationPage page = new RunConfigurationPage(new BasePage()) {
            @Override
            public void addParameterTable(List<ConfigurationSpecification> paramList) {
                super.addParameterTable(paramList);
                finishTest();
            }
        };
        AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, tabWrapper);
        page.add(jarChooser, DockPanel.NORTH);

        // Execute functionality
        jarChooser.listbox.setItemSelected(1, true);
        jarChooser.submit();

        // Set a delay period
        delayTestFinish(500);
    }

    /**
     * Ensure that all algorithms in the list given at construction are displayed
     */
    public void testConstructor() {
        //Setup
        LinkedList<Algorithm> algorithms = new LinkedList<>();
        algorithms.add(new Algorithm("Algorithm 1"));
        algorithms.add(new Algorithm("Algorithm 2"));

        //Execute
        AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

        //Test
        assertEquals(2, jarChooser.getWidgetCount());
        assertEquals(algorithms.size() + 1, jarChooser.getListItemCount());
    }
    
    /**
     * Test that the algorithms are listed in alphabetical order of their names
     */
    public void testOrdering() {
    	// Setup
        LinkedList<Algorithm> algorithms = new LinkedList<>();
        Algorithm algo1 = new Algorithm("");
        algo1.setName("C");
        algorithms.add(algo1);
        Algorithm algo2 = new Algorithm("");
        algo2.setName("B");
        algorithms.add(algo2);
        Algorithm algo3 = new Algorithm("");
        algo3.setName("A");
        
        
        // Create dropdown
        AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());
        
        // Check
        assertTrue(jarChooser.listbox.getItemText(1).compareTo(jarChooser.listbox.getItemText(2)) < 0);
        
        // Add another algorithm
        jarChooser.addAlgorithm(algo3);
        
        // Check
        assertTrue(jarChooser.listbox.getItemText(1).compareTo(jarChooser.listbox.getItemText(2)) < 0);
        assertTrue(jarChooser.listbox.getItemText(2).compareTo(jarChooser.listbox.getItemText(3)) < 0);
    }

    @Override
    public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
    }
}
