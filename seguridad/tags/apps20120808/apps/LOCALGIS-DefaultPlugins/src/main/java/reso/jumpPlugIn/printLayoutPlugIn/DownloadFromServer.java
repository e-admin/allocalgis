package reso.jumpPlugIn.printLayoutPlugIn;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Collection;


import com.geopista.app.AppContext;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.plantillas.LocalGISPlantillasClient;
import com.geopista.app.reports.ReportsConstants;
//import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.server.administradorCartografia.ACException;


public class DownloadFromServer {

	
	public DownloadFromServer() {}
	
	
	public void getServerPlantillas(String extension) throws Exception {
		LocalGISPlantillasClient plantillasClient = new LocalGISPlantillasClient(
				AppContext.getApplicationContext().getString(
						AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) + "/PlantillasServlet");
//		Collection colDatos = eielClient.getPlantillas("plantillas/impresion","jmp", null, null);
		String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(AppContext.idAppType);
		Collection colDatos = plantillasClient.getPlantillas(ConstantesLocalGISPlantillas.PATH_PLANTILLAS + File.separator + idAppType,extension, null, null);		
		Object[] objs = colDatos.toArray();
		if (objs != null){
			for (int j = 0; j < objs.length; j++){
				ArrayList plantillas = (ArrayList) objs[j];
				String path = AppContext.getApplicationContext().getUserPreference(
						AppContext.PREFERENCES_DATA_PATH_KEY,
						AppContext.DEFAULT_DATA_PATH, true);
				
				if (!idAppType.equals("")){
					switch (j) {
					case 0:
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppType+File.separator + ConstantesLocalGISPlantillas.PATH_IMG + File.separator;
						break;
					case 1:
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppType+File.separator;
						break;
					case 2:
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppType+File.separator + ConstantesLocalGISPlantillas.PATH_SUBREPORTS + File.separator;
						break;
					default:
						path = path+ ReportsConstants.REPORTS_DIR + File.separator+idAppType+File.separator;
						break;
					}
				}
				for (int i = 0; i < plantillas.size(); i++) {
					guardarPlantilla((Object[]) plantillas.get(i), path);
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
