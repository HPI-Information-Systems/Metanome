package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.ParameterTable;
import de.uni_potsdam.hpi.metanome.frontend.server.InputParameter;

/**
 * 
 * 
 */
public class AlgorithmTab extends DockPanel{
	private JarChooser jarChooser;
	private ParameterTable parameterTable;
	
	public void addJarChooser(String... filenames){
		jarChooser = new JarChooser(filenames);
		this.add(jarChooser);
	}
	
	public void addParameterTable(List<InputParameter> paramList){
		parameterTable = new ParameterTable(paramList);
		this.add(parameterTable);
	}
	  
	public JarChooser getJarChooser() {
		return jarChooser;
	}

	public ParameterTable getParameterTable() {
		return parameterTable;
	}
}
