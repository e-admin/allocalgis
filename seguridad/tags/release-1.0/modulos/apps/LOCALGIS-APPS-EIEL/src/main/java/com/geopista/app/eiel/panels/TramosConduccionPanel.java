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
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.dialogs.TramosConduccionDialog;
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

public class TramosConduccionPanel extends JPanel implements FeatureExtendedPanel 
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    private JPanel jPanelRevision = null;
    private TramosConduccionEIEL tramoconduccion = null;
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelClave = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldOrden = null;
	private JLabel jLabelTitular = null;
	private JLabel jLabelGestor = null;
	private JLabel jLabelEstado = null;
	private JLabel jLabelSistTrans = null;
	private JLabel jLabelEstadoRevision = null;
	private JLabel jLabelMaterial = null;
	private JLabel jLabelFechaRevision = null;
	private JLabel jLabelObserv = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private ComboBoxEstructuras jComboBoxMaterial = null;
	private ComboBoxEstructuras jComboBoxSistTrans = null;
	private JTextField jTextFieldFechaRevision = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldClave = null;
	private JComboBox jComboBoxProvincia = null;
	private JComboBox jComboBoxMunicipio = null;
	private DateField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado_revision = null;
	private JLabel jLabelFechaInst = null;

    /**
     * This method initializes
     * 
     */
    public TramosConduccionPanel()
    {
        super();
        initialize();
    }
    
    public void loadData()
    {
    	Object object = AppContext.getApplicationContext().getBlackboard().get("conduccion");    	
    	if (object != null && object instanceof TramosConduccionEIEL){      		
    		TramosConduccionEIEL elemento = (TramosConduccionEIEL)object;
    		
    		this.tramoconduccion = elemento;
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
            
            if (elemento.getTramo_cn() != null){
        		jTextFieldOrden.setText(elemento.getTramo_cn());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            
            if (elemento.getTitular() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitular());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (elemento.getGestor() != null){
            	jComboBoxGestor.setSelectedPatron(elemento.getGestor());
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

            if (elemento.getFecha_revision() != null && elemento.getFecha_revision().equals( new java.util.Date()) ){
        		jTextFieldFechaRevision.setText(elemento.getFecha_revision().toString());
        	}
        	else{
        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);

                jTextFieldFechaRevision.setText(datetime);
        	}
            if (elemento.getFechaInstalacion() != null){
        		jTextFieldFecha.setDate(elemento.getFechaInstalacion());
        	}
        	else{
        		jTextFieldFecha.setDate(null);
        	}
            
   
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
        	
        	if (elemento.getSist_trans() != null){
        		jComboBoxSistTrans.setSelectedPatron(elemento.getSist_trans());
        	}
        	else{
        		jComboBoxSistTrans.setSelectedIndex(0);
        	}	
        	if (elemento.getEstado_revision() != null){
            	jComboBoxEstado_revision.setSelectedPatron(elemento.getEstado_revision().toString());
        	}
        	else{
        		jComboBoxEstado_revision.setSelectedIndex(0);
        	}     
        	
        }else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.CONDUCCION_CLAVE);
        	        	       	
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
    public void loadData(TramosConduccionEIEL panelTramosConduccionEIEL)
    {
    	if (panelTramosConduccionEIEL != null) {
            //Datos identificacion
        	if (panelTramosConduccionEIEL.getClave() != null){
        		jTextFieldClave.setText(panelTramosConduccionEIEL.getClave());
        	}
        	else{
        		jTextFieldClave.setText("");
        	}
            
        	if (panelTramosConduccionEIEL.getCodINEProvincia() != null) {
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(panelTramosConduccionEIEL
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}

			if (panelTramosConduccionEIEL.getCodINEMunicipio() != null) {
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(panelTramosConduccionEIEL
								.getCodINEMunicipio()));
			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}

            
            if (panelTramosConduccionEIEL.getTramo_cn() != null){
        		jTextFieldOrden.setText(panelTramosConduccionEIEL.getTramo_cn());
        	}
        	else{
        		jTextFieldOrden.setText("");
        	}
            
            if (panelTramosConduccionEIEL.getTitular() != null){
            	jComboBoxTitular.setSelectedPatron(panelTramosConduccionEIEL.getTitular());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (panelTramosConduccionEIEL.getGestor() != null){
            	jComboBoxGestor.setSelectedPatron(panelTramosConduccionEIEL.getGestor());
        	}
        	else{
        		jComboBoxGestor.setSelectedIndex(0);
        	}
            
            if (panelTramosConduccionEIEL.getEstado() != null){
            	jComboBoxEstado.setSelectedPatron(panelTramosConduccionEIEL.getEstado());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
            
            if (panelTramosConduccionEIEL.getMaterial() != null){
        		jComboBoxMaterial.setSelectedPatron(panelTramosConduccionEIEL.getMaterial());
        	}
        	else{
        		jComboBoxMaterial.setSelectedIndex(0);
        	}

            
            if (panelTramosConduccionEIEL.getFecha_revision()!= null && panelTramosConduccionEIEL.getFecha_revision().equals( new java.util.Date()) ){
        		jTextFieldFechaRevision.setText(panelTramosConduccionEIEL.getFecha_revision().toString());
        	}
        	else{
        	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);

                jTextFieldFechaRevision.setText(datetime);
        	}
            
   
            if (panelTramosConduccionEIEL.getObservaciones() != null){
        		jTextFieldObserv.setText(panelTramosConduccionEIEL.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}
        	
        	if (panelTramosConduccionEIEL.getSist_trans() != null){
        		jComboBoxSistTrans.setSelectedPatron(panelTramosConduccionEIEL.getSist_trans());
        	}
        	else{
        		jComboBoxSistTrans.setSelectedIndex(0);
        	}        
        	if (panelTramosConduccionEIEL.getEstado_revision() != null){
            	jComboBoxEstado_revision.setSelectedPatron(panelTramosConduccionEIEL.getEstado_revision().toString());
        	}
        	else{
        		jComboBoxEstado_revision.setSelectedIndex(0);
        	}
        	 if (panelTramosConduccionEIEL.getFechaInstalacion() != null){
           		jTextFieldFecha.setDate(panelTramosConduccionEIEL.getFechaInstalacion());
           	}
           	else{
           		jTextFieldFecha.setDate(null);
           	}      	 
        	 
        }else {
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	
        	jTextFieldClave.setText(ConstantesLocalGISEIEL.CONDUCCION_CLAVE);
        	        	       	
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
    
    public TramosConduccionEIEL getTramosConduccion (TramosConduccionEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new TramosConduccionEIEL();
            }
            
    		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		
            if (jTextFieldClave.getText()!=null){
            	elemento.setClave(jTextFieldClave.getText());
            }
            
            if (jTextFieldOrden.getText()!=null){
            	elemento.setTramo_cn(jTextFieldOrden.getText());
            }
            
            if (jComboBoxTitular.getSelectedPatron()!=null)
    			elemento.setTitular((String) jComboBoxTitular.getSelectedPatron());
    		else elemento.setTitular("");
            
    		if (jComboBoxGestor.getSelectedPatron()!=null)
    			elemento.setGestor((String) jComboBoxGestor.getSelectedPatron());
    		else elemento.setGestor("");
            
            if (jComboBoxEstado.getSelectedPatron()!=null)
                elemento.setEstado((String) jComboBoxEstado.getSelectedPatron());
    		else 
    			elemento.setEstado("");
            
            if (jComboBoxMaterial.getSelectedPatron()!=null)
                elemento.setMaterial((String) jComboBoxMaterial.getSelectedPatron());
    		else 
    			elemento.setMaterial("");
            
            if (jComboBoxSistTrans.getSelectedPatron()!=null)
                elemento.setSist_trans((String) jComboBoxSistTrans.getSelectedPatron());
    		else 
    			elemento.setSist_trans("");
            
            if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
    			String fechas=jTextFieldFechaRevision.getText();
    			String anio=fechas.substring(0,4);
    			String mes=fechas.substring(5,7);
    			String dia=fechas.substring(8,10);

    			java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
    			elemento.setFecha_revision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));

    		}  
    		else{
    			elemento.setFecha_revision(null);
    		}

    		if (jTextFieldFecha.getDate()!=null && !jTextFieldFecha.getDate().toString().equals("")){

    			java.sql.Date sqlDate = new java.sql.Date(
    					getJTextFieldFechaInstalacion().getDate().getYear(),
    					getJTextFieldFechaInstalacion().getDate().getMonth(),
    					getJTextFieldFechaInstalacion().getDate().getDate()
    			);

    			elemento.setFechaInstalacion(sqlDate);
    		}  
    		else{
    			elemento.setFechaInstalacion(null);
    		}
            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            if (jComboBoxEstado_revision.getSelectedPatron()!=null){
          		elemento.setEstado_revision(Integer.parseInt(jComboBoxEstado_revision.getSelectedPatron()));
     	    }
            
            if (elemento.getFechaInstalacion() != null){
           		jTextFieldFecha.setDate(elemento.getFechaInstalacion());
           	}
           	else{
           		jTextFieldFecha.setDate(null);
           	}
        }
        
        return elemento;
    }
    public TramosConduccionEIEL getTramosConduccionData ()
    {
    	TramosConduccionEIEL elemento = new TramosConduccionEIEL();
            
		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		
            if (jTextFieldClave.getText()!=null){
            	elemento.setClave(jTextFieldClave.getText());
            }
            
            if (jTextFieldOrden.getText()!=null){
            	elemento.setTramo_cn(jTextFieldOrden.getText());
            }
            
            if (jComboBoxTitular.getSelectedPatron()!=null)
    			elemento.setTitular((String) jComboBoxTitular.getSelectedPatron());
    		else elemento.setTitular("");
            
    		if (jComboBoxGestor.getSelectedPatron()!=null)
    			elemento.setGestor((String) jComboBoxGestor.getSelectedPatron());
    		else elemento.setGestor("");
            
            if (jComboBoxEstado.getSelectedPatron()!=null)
                elemento.setEstado((String) jComboBoxEstado.getSelectedPatron());
    		else 
    			elemento.setEstado("");
            
            if (jComboBoxMaterial.getSelectedPatron()!=null)
                elemento.setMaterial((String) jComboBoxMaterial.getSelectedPatron());
    		else 
    			elemento.setMaterial("");
            
            if (jComboBoxSistTrans.getSelectedPatron()!=null)
                elemento.setSist_trans((String) jComboBoxSistTrans.getSelectedPatron());
    		else 
    			elemento.setSist_trans("");
            
            if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
    			String fechas=jTextFieldFechaRevision.getText();
    			String anio=fechas.substring(0,4);
    			String mes=fechas.substring(5,7);
    			String dia=fechas.substring(8,10);

    			java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
    			elemento.setFecha_revision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));

    		}  
    		else{
    			elemento.setFecha_revision(null);
    		}

    		if (jTextFieldFecha.getDate()!=null && !jTextFieldFecha.getDate().toString().equals("")){

    			java.sql.Date sqlDate = new java.sql.Date(
    					getJTextFieldFechaInstalacion().getDate().getYear(),
    					getJTextFieldFechaInstalacion().getDate().getMonth(),
    					getJTextFieldFechaInstalacion().getDate().getDate()
    			);

    			elemento.setFechaInstalacion(sqlDate);
    		}  
    		else{
    			elemento.setFechaInstalacion(null);
    		}
            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            
            if (jComboBoxEstado_revision.getSelectedPatron()!=null){
          		elemento.setEstado_revision(Integer.parseInt(jComboBoxEstado_revision.getSelectedPatron()));
     	    }
        
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(TramosConduccionDialog.DIM_X,TramosConduccionDialog.DIM_Y);
        
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
    
    public JTextField getJTextFieldClave()
    {
        if (jTextFieldClave  == null)
        {
            jTextFieldClave = new TextField(3);
            jTextFieldClave.setEnabled(false);

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
    
    /**
     * This method initializes jTextFieldPlanta   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JTextField getJTextFieldOrden()
    {
    	if (jTextFieldOrden   == null)
    	{
    		jTextFieldOrden = new TextField(3);
    		jTextFieldOrden.setEnabled(false);

    	}
    	return jTextFieldOrden;
    }
    

    private ComboBoxEstructuras getJComboBoxTitular()
    {
    	 if (jComboBoxTitular == null)
         {
             Estructuras.cargarEstructura("eiel_Titularidad");
             jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                     null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
         
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
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxGestor;        
    }
    
    private ComboBoxEstructuras getJComboBoxMaterial()
    { 
        if (jComboBoxMaterial == null)
        {
            Estructuras.cargarEstructura("eiel_material");
            jComboBoxMaterial = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxMaterial.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxMaterial;        
    }
        
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv  == null)
    	{
    		jTextFieldObserv  = new TextField(100);
    		
    	}
    	return jTextFieldObserv;
    }
    
    private ComboBoxEstructuras getJComboBoxSist_trans()
    { 
        if (jComboBoxSistTrans == null)
        {
            Estructuras.cargarEstructura("eiel_sist_trans");
            jComboBoxSistTrans = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxSistTrans.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxSistTrans;        
    }
    
    private JTextField getJTextFieldFechaRevision()
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
    
    private DateField getJTextFieldFechaInstalacion()
    {
    	if (jTextFieldFecha  == null){
    		jTextFieldFecha  = new DateField((java.util.Date) null, 0);    		
    		jTextFieldFecha.setDateFormatString("yyyy-MM-dd");
    	}
    	return jTextFieldFecha;
    }
    
    private ComboBoxEstructuras getJComboBoxEstado_revision()
    { 
        if (jComboBoxEstado_revision == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            jComboBoxEstado_revision = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstado_revision.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado_revision;        
    }
    private ComboBoxEstructuras getJComboBoxEstado()
    { 
        if (jComboBoxEstado == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            jComboBoxEstado = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado;        
    }
        
    
    public TramosConduccionPanel(GridBagLayout layout)
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
        	
            jLabelTitular = new JLabel("", JLabel.CENTER); 
            jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular")); 

            jLabelGestor = new JLabel("", JLabel.CENTER);
            jLabelGestor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestor"));
            
            jLabelEstado = new JLabel("", JLabel.CENTER);
            jLabelEstado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado_conserv"));
            
            jLabelMaterial = new JLabel("", JLabel.CENTER);
            jLabelMaterial.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.material"));
            
            jLabelSistTrans = new JLabel("", JLabel.CENTER);
            jLabelSistTrans.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sist_trans"));
            
            
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
            
            jPanelInformacion.add(jLabelSistTrans,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxSist_trans(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 3, 2, 1, 0.1, 0.1,
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
               
            jLabelFechaInst = new JLabel("", JLabel.CENTER);
            jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.instalacion"));
            

            jPanelRevision.add(jLabelFechaRevision,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(getJTextFieldFechaRevision(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(jLabelFechaInst,
                    new GridBagConstraints(2,0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(getJTextFieldFechaInstalacion(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevision.add(jLabelEstadoRevision, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelRevision.add(getJComboBoxEstado_revision(), 
                    new GridBagConstraints(1, 1, 2, 1, 0.1, 0.1,
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
	        	
	        	String tramo_cn = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("tramo_cn"))!=null){
	        		tramo_cn=(feature.getAttribute(esquema.getAttributeByColumn("tramo_cn"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelTramoConduccionEIEL(clave, codprov, codmunic, tramo_cn));
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, tramo_cn);       	
			}
		}
	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String tramo_cn) {
		
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

        if (tramo_cn!= null){
    		jTextFieldOrden.setText(tramo_cn);
    	}
    	else{
    		jTextFieldOrden.setText("");
    	}
		
	}
	
	public void enter() {
        loadData();
        loadDataIdentificacion();
	}

	public void exit() {
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
