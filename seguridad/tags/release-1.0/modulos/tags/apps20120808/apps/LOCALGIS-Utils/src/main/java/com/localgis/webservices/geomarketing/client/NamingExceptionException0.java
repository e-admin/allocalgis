package com.localgis.webservices.geomarketing.client;

public class NamingExceptionException0 extends java.lang.Exception{
    
    private com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.NamingException faultMessage;
    
    public NamingExceptionException0() {
        super("NamingExceptionException0");
    }
           
    public NamingExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public NamingExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.NamingException msg){
       faultMessage = msg;
    }
    
    public com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.NamingException getFaultMessage(){
       return faultMessage;
    }
}
    