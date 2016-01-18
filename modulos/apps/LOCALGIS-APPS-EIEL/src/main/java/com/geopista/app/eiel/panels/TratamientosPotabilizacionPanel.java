/**
 * TratamientosPotabilizacionPanel.java
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

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.dialogs.TratamientosPotabilizacionDialog;
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

public class TratamientosPotabilizacionPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;
    
    public boolean okPressed = false;	

    private JLabel jLabelClave = null;
	private JLabel jLabelCodProv = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldClave = null;
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JTextField jTextFieldOrden = null;

    private JLabel jLabelTipo = null;
	private JLabel jLabelUbicacion = null;
	private JLabel jLabelSoloDesinf = null;
	private JLabel jLabelCategoriaA1 = null;
	private JLabel jLabelCategoriaA2 = null;
	private JLabel jLabelCategoriaA3 = null;
	private JLabel jLabelDesaladora = null;
	private JLabel jLabelOtros = null;
	private JLabel jLabelDesinf1 = null;
	private JLabel jLabelDesinf2 = null;
	private JLabel jLabelDesinf3 = null;
	private JLabel jLabelPerioricidad = null;
	private JLabel jLabelOrg_Control = null;
	private JLabel jLabelEstTp = null;
	private JLabel jLabelFechaInst = null;
	private JLabel jLabelObserv= null;
	private JLabel jLabelFechaRevi = null;
	private JLabel jLabelEstado = null;

	private ComboBoxEstructuras jComboBoxTipo = null;
	private ComboBoxEstructuras jComboBoxUbicacion = null;
	private ComboBoxEstructuras jComboBoxSoloDesinf = null;
	private ComboBoxEstructuras jComboBoxCategoriaA1 = null;
	private ComboBoxEstructuras jComboBoxCategoriaA2 = null;
	private ComboBoxEstructuras jComboBoxCategoriaA3 = null;
	private ComboBoxEstructuras jComboBoxDesaladora = null;
	private ComboBoxEstructuras jComboBoxOtros = null;
	private ComboBoxEstructuras jComboBoxDesinf1 = null;
	private ComboBoxEstructuras jComboBoxDesinf2 = null;
	private ComboBoxEstructuras jComboBoxDesinf3 = null;
	private ComboBoxEstructuras jComboBoxPerioricidad = null;
	private ComboBoxEstructuras jComboBoxOrg_Control = null;
	private ComboBoxEstructuras jComboBoxEst = null;
	private DateField jTextFieldFechaInst = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldFechaRev = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	
	private String idMunicipioSelected;
	
    public TratamientosPotabilizacionPanel(){
        super();
        initialize();
    }
  
    public TratamientosPotabilizacionPanel(TratamientosPotabilizacionEIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    public void loadData(TratamientosPotabilizacionEIEL elemento){
        if (elemento!=null){
        	
        	idMunicipioSelected=elemento.getCodINEMunicipio();
        	/* Campos Clave */
        	if (elemento.getClave() != null){
        		jTextFieldClave.setText(elemento.getClave());
        	} else{
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
            if (elemento.getOrdenPotabilizadora() != null){
        		jTextFieldOrden.setText(elemento.getOrdenPotabilizadora().toString());
        	} else{
        		jTextFieldOrden.setText("");
        	}
            /* Otros Campos */
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            if (elemento.getUbicacion() != null){
            	jComboBoxUbicacion.setSelectedPatron(elemento.getUbicacion());
        	}
        	else{
        		jComboBoxUbicacion.setSelectedIndex(0);
        	}
            if (elemento.getSoloDesinfeccion() != null){
            	jComboBoxSoloDesinf.setSelectedPatron(elemento.getSoloDesinfeccion());
        	}
        	else{
        		jComboBoxSoloDesinf.setSelectedIndex(0);
        	}
            if (elemento.getCategoriaA1() != null){
            	jComboBoxCategoriaA1.setSelectedPatron(elemento.getCategoriaA1());
        	}
        	else{
        		jComboBoxCategoriaA1.setSelectedIndex(0);
        	}
            if (elemento.getCategoriaA2() != null){
            	jComboBoxCategoriaA2.setSelectedPatron(elemento.getCategoriaA2());
        	}
        	else{
        		jComboBoxCategoriaA2.setSelectedIndex(0);
        	}
            if (elemento.getCategoriaA3() != null){
            	jComboBoxCategoriaA3.setSelectedPatron(elemento.getCategoriaA3());
        	}
        	else{
        		jComboBoxCategoriaA3.setSelectedIndex(0);
        	}
            if (elemento.getDesaladora() != null){
            	jComboBoxDesaladora.setSelectedPatron(elemento.getDesaladora());
        	}
        	else{
        		jComboBoxDesaladora.setSelectedIndex(0);
        	}
            if (elemento.getMetodoDesinfeccion1() != null){
            	jComboBoxDesinf1.setSelectedPatron(elemento.getMetodoDesinfeccion1().toString());
        	} else{
        		jComboBoxDesinf1.setSelectedIndex(0);
        	}
            if (elemento.getMetodoDesinfeccion2() != null){
            	jComboBoxDesinf2.setSelectedPatron(elemento.getMetodoDesinfeccion2().toString());
        	} else{
        		jComboBoxDesinf2.setSelectedIndex(0);
        	}
            if (elemento.getMetodoDesinfeccion3() != null){
            	jComboBoxDesinf3.setSelectedPatron(elemento.getMetodoDesinfeccion3().toString());
        	} else{
        		jComboBoxDesinf3.setSelectedIndex(0);
        	}
            if (elemento.getPerioricidad() != null){
            	jComboBoxPerioricidad.setSelectedPatron(elemento.getPerioricidad().toString());
        	} else{
        		jComboBoxPerioricidad.setSelectedIndex(0);
        	}
            if (elemento.getOrganismoControl() != null){
            	jComboBoxOrg_Control.setSelectedPatron(elemento.getOrganismoControl().toString());
        	} else{
        		jComboBoxOrg_Control.setSelectedIndex(0);
        	}
            if (elemento.getEstado() != null){
            	jComboBoxEst.setSelectedPatron(elemento.getEstado().toString());
        	} else{
        		jComboBoxEst.setSelectedIndex(0);
        	}
            if (elemento.getOtros() != null){
            	jComboBoxOtros.setSelectedPatron(elemento.getOtros().toString());
        	} else{
        		jComboBoxOtros.setSelectedIndex(0);
        	}
            if (elemento.getFechaInstalacion() != null){
        		jTextFieldFechaInst.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
        	} 
            if (elemento.getObserv() != null){
        		jTextFieldObserv.setText(elemento.getObserv().toString());
        	} else{
        		jTextFieldObserv.setText("");
        	} 
                        
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(elemento.getFechaRevision().toString());
        	} else{
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
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.TRATAMIENTOSPOTABILIZACION_CLAVE);
        	        	       	
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

    	Object object = AppContext.getApplicationContext().getBlackboard().get("tratamientospotabilizacion_panel");    	
    	if (object != null && object instanceof TratamientosPotabilizacionEIEL){    		
    		TratamientosPotabilizacionEIEL elemento = (TratamientosPotabilizacionEIEL)object;
        	/* Campos Clave */
        	if (elemento.getClave() != null){
        		jTextFieldClave.setText(elemento.getClave());
        	} else{
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
            if (elemento.getOrdenPotabilizadora() != null){
        		jTextFieldOrden.setText(elemento.getOrdenPotabilizadora().toString());
        	} else{
        		jTextFieldOrden.setText("");
        	}
            /* Otros Campos */
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            if (elemento.getUbicacion() != null){
            	jComboBoxUbicacion.setSelectedPatron(elemento.getUbicacion());
        	}
        	else{
        		jComboBoxUbicacion.setSelectedIndex(0);
        	}
            if (elemento.getSoloDesinfeccion() != null){
            	jComboBoxSoloDesinf.setSelectedPatron(elemento.getSoloDesinfeccion());
        	}
        	else{
        		jComboBoxSoloDesinf.setSelectedIndex(0);
        	}
            if (elemento.getCategoriaA1() != null){
            	jComboBoxCategoriaA1.setSelectedPatron(elemento.getCategoriaA1());
        	}
        	else{
        		jComboBoxCategoriaA1.setSelectedIndex(0);
        	}
            if (elemento.getCategoriaA2() != null){
            	jComboBoxCategoriaA2.setSelectedPatron(elemento.getCategoriaA2());
        	}
        	else{
        		jComboBoxCategoriaA2.setSelectedIndex(0);
        	}
            if (elemento.getCategoriaA3() != null){
            	jComboBoxCategoriaA3.setSelectedPatron(elemento.getCategoriaA3());
        	}
        	else{
        		jComboBoxCategoriaA3.setSelectedIndex(0);
        	}
            if (elemento.getDesaladora() != null){
            	jComboBoxDesaladora.setSelectedPatron(elemento.getDesaladora());
        	}
        	else{
        		jComboBoxDesaladora.setSelectedIndex(0);
        	}
            if (elemento.getMetodoDesinfeccion1() != null){
            	jComboBoxDesinf1.setSelectedPatron(elemento.getMetodoDesinfeccion1().toString());
        	} else{
        		jComboBoxDesinf1.setSelectedIndex(0);
        	}
            if (elemento.getMetodoDesinfeccion2() != null){
            	jComboBoxDesinf2.setSelectedPatron(elemento.getMetodoDesinfeccion2().toString());
        	} else{
        		jComboBoxDesinf2.setSelectedIndex(0);
        	}
            if (elemento.getMetodoDesinfeccion3() != null){
            	jComboBoxDesinf3.setSelectedPatron(elemento.getMetodoDesinfeccion3().toString());
        	} else{
        		jComboBoxDesinf3.setSelectedIndex(0);
        	}
            if (elemento.getPerioricidad() != null){
            	jComboBoxPerioricidad.setSelectedPatron(elemento.getPerioricidad().toString());
        	} else{
        		jComboBoxPerioricidad.setSelectedIndex(0);
        	}
            if (elemento.getOrganismoControl() != null){
            	jComboBoxOrg_Control.setSelectedPatron(elemento.getOrganismoControl().toString());
        	} else{
        		jComboBoxOrg_Control.setSelectedIndex(0);
        	}
            if (elemento.getEstado() != null){
            	jComboBoxEst.setSelectedPatron(elemento.getEstado().toString());
        	} else{
        		jComboBoxEst.setSelectedIndex(0);
        	}
            if (elemento.getOtros() != null){
            	jComboBoxOtros.setSelectedPatron(elemento.getOtros().toString());
        	} else{
        		jComboBoxOtros.setSelectedIndex(0);
        	}
            if (elemento.getFechaInstalacion() != null){
        		jTextFieldFechaInst.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
        	}
            if (elemento.getObserv() != null){
        		jTextFieldObserv.setText(elemento.getObserv().toString());
        	} else{
        		jTextFieldObserv.setText("");
        	} 
                        
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(elemento.getFechaRevision().toString());
        	} else{
        	    
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
    
    
    public TratamientosPotabilizacionEIEL getTratamientosPotabilizacionData (){

    	TratamientosPotabilizacionEIEL	elemento = new TratamientosPotabilizacionEIEL();

    	/* Claves: COMBOBOX  y JTEXT */
    	elemento.setClave(jTextFieldClave.getText());
		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		elemento.setOrdenPotabilizadora(jTextFieldOrden.getText());
    	/* JTEXT - Integer */            
    	if (jComboBoxEstado.getSelectedPatron()!=null)
    		elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));

    	/* JTEXT - String */
    	if (jComboBoxTipo.getSelectedPatron()!=null)
    		elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
    	else elemento.setTipo("");
    	if (jComboBoxUbicacion.getSelectedPatron()!=null)
    		elemento.setUbicacion((String) jComboBoxUbicacion.getSelectedPatron());
    	else elemento.setUbicacion("");
    	if (jComboBoxSoloDesinf.getSelectedPatron()!=null)
    		elemento.setSoloDesinfeccion((String) jComboBoxSoloDesinf.getSelectedPatron());
    	else elemento.setSoloDesinfeccion("");
    	if (jComboBoxCategoriaA1.getSelectedPatron()!=null)
    		elemento.setCategoriaA1((String) jComboBoxCategoriaA1.getSelectedPatron());
    	else elemento.setCategoriaA1("");
    	if (jComboBoxCategoriaA2.getSelectedPatron()!=null)
    		elemento.setCategoriaA2((String) jComboBoxCategoriaA2.getSelectedPatron());
    	else elemento.setCategoriaA2("");
    	if (jComboBoxCategoriaA3.getSelectedPatron()!=null)
    		elemento.setCategoriaA3((String) jComboBoxCategoriaA3.getSelectedPatron());
    	else elemento.setCategoriaA3("");
    	if (jComboBoxDesaladora.getSelectedPatron()!=null)
    		elemento.setDesaladora((String) jComboBoxDesaladora.getSelectedPatron());
    	else elemento.setDesaladora("");
    	if (jComboBoxOtros.getSelectedPatron()!=null)
    		elemento.setOtros((String) jComboBoxOtros.getSelectedPatron());
    	else elemento.setOtros("");
    	if (jComboBoxDesinf1.getSelectedPatron()!=null)
    		elemento.setMetodoDesinfeccion1((String) jComboBoxDesinf1.getSelectedPatron());
    	else elemento.setMetodoDesinfeccion1("");
    	if (jComboBoxDesinf2.getSelectedPatron()!=null)
    		elemento.setMetodoDesinfeccion2((String) jComboBoxDesinf2.getSelectedPatron());
    	else elemento.setMetodoDesinfeccion2("");
    	if (jComboBoxDesinf3.getSelectedPatron()!=null)
    		elemento.setMetodoDesinfeccion3((String) jComboBoxDesinf3.getSelectedPatron());
    	else elemento.setMetodoDesinfeccion3("");
    	if (jComboBoxPerioricidad.getSelectedPatron()!=null)
    		elemento.setPerioricidad((String) jComboBoxPerioricidad.getSelectedPatron());
    	else elemento.setPerioricidad("");
    	if (jComboBoxOrg_Control.getSelectedPatron()!=null)
    		elemento.setOrganismoControl((String) jComboBoxOrg_Control.getSelectedPatron());
    	else elemento.setOrganismoControl("");
    	if (jComboBoxEst.getSelectedPatron()!=null)
    		elemento.setEstado((String) jComboBoxEst.getSelectedPatron());            
    	else elemento.setEstado("");
    	elemento.setObserv(jTextFieldObserv.getText());
    	/* JTEXT - Date */
    	if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){
    		java.util.Date fecha = jTextFieldFechaInst.getDate();
    		elemento.setFechaInstalacion(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));
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
         }  
         else{
         	elemento.setFechaRevision(null);
         }
    	return elemento;
    }
    
    public TratamientosPotabilizacionEIEL getTratamientosPotabilizacion (TratamientosPotabilizacionEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new TratamientosPotabilizacionEIEL();
            }
            /* Claves: COMBOBOX  y JTEXT */
            elemento.setClave(jTextFieldClave.getText());
    		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setOrdenPotabilizadora(jTextFieldOrden.getText());
            /* JTEXT - Integer */            
            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
            
            /* JTEXT - String */
            if (jComboBoxTipo.getSelectedPatron()!=null)
            elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
            else elemento.setTipo("");
            if (jComboBoxUbicacion.getSelectedPatron()!=null)
            	elemento.setUbicacion((String) jComboBoxUbicacion.getSelectedPatron());
            else elemento.setUbicacion("");
            if (jComboBoxSoloDesinf.getSelectedPatron()!=null)
            	elemento.setSoloDesinfeccion((String) jComboBoxSoloDesinf.getSelectedPatron());
            else elemento.setSoloDesinfeccion("");
            if (jComboBoxCategoriaA1.getSelectedPatron()!=null)
            	elemento.setCategoriaA1((String) jComboBoxCategoriaA1.getSelectedPatron());
            else elemento.setCategoriaA1("");
            if (jComboBoxCategoriaA2.getSelectedPatron()!=null)
            	elemento.setCategoriaA2((String) jComboBoxCategoriaA2.getSelectedPatron());
            else elemento.setCategoriaA2("");
            if (jComboBoxCategoriaA3.getSelectedPatron()!=null)
            	elemento.setCategoriaA3((String) jComboBoxCategoriaA3.getSelectedPatron());
            else elemento.setCategoriaA3("");
            if (jComboBoxDesaladora.getSelectedPatron()!=null)
            	elemento.setDesaladora((String) jComboBoxDesaladora.getSelectedPatron());
            else elemento.setDesaladora("");
            if (jComboBoxOtros.getSelectedPatron()!=null)
            	elemento.setOtros((String) jComboBoxOtros.getSelectedPatron());
            else elemento.setOtros("");
            if (jComboBoxDesinf1.getSelectedPatron()!=null)
            	elemento.setMetodoDesinfeccion1((String) jComboBoxDesinf1.getSelectedPatron());
            else elemento.setMetodoDesinfeccion1("");
            if (jComboBoxDesinf2.getSelectedPatron()!=null)
            	elemento.setMetodoDesinfeccion2((String) jComboBoxDesinf2.getSelectedPatron());
            else elemento.setMetodoDesinfeccion2("");
            if (jComboBoxDesinf3.getSelectedPatron()!=null)
            	elemento.setMetodoDesinfeccion3((String) jComboBoxDesinf3.getSelectedPatron());
            else elemento.setMetodoDesinfeccion3("");
            if (jComboBoxPerioricidad.getSelectedPatron()!=null)
            	elemento.setPerioricidad((String) jComboBoxPerioricidad.getSelectedPatron());
            else elemento.setPerioricidad("");
            if (jComboBoxOrg_Control.getSelectedPatron()!=null)
            	elemento.setOrganismoControl((String) jComboBoxOrg_Control.getSelectedPatron());
            else elemento.setOrganismoControl("");
            if (jComboBoxEst.getSelectedPatron()!=null)
            	elemento.setEstado((String) jComboBoxEst.getSelectedPatron());            
            else elemento.setEstado("");
            elemento.setObserv(jTextFieldObserv.getText());
            /* JTEXT - Date */
            if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){
            	java.util.Date fecha = jTextFieldFechaInst.getDate();
            	elemento.setFechaInstalacion(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));
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
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.tratamientospotabilizacion.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(TratamientosPotabilizacionDialog.DIM_X,TratamientosPotabilizacionDialog.DIM_Y);
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
			ArrayList lst = oper.obtenerProvinciasConNombre(true);
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
			 oper.obtenerProvinciasConNombre(true));*/
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
							if (jComboBoxProvincia.getSelectedIndex()==-1)
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

					}
				}
			});
		}
		return jComboBoxMunicipio;
    }
   
    private JTextField getJTextFieldOrden(){
        if (jTextFieldOrden == null){
        	jTextFieldOrden = new TextField(3);
        }
        return jTextFieldOrden;
    }
    
    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private ComboBoxEstructuras getJComboBoxTipo()
    { 
        if (jComboBoxTipo == null)
        {
            Estructuras.cargarEstructura("eiel_Automatización del equipamiento");
            jComboBoxTipo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTipo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTipo;        
    }

    private ComboBoxEstructuras getJComboBoxUbicacion()
    { 
        if (jComboBoxUbicacion == null)
        {
            Estructuras.cargarEstructura("eiel_Ubicación del tratamiento de potabilización");
            jComboBoxUbicacion = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxUbicacion.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxUbicacion;        
    }
    
    private ComboBoxEstructuras getJComboBoxSoloDesinf()
    { 
        if (jComboBoxSoloDesinf == null)
        {
            Estructuras.cargarEstructura("eiel_s_desinf");
            jComboBoxSoloDesinf = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxSoloDesinf.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxSoloDesinf;        
    }

    private ComboBoxEstructuras getJComboBoxCategoriaA1()
    { 
        if (jComboBoxCategoriaA1 == null)
        {
            Estructuras.cargarEstructura("eiel_categoria_a1");
            jComboBoxCategoriaA1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxCategoriaA1.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCategoriaA1;        
    }    
    private ComboBoxEstructuras getJComboBoxCategoriaA2()
    { 
        if (jComboBoxCategoriaA2 == null)
        {
            Estructuras.cargarEstructura("eiel_categoria_a2");
            jComboBoxCategoriaA2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCategoriaA2.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCategoriaA2;        
    }  
    private ComboBoxEstructuras getJComboBoxCategoriaA3()
    { 
        if (jComboBoxCategoriaA3 == null)
        {
            Estructuras.cargarEstructura("eiel_categoria_a3");
            jComboBoxCategoriaA3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCategoriaA3.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCategoriaA3;        
    }

    private ComboBoxEstructuras getJComboBoxDesaladora()
    { 
        if (jComboBoxDesaladora == null)
        {
            Estructuras.cargarEstructura("eiel_desaladora");
            jComboBoxDesaladora = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxDesaladora.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxDesaladora;        
    }
    
    private ComboBoxEstructuras getJComboBoxOtros()
    { 
        if (jComboBoxOtros == null)
        {
            Estructuras.cargarEstructura("eiel_Otras");
            jComboBoxOtros = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxOtros.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxOtros;        
    }

    private ComboBoxEstructuras getJComboBoxDesinf1()
    { 
        if (jComboBoxDesinf1 == null)
        {
            Estructuras.cargarEstructura("eiel_Método de desinfección");
            jComboBoxDesinf1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxDesinf1.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxDesinf1;        
    }
    private ComboBoxEstructuras getJComboBoxDesinf2()
    { 
        if (jComboBoxDesinf2 == null)
        {
            Estructuras.cargarEstructura("eiel_Método de desinfección");
            jComboBoxDesinf2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxDesinf2.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxDesinf2;        
    }
    private ComboBoxEstructuras getJComboBoxDesinf3()
    { 
        if (jComboBoxDesinf3 == null)
        {
            Estructuras.cargarEstructura("eiel_Método de desinfección");
            jComboBoxDesinf3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxDesinf3.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxDesinf3;        
    }

    private ComboBoxEstructuras getJComboBoxPerioricidad()
    { 
        if (jComboBoxPerioricidad == null)
        {
            Estructuras.cargarEstructura("eiel_Periodicidad");
            jComboBoxPerioricidad = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxPerioricidad.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxPerioricidad;        
    }

    private ComboBoxEstructuras getJComboBoxOrg_Control()
    { 
        if (jComboBoxOrg_Control == null)
        {
            Estructuras.cargarEstructura("eiel_Control de calidad: Organismo");
            jComboBoxOrg_Control = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxOrg_Control.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxOrg_Control;        
    }
    
    private ComboBoxEstructuras getJComboBoxEst()
    { 
        if (jComboBoxEst == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEst = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEst.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEst;        
    }
    
    private DateField getjTextFieldFechaInst(){
    	if (jTextFieldFechaInst == null){
    		jTextFieldFechaInst = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaInst.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaInst.setEditable(true);

    	}
    	return jTextFieldFechaInst;
    } 
 
    private JTextField getjTextFieldObserv(){
    	if (jTextFieldObserv == null){
    		jTextFieldObserv = new TextField(50);
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
    
    
    public TratamientosPotabilizacionPanel(GridBagLayout layout){
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
        	jLabelTipo = new JLabel("", JLabel.CENTER); 
        	jLabelTipo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo_equipamiento"));
            jLabelUbicacion = new JLabel("", JLabel.CENTER); 
            jLabelUbicacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.ubicacion_tp")); 
            jLabelSoloDesinf = new JLabel("", JLabel.CENTER); 
            jLabelSoloDesinf.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_desinf")); 
            jLabelCategoriaA1 = new JLabel("", JLabel.CENTER); 
            jLabelCategoriaA1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.cat_a1"));
            jLabelCategoriaA2 = new JLabel("", JLabel.CENTER); 
            jLabelCategoriaA2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.cat_a2"));
            jLabelCategoriaA3 = new JLabel("", JLabel.CENTER); 
            jLabelCategoriaA3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.cat_a3"));
            jLabelDesaladora = new JLabel("", JLabel.CENTER); 
            jLabelDesaladora.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.desaladora"));
            jLabelOtros = new JLabel("", JLabel.CENTER); 
            jLabelOtros.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.otros_tp"));
            jLabelDesinf1 = new JLabel("", JLabel.CENTER); 
            jLabelDesinf1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.desinf1"));
            jLabelDesinf2 = new JLabel("", JLabel.CENTER); 
            jLabelDesinf2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.desinf2"));
            jLabelDesinf3 = new JLabel("", JLabel.CENTER); 
            jLabelDesinf3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.desinf3"));
            jLabelPerioricidad = new JLabel("", JLabel.CENTER); 
            jLabelPerioricidad.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.perioricidad"));
            jLabelOrg_Control = new JLabel("", JLabel.CENTER); 
            jLabelOrg_Control.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.org_control"));
            jLabelEstTp = new JLabel("", JLabel.CENTER); 
            jLabelEstTp.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.est_tp"));
            jLabelFechaInst = new JLabel("", JLabel.CENTER);
            jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechainst"));
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelTipo,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxTipo(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelUbicacion,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxUbicacion(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelSoloDesinf,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxSoloDesinf(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCategoriaA1,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCategoriaA1(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCategoriaA2,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCategoriaA2(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCategoriaA3,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCategoriaA3(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelDesaladora,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxDesaladora(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelOtros,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxOtros(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelDesinf1,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxDesinf1(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelDesinf2,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxDesinf2(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelDesinf3,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxDesinf3(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPerioricidad,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxPerioricidad(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelOrg_Control,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxOrg_Control(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelEstTp,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxEst(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFechaInst,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldFechaInst(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldObserv(), 
                    new GridBagConstraints(1, 8, 2, 2, 0.1, 0.1,
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
                            new Insets(5, 5, 5, 5), 150, 0));
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
	        	
	        	String orden_tp = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_tp"))!=null){
	        		orden_tp=(feature.getAttribute(esquema.getAttributeByColumn("orden_tp"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelTratamientosPotabilizacionEIEL(clave, codprov, codmunic, orden_tp));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_tp);      	
			}
		}

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_tp) {
		
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

        if (orden_tp != null){
    		jTextFieldOrden.setText(orden_tp);
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
