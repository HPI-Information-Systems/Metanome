package de.uni_potsdam.hpi.metanome.algorithm_integration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;

/**
 * @author Jakob Zwiener
 * 
 * An {@link Algorithm} that writes to temporary files.
 */
public interface TempFileAlgorithm extends Algorithm {
	
	/**
	 * @param tempFileGenerator
	 */
	void setTempFileGenerator(FileGenerator tempFileGenerator);

}
