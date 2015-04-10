package de.metanome.backend.resources;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies.INDResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCResultAnalyzer;
import de.metanome.backend.results_db.Execution;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Backend connection to perform result analysis on execution results
 *
 * Created by Alexander Spivak on 28.02.2015.
 */
@Path("result_analyzer_execution")
public class ResultAnalyzerExecutorResource {

  @POST
  @Path("/data_dependent")
  @Consumes("application/json")
  @Produces("application/json")
  public void dataDependentAnalysis(GwtLong executionID){
    resultAnalysis(executionID.getValue(), true);
  }

  @POST
  @Path("/data_independent")
  @Consumes("application/json")
  @Produces("application/json")
  public void dataIndependentAnalysis(GwtLong executionID){
    resultAnalysis(executionID.getValue(), false);
  }

  private void resultAnalysis(long executionID, boolean useData){
    ExecutionResource executionResource = new ExecutionResource();
    Execution execution = executionResource.get(executionID);

    if(execution.getAlgorithm().isFd()){
      (new FDResultAnalyzer()).analyzeResults(execution, useData);
    }
    if(execution.getAlgorithm().isUcc()){
      (new UCCResultAnalyzer()).analyzeResults(execution, useData);
    }
    if(execution.getAlgorithm().isInd()){
      (new INDResultAnalyzer()).analyzeResults(execution, useData);
    }
  }

}
