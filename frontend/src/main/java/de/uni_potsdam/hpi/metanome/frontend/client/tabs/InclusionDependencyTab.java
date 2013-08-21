package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import com.google.gwt.user.client.ui.DockPanel;


public class InclusionDependencyTab extends AlgorithmTab {

	public InclusionDependencyTab() {
		//TODO retrieve available algorithms
		String[] filenames = {"Spider", "brute-force"};
		this.addJarChooser(filenames);	
	}

	@Override
	public void addJarChooser(String... filenames) {
		jarChooser = new InclusionDependencyJarChooser(filenames);
		this.add(jarChooser, DockPanel.NORTH);
	}
}
