/*
 * BienesJPanel.java
 *
 * Created on 7 de julio de 2006, 9:39
 */

package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.CementeriosTableModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.cementerios.ExhumacionBean;
import com.geopista.protocol.cementerios.HistoricoDifuntoBean;
import com.geopista.protocol.cementerios.HistoricoPropiedadBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.IntervencionBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;

public class ElemCementeriosJPanel extends javax.swing.JPanel {
    Logger logger= Logger.getLogger(ElemCementeriosJPanel.class);

    private AppContext aplicacion;
    private String locale;
    
    private CementeriosTableModel cementeriosJTableModel;
    JTabbedPane panelPestañas;

    
    private ArrayList actionListeners= new ArrayList();   
    private int selectedRow= -1;
    private TableSorted tableSorted;
    
    private javax.swing.JScrollPane elemCementeriosJScrollPane;
    private javax.swing.JTable elemCementeriosJTable;

    private JPanel contadorElemCementeriosJPanel;
	private JLabel contadorElemCementeriosJLabel;
	
	public static final String DOBLE_CLICK="DOBLE_CLICK";
	
	private String tipoSeleccionado;
	private String subtipoSeleccionado;
	
	
	/**
     * Método que genera el panel del listado de elementos asociadas a un tipo de elemento
     * @param locale
     */
    public ElemCementeriosJPanel( String locale) {
        this.locale= locale;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    /**
     * Método llamado por el constructor para inicializar el formulario
     */
    private void initComponents() {
        elemCementeriosJScrollPane = new javax.swing.JScrollPane();
        elemCementeriosJTable = new BienTableRenderer(7); 
        contadorElemCementeriosJPanel = new JPanel();
        contadorElemCementeriosJPanel.setLayout(new GridBagLayout());
        contadorElemCementeriosJLabel = new JLabel();
        contadorElemCementeriosJLabel.setText("");
        contadorElemCementeriosJPanel.add(contadorElemCementeriosJLabel, 
        		new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
        				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 10, 0),0,0));
        

        setLayout(new java.awt.BorderLayout());
        initHeadersJTable();

