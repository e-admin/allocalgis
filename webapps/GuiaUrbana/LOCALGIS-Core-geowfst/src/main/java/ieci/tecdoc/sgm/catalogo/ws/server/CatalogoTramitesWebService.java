/**
 * CatalogoTramitesWebService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * CatalogoTramitesWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.catalogo.ws.server;

public interface CatalogoTramitesWebService extends java.rmi.Remote {
    public ieci.tecdoc.sgm.catalogo.ws.server.Tramites query(ieci.tecdoc.sgm.catalogo.ws.server.TramiteConsulta query, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Documento getDocument(java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Documentos getDocuments(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateDocument(ieci.tecdoc.sgm.catalogo.ws.server.Documento document, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addProcedure(ieci.tecdoc.sgm.catalogo.ws.server.Tramite procedure, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Tramite getProcedure(java.lang.String procedureId, ieci.tecdoc.sgm.catalogo.ws.server.RetornoLogico loadDocuments, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteProcedure(java.lang.String procedureId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateProcedure(ieci.tecdoc.sgm.catalogo.ws.server.Tramite procedure, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.RetornoLogico isDocumentReferenced(java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addProcedureDocument(ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite procedureDocument, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteProcedureDocument(ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite procedureDocument, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addDocument(ieci.tecdoc.sgm.catalogo.ws.server.Documento document, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteDocument(java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Documentos getProcedureDocuments(java.lang.String procedureId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite getProcedureDocument(java.lang.String procedureId, java.lang.String documentId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite getProcedureDocumentByCode(java.lang.String procedureId, java.lang.String documentId, java.lang.String code, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateProcedureDocument(ieci.tecdoc.sgm.catalogo.ws.server.DocumentoTramite procedureDocument, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Tramites getProcedures(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Documento getDocumentfromCode(java.lang.String code, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.OrganoDestinatario getAddressee(java.lang.String addresseeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addAddressee(ieci.tecdoc.sgm.catalogo.ws.server.OrganoDestinatario addressee, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteAddressee(java.lang.String addresseeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateAddressee(ieci.tecdoc.sgm.catalogo.ws.server.OrganoDestinatario addressee, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.OrganosDestinatarios getAddressees(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Conector getHook(java.lang.String hookId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addHook(ieci.tecdoc.sgm.catalogo.ws.server.Conector hook, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteHook(java.lang.String hookId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateHook(ieci.tecdoc.sgm.catalogo.ws.server.Conector hook, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Conectores getHooks(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.Conectores getHooksByType(java.lang.String hookType, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.TipoConector getHookType(java.lang.String typeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addHookType(ieci.tecdoc.sgm.catalogo.ws.server.TipoConector hookType, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteHookType(java.lang.String typeId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateHookType(ieci.tecdoc.sgm.catalogo.ws.server.TipoConector hookType, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.TiposConectores getHookTypes(ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.ConectoresAutenticacion getAuthHooks(java.lang.String tramiteId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio addAuthHooks(ieci.tecdoc.sgm.catalogo.ws.server.ConectorAutenticacion authHook, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio deleteAuthHooks(java.lang.String tramiteId, java.lang.String conectorId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio updateAuthHooks(ieci.tecdoc.sgm.catalogo.ws.server.ConectorAutenticacion conectorAutenticacion, java.lang.String oldHookId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.catalogo.ws.server.ConectorAutenticacion getAuthHook(java.lang.String tramiteId, java.lang.String conectorId, ieci.tecdoc.sgm.core.services.dto.Entidad entidad) throws java.rmi.RemoteException;
}
