/**
 * PredefinedCoordinateSystems.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.coordsys;

/**
 * Provides a number of named coordinate systems.
 */
public class PredefinedCoordinateSystems {

	public static final CoordinateSystem BC_ALBERS_NAD_83 = new CoordinateSystem("BC Albers",
            42102,"North_American_Datum_1983",0);
    public static final CoordinateSystem GEOGRAPHICS_WGS_84 = new CoordinateSystem("Geographics",
            4326, "WGS84",0);
    public static final CoordinateSystem GEOGRAPHICS_ETRS89 = new CoordinateSystem("Geographics",
            4258, "ETRS89",0);
    public static final CoordinateSystem GEOGRAPHICS_ED50 = new CoordinateSystem("Geographics",
            4230, "ED50",0);
    public static final CoordinateSystem UTM_07N_WGS_84 = createUTMNorth(7);
    public static final CoordinateSystem UTM_08N_WGS_84 = createUTMNorth(8);
    public static final CoordinateSystem UTM_09N_WGS_84 = createUTMNorth(9);
    public static final CoordinateSystem UTM_10N_WGS_84 = createUTMNorth(10);
    public static final CoordinateSystem UTM_11N_WGS_84 = createUTMNorth(11);
    public static final CoordinateSystem UTM_30N_ED50 = createED50(30);
    public static final CoordinateSystem UTM_29N_ED50 = createED50(29);
    public static final CoordinateSystem UTM_31N_ED50 = createED50(31);
    public static final CoordinateSystem UTM_28N_ED50 = createED50(28);
    public static final CoordinateSystem UTM_30N_ETRS89 = createETRS89(30);
    public static final CoordinateSystem UTM_29N_ETRS89 = createETRS89(29);
    public static final CoordinateSystem UTM_31N_ETRS89 = createETRS89(31);
    public static final CoordinateSystem UTM_28N_ETRS89 = createETRS89(28);

    private PredefinedCoordinateSystems() {
    }

    public static CoordinateSystem createUTMNorth(final int zone) {
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N", 32600 + zone,"WGS84",zone);
    }

    public static CoordinateSystem createED50(final int zone) {
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N ED50", 23000 + zone,"ED50",zone);
    }

    public static CoordinateSystem createETRS89(final int zone) {
        return new CoordinateSystem("UTM " + (zone < 10 ? "0" : "") + zone + "N ETRS89", 25800 + zone,"ETRS89",zone);
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
        } else if ( epsgCode == UTM_29N_ED50.getEPSGCode() ) {
            cs = UTM_29N_ED50;
        } else if ( epsgCode == UTM_30N_ED50.getEPSGCode() ) {
            cs = UTM_30N_ED50;
        } else if ( epsgCode == UTM_31N_ED50.getEPSGCode() ) {
            cs = UTM_31N_ED50;
        } else if ( epsgCode == UTM_29N_ETRS89.getEPSGCode() ) {
            cs = UTM_31N_ETRS89;
        } else if ( epsgCode == UTM_30N_ETRS89.getEPSGCode() ) {
            cs = UTM_31N_ETRS89;
        } else if ( epsgCode == UTM_31N_ETRS89.getEPSGCode() ) {
            cs = UTM_31N_ETRS89;
        } else if ( epsgCode == GEOGRAPHICS_ED50.getEPSGCode()){
        	cs = GEOGRAPHICS_ED50;
        } else if ( epsgCode == GEOGRAPHICS_ETRS89.getEPSGCode()){
        	cs = GEOGRAPHICS_ETRS89;
        } else {
            // don't do an assertion - it should be alright if the EPSG code
            // is one of the predefined ones.
        }

        return cs;
    }

}