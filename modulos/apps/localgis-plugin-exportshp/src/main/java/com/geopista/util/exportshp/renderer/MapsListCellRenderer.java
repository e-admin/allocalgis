/**
 * MapsListCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.util.exportshp.renderer;


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

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.model.ExportMapBean;
import com.geopista.model.GeopistaMap;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;



public class MapsListCellRenderer extends JLabel implements ListCellRenderer {
    

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, I18N.getLocaleAsObject().getLanguage()+"_"+I18N.getLocaleAsObject().getCountry(), false);
    
    /**
     * Constructor por defecto
     *
     */
    public MapsListCellRenderer() {
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

        if (value instanceof ExportMapBean)
        {
        	ExportMapBean lt = (ExportMapBean)value;
            String texto= lt.getEntidad().getNombre()+" - "+lt.getName();         
            setText(texto);            
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
