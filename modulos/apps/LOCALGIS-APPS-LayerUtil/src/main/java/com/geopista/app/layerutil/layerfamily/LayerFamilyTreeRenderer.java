/**
 * LayerFamilyTreeRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layerfamily;


/**
 * Define el aspecto del árbol de layerfamilies del sistema
 * 
 * @author cotesa
 *
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.SystemColor;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.util.config.UserPreferenceStore;


public class LayerFamilyTreeRenderer extends JComponent implements TreeCellRenderer 
{
    
    public static final int TIPO_LAYERFAMILY=0;
    public static final int TIPO_LAYER=1;    
     
    //Crea los iconos
    public static final String ICONO_RAIZ= "raizLayerFamilies.gif";
    public static final String ICONO_LAYER_FAMILY= "layerfamily.gif";
    public static final String ICONO_LAYER= "layer.gif";
    
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String locale =UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_DATA_PATH,AppConstants.DEFAULT_LOCALE, false);
    
    private JLabel label = new JLabel();
    private JPanel panel = new JPanel();
    
    
    /**
     * Constructor por defecto
     *
     */
    public LayerFamilyTreeRenderer() 
    {
        setOpaque(false);        
    }
    
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
            boolean hasFocus) 
    {  
        if (row>0)
        {
            switch (getTypeNode(value))
            {
            case TIPO_LAYERFAMILY:
                label.setIcon(IconLoader.icon(ICONO_LAYER_FAMILY));   
                break;
            case TIPO_LAYER:
                label.setIcon(IconLoader.icon(ICONO_LAYER));     
                break;               
            }
            
            String sTitle=getNameNode(value);
            
            if (sTitle!=null)
            {
                label.setName(sTitle);
                label.setText(sTitle);  
            }
            else
            { 
                label.setText ("Error");
            }
            
            if (sel)
            {
                label.setBackground(SystemColor.activeCaption);
                label.setForeground(SystemColor.activeCaptionText);
                label.setOpaque(true);
            }
            else
            {
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                label.setOpaque(true);
            }            
        } 
        else
        {
            label.setIcon(IconLoader.icon(ICONO_RAIZ));
            label.setText(tree.getName());
            label.setBackground(Color.WHITE);
            label.setForeground(Color.BLACK);
            label.setOpaque(true);
            label.setFocusable(false);
            
            return label;
        }
        
        label.setBounds(new Rectangle(1,0,173,20));
        
        panel.setLayout(null);
        panel.setSize(200, 20);
        panel.add(label, null);
        panel.setBackground(Color.WHITE);        
        
        return label;
        
    }
    
    /**
     * Obtiene el tipo de nodo
     * @param value Nodo seleccionado
     * @return Tipo de nodo seleccionado, de acuerdo a los indicados en LayerFamilyTreeRenderer
     */
    protected int getTypeNode(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        try
        {
            if (node.getUserObject() instanceof LayerFamilyTable) 
                return TIPO_LAYERFAMILY;
            
            else  if (node.getUserObject() instanceof LayerTable) 
                return TIPO_LAYER;
            
            return -1;
        }
        catch(Exception e)
        {
            return -1;
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
            
            if (node.getUserObject() instanceof LayerFamilyTable) 
            {
                LayerFamilyTable nodeInfo =   (LayerFamilyTable)(node.getUserObject());
                if(nodeInfo.getHtNombre().get(locale)!=null)
                    title = nodeInfo.getHtNombre().get(locale).toString();
                else if (nodeInfo.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!= null)
                    title = nodeInfo.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
                
                else if (nodeInfo.getHtDescripcion().get(locale)!=null)
                    title = nodeInfo.getHtDescripcion().get(locale).toString();
                else if (nodeInfo.getHtDescripcion().get(AppConstants.DEFAULT_LOCALE)!= null)
                    title = nodeInfo.getHtDescripcion().get(AppConstants.DEFAULT_LOCALE).toString();                 
                
            }            
            else if (node.getUserObject() instanceof LayerTable)
            {
                LayerTable nodeInfo =   (LayerTable)(node.getUserObject());
                
                if(nodeInfo.getHtNombre().get(locale)!=null)
                    title = nodeInfo.getHtNombre().get(locale).toString();
                else if (nodeInfo.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!= null)
                    title = nodeInfo.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
                else                
                    title = nodeInfo.getLayer().getDescription();
            }        
            
            if (title==null) 
                title="           ";
            
            return title;            
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public boolean isSelected()
    {
        return true;
    }    
}
