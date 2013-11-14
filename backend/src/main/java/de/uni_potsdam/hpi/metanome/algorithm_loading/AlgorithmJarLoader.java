package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

public class AlgorithmJarLoader {
	
	protected static final String bootstrapClassTagName = "Algorithm-Bootstrap-Class";
	protected Algorithm algorithmSubclass;
	
	/**
	 * Loads a jar file containing an algorithm and returns an instance of the bootstrap class.
	 * 
	 * @param path
	 * @return runnable algorithm 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public Algorithm loadAlgorithm(String path) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String pathToFolder = Thread.currentThread().getContextClassLoader().getResource("algorithms" + "/" + path).getPath();
		
		File file = new File(URLDecoder.decode(pathToFolder, "utf-8"));
		JarFile jar = new JarFile(file);
		
		Manifest man = jar.getManifest();
        Attributes attr = man.getMainAttributes();
        String className = attr.getValue(bootstrapClassTagName);
        
        URL[] url = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(url, Algorithm.class.getClassLoader());
        
        Class<? extends Algorithm> algorithmClass = 
        		Class.forName(className, true, loader).asSubclass(Algorithm.class);
        
        jar.close();
        
		return algorithmClass.getConstructor().newInstance();
	}
	
}
