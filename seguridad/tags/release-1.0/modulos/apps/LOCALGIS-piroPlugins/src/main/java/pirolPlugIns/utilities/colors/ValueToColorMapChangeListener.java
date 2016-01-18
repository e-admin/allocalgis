/*
 * Created on 20.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ValueToColorMapChangeListener.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:05 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/ValueToColorMapChangeListener.java,v $
 */
package pirolPlugIns.utilities.colors;

/**
 * simple "event" class to inform other object, when the map changed.
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
public interface ValueToColorMapChangeListener {
    public void valueToColorMapChanged(ValueToColorMap source);
}
