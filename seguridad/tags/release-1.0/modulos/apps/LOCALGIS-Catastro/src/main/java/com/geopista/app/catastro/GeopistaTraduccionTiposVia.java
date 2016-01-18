package com.geopista.app.catastro;

public class GeopistaTraduccionTiposVia 
{
  private String tipoViaNormalizado;
  private String tipoVia;
  private String descripcion;

  public GeopistaTraduccionTiposVia()
  {
  }

  public String getTipoViaNormalizado()
  {
    return tipoViaNormalizado;
  }

  public void setTipoViaNormalizado(String newTipoViaNormalizado)
  {
    tipoViaNormalizado = newTipoViaNormalizado;
  }

  public String getTipoVia()
  {
    return tipoVia;
  }

  public void setTipoVia(String newTipoVia)
  {
    tipoVia = newTipoVia;
  }

  public String getDescripcion()
  {
    return descripcion;
  }

  public void setDescripcion(String newDescripcion)
  {
    descripcion = newDescripcion;
  }
}