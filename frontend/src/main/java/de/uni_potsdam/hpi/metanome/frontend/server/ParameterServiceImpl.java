package de.uni_potsdam.hpi.metanome.frontend.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmJarLoader;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;

/**
 * Service Implementation that provides functionality for retrieving and setting parameters on 
 * algorithms.
 */
public class ParameterServiceImpl extends RemoteServiceServlet implements ParameterService {

	private static final long serialVersionUID = 1L;
	
	private AlgorithmJarLoader<InclusionDependencyAlgorithm> inclusionDependencyJarLoader = 
			new AlgorithmJarLoader<InclusionDependencyAlgorithm>(InclusionDependencyAlgorithm.class);
	private AlgorithmJarLoader<FunctionalDependencyAlgorithm> functionalDependencyJarLoader = 
			new AlgorithmJarLoader<FunctionalDependencyAlgorithm>(FunctionalDependencyAlgorithm.class);
	private AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm> uniqueColumnCombinationsJarLoader = 
			new AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm>(UniqueColumnCombinationsAlgorithm.class);

	/**
	 * 
	 * @param algorithmFileName	name of the algorithm for which the configuration parameters shall be 
	 * 							retrieved
	 * @param jarLoader			an <link>AlgorithmJarLoader</link> instance for the correct algorithm 
	 * 							type 
	 * @return	a list of <link>InputParameter</link>s necessary for calling the given algorithm
	 */
	private List<InputParameter> retrieveParameters(String algorithmFileName, AlgorithmJarLoader<?> jarLoader){
//		Algorithm algorithm = null;
//		try {
//			algorithm = jarLoader.loadAlgorithm(algorithmFileName);
//		} catch (Exception e) {
			System.out.println("FAILED to LOAD algorithm");
			// TODO error handling, then remove default answer
			ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
			paramList.add(new InputParameterString("tableName" + algorithmFileName.substring(0,5)));
			paramList.add(new InputParameterBoolean("showProgress"));
			return paramList;
//		}
//		
//		List<ConfigurationSpecification> configList = algorithm.getConfigurationRequirements();
//		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
//		
//		for (ConfigurationSpecification config : configList){
//			paramList.add(getInputParameterInstance(config));
//		}
//		
//		return paramList;
	}
	
	/**
	 * maps the <link>ConfigurationSpecification</link> subclass to the corresponding Type enum
	 * element
	 * 
	 * @param config	the ConfigurationSpecification object to be mapped
	 * @return the Type enum 
	 */
	private InputParameter getInputParameterInstance(ConfigurationSpecification config) {
		if (config instanceof ConfigurationSpecificationString) {
			return new InputParameterString(config.getIdentifier());
		} else if (config instanceof ConfigurationSpecificationBoolean) {
			return new InputParameterBoolean(config.getIdentifier());
		} else {
			return null;
		}
	}

	@Override
	public List<InputParameter> retrieveInclusionDependencyParameters(
			String algorithmFileName) {
		return retrieveParameters(algorithmFileName, inclusionDependencyJarLoader);
	}

	@Override
	public List<InputParameter> retrieveFunctionalDependencyParameters(
			String algorithmFileName) {
		return retrieveParameters(algorithmFileName, functionalDependencyJarLoader);
	}

	@Override
	public List<InputParameter> retrieveUniqueColumnCombinationsParameters(
			String algorithmFileName) {
		return retrieveParameters(algorithmFileName, uniqueColumnCombinationsJarLoader);
	}

}
