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
		String pathToFolder = ClassLoader.getSystemResource("algorithms").getPath();
		
		File file = new File(URLDecoder.decode(pathToFolder + "/" + path, "utf-8"));
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
