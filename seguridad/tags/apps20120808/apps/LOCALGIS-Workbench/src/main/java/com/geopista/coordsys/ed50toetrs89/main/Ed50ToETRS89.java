package com.geopista.coordsys.ed50toetrs89.main;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarFile;

import com.geopista.app.AppContext;
import com.geopista.coordsys.ed50toetrs89.info.Header;
import com.geopista.coordsys.ed50toetrs89.info.LonLat;
import com.geopista.coordsys.ed50toetrs89.info.SubGrid;
import com.geopista.coordsys.ed50toetrs89.info.UtilsToReadGridFile;

/**
 * This class defines the main operation to convert coordinates from ED50 to etrs89.
 * @author javieraragon.
 */

public class Ed50ToETRS89 {

	static String FILENAME = "peninsula.gsb";
	static String URLGRIDFILE = "c:\\peninsula.gsb";
	static SubGrid lastUsedSubGrid=null;


	/**
	 *  Constructor.
	 * @param filename The grid file name to make the conversion.
	 */
	public Ed50ToETRS89(String filename) {
		// TODO Auto-generated constructor stub
		super();
		
		// sets the grid file name to 'filename' parametes
		Ed50ToETRS89.FILENAME = filename;

		// sets the base directory where is going to be located the local grid file. 
		String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
		File dir = new File(base,"tranformaciones");
		if(! dir.exists() ){
			dir.mkdirs();
		}

		// look for 'filenane' on base directory.
		if(!(new File( base,Ed50ToETRS89.FILENAME)).exists()){
			URL urlFile = Ed50ToETRS89.class.getResource(FILENAME);
			if (urlFile.getProtocol().equals("jar")){
				// if the file does not exist into data directory.
				//Read grid file from jar. and copy it to data directory.
				CretateFileFromJar(urlFile,dir.getAbsolutePath());
			} else {
				// The file does not exists and is not in a jar File.
				// Try copy it drirectly into default data path.
				CreateFile(urlFile, base);
			}
		}

		// finally set the URL of the grid file.
		Ed50ToETRS89.URLGRIDFILE = new File(dir,Ed50ToETRS89.FILENAME).getAbsolutePath();
	}

	/**
	 * Constructor.
	 */
	public Ed50ToETRS89() {
		// TODO Auto-generated constructor stub
		super();

		// gets filename property from Geopista.properties file
		Ed50ToETRS89.FILENAME = readFileNameProperty();

		// sets the base directory where is going to be located the local grid file.
		String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
		File dir = new File(base,"tranformaciones");
		if(! dir.exists() ){
			dir.mkdirs();
		}

		// look for 'filenane' on base directory.
		if(!(new File( base,Ed50ToETRS89.FILENAME)).exists()){
			URL urlFile = Ed50ToETRS89.class.getResource(FILENAME);
			if (urlFile.getProtocol().equals("jar")){
				// The file does not exist into data directory.
				//Read grid file from jar. and copy it to data directory.
				CretateFileFromJar(urlFile,dir.getAbsolutePath());
			} else {
				// The file does not exists and is not in a jar File.
				// Try copy it drirectly into default data path.
				CreateFile(urlFile, dir.getAbsolutePath());
			}
		}
		
		// finally set the URL of the grid file.
		Ed50ToETRS89.URLGRIDFILE = new File(dir,Ed50ToETRS89.FILENAME).getAbsolutePath();
	}




