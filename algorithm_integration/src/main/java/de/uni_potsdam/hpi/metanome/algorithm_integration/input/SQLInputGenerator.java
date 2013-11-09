package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.sql.ResultSet;

/**
 * @author Jakob Zwiener
 * 
 * Generates new copies of {@link RelationalInput}s or {@link ResultSet}s.
 *
 */
public interface SQLInputGenerator {
	
	/**
	 * Creates a {@link RelationalInput} from an sql statement issued to a database.
	 * 
	 * @param queryString
	 * @return
	 * @throws InputGenerationException
	 */
	RelationalInput generateRelationalInputFromSql(String queryString) throws InputGenerationException;
	
	/**
	 * Creates a {@link ResultSet} from an sql statement issued to a database.
	 * 
	 * @param queryString
	 * @return
	 * @throws InputGenerationException
	 */
	ResultSet generateResultSetFromSql(String queryString) throws InputGenerationException;
}
