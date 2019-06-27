/**
 * Copyright 2014-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.metanome.backend.algorithm_loading;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.backend.constants.Constants;
import org.apache.commons.lang3.ClassUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Class that provides utilities to retrieve information on the available algorithm jars.
 */
public class AlgorithmFinder {

  /**
   * @param algorithmSubclass Class of algorithms to retrieve, or null if all subclasses
   * @return an array with the names of the available algorithms
   * @throws java.io.IOException if the algorithm folder could not be opened
   * @throws java.lang.ClassNotFoundException if an algorithm contains a not supported algorithm subclass
   */
  public String[] getAvailableAlgorithmFileNames(Class<?> algorithmSubclass)
    throws IOException, ClassNotFoundException {

    LinkedList<String> availableAlgorithms = new LinkedList<>();

    String pathToFolder = "";
    try {
        pathToFolder = Thread.currentThread().getContextClassLoader().getResource(Constants.ALGORITHMS_RESOURCE_NAME).getPath();
    } catch (NullPointerException e) {
      // The algorithm folder does not exist
      return new String[]{};
    }
    File[] jarFiles = retrieveJarFiles(pathToFolder);

    for (File jarFile : jarFiles) {
      if (algorithmSubclass == null ||
        getAlgorithmInterfaces(jarFile).contains(algorithmSubclass)) {
        availableAlgorithms.add(jarFile.getName());
      }
    }

    String[] stringArray = new String[availableAlgorithms.size()];
    return availableAlgorithms.toArray(stringArray);
  }

  /**
   * Returns a String with the path of Metanome's algorithms directory.
   *
   * @return String with path to algorithm directory
   */
  public String getAlgorithmDirectory() {
    String pathToFolder = "";
    try {
      pathToFolder = Thread.currentThread().getContextClassLoader().getResource(Constants.ALGORITHMS_RESOURCE_NAME).getPath();
    } catch (NullPointerException e) {
      // The algorithm folder does not exist
      throw new NullPointerException("Algorithm directory is missing!");
    }
    return pathToFolder;

  }
  /**
   * @param pathToFolder Path to search for jar files
   * @return an array of Files with ".jar" ending
   * @throws java.io.UnsupportedEncodingException if the file path could not be decoded in utf-8
   */
  private File[] retrieveJarFiles(String pathToFolder) throws UnsupportedEncodingException {
    File folder = new File(URLDecoder.decode(pathToFolder, Constants.FILE_ENCODING));
    File[] jars = folder.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File file, String name) {
        return name.endsWith(Constants.JAR_FILE_ENDING);
      }
    });

    if (jars == null) {
      jars = new File[0];
    }
    return jars;
  }

  /**
   * Finds out which subclass of Algorithm is implemented by the source code in the algorithmJarFile
   * by file name.
   *
   * @param algorithmJarFileName the algorithm's file name
   * @return the interfaces of the algorithm implementation in algorithmJarFile
   * @throws java.io.IOException if the algorithm jar file could not be opened
   * @throws java.lang.ClassNotFoundException if the algorithm contains a not supported interface
   */
  public Set<Class<?>> getAlgorithmInterfaces(String algorithmJarFileName)
    throws IOException, ClassNotFoundException {

    String jarFilePath = "";
    try {
      jarFilePath = Thread.currentThread().getContextClassLoader().getResource(Constants.ALGORITHMS_RESOURCE_NAME + Constants.FILE_SEPARATOR + algorithmJarFileName).getFile();
    } catch (NullPointerException e) {
      // The algorithm folder does not exist
      return new HashSet<>();
    }

    File file = new File(URLDecoder.decode(jarFilePath, Constants.FILE_ENCODING));

    return getAlgorithmInterfaces(file);
  }

  /**
   * Finds out which subclass of Algorithm is implemented by the source code in the
   * algorithmJarFile.
   *
   * @param algorithmJarFile the algorithm's jar file
   * @return the interfaces of the algorithm implementation in algorithmJarFile
   * @throws java.io.IOException if the algorithm jar file could not be opened
   * @throws java.lang.ClassNotFoundException if the algorithm contains a not supported interface
   */
  public Set<Class<?>> getAlgorithmInterfaces(File algorithmJarFile)
    throws IOException, ClassNotFoundException {
    JarFile jar = new JarFile(algorithmJarFile);

    Manifest man = jar.getManifest();
    Attributes attr = man.getMainAttributes();
    String className = attr.getValue(Constants.BOOTRSTAP_CLASS_TAG_NAME);

    URL[] url = {algorithmJarFile.toURI().toURL()};
    ClassLoader loader = new URLClassLoader(url, Algorithm.class.getClassLoader());

    Class<?> algorithmClass;
    try {
      algorithmClass = Class.forName(className, false, loader);
    } catch (ClassNotFoundException e) {
      System.out.println("Could not find class " + className);
      return new HashSet<>();
    } finally {
      jar.close();
    }

    return new HashSet<>(ClassUtils.getAllInterfaces(algorithmClass));
  }
}
