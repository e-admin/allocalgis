/**
 * AttributeRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.schema.attribute;


/**
 * Define el aspecto de la lista de atributos
 * 
 * @author cotesa
 *
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.feature.Attribute;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;

public class AttributeRenderer extends JLabel implements TableCellRenderer 
{
        
    private static final String ICONO_BANDERA= "banderas.gif";
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    

    /**
     * Constructor de la clase
     *
     */
    public AttributeRenderer() {
      
        setOpaque(true); 
    }

    /**
     * Obtiene el componente a pintar
     * 
     * @param table La tabla en la que está el componente
     * @param valor Atributo a pintar
     * @param sel Verdadero si el componente está seleccionado
     * @param hasFocus Verdadero si el componente tiene el foco
     * @param row Índice de la fila
     * @param column Índice de la columna
     */
    public Component getTableCellRendererComponent(
                            JTable table, Object valor,
                            boolean sel, boolean hasFocus,
                            int row, int column) {
        
        Attribute att = (Attribute)valor;
        String txtBoton =I18N.get("GestorCapas","layers.boton.atributo");
        if (att!=null && att.getHtTraducciones()!=null)
        {
            if(att.getHtTraducciones().get(locale)!=null &&!att.getHtTraducciones().get(locale).toString().trim().equals(""))
                txtBoton = att.getHtTraducciones().get(locale).toString();
            else if (att.getHtTraducciones().get(AppConstants.DEFAULT_LOCALE)!=null &&!att.getHtTraducciones().get(AppConstants.DEFAULT_LOCALE).toString().trim().equals(""))
                txtBoton = att.getHtTraducciones().get(AppConstants.DEFAULT_LOCALE).toString();
        }
        
        setText (txtBoton);
        setIcon(IconLoader.icon(ICONO_BANDERA));
        
        if (sel)
        {
            this.setBackground(SystemColor.activeCaption);
            this.setForeground(SystemColor.activeCaptionText);
            this.setOpaque(true);
        }
        else
        {
            this.setBackground(SystemColor.controlHighlight);
            this.setForeground(Color.BLACK);
            this.setOpaque(true);
        }
                
        return this;
    }
}
