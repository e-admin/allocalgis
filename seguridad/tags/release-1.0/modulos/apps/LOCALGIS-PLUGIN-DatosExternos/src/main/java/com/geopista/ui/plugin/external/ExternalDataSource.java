package com.geopista.ui.plugin.external;

public class ExternalDataSource {

	private String name;
	private String driver;
	private String connectString;
	private String userName;
	private String password;
	private int id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getConnectString() {
		return connectString;
	}
	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
