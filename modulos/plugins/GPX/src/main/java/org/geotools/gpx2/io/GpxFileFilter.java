/**
 * GpxFileFilter.java
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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import jodd.io.FileNameUtil;

/**
 * Provides a FileFilter object that can be used with a JFileChooser object to
 * filter for GPX files. This filter can be configured to accepts files with the
 * TXT and XML file extensions. GPX file extensions are accepted by default.
 * 
 * @author lblake
 * 
 */
public class GpxFileFilter extends FileFilter {
	boolean acceptTextFiles = false;
	boolean acceptXmlFiles = false;

	public GpxFileFilter(boolean argAcceptTextFiles, boolean argAcceptXmlFiles) {
		this.acceptTextFiles = argAcceptTextFiles;
		this.acceptXmlFiles = argAcceptXmlFiles;
	}

	@Override
	public boolean accept(File argTargetFile) {
		// Get the file extension.
		String fullPath = argTargetFile.getAbsolutePath();
		String extension = FileNameUtil.getExtension(fullPath);

		if (argTargetFile.isDirectory()) return true;
		
		if (extension.equals("gpx") == true) {
			return true;
		}

		if (this.acceptTextFiles == true) {
			if (extension.equals("txt") == true) {
				return true;
			}
		}

		if (this.acceptXmlFiles == true) {
			if (extension.equals("xml") == true) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "GPX Files";
	}

}
