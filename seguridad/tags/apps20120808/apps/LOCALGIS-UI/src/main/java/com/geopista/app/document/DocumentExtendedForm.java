package com.geopista.app.document;

import com.geopista.util.FeatureDialogHome;
import com.geopista.feature.AbstractValidator;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;

import com.geopista.feature.*;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.DocumentDialog;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

public class DocumentExtendedForm implements FeatureExtendedForm {
	private InfoDocumentPanel infoDocument;
	private FeatureDialogHome fd;
	private AppContext aplicacion;

	public DocumentExtendedForm() {

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
		if (infoDocument != null)
			infoDocument.setEnabled(false);

	}

	public InfoDocumentPanel getInfoDocumentPanel() {
		return infoDocument;
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

		infoDocument = new InfoDocumentPanel();
		infoDocument.setPreferredSize(new Dimension(600, 450));
		infoDocument.setMinimumSize(new Dimension(600, 450));
        infoDocument.setFd(fd);
		fd.addPanel(infoDocument);
		addAyudaOnline();
	}

	/**
	 * Ayuda Online
	 * 
	 */
	private void addAyudaOnline() {
		JDialog fdDialog;
		if (fd instanceof DocumentDialog) {
			fdDialog = (DocumentDialog) fd;

			fdDialog.getRootPane().getInputMap(
					JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
					KeyStroke.getKeyStroke("F1"), "action F1");

			fdDialog.getRootPane().getActionMap().put("action F1",
					new AbstractAction() {
						public void actionPerformed(ActionEvent ae) {
							String uriRelativa = "/Geocuenca:Georreferenciaci%C3%B3n_de_documentos";
							GeopistaBrowser.openURL(aplicacion
									.getString("ayuda.geopista.web")
									+ uriRelativa);
						}
					});
		}
	}

	public GeopistaFeature getSelectedFeature() {
		if (infoDocument == null)
			return null;
		return infoDocument.getSelectedFeature();
	}

}
