package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 
 */
public class InputDataFinder {

	public File[] getAvailableCsvs() throws UnsupportedEncodingException {
		String pathToFolder = Thread.currentThread().getContextClassLoader().getResource("inputData").getPath();
		File[] csvFiles = retrieveCsvFiles(pathToFolder);
		
		return csvFiles;
	}

	/**
	 * retrieves all CSV Files located directly in the given directory
	 * TODO consider sharing code with algorithm finding methods
	 * 
	 * @param pathToFolder	path to the folder to be searched in 
	 * @return names of all CSV files located directly in the given directory (no subfolders)
	 * 
	 * @throws UnsupportedEncodingException 
	 */
	protected File[] retrieveCsvFiles(String pathToFolder) throws UnsupportedEncodingException {
		File folder = new File(URLDecoder.decode(pathToFolder, "utf-8"));
		File[] csvs = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File file, String name) {
		        return name.endsWith(".csv");
		    }
		});

		return csvs;
	}

}
