/**
 * LocalGISMobile.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.core;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.validation.ValidationFacade;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.LocalGISMainWindow;
import es.satec.svgviewer.localgis.GPSProvider;
import es.satec.svgviewer.localgis.LocalGISGPSProvider;

public class LocalGISMobile {
	
	private static Display display;
	private static LocalGISMainWindow mainWindow;
	private static Logger logger = Global.getLoggerFor(LocalGISMobile.class);
	
	
	public static Display getDisplay() {
		return display;
	}
	
	public static LocalGISMainWindow getMainWindow() {
		return mainWindow;
	}

	public static void main(String[] args) {
		try {
			// Carga de dlls para ecw
			logger.info ("loading dlls");
			final String LIB_PATH = Global.APP_PATH + File.separator; //+ "dll" + File.separator;
			
			//CARGA DE LIBRERIAS DLL			
			System.load(LIB_PATH + "NCSUtil.dll");
			logger.info ("loaded NCSUtil.dll");

			System.load(LIB_PATH + "NCScnet.dll");
			logger.info ("loaded NSCnet.dll");
			
			System.load(LIB_PATH + "NCSEcw.dll");
			logger.info ("loaded NCSEcw.dll");
			
			if (Global.USING_PDA) {
				System.load(LIB_PATH + "jecwppc.dll");
			}
			else {
				System.load(LIB_PATH + "jecw.dll");
			}
					
			logger.info("Carga de informacion");
			display = new Display();


			// Inicio del GPS
			//GPSProvider gpsProvider = new TestGPSProvider(); // TODO para pruebas
			GPSProvider gpsProvider = new LocalGISGPSProvider(Config.prLocalgis.getProperty(Config.PROPERTY_GPS_PROPERTIES));
			logger.info ("después de GPS Provider");
			SessionInfo.getInstance().setGPSProvider(gpsProvider);
			
			// Inicio del entorno grafico
			mainWindow = new LocalGISMainWindow(display, LocalGISMainWindow.LOGIN_SCREEN_COMPOSITE);
			while (!mainWindow.isClosed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			
			ValidationFacade vf = new ValidationFacade();
			vf.logout(Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_HOST),
				Config.prLocalgis.getPropertyAsInt(Config.PROPERTY_SERVER_PORT, 80),
				Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_CONTEXT));
			
			if (gpsProvider.isStarted())
				gpsProvider.stopGPS();
	
			if ((display!=null) && (!display.isDisposed()))
				display.dispose();
			
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		    System.exit(1);
		}
	}

}
