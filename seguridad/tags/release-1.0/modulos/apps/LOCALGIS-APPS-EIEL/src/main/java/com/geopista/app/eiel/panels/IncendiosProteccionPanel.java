package com.geopista.app.eiel.panels;

import java.awt.Dimension;
import com.geopista.protocol.inventario.Inventario;
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
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.dialogs.IncendiosProteccionDialog;
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
import com.geopista.protocol.inventario.Inventario;
public class IncendiosProteccionPanel extends InventarioPanel implements FeatureExtendedPanel
{
   
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;
    
    private boolean okPressed = false;	

    private JLabel jLabelClave = null;
	private JLabel jLabelCodProv = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelCodEntidad = null;
	private JLabel jLabelCodPoblamiento = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldClave = null;
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JComboBox jComboBoxEntidad = null;
    private JComboBox jComboBoxNucleo = null;
    private JTextField jTextFieldOrden = null;
    
    private JLabel jLabelNombre = null;
	private JLabel jLabelTitular = null;
	private JLabel jLabelTipo = null;
	private JLabel jLabelGestor = null;
	private JLabel jLabelAmbito = null;
	private JLabel jLabelSCubierta = null;
	private JLabel jLabelSAire = null;
	private JLabel jLabelSSolar = null;
	private JLabel jLabelPlanProfe = null;
	private JLabel jLabelPlanVolun = null;
	private JLabel jLabelEstIp = null;
	private JLabel jLabelVehIncendio = null;
	private JLabel jLabelVehRescate = null;
	private JLabel jLabelAmbulancia = null;
	private JLabel jLabelMedAereos = null;
	private JLabel jLabelOtrosVehic = null;
	private JLabel jLabelQuitanieve = null;
	private JLabel jLabelDetecInce = null;
	private JLabel jLabelOtros = null;
	private JLabel jLabelFechaInst = null;
	private JLabel jLabelObserv = null;
	private JLabel jLabelFechaRevi = null;
	private JLabel jLabelEstado = null;
	private JLabel jLabelObraEjec = null;
	private JLabel jLabelAccesoRuedas = null;
	private JPanel jPanelInventario = null;
	private JTextField jTextFieldNombre = null;
	private ComboBoxEstructuras jComboBoxTipo = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private ComboBoxEstructuras jComboBoxAmbito = null;
	private JTextField jTextFieldSCubierta = null;
	private JTextField jTextFieldSAire = null;
	private JTextField jTextFieldSSolar = null;
	private JTextField jTextFieldPlanProfe = null;
	private JTextField jTextFieldPlanVolun = null;
	private ComboBoxEstructuras jComboBoxEstIp = null;
	private JTextField jTextVehIncendio = null;
	private JTextField jTextVehRescate = null;
	private JTextField jTextAmbulancia = null;
	private JTextField jTextMedAereo = null;
	private JTextField jTextOtrosVehic = null;
	private JTextField jTextQuitanieve = null;
	private JTextField jTextDetecInce = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private DateField jTextFieldFechaInst = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldFechaRev = null;
	private JTextField jTextFieldOtros = null;
	private ComboBoxEstructuras jComboBoxObraEjec = null;
	private ComboBoxEstructuras jComboBoxAccesoRuedas = null;
	
    public IncendiosProteccionPanel(){
        super();
        initialize();
    }
  
