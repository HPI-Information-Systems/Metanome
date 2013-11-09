package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

public class FunctionalDependencyTab extends AlgorithmTab {
	
	/**
	 * Constructor.
	 */
	public FunctionalDependencyTab(){
		super();
		getFunctionalDependencyAlgorithms();
	}

	/**
	 * Service call to retrieve available FD algorithms
	 */
	private void getFunctionalDependencyAlgorithms() {
		finderService.listFunctionalDependencyAlgorithms(addJarChooserCallback);
	}
	
}
