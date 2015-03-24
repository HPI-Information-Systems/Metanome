package de.metanome.frontend.client.services;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by Daniel on 04.03.2015.
 */
@Path("api/visualization")
public interface VisualizationRestService extends RestService{
  @GET
  @Path("/get/{executionID}")
  public void createPrefixTree(@PathParam("executionID") long executionID,
                               MethodCallback<Void> callback);
}
