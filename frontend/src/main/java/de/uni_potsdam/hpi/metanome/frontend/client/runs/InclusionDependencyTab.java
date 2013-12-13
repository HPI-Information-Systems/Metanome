package de.uni_potsdam.hpi.metanome.frontend.client.runs;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;

public class InclusionDependencyTab extends AlgorithmTab {

	public InclusionDependencyTab(BasePage basePage) {
		super(basePage);
		getInclusionDependencyAlgorithms();
	}

	private void getInclusionDependencyAlgorithms() {
		finderService.listInclusionDependencyAlgorithms(addJarChooserCallback);
	}

}
