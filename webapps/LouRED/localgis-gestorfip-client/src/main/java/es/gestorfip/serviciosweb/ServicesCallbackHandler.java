
/**
 * ServicesCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package es.gestorfip.serviciosweb;

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
            * auto generated Axis2 call back method for obtenerDetValoresReferenciaCondicionUrbanistica method
            * override this method for handling normal response from obtenerDetValoresReferenciaCondicionUrbanistica operation
            */
           public void receiveResultobtenerDetValoresReferenciaCondicionUrbanistica(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDetValoresReferenciaCondicionUrbanisticaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDetValoresReferenciaCondicionUrbanistica operation
           */
            public void receiveErrorobtenerDetValoresReferenciaCondicionUrbanistica(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDatosDeterminacion method
            * override this method for handling normal response from obtenerDatosDeterminacion operation
            */
           public void receiveResultobtenerDatosDeterminacion(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDatosDeterminacionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDatosDeterminacion operation
           */
            public void receiveErrorobtenerDatosDeterminacion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerCaracteresDeterminaciones method
            * override this method for handling normal response from obtenerCaracteresDeterminaciones operation
            */
           public void receiveResultobtenerCaracteresDeterminaciones(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerCaracteresDeterminacionesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerCaracteresDeterminaciones operation
           */
            public void receiveErrorobtenerCaracteresDeterminaciones(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerEntidadesAsociadasToDeterminacion method
            * override this method for handling normal response from obtenerEntidadesAsociadasToDeterminacion operation
            */
           public void receiveResultobtenerEntidadesAsociadasToDeterminacion(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerEntidadesAsociadasToDeterminacionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerEntidadesAsociadasToDeterminacion operation
           */
            public void receiveErrorobtenerEntidadesAsociadasToDeterminacion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDatosIdentificacionFip method
            * override this method for handling normal response from obtenerDatosIdentificacionFip operation
            */
           public void receiveResultobtenerDatosIdentificacionFip(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDatosIdentificacionFipResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDatosIdentificacionFip operation
           */
            public void receiveErrorobtenerDatosIdentificacionFip(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDeterminacionesAsociadasFip_GestorPlaneamiento method
            * override this method for handling normal response from obtenerDeterminacionesAsociadasFip_GestorPlaneamiento operation
            */
           public void receiveResultobtenerDeterminacionesAsociadasFip_GestorPlaneamiento(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDeterminacionesAsociadasFip_GestorPlaneamientoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDeterminacionesAsociadasFip_GestorPlaneamiento operation
           */
            public void receiveErrorobtenerDeterminacionesAsociadasFip_GestorPlaneamiento(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerFechaFipXML method
            * override this method for handling normal response from obtenerFechaFipXML operation
            */
           public void receiveResultobtenerFechaFipXML(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerFechaFipXMLResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerFechaFipXML operation
           */
            public void receiveErrorobtenerFechaFipXML(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDatosTramite method
            * override this method for handling normal response from obtenerDatosTramite operation
            */
           public void receiveResultobtenerDatosTramite(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDatosTramiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDatosTramite operation
           */
            public void receiveErrorobtenerDatosTramite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerLstTiposOperacionesDeterminaciones method
            * override this method for handling normal response from obtenerLstTiposOperacionesDeterminaciones operation
            */
           public void receiveResultobtenerLstTiposOperacionesDeterminaciones(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerLstTiposOperacionesDeterminacionesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerLstTiposOperacionesDeterminaciones operation
           */
            public void receiveErrorobtenerLstTiposOperacionesDeterminaciones(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerLstFips method
            * override this method for handling normal response from obtenerLstFips operation
            */
           public void receiveResultobtenerLstFips(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerLstFipsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerLstFips operation
           */
            public void receiveErrorobtenerLstFips(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerVersionesConsolaUER method
            * override this method for handling normal response from obtenerVersionesConsolaUER operation
            */
           public void receiveResultobtenerVersionesConsolaUER(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerVersionesConsolaUERResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerVersionesConsolaUER operation
           */
            public void receiveErrorobtenerVersionesConsolaUER(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerRegimenesCasoCondicionUrbanistica method
            * override this method for handling normal response from obtenerRegimenesCasoCondicionUrbanistica operation
            */
           public void receiveResultobtenerRegimenesCasoCondicionUrbanistica(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerRegimenesCasoCondicionUrbanisticaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerRegimenesCasoCondicionUrbanistica operation
           */
            public void receiveErrorobtenerRegimenesCasoCondicionUrbanistica(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDatosMigracionAsistida method
            * override this method for handling normal response from obtenerDatosMigracionAsistida operation
            */
           public void receiveResultobtenerDatosMigracionAsistida(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDatosMigracionAsistidaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDatosMigracionAsistida operation
           */
            public void receiveErrorobtenerDatosMigracionAsistida(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerTramitesEncargadoAsociadosFip method
            * override this method for handling normal response from obtenerTramitesEncargadoAsociadosFip operation
            */
           public void receiveResultobtenerTramitesEncargadoAsociadosFip(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerTramitesEncargadoAsociadosFipResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerTramitesEncargadoAsociadosFip operation
           */
            public void receiveErrorobtenerTramitesEncargadoAsociadosFip(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerEntidadesAsociadasFip_GestorPlaneamiento method
            * override this method for handling normal response from obtenerEntidadesAsociadasFip_GestorPlaneamiento operation
            */
           public void receiveResultobtenerEntidadesAsociadasFip_GestorPlaneamiento(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerEntidadesAsociadasFip_GestorPlaneamientoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerEntidadesAsociadasFip_GestorPlaneamiento operation
           */
            public void receiveErrorobtenerEntidadesAsociadasFip_GestorPlaneamiento(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerCRS method
            * override this method for handling normal response from obtenerCRS operation
            */
           public void receiveResultobtenerCRS(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerCRSResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerCRS operation
           */
            public void receiveErrorobtenerCRS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDatosFip method
            * override this method for handling normal response from obtenerDatosFip operation
            */
           public void receiveResultobtenerDatosFip(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDatosFipResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDatosFip operation
           */
            public void receiveErrorobtenerDatosFip(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDeterminacionesAsociadasToEntidad method
            * override this method for handling normal response from obtenerDeterminacionesAsociadasToEntidad operation
            */
           public void receiveResultobtenerDeterminacionesAsociadasToEntidad(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDeterminacionesAsociadasToEntidadResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDeterminacionesAsociadasToEntidad operation
           */
            public void receiveErrorobtenerDeterminacionesAsociadasToEntidad(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerConfigVersionConsolaUER method
            * override this method for handling normal response from obtenerConfigVersionConsolaUER operation
            */
           public void receiveResultobtenerConfigVersionConsolaUER(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerConfigVersionConsolaUERResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerConfigVersionConsolaUER operation
           */
            public void receiveErrorobtenerConfigVersionConsolaUER(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerValorSecuencia method
            * override this method for handling normal response from obtenerValorSecuencia operation
            */
           public void receiveResultobtenerValorSecuencia(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerValorSecuenciaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerValorSecuencia operation
           */
            public void receiveErrorobtenerValorSecuencia(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerAmbitoTrabajo method
            * override this method for handling normal response from obtenerAmbitoTrabajo operation
            */
           public void receiveResultobtenerAmbitoTrabajo(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerAmbitoTrabajoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerAmbitoTrabajo operation
           */
            public void receiveErrorobtenerAmbitoTrabajo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDeterminacionesAsociadasFip method
            * override this method for handling normal response from obtenerDeterminacionesAsociadasFip operation
            */
           public void receiveResultobtenerDeterminacionesAsociadasFip(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDeterminacionesAsociadasFipResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDeterminacionesAsociadasFip operation
           */
            public void receiveErrorobtenerDeterminacionesAsociadasFip(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerArbolEntidadesAsociadasTramite method
            * override this method for handling normal response from obtenerArbolEntidadesAsociadasTramite operation
            */
           public void receiveResultobtenerArbolEntidadesAsociadasTramite(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerArbolEntidadesAsociadasTramiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerArbolEntidadesAsociadasTramite operation
           */
            public void receiveErrorobtenerArbolEntidadesAsociadasTramite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerLstTiposOperacionesEntidades method
            * override this method for handling normal response from obtenerLstTiposOperacionesEntidades operation
            */
           public void receiveResultobtenerLstTiposOperacionesEntidades(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerLstTiposOperacionesEntidadesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerLstTiposOperacionesEntidades operation
           */
            public void receiveErrorobtenerLstTiposOperacionesEntidades(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica method
            * override this method for handling normal response from obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica operation
            */
           public void receiveResultobtenerRegimenEspecificoRegimenCasoCondicionUrbanistica(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerRegimenEspecificoRegimenCasoCondicionUrbanisticaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica operation
           */
            public void receiveErrorobtenerRegimenEspecificoRegimenCasoCondicionUrbanistica(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerFechaConsolidacionFip method
            * override this method for handling normal response from obtenerFechaConsolidacionFip operation
            */
           public void receiveResultobtenerFechaConsolidacionFip(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerFechaConsolidacionFipResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerFechaConsolidacionFip operation
           */
            public void receiveErrorobtenerFechaConsolidacionFip(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerEntidadesAsociadasFip method
            * override this method for handling normal response from obtenerEntidadesAsociadasFip operation
            */
           public void receiveResultobtenerEntidadesAsociadasFip(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerEntidadesAsociadasFipResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerEntidadesAsociadasFip operation
           */
            public void receiveErrorobtenerEntidadesAsociadasFip(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for guardarConfiguracionGestorFip method
            * override this method for handling normal response from guardarConfiguracionGestorFip operation
            */
           public void receiveResultguardarConfiguracionGestorFip(
                    es.gestorfip.serviciosweb.ServicesStub.GuardarConfiguracionGestorFipResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from guardarConfiguracionGestorFip operation
           */
            public void receiveErrorguardarConfiguracionGestorFip(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDetByTipoCaracterCondUrban method
            * override this method for handling normal response from obtenerDetByTipoCaracterCondUrban operation
            */
           public void receiveResultobtenerDetByTipoCaracterCondUrban(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDetByTipoCaracterCondUrbanResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDetByTipoCaracterCondUrban operation
           */
            public void receiveErrorobtenerDetByTipoCaracterCondUrban(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerArbolDeterminacionesAsociadasTramite method
            * override this method for handling normal response from obtenerArbolDeterminacionesAsociadasTramite operation
            */
           public void receiveResultobtenerArbolDeterminacionesAsociadasTramite(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerArbolDeterminacionesAsociadasTramiteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerArbolDeterminacionesAsociadasTramite operation
           */
            public void receiveErrorobtenerArbolDeterminacionesAsociadasTramite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerUnidadDeterminacion method
            * override this method for handling normal response from obtenerUnidadDeterminacion operation
            */
           public void receiveResultobtenerUnidadDeterminacion(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerUnidadDeterminacionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerUnidadDeterminacion operation
           */
            public void receiveErrorobtenerUnidadDeterminacion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertarFIP1 method
            * override this method for handling normal response from insertarFIP1 operation
            */
           public void receiveResultinsertarFIP1(
                    es.gestorfip.serviciosweb.ServicesStub.InsertarFIP1Response result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertarFIP1 operation
           */
            public void receiveErrorinsertarFIP1(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerCasosCondicionUrbanistica method
            * override this method for handling normal response from obtenerCasosCondicionUrbanistica operation
            */
           public void receiveResultobtenerCasosCondicionUrbanistica(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerCasosCondicionUrbanisticaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerCasosCondicionUrbanistica operation
           */
            public void receiveErrorobtenerCasosCondicionUrbanistica(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerDetAplicablesEntidad method
            * override this method for handling normal response from obtenerDetAplicablesEntidad operation
            */
           public void receiveResultobtenerDetAplicablesEntidad(
                    es.gestorfip.serviciosweb.ServicesStub.ObtenerDetAplicablesEntidadResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerDetAplicablesEntidad operation
           */
            public void receiveErrorobtenerDetAplicablesEntidad(java.lang.Exception e) {
            }
                


    }
    