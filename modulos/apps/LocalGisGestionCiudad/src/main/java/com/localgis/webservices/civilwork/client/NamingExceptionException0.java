
/**
 * NamingExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

package com.localgis.webservices.civilwork.client;

public class NamingExceptionException0 extends java.lang.Exception{
    
    private com.localgis.webservices.civilwork.client.CivilWorkWSStub.NamingException faultMessage;
    
    public NamingExceptionException0() {
        super("NamingExceptionException0");
    }
           
    public NamingExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public NamingExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.localgis.webservices.civilwork.client.CivilWorkWSStub.NamingException msg){
       faultMessage = msg;
    }
    
    public com.localgis.webservices.civilwork.client.CivilWorkWSStub.NamingException getFaultMessage(){
       return faultMessage;
    }
}
    