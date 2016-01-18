/**
 * JPanelReferencia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.utilidades.estructuras.PanelCheckBoxEstructuras;
import com.geopista.protocol.metadatos.MD_Metadata;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-jul-2004
 * Time: 12:53:51
 */
public class JPanelReferencia  extends JPanelPrintable {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelReferencia.class);
    ResourceBundle messages;
    private JFrame framePadre;

    public JPanelReferencia(ResourceBundle messages, JFrame framePadre) {
        super();
        this.framePadre=framePadre;
        this.messages=messages;
        initComponents();
        changeScreenLang(messages);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        jLabelCodigo=new JLabel();
        jPanelCheckBoxEstructuraReferencia = new PanelCheckBoxEstructuras(Estructuras.getListaReferenceSystem(),
                                 com.geopista.app.metadatos.init.Constantes.Locale, 20 /*Estructuras.getListaReferenceSystem().getLista().size()*/,1);
        jPanelCheckBoxEstructuraReferencia.setBackground(Color.WHITE);
        jPanelCheckBoxEstructuraReferencia.setAlignmentY(TOP_ALIGNMENT);
        JScrollPane auxScroll= new JScrollPane();
        auxScroll.setViewportView(jPanelCheckBoxEstructuraReferencia);

        this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), new Color(81,181,103)));
        add(jLabelCodigo, BorderLayout.NORTH);
        add(auxScroll, BorderLayout.CENTER);
    }

    public void changeScreenLang(ResourceBundle messages)
    {
        this.messages=messages;
        jLabelCodigo.setText(messages.getString("JPanelReferencia.jLabelCodigo"));//Formatos:");
    }

    public void load(MD_Metadata metadato)
    {
        if (metadato!=null && metadato.getReference()!=null)
            jPanelCheckBoxEstructuraReferencia.setMarcados(metadato.getReference());
        else
            jPanelCheckBoxEstructuraReferencia.setMarcados(new Vector());

    }
    public Vector getReferencias()
    {
        return jPanelCheckBoxEstructuraReferencia.getMarcados();
    }
    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("CMetadatos.jPanelReferencia"));
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelReferencia.jLabelCodigo"));
        if (jPanelCheckBoxEstructuraReferencia.getStringMarcados()!=null&& jPanelCheckBoxEstructuraReferencia.getStringMarcados().size()>0)
        {
            for (Enumeration e=jPanelCheckBoxEstructuraReferencia.getStringMarcados().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g,pageFormat,pageIndex,(String)e.nextElement());
            }
        }
        else
            printDetalle((Graphics2D)g,pageFormat,pageIndex," ");
        printSuperTitulo((Graphics2D)g,pageFormat,pageIndex," ");
        return Printable.PAGE_EXISTS;
    }

    protected com.geopista.app.utilidades.estructuras.PanelCheckBoxEstructuras jPanelCheckBoxEstructuraReferencia;
    private JLabel jLabelCodigo;

}
