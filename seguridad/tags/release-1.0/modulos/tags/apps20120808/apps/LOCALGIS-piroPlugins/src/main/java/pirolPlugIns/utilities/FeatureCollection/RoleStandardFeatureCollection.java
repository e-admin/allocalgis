/*
 * Created on 09.11.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: RoleStandardFeatureCollection.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FeatureCollection/RoleStandardFeatureCollection.java,v $
 */
package pirolPlugIns.utilities.FeatureCollection;

/**
 * Standard Role with no special capabilities 
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
public class RoleStandardFeatureCollection extends PirolFeatureCollectionRole {

    /**
     *
     */
    public RoleStandardFeatureCollection() {
        super();
    }

    /**
     *@inheritDoc
     */
    public int getRoleId() {
        return PirolFeatureCollectionRole.ROLE_STANDARD;
    }

}
