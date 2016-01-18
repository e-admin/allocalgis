/**
 * Gpx10Writer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.satec.gpx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.satec.gpx.gpx10.Gpx;
import com.satec.gpx.interfaces.GpxWriterI;
import com.satec.gpx.utils.Constants;
import com.satec.gpx.utils.GpxNamespacePrefixMapper;

public class Gpx10Writer implements GpxWriterI<Gpx> {
	
	private static Logger log = Logger.getLogger(Gpx10Writer.class);
	
	protected Gpx10Writer() {}
	
	@Override
	public ByteArrayOutputStream convertGpx(Gpx gpx) {
		ByteArrayOutputStream baos = null;
		
		try {
			JAXBContext context = JAXBContext.newInstance(gpx.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, Constants.GPX_SCHEMA_LOCATION);
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new GpxNamespacePrefixMapper());
			
			//Convertir objetos en XML en un array de Bytes...
			baos = new ByteArrayOutputStream();
			m.marshal(gpx, baos);
			log.debug("XML generado...");
			
			//Convertir el array de Bytes a String y quitar los prefijos ":geopista" y "geopista:"
			String xmlStr = baos.toString();
			xmlStr = xmlStr.replaceAll(Constants.XML_PREFIX + ":", "");
			xmlStr = xmlStr.replaceAll(":" + Constants.XML_PREFIX, "");
		
			//Reconvertir String a ByteArrayOutputStream
			baos.reset();
			baos.write(xmlStr.getBytes());
			log.debug("XML generado...");
		} catch (JAXBException e) {
			log.error("Error al convertir objeto a XML." + e.getMessage());
		} catch(IOException e) {
			log.error("Error al convertir objeto a XML." + e.getMessage());
		}
		return baos;
	}

	@Override
	public boolean writeGpxFile(Gpx gpx, String filename) {
		File file = new File(filename);
		return writeGpxFile(gpx, file);
	}
	
	@Override
	public boolean writeGpxFile(Gpx gpx, File file) {
		try {
			//Convertir el objeto a XML
			String xmlStr = convertGpx(gpx).toString();
			
			//Grabar archivo final
			FileOutputStream os = new FileOutputStream(file.getAbsolutePath());
			os.write(xmlStr.getBytes());
			os.close();
			log.debug("Archivo gpx generado: " + file.getAbsolutePath());
		} catch (IOException e) {
			log.error("Error escribiendo archivo " + file.getName());
			return false;
		}
		
		return true;
	}

	@Override
	public String getGpxVersion() {
		return "1.0";
	}
}
