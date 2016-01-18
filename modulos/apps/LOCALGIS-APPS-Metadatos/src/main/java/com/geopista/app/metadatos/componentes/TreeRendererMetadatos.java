/**
 * TreeRendererMetadatos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.componentes;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


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
