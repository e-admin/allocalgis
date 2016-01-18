/**
 * UtilsContaminantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import reso.jumpPlugIn.printLayoutPlugIn.DownloadFromServer;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.protocol.contaminantes.CPlantilla;
import com.geopista.util.config.UserPreferenceStore;


public class UtilsContaminantes {

	static Logger logger = Logger.getLogger(UtilsContaminantes.class);

	public static void mostrarMensaje(Component panel, String mensaje, String tittle) {
		JOptionPane.showMessageDialog(panel, mensaje, tittle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void cargarPlantillas(String path, JComboBox plantillasComboBox){
		try {
			AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.idAppType, path);  
			
			DownloadFromServer dfs = new DownloadFromServer();
			dfs.getServerPlantillas(ConstantesLocalGISPlantillas.EXTENSION_JRXML);	

			Vector plantillas = getPlantillas();
			if (plantillas != null) {
				Enumeration enumeration = plantillas.elements();
				while (enumeration.hasMoreElements()) {
					CPlantilla plantilla = (CPlantilla) enumeration.nextElement();
					logger.debug("plantilla.getFileName=" + plantilla.getFileName());
					plantillasComboBox.addItem(plantilla.getFileName());
				}
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}
	
	private static Vector getPlantillas() {
		try {
			Vector vPlantillas = new Vector();

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.endsWith("." + ConstantesLocalGISPlantillas.EXTENSION_JRXML));
				}
			};

			String path = getLocalPathPlantillas();

			File dir = new File(path);
			if (dir.isDirectory()) {
				File[] children = dir.listFiles(filter);
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						// Get filename of file or directory
						File file = children[i];
						
						FileInputStream fis = new FileInputStream(file);
						byte data[] = new byte[(int)file.length()];
						fis.read(data);
						
						String fname = file.getName();
						BufferedWriter bw = new BufferedWriter(new FileWriter(path + fname));
						String sdata = new String(data);
						bw.write(sdata);
					
						bw.flush();
						bw.close();
						
						CPlantilla plantilla = new CPlantilla(fname);
						vPlantillas.addElement(plantilla);
					}
				}
			}
			return vPlantillas;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return new Vector();
		}
	}
	
	public static String getLocalPathPlantillas () {
    	String localPathBase = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);
    	String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.idAppType);
    	String localPath = localPathBase + UserPreferenceConstants.REPORT_DIR_NAME + File.separator + idAppType + File.separator;
        
		return localPath;
	}
}
