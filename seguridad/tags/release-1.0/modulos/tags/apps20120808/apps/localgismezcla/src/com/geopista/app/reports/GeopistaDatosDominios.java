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