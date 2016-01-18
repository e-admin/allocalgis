package com.geopista.ui.plugin.external;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class ExternalDataSourcePlugin extends AbstractPlugIn {
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  

	
    private ExternalDataSourceDialog dialog;
    private ConfigureExternalDSDialog configDialog;
    private Hashtable drivers;
    
	public boolean execute(PlugInContext context) throws Exception {
		boolean first = isEmptyExternalDataSourceList();
        if(!aplicacion.isLogged()){
             aplicacion.setProfile("Geopista");
             aplicacion.login();             
        }

        if(aplicacion.isLogged())
        {
        	return prompt(context,first);
        }
        return true;
	}

	public void initialize(PlugInContext context) throws Exception {
		  //ConnectionUtility.findAllDrivers(".");
		  //System.out.println(aplicacion.getI18nString("ExternalDataSource"));
		  drivers = ConnectionUtility.findAllDrivers();
		  ConnectionUtility.loadDrivers(drivers);
	      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      featureInstaller.addMainMenuItem(this,
	    		  new String[] { aplicacion.getI18nString("Tools"),aplicacion.getI18nString("Datos Externos") },
	    		  aplicacion.getI18nString("ConfigureExternalDataSource.titulo"), false, null,null);
	}
	
	private boolean prompt(PlugInContext context,boolean empty) {
		
        if (isEmptyExternalDataSourceList()) {
        	initNewDialog(context);
            dialog.setVisible(true);
            while (!dialog.isCancelButtonPressed()&&!dialog.isClosed()) {
            	ExternalDataSource dataSource = dialog.getExternalDataSource();
            	if (!testConnection(dataSource)){
            		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Error al establecer la conexion", "Error", JOptionPane.ERROR_MESSAGE);
            		dialog.setVisible(true);
            	}
            	else {
                	if (!insertExternalDataSource(dataSource)) {
                		ErrorDialog.show(context.getWorkbenchFrame(), "Error", "Ya existe un DataSource con ese nombre", "Nombre Duplicado");
        				dialog.setVisible(true);
        				dataSource = dialog.getExternalDataSource();
                	}
                	else {
                		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "DataSource Creado");
                		return true;
                	}
            	}
            }            
             return true;
        }
        else {
        	initConfigureDialog(context);
        	configDialog.setDataSourceList(getDataSourceNames());
        	configDialog.setVisible(true);        	
        	if (configDialog.isNewButtonPressed()) {
        		initNewDialog(context);
                dialog.setVisible(true);
                while (!dialog.isCancelButtonPressed()&&!dialog.isClosed()) {
                	ExternalDataSource dataSource = dialog.getExternalDataSource();
                	if (!testConnection(dataSource)){
                		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Error al establecer la conexion", "Error", JOptionPane.ERROR_MESSAGE);
                		dialog.setVisible(true);
                	}
                	else {
	                	if (!insertExternalDataSource(dataSource)) {
	                		ErrorDialog.show(context.getWorkbenchFrame(), "Error", "Ya existe un DataSource con ese nombre", "Nombre Duplicado");
	        				dialog.setVisible(true);
	        				dataSource = dialog.getExternalDataSource();
	                	}
	                	else {
	                		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "DataSource Creado");
	                		return true;
	                	}
                	}
                }            
        	}
        	if (configDialog.isDeletebuttonPressed()) {
        		String externalDataSourceName = configDialog.getSelectedDataSource();
                deleteExternalDataSource(externalDataSourceName);
        	}
        	if (configDialog.isModifyButtonPressed()) {
        		String externalDataSourceName = configDialog.getSelectedDataSource();
        		ExternalDataSource externalDataSource = getExternalDataSource(externalDataSourceName);
        		initNewDialog(context);
        		String oldName = externalDataSource.getName();
        		int idExternalDataSource = externalDataSource.getId();
        		dialog.setExternalDataSource(externalDataSource);
        		dialog.setInfo();
                dialog.setVisible(true);
                while (!dialog.isCancelButtonPressed()&&!dialog.isClosed()) {
                	ExternalDataSource dataSource = dialog.getExternalDataSource();
                	dataSource.setId(idExternalDataSource);
                  	if (!testConnection(dataSource)){
                		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Error al establecer la conexion", "Error", JOptionPane.ERROR_MESSAGE);
                		dialog.setVisible(true);
                  	}
                  	else {
                  		if (!updateExternalDataSource(dataSource, oldName)) {
                  			ErrorDialog.show(context.getWorkbenchFrame(), "Error", "Ya existe un DataSource con ese nombre", "Nombre Duplicado");
                  			dialog.setVisible(true);
                  			dataSource = dialog.getExternalDataSource();
                  		}
                  		else {
                  			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "DataSource Modificado");
                  			return true;
                  		}
                  	}
                
                }
        	}
        	return true;       	
        }
	}
	
	public boolean testConnection(ExternalDataSource externalDataSource) {
		return ConnectionUtility.testConnection(externalDataSource.getDriver(),
				externalDataSource.getConnectString(), externalDataSource.getUserName(), externalDataSource.getPassword());		
	}
	
	private void initNewDialog(PlugInContext context) {

		dialog = new ExternalDataSourceDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),true,drivers);
		GUIUtil.centreOnWindow(dialog);
	}
	
	private void initConfigureDialog(PlugInContext context) {
		configDialog = new ConfigureExternalDSDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),true);
		GUIUtil.centreOnWindow(configDialog);
	}
	
	public ExternalDataSource getExternalDataSource(String externalDataSourceName) {
		ExternalDataSourceDAO dataSourceDAO = new ExternalDataSourceDAO();
		return dataSourceDAO.find(externalDataSourceName);		
	}
	
	public boolean insertExternalDataSource(ExternalDataSource externalDataSource) {
		ExternalDataSourceDAO dataSourceDAO = new ExternalDataSourceDAO();
		if (dataSourceDAO.find(externalDataSource.getName())!=null) {
			return false;
		}
		else {
			dataSourceDAO.insert(externalDataSource);
			return true;			
		}				

	}
	public boolean updateExternalDataSource(ExternalDataSource externalDataSource,String oldName) {
		ExternalDataSourceDAO dataSourceDAO = new ExternalDataSourceDAO();
		if (oldName.equals(externalDataSource.getName())){
			dataSourceDAO.update(externalDataSource);
			return true;
		}
		else
		{	if (dataSourceDAO.find(externalDataSource.getName())!=null) {
				return false;
			}
			else {
				dataSourceDAO.update(externalDataSource);
				return true;			
			}				
		}
	}
	public void deleteExternalDataSource(String externalDataSourceName) {
		ExternalDataSourceDAO dataSourceDAO = new ExternalDataSourceDAO();
		ExternalDataSource externalDataSource = dataSourceDAO.find(externalDataSourceName);
		dataSourceDAO.delete(externalDataSource.getId());			

	}
	public boolean isEmptyExternalDataSourceList() {
		ExternalDataSourceDAO dataSourceDAO = new ExternalDataSourceDAO();
		Vector vector = dataSourceDAO.list();
		if (vector==null||vector.size()==0) {
			return true;
		}
		else {
			return false;
		}		
	}
	
	public String[] getDataSourceNames() {
		ExternalDataSourceDAO dataSourceDAO = new ExternalDataSourceDAO();
		Vector vector = dataSourceDAO.list();
		if (vector == null) {
			return null;
		}
		else {
			String[] dataSourcesNames = new String[vector.size()];
			int i = 0;
			for (Iterator iterator = vector.iterator()  ; iterator.hasNext(); i ++) {
				ExternalDataSource dataSource = (ExternalDataSource) iterator.next();
				dataSourcesNames[i] = dataSource.getName();
			}
			return dataSourcesNames;
		}		
	}
	

	
	public static void main(String[] args) {
		EncriptarPassword encrypt = new EncriptarPassword();
		try {
			System.out.println(encrypt.undoEncrip("hK1KB3GldGloLHSBEKL1sQ=="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
