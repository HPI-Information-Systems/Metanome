package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
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
	
	protected HorizontalPanel resultsPanel;
	
	protected FlexTable uccTable;
	protected FlexTable indTable;
	protected FlexTable fdTable;
	protected FlexTable basicsTable;
	
	public ResultsTab(ExecutionServiceAsync executionService, String algorithmName) {
		this.executionService = executionService;
		this.algorithmName = algorithmName;
		
		this.resultsPanel = new HorizontalPanel();
		this.add(resultsPanel, DockPanel.NORTH);
		
		indTable = new FlexTable();
		this.resultsPanel.add(indTable);
		uccTable = new FlexTable();
		this.resultsPanel.add(uccTable);
		fdTable = new FlexTable();
		this.resultsPanel.add(fdTable);
		basicsTable = new FlexTable();
		this.resultsPanel.add(basicsTable);		
	}

	public void startPolling() {
		this.timer = new Timer() {
	      public void run() {		    	  
	    	  fetchNewResults();
	      }
	    };

	    this.timer.scheduleRepeating(10000);
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
		int row = basicsTable.getRowCount();
		basicsTable.setText(row, 0, statistic.getColumnCombination().toString());
		basicsTable.setText(row, 1, statistic.getStatisticName());
		basicsTable.setText(row, 2, statistic.getStatisticValue().toString());
	}

	@Override
	public void receiveResult(InclusionDependency inclusionDependency) {
		int row = indTable.getRowCount();
		indTable.setText(row, 0, inclusionDependency.getDependant().toString());
		indTable.setText(row, 1, inclusionDependency.getReferenced().toString());
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
			throws CouldNotReceiveResultException {
		int row = uccTable.getRowCount();
		int col = 0;
		for(ColumnIdentifier colId : uniqueColumnCombination.getColumnCombination().getColumnIdentifiers()) {
			uccTable.setText(row, col, colId.toString());
			col++;
		}
	}		

	@Override
	public void receiveResult(FunctionalDependency functionalDependency) {
		int row = fdTable.getRowCount();
		fdTable.setText(row, 0, functionalDependency.getDeterminant().toString());
		fdTable.setText(row, 1, functionalDependency.getDependant().toString());		
	}
}
