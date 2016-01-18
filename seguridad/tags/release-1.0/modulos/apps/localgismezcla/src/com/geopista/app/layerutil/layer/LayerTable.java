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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.layer;


/**
 * Bean que representa los datos de una Layer de acuerdo a los recogidos en la tabla
 * <code>layer</code> de la base de datos geopista
 * 
 * @author cotesa
 *
 */
import java.util.Hashtable;

import com.geopista.protocol.administrador.Acl;
import com.vividsolutions.jump.workbench.model.Layer;


public class LayerTable
{
    
    private Layer layer = new Layer();
    private int idLayer;
    private Hashtable htNombre = new Hashtable();
    private Acl acl =new Acl();
    private String idEntidadLayer = "";
    
    /**
     * Constructor por defecto
     */
    public LayerTable()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Constructor de la clase
     * @param idLayer Identificador de la capa
     * @param layer Datos de la capa, en un objeto Layer
     */
    public LayerTable (int idLayer, Layer layer)
    {
        this.layer = layer;
        this.idLayer = idLayer;
    }

    /**
     * @return Returns the idLayer.
     */
    public int getIdLayer()
    {
        return idLayer;
    }

    /**
     * @param idLayer The idLayer to set.
     */
    public void setIdLayer(int idLayer)
    {
        this.idLayer = idLayer;
    }

    /**
     * @return Returns the layer.
     */
    public Layer getLayer()
    {
        return layer;
    }

    /**
     * @param layer The layer to set.
     */
    public void setLayer(Layer layer)
    {
        this.layer = layer;
    }

    /**
     * @return Returns the htNombre.
     */
    public Hashtable getHtNombre()
    {
        return htNombre;
    }

    /**
     * @param htNombre The htNombres to set.
     */
    public void setHtNombre(Hashtable htNombre)
    {
        this.htNombre = htNombre;
    }
        
    /**
     * Comprueba si dos objetos son iguales
     * @param o Objeto a comparar
     */
    public boolean equals(Object o) 
    {
        if (!(o instanceof LayerTable)) return false;
        if ( ((LayerTable)o).getIdLayer() == this.getIdLayer())
            return true;
        else
            return false;
    }
        
    /**
     * @return Returns the idAcl.
     */
    public Acl getAcl()
    {
        return acl;
    }

    /**
     * @param idAcl The idAcl to set.
     */
    public void setAcl(Acl acl)
    {
        this.acl = acl;
    }

    /**
     * Devuelve el código hash del objeto
     */
    public int hashCode()
    {
        return idLayer;
    }
    
    public String getIdEntidadLayer(){
    	return idEntidadLayer;
    }
    
    public void setIdEntidadLayer(String idEntidadLayer){
    	this.idEntidadLayer = idEntidadLayer;
    }
    
}
