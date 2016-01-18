/**
 * ZoomToAreaDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.zoomtoarea;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class ZoomToAreaDialog extends JDialog{
	
	private javax.swing.JPanel jContentPane = null;
    private ZoomToAreaPanel zoomToAreaDialog = null;
    private PlugInContext context;    
    private OKCancelPanel _okCancelPanel = null;
       
	protected ZoomToAreaPanel getZoomToAreaDialog(PlugInContext context) {
        if (zoomToAreaDialog == null) {
        	zoomToAreaDialog = new ZoomToAreaPanel(context);
        }
        return zoomToAreaDialog;
    }
	
	/**
     * This is the default constructor
     */
    public ZoomToAreaDialog(PlugInContext context) {
    	
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
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.zoomtoarea.languages.ZoomToAreaPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ZoomToAreaPlugIn",bundle);
    }

    private void initialize() {

    	this.setSize(420, 150);  // Generated    	
    	this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);  // Generated
    	this.setTitle(I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.title"));  // Generated
    	this.setModal(true);  // Generated
    	this.setResizable(false);  // Generated
    	this.setContentPane(getJContentPane());
    	this.setVisible(true);  // Generated
    }
    
    private javax.swing.JPanel getJContentPane() {
    	
        if(jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getZoomToAreaDialog(context), java.awt.BorderLayout.CENTER);  // Generated
            jContentPane.add(getOkCancelPanel(), java.awt.BorderLayout.SOUTH);  // Generated
        }
        return jContentPane;
    }
        
    private OKCancelPanel getOkCancelPanel() {
        if (_okCancelPanel == null) {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener() { 
                
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    
                    if (_okCancelPanel.wasOKPressed()) {
                        String errorMessage = getZoomToAreaDialog(context).validateInput();
                        
                        if (errorMessage != null) {
                            JOptionPane.showMessageDialog(
                            		ZoomToAreaDialog.this,
                                    errorMessage,
                                    "GEOPISTA",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            getZoomToAreaDialog(context).okPressed();                            
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

	
}
