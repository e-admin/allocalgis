/**
 * DocumentSinFeatureExtendedForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.document;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.feature.AbstractValidator;
import com.geopista.ui.dialogs.DocumentSinFeatureDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.vividsolutions.jump.util.Blackboard;

public class DocumentSinFeatureExtendedForm {
	private InfoDocumentSinFeaturePanel infoDocument;
	private FeatureDialogHome fd;
	private AppContext aplicacion;

	public DocumentSinFeatureExtendedForm() {

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

	public InfoDocumentSinFeaturePanel getInfoDocumentPanel() {
		return infoDocument;
	}

	public void initialize(FeatureDialogHome fd) {
		this.fd=fd;
        if (!AppContext.getApplicationContext().isOnline()) return;
		
		AppContext app = (AppContext) AppContext.getApplicationContext();
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		Blackboard Identificadores = app.getBlackboard();

		infoDocument = new InfoDocumentSinFeaturePanel();
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
		if (fd instanceof DocumentSinFeatureDialog) {
			fdDialog = (DocumentSinFeatureDialog) fd;

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

}
