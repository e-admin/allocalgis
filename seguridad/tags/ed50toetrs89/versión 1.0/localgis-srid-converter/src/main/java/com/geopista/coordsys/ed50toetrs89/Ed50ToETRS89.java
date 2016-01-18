package com.geopista.coordsys.ed50toetrs89;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.cli.ParseException;

import com.geopista.coordsys.ed50toetrs89.info.Header;
import com.geopista.coordsys.ed50toetrs89.info.LonLat;
import com.geopista.coordsys.ed50toetrs89.info.SubGrid;
import com.geopista.instalador.UTMED50ToGeoETRS89Converter;

/**
 * This class defines the main operation to convert coordinates from ED50 to
 * etrs89.
 * 
 * @author
 */

public class Ed50ToETRS89 {

	private static String URLGRIDFILE = "./peninsula.gsb";
	private static SubGrid lastUsedSubGrid = null;

	/**
	 * Constructor.
	 */
	public Ed50ToETRS89() {

	}

	/**
	 * This is the main method that do the coordinates conversion from ED50 to
	 * ETRS89. The coordinates are given as longitude and latitude, and get in
	 * to the method as parameters. It reads the "grid file" and try to locate
	 * the coordinates into any subgrid contained into the file. And tries to do
	 * the conversion, if the conversion is successful retrieves the result as a
	 * {@link LonLat} object, if the conversion fails the it retrieves same
	 * coordinates passed to this method.
	 * 
	 * @param longitude
	 *            The coordinate's longitude to be converted.
	 * @param latitude
	 *            The coordinate's latitude to be converted.
	 * @return A {@link LonLat} object with the coordinates converted. If no
	 *         conversion has been made the same coordinates, the same
	 *         coordinates given as parameters are returned.
	 * @throws IOException
	 * @throws ParseException
	 */
	public LonLat ed50toEtrs89(Double longitude, Double latitude)
			throws IOException, ParseException {
		// vars & types declaration
		longitude = longitude * 3600;
		latitude = latitude * 3600;

		longitude = -longitude; // POSITIVE WEST

		SubGrid selGrid = null;
		LonLat result = new LonLat(longitude, latitude); // resultado de
															// ed50toetrs89

		if (lastUsedSubGrid != null
				&& lastUsedSubGrid.isInside(longitude, latitude)) {
			selGrid = lastUsedSubGrid;
		} else {
			selGrid = findGrid(longitude, latitude);
			lastUsedSubGrid = selGrid;
		}

		if (selGrid != null) {
			double y = (latitude - selGrid.getLatA()) / selGrid.getLatInc();
			double x = (longitude - selGrid.getLonA()) / selGrid.getLongInc();

			// Coeficientes
			float a0 = selGrid.getNodes()[0].getIlat();
			float a1 = selGrid.getNodes()[1].getIlat()
					- selGrid.getNodes()[0].getIlat();
			float a2 = selGrid.getNodes()[2].getIlat()
					- selGrid.getNodes()[0].getIlat();
			float a3 = selGrid.getNodes()[0].getIlat()
					+ selGrid.getNodes()[3].getIlat()
					- selGrid.getNodes()[1].getIlat()
					- selGrid.getNodes()[2].getIlat();
			double ip = a0 + a1 * x + a2 * y + a3 * x * y;
			result.setLatitude(latitude + ip);

			float b0 = selGrid.getNodes()[0].getIlon();
			float b1 = selGrid.getNodes()[1].getIlon()
					- selGrid.getNodes()[0].getIlon();
			float b2 = selGrid.getNodes()[2].getIlon()
					- selGrid.getNodes()[0].getIlon();
			float b3 = selGrid.getNodes()[0].getIlon()
					+ selGrid.getNodes()[3].getIlon()
					- selGrid.getNodes()[1].getIlon()
					- selGrid.getNodes()[2].getIlon();
			double ib = b0 + b1 * x + b2 * y + b3 * x * y;
			result.setLongitude(longitude + ib);

			result.setLatitude(result.getLatitude() / 3600);
			result.setLongitude(result.getLongitude() / 3600);
		}

		return result;
	}

	/**
	 * This method optimizes the accesses to the grid file.
	 * 
	 * @param lon
	 *            The coordinate's longitude to be converted.
	 * @param lat
	 *            The coordinate's latitude to be converted.
	 * @return The grid where is located the coordinate (longitude,latitude) and
	 *         null if the coordinate is not contained in any file's grid.
	 * @throws IOException
	 * @throws ParseException
	 */
	private SubGrid findGrid(Double lon, Double lat) throws IOException  {
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

		File tempGridFile = new File(URLGRIDFILE);
		RandomAccessFile file = new RandomAccessFile(tempGridFile, "r");

		try {

			header.readHeader(file);

			// Selección de la grid adecuada.
			SubGrid selGrid = new SubGrid();

			for (int f = 0; f < header.getNUMFILE(); f++) {
				// Read the grid "i" from file.
				SubGrid readGrid = new SubGrid();
				readGrid.readSubGrid(file);
				readGrid.setPosicion(file.getChannel().position());

				// Calculos fundamentales de la rejilla.
				M = (long) (1 + (readGrid.getNLat() - readGrid.getSLat())
						/ readGrid.getLatInc());
				n = (long) (1 + (readGrid.getWLong() - readGrid.getELong())
						/ readGrid.getLongInc());

				// Calculo de los nodos a interpolar
				i = (long) (1 + (lat - readGrid.getSLat())
						/ readGrid.getLatInc());
				j = (long) (1 + (lon - readGrid.getELong())
						/ readGrid.getLongInc());

				if ((i > 0) && (j > 0) && (i < M) && (j < n)) {
					// The grid readed is the "Rigth grid".
					if (readGrid.getLatInc() < msel) {
						sel = f + 1;
						msel = (int) readGrid.getLatInc();
						selGrid = readGrid;
						isel = i;
						jsel = j;
						nsel = n;
						readGrid = null;
					}
				}
			} // end for reading subgrids from file

			if (sel != 0) {
				// A grid has been selected.
				p[0] = nsel * (isel - 1) + jsel;
				p[1] = p[0] + 1;
				p[2] = p[0] + nsel;
				p[3] = p[2] + 1;
				selGrid.readNodes(file, p, new LonLat(lon, lat));

				double lata = selGrid.getSLat() + (isel - 1)
						* selGrid.getLatInc();
				double lona = selGrid.getELong() + (jsel - 1)
						* selGrid.getLongInc();
				selGrid.setLatA(lata);
				selGrid.setLonA(lona);
				return selGrid;
			} else {

				UTMED50ToGeoETRS89Converter
						.printMessage("La geometría está fuera de las coordenadas de la rejilla");
				//throw new ParseException(
				//		"La geometría está fuera de las coordenadas de la rejilla");
				return null;
			}
		} finally {
			file.close();
		}

	}

}
