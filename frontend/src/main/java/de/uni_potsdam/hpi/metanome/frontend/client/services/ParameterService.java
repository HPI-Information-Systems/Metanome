package de.uni_potsdam.hpi.metanome.frontend.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;

@RemoteServiceRelativePath("parameterService")
public interface ParameterService extends RemoteService {

	public List<InputParameter> retrieveInclusionDependencyParameters(String selectedValue);
	public List<InputParameter> retrieveFunctionalDependencyParameters(String selectedValue);
	public List<InputParameter> retrieveUniqueColumnCombinationsParameters(String selectedValue);
	public List<InputParameter> retrieveBasicStatisticsParameters(String selectedValue);
	
}
