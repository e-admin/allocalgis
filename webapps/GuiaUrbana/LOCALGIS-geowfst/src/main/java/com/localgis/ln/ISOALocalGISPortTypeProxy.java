/**
 * ISOALocalGISPortTypeProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ln;

public class ISOALocalGISPortTypeProxy implements com.localgis.ln.ISOALocalGISPortType {
  private String _endpoint = null;
  private com.localgis.ln.ISOALocalGISPortType iSOALocalGISPortType = null;
  
  public ISOALocalGISPortTypeProxy() {
    _initISOALocalGISPortTypeProxy();
  }
  
  public ISOALocalGISPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initISOALocalGISPortTypeProxy();
  }
  
  private void _initISOALocalGISPortTypeProxy() {
    try {
      iSOALocalGISPortType = (new com.localgis.ln.ISOALocalGISLocator()).getISOALocalGISHttpPort();
      if (iSOALocalGISPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iSOALocalGISPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iSOALocalGISPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iSOALocalGISPortType != null)
      ((javax.xml.rpc.Stub)iSOALocalGISPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.localgis.ln.ISOALocalGISPortType getISOALocalGISPortType() {
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType;
  }
  
  public com.localgis.model.ot.ParcelaOT[] consultarCatastro(java.lang.String in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.consultarCatastro(in0);
  }
  
  public com.localgis.model.ot.ProvinciaOT[] obtenerProvincias() throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.obtenerProvincias();
  }
  
  public boolean validarReferencia(java.lang.String in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.validarReferencia(in0);
  }
  
  public com.localgis.web.core.model.LocalgisMap[] verPlanosPublicados(int in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.verPlanosPublicados(in0);
  }
  
  public java.lang.String modificacionCallejero(int in0, com.localgis.model.ot.CalleOT in1) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.modificacionCallejero(in0, in1);
  }
  
  public com.localgis.web.core.model.GeopistaMunicipio[] verMunicipiosPublicados() throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.verMunicipiosPublicados();
  }
  
  public java.lang.String bajaPOI(int in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.bajaPOI(in0);
  }
  
  public java.lang.String altaCallejero(com.localgis.model.ot.CalleOT in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.altaCallejero(in0);
  }
  
  public com.localgis.model.ot.URLsPlano verPlanoPorIdNumeroPolicia(int in0, int in1, int in2, int in3, int in4, int in5) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.verPlanoPorIdNumeroPolicia(in0, in1, in2, in3, in4, in5);
  }
  
  public com.localgis.model.ot.EntidadOT[] obtenerEntidadMunicipios(int in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.obtenerEntidadMunicipios(in0);
  }
  
  public com.localgis.model.ot.MunicipioOT[] obtenerMunicipios(int in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.obtenerMunicipios(in0);
  }
  
  public com.localgis.model.ot.URLsPlano verPlanoPorIdVia(int in0, int in1, int in2, int in3, int in4, int in5) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.verPlanoPorIdVia(in0, in1, in2, in3, in4, in5);
  }
  
  public java.lang.String insertBienPreAlta(com.localgis.model.ot.BienPreAltaOT in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.insertBienPreAlta(in0);
  }
  
  public java.lang.String altaNumerero(com.localgis.model.ot.NumeroOT in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.altaNumerero(in0, in1, in2);
  }
  
  public com.localgis.web.core.model.GeopistaColumn[] selectColumnsByLayerTranslated(int in0, java.lang.String in1) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.selectColumnsByLayerTranslated(in0, in1);
  }
  
  public java.lang.String bajaCallejero(int in0, int in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.util.Calendar in5) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.bajaCallejero(in0, in1, in2, in3, in4, in5);
  }
  
  public java.lang.String altaPOI(com.localgis.model.ot.PoiOT in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.altaPOI(in0);
  }
  
  public com.localgis.model.ot.CapaOT[] obtenerListaCapas(int in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.obtenerListaCapas(in0);
  }
  
  public com.localgis.ln.AnyType2AnyTypeMapEntry[] getURLReportMap(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.Object in4, java.lang.String in5, int in6, int in7, java.lang.String in8, java.lang.String in9, java.lang.String in10, java.lang.String in11) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.getURLReportMap(in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11);
  }
  
  public com.localgis.model.ot.URLsPlano verPlanoPorReferenciaCatastral(int in0, java.lang.String in1, int in2, int in3, int in4, int in5) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.verPlanoPorReferenciaCatastral(in0, in1, in2, in3, in4, in5);
  }
  
  public java.lang.String bajaNumerero(com.localgis.model.ot.NumeroOT in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.bajaNumerero(in0, in1, in2);
  }
  
  public java.lang.String modificacionNumerero(com.localgis.model.ot.NumeroOT in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.modificacionNumerero(in0, in1, in2);
  }
  
  public com.localgis.web.core.model.LocalgisLayer[] selectLayersByIdMap(int in0) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.selectLayersByIdMap(in0);
  }
  
  public com.localgis.model.ot.TipoViaOT[] obtenerTiposDeVia() throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.obtenerTiposDeVia();
  }
  
  public com.localgis.model.ot.URLsPlano verPlanoPorCoordenadas(int in0, double in1, double in2, int in3, int in4, int in5, int in6) throws java.rmi.RemoteException{
    if (iSOALocalGISPortType == null)
      _initISOALocalGISPortTypeProxy();
    return iSOALocalGISPortType.verPlanoPorCoordenadas(in0, in1, in2, in3, in4, in5, in6);
  }
  
  
}