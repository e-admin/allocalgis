/*
 * Created on 09.11.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: RoleApplicationGrid.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FeatureCollection/RoleApplicationGrid.java,v $
 */
package pirolPlugIns.utilities.FeatureCollection;

/**
 * role for a feature collection containing gridded data
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
public class RoleApplicationGrid extends PirolFeatureCollectionRole {

    public RoleApplicationGrid() {
        super();
    }

    /**
     *@inheritDoc
     */
    public int getRoleId() {
        return PirolFeatureCollectionRole.ROLE_APPGRID;
    }

    /**
     *@inheritDoc
     */
    public boolean containsGrid() {
        return true;
    }
    
    

}
