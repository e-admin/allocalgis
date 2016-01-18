/**
 * TreeCellRendererTiposBienes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.geopista.app.administrador.dominios.TreeRendererDominios;
import com.geopista.protocol.administrador.dominios.DomainNode;


/**
 * Creado por SATEC.
 * User: angeles
 * Date: 06-sep-2006
 * Time: 15:17:05
 */

public class TreeCellRendererTiposBienes extends DefaultTreeCellRenderer{
        private static ClassLoader cl =(new TreeRendererDominios()).getClass().getClassLoader();
        public static final Icon rootIcon= new javax.swing.ImageIcon(cl.getResource("com/geopista/ui/images/rootbienes.gif"));
        public static final Icon bienesIcon= new javax.swing.ImageIcon(cl.getResource("com/geopista/ui/images/bienes.gif"));
        public static final Icon subBienesIcon= new javax.swing.ImageIcon(cl.getResource("com/geopista/ui/images/subbienes.gif"));

        String locale;
        public TreeCellRendererTiposBienes(String locale)
        {
            this.locale=locale;
        }
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
            if (row==0)
            {    setIcon(rootIcon);}
           // else if (row==1)
           // {    setIcon(bienesIcon);}
            else
            {    setIcon(subBienesIcon);}
            String sTitle=getNameNode(value);
            if (sTitle!=null)
            {
                    this.setName(sTitle);
                    this.setText(sTitle);
            }

            return this;
        }
        protected String getNameNode(Object value) {
            try
            {
                DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)value;
                Object nodeInfo = node.getUserObject();

                String title = new String();
                 if (nodeInfo instanceof DomainNode)
                {
                    title = ((DomainNode)nodeInfo).getTerm(locale);
                    if (title==null) title = ((DomainNode)nodeInfo).getFirstTerm();
                }
                else
                    return null;

                if (title==null)
                    title="           ";
                return title;
            }catch(Exception e)
            {
                return null;
            }
        }
    }
