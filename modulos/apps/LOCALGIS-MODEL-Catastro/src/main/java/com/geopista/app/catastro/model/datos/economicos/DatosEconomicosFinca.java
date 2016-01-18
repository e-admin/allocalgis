/**
 * DatosEconomicosFinca.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.economicos;



import com.geopista.app.catastro.model.datos.ponencia.PonenciaPoligono;

import java.io.Serializable;


public class DatosEconomicosFinca implements Serializable {  
    /**
     * Año de aprobación de la ponencia de valores a aplicar
     */
    private Integer anioAprobacion;
    /**
     * Código de la fórmula de cálculo del valor catastral
     */
    private Integer codigoCalculoValor;
    /**
     * 
     */
    private PonenciaPoligono poligonoCatastralValor = new PonenciaPoligono();
    /**
     * Indicador de modalidad de reparto del valor de la edificabilidad 
     * no materializada o derecho de vuelo. Puede tomar los valores:
     * 1 por partes iguales
     * 2 por superficie
     * 3 por coeficientes
     * A se valora por repercusión
     * B anula el vuelo indicado en ponencia
     * C fincas infraedificadas o ruinosas
     * N reparto efectuado según forma de cálculo
     * 
     */
    private String indModalidadReparto;
    /**
     * Indicador de infraedificabilidad en procedimiento de valoración 
     * colectiva 2005 y posteriores
     */
    private String indInfraedificabilidad;
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosEconomicosFinca()
    {
        
    }	
    
    
    /**
     * @return Returns the anioAprobacion.
     */
    public Integer getAnioAprobacion()
    {
        return anioAprobacion;
    }
    
    /**
     * @param anioAprobacion The anioAprobacion to set.
     */
    public void setAnioAprobacion(Integer anioAprobacion)
    {
        this.anioAprobacion = anioAprobacion;
    }
    
    /**
     * @return Returns the codigoCalculoValor.
     */
    public Integer getCodigoCalculoValor()
    {
        return codigoCalculoValor;
    }
    
    /**
     * @param codigoCalculoValor The codigoCalculoValor to set.
     */
    public void setCodigoCalculoValor(Integer codigoCalculoValor)
    {
        this.codigoCalculoValor = codigoCalculoValor;
    }
    
    /**
     * @return Returns the indInfraedificabilidad.
     */
    public String getIndInfraedificabilidad()
    {
        return indInfraedificabilidad;
    }
    
    /**
     * @param indInfraedificabilidad The indInfraedificabilidad to set.
     */
    public void setIndInfraedificabilidad(String indInfraedificabilidad)
    {
        this.indInfraedificabilidad = indInfraedificabilidad;
    }
    
    /**
     * @return Returns the indModalidadReparto.
     */
    public String getIndModalidadReparto()
    {
        return indModalidadReparto;
    }
    
    /**
     * @param indModalidadReparto The indModalidadReparto to set.
     */
    public void setIndModalidadReparto(String indModalidadReparto)
    {
        this.indModalidadReparto = indModalidadReparto;
    }
    
    /**
     * @return Returns the poligonoCatastralValor.
     */
    public PonenciaPoligono getPoligonoCatastralValor()
    {
        return poligonoCatastralValor;
    }
    
    /**
     * @param poligonoCatastralValor The poligonoCatastralValor to set.
     */
    public void setPoligonoCatastralValor(PonenciaPoligono poligonoCatastralValor)
    {
        this.poligonoCatastralValor = poligonoCatastralValor;
    }
    
    /**
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }
    
    
}
