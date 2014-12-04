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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;

import java.util.ArrayList;
import java.util.List;

@RemoteServiceRelativePath("executionService")
public interface ExecutionService extends RemoteService {

  public long executeAlgorithm(String algorithmName,
                               long algorithmId,
                               String executionIdentifier,
                               List<ConfigurationRequirement> parameters)
      throws AlgorithmConfigurationException, AlgorithmLoadingException,
             AlgorithmExecutionException;

  public ArrayList<Result> fetchNewResults(String algorithmName);

  public float fetchProgress(String executionIdentifier);
}
