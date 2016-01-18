package com.geopista.app.document;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import java.util.Collection;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 08-may-2006
 * Time: 10:31:19
 * To change this template use File | Settings | File Templates.
 */

public class JDialogBD extends JDialog
{
    private static final Log logger = LogFactory.getLog(JDialogBD.class);

    private DocumentPanel documentPanel;
    public OKCancelPanel okCancelPanel = new OKCancelPanel();
    private ApplicationContext aplicacion;

     /* constructor de la clase */
    public JDialogBD(JFrame frame, char tipo)
    {
    	super(frame);
    	aplicacion = (AppContext) AppContext.getApplicationContext();
        setTitle(aplicacion.getI18nString("document.infodocument.panel.titulo"));
        
        GeopistaFeature feature= null;
        
        getContentPane().setLayout(new BorderLayout());
        /* Cargamos la lista */
        Blackboard identificadores = aplicacion.getBlackboard();
        Object[] lista = (Object[])identificadores.get("feature");
        String sUrl = aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		ServletConstants.DOCUMENT_SERVLET_NAME;
        DocumentClient documentClient = new DocumentClient(sUrl);

      //  if(lista != null)
      //  {
            try
            {
                Collection collection;
                if (lista!=null && lista.length>0)
                	feature = (GeopistaFeature) lista[0];
                collection = documentClient.get(tipo);
                documentPanel = new DocumentPanel(feature, collection, null);
            }
            catch(Exception e)
            {
                logger.error("Error al mostrar la lista de objetos ", e);
                ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
            }
        //}
        okCancelPanel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
				okCancelPanel_actionPerformed(e);
			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter()
        {
			public void componentShown(ComponentEvent e)
            {
				this_componentShown(e);
			}
		});

        setModal(true);
        getContentPane().add(documentPanel, BorderLayout.CENTER);
        getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        setSize(500, 300);
        setLocation(150, 90);
    }

    /* salvamos la imagen, el documento o el texto seleccionados en el panel */
    public DocumentBean get()
    {
        return documentPanel.getdocumentSelected();
     }

    /* método xa cargar el documento, imagen o texto */
    public void load(DocumentBean documento)
    {
        if(documento.isImagen() != true)
            documento = documentPanel.getdocumentSelected();
        else
            documento = documentPanel.getdocumentSelected();
        documentPanel.add(documento);
    }

    /* sólo se realiza una accion si se ha seleccionado un elemento de la lista */
    void okCancelPanel_actionPerformed(ActionEvent e)
    {
         if(okCancelPanel.wasOKPressed())
         {
             if(documentPanel.getdocumentSelected() == null) return;
         }
         setVisible(false);
         return;
    }

    /* recogemos el evento cd se pulsa Aceptar */
    void this_componentShown(ComponentEvent e)
    {
        okCancelPanel.setOKPressed(false);
    }
}
