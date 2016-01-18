/**
 * CoordinateConverter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.gps.srs;

/**
 * Clase utilidad para realizar:
 * <ul>
 * <li>Conversión de coordenadas UTM a geodésicas y viceversa</li>
 * <li>Conversión de coordenadas geodésiicas a geocéntricas y viceversa</li>
 * <li>Transformación de coordenadas entre datums</li>
 * </ul>
 * @author jpresa
 */
public class CoordinateConverter {
 
	/**
	 * Elipsoides contemplados
	 */
	public final static int WGS84 = 0;
	public final static int HAYFORD = 1;
	
	/**
	 * Semieje mayor de los elipsoides contemplados
	 */
	private final static double[] ELLIPSOID_A = {6378137.000, 6378388.000};
	/**
	 * Semieje menor de los elipsoides contemplados
	 */
	private final static double[] ELLIPSOID_B = {6356752.3142449996, 6356911.946130};

	/**
	 * Transformaciones entre elipsoides contempladas
	 */
	public final static int SPAIN_MAINLAND_WGS84_ED50 = 0;
	public final static int SPAIN_NORTHWEST_WGS84_ED50 = 1;
	public final static int SPAIN_BALEARIC_ISLANDS_WGS84_ED50 = 2;
	public final static int SPAIN_MAINLAND_ED50_WGS84 = 3;
	public final static int SPAIN_NORTHWEST_ED50_WGS84 = 4;
	public final static int SPAIN_BALEARIC_ISLANDS_ED50_WGS84 = 5;
	/**
	 * Parametros de transformacion entre elipsoides
	 */
	private final static double[][] TRANSFORM_PARAMS =
		{{131.032, 100.251, 163.354, -1.2438, -0.0195, -1.1436, -9.39},
		{178.383, 83.172, 221.293, 0.5401, -0.5319, -0.1263, -21.2},
		{181.4609, 90.2931, 187.1902, 0.1435, 0.4922, -0.3935, -17.57},
		{-131.032, -100.251, -163.354, 1.2438, 0.0195, 1.1436, 9.39},
		{-178.383, -83.172, -221.293, -0.5401, 0.5319, 0.1263, 21.2},
		{-181.4609, -90.2931, -187.1902, -0.1435, -0.4922, 0.3935, 17.57}};

	/**
	 * Conversión de coordenadas geográficas a coordenadas UTM para el elipsoide WGS84.
	 * @param longitude Longitud
	 * @param latitude Latitud
	 * @return El punto UTM calculado
	 */
	public static UTMPoint fromGeodeticToUTM(String longitude, String latitude) {
		return fromGeodeticToUTM(longitude, latitude, WGS84);
    }
	
	/**
	 * Conversión de coordenadas geográficas a coordenadas UTM para el elipsoide WGS84.
	 * @param point Punto en coordenadas geográficas
	 * @return El punto UTM calculado
	 */
	public static UTMPoint fromGeodeticToUTM(GeodeticPoint point) {
		return fromGeodeticToUTM(point, WGS84);
    }

	/**
	 * Conversión de coordenadas geográficas a coordenadas UTM para el elipsoide WGS84.
	 * @param longitude Longitud
	 * @param latitude Latitud
	 * @return El punto UTM calculado
	 */
	public static UTMPoint fromGeodeticToUTM(double longitude, double latitude) {
		return fromGeodeticToUTM(longitude, latitude, WGS84);
    }

	/**
	 * Conversión de coordenadas geográficas a coordenadas UTM.
	 * @param longitude Longitud
	 * @param latitude Latitud
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto UTM calculado
	 */
	public static UTMPoint fromGeodeticToUTM(String longitude, String latitude, int ellipsoid) {
    	try {
			double longitudeAsDouble = Double.valueOf(longitude).doubleValue();
	    	double latitudeAsDouble = Double.valueOf(latitude).doubleValue();
	    	return fromGeodeticToUTM(longitudeAsDouble, latitudeAsDouble, ellipsoid);
    	} catch (NumberFormatException e) {
    		return null;
    	}
    }
	
	/**
	 * Conversión de coordenadas geográficas a coordenadas UTM.
	 * @param point Punto en coordenadas geográficas
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto UTM calculado
	 */
	public static UTMPoint fromGeodeticToUTM(GeodeticPoint point, int ellipsoid) {
		return fromGeodeticToUTM(point.getLongitude(), point.getLatitude(), ellipsoid);
    }

