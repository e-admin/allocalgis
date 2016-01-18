/*
 * Created on 09.11.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: RoleOutline.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FeatureCollection/RoleOutline.java,v $
 */
package pirolPlugIns.utilities.FeatureCollection;

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
public class RoleOutline extends PirolFeatureCollectionRole {

    /**
     *
     */
    public RoleOutline() {
        super();
    }

    /**
     *@inheritDoc
     */
    public int getRoleId() {
        return PirolFeatureCollectionRole.ROLE_OUTLINE;
    }
}
