/**
 * BienesPreAltaTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 07-jul-2006
 * Time: 10:11:55
 * To change this template use File | Settings | File Templates.
 */

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.inventario.BienPreAltaBean;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class BienesPreAltaTableModel  extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
    private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(BienesPreAltaTableModel.class);
    public String[] columnNames;
    public boolean[] columnEditables;
    private String locale;
    private Hashtable rows= new Hashtable();
    private TableSorted tableSorted;
    private javax.swing.JTable tabla;
    private boolean showSeleccion = false;

    public BienesPreAltaTableModel(String[] colNames, boolean[] colEditables, String locale, boolean showSeleccion) {
        columnNames= colNames;
        columnEditables= colEditables;
        this.showSeleccion=showSeleccion;
        this.locale= locale;
        new DefaultTableModel(columnNames, 0);

    }
    

    public void setTableSorted(TableSorted tableSorted){
        this.tableSorted= tableSorted;
    }
    
    public TableSorted getTableSorted(){
    	return this.tableSorted;
    }

    public void setColumnNames(String[] colNames) {
        columnNames= colNames;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public boolean isCellEditable(int row, int col) {
        if (columnEditables[col]) return true;
        return false;
    }
    
    public Class getColumnClass(int columnIndex) {
    	try{
    		Object obj=tableSorted.getValueAt(0, columnIndex); 
    		return obj.getClass();
    	
    	}catch(Exception ex){}
    	return Object.class;
    }


    private void setRows(List lst){
        rows= new Hashtable();
        if ((lst == null) || (lst.size()==0)) return;
        for (int i=0; i<lst.size(); i++){
                rows.put(((BienPreAltaBean)lst.get(i)).getId(), lst.get(i));
        }
    }

    private Object[] getDatosRow(BienPreAltaBean bienPA){
    	
		Object[] rowData = { bienPA.getNombre(),
				bienPA.getDescripcion(), bienPA.getIdMunicipio(),
				bienPA.getFechaAdquisicion(),
				bienPA.getCosteAdquisicion(), bienPA.getId()};
		
        return rowData;
        
    }
   
    public void actualizarRow(int index, BienPreAltaBean obj){
    	try{
	        if (tableSorted == null) return;
	        String hidden= (String)tableSorted.getValueAt(index, tableSorted.getColumnCount()-1);
	        if (rows.containsKey(hidden))
	            rows.remove(hidden);
	        rows.put(hidden, obj);
	        tableSorted.removeRow(index);
	        if (index >= tableSorted.getRowCount()) tableSorted.addRow(getDatosRow(obj));
	        else tableSorted.insertRow(index, getDatosRow(obj));
    	}catch(Exception ex){}
    }

    public void annadirRow(BienPreAltaBean obj){
        if (tableSorted == null) return;
            rows.put((((BienPreAltaBean)obj).getId()), obj);

        tableSorted.addRow(getDatosRow(obj));
    }

    
    public void deleteRow(int index, String key){
        if (tableSorted == null) return;
        rows.remove(key);
        tableSorted.removeRow(index);
    }
    
    public void deleteRow(int index, BienPreAltaBean bien){
        if (tableSorted == null) return;
        rows.remove(bien.getId());
        tableSorted.removeRow(index);
    }

    public BienPreAltaBean getObjetAt (int index) {
        if (index == -1) return null;
        if (tableSorted == null) return null;
        BienPreAltaBean bienPA =null;
        try{
        	long val = (Long) tableSorted.getValueAt(index, tableSorted.getColumnCount()-1);
        	bienPA = (BienPreAltaBean) rows.get(val);
        }catch(Exception e){
        	logger.error("No se ha encontrado el bien asociado a la row: "+index);
        	e.printStackTrace();
        }
    	return bienPA;
    }
    
    public void clearModel(){
        if (tableSorted == null) return;
        for (int i=0; i<tableSorted.getRowCount();){
            tableSorted.removeRow(i);
        }
    }

    public void setTable(javax.swing.JTable t){
         this.tabla= t;
    }

    public void setRows(Hashtable rows){
    	this.rows = rows;
    }
    
    public Hashtable getRows(){
    	return rows;
    }
    public Vector getSeleccionados(){
    	Vector seleccionados = new Vector();
    	try{
    		if (!showSeleccion) return null;
    		for (int i=0;i<tableSorted.getRowCount();i++){
    			if ((Boolean)tableSorted.getValueAt(i, 0)){
    				seleccionados.add(rows.get(tableSorted.getValueAt(i, tableSorted.getColumnCount()-1)));
    			}
    		}
    	}catch (Exception e) {
    		logger.error("Error al obtener la lista de seleccionados",e);
     	}
    	return seleccionados;
    }
}
