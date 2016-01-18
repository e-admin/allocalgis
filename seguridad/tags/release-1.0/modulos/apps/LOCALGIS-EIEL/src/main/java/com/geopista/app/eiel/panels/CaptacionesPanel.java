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
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.CaptacionesDialog;
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

public class CaptacionesPanel extends JPanel implements FeatureExtendedPanel {

	private static final long serialVersionUID = 1L;

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	private Blackboard Identificadores = aplicacion.getBlackboard();

	private CaptacionesEIEL captacion = null;

	private JPanel jPanelIdentificacion = null;
	private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;

	private boolean okPressed = false;

	private JLabel jLabelCodProv = null;
	private JLabel jLabelClave = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelOrden = null;
	private JTextField jTextFieldOrden = null;
	private JLabel jLabelNombre = null;
	private JLabel jLabelTipo = null;
	private JLabel jLabelTitular = null;
	private JLabel jLabelGestor = null;
	private JLabel jLabelSistCapta = null;
	private JLabel jLabelEstado = null;
	private JLabel jLabelUso = null;
	private JLabel jLabelProteccion = null;
	private JLabel jLabelContador = null;
	private JLabel jLabelObserv = null;
	private JTextField jTextFieldNombre = null;
	private ComboBoxEstructuras jComboBoxTipo = null;
	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private ComboBoxEstructuras jComboBoxSistImp = null;
	private ComboBoxEstructuras jComboBoxUso = null;
	private ComboBoxEstructuras jComboBoxProteccion = null;
	private ComboBoxEstructuras jComboBoxContador = null;
	private JTextField jTextFieldObserv = null;
	private JTextField jTextFieldFecha = null;
	private DateField jTextFieldFechaInst = null;
	private ComboBoxEstructuras jComboBoxEst = null;
	private JTextField jTextFieldClave = null;
	private JComboBox jComboBoxProvincia = null;
	private JComboBox jComboBoxMunicipio = null;
	private JLabel jLabelFechaInst = null;
	private JLabel jLabelFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;
	private JLabel jLabelEstadoRevision = null;

	private String idMunicipioSelected;

	/**
	 * This method initializes
	 * 
	 */
	public CaptacionesPanel() {
		super();
		initialize();
	}

	public void loadData() {
		Object object = AppContext.getApplicationContext().getBlackboard().get("captacion_panel");
		if (object != null && object instanceof CaptacionesEIEL) {
			CaptacionesEIEL elemento = (CaptacionesEIEL) object;

			this.captacion = elemento;

			// Datos identificacion
			if (elemento.getClave() != null) {
				jTextFieldClave.setText(elemento.getClave());
			} else {
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

			if (elemento.getCodOrden() != null) {
				jTextFieldOrden.setText(elemento.getCodOrden());
			} else {
				jTextFieldOrden.setText("");
			}

			if (elemento.getNombre() != null) {
				jTextFieldNombre.setText(elemento.getNombre());
			} else {
				jTextFieldNombre.setText("");
			}

			if (elemento.getTipo() != null) {
				jComboBoxTipo.setSelectedPatron(elemento.getTipo());
			} else {
				jComboBoxTipo.setSelectedIndex(0);
			}

			if (elemento.getTitularidad() != null) {
				jComboBoxTitular.setSelectedPatron(elemento.getTitularidad());
			} else {
				jComboBoxTitular.setSelectedIndex(0);
			}

			if (elemento.getGestion() != null) {
				jComboBoxGestor.setSelectedPatron(elemento.getGestion());
			} else {
				jComboBoxGestor.setSelectedIndex(0);
			}

			if (elemento.getSistema() != null) {
				jComboBoxSistImp.setSelectedPatron(elemento.getSistema());
			} else {
				jComboBoxSistImp.setSelectedIndex(0);
			}

			if (elemento.getEstado() != null) {
				jComboBoxEst.setSelectedPatron(elemento.getEstado());
			} else {
				jComboBoxEst.setSelectedIndex(0);
			}

			if (elemento.getTipoUso() != null) {
				jComboBoxUso.setSelectedPatron(elemento.getTipoUso());
			} else {
				jComboBoxUso.setSelectedIndex(0);
			}

			if (elemento.getProteccion() != null) {
				jComboBoxProteccion.setSelectedPatron(elemento.getProteccion());
			} else {
				jComboBoxProteccion.setSelectedIndex(0);
			}

			if (elemento.getContador() != null) {
				jComboBoxContador.setSelectedPatron(elemento.getContador());
			} else {
				jComboBoxContador.setSelectedIndex(0);
			}

			if (elemento.getObservaciones() != null) {
				jTextFieldObserv
						.setText(elemento.getObservaciones().toString());
			} else {
				jTextFieldObserv.setText("");
			}

			if (elemento.getFechaRevision() != null
					&& elemento.getFechaRevision().equals(new java.util.Date())) {
				jTextFieldFecha.setText(elemento.getFechaRevision().toString());
			} else {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = new java.util.Date();
				String datetime = dateFormat.format(date);

				jTextFieldFecha.setText(datetime);
			}

			if (elemento.getFechaInst() != null) {
				jTextFieldFechaInst.setDate(elemento.getFechaInst());
			} else {
				jTextFieldFechaInst.setDate(null);
			}

			if (elemento.getEstadoRevision() != null) {
				jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision()
						.toString());
			} else {
				jComboBoxEstado.setSelectedIndex(0);
			}

		}
	}

