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

package de.uni_potsdam.hpi.metanome.frontend.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

import java.util.List;

/**
 * Service Implementation for service that lists available algorithms
 *
 * TODO docs
 */
public class FinderServiceImpl extends RemoteServiceServlet implements
        FinderService {

    private static final long serialVersionUID = 1L;

    /**
     * TODO docs
     *
     * @param algorithmClass the subclass of algorithms to be listed, or null for all algorithms
     * @return a list of filenames (without path)
     */
    protected String[] listAlgorithmFileNames(Class<?> algorithmClass) {
        List<Algorithm> algorithms = Algorithm.retrieveAll(algorithmClass);

        String[] algorithmNames = new String[algorithms.size()];

        int algorithmIndex = 0;
        for (Algorithm algorithm : algorithms) {
            algorithmNames[algorithmIndex] = algorithm.getName();
            algorithmIndex++;
        }

        return algorithmNames;
    }

    @Override
    public String[] listInclusionDependencyAlgorithmFileNames() {
        return listAlgorithmFileNames(InclusionDependencyAlgorithm.class);
    }

    @Override
    public String[] listFunctionalDependencyAlgorithmFileNames() {
        return listAlgorithmFileNames(FunctionalDependencyAlgorithm.class);
    }

    @Override
    public String[] listUniqueColumnCombinationsAlgorithmFileNames() {
        return listAlgorithmFileNames(UniqueColumnCombinationsAlgorithm.class);
    }

    @Override
    public String[] listBasicStatisticsAlgorithmFileNames() {
        return listAlgorithmFileNames(BasicStatisticsAlgorithm.class);
    }

    @Override
    public String[] listAllAlgorithmFileNames() {
        return listAlgorithmFileNames(null);
    }

}
