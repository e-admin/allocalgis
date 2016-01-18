
/**
 * UrbrWSServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package es.mitc.redes.urbanismoenred.serviciosweb.v1_86;

    /**
     *  UrbrWSServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class UrbrWSServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public UrbrWSServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public UrbrWSServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for GetLeyenda method
            * override this method for handling normal response from GetLeyenda operation
            */
           public void receiveResultGetLeyenda(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.GetLeyendaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetLeyenda operation
           */
            public void receiveErrorGetLeyenda(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for metadatosTramite method
            * override this method for handling normal response from metadatosTramite operation
            */
           public void receiveResultmetadatosTramite(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.MetadatosTramiteResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from metadatosTramite operation
           */
            public void receiveErrormetadatosTramite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetVias method
            * override this method for handling normal response from GetVias operation
            */
           public void receiveResultGetVias(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.GetViasResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetVias operation
           */
            public void receiveErrorGetVias(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for AmbitosWithPlan method
            * override this method for handling normal response from AmbitosWithPlan operation
            */
           public void receiveResultAmbitosWithPlan(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.AmbitosWithPlanResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from AmbitosWithPlan operation
           */
            public void receiveErrorAmbitosWithPlan(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for extensionAmbito method
            * override this method for handling normal response from extensionAmbito operation
            */
           public void receiveResultextensionAmbito(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.ExtensionAmbitoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from extensionAmbito operation
           */
            public void receiveErrorextensionAmbito(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DeterminacionesPadre method
            * override this method for handling normal response from DeterminacionesPadre operation
            */
           public void receiveResultDeterminacionesPadre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.DeterminacionesPadreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DeterminacionesPadre operation
           */
            public void receiveErrorDeterminacionesPadre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for AmbitosPadre method
            * override this method for handling normal response from AmbitosPadre operation
            */
           public void receiveResultAmbitosPadre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.AmbitosPadreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from AmbitosPadre operation
           */
            public void receiveErrorAmbitosPadre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for planesFromNombre method
            * override this method for handling normal response from planesFromNombre operation
            */
           public void receiveResultplanesFromNombre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.PlanesFromNombreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from planesFromNombre operation
           */
            public void receiveErrorplanesFromNombre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for fechaRefundido method
            * override this method for handling normal response from fechaRefundido operation
            */
           public void receiveResultfechaRefundido(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.FechaRefundidoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from fechaRefundido operation
           */
            public void receiveErrorfechaRefundido(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GISPrevio method
            * override this method for handling normal response from GISPrevio operation
            */
           public void receiveResultGISPrevio(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.GISPrevioResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GISPrevio operation
           */
            public void receiveErrorGISPrevio(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlanesHijo method
            * override this method for handling normal response from PlanesHijo operation
            */
           public void receiveResultPlanesHijo(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.PlanesHijoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlanesHijo operation
           */
            public void receiveErrorPlanesHijo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetCoordenadas method
            * override this method for handling normal response from GetCoordenadas operation
            */
           public void receiveResultGetCoordenadas(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.GetCoordenadasResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetCoordenadas operation
           */
            public void receiveErrorGetCoordenadas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ProvinciasCallejero method
            * override this method for handling normal response from ProvinciasCallejero operation
            */
           public void receiveResultProvinciasCallejero(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.ProvinciasCallejeroResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ProvinciasCallejero operation
           */
            public void receiveErrorProvinciasCallejero(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for CreaWMSDoc method
            * override this method for handling normal response from CreaWMSDoc operation
            */
           public void receiveResultCreaWMSDoc(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.CreaWMSDocResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from CreaWMSDoc operation
           */
            public void receiveErrorCreaWMSDoc(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for extensionEntidad method
            * override this method for handling normal response from extensionEntidad operation
            */
           public void receiveResultextensionEntidad(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.ExtensionEntidadResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from extensionEntidad operation
           */
            public void receiveErrorextensionEntidad(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultaGrafica method
            * override this method for handling normal response from consultaGrafica operation
            */
           public void receiveResultconsultaGrafica(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.ConsultaGraficaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultaGrafica operation
           */
            public void receiveErrorconsultaGrafica(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ReferenciaCatastral method
            * override this method for handling normal response from ReferenciaCatastral operation
            */
           public void receiveResultReferenciaCatastral(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.ReferenciaCatastralResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ReferenciaCatastral operation
           */
            public void receiveErrorReferenciaCatastral(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for IdAmbito method
            * override this method for handling normal response from IdAmbito operation
            */
           public void receiveResultIdAmbito(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.IdAmbitoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from IdAmbito operation
           */
            public void receiveErrorIdAmbito(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DeterminacionesHijas method
            * override this method for handling normal response from DeterminacionesHijas operation
            */
           public void receiveResultDeterminacionesHijas(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.DeterminacionesHijasResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DeterminacionesHijas operation
           */
            public void receiveErrorDeterminacionesHijas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for entidadesFromClave method
            * override this method for handling normal response from entidadesFromClave operation
            */
           public void receiveResultentidadesFromClave(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.EntidadesFromClaveResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from entidadesFromClave operation
           */
            public void receiveErrorentidadesFromClave(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlanesPadre method
            * override this method for handling normal response from PlanesPadre operation
            */
           public void receiveResultPlanesPadre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.PlanesPadreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlanesPadre operation
           */
            public void receiveErrorPlanesPadre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for extensionPlan method
            * override this method for handling normal response from extensionPlan operation
            */
           public void receiveResultextensionPlan(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.ExtensionPlanResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from extensionPlan operation
           */
            public void receiveErrorextensionPlan(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for URLDoc method
            * override this method for handling normal response from URLDoc operation
            */
           public void receiveResultURLDoc(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.URLDocResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from URLDoc operation
           */
            public void receiveErrorURLDoc(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for CoordenadasRefCatastral method
            * override this method for handling normal response from CoordenadasRefCatastral operation
            */
           public void receiveResultCoordenadasRefCatastral(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.CoordenadasRefCatastralResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from CoordenadasRefCatastral operation
           */
            public void receiveErrorCoordenadasRefCatastral(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for MunicipiosCallejero method
            * override this method for handling normal response from MunicipiosCallejero operation
            */
           public void receiveResultMunicipiosCallejero(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.MunicipiosCallejeroResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from MunicipiosCallejero operation
           */
            public void receiveErrorMunicipiosCallejero(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVersion method
            * override this method for handling normal response from getVersion operation
            */
           public void receiveResultgetVersion(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.GetVersionResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVersion operation
           */
            public void receiveErrorgetVersion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for metadatosPlan method
            * override this method for handling normal response from metadatosPlan operation
            */
           public void receiveResultmetadatosPlan(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.MetadatosPlanResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from metadatosPlan operation
           */
            public void receiveErrormetadatosPlan(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for entidadesFromNombre method
            * override this method for handling normal response from entidadesFromNombre operation
            */
           public void receiveResultentidadesFromNombre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.EntidadesFromNombreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from entidadesFromNombre operation
           */
            public void receiveErrorentidadesFromNombre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for TiposCalle method
            * override this method for handling normal response from TiposCalle operation
            */
           public void receiveResultTiposCalle(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.TiposCalleResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from TiposCalle operation
           */
            public void receiveErrorTiposCalle(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for estadoDocumentosTramite method
            * override this method for handling normal response from estadoDocumentosTramite operation
            */
           public void receiveResultestadoDocumentosTramite(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.EstadoDocumentosTramiteResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from estadoDocumentosTramite operation
           */
            public void receiveErrorestadoDocumentosTramite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for estadoRegistro method
            * override this method for handling normal response from estadoRegistro operation
            */
           public void receiveResultestadoRegistro(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.EstadoRegistroResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from estadoRegistro operation
           */
            public void receiveErrorestadoRegistro(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for AmbitosHijo method
            * override this method for handling normal response from AmbitosHijo operation
            */
           public void receiveResultAmbitosHijo(
                    es.mitc.redes.urbanismoenred.serviciosweb.v1_86.UrbrWSServiceStub.AmbitosHijoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from AmbitosHijo operation
           */
            public void receiveErrorAmbitosHijo(java.lang.Exception e) {
            }
                


    }
    