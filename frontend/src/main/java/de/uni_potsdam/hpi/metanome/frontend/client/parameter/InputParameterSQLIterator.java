package de.uni_potsdam.hpi.metanome.frontend.client.parameter;


/**
 * Client compatible parameter for a requested SQLInputGenerator
 */
public class InputParameterSQLIterator extends InputParameter {

	private static final long serialVersionUID = -8258047430500209172L;

	private String dbUrl;
	private String userName;
	private String password;
	
	/** 
	 * Default no-argument constructor. 
	 * Use <link>InputParameterSQLIterator(String identifier) instead</link> 
	 **/
	public InputParameterSQLIterator(){
		super();
	}
	
	public InputParameterSQLIterator(String identifier) {
		super(identifier);
	}

	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl){
		this.dbUrl = dbUrl;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	
	@Override
	public InputParameterWidget createWrappingWidget() {
		return new InputParameterSQLIteratorWidget(this);
	}

}
