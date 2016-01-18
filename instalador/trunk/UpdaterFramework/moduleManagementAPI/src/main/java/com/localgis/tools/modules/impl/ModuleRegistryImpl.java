/**
 * ModuleRegistryImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.impl;

import java.util.logging.Logger;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTreeImpl;
import com.localgis.tools.modules.ModuleRegistry;
@Deprecated
public class ModuleRegistryImpl implements ModuleRegistry
{
    private static final Logger log=Logger.getLogger(ModuleRegistryImpl.class.toString());
    private static ModuleRegistry _singleton = new ModuleDependencyTreeImpl();
    
   
    
    public Module register(Module newMod)
    {
	return null;
    }
    private void registerDependencies(Module newMod)
    {
	
    }
    /**
     * Reemplaza todas las dependencias coincidentes por la nueva instancia
     * @param newMod
     */
    private void replaceInstances(Module newMod)
    {
	
    }
    public static ModuleRegistry getInstance()
    {
	return ModuleRegistryImpl._singleton;
    }

}
