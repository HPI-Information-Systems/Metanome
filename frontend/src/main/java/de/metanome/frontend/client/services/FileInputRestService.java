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

import de.metanome.backend.results_db.FileInput;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api/fileInputs")
public interface FileInputRestService extends RestService {

  @GET
  public void listFileInputs(MethodCallback<List<FileInput>> callback);

  @GET
  @Path("/availableInputFiles")
  public void listAvailableInputFiles(MethodCallback<List<String>> callback);

  @GET
  @Path("/get/{id}")
  public void getFileInput(@PathParam("id") long id, MethodCallback<FileInput> callback);

  @POST
  @Path("/store")
  public void storeFileInput(FileInput fileInput, MethodCallback<FileInput> callback);

  @DELETE
  @Path("/delete/{id}")
  public void deleteFileInput(@PathParam("id") long id, MethodCallback<Void> callback);

  @POST
  @Path("/update")
  public void updateFileInput(FileInput fileInput, MethodCallback<FileInput> callback);

}
