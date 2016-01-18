package com.geopista.app.patrimonio.estructuras;

import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.CConstantesComando;


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

/*
 * Created on 02-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author hgarcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Estructuras {
    private static boolean cargada=false;
    private static boolean iniciada=false;
    private static ListaEstructuras listaTiposAprovechamiento=new ListaEstructuras("Aprovechamiento");
    private static ListaEstructuras listaTiposPropiedad=new ListaEstructuras("Tipo de propiedad patrimonial");
    private static ListaEstructuras listaTiposConstruccion=new ListaEstructuras("Tipo de construcción");
    private static ListaEstructuras listaEstadosConservacion=new ListaEstructuras("Estado de conservación");
    private static ListaEstructuras listaTiposMaterial=new ListaEstructuras("Tipo de material constructivo");
    private static ListaEstructuras listaTiposCubierta= new ListaEstructuras("Tipo de cubierta");
    private static ListaEstructuras listaTiposCarpinteria= new ListaEstructuras("Tipo de carpintería");
    private static ListaEstructuras listaTiposViaIni=null;
    private static List domainsList=null;
    
    
    /**
     * @return Returns the domainsList.
     */
    public static List getDomainsList()
    {
        return domainsList;
    }
    /**
     * @param domainsList The domainsList to set.
     */
    public static void setDomainsList(List domainsList)
    {
        Estructuras.domainsList = domainsList;
    }
    /**
     * @return Returns the listaTiposViaIni.
     */
    public static ListaEstructuras getListaTiposViaIni()
    {
        return listaTiposViaIni;
    }
    /**
     * @param listaTiposViaIni The listaTiposViaIni to set.
     */
    public static void setListaTiposViaIni(ListaEstructuras listaTiposViaIni)
    {
        Estructuras.listaTiposViaIni = listaTiposViaIni;
    }
    /**
     * @return Returns the listaEstadosConservacion.
     */
    public static ListaEstructuras getListaEstadosConservacion()
    {
        return listaEstadosConservacion;
    }
    /**
     * @param listaEstadosConservacion The listaEstadosConservacion to set.
     */
    public static void setListaEstadosConservacion(
            ListaEstructuras listaEstadosConservacion)
    {
        Estructuras.listaEstadosConservacion = listaEstadosConservacion;
    }
    /**
     * @return Returns the listaTiposAprovechamiento.
     */
    public static ListaEstructuras getListaTiposAprovechamiento()
    {
        return listaTiposAprovechamiento;
    }
    /**
     * @param listaTiposAprovechamiento The listaTiposAprovechamiento to set.
     */
    public static void setListaTiposAprovechamiento(
            ListaEstructuras listaTiposAprovechamiento)
    {
        Estructuras.listaTiposAprovechamiento = listaTiposAprovechamiento;
    }
    /**
     * @return Returns the listaTiposCarpinteria.
     */
    public static ListaEstructuras getListaTiposCarpinteria()
    {
        return listaTiposCarpinteria;
    }
    /**
     * @param listaTiposCarpinteria The listaTiposCarpinteria to set.
     */
    public static void setListaTiposCarpinteria(ListaEstructuras listaTiposCarpinteria)
    {
        Estructuras.listaTiposCarpinteria = listaTiposCarpinteria;
    }
    /**
     * @return Returns the listaTiposConstruccion.
     */
    public static ListaEstructuras getListaTiposConstruccion()
    {
        return listaTiposConstruccion;
    }
    /**
     * @param listaTiposConstruccion The listaTiposConstruccion to set.
     */
    public static void setListaTiposConstruccion(ListaEstructuras listaTiposConstruccion)
    {
        Estructuras.listaTiposConstruccion = listaTiposConstruccion;
    }
    /**
     * @return Returns the listaTiposCubierta.
     */
    public static ListaEstructuras getListaTiposCubierta()
    {
        return listaTiposCubierta;
    }
    /**
     * @param listaTiposCubierta The listaTiposCubierta to set.
     */
    public static void setListaTiposCubierta(ListaEstructuras listaTiposCubierta)
    {
        Estructuras.listaTiposCubierta = listaTiposCubierta;
    }
    /**
     * @return Returns the listaTiposMaterial.
     */
    public static ListaEstructuras getListaTiposMaterial()
    {
        return listaTiposMaterial;
    }
    /**
     * @param listaTiposMaterial The listaTiposMaterial to set.
     */
    public static void setListaTiposMaterial(ListaEstructuras listaTiposMaterial)
    {
        Estructuras.listaTiposMaterial = listaTiposMaterial;
    } 
    /**
     * @return Returns the listaTiposPropiedad.
     */
    public static ListaEstructuras getListaTiposPropiedad()
    {
        return listaTiposPropiedad;
    }
    /**
     * @param listaTiposPropiedad The listaTiposPropiedad to set.
     */
    public static void setListaTiposPropiedad(ListaEstructuras listaTiposPropiedad)
    {
        Estructuras.listaTiposPropiedad = listaTiposPropiedad;
    }
    
    
   

     public static void cargarEstructuras()
    {
        iniciada=true;
        try
        {
            cargarLista(listaEstadosConservacion);
            cargarLista(listaTiposAprovechamiento);
            cargarLista(listaTiposCarpinteria);
            cargarLista(listaTiposConstruccion);
            cargarLista(listaTiposCubierta);
            cargarLista(listaTiposMaterial);
            cargarLista(listaTiposPropiedad);

        
        }catch(Exception e){}
        cargada=true;
    }
     //Este metodo cargara las estructuras segun una lista dada de Strings
     
     public static void cargarEstructuras(List lnameStrutura)
     {
         iniciada=true;
         try
         {
             Iterator nameStructIter = lnameStrutura.iterator();
             while(nameStructIter.hasNext()){
                 ListaEstructuras nameDomain=(ListaEstructuras) nameStructIter.next();
                 cargarLista(nameDomain);
                 setListaTiposViaIni(nameDomain);
             }

         
         }catch(Exception e){}
         cargada=true;
     }
    public static void cargarLista(ListaEstructuras lista)
    {
       AppContext app =(AppContext) AppContext.getApplicationContext();
       if (!lista.loadDB(app.getString("geopista.conexion.servidor"))) 
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


  
}

