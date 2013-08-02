package de.uni_potsdam.hpi.metanome.frontend.client.tabs;


public class UniqueColumnCombinationTab extends AlgorithmTab {

	public UniqueColumnCombinationTab() {
		//TODO retrieve available algorithms
		String[] filenames = {"DUCC", "HCA"};
		this.addJarChooser(filenames);
	}

}
