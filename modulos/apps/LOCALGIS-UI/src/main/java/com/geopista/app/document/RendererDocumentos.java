/**
 * RendererDocumentos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.document;

import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.geopista.protocol.document.DocumentBean;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

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
            String store = "LocalGIS";
            if(AlfrescoManagerUtils.isAlfrescoUuid(documento.getId(), documento.getIdMunicipio())){
            	 store = "Alfresco";
            }
            sTitulo += " ( " + store + " )";
            JLabel label =(JLabel) super.getListCellRendererComponent(l,sTitulo,i, s, f);
           
            label.setIcon(documento.getIcon());
            return label;
    }

}

