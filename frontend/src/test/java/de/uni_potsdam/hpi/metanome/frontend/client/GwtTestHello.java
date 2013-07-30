package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;

import de.uni_potsdam.hpi.metanome.frontend.client.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.ParameterTable;
import de.uni_potsdam.hpi.metanome.frontend.server.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.server.InputParameter.Type;

public class GwtTestHello extends GWTTestCase{

	@Test
	public void testJarLoaderInput() {
		String[] filenames = {"filename1.jar", "filename2.jar"};
		JarChooser jarChooser = new JarChooser(filenames);
		
		assertEquals(3, jarChooser.getWidgetCount());
		assertEquals(2, jarChooser.getListItemCount());
	}
	
	@Test
	public void testLayout() {
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		paramList.add(new InputParameter("Filename", Type.STRING));
		paramList.add(new InputParameter("Omit warnings", Type.BOOL));
		paramList.add(new InputParameter("Number of runs", Type.INT));
		ParameterTable pt = new ParameterTable(paramList);
		
		assertEquals(3, pt.getRowCount());
		
		//check STRING row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(0, 1).getClass(), TextBox.class);
		
		//check BOOL row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(1, 1).getClass(), CheckBox.class);
				
		//check INT row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(pt.getWidget(2, 1).getClass(), IntegerBox.class);
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}

}
