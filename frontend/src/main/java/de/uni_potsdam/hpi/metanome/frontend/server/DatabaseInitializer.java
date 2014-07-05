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

package de.uni_potsdam.hpi.metanome.frontend.server;

import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmFinder;
import de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.results_db.Input;

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
   * TODO docs
   *
   * @param servletContextEvent the servlet context
   */
  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      addAlgorithms();
    } catch (IOException | ClassNotFoundException e) {
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
   */
  protected void addAlgorithms() throws IOException, ClassNotFoundException {
    // only prefill algorithms table if it is currently empty
    if (!Algorithm.retrieveAll().isEmpty()) {
      return;
    }

    AlgorithmFinder jarFinder = new AlgorithmFinder();

    // FIXME: do I really want these exceptions here?
    String[] algorithmFileNames;
    algorithmFileNames = jarFinder.getAvailableAlgorithmFileNames(null);

    for (String filePath : algorithmFileNames) {
      try {
        Set<Class<?>> algorithmInterfaces = jarFinder.getAlgorithmInterfaces(filePath);
        Algorithm algorithm = new Algorithm(filePath, algorithmInterfaces)
            .setName(filePath.replaceAll(".jar", ""))
            .store();
      } catch (EntityStorageException | IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Prefills the inputs table in the database with the existing input files.
   */
  protected void addFileInputs() throws UnsupportedEncodingException, EntityStorageException {
    // only prefill input table if currently empty
    if (!Input.retrieveAll().isEmpty()) {
      return;
    }

    InputDataFinder inputDataFinder = new InputDataFinder();

    File[] inputs = inputDataFinder.getAvailableCsvs();

    for (File input : inputs) {
      FileInput fileInput = new FileInput(input.getName());
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
