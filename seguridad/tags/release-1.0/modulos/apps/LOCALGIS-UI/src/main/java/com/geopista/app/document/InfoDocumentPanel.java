package com.geopista.app.document;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.licencias.CUtilidadesComponentes_LCGIII;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.client.alfresco.ui.AlfrescoExplorer;
import com.geopista.client.alfresco.utils.implementations.LocalgisIntegrationManagerImpl;
import com.geopista.client.alfresco.utils.interfaces.LocalgisIntegrationManager;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.ui.dialogs.DocumentDialog;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class InfoDocumentPanel extends JPanel implements DocumentInterface,
		FeatureExtendedPanel {
	private static final Log logger = LogFactory
			.getLog(InfoDocumentPanel.class);

	/* Paneles */
	private JPanel jPanelButtons = new JPanel();
	private DocumentPanel panelDocumentos = null;
	private JPanelComentarios panelComentarios = null;

	/* Botones jPanelButtons */
	private JButton bAnadir = new JButton();
	private JButton bModificar = new JButton();
	private JButton bBorrar = new JButton();
	private JButton bGuardar = new JButton();
	private JButton bVisualizar = new JButton();
	// NUEVO
	private JButton bAlfrescoManager = new JButton();
	// FIN NUEVO
	private DocumentClient documentClient = null;
	private AppContext aplicacion;
	private Vector vPaneles = new Vector();
	private DocumentBean document;
	private DocumentBean auxDocumento;
	private FeatureDialogHome fd;

	/* Necesario xa mostrar la pantalla del reloj */
	private DocumentBean auxDocument = new DocumentBean();

	/* Alfresco */
	private LocalgisIntegrationManager localgisIntegrationManager;

	DocumentInterface docInt;
	JTabbedPane jTabbedPane = null;

	/* constructor de la clase q llama al método que inicializa el panel */
	public InfoDocumentPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			logger.error("Error en la llamada al constructor de la clase ", e);
		}
	}

	/* método xa inicializar el panel */
	private void jbInit() throws Exception {
		aplicacion = (AppContext) AppContext.getApplicationContext();

		if (AlfrescoManagerUtils.isAlfrescoActive()){
			localgisIntegrationManager = new LocalgisIntegrationManagerImpl(
					aplicacion.getUserPreference(AppContext.HOST_ADMCAR, "",
							false),
					String.valueOf(aplicacion.getIdMunicipio()),
					AlfrescoConstants.APP_GENERAL);
		}

		this.setName(aplicacion
				.getI18nString("document.infodocument.panel.listado"));
		this.setLayout(new BorderLayout());
		//this.setSize(new Dimension(800, 550));

		loadDocumentPanel();		
		
		/**
		 * Comentarios Panel
		 */
		JPanelComentarios jPanelComentarios = new JPanelComentarios();
		jPanelComentarios.setEnabled(false);
		this.add(jPanelComentarios, BorderLayout.CENTER);
		panelComentarios = jPanelComentarios;

		/**
		 * Botones Panel
		 */
		jPanelButtons.setLayout(new FlowLayout());
		/* Componentes */
		bModificar.setText(aplicacion
				.getI18nString("document.infodocument.botones.modificar"));
		bModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyDocument();
			}
		});
		bAnadir.setText(aplicacion
				.getI18nString("document.infodocument.botones.anadir"));
		bAnadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addDocument();
			}
		});
		bBorrar.setText(aplicacion
				.getI18nString("document.infodocument.botones.borrar"));
		bBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});
		bGuardar.setText(aplicacion
				.getI18nString("document.infodocument.botones.guardar"));
		bGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		bVisualizar.setText(aplicacion
				.getI18nString("document.infodocument.botones.visualizar"));
		bVisualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizar();
			}
		});

		/* Añadimos los componentes al panel */
		jPanelButtons.add(bAnadir);
		jPanelButtons.add(bModificar);
		jPanelButtons.add(bBorrar);
		jPanelButtons.add(bGuardar);
		jPanelButtons.add(bVisualizar);

		if (AlfrescoManagerUtils.isAlfrescoActive()) {
			// bAlfrescoManager.setText(aplicacion.getI18nString("document.infodocument.botones.visualizar"));
			bAlfrescoManager.setText("Gestor Documental");
			bAlfrescoManager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					docManager();
				}
			});
			jPanelButtons.add(bAlfrescoManager);
		}

		/* Añadimos los paneles */
		this.add(jPanelButtons, BorderLayout.SOUTH);
	}
	
	private void loadDocumentPanel(){
		/* Cargamos la lista */
		Blackboard identificadores = aplicacion.getBlackboard();
		final Hashtable hFeaturesDocs = new Hashtable();
		/* devuelve un array */
		final Object[] lista = (Object[]) identificadores.get("feature");
		String sUrl = aplicacion
				.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
				+ ServletConstants.DOCUMENT_SERVLET_NAME;
		documentClient = new DocumentClient(sUrl);

		if (lista != null) {
			if (lista.length == 1) {
				try {
					GeopistaFeature feature = (GeopistaFeature) lista[0];
					Collection collection = documentClient
							.getAttachedDocuments(feature);
					if (feature.getLayer() == null
							|| (feature.getLayer() instanceof GeopistaLayer && ((GeopistaLayer) feature
									.getLayer()).isLocal())) {
						setEnabled(false);
					}
					if (collection == null) {
						collection = new ArrayList();
					}

					DocumentPanel jPanel = new DocumentPanel(feature,
							collection, this);
					this.add(jPanel, BorderLayout.NORTH);
					jPanel.addMouseListener(new ActionJList());
					panelDocumentos = jPanel;
					// vPaneles.add(jPanel);
					vPaneles.add(panelDocumentos);
				} catch (Exception e) {
					logger.error(
							"Error al mostrar los documentos de una feature ",
							e);
					ErrorDialog.show(aplicacion.getMainFrame(),
							aplicacion.getI18nString("SQLError.Titulo"),
							aplicacion.getI18nString("SQLError.Aviso"),
							StringUtil.stackTrace(e));
				}
			} else if (lista.length > 1) {
				final InfoDocumentPanel aux = this;
				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
						aplicacion.getMainFrame(), null);
				// progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.abrir"));
				// progressDialog.report(aplicacion.getI18nString("document.infodocument.abrir"));
				progressDialog.addComponentListener(new ComponentAdapter() {
					public void componentShown(ComponentEvent e) {
						new Thread(new Runnable() {
							public void run() {
								try {
									jTabbedPane = new JTabbedPane();
									jTabbedPane
											.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
									for (int i = 0; i < lista.length; i++) {

										// MOD --> ojo i18n.
										progressDialog.report(i, lista.length,
												"Features cargadas");
										GeopistaFeature feature = (GeopistaFeature) lista[i];
										Collection collection = documentClient
												.getAttachedDocuments(feature);
										DocumentPanel jDocumentPanel = new DocumentPanel(
												feature, collection, aux);
										jDocumentPanel
												.addMouseListener(new ActionJList());
										hFeaturesDocs.put(
												feature.getSystemId(),
												collection);
										/*
										 * mostramos la id de la feature en el
										 * panel xa distinguirlas
										 */
										String nombre = "FID:"
												+ feature.getSystemId();
										jTabbedPane.addTab(nombre,
												jDocumentPanel);
										aux.add(jTabbedPane, BorderLayout.NORTH);
										vPaneles.add(jDocumentPanel);
									}
									jTabbedPane
											.addChangeListener(new ChangeListener() {
												public void stateChanged(
														ChangeEvent evt) {
													JTabbedPane pane = (JTabbedPane) evt
															.getSource();
													int sel = pane
															.getSelectedIndex();
													panelDocumentos = (DocumentPanel) vPaneles
															.get(sel);
													if (fd instanceof DocumentDialog)
														((DocumentDialog) fd)
																.setDescription();
												}
											});
									jTabbedPane.setSelectedIndex(0);
									panelDocumentos = (DocumentPanel) vPaneles
											.get(0);
								} catch (Exception e) {
									logger.error(
											"Error al mostrar los documentos de varias features ",
											e);
									ErrorDialog.show(
											aplicacion.getMainFrame(),
											aplicacion
													.getI18nString("SQLError.Titulo"),
											aplicacion
													.getI18nString("SQLError.Aviso"),
											StringUtil.stackTrace(e));
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
	}

	public void setFd(FeatureDialogHome fd) {
		this.fd = fd;
	}

	/* método xa eliminar el documento seleccionado de la lista */
	private void eliminar() {
		if (panelDocumentos.getdocumentSelected() == null)
			return;
		int n = JOptionPane
				.showOptionDialog(
						aplicacion.getMainFrame(),
						aplicacion
								.getI18nString("document.infodocument.respuesta.mensaje"),
						"", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (n == JOptionPane.NO_OPTION)
			return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion
				.getI18nString("document.infodocument.salvando.title"));
		progressDialog.report(aplicacion
				.getI18nString("document.infodocument.salvando"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							GeopistaFeature feature = new GeopistaFeature();
							feature = panelDocumentos.getgFeature();
							DocumentBean document = (DocumentBean) panelDocumentos
									.getdocumentSelected();
							documentClient.detachDocument(feature, document);
							/* borrar el elemento de la lista */
							panelDocumentos.borrar();
							panelComentarios.load(null);
						} catch (Exception e) {
							logger.error("Error al eliminar un documento ", e);
							ErrorDialog.show(
									aplicacion.getMainFrame(),
									aplicacion.getI18nString("SQLError.Titulo"),
									aplicacion.getI18nString("SQLError.Aviso"),
									StringUtil.stackTrace(e));
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

	/*
	 * método q realiza las llamadas correspondientes xa añadir nuevos
	 * documentos
	 */
	private void addDocument() {
		if (!AlfrescoManagerUtils.isAlfrescoActive()) {
			JFrameOpciones frameOpciones = new JFrameOpciones(
					aplicacion.getMainFrame());
			frameOpciones.show();
			if (!frameOpciones.isAceptar())
				return;
			if (frameOpciones.isLocal())
				// FIN NUEVO
				anadirlocal();
			else
				anadirBD();
		} else
			anadirlocal();
	}

	/* método xa visualizar en un editor el documento seleccionado de una lista */
	private void visualizar() {
		// String tempdir = System.getProperty("java.io.tmpdir");
		// System.out.println("Directorio temporal:"+tempdir);

		// System.out.println("Visualizando documentos");
		if (panelDocumentos.getdocumentSelected() == null)
			return;
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion
				.getI18nString("document.infodocument.abrir"));
		progressDialog.report(aplicacion
				.getI18nString("document.infodocument.abrir"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							auxDocumento = panelDocumentos
									.getdocumentSelected();
							// NUEVO
							if (!AlfrescoManagerUtils.isAlfrescoActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocumento.getId(), auxDocumento.getIdMunicipio())) 
								auxDocumento.setContent(documentClient
										.getAttachedByteStream(auxDocumento));
							// FIN NUEVO
						} catch (Exception e) {
							logger.error(
									"Error al mostrar la pantalla de reloj ", e);
							ErrorDialog.show(
									aplicacion.getMainFrame(),
									aplicacion.getI18nString("SQLError.Titulo"),
									aplicacion.getI18nString("SQLError.Aviso"),
									StringUtil.stackTrace(e));
						} finally {
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});

		try {
			GUIUtil.centreOnWindow(progressDialog);
			progressDialog.setVisible(true);
			if (auxDocumento == null)
				return;
			// String selectedFile =
			// seleccionaFichero(auxDocumento.getFileName());
			// System.out.println("Nombre del documento:"+auxDocumento.getFileName());
			if (auxDocumento.getFileName() == null)
				return;
			// File tempFile=File.createTempFile(auxDocumento.getFileName(),
			// null);
			String nombreFicheroTemporal = System.getProperty("java.io.tmpdir")
					+ auxDocumento.getFileName();
			// NUEVO
			if (!AlfrescoManagerUtils.isAlfrescoActive()) {
				RandomAccessFile outFile = new RandomAccessFile(
						nombreFicheroTemporal, "rw");

				if (auxDocumento == null)
					return;

				outFile.write(auxDocumento.getContent());
				outFile.close();
			} else {
				if (localgisIntegrationManager.downloadAssociateDocument(
						auxDocumento.getId(), nombreFicheroTemporal))
					System.out.println("BIEN");
				else
					System.out.println("MAL");
			}
			// FIN NUEVO

			/* visualizamos si el SO es Windows */
			if (CUtilidadesComponentes_LCGIII.isWindows()) {
				try {
					Runtime.getRuntime().exec(
							"rundll32 SHELL32.DLL,ShellExec_RunDLL \""
									+ nombreFicheroTemporal + "\"");
				} catch (Exception ex) {
					logger.error("Error al abrir el documento : ", ex);
				}
			}
		} catch (Exception e) {
			logger.error("Error al visualizar un documento ", e);
			ErrorDialog.show(aplicacion.getMainFrame(),
					aplicacion.getI18nString("SQLError.Titulo"),
					aplicacion.getI18nString("SQLError.Aviso"),
					StringUtil.stackTrace(e));
		}
	}

	/* método xa visualizar en un editor el documento seleccionado de una lista */
	private void guardar() {
		// String tempdir = System.getProperty("java.io.tmpdir");
		// System.out.println("Directorio temporal:"+tempdir);

		if (panelDocumentos.getdocumentSelected() == null)
			return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion
				.getI18nString("document.infodocument.abrir"));
		progressDialog.report(aplicacion
				.getI18nString("document.infodocument.abrir"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							auxDocumento = panelDocumentos
									.getdocumentSelected();
							// NUEVO
							if (!AlfrescoManagerUtils.isAlfrescoActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocumento.getId(), auxDocumento.getIdMunicipio()))
								auxDocumento.setContent(documentClient
										.getAttachedByteStream(auxDocumento));
							// FIN NUEVO
						} catch (Exception e) {
							logger.error(
									"Error al mostrar la pantalla de reloj ", e);
							ErrorDialog.show(
									aplicacion.getMainFrame(),
									aplicacion.getI18nString("SQLError.Titulo"),
									aplicacion.getI18nString("SQLError.Aviso"),
									StringUtil.stackTrace(e));
						} finally {
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		try {
			GUIUtil.centreOnWindow(progressDialog);
			progressDialog.setVisible(true);
			// System.out.println("Nombre del documento en guardar:"+auxDocumento.getFileName());

			String selectedFile = seleccionaFichero(auxDocumento.getFileName());
			if (selectedFile == null)
				return;
			// NUEVO
			if (!AlfrescoManagerUtils.isAlfrescoActive()) {
				RandomAccessFile outFile = new RandomAccessFile(selectedFile,
						"rw");

				if (auxDocumento == null)
					return;

				outFile.write(auxDocumento.getContent());
				outFile.close();
			} else {
				if (localgisIntegrationManager.downloadAssociateDocument(
						auxDocumento.getId(), selectedFile))
					// CAMBIAR
					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
							"Documento descargado correctamente");
				else
					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
							"Error al descargar el documento");

			}
			// FIN NUEVO
			/* visualizamos si el SO es Windows */
			if (CUtilidadesComponentes_LCGIII.isWindows()) {
				try {
					Runtime.getRuntime().exec(
							"rundll32 SHELL32.DLL,ShellExec_RunDLL \""
									+ selectedFile + "\"");
				} catch (Exception ex) {
					logger.error("Error al abrir el documento : ", ex);
				}
			}
		} catch (Exception e) {
			logger.error("Error al visualizar un documento ", e);
			ErrorDialog.show(aplicacion.getMainFrame(),
					aplicacion.getI18nString("SQLError.Titulo"),
					aplicacion.getI18nString("SQLError.Aviso"),
					StringUtil.stackTrace(e));
		}
	}

	/*
	 * método xa abrir el directorio dd queremos dejar el documento que estamos
	 * abriendo xa visualizar
	 */
	private String seleccionaFichero(String filename) throws Exception {
		File f = new File(filename);
		/* Dialogo para seleccionar donde dejar el fichero */
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setSelectedFile(f);
		if (chooser.showSaveDialog(aplicacion.getMainFrame()) != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = chooser.getSelectedFile();
		if (selectedFile == null)
			return null;
		String tmpDir = "";
		String tmpFile = selectedFile.getAbsolutePath();
		if (tmpFile.lastIndexOf(selectedFile.getName()) != -1) {
			tmpDir = tmpFile.substring(0,
					tmpFile.lastIndexOf(selectedFile.getName()));
		}
		/** Comprobamos si existe el directorio. */
		try {
			File dir = new File(tmpDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} catch (Exception ex) {
			logger.error("Error al seleccionar un fichero ", ex);
		}
		return selectedFile.getAbsolutePath();
	}
	
	private void docManager() {
		(new AlfrescoExplorer(aplicacion.getMainFrame(),
				getSelectedFeatures(), this.getFeatureDocumentsId(), String.valueOf(aplicacion
						.getIdMunicipio()), AlfrescoConstants.APP_GENERAL))
				.setVisible(true);
		loadDocumentPanel();
	}

	/* Cuando nos movemos x la ventana de atributos */
	public void enter() {
		// De momento va vacío
	}

	/* De momento no se usa */
	public void exit() {
		// De momento va vacío
	}

	/* añadimos un documento en local */
	private void anadirlocal() {
		JDialogLocal jDialogLocal = new JDialogLocal(aplicacion.getMainFrame(),
				DocumentBean.DOC_CODE);
		jDialogLocal.show();
		if (!jDialogLocal.okCancelPanel.wasOKPressed())
			return;
		auxDocument = new DocumentBean();
		auxDocument = jDialogLocal.save(auxDocument);
		auxDocument.setIsDocument();
		if (auxDocument.getFileName() == null)
			return;

		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion
				.getI18nString("document.infodocument.salvando.title"));
		progressDialog.report(aplicacion
				.getI18nString("document.infodocument.salvando"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						/* añadimos el documento a la lista */
						try {
							Object[] array = getSelectedFeatures();
							if (!AlfrescoManagerUtils.isAlfrescoActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocumento.getId(), auxDocumento.getIdMunicipio())) {
								auxDocument = documentClient.attachDocument(
										array, auxDocument);
								setFeatureDocuments(auxDocument);
							} else {
								if (localgisIntegrationManager.associateAlfrescoDocument(
										String.valueOf(aplicacion
												.getIdMunicipio()),
										AlfrescoConstants.APP_GENERAL,
										auxDocument, documentClient, array)) {
									setFeatureDocuments(auxDocument);
									// CAMBIAR
									JOptionPane.showMessageDialog(
											aplicacion.getMainFrame(),
											"Documento añadido correctamente");
								} else
									JOptionPane.showMessageDialog(
											aplicacion.getMainFrame(),
											"Error al añadir el documento");
							}

						} catch (Exception e) {
							logger.error("Error al añadir el documento ", e);
							ErrorDialog.show(
									aplicacion.getMainFrame(),
									aplicacion.getI18nString("SQLError.Titulo"),
									aplicacion.getI18nString("SQLError.Aviso"),
									StringUtil.stackTrace(e));
							return;
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

	// NUEVO
	private Object[] getSelectedFeatures() {
		Object[] array = new GeopistaFeature[vPaneles.size()];
		int i = 0;
		for (Enumeration e = vPaneles.elements(); e.hasMoreElements();) {
			array[i] = ((DocumentPanel) e.nextElement()).getgFeature();
			i++;
		}
		return array;
	}
	// FIN NUEVO

	// NUEVO
	public void setFeatureDocuments(DocumentBean document) {
		for (Enumeration e = vPaneles.elements(); e.hasMoreElements();)
			((DocumentPanel) e.nextElement()).add(document);
	}
	// FIN NUEVO
	
	// NUEVO
	public ArrayList<String> getFeatureDocumentsId() {
		ArrayList<String> idDocuments = new ArrayList<String>();
		for (Enumeration e = vPaneles.elements(); e.hasMoreElements();){
			ArrayList<DocumentBean> documents = new ArrayList<DocumentBean>(((DocumentPanel) e.nextElement()).getDocumentos());	
			Iterator<DocumentBean> it = documents.iterator();
			while(it.hasNext()){
				DocumentBean documentBean = it.next();
				idDocuments.add(documentBean.getId());
			}
		}
		return idDocuments;
	}
	// FIN NUEVO

	/* añadimos un documento en BD */
	private void anadirBD() {
		final JDialogBD jDialogBD = new JDialogBD(aplicacion.getMainFrame(),
				DocumentBean.DOC_CODE);
		jDialogBD.show();
		if (!jDialogBD.okCancelPanel.wasOKPressed())
			return;
		auxDocument = jDialogBD.get();
		auxDocument.setIsDocument();
		if (auxDocument.getFileName() == null)
			return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion
				.getI18nString("document.infodocument.salvando.title"));
		progressDialog.report(aplicacion
				.getI18nString("document.infodocument.salvando"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							/* añadimos el documento a la BD */
							Vector lista = new Vector();
							for (Enumeration e = vPaneles.elements(); e
									.hasMoreElements();) {
								DocumentPanel auxDocumentPanel = (DocumentPanel) e
										.nextElement();
								if (!auxDocumentPanel
										.existDocument(auxDocument))
									lista.add(auxDocumentPanel.getgFeature());
								else {
									int n = JOptionPane.showOptionDialog(
											aplicacion.getMainFrame(),
											aplicacion
													.getI18nString("document.infodocument.aviso.mensaje")
													+ " "
													+ auxDocumentPanel
															.getgFeature()
															.getSystemId(), "",
											JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.WARNING_MESSAGE, null,
											null, null);
									if (n == JOptionPane.NO_OPTION)
										return;
								}
							}
							if (lista == null || lista.size() == 0)
								return;
							documentClient.linkDocument(lista, auxDocument);
							for (Enumeration e = vPaneles.elements(); e
									.hasMoreElements();) {
								DocumentPanel auxDocumentPanel = (DocumentPanel) e
										.nextElement();
								if (!auxDocumentPanel
										.existDocument(auxDocument))
									auxDocumentPanel.add(auxDocument);
							}
						} catch (Exception e) {
							logger.error(
									"Error al asociar el documento a la feature ",
									e);
							ErrorDialog.show(
									aplicacion.getMainFrame(),
									aplicacion.getI18nString("SQLError.Titulo"),
									aplicacion.getI18nString("SQLError.Aviso"),
									StringUtil.stackTrace(e));
							return;
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

	/*
	 * método xa cargar los comentarios y la opción de público asociados a un
	 * documento
	 */
	public void seleccionar(DocumentBean documento) {
		panelComentarios.load(documento);
	}

	/* método de la interfaz no implementado */
	public void seleccionar(DocumentBean documentBean, int indicePanel) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	/* modificamos un documento ya existente */
	private void modifyDocument() {
		final JDialogLocal jDialogLocal = new JDialogLocal(
				aplicacion.getMainFrame(), DocumentBean.DOC_CODE);
		if (panelDocumentos.getdocumentSelected() == null)
			return;
		document = (DocumentBean) panelDocumentos.getdocumentSelected().clone();
		jDialogLocal.load(document);
		jDialogLocal.show();
		if (!jDialogLocal.okCancelPanel.wasOKPressed())
			return;
		document = jDialogLocal.save(document);
		document.setIsDocument();
		document.setContent(null);
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion
				.getI18nString("document.infodocument.salvando.title"));
		progressDialog.report(aplicacion
				.getI18nString("document.infodocument.salvando"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							if (!AlfrescoManagerUtils.isAlfrescoActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocumento.getId(), auxDocumento.getIdMunicipio())) {
								if (jDialogLocal.isNewDocument()) {
									File file = new File(document.getFileName());
									document.setFileName(file.getName());
									documentClient.updateDocument(document,
											file);
								} else {
									documentClient
											.updateDocumentByteStream(document);
								}		
								//setFeatureDocuments(document);
								panelDocumentos.seleccionar(document);
							} 
							else {
								// CAMBIAR: Mirar si ya existe el documento (del
								// tipo ) o no y crear nueva version
								if (localgisIntegrationManager.associateAlfrescoDocument(
										String.valueOf(aplicacion
												.getIdMunicipio()),
										AlfrescoConstants.APP_GENERAL, panelDocumentos.getdocumentSelected().getFileName(),
										document, documentClient)){
									panelDocumentos.getDocumentos().remove(panelDocumentos.getdocumentSelected());									
									setFeatureDocuments(document);
									panelDocumentos.seleccionar(document);
									// CAMBIAR
									JOptionPane.showMessageDialog(
											aplicacion.getMainFrame(),
											"Documento modificado correctamente");
								}
								else
									JOptionPane.showMessageDialog(
											aplicacion.getMainFrame(),
											"Error al modificar el documento");
							}							
						} catch (Exception e) {
							logger.error("Error al modificar el documento ", e);
							ErrorDialog.show(
									aplicacion.getMainFrame(),
									aplicacion.getI18nString("SQLError.Titulo"),
									aplicacion.getI18nString("SQLError.Aviso"),
									StringUtil.stackTrace(e));
							return;
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

	/**
	 * si la feature no está accesible, deshabilitamos los botones con los q
	 * operamos sobre sus documentos
	 */
	public void setEnabled(boolean estado) {
		bAnadir.setEnabled(estado);
		bModificar.setEnabled(estado);
		bBorrar.setEnabled(estado);
		if (AlfrescoManagerUtils.isAlfrescoActive()) {
			bAlfrescoManager.setEnabled(estado);
		}
	}

	/* obtenemos la feature seleccionada */
	public GeopistaFeature getSelectedFeature() {
		if (panelDocumentos == null)
			return null;
		return panelDocumentos.getgFeature();
	}

	public void setFeature(GeopistaFeature feature) {
		Blackboard identificadores = aplicacion.getBlackboard();
		Hashtable hFeaturesDocs = new Hashtable();
		/* devuelve un array */
		Object[] lista = (Object[]) identificadores.get("feature");

		for (int i = 0; i < lista.length; i++) {
			GeopistaFeature featureTab = (GeopistaFeature) lista[i];
			if (featureTab.getSystemId() == feature.getSystemId()) {
				jTabbedPane.setSelectedIndex(i);
				panelDocumentos = (DocumentPanel) vPaneles.get(i);
			}
		}
	}

	class ActionJList extends MouseAdapter {
		public ActionJList() {
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				visualizar();
			}
		}
	}
}
