/**
 * EntidadesWebService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * EntidadesWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.entidades.ws.server;

public interface EntidadesWebService extends java.rmi.Remote {
    public ieci.tecdoc.sgm.entidades.ws.server.Entidad nuevaEntidad(ieci.tecdoc.sgm.entidades.ws.server.Entidad poEntidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.core.services.dto.RetornoServicio eliminarEntidad(ieci.tecdoc.sgm.entidades.ws.server.Entidad poEntidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.entidades.ws.server.Entidad actualizarEntidad(ieci.tecdoc.sgm.entidades.ws.server.Entidad poEntidad) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.entidades.ws.server.Entidad obtenerEntidad(java.lang.String identificador) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.entidades.ws.server.Entidades buscarEntidades(ieci.tecdoc.sgm.entidades.ws.server.CriterioBusquedaEntidades poCriterio) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.entidades.ws.server.Entidades obtenerEntidades() throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.entidades.ws.server.RetornoCadena obtenerIdentificadorEntidad() throws java.rmi.RemoteException;
}
