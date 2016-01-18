/**
 * Constantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes;

import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.security.GeopistaPrincipal;
/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 13-oct-2004
 * Time: 12:44:05
 */
public class Constantes {
    public static String url;
    public static int timeout=15000;
    public static String Provincia ;
	public static String Municipio ;
    public static int idMunicipio;
    public static String Locale="es_ES";
    public static final String LOCALE_CATALAN="ca_ES";
    public static final String LOCALE_CASTELLANO="es_ES";
    public static final String LOCALE_EUSKEDA="eu_ES";
    public static final String LOCALE_VALENCIANO="va_ES";
    public static final String LOCALE_GALLEGO="ga_ES";
    /** formatos de salida */
    public static int formatoPDF= 0;
    public static int formatoTEXTO= 1;
    public static int formatoPREVIEW= 2;
    /** Informes fuente de datos BD */
    public static String DriverClassName;
    public static String DBPassword;
    public static String DBUser;
    public static String ConnectionInfo;
    public static String helpSetHomeID= "geopistaIntro";

    /** Tamanno Total maximo permitido en los anexos */
    public static long totalMaxSizeFilesUploaded= 1024 * 1000; // 1M
    //** Tipos de actividad medioambiental
    public static final int CONTAMINANTES=0;
    public static final int VERTEDEROS=1;
    public static final int ARBOLEDAS=2;
    //** Tipos de acciones
   public static final int ADD=0;
   public static final int MODIFICAR=2;
   public static final int BORRAR=1;
   public static final int ANOTAR=3;
   
   public static CPersonaJuridicoFisica persona = null;
   public static CPersonaJuridicoFisica representante = null;
   public static CPersonaJuridicoFisica tecnico = null;
   public static CPersonaJuridicoFisica promotor = null;
   
   /** Usuario de login */
   public static GeopistaPrincipal principal= new GeopistaPrincipal();
   
   /** Referencia Catastral Selecciona */
   public static CReferenciaCatastral referencia = new CReferenciaCatastral();

}
