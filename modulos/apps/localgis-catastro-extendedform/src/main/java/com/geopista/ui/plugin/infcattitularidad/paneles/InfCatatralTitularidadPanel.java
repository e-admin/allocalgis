/**
 * InfCatatralTitularidadPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.infcattitularidad.paneles;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.RepartoPanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableTreeModel;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaUrbanistica;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;
import com.geopista.ui.plugin.infcattitularidad.dialogs.ASCPanel;
import com.geopista.ui.plugin.infcattitularidad.dialogs.FincaPanel;
import com.geopista.ui.plugin.infcattitularidad.dialogs.FxccPanel;
import com.geopista.ui.plugin.infcattitularidad.dialogs.ImagenCatastroPanel;
import com.geopista.ui.plugin.infcattitularidad.dialogs.SueloPanel;
import com.geopista.ui.plugin.infcattitularidad.dialogs.UnidadConstructivaPanel;
import com.geopista.ui.plugin.infcattitularidad.utils.EdicionUtils;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

public class InfCatatralTitularidadPanel extends JPanel implements
		TreeSelectionListener {

	/**
	 * Auto generates Serial ID
	 */
	private static final long serialVersionUID = 8723205371229343251L;

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	private final Blackboard blackboard = aplicacion.getBlackboard();

	private JScrollPane jScrollPane = null;
	private JPanel jPanelBotones = null;
	private JPanel jPanelVisualizar = null;
	private JCheckBox jCheckBoxUC = null;
	private JCheckBox jCheckBoxSuelos = null;
	private JCheckBox jCheckBoxLcomunes = null;
	private JButton jButtonGuardar = null;
	private JButton jButtonValidar = null;
	private JButton jButtonGuardarFinalizar = null;
	private JPanel jPanelLeftPosition = null;
	private JSplitPane jSplitPane = null;

	public static final String ICONO_EXPEDIENTE = "tab_expediente.gif";
	public static final String ICONO_FINCA = "tab_finca.gif";
	public static final String ICONO_UC = "tab_uc.gif";
	public static final String ICONO_SUELO = "tab_suelo.gif";
	public static final String ICONO_REPARTO = "tab_reparto.gif";
	public static final String ICONO_FXCC = "tab_fxcc.gif";
	public static final String ICONO_BUSCAR = "buscar.gif";

	//private ExpedientePanelTree jPanelTree = null;
	
	private InfoCatastralPanelTree jPanelTree = null;
	private JTabbedPane jTabbedPane;

	private JTree tree = null;
	private HideableTreeModel model = null;
	private Collection collParcelas = null;
	private String convenio = null;
	private FincaPanel parcelaPanel = null;
	private SueloPanel sueloPanel = null;
	private UnidadConstructivaPanel ucPanel = null;
	private FxccPanel fxccPanel = null;
	private ASCPanel ascPanel = null;
	private FincaCatastro parcela = null;
	private SueloCatastro suelo = null;
	private BienInmuebleJuridico bijPanel = null;
	private UnidadConstructivaCatastro uc = null;
	private final FX_CC fxcc = null;
	private ImagenCatastroPanel imagenCatastroPanel = null;

	private RepartoPanel repartoPanel;
	private final RepartoCatastro reparto = null;

	private ArrayList lstParcelasEditadas;

	private boolean isEditable = false;

	private boolean ultimoEsLocal = true;
	private boolean cambioPestañaManual = false;
	private boolean cambioPestañaAutomatico = false;
	private boolean evaluandoDatosCorrectos = false;
	private boolean errorEncontrado = false;

	// Nodo seleccionado en el arbol para recoger sus datos.
	private DefaultMutableTreeNode preNodeInfo;
	private Object nodoAPegar = null;
	private JMenuItem copiar;
	private JMenuItem pegar;
	private JButton jButtonEstadoModificador = null;

	
	ArrayList listReferencias = new ArrayList();
	
	public InfCatatralTitularidadPanel( boolean isEditable, Collection coll,String convenio) {
		super();
		setName("GestionExpedientePanel");
		this.isEditable = true;
		this.convenio = convenio;
		this.collParcelas = coll;
		initialize();

	}

	public InfCatatralTitularidadPanel() {

	}

	/**
	 * This method initializes this
	 * @throws Exception 
	 * 
	 */
	private void initialize() {
		Locale loc = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.infcattitularidad.languages.InfCatastraTitularidadPlugIni18n",
						loc, this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("InfCatastralTitularidadPlugIn", bundle);

		EdicionOperations oper = new EdicionOperations();
		parcelaPanel = new FincaPanel(/*expediente.getTipoExpediente()*/);
		sueloPanel = new SueloPanel();
		ucPanel = new UnidadConstructivaPanel();
		fxccPanel = new FxccPanel();
		ascPanel = new ASCPanel();
		ascPanel.setFxccPanel(fxccPanel);
		imagenCatastroPanel = new ImagenCatastroPanel();

		this.setPreferredSize(new java.awt.Dimension(800, 575));
		this.setLayout(new GridBagLayout());

		// se buscan los datos de las parcelas seleccionadas en el mapa.
		for (Iterator iterator = collParcelas.iterator(); iterator.hasNext();) {
			Integer idParcela = (Integer)iterator.next();
			try{
				FincaCatastro finca = ConstantesRegExp.clienteCatastro.obtenerInfCastatralFinca(idParcela, convenio);

				finca.setLstSuelos(null);
				finca.setLstConstrucciones(null);
				finca.setLstCultivos(null);
				finca.setLstUnidadesConstructivas(null);
				finca.setLstReparto(null);
				listReferencias.add(finca);
			}
			catch (Exception e) {
				 ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
	             e.printStackTrace();
			}
		}
		
		jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getJPanelLeftPosition(), getJTabbedPaneExpediente());

		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setDividerLocation(175);
		jSplitPane.setDividerSize(10);

		this.add(jSplitPane, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Listener de los cambios en el arbol
		getJPanelTree().getTree().addTreeSelectionListener(this);

		// Carga los datos más pesados
		ArrayList lst = null;
		lst = (ArrayList) blackboard.get("ListaMunicipios");
		if (lst == null) {
			lst = oper.obtenerMunicipios();
			blackboard.put("ListaMunicipios", lst);
		}

		lst = null;
		lst = (ArrayList) blackboard.get("ListaProvincias");
		if (lst == null) {
			lst = oper.obtenerProvincias();
			blackboard.put("ListaProvincias", lst);
		}
		
		blackboard.put("lastSelected", null);

		lstParcelasEditadas = jPanelTree.getLstReferencias();
		initilizeListaReferencias(lstParcelasEditadas);

		// Si es consulta no se permitirá modificar los campos
		EdicionUtils.enablePanel(parcelaPanel, false);
		parcelaPanel.setEditable(false);
		parcelaPanel.setEnabled(false);
	
		parcelaPanel.asignaArbolPanel(jPanelTree);
		ucPanel.asignaArbolPanel(jPanelTree);
		sueloPanel.asignaArbolPanel(jPanelTree);
		
		//ascPanel.loadExpedienteData(this.getExpediente());

		EdicionUtils.disabledButtons(parcelaPanel, "_borrar");
		EdicionUtils.disabledButtons(parcelaPanel, "vista");

	}

	private void initilizeListaReferencias(ArrayList lstReferencias) {

		if ((lstReferencias.iterator() != null) && (lstReferencias.size() > 0)) {
			if (lstReferencias.iterator().next() instanceof FincaCatastro) {
				loadFincaCatastro(lstReferencias);
			} else if (lstReferencias.iterator().next() instanceof BienInmuebleCatastro) {
				loadBienes(lstReferencias);
//				loadBienesAscPanel(lstReferencias);
			}
		}

	}

	
	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJPanelTree());

		}
		return jScrollPane;
	}

	private JPanel getJPanelLeftPosition() {

		if (jPanelLeftPosition == null) {

			jPanelLeftPosition = new JPanel(new GridBagLayout());

			jPanelLeftPosition
					.add(getJScrollPane(), new GridBagConstraints(0, 0, 1, 1,
							0.3, 0.9, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(10, 10, 0, 10),
							120, 0));

			jPanelLeftPosition.add(getJPanelVisualizar(),
					new GridBagConstraints(0, 1, 1, 1, 0.3, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(10, 10, 10, 10), 0, 0));
		}
		return jPanelLeftPosition;
	}

	private InfoCatastralPanelTree getJPanelTree() {
		if (jPanelTree == null) {
			/*jPanelTree = new ExpedientePanelTree(
					TreeSelectionModel.SINGLE_TREE_SELECTION, expediente);
					*/
			jPanelTree = new InfoCatastralPanelTree(
					TreeSelectionModel.SINGLE_TREE_SELECTION, listReferencias);
			
			tree = jPanelTree.getTree();
			model = (HideableTreeModel) tree.getModel();
		}

		return jPanelTree;
	}

	/**
	 * This method initializes jPanelVisualizar
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelVisualizar() {
		if (jPanelVisualizar == null) {
			jPanelVisualizar = new JPanel();

			jPanelVisualizar.setPreferredSize(new Dimension(200, 70));
			jPanelVisualizar.setLayout(new GridBagLayout());
			jPanelVisualizar.add(getJCheckBoxUC(), new GridBagConstraints(0, 0,
					1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelVisualizar.add(getJCheckBoxSuelos(), new GridBagConstraints(
					0, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelVisualizar.add(getJCheckBoxLcomunes(),
					new GridBagConstraints(0, 2, 1, 1, 0, 0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 0, 0, 0), 0, 0));
			jPanelVisualizar.setBorder(BorderFactory.createTitledBorder(null,
					I18N.get("InfCatastralTitularidadPlugIn",
							"infCatastralTitularidad.panel.visualizar.titulo"),
					TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
							Font.BOLD, 12)));
		}
		return jPanelVisualizar;
	}

	/**
	 * This method initializes jCheckBoxUC
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxUC() {
		if (jCheckBoxUC == null) {
			jCheckBoxUC = new JCheckBox();
			jCheckBoxUC.setText(I18N.get("InfCatastralTitularidadPlugIn",
					"infCatastralTitularidad.panel.visualizar.unidadesconstructivas"));
			jCheckBoxUC.setSelected(true);
			jCheckBoxUC.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showNodesOfClass(jCheckBoxUC.isSelected(),
							UnidadConstructivaCatastro.class);
					if (!jCheckBoxUC.isSelected())
						EdicionUtils.clearPanel(ucPanel);
				}
			});
		}
		return jCheckBoxUC;
	}

	protected void showNodesOfClass(boolean b, Class type) {

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel()
				.getRoot();
		tree = visitAllNodes(root, tree, b, type);
		model.reload();

		// Se expanden los nodos de fincas del expediente
		for (int i = 0; i < root.getChildCount(); i++)
			tree.expandPath(new TreePath(((DefaultMutableTreeNode) root
					.getChildAt(i)).getPath()));

		// Una vez expandido el árbol, se selecciona el elemento raíz. Así se
		// evitan problemas asociados a la pestaña seleccionada
		tree.setSelectionRow(0);
	}

	// Valida si son correctos los datos de los paneles
	/*public boolean validaDatosCorrectos(DefaultMutableTreeNode nodo) {
		int n = nodo.getChildCount();
		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) nodo
					.getChildAt(i);
			if (!errorEncontrado && datosMinimosYCorrectos(hijo)) {
				validaDatosCorrectos(hijo);
			} else {
				errorEncontrado = true;
				return false;
			}
		}
		return true;
	}
	*/

	public JTree visitAllNodes(DefaultMutableTreeNode node, JTree tree,
			boolean visible, Class type) {
		System.out.println(node.getUserObject().toString());

		if (node.getUserObject().getClass() == type) {

			if (node.getUserObject().getClass() == ConstruccionCatastro.class) {
				ConstruccionCatastro cons = (ConstruccionCatastro) node
						.getUserObject();
				if (cons.getDatosEconomicos().getCodTipoValor() != null
						&& !cons.getDatosEconomicos().getCodTipoValor().equals(
								"")
						&& cons.getDatosEconomicos().getCodModalidadReparto() != null)
					((HideableNode) node).setVisible(visible);
			} else {
				((HideableNode) node).setVisible(visible);
			}
		}

		else if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				HideableNode n = (HideableNode) e.nextElement();
				tree = visitAllNodes(n, tree, visible, type);
			}
		}
		return tree;
	}

	/**
	 * This method initializes jCheckBoxSuelos
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxSuelos() {
		if (jCheckBoxSuelos == null) {
			jCheckBoxSuelos = new JCheckBox();
			jCheckBoxSuelos.setSelected(true);
			jCheckBoxSuelos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showNodesOfClass(jCheckBoxSuelos.isSelected(),
							SueloCatastro.class);
					if (!jCheckBoxSuelos.isSelected())
						EdicionUtils.clearPanel(sueloPanel);
				}
			});
			jCheckBoxSuelos.setText(I18N.get("InfCatastralTitularidadPlugIn",
					"infCatastralTitularidad.panel.visualizar.suelos"));
		}
		return jCheckBoxSuelos;
	}

	/**
	 * This method initializes jCheckBoxLcomunes
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxLcomunes() {
		if (jCheckBoxLcomunes == null) {
			jCheckBoxLcomunes = new JCheckBox();
			jCheckBoxLcomunes.setSelected(true);
			jCheckBoxLcomunes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showNodesOfClass(jCheckBoxLcomunes.isSelected(),
							ConstruccionCatastro.class);
				}
			});
			jCheckBoxLcomunes.setText(I18N.get("InfCatastralTitularidadPlugIn",
					"infCatastralTitularidad.panel.visualizar.lcomunes"));
		}
		return jCheckBoxLcomunes;
	}

	

	/**
	 * This method initializes jTabbedPaneExpediente
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPaneExpediente() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane
					.addChangeListener(new javax.swing.event.ChangeListener() {

						private FeatureExtendedPanel oldpanel = null;

						public void stateChanged(ChangeEvent e) {

							// Notifica a los paneles de los cambios de pestañas
							FeatureExtendedPanel panel = (FeatureExtendedPanel) jTabbedPane
									.getSelectedComponent();
							if (panel != null)
								panel.enter();
							oldpanel = panel;
							cambioPestañaManual = true;
							if (oldpanel.equals(sueloPanel)) {
								actualizaArbol("Suelo");
								DefaultMutableTreeNode nodeF = (DefaultMutableTreeNode) tree
										.getLastSelectedPathComponent();
								Object nodeInfoF = nodeF.getUserObject();
								FincaCatastro finca = null;
								if (nodeInfoF instanceof FincaCatastro) {
									finca = (FincaCatastro) nodeInfoF;
									EdicionUtils.clearPanel(sueloPanel);
									sueloPanel.setSuelo(null);
								} else if (nodeInfoF instanceof SueloCatastro) {
									finca = (FincaCatastro) ((DefaultMutableTreeNode) tree
											.getSelectionPath().getParentPath()
											.getLastPathComponent())
											.getUserObject();
								}
								sueloPanel.setFincaActual(finca);
							} else if (oldpanel.equals(ucPanel)) {
								actualizaArbol("UC");
								DefaultMutableTreeNode nodeF = (DefaultMutableTreeNode) tree
										.getLastSelectedPathComponent();
								Object nodeInfoF = nodeF.getUserObject();
								FincaCatastro finca = null;
								if (nodeInfoF instanceof FincaCatastro) {
									finca = (FincaCatastro) nodeInfoF;
									EdicionUtils.clearPanel(ucPanel);
									ucPanel.setUnidadCons(null);
								} else if (nodeInfoF instanceof UnidadConstructivaCatastro) {
									finca = (FincaCatastro) ((DefaultMutableTreeNode) tree
											.getSelectionPath().getParentPath()
											.getLastPathComponent())
											.getUserObject();
								}
								ucPanel.setFincaActual(finca);
							}
							cambioPestañaManual = false;
						}
					});

			jTabbedPane.addTab(I18N.get("InfCatastralTitularidadPlugIn",
					"infCatastralTitularidad.pestania.finca.titulo"), IconLoader
					.icon(ICONO_FINCA), parcelaPanel, null);
			jTabbedPane.addTab(I18N.get("InfCatastralTitularidadPlugIn",
					"infCatastralTitularidad.pestania.suelo.titulo"), IconLoader
					.icon(ICONO_SUELO), sueloPanel, null);
			jTabbedPane.addTab(I18N.get("InfCatastralTitularidadPlugIn",
					"infCatastralTitularidad.pestania.uc.titulo"), IconLoader
					.icon(ICONO_UC), ucPanel, null);
			jTabbedPane.addTab("DXF", null, fxccPanel, null);
			jTabbedPane.addTab("ASC", null, ascPanel, null);
			jTabbedPane
					.addTab("IMG", null, imagenCatastroPanel, null);

			// Deshabilita todas las pestañas al cargar el árbol
			jTabbedPane.setSelectedComponent(parcelaPanel);
			jTabbedPane.setEnabledAt(jTabbedPane
					.indexOfComponent(sueloPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane
					.indexOfComponent(sueloPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane
					.indexOfComponent(ucPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane
					.indexOfComponent(parcelaPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane
					.indexOfComponent(fxccPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane
					.indexOfComponent(ascPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane
					.indexOfComponent(imagenCatastroPanel), false);

			jTabbedPane.setPreferredSize(new Dimension(650, 500));
			jTabbedPane.setMaximumSize(jTabbedPane
					.getPreferredSize());
			jTabbedPane.setMinimumSize(jTabbedPane
					.getPreferredSize());

			EdicionUtils.enablePanel(jTabbedPane, true);
			parcelaPanel.setEditable(isEditable);

		}
		return jTabbedPane;
	}


	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {

		blackboard.put("ComBienes", false);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null) {
			setPreNodeInfo(node);
			return;
		}

		// Recogemos los datos del panel seleccionado por el nodo anterior
		if (getPreNodeInfo() != null) {
			if (getPreNodeInfo() != node) {
				//datosMinimosYCorrectos(getPreNodeInfo());
				recopilarDatos(getPreNodeInfo());
			}
		}

		cambioPestañaAutomatico = true;
		Object nodeInfo = node.getUserObject();
		aplicacion.getBlackboard().put("lastSelected", nodeInfo);

		// Guardamos el nodo seleccionado para guardar sus datos en el siguiente
		// cambio.
		setPreNodeInfo(node);

		// Si se selecciona una finca del árbol, se cargan los bienes, cultivos,
		// etc.
		// de la finca en el árbol
		if (nodeInfo instanceof FincaCatastro) {
			// Extraer información de la parcela y rellenar el arbol
			// Devuelve la finca, porque ya recupera todos sus datos
			parcela = getJPanelTree().fillObject(node);
			obtenerInformacionPonencias();
			tree.expandPath(tree.getSelectionPath());

			// Sustituye el nodo finca por el relleno con los datos de la misma
			if (parcela != null)
				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent())
						.setUserObject(parcela);
			tree.repaint();
			

		}
		// Si cuelgan los bienes directamente del expediente porque se trata de
		// información de titularidad
		else if (nodeInfo instanceof BienInmuebleJuridico
				&& ((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof Expediente) {
			// Extraer información del bien y rellenar el arbol
			bijPanel = getJPanelTree().fillObjectBienInmueble(node);
			tree.expandPath(tree.getSelectionPath());

			// Sustituye el nodo bien inmueble por el relleno con los datos de
			// la misma
			if (bijPanel != null)
				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent())
						.setUserObject(bijPanel);
			tree.repaint();
		}

		if (nodeInfo instanceof Expediente) {
			jTabbedPane.setSelectedComponent(parcelaPanel);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(sueloPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ucPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(parcelaPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(fxccPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ascPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(imagenCatastroPanel), false);

		} else if ((nodeInfo instanceof FincaCatastro
				|| nodeInfo instanceof BienInmuebleJuridico
				|| nodeInfo instanceof ConstruccionCatastro
				|| nodeInfo instanceof Persona || nodeInfo instanceof Cultivo)
				&& !cambioPestañaManual) {
			jTabbedPane.setSelectedComponent(parcelaPanel);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(parcelaPanel), true);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(sueloPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ucPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(fxccPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ascPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(imagenCatastroPanel), false);

			
			
			
			if (nodeInfo instanceof Cultivo && ultimoEsLocal) {
				ultimoEsLocal = false;
				parcelaPanel.getJPanelBienInmueble().remove(parcelaPanel.getJPanelLocal());
				parcelaPanel.getJPanelBienInmueble().add(parcelaPanel.getJPanelCultivo(),
						new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 5,
										0, 5), 0, 0));

				parcelaPanel.getJPanelBienInmueble().updateUI();
			} else if (nodeInfo instanceof ConstruccionCatastro
					&& !ultimoEsLocal) {
				ultimoEsLocal = true;
				parcelaPanel.getJPanelBienInmueble().remove(parcelaPanel.getJPanelCultivo());
				parcelaPanel.getJPanelBienInmueble().add(parcelaPanel.getJPanelLocal(),
						new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 5,
										0, 5), 0, 0));
				parcelaPanel.getJPanelBienInmueble().updateUI();
			}
		}

		else if (nodeInfo instanceof SueloCatastro) {
			parcelaPanel.setFocoEn(null);
			jTabbedPane.setSelectedComponent(sueloPanel);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(parcelaPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ucPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(sueloPanel), true);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(fxccPanel), true);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ascPanel), true);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(imagenCatastroPanel), true);
		} else if (nodeInfo instanceof UnidadConstructivaCatastro) {
			parcelaPanel.setFocoEn(null);
			jTabbedPane.setSelectedComponent(ucPanel);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(parcelaPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(sueloPanel), false);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ucPanel), true);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(fxccPanel), true);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(ascPanel), true);
			jTabbedPane.setEnabledAt(jTabbedPane.indexOfComponent(imagenCatastroPanel), true);
		}

		// Si es consulta no se permitirá modificar los campos

		if (nodeInfo instanceof FincaCatastro
				|| nodeInfo instanceof ConstruccionCatastro
				|| nodeInfo instanceof Cultivo || nodeInfo instanceof Titular
				|| nodeInfo instanceof UnidadConstructivaCatastro
				|| nodeInfo instanceof SueloCatastro
				|| nodeInfo instanceof BienInmuebleCatastro
				|| nodeInfo instanceof BienInmuebleJuridico)
			loadExpediente();

		loadData(node);

		if (getListReferencias() != null) {
			if (!getListReferencias().isEmpty()) {
				Object object = getListReferencias().iterator()
						.next();
				if (!(object instanceof FincaCatastro)) {
					EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneFincas(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBotoneraFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelLocal(),	false);

				}
			}
		}

		if (nodeInfo instanceof FincaCatastro)
			if (parcela != null)
				if (parcela.isDelete()) {
					EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneFincas(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBotoneraFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelLocal(),	false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelCultivo(),false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneCultivo(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneLocal(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBienInmueble(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneBienInmueble(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelTitular(),false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneTitular(), false);

	
				} else {
					
					EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneFincas(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBotoneraFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelLocal(),	false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelCultivo(),false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneCultivo(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneLocal(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBienInmueble(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneBienInmueble(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelTitular(),false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneTitular(), true);
				
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_borrarfinca");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraTitular(), "_borrartitular");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraBienInmueble(), "_borrarbien");
				}
		
		// Se deshabilitan los botones 
		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_borrarfinca");
		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraTitular(), "_borrartitular");
		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraBienInmueble(), "_borrarbien");
		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraCultivo(), "_borrarcultivo");
		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraCultivo(), "_Creparto");
		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "vistacultivo");
		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "_Cmasdatoslocales");
		
		
		// Se habilitan los botones 
		EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraCultivo(), "_Cmasdatoscultivo");
		EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraCultivo(), "vistalocal");
		EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraTitular(), "_Cmasdatostitular");
		EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraBienInmueble(), "_Cmasdatosinmueble");
		
		if (!parcelaPanel.isFocoAutomatico()) {
			parcelaPanel.asignaFocoManual();
		}
		cambioPestañaAutomatico = false;
	}


	private void loadData(DefaultMutableTreeNode node) {
		Object nodeInfo = node.getUserObject();

		// Cargar la información de la entidad catastral seleccionada en los
		// campos correspondientes, limpiando antes todo el panel
		parcelaPanel.clearPanel(parcelaPanel);

		if (nodeInfo instanceof Titular) {
			if (((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
					.getParent()).getUserObject()).getLstComBienes() != null
					&& ((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
							.getParent()).getUserObject()).getLstComBienes()
							.size() != 0)
				parcelaPanel.loadData(((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
								.getParent()).getUserObject()));
		}

		parcelaPanel.loadData(nodeInfo);

		// Recargar informacion de parcela si se selecciona un bien inmueble
		if (nodeInfo instanceof BienInmuebleCatastro) {
			parcelaPanel.loadData(((DefaultMutableTreeNode) node.getParent())
					.getUserObject());
			loadFinca();
		} else if (nodeInfo instanceof BienInmuebleJuridico) {

			if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof FincaCatastro) {
				parcelaPanel.loadData(((DefaultMutableTreeNode) node
						.getParent()).getUserObject());
			}

			loadFinca();
			loadLocales((BienInmuebleJuridico) nodeInfo);
			loadCultivos((BienInmuebleJuridico) nodeInfo);
			loadBienes((BienInmuebleJuridico) nodeInfo);

			parcelaPanel.loadComunidadBienes((BienInmuebleJuridico) nodeInfo);

		}
		// Recarga información de parcela si se selecciona local o cultivo
		// y de bien inmueble si se trata de local o cultivo no comunes
		// Si es titular recarga siempre parcela y bien inmueble
		else if (nodeInfo instanceof ConstruccionCatastro
				|| nodeInfo instanceof Cultivo || nodeInfo instanceof Titular) {
			if (((DefaultMutableTreeNode) node).getUserObject() instanceof ConstruccionCatastro) {
				loadLocales((ConstruccionCatastro) nodeInfo);
				clearTitulares();
				parcelaPanel.loadData(((DefaultMutableTreeNode) node)
						.getUserObject());
			}
			if (((DefaultMutableTreeNode) node).getUserObject() instanceof Cultivo) {
				loadCultivos((Cultivo) nodeInfo);
				clearTitulares();
				parcelaPanel.loadData(((DefaultMutableTreeNode) node)
						.getUserObject());
			}
			if (((DefaultMutableTreeNode) node).getUserObject() instanceof Titular) {
				if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof BienInmuebleJuridico) {

					loadFinca();
					loadLocales((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
							.getParent()).getUserObject());
					loadCultivos((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
							.getParent()).getUserObject());
					loadBienes((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
							.getParent()).getUserObject());

					parcelaPanel.loadData(((DefaultMutableTreeNode) node
							.getParent()).getUserObject());

					if (((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
							.getParent()).getUserObject()).getLstComBienes() != null
							&& ((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
									.getParent()).getUserObject())
									.getLstComBienes().size() != 0)
						parcelaPanel
								.loadData(((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
										.getParent()).getUserObject())
										.getLstComBienes());

					loadComunidadBienes(
							(Titular) ((DefaultMutableTreeNode) node)
									.getUserObject(),
							((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
									.getParent()).getUserObject()));
				}
				loadTitulares();

			}
			if ((((DefaultMutableTreeNode) node).getUserObject() instanceof Cultivo)
					|| (((DefaultMutableTreeNode) node).getUserObject() instanceof ConstruccionCatastro)) {
				if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof BienInmuebleJuridico) {
					loadFinca();
					loadBienes((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
							.getParent()).getUserObject());
					parcelaPanel.loadData(((DefaultMutableTreeNode) node
							.getParent()).getUserObject());
				}
			}
			// Común
			if (((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof FincaCatastro) {
				blackboard.put("Comun", true);
				parcelaPanel.loadData(((DefaultMutableTreeNode) node
						.getParent()).getUserObject());
			}
			// No común o titular
			else if (((DefaultMutableTreeNode) node.getParent().getParent())
					.getUserObject() instanceof FincaCatastro) {
				blackboard.put("Comun", false);
				parcelaPanel.loadData(((DefaultMutableTreeNode) node
						.getParent().getParent()).getUserObject());
				parcelaPanel.loadData(((DefaultMutableTreeNode) node
						.getParent()).getUserObject());
			}
		} else if (nodeInfo instanceof SueloCatastro) {
			sueloPanel.load((SueloCatastro) nodeInfo);
			parcelaPanel.loadData(((DefaultMutableTreeNode) node.getParent())
					.getUserObject());
		} else if (nodeInfo instanceof UnidadConstructivaCatastro) {
			ucPanel.load((UnidadConstructivaCatastro) nodeInfo);
			parcelaPanel.loadData(((DefaultMutableTreeNode) node.getParent())
					.getUserObject());
		} else if (nodeInfo instanceof FincaCatastro) {

			loadFinca((FincaCatastro) nodeInfo);
			loadLocales((FincaCatastro) nodeInfo);
			loadCultivos((FincaCatastro) nodeInfo);
			loadBienes((FincaCatastro) nodeInfo);
			ascPanel.loadFincaCatastroData((FincaCatastro) nodeInfo);
			fxccPanel.setDxfData(((FincaCatastro) nodeInfo));
			ascPanel.setDxfData(((FincaCatastro) nodeInfo));
			imagenCatastroPanel.setLstImagenes(((FincaCatastro) nodeInfo).getRefFinca().getRefCatastral());
		} 
		/*else if (nodeInfo instanceof Expediente) {
			ascPanel.loadExpedienteData((Expediente) nodeInfo);
			loadExpediente((Expediente) nodeInfo);
			loadBienes((Expediente) nodeInfo);
			loadLocales((Expediente) nodeInfo);
			loadCultivos((Expediente) nodeInfo);

		}
*/
	}

	/*private void loadBienes(Expediente expediente) {

		ArrayList bienes = new ArrayList();

		Iterator itRef = expediente.getListaReferencias().iterator();
		while (itRef.hasNext()) {
			Object referencia = (Object) itRef.next();
			if (referencia instanceof FincaCatastro
					&& ((FincaCatastro) referencia).getLstBienesInmuebles() != null) {
				Iterator itFinc = ((FincaCatastro) referencia)
						.getLstBienesInmuebles().iterator();
				while (itFinc.hasNext()) {
					bienes.add(((BienInmuebleJuridico) itFinc.next())
							.getBienInmueble());
				}
			} else if (referencia instanceof BienInmuebleCatastro) {
				bienes.add(referencia);
			}
		}

		loadBienes(bienes);
//		loadBienesAscPanel(bienes);

		return;
	}
*/
//	private void loadBienesAscPanel(ArrayList bienes) {
//		// TODO Auto-generated method stub
//		this.ascPanel.loadTablePlantas(bienes);
//	}

