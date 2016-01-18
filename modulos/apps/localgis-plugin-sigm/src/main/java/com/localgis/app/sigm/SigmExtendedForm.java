/**
 * SigmExtendedForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.sigm;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.DocumentDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;

public class SigmExtendedForm implements FeatureExtendedForm {
	private InfoSigmPanel infoSigm;
	private FeatureDialogHome fd;
	private AppContext aplicacion;

	public SigmExtendedForm() {

	}

	public void setApplicationContext(ApplicationContext context) {

	}

	public void flush() {
	}

	public boolean checkPanels() {
		return true;
	}

	public AbstractValidator getValidator() {
		return null;
	}

	public void disableAll() {
		if (infoSigm != null)
			infoSigm.setEnabled(false);

	}

	public InfoSigmPanel getInfoDocumentPanel() {
		return infoSigm;
	}

	public void initialize(FeatureDialogHome fd) {
		this.fd=fd;
        if (!AppContext.getApplicationContext().isOnline()) return;
		// GeopistaSchema esquema = (GeopistaSchema)fd.getFeature().getSchema();
		Object[] features;
		if (fd instanceof DocumentDialog)
			features = (((DocumentDialog) fd).getFeatures()).toArray();
		else {
			features = new Object[1];
			features[0] = (GeopistaFeature) fd.getFeature();
			
			if (features[0] == null){
				return;
			}
			else if (features[0] instanceof GeopistaFeature){
				GeopistaFeature geopistaFeature = (GeopistaFeature)features[0];
				if (((GeopistaFeature)features[0]).getLayer()!= null && 
				((GeopistaFeature)features[0]).getLayer() instanceof GeopistaLayer ){
					GeopistaLayer layer = (GeopistaLayer)((GeopistaFeature)features[0]).getLayer();
					if (layer.isLocal() || layer.isExtracted()){
						return;
					}
				}
				else{
					return;
				}
			}
			
			
			try { // Si se esta insertando no dejamos meter documentos.
				new Long(((GeopistaFeature) fd.getFeature()).getSystemId());
			} catch (Exception e) {
				return;
			}// Si es una inserción no mostramos la pantalla
		}
		AppContext app = (AppContext) AppContext.getApplicationContext();
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		Blackboard Identificadores = app.getBlackboard();
		Identificadores.put("feature", features);

		infoSigm = new InfoSigmPanel();
		//infoDocument.setPreferredSize(new Dimension(600, 450));
		//infoDocument.setMinimumSize(new Dimension(600, 450));
        infoSigm.setFd(fd);
		fd.addPanel(infoSigm);
	}

}
