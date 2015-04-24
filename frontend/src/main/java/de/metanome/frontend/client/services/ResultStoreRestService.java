/*
 * Copyright 2015 by the Metanome project
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

import de.metanome.algorithm_integration.results.Result;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api/result_store")
public interface ResultStoreRestService extends RestService {

  @GET
  @Path("/getAll//{type}{executionId}")
  public void listAll(@PathParam("type") String type, @PathParam("executionId") long executionID,
                      MethodCallback<List<Result>> callback);

  @GET
  @Path("/getAllFromTo/{type}/{executionId}/{sortProperty}/{sortOrder}/{start}/{end}")
  public void listAllFromTo(@PathParam("type") String type,
                            @PathParam("executionId") long executionID,
                            @PathParam("sortProperty") String sortProperty,
                            @PathParam("sortOrder") boolean ascending,
                            @PathParam("start") int start,
                            @PathParam("end") int end,
                            MethodCallback<List<Result>> callback);

  @GET
  @Path("/count/{type}/{executionId}")
  public void count(@PathParam("type") String type, @PathParam("executionId") long executionID,
                    MethodCallback<Integer> callback);

  @GET
  @Path("/loadExecution/{executionId}")
  public void loadExecution(@PathParam("executionId") long id, MethodCallback<Void> callback);
}
