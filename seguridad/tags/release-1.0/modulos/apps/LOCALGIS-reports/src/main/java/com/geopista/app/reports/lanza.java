package com.geopista.app.reports;
import jimm.datavision.DataVision;
import jimm.datavision.gui.DesignWin;
public class lanza 
{
  public lanza()
  {
  }

  public static void main(String[] args)
  {
    lanza lanza = new lanza();
    DataVision aa = new DataVision();
    DesignWin win = new DesignWin("c:\\Planeamiento_subparcelas", null);
    win.getReport();
  }
}