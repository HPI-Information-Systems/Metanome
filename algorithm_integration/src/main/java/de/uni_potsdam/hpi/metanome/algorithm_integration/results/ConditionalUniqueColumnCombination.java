/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import java.util.List;

/**
 * Represents a conditional unique column combination
 *
 * @author Jens Hildebrandt
 */
public class ConditionalUniqueColumnCombination implements Result {

//    private static final long serialVersionUID = 6744030409666817339L;

    protected ColumnCombination columnCombination;
    protected List<ColumnCondition> conditionList;


    /**
     * Exists for GWT serialization.
     */
    protected ConditionalUniqueColumnCombination() {
        this.columnCombination = new ColumnCombination();
    }

    /**
     * Constructs a {@link UniqueColumnCombination} from a number of {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier}s.
     *
     * @param columnIdentifier the column identifier comprising the column combination
     */
    public ConditionalUniqueColumnCombination(ColumnIdentifier... columnIdentifier) {
        this.columnCombination = new ColumnCombination(columnIdentifier);
    }

    /**
     * Constructs a {@link UniqueColumnCombination} from a {@link ColumnCombination}.
     *
     * @param columnCombination a supposedly unique column combination
     */
    public ConditionalUniqueColumnCombination(ColumnCombination columnCombination) {
        this.columnCombination = columnCombination;
    }

    @Override
    public void sendResultTo(OmniscientResultReceiver resultReceiver) throws CouldNotReceiveResultException {
        resultReceiver.receiveResult(this);
    }

    /**
     * @return the column combination
     */
    public ColumnCombination getColumnCombination() {
        return columnCombination;
    }

    @Override
    public String toString() {
        return columnCombination.toString();
    }
}
