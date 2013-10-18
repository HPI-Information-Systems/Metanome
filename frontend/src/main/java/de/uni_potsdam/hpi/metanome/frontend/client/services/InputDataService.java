package de.uni_potsdam.hpi.metanome.frontend.client.services;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("inputDataService")
public interface InputDataService extends RemoteService {

	public String[] listCsvInputFiles();

}
