/**
 * TablesTreeRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.schema.table;


/**
 * Define el aspecto del árbol de tablas y columnas del sistema
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
import com.geopista.feature.Domain;
import com.geopista.feature.Table;
import com.vividsolutions.jump.I18N;




public class TablesTreeRenderer extends DefaultTreeCellRenderer{
    
	// Tabla del sistema:
    public static final int TIPO_TABLA=0;
    public static final int TIPO_COLUMNA=1;
    // Tabla externa:
    public static final int TIPO_TABLA_EXTERNAL=2;
    public static final int TIPO_TITULO=3;
   
    //Crea los iconos
    public static final String ICONO_RAIZ = "raizTablas.gif";
    public static final String ICONO_TITULO = "titulo.gif";
    public static final String ICONO_TABLA = "tablaBD.gif";
    public static final String ICONO_TABLA_EXTERNAL = "tabla.gif";
    public static final String ICONO_COLUMNA= "columna.gif";
    
    
    /**
     * Constructor por defecto
     *
     */
    public TablesTreeRenderer() {}
    
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
            case TIPO_TABLA_EXTERNAL:
                setIcon(IconLoader.icon(ICONO_TABLA_EXTERNAL));   
                esTabla=true;
                break;
            case TIPO_TITULO:
                setIcon(IconLoader.icon(ICONO_TITULO));     
                esTabla=false;
                break;                
            default:                   
                setIcon(IconLoader.icon(ICONO_RAIZ));            
            }
            String sTitle=getNameNode(value);
            
            if (getTypeNode(value) != TIPO_TITULO){
            	if (sTitle!=null){                
            		this.setName(getNameNode(value));
	                if (esTabla){
	                	this.setText(getNameNode(value));
	                }
	                else{
	                	this.setText(getNameNode(value)+"  ->  "+ getNameDomain(value) + getLevelColumn(value));
	                }
	            }
	            else{  
	            	this.setText(((Table)value).getName());
	            }
            }            
        }else {
            setIcon(IconLoader.icon(ICONO_RAIZ));
        }
        
        return this;
    }
    
    /**
     * Obtiene el tipo de nodo
     * @param value Nodo seleccionado
     * @return Tipo de nodo seleccionado, de acuerdo a los indicados en TablesTreeRenderer
     */
    protected int getTypeNode(Object value) {
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)value;
        try
        {
            if (node.getUserObject() instanceof Table){
            	Table tabla = (Table) node.getUserObject();
            	if (tabla.getExternal()==1)
            		return TIPO_TABLA;
            	else
            		return TIPO_TABLA_EXTERNAL;
            }
            else{
            	if (node.getUserObject() instanceof Column){
            		return TIPO_COLUMNA;
            	}
            	else{
            		if (node.getUserObject() instanceof String)
            			return TIPO_TITULO;            			
            	}
            }
                
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
        try{
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            String title = null;
            
            if (node.getUserObject() instanceof Table){
                Table nodeInfo =   (Table)(node.getUserObject());
                title = nodeInfo.getName();
            }          
            else{
            	if (node.getUserObject() instanceof Column){
            		Column nodeInfo =   (Column)(node.getUserObject());
            		title = nodeInfo.getName();
            	}
            	else{
            		if (node.getUserObject() instanceof String){
            			String nodeInfo = (String) (node.getUserObject());
            			title = nodeInfo;
            		}
            	}
            }
            
            if (title==null) title="           ";
            
            return title;
        }catch(Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene el nombre del dominio asociado a la Column que se pasa por parámetro
     * @param value Nodo de una Column del árbol de tablas y columnas
     * @return Nombre del dominio
     */
    protected String getNameDomain(Object value) {
        try
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            Column nodeInfo =   (Column)(node.getUserObject());
            if (nodeInfo.getDomain()!=null){
                String title = nodeInfo.getDomain().getName().toString();
                if (title==null)
                    title=I18N.get("GestorCapas","tablas.dominios.sinnombre");
                return title;
            }else{
                return I18N.get("GestorCapas","tablas.dominios.sindominio");
            }
            
        }catch(Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene el nivel del dominio asociado a la Column que se pasa por parámetro
     * @param value Nodo de una Column
     * @return Nivel del dominio asociado a la Column
     */
    protected String getLevelColumn(Object value) {
        try
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            Column nodeInfo =   (Column)(node.getUserObject());
            if (nodeInfo.getDomain()!=null){
                String level=null;
                
                if (nodeInfo.getDomain().getType()==Domain.TREE)
                    level = String.valueOf(nodeInfo.getLevel());
                if (level==null) return "";
                return " ("+level+")";
            }else{
                return "";
            }
            
        }catch(Exception e)
        {
            return null;
        }
    }
}

