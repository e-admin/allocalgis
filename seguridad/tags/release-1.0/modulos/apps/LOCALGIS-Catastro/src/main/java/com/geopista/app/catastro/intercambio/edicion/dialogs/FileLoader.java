package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileLoader {
	
	public static InputStream getFile(String name){
				
		InputStream inputStream = null;
	    try {
	    	inputStream = FileLoader.class.getResourceAsStream(name);	      
	      }
	    catch (Exception e) {
	      e.printStackTrace();
	      }
	    return inputStream;

	}    
	  	
}
