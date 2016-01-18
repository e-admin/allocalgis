/**
 * ConfiguracionGestorFip.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.config;

import java.io.File;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;

public class ConfiguracionGestorFip {
	
	 private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	 
	 /**
     * Actualiza los municipios que estuvieran previamente configurados en
     * Geopista de forma que en la base de datos se informe del SRID para cada
     * uno de ellos.
     */
    public static void executeCargaPropertiesGestorFip() {
        boolean error = false;
      
    	HashMap propertiesHash = new HashMap();
    	 
    	propertiesHash.put("ws.timeout", "900000"); // 15 minutos
    	propertiesHash.put("ws.timeoutFip", "864000000");// 10 dias
    	
        aplicacion.getBlackboard().put(ConstantesGestorFIP.PROPERTIES_GESTORFIP, propertiesHash);
       
        
        if (error) {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "Error al cargar el fichero de propiedades", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    

}
