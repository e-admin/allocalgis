package com.geopista.app.eiel.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
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
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.dialogs.CarreterasDialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.geopista.protocol.inventario.Inventario;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.inventario.ConstantesEIEL;

public class CarreterasPanel extends InventarioPanel 
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
    
    private boolean okPressed = false;	
	
	private JLabel jLabelCodProv = null;
    private JLabel jLabelCodCarrt = null;
	
	private JComboBox jComboBoxProvincia = null;
	
    private JTextField jTextFieldCarretera = null;
    
	private JLabel jLabelClaseVia = null;
	private JLabel jLabelDenom = null;
	private JLabel jLabelTitularVia = null;
	private JLabel jLabelFechaAct = null;
	private JLabel jLabelObserv = null;
	
	private JTextField jTextFieldClaseVia = null;
	private JTextField jTextFieldDenom = null;
	private ComboBoxEstructuras jComboBoxTitularVia = null;
	private JTextField jTextFieldFechaAct = null;
	private JTextField jTextFieldObserv = null;
	
    /**
     * This method initializes
     * 
     */
    public CarreterasPanel()
    {
        super();
        initialize();
    }
    
    public CarreterasPanel(TramosCarreterasEIEL pers)
    {
        super();
        initialize();
        loadData (pers);
    }
    
    public void loadData(TramosCarreterasEIEL elemento)
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

            
            if (elemento.getCodCarretera() != null){            	
            	jTextFieldCarretera.setText(elemento.getCodCarretera());
            }
            else{
            	jTextFieldCarretera.setText("");
            }
            
            if (elemento.getClaseVia() != null){
        		jTextFieldClaseVia.setText(elemento.getClaseVia());
        	}
        	else{
        		jTextFieldClaseVia.setText("");
        	}
            
            if (elemento.getDenominacion() != null){
        		jTextFieldDenom.setText(elemento.getDenominacion());
        	}
        	else{
        		jTextFieldDenom.setText("");
        	}
            
            if (elemento.getTitularidad() != null){
            	jComboBoxTitularVia.setSelectedPatron(elemento.getTitularidad());
        	}
        	else{
        		jComboBoxTitularVia.setSelectedIndex(0);
        	}
            
            if (elemento.getFechaActualizacion() != null){
        		jTextFieldFechaAct.setText(elemento.getFechaActualizacion().toString());
        	}
        	else{
        		jTextFieldFechaAct.setText("");
        	}
            
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones());
        	}
        	else{
        		jTextFieldObserv.setText("");
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

        }
    }
    
    
    public TramosCarreterasEIEL getCarreteras (TramosCarreterasEIEL elemento)
    {

        if (okPressed)
        {
            if(elemento==null)
            {
            	elemento = new TramosCarreterasEIEL();
            }
            
			elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());            
            elemento.setCodCarretera(jTextFieldCarretera.getText());
            elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));
            
            if (jTextFieldClaseVia.getText()!=null){
            	elemento.setClaseVia(jTextFieldClaseVia.getText());
            }
            if (jTextFieldDenom.getText()!=null){
            	elemento.setDenominacion(jTextFieldDenom.getText());
            }            
            
            if (jComboBoxTitularVia.getSelectedPatron()!=null){
    			elemento.setTitularidad((String) jComboBoxTitularVia.getSelectedPatron());
            }
            
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            
            if (jTextFieldFechaAct.getText()!=null && !jTextFieldFechaAct.getText().equals("")){
            	String fechas=jTextFieldFechaAct.getText();
            	String anio=fechas.substring(0,4);
            	String mes=fechas.substring(5,7);
            	String dia=fechas.substring(8,10);
            	
            	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
            	elemento.setFechaActualizacion(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
            }  
            else{
            	elemento.setFechaActualizacion(null);
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
        }
        
        return elemento;
    }
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(CarreterasDialog.DIM_X,CarreterasDialog.DIM_Y);
        
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosInventario(),  new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
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
   			Provincia p = new Provincia();
   			p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
   			p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
   			getJComboBoxProvincia().setSelectedItem(p);
   		}

    }
    
    private JTextField getJTextFieldCarretera()
    {
        if (jTextFieldCarretera == null)
        {
            jTextFieldCarretera = new TextField(8);
        }
        return jTextFieldCarretera;
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
			}
        return jComboBoxProvincia;
    }
    
    private JTextField getJTextFieldClaseVia()
    {
    	if (jTextFieldClaseVia == null)
    	{
    		jTextFieldClaseVia = new TextField(3);
    		jTextFieldClaseVia.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldClaseVia, 10, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldClaseVia;
    }
    
    private JTextField getJTextFieldDenom()
    {
    	if (jTextFieldDenom == null)
    	{
    		jTextFieldDenom  = new TextField(100);
    		jTextFieldDenom.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDenom, 100, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDenom;
    }
    
    private ComboBoxEstructuras getJComboBoxTitularVia()
    {
    	 if (jComboBoxTitularVia == null)
         {
             Estructuras.cargarEstructura("eiel_Titular Via");
             jComboBoxTitularVia = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                     null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), false);
         
             jComboBoxTitularVia.setPreferredSize(new Dimension(100, 20));
         }
         return jComboBoxTitularVia;      
    }
     
    private JTextField getJTextFieldFechaAct()
    {
    	if (jTextFieldFechaAct == null)
    	{
    		jTextFieldFechaAct  = new TextField();
    		
    	}
    	return jTextFieldFechaAct;
    }
    
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv == null)
    	{
    		jTextFieldObserv  = new TextField();
    		
    	}
    	return jTextFieldObserv;
    }    
    
    public CarreterasPanel(GridBagLayout layout)
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
            
            jLabelCodCarrt = new JLabel("", JLabel.CENTER); 
            jLabelCodCarrt.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codcarrt"))); 

             jPanelIdentificacion.add(jLabelCodProv, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelIdentificacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentificacion.add(jLabelCodCarrt, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelIdentificacion.add(getJTextFieldCarretera(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
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
        	
            jLabelClaseVia = new JLabel("", JLabel.CENTER); 
            jLabelClaseVia.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.via")); 
            
            jLabelDenom = new JLabel("", JLabel.CENTER); 
            jLabelDenom.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.denom")); 
            
            jLabelTitularVia = new JLabel("", JLabel.CENTER); 
            jLabelTitularVia.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.tit")); 

            jLabelFechaAct = new JLabel("", JLabel.CENTER);
            jLabelFechaAct.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechaAct"));
            
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            
            
            jPanelInformacion.add(jLabelClaseVia,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldClaseVia(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelDenom,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldDenom(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelTitularVia,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJComboBoxTitularVia(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelFechaAct,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldFechaAct(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
        }
        return jPanelInformacion;
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
        (jTextFieldCarretera.getText()!=null && !jTextFieldCarretera.getText().equalsIgnoreCase(""));
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

}
