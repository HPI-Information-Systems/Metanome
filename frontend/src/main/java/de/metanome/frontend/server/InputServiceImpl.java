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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Input;
import de.metanome.frontend.client.services.InputService;

import java.util.ArrayList;
import java.util.List;

public class InputServiceImpl extends RemoteServiceServlet implements InputService {

  /**
   * Lists all inputs (table and file inputs) available in the database.
   * @return all inputs
   */
  @Override
  public List<Input> listInputs() {
    try {
      List<Input> inputs = new ArrayList<>();
      for (Input input : Input.retrieveAll()) {
        inputs.add((Input) input);
      }
      return inputs;
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return null;
  }
}
