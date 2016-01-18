package com.geopista.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
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