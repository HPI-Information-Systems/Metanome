package de.metanome.backend.resources;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultsStoreHolder;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtUCCResult;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.ValueTooltipTuple;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCResult;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCResultsStore;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Provides database access for a GWT compatible representation of UCCResult for frontend requests
 *
 * Created by Alexander Spivak on 11.03.2015.
 */
@Path("gwtUCCResults")
public class GwtUCCResultResource {

  // Name of the store in the results store holder
  private static final String UCC_STORE_NAME = "UCC_STORE";

  /**
   * Registers a new IND store in the results store holder
   */
  static {
    ResultsStoreHolder.register(UCC_STORE_NAME, new UCCResultsStore());
  }

  /**
   * Creates a GWT compatible representation of given UCCResult
   * Not part of GwtUCCResult because I am not sure that GWT would accept the usage of column combinations here.
   *
   * @param uccResult UCCResult which should be transformed to a GWT compatible representation
   * @return Returns a GWT compatible representation of given UCCResult
   */
  private GwtUCCResult createFrom(UCCResult uccResult){
    GwtUCCResult gwtUCCResult = new GwtUCCResult();

    gwtUCCResult.setUcc(new ValueTooltipTuple(uccResult.getUccAsString(),
                                              uccResult.getUccWithColumnNames()));
    gwtUCCResult.setUccRank(uccResult.getRank());

    return gwtUCCResult;
  }

  /**
   * Creates a GWT compatible representation of given UCCResult list
   *
   * @param uccResults List of UCCResult which should be transformed to a GWT compatible representation
   * @return Returns a GWT compatible representation of given UCCResult list
   */
  private List<GwtUCCResult> createFrom(List<UCCResult> uccResults){
    List<GwtUCCResult> gwtUCCResults = new ArrayList<>(uccResults.size());

    for(UCCResult uccResult : uccResults){
      gwtUCCResults.add(createFrom(uccResult));
    }

    return gwtUCCResults;
  }

  /**
   * Persists a list of UCC results in the store
   *
   * @param uccResults List of UCC results to be persisted
   * @param executionID ID of the execution from which the UCC results are derived
   */
  public static void storeResults(List<UCCResult> uccResults, long executionID){
    ((UCCResultsStore)ResultsStoreHolder.getStore(UCC_STORE_NAME)).store(uccResults, executionID);
  }

  /**
   * Returns the list of persisted UCC results retrieved from the store
   *
   * @param executionID ID of the execution from which the UCC results are derived
   * @return Returns the list of persisted UCC results retrieved from the store for given execution
   */
  public static List<UCCResult> retrieveResults(long executionID){
    return ((UCCResultsStore)ResultsStoreHolder.getStore(UCC_STORE_NAME)).list(executionID);
  }

  /**
   * Returns the count of persisted UCC results
   *
   * @param executionID Execution ID of the last execution
   * @return Returns the count of persisted UCC results for given execution
   */
  @GET
  @Path("/count/{executionID}")
  @Produces("application/json")
  public GwtLong count(@PathParam("executionID") long executionID){
    try {
      return new GwtLong((ResultsStoreHolder.getStore(UCC_STORE_NAME)).count(executionID));
    }
    catch (Exception e){
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns all persisted UCC results
   *
   * @param executionID Execution ID of the last execution
   * @return Returns all persisted UCC results or null if the results of the wrong execution are persisted
   */
  @GET
  @Path("/getAll/{executionID}")
  @Produces("application/json")
  public List<GwtUCCResult> getAll(@PathParam("executionID") long executionID) {
    try {
      return createFrom(((UCCResultsStore)ResultsStoreHolder.getStore(UCC_STORE_NAME)).list(
          executionID));
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns a sublist of persisted UCC results sorted in given way
   *
   * @param executionID Execution ID of the last execution
   * @param sortProperty Name of the sort property
   * @param ascending Should the sort be performed in ascending or descending manner?
   * @param start Inclusive start index
   * @param end Exclusive end index
   * @return Returns a sublist of persisted UCC results sorted in given way or null if the results of the wrong execution are persisted
   */
  @GET
  @Path("/getAllFromTo/{executionID}/{sortProperty}/{sortOrder}/{start}/{end}")
  @Produces("application/json")
  public List<GwtUCCResult> getAll(@PathParam("executionID") long executionID,
                                   @PathParam("sortProperty") String sortProperty,
                                   @PathParam("sortOrder") boolean ascending,
                                   @PathParam("start") int start,
                                   @PathParam("end") int end) {
    try {
      return createFrom(((UCCResultsStore)ResultsStoreHolder.getStore(UCC_STORE_NAME)).subList(
          executionID, sortProperty, ascending, start, end));
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
}
