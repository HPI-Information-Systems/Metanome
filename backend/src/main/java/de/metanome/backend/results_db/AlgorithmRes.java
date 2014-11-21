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

import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
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
  public Algorithm retrieve(@PathParam("fileName") String fileName) throws EntityStorageException {
    return (Algorithm) HibernateUtil.retrieve(Algorithm.class, fileName);
  }

  /**
   * Retrieves all algorithms stored in the database.
   *
   * @return a list of all algorithms
   */
  @GET
  @Path("/all")
  @Produces("application/json")
  public List<Algorithm> retrieveAll() throws EntityStorageException {
    return HibernateUtil.queryCriteria(Algorithm.class);
  }

  /**
   * Retrieves all algorithms stored in the database, that are of a type associated to one of the
   * interfaces.
   *
   * @param algorithmInterfaces algorithm interfaces specifying the expected algorithm type
   * @return a list of matching algorithms
   */
  @GET
  @Path("/all/{interfaces}")
  @Produces("application/json")
  public static List<Algorithm> retrieveAll(@PathParam("interfaces") Class<?>... algorithmInterfaces) {
    // Cannot directly use array, as some interfaces might not be relevant for query.
    ArrayList<Criterion> criteria = new ArrayList<>(algorithmInterfaces.length);

    Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(algorithmInterfaces));

    if (interfaces.contains(InclusionDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("ind", true));
    }
    if (interfaces.contains(FunctionalDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("fd", true));
    }

    if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
      criteria.add(Restrictions.eq("ucc", true));
    }

    if (interfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class)) {
      criteria.add(Restrictions.eq("cucc", true));
    }

    if (interfaces.contains(OrderDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("od", true));
    }

    if (interfaces.contains(BasicStatisticsAlgorithm.class)) {
      criteria.add(Restrictions.eq("basicStat", true));
    }

    List<Algorithm> algorithms = null;
    try {
      algorithms =
          HibernateUtil
              .queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));
    } catch (EntityStorageException e) {
      // Algorithm should implement Entity, so the exception should not occur.
      e.printStackTrace();
    }

    return algorithms;
  }

}
