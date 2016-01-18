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
