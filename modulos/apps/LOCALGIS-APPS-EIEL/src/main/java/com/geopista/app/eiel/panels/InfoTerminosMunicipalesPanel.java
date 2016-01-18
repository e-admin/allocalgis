/**
 * InfoTerminosMunicipalesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.dialogs.InfoTerminosMunicipalesDialog;
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

public class InfoTerminosMunicipalesPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;
    
    public boolean okPressed = false;	

	private JLabel jLabelCodProv = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelCodEntidad = null;
	private JLabel jLabelCodPoblamiento = null;
	
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JComboBox jComboBoxEntidad = null;
    private JComboBox jComboBoxNucleo = null;
    
    private JLabel jLabelTvAnt = null;
	private JLabel jLabelTvCa = null;
	private JLabel jLabelGSM = null;
	private JLabel jLabelUMTS = null;
	private JLabel jLabelGPRS = null;
	private JLabel jLabelCorreo = null;
	private JLabel jLabelRDSI = null;
	private JLabel jLabelADSL = null;
	private JLabel jLabelWifi = null;
	private JLabel jLabelCable = null;
	private JLabel jLabelRed = null;
	private JLabel jLabelSatelite = null;
	private JLabel jLabelCapi = null;
	private JLabel jLabelElectric = null;
	private JLabel jLabelGas = null;
	private JLabel jLabelVivAlu = null;
	private JLabel jLabelLongAlu = null;
	private JLabel jLabelObserv = null;
	private JLabel jLabelFechaRevi = null;
	private JLabel jLabelEstado = null;

	private ComboBoxEstructuras jComboBoxTvAnt = null;
	private ComboBoxEstructuras jComboBoxTvCa = null;
	private ComboBoxEstructuras jComboBoxGSM = null;
	private ComboBoxEstructuras jComboBoxUMTS = null;
	private ComboBoxEstructuras jComboBoxCorreo = null;
	private ComboBoxEstructuras jComboBoxRDSI = null;
	private ComboBoxEstructuras jComboBoxADSL = null;
	private ComboBoxEstructuras jComboBoxWifi = null;
	private ComboBoxEstructuras jComboBoxCable = null;
	private ComboBoxEstructuras jComboBoxRed = null;
	private ComboBoxEstructuras jComboBoxSatelite = null;
	private ComboBoxEstructuras jComboBoxCapi = null;
	private ComboBoxEstructuras jComboBoxElectric = null;
	private ComboBoxEstructuras jComboBoxGas = null;	
	private JTextField jTextFieldVivAlu = null;
	private JTextField jTextFieldLongAlu = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldFechaRev = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	private ComboBoxEstructuras jComboBoxGPRS=null;
	
    public InfoTerminosMunicipalesPanel(){
        super();
        initialize();
    }
  
    public InfoTerminosMunicipalesPanel(NucleoEncuestado7EIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    public void loadData(NucleoEncuestado7EIEL elemento){
        if (elemento!=null){
        	/* Campos Clave */
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
            /* Otros Campos */
            if (elemento.getTvAntena() != null){
        		jComboBoxTvAnt.setSelectedPatron(elemento.getTvAntena().toString());
        	}
        	else{
        		jComboBoxTvAnt.setSelectedIndex(0);
        	}
            if (elemento.getTvCable() != null){
        		jComboBoxTvCa.setSelectedPatron(elemento.getTvCable().toString());
        	}
        	else{
        		jComboBoxTvCa.setSelectedIndex(0);
        	}
            if (elemento.getCalidadGSM() != null){
        		jComboBoxGSM.setSelectedPatron(elemento.getCalidadGSM().toString());
        	}
        	else{
        		jComboBoxGSM.setSelectedIndex(0);
        	}
            if (elemento.getCalidadUMTS() != null){
        		jComboBoxUMTS.setSelectedPatron(elemento.getCalidadUMTS().toString());
        	}
        	else{
        		jComboBoxUMTS.setSelectedIndex(0);
        	}
            if (elemento.getCalidadGPRS() != null){
            	jComboBoxGPRS.setSelectedPatron(elemento.getCalidadGPRS().toString());
        	}
        	else{
        		jComboBoxGPRS.setSelectedIndex(0);
        	}
            if (elemento.getCorreos() != null){
        		jComboBoxCorreo.setSelectedPatron(elemento.getCorreos().toString());
        	}
        	else{
        		jComboBoxCorreo.setSelectedIndex(0);
        	}
            if (elemento.getRdsi() != null){
        		jComboBoxRDSI.setSelectedPatron(elemento.getRdsi().toString());
        	}
        	else{
        		jComboBoxRDSI.setSelectedIndex(0);
        	}
            if (elemento.getAdsl() != null){
        		jComboBoxADSL.setSelectedPatron(elemento.getAdsl().toString());
        	}
        	else{
        		jComboBoxADSL.setSelectedIndex(0);
        	}
            if (elemento.getWifi() != null){
        		jComboBoxWifi.setSelectedPatron(elemento.getWifi().toString());
        	}
        	else{
        		jComboBoxWifi.setSelectedIndex(0);
        	}
            if (elemento.getInternetTV() != null){
        		jComboBoxCable.setSelectedPatron(elemento.getInternetTV().toString());
        	}
        	else{
        		jComboBoxCable.setSelectedIndex(0);
        	}
            if (elemento.getInternetRed() != null){
        		jComboBoxRed.setSelectedPatron(elemento.getInternetRed().toString());
        	}
        	else{
        		jComboBoxRed.setSelectedIndex(0);
        	}
            if (elemento.getInternetSatelite() != null){
        		jComboBoxSatelite.setSelectedPatron(elemento.getInternetSatelite().toString());
        	}
        	else{
        		jComboBoxSatelite.setSelectedIndex(0);
        	}      
            if (elemento.getInternetPublico() != null){
        		jComboBoxCapi.setSelectedPatron(elemento.getInternetPublico().toString());
        	}
        	else{
        		jComboBoxCapi.setSelectedIndex(0);
        	}
            if (elemento.getCalidadElectricidad() != null){
        		jComboBoxElectric.setSelectedPatron(elemento.getCalidadElectricidad().toString());
        	}
        	else{
        		jComboBoxElectric.setSelectedIndex(0);
        	}
            if (elemento.getCalidadGas() != null){
        		jComboBoxGas.setSelectedPatron(elemento.getCalidadGas().toString());
        	}
        	else{
        		jComboBoxGas.setSelectedIndex(0);
        	}
            if (elemento.getViviendasDeficitariasAlumbrado() != null){
        		jTextFieldVivAlu.setText(elemento.getViviendasDeficitariasAlumbrado().toString());
        	}
        	else{
        		jTextFieldVivAlu.setText("");
        	}
            if (elemento.getLongitudDeficitariaAlumbrado() != null){
        		jTextFieldLongAlu.setText(elemento.getLongitudDeficitariaAlumbrado().toString());
        	}
        	else{
        		jTextFieldLongAlu.setText("");
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
    
    public void loadData(){

    	Object object = AppContext.getApplicationContext().getBlackboard().get("infoterminosmunicipales_panel");    	
    	if (object != null && object instanceof NucleoEncuestado7EIEL){    		
    		NucleoEncuestado7EIEL elemento = (NucleoEncuestado7EIEL)object;
        	/* Campos Clave */
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
            /* Otros Campos */
            if (elemento.getTvAntena() != null){
        		jComboBoxTvAnt.setSelectedPatron(elemento.getTvAntena().toString());
        	}
        	else{
        		jComboBoxTvAnt.setSelectedIndex(0);
        	}
            if (elemento.getTvCable() != null){
        		jComboBoxTvCa.setSelectedPatron(elemento.getTvCable().toString());
        	}
        	else{
        		jComboBoxTvCa.setSelectedIndex(0);
        	}
            if (elemento.getCalidadGSM() != null){
        		jComboBoxGSM.setSelectedPatron(elemento.getCalidadGSM().toString());
        	}
        	else{
        		jComboBoxGSM.setSelectedIndex(0);
        	}
            if (elemento.getCalidadUMTS() != null){
        		jComboBoxUMTS.setSelectedPatron(elemento.getCalidadUMTS().toString());
        	}
        	else{
        		jComboBoxUMTS.setSelectedIndex(0);
        	}
            if (elemento.getCalidadGPRS() != null){
        		jComboBoxGPRS.setSelectedPatron(elemento.getCalidadGPRS().toString());
        	}
        	else{
        		jComboBoxGPRS.setSelectedIndex(0);
        	}
            if (elemento.getCorreos() != null){
        		jComboBoxCorreo.setSelectedPatron(elemento.getCorreos().toString());
        	}
        	else{
        		jComboBoxCorreo.setSelectedIndex(0);
        	}
            if (elemento.getRdsi() != null){
        		jComboBoxRDSI.setSelectedPatron(elemento.getRdsi().toString());
        	}
        	else{
        		jComboBoxRDSI.setSelectedIndex(0);
        	}
            if (elemento.getAdsl() != null){
        		jComboBoxADSL.setSelectedPatron(elemento.getAdsl().toString());
        	}
        	else{
        		jComboBoxADSL.setSelectedIndex(0);
        	}
            if (elemento.getWifi() != null){
        		jComboBoxWifi.setSelectedPatron(elemento.getWifi().toString());
        	}
        	else{
        		jComboBoxWifi.setSelectedIndex(0);
        	}
            if (elemento.getInternetTV() != null){
        		jComboBoxCable.setSelectedPatron(elemento.getInternetTV().toString());
        	}
        	else{
        		jComboBoxCable.setSelectedIndex(0);
        	}
            if (elemento.getInternetRed() != null){
        		jComboBoxRed.setSelectedPatron(elemento.getInternetRed().toString());
        	}
        	else{
        		jComboBoxRed.setSelectedIndex(0);
        	}
            if (elemento.getInternetSatelite() != null){
        		jComboBoxSatelite.setSelectedPatron(elemento.getInternetSatelite().toString());
        	}
        	else{
        		jComboBoxSatelite.setSelectedIndex(0);
        	}      
            if (elemento.getInternetPublico() != null){
        		jComboBoxCapi.setSelectedPatron(elemento.getInternetPublico().toString());
        	}
        	else{
        		jComboBoxCapi.setSelectedIndex(0);
        	}
            if (elemento.getCalidadElectricidad() != null){
        		jComboBoxElectric.setSelectedPatron(elemento.getCalidadElectricidad().toString());
        	}
        	else{
        		jComboBoxElectric.setSelectedIndex(0);
        	}
            if (elemento.getCalidadGas() != null){
        		jComboBoxGas.setSelectedPatron(elemento.getCalidadGas().toString());
        	}
        	else{
        		jComboBoxGas.setSelectedIndex(0);
        	}
            if (elemento.getViviendasDeficitariasAlumbrado() != null){
        		jTextFieldVivAlu.setText(elemento.getViviendasDeficitariasAlumbrado().toString());
        	}
        	else{
        		jTextFieldVivAlu.setText("");
        	}
            if (elemento.getLongitudDeficitariaAlumbrado() != null){
        		jTextFieldLongAlu.setText(elemento.getLongitudDeficitariaAlumbrado().toString());
        	}
        	else{
        		jTextFieldLongAlu.setText("");
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
        }	
    }
    
    
    public NucleoEncuestado7EIEL getInfoTerminosMunicipales (NucleoEncuestado7EIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new NucleoEncuestado7EIEL();
            }
            /* Claves: COMBOBOX  y JTEXT */
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            /* JTEXT - Integer */
            if (jTextFieldVivAlu.getText()!=null && !jTextFieldVivAlu.getText().equals("")){
            	elemento.setViviendasDeficitariasAlumbrado(new Integer(jTextFieldVivAlu.getText()));
            } else if (jTextFieldVivAlu.getText().equals("")){
            	elemento.setViviendasDeficitariasAlumbrado(new Integer(0));
            }
            if (jTextFieldLongAlu.getText()!=null && !jTextFieldLongAlu.getText().equals("")){
            	elemento.setLongitudDeficitariaAlumbrado(new Integer(jTextFieldLongAlu.getText()));
            } else if (jTextFieldLongAlu.getText().equals("")){
            	elemento.setLongitudDeficitariaAlumbrado(new Integer(0));
            }
            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
            
            /* JTEXT - String */
            if (jComboBoxTvAnt.getSelectedPatron()!=null)
            	elemento.setTvAntena((String) jComboBoxTvAnt.getSelectedPatron());
            else elemento.setTvAntena("");
            if (jComboBoxTvCa.getSelectedPatron()!=null)
            	elemento.setTvCable((String) jComboBoxTvCa.getSelectedPatron());
            else elemento.setTvCable("");
            if (jComboBoxGSM.getSelectedPatron()!=null)
            	elemento.setCalidadGSM((String) jComboBoxGSM.getSelectedPatron());
            else elemento.setCalidadGSM("");
            if (jComboBoxUMTS.getSelectedPatron()!=null)
            	elemento.setCalidadUMTS((String) jComboBoxUMTS.getSelectedPatron());
            else elemento.setCalidadUMTS("");
            if (jComboBoxGPRS.getSelectedPatron()!=null)
            	elemento.setCalidadGPRS((String) jComboBoxGPRS.getSelectedPatron());
            else elemento.setCalidadGPRS("");
            if (jComboBoxCorreo.getSelectedPatron()!=null)
            	elemento.setCorreos((String) jComboBoxCorreo.getSelectedPatron());
            else elemento.setCorreos("");
            if (jComboBoxRDSI.getSelectedPatron()!=null)
            	elemento.setRdsi((String) jComboBoxRDSI.getSelectedPatron());
            else elemento.setRdsi("");
            if (jComboBoxADSL.getSelectedPatron()!=null)
            	elemento.setAdsl((String) jComboBoxADSL.getSelectedPatron());
            else elemento.setAdsl("");
            if (jComboBoxWifi.getSelectedPatron()!=null)
            	elemento.setWifi((String) jComboBoxWifi.getSelectedPatron());
            else elemento.setWifi("");
            if (jComboBoxCable.getSelectedPatron()!=null)
            	elemento.setInternetTV((String) jComboBoxCable.getSelectedPatron());
            else elemento.setInternetTV("");
            if (jComboBoxRed.getSelectedPatron()!=null)
            	elemento.setInternetRed((String) jComboBoxRed.getSelectedPatron());
            else elemento.setInternetRed("");
            if (jComboBoxSatelite.getSelectedPatron()!=null)
            	elemento.setInternetSatelite((String) jComboBoxSatelite.getSelectedPatron());
            else elemento.setInternetSatelite("");
            if (jComboBoxCapi.getSelectedPatron()!=null)
            	elemento.setInternetPublico((String) jComboBoxCapi.getSelectedPatron());
            else elemento.setInternetPublico("");
            if (jComboBoxElectric.getSelectedPatron()!=null)
            	elemento.setCalidadElectricidad((String) jComboBoxElectric.getSelectedPatron());
            else elemento.setCalidadElectricidad("");
            if (jComboBoxGas.getSelectedPatron()!=null)
            	elemento.setCalidadGas((String) jComboBoxGas.getSelectedPatron());
            else elemento.setCalidadGas("");
            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            /* JTEXT - Date */
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
        }	
        return elemento;
    }
    
    private void initialize(){      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.infoterminosmunicipales.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(InfoTerminosMunicipalesDialog.DIM_X,InfoTerminosMunicipalesDialog.DIM_Y);
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
		if (Identificadores.get("ListaProvinciasGenerales") == null) {
			ArrayList lst = oper.obtenerProvinciasConNombre();
			Identificadores.put("ListaProvinciasGenerales", lst);
			EdicionUtils.cargarLista(getJComboBoxProvincia(),
					lst);
			Provincia p = new Provincia();
			p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
			p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
			getJComboBoxProvincia().setSelectedItem(p);
		} else {
			EdicionUtils.cargarLista(getJComboBoxProvincia(),
					(ArrayList) Identificadores.get("ListaProvinciasGenerales"));
			 /*EdicionUtils.cargarLista(getJComboBoxProvincia(),
			 oper.obtenerProvinciasConNombre());*/
		}
		loadData();
    }
    
