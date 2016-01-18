/**
 * InfoImagePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.image;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.document.DocumentInterface;
import com.geopista.app.document.DocumentPanel;
import com.geopista.app.document.JDialogBD;
import com.geopista.app.document.JDialogInternet;
import com.geopista.app.document.JDialogLocal;
import com.geopista.app.document.JFrameOpciones;
import com.geopista.app.licencias.CUtilidadesComponentes_LCGIII;
import com.geopista.app.utilidades.UtilsResource;
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
import com.geopista.ui.dialogs.ImageDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 08-may-2006
 * Time: 11:45:31
 */

public class InfoImagePanel extends JPanel implements FeatureExtendedPanel
{
    private static final Log logger = LogFactory.getLog(InfoImagePanel.class);

    /* Paneles */
    private JPanel jPanelButtons = new JPanel();
    private ImagePanel panelImagen;

    /* Botones jPanelButtons */
    private JButton bAnadir = new JButton();
    private JButton bModificar = new JButton();
    private JButton bBorrar = new JButton();
    private JButton bDescargar = new JButton();

    private JButton bAlfrescoManager = new JButton();
    
    private LocalgisIntegrationManager localgisIntegrationManager;
    
    private AppContext aplicacion;

    DocumentClient documentClient = null;
    DocumentInterface docInt;
    private Vector vPaneles = new Vector();
    /* Necesario xa mostrar la pantalla del reloj */
    private DocumentBean auxDocument;

    private FeatureDialogHome fd;
    private JTabbedPane jTabbedPane;

