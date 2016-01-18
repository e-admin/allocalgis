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
