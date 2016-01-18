/*
 * Created on 09.11.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: PirolFeatureCollectionRole.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FeatureCollection/PirolFeatureCollectionRole.java,v $
 */
package pirolPlugIns.utilities.FeatureCollection;

/**
 * Base class for different roles of a PirolFeatureCollection, like RasterImage, Grid, Outline, etc.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * @see pirolPlugIns.utilities.FeatureCollection.PirolFeatureCollection
 * 
 */
public abstract class PirolFeatureCollectionRole {

    /** no specific role */
    public final static int ROLE_STANDARD    = 0;
    /** FeatureCollection that contains application grids */
    public final static int ROLE_APPGRID     = 1;
    /** outline of an acre (e.g. for craeting grids) */
    public final static int ROLE_OUTLINE     = 2;
    /** point layer */
    public final static int ROLE_POINT       = 3;
    
    /** Number of existent roles roles */
    public static final int numOfExistentRoles = 4;
    
    
    /**
     * 
     *@return an integer that specifies the role type of the derived role object
     *
     *@see PirolFeatureCollectionRole#ROLE_STANDARD
     *@see PirolFeatureCollectionRole#ROLE_APPGRID
     *@see PirolFeatureCollectionRole#ROLE_OUTLINE
     */
    abstract int getRoleId();
    
    /**
     * 
     *@return true if it contains gridded data (e.g. grid layer), else false 
     */
    public boolean containsGrid(){
        return false;
    }
    
    /**
     * 
     *@return true if it contains raster data (e.g. raster image layer), else false 
     */
    public boolean containsImage(){
        return false;
    }

    /**
     * Check if this role is the same type of role as the given one.
     * Caution: If this role contains specific information (like RasterImage role), this information
     * is not checked for equality - Only the type of the role is checked!
     *@param role role to check for type equality
     *@return true if this role is the same type of role as the given one, else false
     */
    public boolean equalsRole(PirolFeatureCollectionRole role) {
        return this.getRoleId() == role.getRoleId();
    }
    
    /**
     * Check if this role is the same type of role as the given role id.
     *@param roleID id of the role type to check for type equality
     *@return true if this role is the same type of role as the given ID, else false
     *
     *@see PirolFeatureCollectionRole#ROLE_STANDARD
     *@see PirolFeatureCollectionRole#ROLE_APPGRID
     *@see PirolFeatureCollectionRole#ROLE_OUTLINE
     */
    public boolean equalsRole(int roleID) {
        return this.getRoleId() == roleID;
    }
    
    /**
     * Method to be called e.g. by a FeatureCollection, when it's disposed
     * to free RAM that may be bound in references to objects, that are still
     * referenced by other objects, but won't be used without the FeatureCollection.
     * 
     */
    public void clearRam(){
        return;
    }
    
}
