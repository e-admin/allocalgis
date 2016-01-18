/**
 * InventarioTableModel.java
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.inventario.component.FechaInventario;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.NumInventario;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class InventarioTableModel  extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
    private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(InventarioTableModel.class);
    public String[] columnNames;
    public boolean[] columnEditables;
    private String locale;
    private Hashtable rows= new Hashtable();
    private TableSorted tableSorted;
    private javax.swing.JTable tabla;
    private boolean showSeleccion = false;

    public InventarioTableModel(String[] colNames, boolean[] colEditables, String locale, boolean showSeleccion) {
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
            if (lst.get(i) instanceof BienBean){
                rows.put(""+((BienBean)lst.get(i)).getId()+"_"+((BienBean)lst.get(i)).getRevisionActual(), lst.get(i));
            }else if (lst.get(i) instanceof BienRevertible){
            	rows.put(""+((BienRevertible)lst.get(i)).getId()+"_"+((BienRevertible)lst.get(i)).getRevisionActual(), lst.get(i));
            }else if (lst.get(i) instanceof Lote){
            	rows.put(""+((Lote)lst.get(i)).getId_lote(), lst.get(i));
            }
        }
    }

    private Object[] getDatosRow(Object obj){
        if (obj instanceof BienBean)  return getDatosRowBien((BienBean)obj);
        if (obj instanceof BienRevertible) return getDatosRowBienRevertible((BienRevertible)obj);
        if (obj instanceof Lote) return getDatosRowLote((Lote)obj);
        return null;
        
    }
   
    private Object[] getDatosRowBien(BienBean bien){
    	 com.geopista.app.licencias.utilidades.CheckBoxRenderer  renderCheck=
        	  (com.geopista.app.licencias.utilidades.CheckBoxRenderer) tabla.getCellRenderer(this.getRowCount()==0?0:this.getRowCount()-1, 0);
          
          if (!showSeleccion) renderCheck.setSelected(bien.getBorrado());
          
          String uso="";
          if (bien.getUso()!=null){
          	if (Estructuras.getListaUsoJuridico().getDomainNode(bien.getUso())!=null)
          		uso=Estructuras.getListaUsoJuridico().getDomainNode(bien.getUso()).getTerm(locale);
          	else
          		logger.warn("No se ha encontrado el uso para "+bien.getUso());
          }
          
          Object row[]= new Object[] {new Boolean(renderCheck.isSelected()),
              	      new NumInventario(bien.getNumInventario()),
              	      new FechaInventario(bien.getFechaAlta()),
              	      new FechaInventario(bien.getFechaUltimaModificacion()),
                      bien.getNombre()!=null?bien.getNombre():"",
                      uso,		
                      //bien.getDescripcion()!=null?bien.getDescripcion():"",
                      /*columna hidden*/bien.getRevisionExpirada()+"",""+bien.getId()+"_"+bien.getRevisionActual()};
              return row;
         
    }
    /**
     * Devuelve una fila con los datos del objeto que es de tipo
     * bien Revertible
     * @param obj
     * @return
     */
    private Object[] getDatosRowBienRevertible(BienRevertible bien){
  	    com.geopista.app.licencias.utilidades.CheckBoxRenderer renderCheck= (com.geopista.app.licencias.utilidades.CheckBoxRenderer) tabla.getCellRenderer(this.getRowCount()==0?0:this.getRowCount()-1, 0);
        renderCheck.setSelected(bien.isBorrado());
        
        Object row[]= new Object[] {new Boolean(renderCheck.isSelected()),
                                        new NumInventario(bien.getNumInventario()),
                                        new FechaInventario(bien.getFechaAlta()),
                                        new FechaInventario(bien.getFechaUltimaModificacion()),
                                        bien.getPoseedor()!=null?bien.getPoseedor():"",
                                        bien.getTituloPosesion()!=null?bien.getTituloPosesion():"",	
                                        bien.getRevisionExpirada(),
                                /*columna hidden*/bien.getId()+"_"+bien.getRevisionActual()};
        return row;
  }
    
    /**
     * Devuelve una fila con los datos del objeto que es de tipo
     * Lote
     * @param obj
     * @return
     */
    private Object[] getDatosRowLote(Lote lote){
  	    com.geopista.app.licencias.utilidades.CheckBoxRenderer renderCheck= (com.geopista.app.licencias.utilidades.CheckBoxRenderer) tabla.getCellRenderer(this.getRowCount()==0?0:this.getRowCount()-1, 0);
        renderCheck.setSelected(lote.getFecha_baja()!=null);
      
        Object row[]= new Object[] {new Boolean(renderCheck.isSelected()),
        								new FechaInventario(lote.getFecha_alta()), 
        								new FechaInventario(lote.getFecha_ultima_modificacion()),
        		                        lote.getNombre_lote()!=null?lote.getNombre_lote():"",
                                        lote.getSeguro()!=null?lote.getSeguro().getCompannia().getNombre():"",
                                        /*columna hidden*/lote.getId_lote()};
        return row;
  }


    private Object[] getDatosRowVersion(Object obj){
    	
        if (!(obj instanceof BienBean)) return null;
        BienBean bien= (BienBean)obj;
        DateFormat df= new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        Object row[]= new Object[] {new NumInventario(bien.getNumInventario()),
        							new FechaInventario(bien.getFechaVersion(), df),
        							bien.getAutor(),
        							bien.getRevisionActual(),bien.getRevisionExpirada(),""+bien.getId()+"_"+bien.getRevisionActual()};
        return row;
    }

     private Object[] getDatosRowVersionBR(Object obj){
    	
        if (!(obj instanceof BienRevertible)) return null;
        BienRevertible bienRevertible= (BienRevertible)obj;
        DateFormat df= new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        Object row[]= new Object[] {new NumInventario(bienRevertible.getNumInventario()),
        							new FechaInventario(bienRevertible.getFechaVersion(),df),
        							bienRevertible.getAutor(),
        							bienRevertible.getRevisionActual(),bienRevertible.getRevisionExpirada(),""+bienRevertible.getId()+"_"+bienRevertible.getRevisionActual()};
        return row;
    }
    public void actualizarRow(int index, Object obj){
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

    public void annadirRow(Object obj){
        if (tableSorted == null) return;
        if (obj instanceof BienBean){
            rows.put(""+(((BienBean)obj).getId())+"_"+((BienBean)obj).getRevisionActual(), obj);
        }
        if (obj instanceof BienRevertible){
            rows.put(""+(((BienRevertible)obj).getId())+"_"+((BienRevertible)obj).getRevisionActual(), obj);
        }

        tableSorted.addRow(getDatosRow(obj));
    }

    
    public void deleteRow(int index, String key){
        if (tableSorted == null) return;
        rows.remove(key);
        tableSorted.removeRow(index);
    }
    
    public void deleteRow(int index, BienBean bien){
        if (tableSorted == null) return;
        rows.remove(""+bien.getId()+"_"+bien.getRevisionActual());
        tableSorted.removeRow(index);
    }

    public Object getObjetAt (int index) {
        if (index == -1) return null;
        if (tableSorted == null) return null;
        return rows.get(""+tableSorted.getValueAt(index, tableSorted.getColumnCount()-1));
    }
    
    public void clearModel(){
        if (tableSorted == null) return;
        for (int i=0; i<tableSorted.getRowCount();){
            tableSorted.removeRow(i);
        }
    }

    
    public void setModelData(Collection lista) {
        try{
            if (tableSorted == null) return;
            rows= new Hashtable();
            this.clearModel();
            if (lista == null) return;
            
            Object row[]= new Object[] {new Boolean(false),
            	      new NumInventario(""),
            	      new FechaInventario(new Date()),
            	      new FechaInventario(new Date()),
                    "",
                    "",		
                    0+"",""+0+"_"+0};      
            tableSorted.addRow(row);
            fireTableDataChanged();
            this.clearModel();
            fireTableDataChanged();
            
            this.setRows((List)lista);
            for (Iterator iterator= lista.iterator(); iterator.hasNext();) {
                Object obj= iterator.next();
                Object[] datosRow=getDatosRow(obj);
	    		//logger.info("INVEN:"+datosRow[1]);
                tableSorted.addRow(datosRow);
                //tableSorted.addRow(getDatosRow(obj));
		    }
	    	fireTableDataChanged();
        }catch(Exception e){
            logger.error("Error al poner la lista en el table model: ", e);
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
    
    public void setModelDataVersionBR(Collection listaBienes) {
        try{
            if (tableSorted == null) return;
            rows= new Hashtable();
            this.clearModel();
            if (listaBienes == null) return;
            this.setRows((List)listaBienes);
            for (Iterator iterator= listaBienes.iterator(); iterator.hasNext();) {
                Object obj= iterator.next();
                tableSorted.addRow(getDatosRowVersionBR(obj));
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
