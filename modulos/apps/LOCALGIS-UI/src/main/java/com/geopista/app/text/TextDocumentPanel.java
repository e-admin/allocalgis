/**
 * TextDocumentPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.text;

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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.document.DocumentInterface;
import com.geopista.app.document.DocumentPanel;
import com.geopista.app.document.JDialogBD;
import com.geopista.app.document.JDialogLocal;
import com.geopista.app.document.JFrameOpciones;
import com.geopista.app.document.JPanelComentarios;
import com.geopista.app.utilidades.CUtilidades;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.client.alfresco.ui.AlfrescoExplorer;
import com.geopista.client.alfresco.utils.implementations.LocalgisIntegrationManagerImpl;
import com.geopista.client.alfresco.utils.interfaces.LocalgisIntegrationManager;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.ui.dialogs.TextDialog;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class TextDocumentPanel extends  JPanel implements DocumentInterface, FeatureExtendedPanel
{
    private static final Log logger = LogFactory.getLog(TextDocumentPanel.class);

    /* Paneles */
    private JPanel jPanelButtons = new JPanel();
    private JPanel jPanelListado = new JPanel();
    private DocumentPanel panelDocumentos;
    private TextPanel jTextPanel;
    private JPanelComentarios panelComentarios;

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
    private JTabbedPane jTabbedPane;

    /* Necesario xa mostrar la pantalla del reloj*/
    private DocumentBean auxDocument;

    private FeatureDialogHome fd;
    
	/* Alfresco */
	private LocalgisIntegrationManager localgisIntegrationManager;

    /* constructor de la clase q llama al método que inicializa el panel */
    public TextDocumentPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            logger.error("Error en la llamada al constructor de la clase ", e);
        }
    }

    /* método xa inicializar el panel */
    private void jbInit() throws Exception
    {
        aplicacion = (AppContext) AppContext.getApplicationContext();

		 boolean activo=LocalgisIntegrationManagerImpl.verifyStatusAlfresco(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL, "",false),
					String.valueOf(aplicacion.getIdMunicipio()),AlfrescoConstants.APP_GENERAL);
	     AlfrescoManagerUtils.setAlfrescoActive(activo);
	     
        if (AlfrescoManagerUtils.isAlfrescoClientActive()){
			localgisIntegrationManager = new LocalgisIntegrationManagerImpl(
					UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL, "",
							false),
					String.valueOf(aplicacion.getIdMunicipio()),
					AlfrescoConstants.APP_GENERAL);
		}
        
        this.setName(aplicacion.getI18nString("text.infotext.panel.listado"));
        this.setLayout(new BorderLayout());
       // this.setSize(new Dimension(600, 550));

        jPanelListado = new JPanel();
        jPanelListado.setLayout(new BorderLayout());

        panelComentarios = new JPanelComentarios();
        panelComentarios.setEnabled(false);
        jPanelListado.add(panelComentarios, BorderLayout.CENTER);

        String sUrl=aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
                ServletConstants.DOCUMENT_SERVLET_NAME;
        documentClient= new DocumentClient(sUrl);
        
        loadDocumentPanel();
       
        /** Comentarios
          * Panel */
        jTextPanel = new TextPanel();
        jTextPanel.setEnabled(false);
        this.add(jTextPanel, BorderLayout.CENTER);

        /** Botones
          * Panel */
        jPanelButtons.setLayout(new FlowLayout());
        /* Componentes */
        bModificar.setText(aplicacion.getI18nString("document.infodocument.botones.modificar"));
        bModificar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                modifyDocument();
            }
        });
        bAnadir.setText(aplicacion.getI18nString("document.infodocument.botones.anadir"));
        bAnadir.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addDocument();
            }
        });
        bBorrar.setText(aplicacion.getI18nString("document.infodocument.botones.borrar"));
        bBorrar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                eliminar();
            }
        });
        bGuardar.setText(aplicacion.getI18nString("document.infodocument.botones.guardar"));
        bGuardar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                guardar();
            }
        });
        bVisualizar.setText(aplicacion.getI18nString("document.infodocument.botones.visualizar"));
        bVisualizar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                visualizar();
            }
        });
        /* Añadimos los componentes al panel */
        jPanelButtons.add(bAnadir);
        jPanelButtons.add(bModificar);
        jPanelButtons.add(bBorrar);
        jPanelButtons.add(bGuardar);
        jPanelButtons.add(bVisualizar);

        if (AlfrescoManagerUtils.isAlfrescoClientActive()) {
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
        Hashtable hFeaturesDocs= new Hashtable();
        Object[] lista = (Object[])identificadores.get("feature");
       
        if(lista != null)
        {
            if(lista.length == 1)
            {
                try
                {
                    GeopistaFeature feature = (GeopistaFeature) lista[0];
                    Collection collection = documentClient.getAttachedTexts(feature);
                    if (feature.getLayer() == null || (feature.getLayer() instanceof GeopistaLayer && ((GeopistaLayer)feature.getLayer()).isLocal())){                    	
                    	setEnabled(false);
                    }
            		if (collection == null){
            			collection = new ArrayList();
            		}
            		
            		if(panelDocumentos != null){
						panelDocumentos.actualizarModelo(collection);
					}
					else{
						DocumentPanel jPanel = new DocumentPanel(feature, collection, this);
						panelDocumentos = jPanel;
						jPanelListado.add(jPanel, BorderLayout.WEST);
	                    jPanel.addMouseListener(new ActionJList());
	                    
					}
            		
//                    DocumentPanel jPanel = new DocumentPanel(feature, collection, this);
//                    jPanelListado.add(jPanel, BorderLayout.WEST);
//                    jPanel.addMouseListener(new ActionJList());
//                    panelDocumentos = jPanel;
//            		vPaneles.add(jPanel);
            		vPaneles.removeAllElements();
					vPaneles.add(panelDocumentos);
                }
                catch(Exception e)
                {
                    logger.error("Error al mostrar los documentos de una feature ", e);
                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                 }
            }
            else if (lista.length>0)
            {
                try
                {
                    jTabbedPane = new JTabbedPane();
                    jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                    for(int i=0; i<lista.length; i++)
                    {
                        GeopistaFeature feature = (GeopistaFeature) lista[i];
                        Collection collection = documentClient.getAttachedTexts(feature);
                        DocumentPanel jDocumentPanel = new DocumentPanel(feature, collection, this);
                        jDocumentPanel.addMouseListener(new ActionJList());
                        hFeaturesDocs.put(feature.getSystemId(), collection);
                        /* mostramos la id de la feature en el panel xa distinguirlas */
                        String nombre = feature.getSystemId();
                        jTabbedPane.addTab(nombre, jDocumentPanel);
                        jPanelListado.add(jTabbedPane, BorderLayout.WEST);
                        vPaneles.add(jDocumentPanel);
                    }
                    jTabbedPane.addChangeListener(new ChangeListener()
                    {
                            public void stateChanged(ChangeEvent evt)
                            {
                                JTabbedPane pane = (JTabbedPane)evt.getSource();
                                int sel = pane.getSelectedIndex();
                                panelDocumentos = (DocumentPanel)vPaneles.get(sel);
                                seleccionar(panelDocumentos.getdocumentSelected());
                                if (fd instanceof TextDialog)
                                     ((TextDialog)fd).setDescription();

                            }
                    });
                    jTabbedPane.setSelectedIndex(0);
                    panelDocumentos = (DocumentPanel)vPaneles.get(0);
                }
                catch(Exception e)
                {
                    logger.error("Error al mostrar los textos de varias features ", e);
                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                }
            }
        }
        this.add(jPanelListado, BorderLayout.NORTH);
    }
    

    /* método xa eliminar el texto seleccionado de la lista */
    private void eliminar()
    {
        if(panelDocumentos.getdocumentSelected() == null) return;
        int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                aplicacion.getI18nString("text.infotext.respuesta.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        if(n == JOptionPane.NO_OPTION) return;
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("text.infotext.eliminando.title"));
        progressDialog.report(aplicacion.getI18nString("text.infotext.eliminando"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            GeopistaFeature feature = new GeopistaFeature();
                            DocumentBean document = new DocumentBean();
                            feature = panelDocumentos.getgFeature();
                            document = panelDocumentos.getdocumentSelected();
                            documentClient.detachDocument(feature, document);
                            /* borrar el elemento de la lista */
                            panelDocumentos.borrar();
                            panelComentarios.load(null);
                            jTextPanel.load(null);
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al eliminar un documento ", e);
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }

    /* método q realiza las llamadas correspondientes xa añadir nuevos textos */
    private void addDocument() {
		if (!AlfrescoManagerUtils.isAlfrescoClientActive()) {
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

    /* método xa visualizar en un editor el texto seleccionado de una lista */
    private void guardar()
    {
        if(panelDocumentos.getdocumentSelected() == null) return;
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("text.infotext.abrir"));
        progressDialog.report(aplicacion.getI18nString("text.infotext.abrir"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            auxDocument = panelDocumentos.getdocumentSelected();
                            // NUEVO
							if (!AlfrescoManagerUtils.isAlfrescoClientActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio()))
								auxDocument.setContent(documentClient
										.getAttachedByteStream(auxDocument));
							// FIN NUEVO
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al mostrar la pantalla de reloj ", e);
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                }).start();
             }
        });
        try
        {
            GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);
            String selectedFile = seleccionaFichero(auxDocument.getFileName());
            if (selectedFile == null) return;
            
         // NUEVO
         			if (!AlfrescoManagerUtils.isAlfrescoClientActive()) {
         				RandomAccessFile outFile = new RandomAccessFile(selectedFile,
         						"rw");

         				if (auxDocument == null)
         					return;

         				outFile.write(auxDocument.getContent());
         				outFile.close();
         			} else {
         				if (localgisIntegrationManager.downloadAssociateDocument(
         						auxDocument.getId(), selectedFile))
         					// CAMBIAR
         					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
         							"Documento descargado correctamente");
         				else
         					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
         							"Error al descargar el documento");

         			}
         			// FIN NUEVO
            /* visualizamos si el SO es Windows */
            if(CUtilidades.isWindows())
            {
                try
                {
                    Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" +  selectedFile+ "\"");
                }
                catch(Exception ex)
                {
                    logger.error("Error al abrir el documento : ", ex);
                }
            }
        }
        catch(Exception e)
        {
             logger.error("Error al visualizar un documento ", e);
             ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
        }
    }

    /* método xa visualizar en un editor el documento seleccionado de una lista */
    private void visualizar()
    {
        if(panelDocumentos.getdocumentSelected() == null) return;
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.abrir"));
        progressDialog.report(aplicacion.getI18nString("document.infodocument.abrir"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                        	auxDocument = panelDocumentos
									.getdocumentSelected();
							// NUEVO
							if (!AlfrescoManagerUtils.isAlfrescoClientActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())) 
								auxDocument.setContent(documentClient
										.getAttachedByteStream(auxDocument));
							// FIN NUEVO
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al mostrar la pantalla de reloj ", e);
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                  }).start();
            }
        });

        try
        {
        	GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);
            if(auxDocument == null) return;
            if (auxDocument.getFileName() == null) return;
            String nombreFicheroTemporal=System.getProperty("java.io.tmpdir")+auxDocument.getFileName();
        	// NUEVO
			if (!AlfrescoManagerUtils.isAlfrescoClientActive()) {
				RandomAccessFile outFile = new RandomAccessFile(
						nombreFicheroTemporal, "rw");

				if (auxDocument == null)
					return;

				outFile.write(auxDocument.getContent());
				outFile.close();
			} else {
				if (localgisIntegrationManager.downloadAssociateDocument(
						auxDocument.getId(), nombreFicheroTemporal))
					System.out.println("BIEN");
				else
					System.out.println("MAL");
			}
			// FIN NUEVO

            /* visualizamos si el SO es Windows */
            if(CUtilidades.isWindows())
            {
                   try
                    {
                         Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" +  nombreFicheroTemporal+ "\"");
                    }
                    catch(Exception ex)
                    {
                            logger.error("Error al abrir el documento : ", ex);
                    }
            }
        }
        catch(Exception e)
        {
             logger.error("Error al visualizar un documento ", e);
             ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
        }
    }

    public void setFd(FeatureDialogHome fd) {
        this.fd = fd;
    }

    /* método xa abrir el directorio dd queremos dejar el texto que estamos abriendo xa visualizar*/
    private String seleccionaFichero(String filename) throws Exception
    {
        File f= new File(filename);
        /* Dialogo para seleccionar donde dejar el fichero */
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setSelectedFile(f);
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return null;

        File selectedFile= chooser.getSelectedFile();
        if (selectedFile ==null) return null;
        String tmpDir= "";
        String tmpFile= selectedFile.getAbsolutePath();
        if (tmpFile.lastIndexOf(selectedFile.getName()) != -1)
        {
            tmpDir= tmpFile.substring(0, tmpFile.lastIndexOf(selectedFile.getName()));
        }
        /** Comprobamos si existe el directorio. */
        try
        {
            File dir = new File(tmpDir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
        } catch (Exception ex)
        {
            logger.error("Error al seleccionar un fichero ", ex);
        }
        return selectedFile.getAbsolutePath();
    }

    private void docManager() {
		new AlfrescoExplorer(aplicacion.getMainFrame(),
				getSelectedFeatures(), this.getFeatureDocumentsId(), String.valueOf(aplicacion
						.getIdMunicipio()), AlfrescoConstants.APP_GENERAL);
		loadDocumentPanel();
	}
    
    /* Cuando nos movemos x la ventana de atributos */
    public void enter()
    {
        //De momento va vacío
    }

    /* De momento no se usa */
    public void exit()
    {
        //De momento va vacío
    }

    /* añadimos un texto en local */
    private void anadirlocal()
    {
        JDialogLocal jDialogLocal = new JDialogLocal(aplicacion.getMainFrame(), DocumentBean.TEXT_CODE);
        jDialogLocal.show();
        if(!jDialogLocal.okCancelPanel.wasOKPressed()) return;
        auxDocument = new DocumentBean();
        auxDocument = jDialogLocal.save(auxDocument);
        auxDocument.setIsTexto();
        if(auxDocument.getFileName() == null) return;

        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("text.infotext.salvando.title"));
        progressDialog.report(aplicacion.getI18nString("text.infotext.salvando"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        /* añadimos el documento a la lista */
                        try
                        {
                        	Object[] array = getSelectedFeatures();
							if (!AlfrescoManagerUtils.isAlfrescoClientActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())) {
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
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al añadir el documento ", e);
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                            return;
                        }
                        finally
                        {
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
    
    /* añadimos un texto en BD */
    private void anadirBD()
    {
        final JDialogBD jDialogBD = new JDialogBD(aplicacion.getMainFrame(), DocumentBean.TEXT_CODE);
        jDialogBD.show();
        if(!jDialogBD.okCancelPanel.wasOKPressed()) return;
        auxDocument = jDialogBD.get();
        auxDocument.setIsTexto();
        if(auxDocument.getFileName() == null) return;
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("text.infotext.salvando.title"));
        progressDialog.report(aplicacion.getI18nString("text.infotext.salvando"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            /* añadimos el documento a la BD */
                            Vector lista = new Vector();
                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
                            {
                                DocumentPanel auxDocumentPanel=(DocumentPanel)e.nextElement();
                                if (!auxDocumentPanel.existDocument(auxDocument))
                                    lista.add(auxDocumentPanel.getgFeature());
                                else
                                {
                                    int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                                    aplicacion.getI18nString("document.infodocument.aviso.mensaje") +  " " +
                                            auxDocumentPanel.getgFeature().getSystemId(),
                                            "",
                                            JOptionPane.OK_CANCEL_OPTION,
                                            JOptionPane.WARNING_MESSAGE,
                                            null, null, null);
                                    if(n == JOptionPane.NO_OPTION) return;
                                }
                            }
                            if(lista == null || lista.size()==0) return;
                            documentClient.linkDocument(lista, auxDocument);
                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
                            {
                                DocumentPanel auxDocumentPanel=(DocumentPanel)e.nextElement();
                                if (!auxDocumentPanel.existDocument(auxDocument))
                                  auxDocumentPanel.add(auxDocument);
                            }
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al asociar el documento a la feature ", e);
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                            return;
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }

    /* método xa cargar los comentarios y el contenido asociados a un texto */
    public void seleccionar(DocumentBean documento)
    {
        if(documento == null) return;
        panelComentarios.load(documento);
        try
        {
            if(documento.getContent() == null)
            {
                documento.setContent(documentClient.getAttachedByteStream(documento));
            }
            jTextPanel.loadTextBD(documento);
        }
        catch(Exception e)
        {
            logger.error("Error al seleccionar el archivo ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
        }
    }

    /* método de la interfaz sin implementar */
    public void seleccionar(DocumentBean documentBean, int indicePanel)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /* modificamos un documento ya existente */
    private void modifyDocument()
    {
        final JDialogText jDialogText = new JDialogText(aplicacion.getMainFrame());
        if(panelDocumentos.getdocumentSelected() == null) return;
        auxDocument = (DocumentBean) panelDocumentos.getdocumentSelected();//.clone();
        jDialogText.load(auxDocument);
        jDialogText.show();
        if(!jDialogText.okCancelPanel.wasOKPressed()) return;
        auxDocument = jDialogText.save(auxDocument);
        auxDocument.setIsTexto();

        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("text.infotext.salvando.title"));
        progressDialog.report(aplicacion.getI18nString("text.infotext.salvando"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {                        	
                        	if (!AlfrescoManagerUtils.isAlfrescoClientActive() && AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())) {
								auxDocument = documentClient.updateDocument(auxDocument);
	                            panelDocumentos.seleccionar(auxDocument);
	                            seleccionar(auxDocument);
							} 
							else {
								// CAMBIAR: Mirar si ya existe el documento (del
								// tipo ) o no y crear nueva version
								if (localgisIntegrationManager.associateAlfrescoDocument(
										String.valueOf(aplicacion
												.getIdMunicipio()),
										AlfrescoConstants.APP_GENERAL, panelDocumentos.getdocumentSelected().getFileName(),
										auxDocument, documentClient)){
									panelDocumentos.getDocumentos().remove(panelDocumentos.getdocumentSelected());									
									setFeatureDocuments(auxDocument);
									panelDocumentos.seleccionar(auxDocument);
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
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al modificar el documento ", e);
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                            return;
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }

    /** si la feature no está accesible, deshabilitamos los botones con los q operamos sobre
      * sus documentos */
    public void setEnabled (boolean estado)
    {
        bAnadir.setEnabled(estado);
        bModificar.setEnabled(estado);
        bBorrar.setEnabled(estado);
    }

    /* obtenemos la feature seleccionada */
    public GeopistaFeature getSelectedFeature()
    {
        if (panelDocumentos==null) return null;
        return panelDocumentos.getgFeature();
    }

	public void setFeature(GeopistaFeature feature) {
        Blackboard identificadores = aplicacion.getBlackboard();
        /* devuelve un array */
        Object[] lista = (Object[])identificadores.get("feature");

        for(int i=0; i<lista.length; i++)
        {
			GeopistaFeature featureTab = (GeopistaFeature) lista[i];
			if (featureTab.getSystemId() == feature.getSystemId()) {
				jTabbedPane.setSelectedIndex(i);
				panelDocumentos = (DocumentPanel) vPaneles.get(i);
			}
		}	
	}
     class ActionJList extends MouseAdapter{
    public ActionJList(){
   }

  public void mouseClicked(MouseEvent e){
   if(e.getClickCount() == 2){
       visualizar();
   }
   }
}
}
