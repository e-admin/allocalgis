package com.geopista.app.version;

import java.io.IOException;
import java.util.Properties;

public class Release {

	public static final String FICHERO_PROPERTIES = "/com/geopista/app/version/release.properties";
	Properties properties = new Properties();
	
	public String getRelease(){
	
		cogeProperties();
		String release=(String)properties.get("release");
		System.out.println("release:"+release);
		return release;		
	}
	
	public void cogeProperties() {
		
		try {
			properties.load(this.getClass().getResourceAsStream(FICHERO_PROPERTIES));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
