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

import de.uni_potsdam.hpi.metanome.frontend.client.services.TableInputService;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.Input;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for service that lists available table inputs stored in the database.
 */
public class TableInputServiceImpl extends RemoteServiceServlet implements
                                                                TableInputService {

  private static final long serialVersionUID = -4340796020369247112L;

  /**
   * Lists all table inputs stored in the database
   * @return a list of all currently stored table inputs
   */
  @Override
  public List<TableInput> listTableInputs() {
    try {
      List<TableInput> inputs = new ArrayList<>();
      for (Input input: TableInput.retrieveAll()) {
        inputs.add((TableInput) input);
      }
      return inputs;
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   *
   * @param id the id of the table inputs which should be returned
   * @return the table input with the given id
   */
  @Override
  public TableInput getTableInput(long id) {
    try {
      return TableInput.retrieve(id);
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Stores a table input into the database
   * @param input the table input which should be stored
   */
  @Override
  public void storeTableInput(TableInput input) {
    try {
      input.store();
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteTableInput(TableInput input) {
    input.delete();
  }

}
