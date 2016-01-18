/*
 * Project Name: GeoTools GPX Support
 * Original Organization Name: The SurveyOs Project
 * Original Programmer Name: The Sunburned Surveyor
 * Current Maintainer Name: The SurveyOS Project
 * Current Maintainer Contact Information
 *    E-Mail Address: The Sunburned Surveyor
 * Copyright Holder: The SurveyOS Project
 * Date Last Modified: Sep 19, 2008
 * Current Version Number: 00.00.01
 * IDE Name: Eclipse
 * IDE Version: 3.2.1
 * Type: Java Class
 */
package net.surveyos.sourceforge.openjump.plugins.gpx;

import java.util.*;

import org.geotools.gpx2.gpxentities.SimpleWaypoint;
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * Provides a static method that can be used to obtain an implementation
 * of the OpenJUMP Feature interface based on the SimpleWaypoint object
 * provided to the method. The implementation currently returned is an instance
 * of the OpenJUMP BasicFeature class.
 */
public class SimpleWaypointToFeatureConverter
{
	
	/**
	 * Creates and returns a BasicFeature object based on the SimpleWaypoint
	 * that is provided. The BasicFeature object has two non-spatial attributes.
	 * The first is the name of the SimpleWaypoint. This attribute can be 
	 * accessed using the "Waypoint Name" attribute name. The second is a 
	 * JDK Date object representing the instant the SimpleWaypoint was collected.
	 * This attribute value can be accessed using the "Date & Time Collected"
	 * attribute name.
	 * 
	 * When converted to a BasicFeature object the SimpleWaypoint object is
	 * represented by a JTS Point object. The latitude of the SimpleWaypoint 
	 * object is used as the Point's Y ordinate. The longitude of the 
	 * SimpleWaypoint object is used as the Point's X ordinate. The elevation
	 * of the SimpleWaypoint object is used as the Point's elevation. The
	 * point is constructed using the defaul GeometryFactory from JTS. This
	 * means the Point object is built with the floating precision model
	 * and a spatial-reference ID of 0.
	 */
	public static Feature convertSimpleWaypointToFeature(SimpleWaypoint argTarget)
	{
		// Obtain the FeatureSchema.
		FeatureSchema schema = SimpleWaypointToFeatureConverter.getSimpleWaypointFeatureSchema();
		
		// Create the Feature
		BasicFeature toReturn = new BasicFeature(schema);
		
		
		// Set feature attributes.
		String waypointName = argTarget.getName();
		toReturn.setAttribute("Waypoint Name", waypointName);
                
                boolean hasDateTime = argTarget.hasDateAndTimeCollected();
                
                // Ignore the lack of a date and time. Otherwise and exception
                // will be thrown when we try to access the date and time.
                if(hasDateTime == true)
                {
                    DateTime jodaDate = argTarget.getDateAndTimeCollected();
                    Date jdkDate = jodaDate.toDate();
                    toReturn.setAttribute("Date & Time Collected", jdkDate);
                }
		
		// Create and set feature geometry.
		Coordinate waypointLocation = new Coordinate();
		waypointLocation.x = argTarget.getLongitude();
		waypointLocation.y = argTarget.getLatitude();
		waypointLocation.z = argTarget.getElevation();
		
		GeometryFactory factory = new GeometryFactory();	
		Point geometry = factory.createPoint(waypointLocation);
		
		toReturn.setGeometry(geometry);
		
		return toReturn;
	}
	
	/**
	 * Creates a FeatureSchema representing the properties of a SimpleWaypoint
	 * object as a BasicFeature.
	 */
	public static FeatureSchema getSimpleWaypointFeatureSchema()
	{
		FeatureSchema schema = new FeatureSchema();
		
		schema.addAttribute("Geometry", AttributeType.GEOMETRY);
		schema.addAttribute("Waypoint Name", AttributeType.STRING);
		schema.addAttribute("Date & Time Collected", AttributeType.DATE);
		
		return schema;
	}
	
}
