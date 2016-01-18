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
