package com.geopista.app.metadatos.paneles;

import com.geopista.app.utilidades.estructuras.PanelCheckBoxEstructuras;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.protocol.metadatos.MD_Metadata;
import javax.swing.*;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.Printable;


/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


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
