
package com.geopista.server.administrador.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "PublishMapsPortType", targetNamespace = "http://web.administrador.server.geopista.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PublishMapsPortType {


    @WebMethod(operationName = "configureLayerAndStylesFile", action = "")
    @WebResult(name = "out", targetNamespace = "http://web.administrador.server.geopista.com")
    public String configureLayerAndStylesFile(
        @WebParam(name = "in0", targetNamespace = "http://web.administrador.server.geopista.com")
        LocalgisLayer in0);

}
