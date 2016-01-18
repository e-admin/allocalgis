/**
 * AppComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import java.util.Comparator;
import java.util.Map.Entry;

import com.geopista.protocol.administrador.App;

public class AppComparator implements Comparator
{
        public int compare(Object o1,Object o2)
        {
        	Entry entry1=(Entry)o1;
        	App app1=(App)entry1.getValue();
        	Entry entry2=(Entry)o2;
        	App app2=(App)entry2.getValue();

        	String name1=app1.getNombre();
        	String name2=app2.getNombre();
        	if (name1.compareTo(name2)>0)
        		return 1;
        	else if (name1.compareTo(name2)<0)
        		return -1;
        	else
        		return 0;
        }
}

