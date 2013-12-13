package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.BasicStatisticsTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.FunctionalDependencyTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.InclusionDependencyTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.ResultsTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.UniqueColumnCombinationTab;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.TabHeader;

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
		this.setHeight("95%");
		  
		this.add(createDataSourcesPage(), "Data Sources");
		this.add(createRunsPage(), "New Run");
		this.add(createResultsPage(), "Results");
	}

	private Widget createDataSourcesPage() {
		Label temporaryContent = new Label();
		temporaryContent.setText("As data sources, you can configure any database connection in the Run Configurations,"
				+ "or choose from any CSV-files in the designated folder.");
		return temporaryContent;
	}

	private TabLayoutPanel createRunsPage() {
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
	
	private TabLayoutPanel createResultsPage() {
		resultsPage = new TabLayoutPanel(1, Unit.CM);
		resultsPage.setHeight("100%");
		resultsPage.setWidth("99%");

		return resultsPage;
	}

	public void startExecutionAndResultPolling(ExecutionServiceAsync executionService,
			String algorithmName, List<InputParameter> parameters) {
		ResultsTab resultsTab = new ResultsTab(executionService, algorithmName);
		executionService.executeAlgorithm(algorithmName, parameters, resultsTab.getCancelCallback());
		resultsTab.startPolling();
				
		ScrollPanel scrollableResultsTab = new ScrollPanel(resultsTab);
		resultsPage.add(scrollableResultsTab, new TabHeader(algorithmName, scrollableResultsTab, resultsPage));

		this.selectTab(resultsPage);
		resultsPage.selectTab(scrollableResultsTab);
	}

}
