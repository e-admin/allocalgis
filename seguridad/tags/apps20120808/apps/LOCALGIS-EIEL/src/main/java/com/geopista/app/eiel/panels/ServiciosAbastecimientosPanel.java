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
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.dialogs.ServiciosAbastecimientosDialog;
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

public class ServiciosAbastecimientosPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelCodEntidad = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelCodPoblamiento = null;
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxEntidad = null;
    private JComboBox jComboBoxMunicipio = null;
	private JComboBox jComboBoxNucleo = null;
	private JLabel jLabelVivConConex = null;
	private JLabel jLabelVivSinConex = null;
	private JLabel jLabelConsumoIn = null;
	private JLabel jLabelConsumoVe = null;
	private JLabel jLabelVivExcPresion = null;
	private JLabel jLabelVivDefPresion = null;
	private JLabel jLabelPerdAgua = null;
	private JLabel jLabelCalidadServ = null;
	private JLabel jLabelLongDef = null;
	private JLabel jLabelVivDefic = null;
	private JLabel jLabelPoblResDef = null;
	private JLabel jLabelPoblEstDef = null;
	private JLabel jLabelObserv = null;
	private JLabel jLabelFechaRevi = null;
	private JLabel jLabelEstado = null;
	private JTextField jTextFieldVivConConex = null;
	private JTextField jTextFieldVivSinConex = null;
	private JTextField jTextFieldConsumoIn = null;
	private JTextField jTextFieldConsumoVe = null;
	private JTextField jTextFieldVivExcP = null;
	private JTextField jTextFieldVivDefP = null;
	private JTextField jTextFieldPerdAgua = null;
	private ComboBoxEstructuras jComboBoxCalidad = null;
	private JTextField jTextFieldLongDef = null;
	private JTextField jTextFieldVivDef = null;
	private JTextField jTextFieldPoblRes = null;
	private JTextField jTextFieldPoblEst = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldFechaRevi = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	
    public ServiciosAbastecimientosPanel(){
        super();
        initialize();
    }
  
    public ServiciosAbastecimientosPanel(ServiciosAbastecimientosEIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    public void loadData(ServiciosAbastecimientosEIEL elemento){
        if (elemento!=null){
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
            
            if (elemento.getViviendasConectadas() != null){
        		jTextFieldVivConConex.setText(elemento.getViviendasConectadas().toString());
        	}
        	else{
        		jTextFieldVivConConex.setText("");
        	}
            
            if (elemento.getViviendasNoConectadas() != null){
        		jTextFieldVivSinConex.setText(elemento.getViviendasNoConectadas().toString());
        	}
        	else{
        		jTextFieldVivSinConex.setText("");
        	}
            
            if (elemento.getConsumoInvierno() != null){
        		jTextFieldConsumoIn.setText(elemento.getConsumoInvierno().toString());
        	}
        	else{
        		jTextFieldConsumoIn.setText("");
        	}
            
            if (elemento.getConsumoVerano() != null){
        		jTextFieldConsumoVe.setText(elemento.getConsumoVerano().toString());
        	}
        	else{
        		jTextFieldConsumoVe.setText("");
        	}
            
            if (elemento.getViviendasExcesoPresion() != null){
        		jTextFieldVivExcP.setText(elemento.getViviendasExcesoPresion().toString());
        	}
        	else{
        		jTextFieldVivExcP.setText("");
        	}
            
            if (elemento.getViviendasDeficitPresion() != null){
        		jTextFieldVivDefP.setText(elemento.getViviendasDeficitPresion().toString());
        	}
        	else{
        		jTextFieldVivDefP.setText("");
        	}
            
            if (elemento.getPerdidasAgua() != null){
        		jTextFieldPerdAgua.setText(elemento.getPerdidasAgua().toString());
        	}
        	else{
        		jTextFieldPerdAgua.setText("");
        	}
            
            if (elemento.getCalidadServicio() != null){
            	jComboBoxCalidad.setSelectedPatron(elemento.getCalidadServicio().toString());
        	}
        	else{
        		jComboBoxCalidad.setSelectedIndex(0);
        	}
            
            if (elemento.getLogitudDeficitaria() != null){
        		jTextFieldLongDef.setText(elemento.getLogitudDeficitaria().toString());
        	}
        	else{
        		jTextFieldLongDef.setText("");
        	}
            
            if (elemento.getViviendasDeficitarias() != null){
        		jTextFieldVivDef.setText(elemento.getViviendasDeficitarias().toString());
        	}
        	else{
        		jTextFieldVivDef.setText("");
        	}
            
            if (elemento.getPoblacionResidenteDeficitaria() != null){
            	jTextFieldPoblRes.setText(elemento.getPoblacionResidenteDeficitaria().toString());
        	}
        	else{
        		jTextFieldPoblRes.setText("");
        	}
            
            if (elemento.getPoblacionEstacionalDeficitaria() != null){
            	jTextFieldPoblEst.setText(elemento.getPoblacionEstacionalDeficitaria().toString());
        	}
        	else{
        		jTextFieldPoblEst.setText("");
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
                        
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRevi.setText(elemento.getFechaRevision().toString());
        	}
        	else{
        	    
        		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                 java.util.Date date = new java.util.Date();
                 String datetime = dateFormat.format(date);

         		jTextFieldFechaRevi.setText(datetime);
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
        	// (new EdicionOperations()).obtenerEntidades("");
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
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("serviciosabastecimiento_panel");    	
    	if (object != null && object instanceof ServiciosAbastecimientosEIEL){    		
    		ServiciosAbastecimientosEIEL elemento = (ServiciosAbastecimientosEIEL)object;
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
            
            if (elemento.getViviendasConectadas() != null){
        		jTextFieldVivConConex.setText(elemento.getViviendasConectadas().toString());
        	}
        	else{
        		jTextFieldVivConConex.setText("");
        	}
            
            if (elemento.getViviendasNoConectadas() != null){
        		jTextFieldVivSinConex.setText(elemento.getViviendasNoConectadas().toString());
        	}
        	else{
        		jTextFieldVivSinConex.setText("");
        	}
            
            if (elemento.getConsumoInvierno() != null){
        		jTextFieldConsumoIn.setText(elemento.getConsumoInvierno().toString());
        	}
        	else{
        		jTextFieldConsumoIn.setText("");
        	}
            
            if (elemento.getConsumoVerano() != null){
        		jTextFieldConsumoVe.setText(elemento.getConsumoVerano().toString());
        	}
        	else{
        		jTextFieldConsumoVe.setText("");
        	}
            
            if (elemento.getViviendasExcesoPresion() != null){
        		jTextFieldVivExcP.setText(elemento.getViviendasExcesoPresion().toString());
        	}
        	else{
        		jTextFieldVivExcP.setText("");
        	}
            
            if (elemento.getViviendasDeficitPresion() != null){
        		jTextFieldVivDefP.setText(elemento.getViviendasDeficitPresion().toString());
        	}
        	else{
        		jTextFieldVivDefP.setText("");
        	}
            
            if (elemento.getPerdidasAgua() != null){
        		jTextFieldPerdAgua.setText(elemento.getPerdidasAgua().toString());
        	}
        	else{
        		jTextFieldPerdAgua.setText("");
        	}
            
            if (elemento.getCalidadServicio() != null){
            	jComboBoxCalidad.setSelectedPatron(elemento.getCalidadServicio().toString());
        	}
        	else{
        		jComboBoxCalidad.setSelectedIndex(0);
        	}
            
            if (elemento.getLogitudDeficitaria() != null){
        		jTextFieldLongDef.setText(elemento.getLogitudDeficitaria().toString());
        	}
        	else{
        		jTextFieldLongDef.setText("");
        	}
            
            if (elemento.getViviendasDeficitarias() != null){
        		jTextFieldVivDef.setText(elemento.getViviendasDeficitarias().toString());
        	}
        	else{
        		jTextFieldVivDef.setText("");
        	}
            
            if (elemento.getPoblacionResidenteDeficitaria() != null){
            	jTextFieldPoblRes.setText(elemento.getPoblacionResidenteDeficitaria().toString());
        	}
        	else{
        		jTextFieldPoblRes.setText("");
        	}
            
            if (elemento.getPoblacionEstacionalDeficitaria() != null){
            	jTextFieldPoblEst.setText(elemento.getPoblacionEstacionalDeficitaria().toString());
        	}
        	else{
        		jTextFieldPoblEst.setText("");
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
                        
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRevi.setText(elemento.getFechaRevision().toString());
        	}
        	else{      	    
        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
                jTextFieldFechaRevi.setText(datetime);
        	}
            
            if (elemento.getEstadoRevision() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
            
        }	
    }
    
    
    public ServiciosAbastecimientosEIEL getServiciosAbastecimientos (ServiciosAbastecimientosEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new ServiciosAbastecimientosEIEL();
            }
            /* Claves: COMBOBOX  y JTEXT */
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            
            /* JTEXT - Integer */
            if (jTextFieldVivConConex.getText()!=null && !jTextFieldVivConConex.getText().equals("")) {
            	elemento.setViviendasConectadas(new Integer(jTextFieldVivConConex.getText()));
            } else if (jTextFieldVivConConex.getText().equals("")){
            	elemento.setViviendasConectadas(new Integer(0));
            }
            if (jTextFieldVivSinConex.getText()!=null && !jTextFieldVivSinConex.getText().equals("")) {
            	elemento.setViviendasNoConectadas(new Integer(jTextFieldVivSinConex.getText()));
            } else if (jTextFieldVivSinConex.getText().equals("")){
            	elemento.setViviendasNoConectadas(new Integer(0));
            }
            if (jTextFieldConsumoIn.getText()!=null && !jTextFieldConsumoIn.getText().equals("")){
            	elemento.setConsumoInvierno(new Integer(jTextFieldConsumoIn.getText()));
            } else if (jTextFieldConsumoIn.getText().equals("")){
            	elemento.setConsumoInvierno(new Integer(0));
            }
            if (jTextFieldConsumoVe.getText()!=null && !jTextFieldConsumoVe.getText().equals("")){
            	elemento.setConsumoVerano(new Integer(jTextFieldConsumoVe.getText()));
            } else if (jTextFieldConsumoVe.getText().equals("")){
            	elemento.setConsumoVerano(new Integer(0));
            }
            if (jTextFieldVivExcP.getText()!=null && !jTextFieldVivExcP.getText().equals("")){
            	elemento.setViviendasExcesoPresion(new Integer(jTextFieldVivExcP.getText()));
            } else if (jTextFieldVivExcP.getText().equals("")){
            	elemento.setViviendasExcesoPresion(new Integer(0));
            }
            if (jTextFieldVivDefP.getText()!=null && !jTextFieldVivDefP.getText().equals("")){
            	elemento.setViviendasDeficitPresion(new Integer(jTextFieldVivDefP.getText()));
            } else if (jTextFieldVivDefP.getText().equals("")){
            	elemento.setViviendasDeficitPresion(new Integer(0));
            }
            if (jTextFieldPerdAgua.getText()!=null && !jTextFieldPerdAgua.getText().equals("")){
            	elemento.setPerdidasAgua(new Integer(jTextFieldPerdAgua.getText()));
            } else if (jTextFieldPerdAgua.getText().equals("")){
            	elemento.setPerdidasAgua(new Integer(0));
            }
            if (jTextFieldLongDef.getText()!=null && !jTextFieldLongDef.getText().equals("")){
            	elemento.setLongitudDeficitaria(new Integer(jTextFieldLongDef.getText()));
            } else if (jTextFieldLongDef.getText().equals("")){
            	elemento.setLongitudDeficitaria(new Integer(0));
            }
            if (jTextFieldVivDef.getText()!=null && !jTextFieldVivDef.getText().equals("")){
            	elemento.setViviendasDeficitarias(new Integer(jTextFieldVivDef.getText()));
            } else if (jTextFieldVivDef.getText().equals("")){
            	elemento.setViviendasDeficitarias(new Integer(0));
            }
            if (jTextFieldPoblEst.getText()!=null && !jTextFieldPoblEst.getText().equals("")){
            	elemento.setPoblacionEstacionalDeficitaria(new Integer(jTextFieldPoblEst.getText()));
            } else if (jTextFieldPoblEst.getText().equals("")){
            	elemento.setPoblacionEstacionalDeficitaria(new Integer(0));
            }
            if (jTextFieldPoblRes.getText()!=null && !jTextFieldPoblRes.getText().equals("")){
            	elemento.setPoblacionResidenteDeficitaria(new Integer(jTextFieldPoblRes.getText()));
            } else if (jTextFieldPoblRes.getText().equals("")){
            	elemento.setPoblacionResidenteDeficitaria(new Integer(0));
            }
            if (jComboBoxEstado.getSelectedPatron()!=null)
            	elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
            /* JTEXT - String */
            if (jComboBoxCalidad.getSelectedPatron()!=null)
            	elemento.setCalidadServicio((String) jComboBoxCalidad.getSelectedPatron());
            else elemento.setCalidadServicio("");
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            /* JTEXT - Date */
            if (jTextFieldFechaRevi.getText()!=null && !jTextFieldFechaRevi.getText().equals("")){
            	String fechas=jTextFieldFechaRevi.getText();
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
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.serviciosabastecimiento.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(ServiciosAbastecimientosDialog.DIM_X,ServiciosAbastecimientosDialog.DIM_Y);
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
    
/* Metodos que devuelven CAMPOS CLAVE */
    
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
    	 if (jComboBoxNucleo  == null)
         {
         	jComboBoxNucleo = new JComboBox();
         	jComboBoxNucleo.setRenderer(new UbicacionListCellRenderer());
         }
         return jComboBoxNucleo;
	}
    
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
   
    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private JTextField getjTextFieldVivConConex(){
    	if (jTextFieldVivConConex == null){
    		jTextFieldVivConConex = new TextField(10);
    		jTextFieldVivConConex.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivConConex, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivConConex;
    }
    
    private JTextField getjTextFieldVivSinConex(){
    	if (jTextFieldVivSinConex == null){
    		jTextFieldVivSinConex = new TextField(10);
    		jTextFieldVivSinConex.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivSinConex,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivSinConex;
    }
    
    private JTextField getjTextFieldConsumoIn(){
    	if (jTextFieldConsumoIn == null){
    		jTextFieldConsumoIn = new TextField(10);
    		jTextFieldConsumoIn.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldConsumoIn,10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldConsumoIn;
    }
    
    private JTextField getjTextFieldConsumoVe(){
    	if (jTextFieldConsumoVe == null){
    		jTextFieldConsumoVe = new TextField(10);
    		jTextFieldConsumoVe.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldConsumoVe, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldConsumoVe;
    }
    
    private JTextField getjTextFieldVivExcP(){
    	if (jTextFieldVivExcP == null){
    		jTextFieldVivExcP = new TextField(10);
    		jTextFieldVivExcP.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivExcP, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivExcP;
    }
    
    private JTextField getjTextFieldVivDefP(){
    	if (jTextFieldVivDefP == null){
    		jTextFieldVivDefP = new TextField(10);
    		jTextFieldVivDefP.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDefP, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDefP;
    }
    
    private JTextField getjTextFieldPerdAgua(){
    	if (jTextFieldPerdAgua == null){
    		jTextFieldPerdAgua = new TextField(10);
    		jTextFieldPerdAgua.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPerdAgua, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPerdAgua;
    }
    
    
    private ComboBoxEstructuras getJComboBoxCalidad()
    { 
        if (jComboBoxCalidad == null)
        {
            Estructuras.cargarEstructura("eiel_Calidad del servicio");
            jComboBoxCalidad = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCalidad.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCalidad;        
    }
    
    
    private JTextField getjTextFieldLongDef(){
    	if (jTextFieldLongDef == null){
    		jTextFieldLongDef = new TextField(10);
    		jTextFieldLongDef.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLongDef, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLongDef;
    }
    
    private JTextField getjTextFieldVivDef(){
    	if (jTextFieldVivDef == null){
    		jTextFieldVivDef = new TextField(10);
    		jTextFieldVivDef.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDef, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDef;
    }
    
    private JTextField getjTextFieldPoblRes(){
    	if (jTextFieldPoblRes == null){
    		jTextFieldPoblRes = new TextField(10);
    		jTextFieldPoblRes.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPoblRes, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPoblRes;
    }
    
    private JTextField getjTextFieldPoblEst(){
    	if (jTextFieldPoblEst == null){
    		jTextFieldPoblEst = new TextField(10);
    		jTextFieldPoblEst.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPoblEst, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPoblEst;
    }
    
    private JTextField getjTextFieldObserv(){
    	if (jTextFieldObserv == null){
    		jTextFieldObserv = new TextField(50);
    		jTextFieldObserv.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObserv;
    }
    
    private JTextField getjTextFieldFechaRevi(){
    	if (jTextFieldFechaRevi == null){
    		jTextFieldFechaRevi = new TextField();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);
            
            jTextFieldFechaRevi.setText(datetime);
    	}
    	return jTextFieldFechaRevi;
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
    
    
    public ServiciosAbastecimientosPanel(GridBagLayout layout){
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
            jLabelCodEntidad = new JLabel("", JLabel.CENTER); 
            jLabelCodEntidad.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codentidad"))); 
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 
            jLabelCodPoblamiento  = new JLabel("", JLabel.CENTER);
            jLabelCodPoblamiento.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codpoblamiento")));
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
            jLabelVivConConex = new JLabel("", JLabel.CENTER); 
            jLabelVivConConex.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv_con_conex")); 
            jLabelVivSinConex = new JLabel("", JLabel.CENTER); 
            jLabelVivSinConex.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv_sin_conex")); 
            jLabelConsumoIn = new JLabel("", JLabel.CENTER); 
            jLabelConsumoIn.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.consumo_inv")); 
            jLabelConsumoVe = new JLabel("", JLabel.CENTER);
            jLabelConsumoVe.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.consumo_verano"));
            jLabelVivExcPresion = new JLabel("", JLabel.CENTER);
            jLabelVivExcPresion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv_exc_presion"));
            jLabelVivDefPresion = new JLabel("", JLabel.CENTER);
            jLabelVivDefPresion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv_def_presion"));
            jLabelPerdAgua = new JLabel("", JLabel.CENTER);
            jLabelPerdAgua.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.perdidas_agua"));
            jLabelCalidadServ = new JLabel("", JLabel.CENTER);
            jLabelCalidadServ.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.calidad_serv"));
            jLabelLongDef = new JLabel("", JLabel.CENTER);
            jLabelLongDef.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.long_def"));
            jLabelVivDefic = new JLabel("", JLabel.CENTER);
            jLabelVivDefic.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv_def"));
            jLabelPoblResDef = new JLabel("", JLabel.CENTER);
            jLabelPoblResDef.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobl_res_def"));
            jLabelPoblEstDef = new JLabel("", JLabel.CENTER);
            jLabelPoblEstDef.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pobl_est_def"));
            jLabelObserv = new JLabel("", JLabel.CENTER); 
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ")); 
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelVivConConex,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivConConex(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivSinConex,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivSinConex(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelConsumoIn,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldConsumoIn(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelConsumoVe,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldConsumoVe(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivExcPresion,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivExcP(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivDefPresion,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivDefP(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPerdAgua,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPerdAgua(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCalidadServ,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJComboBoxCalidad(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelLongDef,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldLongDef(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivDefic,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivDef(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPoblResDef,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPoblRes(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 60, 0));
            jPanelInformacion.add(jLabelPoblEstDef,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPoblEst(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(getjTextFieldObserv(), 
                    new GridBagConstraints(1, 6, 2, 2, 0.1, 0.1,
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
            jPanelRevision.add(getjTextFieldFechaRevi(), 
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
                            new Insets(5, 5, 5, 5), 90, 0));
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
	        	loadData(operations.getPanelServiciosAbastecimientoEIEL(codprov, codmunic, entidad, nucleo));

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
