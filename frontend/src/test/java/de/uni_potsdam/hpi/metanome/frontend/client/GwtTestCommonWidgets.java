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

package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.LinkedList;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.runs.AlgorithmChooser;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

public class GwtTestCommonWidgets extends GWTTestCase {

    @Test
    public void testAlgorithmChooser() {
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

    @Override
    public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
    }

}
