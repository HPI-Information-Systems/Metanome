/**
 * Copyright 2014-2017 by Metanome Project
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

import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.MatchingDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalFunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalInclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.MultivaluedDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.results.DenialConstraint;
import de.metanome.backend.algorithm_loading.AlgorithmAnalyzer;
import de.metanome.backend.algorithm_loading.AlgorithmFinder;
import de.metanome.backend.algorithm_loading.AlgorithmJarLoader;
import de.metanome.backend.algorithm_loading.FileUpload;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.AlgorithmType;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Responsible for the database communication for algorithm and for handling all restful calls of
 * algorithms.
 *
 * @author Tanja Bergmann
 */
@Path("algorithms")
public class AlgorithmResource implements Resource<Algorithm> {


  /**
   * Adds a algorithm to the database for file already existing in the algorithms directory.
   *
   * @param algorithm the algorithm stored in the application
   */
  @POST
  @Path("/store")
  @Consumes("application/json")
  @Produces("application/json")
  public void executeDatabaseStore(Algorithm algorithm) {
    try {
      store(algorithm);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }




  /**
   * Uploads algorithm into the algorithms directory and adds the algorithm to the database
   *
   * @param uploadedInputStream Stream of File send to Backend
   * @param fileDetail Additional Meta-Information about the uploaded file
   */

  @POST
  @Path("/store")
  @Consumes("multipart/form-data")
  @Produces("application/json")
  public void uploadAndExecuteStore(@FormDataParam("file") InputStream uploadedInputStream,
                              @FormDataParam("file") FormDataContentDisposition fileDetail) {

    try {
      /* Check if Algorithm already exist */
      AlgorithmFinder jarFinder = new AlgorithmFinder();

      /* Upload file to algorithm directory */

      FileUpload fileToDisk= new FileUpload();
      Boolean fileExist =
            fileToDisk.writeFileToDisk(
                    uploadedInputStream,
                    fileDetail,
                    jarFinder.getAlgorithmDirectory());

      /* Add Algorithm to the Database if newly added using Store function*/
      if(!fileExist) {
        Algorithm algorithm = new Algorithm(fileDetail.getFileName());
        store(algorithm);
      }
    } catch(Exception e){
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }



  /**
   * Adds a algorithm to the database for file already existing in the algorithms directory.
   *
   * @param algorithm the algorithm stored in the application
   * @return the stored algorithm
   */
  public Algorithm store(Algorithm algorithm) {
    try {
      // Load the jar and get the author and description from the algorithm
      AlgorithmJarLoader loader = new AlgorithmJarLoader();
      de.metanome.algorithm_integration.Algorithm jarAlgorithm =
                                              loader.loadAlgorithm(algorithm.getFileName());
      String authors = jarAlgorithm.getAuthors();
      String description = jarAlgorithm.getDescription();

      algorithm = setAlgorithmTypes(algorithm);
      algorithm.setAuthor(authors);
      algorithm.setDescription(description);

      HibernateUtil.store(algorithm);
      return algorithm;
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }


  /**
   * Deletes the algorithm, which has the given id, from the database.
   *
   * @param id the id of the algorithm, which should be deleted
   */
  @DELETE
  @Path("/delete/{id}")
  @Override
  public void delete(@PathParam("id") long id) {
    try {
      Algorithm algorithm = (Algorithm) HibernateUtil.retrieve(Algorithm.class, id);
      HibernateUtil.delete(algorithm);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Retrieves an Algorithm from the database.
   *
   * @param id the Algorithm's id
   * @return the algorithm
   */
  @GET
  @Path("/get/{id}")
  @Produces("application/json")
  @Override
  public Algorithm get(@PathParam("id") long id) {
    try {
      return (Algorithm) HibernateUtil.retrieve(Algorithm.class, id);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all algorithms in the database
   */
  @GET
  @Produces("application/json")
  @SuppressWarnings("unchecked")
  @Override
  public List<Algorithm> getAll() {
    try {
      return (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class);
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all inclusion dependency algorithms in the database
   */
  @GET
  @Path("/inclusion-dependency-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listInclusionDependencyAlgorithms() {
    try {
      return listAlgorithms(InclusionDependencyAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all unique column combination algorithms in the database
   */
  @GET
  @Path("/unique-column-combination-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listUniqueColumnCombinationsAlgorithms() {
    try {
      return listAlgorithms(UniqueColumnCombinationsAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all conditional unique column combination algorithms in the database
   */
  @GET
  @Path("/conditional-unique-column-combination-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listConditionalUniqueColumnCombinationsAlgorithms() {
    try {
      return listAlgorithms(ConditionalUniqueColumnCombinationAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all functional dependency algorithms in the database
   */
  @GET
  @Path("/functional-dependency-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listFunctionalDependencyAlgorithms() {
    try {
      return listAlgorithms(FunctionalDependencyAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
  
  /**
   * @return all conditional inclusion dependency algorithms in the database
   */
  @GET
  @Path("/conditional-inclusion-dependency-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listConditionalInclusionDependencyAlgorithms() {
    try {
      return listAlgorithms(ConditionalInclusionDependencyAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all matching dependency algorithms in the database
   */
  @GET
  @Path("/matching-dependency-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listMatchingDependencyAlgorithms() {
    try {
      return listAlgorithms(MatchingDependencyAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all conditional functional dependency algorithms in the database
   */
  @GET
  @Path("/conditional-functional-dependency-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listConditionalFunctionalDependencyAlgorithms() {
    try {
      return listAlgorithms(ConditionalFunctionalDependencyAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all order dependency algorithms in the database
   */
  @GET
  @Path("/order-dependency-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listOrderDependencyAlgorithms() {
    try {
      return listAlgorithms(OrderDependencyAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
  
  /**
   * @return all multivalued dependency algorithms in the database
   */
  @GET
  @Path("/multivalued-dependency-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listMultivaluedDependencyAlgorithms() {
    try {
      return listAlgorithms(MultivaluedDependencyAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * @return all basic statistics algorithms in the database
   */
  @GET
  @Path("/basic-statistics-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listBasicStatisticsAlgorithms() {
    try {
      return listAlgorithms(BasicStatisticsAlgorithm.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
  
  /**
   * @return all denial constraint algorithms in the database
   */
  @GET
  @Path("/denial-constraint-algorithms/")
  @Produces("application/json")
  public List<Algorithm> listDenialConstraintAlgorithms() {
    try {
      return listAlgorithms(DenialConstraint.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Lists all algorithms from the database that implement a certain interface, or all if algorithm
   * class is null.
   *
   * @param algorithmClass the implemented algorithm interface.
   * @return the algorithms
   * @throws de.metanome.backend.results_db.EntityStorageException if algorithms could not be listed
   */
  @SuppressWarnings("unchecked")
  protected List<Algorithm> listAlgorithms(Class<?>... algorithmClass)
    throws EntityStorageException {
    // Cannot directly use array, as some interfaces might not be relevant for query.
    ArrayList<Criterion> criteria = new ArrayList<>(algorithmClass.length);

    Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(algorithmClass));

    if (interfaces.contains(InclusionDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("ind", true));
    }
    if (interfaces.contains(FunctionalDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("fd", true));
    }
    if (interfaces.contains(ConditionalInclusionDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("cid", true));
    }
    if (interfaces.contains(MatchingDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("md", true));
    }

    if (interfaces.contains(ConditionalFunctionalDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("cfd", true));
    }

    if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
      criteria.add(Restrictions.eq("ucc", true));
    }

    if (interfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class)) {
      criteria.add(Restrictions.eq("cucc", true));
    }

    if (interfaces.contains(OrderDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("od", true));
    }
    
    if (interfaces.contains(MultivaluedDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("mvd", true));
    }

    if (interfaces.contains(BasicStatisticsAlgorithm.class)) {
      criteria.add(Restrictions.eq("basicStat", true));
    }
    
    if (interfaces.contains(DenialConstraint.class)) {
      criteria.add(Restrictions.eq("dc", true));
    }

    return (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));
  }

  /**
   * Lists all algorithm file names located in the algorithm folder.
   *
   * @return list of algorithm file names
   */
  @GET
  @Path("/available-algorithm-files/")
  @Produces("application/json")
  public List<String> listAvailableAlgorithmFiles() {
    try {
      AlgorithmFinder algorithmFinder = new AlgorithmFinder();
      List<String> files = new ArrayList<>();
      Collections.addAll(files, algorithmFinder.getAvailableAlgorithmFileNames(null));
      return files;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Updates an algorithm in the database.
   *
   * @param algorithm the algorithm
   * @return the updated algorithm
   */
  @POST
  @Path("/update")
  @Consumes("application/json")
  @Produces("application/json")
  @Override
  public Algorithm update(Algorithm algorithm) {
    try {
      // Load the jar and get the author and description from the algorithm
      AlgorithmJarLoader loader = new AlgorithmJarLoader();
      de.metanome.algorithm_integration.Algorithm jarAlgorithm = loader.loadAlgorithm(algorithm.getFileName());
      String authors = jarAlgorithm.getAuthors();
      String description = jarAlgorithm.getDescription();

      algorithm = setAlgorithmTypes(algorithm);
      algorithm.setAuthor(authors);
      algorithm.setDescription(description);

      HibernateUtil.update(algorithm);
      return algorithm;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  private Algorithm setAlgorithmTypes(Algorithm algorithm) throws Exception {
    AlgorithmAnalyzer analyzer = new AlgorithmAnalyzer(algorithm.getFileName());

    algorithm.setFd(analyzer.hasType(AlgorithmType.FD));
    algorithm.setCid(analyzer.hasType(AlgorithmType.CID));
    algorithm.setMd(analyzer.hasType(AlgorithmType.MD));
    algorithm.setCfd(analyzer.hasType(AlgorithmType.CFD));
    algorithm.setInd(analyzer.hasType(AlgorithmType.IND));
    algorithm.setUcc(analyzer.hasType(AlgorithmType.UCC));
    algorithm.setCucc(analyzer.hasType(AlgorithmType.CUCC));
    algorithm.setOd(analyzer.hasType(AlgorithmType.OD));
    algorithm.setMvd(analyzer.hasType(AlgorithmType.MVD));
    algorithm.setBasicStat(analyzer.hasType(AlgorithmType.BASIC_STAT));
    algorithm.setDc(analyzer.hasType(AlgorithmType.DC));
    algorithm.setDatabaseConnection(analyzer.hasType(AlgorithmType.DB_CONNECTION));
    algorithm.setFileInput(analyzer.hasType(AlgorithmType.FILE_INPUT));
    algorithm.setRelationalInput(analyzer.hasType(AlgorithmType.RELATIONAL_INPUT));
    algorithm.setTableInput(analyzer.hasType(AlgorithmType.TABLE_INPUT));

    return algorithm;
  }

  @GET
  @Path("/algorithms-for-file-inputs")
  @Produces("application/json")
  @SuppressWarnings("unchecked")
  public List<Algorithm> getAlgorithmsForFileInputs() {
    try {
      ArrayList<Criterion> criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("fileInput", true));

      List<Algorithm> algorithms = (List<Algorithm>) HibernateUtil
            .queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));

      criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("relationalInput", true));
      List<Algorithm> storedAlgorithms = (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));
      if (algorithms != null) {
        algorithms.addAll(storedAlgorithms);
      }
      return algorithms;

    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  @GET
  @Path("/algorithms-for-table-inputs")
  @Produces("application/json")
  @SuppressWarnings("unchecked")
  public List<Algorithm> getAlgorithmsForTableInputs() {
    try {
      ArrayList<Criterion> criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("fileInput", true));

      List<Algorithm> algorithms = (List<Algorithm>) HibernateUtil
        .queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));

      criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("relationalInput", true));

      List<Algorithm> storedAlgorithms = (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));
      if (algorithms != null) {
        algorithms.addAll(storedAlgorithms);
      }

      return algorithms;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  @GET
  @Path("/algorithms-for-database-connections")
  @Produces("application/json")
  @SuppressWarnings("unchecked")
  public List<Algorithm> getAlgorithmsForDatabaseConnections() {
    try {
      ArrayList<Criterion> criteria = new ArrayList<>();
      criteria.add(Restrictions.eq("databaseConnection", true));

      return (List<Algorithm>) HibernateUtil
          .queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));
    } catch (EntityStorageException e) {
      e.printStackTrace();
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }


}
