/*
 * Created on 06.09.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: LoadPropertiesEvent.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/LoadPropertiesEvent.java,v $
 */
package pirolPlugIns.dialogs;

import pirolPlugIns.utilities.Properties.PropertiesHandler;

/**
 * TODO: comment class
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
public class LoadPropertiesEvent {
    protected PropertiesHandler ph = null;

    public LoadPropertiesEvent(PropertiesHandler ph) {
        super();
        this.ph = ph;
    }

    public PropertiesHandler getPropertiesHandler() {
        return ph;
    }
    
    
}
