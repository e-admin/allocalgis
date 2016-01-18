/**
 * GeopistaObjetoInformeEncabezado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

public class GeopistaObjetoInformeEncabezado 
{
  private String logo;
  private String tituloInforme;
  private String subTituloInforme;
  private String tablaEncabezado;
  private String etiquetaEncabezado;
  private String etiquetaDescripcion;
  private String campoEncabezado;
  private String campoDescripcion;
  private String tituloImagen;
  private String crearImagen;
  private String imagenEnInforme;

/**
 * @return Returns the imagenEnInforme.
 */
public String getImagenEnInforme()
{
    return imagenEnInforme;
}
/**
 * @param imagenEnInforme The imagenEnInforme to set.
 */
public void setImagenEnInforme(String imagenEnInforme)
{
    this.imagenEnInforme = imagenEnInforme;
}
  public String getLogo()
  {
    return logo;
  }

  public void setLogo(String newLogo)
  {
    logo = newLogo;
  }

  public String getTituloInforme()
  {
    return tituloInforme;
  }

  public void setTituloInforme(String newTituloInforme)
  {
    tituloInforme = newTituloInforme;
  }

  public String getSubTituloInforme()
  {
    return subTituloInforme;
  }

  public void setSubTituloInforme(String newSubTituloInforme)
  {
    subTituloInforme = newSubTituloInforme;
  }

  public String getTablaEncabezado()
  {
    return tablaEncabezado;
  }

  public void setTablaEncabezado(String newTablaEncabezado)
  {
    tablaEncabezado = newTablaEncabezado;
  }

  public String getEtiquetaEncabezado()
  {
    return etiquetaEncabezado;
  }

  public void setEtiquetaEncabezado(String newEtiquetaEncabezado)
  {
    etiquetaEncabezado = newEtiquetaEncabezado;
  }

  public String getEtiquetaDescripcion()
  {
    return etiquetaDescripcion;
  }

  public void setEtiquetaDescripcion(String newEtiquetaDescripcion)
  {
    etiquetaDescripcion = newEtiquetaDescripcion;
  }

  public String getCampoEncabezado()
  {
    return campoEncabezado;
  }

  public void setCampoEncabezado(String newCampoEncabezado)
  {
    campoEncabezado = newCampoEncabezado;
  }

  public String getCampoDescripcion()
  {
    return campoDescripcion;
  }

  public void setCampoDescripcion(String newCampoDescripcion)
  {
    campoDescripcion = newCampoDescripcion;
  }

  public String getTituloImagen()
  {
    return tituloImagen;
  }

  public void setTituloImagen(String newTituloImagen)
  {
    tituloImagen = newTituloImagen;
  }

  public String getCrearImagen()
  {
    return crearImagen;
  }

  public void setCrearImagen(String newCrearImagen)
  {
    crearImagen = newCrearImagen;
  }
}