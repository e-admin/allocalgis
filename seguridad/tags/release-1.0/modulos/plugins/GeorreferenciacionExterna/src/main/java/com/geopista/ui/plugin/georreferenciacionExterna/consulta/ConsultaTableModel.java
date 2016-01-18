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
