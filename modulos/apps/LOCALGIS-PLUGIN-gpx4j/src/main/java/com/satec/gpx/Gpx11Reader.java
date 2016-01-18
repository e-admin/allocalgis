/**
 * Gpx11Reader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.satec.gpx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.satec.gpx.gpx11.GpxType;
import com.satec.gpx.interfaces.GpxReaderI;
import com.satec.gpx.utils.Constants;

public class Gpx11Reader extends GpxReader<GpxType> implements GpxReaderI<GpxType> {
	
	protected Gpx11Reader() {}
		
	@Override
	public GpxType readGpxFile(File file, InputStream input) throws IOException {
		GpxType gpx = null;
		
		//Validación del XML de entrada
		if(!validateDocumentRemote(input)) {
			return null;
		}
		
		//Probamos a leer un GPX versión 1.1
		try {
			JAXBContext context = JAXBContext.newInstance(Constants.GPX11);
			Unmarshaller u = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			JAXBElement<GpxType> gpxElement = (JAXBElement<GpxType>) u.unmarshal(file);
			gpx = gpxElement.getValue();
		} catch(JAXBException e) {
			e.printStackTrace();
			System.out.println("Error leyendo GPX " + getGpxVersion() + " " + e.getMessage());
		}
		return gpx;
	}

	@Override
	public GpxType readGpxFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		return readGpxFile(file, fis);
	}

	@Override
	public GpxType readGpxFile(String filename) throws IOException {
		File file = new File(filename);
		return readGpxFile(file);
	}

	@Override
	public String getGpxVersion() {
		return "1.1";
	}
}
