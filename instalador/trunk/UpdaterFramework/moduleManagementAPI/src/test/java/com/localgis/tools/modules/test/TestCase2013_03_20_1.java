/**
 * TestCase2013_03_20_1.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.XMLModuleSerializer;

public class TestCase2013_03_20_1
{
    private static final Log LOGGER=LogFactory.getLog(TestCase2013_03_20_1.class);

    private ModuleDependencyTree registry;
    private Module demoMod;

    @Before
    public void setUp() throws Exception
    {
	InputStream is = this.getClass().getClassLoader().getResourceAsStream("testCase2013-05-20-1/pack.xml");
	this.registry= XMLModuleSerializer.getModuleDependencyTreeFromXMLStream(is);
	is = this.getClass().getClassLoader().getResourceAsStream("testCase2013-05-20-1/demoModule.xml");
	this.demoMod= XMLModuleSerializer.getModuleFromXMLStream(is);
    }
/**
  |- Modulo de EIEL de LOCALGIS 3.0:3.0.0 (1600999872)
  |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-EIEL:3.0.0 (34751794)
  |  |- Modulo agrupador: Instalacion basica cliente jnlp:3.0.0 (2105333421)
  |     |- Modulo agrupador: Instalacion basica sys server:3.0.0 (250755128)
  |     |- Modulo Software:3.0.0 (1078520882)
  |        |- Modulo Registro Instalacion:3.0.0 (1938483094)
  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0 (610952221)
  |  |- Modulo Registro Instalacion:3.0.0 (1938483094)
  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-EIEL:3.0.0 (34751794)
  |- Modulo agrupador: Instalacion basica cliente jnlp:3.0.0 (2105333421)
  |  |- Modulo agrupador: Instalacion basica sys server:3.0.0 (250755128)
  |  |- Modulo Software:3.0.0 (1078520882)
  |     |- Modulo Registro Instalacion:3.0.0 (1938483094)
  |- Modulo Registro Instalacion:3.0.0 (1938483094)
  |- Modulo de Administracion de LOCALGIS:3.0.0 (1154662040)
  |  |- Modulo agrupador: Instalacion basica cliente jnlp:3.0.0 (2105333421)
  |  |  |- Modulo agrupador: Instalacion basica sys server:3.0.0 (250755128)
  |  |  |- Modulo Software:3.0.0 (1078520882)
  |  |     |- Modulo Registro Instalacion:3.0.0 (1938483094)
  |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0 (610952221)
  |     |- Modulo Registro Instalacion:3.0.0 (1938483094)
  |- Modulo Software:3.0.0 (1078520882)
  |  |- Modulo Registro Instalacion:3.0.0 (1938483094)
  |- Modulo agrupador: Instalacion basica sys server:3.0.0 (250755128)

 *  
 *  Fallo de prueba:
 *  [Modulo Registro Instalacion:3.0.0,
 *   Modulo Software:3.0.0, 
 *   Modulo agrupador: Instalacion basica sys server:3.0.0, 
 *   Modulo agrupador: Instalacion basica cliente jnlp:3.0.0,
 *   Modulo de Administracion de LOCALGIS:3.0.0, 
 *   Modulo instalacion/actualizacion LOCALGIS-SERVER-EIEL:3.0.0,
 *   Modulo de EIEL de LOCALGIS 3.0:3.0.0,
 *   Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0 <=== TIENE QUE SER PREVIO A Modulo de Administracion de LOCALGIS:3.0.0
 *  ]
 *  
 *  ImplementaciÃ³n actual:
 *  
 *  [Modulo Registro Instalacion:3.0.0,
 *  Modulo Software:3.0.0, 
 *  Modulo agrupador: Instalacion basica sys server:3.0.0, 
 *  Modulo agrupador: Instalacion basica cliente jnlp:3.0.0, 
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-EIEL:3.0.0, 
 *  Modulo de EIEL de LOCALGIS 3.0:3.0.0, 
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0, 
 *  Modulo de Administracion de LOCALGIS:3.0.0]
 *  
 * 
 */
    @Test
    public void testDependencyOrdering()
    {
	List<Module> modules = new ArrayList<Module>(this.registry.getModules());
	System.out.println("=========== DEPENDENCIES ==========================================");
	TestUtils.dumpDependencyTree(this.registry.getModules(), 0,null,null);
	System.out.println("=========== DEPENDENTS ==========================================");
	Collection<Module> independentModules = this.registry.getIndependentModules();
	if (independentModules.size()>1)
	    {
		LOGGER.warn("WARNING!! Dependency tree has no unique root. Check your design!!");
	    }
	TestUtils.dumpDependentTree(independentModules, 0,null);
	List<Module> ordered = this.registry.orderModulesByDependencies(modules);
	String string = ordered.toString();
	LOGGER.debug("Proposed order:"+string);
	Assert.assertTrue(TestUtils.checkDependencyOrdered(ordered));
//	assertEquals("[Modulo Registro Instalacion:3.0.0, Modulo Software:3.0.0, Modulo agrupador: Instalacion basica sys server:3.0.0, Modulo agrupador: Instalacion basica cliente jnlp:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-EIEL:3.0.0, Modulo de EIEL de LOCALGIS 3.0:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0, Modulo de Administracion de LOCALGIS:3.0.0]",string);
    }
   
}
