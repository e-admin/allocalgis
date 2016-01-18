
/**
 * ACExceptionException1.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

package com.localgis.webservices.geomarketing.client;

public class ACExceptionException1 extends java.lang.Exception{
    
    private com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.ACExceptionE faultMessage;
    
    public ACExceptionException1() {
        super("ACExceptionException1");
    }
           
    public ACExceptionException1(java.lang.String s) {
       super(s);
    }
    
    public ACExceptionException1(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.ACExceptionE msg){
       faultMessage = msg;
    }
    
    public com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.ACExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    