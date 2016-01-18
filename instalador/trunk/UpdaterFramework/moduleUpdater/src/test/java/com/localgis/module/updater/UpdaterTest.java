/**
 * UpdaterTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import junit.framework.Assert;

import org.junit.Test;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.impl.ModuleRegistryImpl;

public class UpdaterTest
{
    @Test
    public void testRuntimeRegister() throws ModuleNotFoundException
    {
	Updater updater = new ExampleUpdater();
	updater.register();
	ModuleDependencyTree registry = (ModuleDependencyTree)ModuleRegistryImpl.getInstance();
	Module module = registry.getModuleInstance(new ModuleImpl("TestModule", "1.0.0"));
	Assert.assertFalse(module.isReference());
    }
    @Test
    public void testStaticRegister() throws ModuleNotFoundException
    {
	ModuleDependencyTree registry = (ModuleDependencyTree)ModuleRegistryImpl.getInstance();
	Module module = registry.getModuleInstance(new ModuleImpl("TestModule", "1.0.0"));
	Assert.assertFalse(module.isReference());
    }
    /**
     * Bootstrap statically
     */
    static 
    {
	Updater updater=new ExampleUpdater();
	updater.register();
    }
    
    //@Test
    public void testInstallInteraction()
    {
	Assert.fail("Not implemented.");
    }
    
    //@Test
    public void testLoadDependencies()
    {
	Assert.fail("Not implemented.");
    }
}
