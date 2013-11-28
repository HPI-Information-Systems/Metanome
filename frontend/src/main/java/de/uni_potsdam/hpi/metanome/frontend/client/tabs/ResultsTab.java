package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
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
		executionService.fetchNewResults(algorithmName, new AsyncCallback<List<Result>>() {
			  
			  @Override
			  public void onFailure(Throwable caught) {
				  // TODO Auto-generated method stub
			  }
			  
			  @Override
			  public void onSuccess(List<Result> result) {
				  displayResults(result);
			  }
		  });
	}
	
	protected void displayResults(List<Result> results) {
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
	public void receiveResult(ColumnCombination columns, String statisticName,
			Object statisticValue) {
		basicsTable.setText(basicsTable.getRowCount(), 0, columns.toString());
		basicsTable.setText(basicsTable.getRowCount(), 1, statisticName);
		basicsTable.setText(basicsTable.getRowCount(), 2, statisticName.toString());
	}

	@Override
	public void receiveResult(ColumnCombination determinant,
			ColumnIdentifier dependent) throws CouldNotReceiveResultException {
		fdTable.setText(fdTable.getRowCount(), 0, determinant.toString());
		fdTable.setText(fdTable.getRowCount(), 1, dependent.toString());
	}

	@Override
	public void receiveResult(ColumnCombination dependent,
			ColumnCombination referenced) throws CouldNotReceiveResultException {
		indTable.setText(indTable.getRowCount(), 0, dependent.toString());
		indTable.setText(indTable.getRowCount(), 1, referenced.toString());
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
			throws CouldNotReceiveResultException {
		for(ColumnIdentifier col : uniqueColumnCombination.getColumnCombination().getColumnIdentifiers()) {
			int row = uccTable.getRowCount();
			uccTable.setText(row, uccTable.getCellCount(row), col.toString());
		}		
	}
}
