/*
 * Created on 09.11.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: RolePointFeatures.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FeatureCollection/RolePointFeatures.java,v $
 */
package pirolPlugIns.utilities.FeatureCollection;

/**
 * Role for FeatureCollections that contain point geometries, only
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
public class RolePointFeatures extends PirolFeatureCollectionRole {

    /**
     *
     */
    public RolePointFeatures() {
        super();
    }

    /**
     *@inheritDoc
     */
    int getRoleId() {
        return PirolFeatureCollectionRole.ROLE_POINT;
    }

}
