/**
 * NotesEdicionDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.notes.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.gestionciudad.utils.UtilsEspacioPublico;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.documents.panels.AsociatedDocumentsPanel;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.dialogs.notes.panels.NotesFieldsPanel;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class NotesEdicionDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8103959152474028116L;

	private NotesInterventionsEditionTypes tipoDialogo = null;

	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel = null;
	private NotesFieldsPanel notesFieldsPanel = null;
	private AsociatedDocumentsPanel docuemntsPanel = null;

	private LocalGISNote note = null;
	
	public static NotesEdicionDialog createNotesEdicionDialog(Component parentComponent, NotesInterventionsEditionTypes tipo, LocalGISNote note){
        Window window = UtilsEspacioPublico.getWindowForComponent(parentComponent);
        if (window instanceof Frame) {
        	return new NotesEdicionDialog((Frame)window, tipo, note);
        } else {
            return new NotesEdicionDialog((Dialog)window, tipo, note);
        }			
	}

	private NotesEdicionDialog(Frame parentComponent, NotesInterventionsEditionTypes tipo, LocalGISNote note){
		super(parentComponent, "", true);
		this.tipoDialogo = tipo;
		this.note = note;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.setSize(500, 500);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		this.initialize();
		this.pack();
		this.setVisible(true);
	}

	private NotesEdicionDialog(Dialog parentComponent, NotesInterventionsEditionTypes tipo, LocalGISNote note){
		super(parentComponent, "", true);	
		this.tipoDialogo = tipo;
		this.note = note;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.setSize(500, 500);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		this.initialize();
		this.pack();
		this.setVisible(true);
	}
	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

		this.setLayout(new GridBagLayout());

		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));


		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}

	private JPanel getRootPanel(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(this.getAvisoFieldsPanel(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			
			rootPanel.add(this.getDocumentsPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
		}
		return rootPanel;
	}

	private NotesFieldsPanel getAvisoFieldsPanel(){
		if (notesFieldsPanel == null){
			notesFieldsPanel = new NotesFieldsPanel(this.tipoDialogo, this.note);
		}
		return notesFieldsPanel;
	}

	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();
			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}

	protected boolean isInputValid() {
		if (!allDataIscorrect()){
			return false;
		}
		return setDataToAdvise(); 
	}


	private boolean setDataToAdvise() {
		// TODO Auto-generated method stub
		this.note = this.getAvisoFieldsPanel().getNoteData();
		return true;
	}


	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	private boolean allDataIscorrect() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public LocalGISNote getNote(){
		return this.note;
	}
	
	public void setNote(LocalGISNote aviso){
		this.note = aviso;
	}

	private AsociatedDocumentsPanel getDocumentsPanel(){
		if (docuemntsPanel == null){
			docuemntsPanel = new AsociatedDocumentsPanel(this.note,this.tipoDialogo);
		}
		return docuemntsPanel;
	}

}
