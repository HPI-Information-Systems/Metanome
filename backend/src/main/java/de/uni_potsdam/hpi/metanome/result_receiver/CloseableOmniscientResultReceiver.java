package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.Closeable;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

public interface CloseableOmniscientResultReceiver extends OmniscientResultReceiver, Closeable {

}
