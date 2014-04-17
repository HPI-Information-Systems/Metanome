package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;


public class ConfigurationSettingSQLIterator extends ConfigurationSettingDataSource {
	private static final long serialVersionUID = 3242593091096735218L;

	private String dbUrl;
	private String username;
	private String password;
	
	public ConfigurationSettingSQLIterator() {}
	
	public ConfigurationSettingSQLIterator(String dbUrl, String username, String password) {
		this.dbUrl = dbUrl;
		this.username = username;
		this.password = password;
	}
	
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getValueAsString() {
		return this.dbUrl + ";" + this.username + ";" + this.password;
	}
}
