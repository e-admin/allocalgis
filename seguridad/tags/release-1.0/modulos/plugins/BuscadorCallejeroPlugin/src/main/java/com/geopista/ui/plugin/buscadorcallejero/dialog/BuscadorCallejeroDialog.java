package com.geopista.ui.plugin.buscadorcallejero.dialog;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.buscadorcallejero.paneles.BuscadorCallejeroPanelBusqueda;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class BuscadorCallejeroDialog extends JDialog{

	
	private static ApplicationContext aplicacion = AppContext.getApplicationContext();
	private BuscadorCallejeroPanelBusqueda  buscadorCallejeroPanelBusqueda= null;
	
	public BuscadorCallejeroDialog(PlugInContext context, String user){
		
 	
    	
		super(aplicacion.getMainFrame(), aplicacion
                .getI18nString("BuscadorCallejeroPlugInDescription"), true);
		buscadorCallejeroPanelBusqueda = new BuscadorCallejeroPanelBusqueda(context, user);
		
		Locale loc=I18N.getLocaleAsObject();      
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.buscadorcallejero.languages.BuscadorCallejeroi18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("BuscadorCallejero",bundle);   
    	
		 this.setResizable(false);
         this.getContentPane().add(buscadorCallejeroPanelBusqueda);
         this.setSize(550, 500);
         this.setLocation(250, 320);
         this.setTitle(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.buscador"));

         this.setVisible(true);
	}
	
	public boolean wasOKPressed() {
        return buscadorCallejeroPanelBusqueda.wasOKPressed();
    }
}
