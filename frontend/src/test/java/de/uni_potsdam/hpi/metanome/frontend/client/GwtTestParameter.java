package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterInteger;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterBooleanWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterCsvFileWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterIntegerWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterStringWidget;
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
		
		InputParameterString inputParameterString = new InputParameterString("Filename");
		InputParameterBoolean inputParameterBoolean = new InputParameterBoolean("Omit warnings");
		InputParameterInteger inputParameterInteger = new InputParameterInteger("Number of runs");
		InputParameterCsvFile inputParameterCsvFile = new InputParameterCsvFile("inputData");
		
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
//	
//	@Test
//	public void testInputParameterSetInvalidValue() {
//		//Setup
//		InputParameter string = new InputParameterString("Filename");
//		InputParameter integer = new InputParameterInteger("");
//		InputParameter bool = new InputParameterBoolean();
//		InputParameter csv = new InputParameterCsvFile("inputFile");
//		Object nonStringValue = 4;
//		Object nonIntegerValue = "oh-no";
//		Object nonBoolValue = 1;
//		boolean exceptionString = false;
//		boolean exceptionInteger = false;
//		boolean exceptionBool = false;
//		boolean exceptionCsv = false;
//		
//		//Execute
//		try {			
//			string.setValue(nonStringValue);	
//		} catch (ClassCastException e) {
//			exceptionString = true;
//		}
//		
//		try {			
//			integer.setValue(nonIntegerValue);	
//		} catch (ClassCastException e) {
//			exceptionInteger = true;
//		}
//		
//		try {			
//			bool.setValue(nonBoolValue);	
//		} catch (ClassCastException e) {
//			exceptionBool = true;
//		}
//		
//		try {			
//			csv.setValue(nonStringValue);	
//		} catch (ClassCastException e) {
//			exceptionCsv = true;
//		}
//		
//		//Check
//		assertTrue(exceptionString);
//		assertTrue(exceptionInteger);
//		assertTrue(exceptionBool);
//		assertTrue(exceptionCsv);
//	}
//	
//	@Test
//	public void testInputParameterSetValidValue() {
//		//Setup
//		InputParameter string = new InputParameterString("Filename");
//		Object stringValue = "test";
//		InputParameter integer = new InputParameterInteger();
//		Object intValue = 4;
//		InputParameter bool = new InputParameterBoolean("bool");
//		Object boolValue = true;
//		InputParameter csv = new InputParameterCsvFile("inputFile");
//		Object csvValue = "test.csv";
//		
//		//Execute
//		string.setValue(stringValue);	
//		integer.setValue(intValue);
//		bool.setValue(boolValue);
//		csv.setValue(csvValue);
//		
//		//Check
//		assertEquals("test", string.getValue());
//		assertEquals(4, integer.getValue());
//		assertEquals(true, bool.getValue());
//		assertEquals("test.csv", csv.getValue());
//	}
	
//	@Test
//	public void testInputParameterCsvFile() {
//		//Setup
//		InputParameter csv = new InputParameterCsvFile("inputFile");
//		
//		//Execute
//		MetanomeListBox listbox = (MetanomeListBox) csv.getWidget();
//		assertEquals("--", listbox.getValue());
//	}
	
	//TODO separate test to check that available options are added
	
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}
}
