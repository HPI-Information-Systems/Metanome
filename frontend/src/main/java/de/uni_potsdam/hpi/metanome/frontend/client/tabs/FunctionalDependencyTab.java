package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;

public class FunctionalDependencyTab extends AlgorithmTab {
	
	/**
	 * Constructor.
	 */
	public FunctionalDependencyTab(BasePage basePage){
		super(basePage);
		getFunctionalDependencyAlgorithms();
	}

	/**
	 * Service call to retrieve available FD algorithms
	 */
	private void getFunctionalDependencyAlgorithms() {
		finderService.listFunctionalDependencyAlgorithms(addJarChooserCallback);
	}
	
}
