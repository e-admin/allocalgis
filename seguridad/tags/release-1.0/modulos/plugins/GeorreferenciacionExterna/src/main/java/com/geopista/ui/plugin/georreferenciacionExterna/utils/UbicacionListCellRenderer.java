

package com.geopista.ui.plugin.georreferenciacionExterna.utils;


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


public class UbicacionListCellRenderer extends JLabel implements ListCellRenderer {
    
    
    
    
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
        
//        if (value instanceof Provincia)
//        {  
//        	if (((Provincia)value).getNombreOficial()!=null){
//        		if (((Provincia)value).getNombreOficial().equals(""))
//        			setText (NO_ESPECIFICADO);
//        		else
//        			setText(((Provincia)value).getIdProvincia() + "-" + ((Provincia)value).getNombreOficial());   
//        	}
//        }
        
        if (value instanceof MunicipioEIEL)
        {
        	if (((MunicipioEIEL)value).getCodMunicipio()!=null){
        		if (((MunicipioEIEL)value).getCodMunicipio().equals(""))
        			setText("NO HAY TABLAS");
        		else
        			setText(((MunicipioEIEL)value).getCodMunicipio());   
        	}
        }
        else if (value instanceof EntidadEIEL)
        {
        	if (((EntidadEIEL)value).getCodEntidad()!=null){
        		if (((EntidadEIEL)value).getCodEntidad().equals(""))
        			setText("NO HAY TABLAS");
        		else
        			setText(((EntidadEIEL)value).getCodEntidad());   
        	}
        }
        
        else if (value instanceof String){
        	
        	if (((String)value).toString() != null){
        		if (((String)value).toString().equals(""))
        			setText("NO HAY TABLAS");
        		else
        			setText(((String)value).toString());
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
