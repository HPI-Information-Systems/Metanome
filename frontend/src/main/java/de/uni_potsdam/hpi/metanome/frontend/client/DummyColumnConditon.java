package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionAnd;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionOr;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;

/**
 * Created by Jens on 18.08.2014.
 */
public class DummyColumnConditon implements IsSerializable {

  private ColumnCondition conditon;
  private ColumnConditionValue conditonValue;
  private ColumnConditionOr conditonOr;
  private ColumnConditionAnd conditonAnd;
  private ConditionalUniqueColumnCombination combination;


}
