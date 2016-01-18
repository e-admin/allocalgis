/**
 * Constants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.satec.gpx.utils;

import com.satec.gpx.gpx10.Gpx;
import com.satec.gpx.gpx11.GpxType;

public class Constants {
	public static final Class<Gpx> GPX10 = Gpx.class;
	public static final Class<GpxType> GPX11 = GpxType.class;
	
	public static final String GPX_1_0_REMOTE_URL = "http://www.topografix.com/GPX/1/0/gpx.xsd";
	public static final String GPX_1_0_LOCAL_URL = "/gpx1_0.xsd";
	public static final String GPX_1_1_REMOTE_URL = "http://www.topografix.com/GPX/1/1/gpx.xsd";
	public static final String GPX_1_1_LOCAL_URL = "/gpx1_1.xsd";
	
	public static final String GPX_TAG = "gpx";
	public static final String GPX_SCHEMA_LOCATION = "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd";
	public static final String XML_PREFIX = "geopista";
	
	public static final String MULTILINE_ROUTE_TOKEN = "MULTILINESTRING";
	public static final String LINE_ROUTE_TOKEN = "LINESTRING";
}
