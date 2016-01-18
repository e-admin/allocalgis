package com.geopista.app.metadatos.estructuras;

import com.geopista.app.metadatos.init.Constantes;
import com.geopista.protocol.ListaEstructuras;



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
 * Date: 22-jul-2004
 * Time: 16:59:52
 */
public class Estructuras {
    private static boolean cargada=false;
    private static boolean iniciada=false;
    private static ListaEstructuras listaRoles=new ListaEstructuras("CI_ROLECODE");
    private static ListaEstructuras listaFunctionCode=new ListaEstructuras("CI_ONLINEFUNCTIONCODE");
    private static ListaEstructuras listaLanguage=new ListaEstructuras("LANGUAGE");
    private static ListaEstructuras listaTopicCategory= new ListaEstructuras("MD_TOPICCATEGORYCODE");
    private static ListaEstructuras listaSpatialRepresentation= new ListaEstructuras("MD_SPATIALREPRESENTATIONTYPECODE");
    private static ListaEstructuras listaDateType= new ListaEstructuras("CI_DATETYPECODE");
    private static ListaEstructuras listaRestriction= new ListaEstructuras("MD_RESTRICTIONCODE");
    private static ListaEstructuras listaTipoInforme=new ListaEstructuras("DQ_SUBCLASSES");
    private static ListaEstructuras listaScopeCode=new ListaEstructuras("MD_SCOPECODE");
    private static ListaEstructuras listaReferenceSystem=new ListaEstructuras("MD_REFERENCESYSTEM");
    private static ListaEstructuras listaCapas=new ListaEstructuras("LAYERS");

    public static com.geopista.protocol.ListaEstructuras getListaLanguage() {
        return listaLanguage;
    }

    public static ListaEstructuras getListaFunctionCode() {
        return listaFunctionCode;
    }

    public static ListaEstructuras getListaRoles() {
        return listaRoles;
    }

    public static ListaEstructuras getListaTopicCategory() {
        return listaTopicCategory;
    }
    public static ListaEstructuras getListaSpatialRepresentation()
    {
        return listaSpatialRepresentation;
    }

    public static ListaEstructuras getListaDateType()
    {
        return listaDateType;
    }

    public static ListaEstructuras getListaRestriction()
            {
                return listaRestriction;
            }
    public static ListaEstructuras getListaTipoInforme()
            {
                return listaTipoInforme;
            }

    public static ListaEstructuras getListaScopeCode() {
        return listaScopeCode;
    }

    public static void setListaScopeCode(ListaEstructuras listaScopeCode) {
        Estructuras.listaScopeCode = listaScopeCode;
    }

    public static ListaEstructuras getListaReferenceSystem() {
        return listaReferenceSystem;
    }

    public static void setListaReferenceSystem(ListaEstructuras listaReferenceSystem) {
        Estructuras.listaReferenceSystem = listaReferenceSystem;
    }

    public static ListaEstructuras getListaCapas() {
        return listaCapas;
    }

    public static void setListaCapas(ListaEstructuras listaCapas) {
        Estructuras.listaCapas = listaCapas;
    }

    public static void cargarEstructuras()
    {
        iniciada=true;
        try
        {
            cargarCapas(listaCapas);
            cargarLista(listaRoles);
            cargarLista(listaFunctionCode);
            cargarLista(listaLanguage);
            cargarLista(listaTopicCategory);
            cargarLista(listaSpatialRepresentation);
            cargarLista(listaDateType);
            cargarLista(listaRestriction);
            cargarLista(listaTipoInforme);
            cargarLista(listaScopeCode);
            cargarLista(listaReferenceSystem);

        }catch(Exception e){}
        cargada=true;
    }
    public static void cargarLista(ListaEstructuras lista)
    {
        if (Constantes.CARGAR_DE_DB==1 && com.geopista.security.SecurityManager.isLogged())
        {
            if (!lista.loadDB(com.geopista.app.metadatos.init.Constantes.url))
               lista.loadFile();
        }
        else
            lista.loadFile();
        lista.saveFile();
    }

    public static boolean isCargada() {
        return cargada;
    }

    public static void setCargada(boolean cargada) {
        Estructuras.cargada = cargada;
    }

    public static boolean isIniciada() {
        return iniciada;
    }

    public static void setIniciada(boolean iniciada) {
        Estructuras.iniciada = iniciada;
    }

    public static void cargarCapas(ListaEstructuras lista)
    {
        if (Constantes.CARGAR_DE_DB==1 && com.geopista.security.SecurityManager.isLogged())
        {
           if (!lista.loadCapas(Constantes.url))
               lista.loadFile();
        }
        else
            lista.loadFile();
        lista.saveFile();
    }
}