/* Metodos que devuelven CAMPOS CLAVE */
 
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

	private JComboBox getJComboBoxNucleo() {
		if (jComboBoxNucleo == null) {
			jComboBoxNucleo = new JComboBox();
			jComboBoxNucleo.setRenderer(new UbicacionListCellRenderer());
		}
		return jComboBoxNucleo;
	}

    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private ComboBoxEstructuras getJComboBoxTvAnt()
    { 
        if (jComboBoxTvAnt == null)
        {
            Estructuras.cargarEstructura("eiel_Cobertura del servicio TV por antena");
            jComboBoxTvAnt = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTvAnt.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTvAnt;        
    }

    private ComboBoxEstructuras getJComboBoxTvCa()
    { 
        if (jComboBoxTvCa == null)
        {
            Estructuras.cargarEstructura("eiel_Cobertura del servicio TV por cable");
            jComboBoxTvCa = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTvCa.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTvCa;        
    }

    private ComboBoxEstructuras getJComboBoxGSM()
    { 
        if (jComboBoxGSM == null)
        {
            Estructuras.cargarEstructura("eiel_Calidad de cobertura en telefonía con sistema GSM");
            jComboBoxGSM = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxGSM.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGSM;        
    }

    private ComboBoxEstructuras getJComboBoxUMTS()
    { 
        if (jComboBoxUMTS == null)
        {
            Estructuras.cargarEstructura("eiel_Calidad de cobertura en telefonía con sistema UMTS");
            jComboBoxUMTS = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxUMTS.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxUMTS;        
    }

    private ComboBoxEstructuras getJComboBoxGPRS()
    { 
        if (jComboBoxGPRS == null)
        {
            Estructuras.cargarEstructura("eiel_Calidad de cobertura en telefonía con sistema GPRS");
            jComboBoxGPRS = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxGPRS.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGPRS;        
    }
    private ComboBoxEstructuras getJComboBoxCorreo()
    { 
        if (jComboBoxCorreo == null)
        {
            Estructuras.cargarEstructura("eiel_Existencia de oficina de Correos");
            jComboBoxCorreo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCorreo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCorreo;        
    }

    private ComboBoxEstructuras getJComboBoxRDSI()
    { 
        if (jComboBoxRDSI == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso RDSI");
            jComboBoxRDSI = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxRDSI.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxRDSI;        
    }

    private ComboBoxEstructuras getJComboBoxADSL()
    { 
        if (jComboBoxADSL == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso XDSL");
            jComboBoxADSL = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxADSL.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxADSL;        
    }

    private ComboBoxEstructuras getJComboBoxWifi()
    { 
        if (jComboBoxWifi == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso Inalámbrico");
            jComboBoxWifi = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxWifi.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxWifi;        
    }

    private ComboBoxEstructuras getJComboBoxCable()
    { 
        if (jComboBoxCable == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso tv_cable");
            jComboBoxCable = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCable.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCable;        
    }

    private ComboBoxEstructuras getJComboBoxRed()
    { 
        if (jComboBoxRed == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso red electrica");
            jComboBoxRed = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxRed.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxRed;        
    }

    private ComboBoxEstructuras getJComboBoxSatelite()
    { 
        if (jComboBoxSatelite == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso Satélite");
            jComboBoxSatelite = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxSatelite.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxSatelite;        
    }

    private ComboBoxEstructuras getJComboBoxCapi()
    { 
        if (jComboBoxCapi == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso Público");
            jComboBoxCapi = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCapi.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCapi;        
    }

    private ComboBoxEstructuras getJComboBoxElectric()
    { 
        if (jComboBoxElectric == null)
        {
            Estructuras.cargarEstructura("eiel_Calidad del servicio o suministro");
            jComboBoxElectric = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxElectric.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxElectric;        
    }

    private ComboBoxEstructuras getJComboBoxGas()
    { 
        if (jComboBoxGas == null)
        {
            Estructuras.cargarEstructura("eiel_Calidad del servicio o suministro");
            jComboBoxGas = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxGas.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGas;        
    }

    
    private JTextField getjTextFieldVivAlu(){
    	if (jTextFieldVivAlu == null){
    		jTextFieldVivAlu = new TextField(10);
    		jTextFieldVivAlu.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivAlu,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivAlu;
    }
    
    private JTextField getjTextFieldLongAlu(){
    	if (jTextFieldLongAlu == null){
    		jTextFieldLongAlu = new TextField(10);
    		jTextFieldLongAlu.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLongAlu,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLongAlu;
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
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado;        
    }
    
    
    public InfoTerminosMunicipalesPanel(GridBagLayout layout){
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
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 
            jLabelCodEntidad  = new JLabel("", JLabel.CENTER);
            jLabelCodEntidad.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codentidad")));
            jLabelCodPoblamiento = new JLabel("", JLabel.CENTER);
            jLabelCodPoblamiento.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nucleo")));
            /* Agregamos las Labels al JPANELIDENTIFICATION */
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
            jPanelIdentificacion.add(jLabelCodEntidad, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(getJComboBoxEntidad(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(jLabelCodPoblamiento, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(getJComboBoxNucleo(), 
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
        	jLabelTvAnt = new JLabel("", JLabel.CENTER); 
        	jLabelTvAnt.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tv_ant"));
            jLabelTvCa = new JLabel("", JLabel.CENTER); 
            jLabelTvCa.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tv_ca")); 
            jLabelGSM = new JLabel("", JLabel.CENTER); 
            jLabelGSM.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tm_gsm")); 
            jLabelUMTS = new JLabel("", JLabel.CENTER); 
            jLabelUMTS.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tm_umts"));
            jLabelCorreo = new JLabel("", JLabel.CENTER); 
            jLabelGPRS = new JLabel("", JLabel.CENTER); 
            jLabelGPRS.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tm_gprs"));          
            jLabelCorreo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.correo"));
            jLabelRDSI = new JLabel("", JLabel.CENTER); 
            jLabelRDSI.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rdsi"));
            jLabelADSL = new JLabel("", JLabel.CENTER); 
            jLabelADSL.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.adsl"));
            jLabelWifi = new JLabel("", JLabel.CENTER); 
            jLabelWifi.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.wifi"));
            jLabelCable = new JLabel("", JLabel.CENTER); 
            jLabelCable.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tv_cable"));
            jLabelRed = new JLabel("", JLabel.CENTER); 
            jLabelRed.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.red_domestica"));
            jLabelSatelite = new JLabel("", JLabel.CENTER);
            jLabelSatelite.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.satelite"));            
            jLabelCapi = new JLabel("", JLabel.CENTER);
            jLabelCapi.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capi"));
            jLabelElectric = new JLabel("", JLabel.CENTER);
            jLabelElectric.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.electric"));
            jLabelGas = new JLabel("", JLabel.CENTER);
            jLabelGas.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gas"));
            jLabelVivAlu = new JLabel("", JLabel.CENTER);
            jLabelVivAlu.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv_alu"));
            jLabelLongAlu = new JLabel("", JLabel.CENTER);
            jLabelLongAlu.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.long_alu"));
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelTvAnt,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxTvAnt(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCable,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCable(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelTvCa,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxTvCa(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelGSM,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxGSM(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelUMTS,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxUMTS(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelGPRS,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxGPRS(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            JPanel panelCorreo = new JPanel(new GridBagLayout());
            
            panelCorreo.add(jLabelCorreo,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            panelCorreo.add(getJComboBoxCorreo(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 170, 0));
            jPanelInformacion.add(panelCorreo,
                    new GridBagConstraints(0, 3, 2, 1,0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(5, 5, 5, 5), 5, 0));           
            jPanelInformacion.add(jLabelRDSI,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxRDSI(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelADSL,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxADSL(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelWifi,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxWifi(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelSatelite,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxSatelite(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelLongAlu,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldLongAlu(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelRed,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxRed(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCapi,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCapi(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelElectric,
                    new GridBagConstraints(2, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxElectric(), 
                    new GridBagConstraints(3, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelGas,
                    new GridBagConstraints(0, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxGas(), 
                    new GridBagConstraints(1, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivAlu,
                    new GridBagConstraints(2, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivAlu(), 
                    new GridBagConstraints(3, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            JPanel panelObservaciones = new JPanel(new GridBagLayout());
            
            panelObservaciones.add(jLabelObserv,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            panelObservaciones.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 200, 0));
            
            panelObservaciones.add(new JLabel(), 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 175, 0));
            panelObservaciones.add(new JLabel(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 175, 0));
            
            jPanelInformacion.add(panelObservaciones,
                    new GridBagConstraints(0, 9, 4, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            
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
                            new Insets(5, 5, 5, 5), 20, 0));
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
        return (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&
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
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelInfoTerminosMunicipalesEIEL(codprov, codmunic, entidad, nucleo));
	        	
	        	loadDataIdentificacion(codprov, codmunic, entidad, nucleo);       	
			}
		}
	
	
	
	public void loadDataIdentificacion(String codprov, String codmunic,
			String entidad, String nucleo) {
		
		//Datos identificacion
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
