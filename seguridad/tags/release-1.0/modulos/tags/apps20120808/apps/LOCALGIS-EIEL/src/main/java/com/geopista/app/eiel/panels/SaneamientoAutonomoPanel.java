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
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.dialogs.SaneamientoAutonomoDialog;
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

public class SaneamientoAutonomoPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
        
    private boolean okPressed = false;	
	
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
	
    private JPanel jPanelInformacion = null;
    
    private ComboBoxEstructuras jComboBoxTipo = null;
    private ComboBoxEstructuras jComboBoxEst = null;
    private ComboBoxEstructuras jComboBoxAdecuacion = null;
    private JTextField jTextFieldVivien = null;
    private JTextField jTextFieldPobRe = null;
    private JTextField jTextFieldPobEs = null;
    private JTextField jTextFieldViDef = null;
    private JTextField jTextFieldPobReDef = null;
    private JTextField jTextFieldPobEsDef = null;    
    private DateField jTextFieldFechaInst = null;    
    private JTextField jTextFieldObservacion = null;
    
    private JLabel jLabelTipo = null;
    private JLabel jLabelEst = null;
    private JLabel jLabelAdecuacion = null;
    private JLabel jLabelVivien = null;
    private JLabel jLabelPobRe = null;
    private JLabel jLabelPobEs = null;
    private JLabel jLabelViDef = null;
    private JLabel jLabelPobReDef = null;
    private JLabel jLabelPobEsDef = null;
    private JLabel jLabelFechaInst = null;
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
    public SaneamientoAutonomoPanel()
    {
        super();
        initialize();
    }
    
    public SaneamientoAutonomoPanel(SaneamientoAutonomoEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(SaneamientoAutonomoEIEL elemento)
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
            
            
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            
           if (elemento.getEstado() != null){            	
           	jComboBoxEst.setSelectedPatron(elemento.getEstado());
           }
           else{
           	jComboBoxEst.setSelectedIndex(0);
           } 
           
            if (elemento.getAdecuacion()!= null){            	
            	jComboBoxAdecuacion.setSelectedPatron(elemento.getAdecuacion());
            }
            else{
            	jComboBoxAdecuacion.setSelectedIndex(0);
            } 
            
            if (elemento.getViviendas()!= null){            	
            	jTextFieldVivien.setText(elemento.getViviendas().toString());
            }
            else{
            	jTextFieldVivien.setText("");
            }
            
            
            if (elemento.getPoblResidente() != null){            	
            	jTextFieldPobRe.setText(elemento.getPoblResidente().toString());
            }
            else{
            	jTextFieldPobRe.setText("");
            }
            
            if (elemento.getPoblEstacional() != null){            	
            	jTextFieldPobEs.setText(elemento.getPoblEstacional().toString());
            }
            else{
            	jTextFieldPobEs.setText("");
            } 
            
            if (elemento.getVivDeficitarias()!= null){            	
            	jTextFieldViDef.setText(elemento.getVivDeficitarias().toString());
            }
            else{
            	jTextFieldViDef.setText("");
            } 
            
            if (elemento.getPoblResDeficitaria()!= null){            	
            	jTextFieldPobReDef.setText(elemento.getPoblResDeficitaria().toString());
            }
            else{
            	jTextFieldPobReDef.setText("");
            } 
            
            if (elemento.getPoblEstDeficitaria() != null){            	
            	jTextFieldPobEsDef.setText(elemento.getPoblEstDeficitaria().toString());
            }
            else{
            	jTextFieldPobEsDef.setText("");
            } 
            
            if (elemento.getFechaInstalacion() != null){
        		jTextFieldFechaInst.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
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
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.SANEAMIENTOAUTONOMO_CLAVE);
        	        	       	
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
    	Object object = AppContext.getApplicationContext().getBlackboard().get("saneamientoautonomo_panel");    	
    	if (object != null && object instanceof SaneamientoAutonomoEIEL){    		
    		SaneamientoAutonomoEIEL elemento = (SaneamientoAutonomoEIEL)object;
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
            
            
            if (elemento.getTipo() != null){
            	jComboBoxTipo.setSelectedPatron(elemento.getTipo());
        	}
        	else{
        		jComboBoxTipo.setSelectedIndex(0);
        	}
            
           if (elemento.getEstado() != null){            	
           	jComboBoxEst.setSelectedPatron(elemento.getEstado());
           }
           else{
           	jComboBoxEst.setSelectedIndex(0);
           } 
           
            if (elemento.getAdecuacion()!= null){            	
            	jComboBoxAdecuacion.setSelectedPatron(elemento.getAdecuacion());
            }
            else{
            	jComboBoxAdecuacion.setSelectedIndex(0);
            } 
            
            if (elemento.getViviendas()!= null){            	
            	jTextFieldVivien.setText(elemento.getViviendas().toString());
            }
            else{
            	jTextFieldVivien.setText("");
            }
            
            
            if (elemento.getPoblResidente() != null){            	
            	jTextFieldPobRe.setText(elemento.getPoblResidente().toString());
            }
            else{
            	jTextFieldPobRe.setText("");
            }
            
            if (elemento.getPoblEstacional() != null){            	
            	jTextFieldPobEs.setText(elemento.getPoblEstacional().toString());
            }
            else{
            	jTextFieldPobEs.setText("");
            } 
            
            if (elemento.getVivDeficitarias()!= null){            	
            	jTextFieldViDef.setText(elemento.getVivDeficitarias().toString());
            }
            else{
            	jTextFieldViDef.setText("");
            } 
            
            if (elemento.getPoblResDeficitaria()!= null){            	
            	jTextFieldPobReDef.setText(elemento.getPoblResDeficitaria().toString());
            }
            else{
            	jTextFieldPobReDef.setText("");
            } 
            
            if (elemento.getPoblEstDeficitaria() != null){            	
            	jTextFieldPobEsDef.setText(elemento.getPoblEstDeficitaria().toString());
            }
            else{
            	jTextFieldPobEsDef.setText("");
            } 
            
            if (elemento.getFechaInstalacion() != null){
        		jTextFieldFechaInst.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFechaInst.setDate(null);
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
    
    public SaneamientoAutonomoEIEL getSaneamientoAutonomo (SaneamientoAutonomoEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new SaneamientoAutonomoEIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            elemento.setClave(jTextFieldClave.getText());
            
            if (jComboBoxTipo.getSelectedPatron()!=null)
            	elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
            else elemento.setTipo("");
            if (jComboBoxEst.getSelectedPatron()!=null)
            	elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
            else elemento.setEstado("");
            if (jComboBoxAdecuacion.getSelectedPatron()!=null)
            	elemento.setAdecuacion((String) jComboBoxAdecuacion.getSelectedPatron());
            else elemento.setAdecuacion("");
            
            if (jTextFieldVivien.getText()!=null && !jTextFieldVivien.getText().equals("")){
            	elemento.setViviendas(new Integer(jTextFieldVivien.getText()));
            }
            else{
            	elemento.setViviendas(null);
            }
            
            if (jTextFieldPobRe.getText()!=null && !jTextFieldPobRe.getText().equals("")){
            	elemento.setPoblResidente(new Integer(jTextFieldPobRe.getText()));
            }
            else{
            	elemento.setPoblResidente(null);
            }
            
            if (jTextFieldPobEs.getText()!=null && !jTextFieldPobEs.getText().equals("")){
            	elemento.setPoblEstacional(new Integer(jTextFieldPobEs.getText()));
            }
            else{
            	elemento.setPoblEstacional(null);
            }
            
            if (jTextFieldViDef.getText()!=null && !jTextFieldViDef.getText().equals("")){
            	elemento.setVivDeficitarias(new Integer(jTextFieldViDef.getText()));
            }
            else{
            	elemento.setVivDeficitarias(null);
            }
            
            if (jTextFieldPobReDef.getText()!=null && !jTextFieldPobReDef.getText().equals("")){
            	elemento.setPoblResDeficitaria(new Integer(jTextFieldPobReDef.getText()));
            }
            else{
            	elemento.setPoblResDeficitaria(null);
            }
            
            if (jTextFieldPobEsDef.getText()!=null && !jTextFieldPobEsDef.getText().equals("")){
            	elemento.setPoblEstDeficitaria(new Integer(jTextFieldPobEsDef.getText()));
            }
            else{
            	elemento.setPoblEstDeficitaria(null);
            }
            
            
            if (jTextFieldFechaInst.getDate()!=null && !jTextFieldFechaInst.getDate().toString().equals("")){

               	elemento.setFechaInstalacion(
               			new java.sql.Date(
               					getJTextFieldFechaInst().getDate().getYear(),
               					getJTextFieldFechaInst().getDate().getMonth(),
               					getJTextFieldFechaInst().getDate().getDate()
                		)
               	
               	);
            }  
            else{
            	elemento.setFechaInstalacion(null);
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
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.saneamientoautonomo.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(SaneamientoAutonomoDialog.DIM_X,SaneamientoAutonomoDialog.DIM_Y);
        
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
    
    public JTextField getjTextFieldClave()
    {
        if (jTextFieldClave == null)
        {
            jTextFieldClave = new TextField(3);
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
       
    private ComboBoxEstructuras getJComboBoxTipo()
    { 
        if (jComboBoxTipo == null)
        {
            Estructuras.cargarEstructura("eiel_Tipos de saneamiento autónomo");
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
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEst = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEst.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEst;        
    }
        
    private ComboBoxEstructuras getJComboBoxAdecuacion()
    { 
        if (jComboBoxAdecuacion == null)
        {
            Estructuras.cargarEstructura("eiel_Adecuación");
            jComboBoxAdecuacion = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"),false);
        
            jComboBoxAdecuacion.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAdecuacion;        
    }
    
    private JTextField getjTextFieldVivien()
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
    
    private JTextField getjTextFieldPobRe()
    {
    	if (jTextFieldPobRe == null)
    	{
    		jTextFieldPobRe = new TextField(10);
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
    
    private JTextField getjTextFieldPobEs()
    {
    	if (jTextFieldPobEs == null)
    	{
    		jTextFieldPobEs = new TextField(10);
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
    
    private JTextField getjTextFieldViDef()
    {
    	if (jTextFieldViDef == null)
    	{
    		jTextFieldViDef = new TextField(10);
    		jTextFieldViDef.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldViDef, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldViDef;
    }
    
    private JTextField getjTextFieldPobReDef()
    {
    	if (jTextFieldPobReDef == null)
    	{
    		jTextFieldPobReDef = new TextField(10);
    		jTextFieldPobReDef.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobReDef, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobReDef;
    }
    
    private JTextField getjTextFieldPobEsDef()
    {
    	if (jTextFieldPobEsDef == null)
    	{
    		jTextFieldPobEsDef = new TextField(10);
    		jTextFieldPobEsDef.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEsDef, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEsDef;
    }
    
    private DateField getJTextFieldFechaInst()
    {
    	if (jTextFieldFechaInst == null)
    	{
    		jTextFieldFechaInst = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaInst.setDateFormatString("yyyy-MM-dd");
    		jTextFieldFechaInst.setEditable(true);
    		
    	}
    	return jTextFieldFechaInst;
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
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado;        
    }
    
    
    public SaneamientoAutonomoPanel(GridBagLayout layout)
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
            
            jPanelIdentificacion.add(getjTextFieldClave(), 
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
    
    private JPanel getJPanelDatosInformacion()
    {
        if (jPanelInformacion == null)
        {   
        	jPanelInformacion  = new JPanel(new GridBagLayout());
        	jPanelInformacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.informacion"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
        	jLabelTipo = new JLabel("", JLabel.CENTER); 
        	jLabelTipo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tipo")); 
            
        	jLabelEst = new JLabel("", JLabel.CENTER);
        	jLabelEst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.est"));
        	
        	jLabelAdecuacion = new JLabel("", JLabel.CENTER); 
        	jLabelAdecuacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.adecu")); 

        	jLabelVivien = new JLabel("", JLabel.CENTER);
        	jLabelVivien.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv"));
            
        	jLabelPobRe = new JLabel("", JLabel.CENTER);
        	jLabelPobRe.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobRe"));
            
        	jLabelPobEs = new JLabel("", JLabel.CENTER);
        	jLabelPobEs.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobEs"));
            
        	jLabelViDef = new JLabel("", JLabel.CENTER);
        	jLabelViDef.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viDef"));
            
        	jLabelPobReDef = new JLabel("", JLabel.CENTER);
        	jLabelPobReDef.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobReDef"));
            
        	jLabelPobEsDef = new JLabel("", JLabel.CENTER);
        	jLabelPobEsDef.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobEsDef"));
            
        	jLabelFechaInst = new JLabel("", JLabel.CENTER);
        	jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.instalacion"));
            
        	jLabelObservacion = new JLabel("", JLabel.CENTER);
        	jLabelObservacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
        	
            jPanelInformacion.add(jLabelTipo,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxTipo(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelAdecuacion,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxAdecuacion(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelVivien,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldVivien(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobRe,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldPobRe(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobEs,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldPobEs(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelViDef,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldViDef(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobReDef,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldPobReDef(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelPobEsDef,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldPobEsDef(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFechaInst,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldFechaInst(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelEst,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxEst(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObservacion,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getjTextFieldObservacion(), 
                    new GridBagConstraints(1, 5, 2, 1, 0.1, 0.1,
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

        return (jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) && 
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
	        	SaneamientoAutonomoEIEL element = operations.getPanelSaneamientoAutonomoEIEL(codprov, codmunic, entidad, nucleo);
	        	loadData(element);
	        	
	        	if (element != null){
	        		loadDataIdentificacion(element.getClave(), element.getCodINEProvincia(), element.getCodINEMunicipio(), element.getCodINEEntidad(), element.getCodINENucleo());
	        	}
			}
		}
	

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String entidad, String nucleo) {
		
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
