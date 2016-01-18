/**
 * GeoMarketingWSCallbackHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * GeoMarketingWSCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package com.geopista.webservices.geomarketing.client.protocol;

    /**
     *  GeoMarketingWSCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class GeoMarketingWSCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public GeoMarketingWSCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public GeoMarketingWSCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getPortalStepRelations method
            * override this method for handling normal response from getPortalStepRelations operation
            */
           public void receiveResultgetPortalStepRelations(
                    com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GetPortalStepRelationsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPortalStepRelations operation
           */
            public void receiveErrorgetPortalStepRelations(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGeomarketingData method
            * override this method for handling normal response from getGeomarketingData operation
            */
           public void receiveResultgetGeomarketingData(
                    com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GetGeomarketingDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGeomarketingData operation
           */
            public void receiveErrorgetGeomarketingData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGeomarketingAndElementsData method
            * override this method for handling normal response from getGeomarketingAndElementsData operation
            */
           public void receiveResultgetGeomarketingAndElementsData(
                    com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GetGeomarketingAndElementsDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGeomarketingAndElementsData operation
           */
            public void receiveErrorgetGeomarketingAndElementsData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPostalDataFromIdTramosAndIdVia method
            * override this method for handling normal response from getPostalDataFromIdTramosAndIdVia operation
            */
           public void receiveResultgetPostalDataFromIdTramosAndIdVia(
                    com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdViaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPostalDataFromIdTramosAndIdVia operation
           */
            public void receiveErrorgetPostalDataFromIdTramosAndIdVia(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumElements method
            * override this method for handling normal response from getNumElements operation
            */
           public void receiveResultgetNumElements(
                    com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GetNumElementsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumElements operation
           */
            public void receiveErrorgetNumElements(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGeomarketingByIdLayerAndAttributeName method
            * override this method for handling normal response from getGeomarketingByIdLayerAndAttributeName operation
            */
           public void receiveResultgetGeomarketingByIdLayerAndAttributeName(
                    com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGeomarketingByIdLayerAndAttributeName operation
           */
            public void receiveErrorgetGeomarketingByIdLayerAndAttributeName(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGeomarketingAndElementsDataByIdLayerAndAttributeName method
            * override this method for handling normal response from getGeomarketingAndElementsDataByIdLayerAndAttributeName operation
            */
           public void receiveResultgetGeomarketingAndElementsDataByIdLayerAndAttributeName(
                    com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGeomarketingAndElementsDataByIdLayerAndAttributeName operation
           */
            public void receiveErrorgetGeomarketingAndElementsDataByIdLayerAndAttributeName(java.lang.Exception e) {
            }
                


    }
    