	/** 
	 * This method takes a file from a jar file (the jar and the file are in a {@link URL}) and copy the file that is contained
	 * in the jar to the base directory passed as a parameter.
	 * 
	 * @param url {@link URL} with the path file that is going to be copied, which contains jar path and file path inside the jar.
	 * @param baseDirectory The absolute path where is going to be located the new copied file. 
	 * @return true if the file has been created correctly and false in any other case.
	 */
	private boolean CretateFileFromJar(URL url,String baseDirectory){

		try {
			//find "filename" into resources
			String tempUrl = url.toString();
			// the file's path into jar's directory tree
			String insideJarPath = tempUrl.substring(tempUrl.lastIndexOf("!")+2);
			// the jar's path
			String jarPath = tempUrl.substring(0, tempUrl.lastIndexOf("!")+2 );

			// Creates the jar file, with jarUrlConnection.
			URL jarUrl = new URL(jarPath);
			JarURLConnection connectionJarFile = (JarURLConnection)jarUrl.openConnection();
			JarFile jarfile = connectionJarFile.getJarFile();

			// Creates the temp file into default data directory.
			File tempGridFile = new File(baseDirectory,Ed50ToETRS89.FILENAME);

			// Copy the source file to tempFile
			UtilsToReadGridFile.copyFileFromJarToFile(jarfile, insideJarPath, tempGridFile);
			
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	
	
	/**
	 * This method takes a file from a url and copy the it to the base directory passed as a parameter.
	 * 
	 * @param url {@link URL} with the path file that is going to be copied.
	 * @param baseDirectory The absolute path where is going to be located the new copied file.
	 * @return true if the File has been created correctly and false in any other case.
	 */
	private boolean CreateFile (URL url, String baseDirectory){
			File f2 = new File (baseDirectory,Ed50ToETRS89.FILENAME);
			return 	UtilsToReadGridFile.copyfile(url, f2);
	}


	/**
	 * This is the main method that do the coordinates conversion from ED50 to ETRS89. The coordinates are given as longitude and latitude, 
	 * and get in to the method as parameters.
	 * It reads the "grid file" and try to locate the coordinates into any subgrid contained into the file.
	 * And tries to do the conversion, if the conversion is successful retrieves the result as a {@link LonLat} object,
	 * if the conversion fails the it retrieves same coordinates passed to this method.  
	 *  
	 * @param longitude The coordinate's longitude to be converted. 
	 * @param latitude The coordinate's latitude to be converted.
	 * @return A {@link LonLat} object with the coordinates converted. If no conversion has been made the same coordinates, the same coordinates given as parameters are returned. 
	 */
	public LonLat ed50toEtrs89 (Double longitude, Double latitude){
		// vars & types declaration
		longitude=longitude*3600;
		latitude=latitude*3600;

		longitude = -longitude;  //POSITIVE WEST

		SubGrid selGrid=null;
		LonLat result = new LonLat(longitude ,latitude); //resultado de ed50toetrs89

		if (lastUsedSubGrid!=null && lastUsedSubGrid.isInside(longitude,latitude))
			selGrid=lastUsedSubGrid;
		else
		{
			selGrid=findGrid(longitude,latitude);
			lastUsedSubGrid=selGrid;
		}


		if(selGrid != null){
			double y = (latitude - selGrid.getLatA()) / selGrid.getLatInc();
			double x = (longitude - selGrid.getLonA()) / selGrid.getLongInc();

			//Coeficientes
			float a0 = selGrid.getNodes()[0].getIlat();
			float a1 = selGrid.getNodes()[1].getIlat() - selGrid.getNodes()[0].getIlat();
			float a2 = selGrid.getNodes()[2].getIlat() - selGrid.getNodes()[0].getIlat();
			float a3 = selGrid.getNodes()[0].getIlat() + selGrid.getNodes()[3].getIlat() - selGrid.getNodes()[1].getIlat() - selGrid.getNodes()[2].getIlat();
			double ip = a0 + a1 * x + a2 * y + a3 * x * y;
			result.setLatitude(latitude + ip);

			float b0 = selGrid.getNodes()[0].getIlon();
			float b1 = selGrid.getNodes()[1].getIlon() - selGrid.getNodes()[0].getIlon();
			float b2 = selGrid.getNodes()[2].getIlon() - selGrid.getNodes()[0].getIlon();
			float b3 = selGrid.getNodes()[0].getIlon() + selGrid.getNodes()[3].getIlon() - selGrid.getNodes()[1].getIlon() - selGrid.getNodes()[2].getIlon();
			double ib = b0 + b1 * x + b2 * y + b3 * x * y;
			result.setLongitude(longitude + ib);

			result.setLatitude(result.getLatitude()/3600);
			result.setLongitude(result.getLongitude()/3600);
		} 


		return result;
	}

	/**
	 * This method optimizes the accesses to the grid file.
	 * @param lon The coordinate's longitude to be converted. 
	 * @param lat The coordinate's latitude to be converted.
	 * @return The grid where is located the coordinate (longitude,latitude) and null if the coordinate is not contained in any file's grid.
	 */
	private SubGrid findGrid(Double lon, Double lat) {
		int msel = 1000000;
		Header header = new Header();

		float i;
		float j;
		float M;
		float n;
		float isel = 0;
		float jsel = 0;
		float nsel = 0;
		float[] p = new float[4];

		int sel = 0;

		// RandomAccessFile--> read from FileName.
		//Open file & read data.
		// read a "Header" from file and assign it to header var.
		try {

			File tempGridFile = new File(Ed50ToETRS89.URLGRIDFILE);

			RandomAccessFile file = new RandomAccessFile(tempGridFile,"r");
			header.readHeader(file);


			//Selección de la grid adecuada.
			SubGrid selGrid = new SubGrid();

			//log4j.debug("Number of grids: " + header.getNUMFILE());
			for(int f=0; f < header.getNUMFILE(); f++){
				//Read the grid "i" from file.
				SubGrid readGrid = new SubGrid();
				readGrid.readSubGrid(file);
				readGrid.setPosicion(file.getChannel().position());


				//Calculos fundamentales de la rejilla.
				M =  (long) (1 + (readGrid.getNLat() - readGrid.getSLat())/readGrid.getLatInc()) ;
				n =  (long) (1 + (readGrid.getWLong() - readGrid.getELong()) / readGrid.getLongInc()) ;

				//Calculo de los nodos a interpolar
				i = (long) (1 + (lat - readGrid.getSLat()) / readGrid.getLatInc());
				j = (long) (1 + (lon - readGrid.getELong()) / readGrid.getLongInc()) ;


				if ( (i>0) && (j>0) && (i<M) && (j<n) ){ 
					//The grid readed is the "Rigth grid".
					if( readGrid.getLatInc() < msel ){
						sel= f + 1;
						msel = (int) readGrid.getLatInc();
						selGrid = readGrid;
						isel = i;
						jsel = j;
						nsel = n;
						readGrid = null;
					}
				}
			} //end for reading subgrids from file

			if (sel != 0){
				//A grid has been selected.
				p[0] = nsel * (isel - 1) + jsel;
				p[1] = p[0] + 1;
				p[2] = p[0] + nsel;
				p[3] = p[2] + 1;
				selGrid.readNodes(file,p, new LonLat(lon,lat));
				
				file.close();
				double lata = selGrid.getSLat() + (isel - 1) * selGrid.getLatInc();
				double lona = selGrid.getELong() + (jsel - 1) * selGrid.getLongInc();
				selGrid.setLatA(lata);
				selGrid.setLonA(lona);
				return selGrid;
			} else {
				System.out.println("La rejilla no ha sido seleccionada");
				file.close();
			}
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		return null; 
	}


	/**
	 * @return a String with the "localgis.cordsys.gridfile" property from Geopista.properties
	 */
	private String readFileNameProperty(){
		try {
			Properties propers = new Properties();
			propers.load(Ed50ToETRS89.class
					.getResourceAsStream("/GeoPista.properties"));
			return propers.getProperty("localgis.cordsys.gridfile");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


}
