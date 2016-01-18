package com.geopista.app.eiel.panels;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.InventarioEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.DepositosDialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.components.DateField;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class DepositosPanel extends InventarioPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelClave = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldClave = null;
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JTextField jTextFieldOrden = null;
	private JLabel jLabelUbic = null;
	private JLabel jLabelTit = null;
	private JLabel jLabelGest = null;
	private JLabel jLabelCapac = null;
	private JLabel jLabelEst = null;
	private JLabel jLabelProt = null;
	private JLabel jLabelFechaLimp = null;
	private JLabel jLabelCont = null;
	private JLabel jLabelFechaInst = null;
	private JLabel jLabelObserv = null;
	private JLabel jLabelFechaRevi = null;
	private JLabel jLabelEstado = null;
	private ComboBoxEstructuras jTextFieldUbic = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private JTextField jTextFieldCapac = null;
	private ComboBoxEstructuras jComboBoxEst = null;
	private ComboBoxEstructuras jComboBoxProteccion = null;
	private JTextField jTextFieldFechaLimp = null;
	private ComboBoxEstructuras jComboBoxContador = null;
	private JTextField jTextFieldObserv = null;
	private DateField jTextFieldFechaInst = null;
	private JTextField jTextFieldFechaRev = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	private String idMunicipioSelected;
	
    public DepositosPanel(){
        super();
        initialize();
    }
  
    public DepositosPanel(DepositosEIEL dato){
        super();
        initialize();
        loadData ();
    }
    
    
	public void loadData(DepositosEIEL panelDepositoEIEL) {
		    	
    	if (panelDepositoEIEL != null){    		
    		
			idMunicipioSelected=panelDepositoEIEL.getCodINEMunicipio();

            //Datos identificacion
        	if (panelDepositoEIEL.getClave() != null){
        		jTextFieldClave.setText(panelDepositoEIEL.getClave());
        	}
        	else{
        		jTextFieldClave.setText("");
        	}
        	
        	if (panelDepositoEIEL.getCodINEProvincia() != null) {
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(panelDepositoEIEL
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}

			if (panelDepositoEIEL.getCodINEMunicipio() != null) {
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(panelDepositoEIEL
								.getCodINEMunicipio()));
			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}
                      
            if (panelDepositoEIEL.getOrdenDeposito() != null){
        		jTextFieldOrden.setText(panelDepositoEIEL.getOrdenDeposito().toString());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            
            if (panelDepositoEIEL.getUbicacion() != null){
        		jTextFieldUbic.setSelectedPatron(panelDepositoEIEL.getUbicacion());
        	}
        	else{
        		jTextFieldUbic.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getTitularidad() != null){
            	jComboBoxTitular.setSelectedPatron(panelDepositoEIEL.getTitularidad());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getGestor() != null){
            	jComboBoxGestor.setSelectedPatron(panelDepositoEIEL.getGestor());
        	}
        	else{
        		jComboBoxGestor.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getCapacidad() != null){
        		jTextFieldCapac.setText(panelDepositoEIEL.getCapacidad().toString());
        	}
        	else{
        		jTextFieldCapac.setText("");
        	}
            
            if (panelDepositoEIEL.getEstado() != null){
        		jComboBoxEst.setSelectedPatron(panelDepositoEIEL.getEstado());
        	}
        	else{
        		jComboBoxEst.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getProteccion() != null){
            	jComboBoxProteccion.setSelectedPatron(panelDepositoEIEL.getProteccion());
        	}
        	else{
        		jComboBoxProteccion.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getFechaLimpieza() != null){
        		jTextFieldFechaLimp.setText(panelDepositoEIEL.getFechaLimpieza().toString());
        	}
        	else{
        		jTextFieldFechaLimp.setText("");
        	}
            
            if (panelDepositoEIEL.getContador() != null){
            	jComboBoxContador.setSelectedPatron(panelDepositoEIEL.getContador().toString());
        	}
        	else{
        		jComboBoxContador.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getFechaInstalacion() != null){
        		jTextFieldFechaInst.setDate(panelDepositoEIEL.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
        	}
            
            if (panelDepositoEIEL.getObservaciones() != null){
        		jTextFieldObserv.setText(panelDepositoEIEL.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
            
            
            if (panelDepositoEIEL.getFechaRevision() != null && panelDepositoEIEL.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(panelDepositoEIEL.getFechaRevision().toString());
        	}
        	else{
        	    
        		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                 java.util.Date date = new java.util.Date();
                 String datetime = dateFormat.format(date);

         		jTextFieldFechaRev.setText(datetime);
        	}
            
            if (panelDepositoEIEL.getEstadoRevision() != null){
            	jComboBoxEstado.setSelectedPatron(panelDepositoEIEL.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getTitularidadMunicipal() != null){
            	jComboBoxTitularidadMunicipal.setSelectedPatron(panelDepositoEIEL.getTitularidadMunicipal());
        	}
        	else{
        		jComboBoxTitularidadMunicipal.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getEpigInventario() != null){
            	jComboBoxEpigInventario.setSelectedPatron(panelDepositoEIEL.getEpigInventario().toString());
        	}
        	else{
        		if (jComboBoxEpigInventario.isEnabled())
        			jComboBoxEpigInventario.setSelectedIndex(0);
        	}
            
            if (panelDepositoEIEL.getIdBien() != null && panelDepositoEIEL.getIdBien() != 0){
            	jComboBoxNumInventario.setEnabled(true);
				EdicionOperations oper = new EdicionOperations();          			
				EdicionUtils.cargarLista(getJComboBoxNumInventario(), oper.obtenerNumInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron())));
				InventarioEIEL inventario = new InventarioEIEL();
				inventario.setIdBien(panelDepositoEIEL.getIdBien());
            	jComboBoxNumInventario.setSelectedItem(inventario);
            }
            else{
            	if (jComboBoxNumInventario.isEnabled())
            		jComboBoxNumInventario.setSelectedIndex(0);
            }
            
            
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.DEPOSITOS_CLAVE);
        	 if (ConstantesLocalGISEIEL.idProvincia!=null){
 				jComboBoxProvincia
 				.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
 			 }

 			 if (ConstantesLocalGISEIEL.idMunicipio!=null){
 				 jComboBoxMunicipio
 					.setSelectedIndex(municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio));
 			 }        	       	
        	jComboBoxEpigInventario.setEnabled(false);
        }
	}
    
    
    public void loadData(){
    	Object object = AppContext.getApplicationContext().getBlackboard().get("deposito_panel");    	
    	if (object != null && object instanceof DepositosEIEL){    		
    		DepositosEIEL elemento = (DepositosEIEL)object;
            //Datos identificacion
        	if (elemento.getClave() != null){
        		jTextFieldClave.setText(elemento.getClave());
        	}
        	else{
        		jTextFieldClave.setText("");
        	}
        	
        	if (elemento.getCodINEProvincia() != null) {
				// jComboBoxProvincia.setSelectedItem(panelCaptacionEIEL.getCodINEProvincia());
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(elemento
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}

			if (elemento.getCodINEMunicipio() != null) {
				// jComboBoxMunicipio.setSelectedItem(panelCaptacionEIEL.getCodINEMunicipio());
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(elemento
								.getCodINEMunicipio()));

			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}
                      
            if (elemento.getOrdenDeposito() != null){
        		jTextFieldOrden.setText(elemento.getOrdenDeposito().toString());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            
            if (elemento.getUbicacion() != null){
        		jTextFieldUbic.setSelectedPatron(elemento.getUbicacion());
        	}
        	else{
        		jTextFieldUbic.setSelectedIndex(0);
        	}
            
            if (elemento.getTitularidad() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitularidad());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (elemento.getGestor() != null){
            	jComboBoxGestor.setSelectedPatron(elemento.getGestor());
        	}
        	else{
        		jComboBoxGestor.setSelectedIndex(0);
        	}
            
            if (elemento.getCapacidad() != null){
        		jTextFieldCapac.setText(elemento.getCapacidad().toString());
        	}
        	else{
        		jTextFieldCapac.setText("");
        	}
            
            if (elemento.getEstado() != null){
        		jComboBoxEst.setSelectedPatron(elemento.getEstado());
        	}
        	else{
        		jComboBoxEst.setSelectedIndex(0);
        	}
            
            if (elemento.getProteccion() != null){
            	jComboBoxProteccion.setSelectedPatron(elemento.getProteccion());
        	}
        	else{
        		jComboBoxProteccion.setSelectedIndex(0);
        	}
            
            if (elemento.getFechaLimpieza() != null){
        		jTextFieldFechaLimp.setText(elemento.getFechaLimpieza().toString());
        	}
        	else{
        		jTextFieldFechaLimp.setText("");
        	}
            
            if (elemento.getContador() != null){
            	jComboBoxContador.setSelectedPatron(elemento.getContador().toString());
        	}
        	else{
        		jComboBoxContador.setSelectedIndex(0);
        	}
            
            if (elemento.getFechaInstalacion() != null){
            	
        		jTextFieldFechaInst.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
            
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(elemento.getFechaRevision().toString());
        	}
        	else{
        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
                jTextFieldFechaRev.setText(datetime);
        	}
            
            if (elemento.getEstadoRevision() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
            
            if (elemento.getEpigInventario() != null){
            	jComboBoxEpigInventario.setSelectedPatron(elemento.getEpigInventario().toString());
        	}
        	else{
        		jComboBoxEpigInventario.setSelectedIndex(0);
        	}

            if (elemento.getIdBien() != null && elemento.getIdBien() != 0){
            	jComboBoxNumInventario.setSelectedItem(elemento.getIdBien());
        	}
        	else{
        		jComboBoxNumInventario.setSelectedIndex(0);
        	}
            
            if (elemento.getTitularidadMunicipal() != null){
            	jComboBoxTitularidadMunicipal.setSelectedPatron(elemento.getTitularidadMunicipal());
        	}
        	else{
        		jComboBoxTitularidadMunicipal.setSelectedIndex(0);
        	}
            
        }
    }
    
	public DepositosEIEL getDepositoData (){

		DepositosEIEL elemento = new DepositosEIEL();

		/* Claves: COMBOBOX  y JTEXT */
		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		elemento.setClave(jTextFieldClave.getText());
		elemento.setOrdenDeposito(jTextFieldOrden.getText());
		elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));

		/* JTEXT - Integer */
		if (jTextFieldCapac.getText()!=null && !jTextFieldCapac.getText().equals("")){
			elemento.setCapacidad(new Integer(jTextFieldCapac.getText()));
		} else if (jTextFieldCapac.getText().equals("")){
			elemento.setCapacidad(new Integer(0));
		}
		if (jComboBoxEstado.getSelectedPatron()!=null)
			elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));

		/* JTEXT - String */
		if (jTextFieldUbic.getSelectedPatron() != null) {
			elemento.setUbicacion((String)jTextFieldUbic.getSelectedPatron());
		} else elemento.setUbicacion("");
		if (jComboBoxTitular.getSelectedPatron()!=null)
			elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());
		else elemento.setTitularidad("");
		if (jComboBoxGestor.getSelectedPatron()!=null)
			elemento.setGestor((String) jComboBoxGestor.getSelectedPatron());
		else elemento.setGestor("");
		if (jComboBoxEst.getSelectedPatron()!=null)
			elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
		else elemento.setEstado("");
		if (jComboBoxProteccion.getSelectedPatron()!=null)
			elemento.setProteccion((String) jComboBoxProteccion.getSelectedPatron());
		else elemento.setProteccion("");

		if (jTextFieldFechaLimp.getText()!=null){
			elemento.setFechaLimpieza(jTextFieldFechaLimp.getText());
		}

		if (jComboBoxContador.getSelectedPatron()!=null)
			elemento.setContador((String) jComboBoxContador.getSelectedPatron());
		else elemento.setContador("");

		if (jTextFieldObserv.getText()!=null){
			elemento.setObservaciones(jTextFieldObserv.getText());
		}
		/* JTEXT - Date */
		if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){
			java.util.Date fecha = jTextFieldFechaInst.getDate();
			elemento.setFechaInstalacion(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));
		} else {
			elemento.setFechaInstalacion(null);
		}
		

		if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI)){
			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
			if (jComboBoxEpigInventario.getSelectedPatron()!=null){
				elemento.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
				elemento.setIdBien(((InventarioEIEL) jComboBoxNumInventario.getSelectedItem()).getIdBien());
			}
			else{ 
				elemento.setEpigInventario(0);
				elemento.setIdBien(0);
			}
		}
		else{
			// Si la titularidad Municipal está en blanco significa que es un bien no inventariable
			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
			elemento.setEpigInventario(0);
			elemento.setIdBien(0);
		}

		try{
			if (jTextFieldFechaRev.getText()!=null && !jTextFieldFechaRev.getText().equals("")){
				java.util.Date fecha = new java.util.Date(jTextFieldFechaRev.getText());
				elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDay()));
			} else if (jTextFieldFechaRev.getText().equals("")){
				elemento.setFechaRevision(null);
			}
		} catch (IllegalArgumentException e) {
			elemento.setFechaRevision(null);
		}
		return elemento;
	}

	
    public DepositosEIEL getDepositos (DepositosEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new DepositosEIEL();
            }
            /* Claves: COMBOBOX  y JTEXT */
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());
			elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
					.getSelectedItem()).getIdIne());
			elemento.setClave(jTextFieldClave.getText());
            elemento.setOrdenDeposito(jTextFieldOrden.getText());
            
            elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));

            
            /* JTEXT - Integer */
            if (jTextFieldCapac.getText()!=null && !jTextFieldCapac.getText().equals("")){
            	elemento.setCapacidad(new Integer(jTextFieldCapac.getText()));
            } else if (jTextFieldCapac.getText().equals("")){
            	elemento.setCapacidad(new Integer(0));
            }
            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
            /* JTEXT - String */
            if (jTextFieldUbic.getSelectedPatron() != null) {
            	elemento.setUbicacion((String)jTextFieldUbic.getSelectedPatron());
        	} else elemento.setUbicacion("");
            if (jComboBoxTitular.getSelectedPatron()!=null)
            	elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());
            else elemento.setTitularidad("");
            if (jComboBoxGestor.getSelectedPatron()!=null)
            	elemento.setGestor((String) jComboBoxGestor.getSelectedPatron());
            else elemento.setGestor("");
            if (jComboBoxEst.getSelectedPatron()!=null)
            	elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
            else elemento.setEstado("");
            if (jComboBoxProteccion.getSelectedPatron()!=null)
            	elemento.setProteccion((String) jComboBoxProteccion.getSelectedPatron());
            else elemento.setProteccion("");
            
            if (jTextFieldFechaLimp.getText()!=null){
            	elemento.setFechaLimpieza(jTextFieldFechaLimp.getText());
            }
            
            if (jComboBoxContador.getSelectedPatron()!=null)
            	elemento.setContador((String) jComboBoxContador.getSelectedPatron());
            else elemento.setContador("");
            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            /* JTEXT - Date */
            if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){
            	java.util.Date fecha = jTextFieldFechaInst.getDate();
            	elemento.setFechaInstalacion(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));
            } else {
            	elemento.setFechaInstalacion(null);
            }
           
            if (jTextFieldFechaRev.getText()!=null && !jTextFieldFechaRev.getText().equals("")){
            	String fechas=jTextFieldFechaRev.getText();
            	String anio=fechas.substring(0,4);
            	String mes=fechas.substring(5,7);
            	String dia=fechas.substring(8,10);
            	
            	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
            	elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
            }  
            else{
            	elemento.setFechaRevision(null);
            }
            
            if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI)){
    			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
    			if (jComboBoxEpigInventario.getSelectedPatron()!=null){
    				elemento.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
    				elemento.setIdBien(((InventarioEIEL) jComboBoxNumInventario.getSelectedItem()).getIdBien());
    			}
    			else{ 
    				elemento.setEpigInventario(0);
    				elemento.setIdBien(0);
    			}
    		}
    		else{
    			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
    			elemento.setEpigInventario(0);
    			elemento.setIdBien(0);
    		}
            
            
        }	
        return elemento;
    }
    
    private void initialize(){      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.depositos.panel.title"));
        this.setLayout(new GridBagLayout());
        this.setSize(DepositosDialog.DIM_X,DepositosDialog.DIM_Y);
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosInventario(),  new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosRevision(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));


        EdicionOperations oper = new EdicionOperations();

		// Inicializa los desplegables
		if (Identificadores.get("ListaProvincias") == null) {
			ArrayList lst = oper.obtenerProvincias();
			Identificadores.put("ListaProvincias", lst);
			EdicionUtils.cargarLista(getJComboBoxProvincia(),
					oper.obtenerProvinciasConNombre());
			Provincia p = new Provincia();
			p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
			p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
			getJComboBoxProvincia().setSelectedItem(p);
		} else {
			EdicionUtils.cargarLista(getJComboBoxProvincia(),
					(ArrayList) Identificadores.get("ListaProvincias"));
			 EdicionUtils.cargarLista(getJComboBoxProvincia(),
			 oper.obtenerProvinciasConNombre());
		}
		loadData();
    }
    
