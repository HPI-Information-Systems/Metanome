package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionAnd;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionOr;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;

/**
 * @author Jens Ehrlich
 *
 * This class is a dummy class which serves the sole purpose of whitelisting it's member classes for GWT serialization.
 * Corresponding methods that use this class are required in {@link de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService}, {@link de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync}, and {@link de.uni_potsdam.hpi.metanome.frontend.server.ExecutionServiceImpl}.
 */
public class DummyColumnConditon implements IsSerializable {

  private ColumnCondition conditon;
  private ColumnConditionValue conditonValue;
  private ColumnConditionOr conditonOr;
  private ColumnConditionAnd conditonAnd;
  private ConditionalUniqueColumnCombination combination;
}
