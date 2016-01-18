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
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.dialogs.EntidadesAgrupadasDialog;
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

public class EntidadesAgrupadasPanel extends JPanel implements FeatureExtendedPanel {

	private static final long serialVersionUID = 1L;

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	private Blackboard Identificadores = aplicacion.getBlackboard();

	private EntidadesAgrupadasEIEL entidadesAgrupadas = null;

	private JPanel jPanelIdentificacion = null;
	private JPanel jPanelInformacion = null;

	private boolean okPressed = false;

	private JLabel jLabelCodProv = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelEntidad = null;
	private JLabel jLabelnucleo = null;
	private JLabel jLabelEntidadAgrupada = null;
	private JLabel jLabelnucleoAgrupado = null;


	private JComboBox jComboBoxProvincia = null;
	private JComboBox jComboBoxMunicipio = null;
	private JTextField jTextEntidadAgrupada = null;
	private JTextField jTextNucleoAgrupado = null;
	private JComboBox jComboBoxEntidad = null;
	private JComboBox jComboBoxNucleo = null;


	private String idMunicipioSelected;

	/**
	 * This method initializes
	 * 
	 */
	public EntidadesAgrupadasPanel() {
		super();
		initialize();
	}

	public void loadData() {
		Object object = AppContext.getApplicationContext().getBlackboard().get("captacion_panel");
		if (object != null && object instanceof EntidadesAgrupadasEIEL) {
			EntidadesAgrupadasEIEL elemento = (EntidadesAgrupadasEIEL) object;

			this.entidadesAgrupadas = elemento;

			// Datos identificacion


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


		}
	}

	public void loadData(EntidadesAgrupadasEIEL panelEntidadesAgrupadasEIEL) {

		if (panelEntidadesAgrupadasEIEL != null) {

			idMunicipioSelected = panelEntidadesAgrupadasEIEL.getCodINEMunicipio();
			// Datos identificacion		

//			if (panelEntidadesAgrupadasEIEL.getCodINEProvincia() != null) {
//				jComboBoxProvincia
//						.setSelectedIndex(provinciaIndexSeleccionar(panelEntidadesAgrupadasEIEL
//								.getCodINEProvincia()));
//			} else {
//				jComboBoxProvincia.setSelectedIndex(0);
//			}
			if (ConstantesLocalGISEIEL.idProvincia!=null){
				jComboBoxProvincia
				.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
			 }
			
			if (panelEntidadesAgrupadasEIEL.getCodINEMunicipio() != null) {
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(panelEntidadesAgrupadasEIEL
								.getCodINEMunicipio()));
			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}
			
			if (panelEntidadesAgrupadasEIEL.getCodEntidad() != null){
	        	jComboBoxEntidad.setSelectedIndex(
	        			entidadesSingularesIndexSeleccionar(panelEntidadesAgrupadasEIEL.getCodEntidad()));
	        }
	        else{
	        	jComboBoxEntidad.setSelectedIndex(0);
	        }
	        
	        if (panelEntidadesAgrupadasEIEL.getCodNucleo() != null){
	        	jComboBoxNucleo.setSelectedIndex(
	        			nucleoPoblacionIndexSeleccionar(panelEntidadesAgrupadasEIEL.getCodNucleo())
	        			);	
	        }
	        else{
	        	jComboBoxNucleo.setSelectedIndex(0);
	        }

			if(panelEntidadesAgrupadasEIEL.getCodEntidad_agrupada() != null ){
				jTextEntidadAgrupada.setText(panelEntidadesAgrupadasEIEL.getCodEntidad_agrupada());
			}
			else{
				jTextEntidadAgrupada.setText("");
			}
			
			if(panelEntidadesAgrupadasEIEL.getCodNucleo_agrupado() != null ){
				jTextNucleoAgrupado.setText(panelEntidadesAgrupadasEIEL.getCodNucleo_agrupado());
			}
			else{
				jTextNucleoAgrupado.setText("");
			}
			
			
			
			
		} else {
			// elemento a cargar es null....
			// se carga por defecto la clave, el codigo de provincia y el codigo
			// de municipio

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

	public EntidadesAgrupadasEIEL getEntidadesAgrupadasData() {

		EntidadesAgrupadasEIEL elemento = new EntidadesAgrupadasEIEL();

		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		elemento.setIdMunicipio(Integer
				.parseInt(ConstantesLocalGISEIEL.idMunicipio));

		return elemento;
	}

	public EntidadesAgrupadasEIEL getEntidadesAgrupadas(EntidadesAgrupadasEIEL elemento) {

		if (okPressed) {
			if (elemento == null) {
				elemento = new EntidadesAgrupadasEIEL();
			}

			elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());
			elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
					.getSelectedItem()).getIdIne());

