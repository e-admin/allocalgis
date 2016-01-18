package com.geopista.app.layerutil.layer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.geopista.protocol.administrador.Acl;

public class AclCellRenderer extends JLabel implements ListCellRenderer
{
    /**
     * Constructor por defecto de la clase 
     */
    public AclCellRenderer()
    {
        super();
        setOpaque(true);
    }
    
    /**
     * Obtiene el componente a pintar
     * 
     * @param list La lista en la que está el componente
     * @param value ACL a pintar
     * @param index Posición del componente en la lista
     * @param isSelected Verdadero si el componente está seleccionado
     * @param cellHasFocus Verdadero si el componente tiene el foco
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (value instanceof Acl)
        {
            Acl acl = (Acl)value;
            setText(acl.getNombre());
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
