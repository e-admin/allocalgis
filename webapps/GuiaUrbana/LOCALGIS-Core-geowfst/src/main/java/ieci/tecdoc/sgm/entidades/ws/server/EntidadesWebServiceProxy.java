/**
 * EntidadesWebServiceProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ieci.tecdoc.sgm.entidades.ws.server;

public class EntidadesWebServiceProxy implements ieci.tecdoc.sgm.entidades.ws.server.EntidadesWebService {
  private String _endpoint = null;
  private ieci.tecdoc.sgm.entidades.ws.server.EntidadesWebService entidadesWebService = null;
  
  public EntidadesWebServiceProxy() {
    _initEntidadesWebServiceProxy();
  }
  
  public EntidadesWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initEntidadesWebServiceProxy();
  }
  
  private void _initEntidadesWebServiceProxy() {
    try {
      entidadesWebService = (new ieci.tecdoc.sgm.entidades.ws.server.EntidadesWebServiceServiceLocator()).getEntidadesWebService();
      if (entidadesWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)entidadesWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)entidadesWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (entidadesWebService != null)
      ((javax.xml.rpc.Stub)entidadesWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ieci.tecdoc.sgm.entidades.ws.server.EntidadesWebService getEntidadesWebService() {
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService;
  }
  
  public ieci.tecdoc.sgm.entidades.ws.server.Entidad nuevaEntidad(ieci.tecdoc.sgm.entidades.ws.server.Entidad poEntidad) throws java.rmi.RemoteException{
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService.nuevaEntidad(poEntidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio eliminarEntidad(ieci.tecdoc.sgm.entidades.ws.server.Entidad poEntidad) throws java.rmi.RemoteException{
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService.eliminarEntidad(poEntidad);
  }
  
  public ieci.tecdoc.sgm.entidades.ws.server.Entidad actualizarEntidad(ieci.tecdoc.sgm.entidades.ws.server.Entidad poEntidad) throws java.rmi.RemoteException{
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService.actualizarEntidad(poEntidad);
  }
  
  public ieci.tecdoc.sgm.entidades.ws.server.Entidad obtenerEntidad(java.lang.String identificador) throws java.rmi.RemoteException{
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService.obtenerEntidad(identificador);
  }
  
  public ieci.tecdoc.sgm.entidades.ws.server.Entidades buscarEntidades(ieci.tecdoc.sgm.entidades.ws.server.CriterioBusquedaEntidades poCriterio) throws java.rmi.RemoteException{
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService.buscarEntidades(poCriterio);
  }
  
  public ieci.tecdoc.sgm.entidades.ws.server.Entidades obtenerEntidades() throws java.rmi.RemoteException{
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService.obtenerEntidades();
  }
  
  public ieci.tecdoc.sgm.entidades.ws.server.RetornoCadena obtenerIdentificadorEntidad() throws java.rmi.RemoteException{
    if (entidadesWebService == null)
      _initEntidadesWebServiceProxy();
    return entidadesWebService.obtenerIdentificadorEntidad();
  }
  
  
}