        /* Ordenacion de la tabla */
        tableSorted= new TableSorted(cementeriosJTableModel);
        cementeriosJTableModel.setTableSorted(tableSorted);
        tableSorted.setTableHeader(elemCementeriosJTable.getTableHeader());
        elemCementeriosJTable.setModel(tableSorted);
        elemCementeriosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        elemCementeriosJTable.setCellSelectionEnabled(false);
        elemCementeriosJTable.setColumnSelectionAllowed(false);
        elemCementeriosJTable.setRowSelectionAllowed(true);
        elemCementeriosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS/*JTable.AUTO_RESIZE_OFF*/);

        elemCementeriosJTable.getTableHeader().setReorderingAllowed(false);
        setInvisible(cementeriosJTableModel.getColumnCount()-1,elemCementeriosJTable);
        setInvisible(cementeriosJTableModel.getColumnCount()-2,elemCementeriosJTable);

        cementeriosJTableModel.setTable(elemCementeriosJTable);

        /** Re-Ordenacion de la tabla - repintado del bloqueo */
        ((TableSorted)elemCementeriosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                reColorearBloqueo();
            }
        });
        ((TableSorted)elemCementeriosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reColorearBloqueo();
            }
        });

        elemCementeriosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	ElemCementerioJTableMouseReleased();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt){
            	if(evt.getClickCount() == 2) {
            		getElemSeleccionado();
            		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                        ActionListener l = (ActionListener) i.next();
                        l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                    }
                }
            }
        });

        elemCementeriosJScrollPane.setViewportView(elemCementeriosJTable);
        
        panelPestañas = new JTabbedPane();
        panelPestañas.addTab(aplicacion.getI18nString("cementerio.tab.generico"),elemCementeriosJScrollPane);
        
        add(panelPestañas, BorderLayout.CENTER);
        add(contadorElemCementeriosJPanel, BorderLayout.SOUTH);
    }

    /**
     * Método que hace un columna de la tabla no visible
     */
    private void setInvisible(int column, JTable jTable){
        /** columna hidden no visible */
        TableColumn col= jTable.getColumnModel().getColumn(column);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);
    }

   private void initHeadersJTable(){
   	this.cementeriosJTableModel= new CementeriosTableModel(new String[]{aplicacion.getI18nString("generico.elem.columna0"),
                                                                aplicacion.getI18nString("generico.elem.columna1"),
                                                                aplicacion.getI18nString("generico.elem.columna2"),
                                                                aplicacion.getI18nString("generico.elem.columna3"),
                                                                aplicacion.getI18nString("generico.elem.columna4"),
                                                                aplicacion.getI18nString("generico.elem.columna5"),
                                                                aplicacion.getI18nString("generico.elem.columna6"), "HIDDEN", "HIDDEN"},
                                                                new boolean[]{false, false, false, false, false, false, false, false, false}, locale);
   }

    private void ElemCementerioJTableMouseReleased() {
        getElemSeleccionado();
        fireActionPerformed();
    }

    /**
     * Método que recoge el elemento seleccionado de la tabla
     * @return el elemento seleccionado de la tabla 
     */
    public Object getElemSeleccionado(){
       
    	selectedRow= elemCementeriosJTable.getSelectedRow();
	    if (selectedRow == -1){ 
	        	return null;
        }else{
        	cementeriosJTableModel.setTableSorted(cementeriosJTableModel.getTableSorted());
        	cementeriosJTableModel.setRows(cementeriosJTableModel.getRows());
        	return cementeriosJTableModel.getObjetAt(selectedRow);
        }
    }

    /**
     * Método que desmarca la fila seleccionada en la tabla
     */
    public void clearSelection(JTable jTable){
    	jTable.clearSelection();
    }
    public void clearSelection(){
    	elemCementeriosJTable.clearSelection();
    }

    /**
     * Método que carga en la tabla una lista de elementos
     * @param c Collection de elementos a cargar
     */
    public void loadListaElemCementerios(Collection c) throws Exception{
    	int numElementos=0;
    	String objectBeanStr = ""; 
    	
		objectBeanStr = getTextBySelection();
		renombrarComponentesBySelection(objectBeanStr);
        Collection cRet= new ArrayList();
        if (c != null){
	    	Object[] arrayElems = c.toArray();
	    	Arrays.sort(arrayElems,new CementerioComparator());
	    	int n = arrayElems.length;
	    	if (n>0){
	    		//primer eleme
	    		Object obj = arrayElems[0];
	    		if (obj instanceof UnidadEnterramientoBean){
	    	    	for (int i=0;i<n;i++){
	    	    		UnidadEnterramientoBean elem = (UnidadEnterramientoBean)arrayElems[i];
		    		cRet.add(elem);
		    		numElementos++;
	    	    	}
	    		}else
		    	if (obj instanceof PersonaBean){
		    	    for (int i=0;i<n;i++){
		    	    	PersonaBean elem = (PersonaBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
		    	}
	    		if (obj instanceof ConcesionBean){
		    	    for (int i=0;i<n;i++){
		    	    	ConcesionBean elem = (ConcesionBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    		if (obj instanceof BloqueBean){
		    	    for (int i=0;i<n;i++){
		    	    	BloqueBean elem = (BloqueBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}

	    		if (obj instanceof TarifaBean){
		    	    for (int i=0;i<n;i++){
		    	    	TarifaBean elem = (TarifaBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    		if (obj instanceof DifuntoBean){
		    	    for (int i=0;i<n;i++){
		    	    	DifuntoBean elem = (DifuntoBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    		if (obj instanceof InhumacionBean){
		    	    for (int i=0;i<n;i++){
		    	    	InhumacionBean elem = (InhumacionBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    		if (obj instanceof ExhumacionBean){
		    	    for (int i=0;i<n;i++){
		    	    	ExhumacionBean elem = (ExhumacionBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    		if (obj instanceof IntervencionBean){
		    	    for (int i=0;i<n;i++){
		    	    	IntervencionBean elem = (IntervencionBean)arrayElems[i];
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    		if (obj instanceof HistoricoDifuntoBean){
		    	    for (int i=0;i<n;i++){
		    	    	DifuntoBean elem = ((HistoricoDifuntoBean)arrayElems[i]).getDifunto();
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    		if (obj instanceof HistoricoPropiedadBean){
		    	    for (int i=0;i<n;i++){
		    	    	PersonaBean elem = ((ConcesionBean)((HistoricoPropiedadBean)arrayElems[i]).getElem()).getTitular();
			    	cRet.add(elem);
			    	numElementos++;
		    	    } 	
	    		}
	    }
        }
        cementeriosJTableModel.setModelData(cRet);
        reColorearBloqueo();
        
        int num = 0;
        if (c!=null)
        	num = numElementos;
       
        contadorElemCementeriosJLabel.setText(aplicacion.getI18nString("cementerio.contador." + objectBeanStr)
        		+ " " + num);
    }

    
    public void clearTable(){

    	cementeriosJTableModel.setModelData(new ArrayList());    	
    	cementeriosJTableModel.getTableSorted().sortingStatusChanged();    	

    }

    /**
     * Método que añade un elemento
     * @param obj a cargar
     */
    public void addElemCementerioTabla(Object obj){
        cementeriosJTableModel.annadirRow(obj);
    }

    /**
     * Método que actualiza un elemento en la tabla
     * @param obj a actualizar
     */
    public void updateElemCementerioTabla(Object obj){
        cementeriosJTableModel.actualizarRow(selectedRow, obj);
    }

    /**
     * Método que borra un elem de la tabla
     */
    public void deleteElemCementerioTabla(Object obj){
        cementeriosJTableModel.deleteRow(selectedRow, obj);
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    /**
     * Método que renombra los componentes del panel segun el locale
     */
    public void renombrarComponentes(){
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.bienes.tag1")));}catch(Exception e){}
        /** Headers de la tabla de bienes */
        TableColumn tableColumn= elemCementeriosJTable.getColumnModel().getColumn(0);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("cementerio.bienes.columna0"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(1);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("cementerio.bienes.columna1"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(2);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("cementerio.bienes.columna2"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(3);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("cementerio.bienes.columna3"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(4);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("cementerio.bienes.columna4"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(5);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("cementerio.bienes.columna5"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(6);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("cementerio.bienes.columna6"));}catch(Exception e){}
    }

    public String getTextByBean(Object obj){
    	String objectBeanStr = ""; 
    	if ((obj instanceof UnidadEnterramientoBean)){
    		objectBeanStr = "unidadEnterramiento";
    	}else if ((obj instanceof PersonaBean)){ 
    		{objectBeanStr = "persona";}
    	}else if ((obj instanceof ConcesionBean)){ 
        	{objectBeanStr = "concesion";}
    	}else if ((obj instanceof TarifaBean)){ 
    		{objectBeanStr = "tarifa";}
    	}else if ((obj instanceof BloqueBean)){ 
    		{objectBeanStr = "bloque";}
    	}
    	
    	return objectBeanStr;
    }
    
    public String getTextBySelection(){
    	String objectBeanStr = ""; 
    	
    	if ((getTipoSeleccionado()== null) || (getSubtipoSeleccionado() == null)){
    		objectBeanStr = "generico";
    	}else{
	        if (getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
	            if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BLOQUE)){
	            	objectBeanStr = "bloque";
	            }else if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
	            	objectBeanStr = "unidadEnterramiento";
	            }
	        }else if (getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
	        	if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION)){
	        		objectBeanStr = "concesion";
	        	}else if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR)){
	        		objectBeanStr = "titular";
	        	}else if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
	        		objectBeanStr = "tarifa";
	        	}
	        }else if (getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
	        	if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DEFUNCION)){
	        		objectBeanStr = "difunto";
	        	}else if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
	        		objectBeanStr = "tarifa";
	        	}else if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INHUMACION)){
	        		objectBeanStr = "inhumacion";
	        	}else if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_EXHUMACION)){
	        		objectBeanStr = "exhumacion";
	        	}
	        }else if (getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
	        	if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
	        		objectBeanStr = "historico";
	        	}
	        	else if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_PROPIEDAD)){
	        		objectBeanStr = "historicoPropiedad";
	        	}
	        }
	        else if (getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
	        	if (getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INTERVENCION)){
	        		objectBeanStr = "intervencion";
	        	}
	        }
    	}
    	return objectBeanStr;
    }
    
    /**
     * Método que renombra los componentes del panel segun el tipo de Objeto
     */
    public void renombrarComponentesByBean( String objectBeanStr,  Object obj){

    	panelPestañas.setTitleAt(0, aplicacion.getI18nString("cementerio.tab." + objectBeanStr));
    	
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.tab." + objectBeanStr)));}catch(Exception e){}
        /** Headers de la tabla de bienes */
        TableColumn tableColumn= elemCementeriosJTable.getColumnModel().getColumn(0);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna0"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(1);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna1"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(2);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna2"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(3);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna3"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(4);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna4"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(5);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna5"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(6);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna6"));}catch(Exception e){}
    }

    
    
    /**
     * Método que renombra los componentes del panel segun el tipo de Objeto
     */
    public void renombrarComponentesBySelection(String objectBeanStr){

    	panelPestañas.setTitleAt(0, aplicacion.getI18nString("cementerio.tab." + objectBeanStr));
    	
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.tab." + objectBeanStr)));}catch(Exception e){}
        /** Headers de la tabla de bienes */
        TableColumn tableColumn= elemCementeriosJTable.getColumnModel().getColumn(0);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna0"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(1);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna1"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(2);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna2"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(3);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna3"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(4);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna4"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(5);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna5"));}catch(Exception e){}
        tableColumn= elemCementeriosJTable.getColumnModel().getColumn(6);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString(objectBeanStr +".elem.columna6"));}catch(Exception e){}
    }
    
    /**
     * 
     */
    private void reColorearBloqueo(){
          Vector redRows= new Vector();
          for (int i=0; i < tableSorted.getRowCount(); i++){
              ElemCementerioBean cb= (ElemCementerioBean)((CementeriosTableModel)tableSorted.getTableModel()).getObjetAt(i);
              if (cb != null && cb.getBloqueado()!=null && !cb.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN,null,false))){
                  redRows.add(new Integer(i));
              }else redRows.add(null);
          }

        /** Re-Pintamos en rojo, los bienes que esten bloqueados */
        for (/* la columna 0 es un CheckBoxRenderer */ int j=1; j < cementeriosJTableModel.getColumnCount(); j++){
            // Annadimos a cada columna el renderer ColorTableCellRenderer
            TableColumn col= elemCementeriosJTable.getColumnModel().getColumn(j);
            com.geopista.app.licencias.utilidades.ColorTableCellRenderer colorTableCellRenderer= new com.geopista.app.licencias.utilidades.ColorTableCellRenderer(redRows);
            col.setCellRenderer(colorTableCellRenderer);
        }

    }

    public String getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(String tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}

	public String getSubtipoSeleccionado() {
		return subtipoSeleccionado;
	}

	public void setSubtipoSeleccionado(String subtipoSeleccionado) {
		this.subtipoSeleccionado = subtipoSeleccionado;
	}
    
    
    /**
     * Compruebo si el bien está dado de baja o no
     */
    private boolean estaActivoBien(long revisionExpirada){
    	return revisionExpirada == Long.parseLong("9999999999");
    }    
    
    
    class CementerioComparator implements Comparator {
    	public int compare(Object o1, Object o2) {
    		ElemCementerioBean b1 = (ElemCementerioBean)o1;
    		ElemCementerioBean b2 = (ElemCementerioBean)o2;
	    	if (b1.getId()>b2.getId())
	    		return 1;
	    	else
	    		return -1;
    	}
    }

    class BienTableRenderer extends javax.swing.JTable{
    	private int columnRevisionExpirada; 
    	
    	BienTableRenderer(int columnRevisionExpirada){   		
    		this.columnRevisionExpirada = columnRevisionExpirada;
    	}
    	
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column){ 
        	Component returnComp = super.prepareRenderer(renderer, row,column); 
        	returnComp.setForeground(Color.BLUE); 
        	return returnComp; 
        } 
    	
    }
    
    
}
