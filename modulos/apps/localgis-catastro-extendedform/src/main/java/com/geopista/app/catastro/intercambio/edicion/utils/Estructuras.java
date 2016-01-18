/**
 * Estructuras.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.utils;

import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.ListaEstructuras;



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

