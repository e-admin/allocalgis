package com.geopista.app.administrador.usuarios;

import java.util.Comparator;
import java.util.Map.Entry;

import com.geopista.protocol.administrador.Acl;
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

