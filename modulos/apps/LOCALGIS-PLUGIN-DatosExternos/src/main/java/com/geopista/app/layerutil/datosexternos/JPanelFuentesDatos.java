/**
 * JPanelFuentesDatos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.datosexternos;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.external.ConfigureExternalDSDialog;
import com.geopista.ui.plugin.external.ConnectionUtility;
import com.geopista.ui.plugin.external.ExternalDataSource;
import com.geopista.ui.plugin.external.ExternalDataSourceDAO;
import com.geopista.ui.plugin.external.ExternalDataSourceDialog;
import com.geopista.ui.plugin.external.ExternalDataSourcePlugin;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class JPanelFuentesDatos extends JPanel {

    private JList jListFuentesDatos = null;
    private JScrollPane scrollFuentesDatos = null;
    private DefaultListModel listmodel = new DefaultListModel();
    
    private JLabel lblCadenaConexion = null;
    private JTextField txtCadenaConexion = null;
    
    private JLabel lblUserName = null;
    private JTextField txtUserName = null;
    
    private JLabel lblPassword = null;
    private JTextField txtPassword = null;

    private JLabel lblDriver = null;
    private JTextField txtDriver = null;
    
    private JButton btnDataSourceLayer = null;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private DatosExternosPanel datosExternosPanel;
	
    public JPanelFuentesDatos(DatosExternosPanel datosExternosPanel){
    	super();
    	this.datosExternosPanel = datosExternosPanel;
    	initialize();
    }
    

    private void initialize(){
        
    	this.setLayout(null);
        lblCadenaConexion = new JLabel();
        lblCadenaConexion.setBounds(new Rectangle(320,35,155,24));
        lblCadenaConexion.setText(I18N.get("GestorCapas","datosExternos.lblCadenaConexion"));
        this.add(getTxtCadenaConexion(), null);
        
        lblDriver = new JLabel();
        lblDriver.setBounds(new Rectangle(320,65,155,24));
        lblDriver.setText(I18N.get("GestorCapas","datosExternos.lblDriver"));    
        this.add(getTxtDriver(), null);
        
        lblUserName = new JLabel();
        lblUserName.setBounds(new Rectangle(320,95,155,24));
        lblUserName.setText(I18N.get("GestorCapas","datosExternos.lblUsuario"));
        this.add(getTxtUserName(), null);
        
        lblPassword = new JLabel();
        lblPassword.setBounds(new Rectangle(320,125,155,24));
        lblPassword.setText(I18N.get("GestorCapas","datosExternos.lblPassword"));    
        this.add(getTxtPassword(), null);
        
        scrollFuentesDatos = getScrollFuentesDatos();
        jListFuentesDatos.setForeground(Color.gray);
        jListFuentesDatos.setEnabled(false);
        
        this.add(getBtnDataSource(), null);
    	getBtnDataSource().setEnabled(false);
        this.add(scrollFuentesDatos, null);
        this.add(lblCadenaConexion, null);
        this.add(lblPassword, null);
        this.add(lblUserName, null);
        this.add(lblDriver, null);
        
        jListFuentesDatos.addListSelectionListener(new ValueReporter());
    }

    private class ValueReporter implements ListSelectionListener {
    	public void valueChanged(ListSelectionEvent event) {
    		if (!event.getValueIsAdjusting()){
    			ExternalDataSource externalDS = (ExternalDataSource) jListFuentesDatos.getSelectedValue();
    			
    			if (externalDS!=null){
    				datosExternosPanel.setExternalDS(externalDS);
    				txtCadenaConexion.setText(externalDS.getConnectString());
    				txtDriver.setText(externalDS.getDriver());
    				txtPassword.setText(externalDS.getPassword());
    				txtUserName.setText(externalDS.getUserName());
    				if ((datosExternosPanel.getTxtAreaSQL().getText()!=null) && (!datosExternosPanel.getTxtAreaSQL().getText().equals("")))
    					datosExternosPanel.getTxtAreaSQL().setText("");
    					datosExternosPanel.getJPanelCamposExternos().remove();
    					datosExternosPanel.getJPanelCamposExternos().cargarAtributos();
    			}    				
    		}
    	}
    }
    
	private void setEvents() {
    	
		jListFuentesDatos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            	lstFuentesDatosValueChanged(evt);
            }
        });
        
       btnDataSourceLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnDataSourceLayer_actionPerformed(evt);
            }
        });

    }
    
    public JButton getBtnDataSource(){
        if (btnDataSourceLayer == null)
        {
        	btnDataSourceLayer = new JButton();
        	btnDataSourceLayer.setBounds(new Rectangle(100,130,100,25));
        	btnDataSourceLayer.setText(I18N.get("GestorCapas","datosExternos.boton.dataSource"));
        	btnDataSourceLayer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    //Llamar al metodo correspondiente
                	btnDataSourceLayer_actionPerformed(e);
                }
        	});
        }
        return btnDataSourceLayer;
    } 
    
    /**
     * Acción realizada al pulsar el botón de DataSource
     * @param e
     */
    private void btnDataSourceLayer_actionPerformed(ActionEvent e){
	   	
    	// Cargamos los drivers de las BBDD para las conexiones a los datos externos:
    	Hashtable drivers = ConnectionUtility.findAllDrivers();
    	ConnectionUtility.loadDrivers(drivers);
    	
    	boolean paso = false;
    	ExternalDataSourcePlugin externalDS = new ExternalDataSourcePlugin();
        if (externalDS.isEmptyExternalDataSourceList()) {
        	ExternalDataSourceDialog dialog = new ExternalDataSourceDialog(aplicacion.getMainFrame(),true, drivers);
    		GUIUtil.centreOnWindow(dialog);
            dialog.setVisible(true);
            while (!dialog.isCancelButtonPressed() && !dialog.isClosed()  && !paso) {
            	ExternalDataSource dataSource = dialog.getExternalDataSource();
            	if (!externalDS.testConnection(dataSource)){
            		JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.errorConexion"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo"), JOptionPane.ERROR_MESSAGE);
            		dialog.setVisible(true);
            	}
            	else {
                	if (!externalDS.insertExternalDataSource(dataSource)) {
                		ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.errorCrear"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo2"));
        				dialog.setVisible(true);
        				dataSource = dialog.getExternalDataSource();
                	}
                	else {
                		JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.dataSourceCreado"));
                		paso = true;
                	}
            	}
            }            
        }
        else {
        	
        	ConfigureExternalDSDialog configDialog = new ConfigureExternalDSDialog(aplicacion.getMainFrame(),true);
    		GUIUtil.centreOnWindow(configDialog);
        	configDialog.setDataSourceList(externalDS.getDataSourceNames());
        	configDialog.setVisible(true);        	
        	if (configDialog.isNewButtonPressed()) {
        		ExternalDataSourceDialog dialog = new ExternalDataSourceDialog(aplicacion.getMainFrame(),true, drivers);
        		GUIUtil.centreOnWindow(dialog);
                dialog.setVisible(true);
                while (!dialog.isCancelButtonPressed() && !dialog.isClosed()  && !paso) {
                	ExternalDataSource dataSource = dialog.getExternalDataSource();
                	if (!externalDS.testConnection(dataSource)){
                		JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.errorConexion"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo"), JOptionPane.ERROR_MESSAGE);
                		dialog.setVisible(true);
                	}
                	else {
	                	if (!externalDS.insertExternalDataSource(dataSource)) {
	                		ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.errorCrear"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo2"));
	        				dialog.setVisible(true);
	        				dataSource = dialog.getExternalDataSource();
	                	}
	                	else {
	                		JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.dataSourceCreado"));
	                		paso = true;
	                	}
                	}
                }            
        	}
        	if (configDialog.isDeletebuttonPressed()) {
        		String externalDataSourceName = configDialog.getSelectedDataSource();
        		externalDS.deleteExternalDataSource(externalDataSourceName);
        	}
        	if (configDialog.isModifyButtonPressed()) {
        		String externalDataSourceName = configDialog.getSelectedDataSource();
        		ExternalDataSource externalDataSource = externalDS.getExternalDataSource(externalDataSourceName);
        		ExternalDataSourceDialog dialog = new ExternalDataSourceDialog(aplicacion.getMainFrame(),true, drivers);
        		GUIUtil.centreOnWindow(dialog);
        		String oldName = externalDataSource.getName();
        		int idExternalDataSource = externalDataSource.getId();
        		dialog.setExternalDataSource(externalDataSource);
        		dialog.setInfo();
                dialog.setVisible(true);
                while (!dialog.isCancelButtonPressed() && !dialog.isClosed() && !paso) {
                	ExternalDataSource dataSource = dialog.getExternalDataSource();
                	dataSource.setId(idExternalDataSource);
                  	if (!externalDS.testConnection(dataSource)){
                		JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.errorConexion"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo"), JOptionPane.ERROR_MESSAGE);
                		dialog.setVisible(true);
                  	}
                  	else {
                  		if (!externalDS.updateExternalDataSource(dataSource, oldName)) {
                  			ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.errorCrear"), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.titulo2"));
                  			dialog.setVisible(true);
                  			dataSource = dialog.getExternalDataSource();
                  		}
                  		else {
                  			JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.dataSourceModficado"));
                  			paso = true;
                  		}
                  	}
                
                }
        	}
        	paso = true;
        }
        datosExternosPanel.enter();
    //    jListFuentesDatos = null;
     //   scrollFuentesDatos = null;
        getScrollFuentesDatos();
    	
    }  
    
    public JTextField getTxtCadenaConexion(){
        if (txtCadenaConexion == null){
            txtCadenaConexion = new JTextField();
            
            txtCadenaConexion.setText("");
            txtCadenaConexion.setBounds(new java.awt.Rectangle(420,35,400,20));
            txtCadenaConexion.setEnabled(false);
            
        }
        return txtCadenaConexion;
    }

    public JTextField getTxtDriver(){
        if (txtDriver == null){
            txtDriver = new JTextField();
            
            txtDriver.setText("");
            txtDriver.setBounds(new java.awt.Rectangle(420,65,400,20));
            txtDriver.setEnabled(false);

        }
        return txtDriver;
    }

    public JTextField getTxtUserName(){
        if (txtUserName == null){
            txtUserName = new JTextField();
            
            txtUserName.setText("");
            txtUserName.setBounds(new java.awt.Rectangle(420,95,400,20));
            
            txtUserName.setEnabled(false);
        }
        return txtUserName;
    }

    public JTextField getTxtPassword(){
        if (txtPassword == null)
        {
            txtPassword = new JTextField();
            
            txtPassword.setText("");
            txtPassword.setBounds(new java.awt.Rectangle(420,125,400,20));

            txtPassword.setEnabled(false);
        }
        return txtPassword;
    }
    /**
     * This method initializes scrLayers	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getScrollFuentesDatos()
    {
        if (scrollFuentesDatos == null)
        {
        	scrollFuentesDatos = new JScrollPane();
        	scrollFuentesDatos.setBounds(new Rectangle(20,20,250,100));   
        	scrollFuentesDatos.setViewportView(getLstFuentesDatos());
        //	scrollFuentesDatos.setBorder(BorderFactory.createTitledBorder(""));
        }
        else
        {
        	scrollFuentesDatos.setViewportView(getLstFuentesDatos());
        }
        return scrollFuentesDatos;
    }
    
    /**
     * This method initializes lstLayers	
     * 	
     * @return javax.swing.JList	
     */
    public JList getLstFuentesDatos(){
        if (jListFuentesDatos == null){ 
        	jListFuentesDatos = new JList(listmodel);
            
        	FuentesDatosListCellRenderer renderer = new FuentesDatosListCellRenderer();
        	jListFuentesDatos.setCellRenderer(renderer);
            
        	ExternalDataSourceDAO externalDSDAO = new ExternalDataSourceDAO();
            Vector vectorExternalDS = null;

            vectorExternalDS = externalDSDAO.list();
            
            listmodel.removeAllElements();
            for (int i=0; i< vectorExternalDS.size(); i++){
                listmodel.addElement(vectorExternalDS.get(i));   
            }           
        }
        else{
        	ExternalDataSourceDAO externalDSDAO = new ExternalDataSourceDAO();
            Vector vectorExternalDS = null;

            vectorExternalDS = externalDSDAO.list();

            if (listmodel!=null && listmodel.getSize()!=0)
                listmodel.removeAllElements();
            for (int i=0; i< vectorExternalDS.size(); i++){
                listmodel.addElement(vectorExternalDS.get(i));   
            }       
        }
        return jListFuentesDatos;
    }
    
    public void lstFuentesDatosValueChanged(javax.swing.event.ListSelectionEvent evt){
    	getLstFuentesDatos();
    }
    
	public JList getJListFuentesDatos() {
		return jListFuentesDatos;
	}

}
