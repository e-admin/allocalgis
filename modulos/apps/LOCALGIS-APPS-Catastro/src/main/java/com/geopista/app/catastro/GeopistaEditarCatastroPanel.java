/**
 * GeopistaEditarCatastroPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.GEOPISTAWorkbench;
import com.geopista.util.ApplicationContext;

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