/**
 * Utils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg.util;

public class Utils {	
	
	/**
	 * Check if the string is of type alphanumeric
	 * 
	 * @param String s: value to evaluate
	 * @param int maxLength: maximal length of the alphanumerical parameter
	 *       
	 * 
	 * @return true, false.
	 */
	public static boolean isAlphaNumeric (final String s) {
		final char[] chars = s.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {      
			final char c = chars[i];
			if ((c >= 'a') && (c <= 'z')) continue; // lowercase
			if ((c >= 'A') && (c <= 'Z')) continue; // uppercase
			if ((c >= '0') && (c <= '9')) continue; // numeric
			return false;
		}
		return true;
	}
	
	
	/**
	 * Check if the string is of type numeric
	 * 
	 * @param String s: value to evaluate
	 *       
	 * 
	 * @return true, false.
	 */
	public static boolean isNumeric (final String s) {
		final char[] chars = s.toCharArray();
		
		if (chars.length<=0){
			return false;
		}else{
			for (int i = 0; i < chars.length; i++) {      
				final char c = chars[i];
				if ((c >= '0') && (c <= '9')) continue; // numeric
				return false;
			}
			return true;
		}
	}
}
