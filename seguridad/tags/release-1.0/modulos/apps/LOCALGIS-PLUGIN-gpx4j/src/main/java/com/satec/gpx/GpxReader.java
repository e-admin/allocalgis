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
