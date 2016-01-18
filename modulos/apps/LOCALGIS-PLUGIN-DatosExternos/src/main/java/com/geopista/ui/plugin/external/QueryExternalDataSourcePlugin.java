/**
 * QueryExternalDataSourcePlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

public class QueryExternalDataSourcePlugin extends AbstractPlugIn {
	
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private Blackboard blackboard  = aplicacion.getBlackboard();
	
	public void initialize(PlugInContext context) throws Exception {
	      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      featureInstaller.addMainMenuItem(this,
	            new String[] { aplicacion.getI18nString("Tools"), aplicacion.getI18nString("Datos Externos") },
	            aplicacion.getI18nString("ConfigureQueryExternalDataSource"), false, null,
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
        	if (!isEmptyExternalDataSourceList()) {
	        	ConfigureQueryDialog configureQueryDialog = new ConfigureQueryDialog(context.getWorkbenchFrame(), aplicacion.getI18nString("ConfigureQueryExternalDataSource"),true);
	        	GUIUtil.centreOnWindow(configureQueryDialog);
	        	configureQueryDialog.setVisible(true);
        	}
        	else {
        		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), aplicacion.getI18nString("ConfigureQueryExternalDataSource.dataSource.errorConfiguracion"), aplicacion.getI18nString("ConfigureQueryExternalDataSource.dataSource.informacion"), JOptionPane.INFORMATION_MESSAGE);
        	}
        	
        }

		return true;
	}
	
	private Hashtable getMetaData(QueryModel queryModel) {
		 QueryExpression queryExpression = queryModel.getQueryExpression();
		 QuerySpecification querySpecification = queryExpression.getQuerySpecification();
		 _Expression[] expressions = querySpecification.getSelectList();
		 Hashtable hashtable = new Hashtable();
		 for (int i = 0; i < expressions.length; i++) {
			_Expression expression = expressions[i];
			if (expression instanceof QueryTokens.Column) {
				QueryTokens.Column column = (Column) expression;
				String tableIdentifier = column.getTable().getIdentifier().replaceAll("\"", "");
				String columnName = column.getName();
				if (!hashtable.containsKey(tableIdentifier)) {
					Vector vector = new Vector();
					vector.add(columnName);
					hashtable.put(tableIdentifier, vector);
				}
				else {
					Vector vector = (Vector) hashtable.get(tableIdentifier);
					vector.add(columnName);					
				}
			}			
		 }
		 return hashtable;
	}
	
	private boolean isEmptyExternalDataSourceList() {
		ExternalDataSourceDAO dataSourceDAO = new ExternalDataSourceDAO();
		Vector vector = dataSourceDAO.list();
		if (vector==null||vector.size()==0) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
