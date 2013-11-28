package de.uni_potsdam.hpi.metanome.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public interface OmniscientResultReceiver extends BasicStatisticsResultReceiver, FunctionalDependencyResultReceiver, InclusionDependencyResultReceiver, UniqueColumnCombinationResultReceiver {

}
