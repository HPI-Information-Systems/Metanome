/**
 * Copyright 2014-2016 by Metanome Project
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

import de.metanome.backend.algorithm_loading.FileUpload;
import de.metanome.backend.algorithm_loading.InputDataFinder;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("file-inputs")
public class FileInputResource implements Resource<FileInput> {

  private final InputDataFinder inputDataFinder;

  public FileInputResource() {
    inputDataFinder = new InputDataFinder();
  }

  @GET
  @Path("/available-input-files")
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  public List<String> listAvailableInputFiles() {
    try {
      File[] csvFiles = inputDataFinder.getAvailableFiles(true);

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
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
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
   * @return all Paths of FileInputs in the database as Strings
   */
  public List<String> getAllPaths() {
    List<String> pathList = new ArrayList<>();
    List<FileInput> inputList = getAll();
    for (FileInput elem : inputList) {
      pathList.add(elem.getFileName());
    }
    return pathList;
  }

  /**
   * Updates a file input in the database.
   *
   * @param fileInput the file input
   * @return the updated file input
   */
  @POST
  @Path("/update")
  @Consumes(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
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
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Override
  public FileInput get(@PathParam("id") long id) {
    try {
      FileInput fileInput = (FileInput) HibernateUtil.retrieve(FileInput.class, id);
      return fileInput;
    } catch (EntityStorageException e1) {
      e1.printStackTrace();
      throw new WebException(e1, Response.Status.BAD_REQUEST);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * if file contains a Directory it returns the files in that directory
   * if file contains a file it returns this file
   *
   * @param file FileInput that is either a directory or a file
   * stores FileInputs of the retrieved Files
   */
  @POST
  @Path("/get-directory-files")
  @Consumes(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  public void getDirectoryFiles(FileInput file) {
    try {
      FileInput newFile = store(file);
      File inpFile = new File(newFile.getFileName());
      List<String> pathList = getAllPaths();

      if (inpFile.isDirectory()) {
        File[] directoryFiles = inpFile.listFiles(new FilenameFilter() {
          @Override
          public boolean accept(File file, String name) {
            for (String fileEnding : Constants.ACCEPTED_FILE_ENDINGS_ARRAY) {
              if (name.endsWith(fileEnding)) {
                return true;
              }
            }
            return false;
          }
        });
        delete(newFile.getId());
        for (File curFile : directoryFiles) {
          if (!pathList.contains(curFile.getAbsolutePath())) {
            store(new FileInput(curFile.getAbsolutePath()));
          }
        }
      } else if (inpFile.isFile()) {
        delete(newFile.getId());
        store(newFile);
      } else {
        throw new FileNotFoundException();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Passes parameter to store function.
   *
   * @param file FileInput to store
   */
  @POST
  @Path(Constants.STORE_RESOURCE_PATH)
  @Consumes(Constants.APPLICATION_JSON_RESOURCE_PATH)
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  public void executeDatabaseStore(FileInput file) {
    try {
      store(file);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }


  @POST
  @Path(Constants.STORE_RESOURCE_PATH)
  @Consumes("multipart/form-data")
  @Produces(Constants.APPLICATION_JSON_RESOURCE_PATH)
  public void uploadAndExecuteStore(@FormDataParam("file") InputStream uploadedInputStream,
                         @FormDataParam("file") FormDataContentDisposition fileDetail) {

    try {
    /* Check if File already exist */

      InputDataFinder inputDataFinder = new InputDataFinder();

    /* Upload file to algorithm directory */

      FileUpload fileToDisk= new FileUpload();
      Boolean fileExist = fileToDisk.writeFileToDisk(
              uploadedInputStream,
              fileDetail,
              inputDataFinder.getFileDirectory());

    /* Add InputFile to the Database if InputFile is new using the store function*/

      if (!fileExist) {
        FileInput file = new FileInput(fileDetail.getFileName());
        store(file);
      }
    } catch(Exception e){
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Stores FileInput into the Database
   *
   * @param file FileInput to store
   * @return stored FileInput
   */

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
