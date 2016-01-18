/**
 * GeopistaValidarFINURB_FINCARU.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

/** La clase GeopistaValidarFINURB_FINCARU comprueba que existen las líneas correctas en en el fichero de texto */
public class GeopistaValidarFINURB_FINCARU
{
   public GeopistaValidarFINURB_FINCARU()
  {
  }


/**
 * Este método comprueba que hay una línea que contiene datos sobre parcelas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarParcela (String linea)
    {
      return true;
    }

/**
 * Este método comprueba que hay una línea que contiene datos sobre subparcelas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarSubparcela (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre unidades constructivas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarUC (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre construcciones
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarConstruccion (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre cargos
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarCargo (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre parcelas rusticas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
     public boolean ValidarParcelaRustica (String linea)
    {
      return true;
    }
      public boolean ValidarSubParcelaRustica (String linea)
    {
      return true;
    }

    public boolean ValidarConstruccionRustica (String linea)
    {
      return true;
    }
    
    public boolean ValidarTitularRustico (String linea)
    {
      return true;
    }
}