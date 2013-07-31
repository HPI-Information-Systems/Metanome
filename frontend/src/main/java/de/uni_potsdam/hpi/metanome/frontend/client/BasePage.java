package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import com.google.gwt.user.client.ui.DockPanel;
import de.uni_potsdam.hpi.metanome.frontend.server.InputParameter;

/**
 * HelloWorld application.
 */
public class BasePage extends DockPanel {

	private JarChooser jarChooser;
	private ParameterTable parameterTable;
  
  public BasePage() {
	  //TODO: get available algorithms from server
	String[] filenames = {"duplicateDetection.jar", "functionalDependencies.jar"};
	jarChooser = new JarChooser(filenames);
	this.add(jarChooser, DockPanel.NORTH);
  }

  public void addParameterTable(List<InputParameter> paramList){
	  parameterTable = new ParameterTable(paramList);
	  this.add(parameterTable, DockPanel.NORTH);
  }
  
  public JarChooser getJarChooser() {
	  return jarChooser;
  }

  public ParameterTable getParameterTable() {
	  return parameterTable;
  }

}
