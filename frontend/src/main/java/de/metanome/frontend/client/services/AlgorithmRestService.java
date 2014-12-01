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


import de.metanome.backend.results_db.AlgorithmObj;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api/algorithms")
public interface AlgorithmRestService extends RestService {

  @GET
  public void listAllAlgorithms(MethodCallback<List<AlgorithmObj>> callback);

  @GET
  public void listInclusionDependencyAlgorithms(MethodCallback<List<AlgorithmObj>> callback);

  @GET
  public void listFunctionalDependencyAlgorithms(MethodCallback<List<AlgorithmObj>> callback);

  @GET
  public void listUniqueColumnCombinationsAlgorithms(MethodCallback<List<AlgorithmObj>> callback);

  @GET
  public void listConditionalUniqueColumnCombinationsAlgorithms(MethodCallback<List<AlgorithmObj>> callback);

  @GET
  public void listOrderDependencyAlgorithms(MethodCallback<List<AlgorithmObj>> callback);

  @GET
  public void listBasicStatisticsAlgorithms(MethodCallback<List<AlgorithmObj>> callback);

  @GET
  public void listAvailableAlgorithmFiles(MethodCallback<List<String>> callback);

  @POST
  public void addAlgorithm(AlgorithmObj algorithmObj, MethodCallback<Void> callback);

  @DELETE
  public void deleteAlgorithm(String fileName, MethodCallback<Void> callback);
}
