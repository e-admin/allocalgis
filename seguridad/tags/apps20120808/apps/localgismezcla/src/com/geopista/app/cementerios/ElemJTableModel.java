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
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.cementerios.ExhumacionBean;
import com.geopista.protocol.cementerios.HistoricoDifuntoBean;
import com.geopista.protocol.cementerios.HistoricoPropiedadBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.inventario.BienBean;


@SuppressWarnings("serial")
public class ElemJTableModel  extends DefaultTableModel {
	
private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(ElemJTableModel.class);
    public String[] columnNames;
    public boolean[] columnEditables;
    private String locale;
    private Hashtable rows= new Hashtable();
    private TableSorted tableSorted;
    private javax.swing.JTable tabla;
    
    private SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");

    public ElemJTableModel(String[] colNames, boolean[] colEditables, String locale) {
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
            if (lst.get(i) instanceof TarifaBean){
                rows.put(""+((TarifaBean)lst.get(i)).getId(), lst.get(i));
            }
            if (lst.get(i) instanceof DifuntoBean){
                rows.put(""+((DifuntoBean)lst.get(i)).getPersona().getDNI(), lst.get(i));
            }
            if (lst.get(i) instanceof HistoricoDifuntoBean){
                rows.put(""+((HistoricoDifuntoBean)lst.get(i)).getId_historico(), lst.get(i));
            }
            if (lst.get(i) instanceof HistoricoPropiedadBean){
                rows.put(""+((HistoricoPropiedadBean)lst.get(i)).getId_historico(), lst.get(i));
            }
        }
    }
    
    private Object[] getDatosRow(Object obj){
    	
    	String tipoStr = null;
    	DomainNode node = null;
    	DomainNode node2 = null;
        //cargarmos las estructuras..
        while (!Estructuras.isCargada()){
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }
        if (obj instanceof TarifaBean){
        	TarifaBean tarifa= (TarifaBean)obj;

            Vector vDomainTipoUnidades = Estructuras.getListaCombosSorted(locale);
            for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
      		node = (DomainNode) vDomainTipoUnidades.get(i);
          		if (node.getPatron().equalsIgnoreCase(String.valueOf(tarifa.getTipo_unidad()))){
          			break;
          		}
            }
            Vector vDomaintipoCalculo;
        	if (tarifa.getTipo_tarifa() == Constantes.TARIFA_GPROPIEDAD){
        		vDomaintipoCalculo = Estructuras.getListaComboConcesiones(locale);
        	}else{
//        		vDomaintipoCalculo = Estructuras.getListaComboServicios(locale);
        		vDomaintipoCalculo = Estructuras.getListaComboGestionDifuntos(locale);
        	}
            for (int i = 0; i < vDomaintipoCalculo.size(); i++) {
      		node2 = (DomainNode) vDomaintipoCalculo.get(i);
	      		if (node2.getPatron().equalsIgnoreCase(String.valueOf(tarifa.getTipo_calculo()))){
	      			break;
	      		}
            }
        	Object row[]= new Object[] {
	        			node,
	        			node2,
	        			tarifa.getConcepto(),
	        			tarifa.getPrecio(),
	        			tarifa.getTipo_tarifa() == Constantes.TARIFA_GPROPIEDAD ? "Tarifa Gestión Propiedad" : "Tarifa Servicio sobre difuntos",
	        			tarifa.getId(),locale}; // /*columna hidden*/
        return row;
        }
        else if (obj instanceof DifuntoBean){
        	DifuntoBean elemCementerio= (DifuntoBean)obj;
            Object row[]= new Object[] { 
            					elemCementerio.getPersona().getDNI(),
            					elemCementerio.getPersona().getNombre() + "-" + elemCementerio.getPersona().getApellido1() +
            					"-" + elemCementerio.getPersona().getApellido2(),
            		 			Const.df.format(elemCementerio.getFecha_defuncion()),
            					elemCementerio.getGrupo(),
            					elemCementerio.getDatosFallecimiento().getCausa_fundamental(),
            					elemCementerio.getPersona().getDNI(),elemCementerio.getPersona().getDNI(), elemCementerio.getPersona().getDNI(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof HistoricoDifuntoBean){
        	HistoricoDifuntoBean elemCementerio= (HistoricoDifuntoBean)obj;
        	
        	Object elemObject = elemCementerio.getElem();
        	/**Inhumacion**/
        	if (elemObject instanceof InhumacionBean){
            	InhumacionBean elem= (InhumacionBean)elemObject;
                Vector vDomain = Estructuras.getListaComboContenedores(locale);
                for (int i = 0; i < vDomain.size(); i++) {
          		node = (DomainNode) vDomain.get(i);
    	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elem.getTipo_contenedor()))){
    	      			break;
    	      		}
                }
                Vector vDomainTipo = Estructuras.getListaCombosTipoInhumacionesSorted(locale);
                for (int i = 0; i < vDomainTipo.size(); i++) {
          		node2 = (DomainNode) vDomainTipo.get(i);
    	      		if (node2.getPatron().equalsIgnoreCase(String.valueOf(elem.getTipo_inhumacion()))){
    	      			break;
    	      		}
                }
        	}
        	/**Exhumacion**/
        	else if (elemObject instanceof ExhumacionBean){
            	ExhumacionBean elem= (ExhumacionBean)elemObject;
                Vector vDomain = Estructuras.getListaComboContenedores(locale);
                for (int i = 0; i < vDomain.size(); i++) {
          		node = (DomainNode) vDomain.get(i);
    	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elem.getTipo_contenedor()))){
    	      			break;
    	      		}
                }
                Vector vDomainTipo = Estructuras.getListaCombosTipoExhumacionesSorted(locale);
                for (int i = 0; i < vDomainTipo.size(); i++) {
          		node2 = (DomainNode) vDomainTipo.get(i);
    	      		if (node2.getPatron().equalsIgnoreCase(String.valueOf(elem.getTipo_exhumacion()))){
    	      			break;
    	      		}
                }
        	}
        	/**Completamos la fila**/
            Object row[]= new Object[] { 
            					elemCementerio.getTipoStr(),
            					node2,
            					node,
            					Const.df.format(elemCementerio.getFechaOperacion()),
            					elemCementerio.getComentario(),
            					elemCementerio.getId_historico(),elemCementerio.getId_historico(), elemCementerio.getId_historico(), locale}; /*columna hidden*/
            return row;
        }
        else if (obj instanceof HistoricoPropiedadBean){
        	HistoricoPropiedadBean elemCementerio= (HistoricoPropiedadBean)obj;
        	
        	Object elemObject = elemCementerio.getElem();
        	ConcesionBean elem = null;
        	/**Concesion**/
        	if (elemObject instanceof ConcesionBean){
        		elem= (ConcesionBean)elemObject;
                Vector vDomain =  Estructuras.getListaComboConcesiones(locale);
                for (int i = 0; i < vDomain.size(); i++) {
          		node = (DomainNode) vDomain.get(i);
    	      		if (node.getPatron().equalsIgnoreCase(String.valueOf(elem.getTipo_concesion()))){
    	      			break;
    	      		}
                }
                
                Vector vDomainTipoUnidad = Estructuras.getListaCombosSorted(locale);
                for (int i = 0; i < vDomainTipoUnidad.size(); i++) {
          		node2 = (DomainNode) vDomainTipoUnidad.get(i);
    	      		if (node2.getPatron().equalsIgnoreCase(String.valueOf(elem.getUnidad().getTipo_unidad()))){
    	      			break;
    	      		}
                }
        	}
        	/**Completamos la fila**/
            Object row[]= new Object[] { 
            					node2,
            					node,
            					Const.df.format(elemCementerio.getFechaOperacion()),
            					Const.df.format(elem.getFecha_fin()),
            					elemCementerio.getComentario(),
            					elemCementerio.getId_historico(),elemCementerio.getId_historico(), elemCementerio.getId_historico(), locale}; /*columna hidden*/
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
