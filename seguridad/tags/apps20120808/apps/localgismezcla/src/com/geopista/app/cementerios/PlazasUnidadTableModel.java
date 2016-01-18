package com.geopista.app.cementerios;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.inventario.BienBean;

/**
 * Clase que implementa el modelo de datos de la tabla del dialogo unidadEnterramiento
 */

@SuppressWarnings("serial")
public class PlazasUnidadTableModel  extends DefaultTableModel {
private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(PlazasUnidadTableModel.class);
    public String[] columnNames;
    public boolean[] columnEditables;
    private String locale;
    private Hashtable rows= new Hashtable();
    private TableSorted tableSorted;
    private javax.swing.JTable tabla;
    
    private SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");

    public PlazasUnidadTableModel(String[] colNames, boolean[] colEditables, String locale) {
        columnNames= colNames;
        columnEditables= colEditables;
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


    private void setRows(List lst){
        rows= new Hashtable();
        if ((lst == null) || (lst.size()==0)) return;
        for (int i=0; i<lst.size(); i++){
            if (lst.get(i) instanceof PlazaBean){
                rows.put(""+((PlazaBean)lst.get(i)).getIdPlaza(), lst.get(i));
            }
        }
    }
    
    private Object[] getDatosRow(Object obj){
        if (obj instanceof PlazaBean){
        	PlazaBean plaza= (PlazaBean)obj;
        
        	Object row[]= new Object[] {plaza.getDescripcion(),
        								plaza.getSituacion(),
        								plaza.isEstado(),
        								fecha.format(plaza.getModificado()),
        								(plaza.getDifunto()!= null ? plaza.getDifunto().getPersona().getDNI() : "Usuario"),
        								plaza.getIdPlaza(),locale}; // /*columna hidden*/
        return row;
        }
        else{
           	ElemCementerioBean elemCementerio= (ElemCementerioBean)obj;
            Object row[]= new Object[] {elemCementerio.getId(),
            								  elemCementerio.getId(),
            								  new Date(),
            								  new Date(),
            								  elemCementerio.getNombreCementerio()!=null?elemCementerio.getNombreCementerio():"", 
            								  elemCementerio.getTipo(),
            								  elemCementerio.getEntidad(), 
            								  elemCementerio.getId()}; /*columna hidden*/
                                           // bien.getUso()!=null?Estructuras.getListaUsoJuridico().getDomainNode(bien.getUso()).getTerm(locale):"",
                                            /*columna hidden*/ /*bien.getRevisionExpirada()+"",""+bien.getId()+"_"+bien.getRevisionActual()};*/
            return row;
        }
    }

    private Object[] getDatosRowVersion(Object obj){
        if (!(obj instanceof BienBean)) return null;
        BienBean bien= (BienBean)obj;
        DateFormat df= new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        String sFechaVersion = df.format(bien.getFechaVersion());

        Object row[]= new Object[] {bien.getNumInventario(),
        							sFechaVersion,
        							bien.getAutor(),
        							bien.getRevisionActual(),bien.getRevisionExpirada(),""+bien.getId()+"_"+bien.getRevisionActual()};
        return row;
    }

    public void actualizarRow(int index, Object obj){
        if (tableSorted == null) return;
        String hidden= (String)tableSorted.getValueAt(index, tableSorted.getColumnCount()-1);
        if (rows.containsKey(hidden))
            rows.remove(hidden);
        rows.put(hidden, obj);
        tableSorted.removeRow(index);
        if (index >= tableSorted.getRowCount()) tableSorted.addRow(getDatosRow(obj));
        else tableSorted.insertRow(index, getDatosRow(obj));
    }

    public void annadirRow(Object obj){
        if (tableSorted == null) return;
        if (obj instanceof PlazaBean){
            rows.put(""+(((PlazaBean)obj).getIdPlaza()), obj);
        	
        }
        tableSorted.addRow(getDatosRow(obj));
    }

    
    public void deleteRow(int index, Object obj){
        if (tableSorted == null) return;
        rows.remove(obj);
        tableSorted.removeRow(index);
    }

    public Object getObjetAt (int index) {
        if (index == -1) return null;
        if (tableSorted == null) return null;
        return rows.get(""+ tableSorted.getValueAt(index, tableSorted.getColumnCount()-1));
    }
    
    public void clearModel(){
        if (tableSorted == null) return;
        for (int i=0; i<tableSorted.getRowCount();){
            tableSorted.removeRow(i);
        }
    }

    
    public void setModelData(Collection listaBienes) {
        try{
            if (tableSorted == null) return;
            rows= new Hashtable();
            this.clearModel();
            if (listaBienes == null) return;
            this.setRows((List)listaBienes);
            for (Iterator iterator= listaBienes.iterator(); iterator.hasNext();) {
                Object obj= iterator.next();
                tableSorted.addRow(getDatosRow(obj));
		    }
	    	fireTableDataChanged();
        }catch(Exception e){
            logger.error("Error al poner la lista de bienes: "+ e.toString());
            e.printStackTrace();
        }
	}
    
    
    public void setModelDataVersion(Collection listaBienes) {
        try{
            if (tableSorted == null) return;
            rows= new Hashtable();
            this.clearModel();
            if (listaBienes == null) return;
            this.setRows((List)listaBienes);
            for (Iterator iterator= listaBienes.iterator(); iterator.hasNext();) {
                Object obj= iterator.next();
                tableSorted.addRow(getDatosRowVersion(obj));
		    }
	    	fireTableDataChanged();
        }catch(Exception e){
            logger.error("Error al poner la lista de bienes: "+ e.toString());
            e.printStackTrace();
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
    
}
