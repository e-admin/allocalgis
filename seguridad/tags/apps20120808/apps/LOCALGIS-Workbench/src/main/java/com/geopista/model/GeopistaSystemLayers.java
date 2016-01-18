package com.geopista.model;
import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
public class GeopistaSystemLayers 
{
   //Constante que contiene el sysmtemId de la capa parcelas
  static ApplicationContext appContext=AppContext.getApplicationContext();
  public static final String PARCELAS = appContext.getString("lyrParcelas");
  public static final String NUMEROSPOLICIA = appContext.getString("lyrNumerosPolicia");
  public static final String INMUEBLES =  appContext.getString("lyrInmuebles");
  public static final String TRAMOSVIAS =  appContext.getString("lyrTramosVia");
  public static final String VIAS =  appContext.getString("lyrVias");
  public static final String AMBITOSGESTION =  appContext.getString("lyrAmbitosGestion");
 
  public GeopistaSystemLayers()
  {
  }
}