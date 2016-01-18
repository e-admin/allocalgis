/**
 * MetaInformationKeys.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 17.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: MetaInformationKeys.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:01 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/metaData/MetaInformationKeys.java,v $
 */
package pirolPlugIns.utilities.metaData;

/**
 * Class that contains standard keys (and values?) for the handling of meta information.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * @see pirolPlugIns.utilities.metaData.MetaInformationHandler
 * 
 */
public class MetaInformationKeys {

    /**
     * general key for mata data in a data source's properties
     */
    public final static String KEY_METAINFORMATION 	= "metaData";

    /**
     * Type of dataset, like yield measurement, N-sensor data, etc.
     */
    public final static String KEY_DATASETTYPE 	= "Type of dataset";
    /**
     * the date the data was measured.
     */
    public final static String KEY_DATE			= "date";
    /**
     * depth of measurement, like 30 for 30cm below surface
     */
    public final static String KEY_DEPTH 		= "measurement depth";
    
    /**
     * ID of the field (acre)
     */
    public final static String KEY_FIELDID        = "field ID";
    
    /**
     * ID of the farm
     */
    public final static String KEY_FARMID        = "farm ID";
    
    /**
     * name of the grain 
     */
    public final static String KEY_GRAIN        = "grain";
    
    /**
     * name of the grain variety
     */
    public final static String KEY_VARIETY        = "variety";
    
    /**
     * id of the worker, that created a dataset
     */
    public final static String KEY_WORKER        = "worker";
    
    // TODO: add more!

}
