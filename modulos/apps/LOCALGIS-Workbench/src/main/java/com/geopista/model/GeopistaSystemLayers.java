/**
 * GeopistaSystemLayers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;
import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
public class GeopistaSystemLayers 
{
   //Constante que contiene el sysmtemId de la capa parcelas
  static ApplicationContext appContext=AppContext.getApplicationContext();
  public static final String PARCELAS = appContext.getString("lyrParcelas");
  public static final String NUMEROSPOLICIA = appContext.getString("lyrNumerosPolicia");
  public static final String INMUEBLES =  appContext.getString("lyrInmuebles");
  public static final String TRAMOSVIAS =  appContext.getString("lyrTramosVia");
  public static final String VIAS =  appContext.getString("lyrVias");
  public static final String AMBITOSGESTION =  appContext.getString("lyrAmbitosGestion");
 
  public GeopistaSystemLayers()
  {
  }
}