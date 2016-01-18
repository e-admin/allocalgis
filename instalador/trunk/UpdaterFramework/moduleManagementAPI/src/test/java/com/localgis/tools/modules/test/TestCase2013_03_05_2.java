/**
 * TestCase2013_03_05_2.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.XMLModuleSerializer;

public class TestCase2013_03_05_2
{
    private static final Log LOGGER=LogFactory.getLog(TestCase2013_03_05_2.class);
    private ModuleDependencyTree registry;

    @Before
    public void setUp() throws Exception
    {
	InputStream is = this.getClass().getClassLoader().getResourceAsStream("testCase2013-05-03-2/pack.xml");
	this.registry= XMLModuleSerializer.getModuleDependencyTreeFromXMLStream(is);
    }
/**
 * |- Modulo de Administracion de LOCALGIS:3.0.0 (908671785)
 * |  |- Agrupador modulos instalacion basica sys server:3.0.0 (1752207528)
 * |  |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0 (1143035249)
 * |  |  |  |- Modulo Registro Instalacion:3.0.0 (849286339)
 * |  |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0 (706763928)
 * |  |  |  |- Modulo Registro Instalacion:3.0.0 (849286339)
 * |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0 (1779181611)
 * |  |  |- Modulo Registro Instalacion:3.0.0 (849286339)
 * |  |- Modulo Software:3.0.0 (1290476678)
 * |  |  |- Modulo Registro Instalacion:3.0.0 (849286339)
 * 
 *
 *Secuencia correcta:
 *Modulo Registro Instalacion:3.0.0, 
 *		Modulo Software:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0,
 *	Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0,
 *		Agrupador modulos instalacion basica sys server:3.0.0, 
 *			Modulo de Administracion de LOCALGIS:3.0.0
 *
 *Alternativa:
 *Modulo Registro Instalacion:3.0.0,
 *	Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0,
 *		Agrupador modulos instalacion basica sys server:3.0.0, Modulo Software:3.0.0, 
 *			Modulo de Administracion de LOCALGIS:3.0.0]
 */
    @Test
    public void testDependencyOrdering()
    {
	List<Module> modules = new ArrayList<Module>(this.registry.getModules());
	TestUtils.dumpDependencyTree(this.registry.getModules(), 0,null,null);
	List<Module> ordered = this.registry.orderModulesByDependencies(modules);
	String string = ordered.toString();
	LOGGER.debug("Proposed order:"+string);
//	assertEquals("[Modulo Registro Instalacion:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0, Agrupador modulos instalacion basica sys server:3.0.0, Modulo Software:3.0.0, Modulo de Administracion de LOCALGIS:3.0.0]",string);
//	assertEquals("[Modulo Registro Instalacion:3.0.0, Modulo Software:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0, Agrupador modulos instalacion basica sys server:3.0.0, Modulo de Administracion de LOCALGIS:3.0.0]",string);
//	assertEquals("[Modulo Registro Instalacion:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Administracion:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0, Agrupador modulos instalacion basica sys server:3.0.0, Modulo Software:3.0.0, Modulo de Administracion de LOCALGIS:3.0.0]",string);
	Assert.assertTrue(TestUtils.checkDependencyOrdered(ordered));
    }
   
}
