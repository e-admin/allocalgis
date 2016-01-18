/**
 * LocalGISInstallation.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.install;

import java.io.IOException;
import java.net.URL;

import com.localgis.tools.modules.ModuleDependencyTree;

/**
 * Representa una instalación de LocalGIS
 * Modela el servidor central con todos sus componentes
 * @author juacas
 *
 */
public interface LocalGISInstallation extends ModuleDependencyTree
{

/**
 * Conecta y carga la información de un servidor de LocalGIS
 * @param localGISLocation
 * @param username
 * @param password
 * @return 
 */
public void connect(URL localGISLocation, String username, String password) throws IOException;

public void close();

}
