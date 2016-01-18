/*
 * Created on 10-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RegisterPlugInManager
{
    private Hashtable pluginHashTable = new Hashtable();


    public static void main(String[] args)
    {
    }
    
    
    public void registerPlugIn(Object pluginMenu,Object newPlugin)
    {
        ArrayList currentArrayList = (ArrayList)pluginHashTable.get(pluginMenu);
        if(currentArrayList==null)
        {
            currentArrayList = new ArrayList();
            pluginHashTable.put(pluginMenu,currentArrayList);
        }
        
        currentArrayList.add(newPlugin);
    }
    
    public boolean isRegisteredPlugIn(Object pluginMenu,Object newPlugin)
    {
        ArrayList currentArrayList = (ArrayList)pluginHashTable.get(pluginMenu);
        if(currentArrayList==null) return false;
        if(containsPlugIn(currentArrayList,newPlugin)) return true;
        return false;
        
    }
    
    private boolean containsPlugIn(ArrayList sourceArray, Object search)
    {
        
        Iterator sourceArrayIter = sourceArray.iterator();
        while (sourceArrayIter.hasNext())
        {
            Object currentPlugIn =  sourceArrayIter.next();
            
            if(currentPlugIn.equals(search)) return true;            
        }
        
        return false;
    }

    public Collection getPlugInOfClass(Object pluginMenu,Class classPlugIn){

    	ArrayList collectionPlugIn = new ArrayList();
    	ArrayList currentArrayList = (ArrayList)pluginHashTable.get(pluginMenu);    	
    	if(currentArrayList==null) return null;
    	Iterator sourceArrayIter = currentArrayList.iterator();
    	while (sourceArrayIter.hasNext())
    	{
    		Object currentPlugIn =  sourceArrayIter.next();

    		if(currentPlugIn.getClass().equals(classPlugIn)) 
    			collectionPlugIn.add(currentPlugIn);            
    	}

    	return collectionPlugIn;
    }


	public Collection getPlugins() {
		
		ArrayList lst = new ArrayList();
		
		Collection c = pluginHashTable.values();
		for (Iterator it = c.iterator(); it.hasNext();) {
			Collection plugins = (Collection) it.next();
			lst.addAll(plugins);			
		}	
		
		return lst;
	}
    
}