	public void loadData(CaptacionesEIEL panelCaptacionEIEL) {

		if (panelCaptacionEIEL != null) {

			idMunicipioSelected = panelCaptacionEIEL.getCodINEMunicipio();
			// Datos identificacion
			
			if (panelCaptacionEIEL.getClave() != null) {
				jTextFieldClave.setText(panelCaptacionEIEL.getClave());
			} else {
				jTextFieldClave.setText("");
			}
			if (panelCaptacionEIEL.getCodINEProvincia() != null) {
				// jComboBoxProvincia.setSelectedItem(panelCaptacionEIEL.getCodINEProvincia());
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(panelCaptacionEIEL
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}
			
			if (panelCaptacionEIEL.getCodINEMunicipio() != null) {
				// jComboBoxMunicipio.setSelectedItem(panelCaptacionEIEL.getCodINEMunicipio());
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(panelCaptacionEIEL
								.getCodINEMunicipio()));

			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getCodOrden() != null) {
				jTextFieldOrden.setText(panelCaptacionEIEL.getCodOrden());
			} else {
				jTextFieldOrden.setText("");
			}

			if (panelCaptacionEIEL.getNombre() != null) {
				jTextFieldNombre.setText(panelCaptacionEIEL.getNombre());
			} else {
				jTextFieldNombre.setText("");
			}

			if (panelCaptacionEIEL.getTipo() != null) {
				jComboBoxTipo.setSelectedPatron(panelCaptacionEIEL.getTipo());
			} else {
				jComboBoxTipo.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getTitularidad() != null) {
				jComboBoxTitular.setSelectedPatron(panelCaptacionEIEL
						.getTitularidad());
			} else {
				jComboBoxTitular.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getGestion() != null) {
				jComboBoxGestor.setSelectedPatron(panelCaptacionEIEL
						.getGestion());
			} else {
				jComboBoxGestor.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getSistema() != null) {
				jComboBoxSistImp.setSelectedPatron(panelCaptacionEIEL
						.getSistema());
			} else {
				jComboBoxSistImp.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getEstado() != null) {
				jComboBoxEst.setSelectedPatron(panelCaptacionEIEL.getEstado());
			} else {
				jComboBoxEst.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getTipoUso() != null) {
				jComboBoxUso.setSelectedPatron(panelCaptacionEIEL.getTipoUso());
			} else {
				jComboBoxUso.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getProteccion() != null) {
				jComboBoxProteccion.setSelectedPatron(panelCaptacionEIEL
						.getProteccion());
			} else {
				jComboBoxProteccion.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getContador() != null) {
				jComboBoxContador.setSelectedPatron(panelCaptacionEIEL
						.getContador());
			} else {
				jComboBoxContador.setSelectedIndex(0);
			}

			if (panelCaptacionEIEL.getObservaciones() != null) {
				jTextFieldObserv.setText(panelCaptacionEIEL.getObservaciones()
						.toString());
			} else {
				jTextFieldObserv.setText("");
			}

			if (panelCaptacionEIEL.getFechaRevision() != null
					&& panelCaptacionEIEL.getFechaRevision().equals(
							new java.util.Date())) {
				jTextFieldFecha.setText(panelCaptacionEIEL.getFechaRevision()
						.toString());
			} else {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = new java.util.Date();
				String datetime = dateFormat.format(date);

				jTextFieldFecha.setText(datetime);
			}

			if (panelCaptacionEIEL.getFechaInst() != null) {
				jTextFieldFechaInst.setDate(panelCaptacionEIEL.getFechaInst());
			} else {
				jTextFieldFechaInst.setDate(null);
			}

			if (panelCaptacionEIEL.getEstadoRevision() != null) {
				jComboBoxEstado.setSelectedPatron(panelCaptacionEIEL
						.getEstadoRevision().toString());
			} else {
				jComboBoxEstado.setSelectedIndex(0);
			}
		
		} else {
			// elemento a cargar es null....
			// se carga por defecto la clave, el codigo de provincia y el codigo
			// de municipio

			jTextFieldClave.setText(ConstantesLocalGISEIEL.CAPTACIONES_ClAVE);

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

	public CaptacionesEIEL getCaptacionData() {

		CaptacionesEIEL elemento = new CaptacionesEIEL();

		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		elemento.setIdMunicipio(Integer
				.parseInt(ConstantesLocalGISEIEL.idMunicipio));

		if (jTextFieldClave.getText() != null) {
			elemento.setClave(jTextFieldClave.getText());
		}

		if (jTextFieldOrden.getText() != null) {
			elemento.setCodOrden(jTextFieldOrden.getText());
		}

		if (jTextFieldNombre.getText() != null) {
			elemento.setNombre(jTextFieldNombre.getText());
		}

		if (jComboBoxTipo.getSelectedPatron() != null)
			elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
		else
			elemento.setTipo("");
		if (jComboBoxTitular.getSelectedPatron() != null)
			elemento.setTitularidad((String) jComboBoxTitular
					.getSelectedPatron());
		else
			elemento.setTitularidad("");
		if (jComboBoxGestor.getSelectedPatron() != null)
			elemento.setGestion((String) jComboBoxGestor.getSelectedPatron());
		else
			elemento.setGestion("");
		if (jComboBoxSistImp.getSelectedPatron() != null)
			elemento.setSistema((String) jComboBoxSistImp.getSelectedPatron());
		else
			elemento.setSistema("");
		if (jComboBoxEst.getSelectedPatron() != null)
			elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
		else
			elemento.setEstado("");
		if (jComboBoxUso.getSelectedPatron() != null)
			elemento.setTipoUso((String) jComboBoxUso.getSelectedPatron());
		else
			elemento.setTipoUso("");
		if (jComboBoxProteccion.getSelectedPatron() != null)
			elemento.setProteccion((String) jComboBoxProteccion
					.getSelectedPatron());
		else
			elemento.setProteccion("");
		if (jComboBoxContador.getSelectedPatron() != null)
			elemento.setContador((String) jComboBoxContador.getSelectedPatron());
		else
			elemento.setContador("");

		if (jTextFieldObserv.getText() != null) {
			elemento.setObservaciones(jTextFieldObserv.getText());
		}

		if (jTextFieldFecha.getText() != null
				&& !jTextFieldFecha.getText().equals("")) {
			String fechas = jTextFieldFecha.getText();
			String anio = fechas.substring(0, 4);
			String mes = fechas.substring(5, 7);
			String dia = fechas.substring(8, 10);

			java.util.Date fecha = new java.util.Date(
					Integer.parseInt(anio) - 1900, Integer.parseInt(mes) - 1,
					Integer.parseInt(dia));
			elemento.setFechaRevision(new Date(fecha.getYear(), fecha
					.getMonth(), fecha.getDate()));

		} else {
			elemento.setFechaRevision(null);
		}

		if (jTextFieldFechaInst.getDate() != null
				&& !jTextFieldFechaInst.getDate().toString().equals("")) {

			java.sql.Date sqlDate = new java.sql.Date(getJTextFieldFechaInst()
					.getDate().getYear(), getJTextFieldFechaInst().getDate()
					.getMonth(), getJTextFieldFechaInst().getDate().getDate());

			elemento.setFechaInst(sqlDate);
		} else {
			elemento.setFechaInst(null);
		}

		if (jComboBoxEstado.getSelectedPatron() != null)
			elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado
					.getSelectedPatron()));

		return elemento;
	}

