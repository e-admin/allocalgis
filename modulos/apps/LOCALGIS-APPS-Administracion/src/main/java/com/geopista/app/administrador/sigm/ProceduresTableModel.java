/**
 * ProceduresTableModel.java
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

public class ProceduresTableModel  extends DefaultTableModel {
   // public static final int idIndex=0;
   // public static final int idNombre=1;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProceduresTableModel.class);
    private static String[] modelNames = new String[] {"ID", "Capa", "Tabla", "Mapa", "Procedimiento SiGM"};

   /* public ListaRoles getModelListaRoles() {
        return modelListaRoles;
    }*/

    public void setModelData(HashMap<String, Procedure> procedures) {
    	setModelData(procedures, "");
	}

    public void setModelData(HashMap<String, Procedure> procedures, String filtro) {
        try
        {
        	if(procedures!=null && procedures.size()>0){
	            Iterator<String> keyIt = procedures.keySet().iterator();
	            while(keyIt.hasNext()){
	            	String key = keyIt.next();
	            	Procedure procedimiento = procedures.get(key);
				    if(filtro.equals("") || procedimiento.getId().toUpperCase().contains(filtro.toUpperCase())){
				    	Object row[] = new Object[] {procedimiento.getId(), procedimiento.getLayerName(),procedimiento.getTableName(),procedimiento.getMapName(),procedimiento.getProcedureType()};
				    	this.addRow(row);
	            	}
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

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}





}
