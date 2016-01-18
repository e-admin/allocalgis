/**
 * CiService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * CiService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci;

public interface CiService extends javax.xml.rpc.Service {
    public java.lang.String getCiAddress();

    public com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci getCi() throws javax.xml.rpc.ServiceException;

    public com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci getCi(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
