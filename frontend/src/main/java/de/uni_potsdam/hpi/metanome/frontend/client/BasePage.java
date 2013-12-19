package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.results.ResultsTab;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.AlgorithmTab;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.TabHeader;

/**
 * Overall Application page that has tabs for the various functions (subpages).
 * It also coordinates control between these subpages.
 * Should be added to RootPanel.
 */
public class BasePage extends TabLayoutPanel {
  
	private VerticalPanel runsPage;
	private TabLayoutPanel resultsPage;
	
	/**
	 * Constructor. Initiate creation of subpages.
	 */
	public BasePage() {
		super(1, Unit.CM);
		this.setWidth("100%");
		this.setHeight("95%");
		  
		this.add(createAboutPage(), "About");
		this.add(createDataSourcesPage(), "Data Sources");
		this.add(new AlgorithmsPage(this), "Algorithms");
		this.add(createRunsPage(), "New Run");
		this.add(createResultsPage(), "Results");
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

	/**
	 * Create the "Data Sources" page, which should list available data sources and
	 * allow to configure new ones.
	 * 
	 * @return Widget with contents to be placed on the page.
	 */
	private Widget createDataSourcesPage() {
		Label temporaryContent = new Label();
		temporaryContent.setText("As data sources, you can configure any database connection in the Run Configurations,"
				+ "or choose from any CSV-files in the designated folder.");
		return temporaryContent;
	}

	/**
	 * Create the "Run Configuration" page, which allows to configure and start
	 * a new algorithm run.
	 * 
	 * @return Widget with contents to be placed on the page.
	 */
	private VerticalPanel createRunsPage() {
		runsPage = new VerticalPanel();
		runsPage.setHeight("100%");
		runsPage.setWidth("100%");
		
		runsPage.add(new AlgorithmTab(this));
		
		return runsPage;
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
		ResultsTab resultsTab = new ResultsTab(executionService, algorithmName);
		executionService.executeAlgorithm(algorithmName, parameters, resultsTab.getCancelCallback());
		resultsTab.startPolling();
				
		ScrollPanel scrollableResultsTab = new ScrollPanel(resultsTab);
		resultsPage.add(scrollableResultsTab, new TabHeader(algorithmName, scrollableResultsTab, resultsPage));

		this.selectTab(resultsPage);
		resultsPage.selectTab(scrollableResultsTab);
	}

	/**
	 * Hand control from Algorithms page to Run Configurations, and preconfigure the latter with 
	 * the algorithm.
	 * 
	 * @param algorithmName	algorithm that shall be run
	 */
	public void jumpToRunConfiguration(String algorithmName) {
		// TODO Auto-generated method stub
		System.out.println("Jump to " + algorithmName);
	}

}
