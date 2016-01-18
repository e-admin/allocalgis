package com.geopista.ui.plugin.georreferenciacionExterna.dialog;

import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.georreferenciacionExterna.paneles.GeorreferenciacionExternaPanelConsultas;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class GeorreferenciaExternaConsultaDialog extends JDialog{

	
	private static ApplicationContext aplicacion = AppContext.getApplicationContext();
	private GeorreferenciacionExternaPanelConsultas  georreferenciacionExternaPanelConsultas= null;
	
	public GeorreferenciaExternaConsultaDialog(PlugInContext context, String user){
		
		super(aplicacion.getMainFrame(), aplicacion
                .getI18nString("GeopistaMapPropertiesPlugInDescription"), true);
		georreferenciacionExternaPanelConsultas = new GeorreferenciacionExternaPanelConsultas(context, user ,this);
		 this.setResizable(false);
         this.getContentPane().add(georreferenciacionExternaPanelConsultas);
         this.setSize(700, 600);
         this.setLocation(200, 120);
 
         this.setVisible(true);
	} 
	
	public boolean wasOKPressed() {
        return georreferenciacionExternaPanelConsultas.wasOKPressed();
    }
}
