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

package de.metanome.backend.resources;

import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("inputs")
public class InputResource implements Resource<Input> {

  /**
   * Stores the Input in the database.
   *
   * @return the Input
   */
  @POST
  @Path("/store")
  @Produces("application/json")
  @Override
  public Input store(Input input) {
    try {
      HibernateUtil.store(input);
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return input;
  }

  /**
   * Deletes the Input, which has the given id, from the database.
   * @param id the id of the Input, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(long id) {
    try {
      Input tableInput = (Input) HibernateUtil.retrieve(Input.class, id);
      HibernateUtil.delete(tableInput);
    } catch (EntityStorageException e) {
      throw new WebException(e);
    }
  }

  /**
   * retrieves a Input from the Database
   * @param id the id of the Input
   * @return the retrieved Input
   */
  @GET
  @Path("/get/{id}")
  @Produces("application/json")
  @Override
  public Input get(long id) {
    try {
      return (Input) HibernateUtil.retrieve(Input.class, id);
    } catch (EntityStorageException e) {
      throw new WebException(e);
    }
  }

  /**
   * @return all Inputs in the database
   */
  @GET
  @Produces("application/json")
  @Override
  public List<Input> getAll() {
    try {
      return HibernateUtil.queryCriteria(Input.class);
    } catch (EntityStorageException e) {
      throw new WebException(e);
    }
  }

}
