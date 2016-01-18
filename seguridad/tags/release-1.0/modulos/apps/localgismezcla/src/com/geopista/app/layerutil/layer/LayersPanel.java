/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */

package com.geopista.app.layerutil.layer;

/**
 * Panel que permite realizar operaciones de manipulación sobre las layers
 * de GeoPISTA
 * 
 * @author cotesa
 *
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.sourceforge.jtds.util.Logger;
import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.layerutil.GestorCapas;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.app.layerutil.layer.dialogs.ExportImportDialog;
import com.geopista.app.layerutil.layer.exportimport.beans.LocalGISXmlLayer;
import com.geopista.app.layerutil.layer.exportimport.utils.ExportImportUtils;
import com.geopista.app.layerutil.layerfamily.LayerFamilyOperations;
import com.geopista.app.layerutil.layerfamily.LayerFamilyTable;
import com.geopista.app.layerutil.schema.attribute.AttributeEditor;
import com.geopista.app.layerutil.schema.attribute.AttributeRenderer;
import com.geopista.app.layerutil.schema.attribute.TableAttributesCellRenderer;
import com.geopista.app.layerutil.schema.attribute.TableAttributesModel;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.app.layerutil.schema.table.JPanelTables;
import com.geopista.app.layerutil.schema.table.TableRow;
import com.geopista.app.layerutil.util.JDialogTranslations;
import com.geopista.feature.Attribute;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.Table;
import com.geopista.model.LayerFamily;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Acl;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.geopista.protocol.control.ISesion;
import com.geopista.security.GeopistaPermission;
import com.geopista.server.administrador.web.LocalgisLayer;
import com.geopista.server.administrador.web.PublishMapsClient;
import com.geopista.server.administrador.web.PublishMapsPortType;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.util.FeatureExtendedPanel;
import com.thoughtworks.xstream.XStream;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class LayersPanel extends JPanel implements FeatureExtendedPanel,
		TreeSelectionListener {

	public static final int SELECT = 0;
	public static final int UPDATE = 1;
	public static final int INSERT = 2;
	public static final int DELETE = 3;

	public static final int POSTGRE = 1;
	public static final int ORACLE = 2;
	public static int numSQL = 0;

	private int tipoBD = -1;
	private int tipoQuery = -1;

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	public static final String REVISION_ACTUAL = "revision_actual";
	public static final String REVISION_EXPIRADA = "revision_expirada";
	private static final String ICONO_FLECHA_DERECHA = "flecha_derecha.gif";
	private static final String ICONO_FLECHA_IZQUIERDA = "flecha_izquierda.gif";
	private static final String ICONO_BORRAR = "borrar.gif";
	private static final String ICONO_SUBIR = "subir.gif";
	private static final String ICONO_BAJAR = "bajar.gif";
	// private static final Icon sqlIcono= new
	// javax.swing.ImageIcon(cl.getResource("img/sql.gif"));
	private static final String ICONO_BANDERA = "banderas.GIF";
	private static final String SERVICE_TIME = "WMS-T";
	private static final String SERVICE_WMS = "WMS";
	private static final String VALIDATOR_CLASS = "com.geopista.protocol.VersionValidator";

	private boolean fireEvents = true;

	String string1 = I18N.get("GestorCapas", "general.si");
	String string2 = I18N.get("GestorCapas", "general.no");
	Object[] options = { string1, string2 };

	String string1mapa = I18N.get("GestorCapas", "layers.mapas.eliminarcapa");
	String string2mapa = I18N.get("GestorCapas",
			"layers.mapas.cancelareliminar");
	Object[] optionsMapa = { string1mapa, string2mapa };

	private int numFilasSelec = 0;
	private Column[] columnasSeleccionadas = null;

	private JRadioButton rdbModoCreacion = null;
	private JRadioButton rdbModoModificacion = null;
	private JRadioButton rdbModoEliminacion = null;
	private ButtonGroup btnGroup = null;
	private JPanel jPanelRadio = null;

	private JRadioButton rdbAcl = null;
	private JRadioButton rdbNewAcl = null;
	private ButtonGroup btnGroupAcl = null;
	private JPanel jPanelRadioAcl = null;
	private JTextField txtNuevoAcl = null;

	private JComboBox cmbLayer = null;
	private JComboBox cmbLayerFamily = null;
	private JLabel lblNombreCapa = null;
	private JLabel lblAmbito = null;
	// private JLabel lblAcl = null;
	private JLabel lblIdCapa = null;
	private JTextField txtNombreCapa = null;
	private JTextField txtIdCapa = null;
	// private JPanel jPanelLabels = null;
	private JPanelTables jPanelTablas = null;
	private JTextArea txtAreaSQL = null;
	private JScrollPane scrollAtributos = null;
	private JScrollPane scrollListaClaves = null;
	private JScrollPane scrollAreaSQL = null;
	private JTable tblAtributos = null;
	private JList lstForeignKeys = null;
	private JButton btnAnadirAtributo = null;
	private JButton btnEliminarAtributo = null;
	private JButton btnAnadirClaves = null;
	private JButton btnGenerarSQL = null;
	private JButton btnRecuperarSQL = null;
	private JButton btnProbar = null;
	private JButton btnIdiomas = null;
	private JComboBox cmbBaseDatos = null;
	private JComboBox cmbQuery = null;
	private JComboBox cmbAcl = null;
	private JComboBox cmbAlcance = null;
	private JButton btnGrabar = null;
	private JButton btnSalir = null;
	private JButton btnEliminarLayer = null;
	private JLabel lblTipoBD = null;
	private JLabel lblQuerySQL = null;
	private JButton btnEliminarClave = null;
	private JButton btnSubir = null;
	private JButton btnBajar = null;
	private JButton btnSql = new JButton();
	private JLabel lblLayerFamily = null;
	private JLabel lblLayer = null;
	private JTree treeTablas = new JTree();
	private JCheckBox chbDinamica = null;
	private JCheckBox chbVersionable = null;

	//NUEVO
	private JButton btnExport = null;
	private JButton btnImport = null;
	private JFileChooser fileChooserExport = null;
	private JFileChooser fileChooserImport = null;
	private XStream xStreamSerializer = null;
	private int correct;
	private int total;
	//FIN NUEVO

	private TableAttributesModel tablemodel = new TableAttributesModel();
	private int selectedTableRow = -1;
	private TableRow entrada = new TableRow();

	private DefaultListModel listmodel = new DefaultListModel();
	private int[] selectedListRow;

	private boolean capaNueva = false;
	private String locale = aplicacion.getUserPreference(
			AppContext.GEOPISTA_LOCALE_KEY, GestorCapas.DEFAULT_LOCALE, false);

	private Layer capaSeleccionada = new Layer();
	private LayerTable layerTableSeleccionada = null;
	private int idLayer = 0;
	private LayerTable[] lstCapas = new LayerTable[1];

	private LayerFamily familiaSeleccionada = new LayerFamily();
	private int idLayerFamily = 0;

	private String attGeometryName = new String();

	// Diccionario de nombres de capa
	private Hashtable htNombres = new Hashtable();

	// Objetos Query para cada tipo de BD (generados automaticamente por la
	// aplicacion)
	private Hashtable htQueries = new Hashtable();

	// Objetos Query para cada tipo de BD (originales, almacenados en la BD)
	private Hashtable htQueriesOriginal = new Hashtable();

	// Conjunto de atributos de una capa que desdea eliminar de la misma tras la
	// grabacion
	private HashSet hsAtributosBorrar = new HashSet();

	// Controla si se ha pulsado el boton de generar query automaticamente
	private boolean isQueryGenerada = false;

	// Controla si se ha pulsado el boton de generar query en algun momento para
	// la capa actual
	private boolean isQueriesModificadas = false;

	private Blackboard Identificadores = aplicacion.getBlackboard();

	/**
	 * Evita que al salir de la pestaña se pregunte dos veces si se desea
	 * abandonar
	 */
	private boolean nosale = false;

	/**
	 * This is the default constructor
	 */
	public LayersPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */

	private void initialize() {
		lblLayer = new JLabel();
		lblLayer.setBounds(new Rectangle(200, 50, 135, 24));
		lblLayer.setText(I18N.get("GestorCapas", "layers.seleccionarcapa"));

		lblLayerFamily = new JLabel();
		lblLayerFamily.setBounds(new Rectangle(200, 19, 155, 24));
		lblLayerFamily.setText(I18N.get("GestorCapas",
				"layers.seleccionarlayerfamily"));

		lblQuerySQL = new JLabel();
		lblQuerySQL.setBounds(new Rectangle(520, 450, 125, 20));
		lblQuerySQL.setText(I18N.get("GestorCapas", "layers.tiposql"));

		lblTipoBD = new JLabel();
		lblTipoBD.setBounds(new Rectangle(315, 450, 95, 20));
		lblTipoBD.setText(I18N.get("GestorCapas", "layers.tipobd"));

		lblIdCapa = new JLabel();
		lblIdCapa.setBounds(new Rectangle(500, 50, 119, 24));
		lblIdCapa.setText(I18N.get("GestorCapas", "layers.descripcioncapa"));

		lblNombreCapa = new JLabel();
		lblNombreCapa.setBounds(new Rectangle(500, 19, 119, 24));
		lblNombreCapa.setText(I18N.get("GestorCapas", "layers.nombrecapa"));

		lblAmbito = new JLabel();
		lblAmbito.setBounds(new java.awt.Rectangle(200, 83, 112, 20));
		lblAmbito.setText(I18N.get("GestorCapas", "layers.ambito"));

		chbDinamica = new JCheckBox();
		chbDinamica.setText(I18N.get("GeopistaDynamicLayer"));
		chbDinamica.setBounds(new java.awt.Rectangle(740, 83, 100, 20));

		chbVersionable = new JCheckBox();
		chbVersionable.setText(I18N.get("Versionable"));
		chbVersionable.setBounds(new java.awt.Rectangle(840, 83, 100, 20));

		// Asocia los botones en un ButtonGroup
		btnGroup = new ButtonGroup();
		rdbModoCreacion = getRdbModoCreacion();
		rdbModoCreacion.setEnabled(true);
		btnGroup.add(rdbModoCreacion);
		btnGroup.add(getRdbModoModificacion());
		btnGroup.add(getRdbModoEliminacion());

		jPanelRadio = getJPanelRadio();
		jPanelRadio.setLayout(null);
		jPanelRadio.setBounds(new Rectangle(14, 15, 181, 57));
		jPanelRadio.add(getRdbModoCreacion(), null);
		jPanelRadio.add(getRdbModoModificacion(), null);
		jPanelRadio.add(getRdbModoEliminacion(), null);

		btnGroupAcl = new ButtonGroup();
		rdbAcl = getRdbAcl();
		rdbAcl.setEnabled(true);
		btnGroupAcl.add(rdbAcl);
		btnGroupAcl.add(getRdbNewAcl());
		// btnGroupAcl.add(getRdbModoModificacion());
		// btnGroupAcl.add(getRdbModoEliminacion());

		jPanelRadioAcl = getJPanelRadioAcl();
		jPanelRadioAcl.setLayout(null);
		jPanelRadioAcl.setBounds(new Rectangle(740, 25, 90, 57));
		jPanelRadioAcl.add(getRdbAcl(), null);
		jPanelRadioAcl.add(getRdbNewAcl(), null);
		getTxtNuevoAcl().setEnabled(false);

		jPanelTablas = getJPanelTablas();
		jPanelTablas.setEnabled(false);
		treeTablas = jPanelTablas.getTree();
		treeTablas.addTreeSelectionListener(this);
		txtAreaSQL = getTxtAreaSQL();

		// ////////////////////////////////////////////////
		// se inicializa la zona superior deshabilitada
		Color gris = Color.GRAY;
		lblLayer.setForeground(gris);
		lblLayerFamily.setForeground(gris);
		lblNombreCapa.setForeground(gris);
		lblIdCapa.setForeground(gris);
		lblAmbito.setForeground(gris);
		// lblAcl.setForeground(gris);
		getCmbLayer().setEnabled(false);
		getCmbLayerFamily().setEnabled(false);
		getTxtIdCapa().setEnabled(false);
		getTxtNombreCapa().setEnabled(false);
		getBtnIdiomas().setEnabled(false);
		getCmbAcl().setEnabled(false);
		getCmbAlcance().setEnabled(false);
		// getRdbModoCreacion().setEnabled(true);
		// getRdbModoEliminacion().setEnabled(false);

		// se inicializan la botonera inferior y los combos deshabilitados
		habilitarZonaInferior(false);

		// si esta seleccionado el rdb eliminar se habilita el boton de borrado:
		if (getSelectedRadioButton(btnGroup) == rdbModoEliminacion
				&& cmbLayer.getSelectedIndex() > 0) {
			getBtnEliminarLayer().setEnabled(true);
			getBtnGrabar().setEnabled(false);
			//NUEVO
			hideExportImport();
			//FIN NUEVO
		} else {
			getBtnEliminarLayer().setEnabled(false);
		}

		scrollAtributos = getScrollAtributos();
		scrollListaClaves = getScrollListaClaves();
		scrollAreaSQL = getScrollSql();

		// carga los idiomas del jdialogtraducciones
		// com.geopista.app.administrador.init.Constantes.url="http://127.0.0.1:8081/administracion";
		while (!com.geopista.app.utilidades.estructuras.Estructuras
				.isCargada()) {
			if (!com.geopista.app.utilidades.estructuras.Estructuras
					.isIniciada())
				com.geopista.app.utilidades.estructuras.Estructuras
						.cargarEstructuras();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}

		this.setLayout(null);
		this.setSize(937, 607);

		this.add(lblNombreCapa, null);
		this.add(lblIdCapa, null);
		this.add(lblAmbito, null);
		this.add(jPanelRadio, null);
		this.add(jPanelRadioAcl, null);
		// this.add(jPanelLabels, null);
		this.add(getTxtNombreCapa(), null);
		this.add(getTxtIdCapa(), null);
		this.add(jPanelTablas, null);
		// this.add(getTxtAreaSQL(), null);
		this.add(scrollAtributos, null);
		this.add(scrollListaClaves, null);
		this.add(scrollAreaSQL, null);
		this.add(getLstForeignKeys(), null);
		this.add(getBtnAnadirAtributo(), null);
		this.add(getBtnEliminarAtributo(), null);
		this.add(getBtnAnadirClaves(), null);

		this.add(getBtnRecuperarSQL(), null);
		this.add(getBtnGenerarSQL(), null);
		this.add(getBtnProbar(), null);
		this.add(getCmbBaseDatos(), null);
		this.add(getCmbQuery(), null);
		this.add(getCmbAcl(), null);
		this.add(getCmbAlcance(), null);
		this.add(getBtnGrabar(), null);
		this.add(getBtnEliminarLayer(), null);

		// este era el boton que incluia una nueva linea en la tabla de
		// atributos con todos
		// los elementos vacios
		// this.add(getBtnSql(), null);

		this.add(getJPanelRadio(), null);
		this.add(getJPanelRadioAcl(), null);
		this.add(getBtnSalir(), null);
		this.add(getCmbLayerFamily(), null);
		this.add(getCmbLayer(), null);
		this.add(getCmbAcl(), null);
		this.add(getTxtNuevoAcl(), null);
		this.add(lblTipoBD, null);
		this.add(lblQuerySQL, null);
		// this.add(lblAcl, null);
		this.add(getBtnEliminarClave(), null);
		this.add(getBtnSubir(), null);
		this.add(getBtnBajar(), null);
		this.add(lblLayerFamily, null);
		this.add(lblLayer, null);
		this.add(getBtnIdiomas(), null);
		this.add(chbDinamica, null);
		this.add(chbVersionable, null);
		//NUEVO
		this.add(getBtnExport(), null);
		this.add(getBtnImport(), null);
		//FIN NUEVO
	}

	private void reset() {

		lblLayer = null;
		lblLayerFamily = null;
		lblQuerySQL = null;
		lblTipoBD = null;
		lblIdCapa = null;
		lblAmbito = null;
		lblNombreCapa = null;
		// lblAcl = null;
		btnGroup = null;

		getRdbAcl().removeAll();
		rdbAcl = null;
		getRdbNewAcl().removeAll();
		rdbNewAcl = null;
		getJPanelRadioAcl().removeAll();
		jPanelRadioAcl = null;
		getTxtNuevoAcl().removeAll();
		txtNuevoAcl = null;

		getRdbModoCreacion().removeAll();
		rdbModoCreacion = null;
		getRdbModoModificacion().removeAll();
		rdbModoModificacion = null;
		getRdbModoEliminacion().removeAll();
		rdbModoEliminacion = null;
		getJPanelRadio().removeAll();
		jPanelRadio = null;
		getJPanelTablas().removeAll();
		jPanelTablas = null;
		treeTablas = null;
		txtAreaSQL = null;
		getCmbLayer().removeAll();
		cmbLayer = null;
		getCmbLayerFamily().removeAll();
		cmbLayerFamily = null;
		getTxtIdCapa().removeAll();
		txtIdCapa = null;
		getTxtNombreCapa().removeAll();
		txtNombreCapa = null;
		getBtnIdiomas().removeAll();
		btnIdiomas = null;
		getCmbAcl().removeAll();
		cmbAcl = null;
		getCmbAlcance().removeAll();
		cmbAlcance = null;
		getScrollAtributos().removeAll();
		scrollAtributos = null;
		getScrollListaClaves().removeAll();
		scrollListaClaves = null;
		getLstForeignKeys().removeAll();
		lstForeignKeys = null;
		getBtnAnadirAtributo().removeAll();
		btnAnadirAtributo = null;
		getBtnAnadirClaves().removeAll();
		btnAnadirClaves = null;
		getScrollSql().removeAll();
		scrollAreaSQL = null;
		getTblAtributos().removeAll();
		tblAtributos = null;
		tablemodel = null;
		tablemodel = new TableAttributesModel();

		this.removeAll();

	}

	private JRadioButton getRdbAcl() {
		if (rdbAcl == null) {
			rdbAcl = new JRadioButton();
			rdbAcl.setText(I18N.get("GestorCapas", "layers.acl"));
			rdbAcl.setBounds(new Rectangle(1, 1, 150, 15));
			rdbAcl.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtNuevoAcl.setText("");
					txtNuevoAcl.setEnabled(false);
					cmbAcl.setEnabled(true);
				}
			});
		}
		return rdbAcl;
	}

	private JRadioButton getRdbNewAcl() {
		if (rdbNewAcl == null) {
			rdbNewAcl = new JRadioButton();
			rdbNewAcl.setText(I18N.get("GestorCapas", "layers.nuevoAcl"));
			rdbNewAcl.setBounds(new Rectangle(1, 30, 150, 15));
			rdbNewAcl.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtNuevoAcl.setEnabled(true);
					cmbAcl.setEnabled(false);
				}
			});
		}
		return rdbNewAcl;
	}

	/**
	 * This method initializes rdbModo
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdbModoCreacion() {
		if (rdbModoCreacion == null) {
			rdbModoCreacion = new JRadioButton();
			rdbModoCreacion
					.setText(I18N.get("GestorCapas", "layers.crearcapa"));
			rdbModoCreacion.setBounds(new Rectangle(1, 1, 170, 15));
			rdbModoCreacion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					capaNueva = true;
					controlarAspecto(capaNueva);
					txtNombreCapa.setText("");
					rdbAcl.setSelected(true);
					cmbAcl.setEnabled(true);
					jPanelTablas.setEnabled(true);
					// controlarCombos(capaNueva);
					//NUEVO
					getBtnExport().setVisible(false);
					getBtnImport().setVisible(true);
					//FIN NUEVO
				}
			});
		}
		return rdbModoCreacion;
	}

	/**
	 * This method initializes rdbModoModificacion
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdbModoModificacion() {
		if (rdbModoModificacion == null) {
			rdbModoModificacion = new JRadioButton();
			rdbModoModificacion.setText(I18N.get("GestorCapas",
					"layers.modificarcapa"));
			rdbModoModificacion.setBounds(new Rectangle(1, 21, 170, 15));
			rdbModoModificacion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					capaNueva = false;
					controlarAspecto(capaNueva);
					txtNombreCapa.setText("");
					rdbAcl.setSelected(true);
					cmbAcl.setEnabled(true);
					//NUEVO
					getBtnExport().setVisible(true);
					getBtnImport().setVisible(false);
					//FIN NUEVO
				}
			});
		}
		return rdbModoModificacion;
	}

	/**
	 * This method initializes rdbModoEliminacion
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdbModoEliminacion() {
		if (rdbModoEliminacion == null) {
			rdbModoEliminacion = new JRadioButton();
			rdbModoEliminacion.setText(I18N.get("GestorCapas",
					"layers.eliminarcapa"));
			rdbModoEliminacion.setBounds(new Rectangle(1, 41, 170, 15));
			rdbModoEliminacion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtNombreCapa.setText("");
					capaNueva = false;
					controlarAspecto(capaNueva);
					//NUEVO
					hideExportImport();
					//FIN NUEVO
				}
			});
		}
		return rdbModoEliminacion;
	}

	/**
	 * This method initializes jPanelRadio
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelRadio() {
		if (jPanelRadio == null) {
			jPanelRadio = new JPanel();
			jPanelRadio.setBounds(new Rectangle(266, 5, 10, 10));
		}
		return jPanelRadio;
	}

	private JPanel getJPanelRadioAcl() {
		if (jPanelRadioAcl == null) {
			jPanelRadioAcl = new JPanel();
			jPanelRadioAcl.setBounds(new Rectangle(280, 5, 10, 10));
		}
		return jPanelRadioAcl;
	}

	/**
	 * This method initializes cmbLayer
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbLayer() {
		if (cmbLayer == null) {
			cmbLayer = new JComboBox();
			cmbLayer.setBounds(new Rectangle(357, 51, 130, 20));

			LayersListCellRenderer renderer = new LayersListCellRenderer();			
			cmbLayer.setRenderer(renderer);
			cmbLayer.setFocusable(false);
			
			cmbLayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					cmbLayer_actionPerformed(e);
				}
			});		
		}
		return cmbLayer;
	}

	/**
	 * This method initializes cmbLayerFamily
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbLayerFamily() {
		if (cmbLayerFamily == null) {
			cmbLayerFamily = new JComboBox();
			cmbLayerFamily.setBounds(new Rectangle(357, 19, 130, 20));

			LayersListCellRenderer renderer = new LayersListCellRenderer();
			cmbLayerFamily.setRenderer(renderer);
			cmbLayerFamily.setFocusable(false);

			cmbLayerFamily.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cmbLayerFamily_actionPerformed(e);
				}
			});
		}
		return cmbLayerFamily;
	}

	/**
	 * This method initializes txtNombreCapa
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtNombreCapa() {
		if (txtNombreCapa == null) {
			txtNombreCapa = new JTextField();
			txtNombreCapa.setText("");
			txtNombreCapa.setBounds(new java.awt.Rectangle(600, 19, 98, 20));

			txtNombreCapa.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent evt) {
					htNombres.put(locale, txtNombreCapa.getText());
				}
			});
		}
		return txtNombreCapa;
	}

	/**
	 * This method initializes txtIdCapa
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtIdCapa() {
		if (txtIdCapa == null) {
			txtIdCapa = new JTextField();
			txtIdCapa.setText("");
			txtIdCapa.setBounds(new java.awt.Rectangle(600, 51, 130, 19));
		}
		return txtIdCapa;
	}

	/**
	 * This method initializes jPanelTablas
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanelTables getJPanelTablas() {
		if (jPanelTablas == null) {
			jPanelTablas = new JPanelTables(
					TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			jPanelTablas.setBorder(BorderFactory.createTitledBorder(I18N.get(
					"GestorCapas", "layers.paneltablas.titulo")));
			jPanelTablas.setBounds(new Rectangle(16, 110, 250, 320));
		}
		return jPanelTablas;
	}

	/**
	 * This method initializes scrollAreaSQL
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollSql() {
		scrollAreaSQL = new JScrollPane();
		scrollAreaSQL.setBounds(new Rectangle(21, 479, 840, 106));

		JTextArea txtAreaSQL = getTxtAreaSQL();
		scrollAreaSQL.setViewportView(txtAreaSQL);

		return scrollAreaSQL;
	}

	/**
	 * This method initializes txtAreaSQL
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getTxtAreaSQL() {
		if (txtAreaSQL == null) {
			txtAreaSQL = new JTextArea();
			txtAreaSQL.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		return txtAreaSQL;
	}

	/**
	 * This method initializes scrollAtributos
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollAtributos() {
		if (scrollAtributos == null) {
			scrollAtributos = new JScrollPane();
			scrollAtributos.setBounds(new Rectangle(303, 108, 558, 168));

			JTable table = getTblAtributos();

			scrollAtributos.setViewportView(table);
			scrollAtributos.setBorder(BorderFactory.createTitledBorder(I18N
					.get("GestorCapas", "layers.panelatributos.titulo")));
		}
		return scrollAtributos;
	}

	/**
	 * This method initializes tblAtributos
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblAtributos() {
		if (tblAtributos == null) {
			tblAtributos = new JTable(tablemodel);
		}

		tblAtributos.getColumnModel().getColumn(0)
				.setCellRenderer(new TableAttributesCellRenderer());
		tblAtributos.getColumnModel().getColumn(1)
				.setCellRenderer(new AttributeRenderer());
		tblAtributos.getColumnModel().getColumn(1)
				.setCellEditor(new AttributeEditor());
		// tblAtributos.getColumnModel().getColumn(2).setCellRenderer(new
		// TableSQLCellRenderer());

		tblAtributos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ListSelectionModel rowSM = tblAtributos.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					// System.out.println("No rows are selected.");
					btnSubir.setEnabled(false);
					btnBajar.setEnabled(false);
					btnEliminarAtributo.setEnabled(false);
				} else {
					selectedTableRow = lsm.getMinSelectionIndex();
					entrada = tablemodel.getValueAt(selectedTableRow);
					btnSubir.setEnabled(true);
					btnBajar.setEnabled(true);
					if (selectedTableRow == 0)
						btnSubir.setEnabled(false);
					if (selectedTableRow == tblAtributos.getModel()
							.getRowCount() - 1)
						btnBajar.setEnabled(false);

					btnEliminarAtributo.setEnabled(true);
				}
			}
		});

		return tblAtributos;
	}

	/**
	 * This method initializes scrollAtributos
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollListaClaves() {
		scrollListaClaves = new JScrollPane();
		scrollListaClaves.setBounds(new Rectangle(303, 291, 558, 124));

		JList lista = getLstForeignKeys();
		scrollListaClaves.setViewportView(lista);
		scrollListaClaves.setBorder(BorderFactory.createTitledBorder(I18N.get(
				"GestorCapas", "layers.panelclaves.titulo")));

		return scrollListaClaves;
	}

	/**
	 * This method initializes lstForeignKeys
	 * 
	 * @return javax.swing.JList
	 */
	private JList getLstForeignKeys() {
		lstForeignKeys = new JList(listmodel);

		ListSelectionModel rowSM = lstForeignKeys.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					// System.out.println("No rows are selected.");
					btnEliminarClave.setEnabled(false);
				} else {
					btnEliminarClave.setEnabled(true);
					selectedListRow = lstForeignKeys.getSelectedIndices();
				}
			}
		});

		return lstForeignKeys;
	}

	/**
	 * This method initializes btnAtributos
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAnadirAtributo() {
		if (btnAnadirAtributo == null) {
			btnAnadirAtributo = new JButton();
			btnAnadirAtributo.setBounds(new Rectangle(271, 115, 30, 30));

			btnAnadirAtributo.setIcon(IconLoader.icon(ICONO_FLECHA_DERECHA));
			btnAnadirAtributo.setEnabled(false);
			btnAnadirAtributo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnAnadirAtributo_actionPerformed(e);
				}
			});
		}
		return btnAnadirAtributo;
	}

	/**
	 * This method initializes btnAtributos
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnEliminarAtributo() {
		if (btnEliminarAtributo == null) {
			btnEliminarAtributo = new JButton();
			btnEliminarAtributo.setBounds(new Rectangle(270, 186, 28, 30));

			btnEliminarAtributo
					.setIcon(IconLoader.icon(ICONO_FLECHA_IZQUIERDA));
			btnEliminarAtributo.setEnabled(false);
			btnEliminarAtributo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnEliminarAtributo_actionPerformed(e);
				}
			});
		}
		return btnEliminarAtributo;
	}

	/**
	 * This method initializes btnClaves
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAnadirClaves() {
		if (btnAnadirClaves == null) {
			btnAnadirClaves = new JButton();
			btnAnadirClaves.setBounds(new Rectangle(271, 303, 30, 30));
			btnAnadirClaves.setIcon(IconLoader.icon(ICONO_FLECHA_DERECHA));
			btnAnadirClaves.setEnabled(false);

			btnAnadirClaves.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnAnadirClaves_actionPerformed(e);
				}
			});
		}
		return btnAnadirClaves;
	}

	/**
	 * This method initializes btnGenerarSQL
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnGenerarSQL() {
		if (btnGenerarSQL == null) {
			btnGenerarSQL = new JButton();
			btnGenerarSQL.setBounds(new Rectangle(23, 450, 115, 21));
			btnGenerarSQL.setText(I18N.get("GestorCapas",
					"layers.boton.generarsql"));
			btnGenerarSQL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnGenerarSQL_actionPerformed(e);
				}
			});
		}
		return btnGenerarSQL;
	}

	/**
	 * This method initializes btnRecuperarSQL
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnRecuperarSQL() {
		if (btnRecuperarSQL == null) {
			btnRecuperarSQL = new JButton();
			btnRecuperarSQL.setBounds(new Rectangle(150, 450, 115, 21));
			btnRecuperarSQL.setText(I18N.get("GestorCapas",
					"layers.boton.recuperarsql"));
			btnRecuperarSQL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnRecuperarSQL_actionPerformed(e);
				}
			});
		}
		return btnRecuperarSQL;
	}

	/**
	 * This method initializes btnProbar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnProbar() {
		if (btnProbar == null) {
			btnProbar = new JButton();
			btnProbar.setBounds(new Rectangle(780, 450, 80, 21));
			btnProbar.setText(I18N.get("GestorCapas", "layers.boton.test"));
			btnProbar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnProbar_actionPerformed(e);
				}
			});
		}
		return btnProbar;
	}

	/**
	 * This method initializes cmbBaseDatos
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbBaseDatos() {
		if (cmbBaseDatos == null) {
			cmbBaseDatos = new JComboBox();
			cmbBaseDatos.setBounds(new Rectangle(415, 450, 95, 20));

			cmbBaseDatos.addItem("PostgreSQL");
			cmbBaseDatos.addItem("Oracle");
			cmbBaseDatos.setSelectedIndex(0);
			tipoBD = POSTGRE;

			cmbBaseDatos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					cmbBaseDatos_actionPerformed(e);
				}
			});

		}
		return cmbBaseDatos;
	}

	/**
	 * This method initializes cmbQuery
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbQuery() {
		if (cmbQuery == null) {
			cmbQuery = new JComboBox();
			cmbQuery.setBounds(new Rectangle(650, 450, 80, 20));

			cmbQuery.addItem("SELECT");
			cmbQuery.addItem("UPDATE");
			cmbQuery.addItem("INSERT");
			cmbQuery.addItem("DELETE");
			cmbQuery.setSelectedIndex(0);
			tipoQuery = SELECT;

			cmbQuery.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					cmbQuery_actionPerformed(e);
				}
			});

		}
		return cmbQuery;
	}

	/**
	 * This method initializes cmbAcl
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbAcl() {
		if (cmbAcl == null) {
			cmbAcl = new JComboBox();
			cmbAcl.setBounds(new java.awt.Rectangle(830, 19, 160, 20));

			AclCellRenderer renderer = new AclCellRenderer();
			cmbAcl.setRenderer(renderer);
			cmbAcl.setFocusable(false);
		}
		return cmbAcl;
	}

	private JTextField getTxtNuevoAcl() {
		if (txtNuevoAcl == null) {
			txtNuevoAcl = new JTextField();
			txtNuevoAcl.setText("");
			txtNuevoAcl.setBounds(new java.awt.Rectangle(830, 51, 160, 20));
		}
		return txtNuevoAcl;
	}

	/**
	 * This method initializes cmbAlcance
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbAlcance() {
		if (cmbAlcance == null) {
			cmbAlcance = new JComboBox();
			cmbAlcance.setBounds(new java.awt.Rectangle(357, 83, 130, 20));

			cmbAlcance.addItem("GLOBAL");
			cmbAlcance.addItem("LOCAL");
		}
		return cmbAlcance;
	}

	/**
	 * This method initializes btnGrabar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnGrabar() {
		if (btnGrabar == null) {
			btnGrabar = new JButton();
			btnGrabar.setBounds(new Rectangle(875, 523, 100, 25));
			btnGrabar.setText(I18N.get("GestorCapas", "general.boton.grabar"));
			btnGrabar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnGrabar_actionPerformed(e);
				}
			});
		}
		return btnGrabar;
	}

	/**
	 * This method initializes btnGrabar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnEliminarLayer() {
		if (btnEliminarLayer == null) {
			btnEliminarLayer = new JButton();
			btnEliminarLayer.setBounds(new Rectangle(875, 491, 100, 25));
			btnEliminarLayer.setText(I18N.get("GestorCapas",
					"general.boton.eliminar"));
			btnEliminarLayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnEliminarLayer_actionPerformed(e);
				}
			});
		}
		return btnEliminarLayer;
	}

	/**
	 * This method initializes btnSalir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton();
			btnSalir.setBounds(new Rectangle(875, 555, 100, 25));
			btnSalir.setText(I18N.get("GestorCapas", "general.boton.salir"));
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonSalirActionPerformed();
				}
			});
		}
		return btnSalir;
	}

	/**
	 * This method initializes btnIdiomas
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnIdiomas() {
		if (btnIdiomas == null) {
			btnIdiomas = new JButton();
			btnIdiomas.setBounds(new java.awt.Rectangle(710, 19, 20, 20));
			btnIdiomas.setIcon(IconLoader.icon(ICONO_BANDERA));
			btnIdiomas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnIdiomas_actionPerformed(e);
				}
			});
		}
		return btnIdiomas;
	}

	/**
	 * This method initializes btnEliminarClave
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnEliminarClave() {
		if (btnEliminarClave == null) {
			btnEliminarClave = new JButton();
			btnEliminarClave.setBounds(new Rectangle(865, 317, 30, 30));
			btnEliminarClave.setIcon(IconLoader.icon(ICONO_BORRAR));
			btnEliminarClave.setEnabled(false);
			btnEliminarClave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnEliminarClave_actionPerformed(e);
				}
			});
		}
		return btnEliminarClave;
	}

	/**
	 * This method initializes btnSubir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnSubir() {
		if (btnSubir == null) {
			btnSubir = new JButton();
			btnSubir.setBounds(new Rectangle(865, 144, 30, 30));
			btnSubir.setIcon(IconLoader.icon(ICONO_SUBIR));
			btnSubir.setEnabled(false);
			btnSubir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnSubir_actionPerformed(e);
				}
			});
		}
		return btnSubir;
	}

	/**
	 * This method initializes btnBajar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnBajar() {
		if (btnBajar == null) {
			btnBajar = new JButton();
			btnBajar.setBounds(new Rectangle(865, 185, 30, 30));
			btnBajar.setIcon(IconLoader.icon(ICONO_BAJAR));
			btnBajar.setEnabled(false);
			btnBajar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Llamar al metodo correspondiente
					btnBajar_actionPerformed(e);
				}
			});
		}
		return btnBajar;
	}

	//NUEVO
	/**
	 * This method initializes btnExport
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnExport() {
		if (btnExport == null) {
			btnExport = new JButton();
			btnExport.setBounds(new Rectangle(875, 450, 100, 21));
			btnExport.setText(I18N.get("GestorCapas",
					"layers.boton.exportarcapa"));
			btnExport.setVisible(false);
			//btnExport.setEnabled(false);
			btnExport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnExport_actionPerformed(e);
				}
			});
		}
		return btnExport;
	}

	//FIN NUEVO

	//NUEVO
	/**
	 * This method initializes btnExport
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton();
			btnImport.setBounds(new Rectangle(875, 450, 100, 21));
			btnImport.setText(I18N.get("GestorCapas",
					"layers.boton.importarcapa"));
			btnImport.setVisible(false);
			btnImport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnImport_actionPerformed(e);
				}
			});
		}
		return btnImport;
	}

	//FIN NUEVO

	//NUEVO
	private void hideExportImport() {
		getBtnExport().setVisible(false);
		//getBtnExport().setEnabled(false);
		getBtnImport().setVisible(false);
	}

	//FIN NUEVO

	/*
	 * /** This method initializes btnSql
	 * 
	 * @return javax.swing.JButton
	 */
	/*
	 * private JButton getBtnSql() { if (btnSql == null) { btnSql = new
	 * JButton(); btnSql.setBounds(new Rectangle(865,240,30,30));
	 * btnSql.setIcon(sqlIcono); btnSql.setEnabled(false);
	 * btnSql.addActionListener(new ActionListener() { public void
	 * actionPerformed(ActionEvent e) { //Llamar al metodo correspondiente
	 * btnSql_actionPerformed(e); } }); } return btnSql; }
	 */

	/**
	 * Controla el aspecto de la pantalla, de acuerdo al tipo de operación que
	 * se esté realizando en cada momento
	 * 
	 * @param esCreacion
	 *            Verdadero si la operación a realizar es la de creación de una
	 *            nueva capa
	 */
	private void controlarAspecto(boolean esCreacion) {

		Color fg = null;
		Color nofg = null;

		jPanelTablas.getTree().clearSelection();

		if (esCreacion) {
			fg = Color.BLACK;
			nofg = Color.GRAY;
		} else {
			fg = Color.GRAY;
			nofg = Color.BLACK;
		}

		lblLayerFamily.setForeground(fg);
		lblLayer.setForeground(nofg);

		// Habilitadas siempre que un elemento radiobutton esté seleccionado
		lblIdCapa.setForeground(Color.BLACK);
		lblAmbito.setForeground(Color.BLACK);
		txtIdCapa.setEnabled(true);
		lblNombreCapa.setForeground(Color.BLACK);
		txtNombreCapa.setEnabled(true);
		// lblAcl.setForeground(Color.BLACK);
		cmbAcl.setEnabled(true);
		cmbAcl.setSelectedIndex(-1);
		cmbAlcance.setEnabled(true);
		btnIdiomas.setEnabled(true);
		btnSql.setEnabled(true);
		// jPanelTablas.setEnabled(true);

		// Deshabilitar zona inferior de la pantalla hasta que se seleccione del
		// combo
		habilitarZonaInferior(false);

		// Deshabilitar tambien los botones de operaciones
		btnSubir.setEnabled(false);
		btnBajar.setEnabled(false);
		btnAnadirAtributo.setEnabled(false);
		btnEliminarAtributo.setEnabled(false);
		btnAnadirClaves.setEnabled(false);
		btnEliminarClave.setEnabled(false);
		// //

		cmbLayerFamily.setEnabled(esCreacion);
		if (cmbLayerFamily.getSelectedIndex() != -1)
			cmbLayerFamily.setSelectedIndex(0);

		cmbLayer.setEnabled(!esCreacion);
		if (cmbLayer.getSelectedIndex() != -1)
			cmbLayer.setSelectedIndex(0);

		// si esta seleccionado el rdb eliminar se habilita el boton de borrado:
		if (getSelectedRadioButton(btnGroup) == rdbModoEliminacion
				&& cmbLayer.getSelectedIndex() > 0) {
			getBtnEliminarLayer().setEnabled(true);
			getBtnGrabar().setEnabled(false);
			//NUEVO
			hideExportImport();
			//FIN NUEVO
		} else {
			getBtnEliminarLayer().setEnabled(false);
		}

		txtIdCapa.setText("");
		txtNombreCapa.setText("");
		txtAreaSQL.setText("");

		cargarAtributos(new Vector()); // borra la lista de atributos
		listmodel.removeAllElements(); // borra la lista de claves
		this.remove(scrollListaClaves);
		this.add(getScrollListaClaves());

		cargarLayers(!esCreacion);
		cargarLayerFamilies(esCreacion);
		cargarAcl();
		cmbAcl.setEnabled(false);

		jPanelTablas.setEnabled(esCreacion);

	}

	private void cargarAcl() {
		LayerOperations operaciones = new LayerOperations();
		ArrayList lstAcl = new ArrayList();
		try {
			lstAcl = operaciones.getAclList();
		} catch (DataException e) {
			e.printStackTrace();
		}
		cmbAcl = getCmbAcl();
		cmbAcl.removeAllItems();

		Acl acl = new Acl();
		cmbAcl.addItem(acl);
		Iterator it = lstAcl.iterator();
		while (it.hasNext()) {
			cmbAcl.addItem((Acl) it.next());
		}

		cmbAcl.setSelectedIndex(0);
	}

	/**
	 * Carga las capas en el combo de capas
	 * 
	 * @param realizarOperacion
	 *            Verdadero si se desean cargar las capas
	 */
	private void cargarLayers(boolean realizarOperacion) {
		htQueries = new Hashtable();
		htQueriesOriginal = new Hashtable();
		htNombres = new Hashtable();

		if (realizarOperacion) {

			LayerOperations operaciones = new LayerOperations();

			try {
				lstCapas = operaciones.obtenerLayerTable(true,
						AppContext.getIdEntidad());
			} catch (DataException e) {
				e.printStackTrace();
			}
			cmbLayer = getCmbLayer();
			cmbLayer.removeAllItems();

			LayerTable capa = new LayerTable();
			capa.getLayer().setDescription(" ");
			cmbLayer.addItem(capa);

			for (int i = 0; i < lstCapas.length; i++) {
				capa = (LayerTable) lstCapas[i];
				cmbLayer.addItem(capa);

			}
			cmbLayer.setSelectedIndex(0);
		}
	}

	/**
	 * Carga las layerfamilies en el combo de layerfamilies
	 * 
	 * @param realizarOperacion
	 *            Verdadero si se desea realizar la operación
	 */
	private void cargarLayerFamilies(boolean realizarOperacion) {

		htQueries = new Hashtable();
		htQueriesOriginal = new Hashtable();
		htNombres = new Hashtable();
		LayerFamilyTable[] lstFamilias = new LayerFamilyTable[1];

		if (realizarOperacion) {

			LayerOperations operaciones = new LayerOperations();
			try {
				lstFamilias = operaciones.obtenerLayerFamilyTable();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cmbLayerFamily = getCmbLayerFamily();
			cmbLayerFamily.removeAllItems();

			LayerFamilyTable familiaT = new LayerFamilyTable();
			familiaT.getLayerFamily().setDescription(" ");
			cmbLayerFamily.addItem(familiaT);

			for (int i = 0; i < lstFamilias.length; i++) {
				familiaT = (LayerFamilyTable) lstFamilias[i];
				cmbLayerFamily.addItem(familiaT);
			}

			cmbLayerFamily.setSelectedIndex(0);
		}
	}

	/**
	 * Elimina una clave de la lista de claves de la capa
	 * 
	 * @param e
	 */
	private void btnEliminarClave_actionPerformed(ActionEvent e) {
		int posicionInicial = selectedListRow[0];
		int numSeleccionadas = selectedListRow.length;
		int[] filasSelec = selectedListRow;

		int indice = 0;
		for (int i = 0; i < filasSelec.length; i++) {

			int posicion = filasSelec[i];
			listmodel.remove(posicion - indice);
			indice++;
		}

		this.remove(scrollListaClaves);
		this.add(getScrollListaClaves());

		ListSelectionModel rowSM = lstForeignKeys.getSelectionModel();
		if (listmodel.getSize() != 0) {
			if (listmodel.getSize() == 1)
				rowSM.setSelectionInterval(0, 0);

			else if (posicionInicial < listmodel.getSize() - 1)
				rowSM.setSelectionInterval(posicionInicial, posicionInicial);

			else if (posicionInicial == listmodel.getSize() - 1) {
				if (posicionInicial - numSeleccionadas >= 0)
					rowSM.setSelectionInterval(posicionInicial
							- numSeleccionadas + 1, posicionInicial
							- numSeleccionadas + 1);
			} else
				rowSM.setSelectionInterval(listmodel.getSize() - 1,
						listmodel.getSize() - 1);
		}
	}

	/**
	 * Añade una clave de la lista de claves de la capa
	 * 
	 * @param e
	 */
	private void btnAnadirClaves_actionPerformed(ActionEvent e) {
		String clave = columnasSeleccionadas[0].getTable().getName() + "."
				+ columnasSeleccionadas[0].getName() + "="
				+ columnasSeleccionadas[1].getTable().getName() + "."
				+ columnasSeleccionadas[1].getName();

		listmodel.add(listmodel.getSize(), clave);

		this.remove(scrollListaClaves);
		this.add(getScrollListaClaves());

		ListSelectionModel rowSM = lstForeignKeys.getSelectionModel();
		rowSM.setSelectionInterval(listmodel.getSize() - 1,
				listmodel.getSize() - 1);

		numFilasSelec = 0;
		columnasSeleccionadas = new Column[0];
		jPanelTablas.getTree().clearSelection();
		btnAnadirClaves.setEnabled(false);
		btnAnadirAtributo.setEnabled(false);

	}

	/**
	 * Recupera las SQL anteriores a la modificación de las mismas
	 * 
	 * @param e
	 */
	private void btnRecuperarSQL_actionPerformed(ActionEvent e) {
		if (tipoBD > 0 && tipoQuery >= 0) {
			Query queryOriginal = (Query) htQueriesOriginal.get(new Integer(
					tipoBD));
			if (queryOriginal == null)
				queryOriginal = new Query();
			Query queryModificada = (Query) htQueries.get(new Integer(tipoBD));
			if (queryModificada == null)
				queryModificada = new Query();

			String valorOriginal = queryOriginal.getQuery(tipoQuery);
			queryModificada.setQuery(tipoQuery, valorOriginal);
			htQueries.put(new Integer(tipoBD), queryModificada);

			txtAreaSQL.setText(valorOriginal);
		}
	}

	/**
	 * Sugiere una consulta SQL a partir del resto de datos de la capa
	 * 
	 * @param e
	 */
	private void btnGenerarSQL_actionPerformed(ActionEvent e) {
		if (tipoBD > 0 && tipoQuery >= 0) {
			isQueryGenerada = true;
			isQueriesModificadas = true;

			// guarda las posiciones de todas las columnas de la layer
			Hashtable auxColumnas = new Hashtable();

			HashSet tablas = new HashSet();
			Vector columnas = new Vector();
			Vector columnasQueries = new Vector();
			Vector condiciones = new Vector();
			Vector foreignConditions = new Vector();

			Hashtable posicionesClave = new Hashtable();
			Hashtable posicionesMunicipio = new Hashtable();
			Hashtable posicionesGeometria = new Hashtable();

			// int posicionArea = -1;
			// int posicionLongitud = -1;

			// Column colClave = null;
			Hashtable columnasClave = new Hashtable();

			// Column colMunicipio = null;
			Hashtable columnasMunicipio = new Hashtable();

			// Column colGeometria = null;
			Hashtable columnasGeometria = new Hashtable();

			// lista de tablas para el from y de condiciones para el where
			for (int i = 0; i < listmodel.getSize(); i++) {
				String condicion = listmodel.get(i).toString();
				condiciones.add(condicion);

				int punto1Index = condicion.indexOf(".");
				int igualIndex = condicion.indexOf("=");
				int punto2Index = condicion.lastIndexOf(".");

				tablas.add(condicion.subSequence(0, punto1Index));
				tablas.add(condicion.subSequence(igualIndex + 1, punto2Index));

				foreignConditions.add(new ForeignCondition(condicion));

			}

			// lista de tablas.columnas y mas tablas para el from
			TableRow[] data = tablemodel.getData();

			for (int i = 0; i < data.length; i++) {
				Column columna = data[i].getColumna();
				if (columna.getTable() != null) {
					auxColumnas.put(columna, new Integer(i));

					if (columna.getTable().getName().equals("SYSTEM")) {
						columnasQueries.add(data[i].getSqlQuery());
					} else {
						tablas.add(columna.getTable().getName());

						// if (columna.getName().equals("GEOMETRY"))
						// columna.setName("\"GEOMETRY\"");
						columnas.add(columna);// String.valueOf(columna.getTable().getName()+"."+columna.getName()));

						// Busca los municipios (variable de entorno
						// geopista.DefaultCityId)
						if (columna.getDomain() != null
								&& columna.getDomain().getType() == Domain.AUTO
								&& columna.getDomain().getPattern() != null
								&& (columna
										.getDomain()
										.getPattern()
										.equalsIgnoreCase(
												"ENV_VAR:geopista.DefaultCityId") || columna
										.getDomain()
										.getPattern()
										.equalsIgnoreCase(
												"?ENV_VAR:geopista.DefaultCityId"))) {
							// posicionMunicipio =i;
							// colMunicipio = columna;

							// para permitir las sql de varias tablas
							posicionesMunicipio.put(columna.getTable()
									.getName(), new Integer(i));
							columnasMunicipio.put(columna.getTable().getName(),
									columna);

						}
						// Busca los numéricos autoincrementales únicos y no
						// nulos
						else if (columna.getDomain() != null
								&& columna.getDomain().getType() == Domain.AUTO
								&& columna.getDomain().getPattern() != null
								&& (columna.getDomain().getPattern()
										.equalsIgnoreCase("ID") || columna
										.getDomain().getPattern()
										.equalsIgnoreCase("?ID"))) {
							// posicionClave = i;
							// colClave = columna;

							// para permitir las sql de varias tablas
							posicionesClave.put(columna.getTable().getName(),
									new Integer(i));
							columnasClave.put(columna.getTable().getName(),
									columna);

						} else if (columna.getName().equalsIgnoreCase(
								"GEOMETRY")
								|| columna.getName().equalsIgnoreCase(
										"\"GEOMETRY\"")
								|| (columna.getAttribute().getType() != null && Integer
										.valueOf(
												columna.getAttribute()
														.getType()).intValue() == 1)) {
							// posicionGeometria = i;
							// colGeometria = columna;

							// para permitir las sql de varias tablas
							posicionesGeometria.put(columna.getTable()
									.getName(), new Integer(i));
							columnasGeometria.put(columna.getTable().getName(),
									columna);

						}
						// else if (columna.getDomain().getType()==Domain.AUTO
						// &&
						// columna.getDomain().getPattern().equalsIgnoreCase("AREA"))
						// {
						// posicionArea = i;
						// }
						// else if (columna.getDomain().getType()==Domain.AUTO
						// &&
						// columna.getDomain().getPattern().equalsIgnoreCase("LENGTH"))
						// {
						// posicionLongitud = i;
						// }
					}
				}
			}

			StringBuffer sqlbuf = new StringBuffer();

			// DE MOMENTO LAS QUERIES QUE GENERA SON IGUALES PARA ORACLE Y PARA
			// POSTGRESQL
			switch (tipoQuery) {
			case SELECT:
				if (tablas.size() > 0 && columnas.size() > 0) {
					sqlbuf.append("SELECT ");

					for (int i = 0; i < columnas.size(); i++) {
						Column col = (Column) columnas.get(i);

						if (col.getName().equals("GEOMETRY")) {
							sqlbuf.append("transform(").append("\"")
									.append(col.getTable().getName())
									.append("\".\"").append(col.getName())
									.append("\"").append(", ?T) AS \"")
									.append(col.getName()).append("\",");
						} else {
							sqlbuf.append("\"")
									.append(col.getTable().getName())
									.append("\".\"").append(col.getName())
									.append("\",");
						}

						// int posGeometria = -1;
						// if
						// (posicionesGeometria.get(col.getTable().getName())!=null)
						// posGeometria =
						// ((Integer)posicionesGeometria.get(col.getTable().getName())).intValue();

						// if (posGeometria==i)
						// {
						// sqlbuf.append("\"").append(col.getTable().getName()).append("\".\"").append(col.getName()).append("\",");
						// }
						// else
						// {
						// sqlbuf.append("\"").append(col.getTable().getName()).append("\".").append(col.getName()).append(",");
						// }

					}
					for (int i = 0; i < columnasQueries.size(); i++) {
						if (!columnasQueries.get(i).equals(""))
							sqlbuf.append(columnasQueries.get(i)).append(",");
					}

					sqlbuf = new StringBuffer(sqlbuf.subSequence(0,
							sqlbuf.length() - 1).toString()).append(" FROM ");

					sqlbuf.append(generateFromClause(foreignConditions, tablas));

					Object clave = new Object();
					int posMunicipio = -1;
					if (!posicionesMunicipio.isEmpty()) {
						clave = posicionesMunicipio.keys().nextElement();
						posMunicipio = ((Integer) posicionesMunicipio
								.get(clave)).intValue();
					}

					if (posMunicipio >= 0) {
						Column colMunicipio = (Column) columnasMunicipio
								.get(clave);
						sqlbuf.append(" WHERE \"")
								.append(colMunicipio.getTable().getName())
								.append("\".\"").append(colMunicipio.getName())
								.append("\" IN (?M)");
					} else {
						// Coge el id_municipio de la primera tabla del hashset
						sqlbuf.append(" WHERE \"")
								.append(tablas.iterator().next())
								.append("\".\"id_municipio\"  in (select id_municipio from entidades_municipios where id_entidad=?M)");
					}
				}

				break;

			case UPDATE:
				if (tablas.size() > 0
						&& (columnas.size() > 0 || columnasQueries.size() > 0)) {
					Iterator iterTablas = tablas.iterator();

					while (iterTablas.hasNext()) {
						String nombreTabla = iterTablas.next().toString();

						// solo se genera la sentencia de update si existe una
						// clave en la tabla
						int posClave = -1;
						Column colClave = new Column();
						if (posicionesClave.get(nombreTabla) != null) {
							posClave = ((Integer) posicionesClave
									.get(nombreTabla)).intValue();
							colClave = (Column) columnasClave.get(nombreTabla);
						}
						if (posClave >= 0) {
							int posGeometria = -1;
							if (posicionesGeometria.get(nombreTabla) != null)
								posGeometria = ((Integer) posicionesGeometria
										.get(nombreTabla)).intValue();

							int posMunicipio = -1;
							if (posicionesMunicipio.get(nombreTabla) != null)
								posMunicipio = ((Integer) posicionesMunicipio
										.get(nombreTabla)).intValue();

							sqlbuf = sqlbuf.append("UPDATE \"")
									.append(nombreTabla).append("\" SET ");

							for (Iterator it = columnas.iterator(); it
									.hasNext();) {
								Column col = (Column) it.next();
								int i = ((Integer) auxColumnas.get(col))
										.intValue();

								String nomtab = col.getTable().getName();
								String nomcol = col.getName();
								if (nomtab.equalsIgnoreCase(nombreTabla)) {
									if (i == posGeometria) {
										sqlbuf.append("\"")
												.append(nomcol)
												.append("\"")
												.append("=transform(GeometryFromText(text(?")
												.append(++i)
												.append("),?S), ?T),");
									} else if (i == posMunicipio) {
										sqlbuf.append("\"").append(nomcol)
												.append("\" = ?M,");
									}
									// else if (i == posicionArea)
									// {
									// sql = sql +
									// col+"=area2d(GeometryFromText(?"+posicionGeometria+1+",?S)),";
									// }
									// else if (i == posicionLongitud)
									// {
									// sql = sql +
									// col+"=perimeter(GeometryFromText(?"+posicionGeometria+1+",?S)),";
									// }
									else {
										sqlbuf.append("\"").append(nomcol)
												.append("\"=?").append(++i)
												.append(",");
									}
								}
							}

							sqlbuf = new StringBuffer(sqlbuf.substring(0,
									sqlbuf.length() - 1).toString())
									.append(" WHERE \"")
									.append(colClave.getName()).append("\"=?")
									.append(posClave + 1).append(";");
						}
					}
					// Se elimina el último ";"
					try {
						sqlbuf = new StringBuffer(sqlbuf.substring(0,
								sqlbuf.length() - 1).toString());
					} catch (StringIndexOutOfBoundsException se) {
						sqlbuf = new StringBuffer();
					}
				}
				break;

			case INSERT:

				if (tablas.size() > 0
						&& (columnas.size() > 0 || columnasQueries.size() > 0)) {
					Iterator iterTablas = tablas.iterator();

					while (iterTablas.hasNext()) {
						String nombreTabla = iterTablas.next().toString();

						sqlbuf.append("INSERT INTO \"").append(nombreTabla)
								.append("\" (");
						StringBuffer valuesbuf = new StringBuffer(") VALUES(");

						// Se utiliza para saber si la tabla se ha utilizado en
						// la lista de atributos,
						// o solamente en la lista de claves, en cuyo caso no se
						// realiza un insert de
						// dicha tabla
						boolean hasColumns = false;

						for (Iterator it = columnas.iterator(); it.hasNext();) {
							Column col = (Column) it.next();
							int i = ((Integer) auxColumnas.get(col)).intValue();

							int posClave = -1;
							if (posicionesClave.get(nombreTabla) != null)
								posClave = ((Integer) posicionesClave
										.get(nombreTabla)).intValue();

							int posGeometria = -1;
							if (posicionesGeometria.get(nombreTabla) != null)
								posGeometria = ((Integer) posicionesGeometria
										.get(nombreTabla)).intValue();

							int posMunicipio = -1;
							if (posicionesMunicipio.get(nombreTabla) != null)
								posMunicipio = ((Integer) posicionesMunicipio
										.get(nombreTabla)).intValue();

							String nomtab = col.getTable().getName();
							String nomcol = col.getName();

							if (nomtab.equalsIgnoreCase(nombreTabla)) {
								hasColumns = true;

								sqlbuf.append("\"").append(nomcol)
										.append("\",");
								if (i == posGeometria) {
									valuesbuf
											.append("transform(GeometryFromText(text(?")
											.append(++i).append("),?S), ?T),");
								} else if (i == posMunicipio) {
									valuesbuf.append("?M,");
								} else if (i == posClave) {
									valuesbuf.append("?PK,");
								}
								// else if (i == posicionArea)
								// {
								// values =values
								// +"area2d(GeometryFromText(?"+posicionGeometria+1+",?S)),";
								// }
								// else if (i == posicionLongitud)
								// {
								// values = values
								// +"perimeter(GeometryFromText(?"+posicionGeometria+1+",?S)),";
								// }
								else {
									valuesbuf.append("?").append(++i)
											.append(",");
								}
							}
						}

						if (hasColumns) {
							sqlbuf = new StringBuffer(sqlbuf.substring(0,
									sqlbuf.length() - 1).toString()).append(
									valuesbuf.substring(0,
											valuesbuf.length() - 1).toString())
									.append(");");
							hasColumns = false;
						} else {
							int lastInsert = sqlbuf.toString().lastIndexOf(";") > -1 ? sqlbuf
									.toString().lastIndexOf(";") : 0;
							sqlbuf = new StringBuffer(sqlbuf.substring(0,
									lastInsert).toString()).append(";");
						}
					}

					// Se elimina el último ";"
					try {
						sqlbuf = new StringBuffer(sqlbuf.substring(0,
								sqlbuf.length() - 1));

					} catch (StringIndexOutOfBoundsException se) {
						sqlbuf = new StringBuffer();
					}
				}

				break;

			case DELETE:

				if (tablas.size() > 0) {
					Iterator iterTablas = tablas.iterator();

					while (iterTablas.hasNext()) {
						String nombreTabla = iterTablas.next().toString();

						// solo se genera la sentencia de update si existe una
						// clave en la tabla
						int posClave = -1;
						Column colClave = new Column();
						if (posicionesClave.get(nombreTabla) != null) {
							posClave = ((Integer) posicionesClave
									.get(nombreTabla)).intValue();
							colClave = (Column) columnasClave.get(nombreTabla);
						}

						if (posClave >= 0) {
							sqlbuf.append("DELETE FROM \"").append(nombreTabla)
									.append("\" WHERE ").append("\"")
									.append(colClave.getName()).append("\"=?")
									.append(posClave + 1).append(";");
						}
					}

					// Se elimina el último ";"
					try {
						sqlbuf = new StringBuffer(sqlbuf.substring(0,
								sqlbuf.length() - 1));
					} catch (StringIndexOutOfBoundsException se) {
						sqlbuf = new StringBuffer();
					}
				}
				break;

			default:
				break;
			}
			txtAreaSQL.setText(sqlbuf.toString().replaceAll(";", ";\n"));
		}
	}

	/**
	 * Acción lanzada cada vez que se modifica la selección de una capa dentro
	 * del combo de capas
	 * 
	 * @param e
	 */
	private void cmbLayer_actionPerformed(ActionEvent e) {

		if (cmbLayer.getSelectedIndex() > 0 && getFireEvents()) {
		
			lanzarMensaje(I18N.get("GestorCapas", "general.pestana.layers"),
					I18N.get("GestorCapas",
							"layers.info.modificacion.capa_sistema"),
					JOptionPane.INFORMATION_MESSAGE);

			jPanelTablas.setEnabled(true);
			try {
				hsAtributosBorrar.clear();
				layerTableSeleccionada = (LayerTable) getCmbLayer()
						.getSelectedItem();
				capaSeleccionada = (Layer) layerTableSeleccionada.getLayer();
				idLayer = ((LayerTable) getCmbLayer().getSelectedItem())
						.getIdLayer();

				// Modificar textos
				LayerOperations operaciones = new LayerOperations();
				htNombres = operaciones
						.buscarTraduccionNombres(capaSeleccionada);

				if (htNombres.get(locale) != null)
					txtNombreCapa.setText(htNombres.get(locale).toString());
				else if (htNombres.get(GestorCapas.DEFAULT_LOCALE) != null)
					txtNombreCapa.setText(htNombres.get(
							GestorCapas.DEFAULT_LOCALE).toString());
				else
					txtNombreCapa.setText("");
				txtIdCapa.setText(capaSeleccionada.getDescription());

				// Reiniciar isQueriesModificadas
				isQueriesModificadas = false;

				// Cargar ACL
				Acl acl = findAclItem(layerTableSeleccionada.getAcl());
				cmbAcl.setSelectedItem(acl);

				if (layerTableSeleccionada.getIdEntidadLayer().equals("0")) // Capa
																			// global
					cmbAlcance.setSelectedIndex(0);
				else
					cmbAlcance.setSelectedIndex(1); // Capa local

				operaciones.setDynamicLayer(capaSeleccionada, idLayer, Integer
						.valueOf(layerTableSeleccionada.getIdEntidadLayer()));
				chbDinamica.setSelected(capaSeleccionada.isDinamica());
				chbVersionable.setSelected(capaSeleccionada.isVersionable());

				// Cargar atributos en la lista de atributos
				cargarAtributos(operaciones.buscarColumnasAtributos(idLayer));

				// Cargar claves en la lista de claves
				htQueriesOriginal = operaciones.buscarQueries(idLayer);
				htQueries = new Hashtable(htQueriesOriginal);// operaciones.buscarQueries(idLayer);

				// htQueries = new Hashtable(htQueriesOriginal);
				// htQueries.putAll((Hashtable)(htQueriesOriginal.clone()));

				if (htQueriesOriginal != null && htQueriesOriginal.size() != 0) {
					cargarClaves(htQueriesOriginal);
				} else {
					listmodel.removeAllElements();
					this.remove(scrollListaClaves);
					this.add(getScrollListaClaves());
				}

				// Habilitar zona inferior de la pantalla
				habilitarZonaInferior(true);

				txtAreaSQL.setText("");

				tipoBD = -1;
				tipoQuery = -1;
				cmbBaseDatos.setSelectedIndex(0);
				cmbQuery.setSelectedIndex(0);

				// si esta seleccionado el rdb eliminar se habilita el boton de
				// borrado:
				if (getSelectedRadioButton(btnGroup) == rdbModoEliminacion
						&& cmbLayer.getSelectedIndex() > 0) {
					getBtnEliminarLayer().setEnabled(true);
					getBtnGrabar().setEnabled(false);
				} else {
					getBtnEliminarLayer().setEnabled(false);
				}

				this.repaint();

				//NUEVO
				//getBtnExport().setEnabled(true);
				//FIN NUEVO

			} catch (Exception ex) {
				capaSeleccionada = null;
				idLayer = 0;
				txtIdCapa.setText("");
				txtNombreCapa.setText("");
				// System.out.println("Excepcion :idlayer es " +idLayer);
				cmbLayer.setSelectedIndex(0);
				return;
			}
		} else {
			jPanelTablas.setEnabled(false);
		}
	}

	/**
	 * Devuelve el objeto ACL cuyo id coincide con el del que se pasa por
	 * parámetro
	 * 
	 * @param acl
	 *            Acl a comparar
	 * @return Acl del combo con id coincidente
	 */
	private Acl findAclItem(Acl acl) {
		// Recorremos todos los elementos del combo en lugar de sobreescribir el
		// metodo
		// equals de Acl para evitar posibles problemas asociados en otras
		// partes del
		// código
		for (int i = 0; i < cmbAcl.getItemCount(); i++) {
			if (acl.getId().equals(((Acl) cmbAcl.getItemAt(i)).getId())) {
				return (Acl) cmbAcl.getItemAt(i);
			}
		}
		return null;
	}

	/**
	 * Carga los atributos en la lista de atributos
	 * 
	 * @param vecAtributos
	 *            Vector que contiene los atributos a cargar
	 */
	private void cargarAtributos(Vector vecAtributos) {
		attGeometryName = null;
		Vector vecTableRow = new Vector();
		HashSet hsDeletedAttributes = new HashSet();
		((TableAttributesModel) tblAtributos.getModel())
				.setData(new TableRow[0]);

		for (int i = 0; i < vecAtributos.size(); i++) {

			Attribute att = (Attribute) vecAtributos.get(i);
			if (att.getColumn().getTable() != null) {
				TableRow fila = new TableRow(att.getColumn(), att, "",
						att.isEditable());
				vecTableRow.add(fila);

				if (att.getColumn().getName().equalsIgnoreCase("GEOMETRY")
						|| att.getColumn().getName()
								.equalsIgnoreCase("\"GEOMETRY\""))
					attGeometryName = att.getColumn().getTable().getName();
			} else {
				hsDeletedAttributes.add(att);
			}
		}

		// Se borran de la base de datos todos los atributos que ya no existan
		// en las tablas
//		if (hsDeletedAttributes.size() != 0) {
//			LayerOperations op = new LayerOperations();
//			try {
//				op.eliminarAtributos(hsDeletedAttributes);
//			} catch (DataException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		((TableAttributesModel) tblAtributos.getModel())
				.setData((TableRow[]) vecTableRow
						.toArray(new TableRow[vecTableRow.size()]));
		this.remove(scrollAtributos);
		this.add(getScrollAtributos());

		getScrollAtributos().updateUI();
		this.updateUI();
		this.repaint();
	}
	
	/**
	 * Recupera los atributos
	 * 
	 * @param vecAtributos
	 *            Vector que contiene los atributos a cargar
	 */
	private TableAttributesModel recuperarAtributos(Vector vecAtributos) {
		TableAttributesModel tableAttributesModel = new TableAttributesModel();
		tableAttributesModel.setData(new TableRow[0]);
		String attGeometryName = null;
		Vector vecTableRow = new Vector();
		HashSet hsDeletedAttributes = new HashSet();
//		((TableAttributesModel) tblAtributos.getModel())
//				.setData(new TableRow[0]);

		for (int i = 0; i < vecAtributos.size(); i++) {
			Attribute att = (Attribute) vecAtributos.get(i);
			if (att.getColumn().getTable() != null) {
				TableRow fila = new TableRow(att.getColumn(), att, "",
						att.isEditable());
				vecTableRow.add(fila);

				if (att.getColumn().getName().equalsIgnoreCase("GEOMETRY")
						|| att.getColumn().getName()
								.equalsIgnoreCase("\"GEOMETRY\""))
					attGeometryName = att.getColumn().getTable().getName();
			} else {
				hsDeletedAttributes.add(att);
			}
		}

		tableAttributesModel.setData((TableRow[]) vecTableRow.toArray(new TableRow[vecTableRow.size()]));
//		this.remove(scrollAtributos);
//		this.add(getScrollAtributos());
//		getScrollAtributos().updateUI();
//		this.updateUI();
//		this.repaint();
		return tableAttributesModel;
	}	

	/**
	 * Carga la lista de foreignkeys a partir de las queries almacenadas para la
	 * layer
	 * 
	 * @param htQueries
	 *            Lista de queries a partir de las que se extraen las claves
	 */
	private void cargarClaves(Hashtable htQueries) {
		listmodel.removeAllElements();
//INICIO REEMPLAR (obtenerClaves)
		int indice = 0;
		for (Enumeration enu = htQueries.keys(); enu.hasMoreElements()
				|| indice < 1;) {

			String key = enu.nextElement().toString();
			Query query = (Query) htQueries.get(new Integer(key));

			String sql = query.getSelectQuery();
			int clausulaWhereIndex = sql.lastIndexOf(" WHERE ");
			if (clausulaWhereIndex < 0)
				clausulaWhereIndex = sql.lastIndexOf(" where ");

			if (clausulaWhereIndex > 0) {
				sql = sql.substring(clausulaWhereIndex + 6);
				String[] condiciones = sql.split(" AND ");

				for (int i = 0; i < condiciones.length; i++) {
					int igualIndex = condiciones[i].lastIndexOf("=");
					if (igualIndex > 0
							&& !condiciones[i].substring(igualIndex + 1,
									igualIndex + 2).equals("?"))
						listmodel.add(listmodel.getSize(),
								condiciones[i].replace("\"", ""));
				}

			}

			sql = query.getSelectQuery();
			int clausulaOnIndex = sql.lastIndexOf(" ON ");
			if (clausulaOnIndex < 0)
				clausulaOnIndex = sql.lastIndexOf(" on ");

			int parentesisIniIndex = sql.indexOf("(", clausulaOnIndex);
			if (parentesisIniIndex > 0) {
				int parentesisFinIndex = sql.indexOf(")", parentesisIniIndex);

				if (clausulaOnIndex > 0 && parentesisIniIndex > clausulaOnIndex) {
					String cadena = sql.substring(parentesisIniIndex + 1,
							parentesisFinIndex);
					String[] condiciones = cadena.split(" AND ");

					for (int i = 0; i < condiciones.length; i++) {
						int igualIndex = condiciones[i].lastIndexOf("=");
						if (igualIndex > 0
								&& !condiciones[i].substring(igualIndex + 1,
										igualIndex + 2).equals("?")) {
							listmodel.add(listmodel.getSize(),
									condiciones[i].replace("\"", ""));
						}
					}

				}

			} else {
				String cadena = sql;
				if (clausulaOnIndex + 3 < clausulaWhereIndex)
					cadena = sql.substring(clausulaOnIndex + 3,
							clausulaWhereIndex);
				String[] condiciones = cadena.split(" AND ");

				for (int i = 0; i < condiciones.length; i++) {
					int igualIndex = condiciones[i].lastIndexOf("=");
					if (igualIndex > 0
							&& !condiciones[i].substring(igualIndex + 1,
									igualIndex + 2).equals("?"))
						listmodel.add(listmodel.getSize(), condiciones[i]);
				}

			}

			indice++;
		}
		//FIN REEMPLAR
		this.remove(scrollListaClaves);
		this.add(getScrollListaClaves());

		// ListSelectionModel rowSM = lstForeignKeys.getSelectionModel();
		// rowSM.setSelectionInterval(listmodel.getSize()-1,
		// listmodel.getSize()-1);

	}
	
	//NUEVO
//	private DefaultListModel obtenerClaves(Hashtable htQueries) {	
//		DefaultListModel listmodel = new DefaultListModel();		
//		int indice = 0;
//		for (Enumeration enu = htQueries.keys(); enu.hasMoreElements()
//				|| indice < 1;) {
//
//			String key = enu.nextElement().toString();
//			Query query = (Query) htQueries.get(new Integer(key));
//
//			String sql = query.getSelectQuery();
//			int clausulaWhereIndex = sql.lastIndexOf(" WHERE ");
//			if (clausulaWhereIndex < 0)
//				clausulaWhereIndex = sql.lastIndexOf(" where ");
//
//			if (clausulaWhereIndex > 0) {
//				sql = sql.substring(clausulaWhereIndex + 6);
//				String[] condiciones = sql.split(" AND ");
//
//				for (int i = 0; i < condiciones.length; i++) {
//					int igualIndex = condiciones[i].lastIndexOf("=");
//					if (igualIndex > 0
//							&& !condiciones[i].substring(igualIndex + 1,
//									igualIndex + 2).equals("?"))
//						listmodel.add(listmodel.getSize(),
//								condiciones[i].replace("\"", ""));
//				}
//
//			}
//
//			sql = query.getSelectQuery();
//			int clausulaOnIndex = sql.lastIndexOf(" ON ");
//			if (clausulaOnIndex < 0)
//				clausulaOnIndex = sql.lastIndexOf(" on ");
//
//			int parentesisIniIndex = sql.indexOf("(", clausulaOnIndex);
//			if (parentesisIniIndex > 0) {
//				int parentesisFinIndex = sql.indexOf(")", parentesisIniIndex);
//
//				if (clausulaOnIndex > 0 && parentesisIniIndex > clausulaOnIndex) {
//					String cadena = sql.substring(parentesisIniIndex + 1,
//							parentesisFinIndex);
//					String[] condiciones = cadena.split(" AND ");
//
//					for (int i = 0; i < condiciones.length; i++) {
//						int igualIndex = condiciones[i].lastIndexOf("=");
//						if (igualIndex > 0
//								&& !condiciones[i].substring(igualIndex + 1,
//										igualIndex + 2).equals("?")) {
//							listmodel.add(listmodel.getSize(),
//									condiciones[i].replace("\"", ""));
//						}
//					}
//
//				}
//
//			} else {
//				String cadena = sql;
//				if (clausulaOnIndex + 3 < clausulaWhereIndex)
//					cadena = sql.substring(clausulaOnIndex + 3,
//							clausulaWhereIndex);
//				String[] condiciones = cadena.split(" AND ");
//
//				for (int i = 0; i < condiciones.length; i++) {
//					int igualIndex = condiciones[i].lastIndexOf("=");
//					if (igualIndex > 0
//							&& !condiciones[i].substring(igualIndex + 1,
//									igualIndex + 2).equals("?"))
//						listmodel.add(listmodel.getSize(), condiciones[i]);
//				}
//
//			}
//
//			indice++;
//		}		
//		return listmodel;
//	}
	//FIN NUEVO

	/**
	 * Acción que se lanza cada vez que se modifica la selección de una
	 * layerfamily dentro del combo de layerfamilies
	 * 
	 * @param e
	 */
	private void cmbLayerFamily_actionPerformed(ActionEvent e) {
		if (cmbLayerFamily.getSelectedIndex() > 0) {

			try {
				familiaSeleccionada = ((LayerFamilyTable) getCmbLayerFamily()
						.getSelectedItem()).getLayerFamily();
				idLayerFamily = Integer.parseInt(familiaSeleccionada
						.getSystemId());

				// Habilitar zona inferior de la pantalla
				habilitarZonaInferior(true);

				this.repaint();

			} catch (Exception ex) {
				idLayerFamily = 0;
				familiaSeleccionada = null;
				return;

			}
		}
	}

	/**
	 * Añade una columna de una tabla de GeoPISTA como atributo de una capa
	 * 
	 * @param e
	 */
	private void btnAnadirAtributo_actionPerformed(ActionEvent e) {
		if (numFilasSelec != 0) {

			TableRow datos[] = ((TableAttributesModel) tblAtributos.getModel())
					.getData();
			TableRow datosTotal[] = anadirAtributos(datos);

			((TableAttributesModel) tblAtributos.getModel())
					.setData(datosTotal);
			this.remove(scrollAtributos);
			this.add(getScrollAtributos());

			getScrollAtributos().updateUI();
			this.updateUI();
			this.repaint();

			numFilasSelec = 0;
			columnasSeleccionadas = new Column[0];
			jPanelTablas.getTree().clearSelection();
			btnAnadirAtributo.setEnabled(false);
			btnAnadirClaves.setEnabled(false);
			btnEliminarAtributo.setEnabled(true);
		}
	}

	/**
	 * Elimina una columna de una tabla de GeoPISTa como atributo de una capa
	 * 
	 * @param e
	 */
	private void btnEliminarAtributo_actionPerformed(ActionEvent e) {
		TableRow datos[] = ((TableAttributesModel) tblAtributos.getModel())
				.getData();
		TableRow datosTotal[] = eliminarAtributos(datos);

		((TableAttributesModel) tblAtributos.getModel()).setData(datosTotal);
		this.remove(scrollAtributos);
		this.add(getScrollAtributos());

		ListSelectionModel rowSM = tblAtributos.getSelectionModel();
		if (selectedTableRow == 0)
			rowSM.setSelectionInterval(0, 0);
		else if (selectedTableRow == tblAtributos.getModel().getRowCount())
			rowSM.setSelectionInterval(selectedTableRow - 1,
					selectedTableRow - 1);
		else
			rowSM.setSelectionInterval(selectedTableRow, selectedTableRow);

		if (tblAtributos.getModel().getRowCount() == 0)
			btnEliminarAtributo.setEnabled(false);

		getScrollAtributos().updateUI();
		this.updateUI();
		this.repaint();

	}

	/**
	 * Acción que se lanza cada vez que se modifica la selección dentro del
	 * combo de tipos de base de datos
	 * 
	 * @param e
	 */
	private void cmbBaseDatos_actionPerformed(ActionEvent e) {
		saveQuery(tipoBD, tipoQuery);

		tipoBD = cmbBaseDatos.getSelectedIndex() + 1;
		if (tipoQuery < 0)
			return;
		else
			cargarQuery();
	}

	/**
	 * Acción que se lanza cada vez que se modifica la selección dentro del
	 * combo de tipos de consultas SQL
	 * 
	 * @param e
	 */
	private void cmbQuery_actionPerformed(ActionEvent e) {
		Query queryOriginal = (Query) htQueriesOriginal
				.get(new Integer(tipoBD));
		if (!(queryOriginal != null
				&& queryOriginal.getQuery(tipoQuery) != null && queryOriginal
				.getQuery(tipoQuery).equalsIgnoreCase(txtAreaSQL.getText()))
				|| isQueryGenerada) {
			saveQuery(tipoBD, tipoQuery);
			isQueryGenerada = false;
		}

		tipoQuery = cmbQuery.getSelectedIndex();
		if (tipoBD < 0)
			return;
		else
			cargarQuery();

		// Se habilita el botón de test únicamente para las select
		if (tipoQuery == SELECT)
			btnProbar.setEnabled(true);
		else
			btnProbar.setEnabled(false);

	}

	/**
	 * Guarda una consulta SQL
	 * 
	 * @param tipoBD
	 *            Tipo de Base de Datos
	 * @param tipoQuery
	 *            Tipo de consulta SQL
	 */
	private void saveQuery(int tipoBD, int tipoQuery) {
		if (tipoBD > 0 && tipoQuery >= 0) {
			Query queryModificada = (Query) htQueries.get(new Integer(tipoBD));
			if (queryModificada == null)
				queryModificada = new Query();

			queryModificada.setQuery(tipoQuery, txtAreaSQL.getText());

			htQueries.put(new Integer(tipoBD), queryModificada);
		}
	}

	/**
	 * Carga los datos de las consultas de la capa en el sistema
	 * 
	 */
	private void cargarQuery() {
		Query queryModificada = (Query) (htQueries.get(new Integer(tipoBD)));
		Query queryOriginal = (Query) (htQueriesOriginal
				.get(new Integer(tipoBD)));
		String sql = "";

		if (queryModificada != null || queryOriginal != null) {
			if (queryModificada != null
					&& queryModificada.getQuery(tipoQuery) != null)
				sql = queryModificada.getQuery(tipoQuery);
			else if (queryOriginal != null
					&& queryOriginal.getQuery(tipoQuery) != null)
				sql = queryOriginal.getQuery(tipoQuery);

		}
		txtAreaSQL.setText(sql);
	}

	/**
	 * Baja un atributo de una capa a la siguiente posición
	 * 
	 * @param e
	 */
	private void btnBajar_actionPerformed(ActionEvent e) {
		TableAttributesModel modelo = (TableAttributesModel) tblAtributos
				.getModel();
		TableRow[] datos = modelo.getData();

		TableRow actual = datos[selectedTableRow];
		TableRow anterior = datos[selectedTableRow + 1];

		datos[selectedTableRow] = anterior;
		datos[selectedTableRow + 1] = actual;
		modelo.setData(datos);

		this.remove(scrollAtributos);
		this.add(getScrollAtributos());

		ListSelectionModel rowSM = tblAtributos.getSelectionModel();
		rowSM.setSelectionInterval(selectedTableRow + 1, selectedTableRow + 1);

		getScrollAtributos().updateUI();
		this.updateUI();
		this.repaint();
	}

	/**
	 * Sube un atributo de una capa a su posición anterior
	 * 
	 * @param e
	 */
	private void btnSubir_actionPerformed(ActionEvent e) {
		TableAttributesModel modelo = (TableAttributesModel) tblAtributos
				.getModel();
		TableRow[] datos = modelo.getData();

		TableRow actual = datos[selectedTableRow];
		TableRow anterior = datos[selectedTableRow - 1];

		datos[selectedTableRow] = anterior;
		datos[selectedTableRow - 1] = actual;
		modelo.setData(datos);

		this.remove(scrollAtributos);
		this.add(getScrollAtributos());

		ListSelectionModel rowSM = tblAtributos.getSelectionModel();
		rowSM.setSelectionInterval(selectedTableRow - 1, selectedTableRow - 1);

		getScrollAtributos().updateUI();
		this.updateUI();
		this.repaint();
	}
	
	//NUEVO
    private void setExportImportCorrect(int correct){
    	this.correct = correct;
    }
    
    private int getExportImportCorrect(){
    	return this.correct;
    }
    
    private void setExportImportTotal(int total){
    	this.total = total;
    }
    
    private int getExportImportTotal(){
    	return this.total;
    }
    
	private void cleanExportImportResults(){
		this.correct = 0;
		this.total = 0;
	}
    //FIN NUEVO
	
	//NUEVO	
	private void btnExport_actionPerformed(ActionEvent e) {
		//String layerName = ((LayerTable) getCmbLayer().getSelectedItem()).getLayer().getDescription();
		//ExportImportDialog exportImportDialog = new ExportImportDialog(ExportImportDialog.MODE_EXPORT, layerName);
		ExportImportDialog exportImportDialog = new ExportImportDialog(ExportImportDialog.MODE_EXPORT, locale, getCmbLayer());
		if (exportImportDialog.showDialog() && getFileChooserExport().showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			if(exportLayerToXml(getFileChooserExport().getSelectedFile().getAbsolutePath(), exportImportDialog) && getExportImportTotal()>0 && getExportImportCorrect()>0) 
				JOptionPane.showMessageDialog(this, I18N.get("GestorCapas","layerutil.exportimport.message.exportar.correcto") + " (" + getExportImportCorrect() + " / " +  getExportImportTotal() + ")", I18N.get("GestorCapas","layerutil.exportimport.title.exportar.capa"), JOptionPane.INFORMATION_MESSAGE);		
			else
				JOptionPane.showMessageDialog(this, I18N.get("GestorCapas","layerutil.exportimport.message.exportar.error"), I18N.get("GestorCapas","layerutil.exportimport.title.exportar.capa"), JOptionPane.INFORMATION_MESSAGE);
		} 
	}
	//FIN NUEVO

	//NUEVO
	/**
	 * Exporta una Capa junto con sus dependencias a un fichero XML 
	 * 
	 */
	private boolean exportLayerToXml(String path, ExportImportDialog exportImportDialog){
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);        
		final String finalPath = path;  
		final ExportImportDialog finalExportImportDialog = exportImportDialog;			
		cleanExportImportResults();	
		
		progressDialog.setSize(480, 90);
	    progressDialog.setTitle(I18N.get("GestorCapas","layerutil.exportimport.title.exportar.datosCapa"));
		progressDialog.report(I18N.get("GestorCapas","layerutil.exportimport.title.exportar.datosCapa"));   
	    progressDialog.allowCancellationRequests();
	    progressDialog.addComponentListener(new ComponentAdapter()
	           {
	               public void componentShown(ComponentEvent e)
	               {
	                   new Thread(new Runnable()
	                       {	                	   
	                           public void run()
	                           {
		                        	FileOutputStream fileOutputStream = null;
									try {  
										int layersExported = 0;
										int layersToExport = finalExportImportDialog.getSelectedLayerIndexes().size();
										setExportImportTotal(layersToExport);
										Iterator itLayer = finalExportImportDialog.getSelectedLayerIndexes().iterator();
			                        	while(itLayer.hasNext()){  	 
			                        		String exportState = "Exportadas: " + layersExported + " / " + layersToExport + " - "; 
			                        		
			                        		Integer indexLayer = (Integer) itLayer.next();
			                        		
			                        		fileOutputStream = null;
			                        		try{
												LayerOperations layerOp = new LayerOperations();
												TablesDBOperations tableOp = new TablesDBOperations();
			
												LocalGISXmlLayer localGISXmlLayer = new LocalGISXmlLayer();			
												LayerTable layerTable = (LayerTable) getCmbLayer().getItemAt(indexLayer+1);			
															
												String exportLayer = "Capa " + layerTable.getHtNombre().get(locale) + ": ";
												
												//RECUPERAMOS LA TABLA BD	
												if(finalExportImportDialog.isCheckExportImportTablesChecked()){	
													ArrayList newTables = ExportImportUtils.getTablesByAttributes(layerOp.buscarColumnasAtributos(layerTable.getIdLayer()));											
													if(newTables!=null){
														progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.tabla"));
														localGISXmlLayer.setTables(newTables);
													}
																		
													//RECUPERAMOS LAS COLUMNAS DB	
													HashMap newColumnsDB = new HashMap();
													Iterator itTables = newTables.iterator();
													while(itTables.hasNext()){
														Table newTable = (Table) itTables.next();
														HashMap columnsDB = tableOp.obtenerListaColumnasBD(newTable.getName());
														if(columnsDB!=null){
															//Modifica valores inconsistentes para la correcta importacion
															Iterator itColumnsDB = columnsDB.keySet().iterator();
															while(itColumnsDB.hasNext()){
																Integer key = (Integer) itColumnsDB.next();
																ColumnDB columnDB = (ColumnDB) columnsDB.get(key);																
																if(columnDB.getName().equals("id")){// || columnDB.getName().equals("revision_actual")){
																	//columnDB.setPrimary(true);
																	columnDB.setUnique(true);
																}
																if(columnDB.getDefaultValue()!=null && columnDB.getDefaultValue().lastIndexOf("::")>-1)
																	columnDB.setDefaultValue(columnDB.getDefaultValue().replace(columnDB.getDefaultValue().substring(columnDB.getDefaultValue().lastIndexOf("::")),""));												
															}
															progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.columnasTabla"));												
															newColumnsDB.put(newTable.getDescription(),columnsDB);
														}
													}
													localGISXmlLayer.setColumnsDB(newColumnsDB);
												}
														
												
												//RECUPERAMOS LA CAPA
												if(finalExportImportDialog.isCheckExportImportLayerChecked()){
													if(layerTable!=null){
														progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.capa"));
														localGISXmlLayer.setLayer(layerTable);		
													}
													
													//RECUPERAMOS ATRIBUTOS
													//ArrayList newAttributes = ExportImportUtils.getLayerAttibutes(((TableAttributesModel) tblAtributos.getModel()).getData());			
													if(layerTable!=null){
														ArrayList newAttributes = ExportImportUtils.getLayerAttibutes(recuperarAtributos(layerOp.buscarColumnasAtributos(layerTable.getIdLayer())).getData());			
														if(newAttributes!=null){
															progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.atributosCapa"));
															localGISXmlLayer.setAttributes(newAttributes);
														}
													}
													//RECUPERAMOS LAS QUERIES (SELECT - INSERT - UPDATE - DELETE)
													if(layerTable!=null){
														Hashtable newQueries = layerOp.buscarQueries(layerTable.getIdLayer());
														if(newQueries!=null){
															progressDialog.report(exportState + exportLayer + "Capa " + layerTable.getHtNombre().get(locale) + ": " + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.consultasCapa"));
															localGISXmlLayer.setQueries(newQueries);
														}
													}
												}
												
												//RECUPERAMOS LOS ESTILOS
												if(layerTable!=null){
													String style = layerOp.obtenerEstilos(layerTable.getIdLayer());			
													if(style!=null){
														progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.estilos"));
														localGISXmlLayer.setStyles(style);
													}
												}
												
												//RECUPERAMOS LOS DOMINIOS				
												if(finalExportImportDialog.isCheckExportImportDomainsChecked()){
													ListaDomain newDomains = null;
													//ArrayList attributes = ExportImportUtils.getLayerAttibutes(((TableAttributesModel) tblAtributos.getModel()).getData());
													if(layerTable!=null){
														ArrayList attributes = ExportImportUtils.getLayerAttibutes(recuperarAtributos(layerOp.buscarColumnasAtributos(layerTable.getIdLayer())).getData());
														if(attributes!=null){											
															ListaDomain listaDomain = new ListaDomain();
															ListaDomain listaDomainParticular = new ListaDomain();
															(new OperacionesAdministrador(Constantes.url)).getDominios(null,listaDomain,listaDomainParticular);
															newDomains = new ListaDomain();
															Iterator itAttributes = attributes.iterator();	
															while(itAttributes.hasNext()){
																Attribute attribute = (Attribute) itAttributes.next();		
																if(attribute.getColumn().getDomain()!=null)
																	newDomains.add(listaDomain.getByName(attribute.getColumn().getDomain().getName()));
															}
														}
														if(newDomains!=null){
															progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.dominios"));
															localGISXmlLayer.setDomains(newDomains);
														}
													}
												}
																	
												
												//RECUPERAMOS LAS FAMILIA DE CAPAS			
												if(finalExportImportDialog.isCheckExportImportLayerFamiliesChecked()){
													ArrayList newLayerFamilies = null;
													LayerFamilyTable [] layerFamilyTable = layerOp.obtenerLayerFamilyTable();
													if(layerFamilyTable.length>0){
														newLayerFamilies = new ArrayList();
														Vector layerFamilyIds = layerOp.obtenerLayerfamiliesConLayer(layerTable.getIdLayer());
														Iterator itLayerFamilyIds = null;
														for(int i=0;i<layerFamilyTable.length;i++){
															itLayerFamilyIds = layerFamilyIds.iterator();
															while(itLayerFamilyIds.hasNext()){
																Integer layerFamilyId = (Integer) itLayerFamilyIds.next();
																if(layerFamilyTable[i].getIdLayerFamily()==layerFamilyId){
																	newLayerFamilies.add(layerFamilyTable[i]);
																	break;
																}
															}
														}
														if(newLayerFamilies!=null){
															progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.familiasCapa"));
															localGISXmlLayer.setLayerFamilies(newLayerFamilies);
														}
													}
												}
						
					
												//CREAMOS EL FICHERO XML Y SERIALIZAMOS EL BEAN LocalGISXmlLayer
												progressDialog.report(exportState + exportLayer + I18N.get("GestorCapas","layerutil.exportimport.title.exportar.guardando"));
												String now = (new SimpleDateFormat("yyyy-MM-dd_HH.mm").format(new Date())).toString();
												fileOutputStream = new FileOutputStream(finalPath + "\\" + layerTable.getLayer().getDescription() + "_" + now + ".xml");
												getXStreamSerializer().toXML(localGISXmlLayer, new OutputStreamWriter(fileOutputStream, "UTF-8"));										
												layersExported++;
												setExportImportCorrect(layersExported);
					                        } catch (Exception ex) {
					    	            		System.out.println(ex.getMessage());
					    	            		Logger.println(ex.getMessage());					    	            		
					    	            	} 	
			                        	} 	
							}
	                       	finally{
	                       		if(fileOutputStream!=null){
		                       		try {
										fileOutputStream.close();
									} catch (IOException ex) {
										Logger.println(ex.getMessage());
									}
	                       		}
                               progressDialog.setVisible(false);
                           }
	       				} 		                           
	                    }).start();
	                }	                        
	    });		
	    GUIUtil.centreOnScreen(progressDialog);        
        progressDialog.setVisible(true);
        progressDialog.toFront();    
		System.out.println("BIEN EXPORTAR");
		return !progressDialog.isCancelRequested();
	}
	//FIN NUEVO
		
	//NUEVO
	private void btnImport_actionPerformed(ActionEvent e) {
		if (getFileChooserImport().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			ExportImportDialog exportImportDialog = new ExportImportDialog(ExportImportDialog.MODE_IMPORT,getFileChooserImport().getSelectedFiles());
			if (exportImportDialog.showDialog()){ 
				if(importLayerFromXml(getFileChooserImport().getSelectedFiles(), exportImportDialog) && getExportImportTotal()>0 && getExportImportCorrect()>0)
					JOptionPane.showMessageDialog(this, I18N.get("GestorCapas","layerutil.exportimport.message.importar.correcto") + " (" + getExportImportCorrect() + " / " +  getExportImportTotal() + ")", I18N.get("GestorCapas","layerutil.exportimport.title.importar.capa"), JOptionPane.INFORMATION_MESSAGE);
				else 
					JOptionPane.showMessageDialog(this, I18N.get("GestorCapas","layerutil.exportimport.message.importar.error"), I18N.get("GestorCapas","layerutil.exportimport.title.importar.capa"), JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	//FIN NUEVO		
	
	//NUEVO
	/**
	 * Importa una Capa junto con sus dependencias desde un fichero XML 
	 * 
	 */
	private boolean importLayerFromXml(File [] xmlFiles, ExportImportDialog exportImportDialog){
			final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);  			
			final File [] finalXMLFiles = xmlFiles;  
			final ExportImportDialog finalExportImportDialog = exportImportDialog;			
			cleanExportImportResults();	
			
			progressDialog.setSize(480, 90);
		    progressDialog.setTitle("ImportandoDatosCapa");
			progressDialog.report(I18N.get("GestorCapas","layerutil.exportimport.title.importar.datosCapa"));   
		    progressDialog.allowCancellationRequests();
		    progressDialog.addComponentListener(new ComponentAdapter()
		           {
		               public void componentShown(ComponentEvent e)
		               {
		                   new Thread(new Runnable()
		                       {		                	   
		                           public void run()
		                           {
		                        	   try{
		                        		   int layersImported = 0;
		                        		   int layersToImport = finalExportImportDialog.getSelectedLayerIndexes().size();
		                        		   setExportImportTotal(layersToImport);
		                        		   Iterator itFinalXMLFilesIndex = finalExportImportDialog.getSelectedLayerIndexes().iterator();
		                        		   while(itFinalXMLFilesIndex.hasNext()){
		                        			   String importState = "Importadas: " + layersImported + " / " + layersToImport + " - "; 
		                        			   
		                        			   try {             
													File finalXMLFile = (File) finalXMLFiles[(Integer)itFinalXMLFilesIndex.next()];									
											        
													String importFile = "Fichero " + finalXMLFile.getName() + ": ";
													
													
													LayerOperations layerOp = new LayerOperations();
											        TablesDBOperations tableOp = new TablesDBOperations();
											        LayerFamilyOperations layerFamilyOp = new LayerFamilyOperations();
											                        	   
													LocalGISXmlLayer localGISXmlLayer = (LocalGISXmlLayer) getXStreamSerializer()
															.fromXML(finalXMLFile);
													
													// IMPORTAR Tabla
													HashMap<String,String> oldTablesName = null;
													ArrayList newTables = null;
													if (finalExportImportDialog.isCheckExportImportTablesChecked()){
														newTables = localGISXmlLayer.getTables();
														if(newTables != null) {
															
															progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.tabla"));
															
															oldTablesName = new HashMap<String,String>();												
															Iterator itTables = newTables.iterator();												
															while(itTables.hasNext()){
																Table newTable = (Table) itTables.next();
																if (newTable.getDescription() != null
																		&& !newTable.getDescription().equals("")) {	
																	
																	//Comprobamos si existe una tabla con el mismo nombre 
																	//y si existe, si queremos importar la tabla con otro nombre o utilizar la existente
																	newTable.setDescription(newTable.getDescription());
																	String oldTableName = newTable.getDescription();
																	if(!finalExportImportDialog.isCheckUseCoincidentTableChecked()){
																		while (tableOp.existeTabla(newTable
																				.getDescription())) {
																			if (!changeTableName(newTable))																
																				break;															
																		}
																	}
																	
																	if (!tableOp
																			.existeTabla(newTable.getDescription())) {
																		newTable.setDescription(newTable.getDescription());
																		if (tableOp.crearTablaBD(newTable)) {
																			//CREAMOS SECUENCIA
//																			layerOp.crearSecuencia(newTable.getName());
																			
																			oldTablesName.put(newTable.getDescription(),oldTableName);
																			
																			progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.columnasTabla"));
																			
																			ColumnRow colRow = null;
																			Column colSis = null;
																			
																			//IMPORTAR COLUMNAS DB
																			HashMap newColumnsDB = localGISXmlLayer.getColumnsDB();
																			Iterator itNewColumnsDBKeys = newColumnsDB.keySet().iterator();
																			while(itNewColumnsDBKeys.hasNext()){																				
																				String ColumnsDBKeys = (String)itNewColumnsDBKeys.next();	
																				if(ColumnsDBKeys.equals(oldTableName)){	
																					HashMap ColumnsDB = (HashMap)newColumnsDB.get(ColumnsDBKeys); 
																					Iterator itColumnsDB = ColumnsDB.keySet().iterator();																
																					while(itColumnsDB.hasNext()){
																						ColumnDB columnDB = (ColumnDB) ColumnsDB.get(itColumnsDB.next());
																						if(columnDB.getTableName().equals(oldTableName)){
																							columnDB.setTableName(newTable
																									.getDescription());
																							
																							colSis = new Column(columnDB.getName(),columnDB.getDescription(),null);
																							colSis.setDescription(colSis.getName());																			
																							//DOMINIOS COLUMNAS																		
																							
																							colRow = new ColumnRow();	
																							colRow.setColumnaSistema(colSis);
																							colRow.setColumnaBD(columnDB);
																							//CREA LA SECUENCIA
																							if(colSis.getName()!=null && colSis.getName().equals("GEOMETRY")){ 
																								try{
																									layerOp.crearSecuencia(newTable.getName());																	
																								}
																								catch(Exception e){
																									Logger.println("ERROR AL CREAR SEQUENCIA DE " + newTable.getName() + ": " + e.getMessage());
																								}
																							}
																							if(!tableOp.crearColumnaBD(newTable,colRow)){
																								System.out.println("ERROR: COLUMNBD " + colRow.getColumnaBD().getName());
																							}
																						}
																					}
																				}
																			}
																			
																			Identificadores.put("TablasModificadas", true); 
																		}
																		else{
																			System.out.println("ERROR: TABLE");
																		}
																	}
																}
															}
														}
													}	
													
													//IMPORTAR DOMINIOS
													ListaDomain newDomains = null;
													if(finalExportImportDialog.isCheckExportImportDomainsChecked()){												
														newDomains = localGISXmlLayer.getDomains();
														if(newDomains!=null){											
														
															progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.dominios"));
															
															//IMPORTAR DOMINIOS Y NODOS DE DOMINIO
															ListaDomain listaDomain = new ListaDomain();
															ListaDomain listaDomainParticular = new ListaDomain();
															(new OperacionesAdministrador(Constantes.url)).getDominios(null,listaDomain,listaDomainParticular);
															Iterator itNewDomains = newDomains.getDom().keySet().iterator();
															while(itNewDomains.hasNext()){														
																com.geopista.protocol.administrador.dominios.Domain newDomain = (com.geopista.protocol.administrador.dominios.Domain) newDomains.get((String)itNewDomains.next());
																Iterator itDomains = listaDomain.getDom().keySet().iterator();
																boolean existsDomain = false;
																while(itDomains.hasNext()){
																	com.geopista.protocol.administrador.dominios.Domain domain = (com.geopista.protocol.administrador.dominios.Domain) listaDomain.get((String)itDomains.next());
																	if(newDomain.getIdCategory().equals(domain.getIdCategory())){
																		if(newDomain.getName().equals(domain.getName())){
																			if(!newDomain.getIdDomain().equals(domain.getIdDomain())){
																				newDomain.setIdDomain(domain.getIdDomain());	
																				//CResultadoOperacion resultado=(new OperacionesAdministrador(Constantes.url)).actualizarDomain(newDomain);
																			}
																			existsDomain = true;
																			//Si existe compara los nodos nodos
																			//REVISAR
																			break;
																		}
																	}
																}
																//Si no existe se importa como nuevo, incluyendo sus nodos 
																if(!existsDomain){
																	CResultadoOperacion resultadoDomain=(new OperacionesAdministrador(Constantes.url)).nuevoDomain(newDomain);
																	if(resultadoDomain.getResultado()){
																		if(!ExportImportUtils.insertDomainNodes(resultadoDomain.getDescripcion(), null, newDomain.getListaNodes()))
																			System.out.println("ERROR: DOMAIN");
																	}
																	//Identificadores.put("DominiosInsertados", true);
																}
															}
														}												
													}
													
													//IMPORTAR CAPA
													String oldLayerName = null;
													LayerTable newLayer = null;
													if (finalExportImportDialog.isCheckExportImportLayerChecked()){
														newLayer = localGISXmlLayer.getLayer();
														if(newLayer != null) {
															
															progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.capa"));
															
															oldLayerName = newLayer.getLayer().getDescription();
															newLayer.setIdLayer(0);		
															newLayer.setAcl(layerOp.getAcl(ExportImportUtils.ACL_IMPORT_LAYER));						
															if (newLayer.getLayer().getDescription() != null
																	&& !newLayer.getLayer().getDescription().equals("")) {	
																					
																//Comprobamos si existe una capa con el mismo nombre 
																//y si existe, si queremos importar la capa con otro nombre o utilizar la existente
																if(!finalExportImportDialog.isCheckUseCoincidentLayersChecked()){
																	while (layerOp.existeCapaId(newLayer.getLayer().getDescription())) {
																		if (!changeLayerName(newLayer))
																			break;
																	}
																}
																
																if (!layerOp.existeCapaId(newLayer.getLayer().getDescription())) {	
																	if(!newLayer.getLayer().getDescription().equals(oldLayerName))
																		ExportImportUtils.addToHashtableTranslation(newLayer.getHtNombre(),"_EXP");
																	int idDictionary = layerOp.actualizarDictionary(newLayer.getHtNombre(), 0);
																	if (idDictionary > 0)
																		newLayer.getLayer().setName(String.valueOf(idDictionary));							
																	if (layerOp.actualizarLayer(newLayer, null)==0) {
																		System.out.println("ERROR: LAYER");
																	}
																	
																	//if (!newLayer.getLayer().getDescription().equals(oldLayerName)){
																		//Capa Versionada
																		if(newTables!=null && newLayer.getLayer().isVersionable())
																			crearVersionImportada(layerOp, newLayer, newTables);
																		
																		//Capa Dinamica
																		if(newLayer.getLayer().isDinamica()){
																			String urlMapServer = publicarCapa(layerOp, newLayer);
																			if (urlMapServer.equals("")) {
																				newLayer.getLayer().setUrl(null);
																			} else
																				newLayer.getLayer().setUrl(urlMapServer);
																		}																																	
																	
																		//IMPORTAR ATRIBUTOS
																		ArrayList newAttributes = localGISXmlLayer.getAttributes();
																		if(newAttributes!=null){
																			progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.atributosCapa"));
																			
																			//RECUPERACION DE TABLAS 
																			ArrayList tables = null;
																			if(newTables!=null)
																				tables = newTables;
																			else
																				tables = ExportImportUtils.getTablesByAttributes(newAttributes);														
																			
																			if(tables!=null){
																				Iterator itTables = tables.iterator();
																				while(itTables.hasNext()){																
																					Table newTable = (Table) itTables.next();																
																				
																					List tableColumns = layerOp.obtenerListaColumnas(newTable);							
																				
																					Iterator itAttributes = newAttributes.iterator();
																					while(itAttributes.hasNext()){
																						Attribute newAttribute = (Attribute) itAttributes.next();
																						newAttribute.getColumn().setIdColumn(ExportImportUtils.getIdColumnFromColumnsList(tableColumns,newAttribute.getColumn().getName()));
																						newAttribute.setIdLayer(newLayer.getIdLayer());
																						newAttribute.setSystemID(0);
																						newAttribute.setEditable(newAttribute.isEditable());
																						if(newDomains!=null){
																							Domain domain = newAttribute.getColumn().getDomain();																			
																							if(domain!=null){
																								com.geopista.protocol.administrador.dominios.Domain newDomain = newDomains.getByName(domain.getName());
																								domain.setSystemID(Integer.parseInt(newDomain.getIdDomain()));
																								if(layerOp.obtenerDominioTipo(Integer.parseInt(newDomain.getIdDomain()), newDomain.getName())==null){								
																									newAttribute.getColumn().setDomain(null);
																									if(newAttribute.getColumn().getIdColumn()!=0)																										
																										layerOp.actualizarDominioColumna(newAttribute.getColumn(), domain, newAttribute.getColumn().getLevel());
																								}
																							}
																						}
																						//Si no importa los dominios
																						else newAttribute.getColumn().setDomain(null);																			
																						newAttribute.getColumn().setTable(newTable);																						
																						//idDictionary = layerOp.actualizarDictionary(newAttribute.getHtTraducciones(),newAttribute.getIdAlias());
																						idDictionary = layerOp.actualizarDictionary(newAttribute.getHtTraducciones(),0);
																						if (idDictionary > 0)
																							newAttribute.setIdAlias(idDictionary);								
																						if(layerOp.actualizarAtributo(newAttribute)==-1){
																							System.out.println("ERROR: ATTRIBUTES");
																						}
																					//}
																				}
																			}
																		}
																	}
																
															
																	//IMPORTAR QUERIES
																	Hashtable newQueries = localGISXmlLayer.getQueries();
																	if (newQueries!=null && !newQueries.isEmpty()){	
																		
																		progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.consultasCapa"));
																		if(newTables!=null){
																			Iterator itTables = newTables.iterator();
																			while(itTables.hasNext()){
																				Table newTable = (Table) itTables.next();
																				//Reemplazamos el nombre de la tabla en las queries si el nombre ha sido cambiado
																				if(oldTablesName!=null && oldTablesName.size()>0 && !newTable.getDescription().equals(oldTablesName.get(newTable.getDescription()))){
																					Query newQuery = ((Query)newQueries.get(POSTGRE));
																					newQuery.setSelectQuery(newQuery.getSelectQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
																					newQuery.setInsertQuery(newQuery.getInsertQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
																					newQuery.setUpdateQuery(newQuery.getUpdateQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
																					newQuery.setDeleteQuery(newQuery.getDeleteQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
																				}
																			}
																		}
																		if(layerOp.actualizarConsultas(newLayer.getIdLayer(),newQueries)==-1){
																			System.out.println("ERROR: QUERIES");
																		}	
																	}
																}	
															}
															
															//IMPORTAR ESTILOS
															String style = localGISXmlLayer.getStyles();
															if (!oldLayerName.equals(newLayer.getLayer().getDescription()))
																style = style.replaceAll(oldLayerName, newLayer.getLayer().getDescription());													
															if(style!=null){
																progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.estilos"));
																if(newLayer!=null && newLayer.getIdLayer()==0){
																	try{																			
																		//newLayer.setIdLayer(Integer.parseInt(newLayer.getLayer().getName()));
																		newLayer.setIdLayer(ExportImportUtils.getLayerIdByLayerList(layerOp.obtenerLayerTable(true), newLayer.getLayer().getDescription()));
																	}catch(NumberFormatException e){}
																}
																if(layerOp.actualizarEstilos(newLayer.getIdLayer(), style)==-1){
																	System.out.println("ERROR: ACTUALIZAR ESTILOS");
																}	
															}

														}
													}																								
													
													//IMPORTAR FAMILIAS DE CAPA											
													ArrayList newLayerFamilies = null;		
													if(finalExportImportDialog.isCheckExportImportLayerFamiliesChecked()){
														newLayerFamilies = localGISXmlLayer.getLayerFamilies();
														if(newLayerFamilies!=null){
														
															progressDialog.report(importState + importFile + I18N.get("GestorCapas","layerutil.exportimport.title.importar.familiasCapa"));
															String oldLayerFamilyName = null;
															
															Iterator itLayerFamilies = newLayerFamilies.iterator();
															while(itLayerFamilies.hasNext()){
																LayerFamilyTable newLayerFamily = (LayerFamilyTable) itLayerFamilies.next();
																
																oldLayerFamilyName = (String)newLayerFamily.getHtNombre().get("es_ES");
																if (((String)newLayerFamily.getHtNombre().get("es_ES")) != null
																		&& !((String)newLayerFamily.getHtNombre().get("es_ES")).equals("")) {	
																	
																	LayerFamilyTable [] layerFamilyTable = layerFamilyOp.obtenerLayerFamilyTable();	
																	
																	//Comprobamos si existe una familia de capas con el mismo nombre 
																	//y si existe, si queremos importar la familia de capas con otro nombre o utilizar la existente
																	if(!finalExportImportDialog.isCheckCoincidentUseLayerFamiliesChecked()){
																		while (ExportImportUtils.existeLayerFamily(layerFamilyTable,newLayerFamily)) {
																			if (!changeLayerFamilyName(newLayerFamily))
																				break;
																		}
																	}
																	
																	if (!ExportImportUtils.existeLayerFamily(layerFamilyTable,newLayerFamily)){	
//																		if(!newLayerFamily.getHtNombre().get("es_ES").equals(oldLayerFamilyName))
//																			ExportImportUtils.addToHashtableTranslation(newLayerFamily.getHtNombre(),"_EXP");
																		newLayerFamily.setHtDescripcion(newLayerFamily.getHtNombre());	
																		if (layerFamilyOp.crearLayerFamily(newLayerFamily)==-1) {
																			System.out.println("ERROR: LAYER FAMILY");
																		}							
																	}		
																	else
																		ExportImportUtils.changeLayerFamilyId(layerFamilyTable,newLayerFamily);	
																	
																	if(newLayer!=null && newLayer.getIdLayer()==0){
																		try{																			
																			//newLayer.setIdLayer(Integer.parseInt(newLayer.getLayer().getName()));
																			newLayer.setIdLayer(ExportImportUtils.getLayerIdByLayerList(layerOp.obtenerLayerTable(true), newLayer.getLayer().getDescription()));
																		}catch(NumberFormatException e){}
																	}
																
																	//Si no se ha importado una capa no asocia las familias de capas importas a ninguna capa
																	if(newLayer!=null && newLayer.getIdLayer()!=0){								
																		if(layerFamilyOp.obtenerLayerfamiliesConLayer(newLayer.getIdLayer()).size()==0){
																			layerFamilyOp.asociarLayerFamilyLayer(newLayerFamily.getIdLayerFamily(), newLayer.getIdLayer());
																		}
																	}
																	Identificadores.put("CapasModificadas", true);
																}
															}	
														}
													}		
													enter();
													layersImported++;
													setExportImportCorrect(layersImported);
				                       	} catch (Exception ex) {
				            				System.out.println(ex.getMessage());
				            				Logger.println(ex.getMessage());				            				
				            			} 
		                        	}
		                        }
		                       	finally
                                {
                                    progressDialog.setVisible(false);
                                }
		       				}     
		                           
//		                    private void cancelProcess(){
//		                    	progressDialog.setVisible(false);
//		                    }
	                    }).start();
	                }
	                        
		  });
		  GUIUtil.centreOnScreen(progressDialog);        
		  progressDialog.setVisible(true);
		  progressDialog.toFront();    
		  System.out.println("BIEN IMPORTAR");
		  return !progressDialog.isCancelRequested();
	}
	//FIN NUEVO
		    
	//NUEVO
	private void crearVersionImportada(LayerOperations layerOp, LayerTable lt, List tables)
			throws DataException {
		try {
			// Obtengo todas las tablas asociadas a la capa
			Iterator itTables = tables.iterator();
			while (itTables.hasNext()) {
				Table table = (Table) itTables.next();		
				if (!table.getName().equals("SYSTEM")) {
					layerOp.crearSecuenciaVersionado(table.getName(), true);
					// Actualizo el campo layers.validator
					if (lt.getLayer().getValidator() == null)
						lt.getLayer().setValidator(VALIDATOR_CLASS);
					else
						lt.getLayer().setValidator(
								lt.getLayer().getValidator() + ";"
										+ VALIDATOR_CLASS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}
	}
	//FIN NUEVO
	
	//NUEVO
	private boolean changeTableName(Table newTable) {
		int election = JOptionPane.showConfirmDialog(this,
				I18N.get("GestorCapas","layerutil.exportimport.message.importar.existeTabla"));
		if (election == 0) {
			String newTableName = JOptionPane.showInputDialog(this,
					I18N.get("GestorCapas","layerutil.exportimport.message.importar.cambiarNombreTabla"),
					newTable.getDescription());
			if (newTableName != null) {
				newTable.setName(newTableName);
				newTable.setDescription(newTableName);				
				return true;
			}
		}
		return false;
	}
	//FIN NUEVO
				
	//NUEVO
	private boolean changeLayerName(LayerTable newLayer) {
		int election = JOptionPane.showConfirmDialog(this,
				I18N.get("GestorCapas","layerutil.exportimport.message.importar.existeCapa"));
		if (election == 0) {
			String newLayerName = JOptionPane.showInputDialog(this,
					I18N.get("GestorCapas","layerutil.exportimport.message.importar.cambiarNombreCapa"),
					newLayer.getLayer().getDescription());
			if (newLayerName != null) {
				newLayer.getLayer().setDescription(newLayerName);				
				return true;
			}
		}
		return false;
	}
	//FIN NUEVO
	
	//NUEVO
	private boolean changeLayerFamilyName(LayerFamilyTable layerFamily){		
		int election = JOptionPane.showConfirmDialog(this,
				I18N.get("GestorCapas","layerutil.exportimport.message.importar.existeFamiliasCapas"));
		if (election == 0) {
		String newLayerFamilyName = JOptionPane.showInputDialog(this,
				I18N.get("GestorCapas","layerutil.exportimport.message.importar.cambiarNombreFamiliasCapas"),
				layerFamily.getHtNombre().get("es_ES"));
		if (newLayerFamilyName != null) {
			layerFamily.getHtNombre().put("es_ES", newLayerFamilyName);				
			return true;
		}
		}
		return false;
	}
	//FIN NUEVO
	
	//NUEVO
	/**
	 * This method initializes xStreamSerializer
	 * 
	 * @return com.thoughtworks.xstream.XStream
	 */
	private XStream getXStreamSerializer() {
		if (xStreamSerializer == null) 
			xStreamSerializer = ExportImportUtils.getNewXStreamSerializer();
		return xStreamSerializer;
	}
	//FIN NUEVO


	//NUEVO
	/**
	 * This method initializes fileChooserExport
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFileChooserExport() {
		if (fileChooserExport == null) {
			fileChooserExport = new JFileChooser();
			fileChooserExport
					.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return fileChooserExport;
	}

	//FIN NUEVO

	//NUEVO
	/**
	 * This method initializes fileChooserImport
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFileChooserImport() {
		if (fileChooserImport == null) {
			fileChooserImport = new JFileChooser();
			fileChooserImport.setFileFilter(new FileNameExtensionFilter(
					"Archivos XML", "xml"));
			fileChooserImport.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooserImport.setMultiSelectionEnabled(true);
		}
		return fileChooserImport;
	}

	//FIN NUEVO
	
	
	/**
	 * Lanza la pantalla que permite la inserción de datos en los diferentes
	 * idiomas dados de alta en el sistema
	 * 
	 * @param e
	 */
	private void btnIdiomas_actionPerformed(ActionEvent e) {
		if (capaNueva) {
			if (txtNombreCapa.getText().length() > 0)
				htNombres.put(locale, txtNombreCapa.getText());
			else
				htNombres.put(locale, "");
		} else {
			// LayerOperations operaciones = new LayerOperations();
			// htNombres = this.htNombres;
			// operaciones.buscarTraduccionNombres (capaSeleccionada);
		}

		JDialogTranslations jDiccionario = showIdiomasDescripcion(htNombres);

		Hashtable hDict = jDiccionario.getDiccionario();
		if (hDict != null)
			htNombres = hDict;
		jDiccionario = null;

		if (htNombres.get(locale) != null)
			txtNombreCapa.setText(htNombres.get(locale).toString());
		else if (htNombres.get(GestorCapas.DEFAULT_LOCALE) != null)
			txtNombreCapa.setText(htNombres.get(GestorCapas.DEFAULT_LOCALE)
					.toString());
		else
			txtNombreCapa.setText("");

	}

	/*
	 * /** Acción realizada al pulsar el botón de SQL
	 * 
	 * @param e
	 */
	/*
	 * private void btnSql_actionPerformed(ActionEvent e) {
	 * 
	 * TableRow datos [] =
	 * ((TableAttributesModel)tblAtributos.getModel()).getData(); TableRow
	 * datosTotal[] = anadirAtributoSQL(datos);
	 * 
	 * ((TableAttributesModel)tblAtributos.getModel()).setData(datosTotal);
	 * this.remove(scrollAtributos); this.add(getScrollAtributos());
	 * 
	 * }
	 */

	/**
	 * Muestra el panel con los diferentes valores de un atributo en los
	 * distintos idiomas dados de alta en el sistema
	 * 
	 * @param nombres
	 *            Hashtable con los nombres para cada idioma como valor y el
	 *            locale correspondiente como clave
	 * @return Diálogo de diccionario para mostrar los valores del atributo en
	 *         los distintos idiomas
	 */
	private JDialogTranslations showIdiomasDescripcion(Hashtable nombres) {
		JDialogTranslations jDiccionario = new JDialogTranslations(
				aplicacion.getMainFrame(), true, nombres, true);
		jDiccionario.setSize(600, 500);
		jDiccionario.setLocationRelativeTo(this);
		jDiccionario.show();
		return jDiccionario;
	}

	/**
	 * Acción realizada al pulsar el botón Salir
	 * 
	 */
	private void jButtonSalirActionPerformed() {
		int n = JOptionPane.showOptionDialog(this,
				I18N.get("GestorCapas", "general.salir.mensaje"), "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[1]);
		if (n == JOptionPane.NO_OPTION)
			return;

		aplicacion.getMainFrame().dispose();
		System.exit(0);
	}

	/**
	 * Escucha los posibles cambios producidos en la selección de elementos del
	 * árbol de tables y columns de GeoPISTA
	 */
	public void valueChanged(TreeSelectionEvent e) {
		if (e == null || !(e.getSource() instanceof JTree))
			return;
		JTree arbol = (JTree) e.getSource();

		TreePath[] paths = arbol.getSelectionPaths();

		if (paths != null) {
			numFilasSelec = paths.length;
			columnasSeleccionadas = new Column[numFilasSelec];

			int index = 0;
			for (int i = 0; i < numFilasSelec; i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i]
						.getLastPathComponent();
				Object nodeInfo = node.getUserObject();
				if (nodeInfo instanceof Column) {
					columnasSeleccionadas[index] = (Column) nodeInfo;
					index++;
				}
			}

			if (numFilasSelec > 0 && index > 0) {
				btnAnadirAtributo.setEnabled(true);
				if (numFilasSelec == 2 && index == 2) {
					btnAnadirClaves.setEnabled(true);
				} else {
					btnAnadirClaves.setEnabled(false);
				}
			} else {
				btnAnadirAtributo.setEnabled(false);
			}
		}
	}

	/**
	 * Obtiene el path de un nodo
	 * 
	 * @param node
	 *            Nodo
	 * @return TreePath del nodo
	 */
	public TreePath getPath(TreeNode node) {
		List list = new ArrayList();

		// Añade todos los nodos a la lista
		while (node != null) {
			list.add(node);
			node = node.getParent();
		}
		Collections.reverse(list);

		// Convierte el array de nodos en TreePath
		return new TreePath(list.toArray());
	}

	/**
	 * Obtiene los path de todos los nodos bajo uno inicial
	 * 
	 * @param inicio
	 *            Nodo inicial
	 * @return Array con los TreePath de los nodos bajo el inicial
	 */
	public TreePath[] getPaths(TreeNode inicio) {

		// Array para almacenar los path
		List list = new ArrayList();

		// Atraviesa el arbol desde la raiz, añadiendo los paths de los nodos a
		// la lista
		getPaths(new TreePath(inicio), list, true);

		// Convierte la lista en array
		return (TreePath[]) list.toArray(new TreePath[list.size()]);
	}

	/**
	 * Obtiene los paths de los nodos
	 * 
	 * @param parent
	 *            Padre
	 * @param list
	 *            Lista de nodos
	 * @param esRaiz
	 *            Verdadero si el nodo es el raíz
	 */
	public void getPaths(TreePath parent, List list, boolean esRaiz) {

		// Añade nodo a la lista
		if (!esRaiz) {
			list.add(parent);
		}

		// Crea los paths para todos los nodos hijos
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				getPaths(path, list, false);
			}
		}
	}

	/**
	 * Añade un atributo de tipo SQL a la lista de atributos de una capa
	 * 
	 * @param origen
	 * @return TableRow generada
	 */
	public TableRow[] anadirAtributoSQL(TableRow[] origen) {

		int numElementos = origen.length;

		Set total = new LinkedHashSet();
		TableRow compFila = new TableRow();

		for (int i = 0; i < numElementos; i++) {

			total.add(origen[i]);
		}

		Table tb = new Table();
		Column col = new Column();
		col.setIdColumn(0);
		col.setName("id" + ++numSQL);
		tb.setName("SYSTEM");
		tb.setIdTabla(0);
		col.setTable(tb);

		compFila = new TableRow(col, new Attribute(), null, false);
		total.add(compFila);

		TableRow[] destino = (TableRow[]) total.toArray(new TableRow[total
				.size()]);

		return destino;

	}

	/**
	 * Añade un atributo a la lista de atributos
	 * 
	 * @param origen
	 *            Nuevo atributo
	 * @return Lista de atributos
	 */
	public TableRow[] anadirAtributos(TableRow[] origen) {

		Set total = new LinkedHashSet();
		TableRow compFila = new TableRow();
		List l = Arrays.asList(origen);
		total.addAll(l);

		for (int j = 0; j < numFilasSelec; j++) {
			if (columnasSeleccionadas[j] instanceof Column) {
				Attribute att = new Attribute();
				att.setAccessType(new String(columnasSeleccionadas[j]
						.getAttribute().getAccessType()));
				att.setColumn(columnasSeleccionadas[j].getAttribute()
						.getColumn());
				att.setEditable(columnasSeleccionadas[j].getAttribute()
						.isEditable());
				att.setIdLayer(columnasSeleccionadas[j].getAttribute()
						.getIdLayer());
				att.setPosition(columnasSeleccionadas[j].getAttribute()
						.getPosition());
				att.setSystemID(0);
				att.setIdAlias(0);
				Hashtable ht = new Hashtable();
				ht.put(locale, columnasSeleccionadas[j].getName());
				att.setHtTraducciones(ht);

				compFila = new TableRow(columnasSeleccionadas[j], att, "", true);

				if (!l.contains(compFila))
					total.add(compFila);
			}
		}

		TableRow[] destino = (TableRow[]) total.toArray(new TableRow[total
				.size()]);

		return destino;

	}

	/**
	 * Elimina un atributo de la lista de atributos
	 * 
	 * @param origen
	 *            Lista de atributos original
	 * @return Lista de atributos resultante
	 */
	public TableRow[] eliminarAtributos(TableRow[] origen) {
		int numElementos = origen.length;

		entrada = tablemodel.getValueAt(selectedTableRow);

		TableRow[] total = new TableRow[numElementos - 1];
		int indice = 0;

		if (entrada != null) {
			hsAtributosBorrar.add(entrada.getAtributo());

			for (int i = 0; i < numElementos; i++) {
				if (origen[i].getColumna() != entrada.getColumna()) {

					total[indice] = origen[i];
					indice++;
				}
			}
		} else {
			for (int i = 0; i < numElementos; i++) {
				System.out.println(origen[i]);
				if (origen[i] != null) {
					total[indice] = origen[i];
					indice++;
				}
			}
		}

		return total;

	}

	/**
	 * Acción realizada al pulsar el botón Test
	 * 
	 * @param e
	 */
	private void btnProbar_actionPerformed(ActionEvent e) {
		// LayerOperations.directConnect();

		int idEntidad = aplicacion.getIdEntidad();
		String titulo = I18N.get("GestorCapas", "layers.test.resultado") + " "
				+ idEntidad;

		JDialogTestQuery jDialogTest = new JDialogTestQuery(titulo);
		jDialogTest.setResizable(false);
		jDialogTest.setModal(true);
		jDialogTest.setLocationRelativeTo(this);

		LayerOperations operaciones = new LayerOperations();

		StringBuffer resultado = new StringBuffer();
		try {
			// Antes de pasarle la consulta SQL a la base de datos, se comprueba
			// que no
			// intente filtrar código malicioso. Se impide la realización de
			// sentencias que
			// no sean de selección
			String sqlQuery = txtAreaSQL.getText().toUpperCase();

			// TODO Utilizar expresiones regulares, queda más elegante
			if (!(sqlQuery.indexOf("CREATE") < 0
					&& sqlQuery.indexOf("UPDATE") < 0
					&& sqlQuery.indexOf("INSERT") < 0 && sqlQuery
					.indexOf("DELETE") < 0)) {
				lanzarMensaje(I18N.get("GestorCapas", "layers.test.error"),
						I18N.get("GestorCapas", "layers.test.filtro"),
						JOptionPane.ERROR_MESSAGE);
				return;
			} else
				resultado = operaciones.obtenerResultadoTest(
						txtAreaSQL.getText(), idEntidad);
		} catch (Exception e1) {
			resultado.append(I18N.get("GestorCapas", "layers.test.error"))
					.append("\n\n").append(StringUtil.stackTrace(e1));
		}

		jDialogTest.setResultado(resultado.toString());
		jDialogTest.show();

	}

	/**
	 * Acción realizada al pulsar el botón Grabar
	 * 
	 * @param e
	 */
	private void btnGrabar_actionPerformed(ActionEvent e) {

		setFireEvents(false);
		LayerOperations operaciones = new LayerOperations();

		// Para grabar una capa nueva, hay que asegurar que no existe otra con
		// igual id
		try {
			if (capaNueva && operaciones.existeCapaId(txtIdCapa.getText())) {
				new DataException(I18N.get("GestorCapas",
						"layers.grabar.existeId"));
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// Antes de grabar, comprueba que la select de postgres funcione
		try {
			saveQuery(tipoBD, tipoQuery);
			if (htQueries.get(new Integer(POSTGRE)) != null) {
				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
						aplicacion.getMainFrame(), null);

				progressDialog.setTitle(I18N.get("GestorCapas",
						"layers.progreso.grabacion.titulo"));
				progressDialog.addComponentListener(new ComponentAdapter() {
					public void componentShown(ComponentEvent e) {

						new Thread(new Runnable() {
							public void run() {
								try {
									StringBuffer resTest = new StringBuffer();
									LayerOperations operaciones = new LayerOperations();
									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.grabacion.validar.sql"));
									// resTest =
									// operaciones.obtenerResultadoTest(((Query)htQueries.get(new
									// Integer(POSTGRE))).getSelectQuery(),
									// AppContext.getIdEntidad());

									if (resTest == null
											|| (((Query) htQueries
													.get(new Integer(POSTGRE)))
													.getSelectQuery()).trim()
													.equals("")) {
										new DataException(I18N.get(
												"GestorCapas",
												"layers.grabar.errorSQL"));
										return;
									}
								} catch (Exception de) {
									lanzarMensaje(I18N.get("GestorCapas",
											"general.advertencia"), de
											.getMessage(),
											JOptionPane.WARNING_MESSAGE);
								} finally {
									progressDialog.setVisible(false);
								}
							}
						}).start();
					}
				});
				GUIUtil.centreOnWindow(progressDialog);
				progressDialog.setVisible(true);

			}
		} catch (Exception e1) {
			new DataException(I18N.get("GestorCapas", "layers.grabar.errorSQL"));
			return;
		}

		if ((((Query) htQueries.get(new Integer(POSTGRE))).getSelectQuery())
				.lastIndexOf("WHERE") < 0
				&& (((Query) htQueries.get(new Integer(POSTGRE)))
						.getSelectQuery()).lastIndexOf("where") < 0) {
			new DataException(I18N.get("GestorCapas",
					"layers.grabar.errorWhere"));
			return;
		}

		// Cuando creamos 1 capa cogemos las columnas de una o unas tablas, a la
		// hora de insertar 1 nueva entidad en dicha capa
		// se puede dar el caso de que fallase, porque la tabla o tablas tienen
		// X valores NOT NULL. A la hora de crear la capa
		// Se tienen que coger todos los valores NOT NULL de la tabla o tablas
		// que forman la capa, por lo que comprobamos aquí si
		// se han cogido todos los valores NOT NULL de la tabla o tablas:

		TableRow[] atributos = ((TableAttributesModel) tblAtributos.getModel())
				.getData();

		TablesDBOperations tablesOperaciones = new TablesDBOperations();
		List listaColumnas = new ArrayList();
		List listaTablas = new ArrayList();
		boolean encontrado = true;
		boolean encontrado2 = false;
		int i = 0;
		Column columna = atributos[i].getColumna();
		String nombreTabla = columna.getTable().getName();
		listaTablas.add(nombreTabla);
		try {
			listaColumnas = tablesOperaciones.obtenerColumnasNotNullTabla(
					nombreTabla, listaColumnas);
		} catch (DataException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int numColumnas = atributos.length;
		try {
			while (i < numColumnas) {

				if (!listaTablas.contains((nombreTabla))) {
					if (listaColumnas == null)
						listaColumnas = new ArrayList();
					listaColumnas = tablesOperaciones
							.obtenerColumnasNotNullTabla(atributos[i]
									.getColumna().getTable().getName(),
									listaColumnas);
					listaTablas.add(atributos[i].getColumna().getTable()
							.getName());
				}
				i++;
				if (i < (numColumnas)) {
					nombreTabla = atributos[i].getColumna().getTable()
							.getName();
				}
			}
		} catch (DataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (listaColumnas != null) {
			Iterator iter = listaColumnas.iterator();

			while ((encontrado) && (iter.hasNext())) {
				TableRow tableRow = (TableRow) iter.next();
				i = 0;
				columna = atributos[i].getColumna();
				while ((!encontrado2) && (i < numColumnas)) {
					if ((tableRow.getColumna().getName().equals(columna
							.getName()))
							&& (tableRow.getColumna().getTable().getName()
									.equals(columna.getTable().getName()))) {
						encontrado2 = true;
						// System.out.println("Encontrado: "+columna.getName()+" Tabla: "+columna.getTable().getName());
						columna = atributos[i].getColumna();
					} else {
						i++;
						if ((i < numColumnas)) {
							columna = atributos[i].getColumna();
						}
					}
				}
				if (encontrado2) {
					encontrado2 = false;
				} else {
					encontrado = false;
				}

			}
			if ((((Query) htQueries.get(new Integer(POSTGRE))).getInsertQuery() != null)
					&& (!((Query) htQueries.get(new Integer(POSTGRE)))
							.getInsertQuery().equals(""))) {
				if (encontrado) {

					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							aplicacion.getMainFrame(), null);

					progressDialog.setTitle(I18N.get("GestorCapas",
							"layers.progreso.grabacion.titulo"));
					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {

							// Wait for the dialog to appear before starting the
							// task. Otherwise
							// the task might possibly finish before the dialog
							// appeared and the
							// dialog would never close. [Jon Aquino]
							new Thread(new Runnable() {
								public void run() {
									try {
										progressDialog.report(I18N
												.get("GestorCapas",
														"layers.progreso.grabacion.validar.nombre"));

										// comprueba que el nombre esté formado
										// solo por caracteres alfanuméricos y
										// subrayados
										if (!validaNombreCapa(txtIdCapa
												.getText()))
											throw new DataException(
													I18N.get("GestorCapas",
															"layers.error.nombre.novalido"));

										// comprueba que se haya introducido
										// algún atributo en la capa
										if (((TableAttributesModel) tblAtributos
												.getModel()).getData().length == 0)
											throw new DataException(I18N.get(
													"GestorCapas",
													"layers.error.noatributos"));

										// saveQuery(tipoBD, tipoQuery);

										boolean toSave = false;
										boolean htIguales = false;

										Iterator itOri = htQueriesOriginal
												.keySet().iterator();
										while (itOri.hasNext()) {
											Object key = itOri.next();
											Query qOri = (Query) htQueriesOriginal
													.get(key);
											Query qModif = (Query) htQueries
													.get(key);
											if (qOri.equals(qModif))
												htIguales = true;
											else
												break;
										}

										if (htIguales)
											isQueriesModificadas = false;
										else
											isQueriesModificadas = true;

										if (!isQueriesModificadas || capaNueva) {
											toSave = true;
										} else {
											int n = JOptionPane
													.showOptionDialog(
															(Component) aplicacion
																	.getMainFrame(),
															new StringBuffer(
																	I18N.get(
																			"GestorCapas",
																			"layers.grabar.mensaje1"))
																	.append("\n")
																	.append(I18N
																			.get("GestorCapas",
																					"layers.grabar.mensaje2"))
																	.append("\n")
																	.append(I18N
																			.get("GestorCapas",
																					"general.continuar")),
															I18N.get(
																	"GestorCapas",
																	"general.advertencia"),
															JOptionPane.YES_NO_OPTION,
															JOptionPane.QUESTION_MESSAGE,
															null, options,
															options[1]);

											if (n == JOptionPane.YES_OPTION) {
												toSave = true;
											}
										}

										GeopistaPermission paso = new GeopistaPermission(
												GeopistaPermission.MODIFICAR_CAPAS_GLOBALES);
										boolean grabarGlobales = aplicacion
												.checkPermission(paso,
														"Gestor Capas");
										paso = new GeopistaPermission(
												GeopistaPermission.MODIFICAR_CAPAS_LOCALES);
										boolean grabarLocales = aplicacion
												.checkPermission(paso,
														"Gestor Capas");
										if (toSave) {
											LayerOperations operaciones = new LayerOperations();

											// - Guardar en tabla dictionary los
											// nombres de la capa para los
											// distintos idiomas
											// - Guardar capa en tabla layers
											// - Guardar relacion con
											// layerfamily en tabla
											// layerfamiles_layers_relations
											// - Guardar nombres de atributos en
											// tabla dictionary
											// - Guardar atributos en tabla
											// Attributes
											// - Guardar consultas SQL en tabla
											// queries
											// - Crear una nueva secuencia SQL
											// para las inserciones secuenciales
											// de features en la nueva capa
											if (capaNueva) {
												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.validar.obligatorios"));

												if (!txtNombreCapa.getText()
														.equals(""))
													htNombres.put(locale,
															txtNombreCapa
																	.getText());
												// Comprueba que estén
												// introducidos la layerfamily,
												// la descripcion de la capa, un
												// nombre y el ACL
												if (cmbLayerFamily
														.getSelectedIndex() <= 0
														|| htNombres.isEmpty()
														|| txtIdCapa.getText()
																.trim()
																.equals("")
														|| (!cmbAcl.isEnabled() && !txtNuevoAcl
																.isEnabled())
														|| (((cmbAcl
																.isEnabled()) && (cmbAcl
																.getSelectedIndex() == 0)) && ((txtNuevoAcl
																.getText()
																.trim()
																.equals("")) && (txtNuevoAcl
																.isEnabled())))) {
													lanzarMensaje(
															I18N.get(
																	"GestorCapas",
																	"general.mensaje.error"),
															I18N.get(
																	"GestorCapas",
																	"layers.error.camposobligatorios"),
															JOptionPane.ERROR_MESSAGE);
													return;
													// throw new
													// DataException(I18N.get("GestorCapas","layers.error.camposobligatorios"));
												}

												if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
													if ((txtNuevoAcl != null)
															&& (!txtNuevoAcl
																	.equals(""))) {
														Acl acl = operaciones
																.getAcl(txtNuevoAcl
																		.getText());
														if (acl != null) {
															lanzarMensaje(
																	I18N.get(
																			"GestorCapas",
																			"general.mensaje.error"),
																	I18N.get(
																			"GestorCapas",
																			"layers.error.nuevoAcl"),
																	JOptionPane.ERROR_MESSAGE);
															return;
														}
													}
												}

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.actualizar.nombre"));

												Layer nuevaCapa = new Layer();
												int idDictionary = operaciones
														.actualizarDictionary(
																htNombres, 0);
												if (idDictionary > 0)
													nuevaCapa.setName(String
															.valueOf(idDictionary));

												nuevaCapa
														.setDescription(txtIdCapa
																.getText());
												LayerTable lt = new LayerTable(
														0, nuevaCapa);
												if (getSelectedRadioButton(btnGroupAcl) == rdbAcl) {
													lt.setAcl((Acl) cmbAcl
															.getSelectedItem());
												} else {
													if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
														if ((txtNuevoAcl != null)
																&& (!txtNuevoAcl
																		.equals(""))) {
															operaciones
																	.insertarAcl(txtNuevoAcl
																			.getText());
															Acl acl = operaciones
																	.getAcl(txtNuevoAcl
																			.getText());
															operaciones
																	.insertarPermisosAcl(acl
																			.getId());
															lt.setAcl(acl);
														}
													}
												}
												nuevaCapa
														.setVersionable(chbVersionable
																.isSelected());
												// Guardo si la capa es dinámica
												nuevaCapa
														.setDinamica(chbDinamica
																.isSelected());
												lt.setLayer(nuevaCapa);
												if (((String) cmbAlcance
														.getSelectedItem())
														.equals("GLOBAL")) {
													lt.setIdEntidadLayer("0");
													if (!grabarGlobales) {
														throw new PermissionException(
																I18N.get(
																		"GestorCapas",
																		"PermissionException."
																				+ GeopistaPermission.MODIFICAR_CAPAS_GLOBALES));
													}
												} else {
													lt.setIdEntidadLayer(((ISesion) Identificadores
															.get(AppContext.SESION_KEY))
															.getIdEntidad());
													if (!grabarLocales) {
														throw new PermissionException(
																I18N.get(
																		"GestorCapas",
																		"PermissionException."
																				+ GeopistaPermission.MODIFICAR_CAPAS_LOCALES));
													}
												}
												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.crear.capa"));
												idLayer = operaciones
														.actualizarLayer(lt,
																null);

												if (idLayer == 0) {
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.actualizacion.capa"));
												}

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.relacionar.layerfamily"));
												// - Guardar relacion con
												// layerfamily en tabla
												// layerfamiles_layers_relations
												int posicion = operaciones
														.asociarLayerFamilyLayer(
																idLayerFamily,
																idLayer);

												if (posicion < 0) {
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.asociacion.family"));
												}

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.atributos"));
												TableRow[] atributos = ((TableAttributesModel) tblAtributos
														.getModel()).getData();
												String tablaSecuencia = new String();

												for (int i = 0; i < atributos.length; i++) {
													Attribute att = (Attribute) atributos[i]
															.getAtributo();
													att.setEditable(((Boolean) atributos[i]
															.getEditable())
															.booleanValue());
													att.setIdLayer(idLayer);
													att.setColumn((Column) atributos[i]
															.getColumna());
													att.setPosition(i + 1);

													if (att.getColumn()
															.getIdColumn() != 0
															&& !att.getColumn()
																	.getTable()
																	.getName()
																	.equalsIgnoreCase(
																			"SYSTEM")) {
														idDictionary = operaciones
																.actualizarDictionary(
																		att.getHtTraducciones(),
																		att.getIdAlias());
														if (idDictionary > 0)
															att.setIdAlias(idDictionary);

														if (operaciones
																.actualizarAtributo(att) < 0) {
															throw new DataException(
																	I18N.get(
																			"GestorCapas",
																			"layers.error.actualizacion.atributo")
																			+ " "
																			+ att.getName());
														}

														// Para averiguar el
														// nombre de la tabla en
														// la que está la
														// geometria (esta tabla
														// se utiliza
														// para crear el nombre
														// de la secuencia)
														if (att.getColumn()
																.getName()
																.equals("GEOMETRY"))
															tablaSecuencia = att
																	.getColumn()
																	.getTable()
																	.getName();
													}
												}

												hsAtributosBorrar.clear();

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.consultas"));
												if (!htQueries.isEmpty()
														&& operaciones
																.actualizarConsultas(
																		idLayer,
																		htQueries) < 0) {
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.actualizacion.consultas"));
												}

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.crear.secuencia"));
												// - Crear una nueva secuencia
												// SQL para las inserciones
												// secuenciales de features en
												// la nueva capa
												if (tablaSecuencia != null
														&& !tablaSecuencia
																.trim().equals(
																		""))
													try {
														operaciones
																.crearSecuencia(tablaSecuencia);
													} catch (DataException de) {
														lanzarMensaje(
																I18N.get(
																		"GestorCapas",
																		"general.advertencia"),
																I18N.get(
																		"GestorCapas",
																		"layers.error.creacion.secuencia.mensaje1")
																		+ " SEQ_"
																		+ tablaSecuencia
																		+ "\n"
																		+ I18N.get(
																				"GestorCapas",
																				"layers.error.creacion.secuencia.mensaje2")
																		+ "\n"
																		+ I18N.get(
																				"GestorCapas",
																				"layers.error.creacion.secuencia.mensaje3"),
																JOptionPane.WARNING_MESSAGE);
													}

												// Cuando acaba, carga la capa
												// como modificada
												capaNueva = false;
												// lt = new LayerTable(idLayer,
												// nuevaCapa);
												lt.setHtNombre(htNombres);

												ButtonModel model = rdbModoModificacion
														.getModel();
												btnGroup.setSelected(model,
														true);
												rdbModoModificacion.doClick();

												cmbLayer.setSelectedItem(lt);
											}

											// - Actualizar tabla dictionary con
											// los nombres de la capa para los
											// distintos idiomas
											// - Actualizar nombres de atributos
											// en tabla dictionary
											// - Guardar nuevos nombres de
											// atributos en tabla dictionary
											// - Actualizar atributo en tabla
											// Attributes
											// - Guardar nuevos atributos en
											// tabla attributes
											// - Eliminar de attributes y
											// dictionary los datos de los
											// atributos que se hayan eliminado
											// de la lista
											// - Actualizar layer
											// - Actualizar consultas SQL en
											// tabla queries
											// - Actualizar la secuencia SQL
											// para las inserciones secuenciales
											// de features en la capa
											// (solo si se ha modificado el
											// nombre)

											else {
												if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
													if ((txtNuevoAcl != null)
															&& (!txtNuevoAcl
																	.equals(""))) {
														Acl acl = operaciones
																.getAcl(txtNuevoAcl
																		.getText());
														if (acl != null) {
															lanzarMensaje(
																	I18N.get(
																			"GestorCapas",
																			"general.mensaje.error"),
																	I18N.get(
																			"GestorCapas",
																			"layers.error.nuevoAcl"),
																	JOptionPane.ERROR_MESSAGE);
															return;
														}
													}
												}

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.actualizar.diccionario"));
												int idDictionary = operaciones
														.actualizarDictionary(
																htNombres,
																Integer.parseInt(capaSeleccionada
																		.getName()));
												if (idDictionary > 0)
													capaSeleccionada.setName(String
															.valueOf(idDictionary));

												TableRow[] atributos = ((TableAttributesModel) tblAtributos
														.getModel()).getData();
												String tablaSecuencia = new String();

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.actualizar.atributos"));
												for (int i = 0; i < atributos.length; i++) {
													if ((Column) atributos[i]
															.getColumna() != null
															&& ((Column) atributos[i]
																	.getColumna())
																	.getTable() != null
															&& !((Column) atributos[i]
																	.getColumna())
																	.getTable()
																	.getName()
																	.equalsIgnoreCase(
																			"SYSTEM")) {
														Attribute att = (Attribute) atributos[i]
																.getAtributo();

														att.setEditable(((Boolean) atributos[i]
																.getEditable())
																.booleanValue());
														att.setIdLayer(idLayer);
														att.setColumn((Column) atributos[i]
																.getColumna());
														att.setPosition(i + 1);
														idDictionary = operaciones
																.actualizarDictionary(
																		att.getHtTraducciones(),
																		att.getIdAlias());
														if (idDictionary > 0)
															att.setIdAlias(idDictionary);

														if (operaciones
																.actualizarAtributo(att) < 0) {
															throw new DataException(
																	I18N.get(
																			"GestorCapas",
																			"layers.error.actualizacion.atributo")
																			+ " "
																			+ att.getName());
														}

														// Para averiguar el
														// nombre de la tabla en
														// la que está la
														// geometra (esta tabla
														// se utiliza
														// para crear el nombre
														// de la secuencia)
														if (att.getColumn()
																.getName()
																.equals("GEOMETRY")
																|| att.getColumn()
																		.getName()
																		.equals("\"GEOMETRY\""))
															tablaSecuencia = att
																	.getColumn()
																	.getTable()
																	.getName();
													}
												}
												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.actualizar.capa"));
												String oldName = capaSeleccionada
														.getDescription();
												String newName = txtIdCapa
														.getText();
												if (!oldName.equals(newName)) {
													capaSeleccionada
															.setDescription(newName);
												} else {
													oldName = null;
												}
												LayerTable lt = new LayerTable(
														idLayer,
														capaSeleccionada);
												Acl acl = null;
												if (getSelectedRadioButton(btnGroupAcl) == rdbAcl) {
													acl = (Acl) cmbAcl
															.getSelectedItem();
													lt.setAcl(acl);
												} else {
													if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
														if ((txtNuevoAcl != null)
																&& (!txtNuevoAcl
																		.equals(""))) {
															operaciones
																	.insertarAcl(txtNuevoAcl
																			.getText());
															acl = operaciones
																	.getAcl(txtNuevoAcl
																			.getText());
															operaciones
																	.insertarPermisosAcl(acl
																			.getId());
															lt.setAcl(acl);
														}
													}
												}
												if (((String) cmbAlcance
														.getSelectedItem())
														.equals("GLOBAL")) {
													lt.setIdEntidadLayer("0");
													if (!grabarGlobales) {
														throw new PermissionException(
																I18N.get(
																		"GestorCapas",
																		"PermissionException."
																				+ GeopistaPermission.MODIFICAR_CAPAS_GLOBALES));
													}
												} else {
													lt.setIdEntidadLayer(((ISesion) Identificadores
															.get(AppContext.SESION_KEY))
															.getIdEntidad());
													if (!grabarLocales) {
														throw new PermissionException(
																I18N.get(
																		"GestorCapas",
																		"PermissionException."
																				+ GeopistaPermission.MODIFICAR_CAPAS_LOCALES));
													}
												}

												((LayerTable) getCmbLayer().getSelectedItem())
														.setAcl(acl);
												lt.getLayer().setDinamica(
														chbDinamica
																.isSelected());

												// Si indico que la tabla es
												// versionable, tengo que crear
												// la versión en la tabla
												if (chbVersionable.isSelected()
														&& !lt.getLayer()
																.isVersionable()) {
													crearVersion(operaciones,
															lt);
													lt.getLayer()
															.setVersionable(
																	chbVersionable
																			.isSelected());
												}
												// Si deselecciono la capacidad
												// versionable, tengo que
												// deshacer la versión
												if (!chbVersionable
														.isSelected()
														&& lt.getLayer()
																.isVersionable()) {
													deshacerVersion(
															operaciones, lt);
													lt.getLayer()
															.setVersionable(
																	chbVersionable
																			.isSelected());
												}

												// Si la capa es dinámica, hago
												// la llamada para publicar la
												// capa
												if (chbDinamica.isSelected()) {
													try {
														String urlMapServer = publicarCapa(
																operaciones,
																(LayerTable) cmbLayer
																		.getSelectedItem());
														if (urlMapServer
																.equals("")) {
															lt.getLayer()
																	.setUrl(null);
															throw new DataException(
																	I18N.get(
																			"GestorCapas",
																			"layers.error.publicar.capa"));
														} else
															lt.getLayer()
																	.setUrl(urlMapServer);
														lt.getLayer()
																.setDinamica(
																		true);
													} catch (DataException e) {
														e.printStackTrace();
													} catch (Exception e) {
														e.printStackTrace();
														throw new DataException(
																I18N.get(
																		"GestorCapas",
																		"layers.error.publicar.capa"));
													}
												} else {
													lt.getLayer().setUrl(null);
													lt.getLayer().setDinamica(
															false);

												}

												idLayer = operaciones
														.actualizarLayer(lt,
																oldName);

												if (idLayer == 0) {
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.actualizacion.capa"));
												}

												if (!hsAtributosBorrar
														.isEmpty()
														&& operaciones
																.eliminarAtributos(hsAtributosBorrar) < 0) {
													hsAtributosBorrar.clear();
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.eliminacion.atributos"));
												} else {
													hsAtributosBorrar.clear();
												}
												actualizarTablasyAtributos(operaciones);

												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.actualizar.consultas"));
												if (operaciones
														.actualizarConsultas(
																idLayer,
																htQueries) < 0) {
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.actualizacion.consultas"));
												}

												// Actualizar secuencia
												progressDialog.report(I18N
														.get("GestorCapas",
																"layers.progreso.grabacion.actualizar.secuencia"));

												if (tablaSecuencia != null
														&& !tablaSecuencia
																.trim().equals(
																		"")
														&& attGeometryName != null)
													try {
														operaciones
																.actualizarSecuencia(
																		tablaSecuencia,
																		attGeometryName);
													} catch (SQLException sqle) {
														lanzarMensaje(
																I18N.get(
																		"GestorCapas",
																		"general.advertencia"),
																I18N.get(
																		"GestorCapas",
																		"layers.error.creacion.secuencia.mensaje1")
																		+ " SEQ_"
																		+ tablaSecuencia
																		+ "\n"
																		+ I18N.get(
																				"GestorCapas",
																				"layers.error.creacion.secuencia.mensaje2")
																		+ "\n"
																		+ I18N.get(
																				"GestorCapas",
																				"layers.error.creacion.secuencia.mensaje3"),
																JOptionPane.WARNING_MESSAGE);
													}

												else if (tablaSecuencia != null)
													attGeometryName = new String(
															tablaSecuencia);

												// lt = new LayerTable(idLayer,
												// capaSeleccionada);
												lt.setHtNombre(htNombres);
												cmbLayer.setSelectedItem(lt);

											}

											isQueriesModificadas = false;

											JOptionPane optionPane = new JOptionPane(
													I18N.get("GestorCapas",
															"general.mensaje.fin.grabacion"),
													JOptionPane.INFORMATION_MESSAGE);
											JDialog dialog = optionPane.createDialog(
													aplicacion.getMainFrame(),
													"");
											dialog.show();
											repaint();

											Identificadores.put(
													"CapasModificadas", true);
										} else {
											JOptionPane optionPane = new JOptionPane(
													I18N.get("GestorCapas",
															"general.mensaje.no.grabacion"),
													JOptionPane.INFORMATION_MESSAGE);
											JDialog dialog = optionPane.createDialog(
													aplicacion.getMainFrame(),
													"");
											dialog.show();
										}
									} catch (DataException de) {
										de.printStackTrace();
									} catch (PermissionException de) {
										lanzarMensaje(I18N.get("GestorCapas",
												"general.advertencia"), de
												.getMessage(),
												JOptionPane.WARNING_MESSAGE);
									} finally {
										progressDialog.setVisible(false);
									}
								}
							}).start();
						}
					});
					GUIUtil.centreOnWindow(progressDialog);
					progressDialog.setVisible(true);
				} else {
					iter = listaColumnas.iterator();
					StringBuffer mensaje = new StringBuffer();
					mensaje.append(I18N.get("GestorCapas","general.mensaje.no.grabacion.faltan.columnas")).append("\n");
					while (iter.hasNext()) {
						TableRow tableRow = (TableRow) iter.next();
						mensaje.append(
								tableRow.getColumna().getTable().getName())
								.append(".")
								.append(tableRow.getColumna().getName());
						if (iter.hasNext()) {
							mensaje.append(", ");
						}
					}
					// System.out.println("Atributos: "+atributos.toString());
					JOptionPane optionPane = new JOptionPane(
							mensaje.toString(), JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog = optionPane.createDialog(
							aplicacion.getMainFrame(), "");
					dialog.show();
				}
			} else {

				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
						aplicacion.getMainFrame(), null);

				progressDialog.setTitle(I18N.get("GestorCapas",
						"layers.progreso.grabacion.titulo"));
				progressDialog.addComponentListener(new ComponentAdapter() {
					public void componentShown(ComponentEvent e) {

						// Wait for the dialog to appear before starting the
						// task. Otherwise
						// the task might possibly finish before the dialog
						// appeared and the
						// dialog would never close. [Jon Aquino]
						new Thread(new Runnable() {
							public void run() {
								try {
									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.grabacion.validar.nombre"));

									// comprueba que el nombre esté formado solo
									// por caracteres alfanuméricos y subrayados
									if (!validaNombreCapa(txtIdCapa.getText()))
										throw new DataException(I18N.get(
												"GestorCapas",
												"layers.error.nombre.novalido"));

									// comprueba que se haya introducido algún
									// atributo en la capa
									if (((TableAttributesModel) tblAtributos
											.getModel()).getData().length == 0)
										throw new DataException(I18N.get(
												"GestorCapas",
												"layers.error.noatributos"));

									// saveQuery(tipoBD, tipoQuery);

									boolean toSave = false;
									boolean htIguales = false;

									Iterator itOri = htQueriesOriginal.keySet()
											.iterator();
									while (itOri.hasNext()) {
										Object key = itOri.next();
										Query qOri = (Query) htQueriesOriginal
												.get(key);
										Query qModif = (Query) htQueries
												.get(key);
										if (qOri.equals(qModif))
											htIguales = true;
										else
											break;
									}

									if (htIguales)
										isQueriesModificadas = false;
									else
										isQueriesModificadas = true;

									if (!isQueriesModificadas || capaNueva) {
										toSave = true;
									} else {
										int n = JOptionPane.showOptionDialog(
												(Component) aplicacion
														.getMainFrame(),
												new StringBuffer(
														I18N.get("GestorCapas",
																"layers.grabar.mensaje1"))
														.append("\n")
														.append(I18N
																.get("GestorCapas",
																		"layers.grabar.mensaje2"))
														.append("\n")
														.append(I18N
																.get("GestorCapas",
																		"general.continuar")),
												I18N.get("GestorCapas",
														"general.advertencia"),
												JOptionPane.YES_NO_OPTION,
												JOptionPane.QUESTION_MESSAGE,
												null, options, options[1]);

										if (n == JOptionPane.YES_OPTION) {
											toSave = true;
										}
									}

									GeopistaPermission paso = new GeopistaPermission(
											GeopistaPermission.MODIFICAR_CAPAS_GLOBALES);
									boolean grabarGlobales = aplicacion
											.checkPermission(paso,
													"Gestor Capas");
									paso = new GeopistaPermission(
											GeopistaPermission.MODIFICAR_CAPAS_LOCALES);
									boolean grabarLocales = aplicacion
											.checkPermission(paso,
													"Gestor Capas");
									if (toSave) {
										LayerOperations operaciones = new LayerOperations();

										// - Guardar en tabla dictionary los
										// nombres de la capa para los distintos
										// idiomas
										// - Guardar capa en tabla layers
										// - Guardar relacion con layerfamily en
										// tabla layerfamiles_layers_relations
										// - Guardar nombres de atributos en
										// tabla dictionary
										// - Guardar atributos en tabla
										// Attributes
										// - Guardar consultas SQL en tabla
										// queries
										// - Crear una nueva secuencia SQL para
										// las inserciones secuenciales de
										// features en la nueva capa
										if (capaNueva) {
											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.validar.obligatorios"));

											if (!txtNombreCapa.getText()
													.equals(""))
												htNombres
														.put(locale,
																txtNombreCapa
																		.getText());
											// Comprueba que estén introducidos
											// la layerfamily, la descripcion de
											// la capa, un nombre y el ACL
											if (cmbLayerFamily
													.getSelectedIndex() <= 0
													|| htNombres.isEmpty()
													|| txtIdCapa.getText()
															.trim().equals("")
													|| (!cmbAcl.isEnabled() && !txtNuevoAcl
															.isEnabled())
													|| (((cmbAcl.isEnabled()) && (cmbAcl
															.getSelectedIndex() == 0)) && ((txtNuevoAcl
															.getText().trim()
															.equals("")) && (txtNuevoAcl
															.isEnabled())))) {
												lanzarMensaje(
														I18N.get("GestorCapas",
																"general.mensaje.error"),
														I18N.get("GestorCapas",
																"layers.error.camposobligatorios"),
														JOptionPane.ERROR_MESSAGE);
												return;
												// throw new
												// DataException(I18N.get("GestorCapas","layers.error.camposobligatorios"));
											}

											if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
												if ((txtNuevoAcl != null)
														&& (!txtNuevoAcl
																.equals(""))) {
													Acl acl = operaciones
															.getAcl(txtNuevoAcl
																	.getText());
													if (acl != null) {
														lanzarMensaje(
																I18N.get(
																		"GestorCapas",
																		"general.mensaje.error"),
																I18N.get(
																		"GestorCapas",
																		"layers.error.nuevoAcl"),
																JOptionPane.ERROR_MESSAGE);
														return;
													}
												}
											}

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.actualizar.nombre"));

											Layer nuevaCapa = new Layer();
											int idDictionary = operaciones
													.actualizarDictionary(
															htNombres, 0);
											if (idDictionary > 0)
												nuevaCapa.setName(String
														.valueOf(idDictionary));

											nuevaCapa.setDescription(txtIdCapa
													.getText());
											LayerTable lt = new LayerTable(0,
													nuevaCapa);
											if (getSelectedRadioButton(btnGroupAcl) == rdbAcl) {
												lt.setAcl((Acl) cmbAcl
														.getSelectedItem());
											} else {
												if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
													if ((txtNuevoAcl != null)
															&& (!txtNuevoAcl
																	.equals(""))) {
														operaciones
																.insertarAcl(txtNuevoAcl
																		.getText());
														Acl acl = operaciones
																.getAcl(txtNuevoAcl
																		.getText());
														operaciones
																.insertarPermisosAcl(acl
																		.getId());
														lt.setAcl(acl);
													}
												}
											}
											lt.setLayer(nuevaCapa);
											if (((String) cmbAlcance
													.getSelectedItem())
													.equals("GLOBAL")) {
												lt.setIdEntidadLayer("0");
												if (!grabarGlobales) {
													throw new PermissionException(
															I18N.get(
																	"GestorCapas",
																	"PermissionException."
																			+ GeopistaPermission.MODIFICAR_CAPAS_GLOBALES));
												}
											} else {
												lt.setIdEntidadLayer(((ISesion) Identificadores
														.get(AppContext.SESION_KEY))
														.getIdEntidad());
												if (!grabarLocales) {
													throw new PermissionException(
															I18N.get(
																	"GestorCapas",
																	"PermissionException."
																			+ GeopistaPermission.MODIFICAR_CAPAS_LOCALES));
												}
											}
											lt.getLayer().setDinamica(
													chbDinamica.isSelected());
											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.crear.capa"));
											/*
											 * idLayer =
											 * operaciones.actualizarLayer (lt,
											 * null);
											 * 
											 * if (idLayer ==0) { throw new
											 * DataException
											 * (I18N.get("GestorCapas"
											 * ,"layers.error.actualizacion.capa"
											 * )); }
											 */

											// Si indico que la tabla es
											// versionable, tengo que crear la
											// versión en la tabla
											if (chbVersionable.isSelected()
													&& !lt.getLayer()
															.isVersionable()) {
												crearVersion(operaciones, lt);
												lt.getLayer().setVersionable(
														chbVersionable
																.isSelected());
											}
											// Si deselecciono la capacidad
											// versionable, tengo que deshacer
											// la versión
											if (!chbVersionable.isSelected()
													&& lt.getLayer()
															.isVersionable()) {
												deshacerVersion(operaciones, lt);
												lt.getLayer().setVersionable(
														chbVersionable
																.isSelected());
											}

											// Si la capa es dinámica, hago la
											// llamada para publicar la capa
											if (chbDinamica.isSelected()) {
												try {
													lt.setIdLayer(idLayer);
													String urlMapServer = publicarCapa(
															operaciones, lt);
													if (urlMapServer.equals(""))
														throw new DataException(
																I18N.get(
																		"GestorCapas",
																		"layers.error.publicar.capa"));
													else {
														lt.getLayer().setUrl(
																urlMapServer);
													}
													lt.getLayer().setDinamica(
															true);
												} catch (DataException e) {
													e.printStackTrace();
												} catch (Exception e) {
													e.printStackTrace();
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.publicar.capa"));
												}
											} else {
												lt.getLayer().setUrl(null);
												lt.getLayer()
														.setDinamica(false);
											}

											idLayer = operaciones
													.actualizarLayer(lt, null);

											if (idLayer == 0) {
												throw new DataException(
														I18N.get("GestorCapas",
																"layers.error.actualizacion.capa"));
											}

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.relacionar.layerfamily"));
											// - Guardar relacion con
											// layerfamily en tabla
											// layerfamiles_layers_relations
											int posicion = operaciones
													.asociarLayerFamilyLayer(
															idLayerFamily,
															idLayer);

											if (posicion < 0) {
												throw new DataException(
														I18N.get("GestorCapas",
																"layers.error.asociacion.family"));
											}

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.atributos"));
											TableRow[] atributos = ((TableAttributesModel) tblAtributos
													.getModel()).getData();
											String tablaSecuencia = new String();

											for (int i = 0; i < atributos.length; i++) {
												Attribute att = (Attribute) atributos[i]
														.getAtributo();
												att.setEditable(((Boolean) atributos[i]
														.getEditable())
														.booleanValue());
												att.setIdLayer(idLayer);
												att.setColumn((Column) atributos[i]
														.getColumna());
												att.setPosition(i + 1);

												if (att.getColumn()
														.getIdColumn() != 0
														&& !att.getColumn()
																.getTable()
																.getName()
																.equalsIgnoreCase(
																		"SYSTEM")) {
													idDictionary = operaciones
															.actualizarDictionary(
																	att.getHtTraducciones(),
																	att.getIdAlias());
													if (idDictionary > 0)
														att.setIdAlias(idDictionary);

													if (operaciones
															.actualizarAtributo(att) < 0) {
														throw new DataException(
																I18N.get(
																		"GestorCapas",
																		"layers.error.actualizacion.atributo")
																		+ " "
																		+ att.getName());
													}

													// Para averiguar el nombre
													// de la tabla en la que
													// está la geometria (esta
													// tabla se utiliza
													// para crear el nombre de
													// la secuencia)
													if (att.getColumn()
															.getName()
															.equals("GEOMETRY"))
														tablaSecuencia = att
																.getColumn()
																.getTable()
																.getName();
												}
											}

											hsAtributosBorrar.clear();
											actualizarTablasyAtributos(operaciones);

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.consultas"));
											if (!htQueries.isEmpty()
													&& operaciones
															.actualizarConsultas(
																	idLayer,
																	htQueries) < 0) {
												throw new DataException(
														I18N.get("GestorCapas",
																"layers.error.actualizacion.consultas"));
											}

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.crear.secuencia"));
											// - Crear una nueva secuencia SQL
											// para las inserciones secuenciales
											// de features en la nueva capa
											if (tablaSecuencia != null
													&& !tablaSecuencia.trim()
															.equals(""))
												try {
													operaciones
															.crearSecuencia(tablaSecuencia);
												} catch (DataException de) {
													lanzarMensaje(
															I18N.get(
																	"GestorCapas",
																	"general.advertencia"),
															I18N.get(
																	"GestorCapas",
																	"layers.error.creacion.secuencia.mensaje1")
																	+ " SEQ_"
																	+ tablaSecuencia
																	+ "\n"
																	+ I18N.get(
																			"GestorCapas",
																			"layers.error.creacion.secuencia.mensaje2")
																	+ "\n"
																	+ I18N.get(
																			"GestorCapas",
																			"layers.error.creacion.secuencia.mensaje3"),
															JOptionPane.WARNING_MESSAGE);
												}

											// Cuando acaba, carga la capa como
											// modificada
											capaNueva = false;
											// lt = new LayerTable(idLayer,
											// nuevaCapa);
											lt.setHtNombre(htNombres);

											ButtonModel model = rdbModoModificacion
													.getModel();
											btnGroup.setSelected(model, true);
											rdbModoModificacion.doClick();

											cmbLayer.setSelectedItem(lt);
										}

										// - Actualizar tabla dictionary con los
										// nombres de la capa para los distintos
										// idiomas
										// - Actualizar nombres de atributos en
										// tabla dictionary
										// - Guardar nuevos nombres de atributos
										// en tabla dictionary
										// - Actualizar atributo en tabla
										// Attributes
										// - Guardar nuevos atributos en tabla
										// attributes
										// - Eliminar de attributes y dictionary
										// los datos de los atributos que se
										// hayan eliminado de la lista
										// - Actualizar layer
										// - Actualizar consultas SQL en tabla
										// queries
										// - Actualizar la secuencia SQL para
										// las inserciones secuenciales de
										// features en la capa
										// (solo si se ha modificado el nombre)

										else {
											if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
												if ((txtNuevoAcl != null)
														&& (!txtNuevoAcl
																.equals(""))) {
													Acl acl = operaciones
															.getAcl(txtNuevoAcl
																	.getText());
													if (acl != null) {
														lanzarMensaje(
																I18N.get(
																		"GestorCapas",
																		"general.mensaje.error"),
																I18N.get(
																		"GestorCapas",
																		"layers.error.nuevoAcl"),
																JOptionPane.ERROR_MESSAGE);
														return;
													}
												}
											}

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.actualizar.diccionario"));
											int idDictionary = operaciones.actualizarDictionary(
													htNombres,
													Integer.parseInt(capaSeleccionada
															.getName()));
											if (idDictionary > 0)
												capaSeleccionada.setName(String
														.valueOf(idDictionary));

											TableRow[] atributos = ((TableAttributesModel) tblAtributos
													.getModel()).getData();
											String tablaSecuencia = new String();

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.actualizar.atributos"));
											for (int i = 0; i < atributos.length; i++) {
												if ((Column) atributos[i]
														.getColumna() != null
														&& ((Column) atributos[i]
																.getColumna())
																.getTable() != null
														&& !((Column) atributos[i]
																.getColumna())
																.getTable()
																.getName()
																.equalsIgnoreCase(
																		"SYSTEM")) {
													Attribute att = (Attribute) atributos[i]
															.getAtributo();

													att.setEditable(((Boolean) atributos[i]
															.getEditable())
															.booleanValue());
													att.setIdLayer(idLayer);
													att.setColumn((Column) atributos[i]
															.getColumna());
													att.setPosition(i + 1);
													idDictionary = operaciones
															.actualizarDictionary(
																	att.getHtTraducciones(),
																	att.getIdAlias());
													if (idDictionary > 0)
														att.setIdAlias(idDictionary);

													if (operaciones
															.actualizarAtributo(att) < 0) {
														throw new DataException(
																I18N.get(
																		"GestorCapas",
																		"layers.error.actualizacion.atributo")
																		+ " "
																		+ att.getName());
													}

													// Para averiguar el nombre
													// de la tabla en la que
													// está la geometra (esta
													// tabla se utiliza
													// para crear el nombre de
													// la secuencia)
													if (att.getColumn()
															.getName()
															.equals("GEOMETRY")
															|| att.getColumn()
																	.getName()
																	.equals("\"GEOMETRY\""))
														tablaSecuencia = att
																.getColumn()
																.getTable()
																.getName();
												}
											}
											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.actualizar.capa"));
											String oldName = capaSeleccionada
													.getDescription();
											String newName = txtIdCapa
													.getText();
											if (!oldName.equals(newName)) {
												capaSeleccionada
														.setDescription(newName);
											} else {
												oldName = null;
											}
											LayerTable lt = new LayerTable(
													idLayer, capaSeleccionada);
											Acl acl = null;
											if (getSelectedRadioButton(btnGroupAcl) == rdbAcl) {
												acl = (Acl) cmbAcl
														.getSelectedItem();
												lt.setAcl(acl);
											} else {
												if (getSelectedRadioButton(btnGroupAcl) == rdbNewAcl) {
													if ((txtNuevoAcl != null)
															&& (!txtNuevoAcl
																	.equals(""))) {
														operaciones
																.insertarAcl(txtNuevoAcl
																		.getText());
														acl = operaciones
																.getAcl(txtNuevoAcl
																		.getText());
														operaciones
																.insertarPermisosAcl(acl
																		.getId());
														lt.setAcl(acl);
													}
												}
											}

											((LayerTable) getCmbLayer().getSelectedItem())
													.setAcl(acl);

											if (((String) cmbAlcance
													.getSelectedItem())
													.equals("GLOBAL")) {
												lt.setIdEntidadLayer("0");
												((LayerTable) getCmbLayer().getSelectedItem())
														.setIdEntidadLayer("0");
												if (!grabarGlobales) {
													throw new PermissionException(
															I18N.get(
																	"GestorCapas",
																	"PermissionException."
																			+ GeopistaPermission.MODIFICAR_CAPAS_GLOBALES));
												}
											} else {
												lt.setIdEntidadLayer(((ISesion) Identificadores
														.get(AppContext.SESION_KEY))
														.getIdEntidad());
												((LayerTable) getCmbLayer().getSelectedItem())
														.setIdEntidadLayer(((ISesion) Identificadores
																.get(AppContext.SESION_KEY))
																.getIdEntidad());
												if (!grabarLocales) {
													throw new PermissionException(
															I18N.get(
																	"GestorCapas",
																	"PermissionException."
																			+ GeopistaPermission.MODIFICAR_CAPAS_LOCALES));
												}
											}
											// Si indico que la tabla es
											// versionable, tengo que crear la
											// versión en la tabla
											if (chbVersionable.isSelected()
													&& !lt.getLayer()
															.isVersionable()) {
												crearVersion(operaciones, lt);
												lt.getLayer().setVersionable(
														chbVersionable
																.isSelected());
											}
											// Si deselecciono la capacidad
											// versionable, tengo que deshacer
											// la versión
											if (!chbVersionable.isSelected()
													&& lt.getLayer()
															.isVersionable()) {
												deshacerVersion(operaciones, lt);
												lt.getLayer().setVersionable(
														chbVersionable
																.isSelected());
											}

											// Si la capa es dinámica, hago la
											// llamada para publicar la capa
											if (chbDinamica.isSelected()) {
												try {
													lt.getLayer().setDinamica(
															true);
													lt.setIdLayer(idLayer);
													String urlMapServer = publicarCapa(
															operaciones, lt);
													if (urlMapServer.equals(""))
														throw new DataException(
																I18N.get(
																		"GestorCapas",
																		"layers.error.publicar.capa"));
													else {
														lt.getLayer().setUrl(
																urlMapServer);
													}
												} catch (DataException e) {
													e.printStackTrace();
												} catch (Exception e) {
													e.printStackTrace();
													throw new DataException(
															I18N.get(
																	"GestorCapas",
																	"layers.error.publicar.capa"));
												}
											} else {
												lt.getLayer().setUrl(null);
												lt.getLayer()
														.setDinamica(false);
											}

											idLayer = operaciones
													.actualizarLayer(lt,
															oldName);

											if (idLayer == 0) {
												throw new DataException(
														I18N.get("GestorCapas",
																"layers.error.actualizacion.capa"));
											}

											if (!hsAtributosBorrar.isEmpty()
													&& operaciones
															.eliminarAtributos(hsAtributosBorrar) < 0) {
												hsAtributosBorrar.clear();
												throw new DataException(
														I18N.get("GestorCapas",
																"layers.error.eliminacion.atributos"));
											} else {
												hsAtributosBorrar.clear();
											}
											actualizarTablasyAtributos(operaciones);

											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.actualizar.consultas"));
											if (operaciones
													.actualizarConsultas(
															idLayer, htQueries) < 0) {
												throw new DataException(
														I18N.get("GestorCapas",
																"layers.error.actualizacion.consultas"));
											}

											// Actualizar secuencia
											progressDialog.report(I18N
													.get("GestorCapas",
															"layers.progreso.grabacion.actualizar.secuencia"));

											if (tablaSecuencia != null
													&& !tablaSecuencia.trim()
															.equals("")
													&& attGeometryName != null)
												try {
													operaciones
															.actualizarSecuencia(
																	tablaSecuencia,
																	attGeometryName);
												} catch (SQLException sqle) {
													lanzarMensaje(
															I18N.get(
																	"GestorCapas",
																	"general.advertencia"),
															I18N.get(
																	"GestorCapas",
																	"layers.error.creacion.secuencia.mensaje1")
																	+ " SEQ_"
																	+ tablaSecuencia
																	+ "\n"
																	+ I18N.get(
																			"GestorCapas",
																			"layers.error.creacion.secuencia.mensaje2")
																	+ "\n"
																	+ I18N.get(
																			"GestorCapas",
																			"layers.error.creacion.secuencia.mensaje3"),
															JOptionPane.WARNING_MESSAGE);
												}

											else if (tablaSecuencia != null)
												attGeometryName = new String(
														tablaSecuencia);

											// lt = new LayerTable(idLayer,
											// capaSeleccionada);
											lt.setHtNombre(htNombres);
											cmbLayer.setSelectedItem(lt);
										}

										isQueriesModificadas = false;

										JOptionPane optionPane = new JOptionPane(
												I18N.get("GestorCapas",
														"general.mensaje.fin.grabacion"),
												JOptionPane.INFORMATION_MESSAGE);
										JDialog dialog = optionPane
												.createDialog(aplicacion
														.getMainFrame(), "");
										dialog.show();
										repaint();

										Identificadores.put("CapasModificadas",
												true);
									} else {
										JOptionPane optionPane = new JOptionPane(
												I18N.get("GestorCapas",
														"general.mensaje.no.grabacion"),
												JOptionPane.INFORMATION_MESSAGE);
										JDialog dialog = optionPane
												.createDialog(aplicacion
														.getMainFrame(), "");
										dialog.show();
									}
								} catch (DataException de) {
									de.printStackTrace();
								} catch (PermissionException de) {
									lanzarMensaje(I18N.get("GestorCapas",
											"general.advertencia"), de
											.getMessage(),
											JOptionPane.WARNING_MESSAGE);
								} finally {
									progressDialog.setVisible(false);
								}
							}
						}).start();
					}
				});
				GUIUtil.centreOnWindow(progressDialog);
				progressDialog.setVisible(true);
			}
		}
		setFireEvents(true);
	}

	private boolean validaNombreCapa(String layerName) {
		boolean isValid = false;

		// Se permiten números, letras y subrayados
		String regex = "\\w+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(layerName);

		if (matcher.matches())
			isValid = true;

		return isValid;
	}

	/**
	 * Acción realizada al pulsar el botón Eliminar capa
	 * 
	 * @param e
	 */
	private void btnEliminarLayer_actionPerformed(ActionEvent e) {
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);

		progressDialog.setTitle(I18N.get("GestorCapas",
				"layers.progreso.eliminacion.titulo"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {

				// Wait for the dialog to appear before starting the
				// task. Otherwise
				// the task might possibly finish before the dialog
				// appeared and the
				// dialog would never close. [Jon Aquino]
				new Thread(new Runnable() {
					public void run() {
						try {
							int n = JOptionPane.showOptionDialog(
									aplicacion.getMainFrame(),
									new StringBuffer(I18N.get("GestorCapas",
											"layers.eliminar.mensaje1"))
											.append("\n")
											.append(I18N.get("GestorCapas",
													"layers.eliminar.mensaje2"))
											.append("\n")
											.append(I18N.get("GestorCapas",
													"general.continuar")), I18N
											.get("GestorCapas",
													"general.advertencia"),
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[1]);

							if (n == JOptionPane.YES_OPTION) {

								progressDialog.report(I18N
										.get("GestorCapas",
												"layers.progreso.eliminacion.comprobar.mapa"));
								LayerOperations operaciones = new LayerOperations();

								// - Comprobar si la capa se utiliza en algun
								// mapa
								int mapas = JOptionPane.YES_NO_OPTION;
								Vector vcIdMapas = operaciones
										.buscarMapasWithLayer(idLayer);
								if (!vcIdMapas.isEmpty()) {
									mapas = JOptionPane.showOptionDialog(
											aplicacion.getMainFrame(),
											new StringBuffer(
													I18N.get("GestorCapas",
															"layers.avisomapa.mensaje1"))
													.append(" ")
													.append(vcIdMapas.size())
													.append(" ")
													.append(I18N
															.get("GestorCapas",
																	"layers.avisomapa.mensaje2"))
													.append("\n")
													.append(I18N.get(
															"GestorCapas",
															"general.opciones")),
											I18N.get("GestorCapas",
													"general.advertencia"),
											JOptionPane.YES_NO_OPTION,
											JOptionPane.QUESTION_MESSAGE, null,
											optionsMapa, optionsMapa[1]);
								}

								if (mapas == JOptionPane.YES_OPTION) {

									// - Eliminar de la tabla dictionary los
									// nombres de la capa
									// - Eliminar de la tabla dictionary los
									// nombres de los atributos de la capa
									// - Eliminar entradas de la tabla
									// attributes correspondientes al idLayer
									// - Eliminar entrada en la tabla queries
									// - Eliminar relaciones de la capa con las
									// familias en la tabla
									// layerfamilies_layer_relations
									// - Eliminar entrada en la tabla styles y
									// layers_styles
									// - Eliminar entrada en la tabla layers

									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.eliminacion.atributos"));
									TableRow datosTabla[] = ((TableAttributesModel) tblAtributos
											.getModel()).getData();
									HashSet hsAtributos = new HashSet();
									for (int i = 0; i < datosTabla.length; i++) {
										hsAtributos.add(datosTabla[i]
												.getAtributo());
									}

									if (!hsAtributos.isEmpty()
											&& operaciones
													.eliminarAtributos(hsAtributos) < 0) {
										throw new DataException(
												I18N.get("GestorCapas",
														"layers.error.eliminacion.atributos"));
										// lanzarError(I18N.get("GestorCapas","layers.error.eliminacion.atributos"));
										// return;
									}

									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.eliminacion.consultas"));
									operaciones.actualizarConsultas(idLayer,
											null);

									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.eliminacion.relacionfamily"));
									Vector idFamilias = operaciones
											.obtenerLayerfamiliesConLayer(idLayer);
									Iterator it = idFamilias.iterator();
									while (it.hasNext()) {
										if (!operaciones
												.eliminarRelacionLayerFamilyLayer(
														((Integer) it.next())
																.intValue(),
														idLayer)) {
											throw new DataException(
													I18N.get("GestorCapas",
															"layers.error.eliminacion.relacionfamily"));
											// lanzarError(I18N.get("GestorCapas","layers.error.eliminacion.relacionfamily"));
											// return;
										}
									}

									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.eliminacion.estilos"));
									if (!vcIdMapas.isEmpty()) {
										if (operaciones
												.eliminarLayerStyle(idLayer) < 0) {
											throw new DataException(
													I18N.get("GestorCapas",
															"layers.error.eliminacion.layers_styles"));
											// lanzarError(I18N.get("GestorCapas","layers.error.eliminacion.layers_styles"));
											// return;
										}
									}

									if (operaciones.eliminarStyle(idLayer) < 0) {

										// lanzarError(I18N.get("GestorCapas","layers.error.eliminacion.styles"));
									}

									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.eliminacion.layers"));
									if (operaciones.eliminarLayer(idLayer) < 0) {
										throw new DataException(
												I18N.get("GestorCapas",
														"layers.error.eliminacion.layers"));
										// lanzarError(I18N.get("GestorCapas","layers.error.eliminacion.layers"));
										// return;
									}

									progressDialog.report(I18N
											.get("GestorCapas",
													"layers.progreso.eliminacion.traducciones"));
									operaciones.actualizarDictionary(
											null,
											(new Integer(capaSeleccionada
													.getName())).intValue());

									JOptionPane information = new JOptionPane(
											I18N.get("GestorCapas",
													"general.mensaje.fin.eliminacion"),
											JOptionPane.INFORMATION_MESSAGE);
									JDialog dialogoInfo = information
											.createDialog(
													aplicacion.getMainFrame(),
													"");
									dialogoInfo.show();

									Identificadores.put("CapasModificadas",
											true);

									controlarAspecto(false);
								}
							}
						}

						catch (DataException de) {
							de.printStackTrace();
						} finally {
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);

		/*
		 * 
		 * try{
		 * 
		 * int n = JOptionPane.showOptionDialog((Component)this, new
		 * StringBuffer
		 * (I18N.get("GestorCapas","layers.eliminar.mensaje1")).append("\n")
		 * .append
		 * (I18N.get("GestorCapas","layers.eliminar.mensaje2")).append("\n")
		 * .append(I18N.get("GestorCapas","general.continuar")),
		 * I18N.get("GestorCapas","general.advertencia"),
		 * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
		 * options, options[1]);
		 * 
		 * if (n == JOptionPane.YES_OPTION) { LayerOperations operaciones = new
		 * LayerOperations();
		 * 
		 * 
		 * //- Comprobar si la capa se utiliza en algun mapa int mapas =
		 * JOptionPane.YES_NO_OPTION; Vector vcIdMapas =
		 * operaciones.buscarMapas(idLayer); if (!vcIdMapas.isEmpty()) {
		 * 
		 * mapas = JOptionPane.showOptionDialog((Component)this, new
		 * StringBuffer
		 * (I18N.get("GestorCapas","layers.avisomapa.mensaje1")).append(" ")
		 * .append(vcIdMapas.size()).append(" ")
		 * .append(I18N.get("GestorCapas","layers.avisomapa.mensaje2"
		 * )).append("\n") .append(I18N.get("GestorCapas","general.opciones")),
		 * I18N.get("GestorCapas","general.advertencia"),
		 * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
		 * optionsMapa, optionsMapa[1]); }
		 * 
		 * if (mapas == JOptionPane.YES_OPTION) {
		 * 
		 * //- Eliminar de la tabla dictionary los nombres de la capa //-
		 * Eliminar de la tabla dictionary los nombres de los atributos de la
		 * capa //- Eliminar entradas de la tabla attributes correspondientes al
		 * idLayer //- Eliminar entrada en la tabla queries //- Eliminar
		 * relaciones de la capa con las familias en la tabla
		 * layerfamilies_layer_relations //- Eliminar entrada en la tabla styles
		 * y layers_styles //- Eliminar entrada en la tabla layers
		 * 
		 * operaciones.actualizarDictionary(null, (new
		 * Integer(capaSeleccionada.getName())).intValue());
		 * 
		 * TableRow datosTabla [] =
		 * ((TableAttributesModel)tblAtributos.getModel()).getData(); HashSet
		 * hsAtributos = new HashSet(); for (int i=0; i< datosTabla.length; i++)
		 * { hsAtributos.add(datosTabla[i].getAtributo()); }
		 * 
		 * if (!hsAtributos.isEmpty() &&
		 * operaciones.eliminarAtributos(hsAtributos)<0) { throw new
		 * DataException
		 * (I18N.get("GestorCapas","layers.error.eliminacion.atributos"));
		 * //lanzarError
		 * (I18N.get("GestorCapas","layers.error.eliminacion.atributos"));
		 * //return; }
		 * 
		 * operaciones.actualizarConsultas (idLayer, null);
		 * 
		 * Vector idFamilias =
		 * operaciones.obtenerLayerfamiliesConLayer(idLayer); Iterator it =
		 * idFamilias.iterator(); while (it.hasNext()) { if
		 * (!operaciones.eliminarRelacionLayerFamilyLayer
		 * (((Integer)it.next()).intValue(), idLayer)) { throw new
		 * DataException(
		 * I18N.get("GestorCapas","layers.error.eliminacion.relacionfamily"));
		 * //lanzarError(I18N.get("GestorCapas",
		 * "layers.error.eliminacion.relacionfamily")); //return; } }
		 * 
		 * if (!vcIdMapas.isEmpty()) { if
		 * (operaciones.eliminarLayerStyle(idLayer)<0) { throw new
		 * DataException(
		 * I18N.get("GestorCapas","layers.error.eliminacion.layers_styles"));
		 * //lanzarError
		 * (I18N.get("GestorCapas","layers.error.eliminacion.layers_styles"));
		 * //return; } }
		 * 
		 * if (operaciones.eliminarStyle(idLayer)<0) {
		 * 
		 * lanzarError(I18N.get("GestorCapas","layers.error.eliminacion.styles"))
		 * ; }
		 * 
		 * if (operaciones.eliminarLayer(idLayer)<0) { throw new
		 * DataException(I18N
		 * .get("GestorCapas","layers.error.eliminacion.layers"));
		 * //lanzarError(
		 * I18N.get("GestorCapas","layers.error.eliminacion.layers")); //return;
		 * }
		 * 
		 * JOptionPane information= new
		 * JOptionPane(I18N.get("GestorCapas","general.mensaje.fin.eliminacion"
		 * ), JOptionPane.INFORMATION_MESSAGE); JDialog dialogoInfo =
		 * information.createDialog(this,""); dialogoInfo.show();
		 * 
		 * Identificadores.put("CapasModificadas", true);
		 * //cmbLayer.setSelectedIndex(0); controlarAspecto(false);
		 * 
		 * 
		 * } } }catch(DataException de) { de.printStackTrace(); }
		 */
	}

	/**
	 * Lanza un mensaje
	 * 
	 * @param titulo
	 *            Título de la pantalla
	 * @param mensaje
	 *            Mensaje a mostrar
	 * @param tipo
	 *            Tipo de ventana a lanzar
	 */
	private void lanzarMensaje(String titulo, String mensaje, int tipo) {
		JOptionPane optionPane = new JOptionPane(mensaje, tipo);
		JDialog dialog = optionPane.createDialog(this, "");
		dialog.setTitle(titulo);
		dialog.show();
	}

	/**
	 * Genera la cláusula FROM de la consulta SQL propuesta
	 * 
	 * @param claves
	 *            Vector de claves suministradas por el usuario
	 * @param tablas
	 *            Conjunto de tablas incluidas en las claves
	 * @return Cláusula FROM de la consulta
	 */
	private String generateFromClause(Vector claves, HashSet tablas) {
		StringBuffer fromClause = new StringBuffer();

		// Si no se han suministrado foreign key, el from consiste en el listado
		// de tablas participantes
		if (claves.isEmpty()) {
			for (Iterator it = tablas.iterator(); it.hasNext();) {
				fromClause.append("\"").append(it.next().toString())
						.append("\",");
			}

			if (fromClause.length() > 0)
				return fromClause.toString().substring(0,
						fromClause.length() - 1);
			else
				return fromClause.toString();
		}

		// ForeignCondition previo = new ForeignCondition();
		// Vector clavesOrdenadas = new Vector(claves.size());

		// Método de ordenación:
		// 1. Se ordenan alfabéticamente las condiciones (dentro de cada
		// condicion
		// y entre condiciones)
		// 2. Se recorre el vector resultante de claves y se colocan seguidas
		// las condiciones encadenadas (A.a=B.b; A.c=B.d; B.d=C.e; D.d=C.a;...)

		Comparator clavesComparator = new Comparator() {
			public int compare(Object o1, Object o2) {
				ForeignCondition fc1 = (ForeignCondition) o1;
				ForeignCondition fc2 = (ForeignCondition) o2;

				// Ordenacion dentro de cada condicion
				String condIzq1 = fc1.getCondicionIzquierda();
				String condIzq2 = fc2.getCondicionIzquierda();
				String condDcha1 = fc1.getCondicionDerecha();
				String condDcha2 = fc2.getCondicionDerecha();

				String condIzquierda = null;
				String condDerecha = null;

				if (condIzq1.compareToIgnoreCase(condDcha1) < 0) {
					condIzquierda = String.valueOf(condDcha1);
					condDerecha = String.valueOf(condIzq1);
					fc1.setCondicionIzquierda(condIzquierda);
					fc1.setCondicionDerecha(condDerecha);
				}

				if (condIzq2.compareToIgnoreCase(condDcha2) < 0) {
					condIzquierda = String.valueOf(condDcha2);
					condDerecha = String.valueOf(condIzq2);
					fc2.setCondicionIzquierda(condIzquierda);
					fc2.setCondicionDerecha(condDerecha);
				}

				// Ordenacion entre condiciones
				condIzq1 = fc1.getCondicionIzquierda();
				condIzq2 = fc2.getCondicionIzquierda();

				return condIzq1.compareToIgnoreCase(condIzq2);
			}
		};

		ForeignCondition[] fcArray = (ForeignCondition[]) claves
				.toArray(new ForeignCondition[claves.size()]);
		Arrays.sort(fcArray, clavesComparator);

		// Se convierte el array en vector para poder eliminar elementos mas
		// facilmente
		Vector fcVector = new Vector(Arrays.asList(fcArray));

		// Se crean los tablafrom con los nombres de las tablas y las
		// condiciones
		TableFrom[] tf = new TableFrom[tablas.size()];
		int i = 0;
		for (Iterator it = tablas.iterator(); it.hasNext();) {
			String nomTabla = it.next().toString();
			tf[i] = new TableFrom();
			tf[i].setNombre1(nomTabla);

			for (int j = 0; j < fcVector.size(); j++) {
				ForeignCondition fc = (ForeignCondition) fcVector.get(j);

				if (fc.getTablaIzquierda().equalsIgnoreCase(nomTabla)
						|| fc.getTablaDerecha().equalsIgnoreCase(nomTabla)) {
					if (!fc.getTablaIzquierda().equalsIgnoreCase(nomTabla)
							&& (tf[i].getNombre2() == null || tf[i]
									.getNombre2().equalsIgnoreCase(
											fc.getTablaIzquierda()))) {
						tf[i].addCondicion(fc.getCondicion());
						fcVector.remove(fc);
						j--;
						tf[i].setNombre2(fc.getTablaIzquierda());
					} else if (!fc.getTablaDerecha().equalsIgnoreCase(nomTabla)
							&& (tf[i].getNombre2() == null || tf[i]
									.getNombre2().equalsIgnoreCase(
											fc.getTablaDerecha()))) {
						tf[i].addCondicion(fc.getCondicion());
						fcVector.remove(fc);
						j--;
						tf[i].setNombre2(fc.getTablaDerecha());
					}
				}
			}
			i++;
		}

		// Habra un numero de inner join igual a tablas.size()-1.
		// En la asociacion anterior, uno de los nombre2 habrá quedado sin
		// rellenar
		// Ordeno las relaciones:
		Vector tablasOrdenadas = new Vector();
		String origen = null;
		int maxCont = 0;
		while (tablasOrdenadas.size() < tf.length && maxCont < 10) {
			// buscar el que no tiene joins
			if (origen == null) {
				for (int k = 0; k < tf.length; k++) {
					if (tf[k].getNombre2() == null) {
						origen = tf[k].getNombre1();
						tablasOrdenadas.add(tf[k]);
					}
				}
			}
			// para todos los demas, buscar el que coincide su nombre2 con el
			// nombre1 anterior
			else {
				for (int k = 0; k < tf.length; k++) {
					if (tf[k].getNombre2() != null
							&& tf[k].getNombre2().equals(origen)) {
						origen = tf[k].getNombre1();
						tablasOrdenadas.add(tf[k]);
					}
				}
			}
			maxCont++;
		}

		for (i = 0; i < tablasOrdenadas.size(); i++) {
			TableFrom tabfrom = (TableFrom) (tablasOrdenadas.get(i));
			if (i == 0) {
				fromClause.append("\"").append(tabfrom.getNombre1())
						.append("\"");
			} else {

				if (i == 1)
					fromClause.insert(0, " INNER JOIN ")
							.insert(0, "\"" + tabfrom.getNombre1() + "\"")
							.append(" ON (");
				else
					fromClause.insert(0, " INNER JOIN (")
							.insert(0, tabfrom.getNombre1()).append(") ON (");

				for (int k = 0; k < tabfrom.getCondicionesOn().size(); k++) {
					StringBuffer s = new StringBuffer("\"");
					s.append(tabfrom.getCondicionesOn().get(k).toString())
							.append("\"");
					String st = s.toString().replaceFirst("=", "\"=\"");
					st = st.replaceAll("\\.", "\"\\.\"");

					fromClause.append(st).append(" AND ");
				}
				fromClause = new StringBuffer(fromClause.substring(0,
						fromClause.length() - 5)).append(") ");
			}
		}

		return fromClause.toString();
	}

	/**
	 * Habilita y deshabilita la zona inferior de la pantalla
	 * 
	 * @param isEnabled
	 *            Verdadero si se desea habilitarla
	 */
	private void habilitarZonaInferior(boolean isEnabled) {
		getCmbBaseDatos().setEnabled(isEnabled);
		getCmbQuery().setEnabled(isEnabled);
		getBtnGenerarSQL().setEnabled(isEnabled);
		getBtnRecuperarSQL().setEnabled(isEnabled);
		getBtnProbar().setEnabled(isEnabled);
		getBtnGrabar().setEnabled(isEnabled);

		Color fg = null;

		if (isEnabled)
			fg = Color.BLACK;
		else
			fg = Color.GRAY;

		lblTipoBD.setForeground(fg);
		lblQuerySQL.setForeground(fg);
	}

	/**
	 * Devuelve el radiobutton seleccionado del buttongrop
	 * 
	 * @param group
	 *            ButtonGroup
	 * @return JRadioButton seleccionado dentro del grupo de botones
	 */
	public static JRadioButton getSelectedRadioButton(ButtonGroup group) {
		if (group != null) {
			for (Enumeration e = group.getElements(); e.hasMoreElements();) {
				JRadioButton b = (JRadioButton) e.nextElement();
				if (b.getModel() == group.getSelection()) {
					return b;
				}
			}
		}
		return null;

	}

	/**
	 * Acciones realizadas al entrar en el panel
	 */
	public void enter() {
		nosale = true;
		if ((((Boolean) Identificadores.get("TablasModificadas"))
				.booleanValue() && !((Boolean) Identificadores
				.get("LayersActualizada")).booleanValue())
				|| ((Boolean) Identificadores.get("DomainsModificados"))
						.booleanValue() || ((Boolean) Identificadores.get("CapasModificadas")).booleanValue()) {
			// Cada vez que se entra, se actualiza el arbol de tablas del
			// sistema con
			// los posibles cambios realizados
			//Identificadores.put("CapasModificadas", null);
			Identificadores.put("Tablas", null);
			Identificadores.put("Columnas", null);

			boolean bEstado = (getSelectedRadioButton(btnGroup) != null);
			if (jPanelTablas != null) {
				this.remove(jPanelTablas);
				jPanelTablas = new JPanelTables(
						TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
				jPanelTablas.setBorder(BorderFactory.createTitledBorder(I18N
						.get("GestorCapas", "layers.paneltablas.titulo")));
				jPanelTablas.setBounds(new Rectangle(16, 90, 250, 350));
				this.add(jPanelTablas);
				jPanelTablas.setEnabled(bEstado);
				treeTablas = jPanelTablas.getTree();
				treeTablas.addTreeSelectionListener(this);
			}

			// Si se modifica algo en la pantalla de tablas de base de datos,
			// habrá
			// que actualizar la información tanto en el panel de tablasdominios
			// como
			// en el de layers.
			Identificadores.put("LayersActualizada", true);
			if (((Boolean) Identificadores.get("TablasDominiosActualizada"))
					.booleanValue()) {
				//Identificadores.put("TablasModificadas", false);
				Identificadores.put("LayersActualizada", false);
				Identificadores.put("TablasDominiosActualizada", false);
			}

			if (((Boolean) Identificadores.get("DomainsModificados"))
					.booleanValue()) {
				Identificadores.put("DomainsModificados", false);
			}
		}

		if (!(this.getComponentCount() > 0)) {
			initialize();
		}

		try {
			// Iniciamos la ayuda
			String helpHS = "help/catastro/gestordecapas/GestorCapasHelp_es.hs";
			ClassLoader c1 = this.getClass().getClassLoader();
			URL hsURL = HelpSet.findHelpSet(c1, helpHS);
			HelpSet hs = new HelpSet(null, hsURL);
			HelpBroker hb = hs.createHelpBroker();
			// fin de la ayuda
			hb.enableHelpKey(this, "Pestania3Capas", hs);
		} catch (Exception excp) {
			excp.printStackTrace();
		}
	}

	/**
	 * Acciones realizadas al salir del panel
	 */
	public void exit() {
		if (nosale && getSelectedRadioButton(btnGroup) != null) {
			int n = JOptionPane
					.showOptionDialog(
							this,
							"Se perderá cualquier cambio que no haya grabado. ¿Realmente desea abandonar el panel?",
							"", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);

			// Sale de la pestaña
			LayersPanel lp = null;

			if (n == JOptionPane.YES_OPTION) {
				Container c = this.getRootPane().getParent();

				if (c != null && c instanceof GestorCapas) {
					// int indice =
					// ((GestorCapas)c).getPestanaTables().indexOfComponent(this);
					// LayersPanel panelEliminar = this;
					// panelEliminar.nosale = false;
					// lp = new LayersPanel();
					// lp.setBorder(BorderFactory.createLineBorder(Color.GRAY));

					// ((GestorCapas)c).getPestanaTables().setComponentAt(indice,
					// lp);
					// lp = (LayersPanel)
					// ((GestorCapas)c).getPestanaTables().getComponent(indice-1);
					// lp = new LayersPanel();
					// lp.setBorder(BorderFactory.createLineBorder(Color.GRAY));
					// indice =
					// ((GestorCapas)c).getPestanaTables().indexOfComponent(lp);
					// ((GestorCapas)c).getPestanaTables().

					// ((GestorCapas)c).getPestanaTables().removeTabAt(indice);
					// ((GestorCapas)c).getPestanaTables().insertTab(I18N.get("GestorCapas","general.pestana.layers"),
					// null, lp, null, indice);

					// //
					// ((GestorCapas)c).getPestanaTables().remove(panelEliminar);
					// ((GestorCapas)c).getPestanaTables().removeTabAt(indice);
					// int indice =
					// ((GestorCapas)c).getPestanaTables().getSelectedIndex();
					reset();
					// ((GestorCapas)c).getPestanaTables().setSelectedIndex(indice);

					// ((GestorCapas)c).getPestanaTables().updateUI();
					// ((GestorCapas)c).getPestanaTables().repaint();

				}

				return;
			}
			// Se queda en esta pestaña
			else {
				Container c = this.getRootPane().getParent();

				if (c != null && c instanceof GestorCapas) {
					int indice = ((GestorCapas) c).getPestanaTables()
							.indexOfComponent(this);

					c.getPropertyChangeListeners();
					nosale = false;
					((GestorCapas) c).getPestanaTables().setSelectedIndex(
							indice);

					((GestorCapas) c).getPestanaTables().updateUI();
					((GestorCapas) c).getPestanaTables().repaint();
					this.updateUI();
					this.repaint();

				}
			}
		}

	}

	/**
	 * Método para publicar una capa en el MapServer
	 * 
	 * @param operations
	 * @param lt
	 * @param srid
	 * @return
	 * @throws DataException
	 */
	private String publicarCapa(LayerOperations operations, LayerTable lt)
			throws DataException {
		String urlPublicacion = "";
		try {
			LocalgisLayer localgisLayer = operations.publishMap(lt);
			List stylesList = operations.getStylesLayer(lt.getIdLayer());
			if (stylesList.size() > 0) {
				String msgSeleccionarEstilo = I18N.get("GestorCapas",
						"seleccionar.estilo.capa");
				JDialogStyles dialogStyles = new JDialogStyles(localgisLayer,
						msgSeleccionarEstilo);
				dialogStyles.setListaEstilos(stylesList);
				dialogStyles.setResizable(false);
				dialogStyles.setModal(true);
				dialogStyles.setLocationRelativeTo(this);
				dialogStyles.setVisible(true);
			}
			if (localgisLayer.getLayerquery().equals("")) {
				String query = txtAreaSQL.getText().replaceAll("\"", "");
				Pattern patron = Pattern.compile("GEOMETRY");
				Matcher encaja = patron.matcher(query);
				query = encaja.replaceAll("\"GEOMETRY\"");
				localgisLayer.setLayerquery(query);
			}

			if (chbVersionable.isSelected()) {
				localgisLayer.setService(SERVICE_TIME);
				localgisLayer.setTime("2000-12-31/3000-12-31");
				localgisLayer.setColumnTime("time");

				List tablesList = obtenerTablasAsociadasCapa();
				Iterator itTables = tablesList.iterator();
				while (itTables.hasNext()) {
					Table table = (Table) itTables.next();
					if (!table.getName().equals("SYSTEM")) {
						localgisLayer.setLayerquery(operations
								.getQueryCamposVersion(table.getName(),
										localgisLayer.getLayerquery()));
						localgisLayer.setLayerquery(operations
								.getQueryCamposTime(table.getName(),
										localgisLayer.getLayerquery()));
						// localgisLayer.setLayerquery(operations.getJoinVersiones(table.getName(),
						// localgisLayer.getLayerquery()));
					}
				}

			} else {
				localgisLayer.setService(SERVICE_WMS);
			}
			localgisLayer.setIdEntidad(AppContext.getIdEntidad());
			// Aunque publique la capa para el 23029, se configurará para
			// todas las capas que utilizamos
			localgisLayer.setSrid("23029");
			PublishMapsClient client = new PublishMapsClient();
			PublishMapsPortType service = client.getPublishMapsHttpPort();
			urlPublicacion = service.configureLayerAndStylesFile(localgisLayer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(I18N.get("GestorCapas",
					"layers.error.publicar.capa"));
		}
		return urlPublicacion;
	}

	/**
	 * Crea una versión de la capa
	 * 
	 * @param lt
	 * @return
	 */
	private void crearVersion(LayerOperations operations, LayerTable lt)
			throws DataException {
		try {
			// Obtengo todas las tablas asociadas a la capa
			List tablesList = obtenerTablasAsociadasCapa();
			Iterator itTables = tablesList.iterator();
			while (itTables.hasNext()) {
				Table table = (Table) itTables.next();
				if (!table.getName().equals("SYSTEM")) {
					operations.crearCamposVersionado(table.getName(), true);
					// Actualizo el campo layers.validator
					if (lt.getLayer().getValidator() == null)
						lt.getLayer().setValidator(VALIDATOR_CLASS);
					else
						lt.getLayer().setValidator(
								lt.getLayer().getValidator() + ";"
										+ VALIDATOR_CLASS);

					crearColumnas(operations, table, lt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(I18N.get("GestorCapas",
					"layers.error.versionado"));
		}
	}

	/**
	 * Deshace las versiones de una capa
	 * 
	 * @param lt
	 * @return
	 */
	private void deshacerVersion(LayerOperations operations, LayerTable lt)
			throws DataException {
		try {
			// Obtengo todas las tablas asociadas a la capa
			List tablesList = obtenerTablasAsociadasCapa();
			Iterator itTables = tablesList.iterator();
			while (itTables.hasNext()) {
				Table table = (Table) itTables.next();
				if (!table.getName().equals("SYSTEM")) {
					if (!operations.compartenOtrasCapasTabla(table,
							lt.getLayer()))
						operations.deshacerCamposVersionado(table.getName(),
								true);

					// Actualizo el campo layers.validator
					if (lt.getLayer().getValidator() != null
							&& lt.getLayer().getValidator()
									.equals(VALIDATOR_CLASS))
						lt.getLayer().setValidator(null);
					else if (lt.getLayer().getValidator() != null) {
						String[] validadores = lt.getLayer().getValidator()
								.split(";" + VALIDATOR_CLASS);
						lt.getLayer().setValidator(validadores[0]);
					}
					eliminarColumna(table, lt, operations);
				}
			}
		} catch (Exception e) {
			throw new DataException(I18N.get("GestorCapas",
					"layers.error.versionado"));
		}
	}

	/**
	 * Devuelve si un String es numérico o no
	 * 
	 * @param cadena
	 * @return
	 */
	private static boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public void setFireEvents(boolean fireEvents) {
		this.fireEvents = fireEvents;
	}

	public boolean getFireEvents() {
		return this.fireEvents;
	}

	/**
	 * Se obtiene un listado de las tablas asociadas a la capa
	 * 
	 * @return
	 */
	private List obtenerTablasAsociadasCapa() {
		TableRow[] tableRow = ((TableAttributesModel) tblAtributos.getModel())
				.getData();
		int n = tableRow.length;
		List listaTablas = new ArrayList();
		for (int i = 0; i < n; i++) {
			Table tabla = tableRow[i].getColumna().getTable();
			if (!listaTablas.contains(tabla))
				listaTablas.add(tabla);
		}
		return listaTablas;
	}

	/**
	 * Por lo menos el campo de revision_actual debe aparecer en la tabla
	 * columns y attributes
	 * 
	 * @param operations
	 * @param table
	 * @param lt
	 */
	protected void crearColumnas(LayerOperations operations, Table table,
			LayerTable lt) throws DataException {
		try {
			// Inserta registro en tabla columns
			ColumnRow colrow = new ColumnRow();
			colrow.getColumnaBD().setDescription(REVISION_ACTUAL);
			colrow.getColumnaBD().setName(REVISION_ACTUAL);
			colrow.getColumnaBD().setLength(10);
			colrow.getColumnaBD().setTableName(table.getName());
			colrow.getColumnaBD().setType("NUMERIC");
			colrow.getColumnaBD().setDefaultValue("0");
			operations.annadirColumna(table, colrow);

			// Inserta registro en tabla attributes
			TableRow[] atributos = ((TableAttributesModel) tblAtributos
					.getModel()).getData();
			Attribute att = new Attribute();
			att.setEditable(true);
			att.setIdLayer(lt.getIdLayer());
			colrow.getColumnaSistema().setTable(table);
			att.setColumn(colrow.getColumnaSistema());
			att.setName(REVISION_ACTUAL);
			att.setEditable(false);
			colrow.getColumnaSistema().setAttribute(att);
			colrow.getColumnaSistema().setName(REVISION_ACTUAL);
			Hashtable htTraducciones = new Hashtable();
			att.setHtTraducciones(introducirTraducciones(htTraducciones));
			att.setPosition(atributos.length + 1);
			if (!capaNueva) {
				int idDictionary = operations.actualizarDictionary(
						att.getHtTraducciones(), att.getIdAlias());
				if (idDictionary > 0)
					att.setIdAlias(idDictionary);
				if (operations.actualizarAtributo(att) < 0)
					throw new DataException(I18N.get("GestorCapas",
							"layers.error.actualizacion.atributo")
							+ " "
							+ att.getName());
				this.actualizarTablasyAtributos(operations);
			} else {
				// Actualizo la tabla de atributos con el nuevo atributo
				List listAtributos = Arrays.asList(atributos);
				Vector vecTableRow = new Vector();
				((TableAttributesModel) tblAtributos.getModel())
						.setData(new TableRow[0]);

				Iterator it = listAtributos.iterator();
				while (it.hasNext()) {

					vecTableRow.add((TableRow) it.next());
				}
				TableRow fila = new TableRow(att.getColumn(), att, "",
						att.isEditable());
				vecTableRow.add(fila);
				((TableAttributesModel) tblAtributos.getModel())
						.setData((TableRow[]) vecTableRow
								.toArray(new TableRow[vecTableRow.size()]));
			}

			Identificadores.put("TablasModificadas", true);
			Identificadores.put("LayersActualizada", false);
			Identificadores.put("TablasDominiosActualizada", false);
		} catch (DataException e1) {
			throw new DataException(e1);
		}

	}

	/**
	 * Se elimina la columna de la base de datos del campo revisión actual al
	 * quitar el versionado
	 * 
	 * @param selectedTable
	 * @param lt
	 * @param operaciones
	 * @throws DataException
	 */
	protected void eliminarColumna(Table selectedTable, LayerTable lt,
			LayerOperations operaciones) throws DataException {
		try {
			TableRow[] atributos = ((TableAttributesModel) tblAtributos
					.getModel()).getData();
			ColumnRow columnRow = new ColumnRow();
			int n = atributos.length;
			for (int i = 0; i < n; i++) {
				TableRow tRow = atributos[i];
				if (tRow.getColumna().getName()
						.equals(LayersPanel.REVISION_ACTUAL))
					columnRow.setColumnaSistema(tRow.getColumna());
			}
			TablesDBOperations operations = new TablesDBOperations();
			if (operations.eliminarColumnaSistema(selectedTable, columnRow)) {
				// se elimina la referencia de la columna dentro del table para
				// evitar problemas
				// en caso de edición
				int index = operations.findColumnPosition(selectedTable,
						columnRow);
				selectedTable.getColumns().remove(new Integer(index));
				Identificadores.put("TablasModificadas", true);
				Identificadores.put("ColumnasBorradas", true);

				Identificadores.put("LayersActualizada", false);
				Identificadores.put("TablasDominiosActualizada", false);
			}
		} catch (DataException e1) {
			// TODO Auto-generated catch block
			throw new DataException(e1);
		}
	}

	/**
	 * Una vez realizadas las inserciones de las columnas de versionado, se
	 * actualiza el árbol que muestra las tablas y las tablas de atributos
	 * 
	 * @param operaciones
	 * @throws DataException
	 */
	private void actualizarTablasyAtributos(LayerOperations operaciones)
			throws DataException {
		try {
			// Cada vez que se entra, se actualiza el arbol de tablas del
			// sistema con
			// los posibles cambios realizados
			Identificadores.put("Tablas", null);
			Identificadores.put("Columnas", null);

			boolean bEstado = (getSelectedRadioButton(btnGroup) != null);
			if (jPanelTablas != null) {
				this.remove(jPanelTablas);
				jPanelTablas = new JPanelTables(
						TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
				jPanelTablas.setBorder(BorderFactory.createTitledBorder(I18N
						.get("GestorCapas", "layers.paneltablas.titulo")));
				jPanelTablas.setBounds(new Rectangle(16, 90, 250, 350));
				this.add(jPanelTablas);
				jPanelTablas.setEnabled(bEstado);
				treeTablas = jPanelTablas.getTree();
				treeTablas.addTreeSelectionListener(this);
			}
			if (tblAtributos != null) {
				this.remove(tblAtributos);
				// Cargar atributos en la lista de atributos
				cargarAtributos(operaciones.buscarColumnasAtributos(idLayer));

			}
		} catch (Exception e) {
			throw new DataException(e);
		}

	}

	/**
	 * Traduce en los distintos idiomas el campo revisión actual
	 * 
	 * @param htTraducciones
	 * @return
	 */
	private Hashtable introducirTraducciones(Hashtable htTraducciones) {
		htTraducciones.put(
				Constantes.LOCALE_CASTELLANO,
				traducirNombre(Constantes.LOCALE_CASTELLANO,
						"layers.revision_actual"));
		htTraducciones.put(
				Constantes.LOCALE_VALENCIANO,
				traducirNombre(Constantes.LOCALE_VALENCIANO,
						"layers.revision_actual"));
		htTraducciones.put(
				Constantes.LOCALE_CATALAN,
				traducirNombre(Constantes.LOCALE_CATALAN,
						"layers.revision_actual"));
		htTraducciones.put(
				Constantes.LOCALE_EUSKEDA,
				traducirNombre(Constantes.LOCALE_EUSKEDA,
						"layers.revision_actual"));
		htTraducciones.put(
				Constantes.LOCALE_GALLEGO,
				traducirNombre(Constantes.LOCALE_GALLEGO,
						"layers.revision_actual"));
		return htTraducciones;
	}

	/**
	 * Busca la traducción de un vocablo en la lengua correspondiente
	 * 
	 * @param locale
	 * @param vocablo
	 * @return
	 */
	private String traducirNombre(String locale, String vocablo) {
		Locale currentLocale = new Locale(locale);
		ResourceBundle messages;
		try {
			messages = ResourceBundle.getBundle(
					"com.geopista.app.layerutil.language.GestorCapasi18n",
					currentLocale);
		} catch (Exception e) {
			messages = ResourceBundle.getBundle(
					"com.geopista.app.layerutil.language.GestorCapasi18n",
					new Locale(Constantes.LOCALE_CASTELLANO));
		}
		return messages.getString(vocablo);
	}
} // @jve:decl-index=0:visual-constraint="25,-39"
