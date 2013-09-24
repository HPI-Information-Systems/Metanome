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
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.InclusionDependencyJarChooser;

public class CommonWidgetsTest extends GWTTestCase{

	@Test
	public void testJarChooser() {
		//Setup
		String[] filenames = {"filename1.jar", "filename2.jar"};
		
		//Execute
		JarChooser jarChooser = new InclusionDependencyJarChooser(filenames);
		
		//Test
		assertEquals(2, jarChooser.getWidgetCount());
		assertEquals(2, jarChooser.getListItemCount());
	}
	
	
	@Test
	public void testParameterTable() {
		//Setup
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		paramList.add(new InputParameterString("Filename"));
		paramList.add(new InputParameterBoolean("Omit warnings"));
		paramList.add(new InputParameterInteger("Number of runs"));
		
		//Execute
		ParameterTable pt = new ParameterTable(paramList);
		
		//Check
		assertEquals(3, pt.getRowCount());
		
		//STRING row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(0, 1).getClass(), TextBox.class);
		
		//BOOL row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(1, 1).getClass(), CheckBox.class);
				
		//INT row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(2, 1).getClass(), IntegerBox.class);
	}
	
	@Test
	public void testInputParameterSetInvalidValue() {
		//Setup
		InputParameterString string = new InputParameterString("Filename");
		Object nonStringValue = 4;
		boolean exception = false;
		
		//Execute
		try {			
			string.setValue(nonStringValue);	
		} catch (ClassCastException e) {
			exception = true;
		}
		
		//Check
		assertTrue(exception);
	}
	
	@Test
	public void testInputParameterSetValidValue() {
		//Setup
		InputParameterString string = new InputParameterString("Filename");
		Object stringValue = "test";
		
		//Execute
		string.setValue(stringValue);	
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}

}
