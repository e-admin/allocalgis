/**
 * SQLExceptionException0.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * SQLExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

package com.localgis.ws.georreferenciaexterna.client.protocol;

public class SQLExceptionException0 extends java.lang.Exception{
    
    private com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.SQLExceptionE faultMessage;
    
    public SQLExceptionException0() {
        super("SQLExceptionException0");
    }
           
    public SQLExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public SQLExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.SQLExceptionE msg){
       faultMessage = msg;
    }
    
    public com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.SQLExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    