	public CaptacionesEIEL getCaptacion(CaptacionesEIEL elemento) {

		if (okPressed) {
			if (elemento == null) {
				elemento = new CaptacionesEIEL();
			}

			elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());
			elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
					.getSelectedItem()).getIdIne());

			elemento.setIdMunicipio(Integer
					.parseInt(ConstantesLocalGISEIEL.idMunicipio));

			if (jTextFieldClave.getText() != null) {
				elemento.setClave(jTextFieldClave.getText());
			}

			if (jTextFieldOrden.getText() != null) {
				elemento.setCodOrden(jTextFieldOrden.getText());
			}

			if (jTextFieldNombre.getText() != null) {
				elemento.setNombre(jTextFieldNombre.getText());
			}

			if (jComboBoxTipo.getSelectedPatron() != null)
				elemento.setTipo((String) jComboBoxTipo.getSelectedPatron());
			else
				elemento.setTipo("");
			if (jComboBoxTitular.getSelectedPatron() != null)
				elemento.setTitularidad((String) jComboBoxTitular
						.getSelectedPatron());
			else
				elemento.setTitularidad("");
			if (jComboBoxGestor.getSelectedPatron() != null)
				elemento.setGestion((String) jComboBoxGestor
						.getSelectedPatron());
			else
				elemento.setGestion("");
			if (jComboBoxSistImp.getSelectedPatron() != null)
				elemento.setSistema((String) jComboBoxSistImp
						.getSelectedPatron());
			else
				elemento.setSistema("");
			if (jComboBoxEst.getSelectedPatron() != null)
				elemento.setEstado((String) jComboBoxEst.getSelectedPatron());
			else
				elemento.setEstado("");
			if (jComboBoxUso.getSelectedPatron() != null)
				elemento.setTipoUso((String) jComboBoxUso.getSelectedPatron());
			else
				elemento.setTipoUso("");
			if (jComboBoxProteccion.getSelectedPatron() != null)
				elemento.setProteccion((String) jComboBoxProteccion
						.getSelectedPatron());
			else
				elemento.setProteccion("");
			if (jComboBoxContador.getSelectedPatron() != null)
				elemento.setContador((String) jComboBoxContador
						.getSelectedPatron());
			else
				elemento.setContador("");

			if (jTextFieldObserv.getText() != null) {
				elemento.setObservaciones(jTextFieldObserv.getText());
			}

			if (jTextFieldFecha.getText() != null
					&& !jTextFieldFecha.getText().equals("")) {
				String fechas = jTextFieldFecha.getText();
				String anio = fechas.substring(0, 4);
				String mes = fechas.substring(5, 7);
				String dia = fechas.substring(8, 10);

				java.util.Date fecha = new java.util.Date(
						Integer.parseInt(anio) - 1900,
						Integer.parseInt(mes) - 1, Integer.parseInt(dia));
				elemento.setFechaRevision(new Date(fecha.getYear(), fecha
						.getMonth(), fecha.getDate()));

			} else {
				elemento.setFechaRevision(null);
			}

			if (jTextFieldFechaInst.getDate() != null
					&& !jTextFieldFechaInst.getDate().toString().equals("")) {

				java.sql.Date sqlDate = new java.sql.Date(
						getJTextFieldFechaInst().getDate().getYear(),
						getJTextFieldFechaInst().getDate().getMonth(),
						getJTextFieldFechaInst().getDate().getDate());

				elemento.setFechaInst(sqlDate);
			} else {
				elemento.setFechaInst(null);
			}

			if (jComboBoxEstado.getSelectedPatron() != null)
				elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado
						.getSelectedPatron()));

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
		this.setSize(CaptacionesDialog.DIM_X, CaptacionesDialog.DIM_Y);

		this.add(getJPanelDatosIdentificacion(), new GridBagConstraints(0, 0,
				1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		
		this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 1, 1,
				1, 0.1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

		this.add(getJPanelDatosRevision(), new GridBagConstraints(0, 2, 1, 1,
				0.1, 0.1, GridBagConstraints.CENTER,
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

	public JTextField getJTextFieldClave() {
		if (jTextFieldClave == null) {
			jTextFieldClave = new TextField(2);
			jTextFieldClave.setEnabled(false);
		}
		return jTextFieldClave;
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

	private JTextField getJTextFieldOrden() {
		if (jTextFieldOrden == null) {
			jTextFieldOrden = new TextField(3);
			jTextFieldOrden.setEnabled(false);
		}
		return jTextFieldOrden;
	}

	private JTextField getJTextFieldNombre() {
		if (jTextFieldNombre == null) {
			jTextFieldNombre = new TextField(40);
			jTextFieldNombre.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent evt) {
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldNombre,
							40, aplicacion.getMainFrame());
				}
			});
		}
		return jTextFieldNombre;
	}

	private ComboBoxEstructuras getJComboBoxTipo() {
		if (jComboBoxTipo == null) {
			Estructuras.cargarEstructura("eiel_Tipo de Captación");
			jComboBoxTipo = new ComboBoxEstructuras(
					Estructuras.getListaTipos(), null, aplicacion.getString(
							AppContext.GEOPISTA_LOCALE_KEY, "es_ES"), false);

			jComboBoxTipo.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxTipo;
	}

	private ComboBoxEstructuras getJComboBoxTitular() {
		if (jComboBoxTitular == null) {
			Estructuras.cargarEstructura("eiel_Titularidad");
			jComboBoxTitular = new ComboBoxEstructuras(
					Estructuras.getListaTipos(), null, aplicacion.getString(
							AppContext.GEOPISTA_LOCALE_KEY, "es_ES"), false);

			jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxTitular;
	}

	private ComboBoxEstructuras getJComboBoxGestor() {
		if (jComboBoxGestor == null) {
			Estructuras.cargarEstructura("eiel_Gestión");
			jComboBoxGestor = new ComboBoxEstructuras(
					Estructuras.getListaTipos(), null, aplicacion.getString(
							AppContext.GEOPISTA_LOCALE_KEY, "es_ES"), false);

			jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxGestor;
	}

	private ComboBoxEstructuras getJComboBoxSistImp() {
		if (jComboBoxSistImp == null) {
			Estructuras.cargarEstructura("eiel_sist_impulsion");
			jComboBoxSistImp = new ComboBoxEstructuras(
					Estructuras.getListaTipos(), null, aplicacion.getString(
							AppContext.GEOPISTA_LOCALE_KEY, "es_ES"), false);

			jComboBoxSistImp.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxSistImp;
	}

	private ComboBoxEstructuras getJComboBoxUso() {
		if (jComboBoxUso == null) {
			Estructuras.cargarEstructura("eiel_Tipo de uso CA");
			jComboBoxUso = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,
							"es_ES"), false);

			jComboBoxUso.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxUso;
	}

	private ComboBoxEstructuras getJComboBoxProteccion() {
		if (jComboBoxProteccion == null) {
			Estructuras.cargarEstructura("eiel_Proteccion CA");
			jComboBoxProteccion = new ComboBoxEstructuras(
					Estructuras.getListaTipos(), null, aplicacion.getString(
							AppContext.GEOPISTA_LOCALE_KEY, "es_ES"), false);

			jComboBoxProteccion.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxProteccion;
	}

	private ComboBoxEstructuras getJComboBoxContador() {
		if (jComboBoxContador == null) {
			Estructuras.cargarEstructura("eiel_Contador Abast");
			jComboBoxContador = new ComboBoxEstructuras(
					Estructuras.getListaTipos(), null, aplicacion.getString(
							AppContext.GEOPISTA_LOCALE_KEY, "es_ES"), false);

			jComboBoxContador.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxContador;
	}

	private JTextField getJTextFieldObserv() {
		if (jTextFieldObserv == null) {
			jTextFieldObserv = new TextField(50);
			jTextFieldObserv.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent evt) {
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv,
							50, aplicacion.getMainFrame());
				}
			});
		}
		return jTextFieldObserv;
	}

	private JTextField getJTextFieldFecha() {
		if (jTextFieldFecha == null) {
			jTextFieldFecha = new TextField();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = new java.util.Date();
			String datetime = dateFormat.format(date);
			jTextFieldFecha.setText(datetime);

		}
		return jTextFieldFecha;
	}

	private DateField getJTextFieldFechaInst() {
		if (jTextFieldFechaInst == null) {
			jTextFieldFechaInst = new DateField((java.util.Date) null, 0);
			jTextFieldFechaInst.setDateFormatString("yyyy-MM-dd");
		}
		return jTextFieldFechaInst;
	}

	private ComboBoxEstructuras getJComboBoxEst() {
		if (jComboBoxEst == null) {
			Estructuras.cargarEstructura("eiel_Estado de conservación");
			jComboBoxEst = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,
							"es_ES"), false);

			jComboBoxEst.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxEst;
	}

	private ComboBoxEstructuras getJComboBoxEstado() {
		if (jComboBoxEstado == null) {
			Estructuras.cargarEstructura("eiel_Estado de revisión");
			jComboBoxEstado = new ComboBoxEstructuras(
					Estructuras.getListaTipos(), null, aplicacion.getString(
							AppContext.GEOPISTA_LOCALE_KEY, "es_ES"), true);

			jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxEstado;
	}

	public CaptacionesPanel(GridBagLayout layout) {
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

			jLabelClave = new JLabel("", JLabel.CENTER);
			jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get(
					"LocalGISEIEL", "localgiseiel.panels.label.clave")));

			jLabelCodProv = new JLabel("", JLabel.CENTER);
			jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N
					.get("LocalGISEIEL", "localgiseiel.panels.label.codprov")));

			jLabelCodMunic = new JLabel("", JLabel.CENTER);
			jLabelCodMunic
					.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get(
							"LocalGISEIEL",
							"localgiseiel.panels.label.codmunic")));

			jLabelOrden = new JLabel("", JLabel.CENTER);
			jLabelOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get(
					"LocalGISEIEL", "localgiseiel.panels.label.orden")));

			jPanelIdentificacion.add(jLabelClave,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(getJTextFieldClave(),
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(jLabelCodProv,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(getJComboBoxProvincia(),
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			//getJComboBoxProvincia().setEnabled(false);

			jPanelIdentificacion.add(jLabelCodMunic,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(getJComboBoxMunicipio(),
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(jLabelOrden,
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelIdentificacion.add(getJTextFieldOrden(),
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

			jLabelNombre = new JLabel("", JLabel.CENTER);
			jLabelNombre.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.nombre"));

			jLabelTipo = new JLabel("", JLabel.CENTER);
			jLabelTipo.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.tipo"));

			jLabelTitular = new JLabel("", JLabel.CENTER);
			jLabelTitular.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.titular"));

			jLabelGestor = new JLabel("", JLabel.CENTER);
			jLabelGestor.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.gestor"));

			jLabelSistCapta = new JLabel("", JLabel.CENTER);
			jLabelSistCapta.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.sistcapta"));

			jLabelEstado = new JLabel("", JLabel.CENTER);
			jLabelEstado.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.estado_captacion"));

			jLabelUso = new JLabel("", JLabel.CENTER);
			jLabelUso.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.uso"));

			jLabelProteccion = new JLabel("", JLabel.CENTER);
			jLabelProteccion.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.proteccion"));

			jLabelContador = new JLabel("", JLabel.CENTER);
			jLabelContador.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.contador"));

			jLabelObserv = new JLabel("", JLabel.CENTER);
			jLabelObserv.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.observ"));

			jPanelInformacion.add(jLabelNombre,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJTextFieldNombre(),
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelTipo,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxTipo(),
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelTitular,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxTitular(),
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelGestor,
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxGestor(),
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelSistCapta,
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxSistImp(),
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelEstado,
					new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxEst(),
					new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelUso,
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxUso(),
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelProteccion,
					new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxProteccion(),
					new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelContador,
					new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJComboBoxContador(),
					new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelInformacion.add(jLabelObserv,
					new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 5, 0));

			jPanelInformacion.add(getJTextFieldObserv(),
					new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

		}
		return jPanelInformacion;
	}

	private JPanel getJPanelDatosRevision() {
		if (jPanelRevision == null) {
			jPanelRevision = new JPanel(new GridBagLayout());
			jPanelRevision.setBorder(BorderFactory.createTitledBorder(null,
					I18N.get("LocalGISEIEL", "localgiseiel.panels.revision"),
					TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
							Font.BOLD, 12)));

			jLabelFecha = new JLabel("", JLabel.CENTER);
			jLabelFecha.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.fecha"));

			jLabelFechaInst = new JLabel("", JLabel.CENTER);
			jLabelFechaInst.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.fechainst"));

			jLabelEstadoRevision = new JLabel("", JLabel.CENTER);
			jLabelEstadoRevision.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.panels.label.estado"));

			jPanelRevision.add(jLabelFecha,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelRevision.add(getJTextFieldFecha(),
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			jPanelRevision.add(jLabelFechaInst,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelRevision.add(getJTextFieldFechaInst(),
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
			jPanelRevision.add(jLabelEstadoRevision,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jPanelRevision.add(getJComboBoxEstado(),
					new GridBagConstraints(1, 1, 2, 1, 0.1, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

		}
		return jPanelRevision;
	}

	public void okPressed() {
		okPressed = true;
	}

	public boolean getOkPressed() {
		return okPressed;
	}

	public boolean datosMinimosYCorrectos() {

		return ((jTextFieldClave.getText() != null && !jTextFieldClave
				.getText().equalsIgnoreCase(""))
				&& (jTextFieldOrden.getText() != null && !jTextFieldOrden
						.getText().equalsIgnoreCase(""))
				&& (jComboBoxProvincia != null
						&& jComboBoxProvincia.getSelectedItem() != null && jComboBoxProvincia
						.getSelectedIndex() > 0) && (jComboBoxMunicipio != null
				&& jComboBoxMunicipio.getSelectedItem() != null && jComboBoxMunicipio
				.getSelectedIndex() > 0));

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

			String clave = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("clave")) != null) {
				clave = (feature.getAttribute(esquema
						.getAttributeByColumn("clave"))).toString();
			}

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
			loadData(operations.getPanelCaptacionEIEL(clave, codprov, codmunic,
					orden_ca));

			loadDataIdentificacion(clave, codprov, codmunic, orden_ca);
		}
	}

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_ca) {

		// Datos identificacion
		if (clave != null) {
			jTextFieldClave.setText(clave);
		} else {
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

		if (orden_ca != null) {
			jTextFieldOrden.setText(orden_ca);
		} else {
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
}
