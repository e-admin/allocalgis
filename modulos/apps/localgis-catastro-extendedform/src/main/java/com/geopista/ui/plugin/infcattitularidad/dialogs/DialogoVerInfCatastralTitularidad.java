/**
 * DialogoVerInfCatastralTitularidad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.infcattitularidad.dialogs;

import java.util.Collection;

import javax.swing.JPanel;

import com.geopista.ui.plugin.infcattitularidad.paneles.InfCatatralTitularidadPanel;


public class DialogoVerInfCatastralTitularidad extends javax.swing.JDialog{

	private JPanel todoPanel;
	private JPanel panel;
	
	

	/**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     * @param convenioExpediente String
     */
    public DialogoVerInfCatastralTitularidad(java.awt.Frame parent, boolean modal, Collection coll, String convenio, String title)
    {
		super(parent, modal);
		this.setTitle(title);
        inicializaDialogo(coll, convenio);
	}
    
    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo(Collection coll, String convenio)
    {

    	getContentPane().add(new InfCatatralTitularidadPanel(false, coll, convenio));
    }
    
    
}
