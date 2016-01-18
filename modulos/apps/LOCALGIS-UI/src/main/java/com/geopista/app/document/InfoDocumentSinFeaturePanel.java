/**
 * InfoDocumentSinFeaturePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.utilidades.CUtilidadesComponentes;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.ui.plugin.edit.DocumentSinFeatureManagerPlugin;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class InfoDocumentSinFeaturePanel extends  JPanel implements DocumentInterface,FeatureExtendedPanel
{
    private static final Log logger = LogFactory.getLog(InfoDocumentSinFeaturePanel.class);

    /* Paneles */
    private JPanel jPanelButtons = new JPanel();
    private DocumentPanelSinFeature panelDocumentos = null;
    private JPanelComentariosSinFeature panelComentarios = null;

    /* Botones jPanelButtons */
    private JButton bAnadir = new JButton();
//    private JButton bModificar = new JButton();
    private JButton bBorrar = new JButton();
//    private JButton bGuardar = new JButton();
    private JButton bVisualizar = new JButton();

    private DocumentClient documentClient = null;
    private AppContext aplicacion;
    private Vector vPaneles = new Vector();
    private DocumentBean document;
    private DocumentBean auxDocumento;
    private FeatureDialogHome fd;

    /* Necesario xa mostrar la pantalla del reloj*/
    private DocumentBean auxDocument = new DocumentBean();

    DocumentInterface docInt;
    JTabbedPane jTabbedPane=null;

    /* constructor de la clase q llama al método que inicializa el panel */
    public InfoDocumentSinFeaturePanel()
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

        this.setName(aplicacion.getI18nString("document.infodocument.panel.listado"));
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(800, 550));


        String sUrl=aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
                DocumentSinFeatureManagerPlugin.DOCUMENT_SERVLET_NAME;
        documentClient= new DocumentClient(sUrl);

        try
        {
            Collection collection = documentClient.getAttachedDocumentsSinFeature();
            
            if (collection == null){
            	collection = new ArrayList();
            }
            
            DocumentPanelSinFeature jPanel = new DocumentPanelSinFeature(collection, this);
            this.add(jPanel, BorderLayout.NORTH);
            jPanel.addMouseListener(new ActionJList());
            panelDocumentos = jPanel;
            
            vPaneles.add(jPanel);
        }
        catch(Exception e)
        {
            logger.error("Error al mostrar los documentos ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        /** Comentarios
          * Panel */
        JPanelComentariosSinFeature jPanelComentarios = new JPanelComentariosSinFeature();
        jPanelComentarios.setEnabled(false);
        this.add(jPanelComentarios, BorderLayout.CENTER);
        panelComentarios = jPanelComentarios;

        /** Botones
          * Panel */
        jPanelButtons.setLayout(new FlowLayout());
        /* Componentes */
//        bModificar.setText(aplicacion.getI18nString("document.infodocument.botones.modificar"));
//        bModificar.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                modifyDocument();
//            }
//        });
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
//        bGuardar.setText(aplicacion.getI18nString("document.infodocument.botones.guardar"));
//        bGuardar.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                guardar();
//            }
//        });
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
//        jPanelButtons.add(bModificar);
        jPanelButtons.add(bBorrar);
//        jPanelButtons.add(bGuardar);
        jPanelButtons.add(bVisualizar);

        /* Añadimos los paneles */
        this.add(jPanelButtons, BorderLayout.SOUTH);
        
        ListSelectionModel rowSM = panelDocumentos.getjListDocuments().getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
            	
            	DocumentBean document = (DocumentBean)panelDocumentos.getdocumentSelected();
            	if (document!= null && !document.getPropietario()){
            		bBorrar.setEnabled(false);
            	}
            	else{
            		bBorrar.setEnabled(true);
            	}
            		
            }
        });
    }

    
    public void setFd(FeatureDialogHome fd) {
        this.fd = fd;
    }

    /* método xa eliminar el documento seleccionado de la lista */
    private void eliminar()
    {
        if(panelDocumentos.getdocumentSelected() == null) return;
        int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                aplicacion.getI18nString("document.infodocument.respuesta.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        if(n == JOptionPane.NO_OPTION) return;
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
                            DocumentBean document = (DocumentBean)panelDocumentos.getdocumentSelected();
                            documentClient.detachDocumentSinFeature(document);
                            /* borrar el elemento de la lista */
                            panelDocumentos.borrar();
                            panelComentarios.load(null);
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

    /* método q realiza las llamadas correspondientes xa añadir nuevos documentos*/
    private void addDocument()
    {
  //      JFrameOpciones frameOpciones = new JFrameOpciones(aplicacion.getMainFrame());
  //      frameOpciones.show();
  //      if (!frameOpciones.isAceptar()) return;
  //      if (frameOpciones.isLocal())
           anadirlocal();
  //      else
  //         anadirBD();
    }

    /* método xa visualizar en un editor el documento seleccionado de una lista */
    private void visualizar()
    {
    	//String tempdir = System.getProperty("java.io.tmpdir");
    	//System.out.println("Directorio temporal:"+tempdir);

    	//System.out.println("Visualizando documentos");
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
                            auxDocumento = panelDocumentos.getdocumentSelected();
                            auxDocumento.setContent(documentClient.getAttachedByteStream(auxDocumento));
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
            if(auxDocumento == null) return;            
            //String selectedFile = seleccionaFichero(auxDocumento.getFileName());
            //System.out.println("Nombre del documento:"+auxDocumento.getFileName());
            if (auxDocumento.getFileName() == null) return;
            //File tempFile=File.createTempFile(auxDocumento.getFileName(), null);
            String nombreFicheroTemporal=System.getProperty("java.io.tmpdir")+auxDocumento.getFileName();
            RandomAccessFile outFile = new RandomAccessFile( nombreFicheroTemporal, "rw");
            //System.out.println("Nombre del documento temporal:"+nombreFicheroTemporal);
            //System.out.println("Nombre del documento temporal:"+tempFile.getAbsolutePath());
            
            outFile.write(auxDocumento.getContent());
            outFile.close();

            /* visualizamos si el SO es Windows */
            if(CUtilidadesComponentes.isWindows())
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
    /* método xa abrir el directorio dd queremos dejar el documento que estamos abriendo xa visualizar*/
    private String seleccionaFichero(String filename) throws Exception
    {
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

    /* añadimos un documento en local */
    private void anadirlocal()
    {
        JDialogLocalSinFeature jDialogLocal = new JDialogLocalSinFeature(aplicacion.getMainFrame(), DocumentBean.DOC_CODE);
        jDialogLocal.show();
        if(!jDialogLocal.okCancelPanel.wasOKPressed()) return;
        auxDocument= new DocumentBean();
        auxDocument = jDialogLocal.save(auxDocument);
        auxDocument.setIsDocument();
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
                        /* añadimos el documento a la lista */
                        try
                        {

                            auxDocument=documentClient.attachDocumentSinFeature(auxDocument);
                            for (Enumeration e=vPaneles.elements();e.hasMoreElements();)
                            {
                                ((DocumentPanelSinFeature)e.nextElement()).add(auxDocument);
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

    /* método xa cargar los comentarios y la opción de público asociados a un documento */
    public void seleccionar(DocumentBean documento)
    {
        panelComentarios.load(documento);
    }

    /* método de la interfaz no implementado */
    public void seleccionar(DocumentBean documentBean, int indicePanel)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    /** si la feature no está accesible, deshabilitamos los botones con los q operamos sobre
      * sus documentos */
    public void setEnabled(boolean estado)
    {
        bAnadir.setEnabled(estado);
//        bModificar.setEnabled(estado);
        bBorrar.setEnabled(estado);
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
