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

import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;

/**
 * Provides the implementation of a DataSource defined in the OpenJUMP
 * source code needed to import tracks from a GPX data file into
 * OpenJUMP. Note: This class basically exists to provide a GPXFileConnection
 * object. See the GPXFileConnection class javadoc for more information.
 * 
 * @see GPXFileConnection
 */
public class GpxFileDataSource extends DataSource 
{

	private File targetFile;
	
	/**
	 * Creates the GPXFileDataSource object using the target GPX file
	 * passed as the only argument.
	 */
	public GpxFileDataSource(File argTarget)
	{
		this.targetFile = argTarget;
	}
	
	/**
	 * Provides a GPXFileConnection object for this data source.
	 */
	@Override
	public Connection getConnection() 
	{
		// Stub
		return new GpxFileConnection(this.targetFile);
	}
}
