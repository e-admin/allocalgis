/**
 * GPSReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.gps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import org.apache.log4j.Logger;

/**
 * User: Hugo
 * Date: 01-ago-2007
 * Time: 13:38:24
 */
public class GPSReader extends Thread implements SerialPortEventListener {
    private Logger log = (Logger) Logger.getInstance(GPSReader.class);
//    private Monitor monitor = MonitorFactory.getInstance();

    private static final String CONFIG_FILE = "gps.properties";
    private static final String PROP_ENABLED = "enabled";
    private static final String PROP_PORT = "port";
    private static final String PROP_SAMPLE_RATE = "sample_rate";
    private static final String PROP_BAUDRATE = "baudrate";
    private static final String PROP_DATABITS = "databits";
    private static final String PROP_STOPBITS = "stopbits";
    private static final String PROP_PARITYBITS = "paritybits";
    private static final String PROP_VALID_TIME = "valid_time_minutes";
    private static final String PROP_DLL_PATH = "dll_path";

    private boolean enabled = false;
    private String port = "COM7:";
    private int baudrate = 4800;
    private int databits = 8;
    private int stopbits = 1;
    private int paritybits = 0;
    private long sampleInterval = 1000;
    private long validTime = 5000;
    private String dllPath = "";

    private static final String APPLICATION_NAME = "SatecGPS";
    private static final int OPEN_PORT_TIMEOUT = 2000;
    //private final static String LIB_PATH = Global.APP_PATH + File.separator + "lib" + File.separator;

    private boolean active = true;
    private boolean running = false;

    private SerialPort spPort;
    private InputStream isData;
    private BufferedReader brData;

    private CommPortIdentifier cpiPort;

    public GPSReader(String gpsPropertiesResource) {
    	InputStream pris = null;
        try {
            log.info("Cargando configuracion de GPS. Fichero de configuracion: " + gpsPropertiesResource);
            pris = GPSReader.class.getResourceAsStream(gpsPropertiesResource);
            Properties pConf = new Properties();
            pConf.load(pris);
            String sAux = pConf.getProperty(PROP_ENABLED);
            if (sAux != null)
                enabled = Boolean.valueOf(sAux).booleanValue();
            if (enabled) {
                port = pConf.getProperty(PROP_PORT);
                sAux = pConf.getProperty(PROP_SAMPLE_RATE);
                if (sAux != null)
                    sampleInterval = Long.parseLong(sAux);
                sAux = pConf.getProperty(PROP_BAUDRATE);
                if (sAux != null)
                    baudrate = Integer.parseInt(sAux);
                sAux = pConf.getProperty(PROP_DATABITS);
                if (sAux != null)
                    databits = Integer.parseInt(sAux);
                sAux = pConf.getProperty(PROP_STOPBITS);
                if (sAux != null)
                    stopbits = Integer.parseInt(sAux);
                sAux = pConf.getProperty(PROP_PARITYBITS);
                if (sAux != null)
                    paritybits = Integer.parseInt(sAux);
                sAux = pConf.getProperty(PROP_VALID_TIME);
                if (sAux != null) {
                    validTime = Long.parseLong(sAux);
                    GPSDecoder.getInstance().setValidTime(validTime);
                }
                dllPath = pConf.getProperty(PROP_DLL_PATH);

                // Obtener el valor de java.library.path
                String path = System.getProperty("java.library.path");

                String otherPath = path + ";" + dllPath;

                // Añadir a lo que ya había el directorio donde se encuentra la dll que nos interesa
                Properties props = System.getProperties();
                props.put("java.library.path", otherPath);

                System.setProperties(props);

                Enumeration en = CommPortIdentifier.getPortIdentifiers();
                while (en.hasMoreElements()) {
                    CommPortIdentifier commPortIdentifier = (CommPortIdentifier) en.nextElement();
                    log.debug("Encontrado puerto: " + commPortIdentifier.getName() + " - Tipo: " + (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL ? "SERIAL" : "PARALEL"));
//                    monitor.debug("Encontrado puerto: " + commPortIdentifier.getName() + " - Tipo: " + (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL ? "SERIAL" : "PARALEL"));
                    if (commPortIdentifier.getName().equalsIgnoreCase(port)) {
                        log.debug("El puerto coincide con el configurado: " + port);
//                        monitor.debug("El puerto coincide con el configurado: " + port);
                        cpiPort = commPortIdentifier;
                    }
                }

                // Devolver la variable a su valor original
                props.put("java.library.path", path);
                System.setProperties(props);

            }
        } catch (Exception e) {
            log.error("Error cargando configuracion de GPS: " + e.getMessage(), e);
//            monitor.error("Error cargando configuracion de GPS: " + e.getMessage(), e);
        } finally {
        	if (pris != null) {
        		try {
					pris.close();
				} catch (IOException e) {}
        	}
        }
    }

    public GPSReader() {
    	this("/" + CONFIG_FILE);
    }

