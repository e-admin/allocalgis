/**
 * GeopistaObjetoInformeDatosGenerales.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;
import java.util.List;

public class GeopistaObjetoInformeDatosGenerales 
{
  private String modulo;
  private String titulo;
  private String orientacion;
  private String nombreFichero;
  private String descripcion;
  private List capas;

  public String getModulo()
  {
    return modulo;
  }

  public void setModulo(String newModulo)
  {
    modulo = newModulo;
  }



  public String getTitulo()
  {
    return titulo;
  }

  public void setTitulo(String newTitulo)
  {
    titulo = newTitulo;
  }

  public String getOrientacion()
  {
    return orientacion;
  }

  public void setOrientacion(String newOrientacion)
  {
    orientacion = newOrientacion;
  }

  public String getNombreFichero()
  {
    return nombreFichero;
  }

  public void setNombreFichero(String newNombreFichero)
  {
    nombreFichero = newNombreFichero;
  }

  public String getDescripcion()
  {
    return descripcion;
  }

  public void setDescripcion(String newDescripcion)
  {
    descripcion = newDescripcion;
  }

  public List getCapas()
  {
    return capas;
  }

  public void setCapas(List newCapas)
  {
    capas = newCapas;
  }
}