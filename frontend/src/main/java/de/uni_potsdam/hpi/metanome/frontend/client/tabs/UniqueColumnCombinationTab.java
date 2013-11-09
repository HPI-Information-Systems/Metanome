package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

public class UniqueColumnCombinationTab extends AlgorithmTab {

	public UniqueColumnCombinationTab() {
		super();
		getUniqueColumnCombinationsAlgorithms();
	}

	private void getUniqueColumnCombinationsAlgorithms() {
		finderService.listUniqueColumnCombinationsAlgorithms(addJarChooserCallback);
	}

}
