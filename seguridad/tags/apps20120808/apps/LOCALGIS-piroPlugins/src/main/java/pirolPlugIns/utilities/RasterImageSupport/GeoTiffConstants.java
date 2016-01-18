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
