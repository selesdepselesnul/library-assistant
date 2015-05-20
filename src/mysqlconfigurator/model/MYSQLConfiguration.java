package mysqlconfigurator.model;

import java.io.Serializable;

public class MYSQLConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String port;
	private String hostname;

	public MYSQLConfiguration(String username, String password, String port,
			String hostname) {
		this.username = username;
		this.password = password;
		this.port = port;
		this.hostname = hostname;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	

}
