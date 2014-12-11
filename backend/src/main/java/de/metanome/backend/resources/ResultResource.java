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
import de.metanome.backend.results_db.Result;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("results")
public class ResultResource implements Resource<Result> {

  /**
   * Adds an result to the database.
   * @param result the result
   * @return the stored result
   */
  @POST
  @Path("/store")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public Result store(Result result) {
    try {
      HibernateUtil.store(result);
    } catch (EntityStorageException e) {
      throw new WebException(e);
    }

    return result;
  }

  /**
   * Deletes the result, which has the given id, from the database.
   * @param id the id of the result, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(long id) {
    try {
      Result result = (Result) HibernateUtil.retrieve(Result.class, id);
      HibernateUtil.delete(result);
    } catch (EntityStorageException e) {
      throw new WebException(e);
    }
  }

  /**
   * Retrieves a result from the database.
   *
   * @param id the result's id
   * @return the result
   */
  @GET
  @Path("/get/{id}")
  @Produces("application/json")
  @Override
  public Result get(@PathParam("id") long id) {
    try {
      return (Result) HibernateUtil.retrieve(Result.class, id);
    } catch (EntityStorageException e) {
      throw new WebException(e);
    }
  }

  /**
   * @return all results in the database
   */
  @Override
  public List<Result> getAll() {
    List<Result> results = null;

    try {
      results = HibernateUtil.queryCriteria(Result.class);
    } catch (EntityStorageException e) {
      // Algorithm should implement Entity, so the exception should not occur.
      e.printStackTrace();
    }

    return results;
  }
}
