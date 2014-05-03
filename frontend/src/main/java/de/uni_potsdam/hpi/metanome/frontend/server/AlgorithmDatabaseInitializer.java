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
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

/**
 * Is called upon servlet initialization and initializes metanome's results database.
 *
 * @author Jakob Zwiener
 */
public class AlgorithmDatabaseInitializer implements ServletContextListener {

    /**
     * Prefills the algorithms table in the database with the existing algorithm jars.
     *
     * @param servletContextEvent the servlet context
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // only prefill algorithms table if it is currently empty
        if (!Algorithm.retrieveAll().isEmpty()) {
            return;
        }

        AlgorithmFinder jarFinder = new AlgorithmFinder();

        // FIXME: do I really want these exceptions here?
        String[] algorithmFileNames = new String[0];
        try {
            algorithmFileNames = jarFinder.getAvailableAlgorithms(null);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (String filePath : algorithmFileNames) {
            try {
                Algorithm.store(new Algorithm(filePath));
            } catch (EntityStorageException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