    public void run() {
        if (enabled) {
            try {
                log.debug("Iniciando el puerto: '" + port + "'");
                running = true;
                CommPortIdentifier cpiId;
                if (cpiPort == null) {
                    log.debug("El puerto no se encontron en la inicializacion, intentandolo a mano");
//                    monitor.debug("El puerto no se encontron en la inicializacion, intentandolo a mano");
                    cpiId = CommPortIdentifier.getPortIdentifier(port);
                } else {
                    log.debug("Usando puerto encontrado en la inicializacion.");
//                    monitor.debug("Usando puerto encontrado en la inicializacion.");
                    cpiId = cpiPort;
                }
                log.debug("Puerto " + port + " encontrado.");
                if (cpiId.getPortType() != CommPortIdentifier.PORT_SERIAL) {
                    log.error("El puerto indicado " + port + " no es de tipo SERIE");
//                    monitor.error("El puerto indicado " + port + " no es de tipo SERIE");
                    return;
                }

                log.debug("Abriendo el puerto: " + port);
                spPort = (SerialPort) cpiId.open(APPLICATION_NAME, OPEN_PORT_TIMEOUT);
//                log.debug("Recueprando InputStream");
                isData = spPort.getInputStream();
                brData = new BufferedReader(new InputStreamReader(isData));
//                log.debug("Añadiento listener");
// Cambiamos el listener por un bucle por incompatibilidad con SWT.
//                spPort.addEventListener(this);
//                log.debug("Activando notificacion de datos");
                spPort.notifyOnDataAvailable(true);
//                log.debug("Estableciendo la configuracion");
                spPort.setSerialPortParams(baudrate, databits, stopbits, paritybits);

//                log.debug("Entrando en bucle");
                active = true;
                while (active) {
                    try {
                        sleep(sampleInterval);

                        /* Codigo que sustituye al listener */
                        String sData;
                        try {
                            while (active && brData.ready()) {
                                sData = brData.readLine();
                                if (sData != null)
                                    //GPSDecoder.getInstance().decode(sData);
                                	GPSDecoder.getInstance().addFrame(sData);
                            }
                        } catch (IOException e) {
                            log.error("Error al leer del puerto " + port + " - " + e.getMessage(), e);
//                            monitor.error("Error al leer del puerto " + port + " - " + e.getMessage(), e);
                        }

                    } catch (InterruptedException e) {
                        active = false;
                    }
                }
//                log.debug("Saliendo de bucle");

            } catch (NoSuchPortException e) {
                log.error("Error de puerto no encontrado: " + port + " - " + e.getMessage(), e);
                stopThread();
//                monitor.error("Error de puerto no encontrado: " + port + " - " + e.getMessage(), e);
            } catch (PortInUseException e) {
                log.error("Error el puerto " + port + " ya esta siendo utilizado por otra aplicacion: " + e.currentOwner + " - " + e.getMessage(),
                        e);
                stopThread();
//                monitor.error("Error el puerto " + port + " ya esta siendo utilizado por otra aplicacion: " + e.currentOwner + " - " + e.getMessage(),
//                        e);
            } catch (UnsupportedCommOperationException e) {
                log.error("Error configurando el puerto " + port + " - " + e.getMessage(), e);
                stopThread();
//                monitor.error("Error configurando el puerto " + port + " - " + e.getMessage(), e);
            } catch (IOException e) {
                log.error("Error de flujo en el puerto " + port + " - " + e.getMessage(), e);
                stopThread();
//                monitor.error("Error de flujo en el puerto " + port + " - " + e.getMessage(), e);
//            } catch (TooManyListenersException e) {
//                log.error("Error registrando listener para el puerto " + port + " - " + e.getMessage(), e);
//                stopThread();
//                monitor.error("Error registrando listener para el puerto " + port + " - " + e.getMessage(), e);
            } catch (Exception e) {
                log.error("Error en el puerto " + port + " - " + e.getMessage(), e);
                stopThread();
//                monitor.error("Error en el puerto " + port + " - " + e.getMessage(), e);
            } finally {
                try {
                	if (brData!=null) brData.close();
                    log.debug("Cerrando puerto");
                    if (spPort != null)
                        spPort.close();
                    running = false;
                    log.debug("Puerto cerrado");
                } catch (Throwable e) {
                    log.fatal("ERROR FATAL CERRANDO PUERTO: " + e.getMessage(), e);
                }
            }
        }
    }

    public void stopThread() {
        active = false;
        /*try {
        	GPSDecoder.getInstance().stopThread();
            log.debug("Cerrando puerto");
            if (spPort != null)
                spPort.close();
            running = false;
            log.debug("Puerto cerrado");
        } catch (Throwable e) {
            log.fatal("ERROR FATAL CERRANDO PUERTO: " + e.getMessage(), e);
        }*/
    }

    public boolean isEnabled() {
        return enabled;
    }

    public GPSInfo getGPSInfo() {
        return GPSDecoder.getInstance().getGPSInfo();
    }

    public boolean isRunnig() {
        return running;
    }

    public void serialEvent(SerialPortEvent serialPortEvent) {
    	switch (serialPortEvent.getEventType()) {

            case SerialPortEvent.BI:

            case SerialPortEvent.OE:

            case SerialPortEvent.FE:

            case SerialPortEvent.PE:

            case SerialPortEvent.CD:

            case SerialPortEvent.CTS:

            case SerialPortEvent.DSR:

            case SerialPortEvent.RI:

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;

            case SerialPortEvent.DATA_AVAILABLE:
//                byte[] readBuffer = new byte[2048];
                String sData;
                try {
                    while (active && brData.ready()) {
                        sData = brData.readLine();
                        log.debug("Leido: " + sData);
                        if (sData != null)
                            //GPSDecoder.getInstance().decode(sData);
                        	GPSDecoder.getInstance().addFrame(sData);
                    }
                } catch (IOException e) {
                    log.error("Error al leer del puerto " + port + " - " + e.getMessage(), e);
//                    monitor.error("Error al leer del puerto " + port + " - " + e.getMessage(), e);
                }

                break;
        }
    }

}
