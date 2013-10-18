package de.uni_potsdam.hpi.metanome.frontend.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder;

public class InputDataServiceImpl extends RemoteServiceServlet {

	private static final long serialVersionUID = -4303653997579507943L;
	
	private InputDataFinder inputDataFinder = new InputDataFinder();

	/**
	 * 
	 * @param algorithmClass	the subclass of algorithms to be listed, or null for all algorithms
	 * @return	a list of filenames (without path)
	 */
	public String[] listCsvInputFiles () {
		String[] csvInputFiles = null;
		try {
			csvInputFiles = inputDataFinder.getAvailableCsvs();
		} catch (Exception e){
			//TODO: error handling
			System.out.println("FAILED to FIND input CSV files");
			e.printStackTrace();
		}
		return csvInputFiles;
	}

}
