/**
 * OVCExpedienteLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri.OVCServWeb.OVCExpediente2;

public class OVCExpedienteLocator extends org.apache.axis.client.Service implements org.tempuri.OVCServWeb.OVCExpediente2.OVCExpediente {

    public OVCExpedienteLocator() {
    }


    public OVCExpedienteLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OVCExpedienteLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OVCExpedienteSoap12
    private java.lang.String OVCExpedienteSoap12_address = "https://ovc2.catastro.meh.es/ovcservwebpre/OVCSWExpedientes/OVCExpediente.asmx";

    public java.lang.String getOVCExpedienteSoap12Address() {
        return OVCExpedienteSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OVCExpedienteSoap12WSDDServiceName = "OVCExpedienteSoap12";

    public java.lang.String getOVCExpedienteSoap12WSDDServiceName() {
        return OVCExpedienteSoap12WSDDServiceName;
    }

    public void setOVCExpedienteSoap12WSDDServiceName(java.lang.String name) {
        OVCExpedienteSoap12WSDDServiceName = name;
    }

    public org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap getOVCExpedienteSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OVCExpedienteSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOVCExpedienteSoap12(endpoint);
    }

    public org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap getOVCExpedienteSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap12Stub _stub = new org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap12Stub(portAddress, this);
            _stub.setPortName(getOVCExpedienteSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOVCExpedienteSoap12EndpointAddress(java.lang.String address) {
        OVCExpedienteSoap12_address = address;
    }


    // Use to get a proxy class for OVCExpedienteSoap
    private java.lang.String OVCExpedienteSoap_address = "https://ovc2.catastro.meh.es/ovcservwebpre/OVCSWExpedientes/OVCExpediente.asmx";

    public java.lang.String getOVCExpedienteSoapAddress() {
        return OVCExpedienteSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OVCExpedienteSoapWSDDServiceName = "OVCExpedienteSoap";

    public java.lang.String getOVCExpedienteSoapWSDDServiceName() {
        return OVCExpedienteSoapWSDDServiceName;
    }

    public void setOVCExpedienteSoapWSDDServiceName(java.lang.String name) {
        OVCExpedienteSoapWSDDServiceName = name;
    }

    public org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap getOVCExpedienteSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OVCExpedienteSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOVCExpedienteSoap(endpoint);
    }

    public org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap getOVCExpedienteSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoapStub _stub = new org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoapStub(portAddress, this);
            _stub.setPortName(getOVCExpedienteSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOVCExpedienteSoapEndpointAddress(java.lang.String address) {
        OVCExpedienteSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap12Stub _stub = new org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap12Stub(new java.net.URL(OVCExpedienteSoap12_address), this);
                _stub.setPortName(getOVCExpedienteSoap12WSDDServiceName());
                return _stub;
            }
            if (org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoapStub _stub = new org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoapStub(new java.net.URL(OVCExpedienteSoap_address), this);
                _stub.setPortName(getOVCExpedienteSoapWSDDServiceName());
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
        if ("OVCExpedienteSoap12".equals(inputPortName)) {
            return getOVCExpedienteSoap12();
        }
        else if ("OVCExpedienteSoap".equals(inputPortName)) {
            return getOVCExpedienteSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/OVCServWeb/OVCExpediente2", "OVCExpediente");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/OVCServWeb/OVCExpediente2", "OVCExpedienteSoap12"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/OVCServWeb/OVCExpediente2", "OVCExpedienteSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OVCExpedienteSoap12".equals(portName)) {
            setOVCExpedienteSoap12EndpointAddress(address);
        }
        else 
if ("OVCExpedienteSoap".equals(portName)) {
            setOVCExpedienteSoapEndpointAddress(address);
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
