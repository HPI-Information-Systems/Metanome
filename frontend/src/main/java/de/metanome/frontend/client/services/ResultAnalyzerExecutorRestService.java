package de.metanome.frontend.client.services;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Service providing methods to compute data dependent and independent ranking analysis
 *
 * Created by Alexander Spivak on 28.02.2015.
 */
@Path("/api/result_analyzer_execution")
public interface ResultAnalyzerExecutorRestService extends RestService {

  @POST
  @Path("/data_dependent")
  public void dataDependentAnalysis(GwtLong executionID, MethodCallback<Void> callback);

  @POST
  @Path("/data_independent")
  public void dataIndependentAnalysis(GwtLong executionID, MethodCallback<Void> callback);
}
