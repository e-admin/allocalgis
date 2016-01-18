/**
 * JPanelGraficos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.paneles;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFileChooser;

import com.geopista.app.metadatos.componentes.ListOptativo;
import com.geopista.app.utilidades.GeoPistaFileFilter;
import com.geopista.app.utilidades.TextField;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-jul-2004
 * Time: 12:37:51
 */
public class JPanelGraficos extends JPanelPrintable {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelGraficos.class);
    ResourceBundle messages;

    public JPanelGraficos(ResourceBundle messages) {
        super();
        this.messages=messages;
        initComponents();
        changeScreenLang(messages);
    }
    private void anadir()
    {
        if (jTextFieldFichero.getText().length()>0)
        {
            jListFicheros.addElement(jTextFieldFichero.getText());
        }
    }
    private void borrar()
    {
        jListFicheros.removeElement();
    }
    private void buscar()
    {
        JFileChooser chooser = new JFileChooser();
        GeoPistaFileFilter filter2 = new GeoPistaFileFilter();
        filter2.addExtension("GIF");
        filter2.setDescription("GIF");
        chooser.addChoosableFileFilter(filter2);
        com.geopista.app.utilidades.GeoPistaFileFilter filter3 = new GeoPistaFileFilter();
        filter3.addExtension("JPG");
        filter3.setDescription("JPG");
        chooser.addChoosableFileFilter(filter3);
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            logger.debug("Fichero seleccionado " + selectedFile.getName());
            if (selectedFile != null) {
                jTextFieldFichero.setText(selectedFile.getPath());
            }
        }

    }
    public void load(Vector vGraficos)
    {
       jListFicheros.setModel(vGraficos);
    }
    public Vector getGraficos()
    {
       return jListFicheros.getVectorModel();
    }
    private void initComponents() {
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPaneFicheros = new javax.swing.JScrollPane();
        jTextFieldFichero = new TextField(255);
        jListFicheros=new ListOptativo(messages,messages.getString("JPanelGraficos.nombrefichero"));
        jButtonAnadir= new javax.swing.JButton();
        jButtonBorrar= new javax.swing.JButton();
        jButtonBuscar= new javax.swing.JButton();


        add(jTextFieldFichero, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, -1));
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buscar();
                    }
        });
        jButtonBuscar.setText("...");
        add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 20, 20));
        jButtonAnadir.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        anadir();
                    }
        });
        add(jButtonAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 90,-1));
        jScrollPaneFicheros.setViewportView(jListFicheros);
        add(jScrollPaneFicheros, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 260, 130));
        jButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        borrar();
                    }
        });
        add(jButtonBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 90, -1));

    }

    public void changeScreenLang(ResourceBundle messages)
    {
        this.messages=messages;
        jButtonAnadir.setText(messages.getString("JPanelGraficos.jButtonAnadir"));
        jButtonBorrar.setText(messages.getString("JPanelGraficos.jButtonBorrar"));
    }

    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jPanelGraficos"));
        printTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelGraficos.nombrefichero"));
        if (jListFicheros!=null&&jListFicheros.getVectorModel()!=null&&jListFicheros.getVectorModel().size()>0)
        {
            for (Enumeration e=jListFicheros.getVectorModel().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g, pageFormat,pageIndex,(String)e.nextElement());
            }
        }
        else
            printDetalle((Graphics2D)g, pageFormat,pageIndex,"");
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,"");
        return Printable.PAGE_EXISTS;
    }

    private javax.swing.JScrollPane jScrollPaneFicheros;
    private TextField jTextFieldFichero;
    private ListOptativo jListFicheros;
    private javax.swing.JButton jButtonAnadir;
    private javax.swing.JButton jButtonBorrar;
    private javax.swing.JButton jButtonBuscar;




}