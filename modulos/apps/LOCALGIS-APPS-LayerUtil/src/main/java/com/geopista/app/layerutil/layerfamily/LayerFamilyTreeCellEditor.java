/**
 * LayerFamilyTreeCellEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layerfamily;

/**
 * Editor para los componentes de tipo LayerFamily
 * 
 * @author cotesa
 *
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.GestorCapas;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.app.layerutil.util.JDialogTranslations;
import com.geopista.util.config.UserPreferenceStore;

public class LayerFamilyTreeCellEditor extends AbstractCellEditor implements TreeCellEditor, ActionListener 
{    
    
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    private static final String ICONO_BANDERA= "banderas.gif";
    private static final String ICONO_LAYER= "layer.gif";
    private static final String ICONO_RAIZ= "raizLayerFamilies.gif";
    
    
    LayerFamilyTable currentLayerFamily = new LayerFamilyTable();
    Hashtable traducciones = new Hashtable();
    JButton button;
    JDialogTranslations jDiccionario;
    JPanel panelFamily = new JPanel();
    JPanel panelLayer = new JPanel();
    JLabel labelFamily = new JLabel();
    JLabel labelLayer = new JLabel();
    
    protected static final String EDIT = "edit";
    
    /**
     * Constructor por defecto
     */
    public LayerFamilyTreeCellEditor() {
        
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setSize(25, 20);
        button.setBounds(new Rectangle (1, 3, 20, 20));
        labelFamily.setSize(50, 20);
        labelFamily.setBounds(new Rectangle (30, 0, 173, 20));
        labelFamily.setText("");    
        labelLayer.setSize(50, 20);
        labelLayer.setBounds(new Rectangle (0, 1, 173, 20));
        labelLayer.setText("                                ");    
        
        //label.setText(currentLayerFamily.getHtNombre().get("es_ES").toString());
        //button.setText("pulse para editar");
        button.setOpaque(false);
        button.setIcon(IconLoader.icon(ICONO_BANDERA));        
        
        panelLayer.setLayout(null);
        panelLayer.setSize(200, 20);
        panelLayer.add(labelLayer, null);
        panelLayer.setVisible(true);
        panelLayer.setBackground(Color.WHITE);
        panelLayer.setOpaque(false);
        
        panelFamily.setLayout(null);
        panelFamily.setSize(200, 20);        
        panelFamily.add(button, null);
        panelFamily.add(labelFamily, null);
        panelFamily.setVisible(true);
        panelFamily.setBackground(Color.WHITE);
        panelFamily.setOpaque(false);
    }
    
    /**
     * Handles events from the editor button and from
     * the dialog's OK button.
     */
    public void actionPerformed(ActionEvent e) 
    {
        if (currentLayerFamily !=null){
            
            if (EDIT.equals(e.getActionCommand())) {
                //The user has clicked the cell, so
                //bring up the dialog.
                //button.setBackground(currentColor);
                
                if (currentLayerFamily.getHtNombre()==null)
                    labelFamily.setText(aplicacion.getString("layerfamilies.sinnombre"));
                //traducciones = operaciones.buscarTraduccionLayerFamily(currentLayerFamily);
                else
                {
                    traducciones = currentLayerFamily.getHtNombre();
                    labelFamily.setText(traducciones.get(AppConstants.DEFAULT_LOCALE).toString());
                }
                jDiccionario = new JDialogTranslations(aplicacion.getMainFrame(), true, traducciones,true);
                jDiccionario.setSize(600,500);
                jDiccionario.show();  
                Hashtable diccionario = jDiccionario.getDiccionario();
                if (diccionario!=null && diccionario!=currentLayerFamily.getHtNombre())
                {
                    currentLayerFamily.setHtNombre(diccionario);
                    LayerOperations.familiasModificadas.add(currentLayerFamily);
                    Component[] comp = ((GestorCapas)(aplicacion.getMainFrame())).getPestanaTables().getComponents();
                    for (int i=0; i< comp.length; i++)
                    {
                        if (comp[i] instanceof LayerFamiliesPanel)
                        {
                            ((LayerFamiliesPanel)comp[i]).getBtnGrabar().setEnabled(true);                            
                        }
                    }                    
                }
                //Make the renderer reappear.
                fireEditingStopped();                 
            } 
        }
    }
    
    /**
     * Devuelve la LayerFamilyTable de la posición seleccionada
     * @return LayerFamilyTable seleccionada en el árbol
     */
    public Object getCellEditorValue() 
    {
        return currentLayerFamily;
    }
    
    /**
     * Obtiene el componente a pintar
     * 
     * @param tree El árbol en el que está el componente
     * @param value LayerFamilyTable a pintar
     * @param isSelected Verdadero si el componente está seleccionado
     * @param expanded Verdadero si el componente se tiene que mostrar expandido
     * @param leaf Verdadero si el componente es una hoja
     * @param row Índice de la entrada
     */
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row)
    {     
        if (((DefaultMutableTreeNode)value).getUserObject() instanceof LayerFamilyTable)
        {
            currentLayerFamily = (LayerFamilyTable)((DefaultMutableTreeNode)value).getUserObject();       
            
            if (currentLayerFamily!=null)
            {
                String texto = "";
                
                texto = getNameNode(value);
                /*
                 if (currentLayerFamily.getHtNombre()!=null)
                 {
                 if (!currentLayerFamily.getHtNombre().get(locale).toString().trim().equals(""))
                 texto = currentLayerFamily.getHtNombre().get(locale).toString();
                 else if (!currentLayerFamily.getHtNombre().get("es_ES").toString().trim().equals(""))
                 texto = currentLayerFamily.getHtNombre().get("es_ES").toString();
                 }                
                 */
                
                //panel.remove(label);
                labelFamily.setBackground(SystemColor.activeCaption);
                labelFamily.setForeground(SystemColor.activeCaptionText);
                labelFamily.setText(texto);
                labelFamily.setVisible(true);
                labelFamily.setOpaque(true);
                //panel.add(label, null);
            }
            
            return panelFamily;
        }
        else if (((DefaultMutableTreeNode)value).getUserObject() instanceof LayerTable)
        {
            String texto = "";
            texto = getNameNode(value);
            labelLayer.setIcon(IconLoader.icon(ICONO_LAYER));
            labelLayer.setText(texto);
            
            
            //labelLayer.setForeground(SystemColor.activeCaptionText);            
            labelLayer.setBackground(SystemColor.activeCaption);
            labelLayer.setForeground(SystemColor.activeCaptionText);
            labelLayer.setOpaque(true);
            labelLayer.setVisible(true);
            //panelLayer.setForeground(Color.WHITE);
            
            return panelLayer;
            
        }
        //return panel;
        String nombre = tree.getName();
        JLabel lab = new JLabel(nombre);
        lab.setIcon(IconLoader.icon(ICONO_RAIZ));
        lab.setOpaque(true);
        return lab;
    }
    
    /**
     * Obtiene el nombre del nodo
     * @param value Nodo
     * @return Nombre del nodo
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
        }catch(Exception e)
        {
            return null;
        }
    }
    
}



