package de.uni_potsdam.hpi.metanome.frontend.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;

public interface ExecutionServiceAsync {

	public void executeAlgorithm(String algorithmName, List<InputParameter> parameters, AsyncCallback<Void> callback);
}
