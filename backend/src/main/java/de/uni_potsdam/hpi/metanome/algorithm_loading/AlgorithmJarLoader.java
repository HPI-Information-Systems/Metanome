package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class AlgorithmJarLoader<T extends Algorithm> {
	
	protected static final String bootstrapClassTagName = "Algorithm-Bootstrap-Class";
	protected Class<T> algorithmSubclass;
	
	public AlgorithmJarLoader(Class<T> algorithmSubclass) {
		this.algorithmSubclass = algorithmSubclass;
	}
	
	/**
	 * Loads a jar file containing an algorithm and returns an instance of the bootstrap class.
	 * 
	 * @param path
	 * @return runnable algorithm
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public T loadAlgorithm(String path) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		//TODO remove / replace by correct folder
		//String pathToFolder = ClassLoader.getSystemResource("testjar.jar").getPath();
		String pathToFolder = "/Users/Claudia/Uni/Job/MetanomeWorkspace/metanome/backend/src/test/resources";
		//pathToFolder = pathToFolder.substring(0, pathToFolder.lastIndexOf(File.separator));
		
		File file = new File(pathToFolder + "/" + path);
		JarFile jar = new JarFile(file);
		
		Manifest man = jar.getManifest();
        Attributes attr = man.getMainAttributes();
        String className = attr.getValue(bootstrapClassTagName);
        
        URL[] url = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(url, algorithmSubclass.getClassLoader());
        
        Class<? extends T> algorithmClass = 
        		Class.forName(className, true, loader).asSubclass(algorithmSubclass);
        
		return algorithmClass.getConstructor().newInstance();
	}
	
}
