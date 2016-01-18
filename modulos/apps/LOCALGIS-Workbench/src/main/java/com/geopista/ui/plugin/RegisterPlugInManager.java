/**
 * RegisterPlugInManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RegisterPlugInManager
{
	private static final Log	logger	= LogFactory.getLog(RegisterPlugInManager.class);
    private Hashtable pluginHashTable = new Hashtable();


    public static void main(String[] args)
    {
    }
    
    
    public void registerPlugIn(Object pluginMenu,Object newPlugin)
    {
    	if (pluginMenu==null){
    		logger.warn("Menu para el plugin no inicializado:"+newPlugin);
    		return;
    	}
    	
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
    	
    	if (pluginMenu==null){
    		logger.warn("Menu para el plugin no inicializado:"+newPlugin);
    		return false;
    	}
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
	
	  /**
    	Destruccion del elemento.
    	@baseSatec Destruccion del elemento.
    */
    public void destroy(){
    	pluginHashTable.clear();
    	pluginHashTable=null;
    }

    
}
