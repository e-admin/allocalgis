/**
 * GestionExpedientePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JPopupMenu;
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
import com.geopista.app.catastro.intercambio.edicion.ExpedientePanelTree;
import com.geopista.app.catastro.intercambio.edicion.FechaAlteracionDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableTreeModel;
import com.geopista.app.catastro.intercambio.edicion.validacion.Validacion;
import com.geopista.app.catastro.intercambio.edicion.validacion.dialogs.ValidacionesJDialog;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.ASCCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
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
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GestionExpedientePanel extends JPanel implements
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

	
	private ExpedientePanelTree jPanelExpedientes = null;
	private JTabbedPane jTabbedPaneExpediente;

	private JTree tree = null;


	private HideableTreeModel model = null;

	private Expediente expediente = null;
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
	private ExpedientePanel expedientePanel;
	private ImagenCatastroPanel imagenCatastroPanel = null;

	private RepartoPanel repartoPanel;
	private final RepartoCatastro reparto = null;

	private ArrayList lstParcelasEditadas;

	private boolean isEditable = false;

	private boolean ultimoEsLocal = true;
	private boolean cambioPestanaManual = false;
	private boolean cambioPestanaAutomatico = false;
	private boolean evaluandoDatosCorrectos = true;
	private boolean evaluandoDatosCorrectosTotales = true;
	private boolean errorEncontrado = false;

	// Nodo seleccionado en el arbol para recoger sus datos.
	private DefaultMutableTreeNode preNodeInfo;
	private Object nodoAPegar = null;
	private JMenuItem copiar;
	private JMenuItem pegar;
	private JButton jButtonEstadoModificador = null;

	boolean mostrarPantallaValidaciones = true;
	/**
	 * This method initializes
	 * 
	 */
	public GestionExpedientePanel(Expediente expediente) {
		this(expediente, false);

	}

	public GestionExpedientePanel(Expediente expediente, boolean isEditable) {
		super();
		setName("GestionExpedientePanel");
		this.expediente = expediente;
		this.isEditable = isEditable;
		initialize();
			

	}

	public GestionExpedientePanel() {
		this(null);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		Locale loc = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle
				.getBundle(
						"com.geopista.app.catastro.intercambio.language.Expedientesi18n",
						loc, this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("Expedientes", bundle);

		if(this.expediente.isTipoTramitaExpSitFinales()){
			aplicacion.getBlackboard().put("expSitFinales", true);
		}
		else{
			aplicacion.getBlackboard().put("expSitFinales", false);
		}
		
		EdicionOperations oper = new EdicionOperations();
		parcelaPanel = new FincaPanel(expediente.getTipoExpediente(), this);
		sueloPanel = new SueloPanel(this);
		ucPanel = new UnidadConstructivaPanel(this);
		fxccPanel = new FxccPanel(this);
		ascPanel = new ASCPanel(this);
		ascPanel.setFxccPanel(fxccPanel);
		imagenCatastroPanel = new ImagenCatastroPanel(this);

		this.setPreferredSize(new java.awt.Dimension(800, 575));
		this.setLayout(new GridBagLayout());

		jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getJPanelLeftPosition(), getJTabbedPaneExpediente());

		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setDividerLocation(175);
		jSplitPane.setDividerSize(10);

		this.add(jSplitPane, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Listener de los cambios en el arbol
		getJPanelExpedientes().getTree().addTreeSelectionListener(this);

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

		lstParcelasEditadas = jPanelExpedientes.getLstReferencias();
		initilizeExpediente(lstParcelasEditadas);

		// Si es consulta no se permitirá modificar los campos
		EdicionUtils.enablePanel(parcelaPanel, false);
		parcelaPanel.setEditable(false);
		parcelaPanel.setPanelExpedientes(jPanelExpedientes);
		ucPanel.asignaArbol(jPanelExpedientes);
		sueloPanel.asignaArbol(jPanelExpedientes);
		ascPanel.loadExpedienteData(this.getExpediente());
		asociarBotonDerecho();

		EdicionUtils.disabledButtons(parcelaPanel, "_borrar");
		EdicionUtils.disabledButtons(parcelaPanel, "vista");
		
	
		
		parcelaPanel.getjButtonActualizar().addActionListener(new java.awt.event.ActionListener()
        {
			 public void actionPerformed(java.awt.event.ActionEvent e)
			 {
					 
				 final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							AppContext.getApplicationContext().getMainFrame(),
							null);

					progressDialog.setTitle("TaskMonitorDialog.Wait");
					progressDialog.report(I18N.get("Expedientes",
											"Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.msgGuardando"));
					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {
							new Thread(new Runnable() {
								public void run() {
									try {
							
				
										Expediente expAux = new Expediente();
										expAux.setIdMunicipio(expediente.getIdMunicipio());
										expAux.setIdExpediente(expediente.getIdExpediente());
										expAux.setTipoExpediente(expediente.getTipoExpediente());
										expAux.setDireccionPresentador(expediente.getDireccionPresentador());
										expAux.setTipoTramitaExpSitFinales(expediente.isTipoTramitaExpSitFinales());
										
										if(expediente.getTipoExpediente().getConvenio().equals(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)){
											FincaCatastro fincaEditada = parcelaPanel.getFincaEditada();
											fincaEditada.setActualizadoOVC(true);
											expAux.getListaReferencias().add(fincaEditada);
											
											try {
												Expediente expe = ConstantesRegExp.clienteCatastro.actualizaCatastroTemporal(expAux, expAux.getTipoExpediente().getConvenio());
												
												for (int i=0; i<expediente.getListaReferencias().size(); i++){
													if(expediente.getListaReferencias().get(i) instanceof FincaCatastro){
														if(((FincaCatastro)expediente.getListaReferencias().get(i)).getRefFinca().getRefCatastral().
																equals(fincaEditada.getRefFinca().getRefCatastral())){
															expediente.getListaReferencias().set(i, fincaEditada);
															break;
														}
															
													}
												}

												lstParcelasEditadas = expediente.getListaReferencias();
												initilizeExpediente(lstParcelasEditadas);
												TreePath path = tree.getSelectionPath();

												tree.collapsePath(tree.getSelectionPath());
												
												
												FincaCatastro finc = (FincaCatastro)expe.getListaReferencias().get(0);
												
												DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree	.getLastSelectedPathComponent();
												node.removeAllChildren();

												parcela = getJPanelExpedientes().fillObjectActualiza(node,finc );
												obtenerInformacionPonencias();
												tree.expandPath(tree.getSelectionPath());

												// Sustituye el nodo finca por el relleno con los datos de la misma
												if (parcela != null)
													((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(parcela);
												
												tree.repaint();
												
												model.reload();
												
												
												
												
												
//												DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
//												borrarChildNodos(root, fincaEditada.getRefFinca().getRefCatastral());
//
//												// Sustituye el nodo finca por el relleno con los datos de la misma
//												if (parcela != null)
//													((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(parcela);
//												tree.repaint();
//												
//												model.reload();
	//
												
												for (int j=0; j<jPanelExpedientes.getReferenciasTraidas().length; j++){
													jPanelExpedientes.getReferenciasTraidas()[j] = false;
												}
												parcelaPanel.getjButtonActualizar().setEnabled(false);
												tree.setSelectionPath(path);
												
												
											} catch (Exception e1) {
												System.out.println("Se ha producido un error modificando el expediente "
																+ e1.getMessage()+ e1);
												e1.printStackTrace();
											}
										}
										else if(expediente.getTipoExpediente().getConvenio().equals(DatosConfiguracion.CONVENIO_TITULARIDAD)){
											BienInmuebleCatastro bien = parcelaPanel.getBienInmueble().getBienInmueble();
											bien.setActualizadoOVC(true);
											expAux.getListaReferencias().add(bien);
											
											
											try {
												Expediente expe = ConstantesRegExp.clienteCatastro.actualizaCatastroTemporal(expAux, expAux.getTipoExpediente().getConvenio());
												
												for (int i=0; i<expediente.getListaReferencias().size(); i++){
													if(expediente.getListaReferencias().get(i) instanceof BienInmuebleJuridico){
														if(((BienInmuebleJuridico)expediente.getListaReferencias().get(i)).getBienInmueble().getIdBienInmueble().getIdBienInmueble().
																equals(bien.getIdBienInmueble().getIdBienInmueble())){
															expediente.getListaReferencias().set(i, bien);
															break;
														}
															
													}
												}

												lstParcelasEditadas = expediente.getListaReferencias();
												initilizeExpediente(lstParcelasEditadas);
												TreePath path = tree.getSelectionPath();
												
												
												tree.collapsePath(tree.getSelectionPath());
												
												BienInmuebleJuridico bienInmueble = (BienInmuebleJuridico)expe.getListaReferencias().get(0);
												
												DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree	.getLastSelectedPathComponent();
												node.removeAllChildren();
												
												bijPanel = getJPanelExpedientes().fillObjectBienInmuebleActualiza(node,bienInmueble );
												tree.expandPath(tree.getSelectionPath());
												
											
												// Sustituye el nodo bien inmueble por el relleno con los datos de
												// la misma
												if (bijPanel != null)
													((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(bijPanel);
												tree.repaint();
												model.reload();
												
												
												

//												DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
//												borrarChildNodos(root, bien.getIdBienInmueble().getIdBienInmueble());
//
//												
//												bijPanel.setBienInmueble(bien);
//												// Sustituye el nodo finca por el relleno con los datos de la misma
//												if (bijPanel != null)
//													((DefaultMutableTreeNode) tree.getLastSelectedPathComponent())
//															.setUserObject(bijPanel);
//										
//												tree.repaint();
//												
//												model.reload();
	//
												
												for (int j=0; j<jPanelExpedientes.getReferenciasTraidas().length; j++){
													jPanelExpedientes.getReferenciasTraidas()[j] = false;
												}
												parcelaPanel.getjButtonActualizar().setEnabled(false);
												tree.setSelectionPath(path);
												
												
											} catch (Exception e1) {
												System.out
														.println("Se ha producido un error modificando el expediente "
																+ e1.getMessage()
																+ e1);
												e1.printStackTrace();
											}
																					
										}

									} catch (Exception e) {
										e.printStackTrace();
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
       });
	}

	private void initilizeExpediente(ArrayList lstReferencias) {

		if ((lstReferencias.iterator() != null) && (lstReferencias.size() > 0)) {
			if (lstReferencias.iterator().next() instanceof FincaCatastro) {
				loadExpediente(lstReferencias);
			} else if (lstReferencias.iterator().next() instanceof BienInmuebleCatastro) {
				loadBienes(lstReferencias);
//				loadBienesAscPanel(lstReferencias);
			}
		}

	}

	public JPanel getJPanelBotones() {
		if (jPanelBotones == null) {
			jPanelBotones = new JPanel();
			jPanelBotones.setLayout(new GridBagLayout());
			jPanelBotones.add(getJButtonGuardar(), new GridBagConstraints(0, 0,
					1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			jPanelBotones.add(getJButtonValidar(), new GridBagConstraints(1, 0,
					1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			jPanelBotones.add(getJButtonGuardarFinalizar(),
					new GridBagConstraints(2, 0, 1, 1, 0, 0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));
			jPanelBotones.add(getJButtonEstadoModificado(),
					new GridBagConstraints(3, 0, 1, 1, 0, 0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));
		}

		return jPanelBotones;
	}

	/**
	 * Funcion que llama al ClienteCatastro (El objeto que realiza las
	 * peticiones a la base de datos) para actualizar los datos del expediente,
	 * que el usuario ha estado tratando, en base de datos.
	 */
	public Expediente updateExpedienteEnBD(Expediente expediente) {
		try {
			expediente = ConstantesRegExp.clienteCatastro
					.updateExpediente(expediente);
			return expediente;
		} catch (ACException e) {
			JOptionPane.showMessageDialog(this, e.getCause().getMessage());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private JButton getJButtonGuardarFinalizar() {
		if (jButtonGuardarFinalizar == null) {
			jButtonGuardarFinalizar = new JButton();
			jButtonGuardarFinalizar.setPreferredSize(new Dimension(150, 25));
			jButtonGuardarFinalizar.setText(I18N.get("Expedientes",
					"expedientes.boton.guardarfinalizar"));

			jButtonGuardarFinalizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							AppContext.getApplicationContext().getMainFrame(),
							null);

					progressDialog.setTitle("TaskMonitorDialog.Wait");
					progressDialog.report(
							I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.msgGuardandoYFinalizar"));
					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {
							new Thread(new Runnable() {
								public void run() {
									try {
										recopilarDatos((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent());

										FechaAlteracionDialog fechaAltDialog = 
											new FechaAlteracionDialog(aplicacion.getMainFrame(),expediente.getFechaRegistro());
										fechaAltDialog.setLocation(
														GestionExpedientePanel.this.getWidth()/ 2 - fechaAltDialog.getWidth()/ 2,
														GestionExpedientePanel.this.getHeight()/ 2- fechaAltDialog.getHeight()/ 2);
										fechaAltDialog.setResizable(false);
										fechaAltDialog.show();
										if (fechaAltDialog.getCancelar()) {
											return;
										}
										Date fechaAlt = fechaAltDialog
												.getFechatAlteracion();
										if (fechaAlt == null) {
											JOptionPane.showMessageDialog(GestionExpedientePanel.this,
															I18N.get("Expedientes","Error.J5"));
											return;
										}
										expediente.setFechaAlteracion(fechaAlt);

										// PREV-NOV
										// recopilarDatos((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
										if (jTabbedPaneExpediente.getSelectedComponent().equals(fxccPanel)) {
											FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
											if (fxcc == null) {
												fxcc = new FX_CC();
											}
											fxccPanel.guardarFXCC(fxcc);
											ascPanel.guardarASC(fxccPanel.textoAscTemp,fxcc,fxccPanel.geopistaEditor);

											parcelaPanel.getFincaCatastro().setFxcc(fxcc);
										}
										if (jTabbedPaneExpediente.getSelectedComponent().equals(ascPanel)) {
											FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
											if (fxcc == null) {
												fxcc = new FX_CC();
											}
											ascPanel.guardarASC(ascPanel.textoAscTemp,fxcc,fxccPanel.geopistaEditor);
											if (fxcc.getDXF() == null) {
												fxccPanel.guardarFXCCNoSeleccionado(fxcc);
											}
											parcelaPanel.getFincaCatastro().setFxcc(fxcc);
											
										

										}

										expediente.setIdEstado(
												ConstantesRegistroExp.maquinaEstadosFlujo.estadoSiguiente(expediente.getIdEstado()));

										expediente.setFechaMovimiento(new Date(System.currentTimeMillis()));
										SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
										String horaGene = horaFormat.format(new Time(System.currentTimeMillis()));
										expediente.setHoraMovimiento(horaGene);
										ArrayList lstRef = new ArrayList();
										lstRef.addAll(jPanelExpedientes.getFincasEnMemoria());
										lstRef.addAll(jPanelExpedientes.getLstReferenciasBorradas());
										expediente.setListaReferencias(lstRef);
										updateExpedienteEnBD(expediente);
										try {
											//ConstantesRegistroExp.clienteCatastro.modificarExpediente(expediente);
											ConstantesRegExp.clienteCatastro.modificarExpedienteVariaciones(expediente, true,
													(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales"));
											
											if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
												//expediente de variaciones
												if(jPanelExpedientes.getLstReferenciasEliminarCatastroTemporal() != null &&
														!jPanelExpedientes.getLstReferenciasEliminarCatastroTemporal().isEmpty()){
													//la finca se dio de alta y despues de baja, hay que eliminarla de catastro_temporal
													
													ConstantesRegExp.clienteCatastro.eliminarReferenciaCatastroTemoral(expediente,
																jPanelExpedientes.getLstReferenciasEliminarCatastroTemporal());
												}
											}
										} catch (Exception e1) {
											System.out.println("Se ha producido un error modificando el expediente "+ e1.getMessage()+ e1);
											e1.printStackTrace();
										}
										expediente.setListaReferencias(jPanelExpedientes.getLstReferencias());
										jPanelExpedientes.setLstReferenciasBorradas(new ArrayList());
										jButtonGuardar.setEnabled(false);
										jButtonGuardarFinalizar.setEnabled(false);
										jButtonValidar.setEnabled(false);
										jButtonEstadoModificador.setEnabled(true);
										parcelaPanel.getjButtonActualizar().setEnabled(false);
										deshabilitarTodosPaneles();

										
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										progressDialog.setVisible(false);
										progressDialog.dispose();
									}
								}
							}).start();
						}
					});
					GUIUtil.centreOnWindow(progressDialog);
					progressDialog.setVisible(true);
				}
			});
			jButtonGuardarFinalizar.setEnabled(isEditable);
		}
		return jButtonGuardarFinalizar;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJPanelExpedientes());

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

	private ExpedientePanelTree getJPanelExpedientes() {
		if (jPanelExpedientes == null) {
			jPanelExpedientes = new ExpedientePanelTree(
					TreeSelectionModel.SINGLE_TREE_SELECTION, expediente);
			tree = jPanelExpedientes.getTree();
			model = (HideableTreeModel) tree.getModel();
		}

		return jPanelExpedientes;
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
					I18N.get("Expedientes",
							"expedientes.panel.visualizar.titulo"),
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
			jCheckBoxUC.setText(I18N.get("Expedientes",
					"expedientes.panel.visualizar.unidadesconstructivas"));
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
	
	public StringBuffer validaDatosCorrectos(DefaultMutableTreeNode nodo) {
		StringBuffer sbVal = new StringBuffer();
		int n = nodo.getChildCount();
		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) nodo.getChildAt(i);
			//if (!errorEncontrado && datosMinimosYCorrectos(hijo)) {
			datosMinimosYCorrectosButtonValidar(hijo);
			validaDatosCorrectos(hijo);
			
			if(hijo.getUserObject() instanceof FincaCatastro){
			
				if (!evaluandoDatosCorrectos) {
					evaluandoDatosCorrectosTotales = false;
					evaluandoDatosCorrectos = true;
					
					
					sbVal.append(
							I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.referencia") + " " +
							((FincaCatastro)hijo.getUserObject()).getRefFinca().getRefCatastral()+"\n\r");
					if(!existeDatosMinimosBien){
				    	sbVal.append(valDatosMinimosBienSB.toString()+"\n\r");
				    }
					if(!existeDatosMinimosUC){
						sbVal.append(valDatosMinimosUCSB.toString()+"\n\r");  	
					}
					if(!existeDatosMinimosSuelo){
						sbVal.append(valDatosMinimosSueloSB.toString()+"\n\r");
					}
					if(!existeDatosMinimosCultivo){
						sbVal.append(valDatosMinimosCultivoSB.toString()+"\n\r");
					}
					if(!existeDatosMinimosConstruccion){
						sbVal.append(valDatosMinimosConstruccionSB.toString()+"\n\r");
					}
					if(!existeDatosMinimosTitular){
						sbVal.append(valDatosMinimoTitularsSB.toString()+"\n\r");
					}
				}
				//evaluandoDatosCorrectos = true;
				existeDatosMinimosBien = true;
				existeDatosMinimosUC = true;
				existeDatosMinimosSuelo = true;
				existeDatosMinimosCultivo = true;
				existeDatosMinimosConstruccion = true;
				existeDatosMinimosTitular  = true;
				valDatosMinimosBienSB = new StringBuffer();
				valDatosMinimosUCSB = new StringBuffer();
				valDatosMinimosSueloSB = new StringBuffer();
				valDatosMinimosCultivoSB = new StringBuffer();
				valDatosMinimosConstruccionSB = new StringBuffer();
				valDatosMinimoTitularsSB  = new StringBuffer();
				
			}
			else if(hijo.getUserObject() instanceof  BienInmuebleJuridico){
				
			}
			
//			sbVal.append(I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos")+"\n\r");
////			DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) raiz.getChildAt(0);
//			if(hijo.getUserObject() instanceof FincaCatastro){
//				sbVal.append("Referencia Catastral: "+((FincaCatastro)hijo.getUserObject()).getRefFinca().getRefCatastral()+"\n\r");
//				
//			}
//			else if(hijo.getUserObject() instanceof BienInmuebleJuridico){
//				sbVal.append("Bien: "+ ((BienInmuebleJuridico)hijo.getUserObject()).getBienInmueble().getIdBienInmueble().getIdBienInmueble()+"\n\r");
//				
//			}
			
//			if (!errorEncontrado && datosMinimosYCorrectosButtonValidar(hijo)) {
//				validaDatosCorrectos(hijo);
//			} else {
//				errorEncontrado = true;
//				return false;
//			}
		}
//		if(errorEncontrado){
//			return false;
//		}
//		return true;
		return sbVal;
	}

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
			jCheckBoxSuelos.setText(I18N.get("Expedientes",
					"expedientes.panel.visualizar.suelos"));
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
			jCheckBoxLcomunes.setText(I18N.get("Expedientes",
					"expedientes.panel.visualizar.lcomunes"));
		}
		return jCheckBoxLcomunes;
	}

	/**
	 * This method initializes jButtonGuardar
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonGuardar() {
		if (jButtonGuardar == null) {
			jButtonGuardar = new JButton();
			jButtonGuardar.setPreferredSize(new Dimension(150, 25));
			jButtonGuardar.setText(I18N.get("Expedientes",
					"expedientes.boton.guardar"));
			jButtonGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							AppContext.getApplicationContext().getMainFrame(),
							null);

					progressDialog.setTitle("TaskMonitorDialog.Wait");
					progressDialog.report(I18N.get("Expedientes",
											"Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.msgGuardando"));
					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {
							new Thread(new Runnable() {
								public void run() {
									try {
										// Recogemos la informacion del panel
										// cargado en ese momento
										recopilarDatos((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent());

										if (jTabbedPaneExpediente.getSelectedComponent().equals(fxccPanel)) {
											FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
										
											if (fxcc == null) {
												fxcc = new FX_CC();
											}
											fxccPanel.guardarFXCC(fxcc);
											
											if (ascPanel.getAsc() !=null ){
												
												ASCCatastro ascCatastro = ascPanel.getAsc();
												fxccPanel.setLstPlantasUsos(ascCatastro);
												ascPanel.setAsc();
												
												ascPanel.guardarASC(fxcc);
											}
											parcelaPanel.getFincaCatastro().setFxcc(fxcc);
										}
										if (jTabbedPaneExpediente.getSelectedComponent().equals(ascPanel)) {
											FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
											if (fxcc == null) {
												fxcc = new FX_CC();
											}
											ascPanel.setAsc();
//											ascPanel.guardarASC(fxcc);
											ascPanel.guardarASC(ascPanel.textoAscTemp,fxcc,fxccPanel.geopistaEditor);
											if (fxcc.getDXF() == null) {
												fxccPanel.guardarFXCCNoSeleccionado(fxcc);
											}
											parcelaPanel.getFincaCatastro().setFxcc(fxcc);
										}
										if (jTabbedPaneExpediente
												.getSelectedComponent().equals(
														imagenCatastroPanel)) {

											parcelaPanel.getFincaCatastro().setLstImagenes(imagenCatastroPanel.getLstImagenes());
										}
										ArrayList lstRef = new ArrayList();
										lstRef.addAll(jPanelExpedientes.getFincasEnMemoria());
										lstRef.addAll(jPanelExpedientes.getLstReferenciasBorradas());
										expediente.setListaReferencias(lstRef);
										try {
											//ConstantesRegistroExp.clienteCatastro.modificarExpediente(expediente);
											ConstantesRegExp.clienteCatastro.modificarExpedienteVariaciones(expediente, true,
													(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales"));
											
											if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
												//expediente de variaciones
												if(jPanelExpedientes.getLstReferenciasEliminarCatastroTemporal() != null &&
														!jPanelExpedientes.getLstReferenciasEliminarCatastroTemporal().isEmpty()){
													//la finca se dio de alta y despues de baja, hay que eliminarla de catastro_temporal
													
													ConstantesRegExp.clienteCatastro.eliminarReferenciaCatastroTemoral(expediente,
																jPanelExpedientes.getLstReferenciasEliminarCatastroTemporal());
												}
											}
											
											
										} catch (Exception e1) {
											System.out.println("Se ha producido un error modificando el expediente "
															+ e1.getMessage()+ e1);
											e1.printStackTrace();
										}
										expediente.setListaReferencias(jPanelExpedientes.getLstReferencias());
										jPanelExpedientes.setLstReferenciasBorradas(new ArrayList());
									} catch (Exception e) {
										e.printStackTrace();
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
			});
			jButtonGuardar.setEnabled(isEditable);
		}
		return jButtonGuardar;
	}

	/**
	 * This method initializes jButtonValidar
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonValidar() {
		if (jButtonValidar == null) {
			jButtonValidar = new JButton();
			jButtonValidar.setPreferredSize(new Dimension(150, 25));
			jButtonValidar.setText(I18N.get("Expedientes",
					"expedientes.boton.validar"));
			jButtonValidar.setEnabled(isEditable);
			jButtonValidar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					recopilarDatos((DefaultMutableTreeNode) tree
							.getSelectionPath().getLastPathComponent());
					DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) tree
							.getModel().getRoot();
					errorEncontrado = false;
					
					// se comprueba que al menos hay una finca que ha sido consultada en la modificacion
					//catastral y por lo tanto han sido consultados los datos de la finca
					boolean refAlgunaFincaConsultada = false;
					for(int j=0; j<getJPanelExpedientes().getReferenciasTraidas().length; j++){
						if(getJPanelExpedientes().getReferenciasTraidas()[j]){
							refAlgunaFincaConsultada = true;
						}
					}
					
					StringBuffer sbVal = validacionDatos(refAlgunaFincaConsultada,raiz);

					if(mostrarPantallaValidaciones){
						
						final ValidacionesJDialog validacionesDialog = new ValidacionesJDialog();
						validacionesDialog.setLocation(aplicacion.getMainFrame().getWidth()/2-300,
														aplicacion.getMainFrame().getHeight()/2-250);
						validacionesDialog.setVisible(true);
						validacionesDialog.getjTextArearesultad().setText(sbVal.toString());
						
						validacionesDialog.getValidarjButton().addMouseListener(new java.awt.event.MouseAdapter() {
							public void mousePressed(java.awt.event.MouseEvent e) {
								validacionesDialog.setVisible(false);
								boolean hayRefConsultada = false;
								for(int j=0; j<getJPanelExpedientes().getReferenciasTraidas().length; j++){
									if(getJPanelExpedientes().getReferenciasTraidas()[j]){
										hayRefConsultada = true;
									}
								}
								StringBuffer sbVal = validacionDatos(hayRefConsultada,
										(DefaultMutableTreeNode) tree.getModel().getRoot());
								validacionesDialog.getjTextArearesultad().setText(sbVal.toString());
								validacionesDialog.setVisible(true);
							}
						});
						
						sbVal = new StringBuffer();
						valDatosMinimosBienSB = new StringBuffer();
						valDatosMinimosUCSB = new StringBuffer();
						valDatosMinimosSueloSB = new StringBuffer();
						valDatosMinimosCultivoSB = new StringBuffer();
						valDatosMinimosConstruccionSB = new StringBuffer();
						valDatosMinimoTitularsSB = new StringBuffer();
						existeDatosMinimosBien = true;
						existeDatosMinimosUC = true;
						existeDatosMinimosSuelo = true;
						existeDatosMinimosCultivo = true;
						existeDatosMinimosConstruccion = true;
						existeDatosMinimosTitular = true;
						
					}
				}
			});
		}
		return jButtonValidar;
	}

	private StringBuffer validacionDatos(boolean refAlgunaFincaConsultada, DefaultMutableTreeNode raiz){
		
		StringBuffer sbVal = new StringBuffer();
		mostrarPantallaValidaciones = true;
		// si hay alguna finca consultada se pasa a validar los datos
		// si no hay ninguna finca consultada se muestra un mensaje indicando que no hay datos para validar
		if(refAlgunaFincaConsultada){
			evaluandoDatosCorrectosTotales = true;
			sbVal = validaDatosCorrectos(raiz);
			if (evaluandoDatosCorrectosTotales) {
				if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
					//expediente de situaciones finales
					sbVal = Validacion.validacionTotal(
								getJPanelExpedientes().getFincasEnMemoria(), expediente);
				}
				else{
					// expediente de variaciones
//					sbVal = Validacion.validacionTotal(
//							getJPanelExpedientes().getFincasEnMemoria(), expediente);
					sbVal.append(I18N.get("Expedientes","expedientes.validar.datosminimos.correctos")+"\n\n\r");
				}
			}
//			else{		
//				existeDatosMinimosBien = false;
//				sbVal.append(I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos")+"\n\r");
//
//			    if(!existeDatosMinimosBien){
//			    	sbVal.append(valDatosMinimosBienSB.toString()+"\n\r");
//			    }
//				if(!existeDatosMinimosUC){
//					sbVal.append(valDatosMinimosUCSB.toString()+"\n\r");  	
//				}
//				if(!existeDatosMinimosSuelo){
//					sbVal.append(valDatosMinimosSueloSB.toString()+"\n\r");
//				}
//				if(!existeDatosMinimosCultivo){
//					sbVal.append(valDatosMinimosCultivoSB.toString()+"\n\r");
//				}
//				if(!existeDatosMinimosConstruccion){
//					sbVal.append(valDatosMinimosConstruccionSB.toString()+"\n\r");
//				}
//				if(!existeDatosMinimosTitular){
//					sbVal.append(valDatosMinimoTitularsSB.toString()+"\n\r");
//				}
//
//				mostrarPantallaValidaciones = true;
//			}
			evaluandoDatosCorrectosTotales = false;
		}
		else{
			sbVal.append(I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.noDatosValidar"));
		}
		
		return sbVal;
	}
	/**
	 * This method initializes jButtonEstadoModificado
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonEstadoModificado() {
		if (jButtonEstadoModificador == null) {
			jButtonEstadoModificador = new JButton();
			jButtonEstadoModificador.setPreferredSize(new Dimension(150, 25));
			jButtonEstadoModificador.setText(I18N.get("Expedientes",
					"expedientes.boton.estadomodificado"));
			jButtonEstadoModificador.setEnabled(isEditable);
			jButtonEstadoModificador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (JOptionPane.showConfirmDialog((Component) GestionExpedientePanel.this, 
							I18N.get("Expedientes","expediente.previousstatequestion"), 
							I18N.get("Expedientes","expediente.previousstate"),
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

						if (expediente.getIdEstado() == ConstantesCatastro.ESTADO_GENERADO
								|| expediente.getIdEstado() == ConstantesCatastro.ESTADO_FINALIZADO) {
							expediente.setIdEstado(ConstantesCatastro.ESTADO_MODIFICADO);
							expediente.setFechaAlteracion(null);
							updateExpedienteEnBD(expediente);

							if (GestionExpedientePanel.this.getExpediente()
									.getIdEstado() >= ConstantesCatastro.ESTADO_FINALIZADO) {
								EdicionUtils.enablePanel(GestionExpedientePanel.this.getJPanelBotones(), false);
								GestionExpedientePanel.this.getJButtonEstadoModificado().setEnabled(true);
							} else {
								EdicionUtils.enablePanel(GestionExpedientePanel.this.getJPanelBotones(), true);
								GestionExpedientePanel.this.getJButtonEstadoModificado().setEnabled(false);
							}
						}
					}
				}
			});
		}
		return jButtonEstadoModificador;
	}

	/**
	 * This method initializes jTabbedPaneExpediente
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPaneExpediente() {
		if (jTabbedPaneExpediente == null) {
			jTabbedPaneExpediente = new JTabbedPane();
			jTabbedPaneExpediente
					.addChangeListener(new javax.swing.event.ChangeListener() {

						private FeatureExtendedPanel oldpanel = null;

						public void stateChanged(ChangeEvent e) {
							if (oldpanel != null && !oldpanel.equals(fxccPanel)
									&& !oldpanel.equals(ascPanel)
									&& !cambioPestanaAutomatico) {
								datosMinimosYCorrectos(getPreNodeInfo());
								/*
								 * evaluandoDatosCorrectos= true;
								 * jTabbedPaneExpediente
								 * .setSelectedComponent((Component)oldpanel);
								 * evaluandoDatosCorrectos= false; return;
								 */
							}
							if (oldpanel != null) {
								if (oldpanel.equals(fxccPanel)) {
									if (parcelaPanel.getFincaCatastro() != null) {

										FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
										if (fxcc == null) {
											fxcc = new FX_CC();
										}

										parcelaPanel.getFincaCatastro().setFxcc(fxcc);
									}
									final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
											AppContext.getApplicationContext().getMainFrame(), null);

									progressDialog.setTitle("TaskMonitorDialog.Wait");
									progressDialog.report(I18N.get("Expedientes",
															"Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.msgGuardandoDXF"));
									progressDialog.addComponentListener(new ComponentAdapter() {
												public void componentShown(
														ComponentEvent e) {
													new Thread(new Runnable() {
														public void run() {
															try {
																if (parcelaPanel.getFincaCatastro() != null) {

																	FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
																	if (fxcc == null) {
																		fxcc = new FX_CC();
																	}
																	fxccPanel.guardarFXCC(fxcc);
																	
																	if (ascPanel.getAsc() !=null ){
																		
																		ASCCatastro ascCatastro = ascPanel.getAsc();
																		fxccPanel.setLstPlantasUsos(ascCatastro);
																		ascPanel.setAsc();
																		
																		ascPanel.guardarASC(fxcc);
																	}

																	parcelaPanel.getFincaCatastro().setFxcc(fxcc);
																}
															} catch (Exception e) {
																e.printStackTrace();
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
								if (oldpanel.equals(ascPanel)) {
									if (parcelaPanel.getFincaCatastro() != null) {
										FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
										if (fxcc == null) {
											fxcc = new FX_CC();
										}

										parcelaPanel.getFincaCatastro().setFxcc(fxcc);
									}
									final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
											AppContext.getApplicationContext().getMainFrame(), null);

									progressDialog.setTitle("TaskMonitorDialog.Wait");
									progressDialog.report(I18N.get("Expedientes",
															"Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.msgGuardandoASC"));
									progressDialog.addComponentListener(new ComponentAdapter() {
												public void componentShown(
														ComponentEvent e) {
													new Thread(new Runnable() {
														public void run() {
															try {
																if (parcelaPanel.getFincaCatastro() != null) {
																	FX_CC fxcc = parcelaPanel.getFincaCatastro().getFxcc();
																	if (fxcc == null) {
																		fxcc = new FX_CC();
																	}
																	// El ASC lo
																	// volvemos
																	// a generar
																	// a partir
																	// del DXF.

																	ascPanel.guardarASC(ascPanel.textoAscTemp,fxcc,fxccPanel.geopistaEditor);
																	if (fxcc.getDXF() == null) {
																		fxccPanel.guardarFXCCNoSeleccionado(fxcc);
																	}
																	parcelaPanel.getFincaCatastro().setFxcc(fxcc);
					
																}
															} catch (Exception e) {
																e.printStackTrace();
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
								oldpanel.exit();
							}
							
							// Notifica a los paneles de los cambios de pestañas
							FeatureExtendedPanel panel = (FeatureExtendedPanel) jTabbedPaneExpediente.getSelectedComponent();
							if (panel != null)
								panel.enter();
							oldpanel = panel;
							cambioPestanaManual = true;
							if (oldpanel.equals(sueloPanel)) {
								actualizaArbol("Suelo");
								DefaultMutableTreeNode nodeF = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
								Object nodeInfoF = nodeF.getUserObject();
								FincaCatastro finca = null;
								if (nodeInfoF instanceof FincaCatastro) {
									finca = (FincaCatastro) nodeInfoF;
									EdicionUtils.clearPanel(sueloPanel);
									sueloPanel.setSuelo(null);
								} else if (nodeInfoF instanceof SueloCatastro) {
									finca = (FincaCatastro) ((DefaultMutableTreeNode) tree.getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
								}
								sueloPanel.setFincaActual(finca);
							} else if (oldpanel.equals(ucPanel)) {
								actualizaArbol("UC");
								DefaultMutableTreeNode nodeF = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
								Object nodeInfoF = nodeF.getUserObject();
								FincaCatastro finca = null;
								if (nodeInfoF instanceof FincaCatastro) {
									finca = (FincaCatastro) nodeInfoF;
									EdicionUtils.clearPanel(ucPanel);
									ucPanel.setUnidadCons(null);
								} else if (nodeInfoF instanceof UnidadConstructivaCatastro) {
									finca = (FincaCatastro) ((DefaultMutableTreeNode) tree.getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
								}
								ucPanel.setFincaActual(finca);
							}
							cambioPestanaManual = false;
						}
					});

			jTabbedPaneExpediente.addTab(I18N.get("Expedientes","expedientes.pestania.finca.titulo"), 
					IconLoader.icon(GestionExpedienteConst.ICONO_FINCA), parcelaPanel, null);
			jTabbedPaneExpediente.addTab(I18N.get("Expedientes","expedientes.pestania.suelo.titulo"), 
					IconLoader.icon(GestionExpedienteConst.ICONO_SUELO), sueloPanel, null);
			jTabbedPaneExpediente.addTab(I18N.get("Expedientes","expedientes.pestania.uc.titulo"), 
					IconLoader.icon(GestionExpedienteConst.ICONO_UC), ucPanel, null);
			jTabbedPaneExpediente.addTab("DXF", null, fxccPanel, null);
			jTabbedPaneExpediente.addTab("ASC", null, ascPanel, null);
			jTabbedPaneExpediente.addTab("IMG", null, imagenCatastroPanel, null);

			// Deshabilita todas las pestañas al cargar el árbol
			jTabbedPaneExpediente.setSelectedComponent(parcelaPanel);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(sueloPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(sueloPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ucPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(parcelaPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(fxccPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ascPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(imagenCatastroPanel), false);

			jTabbedPaneExpediente.setPreferredSize(new Dimension(650, 500));
			jTabbedPaneExpediente.setMaximumSize(jTabbedPaneExpediente.getPreferredSize());
			jTabbedPaneExpediente.setMinimumSize(jTabbedPaneExpediente.getPreferredSize());

			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
				EdicionUtils.enablePanel(jTabbedPaneExpediente, isEditable);
				parcelaPanel.setEditable(isEditable);
			}
			else{
				EdicionUtils.enablePanel(jTabbedPaneExpediente, isEditable);
				parcelaPanel.setEditable(isEditable);
			}

		}
		return jTabbedPaneExpediente;
	}

	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {

		boolean  actualizado = true;
		blackboard.put("ComBienes", false);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if (node == null) {
			setPreNodeInfo(node);
			return;
		}

		// Recogemos los datos del panel seleccionado por el nodo anterior
		if (getPreNodeInfo() != null) {
			if (getPreNodeInfo() != node) {
				datosMinimosYCorrectos(getPreNodeInfo());
				recopilarDatos(getPreNodeInfo());
			}
		}

		cambioPestanaAutomatico = true;
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
			parcela = getJPanelExpedientes().fillObject(node);
			obtenerInformacionPonencias();
			tree.expandPath(tree.getSelectionPath());

			// Sustituye el nodo finca por el relleno con los datos de la misma
			if (parcela != null)
				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(parcela);
			
			tree.repaint();
			
			//se consulta el estado de la finca, para comprobar si esta actualizada con la OVC
			
			try {
				actualizado = ConstantesRegExp.clienteCatastro.consultaEstadoFincaOVC(expediente, 
							((FincaCatastro)node.getUserObject()).getRefFinca().getRefCatastral());
				((FincaCatastro)node.getUserObject()).setActualizadoOVC(actualizado);
			} catch (ACException e1) {
				JOptionPane.showMessageDialog(this, e1.getCause().getMessage());

			} catch (Exception e2) {
				e2.printStackTrace();
	
			}
		
			
			
			if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
				//expediente de variaciones
				
				if(parcela.isElementoModificado()){
	                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), true);
				}
				else{
	                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), false);
				}
			}

			AppContext.getApplicationContext().getBlackboard().remove("ascObject");
			AppContext.getApplicationContext().getBlackboard().remove("ascTemp");
			ASCPanel.get_instance().infoplantas.clear();
			
			fxccPanel.limpiarEditor();
			fxccPanel.lastRefCatastral = "";
			
//			fxccPanel.remove(fxccPanel.geopistaEditor);			
//			fxccPanel.geopistaEditor.removeAll();
//			fxccPanel.geopistaEditor=null;
//
//			fxccPanel.geopistaEditor = new GeopistaEditor("workbench-properties-catastro-simple.xml");
//			fxccPanel.geopistaEditor.getToolBar().addCursorTool("Zoom In/Out", new ZoomTool());            
//			fxccPanel.geopistaEditor.getToolBar().addCursorTool("Pan", new PanTool());
//			fxccPanel.geopistaEditor.getToolBar().addCursorTool("Measure", new GeopistaMeasureTool());
//
//			fxccPanel.geopistaEditor.setVisible(true);
//			fxccPanel.geopistaEditor.showLayerName(true);
//
//
//			this.add(fxccPanel.geopistaEditor,
//					new GridBagConstraints(0, 0, 1, 1, 1, 1, 
//							GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets (0,0,0,0), 0,0));

			
//fxccPanel.regeopistaEditor = new GeopistaEditor("workbench-properties-catastro-simple.xml");

		}
		// Si cuelgan los bienes directamente del expediente porque se trata de
		// información de titularidad
		else if (nodeInfo instanceof BienInmuebleJuridico
				&& ((DefaultMutableTreeNode) node.getParent()).getUserObject() instanceof Expediente) {
			// Extraer información del bien y rellenar el arbol
			bijPanel = getJPanelExpedientes().fillObjectBienInmueble(node);
			tree.expandPath(tree.getSelectionPath());

			// Sustituye el nodo bien inmueble por el relleno con los datos de
			// la misma
			if (bijPanel != null)
				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent())
						.setUserObject(bijPanel);
			tree.repaint();

			//se consulta el estado de la finca, para comprobar si esta actualizada con la OVC
			try {
				actualizado = ConstantesRegExp.clienteCatastro.consultaEstadoBienOVC(expediente, 
							((BienInmuebleJuridico)node.getUserObject()).getBienInmueble().getIdBienInmueble().getIdBienInmueble());
				((BienInmuebleJuridico)node.getUserObject()).getBienInmueble().setActualizadoOVC(actualizado);
				
			} catch (ACException e1) {
				JOptionPane.showMessageDialog(this, e1.getCause().getMessage());

			} catch (Exception e2) {
				e2.printStackTrace();
	
			}
			
		}

		if (nodeInfo instanceof Expediente) {
			jTabbedPaneExpediente.setSelectedComponent(parcelaPanel);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(sueloPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ucPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(parcelaPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(fxccPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ascPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(imagenCatastroPanel), false);

		} else if ((nodeInfo instanceof FincaCatastro
				|| nodeInfo instanceof BienInmuebleJuridico
				|| nodeInfo instanceof ConstruccionCatastro
				|| nodeInfo instanceof Persona || nodeInfo instanceof Cultivo)
				&& !cambioPestanaManual) {
			jTabbedPaneExpediente.setSelectedComponent(parcelaPanel);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(parcelaPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(sueloPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ucPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(fxccPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ascPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(imagenCatastroPanel), true);

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
			jTabbedPaneExpediente.setSelectedComponent(sueloPanel);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(parcelaPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ucPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(sueloPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(fxccPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ascPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(imagenCatastroPanel), true);
		} else if (nodeInfo instanceof UnidadConstructivaCatastro) {
			parcelaPanel.setFocoEn(null);
			jTabbedPaneExpediente.setSelectedComponent(ucPanel);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(parcelaPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(sueloPanel), false);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ucPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(fxccPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(ascPanel), true);
			jTabbedPaneExpediente.setEnabledAt(jTabbedPaneExpediente.indexOfComponent(imagenCatastroPanel), true);
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
		if (nodeInfo instanceof Expediente) {
			EdicionUtils.enablePanel(parcelaPanel, false);
			parcelaPanel.setEditable(false);
			EdicionUtils.disabledButtons(parcelaPanel, "_borrar");
			EdicionUtils.disabledButtons(parcelaPanel, "vista");
			EdicionUtils.disabledButtons(parcelaPanel, "_actualizarParcela");
		} else {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
				//expediente de situaciones finales
				EdicionUtils.enablePanel(jTabbedPaneExpediente, isEditable);
				parcelaPanel.setEditable(isEditable);
			}
			else{
				//expediente de variaciones
				EdicionUtils.enablePanel(jTabbedPaneExpediente, isEditable);
				parcelaPanel.setEditable(isEditable);
			}
			EdicionUtils.enabledButtons(parcelaPanel, "vista");
		}

		if (expediente.getListaReferencias() != null) {
			if (!expediente.getListaReferencias().isEmpty()) {
				Object object = expediente.getListaReferencias().iterator()
						.next();
				if (!(object instanceof FincaCatastro)) {
					EdicionUtils.enablePanel(
							parcelaPanel.getJPanelDatosFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneFincas(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBotoneraFinca(), false);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelLocal(),
							false);
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "vistacultivo");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "eliminarlocal");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_borrarfinca");
				}
			}
		}

		if (nodeInfo instanceof FincaCatastro)
			if (parcela != null)
				if (parcela.isDelete()) {
					EdicionUtils.enablePanel(
							parcelaPanel.getJPanelDatosFinca(), false);
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
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraBienInmueble(), "_nuevobien");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraBienInmueble(), "_nuevotitular");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "vistacultivo");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "eliminarlocal");
					EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_borrarfinca");
				} else {
					EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneFincas(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBotoneraFinca(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelLocal(),true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelCultivo(),true);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneCultivo(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneLocal(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelBienInmueble(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneBienInmueble(), true);
					EdicionUtils.enablePanel(parcelaPanel.getJPanelTitular(),true);
					EdicionUtils.enablePanel(parcelaPanel.getJScrollPaneTitular(), true);
					EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraBienInmueble(), "_nuevobien");
					EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraBienInmueble(), "_nuevotitular");
					EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "vistacultivo");
					EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraLocal(), "eliminarlocal");
					EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_borrarfinca");
					
					EdicionUtils.enablePanel(ascPanel.getJPanelPlantaGeneral(), false);
				}

		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
			//expediente de variaciones
			if(expediente.getIdEstado() == ConstantesCatastro.ESTADO_FINALIZADO){
				deshabilitarTodosPaneles();
			}
			else{
				habilitarPaneles(nodeInfo);
			}
			comprobarEstadoElemento(nodeInfo);
		}
		
		
		if (!parcelaPanel.isFocoAutomatico()) {
			parcelaPanel.asignaFocoManual();
		}
		cambioPestanaAutomatico = false;
		

		EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_consultarParcela");
		

		if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO) && 
				expediente.getIdEstado() != ConstantesRegExp.ESTADO_FINALIZADO && expediente.getIdEstado() != ConstantesRegExp.ESTADO_GENERADO){
			 
			if(actualizado){
				EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_actualizarParcela");
			}
			else{
				EdicionUtils.enabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_actualizarParcela");
			}
    	}
    	else{
    		EdicionUtils.disabledButtons(parcelaPanel.getJPanelBotoneraFinca(), "_actualizarParcela");
    	}

	}

	/**
	 *Si el elemento seleccionado en el nodo tiene como tipo de movimiento BAJA "B"
	 *se muestra un mensaje informandolo
	 */
	private void comprobarEstadoElemento(Object nodeInfo){
		
		if (nodeInfo instanceof FincaCatastro) {
			if(((FincaCatastro)nodeInfo).getTIPO_MOVIMIENTO_DELETE() != null &&
					((FincaCatastro)nodeInfo).getTIPO_MOVIMIENTO_DELETE().equals(FincaCatastro.TIPO_MOVIMIENTO_BAJA)){
				JOptionPane.showMessageDialog(this, I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Elemento.Seleccionado.Baja"));
			}
		}
		else if (nodeInfo instanceof BienInmuebleJuridico) {
			if(((BienInmuebleJuridico)nodeInfo).getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_BAJA)){
				JOptionPane.showMessageDialog(this, I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Elemento.Seleccionado.Baja"));
			}
		}
		else if (nodeInfo instanceof Titular) {
			if(((Titular)nodeInfo).getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA)){
				JOptionPane.showMessageDialog(this, I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Elemento.Seleccionado.Baja"));
			}
		}
		else if(nodeInfo instanceof Cultivo){
			if(((Cultivo)nodeInfo).getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_BAJA)){
				JOptionPane.showMessageDialog(this, I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Elemento.Seleccionado.Baja"));
			}
		}
		else if (nodeInfo instanceof ConstruccionCatastro) {
			if(((ConstruccionCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_BAJA)){
				JOptionPane.showMessageDialog(this, I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Elemento.Seleccionado.Baja"));
			}
		}
		
		else if (nodeInfo instanceof UnidadConstructivaCatastro) {
			if(((UnidadConstructivaCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_BAJA)){
				JOptionPane.showMessageDialog(this, I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Elemento.Seleccionado.Baja"));
				EdicionUtils.disabledButtons(ucPanel, "_eliminarUC");
				EdicionUtils.disabledButtons(ucPanel, "_modificarUC");
			}
			else{
				EdicionUtils.enabledButtons(ucPanel, "_eliminarUC");
				EdicionUtils.enabledButtons(ucPanel, "_modificarUC");
			}
		}
		
		else if (nodeInfo instanceof SueloCatastro) {
			if(((SueloCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_BAJA)){
				JOptionPane.showMessageDialog(this, I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Elemento.Seleccionado.Baja"));
				EdicionUtils.disabledButtons(sueloPanel, "_eliminarSuelo");
				EdicionUtils.disabledButtons(sueloPanel, "_modificarSuelo");
				EdicionUtils.disabledButtons(sueloPanel, "_anadirSuelo");
			}
			else{
				EdicionUtils.enabledButtons(sueloPanel, "_eliminarSuelo");
				EdicionUtils.enabledButtons(sueloPanel, "_modificarSuelo");
				EdicionUtils.disabledButtons(sueloPanel, "_anadirSuelo");
			}
		}
		
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
						parcelaPanel.loadData(((BienInmuebleJuridico) ((DefaultMutableTreeNode) node
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
			parcelaPanel.loadData(((DefaultMutableTreeNode) node.getParent()).getUserObject());
		} else if (nodeInfo instanceof UnidadConstructivaCatastro) {
			ucPanel.load((UnidadConstructivaCatastro) nodeInfo);
			parcelaPanel.loadData(((DefaultMutableTreeNode) node.getParent()).getUserObject());
		} else if (nodeInfo instanceof FincaCatastro) {

			loadFinca((FincaCatastro) nodeInfo);
			loadLocales((FincaCatastro) nodeInfo);
			loadCultivos((FincaCatastro) nodeInfo);
			loadBienes((FincaCatastro) nodeInfo);
			ascPanel.loadFincaCatastroData((FincaCatastro) nodeInfo, expediente);
			ascPanel.setFinca((FincaCatastro) nodeInfo);
			fxccPanel.setDxfData(((FincaCatastro) nodeInfo).getRefFinca()
					.getRefCatastral(), expediente.getIdExpediente());
			ascPanel.setDxfData(((FincaCatastro) nodeInfo).getRefFinca()
					.getRefCatastral(), expediente.getIdExpediente());
			imagenCatastroPanel.setLstImagenes(((FincaCatastro) nodeInfo)
					.getRefFinca().getRefCatastral(), expediente
					.getIdExpediente());
		} else if (nodeInfo instanceof Expediente) {
			ascPanel.loadExpedienteData((Expediente) nodeInfo);
			loadExpediente((Expediente) nodeInfo);
			loadBienes((Expediente) nodeInfo);
			loadLocales((Expediente) nodeInfo);
			loadCultivos((Expediente) nodeInfo);

		}

	}

	private void loadBienes(Expediente expediente) {

		ArrayList bienes = new ArrayList();

		Iterator itRef = expediente.getListaReferencias().iterator();
		while (itRef.hasNext()) {
			Object referencia = (Object) itRef.next();
			if (referencia instanceof FincaCatastro
					&& ((FincaCatastro) referencia).getLstBienesInmuebles() != null) {
				Iterator itFinc = ((FincaCatastro) referencia).getLstBienesInmuebles().iterator();
				while (itFinc.hasNext()) {
					bienes.add(((BienInmuebleJuridico) itFinc.next()).getBienInmueble());
				}
			} else if (referencia instanceof BienInmuebleCatastro) {
				bienes.add(referencia);
			}
		}

		loadBienes(bienes);
//		loadBienesAscPanel(bienes);

		return;
	}

//	private void loadBienesAscPanel(ArrayList bienes) {
//		// TODO Auto-generated method stub
//		this.ascPanel.loadTablePlantas(bienes);
//	}

	private void loadLocales(Expediente expediente) {

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

	private void loadCultivos(Expediente expediente) {

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
					parcelaPanel.getJPanelCultivo().add(parcelaPanel.getJPanelDatosCultivo(),
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
					parcelaPanel.getJPanelCultivo().add(parcelaPanel.getJScrollPaneCultivo(),
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

	private void loadExpediente(Expediente exp) {
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

	private void loadExpediente(ArrayList lstExp) {
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
	 * @return Returns the expediente.
	 */
	public Expediente getExpediente() {
		return expediente;
	}

	/**
	 * @param expediente
	 *            The expediente to set.
	 */
	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;

		// Actualiza el expediente mostrado en el árbol
		if (jScrollPane != null)
			this.remove(jScrollPane);

		this.add(getJScrollPane(), new GridBagConstraints(0, 0, 1, 1, 0.1, 0.9,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						10, 10, 0, 10), 80, 80));
	}

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

	public void asociarBotonDerecho() {
		tree.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent me) {
				if ((me.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
					muestraPanelBotonDerecho(me);
				}
			}

			public void mousePressed(MouseEvent me) {
			}

			public void mouseReleased(MouseEvent me) {
			}

			public void mouseEntered(MouseEvent me) {
			}

			public void mouseExited(MouseEvent me) {
			}
		});
	}

	private void muestraPanelBotonDerecho(MouseEvent me) {
		JPopupMenu menu = new JPopupMenu();
		copiar = new JMenuItem(
				I18N
						.get("Expedientes",
								"Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Copiar"));
		pegar = new JMenuItem(
				I18N
						.get("Expedientes",
								"Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.Pegar"));
		if (nodoAPegar == null) {
			pegar.setEnabled(false);
		}
		copiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copiarNodo();
			}
		});
		pegar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pegarNodo();
			}
		});
		menu.add(copiar);
		menu.add(pegar);
		menu.show(me.getComponent(), me.getX(), me.getY());
	}

	private void copiarNodo() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		Object nodeInfo = node.getUserObject();

		if (nodeInfo instanceof Titular) {
			nodoAPegar = ((Titular) nodeInfo).clone((Titular) nodeInfo);
			pegar.setEnabled(true);
		} 
		else if(nodeInfo instanceof BienInmuebleJuridico){						
			nodoAPegar = ((BienInmuebleJuridico) nodeInfo).clone((BienInmuebleJuridico) nodeInfo);
			pegar.setEnabled(true);
		}
		else if (nodeInfo instanceof Cultivo) {
			nodoAPegar = ((Cultivo) nodeInfo).clone((Cultivo) nodeInfo);
			pegar.setEnabled(true);
		} 
		else if (nodeInfo instanceof ConstruccionCatastro) {
			nodoAPegar = ((ConstruccionCatastro) nodeInfo).clone((ConstruccionCatastro) nodeInfo);
			pegar.setEnabled(true);
		} 
		else if (nodeInfo instanceof UnidadConstructivaCatastro) {
			nodoAPegar = ((UnidadConstructivaCatastro) nodeInfo).clone((UnidadConstructivaCatastro) nodeInfo);
			pegar.setEnabled(true);
		} 
		else if(nodeInfo instanceof SueloCatastro){
			nodoAPegar = ((SueloCatastro) nodeInfo).clone((SueloCatastro) nodeInfo);
			pegar.setEnabled(true);
		}
		else
		{
			JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorNoCopiar"));
		}
	}

	private void pegarNodo() {
		boolean pegarNodo = false;		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Object nodeInfo = node.getUserObject();
		
		if(nodoAPegar instanceof Titular){
			if (nodeInfo instanceof BienInmuebleJuridico) {
				pegarNodo = true;
			}
			else{
				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarBienInmueble"));
			}
		}
		else if(nodoAPegar instanceof BienInmuebleJuridico){
			if(nodeInfo instanceof FincaCatastro){
				pegarNodo = true;
			}
			else{
				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
			}
		}
		
		else if(nodoAPegar instanceof Cultivo){
			
			Cultivo cultivo = (Cultivo) nodoAPegar;
			// cultivo comun
			if ((cultivo.getIdCultivo().getNumOrden()==null ||
					cultivo.getIdCultivo().getNumOrden().trim().equals("")) &&
					cultivo.getCodModalidadReparto()!=null)
			{  
				if(nodeInfo instanceof FincaCatastro){
					pegarNodo = true;
				}
				else{
					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
				}
			}	
			else{
				if (nodeInfo instanceof BienInmuebleJuridico) {
					pegarNodo = true;
				}
				else{
					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarBienInmueble"));
				}
			}
		}
		else if(nodoAPegar instanceof ConstruccionCatastro){
			
			ConstruccionCatastro cons = (ConstruccionCatastro)nodoAPegar;
			if (cons.getDatosEconomicos().getCodTipoValor()!=null
					&& !cons.getDatosEconomicos().getCodTipoValor().equals("")
					&& cons.getDatosEconomicos().getCodModalidadReparto()!=null)
			{   
				if(nodeInfo instanceof FincaCatastro){
					pegarNodo = true;
				}
				else{
					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
				}
			
			}
			else{
				if (nodeInfo instanceof BienInmuebleJuridico) {
					pegarNodo = true;
				}
				else{
					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarBienInmueble"));
				}
			}
		}
		else if(nodoAPegar instanceof UnidadConstructivaCatastro) {
			if(nodeInfo instanceof FincaCatastro){
				pegarNodo = true;
			}
			else{
				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
			}
		}
		else if(nodoAPegar instanceof SueloCatastro) {
			if(nodeInfo instanceof FincaCatastro){
				pegarNodo = true;
			}
			else{
				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
			}
		}

		
		
		if (pegarNodo){
		
			HideableNode nodoOrigen = (HideableNode)parcelaPanel.getArbol().getSelectionPath().getLastPathComponent();
			
			CantidadNodosPegarDialog cantidadDialog = new CantidadNodosPegarDialog(aplicacion.getMainFrame(), true, nodoAPegar,  this, nodoOrigen);
			cantidadDialog.setSize(250, 150);
			cantidadDialog.setLocation((aplicacion.getMainFrame().getWidth()/2) - (cantidadDialog.getWidth()/2), 
					(aplicacion.getMainFrame().getHeight()/2) - (cantidadDialog.getHeight()/2));
			
			cantidadDialog.setVisible(true);
			
			
			
//			cantidadDialog.getAceptar().addActionListener(new java.awt.event.ActionListener()
//			{
//				public void actionPerformed(java.awt.event.ActionEvent e)
//				{
//					
//					int cantidadNodos = 0;
//					if (cantidadDialog.getAceptar().wasOKPressed())
//					{
//						cantidadNodos = Integer.valueOf(cantidadDialog.getCantidadField().getText());             
//					}
//					cantidadDialog.dispose(); 			
//					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
//					.getLastSelectedPathComponent();
//					Object nodeInfo = node.getUserObject();
//					
//					if(nodoAPegar instanceof Titular){
//						if (nodeInfo instanceof BienInmuebleJuridico) {
//							for (int i=0; i<cantidadNodos; i++){
//								parcelaPanel.annadeTitular((Titular) nodoAPegar);
//							}
//
//						}
//					
//					}
//					else if(nodoAPegar instanceof BienInmuebleJuridico){
//						if(nodeInfo instanceof FincaCatastro){
//							for (int i=0; i<cantidadNodos; i++){
//								BienInmuebleJuridico bij = (BienInmuebleJuridico)nodoAPegar;
//								parcelaPanel.annadeBI(bij);
//							}
//						}
//						
//					}
//					
//					else if(nodoAPegar instanceof Cultivo){
//						
//						Cultivo cultivo = (Cultivo) nodoAPegar;
//						// cultivo comun
//						if ((cultivo.getIdCultivo().getNumOrden()==null ||
//								cultivo.getIdCultivo().getNumOrden().trim().equals("")) &&
//								cultivo.getCodModalidadReparto()!=null)
//						{  
//							if(nodeInfo instanceof FincaCatastro){
//								for (int i=0; i<cantidadNodos; i++){
//									parcelaPanel.annadeCultivo(cultivo, true);
//								}
//							}
//							
//						}	
//						else{
//							if (nodeInfo instanceof BienInmuebleJuridico) {
//								for (int i=0; i<cantidadNodos; i++){
//									parcelaPanel.annadeCultivo(cultivo, false);
//								}
//							}
//							
//						}
//					}
//					else if(nodoAPegar instanceof ConstruccionCatastro){
//						
//						ConstruccionCatastro cons = (ConstruccionCatastro)nodoAPegar;
//						if (cons.getDatosEconomicos().getCodTipoValor()!=null
//								&& !cons.getDatosEconomicos().getCodTipoValor().equals("")
//								&& cons.getDatosEconomicos().getCodModalidadReparto()!=null)
//						{   
//							if(nodeInfo instanceof FincaCatastro){
//								for (int i=0; i<cantidadNodos; i++){
//									parcelaPanel.annadeConstruccion(cons, true);
//								}
//							}
//							
//						
//						}
//						else{
//							if (nodeInfo instanceof BienInmuebleJuridico) {
//								for (int i=0; i<cantidadNodos; i++){
//									parcelaPanel.annadeConstruccion(cons, false);
//								}
//							}
//							
//						}
//					}
//					else if(nodoAPegar instanceof UnidadConstructivaCatastro) {
//						if(nodeInfo instanceof FincaCatastro){
//							for (int i=0; i<cantidadNodos; i++){
//								UnidadConstructivaCatastro unidadConstructivaCatastro = (UnidadConstructivaCatastro) nodoAPegar;
//								parcelaPanel.annadeUnidadConstructivaCatastro(unidadConstructivaCatastro);
//							}
//						}
//						
//					}
//					else if(nodoAPegar instanceof SueloCatastro) {
//						if(nodeInfo instanceof FincaCatastro){
//							for (int i=0; i<cantidadNodos; i++){
//								SueloCatastro suelo = (SueloCatastro)nodoAPegar;
//								parcelaPanel.annadeSueloCatastro(suelo);
//							}
//						}
//						
//					}			
					
//				}
//	          });
		}
		
//		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
//				.getLastSelectedPathComponent();
//		Object nodeInfo = node.getUserObject();
//		
//		if(nodoAPegar instanceof Titular){
//			if (nodeInfo instanceof BienInmuebleJuridico) {
//				parcelaPanel.annadeTitular((Titular) nodoAPegar);
//			}
//			else{
//				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarBienInmueble"));
//			}
//		}
//		else if(nodoAPegar instanceof BienInmuebleJuridico){
//			if(nodeInfo instanceof FincaCatastro){
//				BienInmuebleJuridico bij = (BienInmuebleJuridico)nodoAPegar;
//				parcelaPanel.annadeBI(bij);
//			}
//			else{
//				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
//			}
//		}
//		
//		else if(nodoAPegar instanceof Cultivo){
//			
//			Cultivo cultivo = (Cultivo) nodoAPegar;
//			// cultivo comun
//			if ((cultivo.getIdCultivo().getNumOrden()==null ||
//					cultivo.getIdCultivo().getNumOrden().trim().equals("")) &&
//					cultivo.getCodModalidadReparto()!=null)
//			{  
//				if(nodeInfo instanceof FincaCatastro){
//					parcelaPanel.annadeCultivo(cultivo, true);
//				}
//				else{
//					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
//				}
//			}	
//			else{
//				if (nodeInfo instanceof BienInmuebleJuridico) {
//					parcelaPanel.annadeCultivo(cultivo, false);
//				}
//				else{
//					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarBienInmueble"));
//				}
//			}
//		}
//		else if(nodoAPegar instanceof ConstruccionCatastro){
//			
//			ConstruccionCatastro cons = (ConstruccionCatastro)nodoAPegar;
//			if (cons.getDatosEconomicos().getCodTipoValor()!=null
//					&& !cons.getDatosEconomicos().getCodTipoValor().equals("")
//					&& cons.getDatosEconomicos().getCodModalidadReparto()!=null)
//			{   
//				if(nodeInfo instanceof FincaCatastro){
//					parcelaPanel.annadeConstruccion(cons, true);
//				}
//				else{
//					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
//				}
//			
//			}
//			else{
//				if (nodeInfo instanceof BienInmuebleJuridico) {
//					parcelaPanel.annadeConstruccion(cons, false);
//				}
//				else{
//					JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarBienInmueble"));
//				}
//			}
//		}
//		else if(nodoAPegar instanceof UnidadConstructivaCatastro) {
//			if(nodeInfo instanceof FincaCatastro){
//				UnidadConstructivaCatastro unidadConstructivaCatastro = (UnidadConstructivaCatastro) nodoAPegar;
//				parcelaPanel.annadeUnidadConstructivaCatastro(unidadConstructivaCatastro);
//			}
//			else{
//				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
//			}
//		}
//		else if(nodoAPegar instanceof SueloCatastro) {
//			if(nodeInfo instanceof FincaCatastro){
//				SueloCatastro suelo = (SueloCatastro)nodoAPegar;
//				parcelaPanel.annadeSueloCatastro(suelo);
//			}
//			else{
//				JOptionPane.showMessageDialog(this,I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.GestionExpedientePanel.ErrorPegarFinca"));
//			}
//		}
//		
		
		
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
	
	private boolean validacionDatosMinimosYCorrectos(Object nodeInfo) {
		boolean correcto = true;

		if (nodeInfo instanceof BienInmuebleJuridico) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((BienInmuebleJuridico)nodeInfo).getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_ALTA) || 
					((BienInmuebleJuridico)nodeInfo).getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosBI();
			}
		}
		if (nodeInfo instanceof UnidadConstructivaCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((UnidadConstructivaCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((UnidadConstructivaCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = ucPanel.datosMinimosYCorrectosUC();
			}
		}
		if (nodeInfo instanceof SueloCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((SueloCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((SueloCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = sueloPanel.datosMinimosYCorrectosSuelo();
			}
		}
		if (nodeInfo instanceof Cultivo) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((Cultivo)nodeInfo).getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_ALTA) || 
					((Cultivo)nodeInfo).getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosCultivo();
			}
		}
		if (nodeInfo instanceof ConstruccionCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((ConstruccionCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((ConstruccionCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosLocal();
			}
		}
		if (nodeInfo instanceof Titular) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((Titular)nodeInfo).getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) || 
					((Titular)nodeInfo).getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosTitular();
			}
		}
	
		return correcto;
	}
	
	private boolean validacionDatosMinimosYCorrectosValidarButton(Object nodeInfo) {
		boolean correcto = true;

		if (nodeInfo instanceof BienInmuebleJuridico) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((BienInmuebleJuridico)nodeInfo).getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_ALTA) || 
					((BienInmuebleJuridico)nodeInfo).getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosBI();
			}
		}
		if (nodeInfo instanceof UnidadConstructivaCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((UnidadConstructivaCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((UnidadConstructivaCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = ucPanel.datosMinimosYCorrectosUC();
			}
		}
		if (nodeInfo instanceof SueloCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((SueloCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((SueloCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = sueloPanel.datosMinimosYCorrectosSuelo();
			}
		}
		if (nodeInfo instanceof Cultivo) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((Cultivo)nodeInfo).getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_ALTA) || 
					((Cultivo)nodeInfo).getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosCultivo();
			}
		}
		if (nodeInfo instanceof ConstruccionCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((ConstruccionCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((ConstruccionCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosLocal();
			}
		}
		if (nodeInfo instanceof Titular) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((Titular)nodeInfo).getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) || 
					((Titular)nodeInfo).getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				correcto = parcelaPanel.datosMinimosYCorrectosTitular();
			}
		}
	
		return correcto;
	}
	
	
	public boolean datosMinimosYCorrectosBI(Object nodeInfo)
    {
		BienInmuebleJuridico bienInmuebleJuridico = (BienInmuebleJuridico)nodeInfo;
		
        return (bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getNumCargo()!=null && !bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getNumCargo().equalsIgnoreCase("")) &&
                (bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getDigControl1()!=null && !bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getDigControl1().equalsIgnoreCase("")) &&
                (bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getDigControl2()!=null && !bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getDigControl2().equalsIgnoreCase(""));
    }
	
	public boolean datosMinimosYCorrectosCultivo(Object nodeInfo)
    {
		Cultivo cultivo = (Cultivo)nodeInfo;
		 //cultivo.getIdCultivo().setCalifCultivo(
        return (cultivo.getCodSubparcela()!=null && !cultivo.getCodSubparcela().equalsIgnoreCase(""))
                &&(cultivo.getIdCultivo().getCalifCultivo() != null && !cultivo.getIdCultivo().getCalifCultivo().equalsIgnoreCase(""));
    }

    public boolean datosMinimosYCorrectosTitular(Object nodeInfo)
    {
    	Titular titular = (Titular)nodeInfo;
        return (titular.getNif()!=null && !titular.getNif().equalsIgnoreCase(""))
                &&(titular.getRazonSocial() != null && !titular.getRazonSocial().equalsIgnoreCase(""));
    }
    
    public boolean datosMinimosYCorrectosLocal(Object nodeInfo)
    {
    	ConstruccionCatastro construccion = (ConstruccionCatastro)nodeInfo;
        return (construccion.getNumOrdenConstruccion()!=null && !construccion.getNumOrdenConstruccion().equalsIgnoreCase(""));
    }
	
    public boolean datosMinimosYCorrectosUC(Object nodeInfo)
    {
    	UnidadConstructivaCatastro unidadCons = (UnidadConstructivaCatastro)nodeInfo;
    	boolean okLoc=true;
    	//Panel Localización Urbana obligatoria
    	if( (unidadCons.getDirUnidadConstructiva().getTipoVia()!=null &&!(unidadCons.getDirUnidadConstructiva().getTipoVia().equals("")))
    			|| (unidadCons.getDirUnidadConstructiva().getNombreVia()!=null && !unidadCons.getDirUnidadConstructiva().getNombreVia().equalsIgnoreCase(""))
    			//|| (unidadCons.getDirUnidadConstructiva().getPrimerNumero()!=null && !jTextFieldNumero.getText().equalsIgnoreCase(""))
    			|| (unidadCons.getDirUnidadConstructiva().getPrimeraLetra()!=null && !unidadCons.getDirUnidadConstructiva().getPrimeraLetra().equalsIgnoreCase(""))
    			//|| (jTextFieldNumeroD.getText()!=null && !jTextFieldNumeroD.getText().equalsIgnoreCase(""))
    			|| (unidadCons.getDirUnidadConstructiva().getSegundaLetra()!=null && !unidadCons.getDirUnidadConstructiva().getSegundaLetra().equalsIgnoreCase(""))
    			|| (unidadCons.getDirUnidadConstructiva().getDireccionNoEstructurada()!=null && !unidadCons.getDirUnidadConstructiva().getDireccionNoEstructurada().equalsIgnoreCase(""))){
    			//|| (unidadCons.getDirUnidadConstructiva().getKilometro()!=null && !jTextFieldKm.getText().equalsIgnoreCase("")) ){

    	        okLoc = ( unidadCons.getDirUnidadConstructiva().getTipoVia()!=null && !(unidadCons.getDirUnidadConstructiva().getTipoVia().equals(""))) &&
	        		(unidadCons.getDirUnidadConstructiva().getNombreVia()!=null && !unidadCons.getDirUnidadConstructiva().getNombreVia().equalsIgnoreCase(""));
    	}
    	
    	PonenciaTramos ponenciaTramos = unidadCons.getDatosEconomicos().getTramoPonencia();
    	boolean okPonencia = true;
    	if((ponenciaTramos != null && ponenciaTramos.getCodTramo()!=null &&  !ponenciaTramos.getCodTramo().equals(""))
				|| (unidadCons.getDatosEconomicos().getCodViaPonencia()!=null && !unidadCons.getDatosEconomicos().getCodViaPonencia().equalsIgnoreCase(""))){    		
        		okPonencia=(ponenciaTramos.getCodTramo()!=null && !ponenciaTramos.getCodTramo().equals(""))
        					&& (unidadCons.getDatosEconomicos().getCodViaPonencia()!=null && !unidadCons.getDatosEconomicos().getCodViaPonencia().equalsIgnoreCase(""));
    	}    	
    	
    	String anioConstruccion = String.valueOf(unidadCons.getDatosFisicos().getAnioConstruccion());
    	if((anioConstruccion!=null && anioConstruccion.length()!=0 && anioConstruccion.length()!=4))
    		JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Error.J7"));

        return  okLoc && okPonencia && (unidadCons.getDatosFisicos().getSupOcupada()!=null && unidadCons.getDatosFisicos().getSupOcupada()>-1) &&
        		(anioConstruccion!=null && anioConstruccion.length()==4) &&
        		(unidadCons.getRefParcela().getRefCatastral1()!=null && !unidadCons.getRefParcela().getRefCatastral1().equalsIgnoreCase("")) &&
        		(unidadCons.getRefParcela().getRefCatastral2()!=null && !unidadCons.getRefParcela().getRefCatastral2().equalsIgnoreCase("")) &&
        		(unidadCons.getCodUnidadConstructiva()!=null && !unidadCons.getCodUnidadConstructiva().equalsIgnoreCase("")) &&
        		(unidadCons.getDatosEconomicos().getCorrectorConservacion()!=null && !unidadCons.getDatosEconomicos().getCorrectorConservacion().equalsIgnoreCase("")) &&
        		(unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null ) &&
        		(unidadCons.getDatosEconomicos().getNumFachadas()!=null && !unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("")) &&
        		(unidadCons.getTipoUnidad()!=null && !unidadCons.getTipoUnidad().equalsIgnoreCase(""));
        		
    }
    
    public boolean datosMinimosYCorrectosSuelo(Object nodeInfo)
    {
    	SueloCatastro suelo = (SueloCatastro)nodeInfo;
    	boolean okPonencia = false;   
    	if((suelo.getDatosEconomicos().getTramos().getCodTramo()!=null && !suelo.getDatosEconomicos().getTramos().getCodTramo().equalsIgnoreCase(""))
				&& (suelo.getDatosEconomicos().getCodViaPonencia()!=null && !suelo.getDatosEconomicos().getCodViaPonencia().equalsIgnoreCase(""))){    		
        		okPonencia=true;
    	}
    	else{
    		if((suelo.getDatosEconomicos().getTramos().getCodTramo()==null  || 
	    			(suelo.getDatosEconomicos().getTramos().getCodTramo()!=null && suelo.getDatosEconomicos().getTramos().getCodTramo().equals("")))
					&& (suelo.getDatosEconomicos().getCodViaPonencia()==null || suelo.getDatosEconomicos().getCodViaPonencia().equalsIgnoreCase(""))
					&& (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor()!=null && !suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().equalsIgnoreCase(""))){
	    		okPonencia=true;
	    	}
    	}
        return  okPonencia && (suelo.getRefParcela().getRefCatastral1()!=null && !suelo.getRefParcela().getRefCatastral1().equalsIgnoreCase("")) &&
        		(suelo.getRefParcela().getRefCatastral2()!=null && !suelo.getRefParcela().getRefCatastral2().equalsIgnoreCase("")) &&
        		(EdicionUtils.getStringValue(suelo.getDatosFisicos().getSupOcupada())!=null && !EdicionUtils.getStringValue(suelo.getDatosFisicos().getSupOcupada()).equalsIgnoreCase("")) &&
        		(suelo.getNumOrden()!=null && !suelo.getNumOrden().equalsIgnoreCase("")) &&
        		(EdicionUtils.getStringValue(suelo.getDatosEconomicos().getCorrectorAprecDeprec())!=null && !EdicionUtils.getStringValue(suelo.getDatosEconomicos().getCorrectorAprecDeprec()).equalsIgnoreCase("")) &&
        		(EdicionUtils.getStringValue(suelo.getDatosEconomicos().getCorrectorCargasSingulares())!=null && !EdicionUtils.getStringValue(suelo.getDatosEconomicos().getCorrectorCargasSingulares()).equalsIgnoreCase("")) &&
        		(suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona()!=null && !suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona().equalsIgnoreCase("")) &&
        		(suelo.getDatosEconomicos().getCodTipoValor()!=null && !suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("")) &&
        		(suelo.getDatosEconomicos().getNumFachadas()!=null && !suelo.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("")) &&
        		(suelo.getDatosFisicos().getTipoFachada()!=null && !suelo.getDatosFisicos().getTipoFachada().equalsIgnoreCase(""));
        
    }
    
    private StringBuffer valDatosMinimosBienSB = new StringBuffer();
    private StringBuffer valDatosMinimosUCSB = new StringBuffer();
    private StringBuffer valDatosMinimosSueloSB = new StringBuffer();
    private StringBuffer valDatosMinimosCultivoSB = new StringBuffer();
    private StringBuffer valDatosMinimosConstruccionSB = new StringBuffer();
    private StringBuffer valDatosMinimoTitularsSB = new StringBuffer();
    private boolean existeDatosMinimosBien = true;
    private boolean existeDatosMinimosUC = true;
    private boolean existeDatosMinimosSuelo = true;
    private boolean existeDatosMinimosCultivo = true;
    private boolean existeDatosMinimosConstruccion = true;
    private boolean existeDatosMinimosTitular = true;
    
	private void validacionDatosMinimosYCorrectosButtonValidar(Object nodeInfo) {

		if (nodeInfo instanceof BienInmuebleJuridico) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((BienInmuebleJuridico)nodeInfo).getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_ALTA) || 
					((BienInmuebleJuridico)nodeInfo).getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				//correcto = datosMinimosYCorrectosBI(nodeInfo);
				if(!datosMinimosYCorrectosBI(nodeInfo)){
					evaluandoDatosCorrectos = false;
					if(existeDatosMinimosBien){
						valDatosMinimosBienSB.append("\n\r\t"+I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos.bien")+"\n\r");
						existeDatosMinimosBien = false;
					}
					valDatosMinimosBienSB.append("\t\t"+((BienInmuebleJuridico)nodeInfo).getBienInmueble().getIdBienInmueble().getIdBienInmueble()+"\n\r");
				}
			}
		}
		if (nodeInfo instanceof UnidadConstructivaCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((UnidadConstructivaCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((UnidadConstructivaCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				//correcto = datosMinimosYCorrectosUC(nodeInfo);
				if(!datosMinimosYCorrectosUC(nodeInfo)){
					evaluandoDatosCorrectos = false;
					if(existeDatosMinimosUC){
						valDatosMinimosUCSB.append("\n\r\t"+I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos.uc")+"\n\r");
						existeDatosMinimosUC = false;
					}
					valDatosMinimosUCSB.append(	"\t\t"+((UnidadConstructivaCatastro)nodeInfo).getCodUnidadConstructiva()+"\n\r");
				}
			}
		}
		if (nodeInfo instanceof SueloCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((SueloCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((SueloCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				if(!datosMinimosYCorrectosSuelo(nodeInfo)){
					evaluandoDatosCorrectos = false;
					if(existeDatosMinimosSuelo){
						valDatosMinimosSueloSB.append("\n\r\t"+I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos.suelo")+"\n\r");
						existeDatosMinimosSuelo = false;
					}
					valDatosMinimosSueloSB.append("\t\t"+((SueloCatastro)nodeInfo).getNumOrden()+"\n\r");
				}
			}
		}
		if (nodeInfo instanceof Cultivo) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((Cultivo)nodeInfo).getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_ALTA) || 
					((Cultivo)nodeInfo).getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				//correcto = datosMinimosYCorrectosCultivo(nodeInfo);
				if(!datosMinimosYCorrectosCultivo(nodeInfo)){
					evaluandoDatosCorrectos = false;
					if(existeDatosMinimosCultivo){
						valDatosMinimosCultivoSB.append("\n\r\t"+I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos.cultivo")+"\n\r");
						existeDatosMinimosCultivo = false;
					}
					valDatosMinimosCultivoSB.append("\t\t"+((Cultivo)nodeInfo).getCodSubparcela()+"\n\r");
				}
			}
		}
		if (nodeInfo instanceof ConstruccionCatastro) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((ConstruccionCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_ALTA) || 
					((ConstruccionCatastro)nodeInfo).getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				//correcto = datosMinimosYCorrectosLocal(nodeInfo);
				if(!datosMinimosYCorrectosLocal(nodeInfo)){
					evaluandoDatosCorrectos = false;
					if(existeDatosMinimosConstruccion){
						valDatosMinimosConstruccionSB.append("\n\r\t"+I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos.construccion")+"\n\r");
						existeDatosMinimosConstruccion = false;
					}
					valDatosMinimosConstruccionSB.append("\t\t"+((ConstruccionCatastro)nodeInfo).getNumOrdenConstruccion()+"\n\r");
				}
			}
		}
		if (nodeInfo instanceof Titular) {
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") || 
					!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales") &&
					(((Titular)nodeInfo).getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) || 
					((Titular)nodeInfo).getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF))){
				//se revisa los datos minimos y correctos si el expediente es de situaciones finales
				// o si el expediente es orientado a variaciones y el tipo de movimiento de la entidad es de Alta o Modificación
				//correcto = datosMinimosYCorrectosTitular(nodeInfo);
				if(!datosMinimosYCorrectosTitular(nodeInfo)){
					evaluandoDatosCorrectos = false;
					if(existeDatosMinimosTitular){
						valDatosMinimoTitularsSB.append("\n\r\t"+I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.validar.datosMinimos.titular")+"\n\r");
						existeDatosMinimosTitular = false;
					}
					valDatosMinimoTitularsSB.append("\t\t"+((Titular)nodeInfo).getNif()+"\n\r");
				}
			}
		}
	}
	

	private boolean datosMinimosYCorrectos(DefaultMutableTreeNode node) {
		if (node != null) {
			if (!evaluandoDatosCorrectos) {
				boolean correcto = true;
				evaluandoDatosCorrectos = true;
				Object nodeInfo = node.getUserObject();
				//if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
					if (getPreNodeInfo() == node) {
						
						correcto = validacionDatosMinimosYCorrectos(nodeInfo);
					}
				/*}	
				else{
					if (getPreNodeInfo() == node) {
						correcto = validacionDatosMinimosYCorrectos(nodeInfo);
					}
				}
				*/
				if (!correcto) {
					/*
					 * TreePath path = new TreePath(node.getPath());
					 * tree.setSelectionPath(path);
					 */
					JOptionPane
							.showMessageDialog(
									this,
									I18N
											.get("Expedientes",
													"Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
				}
				evaluandoDatosCorrectos = false;
				return correcto;
			}
			return false;
		}
		return true;
	}
	
	private void datosMinimosYCorrectosButtonValidar(DefaultMutableTreeNode node) {
		if (node != null) {
			//if (!evaluandoDatosCorrectos) {
				//boolean correcto = true;
				//evaluandoDatosCorrectos = true;
				Object nodeInfo = node.getUserObject();
							
				validacionDatosMinimosYCorrectosButtonValidar(nodeInfo);
				
//				if (!correcto) {
//					/*
//					 * TreePath path = new TreePath(node.getPath());
//					 * tree.setSelectionPath(path);
//					 */
//					JOptionPane.showMessageDialog(this,
//									I18N.get("Expedientes","Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
//				}
			//	evaluandoDatosCorrectos = false;
				//return correcto;
			//}
			//return false;
		}
		//return true;
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
	
	private void deshabilitarTodosBotones(){
		
		EdicionUtils.disabledButtons(parcelaPanel, "_nuevafinca");
		EdicionUtils.disabledButtons(parcelaPanel, "_Cmasdatosfinca");
		EdicionUtils.disabledButtons(parcelaPanel, "_borrarfinca");
		
		EdicionUtils.disabledButtons(parcelaPanel, "_nuevobien");
		EdicionUtils.disabledButtons(parcelaPanel, "_Cmasdatosinmueble");
		EdicionUtils.disabledButtons(parcelaPanel, "_borrarbien");
		EdicionUtils.disabledButtons(parcelaPanel, "_definirrepresentante");
		
		EdicionUtils.disabledButtons(parcelaPanel, "_nuevolocal");
		EdicionUtils.disabledButtons(parcelaPanel, "_localcomun");
		EdicionUtils.disabledButtons(parcelaPanel, "_borrarlocal");
		EdicionUtils.disabledButtons(parcelaPanel, "_Cmasdatoslocales");
		
		EdicionUtils.disabledButtons(parcelaPanel, "_nuevocultivo");
		EdicionUtils.disabledButtons(parcelaPanel, "_Cmasdatoscultivo");
		EdicionUtils.disabledButtons(parcelaPanel, "_borrarcultivo");
		EdicionUtils.disabledButtons(parcelaPanel, "_localcomun");
		EdicionUtils.disabledButtons(parcelaPanel, "_cultivocomun");
		EdicionUtils.disabledButtons(parcelaPanel, "_Creparto");
		
		EdicionUtils.disabledButtons(parcelaPanel, "vistacultivo");
		EdicionUtils.disabledButtons(parcelaPanel, "vistalocal");
		
		EdicionUtils.disabledButtons(parcelaPanel, "_nuevotitular");
		EdicionUtils.disabledButtons(parcelaPanel, "_Cmasdatostitular");
		EdicionUtils.disabledButtons(parcelaPanel, "_borrartitular");
		EdicionUtils.disabledButtons(parcelaPanel, "_combienes");
		
		
		
	}
	private void deshabilitarTodosPaneles(){
		 EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), false);
		 EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosBienInmueble(), false);
		 EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosLocal(), false);
		 EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosCultivo(), false);
		 EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosTitular(), false);

		 EdicionUtils.enablePanel(ucPanel, false);
		 EdicionUtils.enablePanel(sueloPanel, false);
	}
	
	
	private void habilitarPaneles(Object nodeInfo){
		
		deshabilitarTodosPaneles();
		
		if (nodeInfo instanceof Expediente){
			EdicionUtils.enabledButtons(parcelaPanel, "_nuevafinca");
			
		}
		else if (nodeInfo instanceof FincaCatastro){
			
			/*if(((FincaCatastro)nodeInfo).isElementoModificado()){
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), true);
			}
			else{
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), false);
			}*/
			
			if(parcela.isElementoModificado()){
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), true);
			}
			else{
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosFinca(), false);
			}
		}
		else if(nodeInfo instanceof BienInmuebleJuridico){
			if(((BienInmuebleJuridico)nodeInfo).isElementoModificado()){
               // EdicionUtils.enablePanel(parcelaPanel.getJPanelBienInmuebleGlobal(), true);
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosBienInmueble(), true);
			}
			else{
                //EdicionUtils.enablePanel(parcelaPanel.getJPanelBienInmuebleGlobal(), false);
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosBienInmueble(), false);
			}
		
		}
		else if(nodeInfo instanceof ConstruccionCatastro){
			if(((ConstruccionCatastro)nodeInfo).isElementoModificado()){
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosLocal(), true);
			}
			else{
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosLocal(), false);
			}
			
		}
		
		else if(nodeInfo instanceof Cultivo){
			if(((Cultivo)nodeInfo).isElementoModificado()){
                EdicionUtils.enablePanel(parcelaPanel.getJPanelCultivo(), true);
			}
			else{
                EdicionUtils.enablePanel(parcelaPanel.getJPanelCultivo(), false);
			}
		
		}
		else if(nodeInfo instanceof Titular){
			if(((Titular)nodeInfo).isElementoModificado()){
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosTitular(), true);
			}
			else{
                EdicionUtils.enablePanel(parcelaPanel.getJPanelDatosTitular(), false);
			}

		}
	
		else if(nodeInfo instanceof BienInmuebleCatastro){
			if(((BienInmuebleCatastro)nodeInfo).isElementoModificado()){
                EdicionUtils.enablePanel(parcelaPanel.getJPanelBienInmueble(), true);
			}
			else{
                EdicionUtils.enablePanel(parcelaPanel.getJPanelBienInmueble(), false);
			}
			
		}
		else if(nodeInfo instanceof UnidadConstructivaCatastro){
			if(((UnidadConstructivaCatastro)nodeInfo).isElementoModificado()){
                EdicionUtils.enablePanel(ucPanel, true);
			}
			else{
                EdicionUtils.enablePanel(ucPanel, false);
			}
			
			EdicionUtils.enabledButtons(ucPanel, "_modificarUC");
			EdicionUtils.enabledButtons(ucPanel, "_nuevoUC");
			EdicionUtils.enabledButtons(ucPanel, "_anadirSuelo");
			EdicionUtils.enabledButtons(ucPanel, "_eliminarUC");
			
		}
		else if(nodeInfo instanceof SueloCatastro){
			if(((SueloCatastro)nodeInfo).isElementoModificado()){
                EdicionUtils.enablePanel(sueloPanel, true);
			}
			else{
                EdicionUtils.enablePanel(sueloPanel, false);
			}
			
			EdicionUtils.enabledButtons(sueloPanel, "_nuevoSuelo");
			EdicionUtils.enabledButtons(sueloPanel, "_modificarSuelo");
			EdicionUtils.enabledButtons(sueloPanel, "_anadirSuelo");
			EdicionUtils.enabledButtons(sueloPanel, "_eliminarSuelo");
			
		}
		
		if (nodeInfo instanceof FincaCatastro){
			FincaCatastro finc = (FincaCatastro)nodeInfo;
			// Unidad Constructiva
			if(finc.getLstUnidadesConstructivas() != null  && !finc.getLstUnidadesConstructivas().isEmpty()){
				EdicionUtils.enabledButtons(ucPanel, "_eliminarUC");
			}
			else{
				EdicionUtils.disabledButtons(ucPanel, "_eliminarUC");
			}
			
			//Suelo
			if(finc.getLstSuelos() != null && !finc.getLstSuelos().isEmpty()){
				EdicionUtils.enabledButtons(sueloPanel, "_eliminarSuelo");
			}
			else{
				EdicionUtils.disabledButtons(sueloPanel, "_eliminarSuelo");
			}
			
		}

		EdicionUtils.enabledButtons(ucPanel, "_nuevoUC");
		EdicionUtils.enabledButtons(sueloPanel, "_nuevoSuelo");


		
		
	}
	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public FincaPanel getParcelaPanel() {
		return parcelaPanel;
	}

	public void setParcelaPanel(FincaPanel parcelaPanel) {
		this.parcelaPanel = parcelaPanel;
	}

	public SueloPanel getSueloPanel() {
		return sueloPanel;
	}


	
	public void borrarChildNodos(DefaultMutableTreeNode nodo, String refCat) {	
		DefaultMutableTreeNode nodeEncontrado = null;
		if(nodo.getUserObject() instanceof FincaCatastro){
			if(((FincaCatastro)nodo.getUserObject()).getRefFinca().getRefCatastral().equals(refCat)){
				//tree.setSelectionRow(nodo.getLevel());
				nodo.removeAllChildren();

			}
		}
		else if(nodo.getUserObject() instanceof BienInmuebleJuridico){
			if(((BienInmuebleJuridico)nodo.getUserObject()).getBienInmueble().getIdBienInmueble().getIdBienInmueble().equals(refCat)){
				//tree.setSelectionRow(nodo.getLevel());
				nodo.removeAllChildren();
				
			}
		}
		if (nodo.getChildCount() >= 0) { 
			for (Enumeration e=nodo.children(); e.hasMoreElements(); ) { 
				DefaultMutableTreeNode n = (DefaultMutableTreeNode)e.nextElement(); 
				borrarChildNodos(n, refCat); 
			} 
		} 
	}
	

} // @jve:decl-index=0:visual-constraint="5,4"
