/**
 * CapasExtendidasListCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin.external;


/**
 * Define el aspecto de la lista de capas
 * 
 * @author cotesa
 *
 */
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;

public class CapasExtendidasListCellRenderer extends JLabel implements ListCellRenderer {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    
    /**
     * Constructor por defecto
     *
     */
    public CapasExtendidasListCellRenderer() {
        super();
        setOpaque(true);
    }
    /**
     * Obtiene el componente a pintar
     * 
     * @param list La lista en la que está el componente
     * @param value Atributo a pintar
     * @param index Posición del componente en la lista
     * @param isSelected Verdadero si el componente está seleccionado
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

        if (value instanceof CapasExtendidas){
        	CapasExtendidas capaExtendida = (CapasExtendidas)value;
            String texto = capaExtendida.getNombreTraducido(); 
            setText(texto);            
        }
            
        if (isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        return this;
    }
}
