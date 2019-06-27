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
      Thread.currentThread().getContextClassLoader().getResource(Constants.ALGORITHMS_RESOURCE_NAME + Constants.FILE_SEPARATOR + filePath)
        .getPath();

    File file = new File(URLDecoder.decode(pathToFolder, Constants.FILE_ENCODING));
    JarFile jar = new JarFile(file);

    Manifest man = jar.getManifest();
    Attributes attr = man.getMainAttributes();
    String className = attr.getValue(Constants.BOOTRSTAP_CLASS_TAG_NAME);

    URL[] url = {file.toURI().toURL()};
    ClassLoader loader = new URLClassLoader(url, Algorithm.class.getClassLoader());

    Class<? extends Algorithm> algorithmClass =
      Class.forName(className, true, loader).asSubclass(Algorithm.class);

    jar.close();

    return algorithmClass.getConstructor().newInstance();
  }

}
