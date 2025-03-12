package de.metanome.cli;

import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.*;
import de.metanome.backend.result_receiver.ResultReceiver;

import java.io.FileNotFoundException;

public class DiscardingResultReceiver extends ResultReceiver {

    public DiscardingResultReceiver() throws FileNotFoundException {
        super("discarded-execution", null);
    }

    @Override
    public void receiveResult(BasicStatistic statistic) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(FunctionalDependency functionalDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(InclusionDependency inclusionDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(MultivaluedDependency multivaluedDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(OrderDependency orderDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(UniqueColumnCombination uniqueColumnCombination) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(DenialConstraint denialConstraint) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(MatchingDependency matchingDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(ConditionalFunctionalDependency conditionalFunctionalDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(ConditionalInclusionDependency conditionalDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(RelaxedFunctionalDependency relaxedFunctionalDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(RelaxedInclusionDependency relaxedInclusionDependency) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void receiveResult(RelaxedUniqueColumnCombination relaxedUniqueColumnCombination) throws CouldNotReceiveResultException, ColumnNameMismatchException {

    }

    @Override
    public void close() {

    }

}
