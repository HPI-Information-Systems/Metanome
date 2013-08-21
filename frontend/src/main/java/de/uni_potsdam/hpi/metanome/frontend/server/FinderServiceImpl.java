package de.uni_potsdam.hpi.metanome.frontend.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_integration.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmFinder;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;

/**
 * 
 */
public class FinderServiceImpl extends RemoteServiceServlet implements
		FinderService {

	private static final long serialVersionUID = 1L;
	
	AlgorithmFinder algorithmFinder = new AlgorithmFinder();

	public String[] listAlgorithms(Class<?> algorithmClass) {
		String[] algorithms = null;
		try {
			algorithms = algorithmFinder.getAvailableAlgorithms("pathToFolder?", algorithmClass);
		} catch (Exception e){
			//TODO: error handling
		}
		return algorithms;
	}

	@Override
	public String[] listInclusionDependencyAlgorithms() {
		return listAlgorithms(InclusionDependencyAlgorithm.class);
	}

	@Override
	public String[] listFunctionalDependencyAlgorithms() {
		return listAlgorithms(FunctionalDependencyAlgorithm.class);
	}

	@Override
	public String[] listUniqueColumnCombinationsAlgorithms() {
		return listAlgorithms(UniqueColumnCombinationsAlgorithm.class);
	}

}
