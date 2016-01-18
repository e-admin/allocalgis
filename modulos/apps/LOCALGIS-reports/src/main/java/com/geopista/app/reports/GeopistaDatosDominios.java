/**
 * GeopistaDatosDominios.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

public class GeopistaDatosDominios 
{
  private int orden;
  private String campo;
  private String tabla;
  private String dominio;
  private String Etiquetas;

  public GeopistaDatosDominios()
  {
  }

  public int getOrden()
  {
    return orden;
  }

  public void setOrden(int newOrden)
  {
    orden = newOrden;
  }

  public String getCampo()
  {
    return campo;
  }

  public void setCampo(String newCampo)
  {
    campo = newCampo;
  }

  public String getTabla()
  {
    return tabla;
  }

  public void setTabla(String newTabla)
  {
    tabla = newTabla;
  }

  public String getDominio()
  {
    return dominio;
  }

  public void setDominio(String newDominio)
  {
    dominio = newDominio;
  }

  public String getEtiquetas()
  {
    return Etiquetas;
  }

  public void setEtiquetas(String newEtiquetas)
  {
    Etiquetas = newEtiquetas;
  }
}