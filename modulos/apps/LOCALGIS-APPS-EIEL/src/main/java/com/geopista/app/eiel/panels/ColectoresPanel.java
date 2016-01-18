/**
 * ColectoresPanel.java
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
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.ColectoresDialog;
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

public class ColectoresPanel extends JPanel  implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    private JPanel jPanelRevision = null;
    
    public boolean okPressed = false;	
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
	private JLabel jLabelTitular = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private JLabel jLabelGestor = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private JLabel jLabelEstado = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private JLabel jLabelSistImpul = null;
	private ComboBoxEstructuras jComboBoxSistImpul = null;
	private JLabel jLabelMaterial = null;
	private ComboBoxEstructuras jComboBoxMaterial = null;
	private JLabel jLabelTipo_red_interior=null;
	private JLabel jLabelTip_interceptor=null; 
	private JLabel jLabelFecha_inst =null;
	private DateField jTextFieldFechaInst = null;
	private JLabel jLabelObserv = null;
	private JTextField jTextFieldObserv = null;
	// GUI - Revision
	private JLabel jLabelFechaRevision = null;
	private JTextField jTextFieldFechaRevision = null;
	private JLabel jLabelEstadoRevision = null;
	private ComboBoxEstructuras jComboBoxEstado_revision = null;

	private ComboBoxEstructuras jComboBoxTipo_red;

	private ComboBoxEstructuras jComboBoxTipo_interceptor;
	
	private String idMunicipioSelected;
	
    /**
     * This method initializes
     * 
     */
    public ColectoresPanel()
    {
        super();
        initialize();
    }
    
    public ColectoresPanel(ColectorEIEL elemento)
    {
        super();
        initialize();
        loadData (elemento);
    }
    
    public void loadData(ColectorEIEL elemento)
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
        		jComboBoxEstado_revision.setSelectedPatron(elemento.getEstado_Revision().toString());
	        }
	        else{
	        	jComboBoxEstado_revision.setSelectedIndex(0);
	        }       
            if (elemento.getSist_impulsion() != null){
        		jComboBoxSistImpul.setSelectedPatron(elemento.getSist_impulsion());
        	}
        	else{
        		jComboBoxSistImpul.setSelectedIndex(0);
        	}     
            if (elemento.getTipo_red() != null){
        		jComboBoxTipo_red.setSelectedPatron(elemento.getTipo_red());
        	}
        	else{
        		jComboBoxTipo_red.setSelectedIndex(0);
        	}     
            if (elemento.getTip_interceptor() != null){
            	jComboBoxTipo_interceptor.setSelectedPatron(elemento.getTip_interceptor());
        	}
        	else{
        		jComboBoxTipo_interceptor.setSelectedIndex(0);
        	}     
            
    }else{
    	// elemento a cargar es null....
    	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
    	
    	jTextFieldClave.setText(ConstantesLocalGISEIEL.COLECTOR_CLAVE);
    	        	       	
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
    	Object object = AppContext.getApplicationContext().getBlackboard().get("colector");    	
    	if (object != null && object instanceof ColectorEIEL){    		
    		ColectorEIEL elemento = (ColectorEIEL)object;
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
            
            if (elemento.getEstado_Revision()!= null){
            	jComboBoxEstado_revision.setSelectedPatron(Integer.toString(elemento.getEstado_Revision()));            }
            else{
            	jComboBoxEstado_revision.setSelectedIndex(0);
            }       
            if (elemento.getSist_impulsion() != null){
        		jComboBoxSistImpul.setSelectedPatron(elemento.getSist_impulsion());
        	}
        	else{
        		jComboBoxSistImpul.setSelectedIndex(0);
        	}     
            if (elemento.getTipo_red() != null){
        		jComboBoxTipo_red.setSelectedPatron(elemento.getTipo_red());
        	}
        	else{
        		jComboBoxTipo_red.setSelectedIndex(0);
        	}     
            if (elemento.getTip_interceptor() != null){
            	jComboBoxTipo_interceptor.setSelectedPatron(elemento.getTip_interceptor());
        	}
        	else{
        		jComboBoxTipo_interceptor.setSelectedIndex(0);
        	} 
    	}
    }
    
    public ColectorEIEL getColector (ColectorEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new ColectorEIEL();
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
                     
            if (jComboBoxTitular.getSelectedPatron()!=null)
    			elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());
    		else elemento.setTitularidad("");
            
    		if (jComboBoxGestor.getSelectedPatron()!=null)
    			elemento.setGestion((String) jComboBoxGestor.getSelectedPatron());
    		else elemento.setGestion("");
            
            if (jComboBoxEstado.getSelectedPatron()!=null)
                elemento.setEstado((String) jComboBoxEstado.getSelectedPatron());
    		else 
    			elemento.setEstado("");
            
            if (jComboBoxMaterial.getSelectedPatron()!=null)
                elemento.setMaterial((String) jComboBoxMaterial.getSelectedPatron());
    		else 
    			elemento.setMaterial("");
            
            if (jComboBoxSistImpul.getSelectedPatron()!=null)
                elemento.setSist_impulsion((String) jComboBoxSistImpul.getSelectedPatron());
    		else 
    			elemento.setSist_impulsion("");
            
            if (jComboBoxTipo_red.getSelectedPatron()!=null)
                elemento.setTipo_red((String) jComboBoxTipo_red.getSelectedPatron());
    		else 
    			elemento.setTipo_red("");
            if (jComboBoxTipo_interceptor.getSelectedPatron()!=null)
                elemento.setTip_interceptor((String) jComboBoxTipo_interceptor.getSelectedPatron());
    		else 
    			elemento.setTip_interceptor("");      
            
            if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){

    			java.sql.Date sqlDate = new java.sql.Date(
    					getJTextFieldFechaInstalacion().getDate().getYear(),
    					getJTextFieldFechaInstalacion().getDate().getMonth(),
    					getJTextFieldFechaInstalacion().getDate().getDate()
    			);

    			elemento.setFecha_inst(sqlDate);
    		}  
    		else{
    			elemento.setFecha_inst(null);
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
            
            if (jComboBoxEstado_revision.getSelectedPatron()!=null)
         		elemento.setEstado_Revision(Integer.parseInt(jComboBoxEstado_revision.getSelectedPatron()));
            
        }
        
        return elemento;
    }
    
    public ColectorEIEL getColectorData ()
    {
    	ColectorEIEL elemento=new ColectorEIEL();        

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
                 
        if (jComboBoxTitular.getSelectedPatron()!=null)
			elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());
		else elemento.setTitularidad("");
        
		if (jComboBoxGestor.getSelectedPatron()!=null)
			elemento.setGestion((String) jComboBoxGestor.getSelectedPatron());
		else elemento.setGestion("");
        
        if (jComboBoxEstado.getSelectedPatron()!=null)
            elemento.setEstado((String) jComboBoxEstado.getSelectedPatron());
		else 
			elemento.setEstado("");
        
        if (jComboBoxMaterial.getSelectedPatron()!=null)
            elemento.setMaterial((String) jComboBoxMaterial.getSelectedPatron());
		else 
			elemento.setMaterial("");
        
        if (jComboBoxSistImpul.getSelectedPatron()!=null)
            elemento.setSist_impulsion((String) jComboBoxSistImpul.getSelectedPatron());
		else 
			elemento.setSist_impulsion("");
        
        if (jComboBoxTipo_red.getSelectedPatron()!=null)
            elemento.setTipo_red((String) jComboBoxTipo_red.getSelectedPatron());
		else 
			elemento.setTipo_red("");
        if (jComboBoxTipo_interceptor.getSelectedPatron()!=null)
            elemento.setTip_interceptor((String) jComboBoxTipo_interceptor.getSelectedPatron());
		else 
			elemento.setTip_interceptor("");      
        
        if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){

			java.sql.Date sqlDate = new java.sql.Date(
					getJTextFieldFechaInstalacion().getDate().getYear(),
					getJTextFieldFechaInstalacion().getDate().getMonth(),
					getJTextFieldFechaInstalacion().getDate().getDate()
			);

			elemento.setFecha_inst(sqlDate);
		}  
		else{
			elemento.setFecha_inst(null);
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
        
        if (jComboBoxEstado_revision.getSelectedPatron()!=null)
     		elemento.setEstado_Revision(Integer.parseInt(jComboBoxEstado_revision.getSelectedPatron()));
     
                    
        return elemento;
    }
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(ColectoresDialog.DIM_X,ColectoresDialog.DIM_Y);
        
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
			 //EdicionUtils.cargarLista(getJComboBoxProvincia(),
			 //oper.obtenerProvinciasConNombre(true));
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
    
   
    private JTextField getJTextFieldOrden()
    {
    	if (jTextFieldOrden   == null)
    	{
    		jTextFieldOrden = new TextField(3);
    	}
    	return jTextFieldOrden;
    }
    
    private ComboBoxEstructuras getJComboBoxTitular()
    {
    	 if (jComboBoxTitular == null)
         {
             Estructuras.cargarEstructura("eiel_Titularidad");
             jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                     null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
         
             jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
         }
         return jComboBoxTitular;    
    }
    
    private ComboBoxEstructuras getJComboBoxGestor()
    { 
        if (jComboBoxGestor == null)
        {
            Estructuras.cargarEstructura("eiel_Gestión");
            jComboBoxGestor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGestor;        
    }
    
    private ComboBoxEstructuras getJComboBoxSist_impul()
    { 
        if (jComboBoxSistImpul == null)
        {
            Estructuras.cargarEstructura("eiel_Sistema de impulsión");
            jComboBoxSistImpul = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxSistImpul.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxSistImpul;        
    }
    
    private ComboBoxEstructuras getJComboBoxTipo_red()
    { 
        if (jComboBoxTipo_red == null)
        {
            Estructuras.cargarEstructura("eiel_tipo_red_interior");
            jComboBoxTipo_red = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxTipo_red.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTipo_red;        
    }
    
    private ComboBoxEstructuras getJComboBoxTip_interceptor()
    { 
        if (jComboBoxTipo_interceptor == null)
        {
            Estructuras.cargarEstructura("eiel_Colector de Tipo interceptor");
            jComboBoxTipo_interceptor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxTipo_interceptor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTipo_interceptor;        
    }
    
    private ComboBoxEstructuras getJComboBoxEstado()
    { 
        if (jComboBoxEstado == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEstado = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado;        
    }
    
    private ComboBoxEstructuras getJComboBoxMaterial()
    { 
        if (jComboBoxMaterial == null)
        {
            Estructuras.cargarEstructura("eiel_material");
            jComboBoxMaterial = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxMaterial.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxMaterial;        
    }
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv  == null)
    	{
    		jTextFieldObserv  = new TextField(50);
    		
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
    
   	private ComboBoxEstructuras getJComboBoxEstado_revision()
        { 
            if (jComboBoxEstado_revision == null)
            {
                Estructuras.cargarEstructura("eiel_Estado de revisión");
                jComboBoxEstado_revision = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                        null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
            
                jComboBoxEstado_revision.setPreferredSize(new Dimension(100, 20));
            }
            return jComboBoxEstado_revision;        
    }
    
    private DateField getJTextFieldFechaInstalacion()
    {
    	if (jTextFieldFechaInst  == null){
    		jTextFieldFechaInst  = new DateField((java.util.Date) null, 0);    		
    		jTextFieldFechaInst.setDateFormatString("yyyy-MM-dd");
    	}
    	return jTextFieldFechaInst;
    }
    
    
    public ColectoresPanel(GridBagLayout layout)
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
            
            jLabelTip_interceptor = new JLabel("", JLabel.CENTER);
            jLabelTip_interceptor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tip_interceptor"));          
            
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
            
            jPanelInformacion.add(getJComboBoxSist_impul(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFecha_inst,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldFechaInstalacion(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelInformacion.add(jLabelTipo_red_interior,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxTipo_red(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelTip_interceptor,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxTip_interceptor(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
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
            
            
            jPanelRevision.add(getJComboBoxEstado_revision(), 
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

	        	
	        	String tramo_cl = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("tramo_cl"))!=null){
	        		tramo_cl=(feature.getAttribute(esquema.getAttributeByColumn("tramo_cl"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelColectorEIEL(clave, codprov, codmunic, tramo_cl));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, tramo_cl);
			}
		}
	
	


	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String tramo_cl) {
		
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
                
        if (tramo_cl != null){
    		jTextFieldOrden.setText(tramo_cl);
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
