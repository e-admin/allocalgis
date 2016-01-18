package com.geopista.app.administrador.usuarios;

import java.util.Comparator;
import java.util.Map.Entry;

import com.geopista.protocol.administrador.Acl;
import com.geopista.protocol.administrador.Rol;

public class RolComparator implements Comparator
{
        public int compare(Object o1,Object o2)
        {
        	Entry entry1=(Entry)o1;
        	Rol rol1=(Rol)entry1.getValue();
        	Entry entry2=(Entry)o2;
        	Rol rol2=(Rol)entry2.getValue();

        	String name1=rol1.getNombre();
        	String name2=rol2.getNombre();
        	if (name1.compareTo(name2)>0)
        		return 1;
        	else if (name1.compareTo(name2)<0)
        		return -1;
        	else
        		return 0;
        }
}

