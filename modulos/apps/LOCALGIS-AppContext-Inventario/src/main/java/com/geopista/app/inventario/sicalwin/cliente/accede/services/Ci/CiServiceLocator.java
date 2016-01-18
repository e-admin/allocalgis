/**
 * CiServiceLocator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * CiServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci;

public class CiServiceLocator extends org.apache.axis.client.Service implements com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiService {

    public CiServiceLocator() {
    }


    public CiServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CiServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Ci
    private java.lang.String Ci_address = "http://mdlscwtest:8080/accede/services/Ci?wsdl";

    public java.lang.String getCiAddress() {
        return Ci_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CiWSDDServiceName = "Ci";

    public java.lang.String getCiWSDDServiceName() {
        return CiWSDDServiceName;
    }

    public void setCiWSDDServiceName(java.lang.String name) {
        CiWSDDServiceName = name;
    }

    public com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci getCi() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Ci_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCi(endpoint);
    }

    public com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci getCi(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiSoapBindingStub _stub = new com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiSoapBindingStub(portAddress, this);
            _stub.setPortName(getCiWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCiEndpointAddress(java.lang.String address) {
        Ci_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.Ci.class.isAssignableFrom(serviceEndpointInterface)) {
                com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiSoapBindingStub _stub = new com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiSoapBindingStub(new java.net.URL(Ci_address), this);
                _stub.setPortName(getCiWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Ci".equals(inputPortName)) {
            return getCi();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://mdlscwtest:8080/accede/services/Ci?wsdl", "CiService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://mdlscwtest:8080/accede/services/Ci?wsdl", "Ci"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Ci".equals(portName)) {
            setCiEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
