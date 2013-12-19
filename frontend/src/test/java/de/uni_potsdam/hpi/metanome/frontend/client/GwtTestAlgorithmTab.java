package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.jarchooser.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.AlgorithmTab;

/**
 * Tests for the algorithm specific pages (tabs)
 */
public class GwtTestAlgorithmTab extends GWTTestCase {
	
	private BasePage page;

	@Test
	public void testAddJarChooser(){
		//Setup
		page = new BasePage();
		AlgorithmTab algoTab = new AlgorithmTab(page);
		
		//Execute
		algoTab.addJarChooser("Algo1", "Algo2", "Algo3");
		
		//Check
		assertEquals(1, algoTab.getWidgetCount());
		assertTrue(algoTab.getJarChooser() instanceof JarChooser);
	}
	
	@Test
	public void testAddParameterTable(){
		//Setup
		AlgorithmTab algoTab = new AlgorithmTab(page);
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		int widgetCount = algoTab.getWidgetCount();
				
		//Execute
		algoTab.addParameterTable(paramList);
				
		//Check
		assertEquals(widgetCount + 1, algoTab.getWidgetCount());
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}

}
