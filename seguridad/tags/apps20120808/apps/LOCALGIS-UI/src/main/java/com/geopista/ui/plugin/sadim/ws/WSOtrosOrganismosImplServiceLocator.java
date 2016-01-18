package com.geopista.ui.plugin.sadim.ws;


public class WSOtrosOrganismosImplServiceLocator extends org.apache.axis.client.Service implements WSOtrosOrganismosImplService {

    public WSOtrosOrganismosImplServiceLocator() {
    }


    public WSOtrosOrganismosImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSOtrosOrganismosImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSOtrosOrganismosImplPort
    private java.lang.String WSOtrosOrganismosImplPort_address = "http://localhost:8080/ModeloWSOtrosOrganismosDemo/WSOtrosOrganismos";

    public java.lang.String getWSOtrosOrganismosImplPortAddress() {
        return WSOtrosOrganismosImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSOtrosOrganismosImplPortWSDDServiceName = "WSOtrosOrganismosImplPort";

    public java.lang.String getWSOtrosOrganismosImplPortWSDDServiceName() {
        return WSOtrosOrganismosImplPortWSDDServiceName;
    }

    public void setWSOtrosOrganismosImplPortWSDDServiceName(java.lang.String name) {
        WSOtrosOrganismosImplPortWSDDServiceName = name;
    }

    public WSOtrosOrganismos getWSOtrosOrganismosImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSOtrosOrganismosImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSOtrosOrganismosImplPort(endpoint);
    }

    public WSOtrosOrganismos getWSOtrosOrganismosImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WSOtrosOrganismosImplServiceSoapBindingStub _stub = new WSOtrosOrganismosImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSOtrosOrganismosImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSOtrosOrganismosImplPortEndpointAddress(java.lang.String address) {
        WSOtrosOrganismosImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (WSOtrosOrganismos.class.isAssignableFrom(serviceEndpointInterface)) {
                WSOtrosOrganismosImplServiceSoapBindingStub _stub = new WSOtrosOrganismosImplServiceSoapBindingStub(new java.net.URL(WSOtrosOrganismosImplPort_address), this);
                _stub.setPortName(getWSOtrosOrganismosImplPortWSDDServiceName());
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
        if ("WSOtrosOrganismosImplPort".equals(inputPortName)) {
            return getWSOtrosOrganismosImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.modelo.sadim/", "WSOtrosOrganismosImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.modelo.sadim/", "WSOtrosOrganismosImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSOtrosOrganismosImplPort".equals(portName)) {
            setWSOtrosOrganismosImplPortEndpointAddress(address);
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
