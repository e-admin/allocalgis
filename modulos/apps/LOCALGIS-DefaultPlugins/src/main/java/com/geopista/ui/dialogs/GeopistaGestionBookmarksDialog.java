/**
 * GeopistaGestionBookmarksDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class GeopistaGestionBookmarksDialog extends JFrame
{
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
  public GeopistaGestionBookmarksDialog(PlugInContext context)
  {
      this.setTitle(aplicacion.getI18nString("GestionBookmarks"));
      GeopistaGestionBookmarksPanel iniciar = new GeopistaGestionBookmarksPanel(context); 
      this.getContentPane().add(iniciar); //añade el panel al frame
      this.setSize(470,230);  
      this.setVisible(true);
      this.setLocation(150,150);
      this.setResizable(false);
  }

 /* public Hashtable hshListaMarcadores(){
    Hashtable lista = new Hashtable();
  try {
  
    lista.put("Marcador 1","M1");
    lista.put("Marcador 2", "M2");
    lista.put("Marcador 3", "M3");
  }catch (Exception e){
    e.printStackTrace();
    }
    return (lista);
    
  }*/


}

