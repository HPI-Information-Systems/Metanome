package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBooleanWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFileWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterInteger;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterIntegerWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIterator;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIteratorWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterStringWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.ParameterTable;

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
	public void testRetrieveSimpleParameterValues(){	
		//Setup
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		
		InputParameterString inputParameterString = new InputParameterString("string");
		InputParameterBoolean inputParameterBoolean = new InputParameterBoolean("bool");
		InputParameterInteger inputParameterInteger = new InputParameterInteger("int");
		InputParameterCsvFile inputParameterCsvFile = new InputParameterCsvFile("csv");
		InputParameterSQLIterator inputParameterSQLIterator = new InputParameterSQLIterator("sql");
		
		paramList.add(inputParameterString);
		paramList.add(inputParameterBoolean);
		paramList.add(inputParameterInteger);
		paramList.add(inputParameterCsvFile);
		paramList.add(inputParameterSQLIterator);
		
		ParameterTable pt = new ParameterTable(paramList);

		//Execute
		List<InputParameter> retrievedParams = pt.getInputParametersFromChildren();
		
		//Check
		assertTrue(retrievedParams.contains(inputParameterString));
		assertTrue(retrievedParams.contains(inputParameterBoolean));
		assertTrue(retrievedParams.contains(inputParameterInteger));
		assertTrue(retrievedParams.contains(inputParameterCsvFile));
		assertTrue(retrievedParams.contains(inputParameterSQLIterator));
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
		String identifierSql = "sqlParam";
		InputParameter sqlParam = new InputParameterSQLIterator(identifierSql);
		
		//Execute
		InputParameterWidget stringWidget = stringParam.createWrappingWidget();
		InputParameterWidget boolWidget = boolParam.createWrappingWidget();
		InputParameterWidget intWidget = intParam.createWrappingWidget();
		InputParameterWidget csvWidget = csvParam.createWrappingWidget();
		InputParameterWidget sqlWidget = sqlParam.createWrappingWidget();
		
		//Check
		assertTrue(stringWidget instanceof InputParameterStringWidget);
		assertEquals(identifierString, stringWidget.getInputParameter().getIdentifier());
		
		assertTrue(boolWidget instanceof InputParameterBooleanWidget);
		assertEquals(identifierBoolean, boolWidget.getInputParameter().getIdentifier());
		
		assertTrue(intWidget instanceof InputParameterIntegerWidget);
		assertEquals(identifierInteger, intWidget.getInputParameter().getIdentifier());
		
		assertTrue(csvWidget instanceof InputParameterCsvFileWidget);
		assertEquals(identifierCsv, csvWidget.getInputParameter().getIdentifier());
		
		assertTrue(sqlWidget instanceof InputParameterSQLIteratorWidget);
		assertEquals(identifierSql, sqlWidget.getInputParameter().getIdentifier());
	}
	
	@Test
	public void testCsvFileWidget(){
		//Setup
		InputParameterCsvFile inputParameter = new InputParameterCsvFile("identifierCsv");
		inputParameter.setAdvanced(true);
		InputParameterCsvFileWidget csvWidget = new InputParameterCsvFileWidget(inputParameter);
		FlexTable advancedPanel = (FlexTable) csvWidget.getWidget(1);
		String characterString = "X";
		int line = 5;
		boolean boolTrue = true;
		boolean exceptionCaught = false;

		//Execute
		((TextBox) advancedPanel.getWidget(0,1)).setValue(characterString);
		((TextBox) advancedPanel.getWidget(1,1)).setValue(characterString);
		((IntegerBox) advancedPanel.getWidget(3,1)).setValue(line);
		((CheckBox) advancedPanel.getWidget(4,1)).setValue(boolTrue);
		((CheckBox) advancedPanel.getWidget(5,1)).setValue(boolTrue);
		try{			
			inputParameter = csvWidget.getInputParameter();
		} catch (Exception e){
			//TODO make sure some nice exception is thrown when not all values are set.
			exceptionCaught = true;
		}
		((TextBox) advancedPanel.getWidget(2,1)).setValue(characterString);
		inputParameter = csvWidget.getInputParameter();
		
		//Check
		assertTrue(exceptionCaught);

		assertEquals(characterString.charAt(0), inputParameter.getSeparatorChar());
		assertEquals(characterString.charAt(0), inputParameter.getQuoteChar());
		assertEquals(characterString.charAt(0), inputParameter.getEscapeChar());
		assertEquals(line, inputParameter.getLine());
		assertEquals(boolTrue, inputParameter.isStrictQuotes());
		assertEquals(boolTrue, inputParameter.isIgnoreLeadingWhiteSpace());		
	}

		
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}
}
