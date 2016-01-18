package com.geopista.app.metadatos;


import javax.swing.*;
import org.apache.log4j.Logger;
import java.util.ResourceBundle;
import java.io.File;
import java.awt.*;

import com.geopista.app.metadatos.paneles.*;
import com.geopista.app.metadatos.init.Constantes;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.protocol.metadatos.MD_Metadata;

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
 * Date: 26-ago-2004
 * Time: 12:17:22
 */

public class CMetadatos extends javax.swing.JInternalFrame {
	private Logger logger = Logger.getLogger(CMetadatos.class);
    private ResourceBundle messages;
    private JFrame framePadre;
    private File selectedFile;

	/**
	 * Creates new form CCreacionLicencias
	 */
	public CMetadatos(ResourceBundle messages, JFrame frame)
    {
        this.framePadre=frame;
        this.messages=messages;
		initComponents();
		changeScreenLang(messages);
	}


    private void initComponents() {
        logger.debug("Inicializando componentes CMetadatos");
        templateJScrollPane = new javax.swing.JScrollPane();
        templateJPanel = new JPanelShowMetadato(messages,framePadre);
        templateJScrollPane.setViewportView(templateJPanel);
        logger.debug("Inicializada la pantalla de principal");
        jPanelBotonera = new javax.swing.JPanel();
        jButtonSalvar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();

        jButtonSalir = new javax.swing.JButton();
        jButtonValidar = new javax.swing.JButton();
        jButtonSave2File = new javax.swing.JButton();
        jButtonLoadFile = new javax.swing.JButton();
        jButtonPrint = new javax.swing.JButton();

        jPanelBotonera.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jPanelBotonera.setMinimumSize(new java.awt.Dimension(67, 50));
        jPanelBotonera.setPreferredSize(new java.awt.Dimension(87, 50));

        jButtonSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    salvar();
                                }
        });
        jButtonEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    delete();
                                }
        });
        jPanelBotonera.add(jButtonSalvar);
        jPanelBotonera.add(jButtonEliminar);

        jButtonSave2File.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
               save2File();
           }
       });

        jPanelBotonera.add(jButtonSave2File);

        jButtonLoadFile.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
               loadFile();
           }
       });

        jPanelBotonera.add(jButtonLoadFile);


        jButtonValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validarAccion();
            }
        });

        jPanelBotonera.add(jButtonPrint);
        jButtonPrint.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                        print();
                    }
                });

        jPanelBotonera.add(jButtonValidar);

        jButtonSalir.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                        salir();
                    }
                });

        jPanelBotonera.add(jButtonSalir);

        getContentPane().add(templateJScrollPane, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanelBotonera, java.awt.BorderLayout.SOUTH);
        logger.debug("Fin inicializar componentes");

    }
    private void validarAccion()
    {
        templateJPanel.validarAccion();
    }
     private void salir() {
    		this.dispose();
    }
    private void salvar()
    {
       if (!isAutenticado()) return;
       if (CMainMetadatos.aclMetadatos!=null && !CMainMetadatos.aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)))
                  return;

       templateJPanel.save();
    }
    public boolean delete()
    {
       if (!isAutenticado()) return false;
       if (CMainMetadatos.aclMetadatos!=null && !CMainMetadatos.aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)))
           return false;
       if(templateJPanel.delete())
       {
           JOptionPane optionPane= new JOptionPane(messages.getString("CMetadatos.mensaje.metadatoborrado"),JOptionPane.INFORMATION_MESSAGE);
           JDialog dialog =optionPane.createDialog(this,"");
           dialog.show();
           dispose();
       }
       return false;
    }
    public boolean  delete(String sIdMetadato)
    {
       if (!isAutenticado())  return false;
       return templateJPanel.delete(sIdMetadato);
    }
    public boolean delete(MD_Metadata metadato)
    {
       return templateJPanel.delete(metadato);
    }

    public boolean print()
    {
        templateJPanel.printPanel();
        return true;
    }

    public void save2File()
    {
        if (!templateJPanel.validar()) return;

        JFileChooser chooser = new JFileChooser();
        if (selectedFile==null)
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
        else
            chooser.setSelectedFile(selectedFile);
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
             selectedFile = chooser.getSelectedFile();
             logger.debug("Fichero seleccionado para salvar" + selectedFile.getName());
             if (selectedFile != null) templateJPanel.save2File(selectedFile);
        }
    }

     public void exportar()
     {
         if (!templateJPanel.validar()) return;
         JFileChooser chooser = new JFileChooser();
         if (selectedFile==null)
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
         else
            chooser.setSelectedFile(selectedFile);
         if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
         {
             selectedFile = chooser.getSelectedFile();
             logger.debug("Fichero seleccionado para exportar" + selectedFile.getName());
             if (selectedFile != null) templateJPanel.exportar(selectedFile);
         }
     }

     public boolean loadFile()
     {

         JFileChooser chooser = new JFileChooser();
         if (selectedFile==null)
                chooser.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
            else
                chooser.setSelectedFile(selectedFile);

         if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
         {
              selectedFile = chooser.getSelectedFile();
              logger.debug("Fichero seleccionado para salvar" + selectedFile.getName());
              if (selectedFile != null)
              {
                  return templateJPanel.loadFile(selectedFile);
              }
         }
         return false;
     }
     public boolean load(String sFileIdentifier)
     {
         return templateJPanel.load(sFileIdentifier);
     }
     public boolean importar()
    {

        JFileChooser chooser = new JFileChooser();
        if (selectedFile==null)
               chooser.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
           else
               chooser.setSelectedFile(selectedFile);

        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
             selectedFile = chooser.getSelectedFile();
             logger.debug("Fichero seleccionado para importar" + selectedFile.getName());
             if (selectedFile != null)
             {
                 return templateJPanel.importar(selectedFile);
             }
        }
        return false;
    }

     private boolean isAutenticado()
     {
            if (!com.geopista.security.SecurityManager.isLogged())
           {
                CAuthDialog auth = new CAuthDialog(framePadre, true,Constantes.url,
                                        CMainMetadatos.idApp,Constantes.idMunicipio,
                                        messages,true);
                auth.setSize(400,200);
                auth.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                auth.setLocation(d.width/2 - auth.getSize().width/2, d.height/2 - auth.getSize().height/2);
                auth.setResizable(false);
                auth.show();
                if (!com.geopista.security.SecurityManager.isLogged())
                {  return false;}
                else
                {
                    try
                    {
                        CMainMetadatos.aclMetadatos= com.geopista.security.SecurityManager.getPerfil("Metadatos");
                        boolean permitido=CMainMetadatos.aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS));
                        enabled(permitido);
                        if (!permitido)
                        {
                                JOptionPane optionPane= new JOptionPane(messages.getString("CMainMetadatos.mensaje.nopermisos"),JOptionPane.INFORMATION_MESSAGE);
                                JDialog dialog =optionPane.createDialog(this,"");
                                dialog.show();
                        }
                        return permitido;
                    }catch(Exception e)
                    {
                        logger.error("Error al obtener el acl:"+e.toString());
                        return false;
                    }
                }
           }
           return true;
     }

   public void showArbol(boolean bMostrar)
   {
       if  (templateJPanel==null) return;
       if (bMostrar)
         templateJPanel.showArbol();
       else
         templateJPanel.hideArbol();
   }

     public void enabled (boolean b)
     {
         jButtonSalvar.setEnabled(b);
         jButtonEliminar.setEnabled(b);
     }

    public void changeScreenLang(ResourceBundle messages)
    {
        setTitle(messages.getString("CMetadatos.title"));
        jButtonSalvar.setText(messages.getString("CMetadatos.jButtonSalvar"));
        jButtonEliminar.setText(messages.getString("CMetadatos.jButtonEliminar"));
        jButtonSalir.setText(messages.getString("CMetadatos.jButtonSalir"));
        jButtonValidar.setText(messages.getString("CMetadatos.jButtonValidar"));
        jButtonSave2File.setText(messages.getString("CMetadatos.jButtonSave2File"));
        jButtonLoadFile.setText(messages.getString("CMetadatos.jButtonLoadFile"));
        jButtonPrint.setText(messages.getString("CMetadatos.jButtonPrint"));
        if (templateJPanel!=null) templateJPanel.changeScreenLang(messages);
    }


    private JPanelShowMetadato templateJPanel;
    private javax.swing.JScrollPane templateJScrollPane;
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonValidar;
    private javax.swing.JButton jButtonSave2File;
    private javax.swing.JButton jButtonLoadFile;
    private javax.swing.JButton jButtonPrint;

}
