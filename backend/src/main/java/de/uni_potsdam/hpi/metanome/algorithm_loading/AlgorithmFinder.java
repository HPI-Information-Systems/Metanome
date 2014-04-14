/*
 * Copyright 2014 by the Metanome project
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

package de.uni_potsdam.hpi.metanome.algorithm_loading;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Class that provides utilities to retrieve information on the available algorithm jars.
 */
public class AlgorithmFinder {

    protected static final String bootstrapClassTagName = "Algorithm-Bootstrap-Class";

    /**
     * @param algorithmSubclass Class of algorithms to retrieve, or null if all subclasses
     * @return an array with the names of the available algorithms
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public String[] getAvailableAlgorithms(Class<?> algorithmSubclass) throws IOException, ClassNotFoundException {

        LinkedList<String> availableAlgorithms = new LinkedList<String>();
        String pathToFolder = Thread.currentThread().getContextClassLoader().getResource("algorithms").getPath();
        File[] jarFiles = retrieveJarFiles(pathToFolder);

        for (File jarFile : jarFiles) {
            if (algorithmSubclass == null ||
                    getAlgorithmInterfaces(jarFile).contains(algorithmSubclass))
                availableAlgorithms.add(jarFile.getName());
        }

        String[] stringArray = new String[availableAlgorithms.size()];
        return availableAlgorithms.toArray(stringArray);
    }

    /**
     * @param pathToFolder Path to search for jar files
     * @return an array of Files with ".jar" ending
     * @throws UnsupportedEncodingException
     */
    private File[] retrieveJarFiles(String pathToFolder) throws UnsupportedEncodingException {
        File folder = new File(URLDecoder.decode(pathToFolder, "utf-8"));
        File[] jars = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".jar");
            }
        });

        if (jars == null)
            jars = new File[0];
        return jars;
    }

    /**
     * Finds out which subclass of Algorithm is implemented by the source code in the algorithmJarFile.
     *
     * @param algorithmJarFile the algorithm's jar file
     * @return the superclass of the algorithm implementation in algorithmJarFile, which should be a
     * subclass of Algorithm
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Class<?>> getAlgorithmInterfaces(File algorithmJarFile) throws IOException, ClassNotFoundException {
        JarFile jar = new JarFile(algorithmJarFile);

        Manifest man = jar.getManifest();
        Attributes attr = man.getMainAttributes();
        String className = attr.getValue(bootstrapClassTagName);

        URL[] url = {algorithmJarFile.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(url, Algorithm.class.getClassLoader());

        Class<?> algorithmClass;
        try {
            algorithmClass = Class.forName(className, false, loader);
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find class " + className);
            return new LinkedList<Class<?>>();
        } finally {
            jar.close();
        }

        return Arrays.asList(algorithmClass.getInterfaces());
    }
}
