/**
 * DownloadFromServerEiel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package reso.jumpPlugIn.printLayoutPlugIn;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.plantillas.LocalGISPlantillasClient;
import com.geopista.global.ServletConstants;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.util.config.UserPreferenceStore;


public class DownloadFromServerEiel {

	
	public DownloadFromServerEiel() {}
	
	
	public void getServerPlantillas() throws Exception {
//		LocalGISEIELClient eielClient = new LocalGISEIELClient(
//				AppContext.getApplicationContext().getString(
//						AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) + "/EIELServlet");
		
		LocalGISPlantillasClient plantillasClient = new LocalGISPlantillasClient(
				AppContext.getApplicationContext().getString(
						UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) + ServletConstants.PLANTILLAS_SERVLET_NAME);
		
		Collection colDatos = plantillasClient.getPlantillas("plantillas/impresion","jmp", null, null);
		Object[] objs = colDatos.toArray();
		ArrayList plantillasImpresion = (ArrayList) objs[1];
		for (int i = 0; i < plantillasImpresion.size(); i++) {
			guardarPlantilla((Object[]) plantillasImpresion.get(i));
		}
	}

	private String guardarPlantilla(Object[] plantilla) throws Exception {

		if ((byte[]) plantilla[1] == null) throw new ACException("Plantilla vacia");
		String path = UserPreferenceStore.getUserPreference(
				UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,
				UserPreferenceConstants.DEFAULT_DATA_PATH, true);

		FileOutputStream out = new FileOutputStream(path + File.separator+ (String) plantilla[0]);
		out.write((byte[]) plantilla[1]);
		out.close();

		return path + (String) plantilla[0];
	}

}
