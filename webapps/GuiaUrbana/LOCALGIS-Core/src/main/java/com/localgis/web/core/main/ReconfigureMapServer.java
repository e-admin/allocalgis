/**
 * ReconfigureMapServer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.main;

import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.LocalgisManagerBuilderFactory;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisWMSException;

/**
 * Programa principal para reconfigurar el Map Server a partir del contenido de
 * la base de datos
 * 
 * @author albegarcia
 * 
 */
public class ReconfigureMapServer {

    /**
     * Punto de entrada a la aplicación
     * @param args Argumentos
     */
    public static void main(String[] args) {
        try {
            LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderFactory.createLocalgisManagerBuilder();
            localgisManagerBuilder.getLocalgisMapsConfigurationManager().reconfigureWMSServer();
        } catch (LocalgisConfigurationException e) {
            System.err.println("Error de configuración al obtener una instancia de LocalgisManager.");
            e.printStackTrace();
        } catch (LocalgisInitiationException e) {
            System.err.println("Error de inicialización al obtener una instancia de LocalgisManager.");
            e.printStackTrace();
        } catch (LocalgisDBException e) {
            System.err.println("Error de base de datos.");
            e.printStackTrace();
        } catch (LocalgisWMSException e) {
            System.err.println("Error al configurar el servidor de mapas.");
            e.printStackTrace();
        }
    }

}
