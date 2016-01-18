/**
 * GridOptionsPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.grid;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class GridOptionsPanel extends JDialog{
	
    private javax.swing.JPanel jContentPane = null;
    private GridOptionsPanelDialog gridOptionsPanelDialog = null;
    private PlugInContext context;
    private OKCancelPanel _okCancelPanel = null;
    
    AppContext appContext = (AppContext) AppContext.getApplicationContext();
    
    /**
     * This method initializes systemConfigPanel	
     * 	
     * @return com.geopista.ui.dialogs.SystemConfigPanel	
     */    
    
    protected GridOptionsPanelDialog getGridOptionsPanelDialog(PlugInContext context) {
        if (gridOptionsPanelDialog == null) {
        	gridOptionsPanelDialog = new GridOptionsPanelDialog(context.getWorkbenchContext().getIWorkbench().getBlackboard());
        }
        return gridOptionsPanelDialog;
    }
    /**
     * This method initializes OKCancelPanel	
     * 	
     * @return com.vividsolutions.jump.workbench.ui.OKCancelPanel	
     */    
    private OKCancelPanel getOkCancelPanel() {
        if (_okCancelPanel == null) {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener() { 
                public void actionPerformed(java.awt.event.ActionEvent e) {    
                    
                    if (_okCancelPanel.wasOKPressed()) {
                        String errorMessage = getGridOptionsPanelDialog(context).validateInput();
                        
                        if (errorMessage != null) {
                            JOptionPane.showMessageDialog(
                            		GridOptionsPanel.this,
                                    errorMessage,
                                    "GEOPISTA",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            getGridOptionsPanelDialog(context).okPressed();                            
                            setVisible(false);
                                              
                            return;
                        }
                    }
                    setVisible(false);
                    
                    return;
                    
                }
            });
        }
        return _okCancelPanel;
    }
  
    /**
     * This is the default constructor
     */
    public GridOptionsPanel(PlugInContext context) {
        super();
        this.context = context;
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        initialize();
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.grid.languages.GridOptionsPaneli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("GridOptionsPanel",bundle2);
    }
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        
    	this.setSize(350, 190);  // Generated    	
    	this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);  // Generated
        this.setTitle(I18N.get("GridOptionsPanel","plugin.gridoptionspanel.grid"));  // Generated
        this.setModal(true);  // Generated
        this.setResizable(false);  // Generated
        this.setContentPane(getJContentPane());
        this.setVisible(true);  // Generated
    }
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if(jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getGridOptionsPanelDialog(context), java.awt.BorderLayout.CENTER);  // Generated
            jContentPane.add(getOkCancelPanel(), java.awt.BorderLayout.SOUTH);  // Generated
        }
        return jContentPane;
    }
  //  @jve:decl-index=0:visual-constraint="10,10"
}
