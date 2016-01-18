/*
 * 
 * Created on 14-mar-2005 by juacas
 *
 * 
 */
package com.geopista.app.administrador;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.SystemConfigPanel;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ConfigSystem extends JDialog
{
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private javax.swing.JPanel jContentPane = null;
    private SystemConfigPanel systemConfigPanel = null;
    private OKCancelPanel _okCancelPanel = null;
    /**
     * This method initializes systemConfigPanel	
     * 	
     * @return com.geopista.ui.dialogs.SystemConfigPanel	
     */    
    private SystemConfigPanel getSystemConfigPanel() {
        if (systemConfigPanel == null) {
            systemConfigPanel = new SystemConfigPanel();
        }
        
        
        return systemConfigPanel;
    }
    
    private ConfigSystem getConfigSystem(){
    	return this;
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
                        String errorMessage = getSystemConfigPanel().validateInput();
                        
                        if (errorMessage != null) {
                            JOptionPane.showMessageDialog(
                                    ConfigSystem.this,
                                    errorMessage,
                                    "GEOPISTA",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            getSystemConfigPanel().okPressed();
                            setVisible(false);
                            if (fromInicio)
                            	getConfigSystem().dispose();
                            else
                            	System.exit(0);
                            return;
                        }
                    }
                    setVisible(false);
                    
                    if (fromInicio)
                    	getConfigSystem().dispose();
                    else	
                    	System.exit(0);
                    
                }
            });
        }
        return _okCancelPanel;
    }
    public static void main(String[] args)
    {
        ConfigSystem cfg=new ConfigSystem();
        
        cfg.setVisible(true);
        
        System.exit(0);
    }
    private boolean fromInicio;
    public ConfigSystem() {
		this(false);
    }
    /**
     * This is the default constructor
     */
    public ConfigSystem(boolean fromInicio) {
        super();
        this.fromInicio=fromInicio;
        
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        initialize();
    }
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);  // Generated
        //this.setTitle("Config");  // Generated
        
    	this.setTitle(aplicacion.getString("localgis.release")+" Config");
		
        this.setModal(true);  // Generated
        this.setResizable(false);  // Generated
        this.setContentPane(getJContentPane());

        this.setSize(systemConfigPanel.getSize());
        
        
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
            jContentPane.add(getSystemConfigPanel(), java.awt.BorderLayout.CENTER);  // Generated
            jContentPane.add(getOkCancelPanel(), java.awt.BorderLayout.SOUTH);  // Generated
        }
        return jContentPane;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
