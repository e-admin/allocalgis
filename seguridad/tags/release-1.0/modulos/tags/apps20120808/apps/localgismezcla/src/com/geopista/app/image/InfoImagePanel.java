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
import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.app.document.DocumentInterface;
import com.geopista.app.document.JDialogBD;
import com.geopista.app.document.JDialogLocal;
import com.geopista.app.document.JFrameOpciones;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
//import com.geopista.ui.plugin.edit.DocumentManagerPlugin;
import com.geopista.ui.plugin.edit.ImageManagerPlugin;
import com.geopista.ui.dialogs.ImageDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.util.FeatureDialogHome;
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
        String sUrl = aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                        ImageManagerPlugin.DOCUMENT_SERVLET_NAME;
        documentClient = new DocumentClient(sUrl);

        this.setName(aplicacion.getI18nString("image.infoimage.nombre"));
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(600, 550));

        /* Cargamos la lista */
        if(lista != null)
        {
            if(lista.length == 1)
            {
            	try
            	{
            		GeopistaFeature feature = (GeopistaFeature) lista[0];
            		Collection collection = documentClient.getAttachedImages(feature);
            		if (feature.getLayer() == null || (feature.getLayer() instanceof GeopistaLayer && ((GeopistaLayer)feature.getLayer()).isLocal())){                    	
                    	setEnabled(false);
                    }
            		if (collection == null){
            			collection = new ArrayList();
            		}
            		ImagePanel jImagePanel = new ImagePanel(feature, collection, docInt);
            		this.add(jImagePanel, BorderLayout.CENTER);
            		panelImagen = jImagePanel;
            		vPaneles.add(jImagePanel);
            		
                }
                catch(Exception e)
                {
                    logger.error("Error al mostrar las imagenes de una feature", e);
                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                }
            }
            else  if(lista.length > 1)
            {
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
                        this.add(jTabbedPane, BorderLayout.NORTH);
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
                }
            }
        }

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

        /* Añadimos los paneles */
        this.add(jPanelButtons, BorderLayout.SOUTH);
    }

    public void setFd(FeatureDialogHome fd) {
        this.fd = fd;
    }

    /* método q realiza las llamadas correspondientes xa añadir nuevas imagenes */
    private void addImage()
    {
        JFrameOpciones frameOpciones = new JFrameOpciones(aplicacion.getMainFrame());
        frameOpciones.show();
        if (!frameOpciones.isAceptar()) return;
        if (frameOpciones.isLocal())
           anadirlocal();
        else
        {
            anadirBD();
        }
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
                                ImagePanel auxDocumentPanel=(ImagePanel)e.nextElement();
                                if (!auxDocumentPanel.existImage(auxDocument))
                                    lista.add(auxDocumentPanel.getgFeature());
                                else
                                {
                                    int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                                    aplicacion.getI18nString("image.infoimage.aviso.mensaje") +  " " +
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
                                ImagePanel auxDocumentPanel=(ImagePanel)e.nextElement();
                                if (!auxDocumentPanel.existImage(auxDocument))
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
        JDialogLocal jDialogLocal = new JDialogLocal(aplicacion.getMainFrame(), DocumentBean.IMG_CODE);
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
                            auxDocument = documentClient.updateDocument(auxDocument);
                            for(Enumeration e=vPaneles.elements(); e.hasMoreElements();)
                            {
                                ((ImagePanel)e.nextElement()).update(auxDocument);
                            }
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
            String selectedFile = seleccionaFichero(auxDocument.getFileName());
            if(selectedFile == null) return;
            RandomAccessFile outFile = new RandomAccessFile(selectedFile, "rw");

            if(auxDocument == null) return;

            outFile.write(auxDocument.getContent());
            outFile.close();
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
