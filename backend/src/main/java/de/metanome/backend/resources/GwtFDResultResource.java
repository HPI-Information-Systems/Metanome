package de.metanome.backend.resources;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultsStoreHolder;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtFDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.ValueTooltipTuple;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResultsStore;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Provides database access for a GWT compatible representation of FDResult for frontend requests
 *
 * Created by Alexander Spivak on 05.03.2015.
 */
@Path("gwtFDResults")
public class GwtFDResultResource {

  // Name of the store in the results store holder
  private static final String FD_STORE_NAME = "FD_STORE";

  /**
   * Registers a new FD store in the results store holder
   */
  static {
    ResultsStoreHolder.register(FD_STORE_NAME, new FDResultsStore());
  }

  /**
   * Creates a GWT compatible representation of given FDResult
   * Not part of GwtFDResult because I am not sure that GWT would accept the usage of column combinations here.
   *
   * @param fdResult FDResult which should be transformed to a GWT compatible representation
   * @return Returns a GWT compatible representation of given FDResult
   */
  private GwtFDResult createFrom(FDResult fdResult){
    GwtFDResult gwtFDResult = new GwtFDResult();
    gwtFDResult.setFdRank(fdResult.getRank());
    gwtFDResult.setDeterminant(new ValueTooltipTuple(fdResult.getDeterminantAsString(),
                                                     fdResult.getDeterminantWithColumnNames()));
    gwtFDResult.setDependant(new ValueTooltipTuple(fdResult.getDependantAsString(),
                                                   fdResult.getDependantWithColumnNames()));
    gwtFDResult.setAdditionalColumns(new ValueTooltipTuple(fdResult.getAdditionalColumnsAsString(),
                                                           fdResult.getAdditionalColumnsWithColumnNames()));
    return gwtFDResult;
  }

  /**
   * Creates a GWT compatible representation of given FDResult list
   *
   * @param fdResults List of FDResult which should be transformed to a GWT compatible representation
   * @return Returns a GWT compatible representation of given FDResult list
   */
  private List<GwtFDResult> createFrom(List<FDResult> fdResults){
    List<GwtFDResult> gwtFDResults = new ArrayList<>(fdResults.size());

    for(FDResult fdResult : fdResults){
      gwtFDResults.add(createFrom(fdResult));
    }

    return gwtFDResults;
  }

  /**
   * Persists a list of FD results in the store
   *
   * @param fdResults List of FD results to be persisted
   * @param executionID ID of the execution from which the FD results are derived
   */
  public static void storeResults(List<FDResult> fdResults, long executionID){
    ((FDResultsStore)ResultsStoreHolder.getStore(FD_STORE_NAME)).store(fdResults, executionID);
  }

  /**
   * Returns the list of persisted FD results retrieved from the store
   *
   * @param executionID ID of the execution from which the FD results are derived
   * @return Returns the list of persisted FD results retrieved from the store for given execution
   */
  public static List<FDResult> retrieveResults(long executionID){
    return ((FDResultsStore)ResultsStoreHolder.getStore(FD_STORE_NAME)).list(executionID);
  }

  /**
   * Returns the count of persisted FD results
   *
   * @param executionID Execution ID of the last execution
   * @return Returns the count of persisted FD results for given execution
   */
  @GET
  @Path("/count/{executionID}")
  @Produces("application/json")
  public GwtLong count(@PathParam("executionID") long executionID){
    try {
      return new GwtLong((ResultsStoreHolder.getStore(FD_STORE_NAME)).count(
          executionID));
    }
    catch (Exception e){
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns all persisted FD results
   *
   * @param executionID Execution ID of the last execution
   * @return Returns all persisted FD results or null if the results of the wrong execution are persisted
   */
  @GET
  @Path("/getAll/{executionID}")
  @Produces("application/json")
  public List<GwtFDResult> getAll(@PathParam("executionID") long executionID) {
    try {
      return createFrom(((FDResultsStore)ResultsStoreHolder.getStore(FD_STORE_NAME)).list(
          executionID));
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns a sublist of persisted FD results sorted in given way
   *
   * @param executionID Execution ID of the last execution
   * @param sortProperty Name of the sort property
   * @param ascending Should the sort be performed in ascending or descending manner?
   * @param start Inclusive start index
   * @param end Exclusive end index
   * @return Returns a sublist of persisted FD results sorted in given way or null if the results of the wrong execution are persisted
   */
  @GET
  @Path("/getAllFromTo/{executionID}/{sortProperty}/{sortOrder}/{start}/{end}")
  @Produces("application/json")
  public List<GwtFDResult> getAll(@PathParam("executionID") long executionID,
                                  @PathParam("sortProperty") String sortProperty,
                                  @PathParam("sortOrder") boolean ascending,
                                  @PathParam("start") int start,
                                  @PathParam("end") int end) {
    try {
      return createFrom(((FDResultsStore)ResultsStoreHolder.getStore(FD_STORE_NAME)).subList(
          executionID, sortProperty, ascending, start, end));
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
}
