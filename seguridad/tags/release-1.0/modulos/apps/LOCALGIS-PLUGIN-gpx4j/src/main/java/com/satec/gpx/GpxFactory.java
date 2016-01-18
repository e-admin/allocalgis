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
