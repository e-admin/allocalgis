package com.geopista.app.backup.ui;

public class I18N {

	private static I18N i18n; 
	
	public static I18N getInstance() {
		// TODO Auto-generated method stub
		if(i18n==null) {
			i18n = new I18N();
		}
		return i18n;
	}
	
	private I18N() {
		
	}
	
	public String getExternalizadedString(String key) {
		return key;
	}

}