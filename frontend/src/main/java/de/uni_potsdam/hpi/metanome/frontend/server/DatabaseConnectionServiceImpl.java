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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionService;

import java.util.List;

/**
 * Service Implementation for service that lists available database connections stored in the
 * database.
 */
public class DatabaseConnectionServiceImpl extends RemoteServiceServlet implements
                                                                        DatabaseConnectionService {

  private static final long serialVersionUID = -993802777300283362L;

  /**
   * @return a list of all database connections in the database or null, if there are none
   */
  @Override
  public List<DatabaseConnection> listDatabaseConnections() {
    try {
      return DatabaseConnection.retrieveAll();
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param id of the database connection, which should be returned
   * @return the database connection or null, if no database connection with the id was found
   */
  @Override
  public DatabaseConnection getDatabaseConnection(long id) {
    try {
      return DatabaseConnection.retrieve(id);
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param connection the database connection to store
   */
  @Override
  public void storeDatabaseConnection(DatabaseConnection connection) {
    try {
      connection.store();
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteDatabaseConnection(DatabaseConnection connection) {
    connection.delete();
  }
}
