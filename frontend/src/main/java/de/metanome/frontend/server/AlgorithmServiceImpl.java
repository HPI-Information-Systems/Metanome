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

import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.backend.algorithm_loading.AlgorithmAnalyzer;
import de.metanome.backend.algorithm_loading.AlgorithmFinder;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.frontend.client.services.AlgorithmService;

import java.util.List;

/**
 * Service Implementation for service that lists available algorithms stored in the database.
 *
 * @author Jakob Zwiener
 */
public class AlgorithmServiceImpl extends RemoteServiceServlet implements AlgorithmService {

  private static final long serialVersionUID = 2742248537386173766L;

  private AlgorithmFinder algorithmFinder = new AlgorithmFinder();


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

  /**
   * @return all inclusion dependency algorithms in the database
   */
  @Override
  public List<Algorithm> listInclusionDependencyAlgorithms() {
    return listAlgorithms(InclusionDependencyAlgorithm.class);
  }

  /**
   * @return all unique column combination algorithms in the database
   */
  @Override
  public List<Algorithm> listUniqueColumnCombinationsAlgorithms() {
    return listAlgorithms(UniqueColumnCombinationsAlgorithm.class);
  }

  /**
   * @return all conditional unique column combination algorithms in the database
   */
  @Override
  public List<Algorithm> listConditionalUniqueColumnCombinationsAlgorithms() {
    return listAlgorithms(ConditionalUniqueColumnCombinationAlgorithm.class);
  }

  /**
   * @return all functional dependency algorithms in the database
   */
  @Override
  public List<Algorithm> listFunctionalDependencyAlgorithms() {
    return listAlgorithms(FunctionalDependencyAlgorithm.class);
  }

  /**
   * @return all order dependency algorithms in the database
   */
  @Override
  public List<Algorithm> listOrderDependencyAlgorithms() {
    return listAlgorithms(OrderDependencyAlgorithm.class);
  }
  
  /**
   * @return all basic statistics algorithms in the database
   */
  @Override
  public List<Algorithm> listBasicStatisticsAlgorithms() {
    return listAlgorithms(BasicStatisticsAlgorithm.class);
  }

  /**
   * @return all algorithms in the database
   */
  @Override
  public List<Algorithm> listAllAlgorithms() {
    return listAlgorithms(null);
  }

  /* (non-Javadoc)
   * @see de.metanome.frontend.client.services.AlgorithmService#addAlgorithm(de.metanome.backend.results_db.Algorithm)
   */
  @Override
  public Algorithm addAlgorithm(Algorithm algorithm)
      throws EntityStorageException, AlgorithmLoadingException {
    AlgorithmAnalyzer analyzer = null;
    try {
      analyzer = new AlgorithmAnalyzer(algorithm.getFileName());
    } catch (Exception e) {
      throw new AlgorithmLoadingException("The jar of the algorithm could not be loaded! (" + e.toString() + ")", e);
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

    algorithm.store();

    return algorithm;
  }

  @Override
  public void deleteAlgorithm(Algorithm algorithm) {
    algorithm.delete();
  }

  /**
   * Lists all algorithm file names located in the algorithm folder.
   *
   * @return list of algorithm file names
   */
  @Override
  public String[] listAvailableAlgorithmFiles() {
    String[] fileNames = null;

    try {
      fileNames = algorithmFinder.getAvailableAlgorithmFileNames(null);
    } catch (Exception e) {
      //TODO: error handling
      System.out.println("FAILED to FIND algorithm files.");
      e.printStackTrace();
    }

    return fileNames;
  }
}
