package de.uni_potsdam.hpi.metanome.frontend.client.results;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;

public class ResultsTab extends VerticalPanel implements OmniscientResultReceiver {
	
	protected ExecutionServiceAsync executionService;
	
	protected Timer timer;
	protected String algorithmName;
	
	protected HorizontalPanel resultsPanel;
	
	protected ResultTable uccTable;
	protected ResultTable indTable;
	protected ResultTable fdTable;
	protected ResultTable basicsTable;
	
	protected Image runningIndicator;
	
	public ResultsTab(ExecutionServiceAsync executionService, String algorithmName) {
		this.executionService = executionService;
		this.algorithmName = algorithmName;
		
		this.resultsPanel = new HorizontalPanel();
		this.setWidth("100%");
		this.add(resultsPanel);
		
		indTable = new ResultTable("Inclusion Dependencies");
		uccTable = new ResultTable("Unique Column Combinations");
		fdTable = new ResultTable("Functional Dependencies");
		basicsTable = new ResultTable("Basic Statistics");
		
		runningIndicator = new Image("ajax-loader.gif");
		this.add(runningIndicator);
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
		this.remove(runningIndicator);
		fetchNewResults();
		this.add(new Label("Algorithm executed in " +  executionTime/1000000d + " ms."));
	}
	
	public void cancelTimerOnFail(Throwable caught){
		this.timer.cancel();
		this.remove(runningIndicator);
		this.add(new Label("Algorithm did not execute successfully"));
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
		for (Result r : results) {
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
		if (this.resultsPanel.getWidgetIndex(basicsTable) < 0)
			this.resultsPanel.add(basicsTable);

		int row = basicsTable.getRowCount();
		basicsTable.setText(row, 0, statistic.getColumnCombination().toString());
		basicsTable.setText(row, 1, statistic.getStatisticName());
		basicsTable.setText(row, 2, statistic.getStatisticValue().toString());
	}

	@Override
	public void receiveResult(InclusionDependency inclusionDependency) {
		if (this.resultsPanel.getWidgetIndex(indTable) < 0)
			this.resultsPanel.add(indTable);

		int row = indTable.getRowCount();
		indTable.setText(row, 0, inclusionDependency.getDependant().toString());
		indTable.setText(row, 1, inclusionDependency.getReferenced().toString());
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
			throws CouldNotReceiveResultException {
		if (this.resultsPanel.getWidgetIndex(uccTable) < 0)
			this.resultsPanel.add(uccTable);

		int row = uccTable.getRowCount();
		int col = 0;
		for(ColumnIdentifier colId : uniqueColumnCombination.getColumnCombination().getColumnIdentifiers()) {
			uccTable.setText(row, col, colId.toString());
			col++;
		}
	}		

	@Override
	public void receiveResult(FunctionalDependency functionalDependency) {
		if (this.resultsPanel.getWidgetIndex(fdTable) < 0)
			this.resultsPanel.add(fdTable);

		int row = fdTable.getRowCount();
		fdTable.setText(row, 0, functionalDependency.getDeterminant().toString());
		fdTable.setText(row, 1, "-->");
		fdTable.setText(row, 2, functionalDependency.getDependant().toString());		
	}
}
