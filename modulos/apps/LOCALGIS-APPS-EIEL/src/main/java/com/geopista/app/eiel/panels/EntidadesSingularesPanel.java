/**
 * EntidadesSingularesPanel.java
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
import java.util.Collection;
import java.util.Iterator;
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
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.EntidadesSingularesDialog;
import com.geopista.app.eiel.models.EntidadesSingularesEIELTableModel;
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

public class EntidadesSingularesPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null; 
    private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;
	private JPanel jPanelListaEntidades = null;
    
    public boolean okPressed = false;	

	private JLabel jLabelCodProv = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelCodEntidad = null;
	
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JTextField jTextFieldEntidad = null;
    
    private JLabel jLabelDenominacion = null;
	private JLabel jLabelObserv = null;
	private JLabel jLabelFechaRevi = null;
	private JLabel jLabelEstado = null;
	
	private JTextField jTextFieldDenominacion = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldFechaRev = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	
	public static boolean CARGARLISTAENTIDADES = false;

	
    public EntidadesSingularesPanel(){
        super();
        initialize();
    }
  
    public EntidadesSingularesPanel(EntidadesSingularesEIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    public void loadData(EntidadesSingularesEIEL elemento){
        if (elemento!=null){
        	/* Campos Clave */
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
        		jTextFieldEntidad.setText(elemento.getCodINEEntidad().toString());
        	}
        	else{
        		jTextFieldEntidad.setText("");
        	}
            /* Otros Campos */
            if (elemento.getDenominacion() != null){
        		jTextFieldDenominacion.setText(elemento.getDenominacion().toString());
        	}
        	else{
        		jTextFieldDenominacion.setText("");
        	}
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}            
                       
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(elemento.getFechaRevision().toString());
        	}
        	else{
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
        		jTextFieldFechaRev.setText(datetime);
        	}           
            if (elemento.getEstadoRevision() != null){
            	jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision().toString());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
        }
    }
    
    public void loadData(){
    	
   
    	Object object = AppContext.getApplicationContext().getBlackboard().get("entidadessingulares_panel");    	
    	if (object != null && object instanceof EntidadesSingularesEIEL){    		
    		EntidadesSingularesEIEL elemento = (EntidadesSingularesEIEL)object;
    		/* Campos Clave */
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
        		jTextFieldEntidad.setText(elemento.getCodINEEntidad().toString());
        	}
        	else{
        		jTextFieldEntidad.setText("");
        	}
            /* Otros Campos */
            if (elemento.getDenominacion() != null){
        		jTextFieldDenominacion.setText(elemento.getDenominacion().toString());
        	}
        	else{
        		jTextFieldDenominacion.setText("");
        	}
            if (elemento.getObservaciones() != null){
        		jTextFieldObserv.setText(elemento.getObservaciones().toString());
        	}
        	else{
        		jTextFieldObserv.setText("");
        	}            
            
            if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
        		jTextFieldFechaRev.setText(elemento.getFechaRevision().toString());
        	}
        	else{
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
                jTextFieldFechaRev.setText(datetime);
        	}           
            if (elemento.getEstadoRevision() != null){
            	jComboBoxEstado.setSelectedItem(elemento.getEstadoRevision());
        	}
        	else{
        		jComboBoxEstado.setSelectedIndex(0);
        	}
        }
    }
    
    
    public EntidadesSingularesEIEL getEntidadesSingulares (EntidadesSingularesEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new EntidadesSingularesEIEL();
            }
            /* Claves: COMBOBOX  y JTEXT */
        	elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
    				.getSelectedItem()).getIdProvincia());
    		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
    				.getSelectedItem()).getIdIne());
    		elemento.setCodINEEntidad(jTextFieldEntidad.getText());
            /* JTEXT - Integer */
            if (jComboBoxEstado.getSelectedPatron()!=null)
                elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));
            
            /* JTEXT - String */
            if (jTextFieldDenominacion.getText()!=null) {
            	elemento.setDenominacion(jTextFieldDenominacion.getText());
        	}
            if (jTextFieldObserv.getText()!=null){
            	elemento.setObservaciones(jTextFieldObserv.getText());
            }
            /* JTEXT - Date */
            if (jTextFieldFechaRev.getText()!=null && !jTextFieldFechaRev.getText().equals("")){
            	java.util.Date fecha = new java.util.Date(jTextFieldFechaRev.getText());
            	elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));
            } else {
            	elemento.setFechaRevision(null);
            }
        }
        return elemento;
    }
    
    private void initialize(){      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.entidadessingulares.panel.title"));
        
        this.setLayout(new GridBagLayout());
        this.setSize(EntidadesSingularesDialog.DIM_X,EntidadesSingularesDialog.DIM_Y);
        
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 20, 0));
        
        if (this.CARGARLISTAENTIDADES){
        	this.add(getJPanelListaEntidades(),  new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
        			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        			new Insets(0, 5, 0, 5), 0, 0));
        	this.CARGARLISTAENTIDADES = false;
        }
        
        this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosRevision(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
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
			 Provincia p = new Provincia();
				p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
				p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
				getJComboBoxProvincia().setSelectedItem(p);
		}
		loadData();
    }
    
