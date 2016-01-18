/**
 * TestUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDepComparator;

public class TestUtils
{
private static final Log LOGGER=LogFactory.getLog(TestUtils.class);

    public static void dumpDependencyTree(Collection<Module> collection, int level, ArrayList<Module>excluded, ArrayList<Boolean>drawBar)
    {
        if (excluded==null)
    	excluded= new ArrayList<Module>();
        if (drawBar==null)
            drawBar=new ArrayList<Boolean>();
        int number=0;
        if (level>15) 
		{
		    LOGGER.error("!!!!!!!!!!!!!!! >15 levels");
		    return; // avoid loops
		}
	if (drawBar.size()<level+1)
		drawBar.add(true);
	drawBar.set(level, !collection.isEmpty());    
        for (Module module:collection)
    	{
    	   
    	    if (excluded.contains(module))
    		continue;
   	
    	    for (int i=0;i<level+1;i++) 
		    {
			if (drawBar.get(i))
			    {
				System.out.print("  |");
			    } else
			    {
				System.out.print("   ");
			    }
		    }
    	 
    	    System.out.println("- "+module+" ("+System.identityHashCode(module)+")"); 
    	   // excluded.add(module);
    	    number++;
	    drawBar.set(level, number<collection.size());
	    
    	    dumpDependencyTree(module.dependsOn(),level+1,excluded,drawBar);
    	}
    }

    public static void dumpDependentTree(Collection<Module> collection, int level, ArrayList<Module>excluded)
    {
        if (excluded==null)
       	excluded= new ArrayList<Module>();
        for (Module module:collection)
    	{
    	    if (level>15) 
    		{
    		    LOGGER.error("!!!!!!!!!!!!!!! >15 levels");
    		    continue; // avoid loops
    		}
    	    System.out.print(" |");
    	    for (int i=0;i<level;i++) System.out.print("  |");
    	    System.out.println("- "+module+" ("+System.identityHashCode(module)+")"); 
    	    //excluded.add(module);
    	    dumpDependentTree(module.dependents(),level+1,excluded);
    	}
    }
/**
 * 
 * @param ordered
 * @return true
 * @throws IllegalStateException
 */
    public static boolean checkDependencyOrdered(List<Module> ordered) throws IllegalStateException
    {
	for (int i=0;i<ordered.size();i++)
	    {
		Module comp=ordered.get(i);
		for (int j=i+1;j<ordered.size();j++)
		    {
			Module dependentModule = ordered.get(j);
			boolean compare = ModuleDepComparator.isDeepDependency( comp,dependentModule);
			if (compare)
			    {
				throw new IllegalStateException(dependentModule+" is placed later than "+comp);
			    }
		    }
	    }
	return true;
    }

}
