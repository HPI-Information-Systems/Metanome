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

import de.metanome.backend.results_db.Input;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface InputRestService extends RestService {

  @GET
  public void listInputs(MethodCallback<List<Input>> callback);

  @GET
  @Path("/get/{id}")
  public void getInput(@PathParam("id") long id, MethodCallback<Input> callback);

  @POST
  @Path("/store")
  public void storeInput(Input fileInput, MethodCallback<Input> callback);

  @DELETE
  @Path("/delete/{id}")
  public void deleteInput(@PathParam("id") long id, MethodCallback<Void> callback);

  @POST
  @Path("/update")
  public void updateInput(Input input, MethodCallback<Input> callback);

}
