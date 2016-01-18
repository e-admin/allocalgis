/**
 * GeopistaPermission.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security;
import java.io.Serializable;
import java.security.acl.Permission;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 07-may-2004
 * Time: 14:06:33
 * To change this template use Options | File Templates.
 */
public class GeopistaPermission implements Permission, Serializable {

    private String name;

    public GeopistaPermission() {
    }

    public GeopistaPermission(String sNombre)
    {
        name=sNombre;
    }

    public String getName() {
           return name;
       }

    public void setName(String name) {
         this.name = name;
     }

    public int hashCode()
    {
        return getName().hashCode();
    }

    public String getActions() {
        return null;
    }

    public boolean equals( Object obj )
    {
        if (obj==null) return false;
        if( ! ( obj.getClass().equals( GeopistaPermission.class ) ) )
                return false;
        //System.out.println(((GeopistaPermission)obj).getName());
        return this.getName().equalsIgnoreCase(((GeopistaPermission)obj).getName());

    }

    public boolean implies( Permission permission )
    {
        if( ! ( permission.getClass().equals( GeopistaPermission.class ) ) )
                    return false;
            return this.getName().equals(((GeopistaPermission)permission).getName());
    }

    public static String LEER_CAPA="Geopista.Layer.Leer";
    public static String ESCRIBIR_CAPA="Geopista.Layer.Escribir";
    public static String VER_ADMINITRACION="Geopista.Administracion.View";
    public static String EDITAR_ADMINITRACION="Geopista.Administracion.Edit";
    public static String ADMINISTRACION_VER_SESIONES="Geopista.Administracion.VerSesiones";
    public static String EDIT_USER_DATA_ADMINITRACION="Geopista.Administracion.EditUserData";
    public static String VER_METADATOS="Geopista.Metadatos.View";
    public static String EDITAR_METADATOS="Geopista.Metadatos.Edit";
    public static String VER_CONTAMINANTES="Geopista.Contaminantes.View";
    public static String EDITAR_CONTAMINANTES="Geopista.Contaminantes.Edit";
    public static String PUBLICAR_DOCUMENTO="Geopista.Documento.Publicar";
    public static String MODIFICAR_CAPAS_LOCALES="Geopista.Modificar.CapasLocales";
    public static String MODIFICAR_CAPAS_GLOBALES="Geopista.Modificar.CapasGlobales";
    public static String PUBLICAR_DOCUMENTO_SIN_FEATURE="Geopista.DocumentoSinFeature.Publicar";

}
