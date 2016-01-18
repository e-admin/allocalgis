/**
 * GeopistaBookmark.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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