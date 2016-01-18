/**
 * MapImageParametricHelpDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.reports.gui;

import java.awt.Frame;

import javax.swing.JLabel;

public class MapImageParametricHelpDialog extends javax.swing.JDialog {

    public MapImageParametricHelpDialog() {
        initComponents();
    }
    
	public MapImageParametricHelpDialog(Frame owner){
        super(owner);
        initComponents();
    }

	private void initComponents() {
        setTitle("Informes paramétricos");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);        

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 15));

        JLabel jLabelHelp = new JLabel();
        String text = "<html><h2>Informes paramétricos</h2>" +
        	"<p>Son informes que presentan datos e imagenes de mapas de elementos que cumplan una<br/>" +
        	"condición específica expresada en la consulta a la base de datos.</p>" +
        	"<p>Previamente a configurar las imágenes de mapas paramétricos, se debe  haber definido la<br/>" +
        	"consulta del informe. Una vez hecho esto, se deben completar los siguientes pasos:</p>" +
        	"<ol><li>Seleccione el mapa. Debe ser un mapa previamente publicado con carácter<br/>" +
        	"privado en el servidor de mapas de LocalGIS.</li>" +
        	"<li>Seleccione una capa de la lista sobre la cual desea ejecutar el informe.</li>" + 
        	"<li>Seleccione el atributo <b>identificador</b> de la capa. Debe tener valor único<br/>" +
        	"para cada elemento de la capa.</li>" +
        	"<li>Asegúrese que este atributo identificador de la capa es uno de los<br/>" +
        	"atributos seleccionados en la consulta del informe. De no ser asi, el campo<br/>" +
        	"<i>Campo identificador en la consulta</i> se mostrará vacío y no se podrá <br/>" +
        	"ejecutar correctamente el informe.</li>" +
        	"<li>Elija las capas a visualizar en el informe.</li></ol></html>" +
        	"<li>Si selecciona la capa lcg_point_reports se mostrara una marca como un punto en la ubicacion</li></ol></html>" +
        	"<li>Elija la escala de visualización del mapa en el informe.</li></ol></html>";
    	jLabelHelp.setText(text);

    	add(jLabelHelp);

    	pack();
	}

}
