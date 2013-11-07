package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;

public class CouldNotReceiveResultException extends AlgorithmExecutionException {

	private static final long serialVersionUID = -5581062620291673939L;

	public CouldNotReceiveResultException(String message) {
		super(message);
	}

}
