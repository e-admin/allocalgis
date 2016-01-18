package es.satec.gps;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * User: Hugo
 * Date: 02-ago-2007
 * Time: 14:18:18
 */
public class GPSDecoder extends Thread {
    private static Logger log = (Logger) Logger.getInstance(GPSDecoder.class);
//    private static Monitor monitor = MonitorFactory.getInstance();

    private final static String POSITIONING = "$GPGGA";
    private final static String SPEED = "$GPVTG";
    private final static String MINIMUM = "$GPRMC";
    private final static String CHECKSUM_CHAR = "*";

    private final static String NORTH_LATITUDE = "N";
    private final static String SOUTH_LATITUDE = "S";
    private final static String EAST_LONGITUDE = "E";
    private final static String WEST_LONGITUDE = "W";

    private final static String GPRMC_QUALITY_VALID = "A";
    private final static String GPRMC_QUALITY_INVALID = "V";

	private static final int MAX_FRAMES = 100;

	private long validTime = 5000;

    private Date arrivalDate;
    private String time;
    private String fixQuality;
    private String latitude;
    private String longitude;
    private String speed;
    private String direction;
    private String date;
    private int numberOfSatellites;
    private String altitude;

    private final static SimpleDateFormat sdfFrameDateFormat = new SimpleDateFormat("ddMMyyHHmmss");
    
    private boolean active;
    private Vector frameBuffer;

    private static GPSDecoder instance;

    private GPSDecoder() {
    	active = true;
    	frameBuffer = new Vector();
    	this.start();
    }

    public static GPSDecoder getInstance() {
        if (instance == null)
            instance = new GPSDecoder();
        return instance;
    }

    public void decode(String sGPSFrame) {
//        log.debug("Decodificando trama GPS: " + sGPSFrame);
//        monitor.debug("Decodificando trama GPS" + sGPSFrame);
    	try {
	    	int ini = sGPSFrame.lastIndexOf(POSITIONING); //indexOf(POSITIONING);
	        if (ini >= 0) {
	            int fin = sGPSFrame.indexOf(CHECKSUM_CHAR, ini);
	            String sPositionSentence = sGPSFrame.substring(ini, fin + 2);
	            decodeGPGGA(sPositionSentence);
	        } else {
	            ini = sGPSFrame.indexOf(SPEED);
	            if (ini >= 0) {
	                int fin = sGPSFrame.indexOf(CHECKSUM_CHAR, ini);
	                String sSpeedSentence = sGPSFrame.substring(ini, (fin + 2));
	                decodeGPVTG(sSpeedSentence);
	            } else {
	                ini = sGPSFrame.indexOf(MINIMUM);
	                if (ini >= 0) {
	                    int fin = sGPSFrame.indexOf(CHECKSUM_CHAR, ini);
	                    String sMinimumSentence = sGPSFrame.substring(ini, (fin + 2));
	                    decodeGPRMC(sMinimumSentence);
	                }
	            }
	        }
    	} catch (Exception e) {
    		log.error("Error al decodificar trama: " + e.getMessage(), e);
    	}
    }