			elemento.setIdMunicipio(Integer
					.parseInt(ConstantesLocalGISEIEL.idMunicipio));
			
			elemento.setCodINEEntidad(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            elemento.setCodINENucleo(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
            
            elemento.setCodINEEntidad_agrupada(jTextEntidadAgrupada.getText());
            elemento.setCodINENucleo_agrupado(jTextNucleoAgrupado.getText());
			
			
			

		}

		return elemento;
	}

	private void initialize() {
		Locale loc = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.geopista.app.eiel.language.LocalGISEIELi18n", loc, this
						.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("LocalGISEIEL", bundle);

		this.setName(I18N.get("LocalGISEIEL",
				"localgiseiel.captaciones.panel.title"));

		this.setLayout(new GridBagLayout());
		this.setSize(EntidadesAgrupadasDialog.DIM_X, EntidadesAgrupadasDialog.DIM_Y);

		this.add(getJPanelDatosIdentificacion(), new GridBagConstraints(0, 0,
				1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		
		this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 1, 1,
				1, 0.1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		
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

	public JTextField getjTextEntidadAgrupada() {
		if (jTextEntidadAgrupada == null) {
			jTextEntidadAgrupada = new TextField(4);
		}
		return jTextEntidadAgrupada;
	}

	public void setjTextEntidadAgrupada(JTextField jTextEntidadAgrupada) {
		this.jTextEntidadAgrupada = jTextEntidadAgrupada;
	}

	public JTextField getjTextNucleoAgrupado() {
		if (jTextNucleoAgrupado == null) {
			jTextNucleoAgrupado = new TextField(2);
		}
		return jTextNucleoAgrupado;
	}

	public void setjTextNucleoAgrupado(JTextField jTextNucleoAgrupado) {
		this.jTextNucleoAgrupado = jTextNucleoAgrupado;
	}
	
	
	 private JComboBox getJComboBoxEntidad()
	    {
	    	if (jComboBoxEntidad  == null)
			{
				jComboBoxEntidad = new JComboBox();
//			     	jComboBoxEntidad.setEditable(true);
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

	public EntidadesAgrupadasPanel(GridBagLayout layout) {
		super(layout);
		initialize();
	}

	public String validateInput() {
		return null;
	}

	/**
	 * This method initializes jPanelDatosIdentificacion
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getJPanelDatosIdentificacion() {
		if (jPanelIdentificacion == null) {
			jPanelIdentificacion = new JPanel(new GridBagLayout());
			jPanelIdentificacion.setBorder(BorderFactory.createTitledBorder(
					null, I18N.get("LocalGISEIEL",
							"localgiseiel.panels.identity"),
					TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
							Font.BOLD, 12)));

		
			jLabelCodProv = new JLabel("", JLabel.CENTER);
			jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N
					.get("LocalGISEIEL", "localgiseiel.panels.label.codprov")));

			jLabelCodMunic = new JLabel("", JLabel.CENTER);
			jLabelCodMunic
					.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get(
							"LocalGISEIEL",
							"localgiseiel.panels.label.codmunic")));
			
			
			jLabelEntidad = new JLabel("", JLabel.CENTER);
			jLabelEntidad.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.entsing")));
			
			jLabelnucleo = new JLabel("", JLabel.CENTER);
			jLabelnucleo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.nucleo")));

			jPanelIdentificacion.add(jLabelCodProv,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(getJComboBoxProvincia(),
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(jLabelCodMunic,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(getJComboBoxMunicipio(),
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			
			jPanelIdentificacion.add(jLabelEntidad,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			jPanelIdentificacion.add(getJComboBoxEntidad(),
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			
			jPanelIdentificacion.add(jLabelnucleo,
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			jPanelIdentificacion.add(getJComboBoxNucleo(),
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		return jPanelIdentificacion;
	}

	/**
	 * This method initializes jPanelDatosIdentificacion
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelDatosInformacion() {
		if (jPanelInformacion == null) {
			jPanelInformacion = new JPanel(new GridBagLayout());
			jPanelInformacion.setBorder(BorderFactory
					.createTitledBorder(null, I18N.get("LocalGISEIEL",
							"localgiseiel.panels.informacion"),
							TitledBorder.LEADING, TitledBorder.TOP, new Font(
									null, Font.BOLD, 12)));
			
			jLabelEntidadAgrupada = new JLabel("", JLabel.CENTER);
			jLabelEntidadAgrupada.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.entidadagrupada")));

			jLabelnucleoAgrupado = new JLabel("", JLabel.CENTER);
			jLabelnucleoAgrupado.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.nucleoagrupado")));
			
			
			jPanelInformacion.add(jLabelEntidadAgrupada,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(getjTextEntidadAgrupada(),
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelnucleoAgrupado,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(getjTextNucleoAgrupado(),
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			
			
			
			
			
			
			
		}
		return jPanelInformacion;
	}

	

	public void okPressed() {
		okPressed = true;
	}

	public boolean getOkPressed() {
		return okPressed;
	}

	public boolean datosMinimosYCorrectos() {

		 return  (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
	        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0) &&
	        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&
	        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0) &&
	        (jTextEntidadAgrupada!=null && !jTextEntidadAgrupada.getText().equals(""))&& 
	        (jTextNucleoAgrupado!=null && !jTextNucleoAgrupado.getText().equals(""));
	}

	public void enter() {

		loadData();
		loadDataIdentificacion();
	}

	public void exit() {

	}

	public void loadDataIdentificacion() {

		Object obj = AppContext.getApplicationContext().getBlackboard()
				.get("featureDialog");
		if (obj != null && obj instanceof FeatureDialog) {
			FeatureDialog featureDialog = (FeatureDialog) obj;
			Feature feature = featureDialog.get_fieldPanel()
					.getModifiedFeature();

			GeopistaSchema esquema = (GeopistaSchema) feature.getSchema();
			feature.getAttribute(esquema.getAttributeByColumn("id"));

		

			String codprov = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("codprov")) != null) {
				codprov = (feature.getAttribute(esquema
						.getAttributeByColumn("codprov"))).toString();
			}

			String codmunic = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("codmunic")) != null) {
				codmunic = (feature.getAttribute(esquema
						.getAttributeByColumn("codmunic"))).toString();
			}

			String orden_ca = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("orden_ca")) != null) {
				orden_ca = (feature.getAttribute(esquema
						.getAttributeByColumn("orden_ca"))).toString();
			}

			EdicionOperations operations = new EdicionOperations();
			loadData(operations.getPanelEntidadesAgrupadasEIEL(codmunic));

			loadDataIdentificacion( codprov, codmunic);
		}
	}

	public void loadDataIdentificacion( String codprov,	String codmunic) {

		// Datos identificacion
	

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
					if 	(((Municipio) jComboBoxMunicipio.getItemAt(i)).getIdIne()==null)
						continue;
					
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
