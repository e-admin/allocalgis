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

import java.io.File;
import java.util.*;


import org.geotools.gpx2.gpxentities.SimpleWaypoint;
import org.geotools.gpx2.gpxentities.Track;
import org.geotools.gpx2.io.SimpleGpxReader;

import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * Provides a Connection object to GPX files. This allows the GPX plug-in
 * for OpenJUMP to import waypoints and tracks from the GPX file into
 * OpenJUMP.
 */
public class GpxFileConnection implements Connection 
{

	private File targetFile;
	private SimpleGpxReader reader;
	
	/**
	 * Creates a GpxFileConnection object using the GPX file as
	 * its only argument.
	 */
	public GpxFileConnection(File argTarget)
	{
		this.targetFile = argTarget;
		reader = new SimpleGpxReader();
		reader.setUpForReading(targetFile);
	}
	
	@Override
	public void close() 
	{

	}

	@Override
	public FeatureCollection executeQuery(String argQueryString, 
			TaskMonitor argTaskMonitor)
			throws Exception 
	{
		if(argQueryString.equals("Waypoints") == true)
		{
			if(this.targetFileHasWaypoints() == true)
			{
				return this.getWaypointsFromGpxFile();
			}
			
			else
			{
				IllegalStateException toThrow = 
					new IllegalStateException("The selected file" +
							" didn't contain any waypoints.");
				throw toThrow;
			}
		}
		
		else if(argQueryString.equals("Tracks") == true)
		{
			if(this.targetFileHasTracks() == true)
			{
				return this.getTracksFromGpxFile();
			}
			
			else
			{
				IllegalStateException toThrow = 
					new IllegalStateException("The selected file" +
							" didn't contain any tracks.");
				throw toThrow;
			}
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			("The string provided must equal 'Waypoints' or 'Tracks'");
			
			throw toThrow;
		}
	}

	@Override
	public FeatureCollection executeQuery(String argQueryString, 
			Collection argResultHolder,
			TaskMonitor argShowProgress) 
	{
		// Stub
		return null;
	}

	/**
	 * This method is not supported in this Connection implementation.
	 * @return 
	 */
	@Override
	public ArrayList executeUpdate(String argUpdateString, 
			FeatureCollection argInputFeatures,
			TaskMonitor argShowProgress) throws Exception
	{
		return null;
		// Not yet implemented.
	}
	
	/**
	 * Indicates if the source tapped by this connection has
	 * waypoints available.
	 */
	public boolean targetFileHasWaypoints()
	{
		return this.reader.targetDocumentHasWaypoints();
	}
	
	/**
	 * Indicates if the source tapped by this connection has
	 * tracks available.
	 */
	public boolean targetFileHasTracks()
	{
		return this.reader.targetDocumentHasTracks();
	}
	
	private FeatureCollection getTracksFromGpxFile()
	{
		// Create a new GpxReader and obtain waypoints.		
		List<Track> tracks = reader.getTracks();
		
		// Convert Tracks to OpenJUMP Feature objects and add them to a
		// FeatureCollection object.
		Iterator<Track> goOverEach = tracks.iterator();
		
		// Temporary holder for the converted features.
		LinkedList<Feature> features = new LinkedList<Feature>();
		
		while(goOverEach.hasNext() == true)
		{
			Track currentTrack = goOverEach.next();
			List<Feature> currentFeatures = TrackToFeatureConverter
			.convertTrackSegmentsToFeature(currentTrack);
			
			features.addAll(currentFeatures);		
		}
		
		FeatureSchema schema = TrackToFeatureConverter
		.getTrackSegmentFeatureSchema();
		
		FeatureDataset toReturn = new FeatureDataset(features, schema);
		
		return toReturn;
	}
	
	private FeatureCollection getWaypointsFromGpxFile()
	{
		// Create a new GpxReader and obtain waypoints.
		List<SimpleWaypoint> waypoints = reader.getWaypoints();
		
		// Convert Waypoints to OpenJUMP Feature objects and add them to a
		// FeatureCollection object.
		Iterator<SimpleWaypoint> goOverEach = waypoints.iterator();
		
		// Temporary holder for the converted features.
		LinkedList<Feature> features = new LinkedList<Feature>();
		
		while(goOverEach.hasNext() == true)
		{
			SimpleWaypoint currentWaypoint = goOverEach.next();
			Feature currentFeature = SimpleWaypointToFeatureConverter
			.convertSimpleWaypointToFeature(currentWaypoint);
			
			features.add(currentFeature);		
		}
		
		FeatureSchema schema = SimpleWaypointToFeatureConverter
		.getSimpleWaypointFeatureSchema();
		FeatureDataset toReturn = new FeatureDataset(features, schema);
		
		return toReturn;
	}
}
