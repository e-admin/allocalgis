/**
 * CoordinateConversion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.wfs;

import javax.swing.JOptionPane;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.coordsys.CoordinateSystem;

/**
 * The source and destination coordinate reference systems can be in a different
 * datum (for example, WGS 84).
 */
public class CoordinateConversion {
	private static CoordinateConversion instance = new CoordinateConversion();

	private CoordinateConversion() {
	}

	public static CoordinateConversion instance() {
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

		return true;
	}

	public Geometry reproject(Geometry geometry, final CoordinateSystem source,
			final CoordinateSystem destiny) throws Exception {
		try {

			// CRSAuthorityFactory crsFactory =
			// FactoryFinder.getCRSAuthorityFactory("EPSG", null);
			geometry.apply(new CoordinateFilter() {
				public void filter(Coordinate coord) {
					try {
						reproject(coord, source, destiny);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				}
			});
			return geometry;
		} catch (VerifyError e) {
			e.printStackTrace();
			return null;
		}
	}

	public void reproject(Coordinate coordinate, final CoordinateSystem source,
			final CoordinateSystem destiny) {
		try {

			Coordinate coordinateSource = new Coordinate();
			Coordinate coordinateTarget = new Coordinate();
			if(source.getName().startsWith("Geographics"))
			{
				coordinateSource.x = coordinate.y;
				coordinateSource.y = coordinate.x;
			}
			else
			{
				coordinateSource.x = coordinate.x;
				coordinateSource.y = coordinate.y;
			}
			
			CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:"
					+ source.getEPSGCode());
			CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:"
					+ destiny.getEPSGCode());
			
			MathTransform transform = CRS.findMathTransform(sourceCRS,
					targetCRS);
			
			JTS.transform(coordinateSource, coordinateTarget, transform);
			
			if(destiny.getName().startsWith("Geographics"))
			{
				coordinate.x = coordinateTarget.y;
				coordinate.y = coordinateTarget.x;
			}
			else
			{
				coordinate.x = coordinateTarget.x;
				coordinate.y = coordinateTarget.y;
			}
			
			
		} catch (NoSuchAuthorityCodeException e) {
			e.printStackTrace();
			
		} catch (FactoryException e) {
			e.printStackTrace();
			
		} catch (VerifyError e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

}
