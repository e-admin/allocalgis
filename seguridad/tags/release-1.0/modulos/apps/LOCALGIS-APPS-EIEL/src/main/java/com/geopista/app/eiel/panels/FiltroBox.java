package com.geopista.app.eiel.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.filter.LCGCampoCapaEIEL;
import com.geopista.app.filter.CampoFiltro;
import com.geopista.app.filter.CamposFiltroJComboBox;
import com.geopista.app.filter.ComboBoxSelectionManager;
import com.geopista.app.filter.LCGFilter;
import com.geopista.app.filter.OperadoresFiltros;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;

public class FiltroBox extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5250927649868773659L;
	private TextField valorJTField;
	private Component valorComponent;
	private JFormattedTextField valorFJTField;
	private CalendarButton fechaJButton;
	private ComboBoxEstructuras valorEJCBox;
	private JNumberTextField valorDJTField;
	private JNumberTextField valorNJTField;
	private JComboBox valorBJCBox;
	private ListaPane filtroListaPane;
	private JButton addJButton;
	private JButton addAllJButton;
	private JButton removeJButton;
	private JButton clearJButton;
	private JComboBox operadorJCBox;
	private JPanel filtroJPanel;
	private CamposFiltroJComboBox campoJCBox;
    private javax.swing.JCheckBox aplicarFiltroJCBox;
	
	private ArrayList campos;
	private String patron;
	private CampoFiltro campoFiltroSelected;
	private String locale;
	private String nombreFiltro;
	private String filtrosSeleccionados;

	private String campoEntidad=null;
	private String campoNucleo=null;
	
    private LocalGISEIELClient eielClient;
    private AppContext aplicacion;
    
    private boolean filtrosDeshabilitadosPermanentemente=false;
    
    

    private static  org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FiltroBox.class);

	
    public FiltroBox(String patron, String nombreFiltro,String filtrosSeleccionados,String locale) {
    	aplicacion= (AppContext) AppContext.getApplicationContext();
    	this.locale=locale;
    	this.nombreFiltro=nombreFiltro;
    	this.patron = patron;
    	this.filtrosSeleccionados=filtrosSeleccionados;
    	
	    eielClient = new LocalGISEIELClient(aplicacion.getString("geopista.conexion.servidorurl")+"EIEL/EIELServlet");

	    //Si el usuario ha selecciono algun elemento ya no le dejamos filtrar.
	    if (filtrosSeleccionados==null)
	    	initAllCampos();
	    
        initComponents();
        
        if (filtrosSeleccionados!=null){
        	habilitarFiltros(false);
        	filtrosDeshabilitadosPermanentemente=true;
        	fillSelectedFilter(filtrosSeleccionados);
        }
        
        renombrarComponentes();
    }	
    
   
    
    private void initAllCampos(){    	
    	if (patron==null) return;
    	//if ((patron.equals("D1")) || (patron.equals("D2")))
    		//patron="ED";
    	Collection<Object> camposEIEL=getCamposCapaEIEL(patron,locale);
    	
    	logger.debug("Numero de campos:"+camposEIEL.size());
    	if (camposEIEL.size()==0)
    		logger.error("No hay campo para filtra para la capa con patron:"+patron);
		
    	campos=new ArrayList();
    	
	
    	Iterator it=camposEIEL.iterator();
		while (it.hasNext()){
			LCGCampoCapaEIEL campoEIEL=(LCGCampoCapaEIEL)it.next();
			
			//Estos campos nos marcan el campo en la Base de Datos que tiene 
			//el codigo de entidad y el campo que tiene el codigo de nucleo
			if (campoEIEL.getTipoBD()==CampoFiltro.CAMPO_CODIGOENTIDAD){
				campoEntidad=campoEIEL.getCampoBD();
				continue;
			}
			if (campoEIEL.getTipoBD()==CampoFiltro.CAMPO_CODIGONUCLEO){
				campoNucleo=campoEIEL.getCampoBD();
				continue;
			}

			//No dejamos filtrar por el campo codigo nucleo ya que es un
			//numero que suelen ser 01 o 70 y con eso poco filtro se puede hacer
			if (campoEIEL.getCampoBD()!=null && (campoEIEL.getCampoBD().equals("codpoblamiento")))
				continue;
			

			
			logger.debug("Nodo:"+campoEIEL.getTraduccion());	
			
	         CampoFiltro campo= new CampoFiltro();
	         campo.setNombre(campoEIEL.getCampoBD());
	         campo.setDescripcion(campoEIEL.getTraduccion());
	         campo.setTabla(campoEIEL.getTabla());
	         
	         switch(campoEIEL.getTipoBD()){
	        	case CampoFiltro.VARCHAR_CODE:{
	        		campo.setVarchar();
	        		break;
	        	}
	        	case CampoFiltro.NUMERIC_CODE:{
	        		campo.setNumeric();
	        		break;
	        	}
	        	case CampoFiltro.DATE_CODE:{
	        		campo.setDate();
	        		break;
	        	}
	        	case CampoFiltro.DOUBLE_CODE:{
	        		campo.setDouble();
	        		break;
	        	}
	        	case CampoFiltro.DOMINIO_CODE:{
	        		campo.setIsDominio();
		        	campo.setNombreDominio(campoEIEL.getDominio());
	        		break;
	        	}
	        	case CampoFiltro.DOMINIO_CODE_INTEGER:{
	        		campo.setIsDominioInteger();
		        	campo.setNombreDominio(campoEIEL.getDominio());
	        		break;
	        	}
	        	case CampoFiltro.BOOLEAN_CODE:{
	        		campo.setBoolean();
	        		break;
	        	}
	        	case CampoFiltro.COMPUESTO_CODE:{
	        		campo.setCompuesto();
	        		break;
	        	}
	         	        	
	         }
	         campos.add(campo);
		}
    	
    }
  
    
	private void initComponents() {
		
				
		valorJTField = new com.geopista.app.utilidades.TextField(254);
	    valorJTField.setEnabled(false);
	    valorJTField.setVisible(true);
	
	    valorComponent= valorJTField;
	
	    valorFJTField= new JFormattedTextField(ConstantesLocalGISEIEL.df);
	    valorFJTField.setEnabled(false);
	    valorFJTField.setVisible(false);
	    fechaJButton= new CalendarButton(valorFJTField);
	    fechaJButton.setEnabled(false);
	    fechaJButton.setVisible(false);
	
	    valorEJCBox= new ComboBoxEstructuras(/*con blanco*/true);
	    valorEJCBox.setEnabled(false);
	    valorEJCBox.setVisible(false);
	
	    valorDJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
	    valorDJTField.setEnabled(false);
	    valorDJTField.setVisible(false);
	
	    valorNJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(999999999), true, 2);
	    valorNJTField.setEnabled(false);
	    valorNJTField.setVisible(false);
	
	    valorBJCBox= new JComboBox();
	    valorBJCBox.addItem("");
	    valorBJCBox.addItem("true");
	    valorBJCBox.addItem("false");
	    valorBJCBox.setEnabled(false);
	    valorBJCBox.setVisible(false);
	    
	    aplicarFiltroJCBox= new javax.swing.JCheckBox();
        aplicarFiltroJCBox.setSelected(true);
	
	    filtroListaPane= new ListaPane(null);
	    filtroListaPane.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            filtroListaPaneActionPerformed();
	        }
	    });
	
	    addJButton= new JButton();
	    addJButton.setIcon(com.geopista.app.eiel.UtilidadesComponentes.iconoAdd);
	    addJButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            addJButtonActionPerformed();
	        }
	    });
	    addJButton.setEnabled(true);
	    
	    addAllJButton= new JButton();
	    addAllJButton.setIcon(com.geopista.app.eiel.UtilidadesComponentes.iconoOK);
	    addAllJButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            addAllJButtonActionPerformed();
	        }
	    });
	    addAllJButton.setEnabled(true);
	
	    removeJButton= new JButton();
	    removeJButton.setIcon(com.geopista.app.eiel.UtilidadesComponentes.iconoBorrar);
	    removeJButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            removeJButtonActionPerformed();
	        }
	    });
	    removeJButton.setEnabled(false);
	
	    clearJButton= new JButton();
	    clearJButton.setIcon(com.geopista.app.eiel.UtilidadesComponentes.iconoClear);
	    clearJButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            clearJButtonActionPerformed();
	        }
	    });
	    clearJButton.setEnabled(true);	
	
	    if (campos!=null)
	    	campoJCBox= new CamposFiltroJComboBox(campos.toArray(), null, true);
	    else
	    	campoJCBox= new CamposFiltroJComboBox(null, null, true);
	    campoJCBox.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            campoJCBoxActionPerformed();
	        }
	    });
	    campoJCBox.setKeySelectionManager(new ComboBoxSelectionManager());
	    campoJCBox.setFocusCycleRoot(true);
	
	    operadorJCBox= new JComboBox();
	    operadorJCBox.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            operadorJCBoxActionPerformed();
	        }
	    });
	    operadorJCBox.setEnabled(false);
	
	    filtroJPanel= new JPanel();
	    filtroJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
	    filtroJPanel.add(campoJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 20));
	    filtroJPanel.add(operadorJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 160, 20));
	    /** en la misma posicion. En funcion del campo mostramos el componente correspondiente */
	    filtroJPanel.add(valorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
	    filtroJPanel.add(valorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
	    filtroJPanel.add(valorFJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
	    filtroJPanel.add(valorDJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
	    filtroJPanel.add(valorNJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
	    filtroJPanel.add(valorBJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
	    filtroJPanel.add(fechaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 20, 20));
	    filtroJPanel.add(addJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 20, 20));
	    //filtroJPanel.add(addAllJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 20, 20));
	
        aplicarFiltroJCBox.setText("Aplicar Filtro Seleccionado");
	    filtroJPanel.add(aplicarFiltroJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 100, 15));

	    
	    JPanel filtroListaPaneJPanel= new JPanel();
	    filtroListaPaneJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
	    
	    //Tamaño de la textarea donde se muestran los filtros aplicados
	    filtroListaPaneJPanel.add(filtroListaPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 620, 100));
	    filtroListaPaneJPanel.add(removeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 20, 20));
	    filtroListaPaneJPanel.add(clearJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, 20, 20));

	
	    setLayout(new java.awt.BorderLayout());
	    add(filtroJPanel, BorderLayout.NORTH);
	    add(filtroListaPaneJPanel, BorderLayout.CENTER);
 	}	
	
	public void habilitarFiltros(boolean activar) {

		if (filtrosDeshabilitadosPermanentemente) return;
		
	    valorJTField.setEnabled(activar);
	    valorFJTField.setEnabled(activar);
	    fechaJButton.setEnabled(activar);
	    valorEJCBox.setEnabled(activar);
	    valorDJTField.setEnabled(activar);
	    valorNJTField.setEnabled(activar);
	    valorBJCBox.setEnabled(activar);
        aplicarFiltroJCBox.setEnabled(activar);
        filtroListaPane.setEnabled(activar);
	    addJButton.setEnabled(activar);
	    addAllJButton.setEnabled(activar);
	    removeJButton.setEnabled(activar);
	    clearJButton.setEnabled(activar);		
	    campoJCBox.setEnabled(activar);
	    operadorJCBox.setEnabled(activar);	
	    filtroJPanel.setEnabled(activar);
 	}	
	

	
	
 	private void renombrarComponentes(){
        try{addJButton.setToolTipText("Añadir expresión de búsqueda");}catch(Exception e){}
        try{removeJButton.setToolTipText("Elimar expresión de búsqueda");}catch(Exception e){}
        try{clearJButton.setToolTipText("Eliminar Filtro");}catch(Exception e){}
    }
 	
 	private void filtroListaPaneActionPerformed(){
        campoFiltroSelected= (CampoFiltro)filtroListaPane.getSelected();
        if (campoFiltroSelected != null){
            removeJButton.setEnabled(true);
        }
    }
 	
 	 private void addJButtonActionPerformed(){
         if (campoJCBox.getSelectedIndex() == 0){
             campoJCBox.requestFocusInWindow();
             return;
         }
         if (operadorJCBox.getSelectedIndex() == 0){
             operadorJCBox.requestFocusInWindow();
             return;
         }

         filtroListaPane.clearSeleccion();
         removeJButton.setEnabled(false);

         campoFiltroSelected= (CampoFiltro)campoJCBox.getSelectedItem();
         campoFiltroSelected.setOperador((String)operadorJCBox.getSelectedItem());

         if (valorComponent instanceof ComboBoxEstructuras) {
             if (((ComboBoxEstructuras)valorComponent).getSelectedPatron() == null){
                 valorComponent.requestFocusInWindow();
                 return;
             }
             campoFiltroSelected.setValorVarchar(((ComboBoxEstructuras)valorComponent).getSelectedPatron());
             
             campoFiltroSelected.setValorTerm(getTerm());
            
         }else if (valorComponent instanceof JFormattedTextField){
             if (((JFormattedTextField)valorComponent).getText().trim().equalsIgnoreCase("")){
                 valorComponent.requestFocusInWindow();
                 return;
             };
             try{campoFiltroSelected.setValorDate(ConstantesLocalGISEIEL.df.parse(((JFormattedTextField)valorComponent).getText().trim()));}catch(java.text.ParseException e){}
             campoFiltroSelected.setValorVarchar(((JFormattedTextField)valorComponent).getText().trim());
         }else if (valorComponent instanceof JNumberTextField){
             if (((JNumberTextField)valorComponent).getText().trim().equalsIgnoreCase("")){
                 valorComponent.requestFocusInWindow();
                 return;
             };
             if (campoFiltroSelected.isDouble()){
                 try{campoFiltroSelected.setValorDouble(((Double)((JNumberTextField)valorComponent).getNumber()).doubleValue());}catch(Exception e){}
             }else try{campoFiltroSelected.setValorNumeric(((Long)((JNumberTextField)valorComponent).getNumber()).longValue());}catch(Exception e){}
             campoFiltroSelected.setValorVarchar(((JNumberTextField)valorComponent).getText().trim());
         }else if (valorComponent instanceof com.geopista.app.utilidades.TextField){
             if (((com.geopista.app.utilidades.TextField)valorComponent).getText().trim().equalsIgnoreCase("")){
                 valorComponent.requestFocusInWindow();
                 return;
             };
             if (campoFiltroSelected.getOperador().equalsIgnoreCase("LIKE"))
                 campoFiltroSelected.setValorVarchar("%"+((com.geopista.app.utilidades.TextField)valorComponent).getText().trim()+"%");
             else campoFiltroSelected.setValorVarchar(((com.geopista.app.utilidades.TextField)valorComponent).getText().trim());
         }else if (valorComponent instanceof JComboBox){
             if (((JComboBox)valorComponent).getSelectedIndex() == 0){
                 valorComponent.requestFocusInWindow();
                 return;
             }
             campoFiltroSelected.setValorVarchar((String)((JComboBox)valorComponent).getSelectedItem());
             campoFiltroSelected.setValorBoolean(((JComboBox)valorComponent).getSelectedIndex()==1?true:false);
         }

         /* añadimos el filtro a la lista */
         filtroListaPane.addOnlyIfNotExist(campoFiltroSelected.clone(), /*no seleccionar*/false);

         clear();
         operadorJCBox.setEnabled(false);
         valorComponent.setEnabled(false);
         fechaJButton.setEnabled(false);

     }
 	 
 	 private void addAllJButtonActionPerformed(){
 		
     	Iterator it=campos.iterator();
 		while (it.hasNext()){
 			CampoFiltro campoFiltro=(CampoFiltro)it.next();
 	        switch(campoFiltro.getTipo()){
 	        	case CampoFiltro.VARCHAR_CODE:{
 	        		campoFiltro.setOperador("=");
 	        		campoFiltro.setValorVarchar("TEST");
 	        		break;
 	        	}
 	        	case CampoFiltro.NUMERIC_CODE:{
 	        		campoFiltro.setOperador("=");
 	        		campoFiltro.setValorNumeric(0);
 	        		break;
 	        	}
 	        	case CampoFiltro.DATE_CODE:{
 	        		campoFiltro.setOperador("=");
 	        		campoFiltro.setValorDate(new Date());
 	        		break;
 	        	}
 	        	case CampoFiltro.DOUBLE_CODE:{
 	        		campoFiltro.setOperador("=");
 	        		campoFiltro.setValorDouble(0);
 	        		break;
 	        	}
 	        	case CampoFiltro.DOMINIO_CODE:{
 	        		campoFiltro.setOperador("=");
 	        		campoFiltro.setValorTerm("TEST");
 	        		break;
 	        	}
 	        	case CampoFiltro.DOMINIO_CODE_INTEGER:{
 	        		campoFiltro.setOperador("=");
 	        		campoFiltro.setValorNumeric(0);
 	        		break;
 	        	}
 	        	case CampoFiltro.BOOLEAN_CODE:{
 	        		campoFiltro.setOperador("=");
 	        		campoFiltro.setValorBoolean(true);
 	        		break;
 	        	}
 	        	case CampoFiltro.COMPUESTO_CODE:{
 	        		//campo.setCompuesto();
 	        		break;
 	        	}
 	         	        	
 	         }

	         filtroListaPane.addOnlyIfNotExist(campoFiltro.clone(), /*no seleccionar*/false);

 		}
 	 }
 	 
 	 private void fillSelectedFilter(String selectedFilter){
 		 
 		String []campos=selectedFilter.split(" or ");
 		for (int i=0;i<campos.length;i++){
 			CampoFiltro campoFiltro=new CampoFiltro();
 			campoFiltro.setTabla(campos[i]);
 			campoFiltro.setNombre("");
 			campoFiltro.setOperador("");
 			campoFiltro.setValorVarchar("");
 			filtroListaPane.addOnlyIfNotExist(campoFiltro.clone(),false);
 		}
 	 }

 	 
 	private String getTerm(){
    	DomainNode nodoSelected=(DomainNode)((ComboBoxEstructuras)valorComponent).getSelectedItem();
    	return nodoSelected.getTerm(locale);
    	
    }
 	 
 	private void removeJButtonActionPerformed(){
        if (filtroListaPane.getSelected() == null) return;

        filtroListaPane.borrar();
        removeJButton.setEnabled(false);
        addJButton.setEnabled(true);
    }


    private void clearJButtonActionPerformed(){
        filtroListaPane.setCollection(null);
        filtroListaPane.actualizarModelo();
    }
    
    private void operadorJCBoxActionPerformed(){
        if (operadorJCBox.getSelectedIndex()!=0 && campoFiltroSelected!=null){
            addJButton.setEnabled(true);
            if (!campoFiltroSelected.isDate()){
                valorComponent.setEnabled(true);
                valorComponent.requestFocusInWindow();
            }else{
                fechaJButton.setEnabled(true);
            }
        }
    }
    
    /**
     * Accion para mostrar los posibles valores de dominio
     */
    private void campoJCBoxActionPerformed(){
        if (campoJCBox.getSelectedIndex()!=0){
            removeJButton.setEnabled(false);
            filtroListaPane.clearSeleccion();

            campoFiltroSelected= (CampoFiltro)campoJCBox.getSelectedItem();
            /** cargamos los operadores y el campo valor segun el campo seleccionado. */
            if (!campoFiltroSelected.isDate()){
                fechaJButton.setVisible(false);
                fechaJButton.setEnabled(false);

                if (campoFiltroSelected.isVarchar()) {
                	
                	if (campoFiltroSelected.getNombre().contains("codentidad")){
	                   valorComponent= valorEJCBox;
	                   cargarDominioEspecial(campoFiltroSelected.getNombreDominio());
	                   cargarOperadoresDominio();
                	}
                	else{
 	                   valorComponent= valorJTField;
 	                   cargarOperadoresVarchar();	
                	}                
                }else if (campoFiltroSelected.isBoolean()){
                   valorComponent= valorBJCBox;
                   cargarOperadoresBoolean();
                }else if (campoFiltroSelected.isDominio()){
                   valorComponent= valorEJCBox;
                   cargarDominio(campoFiltroSelected.getNombreDominio());
                   cargarOperadoresDominio();
                }else if (campoFiltroSelected.isDominioInteger()){
                    valorComponent= valorEJCBox;
                    cargarDominio(campoFiltroSelected.getNombreDominio());
                    cargarOperadoresDominioInteger();
                 }else if (campoFiltroSelected.isDouble()){
                   valorComponent= valorDJTField;
                   cargarOperadoresNumericDouble();
                }else if (campoFiltroSelected.isNumeric()){
                    valorComponent= valorNJTField;
                    cargarOperadoresNumericDouble();
                }
            }else{
                valorComponent= valorFJTField;
                fechaJButton.setVisible(true);
                cargarOperadoresDate();
            }

            setVisible();
            filtroJPanel.repaint();
            filtroJPanel.invalidate();
            filtroJPanel.validate();

            operadorJCBox.setEnabled(true);
            operadorJCBox.requestFocusInWindow();

        }
    }
    
    private void cargarOperadoresNumericDouble(){
        ArrayList<String> operadores=OperadoresFiltros.initOperadoresNumericDouble();
        populateJCBox(operadores);  
    }
    
    private void cargarOperadoresVarchar(){
    	ArrayList<String> operadores=OperadoresFiltros.initOperadoresVarchar();
    	populateJCBox(operadores);
    }

    private void cargarOperadoresDominio(){
    	ArrayList<String> operadores=OperadoresFiltros.initOperadoresDominio();
    	populateJCBox(operadores);
    }
    private void cargarOperadoresDominioInteger(){
    	ArrayList<String> operadores=OperadoresFiltros.initOperadoresDominioInteger();
    	populateJCBox(operadores);
    }
    
    private void cargarOperadoresDate(){
    	ArrayList<String> operadores=OperadoresFiltros.initOperadoresDate();
    	populateJCBox(operadores);      
    }

    private void cargarOperadoresBoolean(){
    	ArrayList<String> operadores=OperadoresFiltros.initOperadoresBoolean();
    	populateJCBox(operadores);       
    }    
    
    private void populateJCBox(ArrayList<String> operadores){
    	operadorJCBox.removeAllItems();
        Iterator<String> it=operadores.iterator();
        while (it.hasNext()){
        	String operador=it.next();
        	operadorJCBox.addItem(operador);
        }        
    }

    private void cargarDominio(String nombreDominio){
        valorEJCBox.removeAllItems();        
        Estructuras.cargarEstructura(nombreDominio,true);
        
        if (Estructuras.getListaTipos().size()==0)
        	logger.warn("No existe estructura para el dominio:"+nombreDominio);
        valorEJCBox.setEstructuras(Estructuras.getListaTipos(), null, locale, true);
        
    }
    
    private void cargarDominioEspecial(String nombreDominio){
        valorEJCBox.removeAllItems();        
        //Estructuras.cargarEstructura(nombreDominio,true);
        
        Collection c=(Collection)aplicacion.getBlackboard().get("VECTOR_NUCLEOS_MUNICIPIO");
        LCGFilter.loadEstructuraEntidadesNucleo(c);      
        Estructuras.cargarEstructura(ConstantesLocalGISEIEL.ESTRUCTURA_ENTIDADES_NUCLEOS,true);  
        if (Estructuras.getListaTipos().size()==0)
        	logger.warn("No existe estructura para el dominio:"+nombreDominio);
        valorEJCBox.setEstructuras(Estructuras.getListaTipos(), null, locale, true);
        
        
        /*nucleoJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(), null, locale, false);
        
        if (Estructuras.getListaTipos().size()==0)
        	logger.warn("No existe estructura para el dominio:"+nombreDominio);
        valorEJCBox.setEstructuras(Estructuras.getListaTipos(), null, locale, true);*/
        
    }
    
    
   
    private void clear(){
        campoJCBox.setSelectedIndex(0);
        try{operadorJCBox.setSelectedIndex(0);}catch(Exception e){}

        if (valorComponent instanceof ComboBoxEstructuras) {
            ((ComboBoxEstructuras)valorComponent).setSelectedPatron(null);
        }else if (valorComponent instanceof JFormattedTextField){
            ((JFormattedTextField)valorComponent).setText("");
        }else if (valorComponent instanceof JNumberTextField){
            ((JNumberTextField)valorComponent).setText("");
        }else if (valorComponent instanceof com.geopista.app.utilidades.TextField){
            ((com.geopista.app.utilidades.TextField)valorComponent).setText("");
        }else if (valorComponent instanceof JComboBox){
            try{((JComboBox)valorComponent).setSelectedIndex(0);}catch(Exception e){}
        }

    }
    private void setVisible(){
        if (valorComponent instanceof ComboBoxEstructuras) {
            valorEJCBox.setVisible(true);
            valorBJCBox.setVisible(false);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(false);
        }else if (valorComponent instanceof JFormattedTextField){
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(false);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(true);
            valorJTField.setVisible(false);
        }else if (valorComponent instanceof JNumberTextField){
            if (campoFiltroSelected.isNumeric()){
                valorNJTField.setVisible(true);
                valorDJTField.setVisible(false);
            }else{
                valorNJTField.setVisible(false);
                valorDJTField.setVisible(true);
            }
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(false);
        }else if (valorComponent instanceof com.geopista.app.utilidades.TextField){
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(false);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(true);
        }else if (valorComponent instanceof JComboBox){
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(true);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(false);
        }
    }
	
	
	
	private Collection<Object> getCamposCapaEIEL(String nodo,String locale){
		Collection<Object> c=null;
        try {
			c = eielClient.getCamposCapaEIEL(nodo,locale);
		} catch (Exception e) {
			e.printStackTrace();
		}
        

		//Los ordenamos alfabeticamente		
		Object[] arrayNodos= c.toArray();
    	Arrays.sort(arrayNodos,new NodoComparatorByTraduccion());

    	//Los devolvemos ordenados
    	c.clear();
		for (int i = 0; i < arrayNodos.length; ++i) {
			LCGCampoCapaEIEL campoCapaEIEL = (LCGCampoCapaEIEL) arrayNodos[i];
			c.add(campoCapaEIEL);
		}

        return c;
       
    }
	
	public String getNombre() {
		return nombreFiltro;
	}
	
	/**
	 * Obtenemos el valor del filtro
	 * @param nucleoSeleccionado
	 * @return
	 */
	public String getValue(String codEntidad,String codNucleo) {		
		String sql=null;
		
        String sqlNucleo=null;
        if ((codEntidad.equals("000")) && (codNucleo.equals("000")))
        	logger.debug("Filtro sin seleccion de nucleo");
        else{
    		logger.info("Filtro con seleccion con nucleo");
        	if ((campoEntidad==null) || (campoNucleo==null))
        		logger.info("No se ha definido el campo entidad o el campo nucleo para la capa:"+patron);        	
        	else{
        		sqlNucleo="( "+campoEntidad+"='"+codEntidad+"' and "+campoNucleo+"='"+codNucleo+"' )";
        	}
        }
        //******************************************
		//Tenemos que añadir un "and" a la condicion
        //******************************************
		if (filtrosSeleccionados==null){
			sql=OperadoresFiltros.getSQLFromFiltro(filtroListaPane.getCollection());
			if (!sql.equals("")){
				sql=" and "+sql;
				if (sqlNucleo!=null)
					sql+=" and "+sqlNucleo;
			}
			else{
				if (sqlNucleo!=null)
					sql+=" and "+sqlNucleo;
			}
		}
		else{
			sql=" and "+filtrosSeleccionados;
			if (sqlNucleo!=null)
				sql+=" and "+sqlNucleo;
		}
		
		return sql;
	}
	
	public boolean isFiltroSelecionado(){
		return aplicarFiltroJCBox.isSelected();
	}
	
	class NodoComparatorByTraduccion implements Comparator {
    	public int compare(Object o1, Object o2) {
    		if (o1 instanceof LCGCampoCapaEIEL && o2 instanceof LCGCampoCapaEIEL){
    			LCGCampoCapaEIEL b1 = (LCGCampoCapaEIEL)o1;
    			LCGCampoCapaEIEL b2 = (LCGCampoCapaEIEL)o2;
    			String traduccion1=b1.getTraduccion();
    			String traduccion2=b2.getTraduccion();
    			
    			if (traduccion1.compareTo(traduccion2)>0)
    				return 1;
    			else if (traduccion1.compareTo(traduccion2)<0)
    				return -1;
    			else
    				return 0;
            	   			    	
    		}	 
        	return 0;
    	}
    }
	
}
