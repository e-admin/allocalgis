/**
 * LoadEIELData.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * LoadEIELData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.geopista.app.loadEIELData.service;

public interface LoadEIELData extends java.rmi.Remote {
    public com.geopista.app.loadEIELData.vo.EIELLayer[] getEIELLayerList(java.lang.String lang) throws java.rmi.RemoteException;
    public com.geopista.app.loadEIELData.vo.CompleteEIELLayer loadEIELLayer(java.lang.String codigoIneMunicipio, java.lang.String id, java.lang.String lang_name, java.lang.String table, java.lang.String idField, java.lang.String geometryField) throws java.rmi.RemoteException;
    public com.geopista.app.loadEIELData.vo.EIELLayer[] getEIELLayerList_MAP(java.lang.String idMap, java.lang.String lang) throws java.rmi.RemoteException;
    public com.geopista.app.loadEIELData.vo.EIELMap[] getEIELMapList(java.lang.String lang) throws java.rmi.RemoteException;
}
