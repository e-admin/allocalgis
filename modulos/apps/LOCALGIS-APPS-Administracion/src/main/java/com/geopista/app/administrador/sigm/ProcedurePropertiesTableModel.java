/**
 * ProcedurePropertiesTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.sigm;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.administrador.Usuario;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedureProperty;

public class ProcedurePropertiesTableModel  extends DefaultTableModel {
   // public static final int idIndex=0;
   // public static final int idNombre=1;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProcedurePropertiesTableModel.class);
    private static String[] modelNames = new String[] {"Atributo SiGM", "Nombre", "Tipo", "Buscable", "Mostrable"};

   /* public ListaRoles getModelListaRoles() {
        return modelListaRoles;
    }*/

    public void setModelData(HashMap<String, ProcedureProperty> procedureProperties) {
    	try
        {
    		if(procedureProperties!=null && procedureProperties.size()>0){
	            Iterator<ProcedureProperty> it = procedureProperties.values().iterator();
	            while(it.hasNext()){
	            	ProcedureProperty propiedad = it.next();
	            	Object row[] = new Object[] {propiedad.getProperty(), propiedad.getName(), propiedad.getType(), propiedad.getSearchactive(), propiedad.getActive()};
				    this.addRow(row);            	
			    }
		    	fireTableDataChanged();
    		}
        }catch(Exception e)
        {
            logger.error("Error al poner la lista de usuarios: "+ e.toString());
        }
	}

	public int getColumnCount() {
		return modelNames.length;
	}

	public String getColumnName(int c) {
		return modelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return modelNames[c]=sName;
    }

    public boolean isCellEditable(int row,int cols){
      //if(cols==0){
    	 return false;
      //}
      //return true;     
    }    
}
