package de.uni_potsdam.hpi.metanome.frontend.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface InputDataServiceAsync {

	public void listCsvInputFiles(AsyncCallback<String[]> callback);

}
