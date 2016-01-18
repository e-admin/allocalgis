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

import com.geopista.app.AppContext;

import com.geopista.app.layerutil.GestorCapas;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.app.layerutil.layerfamily.LayerFamilyTable;
import com.geopista.model.LayerFamily;

public class LayersListCellRenderer extends JLabel implements ListCellRenderer {
    
    private static final String ICONO_LAYER= "layer.gif";
    public static final String ICONO_LAYER_FAMILY= "layerfamily.gif";
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, GestorCapas.DEFAULT_LOCALE, false);
    
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
            else if (lt.getHtNombre().get(GestorCapas.DEFAULT_LOCALE)!=null)
                texto = lt.getHtNombre().get(GestorCapas.DEFAULT_LOCALE).toString();
            
            
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
            else if (lft.getHtNombre().get(GestorCapas.DEFAULT_LOCALE)!=null)
                texto = lft.getHtNombre().get(GestorCapas.DEFAULT_LOCALE).toString();
            
            
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
