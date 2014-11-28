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

import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.AlgorithmObj;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("algorithms")
public class AlgorithmRes {

  @GET
  @Produces("application/json")
  public List<AlgorithmObj> getAlgorithms() {

    List<AlgorithmObj> algorithms = new ArrayList<AlgorithmObj>();
    for (int i = 0; i < 5; i++) {
      algorithms.add(new AlgorithmObj("test-" + i + ".jar"));
    }

    return algorithms;
  }

  /**
   * Retrieves an Algorithm from the database.
   *
   * @param fileName the Algorithm's file name
   * @return the algorithm
   */
  @GET
  @Path("/{fileName}")
  @Produces("application/json")
  public AlgorithmObj retrieve(@PathParam("fileName") String fileName) throws
                                                                       EntityStorageException {
    Algorithm algo = (Algorithm) HibernateUtil.retrieve(Algorithm.class, fileName);
    return new AlgorithmObj(algo.getFileName());
  }

}
