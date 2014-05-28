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

package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A column with condition for conditional results e.g. [@Link ConditionalUniqueColumnCombination}
 *
 * @author Jens Hildebrandt
 */
public class ColumnCondition implements Comparable<ColumnCondition>, Serializable {
    protected ColumnIdentifier column;
    protected List<String> columnValues;

    /**
     * Exists for Gwt serialization
     */
    protected ColumnCondition() {
        this.column = new ColumnIdentifier();
        this.columnValues = new LinkedList<>();
    }

    /**
     * @param identifier   column of the condition
     * @param columnValues where the condition is true
     */
    public ColumnCondition(ColumnIdentifier identifier, String... columnValues) {
        this.column = identifier;
        for (String columnValue : columnValues) {
            this.columnValues.add(columnValue);
        }
    }

    /**
     * @param identifier      column of the condition
     * @param columnValueList contains values where the condition is true
     */

    public ColumnCondition(ColumnIdentifier identifier, List<String> columnValueList) {
        this.column = identifier;
        this.columnValues = columnValueList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnCondition that = (ColumnCondition) o;

        if (column != null ? !column.equals(that.column) : that.column != null) return false;
        if (columnValues != null ? !columnValues.equals(that.columnValues) : that.columnValues != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = column != null ? column.hashCode() : 0;
        result = 31 * result + (columnValues != null ? columnValues.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(ColumnCondition o) {
        //FIXME Jens implement compareTO
        return 0;
    }
}
