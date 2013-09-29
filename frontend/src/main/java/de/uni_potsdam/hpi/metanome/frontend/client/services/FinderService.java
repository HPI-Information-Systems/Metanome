package de.uni_potsdam.hpi.metanome.frontend.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("finderService")
public interface FinderService extends RemoteService {

	public String[] listInclusionDependencyAlgorithms();
	public String[] listFunctionalDependencyAlgorithms();
	public String[] listUniqueColumnCombinationsAlgorithms();
	public String[] listBasicStatisticsAlgorithms();
}
