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

import de.metanome.backend.results_db.Result;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api/results")
public interface ResultRestService extends RestService {

  @GET
  public void listResults(MethodCallback<List<Result>> callback);

  @GET
  @Path("/get/{id}")
  public void getResult(@PathParam("id") long id, MethodCallback<Result> callback);

  @POST
  @Path("/store")
  public void storeResult(Result result, MethodCallback<Result> callback);

  @DELETE
  @Path("/delete/{id}")
  public void deleteResult(@PathParam("id") long id, MethodCallback<Void> callback);
}
