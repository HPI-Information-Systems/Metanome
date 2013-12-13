package de.uni_potsdam.hpi.metanome.frontend.client.runs;

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
