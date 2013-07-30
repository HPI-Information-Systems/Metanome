package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

import de.uni_potsdam.hpi.metanome.frontend.server.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.server.InputParameter.Type;

/**
 * HelloWorld application.
 */
public class Hello implements EntryPoint {

  public void onModuleLoad() {
	ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
	paramList.add(new InputParameter("filename", Type.STRING));
	paramList.add(new InputParameter("Omit warnings", Type.BOOL));
	paramList.add(new InputParameter("Number of Runs", Type.INT));
	ParameterTable pt = new ParameterTable(paramList);
    
	Button b = new Button("Click me", new ClickHandler() {
      public void onClick(ClickEvent event) {
        Window.alert("Hello, AJAX");
      }
    });

    RootPanel.get().add(pt);
  }

}
