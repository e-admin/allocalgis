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

package com.vividsolutions.jump.coordsys;

import javax.swing.JOptionPane;

import com.geopista.coordsys.ed50toetrs89.info.LonLat;
import com.geopista.coordsys.ed50toetrs89.main.*;
import com.geopista.ui.plugin.wfs.CoordinateConversion;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;


/**
 * The source and destination coordinate reference systems must have
 * the same datum (for example, WGS 84).
 */
public class Reprojector {
	private static Reprojector instance = new Reprojector();

	private Ed50ToETRS89 ed2etConv=new Ed50ToETRS89();

	private Reprojector() {
	}

	public static Reprojector instance() {
		return instance;
	}

	public boolean wouldChangeValues(CoordinateSystem source,
			CoordinateSystem destination) {
		if (source == CoordinateSystem.UNSPECIFIED) {
			return false;
		}

		if (destination == CoordinateSystem.UNSPECIFIED) {
			return false;
		}

		if (source == destination) {
			return false;
		}
		
		if (source.getDatum().equals("ETRS89") && destination.getDatum().equals("ED50")){
			JOptionPane.showMessageDialog(null, "Transformación de ETRS89 a ED50 no implementada");
			return false;
		}

		return true;
	}

	/*    public void reproject(Coordinate coordinate, CoordinateSystem source,
        CoordinateSystem destination) {
        if (!wouldChangeValues(source, destination)) {
            return;
        }

        Planar result = destination.getProjection().asPlanar(source.getProjection()
                                                                   .asGeographic(new Planar(
                        coordinate.x, coordinate.y), new Geographic()),
                new Planar());
        coordinate.x = result.x;
        coordinate.y = result.y;
    }
	 */
	//Cambio la geometría de sistema de coordenadas.
	//Si las geometrías origen y destino pertenecen al mismo datum, se realiza el reproject original que
	//ya existía. Sólo se permite el cambio de datum entre ED50 y ETRS89. En este caso se realiza
	//la operación de rejilla NTV2 que convierte geográficas ED50 en geográficas ETRS89. Si el origen o
	//destino están o se requieren en coordenadas proyectadas, esta transformación tanbién se hará aparte.
	public void reproject(Geometry geom, final CoordinateSystem source,
			final CoordinateSystem destination) {
		try{
			if (!wouldChangeValues(source, destination)) {
				return;
			}
			if (!(source.getDatum().equals("ED50") && destination.getDatum().equals("ETRS89"))){
				geom.apply(new CoordinateFilter() {
					public void filter(Coordinate coord) {
						try{
							CoordinateConversion.instance().reproject(coord, source, destination);
						}catch(Exception e){
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
					}
				});
			}else{
				//Se convierte de datum ED50 a ETRS89
				if (source.getDatum().equals("ED50") && destination.getDatum().equals("ETRS89")){
					//Si el origen está en coordenadas proyectadas ED50, se debe convertir a ETRS89 proyectadas UTM
					//(EPSG:25829,25830,25831) y luego aplicar la corrección de la rejilla NTV2
					//Según el huso inicial, así se determinará el huso destino
					geom.apply(new CoordinateFilter() {


						public void filter(Coordinate coord) {
							try{
								//Primero paso de ED50 UTM a geográficas ED50
								final CoordinateSystem auxDestination1 = PredefinedCoordinateSystems.GEOGRAPHICS_ED50;
								CoordinateConversion.instance().reproject(coord, source, auxDestination1);
								// call convert ed50 to etrs89
								// ed50toEtrs89(lon, lat);
								LonLat lonlat =  ed2etConv.ed50toEtrs89(coord.x, coord.y);
								coord.x = -lonlat.getLongitude();
								coord.y = lonlat.getLatitude();

								// convertED50_ETRS89();
								final CoordinateSystem auxDestination2 = PredefinedCoordinateSystems.GEOGRAPHICS_ETRS89;
								CoordinateConversion.instance().reproject(coord, auxDestination2, destination);
							}catch(Exception e){
								JOptionPane.showMessageDialog(null, e.getMessage());
								e.printStackTrace();
							}
						}
					});
				}else{
					JOptionPane.showMessageDialog(null, "Sólo se pueden realizar conversiones entre el datum ED50 y el ETRS89 con proyección UTM.");
				}
			}
			geom.setSRID(destination.getEPSGCode());
			geom.geometryChanged();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		GeometryFactory geometryFactory = new GeometryFactory();
		Coordinate coord ;
		coord=new Coordinate(-21.9878301648474, -0.0014827350588441691);
//		coord[1]=new Coordinate(723459, 4464818);
		Point point = geometryFactory.createPoint(coord);
		final CoordinateSystem source = PredefinedCoordinateSystems.GEOGRAPHICS_WGS_84;
		final CoordinateSystem destination = PredefinedCoordinateSystems.UTM_30N_ED50;
		Reprojector.instance.reproject(point, source, destination);
		System.out.println(point.toString());
	}

}
