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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class AlgorithmJarLoader {

  protected static final String bootstrapClassTagName = "Algorithm-Bootstrap-Class";
  protected Algorithm algorithmSubclass;

  /**
   * Loads a jar file containing an algorithm and returns an instance of the bootstrap class.
   *
   * @param filePath the file path to the algorithm jar
   * @return runnable algorithm
   * @throws IOException if the algorithm could not be loaded
   * @throws ClassNotFoundException if the algorithm could not be loaded
   * @throws InstantiationException if the algorithm could not be loaded
   * @throws IllegalAccessException if the algorithm could not be loaded
   * @throws IllegalArgumentException if the algorithm could not be loaded
   * @throws InvocationTargetException if the algorithm could not be loaded
   * @throws NoSuchMethodException if the algorithm could not be loaded
   * @throws SecurityException if the algorithm could not be loaded
   */
  public Algorithm loadAlgorithm(String filePath)
    throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
    SecurityException {
    String
      pathToFolder =
      Thread.currentThread().getContextClassLoader().getResource("algorithms" + System.getProperty("file.separator") + filePath)
        .getPath();

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
