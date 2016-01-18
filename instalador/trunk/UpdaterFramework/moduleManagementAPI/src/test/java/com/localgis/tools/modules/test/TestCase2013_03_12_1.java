/**
 * TestCase2013_03_12_1.java
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

public class TestCase2013_03_12_1
{
    private static final Log LOGGER=LogFactory.getLog(TestCase2013_03_12_1.class);

    private ModuleDependencyTree registry;

    @Before
    public void setUp() throws Exception
    {
	InputStream is = this.getClass().getClassLoader().getResourceAsStream("testCase2013-05-12-1/pack.xml");
	this.registry= XMLModuleSerializer.getModuleDependencyTreeFromXMLStream(is);
    }
/**
 *|- Metamodulo instalacion modulos localgis de COTESA:3.0.0 (1002799575)
 *|  |- Modulo backup de LOCALGIS 3.0:3.0.0 (450678213)
 *|  |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Buckup:3.0.0 (373902922)
 *|  |  |  |- Modulo Registro Instalacion:3.0.0 (649258932)
 *|  |  |- Modulo agrupador: Instalacion basica cliente jnlp:3.0.0 (730459717)
 *|  |  |  |- Modulo agrupador: Instalacion basica sys server:3.0.0 (1819937367)
 *|  |  |  |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0 (1077742948)
 *|  |  |  |  |  |- Modulo Registro Instalacion:3.0.0 (649258932)
 *|  |  |  |  |- Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0 (1748648238)
 *|  |  |  |  |  |- Modulo Registro Instalacion:3.0.0 (649258932)
 *|  |  |  |- Modulo Software:3.0.0 (1217226871)
 *|  |  |  |  |- Modulo Registro Instalacion:3.0.0 (649258932)
 * 
 * Secuencia valida:
 *  Modulo Registro Instalacion:3.0.0
 *  Modulo Software:3.0.0
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-Buckup:3.0.0
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0
 *  Modulo agrupador: Instalacion basica sys server:3.0.0
 *  Modulo agrupador: Instalacion basica cliente jnlp:3.0.0
 *  Modulo backup de LOCALGIS 3.0:3.0.0
 *  Metamodulo instalacion modulos localgis de COTESA:3.0.0
 *  
 *  Secuencia VÃ¡lida:
 *  
 *  [Modulo Registro Instalacion:3.0.0, 
 *  Modulo Software:3.0.0, 
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0, 
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0, 
 *  Modulo agrupador: Instalacion basica sys server:3.0.0, 
 *  Modulo agrupador: Instalacion basica cliente jnlp:3.0.0, 
 *  Modulo instalacion/actualizacion LOCALGIS-SERVER-Buckup:3.0.0, 
 *  Modulo backup de LOCALGIS 3.0:3.0.0, 
 *  Metamodulo instalacion modulos localgis de COTESA:3.0.0]
 *  
 *  Alternativa:
 *  Modulo Registro Instalacion:3.0.0
 *  	Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0
 *  	Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0
 *  			Modulo instalacion/actualizacion LOCALGIS-SERVER-Buckup:3.0.0
 *  		Modulo agrupador: Instalacion basica sys server:3.0.0
 *  		Modulo Software:3.0.0
 *  			Modulo agrupador: Instalacion basica cliente jnlp:3.0.0
 *  				 Modulo backup de LOCALGIS 3.0:3.0.0
 *  					Metamodulo instalacion modulos localgis de COTESA:3.0.0
 */
    @Test
    public void testDependencyOrdering()
    {
	List<Module> modules = new ArrayList<Module>(this.registry.getModules());
	TestUtils.dumpDependencyTree(this.registry.getModules(), 0,null,null);
//	System.out.println("================================================================");
	//dumpDependentTree(this.registry.getModules(), 0,null);
	List<Module> ordered = this.registry.orderModulesByDependencies(modules);
	String string = ordered.toString();
	LOGGER.debug("Proposed order:"+string);
	Assert.assertTrue(TestUtils.checkDependencyOrdered(ordered));
	
	//assertEquals("[Modulo Registro Instalacion:3.0.0, Modulo Software:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Buckup:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Geopista:3.0.0, Modulo instalacion/actualizacion LOCALGIS-SERVER-Principal:3.0.0, Modulo agrupador: Instalacion basica sys server:3.0.0, Modulo agrupador: Instalacion basica cliente jnlp:3.0.0, Modulo backup de LOCALGIS 3.0:3.0.0, Metamodulo instalacion modulos localgis de COTESA:3.0.0]",string);
    }
   
}
