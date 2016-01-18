/**
 * CamposFiltroComboBoxRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.filter;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 04-oct-2006
 * Time: 18:11:30
 * To change this template use File | Settings | File Templates.
 */
public class CamposFiltroComboBoxRenderer  extends JLabel implements ListCellRenderer{

	
	private static  org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CamposFiltroComboBoxRenderer.class);
	
    public CamposFiltroComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        if (value==null) return this;
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            //setBorder(BorderFactory.createLineBorder(Color.red,2));
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalAlignment(LEFT);
        if (value instanceof CampoFiltro)
            setText((((CampoFiltro)value).getDescripcion()!=null?((CampoFiltro)value).getDescripcion():((CampoFiltro)value).getNombre()));
        else
        	logger.error("El campo no es un filtro");
        return this;
    }

}
