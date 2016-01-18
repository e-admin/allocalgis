/**
 * ConsultaTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georreferenciacionExterna.consulta;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean;

public class ConsultaTableModel extends DefaultTableModel{
	
	public static final int idIndex=0;
	public static final int idNombreConsulta=1;
    public static final int idDescripcion=2;
    public static final int idUsuario=3;
	
	 private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ConsultaTableModel.class);
	 private static String[] rolesModelNames = new String[] {"Id", "Consulta","Descripcion","Usuario"};
	
	
	 public void setModelData(ArrayList lstConsultas) {
	        try
	        {

	            for (int i=0; i<lstConsultas.size(); i++) {
	            	
	            	ConsultaDatosBean consultaDatos = (ConsultaDatosBean)lstConsultas.get(i);
	            	            
				    Object row[] = new Object[] {
				    		String.valueOf(consultaDatos.getId()), 
				    		consultaDatos.getNombreConsulta(),
				    		consultaDatos.getDescripcion(),
				    		consultaDatos.getUsuario()};
				    this.addRow(row);
			    }

		    	fireTableDataChanged();


	        }catch(Exception e)
	        {
	            logger.error("Error al poner la lista de Consultas: "+ e.toString());
	        }
		}
	 
	 public int getColumnCount() {
			return rolesModelNames.length;
		}

		public String getColumnName(int c) {
			return rolesModelNames[c];
		}
	    public String setColumnName(int c, String sName) {
	        return rolesModelNames[c]=sName;
	    }

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

}
