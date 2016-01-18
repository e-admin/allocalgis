package com.geopista.app.reports;

public class GeopistaObjetoInformeCampos 
{
  private String tipo;
  private String etiquetas;
  private String campo;
  private String tabla;
  private String orden;
  private String subtotales;



  public String getTipo()
  {
    return tipo;
  }

  public void setTipo(String newTipo)
  {
    tipo = newTipo;
  }

  public String getEtiquetas()
  {
    return etiquetas;
  }

  public void setEtiquetas(String newEtiquetas)
  {
    etiquetas = newEtiquetas;
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

  public String getOrden()
  {
    return orden;
  }

  public void setOrden(String newOrden)
  {
    orden = newOrden;
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