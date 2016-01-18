/**
 * LogEventsTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.LogEvent;

public class LogEventsTableModel extends AbstractTableModel
{ 

  ArrayList collectionsEvents = new ArrayList();
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

   String[] columnNames = {"ShowEventsLogPlugIn.Fecha","ShowEventsLogPlugIn.Usuario",
                          "ShowEventsLogPlugIn.TipoEvento","ShowEventsLogPlugIn.SystemId",
                          "ShowEventsLogPlugIn.Borrado","ShowEventsLogPlugIn.Modificado",
                          "ShowEventsLogPlugIn.Nuevo"};

   public String getColumnName(int column) {

      return aplicacion.getI18nString(columnNames[column]);
   }

  // This table model works for any one given directory
  public LogEventsTableModel(HashMap hashMapEvents) { 
    collectionsEvents.addAll(hashMapEvents.values());
  }

  // These are easy methods
  public int getColumnCount() { return 7; } 
  public int getRowCount() { return collectionsEvents.size(); }  

  // Information about each column
 // public String getColumnName(int col) { return columnNames[col]; }
  //public Class getColumnClass(int col) { return columnClasses[col]; }

  // The method that must actually return the value of each cell
  public Object getValueAt(int row, int col) {
    LogEvent actualEvent = (LogEvent)collectionsEvents.get(row);
    DateFormat df= DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
 
    switch(col) {
    
    case 0: return df.format(actualEvent.getTimeStamp());
    case 1: return "";
    case 2: return actualEvent.getEventType();
    case 3: return actualEvent.getFeatureId();
    case 4: return actualEvent.getEventType();
    case 5: return actualEvent.getEventType();
    case 6: return actualEvent.getEventType();
    default: return null;
    }
  }





}