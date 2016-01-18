/*
 * Created on 30.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ValueChecker.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/ValueChecker.java,v $
 */
package pirolPlugIns.dialogs;

/**
 * Interface for a class that checks if the values in e.g. a dialog are ok, so we can proceed or not.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * @see pirolPlugIns.dialogs.OKCancelListener
 * 
 */
public interface ValueChecker {
    public boolean areValuesOk();
}
