/**
 * ObraCivilPrintPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.main;

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
import com.localgis.app.gestionciudad.dialogs.printsearch.panels.SearchListPanel;
import com.localgis.app.gestionciudad.images.IconLoader;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class ObraCivilPrintPanel extends JPanel{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
//    private JPanel jPanelEditingInfo = null;  
    private PrintEditor jPanelGraphicEditor = null;
    private JSplitPane jSplitPane = null;
	
	private PlugInContext context = null;
	
	public ObraCivilPrintPanel() {
		
		super();
        initialize();        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.localgis.app.gestionciudad.language.LocalGISOBRACIVILi18n",loc,this.getClass().getClassLoader());    	
    }

	private void initialize() {

		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		
		this.setLayout(new GridBagLayout());
		
		jSplitPane = new JSplitPane();

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Búsquedas", 
				IconLoader.icon("actuaciones.png"),
				new SearchListPanel());
		
//		jSplitPane.setLeftComponent(new SearchListPanel());
		jSplitPane.setLeftComponent(tabbedPane);
		jSplitPane.setRightComponent(new PrintEditorPanel());
		jSplitPane.setEnabled(true);
//		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setResizeWeight(0);
//		
//		jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//				getAvisosListPanel(),getJPanelGraphicEditor());
//        
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(10);
        
        this.add(jSplitPane, 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        
        ((PrintEditorPanel)getJPanelGraphicEditor().getGeopistaEditorPanel()).addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				mapaJPanel_actionPerformed();
			}
		});
        
        this.setVisible(true);
        
	}
	
	private JPanel getAvisosListPanel() {
		// TODO Auto-generated method stub
		return new AvisosListPanel();
	}

	private void mapaJPanel_actionPerformed(){
//		((EditingInfoPanel)getJPanelEditingInfo()).getJPanelTree().fireActionPerformed();	
	}
	
	public PrintEditor getJPanelGraphicEditor()
	{
		if (jPanelGraphicEditor == null)
		{
			jPanelGraphicEditor = new PrintEditor();
		}
		return jPanelGraphicEditor;
	}
		
	protected void reportNothingToUndoYet() {
        context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

}
