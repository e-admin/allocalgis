package com.geopista.app.catastro;

import com.geopista.app.AppContext;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.GEOPISTAWorkbench;
import com.geopista.util.ApplicationContext;
import javax.swing.JOptionPane;

/** 
 * Edición de datos y capas de Catastro
 */
 
public class GeopistaEditarCatastroPanel
{
  private ApplicationContext appcont = AppContext.getApplicationContext();
  private boolean acceso;
  private JOptionPane OpCuadroDialogo;
  
  public GeopistaEditarCatastroPanel()
  {
  	final String[] parametros = new String[6];

    try{
      GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Catastro.Editar Catastro");
      acceso = appcont.checkPermission(geopistaPerm,"Catastro");
      if(acceso)
      { 
  	    String rutaMapa = appcont.getString("url.mapa.catastro");
        parametros[0] = "-map";
        parametros[1] = rutaMapa;
        parametros[2] = "-closeall";
  	    parametros[3] = "false";
        parametros[4] = "-properties";
        parametros[5] = "GeopistaHerramientas-properties.xml";
        GEOPISTAWorkbench.main(parametros);
      }
      else
      {
        OpCuadroDialogo.showMessageDialog(null, appcont.getI18nString("SinAcceso"));
      }
	   }
    catch(Exception e)
      	{
    	  e.printStackTrace();
       }
  }
}