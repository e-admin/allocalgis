/**
 * TableColumnsModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.schema.column;


/**
 * Modelo para mostrar columnas en la tabla de atributos de una capa de GeoPISTA
 * 
 * @author cotesa
 *
 */


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.feature.Column;
import com.vividsolutions.jump.I18N;

public class TableColumnsModel extends AbstractTableModel {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private String[] columnNames = {I18N.get("GestorCapas","tablasBD.columnas.columnasBD"),
            I18N.get("GestorCapas","tablasBD.columnas.columnasSistema")};
    
    private ColumnRow[] data= new ColumnRow[0];
    
    /**
     * @return número de columnas
     */
    public int getColumnCount() {
        return columnNames.length;
    }
    
    /**
     * @return número de filas
     */
    public int getRowCount() {
        return data.length;
    }
    
    /**
     * Devuelve el nombre de una columna situada en la posición indicada
     * @param col Índice de la columna cuyo nombre se desea conocer
     * @return nombre de la columna 
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    /**
     * Devuelve el valor situado en la fila y columna indicadas
     * @param row Índice de la fila
     * @param col Índice de la columna
     * 
     * @return Objeto situado en la fila y columna indicadas
     */
    public Object getValueAt(int row, int col) {
        
        if (data[row]==null)
            return null;
        
        switch (col)
        {
        case 0: 
            return data[row].getColumnaBD();
        case 1: 
            return data[row].getColumnaSistema();
            
        default: 
            return null;
        
        }
        
    }
    
    /**
     * Devuelve el ColumnRow completo situado en la posición indicada
     * @param row Índice de la fila
     * @return ColumnRow situado en la posición row
     */
    public ColumnRow getValueAt(int row) {
        
        return data[row];
        
    }
    
    
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  
     */
    public Class getColumnClass(int c) {
        //System.out.println("clase: " +getValueAt(0, c).getClass());
        if (getValueAt(0, c)!=null)        
            return getValueAt(0, c).getClass();
        else
            return String.class;
    }
    
    /*
     * Don't need to implement this method unless your table is
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col ==0) {
            return false;
        } else {
            return true;
        }
    }
    
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        
        //      System.out.println("Setting value at " + row + "," + col
        //              + " to " + value
        //              + " (an instance of "
        //              + value.getClass() + ")");
        
        if (data[row] !=null)
        {       
            switch (col)
            {
            case 0: 
                data[row].setColumnaBD((ColumnDB)value);
                break;
            case 1: 
                data[row].setColumnaSistema((Column)value);
                break;
                
            default: 
                break;
            
            
            }
            
            
            fireTableCellUpdated(row, col);
        }
        
        //System.out.println("New value of data:");
        //printDebugData();
        
    }
    
    /**
     * Establece el valor de una fila a un ColumnRow
     * @param value ColumnRow 
     * @param row Índice de la fila
     */
    public void setValueAt(ColumnRow value, int row) {        
        
        setValueAt(value.getColumnaBD(), row, 0);
        setValueAt(value.getColumnaSistema(), row, 1);
        
    }
       
    
    /**
     * Inserta una nueva fila en la tabla
     * @param valores ColumnRow con la nueva fila
     */
    public void insertRow (ColumnRow valores){
        insertRow(valores, -1);
    }
    
    /**
     * Inserta una nueva fila en la tabla en la posición indicada
     * @param valores ColumnRow con la nueva fila
     * @param fila Posición de la fila
     */
    public void insertRow (ColumnRow valores, int fila){
        int longitud = getRowCount();
        data = new ColumnRow[longitud+1];
        if (fila==-1) 
            fila = longitud;       
        
        this.setValueAt(valores, fila);    
        
    }
    
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (ColumnRow[] datos)
    {
        this.data = datos;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public void setData (Map htColBD, Map htColSis)
    {      
        HashMap hmColumnas = new HashMap(htColBD);
        this.data = new ColumnRow[hmColumnas.size()];
        
        int position = 0;
        
        for (int i=0; ;i++)
        {
            if (!hmColumnas.isEmpty())
            {
                if (hmColumnas.get(new Integer(i))!=null)
                {
                    ColumnDB colBD = (ColumnDB)hmColumnas.get(new Integer(i));
                    Column col = getColumnByName (htColSis,colBD.getName());
                    this.data[position] = new ColumnRow(col, colBD);
                    hmColumnas.remove(new Integer(i));
                    position++;
                }
            }
            else
            {
                break;
            }            
        }        
    }
    
    /**
     * Devuelve una Column de una lista conociendo su nombre
     * @param ht Hashtable en la que los valores son las Column y las claves los nombres
     * de las columnas
     * @param name Nombre de la Column buscada
     * @return Column cuyo nombre es el buscado
     */
    private Column getColumnByName(Map ht, String name)
    {        
        if (ht.containsKey(name))
            return (Column)ht.get(name);
        else if (ht.containsKey(name.toUpperCase()))
            return (Column)ht.get(name.toUpperCase());
        else
        {
            for (Iterator it=ht.keySet().iterator(); it.hasNext(); ) 
            {
                String colName = (String)it.next();
                if (colName.equalsIgnoreCase(name)
                        || colName.replaceAll("_", "").equalsIgnoreCase(name.replaceAll("_", ""))
                        || colName.toUpperCase().startsWith(name.toUpperCase()) 
                )
                    return (Column)ht.get(colName);
            }
        }
        
        return new Column();
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public ColumnRow[] getData (){
        return data;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public HashMap getDataMap (){
        
        HashMap hm = new HashMap();
        
        for (int i=0; i<data.length; i++)
            hm.put(new Integer(i), data[i]);
        
        return hm;
    }
}