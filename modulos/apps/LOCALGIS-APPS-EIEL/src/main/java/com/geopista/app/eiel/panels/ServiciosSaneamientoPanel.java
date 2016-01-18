/**
 * ServiciosSaneamientoPanel.java
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
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.dialogs.ServiciosSaneamientoDialog;
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

public class ServiciosSaneamientoPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
        
    public boolean okPressed = false;	
	
    private JLabel jLabelCodProv = null;
    private JLabel jLabelCodMunic = null;
	private JLabel jLabelEntSing = null;
	private JLabel jLabelNucleo = null;
	
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JComboBox jComboBoxEntidad = null;
	private JComboBox jComboBoxNucleo = null;
	
    private JPanel jPanelInformacion = null;
    
    private ComboBoxEstructuras jComboBoxPozos = null;
    private ComboBoxEstructuras jComboBoxSumid = null;
    private ComboBoxEstructuras jComboBoxAlivAcum = null;
    private ComboBoxEstructuras jComboBoxAlivSAcum = null;
    private ComboBoxEstructuras jComboBoxCalidad = null;
    private JTextField jTextFieldVivSCon = null;
    private JTextField jTextFieldVivCCon = null;
    private JTextField jTextFieldLong = null;
    private JTextField jTextFieldVivDefCon = null;    
    private JTextField jTextFieldPobResDefA = null;
    private JTextField jTextFieldPobEsDefA = null;
    private JTextField jTextFieldCaudTot = null;
    private JTextField jTextFieldCaudTrt = null;
    private JTextField jTextFieldRUrb = null;
    private JTextField jTextFieldRRust = null;
    private JTextField jTextFieldRInd = null;
    private JTextField jTextFieldObservacion = null;
    
    private JLabel jLabelPozos = null;
    private JLabel jLabelSumid = null;
    private JLabel jLabelAlivAcum = null;
    private JLabel jLabelAlivSAcum = null;
    private JLabel jLabelCalidad = null;
    private JLabel jLabelVivSCon = null;
    private JLabel jLabelVivCCon = null;
    private JLabel jLabelLong = null;
    private JLabel jLabelVivDefCon = null;    
    private JLabel jLabelPobResDefA = null;
    private JLabel jLabelPobEsDefA = null;
    private JLabel jLabelCaudTot = null;
    private JLabel jLabelCaudTrt = null;
    private JLabel jLabelRUrb = null;
    private JLabel jLabelRRust = null;
    private JLabel jLabelRInd = null;
    private JLabel jLabelObservacion = null;
    
    private JPanel jPanelRevision = null;
    
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;	
	
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	
    /**
     * This method initializes
     * 
     */
    public ServiciosSaneamientoPanel()
    {
        super();
        initialize();
    }
    
    public ServiciosSaneamientoPanel(ServiciosSaneamientoEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(ServiciosSaneamientoEIEL elemento)
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
            
            if (elemento.getPozos() != null){            	
            	jComboBoxPozos.setSelectedPatron(elemento.getPozos());
            }
            else{
            	jComboBoxPozos.setSelectedIndex(0);
            } 
            
           if (elemento.getSumideros() != null){            	
           	jComboBoxSumid.setSelectedPatron(elemento.getSumideros());
           }
           else{
        	   jComboBoxSumid.setSelectedIndex(0);
           } 
           
            if (elemento.getAlivAcumulacion()!= null){            	
            	jComboBoxAlivAcum.setSelectedPatron(elemento.getAlivAcumulacion());
            }
            else{
            	jComboBoxAlivAcum.setSelectedIndex(0);
            } 
            
            if (elemento.getAlivSinAcumulacion()!= null){            	
            	jComboBoxAlivSAcum.setSelectedPatron(elemento.getAlivSinAcumulacion());
            }
            else{
            	jComboBoxAlivSAcum.setSelectedIndex(0);
            }
            
            
            if (elemento.getCalidad() != null){            	
            	jComboBoxCalidad.setSelectedPatron(elemento.getCalidad());
            }
            else{
            	jComboBoxCalidad.setSelectedIndex(0);
            }
            
            if (elemento.getVivNoConectadas() != null){            	
            	jTextFieldVivSCon.setText(elemento.getVivNoConectadas().toString());
            }
            else{
            	jTextFieldVivSCon.setText("");
            } 
            
            if (elemento.getVivConectadas() != null){            	
            	jTextFieldVivCCon.setText(elemento.getVivConectadas().toString());
            }
            else{
            	jTextFieldVivCCon.setText("");
            } 
            
            if (elemento.getLongDeficitaria()!= null){            	
            	jTextFieldLong.setText(elemento.getLongDeficitaria().toString());
            }
            else{
            	jTextFieldLong.setText("");
            } 
            
            if (elemento.getVivDeficitarias() != null){            	
            	jTextFieldVivDefCon.setText(elemento.getVivDeficitarias().toString());
            }
            else{
            	jTextFieldVivDefCon.setText("");
            } 
            
            if (elemento.getPoblResDeficitaria() != null){
            	jTextFieldPobResDefA.setText(elemento.getPoblResDeficitaria().toString());
        	}
        	else{
        		jTextFieldPobResDefA.setText("");
        	}
            
            if (elemento.getPoblEstDeficitaria() != null){
            	jTextFieldPobEsDefA.setText(elemento.getPoblEstDeficitaria().toString());
        	}
        	else{
        		jTextFieldPobEsDefA.setText("");
        	}
            
            if (elemento.getCaudalTotal() != null){
            	jTextFieldCaudTot.setText(elemento.getCaudalTotal().toString());
        	}
        	else{
        		jTextFieldCaudTot.setText("");
        	}
            
            if (elemento.getcCaudalTratado()!= null){
            	jTextFieldCaudTrt.setText(elemento.getcCaudalTratado().toString());
        	}
        	else{
        		jTextFieldCaudTrt.setText("");
        	}
            
            if (elemento.getCaudalUrbano() != null){
            	jTextFieldRUrb.setText(elemento.getCaudalUrbano().toString());
        	}
        	else{
        		jTextFieldRUrb.setText("");
        	}
            
            if (elemento.getCaudalRustico()!= null){
            	jTextFieldRRust.setText(elemento.getCaudalRustico().toString());
        	}
        	else{
        		jTextFieldRRust.setText("");
        	}
            
            if (elemento.getCaudalIndustrial()!= null){
            	jTextFieldRInd.setText(elemento.getCaudalIndustrial().toString());
        	}
        	else{
        		jTextFieldRInd.setText("");
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

    	Object object = AppContext.getApplicationContext().getBlackboard().get("serviciossaneamiento_panel");    	
    	if (object != null && object instanceof ServiciosSaneamientoEIEL){    		
    		ServiciosSaneamientoEIEL elemento = (ServiciosSaneamientoEIEL)object;
            //Datos identificacion   
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
            
            if (elemento.getPozos() != null){            	
            	jComboBoxPozos.setSelectedPatron(elemento.getPozos());
            }
            else{
            	jComboBoxPozos.setSelectedIndex(0);
            } 
            
           if (elemento.getSumideros() != null){            	
           	jComboBoxSumid.setSelectedPatron(elemento.getSumideros());
           }
           else{
        	   jComboBoxSumid.setSelectedIndex(0);
           } 
           
            if (elemento.getAlivAcumulacion()!= null){            	
            	jComboBoxAlivAcum.setSelectedPatron(elemento.getAlivAcumulacion());
            }
            else{
            	jComboBoxAlivAcum.setSelectedIndex(0);
            } 
            
            if (elemento.getAlivSinAcumulacion()!= null){            	
            	jComboBoxAlivSAcum.setSelectedPatron(elemento.getAlivSinAcumulacion());
            }
            else{
            	jComboBoxAlivSAcum.setSelectedIndex(0);
            }
            
            
            if (elemento.getCalidad() != null){            	
            	jComboBoxCalidad.setSelectedPatron(elemento.getCalidad());
            }
            else{
            	jComboBoxCalidad.setSelectedIndex(0);
            }
            
            if (elemento.getVivNoConectadas() != null){            	
            	jTextFieldVivSCon.setText(elemento.getVivNoConectadas().toString());
            }
            else{
            	jTextFieldVivSCon.setText("");
            } 
            
            if (elemento.getVivConectadas() != null){            	
            	jTextFieldVivCCon.setText(elemento.getVivConectadas().toString());
            }
            else{
            	jTextFieldVivCCon.setText("");
            } 
            
            if (elemento.getLongDeficitaria()!= null){            	
            	jTextFieldLong.setText(elemento.getLongDeficitaria().toString());
            }
            else{
            	jTextFieldLong.setText("");
            } 
            
            if (elemento.getVivDeficitarias() != null){            	
            	jTextFieldVivDefCon.setText(elemento.getVivDeficitarias().toString());
            }
            else{
            	jTextFieldVivDefCon.setText("");
            } 
            
            if (elemento.getPoblResDeficitaria() != null){
            	jTextFieldPobResDefA.setText(elemento.getPoblResDeficitaria().toString());
        	}
        	else{
        		jTextFieldPobResDefA.setText("");
        	}
            
            if (elemento.getPoblEstDeficitaria() != null){
            	jTextFieldPobEsDefA.setText(elemento.getPoblEstDeficitaria().toString());
        	}
        	else{
        		jTextFieldPobEsDefA.setText("");
        	}
            
            if (elemento.getCaudalTotal() != null){
            	jTextFieldCaudTot.setText(elemento.getCaudalTotal().toString());
        	}
        	else{
        		jTextFieldCaudTot.setText("");
        	}
            
            if (elemento.getcCaudalTratado()!= null){
            	jTextFieldCaudTrt.setText(elemento.getcCaudalTratado().toString());
        	}
        	else{
        		jTextFieldCaudTrt.setText("");
        	}
            
            if (elemento.getCaudalUrbano() != null){
            	jTextFieldRUrb.setText(elemento.getCaudalUrbano().toString());
        	}
        	else{
        		jTextFieldRUrb.setText("");
        	}
            
            if (elemento.getCaudalRustico()!= null){
            	jTextFieldRRust.setText(elemento.getCaudalRustico().toString());
        	}
        	else{
        		jTextFieldRRust.setText("");
        	}
            
            if (elemento.getCaudalIndustrial()!= null){
            	jTextFieldRInd.setText(elemento.getCaudalIndustrial().toString());
        	}
        	else{
        		jTextFieldRInd.setText("");
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
            
        }
    }
    
    
    public ServiciosSaneamientoEIEL getSaneamientoAutonomo (ServiciosSaneamientoEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new ServiciosSaneamientoEIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            
            if (jComboBoxPozos.getSelectedPatron()!=null)
	            elemento.setPozos((String) jComboBoxPozos.getSelectedPatron());
            else elemento.setPozos("");
            if (jComboBoxSumid.getSelectedPatron()!=null)  
	            elemento.setSumideros((String) jComboBoxSumid.getSelectedPatron());
            else elemento.setSumideros("");
            if (jComboBoxAlivAcum.getSelectedPatron()!=null)  
	            elemento.setAlivAcumulacion((String) jComboBoxAlivAcum.getSelectedPatron());
            else elemento.setAlivAcumulacion("");
            if (jComboBoxAlivSAcum.getSelectedPatron()!=null) 
	            elemento.setAlivSinAcumulacion((String) jComboBoxAlivSAcum.getSelectedPatron());
            else elemento.setAlivSinAcumulacion("");
            if (jComboBoxCalidad.getSelectedPatron()!=null) 
	            elemento.setCalidad((String) jComboBoxCalidad.getSelectedPatron());
            else elemento.setCalidad("");
            
            if (jTextFieldVivSCon.getText()!=null && !jTextFieldVivSCon.getText().equals("")){
            	elemento.setVivNoConectadas(new Integer(jTextFieldVivSCon.getText()));
            }
            else{
            	elemento.setVivNoConectadas(null);
            }
            
            if (jTextFieldVivCCon.getText()!=null && !jTextFieldVivCCon.getText().equals("")){
            	elemento.setVivConectadas(new Integer(jTextFieldVivCCon.getText()));
            }
            else{
            	elemento.setVivConectadas(null);
            }
            
            if (jTextFieldLong.getText()!=null && !jTextFieldLong.getText().equals("")){
            	elemento.setLongDeficitaria(new Integer(jTextFieldLong.getText()));
            }
            else{
            	elemento.setLongDeficitaria(null);
            }
            
            if (jTextFieldVivDefCon.getText()!=null && !jTextFieldVivDefCon.getText().equals("")){
            	elemento.setVivDeficitarias(new Integer(jTextFieldVivDefCon.getText()));
            }
            else{
            	elemento.setVivDeficitarias(null);
            }
            
            if (jTextFieldPobResDefA.getText()!=null && !jTextFieldPobResDefA.getText().equals("")){
            	elemento.setPoblResDeficitaria(new Integer(jTextFieldPobResDefA.getText()));
            }
            else{
            	elemento.setPoblResDeficitaria(null);
            }
            if (jTextFieldPobEsDefA.getText()!=null && !jTextFieldPobEsDefA.getText().equals("")){
            	elemento.setPoblEstDeficitaria(new Integer(jTextFieldPobEsDefA.getText()));
            }
            else{
            	elemento.setPoblEstDeficitaria(null);
            }
            if (jTextFieldCaudTot.getText()!=null && !jTextFieldCaudTot.getText().equals("")){
            	elemento.setCaudalTotal(new Integer(jTextFieldCaudTot.getText()));
            }
            else{
            	elemento.setCaudalTotal(null);
            }
            if (jTextFieldCaudTrt.getText()!=null && !jTextFieldCaudTrt.getText().equals("")){
            	elemento.setCaudalTratado(new Integer(jTextFieldCaudTrt.getText()));
            }
            else{
            	elemento.setCaudalTratado(null);
            }
            if (jTextFieldRUrb.getText()!=null && !jTextFieldRUrb.getText().equals("")){
            	elemento.setCaudalUrbano(new Integer(jTextFieldRUrb.getText()));
            }
            else{
            	elemento.setCaudalUrbano(null);
            }
            if (jTextFieldRRust.getText()!=null && !jTextFieldRRust.getText().equals("")){
            	elemento.setCaudalRustico(new Integer(jTextFieldRRust.getText()));
            }
            else{
            	elemento.setCaudalRustico(null);
            }
            if (jTextFieldRInd.getText()!=null && !jTextFieldRInd.getText().equals("")){
            	elemento.setCaudalIndustrial(new Integer(jTextFieldRInd.getText()));
            }
            else{
            	elemento.setCaudalIndustrial(null);
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
            
        }
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.serviciossaneamiento.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(ServiciosSaneamientoDialog.DIM_X,ServiciosSaneamientoDialog.DIM_Y);
        
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
    
    private ComboBoxEstructuras getJComboBoxPozos()
    { 
        if (jComboBoxPozos == null)
        {
            Estructuras.cargarEstructura("eiel_pozos_registro");
            jComboBoxPozos = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxPozos.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxPozos;        
    }
    
    private ComboBoxEstructuras getJComboBoxSumid()
    { 
        if (jComboBoxSumid == null)
        {
            Estructuras.cargarEstructura("eiel_sumideros");
            jComboBoxSumid = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxSumid.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxSumid;        
    }
    
    
    private ComboBoxEstructuras getJComboBoxAlivAcum()
    { 
        if (jComboBoxAlivAcum == null)
        {
            Estructuras.cargarEstructura("eiel_aliv_c_acum");
            jComboBoxAlivAcum = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAlivAcum.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAlivAcum;        
    }
    
    private ComboBoxEstructuras getJComboBoxAlivSAcum()
    { 
        if (jComboBoxAlivSAcum == null)
        {
            Estructuras.cargarEstructura("eiel_aliv_s_acum");
            jComboBoxAlivSAcum = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAlivSAcum.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAlivSAcum;        
    }
    
    private ComboBoxEstructuras getJComboBoxCalidad()
    { 
        if (jComboBoxCalidad == null)
        {
            Estructuras.cargarEstructura("eiel_Calidad del servicio");//eiel_Servicio de limpieza de calles
            jComboBoxCalidad = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCalidad.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCalidad;        
    }
    
    private JTextField getjTextFieldVivSCon()
    {
    	if (jTextFieldVivSCon == null)
    	{
    		jTextFieldVivSCon = new TextField(10);
    		jTextFieldVivSCon.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivSCon, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivSCon;
    }
    
    private JTextField getjTextFieldVivCCon()
    {
    	if (jTextFieldVivCCon == null)
    	{
    		jTextFieldVivCCon = new TextField(10);
    		jTextFieldVivCCon.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivCCon, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivCCon;
    }
    
    private JTextField getjTextFieldLong()
    {
    	if (jTextFieldLong == null)
    	{
    		jTextFieldLong = new TextField(10);
    		jTextFieldLong.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLong, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLong;
    }
    
    private JTextField getjTextFieldVivDefCon()
    {
    	if (jTextFieldVivDefCon == null)
    	{
    		jTextFieldVivDefCon = new TextField(10);
    		jTextFieldVivDefCon.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDefCon, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDefCon;
    }
    
    private JTextField getjTextFieldPobResDefA()
    {
    	if (jTextFieldPobResDefA == null)
    	{
    		jTextFieldPobResDefA = new TextField(10);
    		jTextFieldPobResDefA.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResDefA, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResDefA;
    }
    
    private JTextField getjTextFieldPobEsDefA()
    {
    	if (jTextFieldPobEsDefA == null)
    	{
    		jTextFieldPobEsDefA = new TextField(10);
    		jTextFieldPobEsDefA.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEsDefA, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEsDefA;
    }
    
    private JTextField getjTextFieldCaudTot()
    {
    	if (jTextFieldCaudTot == null)
    	{
    		jTextFieldCaudTot = new TextField(10);
    		jTextFieldCaudTot.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCaudTot, 10, aplicacion.getMainFrame());
    			}
    		});
    		jTextFieldCaudTot.setEnabled(false);
    		jTextFieldCaudTot.setEditable(false);
    	}
    	return jTextFieldCaudTot;
    }
    
    private JTextField getjTextFieldCaudTrt()
    {
    	if (jTextFieldCaudTrt == null)
    	{
    		jTextFieldCaudTrt = new TextField(10);
    		jTextFieldCaudTrt.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCaudTrt, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCaudTrt;
    }
    
    private JTextField getjTextFieldRUrb()
    {
    	if (jTextFieldRUrb == null)
    	{
    		jTextFieldRUrb = new TextField(10);
    		jTextFieldRUrb.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldRUrb, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldRUrb;
    }
    
    private JTextField getjTextFieldRRust()
    {
    	if (jTextFieldRRust == null)
    	{
    		jTextFieldRRust = new TextField(10);
    		jTextFieldRRust.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldRRust, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldRRust;
    }
    
    private JTextField getjTextFieldRInd()
    {
    	if (jTextFieldRInd == null)
    	{
    		jTextFieldRInd = new TextField(10);
    		jTextFieldRInd.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldRInd, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldRInd;
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
    
    
    public ServiciosSaneamientoPanel(GridBagLayout layout)
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
        	
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelEntSing  = new JLabel("", JLabel.CENTER);
            jLabelEntSing.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.entsing")));
            
            jLabelNucleo   = new JLabel("", JLabel.CENTER);
            jLabelNucleo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nucleo")));
            
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
            
            jPanelIdentificacion.add(jLabelEntSing, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentificacion.add(getJComboBoxEntidad(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentificacion.add(jLabelNucleo, 
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
    
    private JPanel getJPanelDatosInformacion()
    {
        if (jPanelInformacion == null)
        {   
        	jPanelInformacion  = new JPanel(new GridBagLayout());
        	jPanelInformacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.informacion"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
        	jLabelPozos = new JLabel("", JLabel.CENTER); 
        	jLabelPozos.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pozos")); 
            
        	jLabelSumid = new JLabel("", JLabel.CENTER);
        	jLabelSumid.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sumid"));
        	
        	jLabelAlivAcum = new JLabel("", JLabel.CENTER); 
        	jLabelAlivAcum.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.alivAcum")); 

        	jLabelAlivSAcum = new JLabel("", JLabel.CENTER);
        	jLabelAlivSAcum.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.alivSAcum"));
            
        	jLabelCalidad = new JLabel("", JLabel.CENTER);
        	jLabelCalidad.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.calidad"));
            
        	jLabelVivSCon = new JLabel("", JLabel.CENTER);
        	jLabelVivSCon.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.vivSCon"));
            
        	jLabelVivCCon = new JLabel("", JLabel.CENTER);
        	jLabelVivCCon.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.vivCCon"));
            
        	jLabelLong = new JLabel("", JLabel.CENTER);
        	jLabelLong.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.long_def"));
            
        	jLabelVivDefCon = new JLabel("", JLabel.CENTER);
        	jLabelVivDefCon.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.vivDefCon"));
            
        	jLabelPobResDefA = new JLabel("", JLabel.CENTER);
        	jLabelPobResDefA.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobResDefA"));
        	
        	jLabelPobEsDefA = new JLabel("", JLabel.CENTER);
        	jLabelPobEsDefA.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobEsDefA"));
        	
        	jLabelCaudTot = new JLabel("", JLabel.CENTER);
        	jLabelCaudTot.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.caudTot"));
            
        	jLabelCaudTrt = new JLabel("", JLabel.CENTER);
        	jLabelCaudTrt.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.caudTrt"));
        	
        	jLabelRUrb = new JLabel("", JLabel.CENTER);
        	jLabelRUrb.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rUrb"));
        	
        	jLabelRRust = new JLabel("", JLabel.CENTER);
        	jLabelRRust.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rRust"));
        	
        	jLabelRInd = new JLabel("", JLabel.CENTER);
        	jLabelRInd.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rInd"));
        	
        	jLabelObservacion = new JLabel("", JLabel.CENTER);
        	jLabelObservacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
        	
        	jPanelInformacion.add(jLabelPozos,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxPozos(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelSumid,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxSumid(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelAlivAcum,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxAlivAcum(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelAlivSAcum,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxAlivSAcum(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCalidad,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxCalidad(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelVivSCon,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldVivSCon(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelVivCCon,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldVivCCon(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelLong,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldLong(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelVivDefCon,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldVivDefCon(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobResDefA,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldPobResDefA(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobEsDefA,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldPobEsDefA(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCaudTot,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldCaudTot(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCaudTrt,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldCaudTrt(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelRUrb,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldRUrb(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelRRust,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldRRust(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelRInd,
                    new GridBagConstraints(2, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldRInd(), 
                    new GridBagConstraints(3, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObservacion,
                    new GridBagConstraints(0, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldObservacion(), 
                    new GridBagConstraints(1, 8, 2, 1, 0.1, 0.1,
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
                            new Insets(5, 5, 5, 5), 100, 0));
            
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
	        	loadData(operations.getPanelServiciosSaneamientoEIEL(codprov, codmunic, entidad, nucleo));

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
