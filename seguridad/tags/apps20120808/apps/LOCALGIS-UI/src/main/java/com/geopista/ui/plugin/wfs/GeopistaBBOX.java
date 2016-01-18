package com.geopista.ui.plugin.wfs;


public class GeopistaBBOX 
{
  private double xmin;
  private double ymin;
  private double ymax;
  private double xmax;
  public GeopistaBBOX()
  {
   
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