    public IncendiosProteccionPanel(IncendiosProteccionEIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    
    
    public void loadData(IncendiosProteccionEIEL elemento){
        if (elemento!=null){
        	/* Campos Clave */
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
            
			if (elemento.getCodINEEntidad() != null){
	        	jComboBoxEntidad.setSelectedIndex(
	        			entidadesSingularesIndexSeleccionar(elemento.getCodINEEntidad()));
	        }
	        else{
	        	jComboBoxEntidad.setSelectedIndex(0);
	        }
	        
	        if (elemento.getCodINEPoblamiento() != null){
	        	jComboBoxNucleo.setSelectedIndex(
	        			nucleoPoblacionIndexSeleccionar(elemento.getCodINEPoblamiento())
	        			);	
	        }
	        else{
	        	jComboBoxNucleo.setSelectedIndex(0);
	        }
            if (elemento.getOrden() != null){
        		jTextFieldOrden.setText(elemento.getOrden().toString());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            /* Otros Campos */
            if (elemento.getNombre() != null){
        		jTextFieldNombre.setText(elemento.getNombre().toString());
        	}
        	else{
        		jTextFieldNombre.setText("");
        	}
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            
            if (elemento.getTitular() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitular());
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
            if (elemento.getAmbito() != null){
            	jComboBoxAmbito.setSelectedPatron(elemento.getAmbito().toString());
        	}
        	else{
        		jComboBoxAmbito.setSelectedIndex(0);
        	}
            if (elemento.getSuperficieCubierta() != null){
        		jTextFieldSCubierta.setText(elemento.getSuperficieCubierta().toString());
        	}
        	else{
        		jTextFieldSCubierta.setText("");
        	}
            if (elemento.getSuperficieAireLibre() != null){
        		jTextFieldSAire.setText(elemento.getSuperficieAireLibre().toString());
        	}
        	else{
        		jTextFieldSAire.setText("");
        	}
            if (elemento.getSuperficieSolar() != null){
        		jTextFieldSSolar.setText(elemento.getSuperficieSolar().toString());
        	}
        	else{
        		jTextFieldSSolar.setText("");
        	}
            if (elemento.getPlantillaProfesionales() != null){
        		jTextFieldPlanProfe.setText(elemento.getPlantillaProfesionales().toString());
        	}
        	else{
        		jTextFieldPlanProfe.setText("");
        	}
            if (elemento.getPlantillaVoluntarios() != null){
        		jTextFieldPlanVolun.setText(elemento.getPlantillaVoluntarios().toString());
        	}
        	else{
        		jTextFieldPlanVolun.setText("");
        	}
            if (elemento.getEstado() != null){
            	jComboBoxEstIp.setSelectedPatron(elemento.getEstado().toString());
        	}
        	else{
        		jComboBoxEstIp.setSelectedIndex(0);
        	}
            if (elemento.getVechiculosIncendios() != null){
            	jTextVehIncendio.setText(elemento.getVechiculosIncendios().toString());
        	}
        	else{
        		jTextVehIncendio.setText("");
        	}
            if (elemento.getVechiculosRescate() != null){
            	jTextVehRescate.setText(elemento.getVechiculosRescate().toString());
        	}
        	else{
        		jTextVehRescate.setText("");
        	}
            if (elemento.getAmbulancias() != null){
            	jTextAmbulancia.setText(elemento.getAmbulancias().toString());
        	}
        	else{
        		jTextAmbulancia.setText("");
        	}
            if (elemento.getMediosAereos() != null){
            	jTextMedAereo.setText(elemento.getMediosAereos().toString());
        	}
        	else{
        		jTextMedAereo.setText("");
        	}
            if (elemento.getOtrosVehiculos() != null){
            	jTextOtrosVehic.setText(elemento.getOtrosVehiculos().toString());
        	}
        	else{
        		jTextOtrosVehic.setText("");
        	}
            if (elemento.getQuitanieves() != null){
            	jTextQuitanieve.setText(elemento.getQuitanieves().toString());
        	}
        	else{
        		jTextQuitanieve.setText("");
        	}
            if (elemento.getSistemasDeteccionIncencios() != null){
            	jTextDetecInce.setText(elemento.getSistemasDeteccionIncencios().toString());
        	}
        	else{
        		jTextDetecInce.setText("");
        	}
            if (elemento.getOtros() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getOtros().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedPatron("");
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
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(dateFormat.format(elemento.getFechaRevision()));
        	}
        	else{
        	    
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
            if (elemento.getObra_ejec() != null){
            	jComboBoxObraEjec.setSelectedPatron(elemento.getObra_ejec().toString());
        	}
        	else{
        		jComboBoxObraEjec.setSelectedIndex(0);
        	}
            if (elemento.getAcceso_s_ruedas() != null){
            	jComboBoxAccesoRuedas.setSelectedPatron(elemento.getAcceso_s_ruedas().toString());
        	}
        	else{
        		jComboBoxAccesoRuedas.setSelectedIndex(0);
        	}

            if (elemento.getTitularidadMunicipal() != null){
            	jComboBoxTitularidadMunicipal.setSelectedPatron(elemento.getTitularidadMunicipal());
        	}
        	else{
        		jComboBoxTitularidadMunicipal.setSelectedIndex(0);
        	}
            
            if (elemento.getEpigInventario() != null){
            	jComboBoxEpigInventario.setSelectedPatron(elemento.getEpigInventario().toString());
        	}
        	else{
        		if (jComboBoxEpigInventario.isEnabled())
        			jComboBoxEpigInventario.setSelectedIndex(0);
        	}
            
            if (elemento.getIdBien() != null && elemento.getIdBien() != 0){
            	jComboBoxNumInventario.setEnabled(true);
				EdicionOperations oper = new EdicionOperations();          			
				EdicionUtils.cargarLista(getJComboBoxNumInventario(), oper.obtenerNumInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron())));
				Inventario inventario = new Inventario();
				inventario.setId(elemento.getIdBien());
            	jComboBoxNumInventario.setSelectedItem(inventario);
            }
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.INCENDIOSPROTECCION_CLAVE);
        	        	       	
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
        
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("incendiosproteccion_panel");    	
    	if (object != null && object instanceof IncendiosProteccionEIEL){    		
    		IncendiosProteccionEIEL elemento = (IncendiosProteccionEIEL)object;
    		
        	/* Campos Clave */
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
			 if (elemento.getCodINEEntidad() != null){
		        	jComboBoxEntidad.setSelectedIndex(
		        			entidadesSingularesIndexSeleccionar(elemento.getCodINEEntidad())
		        	);
			 }
			 else{
				 jComboBoxEntidad.setSelectedIndex(0);
			 }
		        
			 if (elemento.getCodINEPoblamiento() != null){
				 jComboBoxNucleo.setSelectedIndex(
						 nucleoPoblacionIndexSeleccionar(elemento.getCodINEPoblamiento())
				 );
		        	
			 }
			 else{
				 jComboBoxNucleo.setSelectedIndex(0);
		     }
			 
            if (elemento.getOrden() != null){
        		jTextFieldOrden.setText(elemento.getOrden().toString());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            /* Otros Campos */
            if (elemento.getNombre() != null){
        		jTextFieldNombre.setText(elemento.getNombre().toString());
        	}
        	else{
        		jTextFieldNombre.setText("");
        	}
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            
            if (elemento.getTitular() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitular());
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
            if (elemento.getAmbito() != null){
            	jComboBoxAmbito.setSelectedPatron(elemento.getAmbito().toString());
        	}
        	else{
        		jComboBoxAmbito.setSelectedIndex(0);
        	}
            if (elemento.getSuperficieCubierta() != null){
        		jTextFieldSCubierta.setText(elemento.getSuperficieCubierta().toString());
        	}
        	else{
        		jTextFieldSCubierta.setText("");
        	}
            if (elemento.getSuperficieAireLibre() != null){
        		jTextFieldSAire.setText(elemento.getSuperficieAireLibre().toString());
        	}
        	else{
        		jTextFieldSAire.setText("");
        	}
            if (elemento.getSuperficieSolar() != null){
        		jTextFieldSSolar.setText(elemento.getSuperficieSolar().toString());
        	}
        	else{
        		jTextFieldSSolar.setText("");
        	}
            if (elemento.getPlantillaProfesionales() != null){
        		jTextFieldPlanProfe.setText(elemento.getPlantillaProfesionales().toString());
        	}
        	else{
        		jTextFieldPlanProfe.setText("");
        	}
            if (elemento.getPlantillaVoluntarios() != null){
        		jTextFieldPlanVolun.setText(elemento.getPlantillaVoluntarios().toString());
        	}
        	else{
        		jTextFieldPlanVolun.setText("");
        	}
            if (elemento.getEstado() != null){
            	jComboBoxEstIp.setSelectedPatron(elemento.getEstado().toString());
        	}
        	else{
        		jComboBoxEstIp.setSelectedIndex(0);
        	}
            if (elemento.getVechiculosIncendios() != null){
            	jTextVehIncendio.setText(elemento.getVechiculosIncendios().toString());
        	}
        	else{
        		jTextVehIncendio.setText("");
        	}
            if (elemento.getVechiculosRescate() != null){
            	jTextVehRescate.setText(elemento.getVechiculosRescate().toString());
        	}
        	else{
        		jTextVehRescate.setText("");
        	}
            if (elemento.getAmbulancias() != null){
            	jTextAmbulancia.setText(elemento.getAmbulancias().toString());
        	}
        	else{
        		jTextAmbulancia.setText("");
        	}
            if (elemento.getMediosAereos() != null){
            	jTextMedAereo.setText(elemento.getMediosAereos().toString());
        	}
        	else{
        		jTextMedAereo.setText("");
        	}
            if (elemento.getOtrosVehiculos() != null){
            	jTextOtrosVehic.setText(elemento.getOtrosVehiculos().toString());
        	}
        	else{
        		jTextOtrosVehic.setText("");
        	}
            if (elemento.getQuitanieves() != null){
            	jTextQuitanieve.setText(elemento.getQuitanieves().toString());
        	}
        	else{
        		jTextQuitanieve.setText("");
        	}
            if (elemento.getSistemasDeteccionIncencios() != null){
            	jTextDetecInce.setText(elemento.getSistemasDeteccionIncencios().toString());
        	}
        	else{
        		jTextDetecInce.setText("");
        	}
            if (elemento.getOtros() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getOtros().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedPatron("");
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
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(dateFormat.format(elemento.getFechaRevision()));
        	}
        	else{
        	    
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
            if (elemento.getObra_ejec() != null){
            	jComboBoxObraEjec.setSelectedPatron(elemento.getObra_ejec().toString());
        	}
        	else{
        		jComboBoxObraEjec.setSelectedIndex(0);
        	}
            if (elemento.getAcceso_s_ruedas() != null){
            	jComboBoxAccesoRuedas.setSelectedPatron(elemento.getAcceso_s_ruedas().toString());
        	}
        	else{
        		jComboBoxAccesoRuedas.setSelectedIndex(0);
        	}

			if (elemento.getEpigInventario() != null){
				jComboBoxEpigInventario.setSelectedPatron(elemento.getEpigInventario().toString());
			}
			else{
				if (jComboBoxEpigInventario.isEnabled())
					jComboBoxEpigInventario.setSelectedIndex(0);
			}
			
			if (elemento.getIdBien() != null && elemento.getIdBien() != 0){
				jComboBoxNumInventario.setSelectedItem(elemento.getIdBien());
			}
			else{
				if (jComboBoxNumInventario.isEnabled())
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
    
    
    public IncendiosProteccionEIEL getIncendiosProteccion (IncendiosProteccionEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new IncendiosProteccionEIEL();
            }
            /* Claves: COMBOBOX  y JTEXT */
            elemento.setClave(jTextFieldClave.getText());
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));
            
            elemento.setOrden(jTextFieldOrden.getText());
            /* JTEXT - Integer y Float*/
            if (jTextFieldSCubierta.getText()!=null && !jTextFieldSCubierta.getText().equals("")){
            	elemento.setSuperficieCubierta(new Integer(jTextFieldSCubierta.getText()));
            } else if (jTextFieldSCubierta.getText().equals("")){
            	elemento.setSuperficieCubierta(new Integer(0));
            }
            if (jTextFieldSAire.getText()!=null && !jTextFieldSAire.getText().equals("")){
            	elemento.setSuperficieAireLibre(new Integer(jTextFieldSAire.getText()));
            } else if (jTextFieldSAire.getText().equals("")){
            	elemento.setSuperficieAireLibre(new Integer(0));
            }
            if (jTextFieldSSolar.getText()!=null && !jTextFieldSSolar.getText().equals("")){
            	elemento.setSuperficieSolar(new Integer(jTextFieldSSolar.getText()));
            } else if (jTextFieldSSolar.getText().equals("")){
            	elemento.setSuperficieSolar(new Integer(0));
            }
            if (jTextFieldPlanProfe.getText()!=null && !jTextFieldPlanProfe.getText().equals("")){
            	elemento.setPlantillaProfesionales(new Integer(jTextFieldPlanProfe.getText()));
            } else if (jTextFieldPlanProfe.getText().equals("")){
            	elemento.setPlantillaProfesionales(new Integer(0));
            }
            if (jTextFieldPlanVolun.getText()!=null && !jTextFieldPlanVolun.getText().equals("")){
            	elemento.setPlantillaVoluntarios(new Integer(jTextFieldPlanVolun.getText()));
            } else if (jTextFieldPlanVolun.getText().equals("")){
            	elemento.setPlantillaVoluntarios(new Integer(0));
            }
            if (jTextVehIncendio.getText()!=null && !jTextVehIncendio.getText().equals("")){
            	elemento.setVechiculosIncendios(new Integer(jTextVehIncendio.getText()));
            } else if (jTextVehIncendio.getText().equals("")){
            	elemento.setVechiculosIncendios(new Integer(0));
            }
            if (jTextVehRescate.getText()!=null && !jTextVehRescate.getText().equals("")){
            	elemento.setVechiculosRescate(new Integer(jTextVehRescate.getText()));
            } else if (jTextVehRescate.getText().equals("")){
            	elemento.setVechiculosRescate(new Integer(0));
            }
            if (jTextAmbulancia.getText()!=null && !jTextAmbulancia.getText().equals("")){
            	elemento.setAmbulancias(new Integer(jTextAmbulancia.getText()));
            } else if (jTextAmbulancia.getText().equals("")){
            	elemento.setAmbulancias(new Integer(0));
            }
            if (jTextMedAereo.getText()!=null && !jTextMedAereo.getText().equals("")){
            	elemento.setMediosAereos(new Integer(jTextMedAereo.getText()));
            } else if (jTextMedAereo.getText().equals("")){
            	elemento.setMediosAereos(new Integer(0));
            }
            if (jTextOtrosVehic.getText()!=null && !jTextOtrosVehic.getText().equals("")){
            	elemento.setOtrosVehiculos(new Integer(jTextOtrosVehic.getText()));
            } else if (jTextOtrosVehic.getText().equals("")){
            	elemento.setOtrosVehiculos(new Integer(0));
            }
            if (jTextQuitanieve.getText()!=null && !jTextQuitanieve.getText().equals("")){
            	elemento.setQuitanieves(new Integer(jTextQuitanieve.getText()));
            } else if (jTextQuitanieve.getText().equals("")){
            	elemento.setQuitanieves(new Integer(0));
            }
            if (jTextDetecInce.getText()!=null && !jTextDetecInce.getText().equals("")){
            	elemento.setSistemasDeteccionIncencios(new Integer(jTextDetecInce.getText()));
            } else if (jTextDetecInce.getText().equals("")){
            	elemento.setSistemasDeteccionIncencios(new Integer(0));
            }
            if (jTextFieldOtros.getText()!=null && !jTextFieldOtros.getText().equals("")){
            	elemento.setOtros(new Integer(jTextFieldOtros.getText()));
            } else if (jTextQuitanieve.getText().equals("")){
            	elemento.setOtros(new Integer(0));
            }
            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));

            if (jComboBoxObraEjec.getSelectedPatron()!=null)
            	elemento.setObra_ejec(jComboBoxObraEjec.getSelectedPatron());
            else elemento.setObra_ejec("");
            if (jComboBoxAccesoRuedas.getSelectedPatron()!=null)
            	elemento.setObra_ejec(jComboBoxAccesoRuedas.getSelectedPatron());
            else elemento.setAcceso_s_ruedas("");
            
            /* JTEXT - String */
            if (jTextFieldNombre.getText()!=null) {
            	elemento.setNombre(jTextFieldNombre.getText());
        	}
            
            if (jComboBoxTipo.getSelectedPatron()!=null)
	            elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
            else elemento.setTipo("");
            if (jComboBoxTitular.getSelectedPatron()!=null)
	            elemento.setTitular((String) jComboBoxTitular.getSelectedPatron());
            else elemento.setTitular("");
            if (jComboBoxGestor.getSelectedPatron()!=null)
	            elemento.setGestor((String) jComboBoxGestor.getSelectedPatron());
            else elemento.setGestor("");
            if (jComboBoxAmbito.getSelectedPatron()!=null)
	            elemento.setAmbito((String) jComboBoxAmbito.getSelectedPatron());
            else elemento.setAmbito("");
            if (jComboBoxEstIp.getSelectedPatron()!=null)
	            elemento.setEstado((String) jComboBoxEstIp.getSelectedPatron());
            else elemento.setEstado("");
            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            /* JTEXT - Date */
            if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){
            	java.util.Date fecha = jTextFieldFechaInst.getDate();
            	elemento.setFechaInstalacion(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));
            } else{
            	elemento.setFechaInstalacion(null);
            }
            if (jTextFieldFechaRev.getText()!=null && !jTextFieldFechaRev.getText().equals("")){
            	java.util.Date fecha = new java.util.Date(jTextFieldFechaRev.getText());
            	elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));
            } else {
            	elemento.setFechaRevision(null);
            }
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
    
    private void initialize(){      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.centrosincendios.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(IncendiosProteccionDialog.DIM_X,IncendiosProteccionDialog.DIM_Y);
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
			ArrayList<Provincia> listaProvincias = oper.obtenerProvinciasConNombre();
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
						if (municipio.getCodMunicipio()!=null){
	            			EdicionOperations oper = new EdicionOperations();
	            			EdicionUtils.cargarLista(getJComboBoxEntidad(), oper.obtenerEntidadesConNombre(municipio));
//	            			System.out.println("PASO1");
	            			//EdicionUtils.cargarLista(jComboBoxNucleo, oper.obtenerNucleosConNombre(entidad)); 
            			}

					}
				}
			});
		}
		return jComboBoxMunicipio;
    }
   
    private JComboBox getJComboBoxEntidad()
    {
    	if (jComboBoxEntidad  == null)
		{
			jComboBoxEntidad = new JComboBox();
//		     	jComboBoxEntidad.setEditable(true);
			jComboBoxEntidad.setRenderer(new UbicacionListCellRenderer());
			jComboBoxEntidad.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{   
					
					if (jComboBoxEntidad.getSelectedIndex()==0)
					{
						jComboBoxNucleo.removeAllItems();
					}
					else
					{
						EntidadesSingularesEIEL entidad = new EntidadesSingularesEIEL();
						
						if (jComboBoxProvincia.getSelectedItem() != null)
							entidad.setCodINEProvincia(((Provincia)jComboBoxProvincia.getSelectedItem()).getIdProvincia());
						if (jComboBoxMunicipio.getSelectedItem() != null && !jComboBoxMunicipio.getSelectedItem().equals(""))
							entidad.setCodINEMunicipio(
									((Municipio)jComboBoxMunicipio.getSelectedItem()).getIdIne()
		            						);
		            	if (jComboBoxEntidad.getSelectedItem() != null && !jComboBoxEntidad.getSelectedItem().equals(""))
		            		entidad.setCodINEEntidad(((EntidadesSingularesEIEL)jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
				
		            			
		            	if (entidad.getCodINEEntidad()!=null){
		            		EdicionOperations oper = new EdicionOperations();
		            		EdicionUtils.cargarLista(getJComboBoxNucleo(), 
		                					oper.obtenerNucleosConNombre(entidad));
		            	}
					}

				}
			});
		}
		return jComboBoxEntidad;
    }
    
    private JComboBox getJComboBoxNucleo(){
        if (jComboBoxNucleo  == null){
        	jComboBoxNucleo = new JComboBox();
        	jComboBoxNucleo.setRenderer(new UbicacionListCellRenderer());
        }
        return jComboBoxNucleo;
    }
    
    private JTextField getJTextFieldOrden(){
        if (jTextFieldOrden == null){
        	jTextFieldOrden = new TextField(3);
        }
        return jTextFieldOrden;
    }
    
    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private JTextField getjTextFieldNombre(){
    	if (jTextFieldNombre == null){
    		jTextFieldNombre = new TextField(50);
    		jTextFieldNombre.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldNombre, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldNombre;
    }

    private ComboBoxEstructuras getJComboBoxTipo()
    { 
        if (jComboBoxTipo == null)
        {
            Estructuras.cargarEstructura("eiel_Tipo de Centro de Protección Civil");
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
            Estructuras.cargarEstructura("eiel_Titularidad del Centro de Protección Civil");
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
            Estructuras.cargarEstructura("eiel_Gestor Extincion de Incendios y Proteccion Civil");
            jComboBoxGestor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGestor;        
    }
    
    private ComboBoxEstructuras getJComboBoxAmbito()
    { 
        if (jComboBoxAmbito == null)
        {
            Estructuras.cargarEstructura("eiel_ámbito territorial");
            jComboBoxAmbito = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAmbito.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAmbito;        
    }
    
    private JTextField getjTextFieldSCubierta(){
    	if (jTextFieldSCubierta == null){
    		jTextFieldSCubierta = new TextField(10);
    		jTextFieldSCubierta.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSCubierta,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSCubierta;
    }

    private JTextField getjTextFieldSAire(){
    	if (jTextFieldSAire == null){
    		jTextFieldSAire = new TextField(10);
    		jTextFieldSAire.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSAire,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSAire;
    }
    
    private JTextField getjTextFieldSSolar(){
    	if (jTextFieldSSolar == null){
    		jTextFieldSSolar = new TextField(10);
    		jTextFieldSSolar.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSSolar,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSSolar;
    }
    
    private JTextField getjTextFieldPlanProfe(){
    	if (jTextFieldPlanProfe == null){
    		jTextFieldPlanProfe = new TextField(10);
    		jTextFieldPlanProfe.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPlanProfe,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPlanProfe;
    }
    
    private JTextField getjTextFieldPlanVolun(){
    	if (jTextFieldPlanVolun == null){
    		jTextFieldPlanVolun = new TextField(10);
    		jTextFieldPlanVolun.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPlanVolun,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPlanVolun;
    }
    
    private ComboBoxEstructuras getJComboBoxEstIp()
    { 
        if (jComboBoxEstIp == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEstIp = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEstIp.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstIp;        
    }
    
    private JTextField getjTextVehIncendio(){
    	if (jTextVehIncendio == null){
    		jTextVehIncendio = new TextField(10);
    		jTextVehIncendio.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextVehIncendio,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextVehIncendio;
    }
    private JTextField getjTextVehRescate(){
    	if (jTextVehRescate == null){
    		jTextVehRescate = new TextField(10);
    		jTextVehRescate.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextVehRescate,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextVehRescate;
    }
    private JTextField getjTextAmbulancia(){
    	if (jTextAmbulancia == null){
    		jTextAmbulancia = new TextField(10);
    		jTextAmbulancia.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextAmbulancia,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextAmbulancia;
    }
    private JTextField getjTextMedAereo(){
    	if (jTextMedAereo == null){
    		jTextMedAereo = new TextField(10);
    		jTextMedAereo.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextMedAereo,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextMedAereo;
    }
    private JTextField getjTextOtrosVehic(){
    	if (jTextOtrosVehic == null){
    		jTextOtrosVehic = new TextField(10);
    		jTextOtrosVehic.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextOtrosVehic,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextOtrosVehic;
    }
    private JTextField getjTextQuitanieve(){
    	if (jTextQuitanieve == null){
    		jTextQuitanieve = new TextField(10);
    		jTextQuitanieve.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextQuitanieve,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextQuitanieve;
    }
    private JTextField getjTextDetecInce(){
    	if (jTextDetecInce == null){
    		jTextDetecInce = new TextField(10);
    		jTextDetecInce.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextDetecInce,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextDetecInce;
    }
    private JTextField getJTextFieldOtros(){
    	 if (jTextFieldOtros == null)
         {
             jTextFieldOtros = new TextField(10);
             jTextFieldOtros.addCaretListener(new CaretListener(){
     			public void caretUpdate(CaretEvent evt){
     				EdicionUtils.chequeaLongYCharCampoEdit(jTextDetecInce,10, aplicacion.getMainFrame());
     			}
     		});
         }
         return jTextFieldOtros;    
    }

    private DateField getjTextFieldFechaInst(){
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
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv,50, aplicacion.getMainFrame());
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
    

    private ComboBoxEstructuras getJComboBoxAccesoRuedas()
    { 
        if (jComboBoxAccesoRuedas == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            jComboBoxAccesoRuedas = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAccesoRuedas.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAccesoRuedas;        
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
    
    
    public IncendiosProteccionPanel(GridBagLayout layout){
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
            jLabelCodEntidad  = new JLabel("", JLabel.CENTER);
            jLabelCodEntidad.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codentidad")));
            jLabelCodPoblamiento = new JLabel("", JLabel.CENTER);
            jLabelCodPoblamiento.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nucleo")));
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
            jPanelIdentificacion.add(jLabelCodEntidad, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(getJComboBoxEntidad(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(jLabelCodPoblamiento, 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(getJComboBoxNucleo(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(jLabelOrden, 
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(getJTextFieldOrden(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            for (int i=0; i < jPanelIdentificacion.getComponentCount(); i++){
            	jPanelIdentificacion.getComponent(i).setEnabled(false);
            }
            
        }
        return jPanelIdentificacion;
    }
    
    /**
     * This method initializes jPanelDatosInformacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosInformacion(){
        if (jPanelInformacion == null){   
        	jPanelInformacion  = new JPanel(new GridBagLayout());
        	jPanelInformacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.info"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	/* Definicion de LABEL'S */
        	jLabelNombre = new JLabel("", JLabel.CENTER); 
        	jLabelNombre.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nombre"));
            jLabelTitular = new JLabel("", JLabel.CENTER); 
            jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular")); 
            jLabelTipo = new JLabel("", JLabel.CENTER); 
            jLabelTipo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo")); 
            jLabelGestor = new JLabel("", JLabel.CENTER); 
            jLabelGestor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestor"));
            jLabelAmbito = new JLabel("", JLabel.CENTER); 
            jLabelAmbito.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ambito"));
            jLabelSCubierta = new JLabel("", JLabel.CENTER); 
            jLabelSCubierta.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_cubierta_id"));
            jLabelSAire = new JLabel("", JLabel.CENTER); 
            jLabelSAire.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_aire_id"));
            jLabelSSolar = new JLabel("", JLabel.CENTER); 
            jLabelSSolar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_solar_id"));
            jLabelPlanProfe = new JLabel("", JLabel.CENTER); 
            jLabelPlanProfe.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.plan_prof"));
            jLabelPlanVolun = new JLabel("", JLabel.CENTER); 
            jLabelPlanVolun.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.plan_volun"));
            jLabelEstIp = new JLabel("", JLabel.CENTER); 
            jLabelEstIp.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado1"));
            jLabelVehIncendio = new JLabel("", JLabel.CENTER); 
            jLabelVehIncendio.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.veh_incend"));
            jLabelVehRescate = new JLabel("", JLabel.CENTER); 
            jLabelVehRescate.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.veh_rescate"));
            jLabelAmbulancia = new JLabel("", JLabel.CENTER); 
            jLabelAmbulancia.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ambu"));
            jLabelMedAereos = new JLabel("", JLabel.CENTER); 
            jLabelMedAereos.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.med_aereos"));
            jLabelOtrosVehic = new JLabel("", JLabel.CENTER); 
            jLabelOtrosVehic.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.otros_v"));
            jLabelQuitanieve = new JLabel("", JLabel.CENTER); 
            jLabelQuitanieve.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.quitanieve"));
            jLabelDetecInce = new JLabel("", JLabel.CENTER); 
            jLabelDetecInce.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.detec_incend"));
            jLabelOtros = new JLabel("", JLabel.CENTER); 
            jLabelOtros.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.otros"));
            jLabelFechaInst = new JLabel("", JLabel.CENTER);
            jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechainst"));
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            jLabelObraEjec = new JLabel("", JLabel.CENTER); 
            jLabelObraEjec.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.obraejec")); 
            jLabelAccesoRuedas = new JLabel("", JLabel.CENTER); 
            jLabelAccesoRuedas.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.accesoruedas")); 
            
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelNombre,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldNombre(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelGestor,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxGestor(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelTitular,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxTitular(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelTipo,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxTipo(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelAmbito,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxAmbito(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelSCubierta,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldSCubierta(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelSAire,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldSAire(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelSSolar,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldSSolar(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPlanProfe,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPlanProfe(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPlanVolun,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPlanVolun(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelEstIp,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxEstIp(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVehIncendio,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextVehIncendio(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVehRescate,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextVehRescate(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelAmbulancia,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextAmbulancia(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelMedAereos,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextMedAereo(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelOtrosVehic,
                    new GridBagConstraints(2, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextOtrosVehic(), 
                    new GridBagConstraints(3, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelQuitanieve,
                    new GridBagConstraints(0, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextQuitanieve(), 
                    new GridBagConstraints(1, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelDetecInce,
                    new GridBagConstraints(2, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextDetecInce(), 
                    new GridBagConstraints(3, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelOtros,
                    new GridBagConstraints(0, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldOtros(), 
                    new GridBagConstraints(1, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelFechaInst,
                    new GridBagConstraints(2, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldFechaInst(), 
                    new GridBagConstraints(3, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 40, 0));
            jPanelInformacion.add(jLabelObraEjec,
                    new GridBagConstraints(0, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJComboBoxObraEjec(), 
                    new GridBagConstraints(1, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelAccesoRuedas,
                    new GridBagConstraints(2,10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJComboBoxAccesoRuedas(), 
                    new GridBagConstraints(3, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 11, 2, 1, 0.1, 0.1,
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
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevision.add(jLabelEstado, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevision.add(getJComboBoxEstado(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 100, 0));
            
            
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
        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0) &&
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
	        	 
	        	String entidad = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codentidad"))!=null){
	        		entidad=(feature.getAttribute(esquema.getAttributeByColumn("codentidad"))).toString();
	        	}
	        	
	        	String nucleo = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codpoblamiento"))!=null){
	        		nucleo=(feature.getAttribute(esquema.getAttributeByColumn("codpoblamiento"))).toString();
	        	}
	        	
	        	String orden_ip = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_ip"))!=null){
	        		orden_ip=(feature.getAttribute(esquema.getAttributeByColumn("orden_ip"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelCentrosIncendiosEIEL(clave, codprov, codmunic, entidad, nucleo, orden_ip));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, entidad, nucleo, orden_ip);       	
			}
		}
	
	
	
	
	
	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String entidad, String nucleo, String orden_ip) {
		
		//Datos identificacion
    	if (clave != null){
    		jTextFieldClave.setText(clave);
    	}
    	else{
    		jTextFieldClave.setText("");
    	}
        
    	if (codprov != null){
        	jComboBoxProvincia.setSelectedItem(codprov);
        }
        else{
        	jComboBoxProvincia.setSelectedIndex(0);
        }
        
        if (codmunic != null){            	
        	jComboBoxMunicipio.setSelectedItem(codmunic);
        }
        else{
        	jComboBoxMunicipio.setSelectedIndex(0);
        }
        
        if (entidad != null){
        	jComboBoxEntidad.setSelectedItem(entidad);
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(0);
        }
        
        if (nucleo != null){
        	jComboBoxNucleo.setSelectedItem(nucleo);
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(-1);
        }
        
        if (orden_ip != null){
    		jTextFieldOrden.setText(orden_ip);
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
	
	public int nucleoPoblacionIndexSeleccionar(String nucleoPoblacion){
		for (int i = 0; i < jComboBoxNucleo.getItemCount(); i ++){
			if (((NucleosPoblacionEIEL)jComboBoxNucleo.getItemAt(i)).getCodINEPoblamiento().equals(nucleoPoblacion) ){
				return i;
			}
		}
		
		return -1;
	}
	
	public int entidadesSingularesIndexSeleccionar(String entidadSingular){
		for (int i = 0; i < jComboBoxEntidad.getItemCount(); i ++){
			if (((EntidadesSingularesEIEL)jComboBoxEntidad.getItemAt(i)).getCodINEEntidad().equals(entidadSingular) ){
				return i;
			}
		}
		
		return -1;
	}
    
}
