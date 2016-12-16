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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

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
   * @return FileInputs of the retrieved Files
   */
  @POST
  @Path("/get-directory-files")
  @Consumes("application/json")
  public void getDirectoryFiles(FileInput file) {
    try {
      FileInput newFile = store(file);
      File inpFile = new File(newFile.getFileName());
      List<String> pathList = getAllPaths();

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
