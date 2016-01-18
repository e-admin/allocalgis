/**
 * ConstantesAlp_LCGIII.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.alptolocalgis.beans;

import java.text.DecimalFormat;
import java.util.Hashtable;
import com.geopista.app.alptolocalgis.AlpClient;
import com.geopista.security.GeopistaPrincipal;

/**
 * Clase utilizada por el cliente y el servidor que define las constantes de las acciones que el cliente desea hacer
 * en la parte servidora. En la parte servidora se comparara la accion obtenida y se realizara la accion deseada. El
 * resto de constantes son para permitir obtener los objetos de la hash params en el envio de la peticion al servidor
 * o utilzadas en el cliente y servidor.
 * */

public class ConstantesAlp_LCGIII
{
	public static GeopistaPrincipal principal;
    
    public static AlpClient clienteAlp = null;

}
