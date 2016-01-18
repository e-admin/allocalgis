/**
 * GeopistaEditarCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 01-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.GEOPISTAWorkbench;
import com.geopista.util.ApplicationContext;

public class  GeopistaEditarCatastro
{
   private ApplicationContext appcont = AppContext.getApplicationContext();
   private boolean acceso;
   private JOptionPane OpCuadroDialogo;

   public GeopistaEditarCatastro()
   {
    final String[] parametros = new String[6];
   
    try
    {
      GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.EditarPlaneamiento");
      acceso = appcont.checkPermission(geopistaPerm,"Planeamiento");
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

