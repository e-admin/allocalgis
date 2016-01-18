/**
 * ListaEntidades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;


public class ListaEntidades {
    private Hashtable hEntidades;

    public ListaEntidades() {
        this.hEntidades = new Hashtable();
    }

    public void add(Entidad entidad) {
        this.hEntidades.put(entidad.getId(), entidad);
    }

    public Entidad get(String sIdEntidad) {
        return (Entidad) this.hEntidades.get(sIdEntidad);
    }

    public void remove(Entidad entidadEliminada) {
        this.hEntidades.remove(entidadEliminada.getId());
    }

    public void set(Vector vEntidades) {
        if (vEntidades == null)
            return;
        for (Enumeration e = vEntidades.elements(); e.hasMoreElements();) {
            Entidad auxEntidad = (Entidad) e.nextElement();
            add(auxEntidad);
        }
    }

    public Hashtable gethEntidades() {
        return hEntidades;
    }
    
    
    public JComboBox cargarJComboBox (JComboBox jComboBox) {
        List entidades = sort(); 
        Iterator itr = entidades.iterator();
        String key = "";
        Entidad value = null;
        while (itr.hasNext()) {
            Map.Entry e = (Map.Entry) itr.next();
            key = (String) e.getKey();
            value = (Entidad) e.getValue();
            jComboBox.addItem(value);
        }      
        
        return jComboBox;
    }
    
    
    public List sort() {
        ArrayList myArrayList = new ArrayList(hEntidades.entrySet());
        // Sort the values based on values first and then keys.
        Collections.sort(myArrayList, new EntidadesComparator());
        return myArrayList;
    }
    
    class EntidadesComparator implements Comparator {

        public int compare(Object obj1, Object obj2) {

            int result = 0;
            Map.Entry e1 = (Map.Entry) obj1;
            Map.Entry e2 = (Map.Entry) obj2;
                        
            String entidad1 = ((Entidad) e1.getValue()).getNombre();
            String entidad2 = ((Entidad) e2.getValue()).getNombre();

            if (entidad1.compareToIgnoreCase(entidad2) == 0) {

                String word1 = (String) e1.getKey();
                String word2 = (String) e2.getKey();

                // Sort String in an alphabetical order
                result = word1.compareToIgnoreCase(word2);

            } else {
                result = entidad1.compareToIgnoreCase(entidad2);
            }

            return result;
        }
    }
    
    
}
