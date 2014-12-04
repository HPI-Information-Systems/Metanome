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

package de.metanome.frontend.server;

import de.metanome.backend.algorithm_loading.AlgorithmFinder;
import de.metanome.backend.algorithm_loading.InputDataFinder;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.WebException;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.Input;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Is called upon servlet initialization and initializes metanome's results database.
 *
 * @author Jakob Zwiener
 */
public class DatabaseInitializer implements ServletContextListener {

  /**
   * Initializes the database.
   *
   * @param servletContextEvent the servlet context
   */
  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      addAlgorithms();
    } catch (IOException | ClassNotFoundException | EntityStorageException e) {
      e.printStackTrace();
    }
    try {
      addFileInputs();
    } catch (UnsupportedEncodingException | EntityStorageException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prefills the algorithms table in the database with the existing algorithm jars.
   *
   * @throws IOException            when algorithm jars cannot be retrieved
   * @throws ClassNotFoundException when algorithm bootstrap class cannot be found
   */
  protected void addAlgorithms() throws IOException, ClassNotFoundException,
                                        EntityStorageException {
    // only prefill algorithms table if it is currently empty
    if (!AlgorithmResource.listAllAlgorithms().isEmpty()) {
      return;
    }

    AlgorithmFinder jarFinder = new AlgorithmFinder();

    // FIXME: do I really want these exceptions here?
    // FIXME: do not call Hibernate Util
    String[] algorithmFileNames;
    algorithmFileNames = jarFinder.getAvailableAlgorithmFileNames(null);

    try {
      for (String filePath : algorithmFileNames) {
        Set<Class<?>> algorithmInterfaces = jarFinder.getAlgorithmInterfaces(filePath);
        AlgorithmResource.addAlgorithm(new Algorithm(filePath, algorithmInterfaces)
                                           .setName(filePath.replaceAll(".jar", "")));
      }
    } catch (WebException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prefills the inputs table in the database with the existing input files.
   *
   * @throws UnsupportedEncodingException when csv files cannot be found
   * @throws EntityStorageException       when the existing inputs cannot be queried
   */
  protected void addFileInputs() throws UnsupportedEncodingException, EntityStorageException {
    // only prefill input table if currently empty
    if (!Input.retrieveAll().isEmpty()) {
      return;
    }

    InputDataFinder inputDataFinder = new InputDataFinder();

    File[] inputs = inputDataFinder.getAvailableCsvs();

    for (File input : inputs) {
      FileInput fileInput = new FileInput(input.getPath());
      try {
        fileInput.store();
      } catch (EntityStorageException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
  }
}
