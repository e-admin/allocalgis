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
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.TableModel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.TipoDeporte;
import com.geopista.app.eiel.dialogs.InstalacionDeportivaDialog;
import com.geopista.app.eiel.models.TipoDeporteEIELTableModel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.utilidades.TableSorted;
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
public class InstalacionDeportivaPanel extends InventarioPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelClave = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelEntSing = null;
	private JLabel jLabelNucleo = null;
	private JTextField jTextFieldClave = null;
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JComboBox jComboBoxEntidad = null;
	private JComboBox jComboBoxNucleo = null;
	private JLabel jLabelNombre = null;
	private JLabel jLabelTipo = null;
	private JLabel jLabelTipoDeporte = null;
	private JLabel jLabelTitular = null;
	private JLabel jLabelGestor = null;
	private JLabel jLabelSupCubierta = null;
	private JLabel jLabelSupAire = null;
	private JLabel jLabelSupSolar = null;
	private JLabel jLabelEstado = null;
	private JLabel jLabelObraEjec = null;
	private JLabel jLabelAccesoRuedas = null;
	private JLabel jLabelFechaInstal = null;
	private JLabel jLabelObserv = null;
	private JTextField jTextFieldNombre = null;
	private ComboBoxEstructuras jComboBoxTipo = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private JTextField jTextFieldSupCubierta = null;
	private JTextField jTextFieldSupAire = null;
	private JTextField jTextFieldSupSolar = null;
	private ComboBoxEstructuras jComboBoxEst = null;
	private ComboBoxEstructuras jComboBoxObraEjec = null;
	private ComboBoxEstructuras jComboBoxAccesoRuedas = null;
	private JPanel jPanelInventario = null;
	private JTextField jTextFieldObserv = null;
	private DateField jTextFieldFechaInstal = null;
	private DateField jTextFieldFechaInstalUso = null;
	private JPanel jPanelRevision = null;
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstadoRevision = null;
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldOrden = null;
	private JScrollPane jScroll;
	private JTextField jTextFieldOrdenIdDeportes = null;

	private JLabel jLabelOrdenIdDeportes;


	private JPanel jPanelTiposDeportes;

	private JPanel jPanelDatosTipos;

	private JPanel jPanelBotonera;

	private JPanel jPanelListaTipos;

	private JScrollPane jScrollPaneListaTipos;

	private JTable jTableListaTipos;

	private JLabel jLabelObservaciones;

	private TextField jTextFieldObservaciones;

	private TipoDeporteEIELTableModel tableListaTiposModel;

	private InstalacionesDeportivasEIEL instalacionDeportiva = null;

	private JButton jButtonAniadir;

	private JButton jButtonLimpiar;

	private JButton jButtonEliminar;

	private JPanel jPanelInformacionGlobal;

	private ComboBoxEstructuras jComboBoxTipoDeporte;

	
    /**
     * This method initializes
     * 
     */
    public InstalacionDeportivaPanel()
    {
        super();
        initialize();
    }
    
    public InstalacionDeportivaPanel(InstalacionesDeportivasEIEL instalacion)
    {
        super();
        this.instalacionDeportiva = instalacion;
        initialize();
        loadData (instalacion);
    }
    
    public void loadData(InstalacionesDeportivasEIEL elemento)
    {
        if (elemento!=null)
        {
        	this.instalacionDeportiva = elemento;
        	
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
            if (elemento.getOrdenIdDeportes() != null){
        		jTextFieldOrden.setText(elemento.getOrdenIdDeportes());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            
            if (elemento.getNombre() != null){
        		jTextFieldNombre.setText(elemento.getNombre());
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
            
            if (elemento.getSupCubierta() != null){
        		jTextFieldSupCubierta.setText(elemento.getSupCubierta().toString());
        	}
        	else{
        		jTextFieldSupCubierta.setText("");
        	}
            
            if (elemento.getSupAire() != null){
        		jTextFieldSupAire.setText(elemento.getSupAire().toString());
        	}
        	else{
        		jTextFieldSupAire.setText("");
        	}
            
            if (elemento.getSupSolar() != null){
        		jTextFieldSupSolar.setText(elemento.getSupSolar().toString());
        	}
        	else{
        		jTextFieldSupSolar.setText("");
        	}
            
            if (elemento.getEstado() != null){
        		jComboBoxEst.setSelectedPatron(elemento.getEstado());
        	}
        	else{
        		jComboBoxEst.setSelectedIndex(0);
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
            
            if (elemento.getFechaInstalacion() != null){
        		jTextFieldFechaInstal.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInstal.setDate(null);
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones());
        	}
        	else{
        		jTextFieldObserv.setText("");
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
            
//            if (elemento.getOrdenIdDeportes() != null){
//        		jTextFieldOrdenIdDeportes.setText(elemento.getOrdenIdDeportes().toString());
//        	}
//        	else{
//        		jTextFieldOrdenIdDeportes.setText("");
//        	}
            for (Object tipoDeporte : elemento.getListaTipos()) {
            	loadTipoDeporte((TipoDeporte) tipoDeporte);
				
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
            ((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).setData(elemento!=null?elemento.getListaTipos():new ArrayList());
            
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_CLAVE);
        	        	       	
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
    	Object object = AppContext.getApplicationContext().getBlackboard().get("intalacionesdeportivas_panel");    	
    	if (object != null && object instanceof InstalacionesDeportivasEIEL){    		
    		InstalacionesDeportivasEIEL elemento = (InstalacionesDeportivasEIEL)object;
    		
    		this.instalacionDeportiva = elemento;
    		
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
            
            if (elemento.getOrdenIdDeportes() != null){
        		jTextFieldOrden.setText(elemento.getOrdenIdDeportes());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            
            if (elemento.getNombre() != null){
        		jTextFieldNombre.setText(elemento.getNombre());
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
            
            if (elemento.getSupCubierta() != null){
        		jTextFieldSupCubierta.setText(elemento.getSupCubierta().toString());
        	}
        	else{
        		jTextFieldSupCubierta.setText("");
        	}
            
            if (elemento.getSupAire() != null){
        		jTextFieldSupAire.setText(elemento.getSupAire().toString());
        	}
        	else{
        		jTextFieldSupAire.setText("");
        	}
            
            if (elemento.getSupSolar() != null){
        		jTextFieldSupSolar.setText(elemento.getSupSolar().toString());
        	}
        	else{
        		jTextFieldSupSolar.setText("");
        	}
            
            if (elemento.getEstado() != null){
        		jComboBoxEst.setSelectedPatron(elemento.getEstado());
        	}
        	else{
        		jComboBoxEst.setSelectedIndex(0);
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
            
            if (elemento.getFechaInstalacion() != null){
        		jTextFieldFechaInstal.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInstal.setDate(null);
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones());
        	}
        	else{
        		jTextFieldObserv.setText("");
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
            
            if (elemento.getOrdenIdDeportes() != null){
        		jTextFieldOrdenIdDeportes.setText(elemento.getOrdenIdDeportes().toString());
        	}
        	else{
        		jTextFieldOrdenIdDeportes.setText("");
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
           
            ((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).setData(elemento!=null?elemento.getListaTipos():new ArrayList());
            
        }
    }
    
    public InstalacionesDeportivasEIEL getInstalacionDeportiva (InstalacionesDeportivasEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new InstalacionesDeportivasEIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));

            elemento.setClave(jTextFieldClave.getText());
            elemento.setOrdenIdDeportes(jTextFieldOrden.getText());
            
            if (jTextFieldNombre.getText()!=null){
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
            
            if (jTextFieldSupCubierta.getText()!=null && !jTextFieldSupCubierta.getText().equals("")){
            	elemento.setSupCubierta(new Integer(jTextFieldSupCubierta.getText()));  
            }else{
            	elemento.setSupCubierta(null);
            }
            
            if (jTextFieldSupAire.getText()!=null && !jTextFieldSupAire.getText().equals("")){
            	elemento.setSupAire(new Integer(jTextFieldSupAire.getText()));
            }else{
            	elemento.setSupAire(null);
            }
            
            if (jTextFieldSupSolar.getText()!=null && !jTextFieldSupSolar.getText().equals("")){
            	elemento.setSupSolar(new Integer(jTextFieldSupSolar.getText()));
            }else{
            	elemento.setSupSolar(null);
            }
            
            if (jComboBoxEst.getSelectedPatron()!=null)
            	elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
            else elemento.setEstado("");
            
            if (jComboBoxObraEjec.getSelectedPatron()!=null)
            	elemento.setObra_ejec(jComboBoxObraEjec.getSelectedPatron());
            else elemento.setObra_ejec("");
            if (jComboBoxAccesoRuedas.getSelectedPatron()!=null)
            	elemento.setObra_ejec(jComboBoxAccesoRuedas.getSelectedPatron());
            else elemento.setAcceso_s_ruedas("");
            
            if (jTextFieldFechaInstal.getDate()!=null && !jTextFieldFechaInstal.getDate().toString().equals("")){
            	java.sql.Date sqlDate = new java.sql.Date(
                		getJTextFieldFechaInstal().getDate().getYear(),
                		getJTextFieldFechaInstal().getDate().getMonth(),
                		getJTextFieldFechaInstal().getDate().getDate()
                		);
                	elemento.setFechaInstalacion(sqlDate);
            }  
            else{
            	elemento.setFechaInstalacion(null);
            }
            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
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
            
            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
            
            
            if (this.getJTableListaTipos() !=null &&
            		((TableSorted)getJTableListaTipos().getModel())!= null &&
            			((TableSorted)getJTableListaTipos().getModel()).getTableModel()!=null &&
            				((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).getData()!=null){
            	elemento.setListaTipos(((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).getData());
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
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.instalacionesdeportivas.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(InstalacionDeportivaDialog.DIM_X,InstalacionDeportivaDialog.DIM_Y);
        
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        
		if (jScroll == null) {

			jScroll = new JScrollPane(getJPanelDatosInformacionGlobal());
			jScroll = new JScrollPane();
			jScroll.setViewportView(getJPanelDatosInformacionGlobal());
			jScroll.setBorder(BorderFactory.createTitledBorder
                  (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.info"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			this.add(jScroll, new GridBagConstraints(0, 1, 1, 1, 0.1, 1,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(5, 5, 5, 5), 0, 200));
		}
        this.add(getJPanelDatosInventario(),  new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosRevision(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        
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
    
    private JPanel getJPanelDatosInformacionGlobal()
    {
        if (jPanelInformacionGlobal == null)
        {   
        	jPanelInformacionGlobal  = new JPanel(new GridBagLayout());
     
        	jPanelInformacionGlobal.add(getJPanelDatosInformacion(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.2, 0.2,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
            
        	jPanelInformacionGlobal.add(getJPanelTiposDeportes(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.2, 0.2,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
            
        }
        return jPanelInformacionGlobal;
    }
    

    private JPanel getJPanelTiposDeportes(){

    	if (jPanelTiposDeportes == null){

    		jPanelTiposDeportes = new JPanel(new GridBagLayout());
    		jPanelTiposDeportes.setBorder(BorderFactory.createTitledBorder
    				(null, I18N.get("LocalGISEIEL", "localgiseiel.panels.tipos_deportes"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

    		jPanelTiposDeportes.add(getJPanelDatosTipos(), 
    				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(0, 5, 0, 5), 0, 0));

    		jPanelTiposDeportes.add(getJPanelBotoneraTipos(), 
    				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(0, 5, 0, 5), 0, 0));

    		jPanelTiposDeportes.add(getJPanelListaTipos(), 
    				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.8,
    						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    						new Insets(0, 5, 0, 5), 0, 0));    		
    	}
    	return jPanelTiposDeportes;
    }
    
    private JPanel getJPanelListaTipos(){
    	
    	if (jPanelListaTipos == null){
    		
    		jPanelListaTipos = new JPanel();
    		jPanelListaTipos.setLayout(new GridBagLayout());
    		
    		jPanelListaTipos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.listadeportes"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
    		jPanelListaTipos.add(getJScrollPaneListaTipos(), 
    		new GridBagConstraints(0,0,1,1,0.2,0.2,GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    	}
    	return jPanelListaTipos;
    }
    
    public JScrollPane getJScrollPaneListaTipos(){
    	if (jScrollPaneListaTipos == null){
    		jScrollPaneListaTipos = new JScrollPane();
    		jScrollPaneListaTipos.setViewportView(getJTableListaTipos());
    		jScrollPaneListaTipos.setPreferredSize(new Dimension(150,100));
    	}
    	return jScrollPaneListaTipos;
    }
    
    public JTable getJTableListaTipos()
    {
    	if (jTableListaTipos  == null)
    	{
    		jTableListaTipos   = new JTable();

    		tableListaTiposModel  = new TipoDeporteEIELTableModel();

    		TableSorted tblSorted= new TableSorted((TableModel)tableListaTiposModel);
    		tblSorted.setTableHeader(jTableListaTipos.getTableHeader());
    		jTableListaTipos.setModel(tblSorted);
    		jTableListaTipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		jTableListaTipos.setCellSelectionEnabled(false);
    		jTableListaTipos.setColumnSelectionAllowed(false);
    		jTableListaTipos.setRowSelectionAllowed(true);
    		jTableListaTipos.getTableHeader().setReorderingAllowed(false);
    		
    		jTableListaTipos.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    loadSelectedItem();
                }
            });
    		
    		((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).setData(instalacionDeportiva!=null?instalacionDeportiva.getListaTipos():new ArrayList());

    	}
    	return jTableListaTipos;
    }    
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaTipos().getSelectedRow();
    	Object selectedItem = ((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof TipoDeporte){
    		
    		loadTipoDeporte((TipoDeporte)selectedItem);
    	}    	
    }
    
    private void loadTipoDeporte(TipoDeporte tipoDeporte){
    	
        
        if (tipoDeporte.getTipo() != null){
        	jComboBoxTipoDeporte.setSelectedPatron(tipoDeporte.getTipo());
        }
        else{
        	jComboBoxTipoDeporte.setSelectedIndex(0);
        }
        
        if (tipoDeporte.getOrden() != null){
        	jTextFieldOrdenIdDeportes.setText(tipoDeporte.getOrden().toString());
    	}
    	else{
    		jTextFieldOrdenIdDeportes.setText("");
    	}
        
        if (tipoDeporte.getObservaciones() != null){
    		jTextFieldObservaciones.setText(tipoDeporte.getObservaciones().toString());
    	}
    	else{
    		jTextFieldObservaciones.setText("");
    	}
        
        if (tipoDeporte.getFechaInstalacion() != null){
    		jTextFieldFechaInstalUso.setDate(tipoDeporte.getFechaInstalacion());
    	}
    	else{
    		jTextFieldFechaInstalUso.setDate(null);
    	}
        
    }
    
    private JPanel getJPanelBotoneraTipos(){
    	
    	if (jPanelBotonera == null){
    		
    		jPanelBotonera = new JPanel();
    		jPanelBotonera.setLayout(new GridBagLayout());
    		
    		jPanelBotonera.add(getJButtonAniadir(), 
            		new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
        	
    		jPanelBotonera.add(getJButtonEliminar(), 
            		new GridBagConstraints(1,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
        	
    		jPanelBotonera.add(getJButtonLimpiar(), 
            		new GridBagConstraints(2,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
    		
    	}
    	return jPanelBotonera;
    }
    
    public JButton getJButtonAniadir(){
    	
    	if (jButtonAniadir == null){
    		
    		jButtonAniadir = new JButton();
    		jButtonAniadir.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttonsave"));
    		jButtonAniadir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    annadirTipo();
                }
            });
    	}
    	return jButtonAniadir;
    	
    }
    
    public JButton getJButtonLimpiar(){
    	
    	if (jButtonLimpiar == null){
    		
    		jButtonLimpiar = new JButton();
    		jButtonLimpiar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttonclean"));
    		jButtonLimpiar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    reset();
                }
            });
    	}
    	return jButtonLimpiar;
    }
    
    public JButton getJButtonEliminar(){
    	
    	if (jButtonEliminar == null){
    		
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttondelete"));
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eliminarTipo();
                }
            });
    	}
    	return jButtonEliminar;
    }

    
    private JPanel getJPanelDatosTipos(){
    	
    	if (jPanelDatosTipos == null){
    		
    		jPanelDatosTipos = new JPanel(new GridBagLayout());
    		jPanelDatosTipos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.info"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
    		
    		jLabelTipoDeporte = new JLabel("", JLabel.CENTER);
    		jLabelTipoDeporte.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo_id_deporte")));
            
            jLabelOrdenIdDeportes = new JLabel("", JLabel.CENTER);
            jLabelOrdenIdDeportes.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ordeniddeportes"));
            
            jLabelObservaciones = new JLabel("", JLabel.CENTER);
            jLabelObservaciones.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            
            jLabelFechaInstal = new JLabel("", JLabel.CENTER);
            jLabelFechaInstal.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha_instal"));
                                    
            jPanelDatosTipos.add(jLabelTipoDeporte,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelDatosTipos.add(getJComboBoxTipoDeporte(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelDatosTipos.add(jLabelOrdenIdDeportes,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelDatosTipos.add(getJTextFieldOrdenIdDeportes(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelDatosTipos.add(jLabelObservaciones,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelDatosTipos.add(getJTextFieldObservaciones(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelDatosTipos.add(jLabelFechaInstal,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelDatosTipos.add(getJTextFieldFechaInstal_Uso(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
                		
    	}
    	return jPanelDatosTipos;
    }
    
    
    public JTextField getJTextFieldClave()
    {
        if (jTextFieldClave == null)
        {
            jTextFieldClave = new TextField(2);
        }
        return jTextFieldClave;
    }
    
    private JTextField getJTextFieldOrden()
    {
        if (jTextFieldOrden == null)
        {
        	jTextFieldOrden  = new TextField(3);
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
    
    /**
     * This method initializes jTextFieldEscalera	
     * 	
     * @return javax.swing.JTextField	
     */
    
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
    
    /**
     * This method initializes jTextFieldPlanta   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxNucleo()
    {
        if (jComboBoxNucleo  == null)
        {
        	jComboBoxNucleo = new JComboBox();
        	jComboBoxNucleo.setRenderer(new UbicacionListCellRenderer());
        }
        return jComboBoxNucleo;
    }
    
    private JTextField getJTextFieldNombre()
    {
    	if (jTextFieldNombre == null)
    	{
    		jTextFieldNombre = new TextField(50);
    		jTextFieldNombre.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
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
            Estructuras.cargarEstructura("eiel_Tipo de instalaciones deportivas");
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
            Estructuras.cargarEstructura("eiel_Titularidad_General_equip");
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
            Estructuras.cargarEstructura("eiel_Gestor_General_equip");
            jComboBoxGestor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGestor;        
    }
    
    private JTextField getJTextFieldSupCubierta()
    {
    	if (jTextFieldSupCubierta == null)
    	{
    		jTextFieldSupCubierta  = new TextField(10);
    		jTextFieldSupCubierta.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupCubierta, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupCubierta;
    }
    
    private JTextField getJTextFieldSupAire()
    {
    	if (jTextFieldSupAire == null)
    	{
    		jTextFieldSupAire  = new TextField(10);
    		jTextFieldSupAire.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupAire, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupAire;
    }
    
    private JTextField getJTextFieldSupSolar()
    {
    	if (jTextFieldSupSolar == null)
    	{
    		jTextFieldSupSolar  = new TextField(10);
    		jTextFieldSupSolar.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupSolar, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupSolar;
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
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv == null)
    	{
    		jTextFieldObserv  = new TextField(50);
    		jTextFieldObserv.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObserv;
    }
    
    private JTextField getJTextFieldObservaciones()
    {
    	if (jTextFieldObservaciones == null)
    	{
    		jTextFieldObservaciones  = new TextField(50);
    		jTextFieldObservaciones.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObservaciones, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObservaciones;
    }
    

    
    private DateField getJTextFieldFechaInstal()
    {
    	if (jTextFieldFechaInstal == null)
    	{
    		jTextFieldFechaInstal = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaInstal.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaInstal.setEditable(true);
    		
    	}
    	return jTextFieldFechaInstal;
    }
    
    private DateField getJTextFieldFechaInstal_Uso()
    {
    	if (jTextFieldFechaInstalUso == null)
    	{
    		jTextFieldFechaInstalUso = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaInstalUso.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaInstalUso.setEditable(true);
    	}
    	return jTextFieldFechaInstalUso;
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
    

    
    private ComboBoxEstructuras getJComboBoxTipoDeporte(){
    	
    	if (jComboBoxTipoDeporte == null){
    		    		    		
    		Estructuras.cargarEstructura("eiel_Tipo Deporte");
    		jComboBoxTipoDeporte = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
    		jComboBoxTipoDeporte.setPreferredSize(new Dimension(100, 20));
    		

    	}
    	return jComboBoxTipoDeporte;
    }
    
    private JTextField getJTextFieldOrdenIdDeportes(){
    	
    	if (jTextFieldOrdenIdDeportes == null){
    		
    		jTextFieldOrdenIdDeportes = new JTextField(3);
    		jTextFieldOrdenIdDeportes.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldOrdenIdDeportes, 3, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldOrdenIdDeportes;
    }
    
    public InstalacionDeportivaPanel(GridBagLayout layout)
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

            jLabelEntSing  = new JLabel("", JLabel.CENTER);
            jLabelEntSing.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.entsing")));
            
            jLabelNucleo   = new JLabel("", JLabel.CENTER);
            jLabelNucleo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nucleo")));
            
            jLabelOrden    = new JLabel("", JLabel.CENTER);
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
            
            jPanelIdentificacion.add(jLabelEntSing, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentificacion.add(getJComboBoxEntidad(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentificacion.add(jLabelNucleo, 
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
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.informacion_comun"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            jLabelNombre = new JLabel("", JLabel.CENTER); 
            jLabelNombre.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nombre_id")); 
            
            jLabelTipo = new JLabel("", JLabel.CENTER); 
            jLabelTipo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo_id"))); 
            
            jLabelTitular = new JLabel("", JLabel.CENTER); 
            jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular_id")); 

            jLabelGestor = new JLabel("", JLabel.CENTER);
            jLabelGestor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestora_id"));
            
            jLabelSupCubierta = new JLabel("", JLabel.CENTER);
            jLabelSupCubierta.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_cubierta_id"));
            
            jLabelSupAire = new JLabel("", JLabel.CENTER);
            jLabelSupAire.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_aire_id"));
            
            jLabelSupSolar = new JLabel("", JLabel.CENTER);
            jLabelSupSolar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_solar_id"));
            
            jLabelEstado = new JLabel("", JLabel.CENTER);
            jLabelEstado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado_id"));
                       
            jLabelObraEjec = new JLabel("", JLabel.CENTER); 
            jLabelObraEjec.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.obraejec")); 
            jLabelAccesoRuedas = new JLabel("", JLabel.CENTER); 
            jLabelAccesoRuedas.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.accesoruedas")); 
            
            jLabelFechaInstal = new JLabel("", JLabel.CENTER);
            jLabelFechaInstal.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha_instal"));
            
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));

            jLabelOrdenIdDeportes = new JLabel("", JLabel.CENTER);
            jLabelOrdenIdDeportes.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ordeniddeportes"));
            
            
            jPanelInformacion.add(jLabelNombre,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldNombre(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTipo,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxTipo(), 
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
            
            jPanelInformacion.add(jLabelGestor,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxGestor(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSupCubierta,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldSupCubierta(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSupAire,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldSupAire(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSupSolar,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldSupSolar(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEstado,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxEst(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFechaInstal,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldFechaInstal(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelObraEjec,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJComboBoxObraEjec(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelAccesoRuedas,
                    new GridBagConstraints(2,5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJComboBoxAccesoRuedas(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
    jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 6, 2, 1, 0.1, 0.1,
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
        	
            jLabelFecha = new JLabel("", JLabel.CENTER); 
            jLabelFecha.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 
            
            jLabelEstadoRevision = new JLabel("", JLabel.CENTER); 
            jLabelEstadoRevision.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 
                        
            jPanelRevision.add(jLabelFecha,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(getJTextFieldFecha(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(jLabelEstadoRevision, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelRevision.add(getJComboBoxEstado(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
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

        return  (jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&      
        (jTextFieldOrden.getText()!=null && !jTextFieldOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0) &&
        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0);
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
	        	
	        	String orden_id = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_id"))!=null){
	        		orden_id=(feature.getAttribute(esquema.getAttributeByColumn("orden_id"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelInstalacionesDeportivasEIEL(clave, codprov, codmunic, entidad, nucleo, orden_id));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, entidad, nucleo, orden_id);       	
			}
		}
	
	
	
	
	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String entidad, String nucleo, String orden_id) {
		
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
        
        if (orden_id != null){
    		jTextFieldOrden.setText(orden_id);
    	}
    	else{
    		jTextFieldOrden.setText("");
    	}
		
	}
	
	private TipoDeporte getTipoDeporte(){
    	
    	TipoDeporte tipoDeporte = new TipoDeporte();
    	
    	tipoDeporte.setTipo(((ComboBoxEstructuras)getJComboBoxTipoDeporte()).getSelectedPatron());
    	
    	if (getJTextFieldOrdenIdDeportes().getText() != null && !getJTextFieldOrden().getText().equals("")){
    		tipoDeporte.setOrden(getJTextFieldOrdenIdDeportes().getText());
    	}
    	
    	if (getJTextFieldObservaciones().getText() != null && !getJTextFieldObservaciones().getText().equals("")){
    		tipoDeporte.setObservaciones(getJTextFieldObservaciones().getText());
    	}
    	
    	if (jTextFieldFechaInstalUso.getDate()!=null && !jTextFieldFechaInstalUso.getDate().toString().equals("")){
        	        	
        	java.util.Date fecha = jTextFieldFechaInstalUso.getDate();            	
        	tipoDeporte.setFechaInstalacion(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
        }  
        else{
        	tipoDeporte.setFechaInstalacion(null);
        }
    	
    	return tipoDeporte;
    }
	
	private void annadirTipo(){
		
		if (getTipoDeporte()!=null) {
			if (this.instalacionDeportiva == null){
				instalacionDeportiva = new InstalacionesDeportivasEIEL();				
			}
			this.instalacionDeportiva.getListaTipos().add(getTipoDeporte());
		}
		((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).setData(instalacionDeportiva.getListaTipos());
		((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).fireTableDataChanged();
		getJTableListaTipos().updateUI();

	}
    
    private void eliminarTipo(){
    	
    	int selectedRow = getJTableListaTipos().getSelectedRow();
    	Object selectedItem = ((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof TipoDeporte){
    		
    		this.instalacionDeportiva.getListaTipos().remove(selectedItem);
        	((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).setData(instalacionDeportiva.getListaTipos());
        	((TipoDeporteEIELTableModel)((TableSorted)getJTableListaTipos().getModel()).getTableModel()).fireTableDataChanged();
        	jTableListaTipos.updateUI();
    	}  
    }
    
    private void reset(){
    	
    	jComboBoxTipoDeporte.setSelectedIndex(0);    	
    	jTextFieldObservaciones.setText("");
    	jTextFieldFechaInstalUso.setDate(null);
    	jTextFieldOrdenIdDeportes.setText("");

    }

	public Hashtable getIdentifyFields() {
		return null;
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
