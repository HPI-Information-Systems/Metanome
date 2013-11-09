package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

public class InclusionDependencyTab extends AlgorithmTab {

	public InclusionDependencyTab() {
		super();
		getInclusionDependencyAlgorithms();
	}

	private void getInclusionDependencyAlgorithms() {
		finderService.listInclusionDependencyAlgorithms(addJarChooserCallback);
	}

}
