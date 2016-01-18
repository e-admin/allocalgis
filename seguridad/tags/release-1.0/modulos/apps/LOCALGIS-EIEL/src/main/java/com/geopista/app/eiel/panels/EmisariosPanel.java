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
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.EmisoresDialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
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

public class EmisariosPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    private JPanel jPanelRevision = null;
    
    private boolean okPressed = false;	
    // GUI - Identification
    private JLabel jLabelClave = null;
    private JTextField jTextFieldClave = null;
	private JLabel jLabelCodProv = null;
	private JComboBox jComboBoxProvincia = null;
	private JLabel jLabelCodMunic = null;
	private JComboBox jComboBoxMunicipio = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldOrden = null;
	// GUI - Information
	
	private JLabel jLabelObserv = null;
	private JTextField jTextFieldObserv = null;
	// GUI - Revision
	private JLabel jLabelFechaRevision = null;
	private JTextField jTextFieldFechaRevision = null;
	private JLabel jLabelEstadoRevision = null;
	private ComboBoxEstructuras jComboBoxEstadoRevision = null;
	
	//ALFANUMERICOS

	private JLabel jLabelTitular = null;
	private JLabel jLabelGestor = null;
	
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private ComboBoxEstructuras jComboBoxSistImp = null;
	private JLabel jLabelTipo_red_interior=null;
	private ComboBoxEstructuras jComboBoxTipo_red = null;

	private JLabel jLabelSistImpul = null;
	private JLabel jLabelMaterial = null;
	private ComboBoxEstructuras jComboBoxMaterial = null;

	private JLabel jLabelFecha_inst =null;
	private DateField jTextFieldFechaInst = null;
	
	private JLabel jLabelEstado  = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	
	private String idMunicipioSelected;

    /**
     * This method initializes
     * 
     */
    public EmisariosPanel()
    {
        super();
        initialize();
    }
    
    public EmisariosPanel(EmisariosEIEL elemento)
    {
        super();
        initialize();
        loadData (elemento);
    }
    
    public void loadData(EmisariosEIEL elemento)
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
            
            if (elemento.getCodOrden() != null){
        		jTextFieldOrden.setText(elemento.getCodOrden());
        	}
        	else{
        		jTextFieldOrden.setText("");
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
            
            if (elemento.getEstado() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getEstado());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
            
            if (elemento.getMaterial() != null){
        		jComboBoxMaterial.setSelectedPatron(elemento.getMaterial());
        	}
        	else{
        		jComboBoxMaterial.setSelectedIndex(0);
        	}
            
            if (elemento.getFecha_inst() != null){
        		jTextFieldFechaInst.setDate(elemento.getFecha_inst());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
        	}
            if (elemento.getTipo_red() != null){
        		jComboBoxTipo_red.setSelectedPatron(elemento.getTipo_red());
        	}
        	else{
        		jComboBoxTipo_red.setSelectedIndex(0);
        	}     
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
            
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRevision.setText(elemento.getFechaRevision().toString());
        	}
        	else{
        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
        		jTextFieldFechaRevision.setText(datetime);
        	}
            
            if (elemento.getEstado_Revision() != null){
            	jComboBoxEstadoRevision.setSelectedPatron(elemento.getEstado_Revision().toString());
        	}
        	else{
        		jComboBoxEstadoRevision.setSelectedIndex(0);
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
            
            if (elemento.getSistema() != null){
        		jComboBoxSistImp.setSelectedPatron(elemento.getSistema());
        	}
        	else{
        		jComboBoxSistImp.setSelectedIndex(0);
        	}
            if (elemento.getTipo_red() != null){
        		jComboBoxTipo_red.setSelectedPatron(elemento.getTipo_red());
        	}
        	else{
        		jComboBoxTipo_red.setSelectedIndex(0);
        	}
            
            if (elemento.getEstado_Revision() != null){
            	jComboBoxEstadoRevision.setSelectedPatron(elemento.getEstado_Revision().toString());
        	}
        	else{
        		jComboBoxEstadoRevision.setSelectedIndex(0);
        	}
            
            
            
        }else{
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.EMISARIOS_CLAVE);
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
    public void loadData(){
    	Object object = AppContext.getApplicationContext().getBlackboard().get("emisario");    	
    	if (object != null && object instanceof EmisariosEIEL){    		
    		EmisariosEIEL elemento = (EmisariosEIEL)object;
            //Datos identificacion
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
            
            if (elemento.getCodOrden() != null){
        		jTextFieldOrden.setText(elemento.getCodOrden());
        	}
        	else{
        		jTextFieldOrden.setText("");
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
            
            if (elemento.getEstado() != null){
            	jComboBoxEstadoRevision.setSelectedItem(elemento.getEstado());
        	}
        	else{
        		jComboBoxEstadoRevision.setSelectedIndex(0);
        	}
            
            if (elemento.getMaterial() != null){
        		jComboBoxMaterial.setSelectedItem(elemento.getMaterial());
        	}
        	else{
        		jComboBoxMaterial.setSelectedIndex(0);
        	}
            
            if (elemento.getFecha_inst() != null){
        		jTextFieldFechaInst.setDate(elemento.getFecha_inst());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
        	}
            if (elemento.getTipo_red() != null){
        		jComboBoxTipo_red.setSelectedItem(elemento.getTipo_red());
        	}
        	else{
        		jComboBoxTipo_red.setSelectedIndex(0);
        	}     
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
            
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRevision.setText(elemento.getFechaRevision().toString());
        	}
        	else{
        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
        		jTextFieldFechaRevision.setText(datetime);
        	}
            
            if (elemento.getEstado_Revision() != null){
            	jComboBoxEstadoRevision.setSelectedItem(elemento.getEstado_Revision());
        	}
        	else{
        		jComboBoxEstadoRevision.setSelectedIndex(0);
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
            
            if (elemento.getSistema() != null){
        		jComboBoxSistImp.setSelectedPatron(elemento.getSistema());
        	}
        	else{
        		jComboBoxSistImp.setSelectedIndex(0);
        	}
            if (elemento.getTipo_red() != null){
        		jComboBoxTipo_red.setSelectedPatron(elemento.getTipo_red());
        	}
        	else{
        		jComboBoxTipo_red.setSelectedIndex(0);
        	}
            
            if (elemento.getEstado_Revision() != null){
            	jComboBoxEstadoRevision.setSelectedItem(elemento.getEstado_Revision());
        	}
        	else{
        		jComboBoxEstadoRevision.setSelectedIndex(0);
        	}
        }
    }
    
    public EmisariosEIEL getEmisor (EmisariosEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new EmisariosEIEL();
            }
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		
            if (jTextFieldClave.getText()!=null){
            	elemento.setClave(jTextFieldClave.getText());
            }
            
            if (jTextFieldOrden.getText()!=null){
            	elemento.setCodOrden(jTextFieldOrden.getText());
            }
                      
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            
            if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
            	String fechas=jTextFieldFechaRevision.getText();
            	String anio=fechas.substring(0,4);
            	String mes=fechas.substring(5,7);
            	String dia=fechas.substring(8,10);
            	
            	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
            	elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
            }  
            else{
            	elemento.setFechaRevision(null);
            }
            if (jComboBoxEstadoRevision.getSelectedPatron()!=null)
            	elemento.setEstado_Revision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
            
            
    		if (jComboBoxTitular.getSelectedPatron()!=null)
    			elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());
    		else elemento.setTitularidad("");
    		if (jComboBoxGestor.getSelectedPatron()!=null)
    			elemento.setGestion((String) jComboBoxGestor.getSelectedPatron());
    		else elemento.setGestion("");
    		if (jComboBoxSistImp.getSelectedPatron()!=null)
    			elemento.setSistema((String) jComboBoxSistImp.getSelectedPatron());
    		else elemento.setSistema("");
    		if (jComboBoxTipo_red.getSelectedPatron()!=null)
    			elemento.setTipo_red((String) jComboBoxTipo_red.getSelectedPatron());
    		else elemento.setTipo_red("");
    		if (jComboBoxEstado.getSelectedPatron()!=null)
                elemento.setEstado((String)jComboBoxEstado.getSelectedPatron());
    		else elemento.setEstado("");
    		
    		if (jComboBoxEstadoRevision.getSelectedPatron()!=null)
            	elemento.setEstado_Revision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
            
    		
    	
        }
        
        return elemento;
    }
    
    public EmisariosEIEL getEmisorData()
    {

    		EmisariosEIEL elemento = new EmisariosEIEL();
            
    		 elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
     				.getSelectedItem()).getIdProvincia());
     		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
     				.getSelectedItem()).getIdIne());
     		
            if (jTextFieldClave.getText()!=null){
            	elemento.setClave(jTextFieldClave.getText());
            }
            
            if (jTextFieldOrden.getText()!=null){
            	elemento.setCodOrden(jTextFieldOrden.getText());
            }
                      
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            
            if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
            	String fechas=jTextFieldFechaRevision.getText();
            	String anio=fechas.substring(0,4);
            	String mes=fechas.substring(5,7);
            	String dia=fechas.substring(8,10);
            	
            	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
            	elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
            }  
            else{
            	elemento.setFechaRevision(null);
            }        
            
    		if (jComboBoxTitular.getSelectedPatron()!=null)
    			elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());
    		else elemento.setTitularidad("");
    		if (jComboBoxGestor.getSelectedPatron()!=null)
    			elemento.setGestion((String) jComboBoxGestor.getSelectedPatron());
    		else elemento.setGestion("");
    		if (jComboBoxSistImp.getSelectedPatron()!=null)
    			elemento.setSistema((String) jComboBoxSistImp.getSelectedPatron());
    		else elemento.setSistema("");
    		if (jComboBoxTipo_red.getSelectedPatron()!=null)
    			elemento.setTipo_red((String) jComboBoxTipo_red.getSelectedPatron());
    		else elemento.setTipo_red("");
    		if (jComboBoxEstado.getSelectedPatron()!=null)
                elemento.setEstado((String)jComboBoxEstado.getSelectedPatron());
    		else elemento.setEstado("");
    		
    		 if (jComboBoxEstadoRevision.getSelectedPatron()!=null)
         		elemento.setEstado_Revision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
    		
    		 
    	return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(EmisoresDialog.DIM_X,EmisoresDialog.DIM_Y);
        
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosRevision(), new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
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
    private ComboBoxEstructuras getJComboBoxTitular()
    { 
        if (jComboBoxTitular == null)
        {
            Estructuras.cargarEstructura("eiel_Titularidad");
            jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTitular;        
    }
    
    private ComboBoxEstructuras getJComboBoxMaterial()
    { 
        if (jComboBoxMaterial == null)
        {
            Estructuras.cargarEstructura("eiel_material");
            jComboBoxMaterial = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxMaterial.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxMaterial;        
    }
    
    private ComboBoxEstructuras getJComboBoxGestor()
    { 
        if (jComboBoxGestor == null)
        {
            Estructuras.cargarEstructura("eiel_Gestión");
            jComboBoxGestor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGestor;        
    }
    
    private ComboBoxEstructuras getJComboBoxSistImp()
    { 
        if (jComboBoxSistImp == null)
        {
            Estructuras.cargarEstructura("eiel_Sistema de impulsión");
            jComboBoxSistImp = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxSistImp.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxSistImp;        
    }
    
    private ComboBoxEstructuras getJComboBoxEstado()
    { 
        if (jComboBoxEstado == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEstado = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado;        
    }
    
    private ComboBoxEstructuras getJComboBoxTipo_red()
    { 
        if (jComboBoxTipo_red == null)
        {
            Estructuras.cargarEstructura("eiel_tipo_red_interior");
            jComboBoxTipo_red = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxTipo_red.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTipo_red;        
    }
   
    private JTextField getJTextFieldOrden()
    {
    	if (jTextFieldOrden   == null)
    	{
    		jTextFieldOrden = new TextField(3);
    	}
    	return jTextFieldOrden;
    }
    
   
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv  == null)
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
    
    private JTextField getJTextFieldFecha()
    {
    	if (jTextFieldFechaRevision  == null)
    	{
    		jTextFieldFechaRevision  = new TextField();    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);
            
            jTextFieldFechaRevision.setText(datetime);
    	}
    	return jTextFieldFechaRevision;
    }
    
    private ComboBoxEstructuras getJComboBoxEstadoRevision()
    { 
        if (jComboBoxEstadoRevision == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            jComboBoxEstadoRevision = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstadoRevision.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstadoRevision;        
    }
    
    private DateField getJTextFieldFechaInstalacion()
    {
    	if (jTextFieldFechaInst  == null){
    		jTextFieldFechaInst  = new DateField((java.util.Date) null, 0);    		
    		jTextFieldFechaInst.setDateFormatString("yyyy-MM-dd");
    	}
    	return jTextFieldFechaInst;
    }
    
    
    public EmisariosPanel(GridBagLayout layout)
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
        	
        	
            jLabelTitular = new JLabel("", JLabel.CENTER); 
            jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.PMI")); 
            
            jLabelTitular = new JLabel("", JLabel.CENTER); 
            jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular")); 

            jLabelGestor = new JLabel("", JLabel.CENTER);
            jLabelGestor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestor"));
            
            jLabelEstado = new JLabel("", JLabel.CENTER);
            jLabelEstado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado_conserv"));
            
            jLabelMaterial = new JLabel("", JLabel.CENTER);
            jLabelMaterial.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.material"));
            
            jLabelSistImpul = new JLabel("", JLabel.CENTER);
            jLabelSistImpul.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sist_impul"));
            
            jLabelFecha_inst = new JLabel("", JLabel.CENTER);
            jLabelFecha_inst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.instalacion"));
           
            jLabelTipo_red_interior = new JLabel("", JLabel.CENTER);
            jLabelTipo_red_interior.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo_red"));
            
           
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            
            
            
            jPanelInformacion.add(jLabelTitular,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxTitular(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelGestor,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxGestor(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEstado,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxEstado(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelMaterial,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxMaterial(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSistImpul,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxSistImp(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTipo_red_interior,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxTipo_red(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFecha_inst,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldFechaInstalacion(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            /*jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 2, 3, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));*/
            
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 4, 2, 1, 0.1, 0.1,
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
        	
            jLabelFechaRevision  = new JLabel("", JLabel.CENTER); 
            jLabelFechaRevision.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 
            
            jLabelEstadoRevision  = new JLabel("", JLabel.CENTER); 
            jLabelEstadoRevision.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 
                        
            jPanelRevision.add(jLabelFechaRevision,
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
            
            
            jPanelRevision.add(getJComboBoxEstadoRevision(), 
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

        return  ((jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
        (jTextFieldOrden.getText()!=null && !jTextFieldOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0)); 
        
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

	        	
	        	String tramo_em = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("tramo_em"))!=null){
	        		tramo_em=(feature.getAttribute(esquema.getAttributeByColumn("tramo_em"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelEmisariosEIEL(clave, codprov, codmunic, tramo_em));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, tramo_em);
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

	@Override
	public void enter() {
		loadData();		
		loadDataIdentificacion();
		
	}

	@Override
	public void exit() {
		
	}
    

}
