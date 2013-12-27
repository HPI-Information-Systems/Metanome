package de.uni_potsdam.hpi.metanome.frontend.client;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage.Tabs;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.AlgorithmTab;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderServiceAsync;

/**
 * Tests related to the overall page.
 */
public class GwtTestBasePage extends GWTTestCase{
	
	/** this must contain an algorithm that is currently available */
	private String algorithmName = "example_ucc_algorithm-0.0.1-SNAPSHOT.jar";
	
	private BasePage testPage;
	
	/**
	 * Test BasePage constructor. 
	 */
	@Test
	public void testNewBasePage(){
		//Execute
		testPage = new BasePage();
		
		//Check
		assertEquals(5, testPage.getWidgetCount());
		
		// -- Results page
		assertTrue(testPage.getWidget(Tabs.RESULTS.ordinal()) instanceof TabLayoutPanel);
	}
	
	@Test
	public void testAddAlgorithmsToRunConfigurations() {
		BasePage page = new BasePage();
		int itemCount = page.runConfigurationsPage.getJarChooser().getListItemCount();
		
		//Execute
		page.addAlgorithmsToRunConfigurations("Algorithm 1", "Algorithm 2");
		
		//Check
		assertEquals(itemCount + 2, page.runConfigurationsPage.getJarChooser().getListItemCount());
	}
	
	/**
	 * Test control flow from Algorithms to Run configuration
	 * @throws InterruptedException 
	 */
	@Test
	public void testJumpToRunConfiguration() {
		final BasePage page = new BasePage();
		
		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				caught.printStackTrace();
			}
			
			public void onSuccess(String[] result) { 
				page.addAlgorithmsToRunConfigurations(result);

				//Execute
				page.jumpToRunConfiguration(algorithmName);

				//Check
				assertEquals(Tabs.RUN_CONFIGURATION.ordinal(), page.getSelectedIndex());
				assertEquals(algorithmName, ((AlgorithmTab) page.getWidget(page.getSelectedIndex()))
						.getCurrentlySelectedAlgorithm());				
				
//				Add testing to ensure the parameter table is shown
//				assertEquals(2, (((AlgorithmTab) page.getWidget(page.getSelectedIndex()))).getWidgetCount()); 
//				assertTrue((((AlgorithmTab) page.getWidget(page.getSelectedIndex()))).getWidget(1) instanceof ParameterTable); 
				
				finishTest();
			}
		};
		
		((FinderServiceAsync) GWT.create(FinderService.class)).listAllAlgorithms(callback);
		
		delayTestFinish(5000);
	
		
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";	
	}

}