/* Metodos que devuelven CAMPOS CLAVE */
   
    public JComboBox getJComboBoxProvincia(){
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

					}
				}
			});
		}
		return jComboBoxMunicipio;
    }
   
    private JTextField getJTextFieldEntidad(){
        if (jTextFieldEntidad == null){
        	jTextFieldEntidad = new TextField(4);
        	
        }
        return jTextFieldEntidad;
    }
    
    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private JTextField getjTextFieldDenominacion(){
    	if (jTextFieldDenominacion == null){
    		jTextFieldDenominacion = new TextField(50);
    		jTextFieldDenominacion.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldDenominacion, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldDenominacion;
    }

    private JTextField getJTextFieldObserv(){
    	if (jTextFieldObserv == null){
    		jTextFieldObserv  = new TextField(50);
    		jTextFieldObserv.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv, 50, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObserv;
    }

    private JTextField getJTextFieldFechaRev(){
    	if (jTextFieldFechaRev == null){
    		jTextFieldFechaRev = new TextField();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);            
            jTextFieldFechaRev.setText(datetime);
    	}
    	return jTextFieldFechaRev;
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
    
    
    public EntidadesSingularesPanel(GridBagLayout layout){
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
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 
            jLabelCodEntidad  = new JLabel("", JLabel.CENTER);
            jLabelCodEntidad.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codentidad")));
            /* Agregamos las Labels al JPANELIDENTIFICATION */
            jPanelIdentificacion.add(jLabelCodProv, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));            
            jPanelIdentificacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));
            jPanelIdentificacion.add(jLabelCodMunic, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));            
            jPanelIdentificacion.add(getJComboBoxMunicipio(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 10, 0));
            jPanelIdentificacion.add(jLabelCodEntidad, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));
            jPanelIdentificacion.add(getJTextFieldEntidad(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 1, 0));
            
            
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
        	jLabelDenominacion = new JLabel("", JLabel.CENTER); 
        	jLabelDenominacion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nombre"));
            jLabelObserv = new JLabel("", JLabel.CENTER);
            jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelDenominacion,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldDenominacion(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelObserv,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 200, 0));
            
            jPanelInformacion.add(new JLabel(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 190, 0));
            
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
            jPanelRevision.add(getJTextFieldFechaRev(), 
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
           

    public void okPressed(){
        okPressed = true;
    }
    
    public boolean getOkPressed(){
        return okPressed;
    }

    public boolean datosMinimosYCorrectos(){
        return  (jTextFieldEntidad.getText()!=null && !jTextFieldEntidad.getText().equalsIgnoreCase("")) &&
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
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelEntidadesSingularesEIEL(codprov, codmunic));
	        	
	        	entidadesSingulares = operations.getPanelLstEntidadesSingularesEIEL(codprov, codmunic);
	        	((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).setData(entidadesSingulares);
	        	getJTableListaEntidades().updateUI();
	        	
	        	loadDataIdentificacion(codprov, codmunic);       	
			}
		}
	
	
	
	
	public void loadDataIdentificacion(String codprov, String codmunic) {
		
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

		
	}
	
	
	 /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelListaEntidades()
    {
        if (jPanelListaEntidades == null)
        {   
        	
        	jPanelListaEntidades = new JPanel(new GridBagLayout());
            jPanelListaEntidades.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.listaentidadessingualres"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            
            jPanelListaEntidades.add(getJScrollPaneListaNiveles(), 
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
            
            jPanelListaEntidades.add(panelBotonera, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            
        	
        }
        return jPanelListaEntidades;
    }
    
	private JTable jTableListaEntidades = null;
	private EntidadesSingularesEIELTableModel tableListaEntidadesModel = null ;
	private ArrayList entidadesSingulares = null;
	private JScrollPane jScrollPaneListaEntidades = null;

	private JButton annidair = null;
	private JButton modificar = null;
	private JButton eliminar = null;
	
    private JScrollPane getJScrollPaneListaNiveles(){
    	
		if (jScrollPaneListaEntidades  == null){
    		jScrollPaneListaEntidades = new JScrollPane();
    		jScrollPaneListaEntidades.setViewportView(getJTableListaEntidades());
    		jScrollPaneListaEntidades.setPreferredSize(new Dimension(150,100));
    	}
    	return jScrollPaneListaEntidades;
    }
	
    
    private JTable getJTableListaEntidades()
    {

		if (jTableListaEntidades  == null)
    	{
    		jTableListaEntidades   = new JTable();

    		tableListaEntidadesModel = new EntidadesSingularesEIELTableModel();

    		TableSorted tblSorted= new TableSorted((TableModel)tableListaEntidadesModel);
    		tblSorted.setTableHeader(jTableListaEntidades.getTableHeader());
    		jTableListaEntidades.setModel(tblSorted);
    		jTableListaEntidades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		jTableListaEntidades.setCellSelectionEnabled(false);
    		jTableListaEntidades.setColumnSelectionAllowed(false);
    		jTableListaEntidades.setRowSelectionAllowed(true);
    		jTableListaEntidades.getTableHeader().setReorderingAllowed(false);
    				
    		jTableListaEntidades.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    loadSelectedItem();
                }
            });
    		
    		((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).setData(entidadesSingulares!=null?entidadesSingulares:new ArrayList());

    	}
    	return jTableListaEntidades;
    }    
    
    
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaEntidades().getSelectedRow();
    	Object selectedItem = ((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof EntidadesSingularesEIEL){
    		
    		loadData((EntidadesSingularesEIEL)selectedItem);
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
						limpiarDatosEntidadSingular();
					}
				}

			}
			);
    		
    	}
    	return annidair;
    }

	private boolean onAniadirButtonDo() {
		boolean aniadido;
		if (!this.existeEntidadConCodigo()){
			okPressed = true;

			aniadido = ((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData().add(getEntidadesSingulares(null));

			((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).fireTableDataChanged();
			getJTableListaEntidades().updateUI();
			okPressed = false;
		} else{
			JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
					"La entidad con código '" + getJTextFieldEntidad().getText() + "' ya existe.");
			
			aniadido = false;
		}

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
						limpiarDatosEntidadSingular();
					}
				}

			}
			);
    		
    	}
    	return modificar;
    }


    private boolean onModificarButtonDo() {
    	boolean resultado = false;
    	if (!((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData().isEmpty()
    			&& !getJTableListaEntidades().getSelectionModel().isSelectionEmpty()){
    		int selectedRow = getJTableListaEntidades().getSelectedRow();
    		Object selectedItem = ((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getValueAt(selectedRow);

    		if (selectedItem != null && selectedItem instanceof EntidadesSingularesEIEL){

    			okPressed = true;
    			((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData().remove(selectedRow);
    			selectedItem = getEntidadesSingulares(null);
    			((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData().add(selectedRow, selectedItem);
    			okPressed = false;


    			((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).fireTableDataChanged();
    			getJTableListaEntidades().updateUI();

    			loadData((EntidadesSingularesEIEL) selectedItem);
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
						limpiarDatosEntidadSingular();
					}
				}
			}
			);
    		
    	}
    	return eliminar;
    }
    
	private boolean onEliminarButtonDo() {
		boolean resultado = false;
		if (!((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData().isEmpty()
				&& !getJTableListaEntidades().getSelectionModel().isSelectionEmpty()){
			int selectedRow = getJTableListaEntidades().getSelectedRow();
			Object selectedItem = ((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getValueAt(selectedRow);
			if (!((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData().isEmpty()){
				if (confirm(I18N.get("LocalGISEIEL","localgiseiel.register.tag2"), 
						I18N.get("LocalGISEIEL","localgiseiel.register.tag3"))){
					

					if (selectedItem != null && selectedItem instanceof EntidadesSingularesEIEL){

						((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData().remove(selectedItem);

						((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).fireTableDataChanged();
						getJTableListaEntidades().updateUI();

						// eliminamos de la base de datos
						try {
							Collection lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(selectedItem, ConstantesLocalGISEIEL.ENTIDADES_SINGULARES);
							GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
							String idLayer = geopistaLayer.getSystemId();
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(selectedItem, lstFeatures, idLayer, ConstantesLocalGISEIEL.ENTIDADES_SINGULARES );

							resultado = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}    	
				}
			}
		}
		return resultado;
	}

	public ArrayList getListaEntidadesSingulares() {

	        
	        return ((EntidadesSingularesEIELTableModel)((TableSorted)getJTableListaEntidades().getModel()).getTableModel()).getData();
	    }
	
	private void limpiarDatosEntidadSingular(){
		this.getJTextFieldEntidad().setText("");
		this.getJTextFieldObserv().setText("");
		this.getJComboBoxEstado().setSelectedIndex(0);
		this.getjTextFieldDenominacion().setText("");
	}
	
	private boolean existeEntidadConCodigo(){
		boolean resultado = false;
		Iterator<EntidadesSingularesEIEL> it = this.getListaEntidadesSingulares().iterator();
		while(it.hasNext()){
			if (it.next().getCodINEEntidad().equals(getJTextFieldEntidad().getText())){
				return true;
			}
		}
		
		return resultado;
		
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
	
    
}
