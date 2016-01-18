/**
 * GpxReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.satec.gpx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;

import com.satec.gpx.utils.Constants;

public abstract class GpxReader<T> {
	
	protected Logger log = Logger.getLogger(getClass());
	
	protected GpxReader() {}
	
	public abstract String getGpxVersion();

	protected boolean validateDocumentRemote(InputStream input) {
		SchemaFactory schFact = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL url = null;
		Schema sch = null;
		String remoteUrl = "";
		try {
			if(getGpxVersion().equals("1.0")) {
				remoteUrl = Constants.GPX_1_0_REMOTE_URL;
			} else {
				remoteUrl = Constants.GPX_1_1_REMOTE_URL;
			}
			url = new URL(remoteUrl);
			sch = schFact.newSchema(new StreamSource(url.openStream()));
		
			Validator validator = sch.newValidator();
			validator.validate(new StreamSource(input));
		
			return true;
		} catch(Exception e) {
			System.out.println("Error de Validación por " + e.getMessage());
			log.error("archivo XML no válido contra esquema GPX" + getGpxVersion());
			return false;
		}
	}

	public abstract T readGpxFile(String filename) throws IOException;

	public abstract T readGpxFile(File file) throws IOException;

	public abstract T readGpxFile(File file, InputStream input) throws IOException;
	
}
