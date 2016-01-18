package com.geopista.server.catastro.intercambio.importacion.utils.schemasxsd;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileLoaderServerCatastro {
	
	public static InputStream getFile(String name){
				
		InputStream inputStream = null;
	    try {
	    	inputStream = FileLoaderServerCatastro.class.getResourceAsStream(name);	      
	      }
	    catch (Exception e) {
	      e.printStackTrace();
	      }
	    return inputStream;

	}    
	  	
}
