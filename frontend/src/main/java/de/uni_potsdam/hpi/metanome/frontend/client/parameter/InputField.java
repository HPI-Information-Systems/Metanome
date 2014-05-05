package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public abstract class InputField extends HorizontalPanel {

	protected Button removeButton;
	protected boolean isOptional;

	public InputField(boolean optional) {
		super();
		this.isOptional = optional;
		
		if (this.isOptional) {			
			createRemoveButton();
		}
	}

	/**
	 * Creates the remove button.
	 */
	protected void createRemoveButton() {
		this.removeButton = new Button("Remove");
		this.removeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				removeSelf();
			}
		});
		this.add(this.removeButton);
	}

	public void removeSelf() {
		((InputParameterWidget) this.getParent()).removeField(this);
	}

}