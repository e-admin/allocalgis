/**
 * RendererDocumentos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.documents.dialogs.utils;

import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import com.geopista.protocol.document.DocumentBean;
import com.geopista.ui.plugin.edit.LCGIII_DocumentManagerPlugin;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 27-abr-2006
 * Time: 13:04:48
 */

/* clase a la q hay q llamar xa asociar los iconos a los elementos de la lista */
public class RendererDocumentos extends DefaultListCellRenderer
{
    public static final String WORD = "DOC";
    public static final String PPT = "PPT";
    public static final String PDF = "PDF";
    public static final String TXT = "TXT";
    public static final String XML = "XML";
    public static final String HTML = "HTML";

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

            String extension="";
            try
            {
                extension=documento.getFileName().substring(documento.getFileName().lastIndexOf(".")+1);
            }
            catch(Exception e)
            {
                //Excepción no tratada
            }
            if(extension.equalsIgnoreCase(WORD))
                label.setIcon(LCGIII_DocumentManagerPlugin.iconWord);
            else if(extension.equalsIgnoreCase(PPT))
                label.setIcon(LCGIII_DocumentManagerPlugin.iconPPT);
            else if(extension.equalsIgnoreCase(PDF))
                label.setIcon(LCGIII_DocumentManagerPlugin.iconPDF);
            else if(extension.equalsIgnoreCase(TXT))
                label.setIcon(LCGIII_DocumentManagerPlugin.iconTxt);
            else if(extension.equalsIgnoreCase(XML))
                label.setIcon(LCGIII_DocumentManagerPlugin.iconXML);
            else if(extension.equalsIgnoreCase(HTML))
                label.setIcon(LCGIII_DocumentManagerPlugin.iconHTML);
            else
                label.setIcon(LCGIII_DocumentManagerPlugin.iconDefault);

            /** Inventario. Los documentos e imagenes se tratan desde el mismo panel (DocumentPanel) */
            try{
                if (documento.isImagen()){
                    label.setIcon(new ImageIcon(com.geopista.protocol.document.Thumbnail.escalarImagen(documento.getThumbnail(), 20, 20)));
                }
            }catch(Exception e){}

            return label;
    }

}