/* Metodos que devuelven CAMPOS CLAVE */
    
    public JTextField getJTextFieldClave(){
        if (jTextFieldClave == null){
            jTextFieldClave = new TextField(2);
        }
        return jTextFieldClave;
    }
    
    public JComboBox getJComboBoxProvincia(){
    	if (jComboBoxProvincia == null) {
			EdicionOperations oper = new EdicionOperations();
			ArrayList<Provincia> listaProvincias = oper
					.obtenerProvinciasConNombre();
			jComboBoxProvincia = new JComboBox(listaProvincias.toArray());
			jComboBoxProvincia
					.setSelectedIndex(this
							.provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
			jComboBoxProvincia.setRenderer(new UbicacionListCellRenderer());
			jComboBoxProvincia.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getJComboBoxMunicipio() != null) {
						if (jComboBoxProvincia.getSelectedIndex() == 0) {
							jComboBoxMunicipio.removeAllItems();
							Municipio municipio = new Municipio();
							municipio.setIdIne("");
							municipio.setNombreOficial("");
							jComboBoxMunicipio.addItem(municipio);
						} else {
							EdicionOperations oper = new EdicionOperations();
							jComboBoxProvincia
									.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));

							if (jComboBoxProvincia.getSelectedItem() != null) {
								EdicionUtils.cargarLista(
										getJComboBoxMunicipio(),
										oper.obtenerTodosMunicipios(((Provincia) jComboBoxProvincia
												.getSelectedItem())
												.getIdProvincia()));
								jComboBoxMunicipio
										.setSelectedIndex(municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio));
							}
						}
					}

				}
			});

		}

		return jComboBoxProvincia;
    }
    
    public JComboBox getJComboBoxMunicipio(){
    	if (jComboBoxMunicipio == null) {
			jComboBoxMunicipio = new JComboBox();
			jComboBoxMunicipio.setRenderer(new UbicacionListCellRenderer());
			jComboBoxMunicipio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (jComboBoxMunicipio.getSelectedIndex() != 0) {
						MunicipioEIEL municipio = new MunicipioEIEL();
						if (jComboBoxProvincia.getSelectedItem() != null) {
							municipio
									.setCodProvincia(((Provincia) jComboBoxProvincia
											.getSelectedItem())
											.getIdProvincia());
						}
						if (jComboBoxMunicipio.getSelectedItem() != null) {
							municipio
									.setCodMunicipio(((Municipio) jComboBoxMunicipio
											.getSelectedItem()).getIdIne());
						}

					}
				}
			});
		}
		return jComboBoxMunicipio;
    }
   
    private JTextField getJTextFieldOrden(){
        if (jTextFieldOrden == null){
        	jTextFieldOrden = new TextField(3);
        }
        return jTextFieldOrden;
    }
    
    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private ComboBoxEstructuras getJTextFieldUbic(){
    	
    	if (jTextFieldUbic == null){
    		
    		Estructuras.cargarEstructura("eiel_Ubicacion Deposito");
    		jTextFieldUbic = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
    		jTextFieldUbic.setPreferredSize(new Dimension(100, 20));

    	}
    	return jTextFieldUbic;
    }
    
    private ComboBoxEstructuras getJComboBoxTitular()
    { 
        if (jComboBoxTitular == null)
        {
            Estructuras.cargarEstructura("eiel_Titularidad");
            jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTitular;        
    }
    
    private ComboBoxEstructuras getJComboBoxGestor()
    { 
        if (jComboBoxGestor == null)
        {
            Estructuras.cargarEstructura("eiel_Gestión");
            jComboBoxGestor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGestor;        
    }
    
    private JTextField getJTextFieldCapac(){
    	if (jTextFieldCapac == null){
    		jTextFieldCapac = new TextField(10);
    		jTextFieldCapac.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCapac, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCapac;
    }
    
    private ComboBoxEstructuras getJComboBoxEst()
    { 
        if (jComboBoxEst == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEst = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEst.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEst;        
    }
    
    private ComboBoxEstructuras getJComboBoxProteccion()
    { 
        if (jComboBoxProteccion == null)
        {
            Estructuras.cargarEstructura("eiel_Proteccion DE");
            jComboBoxProteccion = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxProteccion.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxProteccion;        
    }
    
    private JTextField getJTextFieldFechaLimp(){
    	if (jTextFieldFechaLimp == null){
    		jTextFieldFechaLimp = new TextField(4);
    		jTextFieldFechaLimp.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldFechaLimp, 4, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldFechaLimp;
    }
    
    private ComboBoxEstructuras getJComboBoxContador()
    { 
        if (jComboBoxContador == null)
        {
            Estructuras.cargarEstructura("eiel_Contador DE");
            jComboBoxContador = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxContador.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxContador;        
    }
    
    private DateField getJTextFieldFechaInst(){
    	if (jTextFieldFechaInst == null){
    		jTextFieldFechaInst = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaInst.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaInst.setEditable(true);
    	}
    	return jTextFieldFechaInst;
    }
    
    private JTextField getJTextFieldObserv(){
    	if (jTextFieldObserv == null){
    		jTextFieldObserv  = new TextField(50);
    		jTextFieldObserv.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObserv;
    }

    private JTextField getJTextFieldFechaRev(){
    	if (jTextFieldFechaRev == null){
    		jTextFieldFechaRev = new TextField();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);            
            jTextFieldFechaRev.setText(datetime);
    	}
    	return jTextFieldFechaRev;
    }

    private ComboBoxEstructuras getJComboBoxEstado()
    { 
        if (jComboBoxEstado == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            jComboBoxEstado = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado;        
    }
    
    
    public DepositosPanel(GridBagLayout layout){
        super(layout);
        initialize();
    }
       
    public String validateInput(){
        return null; 
    }
    
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getJPanelDatosIdentificacion(){
        if (jPanelIdentificacion == null){   
        	jPanelIdentificacion = new JPanel(new GridBagLayout());
            jPanelIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identity"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            /* Definicion de LABEL'S */
            jLabelClave = new JLabel("", JLabel.CENTER); 
            jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 
            jLabelOrden  = new JLabel("", JLabel.CENTER);
            jLabelOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            /* Agregamos las Labels al JPANELIDENTIFICATION */
            jPanelIdentificacion.add(jLabelClave,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(getJTextFieldClave(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(jLabelCodProv, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentificacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(jLabelCodMunic, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentificacion.add(getJComboBoxMunicipio(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentificacion.add(jLabelOrden, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(getJTextFieldOrden(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            for (int i=0; i < jPanelIdentificacion.getComponentCount(); i++){
            	jPanelIdentificacion.getComponent(i).setEnabled(false);
            }
            
        }
        return jPanelIdentificacion;
    }
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosInformacion(){
        if (jPanelInformacion == null){   
        	jPanelInformacion  = new JPanel(new GridBagLayout());
        	jPanelInformacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.info"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	/* Definicion de LABEL'S */
            jLabelUbic = new JLabel("", JLabel.CENTER); 
            jLabelUbic.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ubicacion_de")); 
            jLabelTit = new JLabel("", JLabel.CENTER); 
            jLabelTit.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular")); 
            jLabelGest = new JLabel("", JLabel.CENTER); 
            jLabelGest.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestor")); 
            jLabelCapac = new JLabel("", JLabel.CENTER);
            jLabelCapac.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capacidad"));
            jLabelEst = new JLabel("", JLabel.CENTER);
            jLabelEst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado_conservacion"));
            jLabelProt = new JLabel("", JLabel.CENTER);
            jLabelProt.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.proteccion"));
            jLabelFechaLimp = new JLabel("", JLabel.CENTER);
            jLabelFechaLimp.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechalimp"));
            jLabelCont = new JLabel("", JLabel.CENTER);
            jLabelCont.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.contador"));
            jLabelFechaInst = new JLabel("", JLabel.CENTER);
            jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechainst"));
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelUbic,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldUbic(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelTit,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxTitular(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelGest,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxGestor(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCapac,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldCapac(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelEst,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxEst(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelProt,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxProteccion(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelFechaLimp,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldFechaLimp(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCont,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxContador(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelFechaInst,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldFechaInst(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 5, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
        }
        return jPanelInformacion;
    }
    
    
    /**
     * This method initializes jPanelDatosRevision	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosRevision(){
        if (jPanelRevision == null){   
        	jPanelRevision = new JPanel(new GridBagLayout());
        	jPanelRevision.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.revision"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jLabelFechaRevi = new JLabel("", JLabel.CENTER); 
            jLabelFechaRevi.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 
            jLabelEstado = new JLabel("", JLabel.CENTER); 
            jLabelEstado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 
            jPanelRevision.add(jLabelFechaRevi,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevision.add(getJTextFieldFechaRev(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 50, 0));
            jPanelRevision.add(jLabelEstado, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevision.add(getJComboBoxEstado(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 120, 0));
        }
        return jPanelRevision;
    }
           

    public void okPressed(){
        okPressed = true;
    }
    
    public boolean getOkPressed(){
        return okPressed;
    }

    public boolean datosMinimosYCorrectos(){
        return  (jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
        (jTextFieldOrden.getText()!=null && !jTextFieldOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0);
    }

	public void enter() {
		loadData();		
		loadDataIdentificacion();
	}

	public void exit() {
		
	}
	
	
	public void loadDataIdentificacion(){

		Object obj = AppContext.getApplicationContext().getBlackboard().get("featureDialog");
		if (obj != null && obj instanceof FeatureDialog){
			FeatureDialog featureDialog = (FeatureDialog) obj;
			Feature feature = featureDialog.get_fieldPanel().getModifiedFeature();
										
				GeopistaSchema esquema = (GeopistaSchema)feature.getSchema();
				feature.getAttribute(esquema.getAttributeByColumn("id"));
							
				String clave = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("clave"))!=null){
	        		clave=(feature.getAttribute(esquema.getAttributeByColumn("clave"))).toString();
	        	}
	        	
	        	String codprov = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
	        		codprov=(feature.getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
	        	}
	        	
	        	String codmunic = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
	        		codmunic=(feature.getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
	        	}

	        	
	        	String orden_de = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_de"))!=null){
	        		orden_de=(feature.getAttribute(esquema.getAttributeByColumn("orden_de"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelDepositoEIEL(clave, codprov, codmunic, orden_de));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_de);
			}
		}
	
	


	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_de) {
		
		//Datos identificacion
    	if (clave != null){
    		jTextFieldClave.setText(clave);
    	}
    	else{
    		jTextFieldClave.setText("");
    	}
        
    	if (codprov != null) {
			// jComboBoxProvincia.setSelectedItem(codprov);
			jComboBoxProvincia
					.setSelectedIndex(provinciaIndexSeleccionar(codprov));
		} else {
			jComboBoxProvincia.setSelectedIndex(-1);
		}

		if (codmunic != null) {
			jComboBoxMunicipio
					.setSelectedIndex(municipioIndexSeleccionar(codmunic));
		} else {
			jComboBoxMunicipio.setSelectedIndex(-1);
		}
                
        if (orden_de != null){
    		jTextFieldOrden.setText(orden_de);
    	}
    	else{
    		jTextFieldOrden.setText("");
    	}
		
	}
	
	public int provinciaIndexSeleccionar(String provinciaId) {
		for (int i = 0; i < jComboBoxProvincia.getItemCount(); i++) {
			if ((!jComboBoxProvincia.getItemAt(i).equals(""))
					&& (jComboBoxProvincia.getItemAt(i) instanceof Provincia)
					&& ((Provincia) jComboBoxProvincia.getItemAt(i))
							.getIdProvincia().equals(provinciaId)) {
				return i;
			}
		}

		return -1;
	}

	public int municipioIndexSeleccionar(String municipioId) {
		if (!municipioId.equals("")) {
			for (int i = 0; i < jComboBoxMunicipio.getItemCount(); i++) {
				try {
					if (((Municipio) jComboBoxMunicipio.getItemAt(i))
							.getIdIne().equals(municipioId.substring(2, 5))) {
						// jComboBoxMunicipioNucleo.setEnabled(false);
						return i;
					}
				} catch (StringIndexOutOfBoundsException e) {
					if (((Municipio) jComboBoxMunicipio.getItemAt(i))
							.getIdIne().equals(municipioId)) {
						// jComboBoxMunicipio.setEnabled(false);
						return i;
					}
				}
			}
		}

		return -1;
	}
    
}
