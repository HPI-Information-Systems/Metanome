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
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.helper.ExceptionParser;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.AlgorithmObj;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("algorithms")
public class AlgorithmResource {


  /**
   * Retrieves an Algorithm from the database.
   *
   * @param fileName the Algorithm's file name
   * @return the algorithm
   */
  @GET
  @Path("/{fileName}")
  @Produces("application/json")
  public AlgorithmObj retrieve(@PathParam("fileName") String fileName) throws EntityStorageException {
    Algorithm algorithm = (Algorithm) HibernateUtil.retrieve(Algorithm.class, fileName);
    return new AlgorithmObj(algorithm.getFileName());
  }


  private List<AlgorithmObj> convert (List<Algorithm> l) {
    List<AlgorithmObj> algorithms = new ArrayList<>();
    for (Algorithm a : l) {
      algorithms.add(new AlgorithmObj(a.getFileName()));
    }
    return algorithms;
  }


  /**
   * @return all algorithms in the database
   */
  @GET
  @Produces("application/json")
  public List<AlgorithmObj> listAllAlgorithms() {
    return convert(listAlgorithms(null));
  }

  /**
   * @return all inclusion dependency algorithms in the database
   */
  @GET
  @Path("/inclusion_dependency_algorithms/")
  @Produces("application/json")
  public List<AlgorithmObj> listInclusionDependencyAlgorithms() {
    return convert(listAlgorithms(InclusionDependencyAlgorithm.class));
  }

  /**
   * @return all unique column combination algorithms in the database
   */
  @GET
  @Path("/unique_column_combination_algorithms/")
  @Produces("application/json")
  public List<AlgorithmObj> listUniqueColumnCombinationsAlgorithms() {
    return convert(listAlgorithms(UniqueColumnCombinationsAlgorithm.class));
  }

  /**
   * @return all conditional unique column combination algorithms in the database
   */
  @GET
  @Path("/conditional_unique_column_combination_algorithms/")
  @Produces("application/json")
  public List<AlgorithmObj> listConditionalUniqueColumnCombinationsAlgorithms() {
    return convert(listAlgorithms(ConditionalUniqueColumnCombinationAlgorithm.class));
  }

  /**
   * @return all functional dependency algorithms in the database
   */
  @GET
  @Path("/functional_dependency_algorithms/")
  @Produces("application/json")
  public List<AlgorithmObj> listFunctionalDependencyAlgorithms() {
    return convert(listAlgorithms(FunctionalDependencyAlgorithm.class));
  }

  /**
   * @return all order dependency algorithms in the database
   */
  @GET
  @Path("/order_dependency_algorithms/")
  @Produces("application/json")
  public List<AlgorithmObj> listOrderDependencyAlgorithms() {
    return convert(listAlgorithms(OrderDependencyAlgorithm.class));
  }

  /**
   * @return all basic statistics algorithms in the database
   */
  @GET
  @Path("/basic_statistics_algorithms/")
  @Produces("application/json")
  public List<AlgorithmObj> listBasicStatisticsAlgorithms() {
    return convert(listAlgorithms(BasicStatisticsAlgorithm.class));
  }

  /**
   * Lists all algorithms from the database that implement a certain interface, or all if algorithm
   * class is null.
   *
   * @param algorithmClass the implemented algorithm interface.
   * @return the algorithms
   */
  protected List<Algorithm> listAlgorithms(Class<?> algorithmClass) {
    return Algorithm.retrieveAll(algorithmClass);
  }

  /* (non-Javadoc)
   * @see de.metanome.frontend.client.services.AlgorithmService#addAlgorithm(de.metanome.backend.results_db.Algorithm)
   */
  @POST
  @Path("/add")
  @Consumes("application/json")
  public void addAlgorithm(AlgorithmObj algorithmObj)
      throws EntityStorageException, AlgorithmLoadingException {
    String fileName = algorithmObj.getFileName();

    AlgorithmAnalyzer analyzer = null;
    try {
      analyzer = new AlgorithmAnalyzer(fileName);
    } catch (Exception e) {
      throw new AlgorithmLoadingException(
          ExceptionParser.parse(e, "The jar of the algorithm could not be loaded"), e);
    }

    Algorithm algorithm = new Algorithm(fileName);
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

    algorithm.store();
  }

  @DELETE
  @Path("/delete/{fileName}")
  public void deleteAlgorithm(@PathParam("fileName") String fileName)
      throws EntityStorageException {
    Algorithm algorithm = (Algorithm) HibernateUtil.retrieve(Algorithm.class, fileName);
    HibernateUtil.delete(algorithm);
  }

  /**
   * Lists all algorithm file names located in the algorithm folder.
   *
   * @return list of algorithm file names
   */
  @GET
  @Path("/files/")
  @Produces("application/json")
  public List<String> listAvailableAlgorithmFiles() throws IOException, ClassNotFoundException {
    AlgorithmFinder algorithmFinder = new AlgorithmFinder();
    List<String> files = new ArrayList<>();
    Collections.addAll(files, algorithmFinder.getAvailableAlgorithmFileNames(null));
    return files;
  }
}
