/**
 * CasaConsistorialPanel.java
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.protocol.inventario.Inventario;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.UsosCasasConsistoriales;
import com.geopista.app.eiel.dialogs.CasaConsistorialDialog;
import com.geopista.app.eiel.models.UsosCasasConsistorialesEIELTableModel;
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
public class CasaConsistorialPanel extends InventarioPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    private JScrollPane jScroll = null;
    
    public boolean okPressed = false;	
	
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
	private JLabel jLabelTitular = null;
	private JLabel jLabelTenencia = null;
	private JLabel jLabelSupCubierta = null;
	private JLabel jLabelSupAire = null;
	private JLabel jLabelSupSolar = null;
	private JLabel jLabelEstado = null;
	private JLabel jLabelFechaInstal = null;
	private JLabel jLabelObserv = null;
	private JTextField jTextFieldNombre = null;
	private ComboBoxEstructuras jComboBoxTipo = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxTenencia = null;
	private JTextField jTextFieldSupCubierta = null;
	private JTextField jTextFieldSupAire = null;
	private JTextField jTextFieldSupSolar = null;
	private ComboBoxEstructuras jComboBoxEst = null;
	private JTextField jTextFieldObserv = null;
	private DateField jTextFieldFechaInstal = null;
	private JPanel jPanelRevision = null;
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstadoRevision = null;
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldOrden = null;
	private JLabel jLabelObraEjec = null;
	private ComboBoxEstructuras jComboBoxObraEjec = null;
	private JLabel jLabelAccesoRuedas = null;
	private ComboBoxEstructuras jComboBoxAccesoRuedas = null;
	private JPanel jPanelInventario = null;
	
	private JPanel jPanelUsosCasasConsistoriales = null;
	private JPanel jPanelDatosUsos = null;
	private JLabel jLabelUso = null;
	private JLabel jLabelSuperficieUso = null;
	private ComboBoxEstructuras jComboBoxUso = null;
	private TextField jTextFieldSuperficieUso = null;
	private JLabel jLabelCodigoOrden = null;
	private JLabel jLabelFechaUso = null;
	private JLabel jLabelObservacionesUso = null;
	private JLabel jLabelInst_P = null;
	private JTextField jTextFieldCodigoOrden = null;
	private DateField jTextFieldFechaUso = null;
	private TextField jTextFieldObservacionesUso = null;
	private ComboBoxEstructuras jComboBoxInst_P = null;
	private JPanel jPanelBotonera = null;
	private JButton jButtonAniadir = null;
	private JButton jButtonEliminar = null;
	private JButton jButtonLimpiar = null;
	private JPanel jPanelListaUsos = null;
	private JScrollPane jScrollPaneListaUsos = null;
	private JTable jTableListaUsos = null;
	private UsosCasasConsistorialesEIELTableModel tableListaUsosModel = null;
	private CasasConsistorialesEIEL casaConsistorial = null;
	private JPanel jPanelDatosInformacionComun = null;
	
	Object selectedItemUsoCasaConsistorial;
	
    /**
     * This method initializes
     * 
     */
    public CasaConsistorialPanel()
    {
        super();
        initialize();
    }
    
    public CasaConsistorialPanel(CasasConsistorialesEIEL casa)
    {
        super();

        this.casaConsistorial = casa;
        initialize();
        loadData (casa);
    }
    
    public void loadData(CasasConsistorialesEIEL elemento)
    {
        if (elemento!=null)
        {
        	this.casaConsistorial = elemento;
        	
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
            
            if (elemento.getCodOrden() != null){
        		jTextFieldOrden.setText(elemento.getCodOrden());
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
            
            if (elemento.getTenencia() != null){
            	jComboBoxTenencia.setSelectedPatron(elemento.getTenencia());
        	}
        	else{
        		jComboBoxTenencia.setSelectedIndex(0);
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
            	jComboBoxEst.setSelectedPatron(elemento.getEstado().toString());
        	}
        	else{
        		jComboBoxEst.setSelectedIndex(0);
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
            if (elemento.getObra_ejec() != null){
            	jComboBoxObraEjec.setSelectedPatron(elemento.getObra_ejec().toString());
        	}
        	else{
        		jComboBoxObraEjec.setSelectedPatron("");
        	}
            if (elemento.getAcceso_s_ruedas() != null){
            	jComboBoxAccesoRuedas.setSelectedPatron(elemento.getAcceso_s_ruedas().toString());
        	}
        	else{
        		jComboBoxAccesoRuedas.setSelectedPatron("");
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
            
            ((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).setData(elemento!=null?elemento.getListaUsos():new ArrayList());
            
        } else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio

        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.CASASCONSISTORIALES_CLAVE);
        	        	       	
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
    	Object object = AppContext.getApplicationContext().getBlackboard().get("casasconsistoriales_panel");    	
    	if (object != null && object instanceof CasasConsistorialesEIEL){    		
    		CasasConsistorialesEIEL elemento = (CasasConsistorialesEIEL)object;
    		
        	this.casaConsistorial = elemento;
        	
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
            
            if (elemento.getCodOrden() != null){
        		jTextFieldOrden.setText(elemento.getCodOrden());
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
            
            if (elemento.getTenencia() != null){
            	jComboBoxTenencia.setSelectedPatron(elemento.getTenencia());
        	}
        	else{
        		jComboBoxTenencia.setSelectedIndex(0);
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
            	jComboBoxEstado.setSelectedPatron(elemento.getEstado());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
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
			
            ((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).setData(elemento!=null?elemento.getListaUsos():new ArrayList());
            
        }
    }
    
    
    public CasasConsistorialesEIEL getCasaConsistorial (CasasConsistorialesEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new CasasConsistorialesEIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));

            elemento.setClave(jTextFieldClave.getText());
            elemento.setCodOrden(jTextFieldOrden.getText());
            
            if (jTextFieldNombre.getText()!=null){
            	elemento.setNombre(jTextFieldNombre.getText());
            }
            if (jComboBoxTipo.getSelectedPatron()!=null)            
            	elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
            else elemento.setTipo("");
            if (jComboBoxTitular.getSelectedPatron()!=null)
            	elemento.setTitular((String) jComboBoxTitular.getSelectedPatron());
            else elemento.setTitular("");
            if (jComboBoxTenencia.getSelectedPatron()!=null)
            	elemento.setTenencia((String) jComboBoxTenencia.getSelectedPatron());
            else elemento.setTenencia("");
            
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
            	elemento.setAcceso_s_ruedas(jComboBoxAccesoRuedas.getSelectedPatron());
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
            
            if (elemento.getObra_ejec() != null){
            	jComboBoxObraEjec.setSelectedPatron(elemento.getObra_ejec().toString());
        	}
        	else{
        		jComboBoxObraEjec.setSelectedIndex(0);
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
            
            
            elemento.setListaUsos(((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).getData());
        }
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.casasconsistoriales.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(CasaConsistorialDialog.DIM_X,CasaConsistorialDialog.DIM_Y);
        
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 15));
        
		if (jScroll == null) {

			jScroll = new JScrollPane(getJPanelDatosInformacion());
			jScroll = new JScrollPane();
			jScroll.setViewportView(jPanelInformacion);
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
                new Insets(0, 5, 0, 5), 0, 10));
        
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
    
    public JTextField getJTextFieldClave()
    {
        if (jTextFieldClave == null)
        {
            jTextFieldClave = new TextField(2);
            jTextFieldClave.setEditable(false);
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
	public JComboBox getJComboBoxProvincia() {
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

	public JComboBox getJComboBoxMunicipio() {
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
            Estructuras.cargarEstructura("eiel_Tipo de Casa Consistorial");
            jComboBoxTipo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTipo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTipo;        
    }
    
    private ComboBoxEstructuras getJComboBoxTitular()
    { 
        if (jComboBoxTitular == null)
        {
            Estructuras.cargarEstructura("eiel_Titularidad Casa Consistorial");
            jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTitular;        
    }
    
    private ComboBoxEstructuras getJComboBoxTenencia()
    { 
        if (jComboBoxTenencia == null)
        {
            Estructuras.cargarEstructura("eiel_Forma de tenencia CC");
            jComboBoxTenencia = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTenencia.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTenencia;        
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
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEst.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEst;        
    }
    
    private ComboBoxEstructuras getJComboBoxObraEjec()
    { 
        if (jComboBoxObraEjec == null)
        {
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            jComboBoxObraEjec = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxObraEjec.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxObraEjec;        
    }
    
    private ComboBoxEstructuras getJComboBoxAccesoRuedas()
    { 
        if (jComboBoxAccesoRuedas == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            jComboBoxAccesoRuedas = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAccesoRuedas.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAccesoRuedas;        
    }
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv == null)
    	{
    		jTextFieldObserv  = new TextField();
    		
    	}
    	return jTextFieldObserv;
    }
    
    private DateField getJTextFieldFechaInstal()
    {
    	if (jTextFieldFechaInstal == null)
    	{
    		jTextFieldFechaInstal  = new DateField((java.util.Date) null, 0);
    		jTextFieldFechaInstal.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaInstal.setEditable(true);
    		
    	}
    	return jTextFieldFechaInstal;
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
    
    
    public CasaConsistorialPanel(GridBagLayout layout)
    {
        super(layout);
        initialize();
    }
    
    
    
    public String validateInput()
    {
    	/*CONSTRAINT "eiel_t_cc_pkey" PRIMARY KEY ("clave", "codprov", "codmunic", "codentidad", "codpoblamiento", "orden_cc", "revision_actual"),
    	CONSTRAINT "acceso_s_ruedaschk" CHECK (((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text)),
    	CONSTRAINT "tenenciachk" CHECK (((((tenencia)::text = 'AL'::text) OR ((tenencia)::text = 'CE'::text)) OR ((tenencia)::text = 'MU'::text)) OR ((tenencia)::text = 'OT'::text)),
    	CONSTRAINT "estadochk" CHECK (((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text)),
    	CONSTRAINT "tipochk" CHECK ((((((tipo)::text = 'AE'::text) OR ((tipo)::text = 'AY'::text)) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'UA'::text)) OR ((tipo)::text = 'VM'::text)),
    	CONSTRAINT "clavechk" CHECK ((clave)::text = 'CC'::text),
    	CONSTRAINT "titularchk" CHECK (((((((titular)::text = 'CA'::text) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OP'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text))
		*/
    	
    	String mensajeError="";
    	if (jComboBoxAccesoRuedas.getSelectedPatron()==null)
    		mensajeError+="- Debe rellenar el campo Acceso Silla de Ruedas\n";
    	if (jComboBoxTitular.getSelectedPatron()==null)
    		mensajeError+="- Debe rellenar el campo Titular\n";
    	if (jComboBoxEst.getSelectedPatron()==null)
    		mensajeError+="- Debe rellenar el campo Estado\n";
    	if (jComboBoxTipo.getSelectedPatron()==null)
    		mensajeError+="- Debe rellenar el campo Tipo\n";
    	if (jComboBoxTenencia.getSelectedPatron()==null)
    		mensajeError+="- Debe rellenar el campo Tenencia\n";
    	
    	if (mensajeError.equals(""))
    		return null; 
    	else
    		return mensajeError;
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

    private JPanel getJPanelDatosInformacion()
    {
    	if (jPanelInformacion == null)
    	{   
    		jPanelInformacion  = new JPanel(new GridBagLayout());
 

    		jPanelInformacion.add(getJPanelDatosInformacionComun(), 
    				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(0, 5, 0, 5), 0, 0));

    		jPanelInformacion.add(getJPanelUsosCasasConsistoriales(), 
    				new GridBagConstraints(0, 1, 1, 1, 0.2, 0.2,
    						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    						new Insets(0, 5, 0, 5), 0, 0));
    		

    	}
    	return jPanelInformacion;
    }

    public JPanel getJPanelUsosCasasConsistoriales(){

    	if (jPanelUsosCasasConsistoriales == null){

    		jPanelUsosCasasConsistoriales = new JPanel(new GridBagLayout());
    		jPanelUsosCasasConsistoriales.setBorder(BorderFactory.createTitledBorder
    				(null, I18N.get("LocalGISEIEL", "localgiseiel.panels.usosCC"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

    		jPanelUsosCasasConsistoriales.add(getJPanelDatosUsos(), 
    				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(0, 5, 0, 5), 0, 0));

    		jPanelUsosCasasConsistoriales.add(getJPanelBotoneraUsos(), 
    				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(0, 5, 0, 5), 0, 0));

    		jPanelUsosCasasConsistoriales.add(getJPanelListaUsos(), 
    				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.8,
    						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    						new Insets(0, 5, 0, 5), 0, 0));    		
    	}

  return jPanelUsosCasasConsistoriales;
    }

    private JPanel getJPanelDatosUsos(){

    	if (jPanelDatosUsos == null){

    		jPanelDatosUsos = new JPanel(new GridBagLayout());
    		jPanelDatosUsos.setBorder(BorderFactory.createTitledBorder
    				(null, I18N.get("LocalGISEIEL", "localgiseiel.panels.info"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

    		jLabelUso = new JLabel("", JLabel.CENTER);
    		jLabelUso.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.uso")));

    		jLabelSuperficieUso = new JLabel("", JLabel.CENTER);
    		jLabelSuperficieUso.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.superficieuso")));

    		jLabelCodigoOrden = new JLabel("", JLabel.CENTER);
    		jLabelCodigoOrden.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codigoorden"));

    		jLabelFechaUso = new JLabel("", JLabel.CENTER);
    		jLabelFechaUso.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechauso"));

    		jLabelObservacionesUso = new JLabel("", JLabel.CENTER);
    		jLabelObservacionesUso.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observacionesuso"));

    		jLabelInst_P = new JLabel("", JLabel.CENTER);
    		jLabelInst_P.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.inst_p"));


    		jPanelDatosUsos.add(jLabelUso,
    				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 5, 0));

    		jPanelDatosUsos.add(getJComboBoxUso(), 
    				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosUsos.add(jLabelSuperficieUso,
    				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 5, 0));

    		jPanelDatosUsos.add(getJTextFieldSuperficieUso(), 
    				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosUsos.add(jLabelCodigoOrden,
    				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 5, 0));

    		jPanelDatosUsos.add(getJTextFieldCodigoOrden(), 
    				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosUsos.add(jLabelFechaUso,
    				new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 5, 0));

    		jPanelDatosUsos.add(getJTextFieldFechaUso(), 
    				new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosUsos.add(jLabelObservacionesUso,
    				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 5, 0));

    		jPanelDatosUsos.add(getJTextFieldObservacionesUso(), 
    				new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosUsos.add(jLabelInst_P,
    				new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 5, 0));

    		jPanelDatosUsos.add(getJComboBoxInst_P(), 
    				new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));
    		
    	}
    	return jPanelDatosUsos;
    }

    private JPanel getJPanelBotoneraUsos(){

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

    private JPanel getJPanelListaUsos(){

    	if (jPanelListaUsos == null){

    		jPanelListaUsos = new JPanel();
    		jPanelListaUsos.setLayout(new GridBagLayout());

    		jPanelListaUsos.setBorder(BorderFactory.createTitledBorder
    				(null, I18N.get("LocalGISEIEL", "localgiseiel.panels.listausos"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 


    		jPanelListaUsos.add(getJScrollPaneListaUsos(), 
    				new GridBagConstraints(0,0,1,1,0.2, 0.2,GridBagConstraints.NORTH,
    						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    	}
    	return jPanelListaUsos;
    }

    private JTextField getJTextFieldObservacionesUso()
    {
    	if (jTextFieldObservacionesUso == null)
    	{
    		jTextFieldObservacionesUso  = new TextField(50);
    		jTextFieldObservacionesUso.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObservacionesUso, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObservacionesUso;
    }
    
    private ComboBoxEstructuras getJComboBoxInst_P()
    { 
        if (jComboBoxInst_P == null)
        {
            Estructuras.cargarEstructura("eiel_Titularidad Casa Consistorial");
            jComboBoxInst_P = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxInst_P.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxInst_P;        
    }

    private DateField getJTextFieldFechaUso()
    {
    	if (jTextFieldFechaUso == null)
    	{
    		jTextFieldFechaUso  = new DateField((java.util.Date) null, 0);
    		jTextFieldFechaUso.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaUso.setEditable(true);

    	}
    	return jTextFieldFechaUso;
    }

    private JTextField getJTextFieldCodigoOrden(){

    	if (jTextFieldCodigoOrden == null){

    		jTextFieldCodigoOrden = new JTextField(2);
    		jTextFieldCodigoOrden.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCodigoOrden, 3, aplicacion.getMainFrame());
    			}
    		});

    	}
    	return jTextFieldCodigoOrden;
    }

    private JTextField getJTextFieldSuperficieUso()
    {
    	if (jTextFieldSuperficieUso == null)
    	{
    		jTextFieldSuperficieUso  = new TextField(10);
    		jTextFieldSuperficieUso.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldSuperficieUso, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldSuperficieUso;
    }

    private ComboBoxEstructuras getJComboBoxUso()
    { 
    	if (jComboBoxUso == null)
    	{
    		Estructuras.cargarEstructura("eiel_Usos Casas Consistoriales");
    		jComboBoxUso = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);

    		jComboBoxUso.setPreferredSize(new Dimension(100, 20));
    	}
    	return jComboBoxUso;        
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
    				eliminarUso();
    			}
    		});
    	}
    	return jButtonEliminar;
    }

    public JButton getJButtonAniadir(){

    	if (jButtonAniadir == null){

    		jButtonAniadir = new JButton();
    		jButtonAniadir.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttonsave"));
    		jButtonAniadir.addActionListener(new java.awt.event.ActionListener() {
    			public void actionPerformed(java.awt.event.ActionEvent evt) {
    				annadirUso();
    			}
    		});
    	}

    	return jButtonAniadir;

    }

    public JScrollPane getJScrollPaneListaUsos(){

    	if (jScrollPaneListaUsos == null){

    		jScrollPaneListaUsos = new JScrollPane();
    		jScrollPaneListaUsos.setViewportView(getJTableListaUsos());
    		jScrollPaneListaUsos.setSize(10,10);

    	}
    	return jScrollPaneListaUsos;
    }

    public JTable getJTableListaUsos()
    {
    	if (jTableListaUsos  == null)
    	{
    		jTableListaUsos   = new JTable();

    		tableListaUsosModel  = new UsosCasasConsistorialesEIELTableModel();

    		TableSorted tblSorted= new TableSorted((TableModel)tableListaUsosModel);
    		tblSorted.setTableHeader(jTableListaUsos.getTableHeader());
    		jTableListaUsos.setModel(tblSorted);
    		jTableListaUsos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		jTableListaUsos.setCellSelectionEnabled(false);
    		jTableListaUsos.setColumnSelectionAllowed(false);
    		jTableListaUsos.setRowSelectionAllowed(true);
    		jTableListaUsos.getTableHeader().setReorderingAllowed(false);

    		jTableListaUsos.setPreferredScrollableViewportSize(new Dimension(100,100));
    		jTableListaUsos.addMouseListener(new java.awt.event.MouseAdapter() {
    			public void mouseReleased(java.awt.event.MouseEvent evt) {
    				loadSelectedItem();
    			}
    		});

    		((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).setData(casaConsistorial!=null?casaConsistorial.getListaUsos():new ArrayList());

    	}
    	return jTableListaUsos;
    }
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosInformacionComun()
    {
    	if (jPanelDatosInformacionComun == null)
    	{   
    		jPanelDatosInformacionComun  = new JPanel(new GridBagLayout());

    		jLabelNombre = new JLabel("", JLabel.CENTER); 
    		jLabelNombre.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nombre_cc")); 

    		jLabelTipo = new JLabel("", JLabel.CENTER); 
    		jLabelTipo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo_cc")); 

    		jLabelTitular = new JLabel("", JLabel.CENTER); 
    		jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular_cc")); 

    		jLabelTenencia = new JLabel("", JLabel.CENTER);
    		jLabelTenencia.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tenencia_cc"));

    		jLabelSupCubierta = new JLabel("", JLabel.CENTER);
    		jLabelSupCubierta.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_cubierta_cc"));

    		jLabelSupAire = new JLabel("", JLabel.CENTER);
    		jLabelSupAire.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_aire_cc"));

    		jLabelSupSolar = new JLabel("", JLabel.CENTER);
    		jLabelSupSolar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.s_solar_cc"));

    		jLabelEstado = new JLabel("", JLabel.CENTER);
    		jLabelEstado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado_cc"));

    		 jLabelObraEjec = new JLabel("", JLabel.CENTER); 
             jLabelObraEjec.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.obraejec")); 
             
             jLabelAccesoRuedas = new JLabel("", JLabel.CENTER); 
             jLabelAccesoRuedas.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.accesoruedas")); 
             
    		jLabelFechaInstal = new JLabel("", JLabel.CENTER);
    		jLabelFechaInstal.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha_instal"));

    		jLabelObserv = new JLabel("", JLabel.CENTER);
    		jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));


    		jPanelDatosInformacionComun.add(jLabelNombre,
    				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJTextFieldNombre(), 
    				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelTipo,
    				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJComboBoxTipo(), 
    				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 30, 0));

    		jPanelDatosInformacionComun.add(jLabelTitular,
    				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJComboBoxTitular(), 
    				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelTenencia,
    				new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJComboBoxTenencia(), 
    				new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelSupCubierta,
    				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJTextFieldSupCubierta(), 
    				new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelSupAire,
    				new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJTextFieldSupAire(), 
    				new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelSupSolar,
    				new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJTextFieldSupSolar(), 
    				new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelEstado,
    				new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJComboBoxEst(), 
    				new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));
    		
    		jPanelDatosInformacionComun.add(jLabelObraEjec,
    				new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJComboBoxObraEjec(), 
    				new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelAccesoRuedas,
    				new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJComboBoxAccesoRuedas(), 
    				new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelFechaInstal,
    				new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJTextFieldFechaInstal(), 
    				new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(jLabelObserv,
    				new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
    						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));

    		jPanelDatosInformacionComun.add(getJTextFieldObserv(), 
    				new GridBagConstraints(1, 6, 2, 1, 0.1, 0.1,
    						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
    						new Insets(5, 5, 5, 5), 0, 0));
    		

    	}
    	return jPanelDatosInformacionComun;
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
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0) &&
        (jComboBoxAccesoRuedas!=null && jComboBoxAccesoRuedas.getSelectedItem()!=null);
    }
    
    private void reset(){
    	
    	jComboBoxUso.setSelectedIndex(0);    	
    	jTextFieldObservacionesUso.setText("");
    	jComboBoxInst_P.setSelectedIndex(0);
    	jTextFieldFechaUso.setDate(null);
    	jTextFieldCodigoOrden.setText("");
    	jTextFieldSuperficieUso.setText("");
    	
    	selectedItemUsoCasaConsistorial=null;

    }
    
    private void eliminarUso(){
    	
    	int selectedRow = getJTableListaUsos().getSelectedRow();
    	if (selectedRow!=-1){
	    	Object selectedItem = ((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).getValueAt(selectedRow);
	    	
	    	if (selectedItem != null && selectedItem instanceof UsosCasasConsistoriales){
	    		
	    		this.casaConsistorial.getListaUsos().remove(selectedItem);
	        	((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).setData(casaConsistorial.getListaUsos());
	        	((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).fireTableDataChanged();
	        	jTableListaUsos.updateUI();
	    	}  
    	}
    }
    
    private void annadirUso(){
    	
    	//Primero borro el uso seleccionado para que no se duplique
    	eliminarUso();
    	
    	UsosCasasConsistoriales uso=getUsoCasaConsistorial();
    	if (uso!=null) {
    		if (this.casaConsistorial == null){
    			casaConsistorial = new CasasConsistorialesEIEL();
    		}
    		this.casaConsistorial.getListaUsos().add(uso);
    	}
    	((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).setData(casaConsistorial.getListaUsos());
    	((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).fireTableDataChanged();
    	getJTableListaUsos().updateUI();
    	
    }
    
    private UsosCasasConsistoriales getUsoCasaConsistorial(){
    	
    	UsosCasasConsistoriales uso = new UsosCasasConsistoriales();
    	
    	uso.setCodigoOrdenUso(getJTextFieldCodigoOrden().getText());
    	uso.setUso(((ComboBoxEstructuras)getJComboBoxUso()).getSelectedPatron());
    	// Quitamos esta comprobacion ya que no es necesaria, Tipo de uso siempre tiene valores: && (getJComboBoxUso().getSelectedIndex()!=0) 
    	if  (datosMinimosYCorrectos() && (getJTextFieldSuperficieUso().getText() != null && !getJTextFieldSuperficieUso().getText().equals(""))){
    			uso.setSuperficieUso(new Integer(getJTextFieldSuperficieUso().getText()));
    	}
    	else {	// algun campo obligatorio, NO esta rellenado
    		JOptionPane.showMessageDialog(this,I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
    		return null;
    	}
    	uso.setObservacionesUso(getJTextFieldObservacionesUso().getText());
    	if (jComboBoxInst_P.getSelectedPatron()!=null)
        	uso.setInstPertenece((String) jComboBoxInst_P.getSelectedPatron());
        else uso.setInstPertenece("");
    	
    	
    	 if (jTextFieldFechaUso.getDate()!=null && !jTextFieldFechaUso.getDate().toString().equals("")){
         	
         	java.sql.Date sqlDate = new java.sql.Date(
         			getJTextFieldFechaUso().getDate().getYear(),
         			getJTextFieldFechaUso().getDate().getMonth(),
         			getJTextFieldFechaUso().getDate().getDate()
         		);
         	uso.setFechaUso(sqlDate);
         }  
         else{
         	uso.setFechaUso(null);
         }
    	
    	return uso;
    }
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaUsos().getSelectedRow();
    	Object selectedItem = ((UsosCasasConsistorialesEIELTableModel)((TableSorted)getJTableListaUsos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof UsosCasasConsistoriales){
    		
    		loadUsosCasasConsistoriales((UsosCasasConsistoriales)selectedItem);
    		selectedItemUsoCasaConsistorial=selectedItem;
    	}    	
    }
    
    private void loadUsosCasasConsistoriales(UsosCasasConsistoriales usosCasasConsistoriales){
    	
        
        if (usosCasasConsistoriales.getUso() != null){
        	jComboBoxUso.setSelectedPatron(usosCasasConsistoriales.getUso());
        }
        else{
        	jComboBoxUso.setSelectedIndex(0);
        }
        
        if (usosCasasConsistoriales.getSuperficieUso() != null){
    		jTextFieldSuperficieUso.setText(usosCasasConsistoriales.getSuperficieUso().toString());
    	}
    	else{
    		jTextFieldSuperficieUso.setText("");
    	}
       
        if (usosCasasConsistoriales.getFechaUso() != null){
    		jTextFieldFechaUso.setDate(usosCasasConsistoriales.getFechaUso());
    	}
    	else{
    		jTextFieldFechaUso.setDate(null);
    	}
        
        if (usosCasasConsistoriales.getCodigoOrdenUso() != null){
    		jTextFieldCodigoOrden.setText(usosCasasConsistoriales.getCodigoOrdenUso());
    	}
    	else{
    		jTextFieldCodigoOrden.setText("");
    	}
        
        if (usosCasasConsistoriales.getObservacionesUso() != null){
    		jTextFieldObservacionesUso.setText(usosCasasConsistoriales.getObservacionesUso());
    	}
    	else{
    		jTextFieldObservacionesUso.setText("");
    	}
    	
        if (usosCasasConsistoriales.getInstPertenece() != null){
    		jComboBoxInst_P.setSelectedPatron(usosCasasConsistoriales.getInstPertenece());
    	}
    	else{
    		jComboBoxInst_P.setSelectedIndex(0);
    	}
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
	        	
	        	String orden_cc = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_cc"))!=null){
	        		orden_cc=(feature.getAttribute(esquema.getAttributeByColumn("orden_cc"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelCasasConsistorialesEIEL(clave, codprov, codmunic, entidad, nucleo, orden_cc));
	        	getJTableListaUsos().updateUI();
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, entidad, nucleo, orden_cc);       	
			}
		}
	
	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String entidad, String nucleo, String orden_cc) {
		
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
        
        if (orden_cc != null){
    		jTextFieldOrden.setText(orden_cc);
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
