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

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("file-inputs")
public class FileInputResource implements Resource<FileInput> {

  private final InputDataFinder inputDataFinder;

  public FileInputResource() {
    inputDataFinder = new InputDataFinder();
  }

  @GET
  @Path("/available-input-files")
  @Produces("application/json")
  public List<String> listAvailableInputFiles() {
    try {
      File[] csvFiles = inputDataFinder.getAvailableFiles();

      List<String> csvInputFilePaths = new ArrayList<>();
      for (int i = 0; i < csvFiles.length; i++) {
        csvInputFilePaths.add(i, csvFiles[i].getPath());
      }

      return csvInputFilePaths;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all FileInputs in the database
   */
  @GET
  @Produces("application/json")
  @SuppressWarnings("unchecked")
  @Override
  public List<FileInput> getAll() {
    try {
      return (List<FileInput>) HibernateUtil.queryCriteria(FileInput.class);
    } catch (Exception e) {
      e.printStackTrace();
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
    } catch (EntityStorageException e1) {
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
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
      FileInput fileInput = (FileInput) HibernateUtil.retrieve(FileInput.class, id);
      if((new File(fileInput.getFileName())).isFile()) {
        return fileInput;
      }
    } catch (EntityStorageException e1) {
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
    return new FileInput();
  }

  /**
   * retrieves Files from a Directory
   *
   * @param name the name of the FileInput of a Directory
   * @return the retrieved Files
   */
  @GET
  @Path("/get-directory-files/{name}")
  //@Consumes("application/json")
  @Produces("application/json")
  public List<FileInput> getDirectoryFiles(@PathParam("name") String name) {
    try {
      List<FileInput> result = new ArrayList<>();
      //FileInput fileInput = (FileInput) HibernateUtil.retrieve(FileInput.class, id.getId());
      File inpFile = new File(name);

      if (inpFile.isDirectory()) {
        File[] directoryFiles = inpFile.listFiles(new FilenameFilter() {
          @Override
          public boolean accept(File file, String name) {
            for (String fileEnding : InputDataFinder.ACCEPTED_FILE_ENDINGS) {
              if (name.endsWith(fileEnding)) {
                return true;
              }
            }
            return false;
          }
        });
        for (File curFile : directoryFiles) {
          FileInput curFileInput = new FileInput(curFile.getName());
          curFileInput.setFileName(curFile.getName());
          //HibernateUtil.store(curFileInput);
          result.add((FileInput) HibernateUtil.retrieve(FileInput.class, curFileInput.getId()));
        }
      } else if (inpFile.isFile()) {
        FileInput curFileInput = new FileInput(inpFile.getName());
        curFileInput.setFileName(inpFile.getName());
        //HibernateUtil.store(curFileInput);
        result.add((FileInput) HibernateUtil.retrieve(FileInput.class, curFileInput.getId()));

      } else {
        throw new FileNotFoundException();
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
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
      } catch (EntityStorageException e1) {
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
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
    } catch (EntityStorageException e1) {
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

}
