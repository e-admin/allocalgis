/**
 * BackupPreferences.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.backup;

import java.util.prefs.Preferences;

import com.geopista.app.UserPreferenceConstants;

public class BackupPreferences {

	private static BackupPreferences preferences;
	private static final String GEOPISTA_PACKAGE = "/com/geopista/app";
	private BackupPreferences() {
		Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
		url = pref.get(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL, "");
//		user = pref.get(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS, "postgres");
//		password = pref.get(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS, "postgres");
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
