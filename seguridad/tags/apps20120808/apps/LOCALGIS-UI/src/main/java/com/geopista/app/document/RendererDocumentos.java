package com.geopista.app.document;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.text.SimpleDateFormat;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 27-abr-2006
 * Time: 13:04:48
 */

/* clase a la q hay q llamar xa asociar los iconos a los elementos de la lista */
public class RendererDocumentos extends DefaultListCellRenderer
{
      /*constructor de la clase */
    public RendererDocumentos()
    {
        //va vacio
    }

    /* renderer xa mostrar iconos e info. adicional a los documentos de la lista */
    public Component getListCellRendererComponent(JList l,
           Object value, int i, boolean s, boolean f)
    {
            DocumentBean documento = (DocumentBean)value;
            String sTitulo=(documento.getFileName()+" ("+documento.getSize()+") - "+
                    (documento.getFechaEntradaSistema()!=null?
                       new SimpleDateFormat("dd-MM-yyyy").format(documento.getFechaEntradaSistema()):""));
            JLabel label =(JLabel) super.getListCellRendererComponent(l,sTitulo,i, s, f);

            label.setIcon(documento.getIcon());
            return label;
    }

}

