package com.geopista.app.contaminantes.estructuras;

import com.geopista.protocol.ListaEstructuras;
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
 * Date: 01-oct-2004
 * Time: 11:20:53
 */
public class Estructuras {
    private static boolean cargada=false;
    private static boolean iniciada=false;
    private static ListaEstructuras listaFormatosSalida= new ListaEstructuras("FORMATOS_SALIDA_CONTAMINANTES");
    private static ListaEstructuras listaRazonEstudio=new ListaEstructuras("RAZON_ESTUDIO");
    private static ListaEstructuras listaTipo=new ListaEstructuras("TIPO_ACTIVIDAD_CONTAMINANTE");
    private static ListaEstructuras listaTipoVia=new ListaEstructuras("Tipos de via normalizados del INE");
    private static ListaEstructuras listaTipoAnexos=new ListaEstructuras("TIPO_ANEXOS_CONTAMINANTE");
    private static ListaEstructuras listaTipoAccion=new ListaEstructuras("TIPO_ACCION");
    private static ListaEstructuras listaTipoMedioambiental=new ListaEstructuras("TIPO_MEDIOAMBIENTAL");
    private static ListaEstructuras listaTipoZonaArbolada=new ListaEstructuras("TIPO_ZONA_ARBOLADA");



     public static void cargarEstructuras()
    {
        iniciada=true;
        try
        {
            cargarLista(listaFormatosSalida);
            cargarLista(listaRazonEstudio);
            cargarLista(listaTipo);
            cargarLista(listaTipoVia);
            cargarLista(listaTipoAnexos);
            cargarLista(listaTipoAccion);
            cargarLista(listaTipoMedioambiental);
            cargarLista(listaTipoZonaArbolada);
        }catch(Exception e){}
        cargada=true;
    }
    public static void cargarLista(ListaEstructuras lista)
    {
       if (!lista.loadDB(com.geopista.app.contaminantes.init.Constantes.url))
               lista.loadFile();
       else
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

    public static ListaEstructuras getListaRazonEstudio() {
        return listaRazonEstudio;
    }

    public static void setListaRazonEstudio(ListaEstructuras listaRazonEstudio) {
        Estructuras.listaRazonEstudio = listaRazonEstudio;
    }

    public static ListaEstructuras getListaTipo() {
        return listaTipo;
    }

    public static void setListaTipo(ListaEstructuras listaTipo) {
        Estructuras.listaTipo = listaTipo;
    }

    public static void setListaFormatosSalida(ListaEstructuras listaFormatosSalida) {
        Estructuras.listaFormatosSalida= listaFormatosSalida;
    }

    public static ListaEstructuras getListaFormatosSalida() {
        return listaFormatosSalida;
    }

    public static void setListaTipoAnexos(ListaEstructuras listaTipoAnexos) {
        Estructuras.listaTipoAnexos= listaTipoAnexos;
    }

    public static ListaEstructuras getListaTipoAnexos() {
        return listaTipoAnexos;
    }

    public static ListaEstructuras getListaTipoVia() {
        return listaTipoVia;
    }

    public static void setListaTipoVia(ListaEstructuras listaTipoVia) {
        Estructuras.listaTipoVia = listaTipoVia;
    }

    public static ListaEstructuras getListaTipoAccion() {
        return listaTipoAccion;
    }
    public static ListaEstructuras getListaTipoMedioambiental() {
        return listaTipoMedioambiental;
    }
     public static ListaEstructuras getListaTipoZonaArbolada() {
        return listaTipoZonaArbolada;
    }
}

