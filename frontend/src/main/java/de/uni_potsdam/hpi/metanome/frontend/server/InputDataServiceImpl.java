package de.uni_potsdam.hpi.metanome.frontend.server;

import java.io.File;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;

public class InputDataServiceImpl extends RemoteServiceServlet implements InputDataService {

	private static final long serialVersionUID = -4303653997579507943L;
	
	private InputDataFinder inputDataFinder = new InputDataFinder();

	/**
	 * 
	 * @param algorithmClass	the subclass of algorithms to be listed, or null for all algorithms
	 * @return	a list of filenames (without path)
	 */
	public String[] listCsvInputFiles () {
		File[] csvInputFiles = null;
		try {
			csvInputFiles = inputDataFinder.getAvailableCsvs();
		} catch (Exception e){
			//TODO: error handling
			System.out.println("FAILED to FIND input CSV files");
			e.printStackTrace();
		}
		
		String[] csvInputFilePaths = new String[csvInputFiles.length];
		for (int i=0; i<csvInputFiles.length; i++){
			csvInputFilePaths[i] = csvInputFiles[i].getPath();
		}
		
		return csvInputFilePaths;
	}

}
