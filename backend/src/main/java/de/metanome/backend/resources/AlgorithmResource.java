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

import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.backend.algorithm_loading.AlgorithmAnalyzer;
import de.metanome.backend.algorithm_loading.AlgorithmFinder;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Responsible for the database communication for algorithm and for
 * handling all restful calls of algorithms.
 *
 * @author Tanja Bergmann
 */
@Path("algorithms")
public class AlgorithmResource implements Resource<Algorithm> {

  /**
   * Adds a algorithm to the database.
   * @param algorithm the algorithm
   * @return the stored algorithm
   */
  @POST
  @Path("/store")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public Algorithm store(Algorithm algorithm) {
    algorithm = setAlgorithmTypes(algorithm);

    try {
      HibernateUtil.store(algorithm);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }

    return algorithm;
  }

  /**
   * Deletes the algorithm, which has the given id, from the database.
   * @param id the id of the algorithm, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(@PathParam("id") long id) {
    try {
      Algorithm algorithm = (Algorithm) HibernateUtil.retrieve(Algorithm.class, id);
      HibernateUtil.delete(algorithm);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }

  }

  /**
   * Retrieves an Algorithm from the database.
   *
   * @param id the Algorithm's id
   * @return the algorithm
   */
  @GET
  @Path("/get/{id}")
  @Produces("application/json")
  @Override
  public Algorithm get(@PathParam("id") long id) {
    try {
      return (Algorithm) HibernateUtil.retrieve(Algorithm.class, id);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all algorithms in the database
   */
  @GET
  @Produces("application/json")
  @Override
  public List<Algorithm> getAll() {
    try {
      return HibernateUtil.queryCriteria(Algorithm.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all inclusion dependency algorithms in the database
   */
  @GET
  @Path("/inclusion_dependency_algorithms/")
  @Produces("application/json")
  public List<Algorithm> listInclusionDependencyAlgorithms() {
    try {
      return listAlgorithms(InclusionDependencyAlgorithm.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all unique column combination algorithms in the database
   */
  @GET
  @Path("/unique_column_combination_algorithms/")
  @Produces("application/json")
  public List<Algorithm> listUniqueColumnCombinationsAlgorithms() {
    try {
      return listAlgorithms(UniqueColumnCombinationsAlgorithm.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all conditional unique column combination algorithms in the database
   */
  @GET
  @Path("/conditional_unique_column_combination_algorithms/")
  @Produces("application/json")
  public List<Algorithm> listConditionalUniqueColumnCombinationsAlgorithms() {
    try {
      return listAlgorithms(ConditionalUniqueColumnCombinationAlgorithm.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all functional dependency algorithms in the database
   */
  @GET
  @Path("/functional_dependency_algorithms/")
  @Produces("application/json")
  public List<Algorithm> listFunctionalDependencyAlgorithms() {
    try {
      return listAlgorithms(FunctionalDependencyAlgorithm.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all order dependency algorithms in the database
   */
  @GET
  @Path("/order_dependency_algorithms/")
  @Produces("application/json")
  public List<Algorithm> listOrderDependencyAlgorithms() {
    try {
      return listAlgorithms(OrderDependencyAlgorithm.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all basic statistics algorithms in the database
   */
  @GET
  @Path("/basic_statistics_algorithms/")
  @Produces("application/json")
  public List<Algorithm> listBasicStatisticsAlgorithms() {
    try {
      return listAlgorithms(BasicStatisticsAlgorithm.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Lists all algorithms from the database that implement a certain interface, or all if algorithm
   * class is null.
   *
   * @param algorithmClass the implemented algorithm interface.
   * @return the algorithms
   */
  protected List<Algorithm> listAlgorithms(Class<?>... algorithmClass) throws EntityStorageException {
    // Cannot directly use array, as some interfaces might not be relevant for query.
    ArrayList<Criterion> criteria = new ArrayList<>(algorithmClass.length);

    Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(algorithmClass));

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

  /**
   * Lists all algorithm file names located in the algorithm folder.
   *
   * @return list of algorithm file names
   */
  @GET
  @Path("/files/")
  @Produces("application/json")
  public List<String> listAvailableAlgorithmFiles() {
    try {
      AlgorithmFinder algorithmFinder = new AlgorithmFinder();
      List<String> files = new ArrayList<>();
      Collections.addAll(files, algorithmFinder.getAvailableAlgorithmFileNames(null));
      return files;
    } catch (ClassNotFoundException | IOException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Updates an algorithm in the database.
   * @param algorithm the algorithm
   * @return the updated algorithm
   */
  @POST
  @Path("/update")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public Algorithm update(Algorithm algorithm) {
    algorithm = setAlgorithmTypes(algorithm);

    try {
      HibernateUtil.update(algorithm);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
    return algorithm;
  }

  private Algorithm setAlgorithmTypes(Algorithm algorithm) {
    AlgorithmAnalyzer analyzer = null;
    try {
      analyzer = new AlgorithmAnalyzer(algorithm.getFileName());
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }

    algorithm.setFd(analyzer.isFunctionalDependencyAlgorithm());
    algorithm.setInd(analyzer.isInclusionDependencyAlgorithm());
    algorithm.setUcc(analyzer.isUniqueColumnCombinationAlgorithm());
    algorithm.setCucc(analyzer.isConditionalUniqueColumnCombinationAlgorithm());
    algorithm.setOd(analyzer.isOrderDependencyAlgorithm());
    algorithm.setBasicStat(analyzer.isBasicStatisticAlgorithm());
    algorithm.setDatabaseConnection(analyzer.isDatabaseConnectionAlgorithm());
    algorithm.setFileInput(analyzer.isFileInputAlgorithm());
    algorithm.setRelationalInput(analyzer.isRelationalInputAlgorithm());
    algorithm.setTableInput(analyzer.isTableInputAlgorithm());

    return algorithm;
  }
}
