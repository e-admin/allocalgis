package com.geopista.app.catastro.intercambio.edicion.utils;

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



public class Estructuras 
{
    private static boolean cargada=false;
    private static boolean iniciada=false;
    
    private static ListaEstructuras listaTipos;
    
    
    public Estructuras (String parentDomain)
    {
        listaTipos=new ListaEstructuras(parentDomain);
    }
    
    public static ListaEstructuras getListaTipos() {
        return listaTipos;
    }
    
    public void cargarEstructuras()
    {
        iniciada=true;
        try
        {
            cargarLista(listaTipos);
            
        }catch(Exception e){}
        
        cargada=true;
    }
    public static void cargarLista(ListaEstructuras lista)
    {
        if (!lista.loadDB(CConstantesComando.loginLicenciasUrl))
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

