package de.metanome.backend.resources;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultsStoreHolder;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtINDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.ValueTooltipTuple;
import de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies.INDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies.INDResultsStore;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Provides database access for a GWT compatible representation of INDResult for frontend requests
 *
 * Created by Alexander Spivak on 05.03.2015.
 */
@Path("gwtINDResults")
public class GwtINDResultResource {

  // Name of the store in the results store holder
  private static final String IND_STORE_NAME = "IND_STORE";

  /**
   * Registers a new IND store in the results store holder
   */
  static {
    ResultsStoreHolder.register(IND_STORE_NAME, new INDResultsStore());
  }

  /**
   * Creates a GWT compatible representation of given INDResult
   * Not part of GwtINDResult because I am not sure that GWT would accept the usage of column combinations here.
   *
   * @param indResult INDResult which should be transformed to a GWT compatible representation
   * @return Returns a GWT compatible representation of given INDResult
   */
  private GwtINDResult createFrom(INDResult indResult){
    GwtINDResult gwtINDResult = new GwtINDResult();

    gwtINDResult.setIndRank(indResult.getRank());
    gwtINDResult.setDependant(new ValueTooltipTuple(indResult.getDependantAsString(),
                                                    indResult.getDependantWithColumnNames()));
    gwtINDResult.setReferenced(new ValueTooltipTuple(indResult.getReferencedAsString(),
                                                     indResult.getReferencedWithColumnNames()));

    return gwtINDResult;
  }

  /**
   * Creates a GWT compatible representation of given INDResult list
   *
   * @param indResults List of INDResult which should be transformed to a GWT compatible representation
   * @return Returns a GWT compatible representation of given INDResult list
   */
  private List<GwtINDResult> createFrom(List<INDResult> indResults){
    List<GwtINDResult> gwtINDResults = new ArrayList<>(indResults.size());

    for(INDResult indResult : indResults){
      gwtINDResults.add(createFrom(indResult));
    }

    return gwtINDResults;
  }

  /**
   * Persists a list of IND results in the store
   *
   * @param indResults List of IND results to be persisted
   * @param executionID ID of the execution from which the IND results are derived
   */
  public static void storeResults(List<INDResult> indResults, long executionID){
    ((INDResultsStore)ResultsStoreHolder.getStore(IND_STORE_NAME)).store(indResults, executionID);
  }

  /**
   * Returns the list of persisted IND results retrieved from the store
   *
   * @param executionID ID of the execution from which the IND results are derived
   * @return Returns the list of persisted IND results retrieved from the store for given execution
   */
  public static List<INDResult> retrieveResults(long executionID){
    return ((INDResultsStore)ResultsStoreHolder.getStore(IND_STORE_NAME)).list(executionID);
  }

  /**
   * Returns the count of persisted IND results
   *
   * @param executionID Execution ID of the last execution
   * @return Returns the count of persisted IND results for given execution
   */
  @GET
  @Path("/count/{executionID}")
  @Produces("application/json")
  public GwtLong count(@PathParam("executionID") long executionID){
    try {
      return new GwtLong((ResultsStoreHolder.getStore(IND_STORE_NAME)).count(
          executionID));
    }
    catch (Exception e){
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns all persisted IND results
   *
   * @param executionID Execution ID of the last execution
   * @return Returns all persisted IND results or null if the results of the wrong execution are persisted
   */
  @GET
  @Path("/getAll/{executionID}")
  @Produces("application/json")
  public List<GwtINDResult> getAll(@PathParam("executionID") long executionID) {
    try {
      return createFrom(((INDResultsStore)ResultsStoreHolder.getStore(IND_STORE_NAME)).list(
          executionID));
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Returns a sublist of persisted IND results sorted in given way
   *
   * @param executionID Execution ID of the last execution
   * @param sortProperty Name of the sort property
   * @param ascending Should the sort be performed in ascending or descending manner?
   * @param start Inclusive start index
   * @param end Exclusive end index
   * @return Returns a sublist of persisted IND results sorted in given way or null if the results of the wrong execution are persisted
   */
  @GET
  @Path("/getAllFromTo/{executionID}/{sortProperty}/{sortOrder}/{start}/{end}")
  @Produces("application/json")
  public List<GwtINDResult> getAll(@PathParam("executionID") long executionID,
                                   @PathParam("sortProperty") String sortProperty,
                                   @PathParam("sortOrder") boolean ascending,
                                   @PathParam("start") int start,
                                   @PathParam("end") int end) {
    try {
      return createFrom(((INDResultsStore)ResultsStoreHolder.getStore(IND_STORE_NAME)).subList(
          executionID, sortProperty, ascending, start, end));
    } catch (Exception e) {
      throw new WebException(e, Response.Status.BAD_REQUEST);
    }
  }
}
