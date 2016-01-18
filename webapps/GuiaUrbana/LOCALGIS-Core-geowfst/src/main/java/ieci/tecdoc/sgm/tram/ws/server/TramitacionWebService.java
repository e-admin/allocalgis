/**
 * TramitacionWebService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * TramitacionWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public interface TramitacionWebService extends java.rmi.Remote {
    public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos getProcedimientosPorTipo(java.lang.String idEntidad, int tipoProc, java.lang.String nombre) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos getProcedimientos(java.lang.String idEntidad, java.lang.String[] idProcs) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Procedimiento getProcedimiento(java.lang.String idEntidad, java.lang.String idProc) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Binario getFichero(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.InfoFichero getInfoFichero(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion getInfoOcupacion(java.lang.String idEntidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.RetornoServicio eliminaFicheros(java.lang.String idEntidad, java.lang.String[] guids) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores getIdsExpedientes(java.lang.String idEntidad, java.lang.String idProc, java.util.Calendar fechaIni, java.util.Calendar fechaFin, int tipoOrd) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes getExpedientes(java.lang.String idEntidad, java.lang.String[] idExps) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Expediente getExpediente(java.lang.String idEntidad, java.lang.String idExp) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.RetornoServicio archivarExpedientes(java.lang.String idEntidad, java.lang.String[] idExps) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Booleano iniciarExpediente(java.lang.String idEntidad, ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente datosComunes, java.lang.String datosEspecificos, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Booleano anexarDocsExpediente(java.lang.String idEntidad, java.lang.String numExp, java.lang.String numReg, java.util.Calendar fechaReg, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Cadena crearExpediente(java.lang.String idEntidad, ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente datosComunes, java.lang.String datosEspecificos, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos, java.lang.String initSystem) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Booleano cambiarEstadoAdministrativo(java.lang.String idEntidad, java.lang.String numExp, java.lang.String estadoAdm) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.tram.ws.server.Booleano moverExpedienteAFase(java.lang.String idEntidad, java.lang.String numExp, java.lang.String idFaseCatalogo) throws java.rmi.RemoteException;
    public ieci.tdw.ispac.services.ws.server.Cadena busquedaAvanzada(java.lang.String idEntidad, java.lang.String groupName, java.lang.String searchFormName, java.lang.String searchXML, int domain) throws java.rmi.RemoteException;
    public ieci.tdw.ispac.services.ws.server.Entero establecerDatosRegistroEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp, java.lang.String xmlDatosEspecificos) throws java.rmi.RemoteException;
    public ieci.tdw.ispac.services.ws.server.Cadena obtenerRegistroEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp, int idRegistro) throws java.rmi.RemoteException;
    public ieci.tdw.ispac.services.ws.server.Cadena obtenerRegistrosEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp) throws java.rmi.RemoteException;
}
