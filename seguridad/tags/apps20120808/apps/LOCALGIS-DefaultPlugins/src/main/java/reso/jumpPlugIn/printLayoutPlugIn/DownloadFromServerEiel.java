package reso.jumpPlugIn.printLayoutPlugIn;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Collection;


import com.geopista.app.AppContext;
import com.geopista.app.plantillas.LocalGISPlantillasClient;
import com.geopista.server.administradorCartografia.ACException;


public class DownloadFromServerEiel {

	
	public DownloadFromServerEiel() {}
	
	
	public void getServerPlantillas() throws Exception {
//		LocalGISEIELClient eielClient = new LocalGISEIELClient(
//				AppContext.getApplicationContext().getString(
//						AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) + "/EIELServlet");
		
		LocalGISPlantillasClient plantillasClient = new LocalGISPlantillasClient(
				AppContext.getApplicationContext().getString(
						AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) + "/PlantillasServlet");
		
		Collection colDatos = plantillasClient.getPlantillas("plantillas/impresion","jmp", null, null);
		Object[] objs = colDatos.toArray();
		ArrayList plantillasImpresion = (ArrayList) objs[1];
		for (int i = 0; i < plantillasImpresion.size(); i++) {
			guardarPlantilla((Object[]) plantillasImpresion.get(i));
		}
	}

	private String guardarPlantilla(Object[] plantilla) throws Exception {

		if ((byte[]) plantilla[1] == null) throw new ACException("Plantilla vacia");
		String path = AppContext.getApplicationContext().getUserPreference(
				AppContext.PREFERENCES_DATA_PATH_KEY,
				AppContext.DEFAULT_DATA_PATH, true);

		FileOutputStream out = new FileOutputStream(path + File.separator+ (String) plantilla[0]);
		out.write((byte[]) plantilla[1]);
		out.close();

		return path + (String) plantilla[0];
	}

}
