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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.dbtable;


/**
 * Define el aspecto del árbol de tablas y columnas de Base de Datos
 * 
 * @author cotesa
 *
 */


import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.feature.Column;
import com.geopista.feature.Table;



public class TablesDBTreeRenderer extends DefaultTreeCellRenderer{
    
    public static final int TIPO_TABLA=0;
    public static final int TIPO_COLUMNA=1;
    
   
    //Crea los iconos
    public static final String ICONO_RAIZ = "raizTablasBD.gif";
    public static final String ICONO_TABLA = "tablaBD.gif";
    public static final String ICONO_COLUMNA = "columnaBD.gif";
    
    /**
     * Constructor por defecto
     *
     */
    public TablesDBTreeRenderer() {}
    
    /**
     * Obtiene el componente a pintar
     * 
     * @param tree El árbol en el que está el componente
     * @param value LayerFamily a pintar
     * @param sel Verdadero si el componente está seleccionado
     * @param expanded Verdadero si el componente se tiene que mostrar expandido
     * @param leaf Verdadero si el componente es una hoja
     * @param row Índice de la entrada
     * @param hasFocus Verdadero si el componente tiene el foco
     */
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
        
        boolean esTabla = false;
        
        if (row>0)
        {
            switch (getTypeNode(value))
            {
            case TIPO_TABLA:
                setIcon(IconLoader.icon(ICONO_TABLA));   
                esTabla=true;
                break;
            case TIPO_COLUMNA:
                setIcon(IconLoader.icon(ICONO_COLUMNA));     
                esTabla=false;
                break;
            default:                   
                setIcon(IconLoader.icon(ICONO_RAIZ));
            
            }
            String sTitle=getNameNode(value);
            if (sTitle!=null)
            {
                
                this.setName(getNameNode(value));
                if (esTabla)
                {
                    this.setText(getNameNode(value));
                }
                
            }
            else
            {   
                this.setText(((Table)value).getName());
            }
        } else {
            setIcon(IconLoader.icon(ICONO_RAIZ));
        }
        
        
        return this;
    }
    
    /**
     * Obtiene el tipo de nodo
     * @param value Nodo seleccionado
     * @return Tipo de nodo seleccionado, de acuerdo a los indicados en TablesDBTreeRenderer
     */
    protected int getTypeNode(Object value) {
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)value;
        try
        {
            if (node.getUserObject() instanceof Table) 
                return TIPO_TABLA;
            else  if (node.getUserObject() instanceof Column) 
                return TIPO_COLUMNA;
            //Columns nodeInfo = (Columns)(node.getUserObject());
            return TIPO_TABLA;
        }catch(Exception e)
        {
            return 0;
        }
    }
    
    /**
     * Obtiene el nombre a mostrar en el nodo
     * @param value Nodo 
     * @return Nombre a mostrar
     */
    protected String getNameNode(Object value) {
        try
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            String title = null;
            
            if (node.getUserObject() instanceof Table) 
            {
                Table nodeInfo =   (Table)(node.getUserObject());
                title = nodeInfo.getDescription();
            }
            /*
             else if (node.getUserObject() instanceof Column)
             {
             Column nodeInfo =   (Column)(node.getUserObject());
             title = nodeInfo.getName();
             }               
             */
            if (title==null) title="           ";
            
            return title;
        }catch(Exception e)
        {
            return null;
        }
    }    
    
}

