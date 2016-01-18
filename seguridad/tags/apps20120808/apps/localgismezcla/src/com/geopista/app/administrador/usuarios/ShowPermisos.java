package com.geopista.app.administrador.usuarios;

import org.apache.log4j.Logger;

import java.util.ResourceBundle;

import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.ListaPermisos;
import com.geopista.protocol.administrador.ListaAcl;

import javax.swing.*;
import javax.swing.table.TableColumn;

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
 * Date: 14-mar-2005
 * Time: 19:15:03
 */
public class ShowPermisos extends javax.swing.JDialog  {
        Logger logger = Logger.getLogger(ShowConexiones.class);
        ResourceBundle messages;
        PermisosTableModel modelPermisos;
        ListaPermisos listaPermisosUsuarioRol=null;
        ListaAcl listaAcl=null;
        private TableSorted sorter;


        /**
         * Creates new form JSearch
         */
        public ShowPermisos(String sIdUsuario, ListaAcl listaAcl, java.awt.Frame parent, boolean modal,
                            ResourceBundle messages) {
            super(parent, modal);
            this.messages=messages;
            this.listaAcl=listaAcl;
            initListas(sIdUsuario);
            initComponents();
            changeScreenLang(messages);

        }

        /**
         * Cuando se muestran los permisos de un rol
         */
        public ShowPermisos(ListaPermisos listaPermisos, ListaAcl listaAcl, java.awt.Frame parent, boolean modal,
                            ResourceBundle messages) {
            super(parent, modal);
            this.messages=messages;
            this.listaAcl=listaAcl;
            this.listaPermisosUsuarioRol=listaPermisos;
            initComponents();
            changeScreenLang(messages);

        }

        private void initComponents() {//GEN-BEGIN:initComponents
            jButtonSalir = new JButton();
            jPanelPermisos = new JPanel();
            jPanelBotonera = new JPanel();
            jScrollPaneTablaPermisos = new javax.swing.JScrollPane();
            jTablePermisos = new javax.swing.JTable();
            actualizarModelo();
            jScrollPaneTablaPermisos.setViewportView(jTablePermisos);

            jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    salir();
                }
            });
             jPanelPermisos.setLayout(new java.awt.BorderLayout());
             jPanelPermisos.add(jScrollPaneTablaPermisos,java.awt.BorderLayout.CENTER);
             jPanelBotonera.add(jButtonSalir);

             getContentPane().setLayout(new java.awt.BorderLayout());
             getContentPane().add(jPanelPermisos, java.awt.BorderLayout.CENTER);
             getContentPane().add(jPanelBotonera, java.awt.BorderLayout.SOUTH);



            pack();
        }
        private void initListas(String sIdUsuario)
        {

            try
            {
                 listaPermisosUsuarioRol= (new OperacionesAdministrador(Constantes.url)).getPermisosUsuario(sIdUsuario);
                 if (listaPermisosUsuarioRol==null)
                     logger.warn("El usuario "+sIdUsuario +" no tiene ningun permiso");
                 else
                     logger.info("Numero de permisos para el usuario "+sIdUsuario+ ": "+listaPermisosUsuarioRol.gethPermisos().size());

            }catch(Exception e)
             {
                   logger.error("Error al inicializa la lista de dominios: "+e.toString());
                   JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
                   JDialog dialog =optionPane.createDialog(this,"ERROR al mostrar los permisos");
                   dialog.show();
             }
        }

        private void salir() {
            dispose();
        }

         public void changeScreenLang(ResourceBundle messages) {
             setTitle(messages.getString("CUsuariosFrame.jLabelPermisos"));
             jPanelPermisos.setToolTipText(messages.getString("CUsuariosFrame.jLabelPermisos"));
             jButtonSalir.setText(messages.getString("ShowConexiones.jButtonSalir"));
             jButtonSalir.setToolTipText(messages.getString("ShowConexiones.jButtonSalir"));

             TableColumn tableColumn= jTablePermisos.getColumnModel().getColumn(0);
             tableColumn.setHeaderValue(messages.getString("ShowPermisos.col0"));
             tableColumn= jTablePermisos.getColumnModel().getColumn(1);
             tableColumn.setHeaderValue(messages.getString("ShowPermisos.col1"));
         }

         private void actualizarModelo()
        {
            modelPermisos= new PermisosTableModel();
            modelPermisos.setModelData(listaPermisosUsuarioRol,listaAcl);
            sorter = new TableSorted(modelPermisos);
            sorter.setTableHeader(jTablePermisos.getTableHeader());
            jTablePermisos.setModel(sorter);
            jTablePermisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            //Ordenamos por nombre del ACL que es la columna 0
            sorter.setSortingStatus(0, 1);
        }

        private javax.swing.JButton jButtonSalir;
        private javax.swing.JPanel jPanelBotonera;
        private javax.swing.JPanel jPanelPermisos;
        private javax.swing.JScrollPane jScrollPaneTablaPermisos;
        private javax.swing.JTable jTablePermisos;
    }

