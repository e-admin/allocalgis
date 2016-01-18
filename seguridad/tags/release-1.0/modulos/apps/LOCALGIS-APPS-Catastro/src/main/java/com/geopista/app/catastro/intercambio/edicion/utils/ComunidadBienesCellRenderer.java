package com.geopista.app.catastro.intercambio.edicion.utils;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.geopista.app.catastro.model.beans.ComunidadBienes;

/**
 * Define el aspecto de la lista de las comunidades de bienes
 * 
 * @author cotesa
 *
 */
public class ComunidadBienesCellRenderer extends JLabel implements ListCellRenderer {
         
    /**
     * Constructor por defecto
     *
     */
    public ComunidadBienesCellRenderer() {
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
        String texto;
        
        if (value instanceof ComunidadBienes)
        {
            texto = ((ComunidadBienes)value).getNif();
            setText(texto);            
            
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
