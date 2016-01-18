package com.geopista.ui.dialogs;



public class GeopistaBookmark 
{
    String nombreMarcador;
  double xmin;
  double ymin;
  double ymax;
  double xmax;
  public GeopistaBookmark()
  {
   
  }
  public String getNombreMarcador()
  {
    return nombreMarcador;
  }

  public void setNombreMarcador(String newNombreMarcador)
  {
    nombreMarcador = newNombreMarcador;
  }

  public double getXmin()
  {
    return xmin;
  }

  public void setXmin(double newXmin)
  {
    xmin = newXmin;
  }

  public double getYmin()
  {
    return ymin;
  }

  public void setYmin(double newYmin)
  {
    ymin = newYmin;
  }

  public double getYmax()
  {
    return ymax;
  }

  public void setYmax(double newYmax)
  {
    ymax = newYmax;
  }

  public double getXmax()
  {
    return xmax;
  }

  public void setXmax(double newXmax)
  {
    xmax = newXmax;
  }
}