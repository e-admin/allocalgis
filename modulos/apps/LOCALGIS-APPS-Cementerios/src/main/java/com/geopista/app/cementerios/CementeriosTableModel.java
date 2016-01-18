/**
 * CementeriosTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.cementerios.ExhumacionBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.IntervencionBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.inventario.BienBean;

/**
 * Clase que implementa el modelo de datos de la tabla del panel cementeriosJPanel
 */

public class CementeriosTableModel  extends DefaultTableModel {
private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(CementeriosTableModel.class);
    public String[] columnNames;
    public boolean[] columnEditables;
    private String locale;
    private Hashtable rows= new Hashtable();
    private TableSorted tableSorted;
    private javax.swing.JTable tabla;

    public CementeriosTableModel(String[] colNames, boolean[] colEditables, String locale) {
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


    @SuppressWarnings("unchecked")
	private void setRows(List lst){
        rows= new Hashtable();
        if ((lst == null) || (lst.size()==0)) return;
        for (int i=0; i<lst.size(); i++){
            if (lst.get(i) instanceof UnidadEnterramientoBean){
                rows.put(""+((UnidadEnterramientoBean)lst.get(i)).getIdUe(), lst.get(i));
            }
            else if (lst.get(i) instanceof ConcesionBean){
                rows.put(""+((ConcesionBean)lst.get(i)).getId_concesion(), lst.get(i));
            }
            else if (lst.get(i) instanceof BloqueBean){
                rows.put(""+((BloqueBean)lst.get(i)).getId_bloque(), lst.get(i));
            }
            else if (lst.get(i) instanceof PersonaBean){
                rows.put(""+((PersonaBean)lst.get(i)).getDNI(), lst.get(i));
            }
            else if (lst.get(i) instanceof TarifaBean){
                rows.put(""+((TarifaBean)lst.get(i)).getId_tarifa(), lst.get(i));
            }
            else if (lst.get(i) instanceof DifuntoBean){
                rows.put(""+((DifuntoBean)lst.get(i)).getPersona().getDNI(), lst.get(i));
            }
            else if (lst.get(i) instanceof InhumacionBean){
                rows.put(""+((InhumacionBean)lst.get(i)).getId_inhumacion(), lst.get(i));
            }
            else if (lst.get(i) instanceof ExhumacionBean){
                rows.put(""+((ExhumacionBean)lst.get(i)).getId_exhumacion(), lst.get(i));
            }
            else if (lst.get(i) instanceof IntervencionBean){
                rows.put(""+((IntervencionBean)lst.get(i)).getId_intervencion(), lst.get(i));
            }
            else if (lst.get(i) instanceof ElemCementerioBean){
                rows.put(""+((ElemCementerioBean)lst.get(i)).getId(), lst.get(i));
            }
            
        }
    }
    
    //TODO imp!!
    /**
     * GetDatosRow
     * @param obj
     * @return
     */
    private Object[] getDatosRow(Object obj){
     //   if (!(obj instanceof ElemCementerioBean)) return null;
        
    	String tipoStr = null;
    	DomainNode node = null;
    	DomainNode node2 = null;
        //cargarmos las estructuras..
        while (!Estructuras.isCargada()){
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }
        
        if (obj instanceof BloqueBean){
        	BloqueBean elemCementerio= (BloqueBean)obj;
        
            Vector vDomainTipoUnidades = Estructuras.getListaCombosSorted(locale);
            for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
      		node = (DomainNode) vDomainTipoUnidades.get(i);
	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_unidades()))){
	      			break;
	      		}
            }
	
            Object row[]= new Object[] {  node,
        								  elemCementerio.getNumFilas(),
        								  elemCementerio.getNumColumnas(),
        								  elemCementerio.getDescripcion(),
        								  elemCementerio.getNombreCementerio()!=null?elemCementerio.getNombreCementerio():"", 
        								  elemCementerio.getTipo_unidades(),
        								  "Bloque", 
        								  elemCementerio.getId_bloque(),elemCementerio.getId_bloque(), elemCementerio.getId_bloque(), locale}; /*columna hidden*/
            return row;
        }
        
        
        if (obj instanceof UnidadEnterramientoBean){
        	UnidadEnterramientoBean elemCementerio= (UnidadEnterramientoBean)obj;
        
            Vector vDomainTipoUnidades = Estructuras.getListaCombosSorted(locale);
            for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
      		node = (DomainNode) vDomainTipoUnidades.get(i);
	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_unidad()))){
	      			break;
	      		}
            }
	
            Object row[]= new Object[] {  node,
        								  elemCementerio.getCodigo(),
        								  Const.df.format(elemCementerio.getFecha_construccion()),
        								  Const.df.format(elemCementerio.getFecha_UltimaRef()),
        								  elemCementerio.getNombreCementerio()!=null?elemCementerio.getNombreCementerio():"", 
        								  elemCementerio.getTipo_unidad(),
        								  elemCementerio.getDescripcion(), 
        								  elemCementerio.getIdUe(),elemCementerio.getIdUe(), elemCementerio.getIdUe(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof ConcesionBean){
        	ConcesionBean elemCementerio= (ConcesionBean)obj;
            Vector vDomainTipoUnidades = Estructuras.getListaComboConcesiones(locale);
            for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
      		node = (DomainNode) vDomainTipoUnidades.get(i);
	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_concesion()))){
	      			break;
	      		}
            }
            Object row[]= new Object[] { node, 	  
            		 			Const.df.format(elemCementerio.getFecha_ini()),
            		 			 Const.df.format(elemCementerio.getFecha_fin()),
            					elemCementerio.getDescripcion(),
            					elemCementerio.getTitular().getDNI(),
            					elemCementerio.getTarifa().getConcepto(),
            					//"concepto",
            					elemCementerio.getEstado()!= Const.Estado_Activa ? "Activa":"Caducada", 
            					elemCementerio.getId_concesion(),elemCementerio.getId_concesion(), elemCementerio.getId_concesion(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof PersonaBean){
        	PersonaBean elemCementerio= (PersonaBean)obj;
            Object row[]= new Object[] { elemCementerio.getDNI(),
            					elemCementerio.getNombre(),
            					elemCementerio.getApellido1(),
            					elemCementerio.getApellido2(),
            					elemCementerio.getFecha_nacimiento(),
            					elemCementerio.getDomicilio(),
            					elemCementerio.getPoblacion(),
            					elemCementerio.getDNI(),elemCementerio.getDNI(), elemCementerio.getDNI(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof DifuntoBean){
        	DifuntoBean elemCementerio= (DifuntoBean)obj;
            Object row[]= new Object[] { elemCementerio.getPersona().getDNI(),
            		 			Const.df.format(elemCementerio.getFecha_defuncion()),
            					elemCementerio.getGrupo(),
            					elemCementerio.getDatosFallecimiento().getCausa_fundamental(),
            					elemCementerio.getDatosFallecimiento().getLugar(),
            					elemCementerio.getDatosFallecimiento().getMedico(),
            					elemCementerio.getPersona().getNombre() + "-" + elemCementerio.getPersona().getApellido1() +
            					"-" + elemCementerio.getPersona().getApellido2(),
            					elemCementerio.getPersona().getDNI(),elemCementerio.getPersona().getDNI(), elemCementerio.getPersona().getDNI(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof TarifaBean){
        	TarifaBean elemCementerio= (TarifaBean)obj;
        	
            Vector vDomainTipoUnidades = Estructuras.getListaCombosSorted(locale);
            for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
      		node = (DomainNode) vDomainTipoUnidades.get(i);
	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_unidad()))){
	      			break;
	      		}
            }
            Vector vDomainTipoConcesion;
            if (elemCementerio.getTipo_tarifa().equals(Const.TARIFA_GPROPIEDAD)){
            	vDomainTipoConcesion = Estructuras.getListaComboConcesiones(locale);
            }else{
            	vDomainTipoConcesion = Estructuras.getListaComboGestionDifuntos(locale);
            }
            for (int i = 0; i < vDomainTipoConcesion.size(); i++) {
      		node2 = (DomainNode) vDomainTipoConcesion.get(i);
	      		if (node2.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_calculo()))){
	      			break;
	      		}
            }
            Object row[]= new Object[] {
            					"Activa",
            		 			elemCementerio.getConcepto(),
            					node2,
            					node,
            					elemCementerio.getPrecio(),
            					elemCementerio.getTipo(),
            					elemCementerio.getTipo_tarifa().equals(Const.TARIFA_GPROPIEDAD) ? "Tarifa Gestión Propiedades" : "Tarifa Gestión servicios",
            					elemCementerio.getId_tarifa(),elemCementerio.getId_tarifa(), elemCementerio.getId_tarifa(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof InhumacionBean){
        	InhumacionBean elemCementerio= (InhumacionBean)obj;
            Vector vDomain = Estructuras.getListaComboContenedores(locale);
            for (int i = 0; i < vDomain.size(); i++) {
      		node = (DomainNode) vDomain.get(i);
	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_contenedor()))){
	      			break;
	      		}
            }
            Vector vDomainTipo = Estructuras.getListaCombosTipoInhumacionesSorted(locale);
            for (int i = 0; i < vDomainTipo.size(); i++) {
      		node2 = (DomainNode) vDomainTipo.get(i);
	      		if (node2.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_inhumacion()))){
	      			break;
	      		}
            }
            Object row[]= new Object[] { elemCementerio.getDifunto().getPersona().getDNI(),
            		 			Const.df.format(elemCementerio.getFecha_inhumacion()),
            		 			node2,
            					node,
            					elemCementerio.getCodigo(),
            					elemCementerio.getDifunto().getDatosFallecimiento().getRegistro_civil(),
            					elemCementerio.getPrecio_final(),
            					elemCementerio.getId_inhumacion(),elemCementerio.getId_inhumacion(), elemCementerio.getId_inhumacion(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof ExhumacionBean){
        	ExhumacionBean elemCementerio= (ExhumacionBean)obj;
            Vector vDomain = Estructuras.getListaComboContenedores(locale);
            for (int i = 0; i < vDomain.size(); i++) {
      		node = (DomainNode) vDomain.get(i);
	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_contenedor()))){
	      			break;
	      		}
            }
            Vector vDomainTipo = Estructuras.getListaCombosTipoExhumacionesSorted(locale);
            for (int i = 0; i < vDomainTipo.size(); i++) {
      		node2 = (DomainNode) vDomainTipo.get(i);
	      		if (node2.getPatron().equalsIgnoreCase(String.valueOf(elemCementerio.getTipo_exhumacion()))){
	      			break;
	      		}
            }
            Object row[]= new Object[] { elemCementerio.getDifunto().getPersona().getDNI(),
            		 			Const.df.format(elemCementerio.getFecha_exhumacion()),
            		 			node2,
            					node,
            					elemCementerio.getCodigo(),
            					elemCementerio.getDifunto().getDatosFallecimiento().getRegistro_civil(),
            					elemCementerio.getPrecio_final(),
            					elemCementerio.getId_exhumacion(),elemCementerio.getId_exhumacion(), elemCementerio.getId_exhumacion(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof IntervencionBean){
        	IntervencionBean elemCementerio= (IntervencionBean)obj;
            Object row[]= new Object[] {
            					elemCementerio.getCodigo(),
            					Const.df.format(elemCementerio.getFechaInicio()),
            					Const.df.format(elemCementerio.getFechaFin()),
            					elemCementerio.getEstado(),
            					elemCementerio.getLocalizacion(),
            					elemCementerio.getInforme(),
            					"Intervención",
            					elemCementerio.getId_intervencion(),elemCementerio.getId_intervencion(), elemCementerio.getId_intervencion(), locale}; /*columna hidden*/
            return row;
        }
        else{
           	ElemCementerioBean elemCementerio= (ElemCementerioBean)obj;
            
            Object row[]= new Object[] {	elemCementerio.getEntidad(),
            								  elemCementerio.getId(),
            								  new Date(),
            								  new Date(),
            								  elemCementerio.getNombreCementerio()!=null?elemCementerio.getNombreCementerio():"", 
            								  elemCementerio.getTipo(),
            								  elemCementerio.getEntidad(), 
            								  elemCementerio.getId()}; /*columna hidden*/
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
        if (obj instanceof ElemCementerioBean){
            rows.put(""+(((ElemCementerioBean)obj).getId()), obj);
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
