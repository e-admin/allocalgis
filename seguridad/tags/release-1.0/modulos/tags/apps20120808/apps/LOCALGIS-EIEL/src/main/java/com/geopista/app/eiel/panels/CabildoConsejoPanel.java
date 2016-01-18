package com.geopista.app.eiel.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.geopista.app.eiel.beans.CabildoConsejoEIEL;
import com.geopista.app.eiel.dialogs.CabildoConsejoDialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class CabildoConsejoPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;
    
    private boolean okPressed = false;	

	private JLabel jLabelCodProv = null;
	private JComboBox jComboBoxProvincia = null;

    
    private JLabel jLabelCodIsla = null;
	private JLabel jLabelDenominacion = null;

	private JTextField jTextFieldCodIsla = null;
	private JTextField jTextFieldDenominacion = null;

	
    public CabildoConsejoPanel(){
        super();
        initialize();
    }
  
    public CabildoConsejoPanel(CabildoConsejoEIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    public void loadData(CabildoConsejoEIEL elemento){
        if (elemento!=null){
        	if (elemento.getCodINEProvincia() != null) {
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(elemento
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}

            if (elemento.getCodIsla() != null){
        		jTextFieldCodIsla.setText(elemento.getCodIsla().toString());
        	}
        	else{
        		jTextFieldCodIsla.setText("");
        	}
            if (elemento.getDenominacion() != null){
        		jTextFieldDenominacion.setText(elemento.getDenominacion().toString());
        	}
        	else{
        		jTextFieldDenominacion.setText("");
        	}
        }else{
        	// elemento a cargar es null....
        	// se carga por defecto la clave, el codigo de provincia y el codigo de municipio
        	        	       	
        	 if (ConstantesLocalGISEIEL.idProvincia!=null){
 				jComboBoxProvincia
 				.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
 			 }        	
        }
    }
    
    public void loadData(){


    	Object object = AppContext.getApplicationContext().getBlackboard().get("cabildo_panel");    	
    	if (object != null && object instanceof CabildoConsejoEIEL){    		
    		CabildoConsejoEIEL elemento = (CabildoConsejoEIEL)object;
    		
            if (elemento.getCodINEProvincia()!=null){
            	jComboBoxProvincia
 				.setSelectedIndex(provinciaIndexSeleccionar(elemento
 						.getCodINEProvincia()));            }
            else{
            	jComboBoxProvincia.setSelectedIndex(0);
            }

            if (elemento.getCodIsla() != null){
        		jTextFieldCodIsla.setText(elemento.getCodIsla().toString());
        	}
        	else{
        		jTextFieldCodIsla.setText("");
        	}
            if (elemento.getDenominacion() != null){
        		jTextFieldDenominacion.setText(elemento.getDenominacion().toString());
        	}
        	else{
        		jTextFieldDenominacion.setText("");
        	}
        }
    }
    
    
    public CabildoConsejoEIEL getCabildoConsejo (CabildoConsejoEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new CabildoConsejoEIEL();
            }
            /* Claves: COMBOBOX */
            elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
            
            /* JTEXT - String */
            if (jTextFieldCodIsla.getText()!=null) {
            	elemento.setCodIsla(jTextFieldCodIsla.getText());
        	}
            if (jTextFieldDenominacion.getText()!=null) {
            	elemento.setDenominacion(jTextFieldDenominacion.getText());
        	}
        }
        return elemento;
    }
    
    private void initialize(){      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.cabildoconsejo.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(CabildoConsejoDialog.DIM_X,CabildoConsejoDialog.DIM_Y);
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
    
    public JComboBox getJComboBoxProvincia() {
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

    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private JTextField getjTextFieldCodIsla(){
    	if (jTextFieldCodIsla == null){
    		jTextFieldCodIsla = new TextField(2);
    		jTextFieldCodIsla.setText("00");
    		jTextFieldCodIsla.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCodIsla, 2, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCodIsla;
    }
    
    private JTextField getjTextFieldDenominacion(){
    	if (jTextFieldDenominacion == null){
    		jTextFieldDenominacion = new TextField(18);
    		jTextFieldDenominacion.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDenominacion, 18, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDenominacion;
    }
        
    
    public CabildoConsejoPanel(GridBagLayout layout){
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
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            /* Agregamos las Labels al JPANELIDENTIFICATION */
            jPanelIdentificacion.add(jLabelCodProv, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentificacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 15), 0, 0));
            
            
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
        	jLabelCodIsla = new JLabel("", JLabel.CENTER); 
        	jLabelCodIsla.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.cabildo.isla"));
            jLabelDenominacion = new JLabel("", JLabel.CENTER);
            jLabelDenominacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.cabildo.denominacion")); 
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelCodIsla,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldCodIsla(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 15), 0, 0));
            jPanelInformacion.add(jLabelDenominacion,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldDenominacion(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 15), 0, 0));
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
        return  (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0);
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

		        	String codisla = null;
		        	if (feature.getSchema().hasAttribute("cod_isla")){
		        		if (feature.getAttribute(esquema.getAttributeByColumn("cod_isla"))!=null){
		        			codisla=(feature.getAttribute(esquema.getAttributeByColumn("cod_isla"))).toString();
		        		}
		        	}

		        	EdicionOperations operations = new EdicionOperations();
		        	loadData(operations.getPanelCabildoConsejoEIEL(codprov, codisla));
		        	
		        	loadDataIdentificacion(codprov, codisla);
		        }
				
			}
		}

	public void loadDataIdentificacion(String codprov, String codisla) {
		//Datos identificacion

        
		if (codprov != null) {
			// jComboBoxProvincia.setSelectedItem(codprov);
			jComboBoxProvincia
					.setSelectedIndex(provinciaIndexSeleccionar(codprov));
		} else {
			jComboBoxProvincia.setSelectedIndex(-1);
		}
        
        if (codisla != null){            	
        	jTextFieldCodIsla.setText(codisla);
        }
        else{
        	jTextFieldCodIsla.setText("");
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
    
}
