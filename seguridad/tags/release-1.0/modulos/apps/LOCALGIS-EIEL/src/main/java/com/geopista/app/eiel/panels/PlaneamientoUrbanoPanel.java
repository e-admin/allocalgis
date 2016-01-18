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
import com.geopista.app.eiel.beans.PlaneamientoUrbanoEIEL;
import com.geopista.app.eiel.dialogs.PlaneamientoUrbanoDialog;
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

public class PlaneamientoUrbanoPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null;     
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelCodMunic = null;
	
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    
	private JPanel jPanelInformacion = null;
	
	private JLabel jLabelTipo = null;
	private JLabel jLabelEst = null;
	private JLabel jLabelDenom = null;
	private JLabel jLabelSupMunic = null;	
	private JLabel jLabelSupUrb = null;
	private JLabel jLabelSupUrbz = null;
	private JLabel jLabelSupNoUrbz = null;
	private JLabel jLabelSupNUrbzE = null;
	private JLabel jLabelFechaPubli = null;
	private JLabel jLabelObserv = null;
	
	private ComboBoxEstructuras jComboBoxTipo = null;
	private ComboBoxEstructuras jComboBoxEst = null;
	private JTextField jTextFieldDenom = null;
	private JTextField jTextFieldSupMunic = null;
	private JTextField jTextFieldOrden = null;
	private JTextField jTextFieldSupUrb = null;
	private JTextField jTextFieldSupUrbz = null;
	private JTextField jTextFieldSupNoUrbz = null;
	private JTextField jTextFieldSupNUrbzE = null;
	private DateField jTextFieldFechaPubli = null;
	private JTextField jTextFieldObserv = null;	
	
	private JPanel jPanelRevision = null;	
	
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;	
	
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	private JLabel jLabelOrden;

	
    /**
     * This method initializes
     * 
     */
    public PlaneamientoUrbanoPanel()
    {
        super();
        initialize();
    }
    
    public PlaneamientoUrbanoPanel(PlaneamientoUrbanoEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(PlaneamientoUrbanoEIEL elemento)
    {
        if (elemento!=null)
        {
            //Datos identificacion
        	
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
            if (elemento.getOrden() != null){            	
            	jTextFieldOrden.setText(elemento.getOrden());            
            }else{
            	jTextFieldOrden.setText("");
            }         
            
            if (elemento.getTipo() != null){
                jComboBoxTipo.setSelectedPatron(elemento.getTipo());
            }
            else{
                jComboBoxTipo.setSelectedIndex(0);
            }
            
            if (elemento.getEstado() != null){
                jComboBoxEst.setSelectedPatron(elemento.getEstado().toString());
        	}
        	else{
        	    jComboBoxEst.setSelectedIndex(0);
        	}
            
            if (elemento.getDenominacion() != null){
        		jTextFieldDenom.setText(elemento.getDenominacion());
        	}
        	else{
        		jTextFieldDenom.setText("");
        	}
            
            if (elemento.getSupMunicipal() != null){
        		jTextFieldSupMunic.setText(elemento.getSupMunicipal().toString());
        	}
        	else{
        		jTextFieldSupMunic.setText("");
        	}
            
            if (elemento.getFechaPublicacion() != null){
        		jTextFieldFechaPubli.setDate(elemento.getFechaPublicacion());
        	}
        	else{
        		jTextFieldFechaPubli.setDate(null);
        	}
            
            if (elemento.getSupUrbano() != null){
        		jTextFieldSupUrb.setText(elemento.getSupUrbano().toString());
        	}
        	else{
        		jTextFieldSupUrb.setText("");
        	}
            
            if (elemento.getSupUrbanizable() != null){
        		jTextFieldSupUrbz.setText(elemento.getSupUrbanizable().toString());
        	}
        	else{
        		jTextFieldSupUrbz.setText("");
        	}
            
            if (elemento.getSupNoUrbanizable() != null){
        		jTextFieldSupNoUrbz.setText(elemento.getSupNoUrbanizable().toString());
        	}
        	else{
        		jTextFieldSupNoUrbz.setText("");
        	}
            
            if (elemento.getSupNoUrbanizableEsp() != null){
        		jTextFieldSupNUrbzE.setText(elemento.getSupNoUrbanizableEsp().toString());
        	}
        	else{
        		jTextFieldSupNUrbzE.setText("");
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
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
            
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	// (new EdicionOperations()).obtenerEntidades("");
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
    	Object object = AppContext.getApplicationContext().getBlackboard().get("planteamientourbano_panel");    	
    	if (object != null && object instanceof PlaneamientoUrbanoEIEL){    		
    		PlaneamientoUrbanoEIEL elemento = (PlaneamientoUrbanoEIEL)object;
    		//Datos identificacion

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
            if (elemento.getOrden() != null){            	
            	jTextFieldOrden.setText(elemento.getOrden());            
            }else{
            	jTextFieldOrden.setText("");
            } 
            
            if (elemento.getTipo() != null){
                jComboBoxTipo.setSelectedPatron(elemento.getTipo());
            }
            else{
                jComboBoxTipo.setSelectedIndex(0);
            }
            
            if (elemento.getEstado() != null){
                jComboBoxEst.setSelectedPatron(elemento.getEstado().toString());
            }
            else{
                jComboBoxEst.setSelectedIndex(0);
            }
            
            if (elemento.getDenominacion() != null){
        		jTextFieldDenom.setText(elemento.getDenominacion());
        	}
        	else{
        		jTextFieldDenom.setText("");
        	}
            
            if (elemento.getSupMunicipal() != null){
        		jTextFieldSupMunic.setText(elemento.getSupMunicipal().toString());
        	}
        	else{
        		jTextFieldSupMunic.setText("");
        	}
            
            if (elemento.getFechaPublicacion() != null){
        		jTextFieldFechaPubli.setDate(elemento.getFechaPublicacion());
        	}
        	else{
        		jTextFieldFechaPubli.setDate(null);
        	}
            
            if (elemento.getSupUrbano() != null){
        		jTextFieldSupUrb.setText(elemento.getSupUrbano().toString());
        	}
        	else{
        		jTextFieldSupUrb.setText("");
        	}
            
            if (elemento.getSupUrbanizable() != null){
        		jTextFieldSupUrbz.setText(elemento.getSupUrbanizable().toString());
        	}
        	else{
        		jTextFieldSupUrbz.setText("");
        	}
            
            if (elemento.getSupNoUrbanizable() != null){
        		jTextFieldSupNoUrbz.setText(elemento.getSupNoUrbanizable().toString());
        	}
        	else{
        		jTextFieldSupNoUrbz.setText("");
        	}
            
            if (elemento.getSupNoUrbanizableEsp() != null){
        		jTextFieldSupNUrbzE.setText(elemento.getSupNoUrbanizableEsp().toString());
        	}
        	else{
        		jTextFieldSupNUrbzE.setText("");
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
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
            
        }
    }
    
    
    public PlaneamientoUrbanoEIEL getPlaneamientoUrbano (PlaneamientoUrbanoEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new PlaneamientoUrbanoEIEL();
            }
            
    		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		
            if (jComboBoxTipo.getSelectedPatron()!=null)
                elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
            else elemento.setTipo("");
            
            if (jComboBoxEst.getSelectedPatron()!=null)
                elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
            else elemento.setEstado("");
            
            if (jTextFieldOrden.getText() != null){            	
            	elemento.setOrden(jTextFieldOrden.getText());            
            }
            if (jTextFieldDenom.getText()!=null){
            	elemento.setDenominacion(jTextFieldDenom.getText());
            }
            
            if (jTextFieldSupMunic.getText()!=null && !jTextFieldSupMunic.getText().equals("")){
            	elemento.setSupMunicipal(new Float(jTextFieldSupMunic.getText()));
            }
            else{
            	elemento.setSupMunicipal(null);
            }
            
            if (jTextFieldFechaPubli.getDate()!=null && !jTextFieldFechaPubli.getDate().toString().equals("")){
            	if (getJTextFieldFechaPubli().getDate()!=null){
            		elemento.setFechaPublicacion(
            				new java.sql.Date(
            						getJTextFieldFechaPubli().getDate().getYear(),
            						getJTextFieldFechaPubli().getDate().getMonth(),
            						getJTextFieldFechaPubli().getDate().getDate()
            				)
            		);
            	}
            	else{
            		elemento.setFechaPublicacion(null);
            	}
            }  
            else{
            	elemento.setFechaPublicacion(null);
            }
            
            if (jTextFieldSupUrb.getText()!=null && !jTextFieldSupUrb.getText().equals("")){
            	elemento.setSupUrbano(new Float(jTextFieldSupUrb.getText()));
            }
            else{
            	elemento.setSupUrbano(null);
            }
            
            if (jTextFieldSupUrbz.getText()!=null && !jTextFieldSupUrbz.getText().equals("")){
            	elemento.setSupUrbanizable(new Float(jTextFieldSupUrbz.getText()));
            }
            else{
            	elemento.setSupUrbanizable(null);
            }
            
            if (jTextFieldSupNoUrbz.getText()!=null && !jTextFieldSupNoUrbz.getText().equals("")){
            	elemento.setSupNoUrbanizable(new Float(jTextFieldSupNoUrbz.getText()));
            }
            else{
            	elemento.setSupNoUrbanizable(null);
            }
            
            if (jTextFieldSupNUrbzE.getText()!=null && !jTextFieldSupNUrbzE.getText().equals("")){
            	elemento.setSupNoUrbanizableEsp(new Float(jTextFieldSupNUrbzE.getText()));
            }
            else{
            	elemento.setSupNoUrbanizableEsp(null);
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
            
        }
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.planeamientourbano.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(PlaneamientoUrbanoDialog.DIM_X,PlaneamientoUrbanoDialog.DIM_Y);
        
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
            Estructuras.cargarEstructura("eiel_Figuras de Planeamiento");
            jComboBoxTipo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTipo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTipo;        
    }
    
    private ComboBoxEstructuras getJComboBoxEst()
    {
    	if (jComboBoxEst == null)
    	{
            Estructuras.cargarEstructura("eiel_Estado de tramitación de la Figura de Planeamiento");
            jComboBoxEst = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEst.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEst;   
    }
    
    private JTextField getJTextFieldOrden()
    {
    	if (jTextFieldOrden == null)
    	{
    		jTextFieldOrden  = new TextField(3);
    		jTextFieldOrden.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldOrden, 3, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldOrden;
    }
    
    private JTextField getJTextFieldDenom()
    {
    	if (jTextFieldDenom == null)
    	{
    		jTextFieldDenom  = new TextField(40);
    		jTextFieldDenom.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDenom, 40, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDenom;
    }
    
    private JTextField getJTextFieldSupMunic()
    {
    	if (jTextFieldSupMunic == null)
    	{
    		jTextFieldSupMunic  = new TextField(10);
    		jTextFieldSupMunic.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupMunic, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupMunic;
    }
    
    private DateField getJTextFieldFechaPubli()
    {
    	if (jTextFieldFechaPubli == null)
    	{
    		jTextFieldFechaPubli = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaPubli.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaPubli.setEditable(true);
    		
    	}
    	return jTextFieldFechaPubli;
    }
    
    private JTextField getJTextFieldSupUrb()
    {
    	if (jTextFieldSupUrb == null)
    	{
    		jTextFieldSupUrb  = new TextField(8);
    		jTextFieldSupUrb.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupUrb, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupUrb;
    }
    
    private JTextField getJTextFieldSupUrbz()
    {
    	if (jTextFieldSupUrbz == null)
    	{
    		jTextFieldSupUrbz  = new TextField(8);
    		jTextFieldSupUrbz.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupUrbz, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupUrbz;
    }
    
    private JTextField getJTextFieldSupNoUrbz()
    {
    	if (jTextFieldSupNoUrbz == null)
    	{
    		jTextFieldSupNoUrbz  = new TextField(12);
    		jTextFieldSupNoUrbz.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupNoUrbz, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupNoUrbz;
    }
    
    private JTextField getJTextFieldSupNUrbzE()
    {
    	if (jTextFieldSupNUrbzE == null)
    	{
    		jTextFieldSupNUrbzE  = new TextField(12);
    		jTextFieldSupNUrbzE.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldSupNUrbzE, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSupNUrbzE;
    }
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv == null)
    	{
    		jTextFieldObserv  = new TextField();
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
    
    
    public PlaneamientoUrbanoPanel(GridBagLayout layout)
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
        	
            
        	
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelOrden = new JLabel("", JLabel.CENTER); 
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
            
            jPanelIdentificacion.add(jLabelOrden, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelIdentificacion.add(getJTextFieldOrden(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
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
        	
            jLabelTipo = new JLabel("", JLabel.CENTER); 
            jLabelTipo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo"))); 
            
            jLabelEst = new JLabel("", JLabel.CENTER); 
            jLabelEst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.est")); 
            
            jLabelDenom = new JLabel("", JLabel.CENTER); 
            jLabelDenom.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.denom"))); 

            jLabelSupMunic = new JLabel("", JLabel.CENTER);
            jLabelSupMunic.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.supmuni"));
            
            jLabelSupUrb = new JLabel("", JLabel.CENTER);
            jLabelSupUrb.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.supurb"));
            
            jLabelSupUrbz = new JLabel("", JLabel.CENTER);
            jLabelSupUrbz.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.supurbz"));
            
            jLabelSupNoUrbz = new JLabel("", JLabel.CENTER);
            jLabelSupNoUrbz.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.supnurbz"));
            
            jLabelSupNUrbzE = new JLabel("", JLabel.CENTER);
            jLabelSupNUrbzE.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.supnurbzes"));
            
            jLabelFechaPubli = new JLabel("", JLabel.CENTER);
            jLabelFechaPubli.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechapubli"));
            
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            
            
            jPanelInformacion.add(jLabelTipo,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTipo(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 150, 0));
            
            jPanelInformacion.add(jLabelDenom,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldDenom(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelInformacion.add(jLabelEst,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxEst(), 
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
           
            
            jPanelInformacion.add(jLabelSupMunic,
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldSupMunic(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSupUrb,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldSupUrb(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSupUrbz,
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldSupUrbz(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSupNoUrbz,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldSupNoUrbz(), 
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSupNUrbzE,
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldSupNUrbzE(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFechaPubli,
                    new GridBagConstraints(0, 8, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldFechaPubli(), 
                    new GridBagConstraints(0, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(1, 8, 1, 1, 0.1, 0.1,
                    		GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 9, 1, 1, 0.1, 0.1,
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

        return  (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0)&&
        (jComboBoxMunicipio!=null && jComboBoxTipo.getSelectedItem()!=null)
        && jTextFieldDenom.getText()!=null && !jTextFieldDenom.getText().equals("")
        && jTextFieldOrden.getText()!=null && !jTextFieldOrden.getText().equals("");
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
	        	
	        	String codprov = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
	        		codprov=(feature.getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
	        	}
	        	
	        	String codmunic = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
	        		codmunic=(feature.getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelPlaneamientoUrbanosEIEL(codprov, codmunic));
	        	
	        	loadDataIdentificacion(codprov, codmunic);       	
			}
		}
	

	public void loadDataIdentificacion(String codprov, String codmunic) {
		
		//Datos identificacion
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
