/**
 * GeoTiffConstants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 11.04.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: GeoTiffConstants.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/RasterImageSupport/GeoTiffConstants.java,v $
 */
package pirolPlugIns.utilities.RasterImageSupport;

/**
 * TODO: comment class
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2006),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class GeoTiffConstants {

    //Here are all of the TIFF tags (and their owners) that are used to store GeoTIFF information of any type. It is very unlikely that any other tags will be necessary in the future (since most additional information will be encoded as a GeoKey).

    public final static int    ModelPixelScaleTag     = 33550 ,//(SoftDesk)
                                ModelTransformationTag = 34264 ,//(JPL Carto Group)
                                ModelTiepointTag       = 33922 ,//(Intergraph)
                                GeoKeyDirectoryTag     = 34735 ,//(SPOT)
                                GeoDoubleParamsTag     = 34736 ,//(SPOT)
                                GeoAsciiParamsTag      = 34737 ,//(SPOT)
                              //Obsoleted Implementation:                             
                                IntergraphMatrixTag = 33920 ;//(Intergraph) -- Use ModelTransformationTag.
  

}
