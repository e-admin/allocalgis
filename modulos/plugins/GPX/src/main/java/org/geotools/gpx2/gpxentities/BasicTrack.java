/**
 * BasicTrack.java
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
 * Provides the default implementation of the Track interface.
 */
public class BasicTrack implements Track {
	private String name;
	private LinkedList<TrackSegment> segments;
	private boolean hasName;

	/**
	 * Constructs a BasicTract from the list of TrackSegments provided as an
	 * argument.
	 */
	public BasicTrack(List<TrackSegment> argTrackSegments) {
		this.segments = new LinkedList<TrackSegment>();
		this.segments.addAll(argTrackSegments);
	}

	/**
	 * Constructs a BasicTrack from the list of TrackSegments provided as the
	 * first argument and with the name provided as the second argument.
	 */
	public BasicTrack(List<TrackSegment> argTrackSegments, String argName) {
		this.segments = new LinkedList<TrackSegment>();
		this.segments.addAll(argTrackSegments);
		this.setName(argName);
		this.hasName = true;
	}

	/**
	 * Constructs a BasicTrack with an empty list of TrackSegments.
	 */
	public BasicTrack() {
		this.segments = new LinkedList<TrackSegment>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String argName) {
		this.name = argName;
		this.hasName = true;
	}

	/**
	 * Indicates if this BasicTrack has a valid name value.
	 */
	public boolean hasName() {
		return this.hasName;
	}

	@Override
	public int getNumberOfSegments() {
		return this.segments.size();
	}

	@Override
	public List<TrackSegment> getTrackSegments() {
		return this.segments;
	}

	@Override
	public void addTrackSegment(TrackSegment argTrackSegment) {
		this.segments.add(argTrackSegment);

	}

	@Override
	public void clearTrackSegments() {
		this.segments.clear();
	}

	public TrackSegment getTrackSegment(int argIndex) {
		int actualIndex = argIndex - 1;
		return this.segments.get(actualIndex);
	}
}
