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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterDataSource;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterServiceAsync;

/**
 * Tests RPC calls to server
 */
public class GwtTestServiceCall extends GWTTestCase {
    
	/**
	 * tests the call from client to executionService.executeAlgorithm()
	 */
	@Test
	public void testExecutionService() {
		// Setup
		List<InputParameter> configs = new ArrayList<InputParameter>();
		InputParameterString inputParameter = new InputParameterString(
				"pathToInputFile");
		inputParameter.setValues("path/to/file1", "path/to/file2");
		configs.add(inputParameter);

		AsyncCallback<Long> callback = new AsyncCallback<Long>() {
			public void onFailure(Throwable caught) {
				fail();
			}

			public void onSuccess(Long executionTime) {
				assertTrue(executionTime > 0);
				finishTest();
			}
		};

		ExecutionServiceAsync executionService = GWT
				.create(ExecutionService.class);

		// Set a delay period
		delayTestFinish(500);

		// Execute
		executionService.executeAlgorithm("example_ucc_algorithm.jar", 
				"executionIdentifier1", 
				configs, new LinkedList<InputParameterDataSource>(), callback);
	}

	/**
	 * tests the call from client to parameterService.retrieveParameters
	 */
	@Test
	public void testParameterService() {
		// Setup
		AsyncCallback<List<InputParameter>> callback = new AsyncCallback<List<InputParameter>>() {
			public void onFailure(Throwable caught) {
				fail();
			}

			public void onSuccess(List<InputParameter> result) {
				assertNotNull(result);
				finishTest();
			}
		};

		ParameterServiceAsync parameterService = GWT
				.create(ParameterService.class);

		// Set a delay period
		delayTestFinish(500);

		// Execute
		parameterService.retrieveParameters("example_ucc_algorithm.jar", callback);

	}
	
	/**
	 * tests the call from client to finderService.listInclusionDependencyAlgorithms()
	 */
	@Test
	public void testFinderService() {
		// Setup
		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
			public void onFailure(Throwable caught) {
				fail();
			}

			public void onSuccess(String[] result) {
				assertNotNull(result);
				finishTest();
			}
		};

		FinderServiceAsync finderService = GWT.create(FinderService.class);

		// Set a delay period
		delayTestFinish(500);

		finderService.listInclusionDependencyAlgorithms(callback);

	}
	
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}
}
