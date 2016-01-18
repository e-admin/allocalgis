/**
 * CollectionsTools.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
