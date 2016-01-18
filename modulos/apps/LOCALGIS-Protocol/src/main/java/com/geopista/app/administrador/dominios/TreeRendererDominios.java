/**
 * TreeRendererDominios.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.dominios;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.administrador.dominios.Category;
import com.geopista.protocol.administrador.dominios.DomainNode;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 08-jun-2004
 * Time: 15:27:01
 */
public class TreeRendererDominios extends DefaultTreeCellRenderer{
        public static final int TIPO_CATEGORIA=-1;
        /*public static final Icon superRootIcon= new javax.swing.ImageIcon("img"+File.separator+"superRootDomain.gif");
        public static final Icon rootIcon= new javax.swing.ImageIcon("img"+File.separator+"RootDomain.jpg");
        public static final Icon stringIcon= new javax.swing.ImageIcon("img"+File.separator+"StringDomain.gif");
        public static final Icon booleanIcon= new javax.swing.ImageIcon("img"+File.separator+"BooleanDomain.gif");
        public static final Icon numberIcon= new javax.swing.ImageIcon("img"+File.separator+"NumberDomain.gif");
        public static final Icon CodeBookIcon= new javax.swing.ImageIcon("img"+File.separator+"CodeBookDomain.gif");
        public static final Icon CodeEntryIcon= new javax.swing.ImageIcon("img"+File.separator+"CodeEntryDomain.gif");
        public static final Icon DateIcon= new javax.swing.ImageIcon("img"+File.separator+"DateDomain.gif");
        public static final Icon TreeIcon= new javax.swing.ImageIcon("img"+File.separator+"TreeDomain.jpg");
        public static final Icon AutoIcon= new javax.swing.ImageIcon("img"+File.separator+"AutoDomain.gif");
        public static final Icon CategoriaIcon= new javax.swing.ImageIcon("img"+File.separator+"categorias.gif");*/
        private static ClassLoader cl =(new TreeRendererDominios()).getClass().getClassLoader();
// Create icons
        public static final Icon superRootIcon= new javax.swing.ImageIcon(cl.getResource("img/superRootDomain.gif"));
        public static final Icon rootIcon= new javax.swing.ImageIcon(cl.getResource("img/RootDomain.jpg"));
        public static final Icon stringIcon= new javax.swing.ImageIcon(cl.getResource("img/StringDomain.gif"));
        public static final Icon booleanIcon= new javax.swing.ImageIcon(cl.getResource("img/BooleanDomain.gif"));
        public static final Icon numberIcon= new javax.swing.ImageIcon(cl.getResource("img/NumberDomain.gif"));
        public static final Icon CodeBookIcon= new javax.swing.ImageIcon(cl.getResource("img/CodeBookDomain.gif"));
        public static final Icon CodeEntryIcon= new javax.swing.ImageIcon(cl.getResource("img/CodeEntryDomain.gif"));
        public static final Icon DateIcon= new javax.swing.ImageIcon(cl.getResource("img/DateDomain.gif"));
        public static final Icon TreeIcon= new javax.swing.ImageIcon(cl.getResource("img/TreeDomain.jpg"));
        public static final Icon AutoIcon= new javax.swing.ImageIcon(cl.getResource("img/AutDomain.gif"));
        public static final Icon CategoriaIcon= new javax.swing.ImageIcon(cl.getResource("img/categorias.gif"));


        public TreeRendererDominios() {}
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
                    case com.geopista.feature.Domain.PATTERN:setIcon(stringIcon);  break;
                    case com.geopista.feature.Domain.TREE:setIcon(TreeIcon); break;
                    case com.geopista.feature.Domain.NUMBER:setIcon(numberIcon); break;
                    case com.geopista.feature.Domain.CODEBOOK:setIcon(CodeBookIcon); break;
                    case com.geopista.feature.Domain.BOOLEAN:setIcon(booleanIcon); break;
                    case com.geopista.feature.Domain.DATE:setIcon(DateIcon);  break;
                    case com.geopista.feature.Domain.CODEDENTRY:setIcon(CodeEntryIcon);  break;
                    case com.geopista.feature.Domain.AUTO:setIcon(AutoIcon);break;
                    case TIPO_CATEGORIA:setIcon(CategoriaIcon);break;
                    default:setIcon(rootIcon);
                }
                String sTitle=getNameNode(value);
                if (sTitle!=null)
                {
                    this.setName(sTitle);
                    this.setText(sTitle);
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
                if (node.getUserObject() instanceof Category) return TIPO_CATEGORIA;
                DomainNode nodeInfo =
                    (DomainNode)(node.getUserObject());
                return nodeInfo.getType();
            }catch(Exception e)
            {
                return 0;
            }
        }
        protected String getNameNode(Object value) {
            try
            {
                DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)value;
                Object nodeInfo = node.getUserObject();
                
                String title = new String();
              
                if (nodeInfo instanceof Category)
                {
                    title = ((Category)nodeInfo).getTerm(Constantes.Locale);
                    if (title==null) title = ((Category)nodeInfo).getFirstTerm();
                }  
                else if (nodeInfo instanceof DomainNode)
                {
                    title = ((DomainNode)nodeInfo).getTerm(Constantes.Locale);
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
