package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.RazonSocialExtendedInfoDialog;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.intercambio.edicion.utils.UbicacionListCellRenderer;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;

public class RazonSocialExtendedInfoPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelDatosIdentificacion = null;
    private JLabel jLabelCif = null;
    private JLabel jLabelRazonSocial = null;
    private JTextField jTextFieldCif = null;
    private JTextField jTextFieldRazonSocial = null;
    private JPanel jPanelDatosLocalizacion = null;
    private JLabel jLabelTipoVia = null;
    private JLabel jLabelDirección = null;
    private ComboBoxEstructuras jComboBoxTipoVia = null;
    private JTextField jTextFieldDireccion = null;
    private JButton jButtonBuscarDireccion = null;
    private JLabel jLabelNumero = null;
    private JLabel jLabelLetra = null;
    private JLabel jLabelNumeroD = null;
    private JLabel jLabelLetraD = null;
    private JLabel jLabelBloque = null;
    private JLabel jLabelEscalera = null;
    private JLabel jLabelPuerta = null;
    private JTextField jTextFieldNumero = null;
    private JTextField jTextFieldLetra = null;
    private JTextField jTextFieldNumeroD = null;
    private JTextField jTextFieldLetraD = null;
    private JTextField jTextFieldBloque = null;
    private JLabel jLabelPlanta = null;
    private JLabel jLabelDirNoEstructurada = null;
    private JLabel jLabelKm = null;
    private JTextField jTextFieldDirNoEstructurada = null;
    private JTextField jTextFieldKm = null;
    private JLabel jLabelProvincia = null;
    private JLabel jLabelMunicipio = null;
    private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JLabel jLabelCodPostal = null;
    private JTextField jTextFieldCodPostal = null;
    
    private boolean okPressed = false;

	private JComboBox jComboBoxEscalera = null;
	private JComboBox jComboBoxPlanta = null;
	private JComboBox jComboBoxPuerta = null;
    /**
     * This method initializes
     * 
     */
    public RazonSocialExtendedInfoPanel()
    {
        super();
        initialize();
    }
    
    public RazonSocialExtendedInfoPanel(Persona pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(Persona pers)
    {
        if (pers!=null)
        {
            //Datos identificacion
            jTextFieldCif.setText(pers.getNif());
            jTextFieldRazonSocial.setText(pers.getRazonSocial());            
            
            //Datos de localización
            if (pers.getDomicilio().getProvinciaINE()!=null) {
            	if (pers.getDomicilio().getProvinciaINE().length()<2)
            		pers.getDomicilio().setProvinciaINE(GeopistaFunctionUtils.completarConCeros(pers.getDomicilio().getProvinciaINE(), 2));

                Provincia p = new Provincia();
                p.setIdProvincia(pers.getDomicilio().getProvinciaINE());
                p.setNombreOficial(pers.getDomicilio().getNombreProvincia());
                jComboBoxProvincia.setSelectedItem(p);
                
                if (pers.getDomicilio().getCodigoMunicipioDGC()!=null) {
                	if (pers.getDomicilio().getCodigoMunicipioDGC().length()<3)
                		pers.getDomicilio().setCodigoMunicipioDGC(GeopistaFunctionUtils.completarConCeros(pers.getDomicilio().getCodigoMunicipioDGC(), 3));

                    Municipio m = new Municipio();
                    m.setIdIne(pers.getDomicilio().getMunicipioINE());
                    m.setIdCatastro(pers.getDomicilio().getCodigoMunicipioDGC());
                    m.setProvincia((Provincia)jComboBoxProvincia.getSelectedItem());
                    jComboBoxMunicipio.setSelectedItem(m);
                    
                }                
            }
            
            jComboBoxTipoVia.setSelectedPatron(pers.getDomicilio().getTipoVia());
            jTextFieldDireccion.setText(pers.getDomicilio().getNombreVia());
            
            if (pers.getDomicilio().getPrimerNumero() == -1){
            	jTextFieldNumero.setText("");
            }
            else{
            	jTextFieldNumero.setText(EdicionUtils.getStringValue(pers.getDomicilio().getPrimerNumero()));
            }
            jTextFieldLetra.setText(pers.getDomicilio().getPrimeraLetra());
            
            if (pers.getDomicilio().getSegundoNumero() == -1){
            	jTextFieldNumeroD.setText("");
            }
            else{
            	jTextFieldNumeroD.setText(EdicionUtils.getStringValue(pers.getDomicilio().getSegundoNumero()));
            }
            jTextFieldLetraD.setText(pers.getDomicilio().getSegundaLetra());
            jTextFieldBloque.setText(pers.getDomicilio().getBloque());
            
            EstructuraDB eEscalera = new EstructuraDB();
            if((jComboBoxEscalera.getItemCount() > 0) && (pers.getDomicilio().getEscalera()!=null)){            	
            	eEscalera.setPatron(pers.getDomicilio().getEscalera());
                jComboBoxEscalera.setSelectedItem(eEscalera);
            }
            else{
            	jComboBoxEscalera.setSelectedItem(eEscalera);
            }
                       
            EstructuraDB ePlanta = new EstructuraDB();
            if((jComboBoxPlanta.getItemCount() > 0) && (pers.getDomicilio().getPlanta()!=null)){            	
            	ePlanta.setPatron(pers.getDomicilio().getPlanta());
                jComboBoxPlanta.setSelectedItem(ePlanta);
            }
            else{
            	jComboBoxPlanta.setSelectedItem(ePlanta);
            }
                           
            EstructuraDB ePuerta = new EstructuraDB();
            if((jComboBoxPuerta.getItemCount() > 0) && (pers.getDomicilio().getPuerta()!=null)){
            	ePuerta.setPatron(pers.getDomicilio().getPuerta());
            	jComboBoxPuerta.setSelectedItem(ePuerta);
            }
            else{
            	jComboBoxPuerta.setSelectedItem(ePuerta);
            }
            
            if (pers.getDomicilio().getCodigoPostal()==null){
            	jTextFieldCodPostal.setText("");
            }
            else{            	
            	jTextFieldCodPostal.setText(EdicionUtils.getStringValue(pers.getDomicilio().getCodigoPostal()));
            }
            jTextFieldDirNoEstructurada.setText(pers.getDomicilio().getDireccionNoEstructurada());
            
            if (pers.getDomicilio().getKilometro() == -1){
            	jTextFieldKm.setText("");
            }
            else{
            	jTextFieldKm.setText(EdicionUtils.getStringValue(pers.getDomicilio().getKilometro()));
            }
        }
    }
    
    public Titular getRazonSocial (Titular pers)
    {

        if (okPressed)
        {
            if(pers==null)
            {
                pers = new Titular();
            }
            
            //datos identificacion
            
           String textCif=jTextFieldCif.getText();
           if(textCif.length()!=9){
        	   char digito=textCif.charAt(0);
        	   if((digito>='A' && digito<='Z') || (digito>='a' && digito<='z')){
        		   textCif=digito+GeopistaFunctionUtils.completarConCeros(textCif.substring(1),8);
        	   }else{
        		   digito=textCif.charAt(textCif.length()-1);
        		   if((digito>='A' && digito<='Z') || (digito>='a' && digito<='z')){
        			   textCif=GeopistaFunctionUtils.completarConCeros(textCif.substring(0,textCif.length()-1),8)+digito;
        		   }else{
        			   textCif=GeopistaFunctionUtils.completarConCeros(textCif,9);
        		   }
        	   }
           }
            pers.setNif(textCif);
            pers.setRazonSocial(jTextFieldRazonSocial.getText());
            
            
            //Datos de localización
            Provincia provincia = (Provincia) jComboBoxProvincia.getSelectedItem();
            pers.getDomicilio().setProvinciaINE(provincia.getIdProvincia());
            pers.getDomicilio().setNombreProvincia(provincia.getNombreOficial());
            Municipio municipio = (Municipio) jComboBoxMunicipio.getSelectedItem();
            if (municipio!=null){
                pers.getDomicilio().setMunicipioINE(municipio.getIdIne());
                if(municipio.getIdCatastro() != null && municipio.getIdCatastro().length()>0)
                    pers.getDomicilio().setCodigoMunicipioDGC(municipio.getIdCatastro());
                else
                    pers.getDomicilio().setCodigoMunicipioDGC(municipio.getIdIne());

                pers.getDomicilio().setNombreMunicipio(municipio.getNombreOficial());
            }

            if (jComboBoxTipoVia.getSelectedPatron()!=null)
                pers.getDomicilio().setTipoVia(jComboBoxTipoVia.getSelectedPatron().toString());
            pers.getDomicilio().setNombreVia(jTextFieldDireccion.getText());
            if (jTextFieldNumero.getText().length()>0)
                pers.getDomicilio().setPrimerNumero(Integer.parseInt(jTextFieldNumero.getText()));
            else if (jTextFieldNumero.getText().length() < 1)
            	pers.getDomicilio().setPrimerNumero(-1);
            pers.getDomicilio().setPrimeraLetra(jTextFieldLetra.getText());
            if (jTextFieldNumeroD.getText().length()>0)
                pers.getDomicilio().setSegundoNumero(Integer.parseInt(jTextFieldNumeroD.getText()));
            else if (jTextFieldNumeroD.getText().length() < 1)
            	pers.getDomicilio().setSegundoNumero(-1);
            pers.getDomicilio().setSegundaLetra(jTextFieldLetraD.getText());
            pers.getDomicilio().setBloque(jTextFieldBloque.getText());
            
            if(jComboBoxEscalera.getSelectedItem()!=null)
            	pers.getDomicilio().setEscalera(((EstructuraDB)jComboBoxEscalera.getSelectedItem()).getPatron());
           
            if(jComboBoxPlanta.getSelectedItem()!=null)
            	pers.getDomicilio().setPlanta(((EstructuraDB)jComboBoxPlanta.getSelectedItem()).getPatron());
            
            if(jComboBoxPuerta.getSelectedItem()!=null)
            	pers.getDomicilio().setPuerta(((EstructuraDB)jComboBoxPuerta.getSelectedItem()).getPatron());

            if (jTextFieldCodPostal.getText().length()>0){
                pers.getDomicilio().setCodigoPostal(jTextFieldCodPostal.getText());
                if(pers.getDomicilio().getApartadoCorreos() == -1)
                    pers.getDomicilio().setApartadoCorreos(Integer.parseInt(pers.getDomicilio().getCodigoPostal()));
            }
            
            else if (jTextFieldCodPostal.getText().length() < 1)
            	pers.getDomicilio().setCodigoPostal(null);   //pers.getDomicilio().setCodigoPostal(-1);
            pers.getDomicilio().setDireccionNoEstructurada(jTextFieldDirNoEstructurada.getText());
            if (jTextFieldKm.getText().length()>0)
                pers.getDomicilio().setKilometro(Double.parseDouble(jTextFieldKm.getText()));
            else if (jTextFieldKm.getText().length() < 1)
            	pers.getDomicilio().setKilometro(-1);     
            
            try
            {
                DireccionLocalizacion dir = ConstantesRegistroExp.clienteCatastro.getViaPorNombre(pers.getDomicilio().getNombreVia());
                if(dir!=null && dir.getNombreVia()!=null && !dir.getNombreVia().equalsIgnoreCase("")){

                    if(!dir.getNombreVia().equalsIgnoreCase(pers.getDomicilio().getNombreVia()))
                    	pers.getDomicilio().setNombreVia(dir.getNombreVia());
                                        
                    pers.getDomicilio().setCodigoVia(dir.getCodigoVia());
                    pers.getDomicilio().setTipoVia(dir.getTipoVia());
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return pers;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(RazonSocialExtendedInfoDialog.DIM_X,RazonSocialExtendedInfoDialog.DIM_Y);
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosLocalizacion(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        //Inicializa los desplegables        
        if (Identificadores.get("ListaProvincias")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerProvincias();
            Identificadores.put("ListaProvincias", lst);
            EdicionUtils.cargarLista(getJComboBoxProvincia(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxProvincia(), 
                    (ArrayList)Identificadores.get("ListaProvincias"));            
        }       
        
        if (Identificadores.get("ListaEscalera")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerEscalera();
            Identificadores.put("ListaEscalera", lst);
            EdicionUtils.cargarLista(getJComboBoxEscalera(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxEscalera(), 
                    (ArrayList)Identificadores.get("ListaEscalera"));
        } 
        
        if (Identificadores.get("ListaPlanta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPlanta();
            Identificadores.put("ListaPlanta", lst);
            EdicionUtils.cargarLista(getJComboBoxPlanta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPlanta(), 
                    (ArrayList)Identificadores.get("ListaPlanta"));
        } 
        
        if (Identificadores.get("ListaPuerta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPuerta();
            Identificadores.put("ListaPuerta", lst);
            EdicionUtils.cargarLista(getJComboBoxPuerta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPuerta(), 
                    (ArrayList)Identificadores.get("ListaPuerta"));
        } 
        
    }
    
    public RazonSocialExtendedInfoPanel(GridBagLayout layout)
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
        if (jPanelDatosIdentificacion == null)
        {   
            jLabelRazonSocial = new JLabel("", JLabel.CENTER); 
            jLabelRazonSocial.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "razonsocial.extended.panel.identificacion.razonsocial"))); 
            
            jLabelCif = new JLabel("", JLabel.CENTER); 
            jLabelCif.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "razonsocial.extended.panel.identificacion.nifcif"))); 
            jPanelDatosIdentificacion = new JPanel(new GridBagLayout());
            jPanelDatosIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "razonsocial.extended.panel.identificacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelDatosIdentificacion.add(jLabelCif, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(jLabelRazonSocial,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosIdentificacion.add(getJTextFieldCif(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(getJTextFieldRazonSocial(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            
        }
        return jPanelDatosIdentificacion;
    }
    
    /**
     * This method initializes jPanelDatosLocalizacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosLocalizacion()
    {
        if (jPanelDatosLocalizacion == null)
        {
            
            jPanelDatosLocalizacion = new JPanel(new GridBagLayout());
            jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jLabelCodPostal = new JLabel("", JLabel.CENTER); 
            jLabelCodPostal.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.codpostal"));            
            jLabelKm = new JLabel("", JLabel.CENTER); 
            jLabelKm.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.km"));            
            jLabelDirNoEstructurada = new JLabel("", JLabel.CENTER); 
            jLabelDirNoEstructurada.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.dirnoestructurada")); 
            jLabelPuerta = new JLabel("", JLabel.CENTER); 
            jLabelPuerta.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.puerta")); 
            jLabelPlanta  = new JLabel("", JLabel.CENTER); 
            jLabelPlanta.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.planta")); 
            jLabelEscalera = new JLabel("", JLabel.CENTER); 
            jLabelEscalera.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.escalera")); 
            jLabelBloque = new JLabel("", JLabel.CENTER); 
            jLabelBloque.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.bloque"));
            jLabelLetraD = new JLabel("", JLabel.CENTER); 
            jLabelLetraD.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.letraD")); 
            jLabelNumeroD = new JLabel("", JLabel.CENTER); 
            jLabelNumeroD.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.numeroD")); 
            jLabelLetra = new JLabel("", JLabel.CENTER); 
            jLabelLetra.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.letra")); 
            jLabelNumero = new JLabel("", JLabel.CENTER); 
            jLabelNumero.setText(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.numero")); 
            jLabelDirección = new JLabel("", JLabel.CENTER); 
            jLabelDirección.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.direccion"))); 
            jLabelTipoVia = new JLabel("", JLabel.CENTER); 
            jLabelTipoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.tipovia"))); 
            jLabelProvincia  = new JLabel("", JLabel.CENTER); 
            jLabelProvincia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.provincia"))); 
            jLabelMunicipio = new JLabel("", JLabel.CENTER); 
            jLabelMunicipio.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "razonsocial.extended.panel.localizacion.municipio"))); 
            
            jPanelDatosLocalizacion.add(jLabelProvincia, 
                    new GridBagConstraints(0, 0, 4, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));            
            jPanelDatosLocalizacion.add(jLabelMunicipio, 
                    new GridBagConstraints(4, 0, 6, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelTipoVia, 
                    new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelDirección, 
                    new GridBagConstraints(2, 2, 7, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJComboBoxTipoVia(), 
                    new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldDireccion(), 
                    new GridBagConstraints(2, 3, 7, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJButtonBuscarDireccion(), 
                    new GridBagConstraints(9, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelNumero, 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelLetra, 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelNumeroD, 
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelLetraD, 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelBloque, 
                    new GridBagConstraints(4, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelEscalera, 
                    new GridBagConstraints(5, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelPlanta, 
                    new GridBagConstraints(6, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelPuerta, 
                    new GridBagConstraints(7, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldNumero(), 
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldLetra(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldNumeroD(), 
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldLetraD(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldBloque(), 
                    new GridBagConstraints(4, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosLocalizacion.add(getJComboBoxEscalera(), 
                    new GridBagConstraints(5, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
           
            jPanelDatosLocalizacion.add(getJComboBoxPlanta(), 
                    new GridBagConstraints(6, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
           
            jPanelDatosLocalizacion.add(getJComboBoxPuerta(), 
                    new GridBagConstraints(7, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelDirNoEstructurada, 
                    new GridBagConstraints(0, 7, 8, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelKm, 
                    new GridBagConstraints(9, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldDirNoEstructurada(), 
                    new GridBagConstraints(0, 8, 8, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 5, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldKm(), 
                    new GridBagConstraints(9, 8, 7, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(0, 1, 4, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJComboBoxMunicipio(), 
                    new GridBagConstraints(4, 1, 6, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelCodPostal, 
                    new GridBagConstraints(9, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldCodPostal(), 
                    new GridBagConstraints(9, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
        }
        return jPanelDatosLocalizacion;
    }
    
    /**
     * This method initializes jTextFieldCif	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldCif()
    {
        if (jTextFieldCif == null)
        {
            jTextFieldCif = new TextField(9);
        }
        return jTextFieldCif;
    }
    
    /**
     * This method initializes jTextFieldRazonSocial	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldRazonSocial()
    {
        if (jTextFieldRazonSocial == null)
        {
            jTextFieldRazonSocial = new TextField(60);
        }
        return jTextFieldRazonSocial;
    }
    
    
    /**
     * This method initializes jComboBoxTipoVia	
     * 	
     * @return javax.swing.JComboBox	
     */
    private ComboBoxEstructuras getJComboBoxTipoVia()
    {
        if (jComboBoxTipoVia == null)
        {           
            //Estructuras.cargarEstructura("Tipos de via normalizados del INE");
        	Estructuras.cargarEstructura("Tipo de vía");
            jComboBoxTipoVia = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        }
        return jComboBoxTipoVia;
    }
    
    /**
     * This method initializes jTextFieldDireccion	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDireccion()
    {
        if (jTextFieldDireccion == null)
        {
            jTextFieldDireccion = new TextField(25);
        }
        return jTextFieldDireccion;
    }
    
    /**
     * This method initializes jButtonBuscarDireccion	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonBuscarDireccion()
    {
        if (jButtonBuscarDireccion == null)
        {
            jButtonBuscarDireccion = new JButton();
            jButtonBuscarDireccion.setIcon(IconLoader.icon(GestionExpedientePanel.ICONO_BUSCAR));
            jButtonBuscarDireccion.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	
                	String tipoVia = "";
                    if(jComboBoxTipoVia.getSelectedPatron()!=null){
                    	tipoVia = jComboBoxTipoVia.getSelectedPatron().toString();
                    }                	
                    ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldDireccion.getText(),tipoVia);
                    
                    dialog.setVisible(true);   
                    
                    String nombreVia = dialog.getVia();
                    
                    jTextFieldDireccion.setText(nombreVia);
                }
                    });
            jButtonBuscarDireccion.setName("_buscarvia");
        }
        return jButtonBuscarDireccion;
    }
    
    /**
     * This method initializes jTextFieldNumero	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumero()
    {
        if (jTextFieldNumero == null)
        {
            jTextFieldNumero = new TextField(4);
            jTextFieldNumero.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumero,4, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldNumero;
    }
    
    /**
     * This method initializes jTextFieldLetra	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldLetra()
    {
        if (jTextFieldLetra == null)
        {
            jTextFieldLetra = new TextField(1);
        }
        return jTextFieldLetra;
    }
    
    /**
     * This method initializes jTextFieldNumeroD	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumeroD()
    {
        if (jTextFieldNumeroD == null)
        {
            jTextFieldNumeroD = new TextField(4);
            jTextFieldNumeroD.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumeroD,4, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldNumeroD;
    }
    
    /**
     * This method initializes jTextFieldLetraD	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldLetraD()
    {
        if (jTextFieldLetraD == null)
        {
            jTextFieldLetraD = new TextField(1);
        }
        return jTextFieldLetraD;
    }
    
    /**
     * This method initializes jTextFieldBloque	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldBloque()
    {
        if (jTextFieldBloque == null)
        {
            jTextFieldBloque = new TextField(4);
        }
        return jTextFieldBloque;
    }
    
    /**
     * This method initializes jTextFieldEscalera	
     * 	
     * @return javax.swing.JTextField	
     */
    
    private JComboBox getJComboBoxEscalera()
    {
        if (jComboBoxEscalera  == null)
        {
        	jComboBoxEscalera = new JComboBox();
        	jComboBoxEscalera.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxEscalera;
    }
    /**
     * This method initializes jTextFieldPlanta   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxPlanta()
    {
        if (jComboBoxPlanta  == null)
        {
        	jComboBoxPlanta = new JComboBox();
        	jComboBoxPlanta.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxPlanta;
    }
    
    /**
     * This method initializes jTextFieldPuerta	
     * 	
     * @return javax.swing.JTextField	
     */
    
    private JComboBox getJComboBoxPuerta()
    {
        if (jComboBoxPuerta  == null)
        {
        	jComboBoxPuerta = new JComboBox();
        	jComboBoxPuerta.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxPuerta;
    }
    
    /**
     * This method initializes jTextFieldDirNoEstructurada	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDirNoEstructurada()
    {
        if (jTextFieldDirNoEstructurada == null)
        {
            jTextFieldDirNoEstructurada = new TextField(25);
        }
        return jTextFieldDirNoEstructurada;
    }
    
    /**
     * This method initializes jTextFieldKm	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldKm()
    {
        if (jTextFieldKm == null)
        {
            jTextFieldKm = new TextField(6);
            jTextFieldKm.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldKm,6, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldKm;
    }
    
    
    /**
     * This method initializes jComboBoxProvincia	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxProvincia()
    {
        if (jComboBoxProvincia == null)
        {
            jComboBoxProvincia = new JComboBox();
            jComboBoxProvincia.setRenderer(new UbicacionListCellRenderer());
            
            jComboBoxProvincia.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {   
                    
                    if (jComboBoxProvincia.getSelectedIndex()==0)
                    {
                        jComboBoxMunicipio.removeAllItems();
                    }
                    else
                    {
                        EdicionOperations oper = new EdicionOperations();
                        EdicionUtils.cargarLista(getJComboBoxMunicipio(), 
                                oper.obtenerMunicipios((Provincia)jComboBoxProvincia.getSelectedItem()));
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
    private JComboBox getJComboBoxMunicipio()
    {
        if (jComboBoxMunicipio == null)
        {
            jComboBoxMunicipio = new JComboBox();
            jComboBoxMunicipio.setRenderer(new UbicacionListCellRenderer());
        }
        return jComboBoxMunicipio;
    }
    
    /**
     * This method initializes jTextFieldCodPostal	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldCodPostal()
    {
        if (jTextFieldCodPostal == null)
        {
            jTextFieldCodPostal = new TextField(5);
            jTextFieldCodPostal.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCodPostal,5, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldCodPostal;
    }
    
    public void okPressed()
    {
        okPressed = true;
    }

    /**
     * Comprueba que el campo NIF es correcto, incluida la letra del mismo si es que está incluida.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaNif(String sNif)
    {
    	boolean okNif=false;
    	char letraAux;
    	StringBuffer sNumeros= new StringBuffer();
    	int i,ndni;
    	char tabla[]={'T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E'};
    	if(sNif!=null && sNif.length()>0){
	    	for (i=0;i<sNif.length();i++) {
		    	if ("1234567890".indexOf(sNif.charAt(i))!=-1) {
		    	sNumeros.append(sNif.charAt(i));
		    	}else{
		    		break;
		    	}
	    	}
	    	if(i==sNif.length()-1 || i==sNif.length()){ //si no hay letra o si sí la hay y está en última posición
	    		if(i==sNif.length()-1){ //si hay letra al final
			    	try{
				    	ndni=Integer.parseInt(sNumeros.toString());
				    	letraAux=Character.toUpperCase(sNif.charAt(sNif.length()-1));		
				    	char letra=tabla[ndni%23];
				    	if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(letraAux)!=-1) {
				    	   if((sNumeros.toString()).length()<=8){
				    	   if (letraAux==letra && (sNumeros.toString()).length()<=8) // letra adecuada para el NIF
				    		   okNif=true;
				    	   }
				    	} 
				    }catch(Exception e){  
				    	okNif=false;
				    }
	    		}
	    		if(i==sNif.length() && (sNumeros.toString()).length()<=8){//si no hay letra y el tamaño es menor o igual a 8
	    			okNif=true;
	    		}
	    	}
    	}
    	return okNif;
    }
    /**
     * Comprueba que el campo CIF es correcto, incluido el dígito de control.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaCif(String sCif)
    {
    	boolean okCif=false;
    	char letra;
    	if(sCif!=null && sCif.length()>0){
    		try{
		    	letra=Character.toUpperCase(sCif.charAt(0));
	    		if("ABCDEFGHPQSKLM".indexOf(letra)!=-1){
	    			okCif=this.validaDigControlCIF(sCif.substring(1));
	    		}
    		}catch(Exception e){
    			okCif=false;
    		}

    	}
    	return okCif;
    }
    /**
     * Comprueba que el dígito de control del CIF es el correcto.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean validaDigControlCIF(String cifras){
    	boolean okDig=false;
    	int digito, resto;
    	int suma=0;
    	if(cifras.length()>0){
	    	 cifras=GeopistaFunctionUtils.completarConCeros(cifras, 8);
	    	
	    	if(cifras.length()==8){
	    		try{
	    			digito=Integer.parseInt(cifras.substring(cifras.length()-1));
	    			cifras=cifras.substring(0,cifras.length()-1);
	    			for(int ind=1; ind<cifras.length(); ind=ind+2){//suma de las posiciones pares
	    				suma=suma+Integer.parseInt(cifras.substring(ind,ind+1));
	    			}
	    			for(int ind=0; ind<cifras.length(); ind=ind+2){
	    				int num=Integer.parseInt(cifras.substring(ind,ind+1))*2; //posiciones impares multiplicadas por 2
	    				num=(num/10)+(num%10); //suma de los digitos del resultado
	    				suma=suma+num; 
	    			}
	    			resto=(10-(suma%10))%10;
	    			if(resto==digito)
	    				okDig=true;   			
	    		}catch(Exception e){
	    			okDig=false;
	    		}
	    	}
    	}
    	return okDig;
    }

    public boolean datosMinimosYCorrectos()
    {

        return  (jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase("")) &&
        (jTextFieldRazonSocial.getText()!=null && !jTextFieldRazonSocial.getText().equalsIgnoreCase("")) &&
        (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedItem()!=null && jComboBoxTipoVia.getSelectedIndex()>0) &&
        (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0);
    }
}
