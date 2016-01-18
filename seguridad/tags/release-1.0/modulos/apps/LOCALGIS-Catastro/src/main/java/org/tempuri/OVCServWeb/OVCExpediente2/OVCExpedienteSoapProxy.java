package org.tempuri.OVCServWeb.OVCExpediente2;

public class OVCExpedienteSoapProxy implements org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap {
  private String _endpoint = null;
  private org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap oVCExpedienteSoap = null;
  
  public OVCExpedienteSoapProxy() {
    _initOVCExpedienteSoapProxy();
  }
  
  public OVCExpedienteSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initOVCExpedienteSoapProxy();
  }
  
  private void _initOVCExpedienteSoapProxy() {
    try {
      oVCExpedienteSoap = (new org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteLocator()).getOVCExpedienteSoap();
      if (oVCExpedienteSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)oVCExpedienteSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)oVCExpedienteSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (oVCExpedienteSoap != null)
      ((javax.xml.rpc.Stub)oVCExpedienteSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap getOVCExpedienteSoap() {
    if (oVCExpedienteSoap == null)
      _initOVCExpedienteSoapProxy();
    return oVCExpedienteSoap;
  }
  
  public es.meh.catastro.www.Expedientes_Out generaExpediente(es.meh.catastro.www.Expedientes_In xmlPeticion) throws java.rmi.RemoteException{
    if (oVCExpedienteSoap == null)
      _initOVCExpedienteSoapProxy();
    return oVCExpedienteSoap.generaExpediente(xmlPeticion);
  }
  
  public es.meh.catastro.www.ConsultaCatastro_Out consultaCatastro(es.meh.catastro.www.ConsultaCatastro_In xmlPeticion) throws java.rmi.RemoteException{
    if (oVCExpedienteSoap == null)
      _initOVCExpedienteSoapProxy();
    return oVCExpedienteSoap.consultaCatastro(xmlPeticion);
  }
  
  public es.meh.catastro.www.ActualizaCatastro_Out actualizaCatastro(es.meh.catastro.www.ActualizaCatastro_In xmlPeticion) throws java.rmi.RemoteException{
    if (oVCExpedienteSoap == null)
      _initOVCExpedienteSoapProxy();
    return oVCExpedienteSoap.actualizaCatastro(xmlPeticion);
  }
  
  public es.meh.catastro.www.ConsultaExpediente_Out consultaExpediente(es.meh.catastro.www.ConsultaExpediente_In xmlPeticion) throws java.rmi.RemoteException{
    if (oVCExpedienteSoap == null)
      _initOVCExpedienteSoapProxy();
    return oVCExpedienteSoap.consultaExpediente(xmlPeticion);
  }
  
  
}