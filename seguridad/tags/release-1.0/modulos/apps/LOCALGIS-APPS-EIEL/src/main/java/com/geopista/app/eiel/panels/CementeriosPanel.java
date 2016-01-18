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
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.InventarioEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.dialogs.CementeriosDialog;
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
public class CementeriosPanel extends InventarioPanel implements FeatureExtendedPanel
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
    private JPanel jPanelInventario = null;
    private JLabel jLabelNombre = null;
	private JLabel jLabelTitular = null;
	private JLabel jLabelDistancia = null;
	private JLabel jLabelAcceso = null;
	private JLabel jLabelCapilla = null;
	private JLabel jLabelDeposito = null;
	private JLabel jLabelAmpliacion = null;
	private JLabel jLabelSaturacion = null;
	private JLabel jLabelSuperficie = null;
	private JLabel jLabelCrematorio = null;
	private JLabel jLabelFechaInst = null;
	private JLabel jLabelObserv = null;
	private JLabel jLabelFechaRevi = null;
	private JLabel jLabelEstado = null;
	private JLabel jLabelObraEjec = null;
	private JLabel jLabelAccesoRuedas = null;
	
	private JTextField jTextFieldNombre = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private JTextField jTextFieldDistancia = null;
	private ComboBoxEstructuras jComboBoxAcceso = null;
	private ComboBoxEstructuras jComboBoxCapilla = null;
	private ComboBoxEstructuras jComboBoxDeposito = null;
	private ComboBoxEstructuras jComboBoxAmpliacion = null;
	private JTextField jTextFieldSaturacion = null;
	private JTextField jTextFieldSuperficie = null;
	private ComboBoxEstructuras jComboBoxCrematorio = null;
	private DateField jTextFieldFechaInst = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldFechaRev = null;
	private ComboBoxEstructuras jComboBoxEstado = null;	
	private ComboBoxEstructuras jComboBoxObraEjec = null;
	private ComboBoxEstructuras jComboBoxAccesoRuedas = null;


	
    public CementeriosPanel(){
        super();
        initialize();
    }
  
    public CementeriosPanel(CementeriosEIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    public void loadData(CementeriosEIEL elemento){
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
            if (elemento.getTitular() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitular());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            if (elemento.getDistancia() != null){
        		jTextFieldDistancia.setText(elemento.getDistancia().toString());
        	}
        	else{
        		jTextFieldDistancia.setText("");
        	}
            if (elemento.getAcceso() != null){
        		jComboBoxAcceso.setSelectedPatron(elemento.getAcceso().toString());
        	}
        	else{
        		jComboBoxAcceso.setSelectedIndex(0);
        	}
            if (elemento.getCapilla() != null){
        		jComboBoxCapilla.setSelectedPatron(elemento.getCapilla().toString());
        	}
        	else{
        		jComboBoxCapilla.setSelectedIndex(0);
        	}
            if (elemento.getDepositoCadaveres() != null){
        		jComboBoxDeposito.setSelectedPatron(elemento.getDepositoCadaveres().toString());
        	}
        	else{
        		jComboBoxDeposito.setSelectedIndex(0);
        	}
            if (elemento.getAmpliacion() != null){
        		jComboBoxAmpliacion.setSelectedPatron(elemento.getAmpliacion().toString());
        	}
        	else{
        		jComboBoxAmpliacion.setSelectedIndex(0);
        	}
            if (elemento.getSaturacion() != null){
        		jTextFieldSaturacion.setText(elemento.getSaturacion().toString());
        	}
        	else{
        		jTextFieldSaturacion.setText("");
        	}
            if (elemento.getSuperficie() != null){
        		jTextFieldSuperficie.setText(elemento.getSuperficie().toString());
        	}
        	else{
        		jTextFieldSuperficie.setText("");
        	}
            if (elemento.getCrematorio() != null){
            	jComboBoxCrematorio.setSelectedPatron(elemento.getCrematorio().toString());
        	}
        	else{
        		jComboBoxCrematorio.setSelectedIndex(0);
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
				InventarioEIEL inventario = new InventarioEIEL();
				inventario.setIdBien(elemento.getIdBien());
            	jComboBoxNumInventario.setSelectedItem(inventario);
            }
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.CEMENTERIOS_CLAVE);
        	        	       	
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

    	Object object = AppContext.getApplicationContext().getBlackboard().get("cementerios_panel");    	
    	if (object != null && object instanceof CementeriosEIEL){    		
    		CementeriosEIEL elemento = (CementeriosEIEL)object;
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
            if (elemento.getTitular() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitular());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            if (elemento.getDistancia() != null){
        		jTextFieldDistancia.setText(elemento.getDistancia().toString());
        	}
        	else{
        		jTextFieldDistancia.setText("");
        	}
            if (elemento.getAcceso() != null){
        		jComboBoxAcceso.setSelectedPatron(elemento.getAcceso().toString());
        	}
        	else{
        		jComboBoxAcceso.setSelectedIndex(0);
        	}
            if (elemento.getCapilla() != null){
        		jComboBoxCapilla.setSelectedPatron(elemento.getCapilla().toString());
        	}
        	else{
        		jComboBoxCapilla.setSelectedIndex(0);
        	}
            if (elemento.getDepositoCadaveres() != null){
        		jComboBoxDeposito.setSelectedPatron(elemento.getDepositoCadaveres().toString());
        	}
        	else{
        		jComboBoxDeposito.setSelectedIndex(0);
        	}
            if (elemento.getAmpliacion() != null){
        		jComboBoxAmpliacion.setSelectedPatron(elemento.getAmpliacion().toString());
        	}
        	else{
        		jComboBoxAmpliacion.setSelectedIndex(0);
        	}
            if (elemento.getSaturacion() != null){
        		jTextFieldSaturacion.setText(elemento.getSaturacion().toString());
        	}
        	else{
        		jTextFieldSaturacion.setText("");
        	}
            if (elemento.getSuperficie() != null){
        		jTextFieldSuperficie.setText(elemento.getSuperficie().toString());
        	}
        	else{
        		jTextFieldSuperficie.setText("");
        	}
            if (elemento.getCrematorio() != null){
            	jComboBoxCrematorio.setSelectedPatron(elemento.getCrematorio().toString());
        	}
        	else{
        		jComboBoxCrematorio.setSelectedIndex(0);
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
            if (elemento.getFechaRevision() != null  && elemento.getFechaRevision().equals( new java.util.Date()) ){
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
    
    public CementeriosEIEL getCementerios (CementeriosEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new CementeriosEIEL();
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
            if (jTextFieldDistancia.getText()!=null && !jTextFieldDistancia.getText().equals("")){
            	elemento.setDistancia(new Float(jTextFieldDistancia.getText()));
            } else if (jTextFieldDistancia.getText().equals("")){
            	elemento.setDistancia(new Float(0));
            }
            if (jTextFieldSaturacion.getText()!=null && !jTextFieldSaturacion.getText().equals("")){
            	elemento.setSaturacion(new Float(jTextFieldSaturacion.getText()));
            } else if (jTextFieldSaturacion.getText().equals("")){
            	elemento.setSaturacion(new Float(0));
            }
            if (jTextFieldSuperficie.getText()!=null && !jTextFieldSuperficie.getText().equals("")){
            	elemento.setSuperficie(new Integer(jTextFieldSuperficie.getText()));
            } else if (jTextFieldSuperficie.getText().equals("")){
            	elemento.setSuperficie(new Integer(0));
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
            if (jComboBoxTitular.getSelectedPatron()!=null)
            	elemento.setTitular((String) jComboBoxTitular.getSelectedPatron());
            else elemento.setTitular("");
            if (jComboBoxAcceso.getSelectedPatron()!=null)
            	elemento.setAcceso((String) jComboBoxAcceso.getSelectedPatron());
            else elemento.setAcceso("");
            if (jComboBoxCapilla.getSelectedPatron()!=null)	
            	elemento.setCapilla((String) jComboBoxCapilla.getSelectedPatron());
            else elemento.setCapilla("");
            if (jComboBoxDeposito.getSelectedPatron()!=null)
            	elemento.setDepositoCadaveres((String) jComboBoxDeposito.getSelectedPatron());
            else elemento.setDepositoCadaveres("");
            if (jComboBoxAmpliacion.getSelectedPatron()!=null)
            	elemento.setAmpliacion((String) jComboBoxAmpliacion.getSelectedPatron());
            else elemento.setAmpliacion("");
            if (jComboBoxCrematorio.getSelectedPatron()!=null)
            	elemento.setCrematorio((String) jComboBoxCrematorio.getSelectedPatron());
            else elemento.setCrematorio("");            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            /* JTEXT - Date */
            if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){

            	java.sql.Date sqlDate = new java.sql.Date(
            			getjTextFieldFechaInst().getDate().getYear(),
            			getjTextFieldFechaInst().getDate().getMonth(),
            			getjTextFieldFechaInst().getDate().getDate()
            	);
            	elemento.setFechaInstalacion(sqlDate);
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
            } else if (jTextFieldFechaRev.getText().equals("")){
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
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.cementerios.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(CementeriosDialog.DIM_X,CementeriosDialog.DIM_Y);
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
   
    private JComboBox getJComboBoxEntidad() {
    	   if (jComboBoxEntidad  == null)
	        {
	        	jComboBoxEntidad = new JComboBox();
//	        	jComboBoxEntidad.setEditable(true);
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

	private JComboBox getJComboBoxNucleo() {
		if (jComboBoxNucleo == null) {
			jComboBoxNucleo = new JComboBox();
			jComboBoxNucleo.setRenderer(new UbicacionListCellRenderer());
		}
		return jComboBoxNucleo;
	}

	private JTextField getJTextFieldOrden() {
		if (jTextFieldOrden == null) {
			jTextFieldOrden = new TextField(3);
		}
		return jTextFieldOrden;
	}
    
    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private JTextField getjTextFieldNombre(){
    	if (jTextFieldNombre == null){
    		jTextFieldNombre = new TextField(40);
    		jTextFieldNombre.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldNombre, 40, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldNombre;
    }

    private ComboBoxEstructuras getJComboBoxTitular()
    { 
        if (jComboBoxTitular == null)
        {
            Estructuras.cargarEstructura("eiel_Titularidad de cementerio");
            jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTitular;        
    }
    
    private JTextField getjTextFieldDistancia(){
    	if (jTextFieldDistancia == null){
    		jTextFieldDistancia = new TextField(7);
    		jTextFieldDistancia.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDistancia,7, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDistancia;
    }

    private ComboBoxEstructuras getJComboBoxAcceso()
    { 
        if (jComboBoxAcceso == null)
        {
            Estructuras.cargarEstructura("eiel_Estado del acceso");
            jComboBoxAcceso = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAcceso.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAcceso;        
    }
    
    private ComboBoxEstructuras getJComboBoxCapilla()
    { 
        if (jComboBoxCapilla == null)
        {
            Estructuras.cargarEstructura("eiel_Capilla");
            jComboBoxCapilla = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCapilla.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCapilla;        
    }

    private ComboBoxEstructuras getJComboBoxDeposito()
    { 
        if (jComboBoxDeposito == null)
        {
            Estructuras.cargarEstructura("eiel_Depósito de cadáveres");
            jComboBoxDeposito = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxDeposito.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxDeposito;        
    }

    private ComboBoxEstructuras getJComboBoxAmpliacion()
    { 
        if (jComboBoxAmpliacion == null)
        {
            Estructuras.cargarEstructura("eiel_Posibilidad de ampliación");
            jComboBoxAmpliacion = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAmpliacion.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAmpliacion;        
    }
    
    private JTextField getjTextFieldSaturacion(){
    	if (jTextFieldSaturacion == null){
    		jTextFieldSaturacion = new TextField(10);
    		jTextFieldSaturacion.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSaturacion,2, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSaturacion;
    }
    
    private JTextField getjTextFieldSuperficie(){
    	if (jTextFieldSuperficie == null){
    		jTextFieldSuperficie = new TextField(10);
    		jTextFieldSuperficie.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSuperficie,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSuperficie;
    }
    
    private ComboBoxEstructuras getJComboBoxCrematorio()
    { 
        if (jComboBoxCrematorio == null)
        {									
            Estructuras.cargarEstructura("eiel_Existencia de crematorio");
            jComboBoxCrematorio = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCrematorio.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCrematorio;        
    }
    
    private DateField getjTextFieldFechaInst(){
    	if (jTextFieldFechaInst == null){
    		jTextFieldFechaInst = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaInst.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaInst.setEditable(true);;
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
    public CementeriosPanel(GridBagLayout layout){
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
            jLabelDistancia = new JLabel("", JLabel.CENTER); 
            jLabelDistancia.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.distancia_cementerio")); 
            jLabelAcceso = new JLabel("", JLabel.CENTER); 
            jLabelAcceso.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.acceso"));
            jLabelCapilla = new JLabel("", JLabel.CENTER); 
            jLabelCapilla.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capilla"));
            jLabelDeposito = new JLabel("", JLabel.CENTER); 
            jLabelDeposito.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.depos_cad"));
            jLabelAmpliacion = new JLabel("", JLabel.CENTER); 
            jLabelAmpliacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ampliacion"));
            jLabelSaturacion = new JLabel("", JLabel.CENTER); 
            jLabelSaturacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.saturacion"));
            jLabelSuperficie = new JLabel("", JLabel.CENTER); 
            jLabelSuperficie.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.superficie"));
            jLabelCrematorio = new JLabel("", JLabel.CENTER); 
            jLabelCrematorio.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.crematorio"));
            jLabelFechaInst = new JLabel("", JLabel.CENTER);
            jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechainicio"));
           
            jLabelObraEjec = new JLabel("", JLabel.CENTER); 
            jLabelObraEjec.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.obraejec")); 
            jLabelAccesoRuedas = new JLabel("", JLabel.CENTER); 
            jLabelAccesoRuedas.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.accesoruedas")); 
            
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            jLabelFechaInst = new JLabel("", JLabel.CENTER);
            jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechainicio"));
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelNombre,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldNombre(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelSuperficie,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldSuperficie(), 
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
            jPanelInformacion.add(jLabelDistancia,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldDistancia(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelAcceso,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxAcceso(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCapilla,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCapilla(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelDeposito,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxDeposito(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelAmpliacion,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxAmpliacion(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelSaturacion,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldSaturacion(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelFechaInst,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldFechaInst(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
           
            jPanelInformacion.add(jLabelCrematorio,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCrematorio(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelObraEjec,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJComboBoxObraEjec(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelAccesoRuedas,
                    new GridBagConstraints(0,6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJComboBoxAccesoRuedas(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 7, 2, 1, 0.1, 0.1,
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
                    new GridBagConstraints(1, 0,2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevision.add(jLabelEstado, 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevision.add(getJComboBoxEstado(), 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
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
	        	
	        	String orden_ce = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_ce"))!=null){
	        		orden_ce=(feature.getAttribute(esquema.getAttributeByColumn("orden_ce"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelCementeriosEIEL(clave, codprov, codmunic, entidad, nucleo, orden_ce));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, entidad, nucleo, orden_ce);       	
			}
		}
	
	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String entidad, String nucleo, String orden_ce) {

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
        
		if (entidad != null){
        	jComboBoxEntidad.setSelectedIndex(
        			entidadesSingularesIndexSeleccionar(entidad)
        	);
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(0);
        }
        
        if (nucleo != null){
        	jComboBoxNucleo.setSelectedIndex(
        			nucleoPoblacionIndexSeleccionar(nucleo)
        			);
        	
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(0);
        }
        
        if (orden_ce != null){
    		jTextFieldOrden.setText(orden_ce);
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
