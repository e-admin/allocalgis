package com.geopista.app.inventario.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.BienesPreAltaTableModel;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.licencias.CUtilidadesComponentes_LCGIII;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienPreAltaBean;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.protocol.inventario.SemovienteBean;
import com.geopista.protocol.inventario.ValorMobiliarioBean;
import com.geopista.protocol.inventario.VehiculoBean;
import com.geopista.protocol.inventario.ViaBean;

import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class BienesPreAltaJPanel extends JDialog {

	Logger logger = Logger.getLogger(BienesPreAltaJPanel.class);
	private AppContext aplicacion;
	private InventarioClient inventarioClient;

	private static final int TAM_Y = 500;
	private static final int TAM_X = 800;

	static final int INMUEBLE = 0;
	static final int MUEBLE = 1;
	static final int DERECHOS_REALES = 2;
	static final int VALOR_MOBILIARIO = 3;
	static final int CREDITO_DERECHO = 4;
	static final int SEMOVIENTE = 5;
	static final int VIAS = 6;
	static final int VEHICULOS = 7;

	// private JInternalFrame desktopInternal;
	private ResourceBundle literales;
	private JFrame desktop;
	private JButton aceptarJButton;
	private JButton salirJButton;
	// private CBienesTableModel bienesTableModel;
	// private TableSorted bienesPASorted;
	private BienesPAltaJTable bienesPAltaJTable;
	private JScrollPane bienesPAJScrollPane;
	private BienesPreAltaTableModel bienesTM;
	private TableSorted bienesSorted;
	private String locale;
	private BienPreAltaBean bienPAseleccionado;
	private Object bienSeleccionado;

	// private JTextArea bienesPAJTArea;

	public BienesPreAltaJPanel(JFrame main, ResourceBundle literales) {
		// TODO Auto-generated constructor stub
		super(main, true);
		// this.desktopInternal = desktop;
		this.literales = literales;
		this.desktop = main;
		aplicacion = (AppContext) AppContext.getApplicationContext();
		this.locale= aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);
		inventarioClient = new InventarioClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ Constantes.INVENTARIO_SERVLET_NAME);
		initComponents();
		// init();

	}

	// private void init() {
	// // TODO Auto-generated method stub
	//
	// }

	private void initComponents() {

		JPanel jPanelBotonera = getBotonera();
		JPanel jPanelTabla = getPanelTabla();

		Dimension d = new Dimension(TAM_X, TAM_Y);
		this.setSize(d);
		this.setPreferredSize(d);
		this.setMaximumSize(this.getPreferredSize());
		this.setMinimumSize(this.getPreferredSize());
		// JPanel jPanel = new JPanel();

		this.setLayout(new GridBagLayout());
		this.add(jPanelTabla, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 10), 0, 0));
		this.add(jPanelBotonera, new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				new Insets(0, 10, 0, 10), 0, 0));
		pack();

	}

	private JPanel getBotonera() {

		aceptarJButton = new JButton("Aceptar");
		aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aceptarJButtonActionPerformed(evt);
			}
		});

		salirJButton = new JButton("Salir");
		salirJButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				salirJButtonMouseClicked();
			}
		});

		JPanel jPanel = new JPanel();
		Dimension d = new Dimension(TAM_X, 50);
		jPanel.setSize(d);
		jPanel.setPreferredSize(d);
		jPanel.setLayout(new GridBagLayout());
		jPanel.add(aceptarJButton, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,
						0, 3, 2), 0, 0));
		jPanel.add(salirJButton, new GridBagConstraints(1, 0, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,
						0, 3, 2), 0, 0));

		return jPanel;
	}

	private JPanel getPanelTabla() {

		bienesPAltaJTable = new BienesPAltaJTable();

		try {
			// String[] columnNamesEventos = {
			// literales.getString("CBienesPreALta.bienesTableModel.text.column1"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column2"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column3"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column4"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column5"),
			// "HIDDEN" };

			String[] columnNames = { "Nombre", "Descripcion", "Municipio",
					"Fecha Adquisicion", "Coste Adquisicion", "HIDDEN" };

			// bienesTM.setColumnNames(columnNames);

			bienesTM = new BienesPreAltaTableModel(columnNames, new boolean[] {
					false, false, false, false, false, false }, locale, true);
		} catch (Exception e) {
			logger.error("Error al poner el titulo de las tablas.", e);
		}
		bienesSorted = new TableSorted(bienesTM);
		bienesSorted.setTableHeader(bienesPAltaJTable.getTableHeader());
		bienesPAltaJTable.setModel(bienesSorted);
		bienesTM.setTableSorted(bienesSorted);
		bienesTM.setTable(bienesPAltaJTable);

		bienesPAltaJTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					bienesPAltaJTableMouseClicked();
				}
			}
		});
		setInvisible(bienesTM.getColumnCount() - 1, bienesPAltaJTable);

		bienesPAJScrollPane = new JScrollPane();
		bienesPAJScrollPane.setViewportView(bienesPAltaJTable);
		Dimension d = new Dimension(800, 500);
		bienesPAJScrollPane.setSize(d);
		bienesPAJScrollPane.setPreferredSize(d);
		bienesPAJScrollPane.setBorder(new TitledBorder(
				"Descripci\u00f3n del Bien en Prealta"));

		JPanel bienesJPanel = new JPanel(new GridLayout());

		bienesJPanel.add(bienesPAJScrollPane, new GridBagConstraints(0, 0, 1,
				1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(3, 0, 3, 0), 0, 0));

		return bienesJPanel;
	}

	/**
	 * Método que hace un columna de la tabla no visible
	 */
	private void setInvisible(int column, JTable jTable) {
		/** columna hidden no visible */
		TableColumn col = jTable.getColumnModel().getColumn(column);
		col.setResizable(false);
		col.setWidth(0);
		col.setMaxWidth(0);
		col.setMinWidth(0);
		col.setPreferredWidth(0);
	}

	public boolean rellenaBienes() {

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		boolean sinBienes = true;

		/** Notificaciones */
		ArrayList vBienesPAVector = null;
		try {
			vBienesPAVector = (ArrayList) inventarioClient.getBienesPreAlta();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if ((vBienesPAVector != null) && (vBienesPAVector.size() > 0)) {
			sinBienes = false;
			CUtilidadesComponentes_LCGIII.clearTable(bienesTM);
			for (int i = 0; i < vBienesPAVector.size(); i++) {
				BienPreAltaBean bienPA = (BienPreAltaBean) vBienesPAVector
						.get(i);
				try {
					bienesTM.annadirRow(bienPA);
				} catch (Exception e) {
					logger.error("Error al cargar las bienes en precarga pendientes ");
					e.printStackTrace();
				}
			}
		}
		if (sinBienes) {

			// JOptionPane.showMessageDialog(desktop,
			// literales.getString("CTareasPendientes.mensaje1"));
			CUtilidadesComponentes_LCGIII.clearTable((DefaultTableModel) bienesTM);
			dispose();
			JOptionPane.showMessageDialog(desktop,
					"En este momento no existen bienes en PreAlta");
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return false;
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return true;
	}

	// protected void bienesPAltaJTableKeyReleased(KeyEvent evt) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// protected void bienesPAltaJTableKeyPressed(KeyEvent evt) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// protected void bienesPAltaJTableKeyTyped(KeyEvent evt) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// protected void bienesPAltaJTableMouseDragged() {
	// // TODO Auto-generated method stub
	//
	// }

	public BienPreAltaBean getBienPATable() {
		// TODO Auto-generated method stub
		int selectedRow = bienesPAltaJTable.getSelectedRow();
		if (selectedRow == -1)
			return null;

		return bienesTM.getObjetAt(selectedRow);
	}

	protected void bienesPAltaJTableMouseClicked() {

		bienPAseleccionado = getBienPATable();

		String tipo;
		try {
			tipo = String.valueOf(bienPAseleccionado.getTipo());
			if (tipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)
					|| tipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)) {
				abrirDialogo(INMUEBLE, tipo);
			} else if (tipo.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)
					|| tipo.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)) {
				abrirDialogo(MUEBLE, tipo);
			} else if (tipo.equals(Const.PATRON_DERECHOS_REALES)) {
				abrirDialogo(DERECHOS_REALES, tipo);
			} else if (tipo.equals(Const.PATRON_VALOR_MOBILIARIO)) {
				abrirDialogo(VALOR_MOBILIARIO, tipo);
			} else if (tipo.equals(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)) {
				abrirDialogo(CREDITO_DERECHO, tipo);
			} else if (tipo.equals(Const.PATRON_SEMOVIENTES)) {
				abrirDialogo(SEMOVIENTE, tipo);
			} else if (tipo.equals(Const.PATRON_VIAS_PUBLICAS_URBANAS)
					|| tipo.equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)) {
				abrirDialogo(VIAS, tipo);
			} else if (tipo.equals(Const.PATRON_VEHICULOS)) {
				abrirDialogo(VEHICULOS, tipo);
			}
		} catch (Exception e) {
			logger.error("No se ha abierto el dialogo correctamente");
			e.printStackTrace();
		}

	}

	private void abrirDialogo(int dialogo, String tipo) throws Exception {
		switch (dialogo) {
		case INMUEBLE:
			abrirDialogoInmueble(Constantes.OPERACION_PREALTA, tipo);
			break;
		case MUEBLE:
			abrirDialogoMueble(Constantes.OPERACION_PREALTA, tipo);
			break;
		case DERECHOS_REALES:
			abrirDialogoDerechosReales(Constantes.OPERACION_PREALTA, tipo);
			break;
		case VALOR_MOBILIARIO:
			abrirDialogoValorMobiliario(Constantes.OPERACION_PREALTA, tipo);
			break;
		case CREDITO_DERECHO:
			abrirDialogoCreditoDerecho(Constantes.OPERACION_PREALTA, tipo);
			break;
		case SEMOVIENTE:
			abrirDialogoSemoviente(Constantes.OPERACION_PREALTA, tipo);
			break;
		case VIAS:
			abrirDialogoVias(Constantes.OPERACION_PREALTA, tipo);
			break;
		case VEHICULOS:
			abrirDialogoVehiculo(Constantes.OPERACION_PREALTA, tipo);
			break;

		}
	}

	/**
	 * Abre el dialogo para un bien inmueble
	 * 
	 * @param operacion
	 *            que realiza el usuario
	 * @throws Exception
	 */
	private void abrirDialogoInmueble(final String operacion, String tipo)
			throws Exception {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			InmuebleJDialog inmuebleJDialog = new InmuebleJDialog(desktop,
					locale, operacion, tipo);

			inmuebleJDialog.setTitle(aplicacion
					.getI18nString("inventario.dialogo.tag1"));

			InmuebleBean inmueble = new InmuebleBean();
			inmueble.setTipo(String.valueOf(bienPAseleccionado.getTipo()));
			inmueble.setNombre(bienPAseleccionado.getNombre());
			inmueble.setDescripcion(bienPAseleccionado.getDescripcion());
			inmueble.setFechaAdquisicion(bienPAseleccionado
					.getFechaAdquisicion());
			inmueble.setValorAdquisicionInmueble(bienPAseleccionado
					.getCosteAdquisicion());
			inmueble.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));

			inmuebleJDialog.load(inmueble, true);

			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				inmuebleJDialog.pmsChecked();
			}
			inmuebleJDialog
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pantallaAction(operacion,
									(BienJDialog) e.getSource());
						}
					});

			GUIUtil.centreOnWindow(inmuebleJDialog);
			inmuebleJDialog.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Abre el dialogo de un bien mueble
	 * 
	 * @param operacion
	 *            operacion que realiza el usuario
	 * @param tipo
	 *            de mueble (Hist. Artistico)
	 * @throws Exception
	 */
	private void abrirDialogoMueble(final String operacion, String tipo)
			throws Exception {
		if (tipo == null)
			return;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try {
			MuebleJDialog muebleJDialog = new MuebleJDialog(desktop, locale,
					tipo);
			muebleJDialog.setOperacion(operacion);

			muebleJDialog.setTitle(aplicacion
					.getI18nString("inventario.dialogo.tag1"));
			MuebleBean mueble = new MuebleBean();
			mueble.setTipo(tipo);
			mueble.setNombre(bienPAseleccionado.getNombre());
			mueble.setDescripcion(bienPAseleccionado.getDescripcion());
			mueble.setFechaAdquisicion(bienPAseleccionado.getFechaAdquisicion());
			mueble.setCosteAdquisicion(bienPAseleccionado.getCosteAdquisicion());
			mueble.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));

			muebleJDialog.load(mueble, true);
			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				muebleJDialog.pmsChecked();
			}

			muebleJDialog
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pantallaAction(operacion,
									(BienJDialog) e.getSource());
						}
					});
			GUIUtil.centreOnWindow(muebleJDialog);
			muebleJDialog.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Abre el dialogo de un bien de tipo Derechos Reales
	 * 
	 * @param operacion
	 *            que realiza el usuario
	 * @param tipo
	 * @throws Exception
	 */
	private void abrirDialogoDerechosReales(final String operacion, String tipo)
			throws Exception {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			JDialogDerechosReales jDialogDerechosReales = new JDialogDerechosReales(
					desktop, locale);
			jDialogDerechosReales.setOperacion(operacion);

			jDialogDerechosReales.setTitle(aplicacion
					.getI18nString("inventario.dialogo.tag1"));
			DerechoRealBean derBien = new DerechoRealBean();
			derBien.setTipo(tipo);
			derBien.setNombre(bienPAseleccionado.getNombre());
			derBien.setDescripcion(bienPAseleccionado.getDescripcion());
			derBien.setFechaAdquisicion(bienPAseleccionado
					.getFechaAdquisicion());
			derBien.setCosteAdquisicion(bienPAseleccionado
					.getCosteAdquisicion());
			derBien.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));

			jDialogDerechosReales.load(derBien, true);
			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				jDialogDerechosReales.pmsChecked();
			}

			jDialogDerechosReales
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pantallaAction(operacion,
									(BienJDialog) e.getSource());
						}
					});

			GUIUtil.centreOnWindow(jDialogDerechosReales);
			jDialogDerechosReales.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Abre el dialogo de un bien de tipo Valor Mobiliario
	 * 
	 * @param operacion
	 *            que realiza el usuario
	 * @param tipo
	 * @throws Exception
	 */
	private void abrirDialogoValorMobiliario(final String operacion, String tipo)
			throws Exception {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			JDialogValorMobiliario valorMobiliarioJDialog = new JDialogValorMobiliario(
					desktop, locale);
			valorMobiliarioJDialog.setOperacion(operacion);

			try {
				valorMobiliarioJDialog.setTitle(aplicacion
						.getI18nString("inventario.dialogo.tag1"));
			} catch (Exception e) {
			}
			ValorMobiliarioBean bien = new ValorMobiliarioBean();
			bien.setTipo(tipo);
			bien.setNombre(bienPAseleccionado.getNombre());
			bien.setDescripcion(bienPAseleccionado.getDescripcion());
			bien.setFechaAdquisicion(bienPAseleccionado.getFechaAdquisicion());
			bien.setCosteAdquisicion(bienPAseleccionado.getCosteAdquisicion());
			bien.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));

			valorMobiliarioJDialog.load(bien, true);
			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				valorMobiliarioJDialog.pmsChecked();
			}

			valorMobiliarioJDialog
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pantallaAction(operacion,
									(BienJDialog) e.getSource());
						}
					});

			GUIUtil.centreOnWindow(valorMobiliarioJDialog);
			valorMobiliarioJDialog.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private void abrirDialogoCreditoDerecho(final String operacion, String tipo)
			throws Exception {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			CreditosDerechosJDialog creditosDerechosJDialog = new CreditosDerechosJDialog(
					desktop, locale);
			creditosDerechosJDialog.setOperacion(operacion);

			try {
				creditosDerechosJDialog.setTitle(aplicacion
						.getI18nString("inventario.dialogo.tag1"));
			} catch (Exception e) {
			}
			CreditoDerechoBean credito = new CreditoDerechoBean();
			credito.setTipo(tipo);
			credito.setNombre(bienPAseleccionado.getNombre());
			credito.setDescripcion(bienPAseleccionado.getDescripcion());
			credito.setFechaAdquisicion(bienPAseleccionado
					.getFechaAdquisicion());
			// credito.setCosteAdquisicion(bienPAseleccionado
			// .getCosteAdquisicion());
			credito.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));
			creditosDerechosJDialog.load(credito, true);
			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				creditosDerechosJDialog.pmsChecked();
			}

			creditosDerechosJDialog
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pantallaAction(operacion,
									(BienJDialog) e.getSource());
						}
					});

			GUIUtil.centreOnWindow(creditosDerechosJDialog);
			creditosDerechosJDialog.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private void abrirDialogoSemoviente(final String operacion, String tipo)
			throws Exception {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			SemovienteJDialog semovienteJDialog = new SemovienteJDialog(
					desktop, locale);
			semovienteJDialog.setOperacion(operacion);

			try {
				semovienteJDialog.setTitle(aplicacion
						.getI18nString("inventario.dialogo.tag1"));
			} catch (Exception e) {
			}
			SemovienteBean semoviente = new SemovienteBean();
			semoviente.setTipo(tipo);
			semoviente.setNombre(bienPAseleccionado.getNombre());
			semoviente.setDescripcion(bienPAseleccionado.getDescripcion());
			semoviente.setFechaAdquisicion(bienPAseleccionado
					.getFechaAdquisicion());
			semoviente.setCosteAdquisicion(bienPAseleccionado
					.getCosteAdquisicion());
			semoviente.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));

			semovienteJDialog.load(semoviente, true);
			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				semovienteJDialog.pmsChecked();
			}

			semovienteJDialog
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pantallaAction(operacion,
									(BienJDialog) e.getSource());
						}
					});

			GUIUtil.centreOnWindow(semovienteJDialog);
			semovienteJDialog.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private void abrirDialogoVias(final String operacion, String tipo)
			throws Exception {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			ViasJDialog viasJDialog = new ViasJDialog(desktop, locale, tipo);
			viasJDialog.setOperacion(operacion);

			try {
				viasJDialog.setTitle(aplicacion
						.getI18nString("inventario.dialogo.tag1"));
			} catch (Exception e) {
			}
			ViaBean via = new ViaBean();
			via.setTipo(tipo);
			via.setNombre(bienPAseleccionado.getNombre());
			via.setDescripcion(bienPAseleccionado.getDescripcion());
			via.setFechaAdquisicion(bienPAseleccionado.getFechaAdquisicion());
			// via.setCosteAdquisicion(bienPAseleccionado.getCosteAdquisicion());
			via.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));

			viasJDialog.load(via, true);
			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				viasJDialog.pmsChecked();
			}

			viasJDialog.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					pantallaAction(operacion, (BienJDialog) e.getSource());
				}
			});

			GUIUtil.centreOnWindow(viasJDialog);
			viasJDialog.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private void abrirDialogoVehiculo(final String operacion, String tipo)
			throws Exception {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			VehiculoJDialog vehiculoJDialog = new VehiculoJDialog(desktop,
					locale);
			vehiculoJDialog.setOperacion(operacion);

			vehiculoJDialog.setTitle(aplicacion
					.getI18nString("inventario.dialogo.tag1"));

			VehiculoBean vehiculo = new VehiculoBean();
			vehiculo.setTipo(tipo);
			vehiculo.setNombre(bienPAseleccionado.getNombre());
			vehiculo.setDescripcion(bienPAseleccionado.getDescripcion());
			vehiculo.setFechaAdquisicion(bienPAseleccionado
					.getFechaAdquisicion());
			vehiculo.setCosteAdquisicion(bienPAseleccionado
					.getCosteAdquisicion());
			vehiculo.setIdMunicipio(String.valueOf(bienPAseleccionado
					.getIdMunicipio()));

			vehiculoJDialog.load(vehiculo, true);
			if (tipo.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)) {
				vehiculoJDialog.pmsChecked();
			}

			vehiculoJDialog
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pantallaAction(operacion,
									(BienJDialog) e.getSource());
						}
					});

			GUIUtil.centreOnWindow(vehiculoJDialog);
			vehiculoJDialog.setVisible(true);
		} finally {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void pantallaAction(String operacion, BienJDialog bienJDialog) {
		try {
			logger.info("Modificando el bien");
			if (bienJDialog.getBien() != null) {

				BienBean bienNuevo = bienJDialog.getBien();
				// ASO comprobamos que el numero de inventario es correcto
				if ((bienNuevo.getNumInventario() == null || bienNuevo
						.getNumInventario().trim().length() == 0)) {
					/** Mostramos mensaje de bloqueo del bien */
					JOptionPane.showMessageDialog((JDialog) bienJDialog,
							aplicacion
									.getI18nString("inventario.mensajes.tag5"));
					return;
				}
				BienBean auxBean = inventarioClient
						.getBienByNumInventario(bienNuevo.getNumInventario());

				if (auxBean != null && auxBean.getId() != bienNuevo.getId()) {
					/** Mostramos mensaje de bloqueo del bien */
					JOptionPane.showMessageDialog((JDialog) bienJDialog,
							aplicacion
									.getI18nString("inventario.mensajes.tag6"));
					return;
				}
				Vector documentos = bienJDialog.getDocumentosJPanel()
						.getFilesInUp();
				if (bienJDialog instanceof MuebleJDialog) {
					if (((MuebleJDialog) bienJDialog).getLotePanel() != null) {
						documentos.addAll(((MuebleJDialog) bienJDialog)
								.getLotePanel().getDocumentosJPanel()
								.getFilesInUp());
					}
				}
				bienSeleccionado = inventarioClient.deleteBienPA(new Object[0],
						bienNuevo, bienPAseleccionado, documentos);
				if (bienSeleccionado == null) {
					return;
				} else {
					JOptionPane.showMessageDialog(desktop,
							"Se ha insertado un bien que estaba en PreAlta");
				}
			} else {
				bienSeleccionado = null;
			}

		} catch (Exception e) {
			logger.error("Error al operar con un vehiculo de una feature ", e);
			e.printStackTrace();
		} finally {
			bienJDialog.setVisible(false);
			if (bienSeleccionado != null)
				recargarTablaBienesPA();
		}
	}

	// protected void bienesPAltaJTableFocusGained() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// protected void bienesPAJTableMouseClicked() {
	// JOptionPane.showMessageDialog(desktop, "Clickando en la tabla");
	//
	// }
	//
	// protected void bienesPAJTableFocusGained() {
	// // TODO Auto-generated method stub
	//
	// }

	private void recargarTablaBienesPA() {
		rellenaBienes();
	}

	protected void salirJButtonMouseClicked() {
		// TODO Auto-generated method stub
		dispose();

	}

	protected void aceptarJButtonActionPerformed(ActionEvent evt) {
		//Abrimos el dialogo para el bien pulsado
		bienesPAltaJTableMouseClicked();

	}

	// public void aceptarJButtonSetEnabled(boolean b) {
	// aceptarJButton.setEnabled(b);
	// }

	public void renombrarComponentes(ResourceBundle literales) {
		// TODO Auto-generated method stub

	}

	public class BienesPAltaJTable extends JTable {

		private int bienesHiddenCol = 6;

		public void bienesPAltaJTable() {
			this.setBorder(new LineBorder(new Color(0, 0, 0)));
			this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			this.setFocusCycleRoot(true);
			this.setSurrendersFocusOnKeystroke(true);
			this.getTableHeader().setReorderingAllowed(false);
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			// bienesPAltaJTable.addFocusListener(new FocusAdapter() {
			// public void focusGained() {
			// bienesPAltaJTableFocusGained();
			// }
			// });

			// bienesPAltaJTable.addMouseMotionListener(new MouseMotionAdapter()
			// {
			// public void mouseDragged(MouseEvent evt) {
			// bienesPAltaJTableMouseDragged();
			// }
			// });
			// bienesPAltaJTable.addKeyListener(new KeyAdapter() {
			// public void keyTyped(KeyEvent evt) {
			// bienesPAltaJTableKeyTyped(evt);
			// }
			//
			// public void keyPressed(KeyEvent evt) {
			// bienesPAltaJTableKeyPressed(evt);
			// }
			//
			// public void keyReleased(KeyEvent evt) {
			// bienesPAltaJTableKeyReleased(evt);
			// }
			// });
			//
			// ((TableSorted) bienesPAltaJTable.getModel()).getTableHeader()
			// .addFocusListener(new FocusAdapter() {
			// public void focusGained(FocusEvent evt) {
			// bienesPAJTableFocusGained();
			// }
			// });
			// ((TableSorted) bienesPAltaJTable.getModel()).getTableHeader()
			// .addMouseListener(new MouseAdapter() {
			// public void mouseClicked(MouseEvent evt) {
			// bienesPAJTableMouseClicked();
			// }
			// });

		}

	}

}
