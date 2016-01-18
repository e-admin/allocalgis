package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;

import com.geopista.app.AppContext;
//import com.geopista.ui.plugin.routeenginetools.leerreddebase.LeerRedDeBasePlugIn;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelDialog;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;

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
			leeroguardarGrafoenBase((WithOutConnectionPanel) dialog
					.getCustomComponent(), monitor, context);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

	}

	protected abstract boolean leeroguardarGrafoenBase(WithOutConnectionPanel panel,
			TaskMonitor monitor, PlugInContext context) throws Exception,
			Throwable;

	private OKCancelDialog getDialog(PlugInContext context) {

		dialog = createDialog(context);
		return dialog;
	}

	protected WithOutConnectionPanel panel(PlugInContext context) {
		return (WithOutConnectionPanel) getDialog(context).getCustomComponent();
	}

	private OKCancelDialog dialog;
	private Connection aplicationConnection = null;

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
		
//		try {
//		if (!AppContext.getApplicationContext().isLogged()){
//			Connection conn = AppContext.getApplicationContext().getConnection();
//			if (conn != null){
//				aplicationConnection = conn;
//			}
//		} else {
//			aplicationConnection = AppContext.getApplicationContext().getConnection();
//		}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		WithOutConnectionPanel conPannel = createPanel(context);				
		OKCancelDialog dialog = new OKCancelDialog(context.getWorkbenchFrame(),
				getName(), 
				true, 
				conPannel,
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