/*	private void loadLocales(Expediente expediente) {

		ArrayList locales = new ArrayList();

		Iterator itRef = expediente.getListaReferencias().iterator();
		while (itRef.hasNext()) {
			Object referencia = (Object) itRef.next();
			if (referencia instanceof FincaCatastro
					&& ((FincaCatastro) referencia).getLstConstrucciones() != null) {
				Iterator itCons = ((FincaCatastro) referencia)
						.getLstConstrucciones().iterator();
				while (itCons.hasNext()) {
					locales.add(((ConstruccionCatastro) itCons.next()));
				}
			} else if (referencia instanceof BienInmuebleCatastro
					&& ((BienInmuebleCatastro) referencia)
							.getLstConstrucciones() != null) {
				Iterator itCons = ((BienInmuebleCatastro) referencia)
						.getLstConstrucciones().iterator();
				while (itCons.hasNext()) {
					locales.add(((ConstruccionCatastro) itCons.next()));
				}
			}
		}

		loadLocales(locales);

		return;
	}
*/
/*	private void loadCultivos(Expediente expediente) {

		ArrayList cultivos = new ArrayList();

		Iterator itRef = expediente.getListaReferencias().iterator();
		while (itRef.hasNext()) {
			Object referencia = (Object) itRef.next();
			if (referencia instanceof FincaCatastro
					&& ((FincaCatastro) referencia).getLstCultivos() != null) {
				Iterator itCult = ((FincaCatastro) referencia).getLstCultivos()
						.iterator();
				while (itCult.hasNext()) {
					cultivos.add(((Cultivo) itCult.next()));
				}
			} else if (referencia instanceof BienInmuebleCatastro
					&& ((BienInmuebleCatastro) referencia).getLstCultivos() != null) {
				Iterator itCult = ((BienInmuebleCatastro) referencia)
						.getLstCultivos().iterator();
				while (itCult.hasNext()) {
					cultivos.add(((Cultivo) itCult.next()));
				}
			}
		}

		loadCultivos(cultivos);

		return;
	}
*/
	private void loadComunidadBienes(Titular titular, BienInmuebleJuridico bij) {

		if (bij.getLstComBienes() != null) {
			Iterator itComBienes = bij.getLstComBienes().iterator();
			while (itComBienes.hasNext()) {
				ComunidadBienes cb = (ComunidadBienes) itComBienes.next();

				if (cb.getNif() != null) {
					if (cb.getNif().equals(titular.getNifCb())) {
						parcelaPanel.loadData(cb);
					}
				}
			}
		}
	}

	private void clearTitulares() {

		if (parcelaPanel.getJScrollPaneTitular().getParent() != null) {
			parcelaPanel.getJPanelTitular().remove(
					parcelaPanel.getJScrollPaneTitular());
			parcelaPanel.getJPanelTitular().add(
					parcelaPanel.getJPanelDatosTitular(),
					new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosTitular());
			parcelaPanel.updateUI();
		}
	}

	private void loadTitulares() {
		if (parcelaPanel.getJScrollPaneTitular().getParent() != null) {
			parcelaPanel.getJPanelTitular().remove(
					parcelaPanel.getJScrollPaneTitular());
			parcelaPanel.getJPanelTitular().add(
					parcelaPanel.getJPanelDatosTitular(),
					new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
		}

		parcelaPanel.updateUI();

	}

	private void loadCultivos(BienInmuebleJuridico bij) {
		if (parcelaPanel.getJPanelCultivo().getParent() != null) {
			if (bij.getBienInmueble().getLstCultivos() != null) {
				ArrayList lstCult = new ArrayList();
				lstCult.addAll(bij.getBienInmueble().getLstCultivos());

				if (lstCult.size() > 1) {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJPanelDatosCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJScrollPaneCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 50));

					parcelaPanel.loadTableCultivo(lstCult);
					parcelaPanel.limpiaTablaCultivo();
					parcelaPanel.updateUI();
				} else if (!lstCult.isEmpty()) {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJPanelDatosCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJPanelDatosCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					parcelaPanel.loadData((Cultivo) lstCult.iterator().next());
					parcelaPanel.updateUI();
				} else {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJScrollPaneCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJPanelDatosCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					EdicionUtils.clearPanel(parcelaPanel
							.getJPanelDatosCultivo());
					parcelaPanel.updateUI();
				}
			}
		}
	}

	private void loadCultivos(FincaCatastro fc) {
		if (parcelaPanel.getJPanelCultivo().getParent() != null) {
			if (fc.getLstCultivos() != null) {
				ArrayList lstCult = new ArrayList();
				lstCult.addAll(fc.getLstCultivos());

				if (lstCult.size() > 1) {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJPanelDatosCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJScrollPaneCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 50));

					parcelaPanel.loadTableCultivo(lstCult);
					parcelaPanel.limpiaTablaCultivo();
					parcelaPanel.updateUI();
				} else if (!lstCult.isEmpty()) {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJPanelDatosCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJPanelDatosCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					parcelaPanel.loadData((Cultivo) lstCult.iterator().next());
					parcelaPanel.updateUI();
				} else {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJScrollPaneCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJPanelDatosCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					EdicionUtils.clearPanel(parcelaPanel
							.getJPanelDatosCultivo());
					parcelaPanel.updateUI();
				}
			} else {
				parcelaPanel.getJPanelCultivo().remove(
						parcelaPanel.getJScrollPaneCultivo());
				parcelaPanel.getJPanelCultivo().add(
						parcelaPanel.getJPanelDatosCultivo(),
						new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 5,
										0, 5), 0, 0));
				EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosCultivo());
				parcelaPanel.updateUI();
			}
		}
	}

	private void loadCultivos(ArrayList lstCult) {
		if (parcelaPanel.getJPanelCultivo().getParent() != null) {
			if (lstCult != null) {

				if (lstCult.size() > 1) {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJPanelDatosCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJScrollPaneCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 50));

					parcelaPanel.loadTableCultivo(lstCult);
					parcelaPanel.limpiaTablaCultivo();
					parcelaPanel.updateUI();
				}

				else {
					parcelaPanel.getJPanelCultivo().remove(
							parcelaPanel.getJScrollPaneCultivo());
					parcelaPanel.getJPanelCultivo().add(
							parcelaPanel.getJPanelDatosCultivo(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					EdicionUtils.clearPanel(parcelaPanel
							.getJPanelDatosCultivo());
					parcelaPanel.updateUI();
				}
			}
		}
	}

	private void loadLocales(BienInmuebleJuridico bij) {

		if (parcelaPanel.getJPanelLocal().getParent() != null) {
			if (bij.getBienInmueble().getLstConstrucciones() != null) {
				ArrayList lstConst = new ArrayList();
				lstConst.addAll(bij.getBienInmueble().getLstConstrucciones());

				if (lstConst.size() > 1) {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJPanelDatosLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJScrollPaneLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 50));

					parcelaPanel.loadTableLocal(lstConst);
					parcelaPanel.limpiaTablaLocal();
					parcelaPanel.updateUI();
				} else if (!lstConst.isEmpty()) {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJScrollPaneLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJPanelDatosLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					parcelaPanel.loadData((ConstruccionCatastro) lstConst
							.iterator().next());

					parcelaPanel.updateUI();
				} else {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJScrollPaneLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJPanelDatosLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosLocal());
					parcelaPanel.updateUI();
				}
			}
		}

	}

	private void loadLocales(FincaCatastro fc) {

		if (parcelaPanel.getJPanelLocal().getParent() != null) {
			if (fc.getLstConstrucciones() != null) {
				ArrayList lstConst = new ArrayList();
				lstConst.addAll(fc.getLstConstrucciones());

				if (lstConst.size() > 1) {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJPanelDatosLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJScrollPaneLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 50));

					parcelaPanel.loadTableLocal(lstConst);
					parcelaPanel.limpiaTablaLocal();
					parcelaPanel.updateUI();
				} else if (!lstConst.isEmpty()) {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJScrollPaneLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJPanelDatosLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					parcelaPanel.loadData((ConstruccionCatastro) lstConst
							.iterator().next());

					parcelaPanel.updateUI();
				} else {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJScrollPaneLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJPanelDatosLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosLocal());
					parcelaPanel.updateUI();
				}
			} else {
				parcelaPanel.getJPanelLocal().remove(
						parcelaPanel.getJScrollPaneLocal());
				parcelaPanel.getJPanelLocal().add(
						parcelaPanel.getJPanelDatosLocal(),
						new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 5,
										0, 5), 0, 0));
				EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosLocal());
				parcelaPanel.updateUI();
			}
		}

	}

	private void loadLocales(ArrayList lstConst) {

		if (parcelaPanel.getJPanelLocal().getParent() != null) {
			if (lstConst != null) {

				if (lstConst.size() > 1) {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJPanelDatosLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJScrollPaneLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 50));

					parcelaPanel.loadTableLocal(lstConst);
					parcelaPanel.limpiaTablaLocal();
					parcelaPanel.updateUI();
				} else {
					parcelaPanel.getJPanelLocal().remove(
							parcelaPanel.getJScrollPaneLocal());
					parcelaPanel.getJPanelLocal().add(
							parcelaPanel.getJPanelDatosLocal(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 5, 0, 5), 0, 0));
					EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosLocal());
					parcelaPanel.updateUI();
				}
			}
		}

	}

	private void loadLocales(ConstruccionCatastro cc) {

		parcelaPanel.getJPanelLocal()
				.remove(parcelaPanel.getJScrollPaneLocal());
		parcelaPanel.getJPanelLocal().add(
				parcelaPanel.getJPanelDatosLocal(),
				new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
						0, 0));
		parcelaPanel.loadData(cc);
		parcelaPanel.updateUI();

	}

	private void loadCultivos(Cultivo c) {

		parcelaPanel.getJPanelCultivo().remove(
				parcelaPanel.getJScrollPaneCultivo());
		parcelaPanel.getJPanelCultivo().add(
				parcelaPanel.getJPanelDatosCultivo(),
				new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
						0, 0));
		parcelaPanel.loadData(c);
		parcelaPanel.updateUI();

	}

	private void loadBienes(FincaCatastro fc) {
		ArrayList lstTit = new ArrayList();

		if (fc.getLstBienesInmuebles() != null) {
			Iterator itBienes = fc.getLstBienesInmuebles().iterator();
			while (itBienes.hasNext()) {
				BienInmuebleJuridico bij = (BienInmuebleJuridico) itBienes
						.next();
				lstTit.addAll(bij.getLstTitulares());
			}

			if (lstTit.size() > 1) {
				parcelaPanel.getJPanelTitular().remove(
						parcelaPanel.getJPanelDatosTitular());
				parcelaPanel.getJPanelTitular().add(
						parcelaPanel.getJScrollPaneTitular(),
						new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 5,
										0, 5), 0, 65));

				parcelaPanel.loadTableTitular(lstTit);
				parcelaPanel.limpiaTablaTitular();
				parcelaPanel.updateUI();
			} else if (!lstTit.isEmpty()) {
				parcelaPanel.getJPanelTitular().remove(
						parcelaPanel.getJScrollPaneTitular());
				parcelaPanel.getJPanelTitular().add(
						parcelaPanel.getJPanelDatosTitular(),
						new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 5,
										0, 5), 0, 0));
				parcelaPanel.loadData((Titular) lstTit.iterator().next());
				parcelaPanel.updateUI();
			} else {
				parcelaPanel.getJPanelTitular().remove(
						parcelaPanel.getJScrollPaneTitular());
				parcelaPanel.getJPanelTitular().add(
						parcelaPanel.getJPanelDatosTitular(),
						new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0, 5,
										0, 5), 0, 0));
				EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosTitular());
				parcelaPanel.updateUI();
			}
		} else {
			parcelaPanel.getJPanelTitular().remove(
					parcelaPanel.getJScrollPaneTitular());
			parcelaPanel.getJPanelTitular().add(
					parcelaPanel.getJPanelDatosTitular(),
					new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosTitular());
			parcelaPanel.updateUI();
		}
	}

	private void loadBienes(BienInmuebleJuridico bij) {
		ArrayList lstTit = new ArrayList();

		if (bij.getLstTitulares() == null)
			bij.setLstTitulares(new ArrayList());
		lstTit.addAll(bij.getLstTitulares());

		if (lstTit.size() > 1) {
			parcelaPanel.getJPanelTitular().remove(
					parcelaPanel.getJPanelDatosTitular());
			parcelaPanel.getJPanelTitular().add(
					parcelaPanel.getJScrollPaneTitular(),
					new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 65));

			parcelaPanel.loadTableTitular(lstTit);
			parcelaPanel.limpiaTablaTitular();
			parcelaPanel.updateUI();
		} else {
			parcelaPanel.getJPanelTitular().remove(
					parcelaPanel.getJScrollPaneTitular());
			parcelaPanel.getJPanelTitular().add(
					parcelaPanel.getJPanelDatosTitular(),
					new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			if (lstTit.size() == 1) {
				parcelaPanel.loadData((Titular) lstTit.iterator().next());
			} else {
				parcelaPanel.loadData(new Titular());
			}
			parcelaPanel.updateUI();
		}
	}

	private void loadFinca(FincaCatastro fc) {
		if ((fc.getLstBienesInmuebles() != null)) {
			if (fc.getLstBienesInmuebles().size() > 1) {
				parcelaPanel.getJPanelBienInmuebleGlobal().remove(
						parcelaPanel.getJPanelDatosBienInmueble());
				parcelaPanel.getJPanelBienInmuebleGlobal().add(
						parcelaPanel.getJScrollPaneBienInmueble(),
						new GridBagConstraints(0, 1, 4, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5, 5,
										5, 5), 0, 65));
				parcelaPanel.updateUI();
				ArrayList lstBI = new ArrayList();
				Iterator itBIJ = fc.getLstBienesInmuebles().iterator();
				while (itBIJ.hasNext())
					lstBI.add(((BienInmuebleJuridico) itBIJ.next())
							.getBienInmueble());

				parcelaPanel.loadTableBienes(lstBI);
				parcelaPanel.limpiaTablaBI();
				parcelaPanel.updateUI();
			} else if (!fc.getLstBienesInmuebles().isEmpty()) {
				parcelaPanel.getJPanelBienInmuebleGlobal().remove(
						parcelaPanel.getJScrollPaneBienInmueble());
				parcelaPanel.getJPanelBienInmuebleGlobal().add(
						parcelaPanel.getJPanelDatosBienInmueble(),
						new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5, 5,
										5, 5), 0, 40));
				parcelaPanel.loadData((BienInmuebleJuridico) fc
						.getLstBienesInmuebles().iterator().next());
				parcelaPanel.updateUI();
			} else {
				parcelaPanel.getJPanelBienInmuebleGlobal().remove(
						parcelaPanel.getJScrollPaneBienInmueble());
				parcelaPanel.getJPanelBienInmuebleGlobal().add(
						parcelaPanel.getJPanelDatosBienInmueble(),
						new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5, 5,
										5, 5), 0, 40));
				EdicionUtils.clearPanel(parcelaPanel
						.getJPanelDatosBienInmueble());
				parcelaPanel.updateUI();
			}
		} else {
			parcelaPanel.getJPanelBienInmuebleGlobal().remove(
					parcelaPanel.getJScrollPaneBienInmueble());
			parcelaPanel.getJPanelBienInmuebleGlobal().add(
					parcelaPanel.getJPanelDatosBienInmueble(),
					new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 40));
			EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosBienInmueble());
			parcelaPanel.updateUI();
		}
	}

	private void loadBienes(ArrayList lstBienes) {
		if (lstBienes.size() > 1) {
			parcelaPanel.getJPanelBienInmuebleGlobal().remove(
					parcelaPanel.getJPanelDatosBienInmueble());
			parcelaPanel.getJPanelBienInmuebleGlobal().add(
					parcelaPanel.getJScrollPaneBienInmueble(),
					new GridBagConstraints(0, 1, 4, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 65));
			parcelaPanel.updateUI();
			parcelaPanel.loadTableBienes(lstBienes);
			parcelaPanel.limpiaTablaBI();
			parcelaPanel.updateUI();
		} else {
			parcelaPanel.getJPanelBienInmuebleGlobal().remove(
					parcelaPanel.getJScrollPaneBienInmueble());
			parcelaPanel.getJPanelBienInmuebleGlobal().add(
					parcelaPanel.getJPanelDatosBienInmueble(),
					new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 40));
			EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosBienInmueble());
			parcelaPanel.updateUI();
		}
	}

	/*private void loadExpediente(Expediente exp) {
		if (exp.getListaReferencias() != null) {
			if (!exp.getListaReferencias().isEmpty()) {
				if (exp.getListaReferencias().iterator().next() instanceof FincaCatastro) {
					if ((exp.getListaReferencias() != null)
							&& (exp.getListaReferencias().size() > 1)) {
						parcelaPanel.getJPanelFinca().remove(
								parcelaPanel.getJPanelDatosFinca());
						parcelaPanel.getJPanelFinca().add(
								parcelaPanel.getJScrollPaneFincas(),
								new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
										GridBagConstraints.NORTH,
										GridBagConstraints.HORIZONTAL,
										new Insets(5, 5, 5, 5), 0, 65));
						parcelaPanel.updateUI();
						ArrayList lstFinc = new ArrayList();
						Iterator itFinc = exp.getListaReferencias().iterator();
						while (itFinc.hasNext())
							lstFinc.add(((FincaCatastro) itFinc.next()));

						parcelaPanel.loadTableFinca(lstFinc);
						parcelaPanel.limpiaTablaFinca();
						parcelaPanel.updateUI();
					} else {
						parcelaPanel.getJPanelFinca().remove(
								parcelaPanel.getJScrollPaneFincas());
						parcelaPanel.getJPanelFinca().add(
								parcelaPanel.getJPanelDatosFinca(),
								new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
										GridBagConstraints.NORTH,
										GridBagConstraints.HORIZONTAL,
										new Insets(5, 5, 5, 5), 0, 40));
						parcelaPanel.updateUI();
					}
				}
			}
		}
	}
*/
	private void loadFincaCatastro(ArrayList lstExp) {
		if (lstExp != null) {
			if (lstExp.iterator().next() instanceof FincaCatastro) {
				if ((lstExp.size() > 1)) {
					parcelaPanel.getJPanelFinca().remove(
							parcelaPanel.getJPanelDatosFinca());
					parcelaPanel.getJPanelFinca().add(
							parcelaPanel.getJScrollPaneFincas(),
							new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											5, 5, 5, 5), 0, 65));
					parcelaPanel.updateUI();
					ArrayList lstFinc = new ArrayList();
					Iterator itFinc = lstExp.iterator();
					while (itFinc.hasNext())
						lstFinc.add(((FincaCatastro) itFinc.next()));

					parcelaPanel.loadTableFinca(lstFinc);
					parcelaPanel.limpiaTablaFinca();
					parcelaPanel.updateUI();
				} else {
					parcelaPanel.getJPanelFinca().remove(
							parcelaPanel.getJScrollPaneFincas());
					parcelaPanel.getJPanelFinca().add(
							parcelaPanel.getJPanelDatosFinca(),
							new GridBagConstraints(0, 0, 6, 1, 0.1, 0.2,
									GridBagConstraints.NORTH,
									GridBagConstraints.HORIZONTAL, new Insets(
											5, 5, 5, 5), 0, 40));
					EdicionUtils.clearPanel(parcelaPanel.getJPanelDatosFinca());
					parcelaPanel.updateUI();
				}
			}
		}
	}

	private void loadFinca() {
		if (parcelaPanel.getJScrollPaneBienInmueble().getParent() != null) {
			parcelaPanel.getJPanelBienInmuebleGlobal().remove(
					parcelaPanel.getJScrollPaneBienInmueble());
			parcelaPanel.getJPanelBienInmuebleGlobal().add(
					parcelaPanel.getJPanelDatosBienInmueble(),
					new GridBagConstraints(0, 1, 4, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 40));
		}

		parcelaPanel.updateUI();

	}

	private void loadExpediente() {
		if (parcelaPanel.getJScrollPaneFincas().getParent() != null) {
			parcelaPanel.getJPanelFinca().remove(
					parcelaPanel.getJScrollPaneFincas());
			parcelaPanel.getJPanelFinca().add(
					parcelaPanel.getJPanelDatosFinca(),
					new GridBagConstraints(0, 1, 6, 1, 0.1, 0.2,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 40));
		}

		parcelaPanel.updateUI();

	}

	/**
	 * @param expediente
	 *            The expediente to set.
	 */
	/*public void setExpediente(Expediente expediente) {
		this.expediente = expediente;

		// Actualiza el expediente mostrado en el árbol
		if (jScrollPane != null)
			this.remove(jScrollPane);

		this.add(getJScrollPane(), new GridBagConstraints(0, 0, 1, 1, 0.1, 0.9,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						10, 10, 0, 10), 80, 80));
	}*/

	private void recopilarDatos(DefaultMutableTreeNode node) {

		if (node != null) {
			Object nodeInfo = node.getUserObject();
			if (nodeInfo instanceof FincaCatastro) {
				// Recogemos los datos del panel de la finca
				// Guardamos en el objeto fincaCatastro la finca que estamos
				// modificando
				parcela = parcelaPanel.getFincaCatastro();

				if (parcela != null) {
					parcelaPanel.recopilarDatosPanelFinca();
				}
			}
			if (nodeInfo instanceof BienInmuebleJuridico) {
				bijPanel = parcelaPanel.recopilarDatosPanelBienInmueble();
			}
			if (nodeInfo instanceof UnidadConstructivaCatastro) {
				uc = ucPanel.getUnidadConstructiva();
			}
			if (nodeInfo instanceof SueloCatastro) {
				DefaultMutableTreeNode nodeParent = (DefaultMutableTreeNode) node
						.getParent();
				parcela = (FincaCatastro) nodeParent.getUserObject();
				// Recogemos los datos del panel del suelo
				suelo = sueloPanel.getSuelo();
			}
			if (nodeInfo instanceof Cultivo) {
				parcelaPanel.recopilarDatosPanelCultivo();
			}
			if (nodeInfo instanceof ConstruccionCatastro) {
				parcelaPanel.recopilarDatosPanelConstruccion();
			}
			if (nodeInfo instanceof Titular) {
				parcelaPanel.recopilarDatosPanelTitular();
			}

		}

	}

	public DefaultMutableTreeNode getPreNodeInfo() {
		return preNodeInfo;
	}

	public void setPreNodeInfo(DefaultMutableTreeNode preNodeInfo) {
		this.preNodeInfo = preNodeInfo;
	}


	private void actualizaArbol(String nodo) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		Object nodeInfo = node.getUserObject();
		if (nodo.equalsIgnoreCase("UC")) {
			if (!(nodeInfo instanceof UnidadConstructivaCatastro)) {
				if (nodeInfo instanceof BienInmuebleJuridico
						|| nodeInfo instanceof SueloCatastro
						|| (nodeInfo instanceof ConstruccionCatastro
								&& blackboard.get("Comun") != null && blackboard
								.getBoolean("Comun"))
						|| (nodeInfo instanceof Cultivo
								&& blackboard.get("Comun") != null && blackboard
								.getBoolean("Comun"))) {
					node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getParentPath().getLastPathComponent();
				} else if (nodeInfo instanceof Titular
						|| nodeInfo instanceof Cultivo
						|| nodeInfo instanceof ConstruccionCatastro) {
					node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getParentPath().getParentPath()
							.getLastPathComponent();
				}
				boolean continua = true;
				for (Enumeration e2 = node.children(); e2.hasMoreElements()
						&& continua;) {
					HideableNode n2 = (HideableNode) e2.nextElement();
					if (n2.getUserObject() instanceof UnidadConstructivaCatastro) {
						TreePath path = new TreePath(n2.getPath());
						tree.setSelectionPath(path);
						continua = false;
					}
				}
				if (continua) {
					TreePath path = new TreePath(node.getPath());
					tree.setSelectionPath(path);
				}
			}
		} else if (nodo.equalsIgnoreCase("Suelo")) {
			if (!(nodeInfo instanceof SueloCatastro)) {
				if (nodeInfo instanceof BienInmuebleJuridico
						|| nodeInfo instanceof UnidadConstructivaCatastro
						|| (nodeInfo instanceof ConstruccionCatastro
								&& blackboard.get("Comun") != null && blackboard
								.getBoolean("Comun"))
						|| (nodeInfo instanceof Cultivo
								&& blackboard.get("Comun") != null && blackboard
								.getBoolean("Comun"))) {
					node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getParentPath().getLastPathComponent();
				} else if (nodeInfo instanceof Titular
						|| nodeInfo instanceof Cultivo
						|| nodeInfo instanceof ConstruccionCatastro) {
					node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getParentPath().getParentPath()
							.getLastPathComponent();
				}
				boolean continua = true;
				for (Enumeration e2 = node.children(); e2.hasMoreElements()
						&& continua;) {
					HideableNode n2 = (HideableNode) e2.nextElement();
					if (n2.getUserObject() instanceof SueloCatastro) {
						TreePath path = new TreePath(n2.getPath());
						tree.setSelectionPath(path);
						continua = false;
					}
				}
				if (continua) {
					TreePath path = new TreePath(node.getPath());
					tree.setSelectionPath(path);
				}
			}
		}
	}

	public void cleanup() {

		if (parcelaPanel != null) {
			parcelaPanel.cleanup();
		}
		if (sueloPanel != null) {
			sueloPanel.cleanup();
		}
		if (ucPanel != null) {
			ucPanel.cleanup();
		}
		if (fxccPanel != null) {
			fxccPanel.cleanup();
		}
		if (ascPanel != null) {
			ascPanel.cleanup();
		}
		if (repartoPanel != null) {
			repartoPanel.cleanup();
		}
		if (imagenCatastroPanel != null) {
			imagenCatastroPanel.cleanup();
		}

	}

	/**
	 * Obtengo la información sobre las ponencias de tramos y la zona de valor
	 */
	private void obtenerInformacionPonencias(){
    	try {
	    	if (parcela.getLstSuelos() != null){
		    	Iterator it = parcela.getLstSuelos().iterator();
		    	while (it.hasNext()){
		    		SueloCatastro suelo = (SueloCatastro)it.next();
		            if (suelo.getDatosEconomicos().getZonaValor()!= null && suelo.getDatosEconomicos().getZonaValor().getCodZonaValor() != null &&
		            		!suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().equals("")){
						PonenciaZonaValor ponenciaZonaValor = ConstantesRegExp.clienteCatastro.obtenerPonenciaZonaValor(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor());
						suelo.getDatosEconomicos().setZonaValor(ponenciaZonaValor);
							
		            }            
		            if (suelo.getDatosEconomicos().getTramos() != null && suelo.getDatosEconomicos().getTramos().getCodTramo() != null &&
		            		!suelo.getDatosEconomicos().getTramos().getCodTramo().equals("")){
		            	
						PonenciaTramos tramos = ConstantesRegExp.clienteCatastro.obtenerPonenciaTramos(suelo.getDatosEconomicos().getTramos().getCodTramo());
						suelo.getDatosEconomicos().setTramos(tramos);
		            }
		            if (suelo.getDatosEconomicos().getZonaUrbanistica() != null && suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona() != null &&
		            		!suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona().equals("")){
		            	
						PonenciaUrbanistica zonaUrbanistica = ConstantesRegExp.clienteCatastro.obtenerPonenciaUrbanistica(suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona());
						suelo.getDatosEconomicos().setZonaUrbanistica(zonaUrbanistica);
		            }
		    	}
	    	}
	    	if (parcela.getLstUnidadesConstructivas() != null){
		    	Iterator it = parcela.getLstUnidadesConstructivas().iterator();
		    	while (it.hasNext()){
		            UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro)it.next();
		            if (uc.getDatosEconomicos().getZonaValor() != null && uc.getDatosEconomicos().getZonaValor().getCodZonaValor() != null &&
		            		!uc.getDatosEconomicos().getZonaValor().getCodZonaValor().equals("")){
		            		
						PonenciaZonaValor ponenciaZonaValor = ConstantesRegExp.clienteCatastro.obtenerPonenciaZonaValor(uc.getDatosEconomicos().getZonaValor().getCodZonaValor());
						uc.getDatosEconomicos().setZonaValor(ponenciaZonaValor);
		            }            
		            if (uc.getDatosEconomicos().getTramoPonencia()!= null && uc.getDatosEconomicos().getTramoPonencia().getCodTramo() != null &&
		            		!uc.getDatosEconomicos().getTramoPonencia().getCodTramo().equals("")){
		            	
						PonenciaTramos tramos = ConstantesRegExp.clienteCatastro.obtenerPonenciaTramos(uc.getDatosEconomicos().getTramoPonencia().getCodTramo());
						uc.getDatosEconomicos().setTramoPonencia(tramos);
		            }
		    	}
	    	}
    	} catch (Exception ex) {					
	        JOptionPane.showMessageDialog(this,ex.getCause().getMessage());
		}
    }
	
	public ArrayList getListReferencias() {
		return listReferencias;
	}

	public void setListReferencias(ArrayList listReferencias) {
		this.listReferencias = listReferencias;
	}


} // @jve:decl-index=0:visual-constraint="5,4"
