/**
 * FeatureSchemaTools.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 28.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: FeatureSchemaTools.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/FeatureSchemaTools.java,v $
 */
package pirolPlugIns.utilities;

import java.util.ArrayList;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * Class for easier handling of featureSchema objects.
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
public class FeatureSchemaTools extends ToolToMakeYourLifeEasier {

    /**
     * Extracts information of all attributes with one of the given types from the given FeatureSchema
     *@param fs FeatureSchema to get information from
     *@param allowedTypes array with AttributeTypes, that specify which attribute to get information about
     *@return array with information on matching attributes
     */
    public static AttributeInfo[] getAttributesWithTypes(FeatureSchema fs, AttributeType[] allowedTypes){
        ArrayList attrInfosList = new ArrayList();
        AttributeInfo[] attrInfoArray = AttributeInfo.schema2AttributeInfoArray(fs);
        int numInfos = attrInfoArray.length;
        
        for ( int i=0; i<numInfos; i++){
            if (FeatureSchemaTools.isAttributeTypeAllowed(attrInfoArray[i].getAttributeType(),allowedTypes)){
                attrInfosList.add(attrInfoArray[i]);
            }
        }
        
        numInfos = attrInfosList.size();
        AttributeInfo[] attrInfoRaw = (AttributeInfo[])attrInfosList.toArray(new AttributeInfo[0]);
        
        return attrInfoRaw;
    }
    
    /**
     * Extracts information on the attribute at the given index from the feature schema.
     *@param fs FeatureSchema to get information from
     *@param attrIndex index of the attribute in the given featureSchema to get information about
     *@return information about the attribute
     */
    public static AttributeInfo getAttributesInfoFor(FeatureSchema fs, int attrIndex){
        AttributeInfo attrInfo = new AttributeInfo(fs.getAttributeName(attrIndex), fs.getAttributeType(attrIndex));
        
        attrInfo.setIndex(attrIndex);
        
        return attrInfo;
    }
    
    /**
     * Extracts information on the attribute with the given name from the feature schema.
     *@param fs FeatureSchema to get information from
     *@param attrName name of the attribute in the given featureSchema to get information about
     *@return information about the attribute
     */
    public static AttributeInfo getAttributesInfoFor(FeatureSchema fs, String attrName){
        return FeatureSchemaTools.getAttributesInfoFor(fs, fs.getAttributeIndex(attrName));
    }
    
    /**
     * Checks if the given attribute type is contained in the given array of allowed attribute types.
     *@param at attribute type to be checked
     *@param allowedTypes array of allowed attribute types
     *@return true if <code>at</code> is contained in <code>allowedTypes</code>, else false
     */
    public static boolean isAttributeTypeAllowed(AttributeType at, AttributeType[] allowedTypes){
        int numTypes = allowedTypes.length;
        for (int i=0; i<numTypes; i++){
            if (allowedTypes[i].equals(at)) return true;
        }
        return false;
    }

}
