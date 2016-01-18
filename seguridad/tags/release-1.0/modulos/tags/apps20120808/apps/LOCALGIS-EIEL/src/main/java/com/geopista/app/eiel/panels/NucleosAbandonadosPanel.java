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
import java.util.Collection;
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
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.dialogs.NucleosAbandonadosDialog;
import com.geopista.app.eiel.models.NucleosAbandonadosEIELTableModel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class NucleosAbandonadosPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    private JPanel jPanelListaNucleos = null;
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelCodMunic = null;
	private JLabel jLabelEntSing = null;
	private JLabel jLabelNucleo = null;
	
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JComboBox jComboBoxEntidad = null;
	private JComboBox jComboBoxNucleo = null;
	
	private JLabel jLabelAbandono = null;
	private JLabel jLabelCausaAband = null;
	private JLabel jLabelTitularAband = null;
	private JLabel jLabelRehab= null;
	private JLabel jLabelAcceso = null;
	private JLabel jLabelAgua = null;
	private JLabel jLabelElec = null;
	private JLabel jLabelObserv = null;
	
	private JTextField jTextFieldAbandono = null;
	private ComboBoxEstructuras jComboBoxCausaAband = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxRehab = null;
	private ComboBoxEstructuras jComboBoxAcceso = null;
	private ComboBoxEstructuras jComboBoxAgua = null;
	private ComboBoxEstructuras jComboBoxElec = null;
	
	private JTextField jTextFieldObserv = null;	
	private JPanel jPanelRevision = null;
	
	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;
	
	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	
	private JButton annidair = null;
	private JButton eliminar = null;
	private JButton modificar = null;
	
	public static boolean CARGARLISTANUCLEOS = false;

	
    /**
     * This method initializes
     * 
     */
    public NucleosAbandonadosPanel()
    {
        super();
        initialize();
    }
    
    public NucleosAbandonadosPanel(NucleosAbandonadosEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(NucleosAbandonadosEIEL elemento)
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
            
            if (elemento.getAnnoAbandono()!= null){
        		jTextFieldAbandono.setText(elemento.getAnnoAbandono());
        	}
        	else{
        		jTextFieldAbandono.setText("");
        	}
            
            if (elemento.getCausaAbandono() != null){
        		jComboBoxCausaAband.setSelectedPatron(elemento.getCausaAbandono());
        	}
        	else{
        		jComboBoxCausaAband.setSelectedIndex(0);
        	}
            
            if (elemento.getTitularidad() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitularidad());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (elemento.getRehabilitacion() != null){
        		jComboBoxRehab.setSelectedPatron(elemento.getRehabilitacion());
        	}
        	else{
        		jComboBoxRehab.setSelectedIndex(0);
        	}
            
            if (elemento.getAcceso() != null){
        		jComboBoxAcceso.setSelectedPatron(elemento.getAcceso());
        	}
        	else{
        		jComboBoxAcceso.setSelectedIndex(0);
        	}
            
            if (elemento.getServicioAgua() != null){
        		jComboBoxAgua.setSelectedPatron(elemento.getServicioAgua());
        	}
        	else{
        		jComboBoxAgua.setSelectedIndex(0);
        	}
            
            if (elemento.getServicioElectricidad()!= null){
        		jComboBoxElec.setSelectedPatron(elemento.getServicioElectricidad());
        	}
        	else{
        		jComboBoxElec.setSelectedIndex(0);
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
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("nucleosabandonados_panel");    	
    	if (object != null && object instanceof NucleosAbandonadosEIEL){    		
    		NucleosAbandonadosEIEL elemento = (NucleosAbandonadosEIEL)object;
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
            
            if (elemento.getAnnoAbandono()!= null){
        		jTextFieldAbandono.setText(elemento.getAnnoAbandono());
        	}
        	else{
        		jTextFieldAbandono.setText("");
        	}
            
            if (elemento.getCausaAbandono() != null){
        		jComboBoxCausaAband.setSelectedPatron(elemento.getCausaAbandono());
        	}
        	else{
        		jComboBoxCausaAband.setSelectedIndex(0);
        	}
            
            if (elemento.getTitularidad() != null){
            	jComboBoxTitular.setSelectedPatron(elemento.getTitularidad());
        	}
        	else{
        		jComboBoxTitular.setSelectedIndex(0);
        	}
            
            if (elemento.getRehabilitacion() != null){
        		jComboBoxRehab.setSelectedPatron(elemento.getRehabilitacion());
        	}
        	else{
        		jComboBoxRehab.setSelectedIndex(0);
        	}
            
            if (elemento.getAcceso() != null){
        		jComboBoxAcceso.setSelectedPatron(elemento.getAcceso());
        	}
        	else{
        		jComboBoxAcceso.setSelectedIndex(0);
        	}
            
            if (elemento.getServicioAgua() != null){
        		jComboBoxAgua.setSelectedPatron(elemento.getServicioAgua());
        	}
        	else{
        		jComboBoxAgua.setSelectedIndex(0);
        	}
            
            if (elemento.getServicioElectricidad()!= null){
        		jComboBoxElec.setSelectedPatron(elemento.getServicioElectricidad());
        	}
        	else{
        		jComboBoxElec.setSelectedIndex(0);
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
    
    
    public ArrayList getListaNucleosAbandonados ()
    {

        return ((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getData();
    }
    
    public NucleosAbandonadosEIEL getNucleoAbandonado (NucleosAbandonadosEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new NucleosAbandonadosEIEL();
            }
            
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINEPoblamiento(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
             
            if (jTextFieldAbandono.getText()!=null){
            	elemento.setAnnoAbandono(jTextFieldAbandono.getText());
            }            

            if (jComboBoxCausaAband.getSelectedPatron()!=null)
	            elemento.setCausaAbandono((String) jComboBoxCausaAband.getSelectedPatron());
            else elemento.setCausaAbandono("");
            if (jComboBoxTitular.getSelectedPatron()!=null)
            	elemento.setTitularidad((String) jComboBoxTitular.getSelectedPatron());
            else elemento.setTitularidad("");
            if (jComboBoxRehab.getSelectedPatron()!=null)
            	elemento.setRehabilitacion((String) jComboBoxRehab.getSelectedPatron());
            else elemento.setRehabilitacion("");
            if (jComboBoxAcceso.getSelectedPatron()!=null)
            	elemento.setAcceso((String) jComboBoxAcceso.getSelectedPatron());
            else elemento.setAcceso("");
            if (jComboBoxAgua.getSelectedPatron()!=null)
            	elemento.setServicioAgua((String) jComboBoxAgua.getSelectedPatron());
            else elemento.setServicioAgua("");
            if (jComboBoxElec.getSelectedPatron()!=null)
            	elemento.setServicioElectricidad((String) jComboBoxElec.getSelectedPatron());
            else elemento.setServicioElectricidad("");

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
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.nucleosabandonados.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(NucleosAbandonadosDialog.DIM_X,NucleosAbandonadosDialog.DIM_Y);
        
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        if (this.CARGARLISTANUCLEOS ){
        	this.add(getJPanelListaNucleos(),  new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
        			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        			new Insets(0, 5, 0, 5), 0, 0));
        	this.CARGARLISTANUCLEOS = false;
        }

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
    
    private JComboBox getJComboBoxNucleo()
    {
        if (jComboBoxNucleo  == null)
        {
        	jComboBoxNucleo = new JComboBox();
        	jComboBoxNucleo.setRenderer(new UbicacionListCellRenderer());
//        	jComboBoxNucleo.setEditable(true);
        }
        return jComboBoxNucleo;
    }
    
    private JTextField getJTextFieldAbandono()
    {
    	if (jTextFieldAbandono == null)
    	{
    		jTextFieldAbandono = new TextField(4);
    		jTextFieldAbandono.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldAbandono, 4, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldAbandono;
    }
    
    private ComboBoxEstructuras getJComboBoxCausaAband()
    { 
        if (jComboBoxCausaAband == null)
        {
            Estructuras.cargarEstructura("eiel_Causa de Abandono");
            jComboBoxCausaAband = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxCausaAband.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCausaAband;        
    }
    
    private ComboBoxEstructuras getJComboBoxTitular()
    { 
        if (jComboBoxTitular == null)
        {
            Estructuras.cargarEstructura("eiel_Titularidad Núcleo Abandonado");
            jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxTitular;        
    }
    
    private ComboBoxEstructuras getJComboBoxRehab()
    { 
        if (jComboBoxRehab == null)
        {
            Estructuras.cargarEstructura("eiel_Posibilidad de rehabilitación");
            jComboBoxRehab = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxRehab.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxRehab;        
    }
    
    private ComboBoxEstructuras getJComboBoxAcceso()
    { 
        if (jComboBoxAcceso == null)
        {
            Estructuras.cargarEstructura("eiel_Acceso a Núcleo Abandonado");
            jComboBoxAcceso = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAcceso.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAcceso;        
    }
    
    private ComboBoxEstructuras getJComboBoxAgua()
    { 
        if (jComboBoxAgua == null)
        {
            Estructuras.cargarEstructura("eiel_Agua en Núcleo Abandonado");
            jComboBoxAgua = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxAgua.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxAgua;        
    }
    
    private ComboBoxEstructuras getJComboBoxElec()
    { 
        if (jComboBoxElec == null)
        {
            Estructuras.cargarEstructura("eiel_Servicio de electricidad");
            jComboBoxElec = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
        
            jComboBoxElec.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxElec;        
    }
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv == null)
    	{
    		jTextFieldObserv  = new TextField();
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
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstado;        
    }
    
    
    public NucleosAbandonadosPanel(GridBagLayout layout)
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
                     
        }
        return jPanelIdentificacion;
    }
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelListaNucleos()
    {
        if (jPanelListaNucleos == null)
        {   
        	
        	jPanelListaNucleos = new JPanel(new GridBagLayout());
            jPanelListaNucleos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.listanucleosabandonados"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            
            jPanelListaNucleos.add(getJScrollPaneListaNiveles(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            
            JPanel panelBotonera = new JPanel(new GridBagLayout());
            panelBotonera.add(getAniadirButton(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            panelBotonera.add(getModificarButton(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));   
            
            panelBotonera.add(getEliminarButton(), 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));   
            
            jPanelListaNucleos.add(panelBotonera, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            
        	
        }
        return jPanelListaNucleos;
    }
    
	private JTable jTableListaNucleos = null;
	private NucleosAbandonadosEIELTableModel tableListaNucleosAbandonadosModel = null ;
	private ArrayList nucleosAbandonados = null;
	private JScrollPane jScrollPaneListaNucleosAbandonados = null;
	
    private JScrollPane getJScrollPaneListaNiveles(){
    	
		if (jScrollPaneListaNucleosAbandonados  == null){
    		jScrollPaneListaNucleosAbandonados = new JScrollPane();
    		jScrollPaneListaNucleosAbandonados.setViewportView(getJTableListaNucleos());
    		jScrollPaneListaNucleosAbandonados.setPreferredSize(new Dimension(150,100));
    	}
    	return jScrollPaneListaNucleosAbandonados;
    }
	
    
    private JTable getJTableListaNucleos()
    {

		if (jTableListaNucleos  == null)
    	{
    		jTableListaNucleos   = new JTable();

    		tableListaNucleosAbandonadosModel = new NucleosAbandonadosEIELTableModel();

    		TableSorted tblSorted= new TableSorted((TableModel)tableListaNucleosAbandonadosModel);
    		tblSorted.setTableHeader(jTableListaNucleos.getTableHeader());
    		jTableListaNucleos.setModel(tblSorted);
    		jTableListaNucleos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		jTableListaNucleos.setCellSelectionEnabled(false);
    		jTableListaNucleos.setColumnSelectionAllowed(false);
    		jTableListaNucleos.setRowSelectionAllowed(true);
    		jTableListaNucleos.getTableHeader().setReorderingAllowed(false);
    				
    		jTableListaNucleos.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    loadSelectedItem();
                }
            });
    		
    		EdicionOperations oper = new EdicionOperations();
    		    		
    		((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(nucleosAbandonados!=null?nucleosAbandonados:new ArrayList());

    	}
    	return jTableListaNucleos;
    }    
    
    
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleosAbandonadosEIEL){
    		
    		loadData((NucleosAbandonadosEIEL)selectedItem);
    	}    	
    }
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JButton getAniadirButton()
    {
    	if (annidair == null){
    		annidair = new JButton("Aniadir");
    		annidair.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					if (onAniadirButtonDo()){
						limpiarDatosNucleoAbandonado();
					}
				}

			}
			);
    		
    	}
    	return annidair;
    }

	private boolean onAniadirButtonDo() {
		okPressed = true;
		
		boolean aniadido = ((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getData().add(getNucleoAbandonado(null));
		
    	((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).fireTableDataChanged();
    	getJTableListaNucleos().updateUI();
		okPressed = false;
		return aniadido;
	}
    
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JButton getModificarButton()
    {
    	if (modificar == null){
    		modificar = new JButton("Modificar");
    		
    		modificar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					if (onModificarButtonDo()){
						limpiarDatosNucleoAbandonado();
					}
				}


			}
			);
    		
    	}
    	return modificar;
    }
    

	private void limpiarDatosNucleoAbandonado() {
	
			this.getJComboBoxEntidad().setSelectedIndex(0);
			this.getJComboBoxNucleo().setSelectedIndex(-1);
			this.getJTextFieldAbandono().setText("");
			this.getJComboBoxCausaAband().setSelectedIndex(0);
			this.getJComboBoxTitular().setSelectedIndex(0);
			this.getJComboBoxRehab().setSelectedIndex(0);
			this.getJComboBoxElec().setSelectedIndex(0);
			
			this.getJComboBoxAcceso().setSelectedIndex(0);
			this.getJComboBoxAgua().setSelectedIndex(0);
			
			
			this.getJComboBoxEstado().setSelectedIndex(0);
			this.getJTextFieldObserv().setText("");
		
	}
    

	private boolean onModificarButtonDo() {
		
		boolean resultado =false;
		if (!((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getData().isEmpty()
				&& !getJTableListaNucleos().getSelectionModel().isSelectionEmpty()){
			int selectedRow = getJTableListaNucleos().getSelectedRow();
			Object selectedItem = ((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);

			if (selectedItem != null && selectedItem instanceof NucleosAbandonadosEIEL){

				okPressed = true;
				((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getData().remove(selectedRow);
				selectedItem = getNucleoAbandonado(null);
				((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getData().add(selectedRow, selectedItem);
				okPressed = false;


				((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).fireTableDataChanged();
				getJTableListaNucleos().updateUI();

				loadData((NucleosAbandonadosEIEL) selectedItem);
				resultado = true;

			}
		}
    	return resultado;
		
	}
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JButton getEliminarButton()
    {
    	if (eliminar == null){
    		eliminar = new JButton("Eliminar");
    		
    		eliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual

						if (onEliminarButtonDo()){
							limpiarDatosNucleoAbandonado();
						}
				}
			}
			);
    		
    	}
    	return eliminar;
    }
    
	private boolean onEliminarButtonDo() {
		
		boolean resultado = false;
		if (!((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getData().isEmpty()
				&& !getJTableListaNucleos().getSelectionModel().isSelectionEmpty()){
			int selectedRow = getJTableListaNucleos().getSelectedRow();
			Object selectedItem = ((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
			if (selectedItem != null && selectedItem instanceof NucleosAbandonadosEIEL){
				if (confirm(I18N.get("LocalGISEIEL","localgiseiel.register.tag2"), 
						I18N.get("LocalGISEIEL","localgiseiel.register.tag3"))){

					((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getData().remove(selectedItem);

					((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).fireTableDataChanged();
					getJTableListaNucleos().updateUI();

					// eliminamos de la base de datos
					try {
						Collection lstFeatures = ConstantesLocalGISEIEL.clienteLocalGISEIEL.getIdsFeatures(selectedItem, ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS);
						GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer.getSystemId();
						ConstantesLocalGISEIEL.clienteLocalGISEIEL.eliminarElemento(selectedItem, lstFeatures, idLayer, ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS );
						resultado = true;
					} catch (Exception e) {
						e.printStackTrace();
						resultado = false;
					}


				}   
			}
    	}
    	return resultado;
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
        	
            jLabelAbandono = new JLabel("", JLabel.CENTER); 
            jLabelAbandono.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.abandono")); 
            
            jLabelCausaAband = new JLabel("", JLabel.CENTER); 
            jLabelCausaAband.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.causa")); 

            jLabelTitularAband = new JLabel("", JLabel.CENTER);
            jLabelTitularAband.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tit"));
            
            jLabelRehab = new JLabel("", JLabel.CENTER);
            jLabelRehab.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rehabilitacion"));
            
            jLabelAcceso = new JLabel("", JLabel.CENTER);
            jLabelAcceso.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.acceso"));
            
            jLabelAgua = new JLabel("", JLabel.CENTER);
            jLabelAgua.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.agua"));
            
            jLabelElec = new JLabel("", JLabel.CENTER);
            jLabelElec.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.electricidad"));
            
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            
            JPanel primeraLineaPanel = new JPanel(new GridBagLayout());
            
            primeraLineaPanel.add(jLabelAbandono,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(getJTextFieldAbandono(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(jLabelCausaAband,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(getJComboBoxCausaAband(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(jLabelTitularAband,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(getJComboBoxTitular(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(jLabelRehab,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(getJComboBoxRehab(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            JPanel segundaLineaPanel = new JPanel(new GridBagLayout());
            
            segundaLineaPanel.add(jLabelAcceso,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));
            
            segundaLineaPanel.add(getJComboBoxAcceso(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 200, 0));
            
            segundaLineaPanel.add(jLabelAgua,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));
            
            segundaLineaPanel.add(getJComboBoxAgua(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));
            
            
            
            primeraLineaPanel.add(jLabelElec,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(getJComboBoxElec(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(jLabelObserv,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            primeraLineaPanel.add(getJTextFieldObserv(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(primeraLineaPanel, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(segundaLineaPanel, 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
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

        return  (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0) &&
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
	        	if (esquema.hasAttribute("codentidad") && feature.getAttribute(esquema.getAttributeByColumn("codentidad"))!=null){
	        		entidad=(feature.getAttribute(esquema.getAttributeByColumn("codentidad"))).toString();
	        	}
	        	
	        	String nucleo = null;
	        	if (esquema.hasAttribute("codpoblamiento") && feature.getAttribute(esquema.getAttributeByColumn("codpoblamiento"))!=null){
	        		nucleo=(feature.getAttribute(esquema.getAttributeByColumn("codpoblamiento"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelNucleosAbandonadosEIEL(codprov, codmunic));
	        	
	        	nucleosAbandonados = operations.getPanelLstNucleosAbandonadosEIEL(codprov, codmunic);
	        	((NucleosAbandonadosEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(nucleosAbandonados);
	        	getJTableListaNucleos().updateUI();
	        	
	        	

	        	loadDataIdentificacion(codprov, codmunic, entidad, nucleo);       	
			}
		}

	public void loadDataIdentificacion(String codprov, String codmunic,
			String entidad, String nucleo) {
		
		//Datos identificacion
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

        
        if (entidad != null){
        	jComboBoxEntidad.setSelectedItem(entidadesSingularesIndexSeleccionar(entidad));
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(0);
        }
        
        if (nucleo != null){
        	jComboBoxNucleo.setSelectedItem(nucleoPoblacionIndexSeleccionar(nucleo));
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(-1);
        }
        
	}
	
    private boolean confirm(String tag1, String tag2){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, tag1, tag2, JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
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