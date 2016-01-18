/**
 * AbastecimientoAutonomoPanel.java
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
import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.dialogs.AbastecimientoAutonomoDialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class AbastecimientoAutonomoPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    
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
	private JLabel jLabelVivien = null;
	private JLabel jLabelPobRe = null;
	private JLabel jLabelPobEs = null;
	private JLabel jLabelDefVi = null;
	private JLabel jLabelDefRe = null;
	private JLabel jLabelDefEs = null;
	private JLabel jLabelFecont = null;
	private JLabel jLabelFencon = null;
	private JLabel jLabelCaudal = null;
	private JLabel jLabelObserv = null;
	private JTextField jTextFieldVivien = null;
	private JTextField jTextFieldPobRe = null;
	private JTextField jTextFieldPobEs = null;
	private JTextField jTextFieldDefVi = null;
	private JTextField jTextFieldDefRe = null;
	private JTextField jTextFieldDefEs = null;
	private JTextField jTextFieldFecont = null;
	private JTextField jTextFieldFencon = null;
	private JTextField jTextFieldObserv = null;
	private ComboBoxEstructuras jComboBoxCaudal = null;
	private JPanel jPanelRevision = null;
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	
    /**
     * This method initializes
     * 
     */
    public AbastecimientoAutonomoPanel()
    {
        super();
        initialize();
    }
    
    public AbastecimientoAutonomoPanel(AbastecimientoAutonomoEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(AbastecimientoAutonomoEIEL elemento)
    {
        if (elemento!=null)
        {
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
            
			 if (elemento.getCodINEEntidad()!= null){
		        	jComboBoxEntidad.setSelectedIndex(
		        			entidadesSingularesIndexSeleccionar(elemento.getCodINEEntidad())
		        	);
		      }
		      else{
		        	jComboBoxEntidad.setSelectedIndex(0);
		      }
		        
		        if (elemento.getCodINENucleo() != null){
		        	jComboBoxNucleo.setSelectedIndex(
		        			nucleoPoblacionIndexSeleccionar(elemento.getCodINENucleo())
		        			);
		        	
		        }
		        else{
		        	jComboBoxNucleo.setSelectedIndex(0);
		        }
            
            if (elemento.getViviendas() != null){
        		jTextFieldVivien.setText(elemento.getViviendas().toString());
        	}
        	else{
        		jTextFieldVivien.setText("");
        	}
            
            if (elemento.getPoblacionEstacional() != null){
        		jTextFieldPobEs.setText(elemento.getPoblacionEstacional().toString());
        	}
        	else{
        		jTextFieldPobEs.setText("");
        	}
            
            if (elemento.getPoblacionResidente() != null){
        		jTextFieldPobRe.setText(elemento.getPoblacionResidente().toString());
        	}
        	else{
        		jTextFieldPobRe.setText("");
        	}
            
            if (elemento.getViviendasDeficitarias() != null){
        		jTextFieldDefVi.setText(elemento.getViviendasDeficitarias().toString());
        	}
        	else{
        		jTextFieldDefVi.setText("");
        	}
            
            if (elemento.getPoblacionResidenteDef() != null){
        		jTextFieldDefRe.setText(elemento.getPoblacionResidenteDef().toString());
        	}
        	else{
        		jTextFieldDefRe.setText("");
        	}
            
            if (elemento.getPoblacionEstacionalDef() != null){
        		jTextFieldDefEs.setText(elemento.getPoblacionEstacionalDef().toString());
        	}
        	else{
        		jTextFieldDefEs.setText("");
        	}
            
            if (elemento.getFuentesControladas() != null){
        		jTextFieldFecont.setText(elemento.getFuentesControladas().toString());
        	}
        	else{
        		jTextFieldFecont.setText("");
        	}
            
            if (elemento.getFuentesNoControladas() != null){
        		jTextFieldFencon.setText(elemento.getFuentesNoControladas().toString());
        	}
        	else{
        		jTextFieldFencon.setText("");
        	}
            
            if (elemento.getSuficienciaCaudal() != null){
        		getJComboBoxCaudal().setSelectedPatron(elemento.getSuficienciaCaudal());
        	}
        	else{
        		getJComboBoxCaudal().setSelectedIndex(0);
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

        	jTextFieldClave.setText(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_CLAVE);
        	        	       	
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
    	Object object = AppContext.getApplicationContext().getBlackboard().get("abastecimiento_panel");    	
    	if (object != null && object instanceof AbastecimientoAutonomoEIEL){    		
    		AbastecimientoAutonomoEIEL elemento = (AbastecimientoAutonomoEIEL)object;
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
						.setSelectedIndex(municipioIndexSeleccionar(elemento.getCodINEMunicipio()));

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
	        
	        if (elemento.getCodINENucleo()!= null){
	        	jComboBoxNucleo.setSelectedIndex(
	        			nucleoPoblacionIndexSeleccionar(elemento.getCodINENucleo())
	        			);
	        	
	        }
	        else{
	        	jComboBoxNucleo.setSelectedIndex(0);
	        }
            
            if (elemento.getViviendas() != null){
        		jTextFieldVivien.setText(elemento.getViviendas().toString());
        	}
        	else{
        		jTextFieldVivien.setText("");
        	}
            
            if (elemento.getPoblacionEstacional() != null){
        		jTextFieldPobEs.setText(elemento.getPoblacionEstacional().toString());
        	}
        	else{
        		jTextFieldPobEs.setText("");
        	}
            
            if (elemento.getPoblacionResidente() != null){
        		jTextFieldPobRe.setText(elemento.getPoblacionResidente().toString());
        	}
        	else{
        		jTextFieldPobRe.setText("");
        	}
            
            if (elemento.getViviendasDeficitarias() != null){
        		jTextFieldDefVi.setText(elemento.getViviendasDeficitarias().toString());
        	}
        	else{
        		jTextFieldDefVi.setText("");
        	}
            
            if (elemento.getPoblacionResidenteDef() != null){
        		jTextFieldDefRe.setText(elemento.getPoblacionResidenteDef().toString());
        	}
        	else{
        		jTextFieldDefRe.setText("");
        	}
            
            if (elemento.getPoblacionEstacionalDef() != null){
        		jTextFieldDefEs.setText(elemento.getPoblacionEstacionalDef().toString());
        	}
        	else{
        		jTextFieldDefEs.setText("");
        	}
            
            if (elemento.getFuentesControladas() != null){
        		jTextFieldFecont.setText(elemento.getFuentesControladas().toString());
        	}
        	else{
        		jTextFieldFecont.setText("");
        	}
            
            if (elemento.getFuentesNoControladas() != null){
        		jTextFieldFencon.setText(elemento.getFuentesNoControladas().toString());
        	}
        	else{
        		jTextFieldFencon.setText("");
        	}
            
            if (elemento.getSuficienciaCaudal() != null){
        		getJComboBoxCaudal().setSelectedPatron(elemento.getSuficienciaCaudal());
        	}
        	else{
        		getJComboBoxCaudal().setSelectedItem(0);
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
            
            if (elemento.getFechaRevision() != null   && elemento.getFechaRevision().equals( new java.util.Date()) ){
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
    


	public AbastecimientoAutonomoEIEL getAbastecimientoAutonomo (AbastecimientoAutonomoEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new AbastecimientoAutonomoEIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
    		elemento.setCodINENucleo(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            
            
            elemento.setClave(jTextFieldClave.getText());
            
            if (jTextFieldVivien.getText()!=null && !jTextFieldVivien.getText().equals("")){
            	elemento.setViviendas(new Integer(jTextFieldVivien.getText()));
            }
            else if (jTextFieldVivien.getText().equals("")){
            	elemento.setViviendas(null);
            }
            
            if (jTextFieldPobRe.getText()!=null && !jTextFieldPobRe.getText().equals("")){
            	elemento.setPoblacionResidente(new Integer(jTextFieldPobRe.getText()));
            }
            else if (jTextFieldPobRe.getText().equals("")){
            	elemento.setPoblacionResidente(null);
            }
            
            if (jTextFieldPobEs.getText()!=null && !jTextFieldPobEs.getText().equals("")){
            	elemento.setPoblacionEstacional(new Integer(jTextFieldPobEs.getText()));
            }
            else if (jTextFieldPobEs.getText().equals("")){
            	elemento.setPoblacionEstacional(null);
            }
            
            if (jTextFieldDefVi.getText()!=null && !jTextFieldDefVi.getText().equals("")){
            	elemento.setViviendasDeficitarias(new Integer(jTextFieldDefVi.getText()));
            }
            else if (jTextFieldDefVi.getText().equals("")){
            	elemento.setViviendasDeficitarias(null);
            }
            
            if (jTextFieldDefRe.getText()!=null && !jTextFieldDefRe.getText().equals("")){
            	elemento.setPoblacionResidenteDef(new Integer(jTextFieldDefRe.getText()));
            }
            else if (jTextFieldDefRe.getText().equals("")){
            	elemento.setPoblacionResidenteDef(null);
            }
            
            if (jTextFieldDefEs.getText()!=null && !jTextFieldDefEs.getText().equals("")){
            	elemento.setPoblacionEstacionalDef(new Integer(jTextFieldDefEs.getText()));
            }
            else if (jTextFieldDefEs.getText().equals("")){
            	elemento.setPoblacionEstacionalDef(null);
            }
            
            if (jTextFieldFecont.getText()!=null && !jTextFieldFecont.getText().equals("")){
            	elemento.setFuentesControladas(new Integer(jTextFieldFecont.getText()));
            }
            else if (jTextFieldFecont.getText().equals("")){
            	elemento.setFuentesControladas(null);
            }
            
            if (jTextFieldFencon.getText()!=null && !jTextFieldFencon.getText().equals("")){
            	elemento.setFuentesNoControladas(new Integer(jTextFieldFencon.getText()));
            }
            else if (jTextFieldFencon.getText().equals("")){
            	elemento.setFuentesNoControladas(null);
            }
            
            if (jComboBoxCaudal.getSelectedPatron()!=null){
            	elemento.setSuficienciaCaudal(jComboBoxCaudal.getSelectedPatron());
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
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.abastecimientoautonomo.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(AbastecimientoAutonomoDialog.DIM_X,AbastecimientoAutonomoDialog.DIM_Y);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.abastecimientoautonomo.panel.title"));
        
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
    
    public JTextField getJTextFieldClave()
    {
        if (jTextFieldClave == null)
        {
            jTextFieldClave = new TextField();
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
        if (jComboBoxProvincia == null){
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
//        	jComboBoxEntidad.setEditable(true);
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
	            			//System.out.println("PASO1");
	            			//EdicionUtils.cargarLista(jComboBoxNucleo, oper.obtenerNucleosConNombre(entidad)); 
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
    
    private JTextField getJTextFieldVivien()
    {
    	if (jTextFieldVivien == null)
    	{
    		jTextFieldVivien = new TextField(10);
    		jTextFieldVivien.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivien, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivien;
    }
    
    private JTextField getJTextFieldPobRe()
    {
    	if (jTextFieldPobRe == null)
    	{
    		jTextFieldPobRe  = new TextField(10);
    		jTextFieldPobRe.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobRe, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobRe;
    }
    
    private JTextField getJTextFieldPobEs()
    {
    	if (jTextFieldPobEs == null)
    	{
    		jTextFieldPobEs  = new TextField(10);
    		jTextFieldPobEs.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEs, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEs;
    }
    
    private JTextField getJTextFieldDefVi()
    {
    	if (jTextFieldDefVi == null)
    	{
    		jTextFieldDefVi  = new TextField(10);
    		jTextFieldDefVi.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDefVi, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDefVi;
    }
    
    private JTextField getJTextFieldDefRe()
    {
    	if (jTextFieldDefRe == null)
    	{
    		jTextFieldDefRe  = new TextField(10);
    		jTextFieldDefRe.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDefRe, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDefRe;
    }
    
    private JTextField getJTextFieldDefEs()
    {
    	if (jTextFieldDefEs == null)
    	{
    		jTextFieldDefEs  = new TextField(10);
    		jTextFieldDefEs.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDefEs, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDefEs;
    }
    
    private JTextField getJTextFieldFecont()
    {
    	if (jTextFieldFecont == null)
    	{
    		jTextFieldFecont  = new TextField(10);
    		jTextFieldFecont.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldFecont, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldFecont;
    }
    
    private JTextField getJTextFieldFencon()
    {
    	if (jTextFieldFencon == null)
    	{
    		jTextFieldFencon  = new TextField(10);
    		jTextFieldFencon.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldFencon, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldFencon;
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
    
    private ComboBoxEstructuras getJComboBoxCaudal()
    {
    	if (jComboBoxCaudal == null)
    	{
    		
    		Estructuras.cargarEstructura("eiel_Disponibilidad de agua");
    		jComboBoxCaudal  = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);

    	}
    	return jComboBoxCaudal;
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
    
    public AbastecimientoAutonomoPanel(GridBagLayout layout)
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
        	
            jLabelVivien = new JLabel("", JLabel.CENTER); 
            jLabelVivien.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.vivien")); 
            
            jLabelPobRe = new JLabel("", JLabel.CENTER); 
            jLabelPobRe.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobre")); 
            
            jLabelPobEs = new JLabel("", JLabel.CENTER); 
            jLabelPobEs.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobes")); 

            jLabelDefVi = new JLabel("", JLabel.CENTER);
            jLabelDefVi.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.defvi"));
            
            jLabelDefRe = new JLabel("", JLabel.CENTER);
            jLabelDefRe.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.delre"));
            
            jLabelDefEs = new JLabel("", JLabel.CENTER);
            jLabelDefEs.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.defes"));
            
            jLabelFecont = new JLabel("", JLabel.CENTER);
            jLabelFecont.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecont"));
            
            jLabelFencon = new JLabel("", JLabel.CENTER);
            jLabelFencon.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fencon"));
            
            jLabelCaudal = new JLabel("", JLabel.CENTER);
            jLabelCaudal.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.caudal"));
            
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            
            
            jPanelInformacion.add(jLabelVivien,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldVivien(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobRe,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldPobRe(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobEs,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldPobEs(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelDefVi,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldDefVi(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelDefRe,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldDefRe(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelDefEs,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldDefEs(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFecont,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldFecont(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFencon,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldFencon(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelCaudal,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxCaudal(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 5, 2, 1, 0.1, 0.1,
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
                            new Insets(5, 5, 5, 5), 1, 0));
            
            
            jPanelRevision.add(getJComboBoxEstado(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 50, 0));
            
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

		        if(obj!=null && !obj.equals("") && 
		                ((esquema.getGeopistalayer()!=null && !esquema.getGeopistalayer().isExtracted() && !esquema.getGeopistalayer().isLocal()) 
		                        || (esquema.getGeopistalayer()==null) && feature instanceof GeopistaFeature && !((GeopistaFeature)feature).getLayer().isExtracted()))
		        {   	

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
		        	loadData(operations.getPanelAbastecimientoAutonomoEIEL(codprov,codmunic,entidad,nucleo));
		        	
		        	loadDataIdentificacion(codprov, codmunic, entidad, nucleo);

		        }
				
			}
		}
	

	public void loadDataIdentificacion(String codprov,
			String codmunic, String entidad, String nucleo) {
		
		//Datos identificacion        
        if (codprov != null){
        	jComboBoxProvincia.setSelectedItem(codprov);
        }
        else{
        	jComboBoxProvincia.setSelectedIndex(-1);
        }
        
        if (codmunic != null){            	
        	jComboBoxMunicipio.setSelectedItem(codmunic);
        }
        else{
        	jComboBoxMunicipio.setSelectedIndex(-1);
        }
        
        if (entidad != null){
        	jComboBoxEntidad.setSelectedItem(entidad);
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(-1);
        }
        
        if (nucleo != null){
        	jComboBoxNucleo.setSelectedItem(nucleo);
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(-1);
        }
        

	}
	
	public int provinciaIndexSeleccionar(String provinciaId){
		for (int i = 0; i < jComboBoxProvincia.getItemCount(); i ++){
			if ( (!jComboBoxProvincia.getItemAt(i).equals("")) && (jComboBoxProvincia.getItemAt(i) instanceof Provincia)&& ((Provincia)jComboBoxProvincia.getItemAt(i)).getIdProvincia().equals(provinciaId) ){
				return i;
			}
		}
		
		return -1;
	}
	
	public int municipioIndexSeleccionar(String municipioId){
		if (!municipioId.equals("")){
			for (int i = 0; i < jComboBoxMunicipio.getItemCount(); i ++){
				try{
					if (((Municipio)jComboBoxMunicipio.getItemAt(i)).getIdIne().equals(municipioId.substring(2, 5)) ){
						return i;
					}
				}catch (StringIndexOutOfBoundsException e) {
					if (((Municipio)jComboBoxMunicipio.getItemAt(i)).getIdIne().equals(municipioId) ){
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
