/**
 * BasicTrackSegment.java
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
 * Provides a basic implementation of the TrackSegment interface.
 */
public class BasicTrackSegment implements TrackSegment {

	private LinkedList<SimpleWaypoint> waypoints = new LinkedList<SimpleWaypoint>();

	/**
	 * Constructs a BasicTrackSegment using the provided list of SimpleWaypoints
	 * representing the "track points" contained in in the TrackSegment.
	 */
	public BasicTrackSegment(List<SimpleWaypoint> argWaypoints) {
		this.waypoints.addAll(argWaypoints);
	}

	@Override
	public List<SimpleWaypoint> getTrackPoints() {
		return this.waypoints;
	}

	@Override
	public int getNumberOfTrackPoints() {
		return this.waypoints.size();
	}
}
