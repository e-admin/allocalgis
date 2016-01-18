/**
 * CatalogoTramitesWebServiceProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ieci.tecdoc.sgm.catalogo.ws.server;

public class CatalogoTramitesWebServiceProxy implements ieci.tecdoc.sgm.catalogo.ws.server.CatalogoTramitesWebService {
  private String _endpoint = null;
  private ieci.tecdoc.sgm.catalogo.ws.server.CatalogoTramitesWebService catalogoTramitesWebService = null;
  
  public CatalogoTramitesWebServiceProxy() {
    _initCatalogoTramitesWebServiceProxy();
  }
  
  public CatalogoTramitesWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCatalogoTramitesWebServiceProxy();
  }
  
  private void _initCatalogoTramitesWebServiceProxy() {
    try {
      catalogoTramitesWebService = (new ieci.tecdoc.sgm.catalogo.ws.server.CatalogoTramitesWebServiceServiceLocator()).getCatalogoTramitesWebService();
      if (catalogoTramitesWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)catalogoTramitesWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)catalogoTramitesWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (catalogoTramitesWebService != null)
      ((javax.xml.rpc.Stub)catalogoTramitesWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.CatalogoTramitesWebService getCatalogoTramitesWebService() {
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService;
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Tramites query(ieci.tecdoc.sgm.catalogo.ws.server.TramiteConsulta query, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.query(query, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Documento getDocument(java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getDocument(documentId, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Documentos getDocuments(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getDocuments(entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateDocument(ieci.tecdoc.sgm.catalogo.ws.server.Documento document, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.updateDocument(document, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addProcedure(ieci.tecdoc.sgm.catalogo.ws.server.Tramite procedure, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.addProcedure(procedure, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Tramite getProcedure(java.lang.String procedureId, ieci.tecdoc.sgm.catalogo.ws.server.RetornoLogico loadDocuments, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getProcedure(procedureId, loadDocuments, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteProcedure(java.lang.String procedureId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.deleteProcedure(procedureId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateProcedure(ieci.tecdoc.sgm.catalogo.ws.server.Tramite procedure, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.updateProcedure(procedure, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.RetornoLogico isDocumentReferenced(java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.isDocumentReferenced(documentId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addProcedureDocument(ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite procedureDocument, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.addProcedureDocument(procedureDocument, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteProcedureDocument(ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite procedureDocument, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.deleteProcedureDocument(procedureDocument, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addDocument(ieci.tecdoc.sgm.catalogo.ws.server.Documento document, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.addDocument(document, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteDocument(java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.deleteDocument(documentId, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Documentos getProcedureDocuments(java.lang.String procedureId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getProcedureDocuments(procedureId, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite getProcedureDocument(java.lang.String procedureId, java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getProcedureDocument(procedureId, documentId, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite getProcedureDocumentByCode(java.lang.String procedureId, java.lang.String documentId, java.lang.String code, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getProcedureDocumentByCode(procedureId, documentId, code, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateProcedureDocument(ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite procedureDocument, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.updateProcedureDocument(procedureDocument, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Tramites getProcedures(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getProcedures(entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Documento getDocumentfromCode(java.lang.String code, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getDocumentfromCode(code, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.OrganoDestinatario getAddressee(java.lang.String addresseeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getAddressee(addresseeId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addAddressee(ieci.tecdoc.sgm.catalogo.ws.server.OrganoDestinatario addressee, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.addAddressee(addressee, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteAddressee(java.lang.String addresseeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.deleteAddressee(addresseeId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateAddressee(ieci.tecdoc.sgm.catalogo.ws.server.OrganoDestinatario addressee, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.updateAddressee(addressee, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.OrganosDestinatarios getAddressees(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getAddressees(entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Conector getHook(java.lang.String hookId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getHook(hookId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addHook(ieci.tecdoc.sgm.catalogo.ws.server.Conector hook, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.addHook(hook, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteHook(java.lang.String hookId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.deleteHook(hookId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateHook(ieci.tecdoc.sgm.catalogo.ws.server.Conector hook, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.updateHook(hook, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Conectores getHooks(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getHooks(entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.Conectores getHooksByType(java.lang.String hookType, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getHooksByType(hookType, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.TipoConector getHookType(java.lang.String typeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getHookType(typeId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addHookType(ieci.tecdoc.sgm.catalogo.ws.server.TipoConector hookType, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.addHookType(hookType, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteHookType(java.lang.String typeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.deleteHookType(typeId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateHookType(ieci.tecdoc.sgm.catalogo.ws.server.TipoConector hookType, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.updateHookType(hookType, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.TiposConectores getHookTypes(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getHookTypes(entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.ConectoresAutenticacion getAuthHooks(java.lang.String tramiteId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getAuthHooks(tramiteId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addAuthHooks(ieci.tecdoc.sgm.catalogo.ws.server.ConectorAutenticacion authHook, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.addAuthHooks(authHook, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteAuthHooks(java.lang.String tramiteId, java.lang.String conectorId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.deleteAuthHooks(tramiteId, conectorId, entidad);
  }
  
  public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateAuthHooks(ieci.tecdoc.sgm.catalogo.ws.server.ConectorAutenticacion conectorAutenticacion, java.lang.String oldHookId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.updateAuthHooks(conectorAutenticacion, oldHookId, entidad);
  }
  
  public ieci.tecdoc.sgm.catalogo.ws.server.ConectorAutenticacion getAuthHook(java.lang.String tramiteId, java.lang.String conectorId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException{
    if (catalogoTramitesWebService == null)
      _initCatalogoTramitesWebServiceProxy();
    return catalogoTramitesWebService.getAuthHook(tramiteId, conectorId, entidad);
  }
  
  
}