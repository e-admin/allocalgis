/**
 * NumberRepresentationTools.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 01.06.2005
 *
 * CVS header information:
 *  $RCSfile: NumberRepresentationTools.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/NumberRepresentationTools.java,v $
 */
package pirolPlugIns.utilities;

/**
 * Some static methods useful for number representation,
 * rounding and scientific notation.
 * @author Stefan Ostermann
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck
 */
public class NumberRepresentationTools {

	/**
	 * Private constructor, no need for instance of this class.
	 */
	private NumberRepresentationTools() {
	}

	
	/**
	 * rounds value by digits
	 * @param value
	 * @param digits
	 * @return rounded value.
	 */
	public static double round (double value, int digits) {
		return Math.rint(value*Math.pow(10,digits))/Math.pow(10,digits);
	}
	
	/**
	 * Returns the power to ten so that a value x is bigger or equal to one:
	 * x*10^power >= 1.0
	 * added: values greater than 1000 will be like 1*10^5
	 * IMPORTANT: multiply power with (-1) to display the real value for scientific notation!<br>
	 * Example: 0.02 becomes 2 with power=2, if you want to display it you print: 2*10^-2
	 * @param value
	 * @return the power
	 */
	public static int getPower(double value) {
		int counter = 0;
		
		if (value<0.1) {
			while (value < 1.0 && value > 0.0) {
				value = value * 10;
				counter ++;
			}
		} else if (value>1000) {
			while (value>10) {
				value = value/10;
				counter--;
			}
		}
		
		return counter;
	}
}
