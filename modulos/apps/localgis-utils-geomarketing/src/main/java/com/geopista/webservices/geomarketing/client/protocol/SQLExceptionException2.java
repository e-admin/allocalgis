/**
 * SQLExceptionException2.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * SQLExceptionException2.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

package com.geopista.webservices.geomarketing.client.protocol;

public class SQLExceptionException2 extends java.lang.Exception{
    
    private com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.SQLExceptionE faultMessage;
    
    public SQLExceptionException2() {
        super("SQLExceptionException2");
    }
           
    public SQLExceptionException2(java.lang.String s) {
       super(s);
    }
    
    public SQLExceptionException2(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.SQLExceptionE msg){
       faultMessage = msg;
    }
    
    public com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.SQLExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    