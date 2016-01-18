/*
 * Created on 20.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ValueChangeListener.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:04 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/Value/ValueChangeListener.java,v $
 */
package pirolPlugIns.utilities.colors.Value;

/**
 * Interface for objects that listen to canges in a ColoredValue
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
public interface ValueChangeListener {
    public void valueChanged(ColoredValue changedObject);
}