	/**
	 * Conversión de coordenadas geográficas a coordenadas UTM
	 * utilizando las ecuaciones de Coticchia-Surace.
	 * @param longitude Longitud
	 * @param latitude Latitud
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto UTM calculado
	 */
	public static UTMPoint fromGeodeticToUTM(double longitude, double latitude, int ellipsoid) {
		/* Calculos sobre la geometría del elipsoide */
		double a = ELLIPSOID_A[ellipsoid]; // Semieje mayor
		double b = ELLIPSOID_B[ellipsoid]; // Semieje menor
		double e2 = Math.sqrt((a*a)-(b*b))/b; // Segunda excentricidad
		double e2cuad = e2*e2;
		double c = a*a/b; // Radio polar de curvatura
			
		/* Calculos sobre la longitud y la latitud */
		double longRad = longitude*Math.PI/180.0;
		double latRad = latitude*Math.PI/180.0;

		/* Calculos sobre el huso */
		int huso = (int) Math.floor((longitude/6)+31);
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

		return new UTMPoint(x, y, huso);
    }

	/**
	 * Conversión de coordenadas UTM a coordenadas geográficas para el elipsoide WGS84.
	 * @param x Coordenada x
	 * @param y Coordenada y
	 * @param zone Huso o zona UTM
	 * @return El punto en coordenadas geográficas calculado
	 */
	public static GeodeticPoint fromUTMToGeodetic(String x, String y, String zone) {
		try {
			double xd = Double.valueOf(x).doubleValue();
			double yd = Double.valueOf(y).doubleValue();
			int zi = Integer.valueOf(zone).intValue();
			return fromUTMToGeodetic(xd, yd, zi, WGS84);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Conversión de coordenadas UTM a coordenadas geográficas para el elipsoide WGS84.
	 * @param point Punto en coordenadas UTM
	 * @return El punto en coordenadas geográficas calculado
	 */
	public static GeodeticPoint fromUTMToGeodetic(UTMPoint point) {
		return fromUTMToGeodetic(point, WGS84);
	}

	/**
	 * Conversión de coordenadas UTM a coordenadas geográficas para el elipsoide WGS84.
	 * @param x Coordenada x
	 * @param y Coordenada y
	 * @param zone Huso o zona UTM
	 * @return El punto en coordenadas geográficas calculado
	 */
	public static GeodeticPoint fromUTMToGeodetic(double x, double y, int zone) {
		return fromUTMToGeodetic(x, y, zone, WGS84);
	}

	/**
	 * Conversión de coordenadas UTM a coordenadas geográficas.
	 * @param x Coordenada x
	 * @param y Coordenada y
	 * @param zone Huso o zona UTM
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto en coordenadas geográficas calculado
	 */
	public static GeodeticPoint fromUTMToGeodetic(String x, String y, String zone, int ellipsoid) {
		try {
			double xd = Double.valueOf(x).doubleValue();
			double yd = Double.valueOf(y).doubleValue();
			int zi = Integer.valueOf(zone).intValue();
			return fromUTMToGeodetic(xd, yd, zi, ellipsoid);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Conversión de coordenadas UTM a coordenadas geográficas.
	 * @param point Punto en coordenadas UTM
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto en coordenadas geográficas calculado
	 */
	public static GeodeticPoint fromUTMToGeodetic(UTMPoint point, int ellipsoid) {
		return fromUTMToGeodetic(point.getX(), point.getY(), point.getZone(), ellipsoid);
	}

	/**
	 * Conversión de coordenadas UTM a coordenadas geográficas
	 * utilizando las ecuaciones de Coticchia-Surace.
	 * @param x Coordenada x
	 * @param y Coordenada y
	 * @param zone Huso o zona UTM
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto en coordenadas geográficas calculado
	 */
	public static GeodeticPoint fromUTMToGeodetic(double x, double y, int zone, int ellipsoid) {
		/* Calculos sobre la geometría del elipsoide */
		double a = ELLIPSOID_A[ellipsoid]; // Semieje mayor
		double b = ELLIPSOID_B[ellipsoid]; // Semieje menor
		double e2 = Math.sqrt((a*a)-(b*b))/b; // Segunda excentricidad
		double e2cuad = e2*e2;
		double c = a*a/b; // Radio polar de curvatura
			
		/* Tratamiento previo de X e Y */
		x -= 500000; // Eliminar el retranqueo del eje de las X
		// El retranqueo del eje Y lo ignoramos porque suponemos que estamos en el hemisferio Norte
		
		/* Calculo del meridiano central del huso */
		int meridCentral = (zone*6)-183; // Meridiano central del huso

		/* Ecuaciones de Coticchia-Surace */
		/* Calculo de parametros */
		double phi = y/(6366197.724*0.9996);
		double v = (c*0.9996)/Math.sqrt((1+e2cuad*Math.pow(Math.cos(phi), 2)));
		double A = x/v;
		double A1 = Math.sin(2*phi);
		double A2 = A1*Math.pow(Math.cos(phi), 2);
		double J2 = phi+A1/2;
		double J4 = (3*J2+A2)/4;
		double J6 = (5*J4+A2*Math.pow(Math.cos(phi), 2))/3;
		double alfa = e2cuad*3/4;
		double beta = alfa*alfa*5/3;
		double gamma = Math.pow(alfa, 3)*35/27;
		double B0 = 0.9996*c*(phi-(alfa*J2)+(beta*J4)-(gamma*J6));
		double B = (y-B0)/v;
		double xi = (e2cuad/2)*A*A*Math.pow(Math.cos(phi), 2);
		double eps = A*(1-(xi/3));
		double nu = B*(1-xi)+phi;
		double senheps = (Math.exp(eps)-Math.exp(-eps))/2;
		double dlambda = Math.atan(senheps/Math.cos(nu));
		double t = Math.atan(Math.cos(dlambda)*Math.tan(nu));
		
		/* Calculo final de coordenadas */
		double longitud = (dlambda*180/Math.PI)+meridCentral;
		double latitud = phi+(1+e2cuad*Math.pow(Math.cos(phi), 2)-1.5*e2cuad*Math.sin(phi)*Math.cos(phi)*(t-phi))*(t-phi);
		latitud = latitud*180/Math.PI;// pasar a grados decimales

		return new GeodeticPoint(longitud, latitud);
	}
	
	/**
	 * Conversión de coordenadas geodésicas a coordenadas geocéntricas
	 * @param lon longitud
	 * @param lat latitud
	 * @param alt altitud elipsoidal
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto en coordenadas geocéntricas calculado
	 */
	public static ECEFPoint fromGeodeticToECEF(double lon, double lat, double alt, int ellipsoid) {
		double a = ELLIPSOID_A[ellipsoid]; // Semieje mayor
		double b = ELLIPSOID_B[ellipsoid]; // Semieje menor
		double longRad = lon*Math.PI/180.0;
		double latRad = lat*Math.PI/180.0;

		double longSin = Math.sin(longRad);
		double longCos = Math.cos(longRad);
		double latSin = Math.sin(latRad);
		double latCos = Math.cos(latRad);
		double N = (a*a)/Math.sqrt((a*a*latCos*latCos)+(b*b*latSin*latSin));
		double X = (N+alt)*latCos*longCos;
		double Y = (N+alt)*latCos*longSin;
		double Z = (((b*b)/(a*a))*N+alt)*latSin;
		
		return new ECEFPoint(X, Y, Z);
	}

	/**
	 * Conversión de coordenadas geocéntricas a coordenadas geodésicas
	 * @param X coordenada x
	 * @param Y coordenada y
	 * @param Z coordenada z
	 * @param ellipsoid Elipsoide de referencia
	 * @return El punto en coordenadas geodésicas calculado
	 */
	public static GeodeticPoint fromECEFToGeodetic(double X, double Y, double Z, int ellipsoid) {
		double a = ELLIPSOID_A[ellipsoid]; // Semieje mayor
		double b = ELLIPSOID_B[ellipsoid]; // Semieje menor

		double ecuad = ((a*a)-(b*b))/(a*a); // Excentricidad al cuadrado
		double e2cuad = ((a*a)-(b*b))/(b*b); // Segunda excentricidad al cuadrado
		double p = Math.sqrt((X*X)+(Y*Y));
		double theta =Math.atan((Z*a)/(p*b));
		
		// Latitud y longitud geodésicas
		double latRad = Math.atan((Z+e2cuad*b*Math.pow(Math.sin(theta), 3))/(p-ecuad*a*Math.pow(Math.cos(theta), 3)));
		double lonRad = Math.atan(Y/X);
		double lat = latRad*180.0/Math.PI;
		double lon = lonRad*180.0/Math.PI;
		
		double N = (a*a)/Math.sqrt((a*a*Math.pow(Math.cos(latRad), 2))+(b*b*Math.pow(Math.sin(latRad), 2)));
		double alt = (p/Math.cos(latRad))-N;

		return new GeodeticPoint(lon, lat, alt);
	}
	
	/**
	 * Transformación de coordenadas entre datums, usando las ecuaciones de Bursa-Wolf
	 * @param p Punto inicial en coordenadas geocéntricas
	 * @param transformation Indica la zona de la tierra y el par de datums
	 * @return Punto transformado en coordenadas geocéntricas
	 */
	public static ECEFPoint datumTransformation(ECEFPoint p, int transformation) {
		// Desplazamientos
		double incX = TRANSFORM_PARAMS[transformation][0];
		double incY = TRANSFORM_PARAMS[transformation][1];
		double incZ = TRANSFORM_PARAMS[transformation][2];
		// Pasamos los giros de segundos sexagesimales a radianes
		double Rx = (TRANSFORM_PARAMS[transformation][3]*Math.PI)/(60.0*60.0*180.0);
		double Ry = (TRANSFORM_PARAMS[transformation][4]*Math.PI)/(60.0*60.0*180.0);
		double Rz = (TRANSFORM_PARAMS[transformation][5]*Math.PI)/(60.0*60.0*180.0);
		// El factor de escala viene en partes por millon
		double e = TRANSFORM_PARAMS[transformation][6]/1000000.0;
		
		double X = p.getX();
		double Y = p.getY();
		double Z = p.getZ();
		
		double X2 = incX+(1+e)*(X+Rz*Y-Ry*Z);
		double Y2 = incY+(1+e)*(-Rz*X+Y+Rx*Z);
		double Z2 = incZ+(1+e)*(Ry*X-Rx*Y+Z);
		
		return new ECEFPoint(X2, Y2, Z2);
	}
	
	/**
	 * Transformación de coordenadas entre datums, usando las ecuaciones de Bursa-Wolf
	 * @param p Punto inicial en coordenadas geodésicas
	 * @param transformation Indica la zona de la tierra y el par de datums
	 * @return Punto transformado en coordenadas geodésicas
	 */
	public static GeodeticPoint datumTransformation(GeodeticPoint p, int transformation) {
		int ellipsoid = WGS84;
		if (transformation >= SPAIN_MAINLAND_ED50_WGS84) ellipsoid = HAYFORD;
		ECEFPoint ep = fromGeodeticToECEF(p.getLongitude(), p.getLatitude(), p.getAltitude(), ellipsoid);
		ECEFPoint ep2 = datumTransformation(ep, transformation);
		if (transformation < SPAIN_MAINLAND_ED50_WGS84) ellipsoid = HAYFORD;
		else ellipsoid = WGS84;
		return fromECEFToGeodetic(ep2.getX(), ep2.getY(), ep2.getZ(), ellipsoid);
	}

	public static void main(String args[]) {
		double longitud = 3.8;
		double latitud = 43.5;
		System.out.println("Geodésicas iniciales: (" + longitud + ", " + latitud + ")");
		UTMPoint p = fromGeodeticToUTM(longitud, latitud);
		System.out.println("UTM: " + p);
		System.out.println("Geodésicas otra vez: " + fromUTMToGeodetic(p));
		
		System.out.println("De WGS84 a ED50:");
		GeodeticPoint gpWGS84 = new GeodeticPoint(-3.599370456, 39.54635859, 771.76); // Vertice de carbonera
		//GeodeticPoint gpWGS84 = new GeodeticPoint(-5.91601, 43.5479);// La curtidora. Cambiar abajo a NORTHWEST

		System.out.println("Geodésicas WGS84: " + gpWGS84);
		GeodeticPoint gpED50 = datumTransformation(gpWGS84, SPAIN_MAINLAND_WGS84_ED50);
		System.out.println("Geodésicas ED50: " + gpED50);
		System.out.println("WGS84 otra vez: " + datumTransformation(gpED50, SPAIN_MAINLAND_ED50_WGS84));
	}
}
