package com.geopista.ui.feature;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;

public class AttributeFilter {

	private HashSet attributesToFilter;
	
	private static AttributeFilter instance = null;
	
	public static AttributeFilter getInstance(){
		if (instance != null){
			return instance;
		}
		
		instance = new AttributeFilter();
		return instance;
	}

	private AttributeFilter(){
		attributesToFilter = new HashSet();
		
		InputStream attributesToFilterStream =
			this.getClass().getResourceAsStream("/com/geopista/ui/feature/filteredAttributes.ini");

		byte[] buffer = new byte[1014];
		int numBytes;
		StringBuffer filterStringBuffer = new StringBuffer();
		
		try {
			numBytes = attributesToFilterStream.read(buffer);
			while (numBytes != -1){				
				filterStringBuffer.append(new String(buffer, 0, numBytes));
				numBytes = attributesToFilterStream.read(buffer);
			}
			
			String filter = filterStringBuffer.toString();			
			String[] filterLines = filter.split("\\n");
			for (int i = 0; i < filterLines.length; i++){
				if (filterLines[i].indexOf('\r') != -1){
					filterLines[i] =
						filterLines[i].substring(0, filterLines[i].indexOf('\r'));
				}
				if (!filterLines[i].startsWith("#")){
					attributesToFilter.add(filterLines[i]);
				}
			}
		} catch (Exception e){
			// Se deja vacia la lista de campos
			attributesToFilter = new HashSet();
		}
	}

	public boolean isBlackListed(String fieldName){
		
		if (attributesToFilter.contains(fieldName)){
			return true;
		}
		
//		if (fieldName.indexOf("id") != -1 || fieldName.indexOf("FID") != -1){
//			return true;
//		}
		return false;
	}

}
