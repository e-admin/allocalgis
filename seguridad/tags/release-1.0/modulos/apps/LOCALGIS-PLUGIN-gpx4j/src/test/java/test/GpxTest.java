package test;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.satec.gpx.GpxFactory;
import com.satec.gpx.GpxGenerator;
import com.satec.gpx.gpx10.Gpx;
import com.satec.gpx.gpx11.GpxType;
import com.satec.gpx.interfaces.GpxReaderI;
import com.satec.gpx.interfaces.GpxWriterI;

public class GpxTest {
	
	private static Logger log = Logger.getLogger(GpxTest.class);
	private static final String LINESTRING_FILENAME = "c:/temporal/linestring.gpx";
	private static final String LINESTRING = "LINESTRING(-6.77552007227662 43.2317482286362,-6.77218708551393 43.2299303770645,-6.76795178668716 43.2302925597119,-6.76669134790622 43.2295353673481,-6.76304283643756 43.2304844263899)";
	
	private static final String MULTILINESTRING_FILENAME = "c:/temporal/multilinestring.gpx";
	private static final String MULTILINESTRING = "MULTILINESTRING((-6.73995858550829 43.2577136685972,-6.73240301650213 43.2427428019755,-6.73978244512561 43.2271114008763,-6.71824282331451 43.2296478483677),(-6.71824282331451 43.2296478483677,-6.70955778520587 43.2310997599865,-6.70247496786597 43.2281846402347,-6.69812574520912 43.236033721624,-6.70343847642728 43.240825991023,-6.72315189155007 43.2417939838772,-6.72939846181696 43.2532014934249,-6.72514327662922 43.2585673032272))";
	
	private static final String GPX10FILE2READ = "gpx10_read.gpx"; 
	private static final String GPX11FILE2READ = "gpx11_read.gpx";
	private static final String GPX10FILE2WRITE = "c:/temporal/gpx10writetest.gpx";
	private static final String GPX11FILE2WRITE = "c:/temporal/gpx11writetest.gpx";
	
	@Ignore
	@Test
	public void generateGpxFileFromLinestring() throws Exception {
		//Datos
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(LINESTRING);
		
		GpxGenerator gpxGen = new GpxGenerator();
		if(gpxGen.generateGpxFile(lista, LINESTRING_FILENAME)) {
			log.debug("OK");
		} else {
			throw new Exception("Mal");
		}
	}
	
	@Test
	public void generateGpxFileFromMultilinestring() throws Exception {
		//Datos
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(MULTILINESTRING);
		
		GpxGenerator gpxGen = new GpxGenerator();
		if(gpxGen.generateGpxFile(lista, MULTILINESTRING_FILENAME)) {
			log.debug("OK");
		} else {
			throw new Exception("Mal");
		}
	}
	
	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void gpx10ReadWrite() throws IOException {
		//Get the file
		String filename = getFilename(GPX10FILE2READ);
		
		//Invoke factory to get reader
		GpxFactory factory = new GpxFactory(filename);
		GpxReaderI<Gpx> iReader = (GpxReaderI<Gpx>) factory.getReader();
		System.out.println("Reader " + iReader.getClass().getSimpleName() + " para versión " + iReader.getGpxVersion());
		
		//Read gpx into object
		Gpx gpx = iReader.readGpxFile(filename);
		System.out.println("Leido GPX versión " + gpx.getVersion());
		
		//Write object into new gpx file
		GpxWriterI<Gpx> iwriter = (GpxWriterI<Gpx>) factory.getWriter();
		System.out.println("Escrito GPX" + gpx.getVersion() + "..." + iwriter.writeGpxFile(gpx, GPX10FILE2WRITE));
	}
	
	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void gpx11ReadWrite() throws IOException {
		//Get the file
		String filename = getFilename(GPX11FILE2READ);
		
		//Invoke factory to get reader
		GpxFactory factory = new GpxFactory(filename);
		GpxReaderI<GpxType> iReader = (GpxReaderI<GpxType>) factory.getReader();
		System.out.println("Reader " + iReader.getClass().getSimpleName() + " para versión " + iReader.getGpxVersion());
		
		//Read gpx into object
		GpxType gpx = iReader.readGpxFile(filename);
		System.out.println("Leido GPX versión " + gpx.getVersion());
		
		//Write object into new gpx file
		GpxWriterI<GpxType> writer = (GpxWriterI<GpxType>) factory.getWriter();
		System.out.println("Escrito GPX" + gpx.getVersion() + "..." + writer.writeGpxFile(gpx, GPX11FILE2WRITE));
		
	}
	
	private String getFilename(String filename) throws MalformedURLException {
		URL url = this.getClass().getResource("/" + filename);
		String file = url.getFile();
		return file;
	}
}
