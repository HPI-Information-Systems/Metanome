package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

/**
 * Class that provides utilities to retrieve information on the available algorithm jars. 
 */
public class AlgorithmFinder {
	
	protected static final String bootstrapClassTagName = "Algorithm-Bootstrap-Class";

	public List<AlgorithmDescriptor> getAvailableAlgorithms(String pathToFolder) throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		ArrayList<AlgorithmDescriptor> availableAlgorithms = new ArrayList<AlgorithmDescriptor>();
		File[] jars = retrieveJarFiles(pathToFolder);
		
		for(File jar : jars){
			AlgorithmDescriptor descriptor = new AlgorithmDescriptor(jar.getName(), getAlgorithmClass(jar));
			availableAlgorithms.add(descriptor);
		}
		
		return availableAlgorithms;
	}

	private File[] retrieveJarFiles(String pathToFolder) {
		File folder = new File(pathToFolder);
		File[] jars = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File file, String name) {
		        return name.endsWith(".jar");
		    }
		});
		
		if (jars == null) jars = new File[0];
		return jars;
	}
	
	public Type getAlgorithmClass(File file) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		JarFile jar = new JarFile(file);
		
		Manifest man = jar.getManifest();
        Attributes attr = man.getMainAttributes();
        String className = attr.getValue(bootstrapClassTagName);
        
        URL[] url = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(url);
        
        Class<?> algorithmClass = Class.forName(className, false, loader);
        
		return algorithmClass.getGenericSuperclass();
	}
	
	public Algorithm loadAlgorithmInstance(String path) throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//TODO: how to instantiate?
		return null;
	}
	
}
