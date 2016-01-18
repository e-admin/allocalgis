package com.geopista.app.document;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.geopista.app.AppContext;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.security.GeopistaPermission;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 05-may-2006
 * Time: 11:23:01
 * To change this template use File | Settings | File Templates.
 */

public class JPanelComentarios extends JPanel
{
    private JCheckBox cPublico = new JCheckBox();
    private JTextArea jAreaComments = new JTextArea();
    private AppContext aplicacion;
    /** Inventario */
    private JCheckBox cOculto= new JCheckBox();

    private static final Log logger = LogFactory.getLog(JPanelComentarios.class);

    /* constructor de la clase */
    public JPanelComentarios()
    {
        jbinit(false);
    }

    public JPanelComentarios(boolean fromInventario){
        jbinit(fromInventario);
    }

    /* constructor de la clase */
    public void jbinit(boolean b)
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(150, 150));
        aplicacion = (AppContext) AppContext.getApplicationContext();
        setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("document.infodocument.comentarios")));

        cPublico.setText(aplicacion.getI18nString("document.infodocument.publico"));
        cOculto.setText(aplicacion.getI18nString("inventario.document.tag9"));

        jAreaComments.setLineWrap(true);

        JScrollPane scroll = new JScrollPane();
        add(scroll, BorderLayout.CENTER);
        scroll.setViewportView(jAreaComments);
        /* Añadimos los componentes al panel */
        if (b){
            add(cOculto, BorderLayout.NORTH);
        }else{
            add(cPublico, BorderLayout.NORTH);
        }
    }

    /* método xa cargar los comentarios asociados a un documento, imagen o texto */
    public void load(DocumentBean document)
    {
        cPublico.setSelected(document==null?false:document.isPublic());
        cOculto.setSelected(document==null?false:document.isOculto());
        jAreaComments.setText(document==null||document.getComentario()==null? "":document.getComentario());
    }

    /**
     * metodo xa habilitar o deshabilitar la opcion de Publico, dependiendo de los
     * permisos q tengamos
     */
    public void setEnabled(boolean estado)
    {
        /* Comprobamos que tengo permisos para publicar el documentos */
        try
        {
            GeopistaPermission paso = new GeopistaPermission(GeopistaPermission.PUBLICAR_DOCUMENTO);
            cPublico.setEnabled(estado&&aplicacion.checkPermission(paso,"General"));
            cOculto.setEnabled(estado);
        }
        catch(Exception e)
        {
             logger.error("Error al comprobar los permisos ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
        }
        jAreaComments.setEnabled(estado);
    }

    /* salvamos el documento, imagen o texto */
    public DocumentBean save(DocumentBean document)
    {
        if(document == null) return null;
        document.setPublico(cPublico.isSelected());
        document.setOculto(cOculto.isSelected());
        document.setComentario(jAreaComments.getText());
        return document;
    }

}
