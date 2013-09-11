package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import com.google.gwt.user.client.ui.DockPanel;


public class UniqueColumnCombinationTab extends AlgorithmTab {

	public UniqueColumnCombinationTab() {
		super();
		getUniqueColumnCombinationsAlgorithms();
	}

	private void getUniqueColumnCombinationsAlgorithms() {
		finderService.listUniqueColumnCombinationsAlgorithms(addJarChooserCallback);
	}

	@Override
	public void addJarChooser(String... filenames) {
		jarChooser = new UniqueColumnCombinationsJarChooser(filenames);
		this.add(jarChooser, DockPanel.NORTH);
	}
}
