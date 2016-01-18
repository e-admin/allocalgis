/**
 * StandardPirolTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 30.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: StandardPirolTableModel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/StandardPirolTableModel.java,v $
 */
package pirolPlugIns.utilities;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * standard implementation for a table model.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public abstract class StandardPirolTableModel extends AbstractTableModel {
    /**
     * each element of this vector represents a single row of the table. Internally, a row is represented
     * by a Object[].
     */
    protected Vector rows = new Vector();
    
    /**
     * array that holds information on the names for the table columns
     */
    protected String[] colNames = null;

    public StandardPirolTableModel(String[] colNames) {
        super();
        this.colNames = colNames;
    }

    
    public int getColumnCount() {
        return colNames.length;
    }

    public int getRowCount() {
        return rows.size();
    }

    public String getColumnName(int column) {
        return this.colNames[column];
    }
    
    /**
     * deletes all data from the table (also imforms the GUI)
     */
    public void clearTable(){
        int numRows = this.getRowCount();
        this.rows.clear();
        if (numRows >= 1)
            this.fireTableRowsDeleted(0, numRows - 1);
    }
    
    /**
     * @param columnName name of column to get the index for 
     * @return the index of the column with the given name
     */
    public int findColumn(String columnName) {
        for ( int i=0; i<this.colNames.length; i++ ){
            if (this.colNames[i].toLowerCase().equals(columnName.toLowerCase())) return i;
        }
        return -1;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ((Object[])this.rows.get(rowIndex))[columnIndex] = aValue;
        this.fireTableCellUpdated(rowIndex,columnIndex);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Object[])this.rows.get(rowIndex))[columnIndex];
    }

    public abstract boolean isCellEditable(int rowIndex, int columnIndex);
    public abstract Class getColumnClass(int columnIndex);
    
    /**
     * simple method to add a row to the table.
     * (It better matches the given column nummer and column types!!)
     *@param newRow
     */
    public void addRow(Object[] newRow){
        this.rows.add(newRow);
        this.fireTableRowsInserted(this.getRowCount()-1,this.getRowCount()-1);
    }


    /**
     *@return array containing the names of the columns
     */
    public String[] getColNames() {
        return colNames;
    }


    /**
     * Setting new column names will flush the table, if the new array has not the same length as the old one!
     *@param colNames array containing new column names
     */
    protected void setColNames(String[] colNames) {
        if (this.colNames.length != colNames.length)
            this.clearTable();
        this.colNames = colNames;
    }
    
    

}
