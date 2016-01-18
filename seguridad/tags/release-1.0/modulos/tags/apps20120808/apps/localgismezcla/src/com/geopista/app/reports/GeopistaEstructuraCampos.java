package com.geopista.app.reports;

public class GeopistaEstructuraCampos 
{
  private int orden;
  private String tipo;
  private String etiqueta;
  private String campo;
  private String tabla;
  private String subtotales;

  public int getOrden()
  {
    return orden;
  }

  public void setOrden(int newOrden)
  {
    orden = newOrden;
  }

  public String getTipo()
  {
    return tipo;
  }

  public void setTipo(String newTipo)
  {
    tipo = newTipo;
  }

  public String getEtiqueta()
  {
    return etiqueta;
  }

  public void setEtiqueta(String newEtiqueta)
  {
    etiqueta = newEtiqueta;
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

  public String getSubtotales()
  {
    return subtotales;
  }

  public void setSubtotales(String newSubtotales)
  {
    subtotales = newSubtotales;
  }
}