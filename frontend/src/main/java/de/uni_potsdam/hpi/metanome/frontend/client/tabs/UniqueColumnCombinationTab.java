package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;

public class UniqueColumnCombinationTab extends AlgorithmTab {

	public UniqueColumnCombinationTab(BasePage basePage) {
		super(basePage);
		getUniqueColumnCombinationsAlgorithms();
	}

	private void getUniqueColumnCombinationsAlgorithms() {
		finderService.listUniqueColumnCombinationsAlgorithms(addJarChooserCallback);
	}

}
