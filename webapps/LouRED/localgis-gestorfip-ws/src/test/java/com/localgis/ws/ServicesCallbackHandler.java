
/**
 * ServicesCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

    package com.localgis.ws;

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
            * auto generated Axis2 call back method for installModule method
            * override this method for handling normal response from installModule operation
            */
           public void receiveResultinstallModule(
                    com.localgis.ws.ServicesStub.InstallModuleResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from installModule operation
           */
            public void receiveErrorinstallModule(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSystemVersion method
            * override this method for handling normal response from getSystemVersion operation
            */
           public void receiveResultgetSystemVersion(
                    com.localgis.ws.ServicesStub.GetSystemVersionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSystemVersion operation
           */
            public void receiveErrorgetSystemVersion(java.lang.Exception e) {
            }
                


    }
    