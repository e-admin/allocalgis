/*
 * Created on 13.04.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: CollectionsTools.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/CollectionsTools.java,v $
 */
package pirolPlugIns.utilities;

import java.util.List;

/**
 * Class for more convenient use of Lists and Arrays.
 * 
 * @author Ole Rahn
 * 
 * FH Osnabrück - University of Applied Sciences Osnabrück
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 * 
 */
public class CollectionsTools extends ToolToMakeYourLifeEasier {

    public static boolean addArrayToList( List toAddTo, Object[] arrayToBeAdded ){
        
        for ( int i=0; i<arrayToBeAdded.length; i++ ){
            toAddTo.add(arrayToBeAdded[i]);
        }
        
        return true;
    }

}
