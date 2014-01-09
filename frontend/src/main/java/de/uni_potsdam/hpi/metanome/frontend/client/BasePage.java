package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage;
import de.uni_potsdam.hpi.metanome.frontend.client.datasources.DataSourcesPage;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.results.ResultsTab;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderServiceAsync;

/**
 * Overall Application page that has tabs for the various functions (subpages).
 * It also coordinates control between these subpages.
 * Should be added to RootPanel.
 */
public class BasePage extends TabLayoutPanel {
  
	protected TabLayoutPanel resultsPage;
	protected RunConfigurationPage runConfigurationsPage;
	
	protected FinderServiceAsync finderService;
	
	public enum Tabs {DATA_SOURCES, ALGORITHMS, RUN_CONFIGURATION, RESULTS, ABOUT};
	
	/**
	 * Constructor. Initiate creation of subpages.
	 */
	public BasePage() {
		super(1, Unit.CM);
		this.setWidth("100%");
		this.setHeight("100%");
				
		this.insert(new DataSourcesPage(), "Data Sources", Tabs.DATA_SOURCES.ordinal());
		this.insert(new AlgorithmsPage(this), "Algorithms", Tabs.ALGORITHMS.ordinal());
		runConfigurationsPage = new RunConfigurationPage(this);
		this.insert(runConfigurationsPage, "Run Configuration", Tabs.RUN_CONFIGURATION.ordinal());
		this.insert(createResultsPage(), "Results", Tabs.RESULTS.ordinal());
		this.insert(createAboutPage(), "About", Tabs.ABOUT.ordinal());
	}
	
	/**
	 * Create the "About" Page, which should include information about the project.
	 * 
	 * @return Widget with contents to be placed on the page.
	 */
	private Widget createAboutPage() {
		Label temporaryContent = new Label();
		temporaryContent.setText("Metanome Version 0.0.1.");
		return temporaryContent;	
	}
	
	private TabLayoutPanel createResultsPage() {
		resultsPage = new TabLayoutPanel(1, Unit.CM);
		resultsPage.setHeight("100%");
		resultsPage.setWidth("99%");

		return resultsPage;
	}

	/**
	 * Hand control from the Run Configuration to displaying Results. Start executing the algorithm
	 * and fetch results at a regular interval.
	 * 
	 * @param executionService
	 * @param algorithmName		
	 * @param parameters
	 */
	public void startExecutionAndResultPolling(ExecutionServiceAsync executionService,
			String algorithmName, List<InputParameter> parameters) {
		
		String executionIdentifier = getExecutionIdetifier(algorithmName);
		
		ScrollPanel resultsTab = new ScrollPanel();
		resultsTab.setHeight("95%");
		resultsPage.add(resultsTab, new TabHeader(algorithmName, resultsTab, resultsPage));

		ResultsTab resultsTabContent = new ResultsTab(executionService, executionIdentifier);
		executionService.executeAlgorithm(algorithmName, executionIdentifier, parameters, resultsTabContent.getCancelCallback());
		resultsTabContent.startPolling();
		
		resultsTab.add(resultsTabContent);

		this.selectTab(resultsPage);
		resultsPage.selectTab(resultsTab);
	}
	
	protected String getExecutionIdetifier(String algorithmName) {
		DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd'T'HHmmss");
		return algorithmName + format.format(new Date());		
	}

	/**
	 * Hand control from Algorithms page to Run Configurations, and preconfigure the latter with 
	 * the algorithm.
	 * 
	 * @param algorithmName	algorithm that shall be run
	 */
	public void jumpToRunConfiguration(String algorithmName) {
		this.selectTab(Tabs.RUN_CONFIGURATION.ordinal());
		this.runConfigurationsPage.selectAlgorithm(algorithmName);
	}

	/**
	 * Forwards any algorithms found by AlgorithmPage to be available in RunConfigurations
	 * 
	 * @param algorithmNames
	 */
	public void addAlgorithmsToRunConfigurations(String... algorithmNames) {
		this.runConfigurationsPage.addAlgorithms(algorithmNames);
	}
	
}
