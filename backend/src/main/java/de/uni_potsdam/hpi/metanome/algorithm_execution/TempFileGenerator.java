package de.uni_potsdam.hpi.metanome.algorithm_execution;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileCreationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;

/**
 * @author Jakob Zwiener
 *
 * TODO docs
 */
public class TempFileGenerator implements FileGenerator {

	public static final String TEMP_FILE_PATH = "temp_files";
	public static final int FILE_NAME_LENGTH = 30;
	
	protected String pathToFolder;
	protected List<File> createdFiles;
	
	public TempFileGenerator() throws UnsupportedEncodingException {
		// Get path to resource dir.
		String pathToFolder = ClassLoader.getSystemResource("").getPath();
		pathToFolder += "/" + TEMP_FILE_PATH;
		this.pathToFolder = URLDecoder.decode(pathToFolder, "utf-8");
		// Create subdir.
		new File(this.pathToFolder).mkdirs();
		
		this.createdFiles = new LinkedList<File>();
	}
	
	@Override
	public File getTemporaryFile() throws FileCreationException {
		String fileName = RandomStringUtils.randomAlphanumeric(FILE_NAME_LENGTH).toLowerCase();
		File tempFile = new File(pathToFolder + "/" + fileName);		
		
		try {
			tempFile.createNewFile();
		} catch (IOException e) {
			throw new FileCreationException("Could not create temporary file.");
		}
		
		// Mark the file for deletion on vm exit. 
		tempFile.deleteOnExit();
		// Remember the file to be deleted on close.
		createdFiles.add(tempFile);
		
		return tempFile;
	}

	@Override
	public void close() throws IOException {
		for (File tempFile : createdFiles) {
			tempFile.delete();
		}	
	}
}
