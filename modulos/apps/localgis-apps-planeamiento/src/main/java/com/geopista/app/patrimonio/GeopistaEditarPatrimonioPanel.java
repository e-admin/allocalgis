/**
 * GeopistaEditarPatrimonioPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;

import java.util.ResourceBundle;

import com.geopista.ui.GEOPISTAWorkbench;
public class GeopistaEditarPatrimonioPanel 
{
 
   public GeopistaEditarPatrimonioPanel()
   {
    final String[] parametros = new String[6];

    try{
    ResourceBundle RB = ResourceBundle.getBundle("GeoPista");
    String rutaMapa = RB.getString("url.mapa.patrimonio");
    parametros[0] = "-map";
    parametros[1] = rutaMapa; 
    parametros[2] = "-closeall";
    parametros[3] = "false";
    parametros[4] = "-properties";
    parametros[5] = "GeopistaHerramientas-properties.xml";
    GEOPISTAWorkbench.main(parametros);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

}