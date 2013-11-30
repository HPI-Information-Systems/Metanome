package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;

public class ResultsTab extends DockPanel implements OmniscientResultReceiver {
	
	protected ExecutionServiceAsync executionService;
	
	protected Timer timer;
	protected String algorithmName;
	
	protected FlexTable uccTable;
	protected FlexTable indTable;
	protected FlexTable fdTable;
	protected FlexTable basicsTable;
	
	public ResultsTab(ExecutionServiceAsync executionService, String algorithmName) {
		this.executionService = executionService;
		this.algorithmName = algorithmName;
		this.uccTable = new FlexTable();
		this.add(uccTable, DockPanel.NORTH);
	}

	public void startPolling() {
		this.timer = new Timer() {
	      public void run() {		    	  
	    	  fetchNewResults();
	      }
	    };

	    this.timer.scheduleRepeating(1000);
	}
	
	public AsyncCallback<Long> getCancelCallback() {
		AsyncCallback<Long> callback = new AsyncCallback<Long>() {
		      public void onFailure(Throwable caught) {
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
		this.add(new Label("Algorithm did not execute successfully"), DockPanel.NORTH);
	}

	protected void fetchNewResults() {
		executionService.fetchNewResults(algorithmName, new AsyncCallback<ArrayList<Result>>() {
			  
			  @Override
			  public void onFailure(Throwable caught) {
				  // TODO Auto-generated method stub
				  System.out.println("Could not fetch results");
			  }
			  
			  @Override
			  public void onSuccess(ArrayList<Result> result) {
				  displayResults(result);
			  }
		  });
	}
	
	protected void displayResults(ArrayList<Result> results) {
		for (Result r : results){
			try {
				r.sendResultTo(this);
			} catch (CouldNotReceiveResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveResult(BasicStatistic statistic) {
		basicsTable.setText(basicsTable.getRowCount(), 0, statistic.getColumnCombination().toString());
		basicsTable.setText(basicsTable.getRowCount(), 1, statistic.getStatisticName());
		basicsTable.setText(basicsTable.getRowCount(), 2, statistic.getStatisticValue().toString());
	}

	@Override
	public void receiveResult(InclusionDependency inclusionDependency) {
		indTable.setText(indTable.getRowCount(), 0, inclusionDependency.getDependant().toString());
		indTable.setText(indTable.getRowCount(), 1, inclusionDependency.getReferenced().toString());
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination) {
		uccTable.setText(uccTable.getRowCount(), 0, uniqueColumnCombination.getColumnCombination().toString());		
	}

	@Override
	public void receiveResult(FunctionalDependency functionalDependency) {
		fdTable.setText(fdTable.getRowCount(), 0, functionalDependency.getDeterminant().toString());
		fdTable.setText(fdTable.getRowCount(), 1, functionalDependency.getDependant().toString());		
	}
}
