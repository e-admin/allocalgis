/**
 * DetallesTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;


import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;



public class DetallesTableModel extends DefaultTableModel  {
	
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DetallesTableModel.class);
   
    private static String[] tableModelNames;
    
    public void setModelData(ArrayList listaDetalles, ArrayList listaColumnas) {
        try  {
        	
        	tableModelNames = new String[listaColumnas.size()];
        	
        	for(int i=0; i < listaColumnas.size(); i++ ){
        		setColumnName(i, (String)listaColumnas.get(i));
        	}
        	
            if (listaDetalles != null){
       
            	Object row[] = listaDetalles.toArray();
            	this.addRow(row);
            	
	                //for (Iterator it = listaDetalles.iterator(); it.hasNext(); ) {		            	
	            	//String dato = (String) it.next();
	            	 
	                //Object row[] = new Object[] {};
				    //this.addRow(row);
	                //}
            }

	    	fireTableDataChanged();


        }
        catch(Exception e) {
            logger.error("Error al poner la lista de detalles: "+ e.toString());
        }
	}

	public int getColumnCount() {
		return tableModelNames.length;
	}

	public String getColumnName(int c) {
		return tableModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return tableModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
}
