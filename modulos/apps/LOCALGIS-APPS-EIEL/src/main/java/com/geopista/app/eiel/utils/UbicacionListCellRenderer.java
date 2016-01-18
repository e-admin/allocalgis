/**
 * UbicacionListCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


package com.geopista.app.eiel.utils;


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
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.beans.indicadores.IndicadorEIEL;
import com.geopista.app.eiel.beans.indicadores.MapaEIEL;
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
        
        if (value instanceof Provincia)
        {  
        	if (((Provincia)value).getNombreOficial()!=null){
        		if (((Provincia)value).getNombreOficial().equals(""))
        			setText (NO_ESPECIFICADO);
        		else
        			setText(((Provincia)value).getNombreOficial() + " (" +((Provincia)value).getIdProvincia()+ ")");   
        	}
        }
        
        if (value instanceof Municipio)
        {
        	if (((Municipio)value).getIdIne()!=null){
        		if (((Municipio)value).getIdIne().equals(""))
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((Municipio)value).getNombreOficial() + "(" +((Municipio)value).getIdIne() + ")");   
        	}
        }
        else if (value instanceof EntidadEIEL)
        {
        	if (((EntidadEIEL)value).getCodEntidad()!=null){
        		if (((EntidadEIEL)value).getCodEntidad().equals(""))
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((EntidadEIEL)value).getCodEntidad());   
        	}
        } 
        else if (value instanceof EntidadesSingularesEIEL)
        {
        	if (((EntidadesSingularesEIEL)value).getCodINEEntidad()!=null){
        		if (((EntidadesSingularesEIEL)value).getCodINEEntidad().equals(""))        			
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((EntidadesSingularesEIEL)value).getDenominacion() + " ("+ ((EntidadesSingularesEIEL)value).getCodINEEntidad() + ")");   
        	}
        	else{
        		setText(NO_ESPECIFICADO);
        	}
        } 
        else if (value instanceof NucleosPoblacionEIEL)
        {
        	if (((NucleosPoblacionEIEL)value).getCodINEPoblamiento()!=null){
        		if (((NucleosPoblacionEIEL)value).getCodINEPoblamiento().equals(""))        			
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((NucleosPoblacionEIEL)value).getNombreOficial() + " ("+ ((NucleosPoblacionEIEL)value).getCodINEPoblamiento() + ")");   
        	}
        	else{
        		setText(NO_ESPECIFICADO);
        	}
        }
        
        else if (value instanceof IndicadorEIEL)
        {
        	if (((IndicadorEIEL)value).getNombreIndicador()!=null){
        		if (((IndicadorEIEL)value).getNombreIndicador().equals(""))        			
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((IndicadorEIEL)value).getNombreIndicador());   
        	}
        	else{
        		setText("");   
        	}
        }
        else if (value instanceof MapaEIEL)
        {
        	if (((MapaEIEL)value).getNombreMapa()!=null){
        		if (((MapaEIEL)value).getNombreMapa().equals(""))        			
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((MapaEIEL)value).getNombreMapa());   
        	}
        	else{
        		setText("");   
        	}
        }
        else if (value instanceof LCGMunicipioEIEL)
        {
        	if (((LCGMunicipioEIEL)value).getNombreOficial()!=null){
        		if (((LCGMunicipioEIEL)value).getNombreOficial().equals(""))        			
        			setText(NO_ESPECIFICADO);
        		else
        			setText(((LCGMunicipioEIEL)value).getNombreOficial());   
        	}
        }
        else if (value instanceof LCGNucleoEIEL)
        {
        	LCGNucleoEIEL valor=(LCGNucleoEIEL)value;
        	if (valor.getCodentidad()!=null){
        		if (valor.getCodentidad().equals("0"))
        			setText(NO_ESPECIFICADO);
        		else
        			setText(valor.getCodentidad()+"_"+valor.getCodpoblamiento()+"_"+valor.getDenominacion());   
        	}
        }
        else if (value instanceof String){
        	
        	if (((String)value).toString() != null){
        		if (((String)value).toString().equals(""))
        			setText(NO_ESPECIFICADO);
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
