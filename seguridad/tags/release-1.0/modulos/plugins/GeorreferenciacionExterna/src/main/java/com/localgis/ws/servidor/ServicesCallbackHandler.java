
/**
 * ServicesCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package com.localgis.ws.servidor;

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
            * auto generated Axis2 call back method for nombresBbdd method
            * override this method for handling normal response from nombresBbdd operation
            */
           public void receiveResultnombresBbdd(
                    com.localgis.ws.servidor.ServicesStub.NombresBbddResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from nombresBbdd operation
           */
            public void receiveErrornombresBbdd(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for guardarConsulta method
            * override this method for handling normal response from guardarConsulta operation
            */
           public void receiveResultguardarConsulta(
                    com.localgis.ws.servidor.ServicesStub.GuardarConsultaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from guardarConsulta operation
           */
            public void receiveErrorguardarConsulta(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerConexion method
            * override this method for handling normal response from obtenerConexion operation
            */
           public void receiveResultobtenerConexion(
                    com.localgis.ws.servidor.ServicesStub.ObtenerConexionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerConexion operation
           */
            public void receiveErrorobtenerConexion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerColumnas method
            * override this method for handling normal response from obtenerColumnas operation
            */
           public void receiveResultobtenerColumnas(
                    com.localgis.ws.servidor.ServicesStub.ObtenerColumnasResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerColumnas operation
           */
            public void receiveErrorobtenerColumnas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for actualizarConsulta method
            * override this method for handling normal response from actualizarConsulta operation
            */
           public void receiveResultactualizarConsulta(
                    com.localgis.ws.servidor.ServicesStub.ActualizarConsultaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from actualizarConsulta operation
           */
            public void receiveErroractualizarConsulta(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDatos method
            * override this method for handling normal response from obtenerDatos operation
            */
           public void receiveResultobtenerDatos(
                    com.localgis.ws.servidor.ServicesStub.ObtenerDatosResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDatos operation
           */
            public void receiveErrorobtenerDatos(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerTotalFilas method
            * override this method for handling normal response from obtenerTotalFilas operation
            */
           public void receiveResultobtenerTotalFilas(
                    com.localgis.ws.servidor.ServicesStub.ObtenerTotalFilasResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerTotalFilas operation
           */
            public void receiveErrorobtenerTotalFilas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerTablas method
            * override this method for handling normal response from obtenerTablas operation
            */
           public void receiveResultobtenerTablas(
                    com.localgis.ws.servidor.ServicesStub.ObtenerTablasResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerTablas operation
           */
            public void receiveErrorobtenerTablas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerConsultas method
            * override this method for handling normal response from obtenerConsultas operation
            */
           public void receiveResultobtenerConsultas(
                    com.localgis.ws.servidor.ServicesStub.ObtenerConsultasResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerConsultas operation
           */
            public void receiveErrorobtenerConsultas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for borrarConsuta method
            * override this method for handling normal response from borrarConsuta operation
            */
           public void receiveResultborrarConsuta(
                    com.localgis.ws.servidor.ServicesStub.BorrarConsutaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from borrarConsuta operation
           */
            public void receiveErrorborrarConsuta(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerTablasBBDDLocalGis method
            * override this method for handling normal response from obtenerTablasBBDDLocalGis operation
            */
           public void receiveResultobtenerTablasBBDDLocalGis(
                    com.localgis.ws.servidor.ServicesStub.ObtenerTablasBBDDLocalGisResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerTablasBBDDLocalGis operation
           */
            public void receiveErrorobtenerTablasBBDDLocalGis(java.lang.Exception e) {
            }
                


    }
    