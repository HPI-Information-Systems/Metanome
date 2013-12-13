package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.jarchooser.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.AlgorithmTab;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.BasicStatisticsTab;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.FunctionalDependencyTab;

/**
 * Tests for the algorithm specific pages (tabs)
 */
public class GwtTestAlgorithmTab extends GWTTestCase {
	
	private BasePage page;

	@Test
	public void testAddJarChooserFD(){
		//Setup
		page = new BasePage();
		AlgorithmTab algoTab = new FunctionalDependencyTab(page);
		
		//Execute
		algoTab.addJarChooser("Algo1", "Algo2", "Algo3");
		
		//Check
		assertEquals(1, algoTab.getWidgetCount());
		assertTrue(algoTab.getJarChooser() instanceof JarChooser);
	}
	
	@Test
	public void testAddParameterTableFD(){
		//Setup
		AlgorithmTab algoTab = new FunctionalDependencyTab(page);
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		int widgetCount = algoTab.getWidgetCount();
				
		//Execute
		algoTab.addParameterTable(paramList);
				
		//Check
		assertEquals(widgetCount + 1, algoTab.getWidgetCount());
	}

	@Test
	public void testAddJarChooserBS(){
		//Setup
		AlgorithmTab algoTab = new BasicStatisticsTab(page);
		
		//Execute
		algoTab.addJarChooser("Algo1", "Algo2", "Algo3");
		
		//Check
		assertEquals(1, algoTab.getWidgetCount());
		assertTrue(algoTab.getJarChooser() instanceof JarChooser);
	}
	
	@Test
	public void testAddParameterTableBS(){
		//Setup
		AlgorithmTab algoTab = new BasicStatisticsTab(page);
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
