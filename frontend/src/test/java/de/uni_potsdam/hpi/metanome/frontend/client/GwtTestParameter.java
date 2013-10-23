package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterInteger;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterBooleanWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterCsvFileWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterIntegerWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterStringWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.ParameterTable;

public class GwtTestParameter extends GWTTestCase {
	
	@Test
	public void testParameterTable() {
		//Setup
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		
		InputParameterString inputParameterString = new InputParameterString("Filename");
		InputParameterBoolean inputParameterBoolean = new InputParameterBoolean("Omit warnings");
		InputParameterInteger inputParameterInteger = new InputParameterInteger("Number of runs");
		InputParameterCsvFile inputParameterCsvFile = new InputParameterCsvFile("inputData");
		
		paramList.add(inputParameterString);
		paramList.add(inputParameterBoolean);
		paramList.add(inputParameterInteger);
		paramList.add(inputParameterCsvFile);
		
		//Execute
		ParameterTable pt = new ParameterTable(paramList);
		
		//Check
		assertEquals(5, pt.getRowCount());
		
		// - STRING row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(InputParameterStringWidget.class, pt.getWidget(0, 1).getClass());
		
		// - BOOL row
		assertEquals(2, pt.getCellCount(1));
		assertEquals(InputParameterBooleanWidget.class, pt.getWidget(1, 1).getClass());
				
		// - INT row
		assertEquals(2, pt.getCellCount(2));
		assertEquals(InputParameterIntegerWidget.class, pt.getWidget(2, 1).getClass());
		
		// - CSV FILE row
		assertEquals(2, pt.getCellCount(3));
		assertEquals(InputParameterCsvFileWidget.class, pt.getWidget(3, 1).getClass());
		
		// - Submit button row
		assertEquals(1, pt.getCellCount(4));
		assertEquals(Button.class, pt.getWidget(4, 0).getClass());
	}
	
	@Test
	public void testRetrieveParameterValues(){	
		//Setup
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		
		InputParameterString inputParameterString = new InputParameterString("string");
		InputParameterBoolean inputParameterBoolean = new InputParameterBoolean("bool");
		InputParameterInteger inputParameterInteger = new InputParameterInteger("int");
		InputParameterCsvFile inputParameterCsvFile = new InputParameterCsvFile("csv");
		
		paramList.add(inputParameterString);
		paramList.add(inputParameterBoolean);
		paramList.add(inputParameterInteger);
		paramList.add(inputParameterCsvFile);
		
		ParameterTable pt = new ParameterTable(paramList);

		//Execute
		List<InputParameter> retrievedParams = pt.getInputParametersFromChildren();
		
		//Check
		assertTrue(retrievedParams.contains(inputParameterString));
		assertTrue(retrievedParams.contains(inputParameterBoolean));
		assertTrue(retrievedParams.contains(inputParameterInteger));
		assertTrue(retrievedParams.contains(inputParameterCsvFile));
	}
	
	@Test
	public void testInputParameterWidgetCreation(){
		//Setup
		String identifierString = "stringParam";
		InputParameter stringParam = new InputParameterString(identifierString);
		String identifierBoolean = "boolParam";
		InputParameter boolParam = new InputParameterBoolean(identifierBoolean);
		String identifierInteger = "intParam";
		InputParameter intParam = new InputParameterInteger(identifierInteger);
		String identifierCsv = "csvParam";
		InputParameter csvParam = new InputParameterCsvFile(identifierCsv);
		
		//Execute
		InputParameterWidget stringWidget = stringParam.createWrappingWidget();
		InputParameterWidget boolWidget = boolParam.createWrappingWidget();
		InputParameterWidget intWidget = intParam.createWrappingWidget();
		InputParameterWidget csvWidget = csvParam.createWrappingWidget();
		
		//Check
		assertTrue(stringWidget instanceof InputParameterStringWidget);
		assertEquals(identifierString, stringWidget.getInputParameter().getIdentifier());
		
		assertTrue(boolWidget instanceof InputParameterBooleanWidget);
		assertEquals(identifierBoolean, boolWidget.getInputParameter().getIdentifier());
		
		assertTrue(intWidget instanceof InputParameterIntegerWidget);
		assertEquals(identifierInteger, intWidget.getInputParameter().getIdentifier());
		
		assertTrue(csvWidget instanceof InputParameterCsvFileWidget);
		assertEquals(identifierCsv, csvWidget.getInputParameter().getIdentifier());
	}

		
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}
}
