
/**
 * SQLExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

package com.localgis.ws.servidor;

public class SQLExceptionException0 extends java.lang.Exception{
    
    private com.localgis.ws.servidor.ServicesStub.SQLExceptionE faultMessage;
    
    public SQLExceptionException0() {
        super("SQLExceptionException0");
    }
           
    public SQLExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public SQLExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.localgis.ws.servidor.ServicesStub.SQLExceptionE msg){
       faultMessage = msg;
    }
    
    public com.localgis.ws.servidor.ServicesStub.SQLExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    