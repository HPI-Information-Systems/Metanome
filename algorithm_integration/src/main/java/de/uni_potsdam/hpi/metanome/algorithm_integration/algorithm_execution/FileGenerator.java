package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution;

import java.io.Closeable;
import java.io.File;

/**
 * Generates temporary files.
 */
public interface FileGenerator extends Closeable {

	/**
	 * Returns a temporary file that will be deleted on close.
	 * 
	 * @return temporary file
	 * 
	 * @throws FileCreationException 
	 */
	File getTemporaryFile() throws FileCreationException;
}
