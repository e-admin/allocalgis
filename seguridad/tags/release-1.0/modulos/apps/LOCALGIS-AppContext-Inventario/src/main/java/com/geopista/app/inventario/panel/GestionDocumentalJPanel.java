package com.geopista.app.inventario.panel;

import com.geopista.app.document.*;
import com.geopista.app.AppContext;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.client.alfresco.ui.AlfrescoExplorer;
import com.geopista.client.alfresco.utils.implementations.LocalgisIntegrationManagerImpl;
import com.geopista.client.alfresco.utils.interfaces.LocalgisIntegrationManager;
import com.geopista.global.ServletConstants;
import com.geopista.util.ApplicationContext;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.Documentable;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.Lote;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.util.StringUtil;

import javax.swing.*;

import org.apache.log4j.Logger;

import java.util.Collection;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by IntelliJ IDEA. User: charo Date: 07-sep-2006 Time: 12:33:37 To
 * change this template use File | Settings | File Templates.
 */
public class GestionDocumentalJPanel extends JPanel implements
		DocumentInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(GestionDocumentalJPanel.class);

	private AppContext aplicacion;

	private DocumentPanel documentosJPanel;
	private JPanelComentarios comentariosJPanel;

	private DocumentClient documentClient = null;

	/* Necesario para mostrar la pantalla del reloj */
	private DocumentBean auxDocument = new DocumentBean();
	private DocumentBean document;

	/* Alfresco */
	private LocalgisIntegrationManager localgisIntegrationManager;

	/* Botones jPanelButtons */
	private JButton annadirJButton = new JButton();
	private JButton modificarJButton = new JButton();
	private JButton borrarJButton = new JButton();
	private JButton guardarJButton = new JButton();
	private JButton visualizarJButton = new JButton();
	private JButton bAlfrescoManager = new JButton();
	
	/**
	 * Si la llamada no se hace desde GestionDocumentalJDialog, no se actualiza
	 * una por una en BD cada una de las operaciones sobre la documentacion.
	 */
	private boolean fromDialog = false;

	private Hashtable filesInUp = new Hashtable();

	private Object key = null;

	public GestionDocumentalJPanel(boolean fromDialog) throws Exception {
		this.fromDialog = fromDialog;
		inicializar(null);
	}

	/**
	 * Método que genera el dialogo que muestra los datos de un bien mueble
	 */
	public GestionDocumentalJPanel(final Object obj, boolean fromDialog)
			throws Exception {
		this.fromDialog = fromDialog;
		inicializar(obj);
	}

	public void inicializar(final Object obj) throws Exception {
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		setLayout(new BorderLayout());

		documentClient = new DocumentClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ ServletConstants.DOCUMENT_SERVLET_NAME);

		if (AlfrescoManagerUtils.isAlfrescoActive()){
			localgisIntegrationManager = new LocalgisIntegrationManagerImpl(
					aplicacion.getUserPreference(AppContext.HOST_ADMCAR, "",
							false),
					String.valueOf(aplicacion.getIdMunicipio()),
					AlfrescoConstants.APP_INVENTORY);
		}

		JPanel botoneraJPanel = new JPanel();
		botoneraJPanel.setLayout(new java.awt.FlowLayout());
		annadirJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				annadirDocumento();
			}
		});
		botoneraJPanel.add(annadirJButton);

		modificarJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modificarDocumento();
			}
		});
		botoneraJPanel.add(modificarJButton);

		borrarJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				eliminarDocumento();
			}
		});
		botoneraJPanel.add(borrarJButton);

		guardarJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				guardarDocumento();
			}
		});
		botoneraJPanel.add(guardarJButton);

		visualizarJButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						visualizarDocumento();
					}
				});
		botoneraJPanel.add(visualizarJButton);

		if (AlfrescoManagerUtils.isAlfrescoActive()) {
			bAlfrescoManager.setText(aplicacion.getI18nString("alfresco.button.documentManager"));
			bAlfrescoManager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					docManager();
				}
			});
			botoneraJPanel.add(bAlfrescoManager);
		}

		key = obj;
		try {
			if (key != null) {
				Collection c = documentClient.getAttachedDocuments(key);			
				documentosJPanel = new DocumentPanel(null, c, this);
			} else {
				documentosJPanel = new DocumentPanel(null, new Vector(), this);
			}
			documentosJPanel.actualizarModelo();
		} catch (Exception e) {
			logger.error(e);
		}

		comentariosJPanel = new JPanelComentarios(true);
		comentariosJPanel.setEnabled(false);
		add(documentosJPanel, BorderLayout.NORTH);
		add(comentariosJPanel, BorderLayout.CENTER);
		add(botoneraJPanel, BorderLayout.SOUTH);
		documentosJPanel.addMouseListener(new ActionJList());
		renombrarComponentes();

	}
	
	/*
	 * método que realiza las llamadas correspondientes para añadir nuevos
	 * documentos
	 */
	private void annadirDocumento() {
		if (!AlfrescoManagerUtils.isAlfrescoActive()) {
			JFrameOpciones frameOpciones = new JFrameOpciones(
					aplicacion.getMainFrame());
			frameOpciones.setVisible(true);
			if (!frameOpciones.isAceptar())
				return;
			if (frameOpciones.isLocal())
				anadirlocal();
			else
				anadirBD();
		} else
			anadirlocal();
	}

	/* añadimos un documento en local */
	private void anadirlocal() {
		JDialogLocal jDialogLocal = new JDialogLocal(aplicacion.getMainFrame(),
				DocumentBean.ALL_CODE, true);
		jDialogLocal.setVisible(true);
		if (!jDialogLocal.okCancelPanel.wasOKPressed())
			return;
		auxDocument = new DocumentBean();
		auxDocument = jDialogLocal.save(auxDocument);
		if (auxDocument.getFileName() == null)
			return;

		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		if (fromDialog) {
			progressDialog.setTitle(aplicacion
					.getI18nString("document.infodocument.salvando.title"));
			progressDialog.report(aplicacion
					.getI18nString("document.infodocument.salvando"));
		} else {
			progressDialog.setTitle(aplicacion
					.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion
					.getI18nString("inventario.gestionDocumental.tag2"));
		}
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						/* añadimos el documento a la lista */
						try {
							if (!AlfrescoManagerUtils.isAlfrescoActive()){
								if (fromDialog) {
									if (auxDocument.getFileName() != null) {
										File file = new File(auxDocument
												.getFileName());
										auxDocument.setTypeByExtension();
										if (auxDocument.isImagen())
											auxDocument
													.setThumbnail(com.geopista.protocol.document.Thumbnail.createThumbnail(
															file.getAbsolutePath(),
															20, 20));
									}								
									auxDocument = documentClient
											.attachInventarioDocument(key,
													auxDocument);
						
								} else {
									File file = new File(auxDocument.getFileName());
									auxDocument.setFileName(file.getAbsolutePath());
									auxDocument.setSize(file.length());
									auxDocument = documentClient
											.updateTipo(auxDocument);
									auxDocument.setTypeByExtension();
									if (auxDocument.isImagen())
										auxDocument
												.setThumbnail(com.geopista.protocol.document.Thumbnail
														.createThumbnail(file
																.getAbsolutePath(),
																20, 20));
	
									filesInUp.put("" + filesInUp.size(), file);
								}
								documentosJPanel.add(auxDocument);
							}
							else {
								if (localgisIntegrationManager
										.associateAlfrescoInventarioDocument(
												String.valueOf(aplicacion
														.getIdMunicipio()),
												AlfrescoConstants.APP_INVENTORY,
												auxDocument,
												documentClient, key)) {
									documentosJPanel.add(auxDocument);
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

	/* añadimos un documento en BD */
	private void anadirBD() {
		final JDialogBD jDialogBD = new JDialogBD(aplicacion.getMainFrame(),
				DocumentBean.ALL_CODE);
		jDialogBD.setVisible(true);
		if (!jDialogBD.okCancelPanel.wasOKPressed())
			return;
		auxDocument = jDialogBD.get();
		if (auxDocument.getFileName() == null)
			return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		if (fromDialog) {
			progressDialog.setTitle(aplicacion
					.getI18nString("document.infodocument.salvando.title"));
			progressDialog.report(aplicacion
					.getI18nString("document.infodocument.salvando"));
		} else {
			progressDialog.setTitle(aplicacion
					.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion
					.getI18nString("inventario.gestionDocumental.tag2"));
		}

		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							if (!documentosJPanel.existDocument(auxDocument)) {
								if (fromDialog) {
									/* enlazamos el documento de BD al bien */
									documentClient.linkInventarioDocument(key,
											auxDocument);
								} else {
									filesInUp.put("" + filesInUp.size(),
											new File(auxDocument.getFileName()));
								}
								documentosJPanel.add(auxDocument);
							}

						} catch (Exception e) {
							logger.error(
									"Error al asociar el documento al inventario ",
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

	/* modificamos un documento ya existente */
	private void modificarDocumento() {
		final JDialogLocal jDialogLocal = new JDialogLocal(
				aplicacion.getMainFrame(), DocumentBean.DOC_CODE, true);
		if (documentosJPanel.getdocumentSelected() == null)
			return;
		document = (DocumentBean) documentosJPanel.getdocumentSelected()
				.clone();
		jDialogLocal.load(document);
		jDialogLocal.show();
		if (!jDialogLocal.okCancelPanel.wasOKPressed())
			return;
		document = jDialogLocal.save(document);
		document.setContent(null);
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		if (fromDialog) {
			progressDialog.setTitle(aplicacion
					.getI18nString("document.infodocument.salvando.title"));
			progressDialog.report(aplicacion
					.getI18nString("document.infodocument.salvando"));
		} else {
			progressDialog.setTitle(aplicacion
					.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion
					.getI18nString("inventario.gestionDocumental.tag2"));
		}
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							if (!AlfrescoManagerUtils
									.isAlfrescoActive()) {
								if (fromDialog) {
									document = documentClient.updateTipo(document);
									document.setTypeByExtension();
									if (document.isImagen())
										document = documentClient
												.updateDocument(document);
									else {									
										if (jDialogLocal.isNewDocument()) {
											File file = new File(document
													.getFileName());
											document.setFileName(file.getName());
											documentClient.updateDocument(
													document, file);
										} else {
											documentClient
													.updateDocumentByteStream(document);
										}
									
									}
								}					
								else {
									File file = new File(document.getFileName());
									if (file.isAbsolute()) {
										document.setSize(file.length());
										document = documentClient
													.updateTipo(document);
										document.setTypeByExtension();
										if (document.isImagen())
											document.setThumbnail(com.geopista.protocol.document.Thumbnail
														.createThumbnail(
																file.getAbsolutePath(),
																20, 20));
									}
									int index = documentosJPanel.getSelectedIndex();
									if (index != -1)
										filesInUp.put("" + index, file);
									}
									documentosJPanel.update(document);
									documentosJPanel.seleccionar(document);
							} 
							else {
								// CAMBIAR: Mirar si ya existe el
								// documento (del tipo ) o no y crear
								// nueva version
								if (localgisIntegrationManager
										.associateAlfrescoInventarioDocument(
												String.valueOf(aplicacion
														.getIdMunicipio()),
												AlfrescoConstants.APP_INVENTORY,
												documentosJPanel.getdocumentSelected().getFileName(),
												document,
												documentClient)){
									documentosJPanel.update(document);
									documentosJPanel.seleccionar(document);
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

	/* método xa eliminar el documento seleccionado de la lista */
	private void eliminarDocumento() {
		if (documentosJPanel.getdocumentSelected() == null)
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
		if (fromDialog) {
			progressDialog.setTitle(aplicacion
					.getI18nString("document.infodocument.salvando.title"));
			progressDialog.report(aplicacion
					.getI18nString("document.infodocument.salvando"));
		} else {
			progressDialog.setTitle(aplicacion
					.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion
					.getI18nString("inventario.gestionDocumental.tag2"));
		}

		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							DocumentBean document = (DocumentBean) documentosJPanel
									.getdocumentSelected();
							if (fromDialog) {
								documentClient.detachInventarioDocument(key,
										document);
							} else {
								int index = documentosJPanel.getSelectedIndex();
								if (index != -1) {
									filesInUp.remove("" + index);
									actualizarFilesInUp(index);
								}
							}
							/* borrar el elemento de la lista */
							documentosJPanel.borrar();
							comentariosJPanel.load(null);
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

	/**
	 * Método para visualizar en un editor el documento seleccionado de una
	 * lista.
	 */
	private void guardarDocumento() {
		if (documentosJPanel.getdocumentSelected() == null)
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
							auxDocument = documentosJPanel
									.getdocumentSelected();
							
							if (!AlfrescoManagerUtils.isAlfrescoActive()){
								if (auxDocument != null
										&& !(new File(auxDocument.getFileName())
												.isAbsolute()))
									auxDocument.setContent(documentClient
											.getAttachedByteStream(auxDocument));
							}
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
			String selectedFile = "";
			if (new File(auxDocument.getFileName()).isAbsolute())
				selectedFile = new File(auxDocument.getFileName())
						.getAbsolutePath();
			else {
				selectedFile = seleccionaFichero(auxDocument.getFileName());
				if (selectedFile == null)
					return;
			
				if (!AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())) {
					RandomAccessFile outFile = new RandomAccessFile(
							selectedFile, "rw");

					outFile.write(auxDocument.getContent());
					outFile.close();
				} else {
					if (localgisIntegrationManager.downloadAssociateDocument(
							auxDocument.getId(), selectedFile))
						// CAMBIAR
						JOptionPane.showMessageDialog(
								aplicacion.getMainFrame(),
								"Documento descargado correctamente");
					else
						JOptionPane.showMessageDialog(
								aplicacion.getMainFrame(),
								"Error al descargar el documento");

				}
				
			}

			/* visualizamos si el SO es Windows */
			if (com.geopista.app.licencias.CUtilidadesComponentes_LCGIII.isWindows()) {
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

	/**
	 * Método para visualizar en un editor el documento seleccionado de una
	 * lista.
	 */
	private void visualizarDocumento() {
		if (documentosJPanel.getdocumentSelected() == null)
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
							auxDocument = documentosJPanel
									.getdocumentSelected();
							
							if (!AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())){ 
								if (auxDocument != null
										&& !(new File(auxDocument.getFileName())
												.isAbsolute()))
									auxDocument.setContent(documentClient
											.getAttachedByteStream(auxDocument));
							}
							
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
			String selectedFile = "";
			if (auxDocument.getFileName() == null)
				return;
			if (new File(auxDocument.getFileName()).isAbsolute())
				selectedFile = new File(auxDocument.getFileName())
						.getAbsolutePath();
			else {
				selectedFile = System.getProperty("java.io.tmpdir")
						+ auxDocument.getFileName();
				
				if (!AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())) {
					if(auxDocument.getContent()==null){
						throw new Exception("Fichero no encontrado en el servidor");
					}
					RandomAccessFile outFile = new RandomAccessFile(
							selectedFile, "rw");
					outFile.write(auxDocument.getContent());
					outFile.close();
				} else {
					if (localgisIntegrationManager.downloadAssociateDocument(
							auxDocument.getId(), selectedFile))
						System.out.println("BIEN");
					else
						System.out.println("MAL");
				}
				
			}

			/* visualizamos si el SO es Windows */
			if (com.geopista.app.licencias.CUtilidadesComponentes_LCGIII.isWindows()) {
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

	/**
	 * Método que abre el directorio donde queremos dejar el documento que
	 * estamos abriendo para su visualizacion
	 * 
	 * @param filename
	 * @return path donde se va a guardar el fichero en local
	 * @throws Exception
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
		(new AlfrescoExplorer(aplicacion.getMainFrame(), key, documentosJPanel.getFeatureDocumentsId(),
				String.valueOf(aplicacion.getIdMunicipio()),
				AlfrescoConstants.APP_INVENTORY)).setVisible(true);
		//RECARGAR PANEL DOCUMENTOS
		try {
			load(key);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void renombrarComponentes() {
		try {
			annadirJButton.setText(aplicacion
					.getI18nString("inventario.document.tag2"));
		} catch (Exception e) {
		}
		try {
			modificarJButton.setText(aplicacion
					.getI18nString("inventario.document.tag3"));
		} catch (Exception e) {
		}
		try {
			borrarJButton.setText(aplicacion
					.getI18nString("inventario.document.tag4"));
		} catch (Exception e) {
		}
		try {
			visualizarJButton.setText(aplicacion
					.getI18nString("inventario.document.tag5"));
		} catch (Exception e) {
		}
		try {
			guardarJButton.setText(aplicacion
					.getI18nString("inventario.document.tag10"));
		} catch (Exception e) {
		}
		if (AlfrescoManagerUtils.isAlfrescoActive()) {
			try {
				bAlfrescoManager.setText(aplicacion
						.getI18nString("alfresco.button.documentManager"));
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Método que carga la lista de documentos de un bien
	 * 
	 * @param bien
	 */
	public void load(Object key) throws Exception {
		if (key == null)
			return;
		Collection c = documentClient.getAttachedDocuments(key);
		documentosJPanel.setDocumentos(c);
		documentosJPanel.actualizarModelo();

		/** actualizamos filesInUp */
		if (c != null) {
			Object[] originales = c.toArray();
			for (int i = 0; i < originales.length; i++) {
				DocumentBean doc = (DocumentBean) originales[i];
				filesInUp.put("" + i, new File(doc.getFileName()));
			}
		}
	}

	/**
	 * Método que carga la lista de documentos
	 * 
	 * @param bien
	 */
	public void load(Collection<DocumentBean> documentos) throws Exception {
		documentosJPanel.setDocumentos(documentos);
		documentosJPanel.actualizarModelo();

		/** actualizamos filesInUp */
		if (documentos != null) {
			Object[] originales = documentos.toArray();
			for (int i = 0; i < originales.length; i++) {
				DocumentBean doc = (DocumentBean) originales[i];
				filesInUp.put("" + i, new File(doc.getFileName()));
			}
		}
	}

	public void actualizarDatos(Documentable objeto) {
		objeto.setDocumentos(documentosJPanel.getDocumentos());
	}

	/**
	 * Devuelve la lista de documentos
	 */
	public Collection<DocumentBean> getDocumentos() {
		return documentosJPanel.getDocumentos();
	}

	public void setEnabled(boolean b) {
		documentosJPanel.setEnabled(b);
		annadirJButton.setEnabled(b);
		modificarJButton.setEnabled(b);
		borrarJButton.setEnabled(b);
		if (AlfrescoManagerUtils.isAlfrescoActive()) {
			bAlfrescoManager.setEnabled(b);
		}		
		guardarJButton.setEnabled(true);
		visualizarJButton.setEnabled(true);
	}

	public void modificarJButtonSetEnabled(boolean b) {
		modificarJButton.setEnabled(b);
	}

	public Vector getFilesInUp() {
		Vector aux = new Vector();
		for (int i = 0; i < filesInUp.size(); i++) {
			aux.add((File) filesInUp.get("" + i));
		}
		return aux;
	}

	private void actualizarFilesInUp(int index) {
		Hashtable aux = new Hashtable();
		if (index != -1) {
			if (index < filesInUp.size()) {
				for (int i = 0; i < index; i++) {
					aux.put("" + i, (File) filesInUp.get("" + i));
				}
				for (int i = index; i < filesInUp.size(); i++) {
					index++;
					aux.put("" + i, (File) filesInUp.get("" + index));
				}
				filesInUp = aux;
			}/* else seria el ultimo */
		}
	}

	/**
	 * Método para cargar los comentarios y la opción de público asociados a un
	 * documento
	 * 
	 * @param documento
	 */
	public void seleccionar(DocumentBean documento) {
		comentariosJPanel.load(documento);
	}

	/**
	 * Método de la interfaz no implementado
	 * 
	 * @param documentBean
	 * @param indicePanel
	 */
	public void seleccionar(DocumentBean documentBean, int indicePanel) {
	}

	class ActionJList extends MouseAdapter {
		public ActionJList() {
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				visualizarDocumento();
			}
		}
	}

}
