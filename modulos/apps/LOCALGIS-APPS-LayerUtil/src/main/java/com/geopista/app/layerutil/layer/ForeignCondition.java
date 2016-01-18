/**
 * ForeignCondition.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layer;


/**
 * Bean que representa una condición para la generación de una relación entre dos tablas
 * a través de un campo de cada una de ellas
 * 
 * @author cotesa
 *
 */
public class ForeignCondition
{
    
    String tablaIzquierda;
    String columnaIzquierda;
    String tablaDerecha;
    String columnaDerecha;
    
    /**
     * Constructor por defecto
     *
     */
    public ForeignCondition()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Constructor de la clase 
     * 
     * @param tablaIzquierda Cadena que representa el nombre de la tabla a la izquierda de 
     * la condición
     * @param columnaIzquierda Cadena que representa el nombre de la columna a la izquierda
     * de la condición
     * @param tablaDerecha Cadena que representa el nombre de la tabla a la derecha
     * de la condición
     * @param columnaDerecha Cadena que representa el nombre de la columna a la derecha de la
     * condición
     */
    public ForeignCondition(String tablaIzquierda, String columnaIzquierda, String tablaDerecha,String columnaDerecha)
    {
        this.tablaIzquierda = tablaIzquierda;
        this.columnaIzquierda = columnaIzquierda;
        this.tablaDerecha = tablaDerecha;
        this.columnaDerecha = columnaDerecha;     
        
        
    }
    
    /**
     * Constructor de la clase
     * 
     * @param izquierda Cadena que representa el nombre de la tabla y de la columna de la 
     * izquierda de la condición, separados por un punto
     * @param derecha Cadena que representa el nombre de la tabla y de la columna de la 
     * derecha de la condición, separados por un punto
     */
    public ForeignCondition (String izquierda, String derecha)
    {
        this.tablaIzquierda = izquierda.substring(0, izquierda.indexOf("."));
        this.columnaIzquierda = izquierda.substring(izquierda.indexOf(".")+1);
        
        this.tablaDerecha = derecha.substring(0, derecha.indexOf("."));
        this.columnaDerecha = derecha.substring(derecha.indexOf(".")+1);
        
    }
    
    /**
     * Constructor de la clase
     * 
     * @param condicion Cadena que representa la condición para la generación de la 
     * clave, en el formato tabla1.columnaA = tabla2.columnaB
     */
    public ForeignCondition (String condicion)
    {
        this(condicion.substring(0, condicion.indexOf("=")), 
                condicion.substring(condicion.indexOf("=")+1));
    }
    
    /**
     * @return Returns the columnaDerecha.
     */
    public String getColumnaDerecha()
    {
        return columnaDerecha;
    }
    
    /**
     * @param columnaDerecha The columnaDerecha to set.
     */
    public void setColumnaDerecha(String columnaDerecha)
    {
        this.columnaDerecha = columnaDerecha;
    }
    
    /**
     * @return Returns the columnaIzquierda.
     */
    public String getColumnaIzquierda()
    {
        return columnaIzquierda;
    }
    
    /**
     * @param columnaIzquierda The columnaIzquierda to set.
     */
    public void setColumnaIzquierda(String columnaIzquierda)
    {
        this.columnaIzquierda = columnaIzquierda;
    }
    
    /**
     * @return Returns the tablaDerecha.
     */
    public String getTablaDerecha()
    {
        return tablaDerecha;
    }
    
    /**
     * @param tablaDerecha The tablaDerecha to set.
     */
    public void setTablaDerecha(String tablaDerecha)
    {
        this.tablaDerecha = tablaDerecha;
    }
    
    /**
     * @return Returns the tablaIzquierda.
     */
    public String getTablaIzquierda()
    {
        return tablaIzquierda;
    }
    
    /**
     * @param tablaIzquierda The tablaIzquierda to set.
     */
    public void setTablaIzquierda(String tablaIzquierda)
    {
        this.tablaIzquierda = tablaIzquierda;
    }
    
    
    /**
     * @return Returns the condicionDerecha.
     */
    public String getCondicionDerecha()
    {
        return tablaDerecha+"." +columnaDerecha;
    }
    
    /**
     * @param columnaDerecha The condicionDerecha to set.
     */
    public void setCondicionDerecha(String condicionDerecha)
    {
        this.tablaDerecha = condicionDerecha.substring(0, condicionDerecha.indexOf("."));
        this.columnaDerecha = condicionDerecha.substring(condicionDerecha.indexOf(".")+1);
    }
    
    /**
     * @return Returns the condicionIzquierda.
     */
    public String getCondicionIzquierda()
    {
        return tablaIzquierda+"."+columnaIzquierda;
    }
    
    /**
     * @param columnaIzquierda The columnaIzquierda to set.
     */
    public void setCondicionIzquierda(String condicionIzquierda)
    {
        this.tablaIzquierda = condicionIzquierda.substring(0, condicionIzquierda.indexOf("."));
        this.columnaIzquierda = condicionIzquierda.substring(condicionIzquierda.indexOf(".")+1);
    }
    
    /**
     * @return Devuelve la condición completa en el formato tabla1.columnaA = tabla2.columnaB
     */
    public String getCondicion ()
    {
        return getCondicionIzquierda()+"="+getCondicionDerecha();
    }
    
    /**
     * Comprueba si dos objetos son iguales
     * 
     * @param o Objeto de comparación
     * @return Devuelve true si los objetos comparados son iguales
     */
    public boolean equals(Object o) {
        if (!(o instanceof ForeignCondition)) return false;       
        if (((ForeignCondition)o).getCondicion().equals(this.getCondicion())) 
            return true;
        else
            return false;
    }
}
