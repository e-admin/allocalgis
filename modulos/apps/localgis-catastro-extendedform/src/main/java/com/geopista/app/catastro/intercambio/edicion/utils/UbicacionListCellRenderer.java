/**
 * UbicacionListCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.catastro.intercambio.edicion.utils;


/**
 * Define el aspecto de la lista de provincias
 * 
 * @author cotesa
 *
 */
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.vividsolutions.jump.I18N;

public class UbicacionListCellRenderer extends JLabel implements ListCellRenderer {
    
    
    private static final String NO_ESPECIFICADO= 
        I18N.get("Expedientes", "desplegable.noespecificado");
    
    /**
     * Constructor por defecto
     *
     */
    public UbicacionListCellRenderer() {
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
        
        if (value instanceof Provincia)
        {  
        	if (((Provincia)value).getNombreOficial()!=null){
        		if (((Provincia)value).getNombreOficial().equals(""))
        			setText (NO_ESPECIFICADO);
        		else
        			setText(((Provincia)value).getIdProvincia() + "-" + ((Provincia)value).getNombreOficial());   
        	}
        }
        
        else if (value instanceof Municipio)
        {
        	if (((Municipio)value).getNombreOficial()!=null){
        		if (((Municipio)value).getNombreOficial().equals(""))
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((Municipio)value).getNombreOficial());   
        	}
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
}
