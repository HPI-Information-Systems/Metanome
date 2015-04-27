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

import de.metanome.backend.algorithm_loading.InputDataFinder;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("fileInputs")
public class FileInputResource implements Resource<FileInput> {

  private final InputDataFinder inputDataFinder;

  public FileInputResource() {
    inputDataFinder = new InputDataFinder();
  }

  @GET
  @Path("/availableInputFiles")
  @Produces("application/json")
  public List<String> listAvailableInputFiles() throws Exception {
    File[] csvFiles = inputDataFinder.getAvailableFiles();

    List<String> csvInputFilePaths = new ArrayList<>();
    for (int i = 0; i < csvFiles.length; i++) {
      csvInputFilePaths.add(i, csvFiles[i].getPath());
    }

    return csvInputFilePaths;
  }

  /**
   * @return all FileInputs in the database
   */
  @GET
  @Produces("application/json")
  @Override
  public List<FileInput> getAll() {
    try {
      return HibernateUtil.queryCriteria(FileInput.class);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Updates a file input in the database.
   *
   * @param fileInput the file input
   * @return the updated file input
   */
  @POST
  @Path("/update")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public FileInput update(FileInput fileInput) {
    try {
      HibernateUtil.update(fileInput);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
    return fileInput;
  }

  /**
   * retrieves a FileInput from the Database
   *
   * @param id the id of the FileInput
   * @return the retrieved FileInput
   */
  @GET
  @Path("/get/{id}")
  @Produces("application/json")
  @Override
  public FileInput get(@PathParam("id") long id) {
    try {
      return (FileInput) HibernateUtil.retrieve(FileInput.class, id);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Stores the FileInput in the database.
   *
   * @return the FileInput
   */
  @POST
  @Path("/store")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public FileInput store(FileInput file) {
    try {
      HibernateUtil.store(file);
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * Deletes the FileInput, which has the given id, from the database.
   *
   * @param id the id of the FileInput, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(@PathParam("id") long id) {
    try {
      FileInput fileInput = (FileInput) HibernateUtil.retrieve(FileInput.class, id);
      HibernateUtil.delete(fileInput);
    } catch (EntityStorageException e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

}
