package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.ParameterTable;

/**
 * 
 * 
 */
public abstract class AlgorithmTab extends DockPanel{
	private ParameterTable parameterTable;
	private JarChooser jarChooser;
		
	public void addParameterTable(List<InputParameter> paramList){
		parameterTable = new ParameterTable(paramList);
		this.add(parameterTable, DockPanel.NORTH);
	}

	public ParameterTable getParameterTable() {
		return parameterTable;
	}
	
	public void addJarChooser(String... filenames) {
		jarChooser = new JarChooser(filenames, "FD");
		this.add(jarChooser, DockPanel.NORTH);
	}
	
	public JarChooser getJarChooser() {
		return jarChooser;
	}
}
