package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import com.google.gwt.user.client.ui.DockPanel;


public class UniqueColumnCombinationTab extends AlgorithmTab {

	public UniqueColumnCombinationTab() {
		//TODO retrieve available algorithms
		String[] filenames = {"DUCC", "HCA"};
		this.addJarChooser(filenames);
	}

	@Override
	public void addJarChooser(String... filenames) {
		jarChooser = new UniqueColumnCombinationsJarChooser(filenames);
		this.add(jarChooser, DockPanel.NORTH);
	}
}
