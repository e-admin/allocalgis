package com.geopista.app.loadEIELData.beans;

public class ConnectionInfo {

	String name;
	String connectionPath;
	String driver;
	String user;
	String password;
		
	public ConnectionInfo() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConnectionPath() {
		return connectionPath;
	}

	public void setConnectionPath(String connectionPath) {
		this.connectionPath = connectionPath;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "{" + getName() + "," + getConnectionPath() + "}";
	}	
	
}
