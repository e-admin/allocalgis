/**
 * LayersListCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layer;


/**
 * Define el aspecto de la lista de capas
 * 
 * @author cotesa
 *
 */
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.app.layerutil.layerfamily.LayerFamilyTable;
import com.geopista.model.LayerFamily;
import com.geopista.util.config.UserPreferenceStore;

public class LayersListCellRenderer extends JLabel implements ListCellRenderer {
    
    private static final String ICONO_LAYER= "layer.gif";
    public static final String ICONO_LAYER_FAMILY= "layerfamily.gif";
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    
    /**
     * Constructor por defecto
     *
     */
    public LayersListCellRenderer() {
        super();
        setOpaque(true);
    }
    /**
     * Obtiene el componente a pintar
     * 
     * @param list La lista en la que esta el componente
     * @param value Atributo a pintar
     * @param index Posicion del componente en la lista
     * @param isSelected Verdadero si el componente esta seleccionado
     * @param cellHasFocus Verdadero si el componente tiene el foco
     */
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
        //int selectedIndex = ((Integer)value).intValue();

        if (value instanceof LayerTable)
        {
            LayerTable lt = (LayerTable)value;
            String texto= lt.getLayer().getDescription();
            if (lt.getHtNombre().get(locale)!=null)
                texto = lt.getHtNombre().get(locale).toString();
            else if (lt.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!=null)
                texto = lt.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
            
            
            setText(texto);            
            
            setIcon(IconLoader.icon(ICONO_LAYER));
            setHScrollFor(list);
        }
        
        else if (value instanceof LayerFamily)
        {
            setText (((LayerFamily)value).getDescription());
        }
        
        else if (value instanceof LayerFamilyTable)
        {
            LayerFamilyTable lft = (LayerFamilyTable)value;
            String texto= lft.getLayerFamily().getDescription();
            if (lft.getHtNombre().get(locale)!=null)
                texto = lft.getHtNombre().get(locale).toString();
            else if (lft.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!=null)
                texto = lft.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
            
            
            setText(texto);           
            
            setIcon(IconLoader.icon(ICONO_LAYER_FAMILY));
            setHScrollFor(list);
        }
            
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        return this;
    }
    private void setHScrollFor(JList list) {
        JScrollPane scrollPane =
                (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, list);
        if (scrollPane.getHorizontalScrollBar() == null
                || scrollPane.getHorizontalScrollBarPolicy()
                != JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
          scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
          scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
      }
}
