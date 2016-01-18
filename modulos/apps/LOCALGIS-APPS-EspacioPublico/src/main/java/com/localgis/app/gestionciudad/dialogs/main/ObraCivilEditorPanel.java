/**
 * ObraCivilEditorPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.main;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;


import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.geopista.app.AppContext;
import com.localgis.app.gestionciudad.dialogs.interventions.panels.AvisosListPanel;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.dialogs.notes.panels.NotesListPanel;
import com.localgis.app.gestionciudad.images.IconLoader;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class ObraCivilEditorPanel extends JPanel{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
    private JPanel jPanelInterventionEditingInfo = null;
    private JPanel jPanelNoteEditingInfo = null;  
    private EditorPanel jPanelGraphicEditor = null;
    private JSplitPane jSplitPane = null;
    private JTabbedPane infoTabbedPane = null;
	
	private PlugInContext context = null;
	
	public ObraCivilEditorPanel() {
		
		super();
        initialize();        
        Locale loc=Locale.getDefault();      	     	
    }

	private void initialize() {

		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		
		this.setLayout(new GridBagLayout());
		
		jSplitPane = new JSplitPane();

		jSplitPane.setLeftComponent(getInfoTabbedPane());
		jSplitPane.setRightComponent(getJPanelGraphicEditor());
		jSplitPane.setEnabled(true);
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setResizeWeight(0);

        jSplitPane.setDividerSize(10);
        
        this.add(jSplitPane, 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        
        ((GeopistaEditorPanel)getJPanelGraphicEditor().getGeopistaEditorPanel()).addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				mapaJPanel_actionPerformed();
			}
		});
        
        this.setVisible(true);
        
	}
	
	private JPanel getNotasListPanel() {
		if (jPanelNoteEditingInfo == null){
			jPanelNoteEditingInfo = new NotesListPanel();
		}
		return jPanelNoteEditingInfo;
	}
	
	private JPanel getAvisosListPanel() {
		if (jPanelInterventionEditingInfo == null){
			jPanelInterventionEditingInfo = new AvisosListPanel();
		}
		return jPanelInterventionEditingInfo;
	}

	private void mapaJPanel_actionPerformed(){
		Component selectedPage = null;
		if (this.getInfoTabbedPane()!=null && 
				this.getInfoTabbedPane().getSelectedComponent()!=null){
			selectedPage = this.getInfoTabbedPane().getSelectedComponent();
		}
		
		if (selectedPage != null){
			if (selectedPage instanceof AvisosListPanel){
				AvisosListPanel avisosListPanel = (AvisosListPanel) selectedPage;
				if (avisosListPanel != null){
					avisosListPanel.onEditorAcctionPerformedDo();
				}
			} else if (selectedPage instanceof NotesListPanel){
				NotesListPanel notesListPanel = (NotesListPanel) selectedPage;
				if (notesListPanel != null){
					notesListPanel.onEditorAcctionPerformedDo();
				}
			}
		}
		
	}
	
		public EditorPanel getJPanelGraphicEditor()
    {
        if (jPanelGraphicEditor == null)
        {
        	jPanelGraphicEditor = new EditorPanel();
        }
        return jPanelGraphicEditor;
    }
		
	protected void reportNothingToUndoYet() {
        context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

	
	  private JTabbedPane getInfoTabbedPane(){
		  if (infoTabbedPane == null){
			  infoTabbedPane = new JTabbedPane();
			  infoTabbedPane.addTab("Actuaciones", 
					  IconLoader.icon("actuaciones.png"),
					  getAvisosListPanel());
			  infoTabbedPane.addTab("Notas",  
					  IconLoader.icon("notas.png"), 
					  getNotasListPanel());
		  }
		  return infoTabbedPane;
	  }
}
