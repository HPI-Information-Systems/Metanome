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

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.backend.algorithm_loading.AlgorithmJarLoader;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("parameter")
public class ParameterResource {

  /**
   * Loads an algorithm and its configuration requirements
   *
   * @param algorithmFileName name of the algorithm for which the configuration parameters shall be
   *                          retrieved
   * @return a list of {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}s
   * necessary for calling the given algorithm
   */
  @GET
  @Path("/{algorithmFileName}")
  @Produces("application/json")
  public List<ConfigurationRequirement> retrieveParameters(
      @PathParam("algorithmFileName") String algorithmFileName) {
    try {
      AlgorithmJarLoader jarLoader = new AlgorithmJarLoader();
      Algorithm algorithm = jarLoader.loadAlgorithm(algorithmFileName);
      return algorithm.getConfigurationRequirements();
    } catch (Throwable e) {
      throw new WebException("Could not retrieve Parameters!", Response.Status.BAD_REQUEST);
    }
  }
}
