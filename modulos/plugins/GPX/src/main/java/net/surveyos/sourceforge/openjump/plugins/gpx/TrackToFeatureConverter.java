/**
 * TrackToFeatureConverter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.surveyos.sourceforge.openjump.plugins.gpx;

import java.util.*;

import org.geotools.gpx2.gpxentities.SimpleWaypoint;
import org.geotools.gpx2.gpxentities.Track;
import org.geotools.gpx2.gpxentities.TrackSegment;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
* Provides a static method that can be used to obtain an implementation
* of the OpenJUMP Feature interface based on the Track object
* provided to the method. The implementation currently returned is an instance
* of the OpenJUMP BasicFeature class.
*/
public class TrackToFeatureConverter 
{
	/**
	 * Creates and returns a list of BasicFeature objects based on the 
	 * TrackSegment objects contained in the Track object
	 * that is provided as an argument. Each TrackSegment becomes a LineString
	 * object in the returned list.
	 * 
	 * The BasicFeature object representing each TrackSegment
	 * has two non-spatial attributes. The first is the name of the
	 * Track that contained the segment. This attribute value can be
	 * accessed using the "Track Name" attribute name.
	 * 
	 * The second non-spatial attribute value is the
	 * JDK Date object representing the instant the SimpleWaypoint 
	 * was collected. This attribute value can be accessed using the 
	 * "Date & Time Collected" attribute name.
	 * 
	 * When converted to a BasicFeature object the Track object is
	 * represented by a JTS LineString object. The latitudes of the track
	 * points contained in the Track object are converted to the Y ordinates
	 * of the LineString being created. The longitudes of the track
	 * points contained in the Track object are converted to the X ordinates
	 * of the LineString being created. The elevations of the track
	 * points contained in the Track object are converted to the Z ordinates
	 * of the LineString being created.
	 * 
	 * The LineString is constructed using the default GeometryFactory 
	 * from JTS. This means the Point object is built with the 
	 * floating precision model and a spatial-reference ID of 0.
	 */
	public static List<Feature> convertTrackSegmentsToFeature(Track 
			argTarget)
	{
		// Create an empty list that will store the Feature objects
		// created during the conversion.
		LinkedList<Feature> toReturn = new LinkedList<Feature>();
		
		// Store the track name, which will be added as a non-spatial
		// attribute to all of the track segment features that are created.
		String trackName = argTarget.getName();
		
		// Obtain the FeatureSchema.
		FeatureSchema schema = TrackToFeatureConverter
		.getTrackSegmentFeatureSchema();
		
		// Obtain the track segments that will be converted into features.
		List<TrackSegment> segments = argTarget.getTrackSegments();
				
		// DBC Precondition check. Make sure the track contains at least one
		// segment.
		if(segments.size() < 1)
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			("The track must have at least 1 track segment.");
			
			throw toThrow;
		}
		
		Iterator<TrackSegment> goOverEach1 = segments.iterator();
		
		while(goOverEach1.hasNext() == true)
		{
			TrackSegment currentSegment = goOverEach1.next();
			
			// Create the Feature
			BasicFeature currentFeature = new BasicFeature(schema);
			
			currentFeature.setAttribute("Track Name", trackName);
			
			List<Coordinate> coordinates = TrackToFeatureConverter
			.getListOfCoordinatesForTrackSegment(currentSegment);
			
			// DBC precondition. Make sure that the track segment has
			// 2 or more coordinates. If it doesn't, ignore the track
			// segment.
			if(coordinates.size() < 2)
			{
				currentSegment = null;
				currentFeature = null;
				coordinates = null;
				
				break;
			}
			
			// Don't know why toArray method of List is generating a class
			// cast exception.
			Coordinate[] coordinatesInArray = new 
			Coordinate[coordinates.size()];
			
			int counter = 0;
			
			Iterator<Coordinate> goOverEach2 = coordinates.iterator();
			
			while(goOverEach2.hasNext() == true)
			{
				coordinatesInArray[counter] = goOverEach2.next();
				counter++;
			}
			
			CoordinateArraySequence sequence = new CoordinateArraySequence
			(coordinatesInArray);
			
			GeometryFactory factory = new GeometryFactory();	
			LineString geometry = factory.createLineString(sequence);
		
			currentFeature.setGeometry(geometry);
			toReturn.add(currentFeature);
		}
		
		return toReturn;	
	}
	
	public static FeatureSchema getTrackSegmentFeatureSchema()
	{
		FeatureSchema schema = new FeatureSchema();
		
		schema.addAttribute("Geometry", AttributeType.GEOMETRY);
		schema.addAttribute("Track Name", AttributeType.STRING);		
		return schema;
	}
	
	/**
	 * Returns a list of Coordinate objects created from the track points
	 * contained in the TrackSegment object passed as an argument to the 
	 * method.
	 */
	public static List<Coordinate> getListOfCoordinatesForTrackSegment
	(TrackSegment argTarget)
	{
		LinkedList<Coordinate> toReturn = new LinkedList<Coordinate>();
		
		List<SimpleWaypoint> trackPoints = argTarget.getTrackPoints();
		Iterator<SimpleWaypoint> goOverEach = trackPoints.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			SimpleWaypoint currentTrackPoint = goOverEach.next();
			Coordinate currentCoordinate = new Coordinate();
			
			currentCoordinate.y = currentTrackPoint.getLatitude();
			currentCoordinate.x = currentTrackPoint.getLongitude();
			currentCoordinate.z = currentTrackPoint.getElevation();
			
			toReturn.add(currentCoordinate);
		}
		
		return toReturn;
	}
}
