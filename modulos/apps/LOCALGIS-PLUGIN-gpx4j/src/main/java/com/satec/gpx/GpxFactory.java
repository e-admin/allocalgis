/**
 * GpxFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.satec.gpx;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.satec.gpx.interfaces.GpxReaderI;
import com.satec.gpx.interfaces.GpxWriterI;

public class GpxFactory {
	
	private GpxReaderI<?> reader;
	private GpxWriterI<?> writer;
	
	private static final String TOKEN = "http://www.topografix.com/GPX/1/";
	private static final String GPX11 = "http://www.topografix.com/GPX/1/1";
	private static final String GPX10 = "http://www.topografix.com/GPX/1/0";

	@SuppressWarnings("unused")
	private GpxFactory() {}
	
	public GpxFactory(String filename) throws IOException {
		File file = new File(filename);
		checkGpxVersion(file);
	}
	
	public GpxFactory(File file) throws IOException {
		checkGpxVersion(file);
	}
	
	private void checkGpxVersion(File file) throws IOException {
		Scanner scan = new Scanner(file);
		String linea = "";
		while(scan.hasNextLine()) {
			linea = scan.nextLine();
			if(linea.indexOf(TOKEN) != -1) {
				if(linea.indexOf(GPX11) != -1) {
					reader = new Gpx11Reader();
					writer = new Gpx11Writer();
				}
				if(linea.indexOf(GPX10) != -1) {
					reader = new Gpx10Reader();
					writer = new Gpx10Writer();
				}
				scan.close();
				return;
			}
		}
	}

	public GpxReaderI<?> getReader() {
		return reader;
	}

	public GpxWriterI<?> getWriter() {
		return writer;
	}
}
