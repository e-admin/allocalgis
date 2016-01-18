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