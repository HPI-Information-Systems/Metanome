/**
 * Copyright 2015-2025 by Metanome Project
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
package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.PartialFunctionalDependency;
import de.metanome.backend.result_postprocessing.result_ranking.PartialFunctionalDependencyRanking;
import de.metanome.backend.result_postprocessing.results.PartialFunctionalDependencyResult;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Analyzes Partial Functional Dependency Results.
 */
public class PartialFunctionalDependencyResultAnalyzer
        extends ResultAnalyzer<PartialFunctionalDependency, PartialFunctionalDependencyResult> {

    public PartialFunctionalDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
                                                         boolean useDataIndependentStatistics)
            throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
        super(inputGenerators, useDataIndependentStatistics);
    }

    @Override
    protected List<PartialFunctionalDependencyResult> analyzeResultsDataIndependent(
            List<PartialFunctionalDependency> prevResults) {
        List<PartialFunctionalDependencyResult> results = convertResults(prevResults);
        return results;
    }

    @Override
    protected List<PartialFunctionalDependencyResult> analyzeResultsDataDependent(
            List<PartialFunctionalDependency> prevResults) {
        List<PartialFunctionalDependencyResult> results = convertResults(prevResults);

        try {
            if (!this.tableInformationMap.isEmpty()) {
                PartialFunctionalDependencyRanking ranking =
                        new PartialFunctionalDependencyRanking(results, tableInformationMap);
                ranking.calculateDataDependentRankings();
            }
        } catch (Exception e) {
            // Could not analyze results due to error
        }

        return results;
    }

    @Override
    public List<PartialFunctionalDependencyResult> convertResults(
            List<PartialFunctionalDependency> prevResults) {
        List<PartialFunctionalDependencyResult> results = new ArrayList<>();

        for (PartialFunctionalDependency prevResult : prevResults) {
            PartialFunctionalDependencyResult result = new PartialFunctionalDependencyResult(prevResult);
            results.add(result);
        }

        return results;
    }

    /**
     * Gets the bit set of the dependant column.
     *
     * @param result the result
     * @return bit set of the dependant column.
     */
    protected BitSet getDependantBitSet(PartialFunctionalDependencyResult result) {
        String tableName = result.getDependantTableName();
        String columnName = result.getDependant().getColumnIdentifier();
        try {
            return this.tableInformationMap.get(tableName).getColumnInformationMap().get(columnName)
                    .getBitSet();
        } catch (Exception e) {
            return new BitSet();
        }
    }

    /**
     * Gets the bit set of the determinant column.
     *
     *
     * @param result the result
     * @return bit set of the determinant column.
     */
    protected BitSet getDeterminantBitSet(PartialFunctionalDependencyResult result) {
        try {
            BitSet bitSet = new BitSet();
            String tableName = result.getDeterminantTableName();
            for (ColumnIdentifier column : result.getDeterminant().getColumnIdentifiers()) {
                bitSet.or(this.tableInformationMap.get(tableName).getColumnInformationMap()
                        .get(column.getColumnIdentifier()).getBitSet());
            }
            return bitSet;
        } catch (Exception e) {
            return new BitSet();
        }
    }

}
