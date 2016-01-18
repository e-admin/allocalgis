/**
 * Estructuras.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades.estructuras;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.protocol.ListaEstructuras;



public class Estructuras {
    private static boolean cargada=false;
    private static boolean iniciada=false;
    
    private static ListaEstructuras listaTipos=null;
    private static List domainsList=null;
    
    
    private static HashMap<String,ListaEstructuras> cachelistaEstructuras=new HashMap<String,ListaEstructuras>();
    
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
     * @return Returns the listaTipos previously loaded
     */
    public static ListaEstructuras getListaTipos()
    {
        return listaTipos;
    }
    /**
     * @param listaTiposViaIni The listaTiposVia to set.
     */
    public static void setListaTipos(ListaEstructuras listaTipos)
    {
        Estructuras.listaTipos = listaTipos;
    }
    
    
    
    public static void cargarEstructuras()
    {
        iniciada=true;
        try
        {            
            cargarLista(listaTipos);
            
            
        }catch(Exception e){}
        cargada=true;
    }
    //Este metodo cargara las estructuras segun una lista dada de Strings
    
    public static void cargarEstructura (String nombreEstructura,boolean useCache)
    {
    	
       if (useCache){
    	   ListaEstructuras lstTipos=cachelistaEstructuras.get(nombreEstructura);
    	   if (lstTipos!=null){
    		   listaTipos = lstTipos;
    		   return;
    	   }
       }
       ListaEstructuras lstTipos=new ListaEstructuras(nombreEstructura);
       iniciada=true;
       try
       {            
           cargarLista(lstTipos);
           listaTipos = lstTipos;  
           if (useCache)
        	   cachelistaEstructuras.put(nombreEstructura,listaTipos);
       }catch(Exception e){}
       cargada=true;
    }
    
    public static ListaEstructuras getEstructuraFromCache(String nombreEstructura){
  	   return cachelistaEstructuras.get(nombreEstructura);
    }
    
    public static void setEstructura (String nombreEstructura,ListaEstructuras lstTipos)
    {
	   cachelistaEstructuras.put(nombreEstructura,lstTipos);	          
    }

    public static void cargarEstructura (String nombreEstructura)
    {
      cargarEstructura(nombreEstructura,true);
    }
    
    public static void cargarEstructuras(List lstEstructuras)
    {
        iniciada=true;
        try
        {
            Iterator nameStructIter = lstEstructuras.iterator();
            while(nameStructIter.hasNext()){
                ListaEstructuras nameDomain=(ListaEstructuras) nameStructIter.next();
                cargarLista(nameDomain);
                setListaTipos(nameDomain);
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

