/**
 * PublishMapsPortType.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.apps.administrador.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.geopista.server.administrador.web.LocalgisLayer;

@WebService(name = "PublishMapsPortType", targetNamespace = "http://web.administrador.server.geopista.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PublishMapsPortType {


    @WebMethod(operationName = "configureLayerAndStylesFile", action = "")
    @WebResult(name = "out", targetNamespace = "http://web.administrador.server.geopista.com")
    public String configureLayerAndStylesFile(
        @WebParam(name = "in0", targetNamespace = "http://web.administrador.server.geopista.com")
        LocalgisLayer in0);

}
