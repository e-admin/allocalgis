/**
 * DialogForDataStorePlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;

import com.geopista.app.AppContext;
import com.geopista.util.UserCancellationException;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelDialog;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;
//import com.geopista.ui.plugin.routeenginetools.leerreddebase.LeerRedDeBasePlugIn;

public abstract class DialogForDataStorePlugin extends ThreadedBasePlugIn {

	public boolean execute(final PlugInContext context) {
		// The user may have added connections using the Connection Manager
		// Toolbox. So refresh the connectionComboBox.
		// OKCancelDialog dlg=null;
		// panel(context).populateConnectionComboBox();
		OKCancelDialog dlg = null;
		if (getAplicationConnection() != null){
			dlg = getDialog(context);
			//if (this instanceof LeerRedDeBasePlugIn){
				//dlg.setTitle("Leer Red de Base de Datos");
			//}
			dlg.setVisible(true);
		}

		return dlg.wasOKPressed();
//		return true;
	}
	public void run(TaskMonitor monitor, final PlugInContext context) {

		try {
			WithOutConnectionPanel panel = (WithOutConnectionPanel) dialog.getCustomComponent();
			String networkName = ((PanelToLoadFromDataStore) panel).getSubredSelected().trim();
			linkGraphToStoreAndCreateLayer(networkName, monitor, context);
			
			
		} catch(UserCancellationException e)
		{
		    return;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected abstract void linkGraphToStoreAndCreateLayer(String networkName,
			TaskMonitor monitor, PlugInContext context) throws Exception;

	private OKCancelDialog getDialog(PlugInContext context) {

		dialog = createDialog(context);
		return dialog;
	}

	protected WithOutConnectionPanel panel(PlugInContext context) {
		return (WithOutConnectionPanel) getDialog(context).getCustomComponent();
	}

	private OKCancelDialog dialog;
	private Connection aplicationConnection = null;
	protected WithOutConnectionPanel connectedNetworkPanel;

	public Connection getAplicationConnection() {
		if (aplicationConnection == null){
			try {
				if (!AppContext.getApplicationContext().isLogged()){
					Connection conn = AppContext.getApplicationContext().getConnection();
					if (conn != null){
						this.aplicationConnection = conn;
					}
				}else{
					this.aplicationConnection = AppContext.getApplicationContext().getConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return aplicationConnection;
	}

	public void setAplicationConnection(Connection aplicationConnection) {
		this.aplicationConnection = aplicationConnection;
	}

	private OKCancelDialog createDialog(PlugInContext context) {
		
	    	connectedNetworkPanel = createPanel(context);				
		OKCancelDialog dialog = new OKCancelDialog(context.getWorkbenchFrame(),
				getName(), 
				true, 
				connectedNetworkPanel,
				new OKCancelDialog.Validator() {
					public String validateInput(Component component) {
						return ((WithOutConnectionPanel) component).validateInput();
					}
				});
		dialog.pack();
		//dialog.setVisible(true);
		//GUIUtil.centreOnWindow(dialog);
		GUIUtil.centreOnWindow(dialog);
			
		return dialog;
	}

	protected abstract WithOutConnectionPanel createPanel(PlugInContext context);

}
