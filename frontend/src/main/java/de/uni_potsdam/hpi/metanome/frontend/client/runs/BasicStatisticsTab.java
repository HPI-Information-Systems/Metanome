package de.uni_potsdam.hpi.metanome.frontend.client.runs;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;


public class BasicStatisticsTab extends AlgorithmTab {
	
	/**
	 * Constructor.
	 */
	public BasicStatisticsTab(BasePage basePage){
		super(basePage);
		getBasicStatisticsAlgorithms();
	}

	/**
	 * Service call to retrieve available basic statistics algorithms
	 */
	private void getBasicStatisticsAlgorithms() {
		finderService.listBasicStatisticsAlgorithms(addJarChooserCallback);
	}
	
}
