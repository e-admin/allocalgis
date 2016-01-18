/**
 * DownloadFromServer.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JasperReport;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.plantillas.LocalGISPlantillasClient;
import com.geopista.app.reports.ReportsConstants;
import com.geopista.app.reports.ReportsManager;
import com.geopista.global.ServletConstants;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.util.config.UserPreferenceStore;
//import com.geopista.app.eiel.LocalGISEIELClient;


public class DownloadFromServer {

	private static final Log	logger	= LogFactory.getLog(DownloadFromServer.class);
	
	public DownloadFromServer() {}
	
	
	public void getServerPlantillas(String extension) throws Exception {
		String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.idAppType);
		getServerPlantillas(extension,idAppType);
	}
	
	public void getServerPlantillas(String extension,String idAppType) throws Exception {
		LocalGISPlantillasClient plantillasClient = new LocalGISPlantillasClient(
				AppContext.getApplicationContext().getString(
						UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) + ServletConstants.PLANTILLAS_SERVLET_NAME);
//		Collection colDatos = eielClient.getPlantillas("plantillas/impresion","jmp", null, null);
		
		
		//Collection colDatos = plantillasClient.getPlantillas(ConstantesLocalGISPlantillas.PATH_PLANTILLAS + File.separator + idAppType,extension, null, null);
		logger.info("IdAppType:"+idAppType);
		Collection colDatos = plantillasClient.getPlantillas(idAppType,extension, null, null);		
		Object[] objs = colDatos.toArray();
		
		String idAppTypeTMP = idAppType;
		if (idAppType.contains("inventario")){
			if (idAppType.toString().contains("\\")){
				String[] arrayPlantillas = idAppType.toString().split("\\\\");
				idAppTypeTMP = arrayPlantillas[0];
			}
			else
				if (idAppType.toString().contains("/")){
					String[] arrayPlantillas = idAppType.toString().split("/");
					idAppTypeTMP = arrayPlantillas[0];
				}
		}
		if (objs != null){
			for (int j = 0; j < objs.length; j++){
				ArrayList plantillas = (ArrayList) objs[j];
				String path = UserPreferenceStore.getUserPreference(
						UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,
						UserPreferenceConstants.DEFAULT_DATA_PATH, true);
				
				if (!idAppType.equals("")){
					switch (j) {
					case 0:						
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppTypeTMP+File.separator + ConstantesLocalGISPlantillas.PATH_IMG + File.separator;
						break;
					case 1:
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppType+File.separator;
						break;
					case 2:
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppTypeTMP+File.separator + ConstantesLocalGISPlantillas.PATH_SUBREPORTS + File.separator;
						break;
					default:
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppType+File.separator;
						break;
					}
				}
				for (int i = 0; i < plantillas.size(); i++) {
					guardarPlantilla((Object[]) plantillas.get(i), path);
					if (j == 2){
						Object[] plantilla = (Object[])plantillas.get(i);
						JasperReport jasperReport=ReportsManager.loadReport(path + plantilla[0]);
					}
				}
			}
		}
	}

	private String guardarPlantilla(Object[] plantilla, String path) throws Exception {
		String pathTMP;
		if ((byte[]) plantilla[1] == null) throw new ACException("Plantilla vacia");
		if (plantilla[0].toString().contains(File.separator)){
			String[] arrayPlantillas = plantilla[0].toString().split("\\"+File.separator);
			pathTMP = path+arrayPlantillas[0];
		}
		else
			if (plantilla[0].toString().contains("/")){
				String[] arrayPlantillas = plantilla[0].toString().split("/");
				pathTMP = path+arrayPlantillas[0];
			}
			else
				pathTMP =path;
		if (!new File(pathTMP).exists()){
			 new File(pathTMP).mkdirs();
		}else{
			if (!new File(pathTMP).isDirectory()) {
				new File(pathTMP).delete();
				new File(pathTMP).mkdirs();
			}
		}
		FileOutputStream out = new FileOutputStream(path + File.separator+ (String) plantilla[0]);
		out.write((byte[]) plantilla[1]);
		out.close();

		return path + (String) plantilla[0];
	}

}
