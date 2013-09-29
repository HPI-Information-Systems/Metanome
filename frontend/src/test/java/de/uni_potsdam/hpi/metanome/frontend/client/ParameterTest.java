package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterInteger;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.ParameterTable;

public class ParameterTest extends GWTTestCase {
	
	@Test
	public void testParameterTable() {
		//Setup
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		paramList.add(new InputParameterString("Filename"));
		paramList.add(new InputParameterBoolean("Omit warnings"));
		paramList.add(new InputParameterInteger("Number of runs"));
		
		//Execute - create
		ParameterTable pt = new ParameterTable(paramList);
		
		//Check
		assertEquals(4, pt.getRowCount());
		
		// - STRING row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(0, 1).getClass(), TextBox.class);
		
		// - BOOL row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(1, 1).getClass(), CheckBox.class);
				
		// - INT row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(2, 1).getClass(), IntegerBox.class);
		
		//Execute - submit
		pt.submit();
	}
	
	@Test
	public void testInputParameterSetInvalidValue() {
		//Setup
		InputParameter string = new InputParameterString("Filename");
		InputParameter integer = new InputParameterString("");
		InputParameter bool = new InputParameterBoolean();
		Object nonStringValue = 4;
		Object nonIntegerValue = "oh-no";
		Object nonBoolValue = 1;
		boolean exceptionString = false;
		boolean exceptionInteger = false;
		boolean exceptionBool = false;
		
		//Execute
		try {			
			string.setValue(nonStringValue);	
		} catch (ClassCastException e) {
			exceptionString = true;
		}
		
		try {			
			integer.setValue(nonIntegerValue);	
		} catch (ClassCastException e) {
			exceptionString = true;
		}
		
		try {			
			bool.setValue(nonBoolValue);	
		} catch (ClassCastException e) {
			exceptionString = true;
		}
		
		//Check
		assertTrue(exceptionString);
		assertTrue(exceptionInteger);
		assertTrue(exceptionBool);
	}
	
	@Test
	public void testInputParameterSetValidValue() {
		//Setup
		InputParameter string = new InputParameterString("Filename");
		Object stringValue = "test";
		InputParameter integer = new InputParameterInteger();
		Object intValue = 4;
		InputParameter bool = new InputParameterBoolean("bool");
		Object boolValue = true;
		
		//Execute
		string.setValue(stringValue);	
		integer.setValue(intValue);
		bool.setValue(boolValue);
		
		//Check
		assertEquals("test", string.getValue());
		assertEquals(4, integer.getValue());
		assertEquals(true, bool.getValue());
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}
}
