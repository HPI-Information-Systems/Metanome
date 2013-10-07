package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
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
    
	@Test
	public void testExecutionService(){
		//Setup
		List<InputParameter> configs = new ArrayList<InputParameter>();
		InputParameterString inputParameter = new InputParameterString("pathToInputFile");
		inputParameter.setValue("blub");
		configs.add(inputParameter);
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		      public void onFailure(Throwable caught) {
		    	  fail();
		      }

		      public void onSuccess(Void result) {  	  
		    	  finishTest();
		      }
		    };

		  ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
		  
		  
		  // Set a delay period
		  delayTestFinish(500);

		  //Execute
		  executionService.executeUniqueColumnCombinationsAlgorithm(
				  "example_algorithm-0.0.1-SNAPSHOT-jar-with-dependencies.jar", 
				  configs, callback);

	}
	
	@Test
	public void testParameterService(){
		//Setup
		AsyncCallback<List<InputParameter>> callback = new AsyncCallback<List<InputParameter>>() {
		      public void onFailure(Throwable caught) {
		    	  fail();
		      }

		      public void onSuccess(List<InputParameter> result) {  	  
		    	  assertNotNull(result);
		    	  finishTest();
		      }
		    };

		  ParameterServiceAsync parameterService = GWT.create(ParameterService.class);
		  
		  
		  // Set a delay period
		  delayTestFinish(500);

		  //Execute
		  parameterService.retrieveUniqueColumnCombinationsParameters(
				  "example_algorithm-0.0.1-SNAPSHOT-jar-with-dependencies.jar", callback);

	}
	
	@Test
	public void testFinderService(){
		//Setup
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
