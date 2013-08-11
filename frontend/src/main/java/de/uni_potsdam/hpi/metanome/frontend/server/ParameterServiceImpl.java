package de.uni_potsdam.hpi.metanome.frontend.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmJarLoader;
import de.uni_potsdam.hpi.metanome.frontend.client.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.InputParameter.Type;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;

/**
 * Service Implementation that provides functionality for retrieving and setting parameters on 
 * algorithms.
 */
public class ParameterServiceImpl extends RemoteServiceServlet implements ParameterService {

	private static final long serialVersionUID = 1L;

	public List<InputParameter> retrieveParameters(String algorithmSubclass, String algorithmFileName){
		Algorithm algorithm = null;
		try {
			//TODO correctly load algorithm
			//algorithm = new AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm>(UniqueColumnCombinationsAlgorithm.class).loadAlgorithm(algorithmFileName);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO error handling
		}
		
		// List<ConfigurationSpecification> configList = algorithm.getConfigurationRequirements();
		
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		//for (ConfigurationSpecification config : configList){
			//TODO get correct parameter type
			//paramList.add(new InputParameter(config.getIdentifier(), Type.INT));
		//}
		
		return paramList;
	}
}
