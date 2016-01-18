/**
 * LoadEIELDataProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.loadEIELData.service;

public class LoadEIELDataProxy implements com.geopista.app.loadEIELData.service.LoadEIELData {
  private String _endpoint = null;
  private com.geopista.app.loadEIELData.service.LoadEIELData loadEIELData = null;
  
  public LoadEIELDataProxy() {
    _initLoadEIELDataProxy();
  }
  
  public LoadEIELDataProxy(String endpoint) {
    _endpoint = endpoint;
    _initLoadEIELDataProxy();
  }
  
  private void _initLoadEIELDataProxy() {
    try {
      loadEIELData = (new com.geopista.app.loadEIELData.service.LoadEIELDataServiceLocator()).getLoadEIELData();
      if (loadEIELData != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)loadEIELData)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)loadEIELData)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (loadEIELData != null)
      ((javax.xml.rpc.Stub)loadEIELData)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.geopista.app.loadEIELData.service.LoadEIELData getLoadEIELData() {
    if (loadEIELData == null)
      _initLoadEIELDataProxy();
    return loadEIELData;
  }
  
  public com.geopista.app.loadEIELData.vo.EIELLayer[] getEIELLayerList(java.lang.String lang) throws java.rmi.RemoteException{
    if (loadEIELData == null)
      _initLoadEIELDataProxy();
    return loadEIELData.getEIELLayerList(lang);
  }
  
  public com.geopista.app.loadEIELData.vo.CompleteEIELLayer loadEIELLayer(java.lang.String codigoIneMunicipio, java.lang.String id, java.lang.String lang_name, java.lang.String table, java.lang.String idField, java.lang.String geometryField) throws java.rmi.RemoteException{
    if (loadEIELData == null)
      _initLoadEIELDataProxy();
    return loadEIELData.loadEIELLayer(codigoIneMunicipio, id, lang_name, table, idField, geometryField);
  }
  
  public com.geopista.app.loadEIELData.vo.EIELLayer[] getEIELLayerList_MAP(java.lang.String idMap, java.lang.String lang) throws java.rmi.RemoteException{
    if (loadEIELData == null)
      _initLoadEIELDataProxy();
    return loadEIELData.getEIELLayerList_MAP(idMap, lang);
  }
  
  public com.geopista.app.loadEIELData.vo.EIELMap[] getEIELMapList(java.lang.String lang) throws java.rmi.RemoteException{
    if (loadEIELData == null)
      _initLoadEIELDataProxy();
    return loadEIELData.getEIELMapList(lang);
  }
  
  
}