/**
 * COperacionesOcupaciones.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.ocupaciones;

import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.net.EnviarSeguro;

public class COperacionesOcupaciones extends COperacionesLicencias {

	public static CResultadoOperacion enviar(String text, Vector vAnexos) throws Exception{
		return EnviarSeguro.enviar(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.OCUPACIONES_WEBAPP_NAME +
	            ServletConstants.CSERVLETOCUPACIONES_SERVLET_NAME, text, vAnexos);
	}
	
}
