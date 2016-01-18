/**
 * GeopistaSchema.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

import java.util.HashMap;
import java.util.Set;

public class GeopistaSchema {

	private HashMap attributes=new HashMap();
	public static final String READ_ONLY="R";
	public static final String READ_WRITE="R/W";
	public static final String NO_ACCESS = "NA";

	public GeopistaSchema() {
	}

	/*
	private String  convert2UTF8(String val) {
		try {
			byte[] bytesRaw = val.getBytes("UTF-8");
			String val2 = new String (bytesRaw);
			return val2;
		} catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
			 return val;
		}
		
	}
	*/
	/**
	 * @param attName
	 * @return
	 */
	public Domain getAttributeDomain(String attName) {
		
		//System.out.println("****** pido atributo " + attName + " bytes->" + toHexString( attName.getBytes()));
		//String nameUTF8 = convert2UTF8(attName);
	//	System.out.println("en utf 8->" + toHexString(nameUTF8.getBytes()));
		return (Domain) ((Attribute)attributes.get(attName)).getColumn().getDomain();
	}

	/**
	 * @param attName
	 * @return
	 */
	public Column getColumnByAttribute(String attName) {
		//String nameUTF8 = convert2UTF8(attName);
	 
		
		return ((Attribute) attributes.get(attName)).getColumn();
	}

	public void addAttribute(Attribute attribute) {
		attribute.setSchema(this);

		attributes.put( (attribute.getName()), attribute);
		
		/*System.out.println("****** meto atributo " + attribute.getName() +
							" hashcode ->" + attribute.getName().hashCode() +
							" bytes->" + toHexString( attribute.getName().getBytes()));
		*/
		 
	}

	public String getAttributeAccess(String attName) {
		//String nameUTF8 = convert2UTF8(attName);
		 
		return ((Attribute) attributes.get(attName)).getAccessType();
	}
	
	public Attribute getAttribute(String attName) {
		 
		 /*System.out.println("****** pido atributo " + attName +
						" hashcode ->" + attName.hashCode()
						+ " bytes->" + toHexString(attName.getBytes()));
		*/
		return (Attribute) attributes.get( attName);
	}
	
	public Set getAttributeKeys() {
		return attributes.keySet();
	}
	
	public static String toHexString(byte[] block) {
	    StringBuffer buf = new StringBuffer();
	    char[] hexChars = { 
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	        'A', 'B', 'C', 'D', 'E', 'F' };
	    int len = block.length;
	    int high = 0;
	    int low = 0;
	    for (int i = 0; i < len; i++) {
	        high = ((block[i] & 0xf0) >> 4);
	        low = (block[i] & 0x0f);
	        buf.append(hexChars[high]);
	        buf.append(hexChars[low]);
	    } 
	    return buf.toString();
	}
	
	
	
}
