package com.localgis.app.gestionciudad.dialogs.documents;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.document.DocumentInterface;
import com.geopista.app.document.DocumentPanel;
import com.geopista.app.document.JDialogLocal;
import com.geopista.app.document.JFrameOpciones;
import com.geopista.app.document.JPanelComentarios;
import com.geopista.app.utilidades.CUtilidades;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;
import com.geopista.util.ApplicationContext;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.documents.utils.GestionCiudadDocumentClient;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 07-sep-2006
 * Time: 12:33:37
 * To change this template use File | Settings | File Templates.
 */
public class GestionDocumentalJPanel extends JPanel  implements DocumentInterface{
	Logger logger= Logger.getLogger(GestionDocumentalJPanel.class);

	private ApplicationContext aplicacion;

	private DocumentPanel documentosJPanel;
	private JPanelComentarios comentariosJPanel;

	private GestionCiudadDocumentClient documentClient= null;

	/* Necesario para mostrar la pantalla del reloj*/
	private DocumentBean auxDocument= new DocumentBean();
	private DocumentBean document;

	/* Botones jPanelButtons */
	private JButton annadirJButton= new JButton();
	private JButton modificarJButton = new JButton();
	private JButton borrarJButton= new JButton();
	private JButton guardarJButton= new JButton();
	private JButton visualizarJButton= new JButton();
	/** Si la llamada no se hace desde GestionDocumentalJDialog, no se actualiza una por una en BD cada una de las operaciones sobre la documentacion. */
	private boolean fromDialog= false;

	private Hashtable filesInUp= new Hashtable();

	private LocalGISNote noteId= null;
	
	private NotesInterventionsEditionTypes tipoEdicion = NotesInterventionsEditionTypes.VIEW;

	public GestionDocumentalJPanel(boolean fromDialog) throws Exception{
		this.fromDialog= fromDialog;
		inicializar(null);
	}
	/**
	 * Método que genera el dialogo que muestra los datos de un bien mueble
	 */
	public GestionDocumentalJPanel(final Object obj, boolean fromDialog, NotesInterventionsEditionTypes tipoEdicion ) throws Exception{
		this.tipoEdicion = tipoEdicion;
		this.fromDialog= fromDialog;
		inicializar(obj);

	}

	public void inicializar(final Object obj) throws Exception{
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		setLayout(new BorderLayout());

		documentClient= new GestionCiudadDocumentClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
				DocumentManagerPlugin.DOCUMENT_SERVLET_NAME);

		JPanel botoneraJPanel= new JPanel();
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

		visualizarJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				visualizarDocumento();
			}
		});
		botoneraJPanel.add(visualizarJButton);
		


		if (obj != null){
			noteId= ((LocalGISNote)obj);
			Collection c= documentClient.getAttachedDocuments(noteId);
			documentosJPanel= new DocumentPanel(null, c, this);
		}else{
			documentosJPanel= new DocumentPanel(null, new HashMap().values(), this);
		}

		comentariosJPanel= new JPanelComentarios(true);
		comentariosJPanel.setEnabled(false);
		add(documentosJPanel, BorderLayout.NORTH);
		add(comentariosJPanel, BorderLayout.CENTER);
		add(botoneraJPanel, BorderLayout.SOUTH);
		documentosJPanel.addMouseListener(new ActionJList());
		renombrarComponentes();


	}

	/* método que realiza las llamadas correspondientes para añadir nuevos documentos*/
	private void annadirDocumento(){
		JFrameOpciones frameOpciones = new JFrameOpciones(aplicacion.getMainFrame());
		frameOpciones.show();
		if (!frameOpciones.isAceptar()) return;
		if (frameOpciones.isLocal())
			anadirlocal();
		else
			anadirBD();
	}

	/* añadimos un documento en local */
	private void anadirlocal(){
		JDialogLocal jDialogLocal= new JDialogLocal(aplicacion.getMainFrame(), DocumentBean.DOC_CODE, true);
		jDialogLocal.show();
		if(!jDialogLocal.okCancelPanel.wasOKPressed()) return;
		auxDocument= new DocumentBean();
		auxDocument = jDialogLocal.save(auxDocument);
		auxDocument.setIsDocument();
		if(auxDocument.getFileName() == null) return;

		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		if (fromDialog){
			progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.salvando.title"));
			progressDialog.report(aplicacion.getI18nString("document.infodocument.salvando"));
		}else{
			progressDialog.setTitle(aplicacion.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion.getI18nString("inventario.gestionDocumental.tag2"));
		}
		progressDialog.addComponentListener(new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				new Thread(new Runnable(){
					public void run(){
						/* añadimos el documento a la lista */
						try{
							if (fromDialog){
								auxDocument= documentClient.attachDocument(noteId, auxDocument);
							}else{
								File file= new File(auxDocument.getFileName());
								auxDocument.setFileName(file.getAbsolutePath());
								auxDocument.setSize(file.length());
								auxDocument= documentClient.updateTipo(auxDocument);
								if (auxDocument.isImagen())
									auxDocument.setThumbnail(com.geopista.protocol.document.Thumbnail.createThumbnail(file.getAbsolutePath(), 20, 20));

								filesInUp.put(""+filesInUp.size(), file);
							}
							documentosJPanel.add(auxDocument);
						}
						catch(Exception e){
							logger.error("Error al añadir el documento ", e);
							ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
							return;
						}
						finally{
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
	private void anadirBD(){
		final com.localgis.app.gestionciudad.dialogs.documents.utils.JDialogBD jDialogBD = new com.localgis.app.gestionciudad.dialogs.documents.utils.JDialogBD(aplicacion.getMainFrame(), DocumentBean.ALL_CODE);
		jDialogBD.show();
		if(!jDialogBD.okCancelPanel.wasOKPressed()) return;
		auxDocument= jDialogBD.get();
		if(auxDocument.getFileName() == null) return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		if (fromDialog){
			progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.salvando.title"));
			progressDialog.report(aplicacion.getI18nString("document.infodocument.salvando"));
		}else{
			progressDialog.setTitle(aplicacion.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion.getI18nString("inventario.gestionDocumental.tag2"));
		}

		progressDialog.addComponentListener(new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				new Thread(new Runnable(){
					public void run(){
						try{
							if (!documentosJPanel.existDocument(auxDocument)){
								if (fromDialog){
									/* enlazamos el documento de BD al bien */
									documentClient.linkGestionCiudadDocument(noteId, auxDocument);
								}else{
									filesInUp.put(""+filesInUp.size(), new File(auxDocument.getFileName()));
								}
								documentosJPanel.add(auxDocument);
							}

						}
						catch(Exception e){
							logger.error("Error al asociar el documento al inventario ", e);
							ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
							return;
						}
						finally{
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
	private void modificarDocumento(){
		final JDialogLocal jDialogLocal= new JDialogLocal(aplicacion.getMainFrame(), DocumentBean.DOC_CODE, true);
		if (documentosJPanel.getdocumentSelected()==null) return;
		document= (DocumentBean)documentosJPanel.getdocumentSelected().clone();
		jDialogLocal.load(document);
		jDialogLocal.show();
		if(!jDialogLocal.okCancelPanel.wasOKPressed()) return;
		document= jDialogLocal.save(document);
		document.setContent(null);
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		if (fromDialog){
			progressDialog.setTitle(aplicacion.getI18nString("imagen.infoimage.salvando.title"));
			progressDialog.report(aplicacion.getI18nString("image.infoimage.salvando"));
		}else{
			progressDialog.setTitle(aplicacion.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion.getI18nString("inventario.gestionDocumental.tag2"));
		}
		progressDialog.addComponentListener(new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				new Thread(new Runnable(){
					public void run(){
						try{
							if (fromDialog){
								document= documentClient.updateTipo(document);
								if (document.isImagen()) document= documentClient.updateDocument(document);
								else {
									if(jDialogLocal.isNewDocument()){
										File file= new File(document.getFileName());
										document.setFileName(file.getName());
										documentClient.updateDocument(document, file);
									}else{
										documentClient.updateDocumentByteStream(document);
									}
								}
							}else{
								File file= new File(document.getFileName());
								if (file.isAbsolute()){
									document.setSize(file.length());
									document= documentClient.updateTipo(document);
									if (document.isImagen())
										document.setThumbnail(com.geopista.protocol.document.Thumbnail.createThumbnail(file.getAbsolutePath(), 20, 20));
								}
								int index= documentosJPanel.getSelectedIndex();
								if (index!=-1) filesInUp.put(""+index, file);
							}
							documentosJPanel.update(document);
							documentosJPanel.seleccionar(document);
						}catch(Exception e){
							logger.error("Error al modificar el documento ", e);
							ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
							return;
						}finally{
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
	private void eliminarDocumento(){
		if(documentosJPanel.getdocumentSelected() == null) return;
		int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
				aplicacion.getI18nString("document.infodocument.respuesta.mensaje"),
				"",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if(n == JOptionPane.NO_OPTION) return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		if (fromDialog){
			progressDialog.setTitle(aplicacion.getI18nString("imagen.infoimage.salvando.title"));
			progressDialog.report(aplicacion.getI18nString("image.infoimage.salvando"));
		}else{
			progressDialog.setTitle(aplicacion.getI18nString("inventario.gestionDocumental.tag1"));
			progressDialog.report(aplicacion.getI18nString("inventario.gestionDocumental.tag2"));
		}

		progressDialog.addComponentListener(new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				new Thread(new Runnable(){
					public void run(){
						try{
							DocumentBean document= (DocumentBean)documentosJPanel.getdocumentSelected();
							if (fromDialog){
								documentClient.detachDocument(noteId, document);
							}else{
								int index= documentosJPanel.getSelectedIndex();
								if (index != -1){
									filesInUp.remove(""+index);
									actualizarFilesInUp(index);
								}
							}
							/* borrar el elemento de la lista */
							documentosJPanel.borrar();
							comentariosJPanel.load(null);
						}catch(Exception e){
							logger.error("Error al eliminar un documento ", e);
							ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
						}finally{
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
	 * Método para visualizar en un editor el documento seleccionado de una lista.
	 */
	private void guardarDocumento(){
		if(documentosJPanel.getdocumentSelected() == null) return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.abrir"));
		progressDialog.report(aplicacion.getI18nString("document.infodocument.abrir"));
		progressDialog.addComponentListener(new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				new Thread(new Runnable(){
					public void run(){
						try{
							auxDocument= documentosJPanel.getdocumentSelected();
							if (auxDocument!=null && !(new File(auxDocument.getFileName()).isAbsolute()))
								auxDocument.setContent(documentClient.getAttachedByteStream(auxDocument));
						}
						catch(Exception e){
							logger.error("Error al mostrar la pantalla de reloj ", e);
							ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
						}finally{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		try{
			GUIUtil.centreOnWindow(progressDialog);
			progressDialog.setVisible(true);
			String selectedFile= "";
			if (new File(auxDocument.getFileName()).isAbsolute()) selectedFile= new File(auxDocument.getFileName()).getAbsolutePath();
			else{
				selectedFile= seleccionaFichero(auxDocument.getFileName());
				if (selectedFile == null) return;
				RandomAccessFile outFile= new RandomAccessFile(selectedFile, "rw");

				outFile.write(auxDocument.getContent());
				outFile.close();
			}

			/* visualizamos si el SO es Windows */
			if(CUtilidades.isWindows()){
				try{
					Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" +  selectedFile+ "\"");
				}
				catch(Exception ex){
					logger.error("Error al abrir el documento : ", ex);
				}
			}
		}
		catch(Exception e){
			logger.error("Error al visualizar un documento ", e);
			ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
		}
	}
	/**
	 * Método para visualizar en un editor el documento seleccionado de una lista.
	 */
	private void visualizarDocumento(){
		if(documentosJPanel.getdocumentSelected() == null) return;
		/* ponemos una pantalla con el reloj */
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.abrir"));
		progressDialog.report(aplicacion.getI18nString("document.infodocument.abrir"));
		progressDialog.addComponentListener(new ComponentAdapter(){
			public void componentShown(ComponentEvent e){
				new Thread(new Runnable(){
					public void run(){
						try{
							auxDocument= documentosJPanel.getdocumentSelected();
							if (auxDocument!=null && !(new File(auxDocument.getFileName()).isAbsolute()))
								auxDocument.setContent(documentClient.getAttachedByteStream(auxDocument));
						}
						catch(Exception e){
							logger.error("Error al mostrar la pantalla de reloj ", e);
							ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
						}finally{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		try{
			GUIUtil.centreOnWindow(progressDialog);
			progressDialog.setVisible(true);
			String selectedFile= "";
			if (auxDocument.getFileName() == null) return;
			if (new File(auxDocument.getFileName()).isAbsolute()) selectedFile= new File(auxDocument.getFileName()).getAbsolutePath();
			else{
				selectedFile=System.getProperty("java.io.tmpdir")+auxDocument.getFileName();
				RandomAccessFile outFile= new RandomAccessFile(selectedFile, "rw");
				outFile.write(auxDocument.getContent());
				outFile.close();
			}

			/* visualizamos si el SO es Windows */
			if(CUtilidades.isWindows()){
				try{
					Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" +  selectedFile+ "\"");
				}
				catch(Exception ex){
					logger.error("Error al abrir el documento : ", ex);
				}
			}
		}
		catch(Exception e){
			logger.error("Error al visualizar un documento ", e);
			ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
		}
	}

	/**
	 * Método que abre el directorio donde queremos dejar el documento que estamos abriendo para su visualizacion
	 * @param filename
	 * @return path donde se va a guardar el fichero en local
	 * @throws Exception
	 */
	private String seleccionaFichero(String filename) throws Exception{
		File f= new File(filename);
		/* Dialogo para seleccionar donde dejar el fichero */
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setSelectedFile(f);
		if (chooser.showSaveDialog(aplicacion.getMainFrame()) != JFileChooser.APPROVE_OPTION) return null;

		File selectedFile= chooser.getSelectedFile();
		if (selectedFile ==null) return null;
		String tmpDir= "";
		String tmpFile= selectedFile.getAbsolutePath();
		if (tmpFile.lastIndexOf(selectedFile.getName()) != -1){
			tmpDir= tmpFile.substring(0, tmpFile.lastIndexOf(selectedFile.getName()));
		}
		/** Comprobamos si existe el directorio. */
		try{
			File dir = new File(tmpDir);
			if (!dir.exists()){
				dir.mkdirs();
			}
		} catch (Exception ex){
			logger.error("Error al seleccionar un fichero ", ex);
		}
		return selectedFile.getAbsolutePath();
	}


	public void renombrarComponentes(){
		try{annadirJButton.setText(aplicacion.getI18nString("inventario.document.tag2"));}catch(Exception e){}
		try{modificarJButton.setText(aplicacion.getI18nString("inventario.document.tag3"));}catch(Exception e){}
		try{borrarJButton.setText(aplicacion.getI18nString("inventario.document.tag4"));}catch(Exception e){}
		try{visualizarJButton.setText(aplicacion.getI18nString("inventario.document.tag5"));}catch(Exception e){}
		try{guardarJButton.setText(aplicacion.getI18nString("inventario.document.tag10"));}catch(Exception e){}
	}

	/**
	 * Método que carga la lista de documentos de un bien
	 * @param note
	 */
	public void load(LocalGISNote note) throws Exception{
		if (note==null) return;
		noteId= note;
		Collection c= documentClient.getAttachedDocuments(noteId);
		documentosJPanel.setDocumentos(c);
		documentosJPanel.actualizarModelo();

		/** actualizamos filesInUp */
		if (c != null){
			Object[] originales= c.toArray();
			for (int i=0; i<originales.length; i++){
				DocumentBean doc= (DocumentBean)originales[i];
				filesInUp.put(""+i, new File(doc.getFileName()));
			}
		}
	}

	public void actualizarDatos(LocalGISNote note){
		// TODO Cambiar para documentobean nuestro
		//        note.setListaDeDocumentos(documentosJPanel.getDocumentos().toArray());
	}

	public void setEnabled(boolean b){

		visualizarJButton.setEnabled(true);
		
		if (tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
			annadirJButton.setEnabled(false);
			borrarJButton.setEnabled(false);
			guardarJButton.setEnabled(false);
			modificarJButton.setEnabled(false);
		}
	}

	public void modificarJButtonSetEnabled(boolean b){
		modificarJButton.setEnabled(b);
	}

	public Vector getFilesInUp() {
		Vector aux= new Vector();
		for (int i=0; i<filesInUp.size(); i++){
			aux.add((File)filesInUp.get(""+i));
		}
		return aux;
	}

	private void actualizarFilesInUp(int index){
		Hashtable aux= new Hashtable();
		if (index != -1){
			if (index < filesInUp.size()){
				for (int i=0; i<index; i++){
					aux.put(""+i, (File)filesInUp.get(""+i));
				}
				for (int i= index; i < filesInUp.size(); i++){
					index++;
					aux.put(""+i, (File)filesInUp.get(""+index));
				}
				filesInUp= aux;
			}/* else seria el ultimo */
		}
	}


	/**
	 * Método para cargar los comentarios y la opción de público asociados a un documento
	 * @param documento
	 */
	public void seleccionar(DocumentBean documento){
		comentariosJPanel.load(documento);
	}

	/**
	 * Método de la interfaz no implementado
	 * @param documentBean
	 * @param indicePanel
	 */
	public void seleccionar(DocumentBean documentBean, int indicePanel){
	}

	class ActionJList extends MouseAdapter{
		public ActionJList(){
		}

		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 2){
				visualizarDocumento();
			}
		}
	}
	
	public Collection getListaDocumentos(){
		return this.documentosJPanel.getDocumentos();
	}


}
