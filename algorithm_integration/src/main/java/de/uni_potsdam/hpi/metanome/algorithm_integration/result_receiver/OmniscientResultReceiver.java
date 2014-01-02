package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import java.io.Closeable;

public interface OmniscientResultReceiver extends
		BasicStatisticsResultReceiver, FunctionalDependencyResultReceiver,
		InclusionDependencyResultReceiver,
		UniqueColumnCombinationResultReceiver, Closeable {

}
