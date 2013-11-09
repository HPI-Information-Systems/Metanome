package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterWidget;

public class InputParameterSQLIterator extends InputParameter {

	private static final long serialVersionUID = -8258047430500209172L;

	private String dbUrl;
	private String userName;
	private String password;
	
	/** Default no-argument constructor. 
	 * Use <link>InputParameterSQLIterator(String identifier) instead</link> **/
	public InputParameterSQLIterator(){
		super();
	}
	
	public InputParameterSQLIterator(String identifier) {
		super(identifier);
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public InputParameterWidget createWrappingWidget() {
		// TODO Auto-generated method stub
		return null;
	}

}
