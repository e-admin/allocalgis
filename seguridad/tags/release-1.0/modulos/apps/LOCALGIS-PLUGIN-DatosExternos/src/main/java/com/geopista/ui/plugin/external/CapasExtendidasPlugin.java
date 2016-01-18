package com.geopista.ui.plugin.external;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.QueryExpression;
import nickyb.sqleonardo.querybuilder.syntax.QuerySpecification;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens.Column;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens._Expression;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class CapasExtendidasPlugin extends AbstractPlugIn {
	
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private Blackboard blackboard  = aplicacion.getBlackboard();
	
	public void initialize(PlugInContext context) throws Exception {
	      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      featureInstaller.addMainMenuItem(this,
	            new String[] { aplicacion.getI18nString("Tools"), aplicacion.getI18nString("Datos Externos") },
	            aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas"), false, null,
	            null);
	}
	
	public boolean execute(PlugInContext context) throws Exception {

        if(!aplicacion.isLogged())
        {
             aplicacion.setProfile("Geopista");
             aplicacion.login();
        }

        if(aplicacion.isLogged())
        {
        	if (!isEmptyCapasExtendidas()) {
	        	CapasExtendidasDialog capasExtendidasDialog = new CapasExtendidasDialog(context.getWorkbenchFrame(),aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas.titulo"),true);
	        	GUIUtil.centreOnWindow(capasExtendidasDialog);
	        	capasExtendidasDialog.setVisible(true);
        	}
        	else {
        		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas.error.contenido"), aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas.error.titulo"), JOptionPane.INFORMATION_MESSAGE);
        	}
        }

		return true;
	}
	
	private boolean isEmptyCapasExtendidas() {
		return false;

	}
	
}
