package com.satec.gpx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.satec.gpx.gpx10.Gpx;
import com.satec.gpx.interfaces.GpxReaderI;
import com.satec.gpx.utils.Constants;

public class Gpx10Reader extends GpxReader<Gpx> implements GpxReaderI<Gpx> {
	
	protected Gpx10Reader() {}

	@Override
	public Gpx readGpxFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		return readGpxFile(file, fis);
	}

	@Override
	public Gpx readGpxFile(String filename) throws IOException {
		File file = new File(filename);
		return readGpxFile(file);
	}

	@Override
	public Gpx readGpxFile(File file, InputStream input) throws IOException {
		Gpx gpx = null;
		
		//Validación del XML de entrada
		if(!validateDocumentRemote(input)) {
			return null;
		}
		
		//Probamos a leer un GPX versión 1.0
		try {
			JAXBContext context = JAXBContext.newInstance(Constants.GPX10);
			Unmarshaller u = context.createUnmarshaller();
			gpx = (Gpx) u.unmarshal(file);
		} catch(JAXBException e) {
			e.printStackTrace();
			System.out.println("Error leyendo GPX " + getGpxVersion() + " " + e.getMessage());
		}
		return gpx;
	}

	@Override
	public String getGpxVersion() {
		return "1.0";
	}
}
