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
import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.dialogs.OtrosServMunicipalesDialog;
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

public class OtrosServMunicipalesPanel extends JPanel implements FeatureExtendedPanel
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
    
    private ComboBoxEstructuras jComboBoxInfGnrl = null;
    private ComboBoxEstructuras jComboBoxInfTur = null;
    private ComboBoxEstructuras jComboBoxGbElec = null;
    private ComboBoxEstructuras jComboBoxOrd = null;
    private ComboBoxEstructuras jComboBoxEnEol = null;
    private JTextField jTextFieldKwEol = null;
    private ComboBoxEstructuras jComboBoxEnSolar = null;
    private JTextField jTextFieldKwSolar = null;
    private ComboBoxEstructuras jComboBoxPlMareo = null;
    private JTextField jTextFieldKwMareo = null;
    private ComboBoxEstructuras jComboBoxOtEnerg = null;
    private JTextField jTextFieldKwOtEnerg = null;
    private JTextField jTextFieldObservacion = null;
    
    private JLabel jLabelInfGnrl = null;
    private JLabel jLabelInfTur = null;
    private JLabel jLabelGbElec = null;
    private JLabel jLabelOrd = null;
    private JLabel jLabelEnEol = null;
    private JLabel jLabelKwEol = null;
    private JLabel jLabelEnSolar = null;
    private JLabel jLabelKwSolar = null;
    private JLabel jLabelPlMareo = null;
    private JLabel jLabelKwMareo = null;
    private JLabel jLabelOtEnerg = null;
    private JLabel jLabelKwOtEnerg = null;
    private JLabel jLabelObservacion = null;
    private JLabel jLabelCobServTlf = null;
    private JLabel jLabelTvDigCable = null;
    
    private JPanel jPanelRevision = null;
    
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;	
	
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private ComboBoxEstructuras jComboBoxCobServTlf = null;
	private ComboBoxEstructuras jComboBoxTvDigCable = null;

	
    /**
     * This method initializes
     * 
     */
    public OtrosServMunicipalesPanel()
    {
        super();
        initialize();
    }
    
    public OtrosServMunicipalesPanel(OtrosServMunicipalesEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(OtrosServMunicipalesEIEL elemento)
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
            
            if (elemento.getSwInfGeneral() != null){            	
            	jComboBoxInfGnrl.setSelectedPatron(elemento.getSwInfGeneral());
            }
            else{
            	jComboBoxInfGnrl.setSelectedIndex(0);
            } 
            
            if (elemento.getSwInfTuristica() != null){            	
            	jComboBoxInfTur.setSelectedPatron(elemento.getSwInfTuristica());
            }
            else{
            	jComboBoxInfTur.setSelectedIndex(0);
            } 
            
            if (elemento.getSwGbElectronico()!= null){            	
            	jComboBoxGbElec.setSelectedPatron(elemento.getSwGbElectronico());
            }
            else{
            	jComboBoxGbElec.setSelectedIndex(0);
            } 
            
            if (elemento.getOrdSoterramiento() != null){            	
            	jComboBoxOrd.setSelectedPatron(elemento.getOrdSoterramiento());
            }
            else{
            	jComboBoxOrd.setSelectedIndex(0);
            }
            
            
            if (elemento.getEnEolica() != null){            	
            	jComboBoxEnEol.setSelectedPatron(elemento.getEnEolica());
            }
            else{
            	jComboBoxEnEol.setSelectedIndex(0);
            }
            
            if (elemento.getKwEolica() != null){            	
            	jTextFieldKwEol.setText(elemento.getKwEolica().toString());
            }
            else{
            	jTextFieldKwEol.setText("");
            } 
            
            if (elemento.getEnSolar()!= null){            	
            	jComboBoxEnSolar.setSelectedPatron(elemento.getEnSolar());
            }
            else{
            	jComboBoxEnSolar.setSelectedIndex(0);
            } 
            
            if (elemento.getKwSolar()!= null){            	
            	jTextFieldKwSolar.setText(elemento.getKwSolar().toString());
            }
            else{
            	jTextFieldKwSolar.setText("");
            } 
            
            if (elemento.getPlMareomotriz() != null){            	
            	jComboBoxPlMareo.setSelectedPatron(elemento.getPlMareomotriz());
            }
            else{
            	jComboBoxPlMareo.setSelectedIndex(0);
            } 
            
            if (elemento.getKwMareomotriz() != null){            	
            	jTextFieldKwMareo.setText(elemento.getKwMareomotriz().toString());
            }
            else{
            	jTextFieldKwMareo.setText("");
            } 
            
            if (elemento.getOtEnergias() != null){            	
            	jComboBoxOtEnerg.setSelectedPatron(elemento.getOtEnergias());
            }
            else{
            	jComboBoxOtEnerg.setSelectedIndex(0);
            } 
            
            if (elemento.getKwOtEnergias() != null){            	
            	jTextFieldKwOtEnerg.setText(elemento.getKwOtEnergias().toString());
            }
            else{
            	jTextFieldKwOtEnerg.setText("");
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
            if (elemento.getCoberturaTlf() != null){
            	jComboBoxCobServTlf.setSelectedPatron(elemento.getCoberturaTlf());
        	}
        	else{
        		jComboBoxCobServTlf.setSelectedIndex(0);
        	}
            if (elemento.getTeleCable() != null){
            	jComboBoxTvDigCable.setSelectedPatron(elemento.getTeleCable());
        	}
        	else{
        		jComboBoxTvDigCable.setSelectedIndex(0);
        	}
            
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
   	       	
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
    	Object object = AppContext.getApplicationContext().getBlackboard().get("otrosserviciosmunicipales_panel");    	
    	if (object != null && object instanceof OtrosServMunicipalesEIEL){    		
    		OtrosServMunicipalesEIEL elemento = (OtrosServMunicipalesEIEL)object;
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
            
            if (elemento.getSwInfGeneral() != null){            	
            	jComboBoxInfGnrl.setSelectedPatron(elemento.getSwInfGeneral());
            }
            else{
            	jComboBoxInfGnrl.setSelectedIndex(0);
            } 
            
            if (elemento.getSwInfTuristica() != null){            	
            	jComboBoxInfTur.setSelectedPatron(elemento.getSwInfTuristica());
            }
            else{
            	jComboBoxInfTur.setSelectedIndex(0);
            } 
            
            if (elemento.getSwGbElectronico()!= null){            	
            	jComboBoxGbElec.setSelectedPatron(elemento.getSwGbElectronico());
            }
            else{
            	jComboBoxGbElec.setSelectedIndex(0);
            } 
            
            if (elemento.getOrdSoterramiento() != null){            	
            	jComboBoxOrd.setSelectedPatron(elemento.getOrdSoterramiento());
            }
            else{
            	jComboBoxOrd.setSelectedIndex(0);
            }
            
            
            if (elemento.getEnEolica() != null){            	
            	jComboBoxEnEol.setSelectedPatron(elemento.getEnEolica());
            }
            else{
            	jComboBoxEnEol.setSelectedIndex(0);
            }
            
            if (elemento.getKwEolica() != null){            	
            	jTextFieldKwEol.setText(elemento.getKwEolica().toString());
            }
            else{
            	jTextFieldKwEol.setText("");
            } 
            
            if (elemento.getEnSolar()!= null){            	
            	jComboBoxEnSolar.setSelectedPatron(elemento.getEnSolar());
            }
            else{
            	jComboBoxEnSolar.setSelectedIndex(0);
            } 
            
            if (elemento.getKwSolar()!= null){            	
            	jTextFieldKwSolar.setText(elemento.getKwSolar().toString());
            }
            else{
            	jTextFieldKwSolar.setText("");
            } 
            
            if (elemento.getPlMareomotriz() != null){            	
            	jComboBoxPlMareo.setSelectedPatron(elemento.getPlMareomotriz());
            }
            else{
            	jComboBoxPlMareo.setSelectedIndex(0);
            } 
            
            if (elemento.getKwMareomotriz() != null){            	
            	jTextFieldKwMareo.setText(elemento.getKwMareomotriz().toString());
            }
            else{
            	jTextFieldKwMareo.setText("");
            } 
            
            if (elemento.getOtEnergias() != null){            	
            	jComboBoxOtEnerg.setSelectedPatron(elemento.getOtEnergias());
            }
            else{
            	jComboBoxOtEnerg.setSelectedIndex(0);
            } 
            
            if (elemento.getKwOtEnergias() != null){            	
            	jTextFieldKwOtEnerg.setText(elemento.getKwOtEnergias().toString());
            }
            else{
            	jTextFieldKwOtEnerg.setText("");
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
            if (elemento.getCoberturaTlf() != null){
            	jComboBoxCobServTlf.setSelectedPatron(elemento.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxCobServTlf.setSelectedIndex(0);
        	}
            if (elemento.getTeleCable() != null){
            	jComboBoxTvDigCable.setSelectedPatron(elemento.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxTvDigCable.setSelectedIndex(0);
        	}
            
        }
    }
    
    
    public OtrosServMunicipalesEIEL getOtrosServMunicipales (OtrosServMunicipalesEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new OtrosServMunicipalesEIEL();
            }
            
    		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		
            if (jComboBoxInfGnrl.getSelectedPatron()!=null)
	            elemento.setSwInfGeneral((String) jComboBoxInfGnrl.getSelectedPatron());
            else elemento.setSwInfGeneral("");
            if (jComboBoxInfTur.getSelectedPatron()!=null)
            	elemento.setSwInfTuristica((String) jComboBoxInfTur.getSelectedPatron());
            else elemento.setSwInfTuristica("");
            if (jComboBoxGbElec.getSelectedPatron()!=null)
            	elemento.setSwGbElectronico((String) jComboBoxGbElec.getSelectedPatron());
            else elemento.setSwGbElectronico("");
            if (jComboBoxOrd.getSelectedPatron()!=null)
            	elemento.setOrdSoterramiento((String) jComboBoxOrd.getSelectedPatron());
            else elemento.setOrdSoterramiento("");
            if (jComboBoxEnEol.getSelectedPatron()!=null)
            	elemento.seteEnEolica((String) jComboBoxEnEol.getSelectedPatron());
            else elemento.seteEnEolica("");
            
            if (jTextFieldKwEol.getText()!=null && !jTextFieldKwEol.getText().equals("")){
            	elemento.setKwEolica(new Integer(jTextFieldKwEol.getText()));
            }
            else{
            	elemento.setKwEolica(null);
            }
            
            if (jComboBoxEnSolar.getSelectedPatron()!=null)
            elemento.setEnSolar((String) jComboBoxEnSolar.getSelectedPatron());
            else elemento.setEnSolar("");
            
            if (jTextFieldKwSolar.getText()!=null && !jTextFieldKwSolar.getText().equals("")){
            	elemento.setKwSolar(new Integer(jTextFieldKwSolar.getText()));
            }
            else{
            	elemento.setKwSolar(null);
            }
            
            if (jComboBoxPlMareo.getSelectedPatron()!=null)
            	elemento.setPlMareomotriz((String) jComboBoxPlMareo.getSelectedPatron());
            else elemento.setPlMareomotriz("");
            
            if (jTextFieldKwMareo.getText()!=null && !jTextFieldKwMareo.getText().equals("")){
            	elemento.setKwMareomotriz(new Integer(jTextFieldKwMareo.getText()));
            }
            else{
            	elemento.setKwMareomotriz(null);
            }
            
            if (jComboBoxOtEnerg.getSelectedPatron()!=null)
            	elemento.setOtEnergias((String) jComboBoxOtEnerg.getSelectedPatron());
            else elemento.setOtEnergias("");
            
            if (jTextFieldKwOtEnerg.getText()!=null && !jTextFieldKwOtEnerg.getText().equals("")){
            	elemento.setKwOtEnergias(new Integer(jTextFieldKwOtEnerg.getText()));
            }
            else{
            	elemento.setKwOtEnergias(null);
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
            
            if (jComboBoxCobServTlf.getSelectedPatron()!=null)
            	elemento.setCoberturaTlf(jComboBoxCobServTlf.getSelectedPatron());
            else elemento.setCoberturaTlf("");
            if (jComboBoxTvDigCable.getSelectedPatron()!=null)
            	elemento.setTeleCable(jComboBoxTvDigCable.getSelectedPatron());
            else elemento.setTeleCable("");
        }
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.otrosserviciosmunicipales.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(OtrosServMunicipalesDialog.DIM_X,OtrosServMunicipalesDialog.DIM_Y);
        
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

    private ComboBoxEstructuras getJComboBoxInfGnrl()
    { 
        if (jComboBoxInfGnrl == null)
        {
            Estructuras.cargarEstructura("eiel_sw_inf_grl");
            jComboBoxInfGnrl = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxInfGnrl.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxInfGnrl;        
    }

    private ComboBoxEstructuras getJComboBoxInfTur()
    { 
        if (jComboBoxInfTur == null)
        {
            Estructuras.cargarEstructura("eiel_sw_inf_tur");
            jComboBoxInfTur = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxInfTur.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxInfTur;        
    }
    
    private ComboBoxEstructuras getJComboBoxGbElec()
    { 
        if (jComboBoxGbElec == null)
        {
            Estructuras.cargarEstructura("eiel_sw_gb_elec");
            jComboBoxGbElec = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxGbElec.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGbElec;        
    }
    
    private ComboBoxEstructuras getJComboBoxOrd()
    { 
        if (jComboBoxOrd == null)
        {
            Estructuras.cargarEstructura("eiel_Ordenanza Soterramiento");
            jComboBoxOrd = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxOrd.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxOrd;        
    }
    
    private ComboBoxEstructuras getJComboBoxEnEol()
    { 
        if (jComboBoxEnEol == null)
        {
            Estructuras.cargarEstructura("eiel_En Eolica");
            jComboBoxEnEol = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEnEol.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEnEol;        
    }
    
    private JTextField getjTextFieldKwEol()
    {
    	if (jTextFieldKwEol == null)
    	{
    		jTextFieldKwEol = new TextField(10);
    		jTextFieldKwEol.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldKwEol, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldKwEol;
    }
    
    private ComboBoxEstructuras getJComboBoxEnSolar()
    { 
        if (jComboBoxEnSolar == null)
        {
            Estructuras.cargarEstructura("eiel_En Solar");
            jComboBoxEnSolar = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEnSolar.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEnSolar;        
    }
    
    private JTextField getjTextFieldKwSolar()
    {
    	if (jTextFieldKwSolar == null)
    	{
    		jTextFieldKwSolar = new TextField(10);
    		jTextFieldKwSolar.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldKwSolar, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldKwSolar;
    }
    
    private ComboBoxEstructuras getJComboBoxPlMareo()
    { 
        if (jComboBoxPlMareo == null)
        {
            Estructuras.cargarEstructura("eiel_En Mareo");
            jComboBoxPlMareo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxPlMareo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxPlMareo;        
    }
    
    private JTextField getjTextFieldKwMareo()
    {
    	if (jTextFieldKwMareo == null)
    	{
    		jTextFieldKwMareo = new TextField(10);
    		jTextFieldKwMareo.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldKwMareo, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldKwMareo;
    }
    
    private ComboBoxEstructuras getJComboBoxOtEnerg()
    { 
        if (jComboBoxOtEnerg == null)
        {
            Estructuras.cargarEstructura("eiel_En Otras");
            jComboBoxOtEnerg = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxOtEnerg.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxOtEnerg;        
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
    
    private JTextField getjTextFieldKwOtEnerg()
    {
    	if (jTextFieldKwOtEnerg == null)
    	{
    		jTextFieldKwOtEnerg = new TextField(10);
    		jTextFieldKwOtEnerg.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldKwOtEnerg, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldKwOtEnerg;
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
    
    private ComboBoxEstructuras getJComboBoxCobServTlf()
    { 
        if (jComboBoxCobServTlf == null)
        {
            Estructuras.cargarEstructura("eiel_cob_serv_tlf_m");
            jComboBoxCobServTlf = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCobServTlf.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCobServTlf;        
    }
    
    private ComboBoxEstructuras getJComboBoxtvDigCable()
    { 
        if (jComboBoxTvDigCable == null)
        {
            Estructuras.cargarEstructura("eiel_tv_dig_cable");
            jComboBoxTvDigCable = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTvDigCable.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTvDigCable;        
    }
    
    
    public OtrosServMunicipalesPanel(GridBagLayout layout)
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
        	
        	jLabelInfGnrl = new JLabel("", JLabel.CENTER); 
        	jLabelInfGnrl.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.infGnrl")); 
            
        	jLabelInfTur = new JLabel("", JLabel.CENTER); 
        	jLabelInfTur.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.infTur")); 
            
        	jLabelGbElec = new JLabel("", JLabel.CENTER); 
        	jLabelGbElec.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gbElec")); 

        	jLabelOrd = new JLabel("", JLabel.CENTER);
        	jLabelOrd.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ord"));
            
        	jLabelEnEol = new JLabel("", JLabel.CENTER);
        	jLabelEnEol.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.enEol"));
            
        	jLabelKwEol = new JLabel("", JLabel.CENTER);
        	jLabelKwEol.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.kwEol"));
            
        	jLabelEnSolar = new JLabel("", JLabel.CENTER);
        	jLabelEnSolar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.enSolar"));
            
        	jLabelKwSolar = new JLabel("", JLabel.CENTER);
        	jLabelKwSolar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.kwSolar"));
            
        	jLabelPlMareo = new JLabel("", JLabel.CENTER);
        	jLabelPlMareo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.plMareo"));
            
        	jLabelKwMareo = new JLabel("", JLabel.CENTER);
        	jLabelKwMareo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.kwMareo"));
            
        	jLabelOtEnerg = new JLabel("", JLabel.CENTER);
        	jLabelOtEnerg.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.otEnerg"));
            
        	jLabelKwOtEnerg = new JLabel("", JLabel.CENTER);
        	jLabelKwOtEnerg.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.kwOtEnerg"));
        	
        	jLabelObservacion = new JLabel("", JLabel.CENTER);
        	jLabelObservacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
        	
        	jLabelCobServTlf = new JLabel("", JLabel.CENTER); 
            jLabelCobServTlf.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.cobserv")); 
            
            jLabelTvDigCable = new JLabel("", JLabel.CENTER); 
            jLabelTvDigCable.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tvdig")); 
            
            jPanelInformacion.add(jLabelInfGnrl,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxInfGnrl(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelInfTur,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxInfTur(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelGbElec,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxGbElec(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelOrd,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxOrd(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEnEol,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxEnEol(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelKwEol,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldKwEol(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEnSolar,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxEnSolar(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelKwSolar,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldKwSolar(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPlMareo,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxPlMareo(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelKwMareo,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldKwMareo(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelOtEnerg,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxOtEnerg(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelKwOtEnerg,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldKwOtEnerg(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCobServTlf,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxCobServTlf(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTvDigCable,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxtvDigCable(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            JPanel panelObservaciones = new JPanel(new GridBagLayout());
            
            panelObservaciones.add(jLabelObservacion,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 1, 0));
            
            panelObservaciones.add(getjTextFieldObservacion(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 270, 0));
            
            panelObservaciones.add(new JPanel(), 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 1, 0));
            
            jPanelInformacion.add(panelObservaciones, 
                    new GridBagConstraints(0, 7, 3, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
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
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(getJTextFieldFecha(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(jLabelEstado, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelRevision.add(getJComboBoxEstado(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 180, 0));
            
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

        return (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
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

	        	String codprov = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
	        		codprov=(feature.getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
	        	}
	        	
	        	String codmunic = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
	        		codmunic=(feature.getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelOtrosServiciosMunicipalesEIEL(codprov, codmunic));

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
