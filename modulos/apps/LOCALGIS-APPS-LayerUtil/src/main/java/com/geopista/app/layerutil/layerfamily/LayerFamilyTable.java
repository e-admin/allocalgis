/**
 * LayerFamilyTable.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layerfamily;


/**
 * Bean con los datos de una layerfamily, de acuerdo a los datos contenidos en la
 * tabla <code>layerfamilies</code> de la base de datos geopista
 * 
 * @author cotesa
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.model.LayerFamily;


public class LayerFamilyTable
{
    
    private LayerFamily layerFamily = new LayerFamily();
    private Hashtable htNombre = new Hashtable();
    private Hashtable htDescripcion = new Hashtable();
    private HashMap hmPosLayers = new HashMap();
    
    /**
     * Constructor por defecto de la clase
     *
     */
    public LayerFamilyTable()
    {
        super();       
    }
     
    /**
     * Constructor de la clase
     * 
     * @param layerFamily Objeto de tipo com.geopista.model.LayerFamily
     * @param hmPosLayers Tabla de posiciones y capas contenidas en la layerfamily
     * @param htNombre Nombres de la layerfamily en los diferentes idiomas disponibles en el sistema
     * @param htDescripcion Descripciones de la layerfamily en los diferentes idiomas disponibles en el sistema
     */
    public LayerFamilyTable (LayerFamily layerFamily, HashMap hmPosLayers, Hashtable htNombre, Hashtable htDescripcion)
    {
        this.layerFamily = layerFamily;
        this.hmPosLayers = hmPosLayers;
        this.htNombre = htNombre;
        this.htDescripcion = htDescripcion;
    }
    
    /**
     * Constructor de la clase
     * @param layerFamily Objeto de tipo com.geopista.model.LayerFamily
     * @param hmPosLayers Tabla de posiciones y capas contenidas en la layerfamily
     */
    public LayerFamilyTable (LayerFamily layerFamily, HashMap hmPosLayers)
    {
        this.layerFamily = layerFamily;
        this.hmPosLayers = hmPosLayers;
        
    }
    
    /**
     * Constructor de la clase
     * @param layerFamily Objeto de tipo com.geopista.model.LayerFamily
     * @param setlayers Conjunto de capas de la layerfamily
     */
    public LayerFamilyTable (LayerFamily layerFamily, Set setlayers)
    {
        this.layerFamily = layerFamily;
        
        Iterator it = setlayers.iterator();
        int i = 0;
        while (it.hasNext())
        {   
            this.hmPosLayers.put(new Integer(++i), (LayerTable)it.next());
            
        }
        
    }
    
    /**
     * @return Returns the htDescripcion.
     */
    public Hashtable getHtDescripcion()
    {
        return htDescripcion;
    }
        
    /**
     * @param htDescripcion The htDescripcion to set.
     */
    public void setHtDescripcion(Hashtable htDescripcion)
    {
        this.htDescripcion = htDescripcion;
    }
        
    /**
     * @return Returns the htNombre.
     */
    public Hashtable getHtNombre()
    {
        return htNombre;
    }
        
    /**
     * @param htNombre The htNombre to set.
     */
    public void setHtNombre(Hashtable htNombre)
    {
        this.htNombre = htNombre;
    }
        
   
    /**
     * @return Returns the layerFamily.
     */
    public LayerFamily getLayerFamily()
    {
        return layerFamily;
    }
        
    /**
     * @param layerFamily The layerFamily to set.
     */
    public void setLayerFamily(LayerFamily layerFamily)
    {
        this.layerFamily = layerFamily;
    }

    /**
     * @return Returns the hmPosLayers.
     */
    public HashMap getHmPosLayers()
    {
        return hmPosLayers;
    }

    /**
     * @param hmPosLayers The hmPosLayers to set.
     */
    public void setHmPosLayers(HashMap hmPosLayers)
    {
        this.hmPosLayers = hmPosLayers;
    }

    /**
     * Añade una LayerTable en la última posición de las LayerTables de una LayerFamilyTable
     * @param layertable LayerTable a añadir
     */
    public void addLayer(LayerTable layertable)
    {        
        this.hmPosLayers.put(new Integer(this.hmPosLayers.size()+1), layertable);
    }
    
    /**
     * Elimina una LayerTable de la lista de LayerTables de la LayerFamilyTable
     * @param layertable LayerTable a eliminar
     */
    public void removeLayer (LayerTable layertable)
    {

        Iterator itValores = this.hmPosLayers.values().iterator();
        Iterator itClaves = this.hmPosLayers.keySet().iterator();

        while (itClaves.hasNext()) 
        {  
            Object value = itValores.next();
            Object clave = itClaves.next();
            
            if ((LayerTable)value == layertable)
                this.hmPosLayers.remove(clave);
        }
        
    }
    
    /**
     * Obtiene la lista de LayerTables de la LayerFamilyTable
     * @return Lista de LayerTables de la LayerFamilyTable
     */
    public List getListaLayerTable()
    {
        int tam = hmPosLayers.size();
        
        if (tam >0)
        {
            ArrayList lstCapas = new ArrayList(tam);
            for (int i = 0 ; i< tam; i++)
            {
                lstCapas.add(i, new LayerTable());
            }
            
            Iterator itValores = this.hmPosLayers.values().iterator();
            Iterator itClaves = this.hmPosLayers.keySet().iterator();
            
            while (itClaves.hasNext()) 
            {  
                LayerTable value = (LayerTable)itValores.next();
                Integer clave = (Integer)itClaves.next();
                lstCapas.set(clave.intValue()-1, value);
                
            }
            return lstCapas;
        }
        return null;
        
    }
    /**
     * Especifica la lista de LayerTables de la LayerFamilyTabla
     * @param lstLayers Lista de LayerTables
     */
    public void setListaLayerTable (List lstLayers)
    {
        hmPosLayers.clear();
        
        for (int i =0; i< lstLayers.size(); i++)
        {
            hmPosLayers.put(new Integer(i+1), lstLayers.get(i));
        }
        
    }
    
    /**
     * Obtiene el identificador de la layerfamily
     * @return Identificador de la layerfamily
     */
    public int getIdLayerFamily()
    {
        if (layerFamily!=null)
            return new Integer(this.layerFamily.getSystemId()).intValue();
        else
            return 0;
        
    }
    
  /*  public int getPosicionLayerTable (LayerTable lt)
    {
        int posicion = 0;
        
        if(hmPosLayers.containsValue(lt))
        {            
            Iterator itValores = this.hmPosLayers.values().iterator();
            Iterator itClaves = this.hmPosLayers.keySet().iterator();

            while (itClaves.hasNext()) 
            {  
                Object value = itValores.next();
                Object clave = itClaves.next();
                lstCapas.add(((Integer)clave).intValue(), (LayerTable)value);                
            }            
        }      
        
        return posicion;
    }
    */
    
    /**
     * Comprueba la iguadad entre dos objetos
     * @param o Objeto a comparar
     */
    public boolean equals(Object o) 
    {
        if (!(o instanceof LayerFamilyTable)) return false;
        if ( ((LayerFamilyTable)o).getLayerFamily().getSystemId().equals(this.getLayerFamily().getSystemId()))
                return true;
        else
            return false;
    }
    
    /**
     * Obtiene el código hash del objeto
     * @return Código hash
     */
    public int hashCode()
    {
        return new Integer(layerFamily.getSystemId()).intValue(); 
    }
}
