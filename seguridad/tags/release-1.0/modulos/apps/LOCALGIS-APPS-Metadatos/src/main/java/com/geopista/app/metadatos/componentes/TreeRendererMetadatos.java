package com.geopista.app.metadatos.componentes;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.*;
import java.io.File;
import java.awt.*;

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
 * Date: 02-sep-2004
 * Time: 17:49:59
 */
public class TreeRendererMetadatos extends DefaultTreeCellRenderer{
        public static final int OBLIGATORIO=1;
        public static final int CONDICIONAL=2;
        public static final int OPTATIVO=3;
        private static ClassLoader cl =(new TreeRendererMetadatos()).getClass().getClassLoader();
// Create icons
        public static final Icon superRootIcon= new javax.swing.ImageIcon(cl.getResource("img/superRootMetadata.gif"));
        public static final Icon obligatorioIcon= new javax.swing.ImageIcon(cl.getResource("img/obligatorio.GIF"));
        public static final Icon condicionalIcon= new javax.swing.ImageIcon(cl.getResource("img/condicional.GIF"));
        public static final Icon optativoIcon= new javax.swing.ImageIcon(cl.getResource("img/optativo.GIF"));




        public TreeRendererMetadatos() {}

        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);

           //System.out.println("La fila es: "+row+" value:"+ value);
            if (row>0)
            {
                switch (getTypeNode(value))
                {
                    case OBLIGATORIO:setIcon(obligatorioIcon);  break;
                    case CONDICIONAL:setIcon(condicionalIcon); break;
                    case OPTATIVO:setIcon(optativoIcon); break;
                    default:setIcon(superRootIcon);
                }
                String sTitle=getNameNode(value);
                if (sTitle!=null)
                {
                    this.setName(getNameNode(value));
                    this.setText(getNameNode(value));
                }
            } else {
                 setIcon(superRootIcon);
            }

            return this;
        }

        protected int getTypeNode(Object value) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)value;
            try
            {
                ArbolEntry entrada =
                    (ArbolEntry)(node.getUserObject());
                return entrada.getTipo();
            }catch(Exception e)
            {
                return 0;
            }
        }
        protected String getNameNode(Object value) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)value;
            try
            {
                ArbolEntry entrada =
                    (ArbolEntry)(node.getUserObject());
                return entrada.getNombre();
            }catch(Exception e)
            {
                return "";
            }
        }

    }
