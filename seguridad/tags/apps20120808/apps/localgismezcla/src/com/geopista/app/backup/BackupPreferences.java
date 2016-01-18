package com.geopista.app.backup;

import java.util.prefs.Preferences;

public class BackupPreferences {

	private static BackupPreferences preferences;
	private static final String GEOPISTA_PACKAGE = "/com/geopista/app";
	private BackupPreferences() {
		Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
		url = pref.get("conexion.url", "");
//		user = pref.get("conexion.pass", "postgres");
//		password = pref.get("conexion.pass", "postgres");
	}
	
	public static BackupPreferences getInstance() {
		if (preferences == null) {
			return new BackupPreferences();
		}
		else {
			return preferences;
		}
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
	private String url;
	
	private String user;
	
	private String password;
	
	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
