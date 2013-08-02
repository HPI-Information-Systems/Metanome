package de.uni_potsdam.hpi.metanome.frontend.client;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class BasePageTest extends GWTTestCase{
	
	@Test
	public void testNewBasePage(){
		//Execute
		BasePage page = new BasePage();
		
		//Check
		//Page should have a tab each for the 3 algorithm types
		assertEquals(3, page.getWidgetCount()); 
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";	
	}

}
