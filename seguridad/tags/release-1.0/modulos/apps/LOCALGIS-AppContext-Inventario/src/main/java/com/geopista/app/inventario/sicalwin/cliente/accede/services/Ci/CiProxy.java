package com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci;

public class CiProxy implements com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci {
  private String _endpoint = null;
  private com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci ci = null;
  
  public CiProxy() {
    _initCiProxy();
  }
  
  public CiProxy(String endpoint) {
    _endpoint = endpoint;
    _initCiProxy();
  }
  
  private void _initCiProxy() {
    try {
      ci = (new com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiServiceLocator()).getCi();
      if (ci != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)ci)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)ci)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (ci != null)
      ((javax.xml.rpc.Stub)ci)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci getCi() {
    if (ci == null)
      _initCiProxy();
    return ci;
  }
  
  public java.lang.String servicio(java.lang.String in0) throws java.rmi.RemoteException{
    if (ci == null)
      _initCiProxy();
    return ci.servicio(in0);
  }
  
  public java.lang.String servicio(java.lang.String in0, com.geopista.app.inventario.sicalwin.cliente.aytos.util.gestion.Traza in1) throws java.rmi.RemoteException{
    if (ci == null)
      _initCiProxy();
    return ci.servicio(in0, in1);
  }
  
  
}