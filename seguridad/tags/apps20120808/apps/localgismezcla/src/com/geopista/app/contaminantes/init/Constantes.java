package com.geopista.app.contaminantes.init;

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

}
