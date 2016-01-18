/**
 * TrackSegment.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gpx2.gpxentities;

import java.util.*;

/**
 * Represents a TrackSegment. A TrackSegment is part of a Track. It contains two
 * (2) or more "track points", which are represented by SimpleWaypoints.
 */
public interface TrackSegment {
	/**
	 * Returns a list of the SimpleWaypoint objects that are the "track points"
	 * for this TrackSegment.
	 */
	public abstract List<SimpleWaypoint> getTrackPoints();

	/**
	 * Returns the number of "track points" stored in this TrackSegment.
	 */
	public abstract int getNumberOfTrackPoints();
}
