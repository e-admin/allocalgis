package com.geopista.app.administrador.usuarios;

import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;
import com.geopista.protocol.administrador.Conexion;
import com.geopista.protocol.administrador.Operacion;

public class OperacionesTableModel extends DefaultTableModel {
		
	    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OperacionesTableModel.class);
	   
	    private static String[] tableModelNames = new String[] { "", "ELEM_AFECTADO", "OPERACION", "NOMBRE", "FECHA", };

	    public void setModelData(Hashtable listaOperaciones) {
	        try  {
	            if (listaOperaciones != null){
	       
		            for (Iterator it= listaOperaciones.keySet().iterator(); it.hasNext(); ) {		            	
		            	String opCount = (String) it.next();
		            	Operacion op = (Operacion) listaOperaciones.get(opCount);
		            	
		                SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
		                String fecha =(op.getFechaOperacion()==null?"":formatter.format(op.getFechaOperacion()));                  
		                
		                Object row[] = new Object[] {opCount, op.getTipoOperacion(), op.getOperacionRealizada(), op.getCapaAfectada(), fecha};
					    this.addRow(row);
		            }
	            }

		    	fireTableDataChanged();


	        }
	        catch(Exception e) {
	            logger.error("Error al poner la lista de operaciones: "+ e.toString());
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
