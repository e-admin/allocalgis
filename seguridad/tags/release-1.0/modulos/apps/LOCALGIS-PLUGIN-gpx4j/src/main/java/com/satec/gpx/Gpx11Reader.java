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
