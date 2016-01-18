/**
 * EIELEditorPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class EIELEditorPanel extends JPanel{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
    private JPanel jPanelEditingInfo = null;  
    private EditorPanel jPanelGraphicEditor = null;
    private JSplitPane jSplitPane = null;
	
	private PlugInContext context = null;
	
	public EIELEditorPanel()  {
		
		super();
        initialize();        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle2);
    }

	public void initialize() {

		this.setLayout(new GridBagLayout());
				
		jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getJPanelEditingInfo(),getJPanelGraphicEditor());
        
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(10);
        
        /*Dimension minimumSize = new Dimension(300, 300);
        getJPanelEditingInfo().setMinimumSize(minimumSize);
        getJPanelGraphicEditor().setMinimumSize(minimumSize);*/
        
        this.add(jSplitPane, 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        
        ((GeopistaEditorPanel)getJPanelGraphicEditor().getGeopistaEditorPanel()).addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	
				if (ConstantesLocalGISEIEL.EVENT_EIEL_MAP_SELECTION_CHANGED.equals(e.getActionCommand())){
					//System.out.println("SELECTION CHANGED");
					mapaJPanel_actionPerformed(e);
					
            	}
				
				else
					mapaJPanel_actionPerformed(e);
			}
		});

	}
	
	public void restartEditingPanel(){
		jPanelEditingInfo=null;
	}
	public boolean isRestarted(){
		return jPanelEditingInfo==null;
	}
	private void mapaJPanel_actionPerformed(ActionEvent e){

		((EditingInfoPanel)getJPanelEditingInfo()).getJPanelTree().fireActionPerformed(e,e.getActionCommand());	
		
	}
	
	
	
	public JPanel getJPanelEditingInfo()
    {
        if (jPanelEditingInfo == null)
        {
        	jPanelEditingInfo = new EditingInfoPanel();
        }
        return jPanelEditingInfo;
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

}
