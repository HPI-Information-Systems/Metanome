package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.BasicStatisticsTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.FunctionalDependencyTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.InclusionDependencyTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.ResultsTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.UniqueColumnCombinationTab;

/**
 * Overall Application page that has one tab for each algorithm type.
 * Should be added to RootPanel.
 */
public class BasePage extends TabLayoutPanel {
  
	private TabLayoutPanel algorithmsPage;
	private TabLayoutPanel resultsPage;
	
	public BasePage() {
		super(1, Unit.CM);
		this.setWidth("100%");
		this.setHeight("100%");
		  
		this.add(createAlgorithmsTab(), "Algorithms");
		this.add(createResultsTab(), "Results");
	}

	private TabLayoutPanel createAlgorithmsTab() {
		algorithmsPage = new TabLayoutPanel(1, Unit.CM);
		algorithmsPage.setHeight("100%");
		algorithmsPage.setWidth("100%");
		
		algorithmsPage.add(new UniqueColumnCombinationTab(this), "Unique Column Combinations");
		algorithmsPage.add(new InclusionDependencyTab(this), "Inclusion Dependencies");
		algorithmsPage.add(new FunctionalDependencyTab(this), "Functional Dependencies");
		algorithmsPage.add(new BasicStatisticsTab(this), "Basic Statistics");
		
		algorithmsPage.selectTab(0);
		return algorithmsPage;
	}
	
	private TabLayoutPanel createResultsTab() {
		resultsPage = new TabLayoutPanel(1, Unit.CM);
		resultsPage.setHeight("100%");
		resultsPage.setWidth("100%");

		return resultsPage;
	}

	public AsyncCallback<Void> showResults(ExecutionServiceAsync executionService,
			String algorithmName) {
		ResultsTab resultsTab = new ResultsTab(executionService, algorithmName);
		resultsTab.startPolling();
		
		this.selectTab(resultsPage);
		resultsPage.add(resultsTab, algorithmName);
		resultsPage.selectTab(resultsTab);
		
		return resultsTab.getCancelCallback();
	}

}
