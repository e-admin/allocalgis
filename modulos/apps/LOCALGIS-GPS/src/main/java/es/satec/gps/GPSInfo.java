/**
 * GPSInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.gps;

import java.io.Serializable;
import java.util.Date;

/**
 * Almacena la información proporcionada por el GPS para una localización
 * concreta
 * 
 * User: Hugo
 * Date: 01-ago-2007
 * Time: 13:00:50
 */
public class GPSInfo implements Serializable {
	private static final long serialVersionUID = 4169138404741261865L;
	private Date time;
    private Date frameTime;
    private String latitude;
    private String longitude;
    private String altitude;
    private String speed;
    private String direction;
    private String quality;
    private int numberOfSatellites;
    private boolean valid = false;
    private String UTMLatitude;
    private String UTMLongitude;
    private String UTMZone;

    public GPSInfo() {
    }

    public GPSInfo(Date time,
                   Date frameTime,
                   String latitude,
                   String longitude,
                   String altitude,
                   String speed,
                   String direction,
                   String quality,
                   int numberOfSatellites, boolean valid) {
        this.time = time;
        this.frameTime = frameTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speed = speed;
        this.direction = direction;
        this.quality = quality;
        this.numberOfSatellites = numberOfSatellites;
        this.valid = valid;
        this.UTMLatitude = null;
        this.UTMLongitude = null;
        this.UTMZone = null;
    }

    /**
     * Retorna la hora, con minutos y segundos a la que se obtuvo por última vez información de localización del dispositivo.
     *
     * @return
     */
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Reorna la hora, con minutos y gegundos que proporcionó el satélite.
     *
     * @return
     */
    public Date getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(Date frameTime) {
        this.frameTime = frameTime;
    }

    /**
     * Retorna la latitud a la que se encuentra el dispositivo móvil. Se indican grados y dirección.
     *
     * @return
     */
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Retorna la longitud a la que se encuentra el dispositivo móvil. Se indican grados y dirección.
     *
     * @return
     */
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Retorna la altitud a la que se encuentra el dispositivo móvil. El valor numérico se acompaña de la unidad de medida
     *
     * @return
     */
    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    /**
     * Retorna la velocidad a la que se desplaza el dispositivo móvil. El valor numérico se acompaña de la unidad de medida.
     *
     * @return
     */
    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * Direccion del movimiento
     *
     * @return
     */
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Indica la calidad de los últimos datos de localización obtenidos.
     *
     * @return
     */
    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * Indica el número de satelites de la toma.
     *
     * @return
     */
    public int getNumberOfSatellites() {
        return numberOfSatellites;
    }

    public void setNumberOfSatellites(int numberOfSatellites) {
        this.numberOfSatellites = numberOfSatellites;
    }

    /**
     * Indica si los últimos datos de localización obtenidos tienen una calidad aceptable. Si no es así, lanzará una excepción indicando tal hecho.
     *
     * @return
     */
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Devuelve la latitud en coordenadas UTM.
     * @return
     */
    public String getUTMLatitude() {
    	if (UTMLatitude==null) calculateUTM();
    	return UTMLatitude;
    }
    
    /**
     * Devuelve la longitud en coordenadas UTM.
     * @return
     */
    public String getUTMLongitude() {
    	if (UTMLongitude==null) calculateUTM();
    	return UTMLongitude;
    }
    
    /**
     * Devuelve el huso geográfico al que se refieren las coordenadas UTM.
     * @return
     */
    public String getUTMZone() {
    	if (UTMZone==null) calculateUTM();
    	return UTMZone;
    }
    
    /**
	 * Transformación de coordenadas geográficas a coordenadas UTM para el elipsoide WGS84,
	 * utilizando las ecuaciones de Coticchia-Surace.
     */
    private void calculateUTM() {
    	try {
	    	/* Longitud y Latitud como doubles */
	    	double longitudeAsDouble = Double.valueOf(longitude).doubleValue();
	    	double latitudeAsDouble = Double.valueOf(latitude).doubleValue();
	
			/* Calculos sobre la geometría del elipsoide WGS84 */
			double a = 6378137.000; // Semieje mayor
			double b = 6356752.3142449996; // Semieje menor
			double e2 = Math.sqrt((a*a)-(b*b))/b; // Segunda excentricidad
			double e2cuad = e2*e2;
			double c = a*a/b; // Radio polar de curvatura
			
			/* Calculos sobre la longitud y la latitud */
			double longRad = longitudeAsDouble*Math.PI/180.0;
			double latRad = latitudeAsDouble*Math.PI/180.0;
			
			/* Calculos sobre el huso */
			int huso = (int) Math.floor((longitudeAsDouble/6)+31);
			int meridCentral = (huso*6)-183; // Meridiano central del huso
			double dAngular = longRad - (meridCentral*Math.PI/180.0); // Distancia angular
			
			/* Ecuaciones de Coticchia-Surace */
			/* Calculo de parametros */
			double A = Math.cos(latRad)*Math.sin(dAngular);
			double eps = 0.5*Math.log((1+A)/(1-A));
			double n = Math.atan(Math.tan(latRad)/Math.cos(dAngular))-latRad;
			double v = (c*0.9996)/Math.sqrt((1+e2cuad*Math.pow(Math.cos(latRad), 2)));
			double phi = (e2cuad/2)*eps*eps*Math.pow(Math.cos(latRad), 2);
			double A1 = Math.sin(2*latRad);
			double A2 = A1*Math.pow(Math.cos(latRad), 2);
			double J2 = latRad+A1/2;
			double J4 = (3*J2+A2)/4;
			double J6 = (5*J4+A2*Math.pow(Math.cos(latRad), 2))/3;
			double alfa = e2cuad*3/4;
			double beta = alfa*alfa*5/3;
			double gamma = Math.pow(alfa, 3)*35/27;
			double B0 = 0.9996*c*(latRad-(alfa*J2)+(beta*J4)-(gamma*J6));
			
			/* Calculo final de coordenadas */
			double x = eps*v*(1+(phi/3))+500000;
			double y = n*v*(1+phi)+B0;
			if (y<0) y += 10000000;
			
			UTMLatitude = String.valueOf(y);
			UTMLongitude = String.valueOf(x);
			UTMZone = String.valueOf(huso);
    	} catch (Exception e) {
    		UTMLatitude = "";
    		UTMLongitude = "";
    		UTMZone = "";
    	}
    }

    public String toString() {
        return "GPSInfo{" +
                "time=" + time +
                ", frameTime=" + frameTime +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", altitude='" + altitude + '\'' +
                ", speed='" + speed + '\'' +
                ", direction='" + direction + '\'' +
                ", quality='" + quality + '\'' +
                ", numberOfSatellites=" + numberOfSatellites +
                ", valid=" + valid +
                '}';
    }
}
