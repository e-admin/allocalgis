/**
 * JPanelDistribucion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.paneles;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;

import com.geopista.app.metadatos.componentes.ListOptativo;
import com.geopista.protocol.metadatos.CI_OnLineResource;
import com.geopista.protocol.metadatos.MD_Format;
import com.geopista.protocol.metadatos.MD_Metadata;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-jul-2004
 * Time: 12:53:51
 */
public class JPanelDistribucion  extends JPanelPrintable {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelDistribucion.class);
    ResourceBundle messages;
    private JFrame framePadre;

    public JPanelDistribucion(ResourceBundle messages, JFrame framePadre) {
        super();
        this.framePadre=framePadre;
        this.messages=messages;
        initComponents();
        changeScreenLang(messages);
    }

    private void initComponents() {
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jLabelFormato=new JLabel();
        jListFormato = new ListOptativo(messages,new java.awt.event.ActionListener() {
                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                     mostrarFormato();
                 }
        });
        jLabelRecursosEnLinea=new JLabel();
        jListRecursosEnLinea = new ListOptativo(messages,new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        mostrarRecursos();
                    }
        });
       // Añadimos el doble click
        jListRecursosEnLinea.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
                    JList list = (JList)evt.getSource();
                    if (evt.getClickCount() == 2) {          // Double-click
                        // Get item index
                       dobleClickRecursos(list.locationToIndex(evt.getPoint()));
                    }
                }
            });
        // Añadimos el doble click
        jListFormato.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
                    JList list = (JList)evt.getSource();
                    if (evt.getClickCount() == 2) {          // Double-click
                        // Get item index
                       dobleClickFormato(list.locationToIndex(evt.getPoint()));
                    }
                }
            });


       this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), new Color(81,181,103)));
       add(jLabelFormato, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
       add(jListFormato, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 520, 120));
       add(jLabelRecursosEnLinea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));
       add(jListRecursosEnLinea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 520, 120));

    }

    public void changeScreenLang(ResourceBundle messages)
    {
        this.messages=messages;
        jLabelFormato.setText(messages.getString("JPanelDistribucion.jLabelFormato"));//Formatos:");
        jLabelRecursosEnLinea.setText(messages.getString("JPanelDistribucion.jLabelRecursosEnLinea"));//"Recursos en linea:");
    }
     private void mostrarRecursos() {
            // Aqui es donde tengo que mostrar la información de la cita
            com.geopista.app.metadatos.distribucion.ShowRecursosEnLinea recursosDialog = new com.geopista.app.metadatos.distribucion.ShowRecursosEnLinea(framePadre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            recursosDialog.setLocation(d.width/2 - 400/2, d.height/2 - 200/2);
            recursosDialog.setSize(400,200);
            recursosDialog.setResizable(false);
            recursosDialog.show();
             if (recursosDialog.getRecurso()!=null)
               jListRecursosEnLinea.addElement(recursosDialog.getRecurso());
            recursosDialog=null;
     }
    private void mostrarFormato() {
            // Aqui es donde tengo que mostrar la información de la cita
            com.geopista.app.metadatos.distribucion.ShowFormatoDistribucion formatoDialog = new com.geopista.app.metadatos.distribucion.ShowFormatoDistribucion(framePadre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            formatoDialog.setLocation(d.width/2 - 400/2, d.height/2 - 200/2);
            formatoDialog.setSize(400,200);
            formatoDialog.setResizable(false);
            formatoDialog.show();
            if (formatoDialog.getFormato()!=null)
               jListFormato.addElement(formatoDialog.getFormato());
            formatoDialog=null;
     }
      private void dobleClickRecursos(int index) {
            if (index<0) mostrarRecursos();
            // Aqui es donde tengo que mostrar la información de la cita
            com.geopista.app.metadatos.distribucion.ShowRecursosEnLinea recursosDialog = new com.geopista.app.metadatos.distribucion.ShowRecursosEnLinea(framePadre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            recursosDialog.setLocation(d.width/2 - 400/2, d.height/2 - 200/2);
            recursosDialog.setSize(400,200);
            recursosDialog.setResizable(false);
            recursosDialog.load((CI_OnLineResource)jListRecursosEnLinea.getElement());
            recursosDialog.show();
             if (recursosDialog.getRecurso()!=null)
               jListRecursosEnLinea.replaceElement(recursosDialog.getRecurso());
            recursosDialog=null;
     }

    private void dobleClickFormato(int index) {
            if (index<0) mostrarFormato();
               // Aqui es donde tengo que mostrar la información de la cita
            com.geopista.app.metadatos.distribucion.ShowFormatoDistribucion formatoDialog = new com.geopista.app.metadatos.distribucion.ShowFormatoDistribucion(framePadre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            formatoDialog.setLocation(d.width/2 - 400/2, d.height/2 - 200/2);
            formatoDialog.setSize(400,200);
            formatoDialog.setResizable(false);
            formatoDialog.load((MD_Format)jListFormato.getElement());
            formatoDialog.show();
            if (formatoDialog.getFormato()!=null)
            jListRecursosEnLinea.replaceElement(formatoDialog.getFormato());
            formatoDialog=null;
        }

    public void load(MD_Metadata metadato)
    {
        if (metadato!=null && metadato.getFormatos()!=null)
            jListFormato.setModel(metadato.getFormatos());
        else
            jListFormato.setModel(new Vector());

        if (metadato!=null && metadato.getOnlineresources()!=null)
            jListRecursosEnLinea.setModel(metadato.getOnlineresources());
        else
            jListRecursosEnLinea.setModel(new Vector());


    }
    public Vector getFormatos()
    {
        return jListFormato.getVectorModel();
    }
    public Vector getRecursos()
    {
        return jListRecursosEnLinea.getVectorModel();
    }

    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("CMetadatos.jPanelDistribucion"));
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelDistribucion.jLabelFormato"));
        if (jListFormato.getVectorModel()!=null&& jListFormato.getVectorModel().size()>0)
        {
            for (Enumeration e=jListFormato.getVectorModel().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g,pageFormat,pageIndex,((MD_Format)e.nextElement()).getName());
            }
        }
        else
            printDetalle((Graphics2D)g,pageFormat,pageIndex," ");
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelDistribucion.jLabelRecursosEnLinea"));
        if (jListRecursosEnLinea.getVectorModel()!=null&& jListRecursosEnLinea.getVectorModel().size()>0)
        {
            for (Enumeration e=jListRecursosEnLinea.getVectorModel().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g,pageFormat,pageIndex,((CI_OnLineResource)e.nextElement()).getLinkage());
            }
        }
        else
            printDetalle((Graphics2D)g,pageFormat,pageIndex," ");
        return Printable.PAGE_EXISTS;
    }

    protected ListOptativo jListFormato;
    protected ListOptativo jListRecursosEnLinea;
    private JLabel jLabelFormato;
    private JLabel jLabelRecursosEnLinea;



}