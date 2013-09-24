package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterInteger;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;

public class ParameterTable extends FlexTable {

	private List<InputParameter> parameters;
	private Button executeButton;

	public ParameterTable(List<InputParameter> requiredParams) {
		super();
		this.parameters = requiredParams;		
		
		int i = 0;
		for (InputParameter param : this.parameters) {
			this.setText(i, 0, param.getIdentifier());
			this.setWidget(i, 1, getInputWidget(param));
			i++;
		}
		
		this.executeButton = new Button("Run");
		this.executeButton.addClickHandler(new ParameterTableSubmitHandler());
		
		this.setWidget(i, 0, executeButton);
	}

	/**
	 * Translates <link>InputParameter.Type</link>s into appropriate input fields
	 * @param type	The type of the parameter
	 * @return		The input field corresponding to the parameter type
	 */
	private Widget getInputWidget(InputParameter parameter) {
		if (parameter instanceof InputParameterString)
			return new TextBox();
		else if (parameter instanceof InputParameterBoolean)
			return new CheckBox();
		else if (parameter instanceof InputParameterInteger) 
			return new IntegerBox();
		else
			return null;
	}
	
	/**
	 * specifies the action undertaken when parameter values are submitted
	 */
	public void submit(){
		setParameterValues();
 
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		      public void onFailure(Throwable caught) {
		    	  // TODO: Do something with errors.
		      }

		      public void onSuccess(Void v) {  	  
		    	  //results are displayed by the ResultReceiver through double dispatch
		      }
		    };

		// Make the call to the parameter service.
		callExecutionService(callback);
	}

	/**
	 * TODO docs
	 */
	private void setParameterValues() {
		int i = 0;
		for (InputParameter param : this.parameters) {
			Widget widget = this.getWidget(i, 1);
			Object value = null;
			if (widget instanceof TextBox){
				value = ((TextBox) widget).getValue();
			} else if (widget instanceof CheckBox){
				value = ((CheckBox) widget).getValue();
			} 
			//TODO check validity before setting value
			param.setValue(value);
			i++;
		}
	}

	protected void callExecutionService(AsyncCallback<Void> callback){
		for (InputParameter param : this.parameters){
			System.out.println(param.getIdentifier() + ":" + param.getValue());
		}
	}
	// TODO call service method to set parameter values and run algorithm (in subclasses)

}
