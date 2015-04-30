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
import de.metanome.backend.resources.InputResource;
import de.metanome.backend.resources.WebException;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;

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

  AlgorithmResource algorithmResource = new AlgorithmResource();
  InputResource inputResource = new InputResource();
  Server server = new Server();


  /**
   * Initializes the database.
   *
   * @param servletContextEvent the servlet context
   */
  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      System.out.println("Starting Database");
      HsqlProperties p = new HsqlProperties();
      p.setProperty("server.database.0",
                    "file:" + new File(".").getAbsolutePath() + "/db/metanomedb");
      p.setProperty("server.dbname.0", "metanomedb");
      p.setProperty("server.port", "9001");
      server.setProperties(p);
      server.setLogWriter(null); // can use custom writer
      server.setErrWriter(null); // can use custom writer
      server.start();
    } catch (ServerAcl.AclFormatException afex) {
      afex.printStackTrace();
    } catch (IOException ioex) {
      ioex.printStackTrace();
    }
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
    if (!algorithmResource.getAll().isEmpty()) {
      return;
    }

    AlgorithmFinder jarFinder = new AlgorithmFinder();

    // FIXME: do I really want these exceptions here?
    String[] algorithmFileNames;
    algorithmFileNames = jarFinder.getAvailableAlgorithmFileNames(null);

    for (String filePath : algorithmFileNames) {
      try {
        Set<Class<?>> algorithmInterfaces = jarFinder.getAlgorithmInterfaces(filePath);
        algorithmResource.store(new Algorithm(filePath, algorithmInterfaces)
                                    .setName(filePath.replaceAll(".jar", "")));
      } catch (WebException e) {
        // Do something with this exception
      }
    }
  }

  /**
   * Prefills the inputs table in the database with the existing input files.
   *
   * @throws UnsupportedEncodingException when input files cannot be found
   * @throws EntityStorageException       when the existing inputs cannot be queried
   */
  protected void addFileInputs() throws UnsupportedEncodingException, EntityStorageException {
    // only prefill input table if currently empty
    if (!inputResource.getAll().isEmpty()) {
      return;
    }

    InputDataFinder inputDataFinder = new InputDataFinder();

    File[] inputs = inputDataFinder.getAvailableFiles();

    for (File input : inputs) {
      FileInput fileInput = new FileInput(input.getPath());
      inputResource.store(fileInput);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    server.shutdown();
  }
}
