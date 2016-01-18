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

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.Depuradoras1Dialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.protocol.inventario.Inventario;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class Depuradoras1Panel extends InventarioPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    private JPanel jPanelRevision = null;
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelClave = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldOrden = null;
	private JLabel jLabelTratPr1 = null;
	private JLabel jLabelTratPr2 = null;
	private JLabel jLabelTratPr3 = null;
	private JLabel jLabelTratSc1 = null;
	private JLabel jLabelTratSc2 = null;
	private JLabel jLabelTratSc3 = null;
	private JLabel jLabelTratAv1 = null;
	private JLabel jLabelTartAv2 = null;
	private JLabel jLabelTratAv3 = null;
	private ComboBoxEstructuras jComboBoxTratPr1 = null;
	private ComboBoxEstructuras jComboBoxTratPr2 = null;
	private ComboBoxEstructuras jComboBoxTratPr3 = null;
	private ComboBoxEstructuras jComboBoxTratSc1 = null;
	private ComboBoxEstructuras jComboBoxTratSc2 = null;
	private ComboBoxEstructuras jComboBoxTratSc3 = null;
	private ComboBoxEstructuras jComboBoxTratAv1 = null;
	private ComboBoxEstructuras jComboBoxTratAv2 = null;
	private ComboBoxEstructuras jComboBoxTratAv3 = null;
	private JTextField jTextFieldFecha = null;	
	private JTextField jTextFieldClave = null;
	private JComboBox jComboBoxProvincia = null;
	private JComboBox jComboBoxMunicipio = null;
	private JLabel jLabelFecha = null;
	private JLabel jLabelProcCm1 = null;
	private JLabel jLabelProcCm2 = null;
	private JLabel jLabelProcCm3 = null;
	private JLabel jLabelTratLd1 = null;
	private JLabel jLabelTratLd2 = null;
	private JLabel jLabelTratLd3 = null;
	private ComboBoxEstructuras jComboBoxProcCm1 = null;
	private ComboBoxEstructuras jComboBoxProcCm2 = null;
	private ComboBoxEstructuras jComboBoxProcCm3 = null;
	private ComboBoxEstructuras jComboBoxTratLd1 = null;
	private ComboBoxEstructuras jComboBoxTratLd2 = null;
	private ComboBoxEstructuras jComboBoxTratLd3 = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private JLabel jLabelEstado;
	
	
    /**
     * This method initializes
     * 
     */
    public Depuradoras1Panel()
    {
        super();
        initialize();
    }
    
    public Depuradoras1Panel(Depuradora1EIEL elemento)
    {
        super();
        initialize();
        loadData ();
    }
    
    public void loadData(Depuradora1EIEL panelDepuradora1EIEL) {
    	    	
    	if (panelDepuradora1EIEL != null){
            //Datos identificacion
	        if (panelDepuradora1EIEL!=null){
	        	if (panelDepuradora1EIEL.getClave() != null){
	        		jTextFieldClave.setText(panelDepuradora1EIEL.getClave());
	        	}
	        	else{
	        		jTextFieldClave.setText("");
	        	}            
	        	if (panelDepuradora1EIEL.getCodINEProvincia() != null) {
					jComboBoxProvincia
							.setSelectedIndex(provinciaIndexSeleccionar(panelDepuradora1EIEL
									.getCodINEProvincia()));
				} else {
					jComboBoxProvincia.setSelectedIndex(0);
				}

				if (panelDepuradora1EIEL.getCodINEMunicipio() != null) {
					jComboBoxMunicipio
							.setSelectedIndex(municipioIndexSeleccionar(panelDepuradora1EIEL
									.getCodINEMunicipio()));
				} else {
					jComboBoxMunicipio.setSelectedIndex(0);
				}      
	            if (panelDepuradora1EIEL.getCodOrden() != null){
	        		jTextFieldOrden.setText(panelDepuradora1EIEL.getCodOrden());
	        	}
	        	else{
	        		jTextFieldOrden.setText("");
	        	}
	            
	            if (panelDepuradora1EIEL.getTratPrimario1() != null){
	            	jComboBoxTratPr1.setSelectedPatron(panelDepuradora1EIEL.getTratPrimario1());
	        	}
	        	else{
	        		jComboBoxTratPr1.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratPrimario2() != null){
	            	jComboBoxTratPr2.setSelectedPatron(panelDepuradora1EIEL.getTratPrimario2());
	        	}
	        	else{
	        		jComboBoxTratPr2.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratPrimario3() != null){
	            	jComboBoxTratPr3.setSelectedPatron(panelDepuradora1EIEL.getTratPrimario3());
	        	}
	        	else{
	        		jComboBoxTratPr3.setSelectedIndex(0);
	        	}
	            
	            if (panelDepuradora1EIEL.getTratSecundario1() != null){
	            	jComboBoxTratSc1.setSelectedPatron(panelDepuradora1EIEL.getTratSecundario1());
	        	}
	        	else{
	        		jComboBoxTratSc1.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratSecundario2() != null){
	            	jComboBoxTratSc2.setSelectedPatron(panelDepuradora1EIEL.getTratSecundario2());
	        	}
	        	else{
	        		jComboBoxTratSc2.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratSecundario3() != null){
	            	jComboBoxTratSc3.setSelectedPatron(panelDepuradora1EIEL.getTratSecundario3());
	        	}
	        	else{
	        		jComboBoxTratSc3.setSelectedIndex(0);
	        	}
	
	            if (panelDepuradora1EIEL.getTratAvanzado1() != null){
	            	jComboBoxTratAv1.setSelectedPatron(panelDepuradora1EIEL.getTratAvanzado1());
	        	}
	        	else{
	        		jComboBoxTratAv1.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratAvanzado2() != null){
	            	jComboBoxTratAv2.setSelectedPatron(panelDepuradora1EIEL.getTratAvanzado2());
	        	}
	        	else{
	        		jComboBoxTratAv2.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratAvanzado3() != null){
	            	jComboBoxTratAv3.setSelectedPatron(panelDepuradora1EIEL.getTratAvanzado3());
	        	}
	        	else{
	        		jComboBoxTratAv3.setSelectedIndex(0);
	        	}
	            
	            if (panelDepuradora1EIEL.getProcComplementario1() != null){
	            	jComboBoxProcCm1.setSelectedPatron(panelDepuradora1EIEL.getProcComplementario1());
	        	}
	        	else{
	        		jComboBoxProcCm1.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getProcComplementario2() != null){
	            	jComboBoxProcCm2.setSelectedPatron(panelDepuradora1EIEL.getProcComplementario2());
	        	}
	        	else{
	        		jComboBoxProcCm2.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getProcComplementario3() != null){
	            	jComboBoxProcCm3.setSelectedPatron(panelDepuradora1EIEL.getProcComplementario3());
	        	}
	        	else{
	        		jComboBoxProcCm3.setSelectedIndex(0);
	        	}
	            
	            if (panelDepuradora1EIEL.getTratLodos1() != null){
	            	jComboBoxTratLd1.setSelectedPatron(panelDepuradora1EIEL.getTratLodos1());
	        	}
	        	else{
	        		jComboBoxTratLd1.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratLodos2() != null){
	            	jComboBoxTratLd2.setSelectedPatron(panelDepuradora1EIEL.getTratLodos2());
	        	}
	        	else{
	        		jComboBoxTratLd2.setSelectedIndex(0);
	        	}
	            if (panelDepuradora1EIEL.getTratLodos3() != null){
	            	jComboBoxTratLd3.setSelectedPatron(panelDepuradora1EIEL.getTratLodos3());
	        	}
	        	else{
	        		jComboBoxTratLd3.setSelectedIndex(0);
	        	}
	            
	            if (panelDepuradora1EIEL.getFechaRevision() != null && panelDepuradora1EIEL.getFechaRevision().equals( new java.util.Date()) ){
	        		jTextFieldFecha.setText(panelDepuradora1EIEL.getFechaRevision().toString());
	        	}
	        	else{
	        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                java.util.Date date = new java.util.Date();
	                String datetime = dateFormat.format(date);
	                
	        		jTextFieldFecha.setText(datetime);
	        	}
	            
	            if (panelDepuradora1EIEL.getEstadoRevision() != null){
	            	jComboBoxEstado.setSelectedPatron(panelDepuradora1EIEL.getEstadoRevision().toString());
	        	}
	        	else{
	        		jComboBoxEstado.setSelectedIndex(0);
	        	}

	            if (panelDepuradora1EIEL.getTitularidadMunicipal() != null){
	            	jComboBoxTitularidadMunicipal.setSelectedPatron(panelDepuradora1EIEL.getTitularidadMunicipal());
	        	}
	        	else{
	        		jComboBoxTitularidadMunicipal.setSelectedIndex(0);
	        	}
	            
	            if (panelDepuradora1EIEL.getEpigInventario() != null){
	            	jComboBoxEpigInventario.setSelectedPatron(panelDepuradora1EIEL.getEpigInventario().toString());
	        	}
	        	else{
	        		if (jComboBoxEpigInventario.isEnabled())
	        			jComboBoxEpigInventario.setSelectedIndex(0);
	        	}
	            
	            if (panelDepuradora1EIEL.getIdBien() != null && panelDepuradora1EIEL.getIdBien() != 0){
	            	jComboBoxNumInventario.setEnabled(true);
					EdicionOperations oper = new EdicionOperations();          			
					EdicionUtils.cargarLista(getJComboBoxNumInventario(), oper.obtenerNumInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron())));
	            	jComboBoxNumInventario.setSelectedItem(panelDepuradora1EIEL.getIdBien());
	            }
	            else{
	            	if (jComboBoxNumInventario.isEnabled())
	            		jComboBoxNumInventario.setSelectedIndex(0);
	            }
	        }
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.DEPURADORAS1_CLAVE);
        	        	       	
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
    
    
    
    public void loadData()
    {
    	Object object = AppContext.getApplicationContext().getBlackboard().get("depuradora1_panel");    	
    	if (object != null && object instanceof Depuradora1EIEL){    		
    		Depuradora1EIEL elemento = (Depuradora1EIEL)object;
            //Datos identificacion
	        if (elemento!=null){
	        	if (elemento.getClave() != null){
	        		jTextFieldClave.setText(elemento.getClave());
	        	}
	        	else{
	        		jTextFieldClave.setText("");
	        	}            
	        	if (elemento.getCodINEProvincia() != null) {
					jComboBoxProvincia
							.setSelectedIndex(provinciaIndexSeleccionar(elemento
									.getCodINEProvincia()));
				} else {
					jComboBoxProvincia.setSelectedIndex(0);
				}

				if (elemento.getCodINEMunicipio() != null) {
					jComboBoxMunicipio
							.setSelectedIndex(municipioIndexSeleccionar(elemento
									.getCodINEMunicipio()));
				} else {
					jComboBoxMunicipio.setSelectedIndex(0);
				}    
	            if (elemento.getCodOrden() != null){
	        		jTextFieldOrden.setText(elemento.getCodOrden());
	        	}
	        	else{
	        		jTextFieldOrden.setText("");
	        	}
	            
	            if (elemento.getTratPrimario1() != null){
	            	jComboBoxTratPr1.setSelectedPatron(elemento.getTratPrimario1());
	        	}
	        	else{
	        		jComboBoxTratPr1.setSelectedIndex(0);
	        	}
	            if (elemento.getTratPrimario2() != null){
	            	jComboBoxTratPr2.setSelectedPatron(elemento.getTratPrimario2());
	        	}
	        	else{
	        		jComboBoxTratPr2.setSelectedIndex(0);
	        	}
	            if (elemento.getTratPrimario3() != null){
	            	jComboBoxTratPr3.setSelectedPatron(elemento.getTratPrimario3());
	        	}
	        	else{
	        		jComboBoxTratPr3.setSelectedIndex(0);
	        	}
	            
	            if (elemento.getTratSecundario1() != null){
	            	jComboBoxTratSc1.setSelectedPatron(elemento.getTratSecundario1());
	        	}
	        	else{
	        		jComboBoxTratSc1.setSelectedIndex(0);
	        	}
	            if (elemento.getTratSecundario2() != null){
	            	jComboBoxTratSc2.setSelectedPatron(elemento.getTratSecundario2());
	        	}
	        	else{
	        		jComboBoxTratSc2.setSelectedIndex(0);
	        	}
	            if (elemento.getTratSecundario3() != null){
	            	jComboBoxTratSc3.setSelectedPatron(elemento.getTratSecundario3());
	        	}
	        	else{
	        		jComboBoxTratSc3.setSelectedIndex(0);
	        	}
	
	            if (elemento.getTratAvanzado1() != null){
	            	jComboBoxTratAv1.setSelectedPatron(elemento.getTratAvanzado1());
	        	}
	        	else{
	        		jComboBoxTratAv1.setSelectedIndex(0);
	        	}
	            if (elemento.getTratAvanzado2() != null){
	            	jComboBoxTratAv2.setSelectedPatron(elemento.getTratAvanzado2());
	        	}
	        	else{
	        		jComboBoxTratAv2.setSelectedIndex(0);
	        	}
	            if (elemento.getTratAvanzado3() != null){
	            	jComboBoxTratAv3.setSelectedPatron(elemento.getTratAvanzado3());
	        	}
	        	else{
	        		jComboBoxTratAv3.setSelectedIndex(0);
	        	}
	            
	            if (elemento.getProcComplementario1() != null){
	            	jComboBoxProcCm1.setSelectedPatron(elemento.getProcComplementario1());
	        	}
	        	else{
	        		jComboBoxProcCm1.setSelectedIndex(0);
	        	}
	            if (elemento.getProcComplementario2() != null){
	            	jComboBoxProcCm2.setSelectedPatron(elemento.getProcComplementario2());
	        	}
	        	else{
	        		jComboBoxProcCm2.setSelectedIndex(0);
	        	}
	            if (elemento.getProcComplementario3() != null){
	            	jComboBoxProcCm3.setSelectedPatron(elemento.getProcComplementario3());
	        	}
	        	else{
	        		jComboBoxProcCm3.setSelectedIndex(0);
	        	}
	            
	            if (elemento.getTratLodos1() != null){
	            	jComboBoxTratLd1.setSelectedPatron(elemento.getTratLodos1());
	        	}
	        	else{
	        		jComboBoxTratLd1.setSelectedIndex(0);
	        	}
	            if (elemento.getTratLodos2() != null){
	            	jComboBoxTratLd2.setSelectedPatron(elemento.getTratLodos2());
	        	}
	        	else{
	        		jComboBoxTratLd2.setSelectedIndex(0);
	        	}
	            if (elemento.getTratLodos3() != null){
	            	jComboBoxTratLd3.setSelectedPatron(elemento.getTratLodos3());
	        	}
	        	else{
	        		jComboBoxTratLd3.setSelectedIndex(0);
	        	}
	            
	            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
	        		jTextFieldFecha.setText(elemento.getFechaRevision().toString());
	        	}
	        	else{
	        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date = new java.util.Date();
                    String datetime = dateFormat.format(date);
                    
	        		jTextFieldFecha.setText(datetime);
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

	            if (elemento.getIdBien() != null && elemento.getIdBien() != 0)
	            	jComboBoxNumInventario.setSelectedItem(elemento.getIdBien());
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
    }
    
    
    public Depuradora1EIEL getDepuradora1Data ()
    {    
    	Depuradora1EIEL	elemento = new Depuradora1EIEL();

    	elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));

    	if (jTextFieldClave.getText()!=null){
    		elemento.setClave(jTextFieldClave.getText());
    	}

    	if (jTextFieldOrden.getText()!=null){
    		elemento.setCodOrden(jTextFieldOrden.getText());
    	}

    	if (jComboBoxTratPr1.getSelectedPatron()!=null)
    		elemento.setTratPrimario1((String) jComboBoxTratPr1.getSelectedPatron());
    	else elemento.setTratPrimario1("");
    	if (jComboBoxTratPr2.getSelectedPatron()!=null)
    		elemento.setTratPrimario2((String) jComboBoxTratPr2.getSelectedPatron());
    	else elemento.setTratPrimario2("");
    	if (jComboBoxTratPr3.getSelectedPatron()!=null)
    		elemento.setTratPrimario3((String) jComboBoxTratPr3.getSelectedPatron());
    	else elemento.setTratPrimario3("");

    	if (jComboBoxTratSc1.getSelectedPatron()!=null)
    		elemento.setTratSecundario1((String) jComboBoxTratSc1.getSelectedPatron());
    	else elemento.setTratSecundario1("");
    	if (jComboBoxTratSc2.getSelectedPatron()!=null)
    		elemento.setTratSecundario2((String) jComboBoxTratSc2.getSelectedPatron());
    	else elemento.setTratSecundario2("");
    	if (jComboBoxTratSc3.getSelectedPatron()!=null)
    		elemento.setTratSecundario3((String) jComboBoxTratSc3.getSelectedPatron());
    	else elemento.setTratSecundario3("");

    	if (jComboBoxTratAv1.getSelectedPatron()!=null)
    		elemento.setTratAvanzado1((String) jComboBoxTratAv1.getSelectedPatron());
    	else elemento.setTratAvanzado1("");
    	if (jComboBoxTratAv2.getSelectedPatron()!=null)
    		elemento.setTratAvanzado2((String) jComboBoxTratAv2.getSelectedPatron());
    	else elemento.setTratAvanzado2("");
    	if (jComboBoxTratAv3.getSelectedPatron()!=null)
    		elemento.setTratAvanzado3((String) jComboBoxTratAv3.getSelectedPatron());
    	else elemento.setTratAvanzado3("");

    	if (jComboBoxProcCm1.getSelectedPatron()!=null)
    		elemento.setProcComplementario1((String) jComboBoxProcCm1.getSelectedPatron());
    	else elemento.setProcComplementario1("");
    	if (jComboBoxProcCm2.getSelectedPatron()!=null)
    		elemento.setProcComplementario2((String) jComboBoxProcCm2.getSelectedPatron());
    	else elemento.setProcComplementario2("");
    	if (jComboBoxProcCm3.getSelectedPatron()!=null)
    		elemento.setProcComplementario3((String) jComboBoxProcCm3.getSelectedPatron());
    	else elemento.setProcComplementario3("");

    	if (jComboBoxTratLd1.getSelectedPatron()!=null)
    		elemento.setTratLodos1((String) jComboBoxTratLd1.getSelectedPatron());
    	else elemento.setTratLodos1("");
    	if (jComboBoxTratLd2.getSelectedPatron()!=null)
    		elemento.setTratLodos2((String) jComboBoxTratLd2.getSelectedPatron());
    	else elemento.setTratLodos2("");
    	if (jComboBoxTratLd3.getSelectedPatron()!=null)
    		elemento.setTratLodos3((String) jComboBoxTratLd3.getSelectedPatron());
    	else elemento.setTratLodos3("");

    	if (jTextFieldFecha.getText()!=null && !jTextFieldFecha.getText().equals("")){
    		String fechas=jTextFieldFecha.getText();
    		String anio=fechas.substring(0,4);
    		String mes=fechas.substring(5,7);
    		String dia=fechas.substring(8,10);

    		java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
    		elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
    	}  
    	else{
    		elemento.setFechaRevision(null);
    	}

    	if (jComboBoxEstado.getSelectedPatron()!=null)
    		elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));

    	if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI)){
			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
			if (jComboBoxEpigInventario.getSelectedPatron()!=null){
				elemento.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
				elemento.setIdBien(((Inventario) jComboBoxNumInventario.getSelectedItem()).getId());
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
    	return elemento;
    }
    
    public Depuradora1EIEL getDepuradora1 (Depuradora1EIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new Depuradora1EIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());
			elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
					.getSelectedItem()).getIdIne());
			elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));
            if (jTextFieldClave.getText()!=null){
            	elemento.setClave(jTextFieldClave.getText());
            }
            
            if (jTextFieldOrden.getText()!=null){
            	elemento.setCodOrden(jTextFieldOrden.getText());
            }
            
            if (jComboBoxTratPr1.getSelectedPatron()!=null)
            	elemento.setTratPrimario1((String) jComboBoxTratPr1.getSelectedPatron());
            else elemento.setTratPrimario1("");
            if (jComboBoxTratPr2.getSelectedPatron()!=null)
            	elemento.setTratPrimario2((String) jComboBoxTratPr2.getSelectedPatron());
            else elemento.setTratPrimario2("");
            if (jComboBoxTratPr3.getSelectedPatron()!=null)
            	elemento.setTratPrimario3((String) jComboBoxTratPr3.getSelectedPatron());
            else elemento.setTratPrimario3("");
            
            if (jComboBoxTratSc1.getSelectedPatron()!=null)
            	elemento.setTratSecundario1((String) jComboBoxTratSc1.getSelectedPatron());
            else elemento.setTratSecundario1("");
            if (jComboBoxTratSc2.getSelectedPatron()!=null)
            	elemento.setTratSecundario2((String) jComboBoxTratSc2.getSelectedPatron());
            else elemento.setTratSecundario2("");
            if (jComboBoxTratSc3.getSelectedPatron()!=null)
            	elemento.setTratSecundario3((String) jComboBoxTratSc3.getSelectedPatron());
            else elemento.setTratSecundario3("");
            
            if (jComboBoxTratAv1.getSelectedPatron()!=null)
            	elemento.setTratAvanzado1((String) jComboBoxTratAv1.getSelectedPatron());
            else elemento.setTratAvanzado1("");
            if (jComboBoxTratAv2.getSelectedPatron()!=null)
            	elemento.setTratAvanzado2((String) jComboBoxTratAv2.getSelectedPatron());
            else elemento.setTratAvanzado2("");
            if (jComboBoxTratAv3.getSelectedPatron()!=null)
            	elemento.setTratAvanzado3((String) jComboBoxTratAv3.getSelectedPatron());
            else elemento.setTratAvanzado3("");
            
            if (jComboBoxProcCm1.getSelectedPatron()!=null)
            	elemento.setProcComplementario1((String) jComboBoxProcCm1.getSelectedPatron());
            else elemento.setProcComplementario1("");
            if (jComboBoxProcCm2.getSelectedPatron()!=null)
            	elemento.setProcComplementario2((String) jComboBoxProcCm2.getSelectedPatron());
            else elemento.setProcComplementario2("");
            if (jComboBoxProcCm3.getSelectedPatron()!=null)
            	elemento.setProcComplementario3((String) jComboBoxProcCm3.getSelectedPatron());
            else elemento.setProcComplementario3("");
            
            if (jComboBoxTratLd1.getSelectedPatron()!=null)
            	elemento.setTratLodos1((String) jComboBoxTratLd1.getSelectedPatron());
            else elemento.setTratLodos1("");
            if (jComboBoxTratLd2.getSelectedPatron()!=null)
            	elemento.setTratLodos2((String) jComboBoxTratLd2.getSelectedPatron());
            else elemento.setTratLodos2("");
            if (jComboBoxTratLd3.getSelectedPatron()!=null)
            	elemento.setTratLodos3((String) jComboBoxTratLd3.getSelectedPatron());
            else elemento.setTratLodos3("");

            if (jTextFieldFecha.getText()!=null && !jTextFieldFecha.getText().equals("")){
            	String fechas=jTextFieldFecha.getText();
            	String anio=fechas.substring(0,4);
            	String mes=fechas.substring(5,7);
            	String dia=fechas.substring(8,10);
            	
            	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
            	elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
            }  
            else{
            	elemento.setFechaRevision(null);
            }

            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
            
            if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI)){
    			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
    			if (jComboBoxEpigInventario.getSelectedPatron()!=null){
    				elemento.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
    				elemento.setIdBien(((Inventario) jComboBoxNumInventario.getSelectedItem()).getId());
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
            
        }
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.depuradoras1.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(Depuradoras1Dialog.DIM_X,Depuradoras1Dialog.DIM_Y);
        
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
    
    public JTextField getJTextFieldClave()
    {
        if (jTextFieldClave  == null)
        {
            jTextFieldClave = new TextField(2);
        }
        return jTextFieldClave;
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
    
    /**
     * This method initializes jComboBoxProvincia	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getJComboBoxProvincia()
    {
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
    
    /**
     * This method initializes jComboBoxMunicipio	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getJComboBoxMunicipio()
    {
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
    
    /**
     * This method initializes jTextFieldPlanta   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JTextField getJTextFieldOrden()
    {
    	if (jTextFieldOrden   == null)
    	{
    		jTextFieldOrden = new TextField(3);
    	}
    	return jTextFieldOrden;
    }
   
    private ComboBoxEstructuras getJComboBoxTratPr1()
    { 
        if (jComboBoxTratPr1 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
            jComboBoxTratPr1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratPr1.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratPr1;        
    }

    private ComboBoxEstructuras getJComboBoxTratPr2()
    { 
        if (jComboBoxTratPr2 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
            jComboBoxTratPr2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratPr2.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratPr2;        
    }

    private ComboBoxEstructuras getJComboBoxTratPr3()
    { 
        if (jComboBoxTratPr3 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
            jComboBoxTratPr3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratPr3.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratPr3;        
    }
    

    private ComboBoxEstructuras getJComboBoxTratSc1()
    { 
        if (jComboBoxTratSc1 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
            jComboBoxTratSc1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratSc1.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratSc1;        
    }
    
    private ComboBoxEstructuras getJComboBoxTratSc2()
    { 
        if (jComboBoxTratSc2 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
            jComboBoxTratSc2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratSc2.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratSc2;        
    }
    
    private ComboBoxEstructuras getJComboBoxTratSc3()
    { 
        if (jComboBoxTratSc3 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
            jComboBoxTratSc3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratSc3.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratSc3;        
    }

   
    private ComboBoxEstructuras getJComboBoxTratAv1()
    { 
        if (jComboBoxTratAv1 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
            jComboBoxTratAv1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratAv1.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratAv1;        
    }

    private ComboBoxEstructuras getJComboBoxTratAv2()
    { 
        if (jComboBoxTratAv2 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
            jComboBoxTratAv2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratAv2.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratAv2;        
    }
    
    private ComboBoxEstructuras getJComboBoxTratAv3()
    { 
        if (jComboBoxTratAv3 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
            jComboBoxTratAv3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratAv3.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratAv3;        
    }

    
    private ComboBoxEstructuras getJComboBoxProcCm1()
    { 
        if (jComboBoxProcCm1 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
            jComboBoxProcCm1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxProcCm1.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxProcCm1;        
    }

    private ComboBoxEstructuras getJComboBoxProcCm2()
    { 
        if (jComboBoxProcCm2 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
            jComboBoxProcCm2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxProcCm2.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxProcCm2;        
    }
    
    private ComboBoxEstructuras getJComboBoxProcCm3()
    { 
        if (jComboBoxProcCm3 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
            jComboBoxProcCm3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxProcCm3.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxProcCm3;        
    }

    
    private ComboBoxEstructuras getJComboBoxTratLd1()
    { 
        if (jComboBoxTratLd1 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
            jComboBoxTratLd1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratLd1.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratLd1;        
    }
    
    private ComboBoxEstructuras getJComboBoxTratLd2()
    { 
        if (jComboBoxTratLd2 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
            jComboBoxTratLd2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratLd2.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratLd2;        
    }
    
    private ComboBoxEstructuras getJComboBoxTratLd3()
    { 
        if (jComboBoxTratLd3 == null)
        {
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
            jComboBoxTratLd3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTratLd3.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTratLd3;        
    }
    
    
    private JTextField getJTextFieldFecha()
    {
    	if (jTextFieldFecha  == null)
    	{
    		jTextFieldFecha  = new TextField();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);            
            jTextFieldFecha.setText(datetime);
    		
    	}
    	return jTextFieldFecha;
    }
    
    public Depuradoras1Panel(GridBagLayout layout)
    {
        super(layout);
        initialize();
    }
    
    
    
    public String validateInput()
    {
        return null; 
    }
    
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getJPanelDatosIdentificacion()
    {
        if (jPanelIdentificacion == null)
        {   
        	jPanelIdentificacion = new JPanel(new GridBagLayout());
            jPanelIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identity"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
        	
            jLabelClave = new JLabel("", JLabel.CENTER); 
            jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 
            
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelOrden  = new JLabel("", JLabel.CENTER);
            jLabelOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            
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
    private JPanel getJPanelDatosInformacion()
    {
        if (jPanelInformacion == null)
        {   
        	jPanelInformacion  = new JPanel(new GridBagLayout());
        	jPanelInformacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.informacion"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            jLabelTratPr1 = new JLabel("", JLabel.CENTER); 
            jLabelTratPr1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratpr1")); 
            
            jLabelTratPr2 = new JLabel("", JLabel.CENTER); 
            jLabelTratPr2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratpr2")); 
            
            jLabelTratPr3 = new JLabel("", JLabel.CENTER); 
            jLabelTratPr3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratpr3")); 

            jLabelTratSc1 = new JLabel("", JLabel.CENTER);
            jLabelTratSc1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratsc1"));
            
            jLabelTratSc2 = new JLabel("", JLabel.CENTER);
            jLabelTratSc2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratsc2"));
            
            jLabelTratSc3 = new JLabel("", JLabel.CENTER);
            jLabelTratSc3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratsc3"));
            
            jLabelTratAv1 = new JLabel("", JLabel.CENTER);
            jLabelTratAv1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratav1"));
            
            jLabelTartAv2 = new JLabel("", JLabel.CENTER);
            jLabelTartAv2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratav2"));
            
            jLabelTratAv3 = new JLabel("", JLabel.CENTER);
            jLabelTratAv3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratav3"));
            
            jLabelProcCm1 = new JLabel("", JLabel.CENTER);
            jLabelProcCm1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.proccm1"));
            
            jLabelProcCm2 = new JLabel("", JLabel.CENTER);
            jLabelProcCm2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.proccm2"));
            
            jLabelProcCm3 = new JLabel("", JLabel.CENTER);
            jLabelProcCm3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.proccm3"));
            
            jLabelTratLd1 = new JLabel("", JLabel.CENTER);
            jLabelTratLd1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratld1"));
            
            jLabelTratLd2 = new JLabel("", JLabel.CENTER);
            jLabelTratLd2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratld2"));
            
            jLabelTratLd3 = new JLabel("", JLabel.CENTER);
            jLabelTratLd3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tratld3"));
                        
            jPanelInformacion.add(jLabelTratPr1,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratPr1(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratPr2,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratPr2(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratPr3,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratPr3(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratSc1,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratSc1(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratSc2,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratSc2(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratSc3,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratSc3(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 170, 0));
            
            jPanelInformacion.add(jLabelTratAv1,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratAv1(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTartAv2,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratAv2(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratAv3,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratAv3(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelProcCm1,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxProcCm1(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelProcCm2,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxProcCm2(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelProcCm3,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxProcCm3(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratLd1,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratLd1(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratLd2,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratLd2(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTratLd3,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTratLd3(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
        }
        return jPanelInformacion;
    }
    
    private JPanel getJPanelDatosRevision()
    {
        if (jPanelRevision == null)
        {   
        	jPanelRevision = new JPanel(new GridBagLayout());
        	jPanelRevision.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.revision"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            jLabelFecha  = new JLabel("", JLabel.CENTER); 
            jLabelFecha.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 
            
            jLabelEstado = new JLabel("", JLabel.CENTER); 
            jLabelEstado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 
                        
            jPanelRevision.add(jLabelFecha,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(getJTextFieldFecha(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(jLabelEstado, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelRevision.add(getJComboBoxEstado(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 130, 0));
            
        }
        return jPanelRevision;
    }
           

    public void okPressed()
    {
        okPressed = true;
    }
    
    public boolean getOkPressed()
    {
        return okPressed;
    }


    public boolean datosMinimosYCorrectos()
    {

        return  ((jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
        (jTextFieldOrden.getText()!=null && !jTextFieldOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0)); 
        
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

	        	String orden_ed = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_ed"))!=null){
	        		orden_ed=(feature.getAttribute(esquema.getAttributeByColumn("orden_ed"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelDepuradora1EIEL(clave, codprov, codmunic, orden_ed));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_ed);       	
			}
		}
	
	
	
	

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_ed) {
		
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
        
        if (orden_ed != null){
    		jTextFieldOrden.setText(orden_ed);
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
