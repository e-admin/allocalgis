/**
 * StandardPirolCorrelationPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 19.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: StandardPirolCorrelationPlugIn.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:07 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/StandardPirolCorrelationPlugIn.java,v $
 */
package pirolPlugIns.plugIns;


/**
 * 
 * PlugIn base class for PlugIns that deal with correlations.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 *
 */
public abstract class StandardPirolCorrelationPlugIn extends StandardPirolPlugIn {
    
    /**
     * For HTML-output: this methods returns a color (as a String) that gives an
     * impression on how strong the correlation (given by the correlation coefficient) is.
     *@param coefficient the correlation coefficient
     *@return green (if correlation is strong), orange (if it's there, but not too strong), red (if it's not there)
     */
    protected String getColorStringforCorrelationCoefficient(double coefficient){
        if (Math.abs(coefficient) >= 0.75)
            return "#00ff00";
        else if (Math.abs(coefficient) >= 0.5)
            return "#dd9900";
        else
            return "red";
    }

}
