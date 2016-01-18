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
 * Represents a GPX track. A track is made up of a TrackSegments. Each
 * TrackSegment in a Track object contains SimpleWaypoints that represent the
 * "track points" in the Track.
 * 
 * @see TrackSegment
 * @see SimpleWaypoint
 * @see BasicTrack
 * @see BasicTrackSegment
 */
public interface Track {
	/**
	 * Returns the name of this TrackSegment.
	 */
	public abstract String getName();

	/**
	 * Sets the name of the TrackSegment.
	 */
	public abstract void setName(String argName);

	/**
	 * Adds a TrackSegment to this Track.
	 */
	public abstract void addTrackSegment(TrackSegment argTrackSegment);

	/**
	 * Clears all of the TrackSegments within this Track.
	 */
	public abstract void clearTrackSegments();

	/**
	 * Returns the TrackSegments contained in this Track.
	 */
	public abstract List<TrackSegment> getTrackSegments();

	/**
	 * Returns the number of TrackSegments contained in this Track.
	 */
	public abstract int getNumberOfSegments();

	/**
	 * Returns the TrackSegment with the position in the Track matches the int
	 * provided as an argument. An int value of 1 should return the first
	 * TrackSegment in the Track.
	 * 
	 * @param argIndex
	 * @return
	 */
	public abstract TrackSegment getTrackSegment(int argIndex);
}
