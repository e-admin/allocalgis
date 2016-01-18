/**
 * ServicesCallbackHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * ServicesCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package com.geopista.ui.plugin.calculateExpresion.ws.server;

    /**
     *  ServicesCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ServicesCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ServicesCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ServicesCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getTablesNames method
            * override this method for handling normal response from getTablesNames operation
            */
           public void receiveResultgetTablesNames(
                    com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.GetTablesNamesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTablesNames operation
           */
            public void receiveErrorgetTablesNames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getColumnNames method
            * override this method for handling normal response from getColumnNames operation
            */
           public void receiveResultgetColumnNames(
                    com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.GetColumnNamesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getColumnNames operation
           */
            public void receiveErrorgetColumnNames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateDataExpresion method
            * override this method for handling normal response from updateDataExpresion operation
            */
           public void receiveResultupdateDataExpresion(
                    com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.UpdateDataExpresionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateDataExpresion operation
           */
            public void receiveErrorupdateDataExpresion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for validateExpresion method
            * override this method for handling normal response from validateExpresion operation
            */
           public void receiveResultvalidateExpresion(
                    com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.ValidateExpresionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from validateExpresion operation
           */
            public void receiveErrorvalidateExpresion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for validarFicheroExpresion method
            * override this method for handling normal response from validarFicheroExpresion operation
            */
           public void receiveResultvalidarFicheroExpresion(
                    com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.ValidarFicheroExpresionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from validarFicheroExpresion operation
           */
            public void receiveErrorvalidarFicheroExpresion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for testDataExpresion method
            * override this method for handling normal response from testDataExpresion operation
            */
           public void receiveResulttestDataExpresion(
                    com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.TestDataExpresionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from testDataExpresion operation
           */
            public void receiveErrortestDataExpresion(java.lang.Exception e) {
            }
                


    }
    