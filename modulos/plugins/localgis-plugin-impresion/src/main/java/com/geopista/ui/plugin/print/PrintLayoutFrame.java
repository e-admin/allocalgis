/**
 * PrintLayoutFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 29-jul-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print;

import java.awt.print.PageFormat;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import reso.jumpPlugIn.printLayoutPlugIn.OpenLegende;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.UtilsPrintPlugin;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class PrintLayoutFrame extends reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3760566394781054518L;

	private TaskMonitorDialog progressDialog;
	
	/**
	 * @param context
	 */
	public PrintLayoutFrame(PlugInContext context)
	{
		super(context);

		// TODO Auto-generated constructor stub
	}
	
	/**
	 * fileChooser == null: Muestra ventana para seleccionar platilla
	 * fileChooser != null: Carga fichero indicado
	 * pageFormat != null: Carga plantilla y establece formato de pagina indicado
	 * @return
	 */
	public JFileChooser forceOpen() {
		return forceOpen (null, null);
	}
	public JFileChooser forceOpen(JFileChooser fileChooser, PageFormat pageFormat) {
		return processOpenLeyend(fileChooser, pageFormat);
	}
	
	public JFileChooser processOpenLeyend(JFileChooser fileChooser, PageFormat pageFormat) {
		// Force to select a template
		OpenLegende ol = new OpenLegende(this, fileChooser, pageFormat);
		ol.setProgressDialog(this.progressDialog);
		File dir = new File(UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY, null, false));
		ol.getFileChooser().setCurrentDirectory(dir);
		ol.getFileChooser().setFileFilter(new FileFilter()
		{
			public boolean accept(File arg0)
			{
				if(arg0.getName().endsWith(".jmp") || arg0.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return UtilsPrintPlugin.getMessageI18N("PrintLayoutPlugin.PrintTemplates");
			}
		});
		ol.actionPerformed(null);
		setMapsExtents(); //refresh maps and labels
		
		return ol.getFileChooser();
	}

	public void setProgressDialog(TaskMonitorDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
}
