/**
 * WSOtrosOrganismosProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.sadim.ws;


public class WSOtrosOrganismosProxy implements WSOtrosOrganismos {
  private String _endpoint = null;
  private WSOtrosOrganismos wSOtrosOrganismos = null;
  
  public WSOtrosOrganismosProxy() {
    _initWSOtrosOrganismosProxy();
  }
  
  public WSOtrosOrganismosProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSOtrosOrganismosProxy();
  }
  
  private void _initWSOtrosOrganismosProxy() {
    try {
      wSOtrosOrganismos = (new WSOtrosOrganismosImplServiceLocator()).getWSOtrosOrganismosImplPort();
      if (wSOtrosOrganismos != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSOtrosOrganismos)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSOtrosOrganismos)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSOtrosOrganismos != null)
      ((javax.xml.rpc.Stub)wSOtrosOrganismos)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public WSOtrosOrganismos getWSOtrosOrganismos() {
    if (wSOtrosOrganismos == null)
      _initWSOtrosOrganismosProxy();
    return wSOtrosOrganismos;
  }
  
  public java.lang.String obtenerInformacionCentroAsistencial(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException{
    if (wSOtrosOrganismos == null)
      _initWSOtrosOrganismosProxy();
    return wSOtrosOrganismos.obtenerInformacionCentroAsistencial(codprov, codmunic, fecha);
  }
  
  public java.lang.String obtenerInformacionEducacion(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException{
    if (wSOtrosOrganismos == null)
      _initWSOtrosOrganismosProxy();
    return wSOtrosOrganismos.obtenerInformacionEducacion(codprov, codmunic, fecha);
  }
  
  public java.lang.String obtenerInformacionIneSadei(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException{
    if (wSOtrosOrganismos == null)
      _initWSOtrosOrganismosProxy();
    return wSOtrosOrganismos.obtenerInformacionIneSadei(codprov, codmunic, fecha);
  }
  
  public java.lang.String obtenerInformacionCentroSanitario(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException{
    if (wSOtrosOrganismos == null)
      _initWSOtrosOrganismosProxy();
    return wSOtrosOrganismos.obtenerInformacionCentroSanitario(codprov, codmunic, fecha);
  }
  
  public java.lang.String obtenerInformacionTurismo(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException{
    if (wSOtrosOrganismos == null)
      _initWSOtrosOrganismosProxy();
    return wSOtrosOrganismos.obtenerInformacionTurismo(codprov, codmunic, fecha);
  }
  
  
}