package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.FunctionalDependencyJarChooser;


public class FunctionalDependencyTab extends AlgorithmTab {
	
	/**
	 * Constructor.
	 */
	public FunctionalDependencyTab(){
		super();
		getFunctionalDependencyAlgorithms();
	}

	/**
	 * Service call to retrieve available FD algorithms
	 */
	private void getFunctionalDependencyAlgorithms() {
		finderService.listFunctionalDependencyAlgorithms(addJarChooserCallback);
	}

	@Override
	public void addJarChooser(String... filenames) {
		jarChooser = new FunctionalDependencyJarChooser(filenames);
		this.add(jarChooser, DockPanel.NORTH);
	}
}
