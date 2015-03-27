package de.metanome.backend.resources;

import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCAnalyzer;
import de.metanome.backend.results_db.Execution;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by Daniel on 04.03.2015.
 */

@Path("visualization")
public class VisualizationResource {
  @GET
  @Path("/get/{executionID}")
  public void getPrefixTree(@PathParam("executionID") long executionID){
    ExecutionResource er = new ExecutionResource();
    Execution ex = er.get(executionID);
    FDResultAnalyzer analyzer = new FDResultAnalyzer();
    analyzer.createPrefixTree(ex);
  }

  @GET
  @Path("/getucc/{executionID}")
  public void getUCCDiagrams(@PathParam("executionID") long executionID){
      ExecutionResource er = new ExecutionResource();
      Execution ex = er.get(executionID);
      UCCAnalyzer analyzer = new UCCAnalyzer();
      analyzer.createUCCDiagrams(ex);
    }

}
