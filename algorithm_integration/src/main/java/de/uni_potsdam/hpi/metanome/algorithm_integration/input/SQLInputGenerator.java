package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.sql.ResultSet;

public interface SQLInputGenerator {
	
	/**
	 * Creates a {@link SimpleRelationalInput} from an sql statement issued to a database.
	 * 
	 * @param queryString
	 * @return
	 * @throws InputGenerationException
	 */
	SimpleRelationalInput generateSimpleRelationalInputFromSql(String queryString) throws InputGenerationException;
	
	/**
	 * Creates a {@link ResultSet} from an sql statement issued to a database.
	 * 
	 * @param queryString
	 * @return
	 * @throws InputGenerationException
	 */
	ResultSet generateResultSetFromSql(String queryString) throws InputGenerationException;
}
