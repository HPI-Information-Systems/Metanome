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

package de.metanome.backend.results_db;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("algorithms")
public class AlgorithmRes {

  /**
   * Retrieves an Algorithm from the database.
   *
   * @param fileName the Algorithm's file name
   * @return the algorithm
   */
  @GET
  @Path("/algorithm/{fileName}")
  @Produces("application/json")
  public AlgorithmObj retrieve(@PathParam("fileName") String fileName) throws EntityStorageException {
    return (AlgorithmObj) HibernateUtil.retrieve(AlgorithmObj.class, fileName);
  }

  @POST
  @Path("/store/{fileName}")
  @Produces("application/json")
  public void storeAlgorithm(@PathParam("fileName") String fileName) throws EntityStorageException {
    AlgorithmObj algorithmObj = new AlgorithmObj(fileName);
    HibernateUtil.store(algorithmObj);
  }

}
