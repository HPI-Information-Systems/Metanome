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

package de.metanome.frontend;

import de.metanome.backend.algorithm_loading.AlgorithmFinder;
import de.metanome.backend.algorithm_loading.InputDataFinder;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

/**
 * Is called upon servlet initialization and initializes metanome's results database.
 *
 * @author Jakob Zwiener
 */
public class DatabaseInitializer implements ServletContextListener {

    Server server = new Server();

    /**
     * Initializes the database.
     *
     * @param servletContextEvent the servlet context
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            HsqlProperties p = new HsqlProperties();
            p.setProperty("server.database.0",
                    "file:" + new File(".").getAbsolutePath() + "/db/metanomedb");
            p.setProperty("server.dbname.0", "metanomedb");
            p.setProperty("server.port", "9001");
            server.setProperties(p);
            server.setLogWriter(null); // can use custom writer
            server.setErrWriter(null); // can use custom writer
            server.start();
        } catch (ServerAcl.AclFormatException | IOException afex) {
            afex.printStackTrace();
        }
        try {
            addAlgorithms();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            addFileInputs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prefills the algorithms table in the database with the existing algorithm jars.
     *
     * @throws IOException            when algorithm jars cannot be retrieved
     * @throws ClassNotFoundException when algorithm bootstrap class cannot be found
     */
    @SuppressWarnings("unchecked")
    protected void addAlgorithms() throws IOException, ClassNotFoundException,
            EntityStorageException {
        List<Algorithm> algorithmList = (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class);
        if (!algorithmList.isEmpty()) {
            return;
        }

        AlgorithmFinder jarFinder = new AlgorithmFinder();

        String[] algorithmFileNames;
        algorithmFileNames = jarFinder.getAvailableAlgorithmFileNames(null);

        for (String filePath : algorithmFileNames) {
            try {
            Set<Class<?>> algorithmInterfaces = jarFinder.getAlgorithmInterfaces(filePath);
            HibernateUtil.store(new Algorithm(filePath, algorithmInterfaces)
                    .setName(filePath.replaceAll(".jar", "")));
            } catch (Exception e) {
                // Could not store algorithm
            }
        }
    }

    /**
     * Prefills the inputs table in the database with the existing input files.
     *
     * @throws UnsupportedEncodingException when input files cannot be found
     * @throws EntityStorageException       when the existing inputs cannot be queried
     */
    @SuppressWarnings("unchecked")
    protected void addFileInputs() throws UnsupportedEncodingException, EntityStorageException {
        List<FileInput> inputList = (List<FileInput>) HibernateUtil.queryCriteria(FileInput.class);
        if (!inputList.isEmpty()) {
            return;
        }

        InputDataFinder inputDataFinder = new InputDataFinder();
        File[] inputs = inputDataFinder.getAvailableFiles();

        for (File input : inputs) {
            try {
                FileInput fileInput = new FileInput(input.getPath());
                HibernateUtil.store(fileInput);
            } catch (Exception e) {
                // Could not store file input
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        server.shutdown();
    }
}
