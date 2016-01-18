/**
 * Encuestados2Panel.java
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
import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.dialogs.Encuestados2Dialog;
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

public class Encuestados2Panel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    
    public boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelCodMunic = null;
	private JLabel jLabelEntSing = null;
	private JLabel jLabelNucleo = null;
	
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JComboBox jComboBoxEntidad = null;
	private JComboBox jComboBoxNucleo = null;
	
	private JLabel jLabelCaudal = null;
	private JLabel jLabelRestri = null;
	private JLabel jLabelContad = null;
	private JLabel jLabelTasa = null;
	private JLabel jLabelInstal = null;
	private JLabel jLabelHidran = null;
	private JLabel jLabelEstHi = null;	
	private JLabel jLabelValvul = null;
	private JLabel jLabelEstVa = null;
	private JLabel jLabelBocasR = null;
	private JLabel jLabelEstBo = null;
	private JLabel jLabelCisterna = null;
	private JLabel jLabelObserv = null;
	
	private ComboBoxEstructuras jComboBoxCaudal = null;
	private ComboBoxEstructuras jComboBoxRestri = null;
	private ComboBoxEstructuras jComboBoxContad = null;
	private ComboBoxEstructuras jComboBoxTasa = null;
//	private ComboBoxEstructuras jComboBoxInstal = null;
	private JTextField jTextFieldInstal = null;
	private ComboBoxEstructuras jComboBoxHidran = null;
	private ComboBoxEstructuras jComboBoxEstHi = null;	
	private ComboBoxEstructuras jComboBoxValvul = null;
	private ComboBoxEstructuras jComboBoxEstVa = null;
	private ComboBoxEstructuras jComboBoxBocasR = null;
	private ComboBoxEstructuras jComboBoxEstBo = null;
	private ComboBoxEstructuras jComboBoxCisterna = null;
	
	private JTextField jTextFieldObserv = null;
	private JPanel jPanelRevision = null;
	
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;
	
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	
    /**
     * This method initializes
     * 
     */
    public Encuestados2Panel()
    {
        super();
        initialize();
    }
    
    public Encuestados2Panel(Encuestados2EIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(Encuestados2EIEL elemento)
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
                        
            if (elemento.getDisponibilidadCaudal() != null){
        		jComboBoxCaudal.setSelectedPatron(elemento.getDisponibilidadCaudal());
        	}
        	else{
        		jComboBoxCaudal.setSelectedIndex(0);
        	}
            
            if (elemento.getRestriccionesAgua() != null){
        		jComboBoxRestri.setSelectedPatron(elemento.getRestriccionesAgua());
        	}
        	else{
        		jComboBoxRestri.setSelectedIndex(0);
        	}
            
            if (elemento.getContadores() != null){
        		jComboBoxContad.setSelectedPatron(elemento.getContadores());
        	}
        	else{
        		jComboBoxContad.setSelectedIndex(0);
        	}
            
            if (elemento.getTasa() != null){
        		jComboBoxTasa.setSelectedPatron(elemento.getTasa());
        	}
        	else{
        		jComboBoxTasa.setSelectedIndex(0);
        	}
            
            if (elemento.getAnnoInstalacion()!= null){
            	jTextFieldInstal.setText(elemento.getAnnoInstalacion());
        	}
        	else{
        		jTextFieldInstal.setText("");
        	}
            
            if (elemento.getHidrantes() != null){
        		jComboBoxHidran.setSelectedPatron(elemento.getHidrantes());
        	}
        	else{
        		jComboBoxHidran.setSelectedIndex(0);
        	}
            
            if (elemento.getEstadoHidrantes() != null){
        		jComboBoxEstHi.setSelectedPatron(elemento.getEstadoHidrantes());
        	}
        	else{
        		jComboBoxEstHi.setSelectedIndex(0);
        	}
            
            if (elemento.getValvulas() != null){
        		jComboBoxValvul.setSelectedPatron(elemento.getValvulas());
        	}
        	else{
        		jComboBoxValvul.setSelectedIndex(0);
        	}
            
            if (elemento.getEstadoValvulas()!= null){
        		jComboBoxEstVa.setSelectedPatron(elemento.getEstadoValvulas());
        	}
        	else{
        		jComboBoxEstVa.setSelectedIndex(0);
        	}
            
            if (elemento.getBocasRiego() != null){
        		jComboBoxBocasR.setSelectedPatron(elemento.getBocasRiego());
        	}
        	else{
        		jComboBoxBocasR.setSelectedIndex(0);
        	}
            
            if (elemento.getEstadoBocasRiego() != null){
        		jComboBoxEstBo.setSelectedPatron(elemento.getEstadoBocasRiego());
        	}
        	else{
        		jComboBoxEstBo.setSelectedIndex(0);
        	}
            
            if (elemento.getCisterna() != null){
        		jComboBoxCisterna.setSelectedPatron(elemento.getCisterna());
        	}
        	else{
        		jComboBoxCisterna.setSelectedIndex(0);
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

    	Object object = AppContext.getApplicationContext().getBlackboard().get("propiedadesnucleosencuestados2Panel_panel");    	
    	    	if (object != null && object instanceof Encuestados2EIEL){    		
    	    		Encuestados2EIEL elemento = (Encuestados2EIEL)object;
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
            
            if (elemento.getDisponibilidadCaudal() != null){
        		jComboBoxCaudal.setSelectedPatron(elemento.getDisponibilidadCaudal());
        	}
        	else{
        		jComboBoxCaudal.setSelectedIndex(0);
        	}
            
            if (elemento.getRestriccionesAgua() != null){
        		jComboBoxRestri.setSelectedPatron(elemento.getRestriccionesAgua());
        	}
        	else{
        		jComboBoxRestri.setSelectedIndex(0);
        	}
            
            if (elemento.getContadores() != null){
        		jComboBoxContad.setSelectedPatron(elemento.getContadores());
        	}
        	else{
        		jComboBoxContad.setSelectedIndex(0);
        	}
            
            if (elemento.getTasa() != null){
        		jComboBoxTasa.setSelectedPatron(elemento.getTasa());
        	}
        	else{
        		jComboBoxTasa.setSelectedIndex(0);
        	}
            
            if (elemento.getAnnoInstalacion()!= null){
            	jTextFieldInstal.setText(elemento.getAnnoInstalacion());
        	}
        	else{
        		jTextFieldInstal.setText("");
        	}
            
            if (elemento.getHidrantes() != null){
        		jComboBoxHidran.setSelectedPatron(elemento.getHidrantes());
        	}
        	else{
        		jComboBoxHidran.setSelectedIndex(0);
        	}
            
            if (elemento.getEstadoHidrantes() != null){
        		jComboBoxEstHi.setSelectedPatron(elemento.getEstadoHidrantes());
        	}
        	else{
        		jComboBoxEstHi.setSelectedIndex(0);
        	}
            
            if (elemento.getValvulas() != null){
        		jComboBoxValvul.setSelectedPatron(elemento.getValvulas());
        	}
        	else{
        		jComboBoxValvul.setSelectedIndex(0);
        	}
            
            if (elemento.getEstadoValvulas()!= null){
        		jComboBoxEstVa.setSelectedPatron(elemento.getEstadoValvulas());
        	}
        	else{
        		jComboBoxEstVa.setSelectedIndex(0);
        	}
            
            if (elemento.getBocasRiego() != null){
        		jComboBoxBocasR.setSelectedPatron(elemento.getBocasRiego());
        	}
        	else{
        		jComboBoxBocasR.setSelectedIndex(0);
        	}
            
            if (elemento.getEstadoBocasRiego() != null){
        		jComboBoxEstBo.setSelectedPatron(elemento.getEstadoBocasRiego());
        	}
        	else{
        		jComboBoxEstBo.setSelectedIndex(0);
        	}
            
            if (elemento.getCisterna() != null){
        		jComboBoxCisterna.setSelectedPatron(elemento.getCisterna());
        	}
        	else{
        		jComboBoxCisterna.setSelectedIndex(0);
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
            
        }
    }
    
    public Encuestados2EIEL getEncuestados2 (Encuestados2EIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new Encuestados2EIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            
            if (jComboBoxCaudal.getSelectedPatron()!=null)
	            elemento.setDisponibilidadCaudal((String) jComboBoxCaudal.getSelectedPatron());
            else elemento.setDisponibilidadCaudal("");
            if (jComboBoxRestri.getSelectedPatron()!=null)
	            elemento.setRestriccionesAgua((String) jComboBoxRestri.getSelectedPatron());
            else elemento.setRestriccionesAgua("");
            if (jComboBoxContad.getSelectedPatron()!=null)
	            elemento.setContadores((String) jComboBoxContad.getSelectedPatron());
            else elemento.setContadores("");
            if (jComboBoxTasa.getSelectedPatron()!=null)
	            elemento.setTasa((String) jComboBoxTasa.getSelectedPatron());
            else elemento.setTasa("");
            if (jTextFieldInstal.getText()!=null)
	            elemento.setAnnoInstalacion((String) jTextFieldInstal.getText());
            else elemento.setAnnoInstalacion("");
            if (jComboBoxHidran.getSelectedPatron()!=null)
	            elemento.setHidrantes((String) jComboBoxHidran.getSelectedPatron());
            else elemento.setHidrantes("");
            if (jComboBoxEstHi.getSelectedPatron()!=null)
	            elemento.setEstadoHidrantes((String) jComboBoxEstHi.getSelectedPatron());
            else elemento.setEstadoHidrantes("");
            if (jComboBoxValvul.getSelectedPatron()!=null)
	            elemento.setValvulas((String) jComboBoxValvul.getSelectedPatron());
            else elemento.setValvulas("");
            if (jComboBoxEstVa.getSelectedPatron()!=null)
	            elemento.setEstadoValvulas((String) jComboBoxEstVa.getSelectedPatron());
            else elemento.setEstadoValvulas("");
            if (jComboBoxBocasR.getSelectedPatron()!=null)
	            elemento.setBocasRiego((String) jComboBoxBocasR.getSelectedPatron());
            else elemento.setBocasRiego("");
            if (jComboBoxEstBo.getSelectedPatron()!=null)
	            elemento.setEstadoBocasRiego((String) jComboBoxEstBo.getSelectedPatron());
            else elemento.setEstadoBocasRiego("");
            if (jComboBoxCisterna.getSelectedPatron()!=null)
	            elemento.setCisterna((String) jComboBoxCisterna.getSelectedPatron());
            else elemento.setCisterna("");
	            
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
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.propiedadesnucleosencuestados2.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(Encuestados2Dialog.DIM_X,Encuestados2Dialog.DIM_Y);
        
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
    
    private ComboBoxEstructuras getJComboBoxCaudal()
    { 
        if (jComboBoxCaudal == null)
        {
            Estructuras.cargarEstructura("eiel_Suficiencia de caudal para el abastecimiento autónomo");
            jComboBoxCaudal = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCaudal.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCaudal;        
    }
    
    private ComboBoxEstructuras getJComboBoxRestri()
    { 
        if (jComboBoxRestri == null)
        {
            Estructuras.cargarEstructura("eiel_Restricciones de agua");
            jComboBoxRestri = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxRestri.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxRestri;        
    }
    
    private ComboBoxEstructuras getJComboBoxContad()
    { 
        if (jComboBoxContad == null)
        {
            Estructuras.cargarEstructura("eiel_Contador Abast");
            jComboBoxContad = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxContad.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxContad;        
    }
    
    private ComboBoxEstructuras getJComboBoxTasa()
    { 
        if (jComboBoxTasa == null)
        {
            Estructuras.cargarEstructura("eiel_Tasa");
            jComboBoxTasa = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTasa.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTasa;        
    }
    
//    private ComboBoxEstructuras getJComboBoxInstal()
//    { 
//        if (jComboBoxInstal == null)
//        {
//            Estructuras.cargarEstructura("eiel_SI_NO");
//            jComboBoxInstal = new ComboBoxEstructuras(Estructuras.getListaTipos(),
//                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
//        
//            jComboBoxInstal.setPreferredSize(new Dimension(100, 20));
//        }
//        return jComboBoxInstal;        
//    }
    private JTextField getJTextFieldInstal()
    {
    	if (jTextFieldInstal == null)
    	{
    		jTextFieldInstal = new TextField(4);
    		jTextFieldInstal.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldInstal, 4, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldInstal;
    }
    
    private ComboBoxEstructuras getJComboBoxHidran()
    { 
        if (jComboBoxHidran == null)
        {
            Estructuras.cargarEstructura("eiel_Suficiencia de caudal para el abastecimiento autónomo");
            jComboBoxHidran = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxHidran.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxHidran;        
    }
    
    private ComboBoxEstructuras getJComboBoxEstHi()
    { 
        if (jComboBoxEstHi == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEstHi = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstHi.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstHi;        
    }
    
    private ComboBoxEstructuras getJComboBoxValvul()
    { 
        if (jComboBoxValvul == null)
        {
            Estructuras.cargarEstructura("eiel_Suficiencia de caudal para el abastecimiento autónomo");
            jComboBoxValvul = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxValvul.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxValvul;        
    }
    
    private ComboBoxEstructuras getJComboBoxEstVa()
    { 
        if (jComboBoxEstVa == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEstVa = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstVa.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstVa;        
    }
    
    private ComboBoxEstructuras getJComboBoxBocasR()
    { 
        if (jComboBoxBocasR == null)
        {
            Estructuras.cargarEstructura("eiel_Suficiencia de caudal para el abastecimiento autónomo");
            jComboBoxBocasR = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxBocasR.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxBocasR;        
    }
    
    private ComboBoxEstructuras getJComboBoxEstBo()
    { 
        if (jComboBoxEstBo == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEstBo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstBo.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstBo;        
    }
    
    private ComboBoxEstructuras getJComboBoxCisterna()
    { 
        if (jComboBoxCisterna == null)
        {
            Estructuras.cargarEstructura("eiel_Cisterna");
            jComboBoxCisterna = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCisterna.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCisterna;        
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
    
    
    public Encuestados2Panel(GridBagLayout layout)
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
        	
            jLabelCaudal = new JLabel("", JLabel.CENTER); 
            jLabelCaudal.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.caudal")); 
            
            jLabelRestri = new JLabel("", JLabel.CENTER); 
            jLabelRestri.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.restricciones")); 

            jLabelContad = new JLabel("", JLabel.CENTER);
            jLabelContad.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.cont"));
            
            jLabelTasa = new JLabel("", JLabel.CENTER);
            jLabelTasa.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tasa"));
            
            jLabelInstal = new JLabel("", JLabel.CENTER);
            jLabelInstal.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.annoInstal"));
            
            jLabelHidran = new JLabel("", JLabel.CENTER);
            jLabelHidran.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.hidrantes"));
            
            jLabelEstHi = new JLabel("", JLabel.CENTER);
            jLabelEstHi.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estadoHid"));
            
            jLabelValvul = new JLabel("", JLabel.CENTER);
            jLabelValvul.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.valvulas"));
            
            jLabelEstVa = new JLabel("", JLabel.CENTER);
            jLabelEstVa.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estadoVal"));
            
            jLabelBocasR = new JLabel("", JLabel.CENTER);
            jLabelBocasR.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.bocasRiego"));
            
            jLabelEstBo = new JLabel("", JLabel.CENTER);
            jLabelEstBo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estadoBoc"));
            
            jLabelCisterna = new JLabel("", JLabel.CENTER);
            jLabelCisterna.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.cisterna"));
            
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            
            
            jPanelInformacion.add(jLabelCaudal,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxCaudal(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelRestri,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxRestri(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 30, 0));
            
            jPanelInformacion.add(jLabelContad,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxContad(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTasa,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxTasa(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelInstal,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJTextFieldInstal(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelHidran,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxHidran(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEstHi,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxEstHi(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelValvul,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxValvul(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEstVa,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxEstVa(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelBocasR,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxBocasR(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEstBo,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxEstBo(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCisterna,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(getJComboBoxCisterna(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            JPanel panelObservaciones = new JPanel(new GridBagLayout());
            
            panelObservaciones.add(jLabelObserv,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            panelObservaciones.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 350, 0));
            
            jPanelInformacion.add(panelObservaciones, 
                    new GridBagConstraints(0, 6, 3, 1, 0.1, 0.1,
                            GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
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
                            new Insets(5, 5, 5, 5), 150, 0));
            
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
	        	loadData(operations.getPanelNucleosEncuestados2EIEL(codprov, codmunic, entidad, nucleo));
	        	
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
