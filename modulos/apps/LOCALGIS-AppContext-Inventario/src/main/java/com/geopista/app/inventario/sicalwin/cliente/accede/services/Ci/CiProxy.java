/**
 * CiProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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