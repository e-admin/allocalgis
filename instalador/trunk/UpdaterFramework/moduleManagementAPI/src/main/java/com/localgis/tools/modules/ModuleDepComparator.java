/**
 * ModuleDepComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules;

import java.util.Collection;
import java.util.Comparator;

public final class ModuleDepComparator implements Comparator<Module>
{
   
	@Override
	public int compare(Module o1, Module o2)
	{
	if (isDeepDependency(o1,o2))
	{
	    return +1;
	}
	else if (isDeepDependency(o2, o1))
	{
	    return -1;
	}
	else
	{ // modulos independientes se ordenan alfabeticamente
	    int compareTo = o1.toString().compareTo(o2.toString());
	    return compareTo==0?0:compareTo>0?1:-1;
	}
	}
	    /**
	     * search in dependency tree for transitive dependencies
	     * dep es una dependencia transitiva de forMod?
	     * @param forMod
	     * @param dep para comprobar si es dependencia de forMod
	     * @return
	     */
	    public static boolean isDeepDependency(Module forMod, Module dep)
	    {
		Collection<Module> dependencies = forMod.dependsOn();
		if (dependencies.contains(dep))
		    return true;
		else
		    {
			for(Module dependency:dependencies)
			    {
				boolean isDependent=isDeepDependency(dependency,dep);
				if (isDependent)
				    return true;
			    }
		    }
		return false;
	    }
}