/**
 * AppContextMap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app;

import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.IAdministradorCartografiaClient;
import com.geopista.util.ApplicationContext;

/**
 * 
 * 
 * Información compartida en el transcurso de una ejecución de la aplicación.
 * Este contexto de aplicación lo utilizan las aplicaciones para comunicarse con sus partes.
 * Debe incluirse este contexto de aplicación en el contexto del editor para que los plugins
 * tengan acceso a los servicios de acceso a Geopista.
 * TODO: Asegurarse que el componente Editor puede incorporar el contexto de aplicación dentro del blackboard de su workbenchcontexto.
 *@author juacas
 **/
public class AppContextMap extends AppContext
{	
    
    /**
     * Obtiene un acceso al servidor de cartografia
     * @return
     */
    public IAdministradorCartografiaClient getClient()
    {
        String sUrlPrefix = getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL);
        return new AdministradorCartografiaClient(sUrlPrefix);
    }

    /**
     * Inicializa una instancia de {@link AppContextMap} como {@link AppContext} de la aplicación.
     * Contiene métodos adicionales de utilidad para los clientes de mapas, y que no son necesarios 
     * para los servidores.
     * 
     * @see AppContextMap
     * @see ApplicationContext
     * @see AppContext 
     */
	public static void initAppContextMap() 
	{
		AppContext.setHeartBeat(false);
    	AppContext.setApplicationContext(new AppContextMap());
    	((AppContext)AppContext.getApplicationContext()).initHeartBeat();
	}
    
    
}