    /* constructor de la clase q llama al método que inicializa el panel */
    public InfoImagePanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            logger.error("Error en la llamada al constructor", e);
        }
    }

    /* método xa inicializar el panel */
    private void jbInit() throws Exception
    {    	
    	aplicacion = (AppContext) AppContext.getApplicationContext();
        Blackboard identificadores = aplicacion.getBlackboard();
        Hashtable hFeaturesDocs = new Hashtable();
        Object[] lista = (Object[])identificadores.get("feature");
        
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
    	
    	/*String sUrl = aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                        ImageManagerPlugin.DOCUMENT_SERVLET_NAME;*/
        
        String sUrl = aplicacion
				.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)
				+ ServletConstants.DOCUMENT_SERVLET_NAME;
        documentClient = new DocumentClient(sUrl);

        this.setName(aplicacion.getI18nString("image.infoimage.nombre"));
        this.setLayout(new BorderLayout());
       // this.setSize(new Dimension(600, 550));
        
        loadImagePanel();	
       

        panelImagen.setPreferredSize(new Dimension(450, 400));

        //Botones
        jPanelButtons.setLayout(new FlowLayout());
        bModificar.setText(aplicacion.getI18nString("document.infodocument.botones.modificar"));
        bModificar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                modifyImage();
            }
        });
        bAnadir.setText(aplicacion.getI18nString("document.infodocument.botones.anadir"));
        bAnadir.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addImage();
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
        bDescargar.setText(aplicacion.getI18nString("image.infoimage.botones.descargar"));
        bDescargar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                downloadImage();
            }
        });
        /* Añadimos los componentes al panel */
        jPanelButtons.add(bAnadir, null);
        jPanelButtons.add(bModificar, null);
        jPanelButtons.add(bBorrar, null);
        jPanelButtons.add(bDescargar, null);

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
    
    private void docManager() {
		new AlfrescoExplorer(aplicacion.getMainFrame(),
				getSelectedFeatures(), this.getFeatureImagesId(), String.valueOf(aplicacion
						.getIdMunicipio()), AlfrescoConstants.APP_GENERAL);
		loadImagePanel();
	}
        
    public ArrayList<String> getFeatureImagesId() {
    	ArrayList<String> idDocuments = new ArrayList<String>();
		for (Enumeration e = vPaneles.elements(); e.hasMoreElements();){
			ArrayList<DocumentBean> documents = new ArrayList<DocumentBean>(((ImagePanel) e.nextElement()).imagenes);	
			Iterator<DocumentBean> it = documents.iterator();
			while(it.hasNext()){
				DocumentBean documentBean = it.next();
				idDocuments.add(documentBean.getId());
			}
		}
		return idDocuments;
	}
    
    private void loadImagePanel(){
		/* Cargamos la lista */
		Blackboard identificadores = aplicacion.getBlackboard();
		final Hashtable hFeaturesDocs = new Hashtable();
		/* devuelve un array */
		final Object[] lista = (Object[]) identificadores.get("feature");
	

		if (lista != null) {
			if (lista.length == 1) {
				try {
					GeopistaFeature feature = (GeopistaFeature) lista[0];
            		Collection collection = documentClient.getAttachedImages(feature);
            		if (feature.getLayer() == null || (feature.getLayer() instanceof GeopistaLayer && ((GeopistaLayer)feature.getLayer()).isLocal())){                    	
                    	setEnabled(false);
                    }
            		if (collection == null){
            			collection = new ArrayList();
            		}
										
					if(panelImagen != null){
						panelImagen.actualizarModelo((ArrayList) collection);
					}
					else{
						ImagePanel jImagePanel = new ImagePanel(feature,
								collection, docInt);
						panelImagen = jImagePanel;
						this.add(jImagePanel, BorderLayout.NORTH);
						
					}
					
//					ImagePanel jPanel = new ImagePanel(feature,
//							collection, docInt);
//					this.add(jPanel, BorderLayout.NORTH);
//					//jPanel.addMouseListener(new ActionJList());
//					panelImagen = jPanel;
					// vPaneles.add(jPanel);
					vPaneles.removeAllElements();
					vPaneles.add(panelImagen);
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
				final InfoImagePanel aux = this;
				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
						aplicacion.getMainFrame(), null);
				// progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.abrir"));
				// progressDialog.report(aplicacion.getI18nString("document.infodocument.abrir"));
				progressDialog.addComponentListener(new ComponentAdapter() {
					public void componentShown(ComponentEvent e) {
						new Thread(new Runnable() {
							public void run() {
								try
				                {
				                    jTabbedPane = new JTabbedPane();
				                    jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
				                    for(int i=0; i<lista.length; i++)
				                    {
				                        GeopistaFeature feature = (GeopistaFeature) lista[i];
				                        Collection collection = documentClient.getAttachedImages(feature);
				                        ImagePanel jPanel = new ImagePanel(feature, collection, docInt);
				                        hFeaturesDocs.put(feature.getSystemId(), collection);
				                        /* mostramos la id de la feature en el panel xa distinguirlas */
				                        String nombre = feature.getSystemId();
				                        jTabbedPane.addTab(nombre, jPanel);
				                        aux.add(jTabbedPane, BorderLayout.NORTH);
				                        vPaneles.add(jPanel);                            
				                    }
				                    jTabbedPane.addChangeListener(new ChangeListener()
				                    {
				                            public void stateChanged(ChangeEvent evt)
				                            {
				                                JTabbedPane pane = (JTabbedPane)evt.getSource();
				                                int sel = pane.getSelectedIndex();
				                                panelImagen = (ImagePanel)vPaneles.get(sel);
				                                if (fd instanceof ImageDialog)
				                                                     ((ImageDialog)fd).setDescription();

				                            }
				                    });
				                    jTabbedPane.setSelectedIndex(0);
				                    panelImagen = (ImagePanel)vPaneles.get(0);
				                }
				                catch(Exception e)
				                {
				                    logger.error("Error al mostrar las imagenes de varias features", e);
				                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
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
    
    private Object[] getSelectedFeatures() {
		Object[] array = new GeopistaFeature[vPaneles.size()];
		int i = 0;
		for (Enumeration e = vPaneles.elements(); e.hasMoreElements();) {
			array[i] = ((ImagePanel) e.nextElement()).getgFeature();
			i++;
		}
		return array;
	}
    
	public void setFeatureDocuments(DocumentBean document) {
		for (Enumeration e = vPaneles.elements(); e.hasMoreElements();)
			((ImagePanel) e.nextElement()).add(document);
	}

    public void setFd(FeatureDialogHome fd) {
        this.fd = fd;
    }

    /* método q realiza las llamadas correspondientes xa añadir nuevas imagenes */
    private void addImage()
    {
//        JFrameOpciones frameOpciones = new JFrameOpciones(aplicacion.getMainFrame());
//        frameOpciones.show();
//        if (!frameOpciones.isAceptar()) return;
//        if (frameOpciones.isLocal())
//           anadirlocal();
//        else if (frameOpciones.isInternet())
//            anadirInternet();
//        else
//        {
//            anadirBD();
//        }        
        if (!AlfrescoManagerUtils.isAlfrescoClientActive()) {
			JFrameOpciones frameOpciones = new JFrameOpciones(
					aplicacion.getMainFrame());
			frameOpciones.show();
			if (!frameOpciones.isAceptar())
				return;
			if (frameOpciones.isLocal())
				// FIN NUEVO
				anadirlocal();
			else if (frameOpciones.isInternet())
				anadirInternet();
			else
				anadirBD();
		} else
			anadirlocal();
    }

    /* añadimos una imagen en local */
    private void anadirlocal()
    {
        JDialogLocal jDialogLocal = new JDialogLocal(aplicacion.getMainFrame(), DocumentBean.IMG_CODE);
        jDialogLocal.show();
        if(!jDialogLocal.okCancelPanel.wasOKPressed()) return;
        auxDocument = new DocumentBean();
        auxDocument = jDialogLocal.save(auxDocument);
        auxDocument.setIsImagen();
        if(auxDocument.getFileName() == null) return;

        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("imagen.infoimage.salvando.title"));
        progressDialog.report(aplicacion.getI18nString("image.infoimage.salvando"));
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
                        	Object[] array = getSelectedFeatures();
							if (!AlfrescoManagerUtils.isAlfrescoClientActive() && !AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())) {
								auxDocument = documentClient.attachDocument(array, auxDocument);
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
//                            Object[] array = new GeopistaFeature[vPaneles.size()];
//                            int i=0;
//                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
//                            {
//                                array[i] = ((ImagePanel)e.nextElement()).getgFeature();
//                                i++;
//                            }
//                            auxDocument = documentClient.attachDocument(array, auxDocument);
//                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
//                            {
//                                ((ImagePanel)e.nextElement()).add(auxDocument);
//                            }
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al añadir la imagen ", e);
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

    /* añadimos un documento en BD */
    private void anadirBD()
    {
        final JDialogBD jDialogBD = new JDialogBD(aplicacion.getMainFrame(),DocumentBean.IMG_CODE);
        jDialogBD.show();
        if(!jDialogBD.okCancelPanel.wasOKPressed()) return;
        auxDocument = jDialogBD.get();
        auxDocument.setIsImagen();
        if(auxDocument.getFileName() == null) return;

        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("document.infodocument.salvando.title"));
        progressDialog.report(aplicacion.getI18nString("document.infodocument.salvando"));
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
                                ImagePanel auxImagePanel=(ImagePanel)e.nextElement();
                                if (!auxImagePanel.existImage(auxDocument))
                                    lista.add(auxImagePanel.getgFeature());
                                else
                                {
                                    int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                                    aplicacion.getI18nString("image.infoimage.aviso.mensaje") +  " " +
                                            auxImagePanel.getgFeature().getSystemId(),
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
                                ImagePanel auxImagePanel=(ImagePanel)e.nextElement();
                                if (!auxImagePanel.existImage(auxDocument))
                                  auxImagePanel.add(auxDocument);
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
    
    
    /* añadimos una imagen de internet */
    private void anadirInternet()
    {
        JDialogInternet jDialogInternet= new JDialogInternet(aplicacion.getMainFrame(), DocumentBean.IMG_CODE);
        jDialogInternet.show();
        if(!jDialogInternet.okCancelPanel.wasOKPressed()) return;
        auxDocument = new DocumentBean();
        auxDocument = jDialogInternet.save(auxDocument);
        auxDocument.setIsImagen();
        if(auxDocument.getFileName() == null) return;

        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("imagen.infoimage.salvando.title"));
        progressDialog.report(aplicacion.getI18nString("image.infoimage.salvando"));
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
                            Object[] array = new GeopistaFeature[vPaneles.size()];
                            int i=0;
                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
                            {
                                array[i] = ((ImagePanel)e.nextElement()).getgFeature();
                                i++;
                            }
                            auxDocument = documentClient.attachDocument(array, auxDocument);
                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
                            {
                                ((ImagePanel)e.nextElement()).add(auxDocument);
                            }
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al añadir la imagen ", e);
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


    /* método xa eliminar la imagen seleccionada de la lista */
    private void eliminar()
    {
        if(panelImagen.getImageSelected() == null) return;
        int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                aplicacion.getI18nString("document.infodocument.respuesta.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        if(n == JOptionPane.NO_OPTION) return;
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("imagen.infoimage.eliminando.title"));
        progressDialog.report(aplicacion.getI18nString("image.infoimage.eliminando"));
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
                            feature = panelImagen.getgFeature();
                            DocumentBean document = (DocumentBean)panelImagen.getImageSelected();
                            documentClient.detachDocument(feature, document);
                            /* borrar el elemento de la lista */
                            panelImagen.borrar(document);
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al eliminar una imagen", e);
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

    /* modificamos una imagen ya existente */
    private void modifyImage()
    {
        final JDialogLocal jDialogLocal = new JDialogLocal(aplicacion.getMainFrame(), DocumentBean.IMG_CODE);
        if(panelImagen.getImageSelected() == null) return;
        auxDocument = (DocumentBean)panelImagen.getImageSelected().clone();
        jDialogLocal.load(auxDocument);
        jDialogLocal.show();
        if(!jDialogLocal.okCancelPanel.wasOKPressed()) return;
        auxDocument = jDialogLocal.save(auxDocument);
        auxDocument.setIsImagen();
        auxDocument.setContent(null);
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("imagen.infoimage.salvando.title"));
        progressDialog.report(aplicacion.getI18nString("image.infoimage.salvando"));
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
                        	if (!AlfrescoManagerUtils.isAlfrescoClientActive() && !AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio())) {
								if (jDialogLocal.isNewDocument()) {
									File file = new File(auxDocument.getFileName());
									auxDocument.setFileName(file.getName());
									documentClient.updateDocument(auxDocument,
											file);
								} else {
									documentClient
											.updateDocumentByteStream(auxDocument);
								}		
								//setFeatureDocuments(document);
								panelImagen.seleccionar(auxDocument);
							} 
							else {
								// CAMBIAR: Mirar si ya existe el documento (del
								// tipo ) o no y crear nueva version
								if (localgisIntegrationManager.associateAlfrescoDocument(
										String.valueOf(aplicacion
												.getIdMunicipio()),
										AlfrescoConstants.APP_GENERAL, panelImagen.getImageSelected().getFileName(),
										auxDocument, documentClient)){
									panelImagen.imagenes.remove(panelImagen.getImageSelected());									
									setFeatureDocuments(auxDocument);
									panelImagen.seleccionar(auxDocument);
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
//                            auxDocument = documentClient.updateDocument(auxDocument);
//                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
//                            {
//                                ((ImagePanel)e.nextElement()).update(auxDocument);
//                            }
                        }
                        catch(Exception e)
                        {
                            logger.error("Error al modificar una imagen ", e);
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

    /* descargamos la imagen */
    private void downloadImage()
    {
        if(panelImagen.getImageSelected() == null) return;
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("image.infoimage.abrir"));
        progressDialog.report(aplicacion.getI18nString("image.infoimage.abrir"));
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
                        	auxDocument = panelImagen.getImageSelected();
                        	if (!AlfrescoManagerUtils.isAlfrescoClientActive() && !AlfrescoManagerUtils.isAlfrescoUuid(auxDocument.getId(), auxDocument.getIdMunicipio()))
								 if (auxDocument.getFileName().startsWith("http"))
		                            	auxDocument.setContent(UtilsResource.getBytesFromResource(auxDocument));
								 else
									 auxDocument.setContent(documentClient.getAttachedByteStream(auxDocument));
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
			if (auxDocument == null)
				return;
			// String selectedFile =
			// seleccionaFichero(auxDocumento.getFileName());
			// System.out.println("Nombre del documento:"+auxDocumento.getFileName());
			String selectedFile = seleccionaFichero(auxDocument.getFileName());
			if (selectedFile == null)
				return;
			
//			if (auxDocument.getFileName() == null)
//				return;
			// File tempFile=File.createTempFile(auxDocumento.getFileName(),
			// null);
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
			if (CUtilidadesComponentes_LCGIII.isWindows()) {
				try {
					Runtime.getRuntime().exec(
							"rundll32 SHELL32.DLL,ShellExec_RunDLL \""
									+ selectedFile + "\"");
				} catch (Exception ex) {
					logger.error("Error al abrir el documento : ", ex);
				}
			}
			
//			String nombreFicheroTemporal=null;
//			 if (auxDocument.getFileName().startsWith("http"))
//				 nombreFicheroTemporal = System.getProperty("java.io.tmpdir")+ "download";
//			 else
//				 nombreFicheroTemporal = System.getProperty("java.io.tmpdir")+ auxDocument.getFileName();
//			// NUEVO
//			if (!AlfrescoManagerUtils.isAlfrescoClientActive()) {
//				RandomAccessFile outFile = new RandomAccessFile(nombreFicheroTemporal, "rw");
//
//				if (auxDocument == null)
//					return;
//
//				outFile.write(auxDocument.getContent());
//				outFile.close();
//			} else {
//				if (localgisIntegrationManager.downloadAssociateDocument(
//						auxDocument.getId(), nombreFicheroTemporal))
//					System.out.println("BIEN");
//				else
//					System.out.println("MAL");
//			}
//            GUIUtil.centreOnWindow(progressDialog);
//            progressDialog.setVisible(true);
//            String selectedFile = seleccionaFichero(auxDocument.getFileName());
//            if(selectedFile == null) return;
//            RandomAccessFile outFile = new RandomAccessFile(selectedFile, "rw");
//
//            if(auxDocument == null) return;
//
//            outFile.write(auxDocument.getContent());
//            outFile.close();
        }
        catch(Exception e)
        {
            logger.error("Error al visualizar un documento ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
        }
    }

    
    /* método xa abrir el directorio dd queremos dejar el documento que estamos abriendo xa visualizar*/
    private String seleccionaFichero(String filename) throws Exception
    {
        File f = new File(filename);
        /* dialogo xa seleccionar dd dejar el fichero */
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setSelectedFile(f);
        if(chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return null;

        File selectedFile = chooser.getSelectedFile();
        if(selectedFile == null) return null;
        String tmpDir = "";
        String tmpFile = selectedFile.getAbsolutePath();
        if(tmpFile.lastIndexOf(selectedFile.getName()) != -1)
        {
            tmpDir = tmpFile.substring(0, tmpFile.lastIndexOf(selectedFile.getName()));
        }

        /* comprobamos si existe el directorio */
        try
        {
            File dir = new File(tmpDir);
            if(!dir.exists())
                dir.mkdirs();
        }
        catch(Exception e)
        {
            logger.error("Error al seleccionar un fichero ", e);
        }
        return selectedFile.getAbsolutePath();
    }

    /** si la feature no está accesible, deshabilitamos los botones con los q operamos sobre
      * sus imagenes */
    public void setEnabled(boolean estado)
    {
        bAnadir.setEnabled(estado);
        bModificar.setEnabled(estado);
        bBorrar.setEnabled(estado);
    }

    /* cd nos movemos x la ventana de atributos */
    public void enter()
    {
        //de momento va vacio
    }

    /* de momento no se usa */
    public void exit()
    {
        //de momento va vacío
    }
    public  GeopistaFeature getSelectedFeature()
    {
           if (panelImagen==null) return null;
           return panelImagen.getgFeature();
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
				panelImagen = (ImagePanel) vPaneles.get(i);
			}
		}	
	}
	
}
