package de.uni_potsdam.hpi.metanome.frontend.client.tabs;


public class BasicStatisticsTab extends AlgorithmTab {
	
	/**
	 * Constructor.
	 */
	public BasicStatisticsTab(){
		super();
		getBasicStatisticsAlgorithms();
	}

	/**
	 * Service call to retrieve available basic statistics algorithms
	 */
	private void getBasicStatisticsAlgorithms() {
		finderService.listBasicStatisticsAlgorithms(addJarChooserCallback);
	}
	
}
