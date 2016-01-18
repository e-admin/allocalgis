package com.geopista.app.administrador.usuarios;

import java.util.Comparator;
import java.util.Map.Entry;

import com.geopista.protocol.administrador.Acl;

public class AclComparator implements Comparator
{
        public int compare(Object o1,Object o2)
        {
        	Entry entry1=(Entry)o1;
        	Acl acl1=(Acl)entry1.getValue();
        	Entry entry2=(Entry)o2;
        	Acl acl2=(Acl)entry2.getValue();

        	String name1=acl1.getNombre();
        	String name2=acl2.getNombre();
        	if (name1.compareTo(name2)>0)
        		return 1;
        	else if (name1.compareTo(name2)<0)
        		return -1;
        	else
        		return 0;
        }
}

