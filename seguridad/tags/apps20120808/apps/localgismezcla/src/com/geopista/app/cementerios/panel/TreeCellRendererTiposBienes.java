package com.geopista.app.cementerios.panel;

import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.app.administrador.dominios.TreeRendererDominios;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.*;
import java.awt.*;


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
