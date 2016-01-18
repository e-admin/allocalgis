/**
 * LocalGISGPSProvider.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis;

import es.satec.gps.GPSController;
import es.satec.gps.GPSInfo;
import es.satec.gps.exceptions.GPSAlreadyStartedException;
import es.satec.gps.exceptions.GPSException;

public class LocalGISGPSProvider implements GPSProvider {

	private GPSController gpsController;
	private boolean started;
	
	public LocalGISGPSProvider(String gpsPropertiesResource) {
		GPSController.init(gpsPropertiesResource);
		gpsController = GPSController.getInstance();
	}
	
	public GPSLocation getGPSLocation() {
		// Para pruebas. punto en herrera de pisuerga
//		return new GPSLocation(-4.323, 42.5881);
		// Para pruebas. punto Aviles
		//return new GPSLocation(-5.91678, 43.54765); // Plaza minusvalidos
		//return new GPSLocation(-5.91694, 43.54813); // Banco de fuera
		//return new GPSLocation(-5.91595, 43.54814); // Bajo nuestra ventana
		//return new GPSLocation(-5.664655,43.391651);
		GPSInfo gpsInfo = gpsController.getGPSInfo();
		if (gpsInfo == null) {
			return null;
		}
		else {
			return new GPSLocation(Double.parseDouble(gpsInfo.getLongitude()),
				Double.parseDouble(gpsInfo.getLatitude()), Double.parseDouble(gpsInfo.getAltitude()),
				gpsInfo.getSpeed(), gpsInfo.getDirection(), gpsInfo.getQuality(), gpsInfo.getNumberOfSatellites());
		}
	}

	public void startGPS() throws GPSAlreadyStartedException, GPSException {
		gpsController.startReading();
		started = true;
	}

	public void stopGPS() {
		gpsController.stopReading();
		started = false;
	}
	
	public boolean isStarted() {
		return started;
	}

}
