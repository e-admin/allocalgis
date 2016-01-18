/**
 * CConstantesLicencias_LCGIII.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias;

import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.security.GeopistaPrincipal;

public class CConstantesLicencias_LCGIII {

    public static CPersonaJuridicoFisica persona = null;
    public static CPersonaJuridicoFisica representante = null;
    public static CPersonaJuridicoFisica tecnico = null;
    public static CPersonaJuridicoFisica promotor = null;

    /** Usuario de login */
    public static GeopistaPrincipal principal= new GeopistaPrincipal();

    /** Referencia Catastral Selecciona */
    public static CReferenciaCatastral referencia = new CReferenciaCatastral();

}