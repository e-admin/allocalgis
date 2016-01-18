
/**
 * ExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

package es.gestorfip.serviciosweb;

public class ExceptionException0 extends java.lang.Exception{
    
    private es.gestorfip.serviciosweb.ServicesStub.ExceptionE faultMessage;
    
    public ExceptionException0() {
        super("ExceptionException0");
    }
           
    public ExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public ExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(es.gestorfip.serviciosweb.ServicesStub.ExceptionE msg){
       faultMessage = msg;
    }
    
    public es.gestorfip.serviciosweb.ServicesStub.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    