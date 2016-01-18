/**
 * TestModuleDependencyTree.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
import com.localgis.tools.modules.impl.ModuleImpl;

public class TestModuleDependencyTree
{

    private ModuleDependencyTree registry;

    @Before
    public void setUp() throws Exception
    {
	InputStream is = this.getClass().getClassLoader().getResourceAsStream("pack.xml");
	this.registry= XMLModuleSerializer.getModuleDependencyTreeFromXMLStream(is);
    }

    @Test
    public void testDependencyOrdering()
    {
	Collection<Module> modules = this.registry.getModules();
	List<Module> ordered = this.registry.orderModulesByDependencies(modules);
	TestUtils.dumpDependencyTree(modules, 0, null, null);
	String string = ordered.toString();
	assertTrue(TestUtils.checkDependencyOrdered(ordered));
	assertEquals("[Example DataModel module 2:3.1.0, Example client module 3:3.0.20, Example module 1:3.0.0]",string);
    }
    @Test
    public void testUpgradeModule() throws ModuleNotFoundException 
	{
	    Vector<Module> pack=new Vector<Module>();
	    ModuleImpl mod = new ModuleImpl("Example DataModel module 2","3.1.6");
	    pack.add(mod);
	    TestUtils.dumpDependencyTree(this.registry.getModules(), 0, null, null);
	    
	    List<Module> upgradables = this.registry.getUpgradablesForPack(pack);
	    // NO es posible actualizar incremental versions automáticamente ya que el proceso de actualización no es 
	    // equivalente a comprobar la compatibilidad
	    //assertTrue(upgradables.contains(mod));
	    assertFalse(upgradables.contains(mod));
	    
	    Vector<Module> modules2=new Vector<Module>();
	    modules2.add(new ModuleImpl("Example DataModel module 2","3.2.0"));
	    upgradables = this.registry.getUpgradablesForPack(modules2);
	    // no es posible actualizar minor versions sin sustituir
	    assertFalse(upgradables.contains(mod));
	   
	    // identificar incompatibilidades
	    Collection<Module> incompatibles= this.registry.getIncompatiblesForPack(pack);
	    // es posible actualizar incremental versions 3.1.6
	    assertTrue(incompatibles.isEmpty());
	    // no es posible actualizar minor versions sin sustituir 3.2.0
	    incompatibles= this.registry.getIncompatiblesForPack(modules2);
	    assertTrue(incompatibles.contains(new ModuleImpl("Example DataModel module 2", "3.1.0")));
	    
	    upgradables = this.registry.getUpgradablesForPack(pack);
	    // no es posible actualizar implícitamente
	    assertFalse(upgradables.contains(mod));
	    // Creo un path explícito para las actualizaciones
	    Module installedExampleDataModelModule2 = this.registry.getModuleInstance(new ModuleImpl("Example DataModel module 2", "3.1.0"));
	    mod.setUpgrades(installedExampleDataModelModule2);
	    upgradables = this.registry.getUpgradablesForPack(pack);
	    // ya es posible actualizar explicitamente
	    assertTrue(upgradables.contains(mod));
	    
	    List<Module> compatibles = this.registry.getCompatiblesForPack(pack);
	    assertTrue(compatibles.contains(installedExampleDataModelModule2));
	}
    /**
     * Prueba la instalación en el registro de un grupo de actualizaciones
     * que tienen dependencias múltiples.
     * No se pueden hacer individualmente porque se rompería la compatibilidad 
     * en el paso intermedio.
     * @throws ModuleNotFoundException
     */
    @Test
    public void testUpgradeModulePack() throws ModuleNotFoundException 
	{
	
	}
}