    public GPSInfo getGPSInfo() {
        try {
            if (fixQuality != null) {
                GPSInfo gpsiRet = new GPSInfo();
                gpsiRet.setTime(arrivalDate);
                if (date != null && date.trim().length() > 0 && time != null && time.trim().length() > 0) {
                    gpsiRet.setFrameTime(sdfFrameDateFormat.parse(date + time));
                } else {
                    gpsiRet.setFrameTime(null);
                }
                gpsiRet.setQuality(fixQuality);
                gpsiRet.setLatitude(latitude);
                gpsiRet.setLongitude(longitude);
                gpsiRet.setSpeed(speed);
                gpsiRet.setDirection(direction);
                gpsiRet.setNumberOfSatellites(numberOfSatellites);
                gpsiRet.setAltitude(altitude);

                long lTime = Long.MAX_VALUE;
                if (arrivalDate != null)
                    lTime = arrivalDate.getTime();
                long lDiferenceTime = System.currentTimeMillis() - lTime;
                gpsiRet.setValid(latitude != null && longitude != null && lDiferenceTime <= validTime);
                if (!gpsiRet.isValid()) {
                    log.debug("La posicion no es valida. latitud: " + latitude + " longitud: " + longitude + " DiferenceTime: " + lDiferenceTime + " validTime: " + validTime);
//                    monitor.debug("La posicion no es valida. latitud: " + latitude + " longitud: " + longitude + " DiferenceTime: " + lDiferenceTime + " validTime: " + validTime);
                }
                return gpsiRet;
            }
        } catch (Exception e) {
            log.error("Error preparando objeto GPSInfo: " + e.getMessage(), e);
        }
        return null;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    /**
     * Deocdifica una trama tipo GPGGA
     * <p/>
     * $GPGGA,<hhmmss_utc>,<ddmm.mmmm_lat>,<N|S>,<dddmm.mmmm_long>,<E|W>,<0|1>,
     * <00-12_numsats_in_use>,<0.5-99.9_horiz_DOP>,<antenna_height>,M,
     * <geoidal_hght>,M,,*hhcrlf
     *
     * @param sGPGGAFrame Trama
     */
    private synchronized void decodeGPGGA(String sGPGGAFrame) {
        log.debug("Decodificando trama GPGGA: " + sGPGGAFrame);
//        monitor.debug("Decodificando trama GPGGA: " + sGPGGAFrame);
        arrivalDate = new Date();
        int iChecksumPos = sGPGGAFrame.indexOf(CHECKSUM_CHAR);
        if (iChecksumPos > 0) {
            String sChecksum = sGPGGAFrame.substring(iChecksumPos + 1);
            sGPGGAFrame = sGPGGAFrame.substring(0, iChecksumPos);
        }
        Vector vTokenizer = stringTokenizer(sGPGGAFrame, ",");
        try {
            time = (String) vTokenizer.elementAt(1);
            latitude = transformLatitudeFormat((String) vTokenizer.elementAt(2));
            String sHemisferio = (String) vTokenizer.elementAt(3);
            if (sHemisferio != null && sHemisferio.equalsIgnoreCase(SOUTH_LATITUDE)) {
                latitude = "-" + latitude;
            }
            longitude = transformLongitudeFormat((String) vTokenizer.elementAt(4));
            String sArea = (String) vTokenizer.elementAt(5);
            if (sArea.equalsIgnoreCase(WEST_LONGITUDE)) {
                longitude = "-" + longitude;
            }
            String sNumberSats = (String) vTokenizer.elementAt(7);
            if (sNumberSats != null && sNumberSats.length() > 0)
                numberOfSatellites = Integer.parseInt(sNumberSats);
            altitude = (String) vTokenizer.elementAt(9);
        } catch (Exception e) {
            log.error("Error parseando dato: " + e.getMessage(), e);
//            monitor.error("Error parseando dato: " + e.getMessage(), e);
        }
//        int iTokenSize = vTokenizer.size();
//        for (int iTokenPos = 0; iTokenPos < iTokenSize; iTokenPos++) {
//            String sToken = (String) vTokenizer.elementAt(iTokenPos);
//            try {
//                switch (iTokenPos) {
//                    case 1: //Hora
//                        time = sToken;
//                        break;
//                    case 2: //Latitud
//                        latitude = transformLatitudeFormat(sToken);
//                        break;
//                    case 3: //Hemisferio (N ó S)
//                        if (sToken.equalsIgnoreCase(SOUTH_LATITUDE)) {
//                            latitude = "-" + latitude;
//                        }
//                        break;
//                    case 4: //Longitud
//                        longitude = transformLongitudeFormat(sToken);
//                        break;
//                    case 5: //Area (E ó W)
//                        if (sToken.equalsIgnoreCase(WEST_LONGITUDE)) {
//                            longitude = "-" + longitude;
//                        }
//                        break;
//                    case 6: //standard non-differential GPS fix is available
//                        fixQuality = sToken;
//                        break;
//                    case 7: //Numero de satelites
//                        numberOfSatellites = Integer.parseInt(sToken);
//                        break;
//                    case 8: //Horizon DOP (dilution of precision)
//                        break;
//                    case 9: //Altitud
//                        altitude = sToken;
//                        break;
//                    case 10: //Unidad de Altitud (siempre M)
//                        break;
//                    case 11: //Tamaño del geoide
//                        break;
//                }
//            } catch (Exception e) {
//                log.error("Error parseando dato: " + iTokenPos + " Value: " + sToken + " - " + e.getMessage(), e);
//                monitor.error("Error parseando dato: " + iTokenPos + " Value: " + sToken + " - " + e.getMessage(), e);
//            }
//        }
    }

    /**
     * Decodifica una trama tipo GPVTG
     * <p/>
     * $GPVTG,<ddd.d_track_angle>,t,<ddd.d_track_angle>,M,<sss.s_speed_over_gorund>,N,
     * <sss.s_speed_over_ground>,K*hhcrlf
     *
     * @param sGPVTGFrame
     */
    private synchronized void decodeGPVTG(String sGPVTGFrame) {
        log.debug("Decodificando trama GPVTG: " + sGPVTGFrame);
//        monitor.debug("Decodificando trama GPVTG: " + sGPVTGFrame);

        int iChecksumPos = sGPVTGFrame.indexOf(CHECKSUM_CHAR);
        if (iChecksumPos > 0) {
            String sChecksum = sGPVTGFrame.substring(iChecksumPos + 1);
            sGPVTGFrame = sGPVTGFrame.substring(0, iChecksumPos);
        }

//        boolean bSpeedTaken = false;
        Vector vTokenizer = stringTokenizer(sGPVTGFrame, ",");
        try {
            speed = (String) vTokenizer.elementAt(7);
        } catch (Exception e) {
            log.error("Error parseando dato: " + e.getMessage(), e);
//            monitor.error("Error parseando dato: " + e.getMessage(), e);
        }
//        int iTokenSize = vTokenizer.size();
//        for (int iTokenPos = 0; iTokenPos < iTokenSize; iTokenPos++) {
//            String sToken = (String) vTokenizer.elementAt(iTokenPos);
//            try {
//                switch (iTokenPos) {
//                    case 6: //En algunos casos la velocidad me la esta tomando en este puesto
//                        if (!sToken.equalsIgnoreCase("N")) {
//                            speed = sToken;
//                            bSpeedTaken = true;
//                        } else {
//                            bSpeedTaken = false;
//                        }
//                        break;
//                    case 7: //Velocidad
//                        if (!bSpeedTaken)
//                            speed = sToken;
//                        break;
//                }
//            } catch (Exception e) {
//                log.error("Error parseando dato: " + iTokenPos + " Value: " + sToken + " - " + e.getMessage(), e);
//                monitor.error("Error parseando dato: " + iTokenPos + " Value: " + sToken + " - " + e.getMessage(), e);
//            }
//        }
    }

    /**
     * Decodifica una trama tipo GPRMC
     * <p/>
     * $GPRMC,<hhmmss_utc>,<A|V>,<ddmm.mmmm_lat>,<N|S>,<dddmm.mmmm_long>,<E|W>,
     * <kkk.k_knots>,<ddd.d_degrees_course>,<ddmmyy>,<ddd.d_mag_var>,<E|W_mag_var_dir>*hhcrlf
     *
     * @param sGPRMCFrame Trama
     */
    private synchronized void decodeGPRMC(String sGPRMCFrame) {
        log.debug("Decodificando trama GPRMC: " + sGPRMCFrame);
//        monitor.debug("Decodificando trama GPRMC: " + sGPRMCFrame);

        String time = null;
        String fixQuality = null;
        String latitude = null;
        String longitude = null;
        String speed = null;
        String direction = null;
        String date = null;

        int iChecksumPos = sGPRMCFrame.indexOf(CHECKSUM_CHAR);
        if (iChecksumPos > 0) {
            String sChecksum = sGPRMCFrame.substring(iChecksumPos + 1);
            sGPRMCFrame = sGPRMCFrame.substring(0, iChecksumPos);
        }

        Vector vTokenizer = stringTokenizer(sGPRMCFrame, ",");
        try {
            time = (String) vTokenizer.elementAt(1);
            int iDecimalPos = time.indexOf(".");
            if (iDecimalPos > 0) {
                time = time.substring(0, iDecimalPos);
            }
            fixQuality = (String) vTokenizer.elementAt(2);
            latitude = transformLatitudeFormat((String) vTokenizer.elementAt(3));
            String sHemisferio = (String) vTokenizer.elementAt(4);
            if (sHemisferio != null && sHemisferio.equalsIgnoreCase(SOUTH_LATITUDE)) {
                latitude = "-" + latitude;
            }
            longitude = transformLongitudeFormat((String) vTokenizer.elementAt(5));
            String sArea = (String) vTokenizer.elementAt(6);
            if (sArea.equalsIgnoreCase(WEST_LONGITUDE)) {
                longitude = "-" + longitude;
            }
            speed = transformSpeedKonts2kmh((String) vTokenizer.elementAt(7));
            direction = (String) vTokenizer.elementAt(8);
            date = (String) vTokenizer.elementAt(9);
        } catch (Exception e) {
            log.error("Error parseando dato: " + e.getMessage(), e);
//            monitor.error("Error parseando dato: " + e.getMessage(), e);
        }

//        int iTokenSize = vTokenizer.size();
//        for (int iTokenPos = 0; iTokenPos < iTokenSize; iTokenPos++) {
//            String sToken = (String) vTokenizer.elementAt(iTokenPos);
//            try {
//                switch (iTokenPos) {
//                    case 1: //Hora
//                        time = sToken;
//                        int iDecimalPos = time.indexOf(".");
//                        if (iDecimalPos > 0) {
//                            time = time.substring(0, iDecimalPos);
//                        }
//                        break;
//                    case 2: //Calidad (A ó´V)
//                        fixQuality = sToken;
//                        break;
//                    case 3: //Latitud
//                        latitude = transformLatitudeFormat(sToken);
//                        break;
//                    case 4: //Hemisferio (N ó S)
//                        if (sToken.equalsIgnoreCase(SOUTH_LATITUDE)) {
//                            latitude = "-" + latitude;
//                        }
//                        break;
//                    case 5: //Longitud
//                        longitude = transformLongitudeFormat(sToken);
//                        break;
//                    case 6: //Area (E ó W)
//                        if (sToken.equalsIgnoreCase(WEST_LONGITUDE)) {
//                            longitude = "-" + longitude;
//                        }
//                        break;
//                    case 7: //Velocidad
//                        speed = transformSpeedKonts2kmh(sToken);
//                        break;
//                    case 8: //Direccion
//                        direction = sToken;
//                        break;
//                    case 9: //Fecha
//                        date = sToken;
//                        break;
//                }
//            } catch (Exception e) {
//                log.error("Error parseando dato: " + iTokenPos + " Value: " + sToken + " - " + e.getMessage(), e);
//                monitor.error("Error parseando dato: " + iTokenPos + " Value: " + sToken + " - " + e.getMessage(), e);
//            }
        if (fixQuality != null && fixQuality.equalsIgnoreCase(GPRMC_QUALITY_VALID)) {
            this.arrivalDate = new Date();
            this.time = time;
            this.fixQuality = fixQuality;
            this.latitude = latitude;
            this.longitude = longitude;
            this.speed = speed;
            this.direction = direction;
            this.date = date;
        }
//        }
    }

    /**
     * Transforma a grados la latitud desde ddmm.mmmm
     *
     * @param sLatitude Latitud
     * @return Transformada
     */
    private String transformLatitudeFormat(String sLatitude) {
        String sRet = sLatitude;
        if (sLatitude != null) {
            int pos = 0;
            if ((pos = sLatitude.indexOf(".")) > 0) {
                String degrees = sLatitude.substring(0, 2);
                double minutes = Double.valueOf("0." + sLatitude.substring(2, 4)).doubleValue();
                double decimalMinutes = Double.valueOf("0.00" + sLatitude.substring(5)).doubleValue();
                minutes = minutes * 100 / 60;

                decimalMinutes = decimalMinutes * 100 / 60;
                minutes = (minutes + decimalMinutes) * 1000000;

                // Si el calculo de minutos da menor que 10, a la cadena
                // de texto hay que añadirle un cero antes de concatenar el
                // valor de los minutos, porque si no el minuto 04 se convertiría
                // en el minuto 40
                if (minutes < 100000)
                    sRet = degrees + ".0" + Math.round(minutes);
                else
                    sRet = degrees + "." + Math.round(minutes);
            }
        }
        return sRet;
    }

    /**
     * Transforma a grados la longitud desde dddmm.mmmm
     *
     * @param sLongitude longitude
     * @return Transformada
     */
    private String transformLongitudeFormat(String sLongitude) {
        String sRet = sLongitude;
        if (sLongitude != null) {
            int pos = 0;
            if ((pos = sLongitude.indexOf(".")) > 0) {
                String degrees = sLongitude.substring(0, 3);
                double minutes = Double.valueOf("0." + sLongitude.substring(3, 5)).doubleValue();
                double decimalMinutes = Double.valueOf("0.00" + sLongitude.substring(6)).doubleValue();
                minutes = minutes * 100 / 60;

                decimalMinutes = decimalMinutes * 100 / 60;
                minutes = (minutes + decimalMinutes) * 1000000;

                // Si el calculo de minutos da menor que 10, a la cadena
                // de texto hay que añadirle un cero antes de concatenar el
                // valor de los minutos, porque si no el minuto 04 se convertiría
                // en el minuto 40
                if (minutes < 100000)
                    sRet = degrees + ".0" + Math.round(minutes);
                else
                    sRet = degrees + "." + Math.round(minutes);
            }
        }
        return sRet;
    }

    /**
     * Transforma a Km/h la velocidad desde Nudos
     *
     * @param sSpeed
     * @return
     */
    private String transformSpeedKonts2kmh(String sSpeed) {
        String sRet = sSpeed;
        if (sSpeed != null) {
            if (sSpeed.length() > 0) {
                double speed = Double.valueOf(sSpeed).doubleValue();
                speed = speed * 1.852; // Pasar de Nudos a Km/h
                sRet = "" + speed;
            } else {
                sRet = "0";
            }
        }
        return sRet;
    }

    private Vector stringTokenizer(String sOriginalString, String sToken) {
        Vector vResult = new Vector();
        while (sOriginalString.length() > 0) {
            int iTokenPos = sOriginalString.indexOf(sToken);
            if (iTokenPos >= 0) {
                vResult.addElement(sOriginalString.substring(0, iTokenPos));
                sOriginalString = sOriginalString.substring(iTokenPos + 1);
            } else {
                vResult.addElement(sOriginalString);
                sOriginalString = "";
            }
        }
        return vResult;
    }

	public void run() {
        synchronized (this) {
        	while (active) {
				if (!frameBuffer.isEmpty()) {
					String currentFrame = (String) frameBuffer.elementAt(0);
					decode(currentFrame);
					frameBuffer.removeElementAt(0);
				}
				else {
					try {
						wait();
					} catch (InterruptedException e) {
						log.error("Hilo interrumpido: " + e.getMessage(), e);
					}
				}
			}
        }
		instance = null;
	}

	public void stopThread() {
		active = false;
		frameBuffer.removeAllElements();
        synchronized (this) {
        	this.notifyAll();
        }
	}
	
	public void addFrame(String frame) {
		if (active) {
			if (frameBuffer.size() > MAX_FRAMES) {
				frameBuffer.removeAllElements();
			}
			frameBuffer.addElement(frame);
	        synchronized (this) {
	        	this.notifyAll();
	        }
		}
	}
}
