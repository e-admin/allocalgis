/**
 * SimpleWaypoint.java
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

import org.joda.time.*;

public interface SimpleWaypoint {
	/**
	 * Returns the latitude of this SimpleWaypoint object. The latitude is in
	 * decimal degrees format.
	 */
	public abstract double getLatitude();

	/**
	 * Sets the latitude of this SimpleWaypoint object. The double value passed
	 * to this method should represent a valid latitude in decimal degrees
	 * format.
	 * 
	 * @see org.geotools.gpx.utils.GpxUtils
	 */
	public abstract void setLatitude(double argLatitude);

	/**
	 * Returns the longitude of this SimpleWaypoint object. The longitude is in
	 * decimal degrees format.
	 */
	public abstract double getLongitude();

	/**
	 * Sets the longitude of this SimpleWaypoint object. The double value passed
	 * to this method should represent a valid longitude in decimal degrees
	 * format.
	 * 
	 * @see org.geotools.gpx.utils.GpxUtils
	 */
	public abstract void setLongitude(double argLongitude);

	/**
	 * Returns the elevation of this SimpleWaypoint object. The elevation
	 * returned is usually in feet. However, this may change depending on the
	 * implementation of this interface.
	 */
	public abstract double getElevation();

	/**
	 * Sets the elevation of this SimpleWaypoint object. This value is typically
	 * in feet, but this may vary depending on the implementation of this
	 * interface. Negative elevation values are allowed and would represent
	 * elevations below sea level or some other datum.
	 */
	public abstract void setElevation(double argElevation);

	/**
	 * Returns the DateTime object that represents the date and time this this
	 * SimpleWaypoint object was collected. The calendar (time unit system) that
	 * this DateTime is referenced to will very depending on the implementation
	 * of this interface.
	 */
	public abstract DateTime getDateAndTimeCollected();

	/**
	 * Sets the DateTime object representing the date and time this
	 * SimpleWaypoint was collected.
	 */
	public abstract void setDateAndTimeCollected(DateTime argTime);

	/**
	 * Sets the name of this Waypoint. This name is not guaranteed to be unique.
	 */
	public abstract void setName(String argName);

	/**
	 * Returns the name of this Waypoint as a String.
	 */
	public abstract String getName();

	/**
	 * Indicates if this SimpleWaypoint object has a name.
	 */
	public abstract boolean hasName();

	/**
	 * Indicates if this SimpleWaypoint object has an elevation.
	 */
	public abstract boolean hasElevation();

	/**
	 * Indicates if this SimpleWaypoint has a DateTime object that reprensets
	 * the data and time it was collected.
	 */
	public abstract boolean hasDateAndTimeCollected();
}
