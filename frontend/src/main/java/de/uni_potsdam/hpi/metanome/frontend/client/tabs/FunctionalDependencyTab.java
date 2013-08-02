package de.uni_potsdam.hpi.metanome.frontend.client.tabs;


public class FunctionalDependencyTab extends AlgorithmTab {

	public FunctionalDependencyTab(){
		//TODO retrieve available algorithms
		String[] filenames = {"Gordian"};
		this.addJarChooser(filenames);
	}
}
