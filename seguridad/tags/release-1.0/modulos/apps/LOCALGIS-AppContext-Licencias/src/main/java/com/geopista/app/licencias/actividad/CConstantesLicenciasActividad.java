package com.geopista.app.licencias.actividad;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.security.GeopistaPrincipal;
/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 13-oct-2004
 * Time: 12:44:05
 */
public class CConstantesLicenciasActividad {

   public static CPersonaJuridicoFisica persona = null;
   public static CPersonaJuridicoFisica representante = null;
   public static CPersonaJuridicoFisica tecnico = null;
   public static CPersonaJuridicoFisica promotor = null;
   
   /** Usuario de login */
   public static GeopistaPrincipal principal= new GeopistaPrincipal();
   
   /** Referencia Catastral Selecciona */
   public static CReferenciaCatastral referencia = new CReferenciaCatastral();

}
