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

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

import java.util.List;

/**
 * Service Implementation for service that lists available algorithms stored in the database. <p/>
 *
 * @author Jakob Zwiener
 */
public class FinderServiceImpl extends RemoteServiceServlet implements
                                                            FinderService {

  private static final long serialVersionUID = 2742248537386173766L;

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
   * @return all functional dependency algorithms in the database
   */
  @Override
  public List<Algorithm> listFunctionalDependencyAlgorithms() {
    return listAlgorithms(FunctionalDependencyAlgorithm.class);
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
}
