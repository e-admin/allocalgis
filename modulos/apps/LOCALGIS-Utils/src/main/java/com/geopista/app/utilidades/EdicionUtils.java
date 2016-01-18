/**
 * EdicionUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class EdicionUtils
{

    public EdicionUtils()
    {
    }

    public static void crearMallaPanel(int numfila, int numCeldas, JPanel panel, double weightx, double weighty, int anchor, 
            int fill, Insets insets, int ipadx, int ipady)
    {
        for(int i = 0; i < numCeldas; i++)
            panel.add(new JPanel(), new GridBagConstraints(i, numfila, 1, 1, weightx, weighty, anchor, fill, insets, ipadx, ipady));

    }

    public static void cargarLista(JComboBox jComboBox, ArrayList lst)
    {
        jComboBox.removeAllItems();
        for(Iterator it = lst.iterator(); it.hasNext(); jComboBox.addItem(it.next()));
    }
    
}