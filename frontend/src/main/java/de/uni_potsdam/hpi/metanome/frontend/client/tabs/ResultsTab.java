package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;

public class ResultsTab extends DockPanel {
	
	protected ExecutionServiceAsync executionService;
	
	protected Timer timer;
	protected String algorithmName;
	protected FlexTable resultTable;

	public ResultsTab(ExecutionServiceAsync executionService, String algorithmName) {
		this.executionService = executionService;
		this.algorithmName = algorithmName;
		this.resultTable = new FlexTable();
		this.add(resultTable, DockPanel.NORTH);
		//TODO add UI elements
	}

	public void startPolling() {
		this.timer = new Timer() {
	      public void run() {		    	  
	    	  fetchNewResults();
	      }
	    };

	    this.timer.scheduleRepeating(200);
	}
	
	public AsyncCallback<Long> getCancelCallback() {
		AsyncCallback<Long> callback = new AsyncCallback<Long>() {
		      public void onFailure(Throwable caught) {
		    	  // TODO: Do something with errors.
		    	  System.out.println("Algorithm did not execute successfully");
				  cancelTimerOnFail(caught);
		      }

		      public void onSuccess(Long executionTime) {  	
				  cancelTimerOnSuccess(executionTime);
		      }
		};
		return callback;
	}
	
	public void cancelTimerOnSuccess(Long executionTime){
		this.timer.cancel();
		fetchNewResults();
		this.add(new Label("Algorithm executed in " +  executionTime/1000000d + " ms."), DockPanel.NORTH);
	}
	
	public void cancelTimerOnFail(Throwable caught){
		this.timer.cancel();
		//TODO: Display error message
	}

	protected void fetchNewResults() {
		executionService.fetchNewResults(algorithmName, new AsyncCallback<List<String>>() {
			  
			  @Override
			  public void onFailure(Throwable caught) {
				  // TODO Auto-generated method stub
			  }
			  
			  @Override
			  public void onSuccess(List<String> result) {
				  // TODO Auto-generated method stub
				  System.out.println("Successfully fetched results:");
				  displayResults(result);
			  }
		  });
	}
	
	protected void displayResults(List<String> results){
		for (String s : results)
			resultTable.setText(resultTable.getRowCount(), 0, s);
	}
}
