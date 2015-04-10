package de.metanome.frontend.client.services;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtUCCResult;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Provides an RestService interface to access GwtUCCResultResource methods
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
@Path("/api/gwtUCCResults")
public interface GwtUCCResultRestService extends RestService {

  @GET
  @Path("/getAll/{executionID}")
  public void listGwtUCCResults(@PathParam("executionID") long executionID,
                                MethodCallback<List<GwtUCCResult>> callback);

  @GET
  @Path("/getAllFromTo/{executionID}/{sortProperty}/{sortOrder}/{start}/{end}")
  public void listGwtUCCResults(@PathParam("executionID") long executionID,
                                @PathParam("sortProperty") String sortProperty,
                                @PathParam("sortOrder") boolean ascending,
                                @PathParam("start") int start,
                                @PathParam("end") int end,
                                MethodCallback<List<GwtUCCResult>> callback);

  @GET
  @Path("/count/{executionID}")
  public void count(@PathParam("executionID") long executionID,
                    MethodCallback<GwtLong> callback);

}
