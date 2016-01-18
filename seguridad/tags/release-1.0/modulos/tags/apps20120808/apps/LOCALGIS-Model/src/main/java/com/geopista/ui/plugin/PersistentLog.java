package com.geopista.ui.plugin;

import java.util.HashMap;
import java.util.Map;

public class PersistentLog 
{
  private HashMap listEvents;

  public Map getListEvents()
  {
    return listEvents;
  }

  public void setListEvents(Map newListEvents)
  {
    listEvents = new HashMap(newListEvents);;
  }
}