/**
 * DatosAlfanumericosPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.edicionalfanum;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.document.DocumentInterface;
import com.geopista.protocol.document.DocumentBean;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 26-may-2006
 * Time: 10:15:21
 * To change this template use File | Settings | File Templates.
 */

public class DatosAlfanumericosPanel extends JScrollPane
{
    private static final Log logger = LogFactory.getLog(DatosAlfanumericosPanel.class);

    private AppContext aplicacion;

    private JTextArea jTextAreaDocumento = new JTextArea();

    DocumentInterface docInt;

    public boolean ubicacionBD = false;

    public DatosAlfanumericosPanel()
    {
        aplicacion = (AppContext)AppContext.getApplicationContext();
        setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("text.infotext.contenido")));

        setLayout(new ScrollPaneLayout());
        setPreferredSize(new Dimension(250, 250));

        jTextAreaDocumento.setLineWrap(true);
        setViewportView(jTextAreaDocumento);
        setFocusable(true);
    }

    
    /* mostramos el texto BD en el TextArea */
    public void loadTextBD(DocumentBean documento)
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(documento.getContent());
            InputStreamReader isr = new InputStreamReader(bais, "ISO-8859-1");//""UTF8");
            Reader in = new BufferedReader(isr);
            int ch;
            while((ch = in.read()) > -1)
            {
                buffer.append((char)ch);
            }
            in.close();
        }
        catch(Exception e)
        {
            logger.error("Error al cargar el archivo a mostrar ", e);
        }
        jTextAreaDocumento.setText(buffer.toString());
    }

    /* salvamos el texto */
    public DocumentBean save(DocumentBean document)
    {
        if(document == null) return null;
        String documento = jTextAreaDocumento.getText();
        byte[] b = documento.getBytes();
        document.setContent(b);
        return document;
    }

    /* cargamos el texto */
    public void load(DocumentBean documento)
    {
        loadTextBD(documento);
    }

    public JTextArea getjTextAreaDocumento()
    {
        return jTextAreaDocumento;
    }

    /* decimos si el TextArea es o no editable*/
    public void setEnabled(boolean estado)
    {
       jTextAreaDocumento.setEnabled(estado);
    }
}
