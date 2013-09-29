package de.uni_potsdam.hpi.metanome.frontend.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FinderServiceAsync {

	public void listInclusionDependencyAlgorithms(AsyncCallback<String[]> callback);
	public void listFunctionalDependencyAlgorithms(AsyncCallback<String[]> callback);
	public void listUniqueColumnCombinationsAlgorithms(AsyncCallback<String[]> callback);
	public void listBasicStatisticsAlgorithms(AsyncCallback<String[]> addJarChooserCallback);

}
