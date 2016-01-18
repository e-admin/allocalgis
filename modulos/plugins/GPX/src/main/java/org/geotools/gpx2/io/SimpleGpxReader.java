/**
 * SimpleGpxReader.java
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
package org.geotools.gpx2.io;

import java.io.*;
import java.util.*;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.joda.time.DateTime;

import org.geotools.gpx2.gpxentities.*;

public class SimpleGpxReader {

	private Document targetDoc;

	public SimpleGpxReader() {
		// Default no-argument constructor.
	}

	public SimpleWaypoint parseWaypoint(Element argWaypointElement) {
		BasicWaypoint toReturn = new BasicWaypoint();

		Attribute latitudeAttribute = argWaypointElement.getAttribute("lat");
		String latitudeAsString = latitudeAttribute.getValue();
		double latitude = Double.parseDouble(latitudeAsString);

		toReturn.setLatitude(latitude);

		Attribute longitudeAttribute = argWaypointElement.getAttribute("lon");
		String longitudeAsString = longitudeAttribute.getValue();
		double longitude = Double.parseDouble(longitudeAsString);

		toReturn.setLongitude(longitude);

		List<Element> allChildren = argWaypointElement.getChildren();
		Iterator<Element> goOverEach = allChildren.iterator();

		while (goOverEach.hasNext() == true) {
			Element currentElement = goOverEach.next();
			String currentElementName = currentElement.getName();

			if (currentElementName.equals("ele")) {
				String elevationAsString = currentElement.getTextTrim();
				double elevation = Double.parseDouble(elevationAsString);

				toReturn.setElevation(elevation);
			}

			if (currentElementName.equals("name")) {
				String nameAsString = currentElement.getTextTrim();

				toReturn.setName(nameAsString);
			}

			if (currentElementName.equals("time")) {
				String timeAsString = currentElement.getTextTrim();
				DateTime dateAndTimeCollected = new DateTime(timeAsString);
				toReturn.setDateAndTimeCollected(dateAndTimeCollected);
			}
		}

		return toReturn;
	}

	public BasicTrack parseTrack(Element argTrackElement) {
		BasicTrack toReturn = new BasicTrack();

		List<Element> allChildren = argTrackElement.getChildren();

		Iterator<Element> goOverEach = allChildren.iterator();

		while (goOverEach.hasNext() == true) {
			Element currentElement = goOverEach.next();
			String currentElementName = currentElement.getName();

			if (currentElementName.equals("name") == true) {
				String name = currentElement.getTextTrim();
				toReturn.setName(name);
			}

			if (currentElementName.equals("trkseg") == true) {
				BasicTrackSegment trackSegment = this
						.parseTrackSegment(currentElement);

				toReturn.addTrackSegment(trackSegment);
			}
		}

		return toReturn;
	}

	public void setUpForReading(File argTarget) {
		SAXBuilder builder = new SAXBuilder();

		try {
			targetDoc = builder.build(argTarget);
		}

		catch (JDOMException caught) {
			caught.printStackTrace();
		}

		catch (IOException caught) {
			caught.printStackTrace();
		}
	}

	public List<SimpleWaypoint> getWaypoints() {
		Element rootElement = targetDoc.getRootElement();

		List<Element> allChildElements = rootElement.getChildren();

		LinkedList<SimpleWaypoint> waypoints = new LinkedList<SimpleWaypoint>();

		Iterator<Element> goOverEach = allChildElements.iterator();

		while (goOverEach.hasNext() == true) {
			Element currentElement = goOverEach.next();

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("wpt") == true) {
				SimpleWaypoint toAdd = this.parseWaypoint(currentElement);
				waypoints.add(toAdd);
			}
		}

		return waypoints;
	}

	public List<Track> getTracks() {
		Element rootElement = targetDoc.getRootElement();

		List<Element> allChildElements = rootElement.getChildren();

		LinkedList<Track> tracks = new LinkedList<Track>();

		Iterator<Element> goOverEach = allChildElements.iterator();

		while (goOverEach.hasNext() == true) {
			Element currentElement = goOverEach.next();

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("trk") == true) {
				Track toAdd = this.parseTrack(currentElement);
				tracks.add(toAdd);
			}
		}

		return tracks;
	}

	private BasicTrackSegment parseTrackSegment(Element argTrackSegmentElement) {
		List<Element> allChildElements = argTrackSegmentElement.getChildren();

		LinkedList<SimpleWaypoint> trackPoints = new LinkedList<SimpleWaypoint>();

		Iterator<Element> goOverEach = allChildElements.iterator();

		while (goOverEach.hasNext() == true) {
			Element currentElement = goOverEach.next();

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("trkpt") == true) {
				SimpleWaypoint toAdd = this.parseWaypoint(currentElement);
				trackPoints.add(toAdd);
			}
		}

		BasicTrackSegment toReturn = new BasicTrackSegment(trackPoints);
		return toReturn;
	}

	/**
	 * Indicates if the target document being processed by this reader has
	 * waypoint elements.
	 */
	public boolean targetDocumentHasWaypoints() {
		Element rootElement = targetDoc.getRootElement();

		List<Element> allChildElements = rootElement.getChildren();

		Iterator<Element> goOverEach = allChildElements.iterator();

		while (goOverEach.hasNext() == true) {
			Element currentElement = goOverEach.next();

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("wpt") == true) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Indicates if the target document being processed by this reader has track
	 * elements.
	 * 
	 * @return
	 */
	public boolean targetDocumentHasTracks() {
		Element rootElement = targetDoc.getRootElement();

		List<Element> allChildElements = rootElement.getChildren();

		Iterator<Element> goOverEach = allChildElements.iterator();

		while (goOverEach.hasNext() == true) {
			Element currentElement = goOverEach.next();

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("trk") == true) {
				return true;
			}
		}

		return false;
	}
}
