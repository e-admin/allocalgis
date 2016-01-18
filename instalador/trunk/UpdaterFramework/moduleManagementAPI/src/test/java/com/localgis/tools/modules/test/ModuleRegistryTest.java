package com.localgis.tools.modules.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.ModuleRegistry;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
import com.localgis.tools.modules.impl.ArtifactImpl;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.impl.ModuleRegistryImpl;
import com.localgis.tools.modules.impl.VersionImpl;

public class ModuleRegistryTest
{

    @Test
    public void test() throws ModuleNotFoundException
    {
	Module mod1=new ModuleImpl("Test1", "1.0.0");
	mod1.setArtifact(new ArtifactImpl("com.localgis", "app1", new VersionImpl("1.0.0"), "war", null));
	Module mod2ref=new ModuleImpl("Test2", "1.0.0");
	Module mod2=new ModuleImpl("Test2", "1.0.0");
	mod2.setArtifact(new ArtifactImpl("com.localgis", "app2", new VersionImpl("1.0.0"), "war", null));
	
	Module mod3=new ModuleImpl("Test3", "1.0.0");
	mod1.addDependency(mod2ref);
	mod1.addDependency(mod3);
	
	ModuleRegistry reg=ModuleRegistryImpl.getInstance();
	ModuleDependencyTree moduleRegistry = ((ModuleDependencyTree)reg);
	reg.register(mod1);
	reg.register(mod3);
	reg.register(mod2);
	Collection<Module> modules = moduleRegistry.getModules();
	
	assertTrue(modules.contains(mod1));
	assertTrue(modules.contains(mod2));
	assertTrue(modules.contains(mod3));
	
	Module registeredMod2 = moduleRegistry.getModuleInstance(mod2);
	assertFalse(registeredMod2.isReference());
	
    }

}
/**
 * ModuleRegistryTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
