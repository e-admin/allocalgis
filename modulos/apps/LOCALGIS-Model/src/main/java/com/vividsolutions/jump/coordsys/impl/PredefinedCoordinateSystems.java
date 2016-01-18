/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.coordsys.impl;

import com.geopista.app.AppContext;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.coordsys.CoordinateSystem;

/**
 * Provides a number of named coordinate systems.
 */
public class PredefinedCoordinateSystems {
	private static PrecisionModel precisionModelGeographic = AppContext.getApplicationContext().getGeometryFactory(100000000).getPrecisionModel();
	private static PrecisionModel precisionModelUTM = AppContext.getApplicationContext().getGeometryFactory(100).getPrecisionModel();
	
    public static final CoordinateSystem BC_ALBERS_NAD_83 = new CoordinateSystem("BC Albers",
            42102,"North_American_Datum_1983",0,precisionModelUTM);
    public static final CoordinateSystem GEOGRAPHICS_WGS_84 = new CoordinateSystem("Geographics WGS84",
            4326, "WGS84",0,precisionModelGeographic);
    public static final CoordinateSystem GEOGRAPHICS_ETRS89 = new CoordinateSystem("Geographics ETRS89",
            4258, "ETRS89",0,precisionModelGeographic);
    public static final CoordinateSystem GEOGRAPHICS_ED50 = new CoordinateSystem("Geographics ED50",
            4230, "ED50",0,precisionModelGeographic);
    public static final CoordinateSystem UTM_07N_WGS_84 = createUTMNorth(7);
    public static final CoordinateSystem UTM_08N_WGS_84 = createUTMNorth(8);
    public static final CoordinateSystem UTM_09N_WGS_84 = createUTMNorth(9);
    public static final CoordinateSystem UTM_10N_WGS_84 = createUTMNorth(10);
    public static final CoordinateSystem UTM_11N_WGS_84 = createUTMNorth(11);
    public static final CoordinateSystem UTM_30N_ED50 = createED50(30);
    public static final CoordinateSystem UTM_28N_ED50 = createED50(28);
    public static final CoordinateSystem UTM_29N_ED50 = createED50(29);
    public static final CoordinateSystem UTM_31N_ED50 = createED50(31);
    public static final CoordinateSystem UTM_30N_ETRS89 = createETRS89(30);
    public static final CoordinateSystem UTM_28N_ETRS89 = createETRS89(28);
    public static final CoordinateSystem UTM_29N_ETRS89 = createETRS89(29);
    public static final CoordinateSystem UTM_31N_ETRS89 = createETRS89(31);
    public static final CoordinateSystem UTM_27N_WGS84 = createWGS84(27);
    public static final CoordinateSystem UTM_28N_WGS84 = createWGS84(28);

    private PredefinedCoordinateSystems() {
    }

    public static CoordinateSystem createUTMNorth(final int zone) {
        Assert.isTrue(1 <= zone && zone <= 60);
        //Pad with zero to facilitate sorting [Jon Aquino]
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N", 32600 + zone,"WGS84",zone,precisionModelUTM);
    }

    public static CoordinateSystem createED50(final int zone) {
        Assert.isTrue(1 <= zone && zone <= 60);
        //Pad with zero to facilitate sorting [Jon Aquino]
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N ED50", 23000 + zone,"ED50",zone,precisionModelUTM);
    }

   
    
    public static CoordinateSystem createETRS89(final int zone) {
        Assert.isTrue(1 <= zone && zone <= 60);
        //Pad with zero to facilitate sorting [Jon Aquino]
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N ETRS89", 25800 + zone,"ETRS89",zone,precisionModelUTM);
    }

    public static CoordinateSystem createWGS84(final int zone) {
        Assert.isTrue(1 <= zone && zone <= 60);
        //Pad with zero to facilitate sorting [Jon Aquino]
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N WGS84", 32600 + zone,"EPSG:"+""+(32600 + zone),zone,precisionModelUTM);
    }
    
    public static CoordinateSystem getCoordinateSystem( int epsgCode ) {
        CoordinateSystem cs = null;

        if ( epsgCode == GEOGRAPHICS_WGS_84.getEPSGCode() ) {
            cs = GEOGRAPHICS_WGS_84;
        } else if ( epsgCode == BC_ALBERS_NAD_83.getEPSGCode() ) {
            cs = BC_ALBERS_NAD_83;
        } else if ( epsgCode == UTM_07N_WGS_84.getEPSGCode() ) {
            cs = UTM_07N_WGS_84;
        } else if ( epsgCode == UTM_08N_WGS_84.getEPSGCode() ) {
            cs = UTM_08N_WGS_84;
        } else if ( epsgCode == UTM_09N_WGS_84.getEPSGCode() ) {
            cs = UTM_09N_WGS_84;
        } else if ( epsgCode == UTM_10N_WGS_84.getEPSGCode() ) {
            cs = UTM_10N_WGS_84;
        } else if ( epsgCode == UTM_11N_WGS_84.getEPSGCode() ) {
            cs = UTM_11N_WGS_84;
        } else if ( epsgCode == UTM_28N_ED50.getEPSGCode() ) {
            cs = UTM_28N_ED50;
        } else if ( epsgCode == UTM_29N_ED50.getEPSGCode() ) {
            cs = UTM_29N_ED50;
        } else if ( epsgCode == UTM_30N_ED50.getEPSGCode() ) {
            cs = UTM_30N_ED50;
        } else if ( epsgCode == UTM_31N_ED50.getEPSGCode() ) {
            cs = UTM_31N_ED50;
        } else if ( epsgCode == UTM_28N_ETRS89.getEPSGCode() ) {
            cs = UTM_28N_ETRS89;
        } else if ( epsgCode == UTM_29N_ETRS89.getEPSGCode() ) {
            cs = UTM_29N_ETRS89;
        } else if ( epsgCode == UTM_30N_ETRS89.getEPSGCode() ) {
            cs = UTM_30N_ETRS89;
        } else if ( epsgCode == UTM_31N_ETRS89.getEPSGCode() ) {
            cs = UTM_31N_ETRS89;
        } else if ( epsgCode == GEOGRAPHICS_ED50.getEPSGCode()){
        	cs = GEOGRAPHICS_ED50;
        } else if ( epsgCode == GEOGRAPHICS_ETRS89.getEPSGCode()){
        	cs = GEOGRAPHICS_ETRS89;
        } else if ( epsgCode == UTM_27N_WGS84.getEPSGCode()){
        	cs = UTM_27N_WGS84;
        }else if ( epsgCode == UTM_28N_WGS84.getEPSGCode()){
        	cs = UTM_28N_WGS84;
        }else {
            // don't do an assertion - it should be alright if the EPSG code
            // is one of the predefined ones.
        }

        return cs;
    }

}
