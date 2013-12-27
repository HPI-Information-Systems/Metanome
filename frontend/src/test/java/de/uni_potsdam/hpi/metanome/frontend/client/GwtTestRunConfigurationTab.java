package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.jarchooser.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;

/**
 * Tests for the algorithm specific pages (tabs)
 */
public class GwtTestRunConfigurationTab extends GWTTestCase {
	
	private BasePage page;

	@Test
	public void testAddJarChooser(){
		//Execute
		RunConfigurationPage algoTab = new RunConfigurationPage(page);
		
		//Check - should contain only the jarChooser so far
		assertEquals(1, algoTab.getWidgetCount());
		assertTrue(algoTab.getJarChooser() instanceof JarChooser);
	}
	
	@Test
	public void testAddParameterTable(){
		//Setup
		RunConfigurationPage algoTab = new RunConfigurationPage(page);
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		int widgetCount = algoTab.getWidgetCount();
				
		//Execute
		algoTab.addParameterTable(paramList);
				
		//Check
		assertEquals(widgetCount + 1, algoTab.getWidgetCount());
	}
	
	@Test
	public void testAddAlgorithms() {
		//Setup
		RunConfigurationPage algoTab = new RunConfigurationPage(page);
		int noOfAlgorithms = algoTab.getJarChooser().getListItemCount();
		
		//Execute
		algoTab.addAlgorithms("Additional 1", "Additional 2");
		
		//Check
		assertEquals(noOfAlgorithms + 2, algoTab.getJarChooser().getListItemCount());
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}

}
