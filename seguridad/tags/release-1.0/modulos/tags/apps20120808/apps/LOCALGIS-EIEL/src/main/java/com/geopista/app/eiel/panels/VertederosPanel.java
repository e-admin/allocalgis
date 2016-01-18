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
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.dialogs.VertederosDialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class VertederosPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
        
    private boolean okPressed = false;	
	
    private JLabel jLabelCodProv = null;
    private JLabel jLabelClave = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelOrden = null;
	
	private JTextField jTextFieldClave = null;
	private JTextField jTextFieldOrden = null;
	
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
        
    private JPanel jPanelInformacion = null;
    
    private ComboBoxEstructuras jComboBoxTipo = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
    private ComboBoxEstructuras jComboBoxOlor = null;
    private ComboBoxEstructuras jComboBoxHumo = null;
    private ComboBoxEstructuras jComboBoxContAnim = null;
    private ComboBoxEstructuras jComboBoxInun = null;
    private ComboBoxEstructuras jComboBoxFiltr = null;
    private ComboBoxEstructuras jComboBoxImpct = null;
    private ComboBoxEstructuras jComboBoxFrecAv = null;
    private ComboBoxEstructuras jComboBoxSatur = null;
    private ComboBoxEstructuras jComboBoxInest = null;
    private ComboBoxEstructuras jComboBoxOtr = null;    
    private JTextField jTextFieldCapTot = null;
    private JTextField jTextFieldCapOcp = null;
    private JTextField jTextFieldCapTrans = null;
    private ComboBoxEstructuras jComboBoxEst = null;
    private JTextField jTextFieldVida = null;
    private ComboBoxEstructuras jComboBoxCateg = null;
    private ComboBoxEstructuras jComboBoxActv = null;
    private TextField jTextFieldFechaApert = null;
    private JTextField jTextFieldObservacion = null;
    private ComboBoxEstructuras jComboBoxCapAmpl = null;
    
    private JLabel jLabelTipo = null;
    private JLabel jLabelTitular = null;
    private JLabel jLabelGestor = null;
    private JLabel jLabelOlor = null;
    private JLabel jLabelHumo = null;
    private JLabel jLabelContAnim = null;
    private JLabel jLabelInun = null;
    private JLabel jLabelFiltr = null;
    private JLabel jLabelImpct = null;
    private JLabel jLabelFrecAv = null;
    private JLabel jLabelSatur = null;
    private JLabel jLabelInest = null;
    private JLabel jLabelOtr = null;
    private JLabel jLabelCapTot = null;
    private JLabel jLabelCapOcp = null;
    private JLabel jLabelCapTrans = null;
    private JLabel jLabelEst = null;
    private JLabel jLabelVida = null;
    private JLabel jLabelCateg = null;
    private JLabel jLabelActv = null;
    private JLabel jLabelFechaApert = null;
    private JLabel jLabelObservacion = null;
    private JLabel jLabelCapAmpl = null;
	private JLabel jLabelObraEjec = null;
    private JPanel jPanelRevision = null;
    
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;	
	
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private ComboBoxEstructuras jComboBoxObraEjec = null;
	
	
	private String idMunicipioSelected;
	
    /**
     * This method initializes
     * 
     */
    public VertederosPanel()
    {
        super();
        initialize();
    }
    
    public VertederosPanel(VertederosEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(VertederosEIEL elemento)
    {
        if (elemento!=null)
        {
        	
			idMunicipioSelected=elemento.getCodINEMunicipio();

            //Datos identificacion   
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
            
            if (elemento.getCodOrden()!=null){
            	jTextFieldOrden.setText(elemento.getCodOrden());
            }
            else{
            	jTextFieldOrden.setText("");
            }
            
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            
            if (elemento.getTitularidad() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitularidad());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (elemento.getGestion() != null){
            	jComboBoxGestor.setSelectedPatron(elemento.getGestion());
        	}
        	else{
        		jComboBoxGestor.setSelectedIndex(0);
        	}            
            
            if (elemento.getOlores() != null){            	
            	jComboBoxOlor.setSelectedPatron(elemento.getOlores());
            }
            else{
            	jComboBoxOlor.setSelectedIndex(0);
            }
            
            if (elemento.getHumos() != null){            	
            	jComboBoxHumo.setSelectedPatron(elemento.getHumos());
            }
            else{
            	jComboBoxHumo.setSelectedIndex(0);
            } 
            
            if (elemento.getContAnimal()!= null){            	
            	jComboBoxContAnim.setSelectedPatron(elemento.getContAnimal());
            }
            else{
            	jComboBoxContAnim.setSelectedIndex(0);
            } 
            
            if (elemento.getRsgoInundacion()!= null){            	
            	jComboBoxInun.setSelectedPatron(elemento.getRsgoInundacion());
            }
            else{
            	jComboBoxInun.setSelectedIndex(0);
            } 
            
            if (elemento.getFiltraciones() != null){            	
            	jComboBoxFiltr.setSelectedPatron(elemento.getFiltraciones());
            }
            else{
            	jComboBoxFiltr.setSelectedIndex(0);
            } 
            
            if (elemento.getImptVisual() != null){            	
            	jComboBoxImpct.setSelectedPatron(elemento.getImptVisual());
            }
            else{
            	jComboBoxImpct.setSelectedIndex(0);
            } 
            
            if (elemento.getFrecAverias() != null){            	
            	jComboBoxFrecAv.setSelectedPatron(elemento.getFrecAverias());
            }
            else{
            	jComboBoxFrecAv.setSelectedIndex(0);
            } 
            
            if (elemento.getSaturacion() != null){            	
            	jComboBoxSatur.setSelectedPatron(elemento.getSaturacion());
            }
            else{
            	jComboBoxSatur.setSelectedIndex(0);
            } 
            
            if (elemento.getInestabilidad() != null){            	
            	jComboBoxInest.setSelectedPatron(elemento.getInestabilidad());
            }
            else{
            	jComboBoxInest.setSelectedIndex(0);
            } 
            
            if (elemento.getOtros() != null){            	
            	jComboBoxOtr.setSelectedPatron(elemento.getOtros());
            }
            else{
            	jComboBoxOtr.setSelectedIndex(0);
            } 
            
            if (elemento.getCapTotal() != null){            	
            	jTextFieldCapTot.setText(elemento.getCapTotal().toString());
            }
            else{
            	jTextFieldCapTot.setText("");
            } 
            
            if (elemento.getCapOcupada() != null){            	
            	jTextFieldCapOcp.setText(elemento.getCapOcupada().toString());
            }
            else{
            	jTextFieldCapOcp.setText("");
            } 
            
            if (elemento.getCapTransform() != null){            	
            	jTextFieldCapTrans.setText(elemento.getCapTransform().toString());
            }
            else{
            	jTextFieldCapTrans.setText("");
            } 
            
            if (elemento.getEstado() != null){
        		jComboBoxEst.setSelectedPatron(elemento.getEstado());
        	}
        	else{
        		jComboBoxEst.setSelectedIndex(0);
        	} 
            
            if (elemento.getVidaUtil() != null){            	
            	jTextFieldVida.setText(elemento.getVidaUtil().toString());
            }
            else{
            	jTextFieldVida.setText("");
            } 
            
            if (elemento.getCategoria() != null){            	
            	jComboBoxCateg.setSelectedPatron(elemento.getCategoria());
            }
            else{
            	jComboBoxCateg.setSelectedIndex(0);
            } 
            
            if (elemento.getActividad() != null){            	
            	jComboBoxActv.setSelectedPatron(elemento.getActividad());
            }
            else{
            	jComboBoxActv.setSelectedIndex(0);
            } 
            
            if (elemento.getFechaApertura() != null){
        		jTextFieldFechaApert.setText(elemento.getFechaApertura().toString());
        	}
        	else{
        		jTextFieldFechaApert.setText(null);
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
            
            if (elemento.getPosbAmpliacion() != null){
            	jComboBoxCapAmpl.setSelectedPatron(elemento.getPosbAmpliacion());
        	}
        	else{
        		jComboBoxCapAmpl.setSelectedIndex(0);
        	}
            
            if (elemento.getObservaciones() != null){            	
            	jTextFieldObservacion.setText(elemento.getObservaciones());
            }
            else{
            	jTextFieldObservacion.setText("");
            } 
            
            if (elemento.getEstadoRevision() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
            if (elemento.getObra_ejecutada()!= null){
            	jComboBoxObraEjec.setSelectedPatron(elemento.getObra_ejecutada().toString());
        	}
        	else{
        		jComboBoxObraEjec.setSelectedIndex(0);
        	}
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.VERTEDEROS_CLAVE);
        	        	       	
        	 if (ConstantesLocalGISEIEL.idProvincia!=null){
 				jComboBoxProvincia
 				.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
 			 }

 			 if (ConstantesLocalGISEIEL.idMunicipio!=null){
 				 jComboBoxMunicipio
 					.setSelectedIndex(municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio));
 			 }
 		}
    }
    
    public void loadData()
    {
    	Object object = AppContext.getApplicationContext().getBlackboard().get("VERTEDEROS_panel");    	
    	if (object != null && object instanceof VertederosEIEL){    		
    		VertederosEIEL elemento = (VertederosEIEL)object;
            //Datos identificacion   
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
            
            if (elemento.getCodOrden()!=null){
            	jTextFieldOrden.setText(elemento.getCodOrden());
            }
            else{
            	jTextFieldOrden.setText("");
            }
            
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            
            if (elemento.getTitularidad() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitularidad());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (elemento.getGestion() != null){
            	jComboBoxGestor.setSelectedPatron(elemento.getGestion());
        	}
        	else{
        		jComboBoxGestor.setSelectedIndex(0);
        	}            
            
            if (elemento.getOlores() != null){            	
            	jComboBoxOlor.setSelectedPatron(elemento.getOlores());
            }
            else{
            	jComboBoxOlor.setSelectedIndex(0);
            }
            
            if (elemento.getHumos() != null){            	
            	jComboBoxHumo.setSelectedPatron(elemento.getHumos());
            }
            else{
            	jComboBoxHumo.setSelectedIndex(0);
            } 
            
            if (elemento.getContAnimal()!= null){            	
            	jComboBoxContAnim.setSelectedPatron(elemento.getContAnimal());
            }
            else{
            	jComboBoxContAnim.setSelectedIndex(0);
            } 
            
            if (elemento.getRsgoInundacion()!= null){            	
            	jComboBoxInun.setSelectedPatron(elemento.getRsgoInundacion());
            }
            else{
            	jComboBoxInun.setSelectedIndex(0);
            } 
            
            if (elemento.getFiltraciones() != null){            	
            	jComboBoxFiltr.setSelectedPatron(elemento.getFiltraciones());
            }
            else{
            	jComboBoxFiltr.setSelectedIndex(0);
            } 
            
            if (elemento.getImptVisual() != null){            	
            	jComboBoxImpct.setSelectedPatron(elemento.getImptVisual());
            }
            else{
            	jComboBoxImpct.setSelectedIndex(0);
            } 
            
            if (elemento.getFrecAverias() != null){            	
            	jComboBoxFrecAv.setSelectedPatron(elemento.getFrecAverias());
            }
            else{
            	jComboBoxFrecAv.setSelectedIndex(0);
            } 
            
            if (elemento.getSaturacion() != null){            	
            	jComboBoxSatur.setSelectedPatron(elemento.getSaturacion());
            }
            else{
            	jComboBoxSatur.setSelectedIndex(0);
            } 
            
            if (elemento.getInestabilidad() != null){            	
            	jComboBoxInest.setSelectedPatron(elemento.getInestabilidad());
            }
            else{
            	jComboBoxInest.setSelectedIndex(0);
            } 
            
            if (elemento.getOtros() != null){            	
            	jComboBoxOtr.setSelectedPatron(elemento.getOtros());
            }
            else{
            	jComboBoxOtr.setSelectedIndex(0);
            } 
            
            if (elemento.getCapTotal() != null){            	
            	jTextFieldCapTot.setText(elemento.getCapTotal().toString());
            }
            else{
            	jTextFieldCapTot.setText("");
            } 
            
            if (elemento.getCapOcupada() != null){            	
            	jTextFieldCapOcp.setText(elemento.getCapOcupada().toString());
            }
            else{
            	jTextFieldCapOcp.setText("");
            } 
            
            if (elemento.getCapTransform() != null){            	
            	jTextFieldCapTrans.setText(elemento.getCapTransform().toString());
            }
            else{
            	jTextFieldCapTrans.setText("");
            } 
            
            if (elemento.getEstado() != null){
        		jComboBoxEst.setSelectedPatron(elemento.getEstado());
        	}
        	else{
        		jComboBoxEst.setSelectedIndex(0);
        	} 
            
            if (elemento.getVidaUtil() != null){            	
            	jTextFieldVida.setText(elemento.getVidaUtil().toString());
            }
            else{
            	jTextFieldVida.setText("");
            } 
            
            if (elemento.getCategoria() != null){            	
            	jComboBoxCateg.setSelectedPatron(elemento.getCategoria());
            }
            else{
            	jComboBoxCateg.setSelectedIndex(0);
            } 
            
            if (elemento.getActividad() != null){            	
            	jComboBoxActv.setSelectedPatron(elemento.getActividad());
            }
            else{
            	jComboBoxActv.setSelectedIndex(0);
            } 
            
            if (elemento.getFechaApertura() != null){
        		jTextFieldFechaApert.setText(elemento.getFechaApertura().toString());
        	}
        	else{
        		jTextFieldFechaApert.setText(null);
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
            
            if (elemento.getPosbAmpliacion() != null){
            	jComboBoxCapAmpl.setSelectedPatron(elemento.getPosbAmpliacion());
        	}
        	else{
        		jComboBoxCapAmpl.setSelectedIndex(0);
        	}
            
            if (elemento.getObservaciones() != null){            	
            	jTextFieldObservacion.setText(elemento.getObservaciones());
            }
            else{
            	jTextFieldObservacion.setText("");
            } 
            
            if (elemento.getEstadoRevision() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
            if (elemento.getObra_ejecutada() != null){
            	jComboBoxObraEjec.setSelectedPatron(elemento.getObra_ejecutada().toString());
        	}
        	else{
        		jComboBoxObraEjec.setSelectedIndex(0);
        	}
            
        }
    }
    
    public VertederosEIEL getVertederosData ()
    {
    	VertederosEIEL	elemento = new VertederosEIEL();

		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		
    	elemento.setCodOrden(jTextFieldOrden.getText());
    	elemento.setClave(jTextFieldClave.getText());

    	if (jComboBoxTipo.getSelectedPatron()!=null)
    		elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());            
    	else elemento.setTipo("");
    	if (jComboBoxTitular.getSelectedPatron()!=null)
    		elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());            
    	else elemento.setTitularidad("");
    	if (jComboBoxGestor.getSelectedPatron()!=null)
    		elemento.setGestion((String) jComboBoxGestor.getSelectedPatron());
    	else elemento.setGestion("");
    	if (jComboBoxOlor.getSelectedPatron()!=null)
    		elemento.setOlores((String) jComboBoxOlor.getSelectedPatron());
    	else elemento.setOlores("");
    	if (jComboBoxHumo.getSelectedPatron()!=null)
    		elemento.setHumos((String) jComboBoxHumo.getSelectedPatron());
    	else elemento.setHumos("");
    	if (jComboBoxContAnim.getSelectedPatron()!=null)
    		elemento.setContAnimal((String) jComboBoxContAnim.getSelectedPatron());
    	else elemento.setContAnimal("");
    	if (jComboBoxInun.getSelectedPatron()!=null)
    		elemento.setRsgoInundacion((String) jComboBoxInun.getSelectedPatron());
    	else elemento.setContAnimal("");
    	if (jComboBoxFiltr.getSelectedPatron()!=null)
    		elemento.setFiltraciones((String) jComboBoxFiltr.getSelectedPatron());
    	else elemento.setFiltraciones("");
    	if (jComboBoxImpct.getSelectedPatron()!=null)
    		elemento.setImptVisual((String) jComboBoxImpct.getSelectedPatron());
    	else elemento.setImptVisual("");
    	if (jComboBoxFrecAv.getSelectedPatron()!=null)
    		elemento.setFrecAverias((String) jComboBoxFrecAv.getSelectedPatron());
    	else elemento.setFrecAverias("");
    	if (jComboBoxSatur.getSelectedPatron()!=null)
    		elemento.setSaturacion((String) jComboBoxSatur.getSelectedPatron());
    	else elemento.setSaturacion("");
    	if (jComboBoxInest.getSelectedPatron()!=null)
    		elemento.setInestabilidad((String) jComboBoxInest.getSelectedPatron());
    	else elemento.setInestabilidad("");
    	if (jComboBoxOtr.getSelectedPatron()!=null)
    		elemento.setOtros((String) jComboBoxOtr.getSelectedPatron());
    	else elemento.setOtros("");

    	if (jTextFieldCapTot.getText()!=null && !jTextFieldCapTot.getText().equals("")){
    		elemento.setCapTotal(new Integer(jTextFieldCapTot.getText()));
    	}
    	else{
    		elemento.setCapTotal(null);
    	}

    	if (jTextFieldCapOcp.getText()!=null && !jTextFieldCapOcp.getText().equals("")){
    		elemento.setCapOcupada(new Integer(jTextFieldCapOcp.getText()));
    	}
    	else{
    		elemento.setCapOcupada(null);
    	}

    	if (jTextFieldCapTrans.getText()!=null && !jTextFieldCapTrans.getText().equals("")){
    		elemento.setCapTransform(new Integer(jTextFieldCapTrans.getText()));
    	}
    	else{
    		elemento.setCapTransform(null);
    	}

    	if (jComboBoxEst.getSelectedPatron()!=null)
    		elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
    	else elemento.setEstado("");

    	if (jTextFieldVida.getText()!=null && !jTextFieldVida.getText().equals("")){
    		elemento.setVidaUtil(new Integer(jTextFieldVida.getText()));
    	}
    	else{
    		elemento.setVidaUtil(null);
    	}

    	if (jComboBoxCateg.getSelectedPatron()!=null)
    		elemento.setCategoria((String) jComboBoxCateg.getSelectedPatron());
    	else elemento.setCategoria("");
    	if (jComboBoxCapAmpl.getSelectedPatron()!=null)
    		elemento.setPosbAmpliacion((String) jComboBoxCapAmpl.getSelectedPatron());
    	else elemento.setPosbAmpliacion("");
    	if (jComboBoxActv.getSelectedPatron()!=null)
    		elemento.setActividad((String) jComboBoxActv.getSelectedPatron());
    	else elemento.setActividad("");

    	if (jTextFieldFechaApert.getText()!=null && !jTextFieldFechaApert.getText().equals("")){
    		elemento.setFechaApertura(new Integer(jTextFieldFechaApert.getText()));
    	}
    	else{
    		elemento.setFechaApertura(null);
    	}

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

    	if (jTextFieldObservacion.getText()!=null){
    		elemento.setObservaciones(jTextFieldObservacion.getText());
    	}

    	if (jComboBoxEstado.getSelectedPatron()!=null)
    		elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
    	if (jComboBoxObraEjec.getSelectedPatron()!=null)
         	elemento.setObra_ejecutada(jComboBoxObraEjec.getSelectedPatron());
         else elemento.setObra_ejecutada("");
    	 
    	return elemento;
    }
    
    
    public VertederosEIEL getVertederos (VertederosEIEL elemento)
    {
        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new VertederosEIEL();
            }    
			elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());
			elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
					.getSelectedItem()).getIdIne());

            elemento.setCodOrden(jTextFieldOrden.getText());
            elemento.setClave(jTextFieldClave.getText());
            
            if (jComboBoxTipo.getSelectedPatron()!=null)
	            elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());            
            else elemento.setTipo("");
            if (jComboBoxTitular.getSelectedPatron()!=null)
	            elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());            
            else elemento.setTitularidad("");
            if (jComboBoxGestor.getSelectedPatron()!=null)
	            elemento.setGestion((String) jComboBoxGestor.getSelectedPatron());
            else elemento.setGestion("");
            if (jComboBoxOlor.getSelectedPatron()!=null)
	            elemento.setOlores((String) jComboBoxOlor.getSelectedPatron());
            else elemento.setOlores("");
            if (jComboBoxHumo.getSelectedPatron()!=null)
	            elemento.setHumos((String) jComboBoxHumo.getSelectedPatron());
            else elemento.setHumos("");
            if (jComboBoxContAnim.getSelectedPatron()!=null)
	            elemento.setContAnimal((String) jComboBoxContAnim.getSelectedPatron());
            else elemento.setContAnimal("");
            if (jComboBoxInun.getSelectedPatron()!=null)
	            elemento.setRsgoInundacion((String) jComboBoxInun.getSelectedPatron());
            else elemento.setContAnimal("");
            if (jComboBoxFiltr.getSelectedPatron()!=null)
	            elemento.setFiltraciones((String) jComboBoxFiltr.getSelectedPatron());
            else elemento.setFiltraciones("");
            if (jComboBoxImpct.getSelectedPatron()!=null)
	            elemento.setImptVisual((String) jComboBoxImpct.getSelectedPatron());
            else elemento.setImptVisual("");
            if (jComboBoxFrecAv.getSelectedPatron()!=null)
	            elemento.setFrecAverias((String) jComboBoxFrecAv.getSelectedPatron());
            else elemento.setFrecAverias("");
            if (jComboBoxSatur.getSelectedPatron()!=null)
	            elemento.setSaturacion((String) jComboBoxSatur.getSelectedPatron());
            else elemento.setSaturacion("");
            if (jComboBoxInest.getSelectedPatron()!=null)
	            elemento.setInestabilidad((String) jComboBoxInest.getSelectedPatron());
            else elemento.setInestabilidad("");
            if (jComboBoxOtr.getSelectedPatron()!=null)
	            elemento.setOtros((String) jComboBoxOtr.getSelectedPatron());
            else elemento.setOtros("");
            
            if (jTextFieldCapTot.getText()!=null && !jTextFieldCapTot.getText().equals("")){
            	elemento.setCapTotal(new Integer(jTextFieldCapTot.getText()));
            }
            else{
            	elemento.setCapTotal(null);
            }
            
            if (jTextFieldCapOcp.getText()!=null && !jTextFieldCapOcp.getText().equals("")){
            	elemento.setCapOcupada(new Integer(jTextFieldCapOcp.getText()));
            }
            else{
            	elemento.setCapOcupada(null);
            }
            
            if (jTextFieldCapTrans.getText()!=null && !jTextFieldCapTrans.getText().equals("")){
            	elemento.setCapTransform(new Integer(jTextFieldCapTrans.getText()));
            }
            else{
            	elemento.setCapTransform(null);
            }
            
            if (jComboBoxEst.getSelectedPatron()!=null)
            	elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
            else elemento.setEstado("");
             
            if (jTextFieldVida.getText()!=null && !jTextFieldVida.getText().equals("")){
            	elemento.setVidaUtil(new Integer(jTextFieldVida.getText()));
            }
            else{
            	elemento.setVidaUtil(null);
            }
            
            if (jComboBoxCateg.getSelectedPatron()!=null)
	            elemento.setCategoria((String) jComboBoxCateg.getSelectedPatron());
            else elemento.setCategoria("");
            if (jComboBoxCapAmpl.getSelectedPatron()!=null)
	            elemento.setPosbAmpliacion((String) jComboBoxCapAmpl.getSelectedPatron());
            else elemento.setPosbAmpliacion("");
            if (jComboBoxActv.getSelectedPatron()!=null)
	            elemento.setActividad((String) jComboBoxActv.getSelectedPatron());
            else elemento.setActividad("");
            
            if (jTextFieldFechaApert.getText()!=null && !jTextFieldFechaApert.getText().equals("")){
            	elemento.setFechaApertura(new Integer(jTextFieldFechaApert.getText()));
            }
            else{
            	elemento.setFechaApertura(null);
            }
            
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
            
            if (jTextFieldObservacion.getText()!=null){
            	elemento.setObservaciones(jTextFieldObservacion.getText());
            }
            
            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));            
            if (jComboBoxObraEjec.getSelectedPatron()!=null)
	            elemento.setObra_ejecutada((String) jComboBoxObraEjec.getSelectedPatron());
            else elemento.setObra_ejecutada("");
            
        }
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.vertederos.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(VertederosDialog.DIM_X,VertederosDialog.DIM_Y);
        
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
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
    
    public JTextField getjTextFieldClave()
    {
        if (jTextFieldClave == null)
        {
            jTextFieldClave = new TextField(2);
        }
        return jTextFieldClave;
    }
    
    public JTextField getJTextFieldOrden()
    {
    	if (jTextFieldOrden   == null)
    	{
    		jTextFieldOrden = new TextField(3);
    	}
    	return jTextFieldOrden;
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
    
    private ComboBoxEstructuras getJComboBoxTipo()
    { 
        if (jComboBoxTipo == null)
        {
            Estructuras.cargarEstructura("eiel_Tipo de vertedero");
            jComboBoxTipo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTipo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTipo;        
    }
    
    private ComboBoxEstructuras getJComboBoxTitular()
    { 
        if (jComboBoxTitular == null)
        {
            Estructuras.cargarEstructura("eiel_Titular del vertedero");
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
            Estructuras.cargarEstructura("eiel_Gestor del Vertedero");
            jComboBoxGestor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGestor;        
    }
    
    private ComboBoxEstructuras getJComboBoxOlor()
    { 
        if (jComboBoxOlor == null)
        {
            Estructuras.cargarEstructura("eiel_olor");
            jComboBoxOlor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxOlor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxOlor;        
    }
    
    private ComboBoxEstructuras getJComboBoxHumo()
    { 
        if (jComboBoxHumo == null)
        {
            Estructuras.cargarEstructura("eiel_humo");
            jComboBoxHumo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxHumo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxHumo;        
    }
    
    private ComboBoxEstructuras getJComboBoxContAnim()
    { 
        if (jComboBoxContAnim == null)
        {
            Estructuras.cargarEstructura("eiel_cont animal");
            jComboBoxContAnim = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxContAnim.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxContAnim;        
    }
    
    private ComboBoxEstructuras getJComboBoxInun()
    { 
        if (jComboBoxInun == null)
        {
            Estructuras.cargarEstructura("eiel_riesgo inundación");
            jComboBoxInun = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxInun.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxInun;        
    }
    
    private ComboBoxEstructuras getJComboBoxFiltr()
    { 
        if (jComboBoxFiltr == null)
        {
            Estructuras.cargarEstructura("eiel_filtraciones");
            jComboBoxFiltr = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxFiltr.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxFiltr;        
    }
    
    private ComboBoxEstructuras getJComboBoxImpct()
    { 
        if (jComboBoxImpct == null)
        {
            Estructuras.cargarEstructura("eiel_impacto visual");
            jComboBoxImpct = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxImpct.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxImpct;        
    }
    
    private ComboBoxEstructuras getJComboBoxFrecAv()
    { 
        if (jComboBoxFrecAv == null)
        {
            Estructuras.cargarEstructura("eiel_frecuentes averías");
            jComboBoxFrecAv = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxFrecAv.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxFrecAv;        
    }
    
    private ComboBoxEstructuras getJComboBoxSatur()
    { 
        if (jComboBoxSatur == null)
        {
            Estructuras.cargarEstructura("eiel_saturación");
            jComboBoxSatur = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxSatur.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxSatur;        
    }
    
    private ComboBoxEstructuras getJComboBoxInest()
    { 
        if (jComboBoxInest == null)
        {
            Estructuras.cargarEstructura("eiel_inestabilidad");
            jComboBoxInest = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxInest.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxInest;        
    }
    
    private ComboBoxEstructuras getJComboBoxOtr()
    { 
        if (jComboBoxOtr == null)
        {
            Estructuras.cargarEstructura("eiel_otros vertedero");
            jComboBoxOtr = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxOtr.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxOtr;        
    }
    
    private JTextField getjTextFieldCapTot()
    {
    	if (jTextFieldCapTot == null)
    	{
    		jTextFieldCapTot = new TextField(8);
    		jTextFieldCapTot.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCapTot, 8, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCapTot;
    }
    
    private JTextField getjTextFieldCapOcp()
    {
    	if (jTextFieldCapOcp == null)
    	{
    		jTextFieldCapOcp = new TextField(4);
    		jTextFieldCapOcp.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCapOcp, 4, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCapOcp;
    }
    
    private JTextField getjTextFieldCapTrans()
    {
    	if (jTextFieldCapTrans == null)
    	{
    		jTextFieldCapTrans = new TextField(4);
    		jTextFieldCapTrans.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCapTrans, 4, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCapTrans;
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
    
    private JTextField getjTextFieldVida()
    {
    	if (jTextFieldVida == null)
    	{
    		jTextFieldVida = new TextField(10);
    		jTextFieldVida.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVida, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVida;
    }
    
    private ComboBoxEstructuras getJComboBoxCateg()
    { 
        if (jComboBoxCateg == null)
        {
            Estructuras.cargarEstructura("eiel_Categoria del vertedero");
            jComboBoxCateg = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCateg.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCateg;        
    }
    
    private ComboBoxEstructuras getJComboBoxActv()
    { 
        if (jComboBoxActv == null)
        {
            Estructuras.cargarEstructura("eiel_Situación de la actividad de la instalación");
            jComboBoxActv = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxActv.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxActv;        
    }
    
    private TextField getJTextFieldFechaApert()
    {
    	if (jTextFieldFechaApert == null)
    	{
    		jTextFieldFechaApert = new TextField(4);
    		jTextFieldFechaApert.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldFechaApert, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldFechaApert;
    }
    
    private JTextField getjTextFieldObservacion()
    {
    	if (jTextFieldObservacion == null)
    	{
    		jTextFieldObservacion = new TextField(50);
    		jTextFieldObservacion.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObservacion, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObservacion;
    }
    
    private ComboBoxEstructuras getJComboBoxCapAmpl()
    { 
        if (jComboBoxCapAmpl == null)
        {
            Estructuras.cargarEstructura("eiel_Posibilidad de ampliación");
            jComboBoxCapAmpl = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCapAmpl.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCapAmpl;        
    }
    
    private JTextField getJTextFieldFecha()
    {
    	if (jTextFieldFecha == null)
    	{
    		jTextFieldFecha  = new TextField();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);            
            jTextFieldFecha.setText(datetime);
    	}
    	return jTextFieldFecha;
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
    
    private ComboBoxEstructuras getJComboBoxObraEjec()
    { 
        if (jComboBoxObraEjec == null)
        {
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            jComboBoxObraEjec = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxObraEjec.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxObraEjec;        
    }
    public VertederosPanel(GridBagLayout layout)
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
    private JPanel getJPanelDatosIdentificacion()
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

            jLabelOrden   = new JLabel("", JLabel.CENTER);
            jLabelOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            
            
            jPanelIdentificacion.add(jLabelCodProv, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelIdentificacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentificacion.add(jLabelCodMunic, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelIdentificacion.add(getJComboBoxMunicipio(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentificacion.add(jLabelClave,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentificacion.add(getjTextFieldClave(), 
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
    
    private JPanel getJPanelDatosInformacion()
    {
        if (jPanelInformacion == null)
        {   
        	jPanelInformacion  = new JPanel(new GridBagLayout());
        	jPanelInformacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.informacion"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
        	jLabelTipo = new JLabel("", JLabel.CENTER); 
        	jLabelTipo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo")); 
            
        	jLabelTitular = new JLabel("", JLabel.CENTER); 
        	jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular")); 

        	jLabelGestor = new JLabel("", JLabel.CENTER);
        	jLabelGestor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestor"));
            
        	jLabelOlor = new JLabel("", JLabel.CENTER);
        	jLabelOlor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.olor"));
            
        	jLabelHumo = new JLabel("", JLabel.CENTER);
        	jLabelHumo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.humo"));
            
        	jLabelContAnim = new JLabel("", JLabel.CENTER);
        	jLabelContAnim.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.animal"));
            
        	jLabelInun = new JLabel("", JLabel.CENTER);
        	jLabelInun.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.riesgo"));
            
        	jLabelFiltr = new JLabel("", JLabel.CENTER);
        	jLabelFiltr.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.filtracion"));
            
        	jLabelImpct = new JLabel("", JLabel.CENTER);
        	jLabelImpct.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.impacto"));
            
        	jLabelFrecAv = new JLabel("", JLabel.CENTER);
        	jLabelFrecAv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.frecAverias"));
            
        	jLabelSatur = new JLabel("", JLabel.CENTER);
        	jLabelSatur.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.saturacion_vertedero"));
        	
        	jLabelInest = new JLabel("", JLabel.CENTER);
        	jLabelInest.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.inestable"));
        	
        	jLabelOtr = new JLabel("", JLabel.CENTER);
        	jLabelOtr.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.otros"));
        	
        	jLabelCapTot = new JLabel("", JLabel.CENTER);
        	jLabelCapTot.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capTot"));
        	
        	jLabelCapOcp = new JLabel("", JLabel.CENTER);
        	jLabelCapOcp.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capOcp"));
        	
        	jLabelCapTrans = new JLabel("", JLabel.CENTER);
        	jLabelCapTrans.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capTrans"));
        	
        	jLabelEst = new JLabel("", JLabel.CENTER);
        	jLabelEst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.est"));
        	
        	jLabelObraEjec = new JLabel("", JLabel.CENTER); 
            jLabelObraEjec.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.obraejec")); 
            
        	jLabelVida = new JLabel("", JLabel.CENTER);
        	jLabelVida.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.vidaUtil"));
        	
        	jLabelCateg = new JLabel("", JLabel.CENTER);
        	jLabelCateg.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.categ"));
        	
        	jLabelActv = new JLabel("", JLabel.CENTER);
        	jLabelActv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.actv"));
        	
        	jLabelFechaApert = new JLabel("", JLabel.CENTER);
        	jLabelFechaApert.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.apertura"));
        	
        	jLabelObservacion = new JLabel("", JLabel.CENTER);
        	jLabelObservacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
        	
        	jLabelCapAmpl = new JLabel("", JLabel.CENTER);
        	jLabelCapAmpl.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capAmpl"));
        	
            jPanelInformacion.add(jLabelTipo,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTipo(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 50, 0));
            
            jPanelInformacion.add(jLabelTitular,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTitular(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelGestor,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxGestor(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelOlor,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxOlor(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelHumo,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxHumo(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelContAnim,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxContAnim(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelInun,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxInun(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFiltr,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxFiltr(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelImpct,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxImpct(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFrecAv,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxFrecAv(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSatur,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxSatur(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelInest,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxInest(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelOtr,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxOtr(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCapTot,
                    new GridBagConstraints(2, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getjTextFieldCapTot(), 
                    new GridBagConstraints(3, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCapOcp,
                    new GridBagConstraints(0, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getjTextFieldCapOcp(), 
                    new GridBagConstraints(1, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCapTrans,
                    new GridBagConstraints(2, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getjTextFieldCapTrans(), 
                    new GridBagConstraints(3, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelInformacion.add(jLabelEst,
                    new GridBagConstraints(0, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxEst(), 
                    new GridBagConstraints(1, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelVida,
                    new GridBagConstraints(2, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getjTextFieldVida(), 
                    new GridBagConstraints(3, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCateg,
                    new GridBagConstraints(0, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxCateg(), 
                    new GridBagConstraints(1, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelActv,
                    new GridBagConstraints(2, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxActv(), 
                    new GridBagConstraints(3, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFechaApert,
                    new GridBagConstraints(0, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldFechaApert(), 
                    new GridBagConstraints(1, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 100, 0));
            
            jPanelInformacion.add(jLabelCapAmpl,
                    new GridBagConstraints(2, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxCapAmpl(), 
                    new GridBagConstraints(3, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObraEjec,
                    new GridBagConstraints(0, 12, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJComboBoxObraEjec(), 
                    new GridBagConstraints(1, 12, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObservacion,
                    new GridBagConstraints(0, 13, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getjTextFieldObservacion(), 
                    new GridBagConstraints(1, 13, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            
        }
        return jPanelInformacion;
    }
    
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */    
    
    private JPanel getJPanelDatosRevision()
    {
        if (jPanelRevision == null)
        {   
        	jPanelRevision = new JPanel(new GridBagLayout());
        	jPanelRevision.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.revision"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            jLabelFecha = new JLabel("", JLabel.CENTER); 
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
                            new Insets(5, 5, 5, 5), 120, 0));
            
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

    	return (jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) && 
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
	        	
	        	String orden_vt = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_vt"))!=null){
	        		orden_vt=(feature.getAttribute(esquema.getAttributeByColumn("orden_vt"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelVertederosEIEL(clave, codprov, codmunic, orden_vt));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_vt);       	
			}
		}
	
	
	

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_vt) {
		
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
        
        if (orden_vt != null){
    		jTextFieldOrden.setText(orden_vt);
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
