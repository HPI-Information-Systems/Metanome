package de.uni_potsdam.hpi.metanome.frontend.client.tabs;


public class InclusionDependencyTab extends AlgorithmTab {

	public InclusionDependencyTab() {
		//TODO retrieve available algorithms
		String[] filenames = {"Spider", "brute-force"};
		this.addJarChooser(filenames);	
	}
}
