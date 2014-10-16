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


package de.metanome.frontend.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;

import java.util.List;

@RemoteServiceRelativePath("algorithmService")
public interface AlgorithmService extends RemoteService {

  public String[] listAvailableAlgorithmFiles();

  public List<Algorithm> listInclusionDependencyAlgorithms();

  public List<Algorithm> listFunctionalDependencyAlgorithms();

  public List<Algorithm> listUniqueColumnCombinationsAlgorithms();

  public List<Algorithm> listConditionalUniqueColumnCombinationsAlgorithms();
  
  public List<Algorithm> listOrderDependencyAlgorithms();

  public List<Algorithm> listBasicStatisticsAlgorithms();

  public List<Algorithm> listAllAlgorithms();

  public Algorithm addAlgorithm(Algorithm algorithm)
      throws EntityStorageException, AlgorithmLoadingException;

  public void deleteAlgorithm(Algorithm algorithm);

}
