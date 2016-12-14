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
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
    return new FileInput();
  }

  /**
   * retrieves Files from a Directory
   *
   * @return the retrieved Files
   */
  @POST
  @Path("/get-directory-files")
  @Consumes("application/json")
  //@Produces("application/json")
  public void getDirectoryFiles(FileInput file) {
    try {
      List<FileInput> result = new ArrayList<>();
      FileInput newFile = store(file);
      File inpFile = new File(newFile.getFileName());
      //List<Criterion> criterion = new ArrayList<>();
      //criterion.add(Restrictions.like("fileName", "%WDC_appearances%").ignoreCase());
      //FileInput fileInput = ((List<FileInput>) HibernateUtil.queryCriteria(FileInput.class, (Criterion[]) criterion.toArray(new Criterion[criterion.size()]))).get(0);
      //FileInput retrievedFileInput = (FileInput) HibernateUtil.retrieve(FileInput.class,
      //        file.getId());

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
          //List<Criterion> curCriterion = new ArrayList<>();
          //curCriterion.add(Restrictions.like("name", curFile.getName()).ignoreCase());
          //FileInput curFileInput = ((List<FileInput>) HibernateUtil.queryCriteria(FileInput.class, criterion.toArray(new Criterion[criterion.size()]))).get(0);
          //result.add(new FileInput(curFile.getName()));
          store(new FileInput(curFile.getName()));
        }
      } else if (inpFile.isFile()) {
        //result.add(newFile);
        delete(newFile.getId());
        store(newFile);
      } else {
        throw new FileNotFoundException();
      }

      //return result;
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
