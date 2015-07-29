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


import de.metanome.backend.results_db.Algorithm;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api/algorithms")
public interface AlgorithmRestService extends RestService {

  @GET
  public void listAlgorithms(MethodCallback<List<Algorithm>> callback);

  @GET
  @Path("/inclusion-dependency-algorithms/")
  public void listInclusionDependencyAlgorithms(MethodCallback<List<Algorithm>> callback);

  @GET
  @Path("/functional-dependency-algorithms/")
  public void listFunctionalDependencyAlgorithms(MethodCallback<List<Algorithm>> callback);

  @GET
  @Path("/unique-column-combination-algorithms/")
  public void listUniqueColumnCombinationsAlgorithms(MethodCallback<List<Algorithm>> callback);

  @GET
  @Path("/conditional-unique-column-combination-algorithms/")
  public void listConditionalUniqueColumnCombinationsAlgorithms(
      MethodCallback<List<Algorithm>> callback);

  @GET
  @Path("/order-dependency-algorithms/")
  public void listOrderDependencyAlgorithms(MethodCallback<List<Algorithm>> callback);

  @GET
  @Path("/basic-statistics-algorithms/")
  public void listBasicStatisticsAlgorithms(MethodCallback<List<Algorithm>> callback);

  @GET
  @Path("/available-algorithm-files/")
  public void listAvailableAlgorithmFiles(MethodCallback<List<String>> callback);

  @GET
  @Path("/get/{id}")
  public void getAlgorithm(@PathParam("id") long id, MethodCallback<Algorithm> callback);

  @POST
  @Path("/store")
  public void storeAlgorithm(Algorithm algorithm, MethodCallback<Algorithm> callback);

  @DELETE
  @Path("/delete/{id}")
  public void deleteAlgorithm(@PathParam("id") long id, MethodCallback<Void> callback);

  @POST
  @Path("/update")
  public void updateAlgorithm(Algorithm algorithm, MethodCallback<Algorithm> callback);
}
