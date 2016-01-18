package com.localgis.tools.modules.test;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Vector;

import junit.framework.TestCase;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.install.AbstractLocalGISInstallation;
import com.localgis.tools.modules.install.LocalGISInstallation;

public class TestLocalGISInstallation extends TestCase {
	LocalGISInstallation install;
	private Module mod1;
	private Module mod2;
	private Module mod3;
	@Override
	protected void setUp() throws Exception 
	{
		super.setUp();
		this.install = new AbstractLocalGISInstallation()
		{

		    @Override
		    public void close()
		    {
			// TODO Auto-generated method stub
			
		    }

		    @Override
		    public void connect(URL localGISLocation, String username, String password) throws IOException
		    {
			// TODO Auto-generated method stub
			
		    }
	
		};
		this.mod1 = new ModuleImpl("LocalGIS Core", "1.2.0-beta");
		this.mod2 = new ModuleImpl("Workbench", "1.1.0-rc1");
		this.mod2.addDependency(new ModuleImpl("LocalGIS Core", "1.2.0-beta"));// for
																		// equivalence
		this.mod3 = new ModuleImpl("ETRS89 Migration", "1.0.0-rc1");
		this.mod3.addDependency(this.mod1);

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}


	
	public void testInstallModule() throws DependencyViolationException, ModuleNotFoundException {

		try {
			this.install.installModule(this.mod1);
			assertTrue("UC04:no insertado", this.install.getModules().contains(this.mod1) );
				
		} catch (DependencyViolationException e) {
			fail("UC00: No instala modulo sin dependencias."+e);
		}
		
		// intento de reinstalar
		try {
			this.install.installModule(this.mod1);
			assertTrue("UC04:no insertado", this.install.getModules().contains(this.mod1) );
				
		} catch (DependencyViolationException e) {
			fail("UC00: No instala modulo sin dependencias."+e);
		}
		try {
			this.install.installModule(this.mod2);
			assertTrue("UC04:no insertado",this.install.getModules().contains(this.mod2));
			
			assertTrue("UC03:fallo cuenta de modulos.",this.install.getModules().size()==2);

			this.install.installModule(this.mod3);
		} catch (DependencyViolationException e) {
			fail("UC01:Debe satisfacer dependencias."+e);
		}
		
		Module moduleInstance = this.install.getModuleInstance(new ModuleImpl("LocalGIS Core", "1.2.0-beta"));
		assertTrue("UC02: No actualiza dependants.", moduleInstance.dependents().contains(this.mod2));

		ModuleImpl mod=new ModuleImpl("Module broken","1.1.1");
		mod.addDependency(new ModuleImpl("LocalGIS Core","1.1.0-beta")); //no satisface dependencia
		try {
			this.install.installModule(mod);
			fail("UC19: Dependencia rota no detectada"); 
		} catch (DependencyViolationException e) {
		
		}
		
	}

	public void testRemoveModule() throws DependencyViolationException, ModuleNotFoundException {
		
		testInstallModule();
		try {
			Module mod=this.install.getModuleInstance(new ModuleImpl("LocalGIS Core","1.2.0-beta"));
			Collection<Module>dep= mod.dependents();
			assertTrue(dep.size()!=0);
			this.install.removeModule(new ModuleImpl("LocalGIS Core","1.2.0-beta"));//tiene dependencias
			fail("UC20: Dependencia violada"); // TODO
		} catch (DependencyViolationException e) {
			
		}  
		try {
			this.install.removeModule(new ModuleImpl("Module broken","1.1.1"));//module no esta instalado
			fail("UC22: modulo no instalado no da excepcion");
		} catch (ModuleNotFoundException e) {
			
			
		} 
		
		try {
			this.install.removeModule(new ModuleImpl("ETRS89 Migration", "1.0.0-rc1"));//se puede borrar
		} catch (DependencyViolationException e) {
			fail("UC21: no borra modulo.");
		
		} 
	}

	public void testCanBeInstalled() throws DependencyViolationException, ModuleNotFoundException {
		testInstallModule();
		Module mod1=this.install.getModuleInstance(new ModuleImpl("ETRS89 Migration", "1.0.0-rc1"));
		mod1.addDependency(new ModuleImpl("LocalGIS Core","1.2.0-beta"));
		assertTrue("UC05: rastrea dependencias.",this.install.canBeInstalled(mod1));
			
		mod1.addDependency(new ModuleImpl("Workbench", "1.1.12-rc1"));
		assertTrue("UC06: implementa compatibilidad con major.minor.*",this.install.canBeInstalled(mod1));
		
		//  probar detección de incompatibilidad de cambio de version
		mod1.removeDependency(new ModuleImpl("Workbench", "1.1.12-rc1"));
		mod1.addDependency(new ModuleImpl("Workbench", "1.2.0"));
		assertFalse("UC06: implementa imcompatibilidad con major.minor.*",this.install.canBeInstalled(mod1));

		mod1.addDependency(new ModuleImpl("Non existent.","1.1.0-beta"));
		assertFalse("UC05: rastrea dependencias.",this.install.canBeInstalled(mod1)); 
			
		
	}

	public void testNeedToBeInstalled() throws DependencyViolationException, ModuleNotFoundException {
		testInstallModule();
		
		Module mod1=this.install.resolve(new ModuleImpl("New Module", "1.0.0-rc1"));
		mod1.addDependency(new ModuleImpl("LocalGIS Core","1.2.0-beta"));
		mod1.addDependency(new ModuleImpl("Workbench", "1.1.12-rc1"));
		mod1.addDependency(new ModuleImpl("Non existent.","1.1.0-beta"));
		mod1.addDependency(new ModuleImpl("GuiaUrbana.","1.0.0-beta"));
		
		
		
		Collection<Module> needed= this.install.needToBeInstalled(mod1,true);
		assertTrue("UC09: detección de dependencias no satisfechas.",needed.size()==2 &&
			needed.contains(new ModuleImpl("Non existent.","1.1.0-beta"))&&
			needed.contains(new ModuleImpl("GuiaUrbana.","1.0.0-beta"))
			);
				
	}
	public void testNeedToBeInstalledForPack() throws DependencyViolationException, ModuleNotFoundException {
		testInstallModule();
		
		//Example Pack
		Vector<Module> pack=new Vector<Module>();
		
		Module mod1=new ModuleImpl("New module 1","1.0.0");
		Module mod2=new ModuleImpl("new Library Module","1.0.0");
		pack.add(mod1);
		pack.add(mod2);
		
		mod1.addDependency(this.install.getModuleInstance(new ModuleImpl("LocalGIS Core","1.2.0-beta")));
		mod1.addDependency(mod2); // this dependency is provided in the pack
		
		Collection<Module> neededForPack=this.install.needToBeInstalledForPack(pack,true);
		assertTrue("Complete Pack not installable.",neededForPack.size()==0 );
		
		mod1.addDependency(new ModuleImpl("notExistent","1.0.0"));
		
		
		Collection<Module> needed=this.install.needToBeInstalledForPack(pack,true);
		
		
		assertTrue("Incomplete Pack installable.", needed.size()==1 );
		assertTrue("dependency not detected.",needed.contains(new ModuleImpl("notExistent","1.0.0")));
		
		mod2.addDependency(new ModuleImpl("newsubdependency","1.0.0"));
		
		needed=this.install.needToBeInstalledForPack(pack,true);
		
		assertTrue("Incomplete Pack installable.", needed.size()==2 );
		assertTrue("Deepth 2 Dependency not detected.",needed.contains(new ModuleImpl("newsubdependency","1.0.0")));
		
		
	}
}
/**
 * TestLocalGISInstallation.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
