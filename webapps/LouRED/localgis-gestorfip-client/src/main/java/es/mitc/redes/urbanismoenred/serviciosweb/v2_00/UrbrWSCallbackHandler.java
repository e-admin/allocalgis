
/**
 * UrbrWSCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package es.mitc.redes.urbanismoenred.serviciosweb.v2_00;

    /**
     *  UrbrWSCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class UrbrWSCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public UrbrWSCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public UrbrWSCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for determinacionesPadre method
            * override this method for handling normal response from determinacionesPadre operation
            */
           public void receiveResultdeterminacionesPadre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.DeterminacionesPadreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from determinacionesPadre operation
           */
            public void receiveErrordeterminacionesPadre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for fechaRefundidoConDocumentos method
            * override this method for handling normal response from fechaRefundidoConDocumentos operation
            */
           public void receiveResultfechaRefundidoConDocumentos(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.FechaRefundidoConDocumentosResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from fechaRefundidoConDocumentos operation
           */
            public void receiveErrorfechaRefundidoConDocumentos(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for metadatosTramite method
            * override this method for handling normal response from metadatosTramite operation
            */
           public void receiveResultmetadatosTramite(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.MetadatosTramiteResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from metadatosTramite operation
           */
            public void receiveErrormetadatosTramite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for municipiosCallejero method
            * override this method for handling normal response from municipiosCallejero operation
            */
           public void receiveResultmunicipiosCallejero(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.MunicipiosCallejeroResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from municipiosCallejero operation
           */
            public void receiveErrormunicipiosCallejero(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for tiposCalle method
            * override this method for handling normal response from tiposCalle operation
            */
           public void receiveResulttiposCalle(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.TiposCalleResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from tiposCalle operation
           */
            public void receiveErrortiposCalle(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVias method
            * override this method for handling normal response from getVias operation
            */
           public void receiveResultgetVias(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.GetViasResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVias operation
           */
            public void receiveErrorgetVias(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ambitosHijo method
            * override this method for handling normal response from ambitosHijo operation
            */
           public void receiveResultambitosHijo(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.AmbitosHijoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ambitosHijo operation
           */
            public void receiveErrorambitosHijo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ambitosWithPlan method
            * override this method for handling normal response from ambitosWithPlan operation
            */
           public void receiveResultambitosWithPlan(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.AmbitosWithPlanResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ambitosWithPlan operation
           */
            public void receiveErrorambitosWithPlan(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for provinciasCallejero method
            * override this method for handling normal response from provinciasCallejero operation
            */
           public void receiveResultprovinciasCallejero(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.ProvinciasCallejeroResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from provinciasCallejero operation
           */
            public void receiveErrorprovinciasCallejero(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCoordenadas method
            * override this method for handling normal response from getCoordenadas operation
            */
           public void receiveResultgetCoordenadas(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.GetCoordenadasResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCoordenadas operation
           */
            public void receiveErrorgetCoordenadas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for extensionAmbito method
            * override this method for handling normal response from extensionAmbito operation
            */
           public void receiveResultextensionAmbito(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.ExtensionAmbitoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from extensionAmbito operation
           */
            public void receiveErrorextensionAmbito(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultaGraficaExtendida method
            * override this method for handling normal response from consultaGraficaExtendida operation
            */
           public void receiveResultconsultaGraficaExtendida(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.ConsultaGraficaExtendidaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultaGraficaExtendida operation
           */
            public void receiveErrorconsultaGraficaExtendida(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for planesFromNombre method
            * override this method for handling normal response from planesFromNombre operation
            */
           public void receiveResultplanesFromNombre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.PlanesFromNombreResponseE result
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
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.FechaRefundidoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from fechaRefundido operation
           */
            public void receiveErrorfechaRefundido(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for referenciaCatastral method
            * override this method for handling normal response from referenciaCatastral operation
            */
           public void receiveResultreferenciaCatastral(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.ReferenciaCatastralResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from referenciaCatastral operation
           */
            public void receiveErrorreferenciaCatastral(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for geometriaPlan method
            * override this method for handling normal response from geometriaPlan operation
            */
           public void receiveResultgeometriaPlan(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.GeometriaPlanResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from geometriaPlan operation
           */
            public void receiveErrorgeometriaPlan(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for idAmbito method
            * override this method for handling normal response from idAmbito operation
            */
           public void receiveResultidAmbito(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.IdAmbitoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from idAmbito operation
           */
            public void receiveErroridAmbito(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for entidadesFromNombre_Plan method
            * override this method for handling normal response from entidadesFromNombre_Plan operation
            */
           public void receiveResultentidadesFromNombre_Plan(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.EntidadesFromNombre_PlanResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from entidadesFromNombre_Plan operation
           */
            public void receiveErrorentidadesFromNombre_Plan(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for planesPadre method
            * override this method for handling normal response from planesPadre operation
            */
           public void receiveResultplanesPadre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.PlanesPadreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from planesPadre operation
           */
            public void receiveErrorplanesPadre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for geometriaEntidad method
            * override this method for handling normal response from geometriaEntidad operation
            */
           public void receiveResultgeometriaEntidad(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.GeometriaEntidadResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from geometriaEntidad operation
           */
            public void receiveErrorgeometriaEntidad(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ambitosPadre method
            * override this method for handling normal response from ambitosPadre operation
            */
           public void receiveResultambitosPadre(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.AmbitosPadreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ambitosPadre operation
           */
            public void receiveErrorambitosPadre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for extensionEntidad method
            * override this method for handling normal response from extensionEntidad operation
            */
           public void receiveResultextensionEntidad(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.ExtensionEntidadResponseE result
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
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.ConsultaGraficaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultaGrafica operation
           */
            public void receiveErrorconsultaGrafica(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for entidadesFromClave method
            * override this method for handling normal response from entidadesFromClave operation
            */
           public void receiveResultentidadesFromClave(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.EntidadesFromClaveResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from entidadesFromClave operation
           */
            public void receiveErrorentidadesFromClave(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for planesHijo method
            * override this method for handling normal response from planesHijo operation
            */
           public void receiveResultplanesHijo(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.PlanesHijoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from planesHijo operation
           */
            public void receiveErrorplanesHijo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for extensionPlan method
            * override this method for handling normal response from extensionPlan operation
            */
           public void receiveResultextensionPlan(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.ExtensionPlanResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from extensionPlan operation
           */
            public void receiveErrorextensionPlan(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for creaWMSDoc method
            * override this method for handling normal response from creaWMSDoc operation
            */
           public void receiveResultcreaWMSDoc(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.CreaWMSDocResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from creaWMSDoc operation
           */
            public void receiveErrorcreaWMSDoc(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getLeyenda method
            * override this method for handling normal response from getLeyenda operation
            */
           public void receiveResultgetLeyenda(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.GetLeyendaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getLeyenda operation
           */
            public void receiveErrorgetLeyenda(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for determinacionesHijas method
            * override this method for handling normal response from determinacionesHijas operation
            */
           public void receiveResultdeterminacionesHijas(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.DeterminacionesHijasResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from determinacionesHijas operation
           */
            public void receiveErrordeterminacionesHijas(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVersion method
            * override this method for handling normal response from getVersion operation
            */
           public void receiveResultgetVersion(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.GetVersionResponseE result
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
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.MetadatosPlanResponseE result
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
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.EntidadesFromNombreResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from entidadesFromNombre operation
           */
            public void receiveErrorentidadesFromNombre(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for coordenadasRefCatastral method
            * override this method for handling normal response from coordenadasRefCatastral operation
            */
           public void receiveResultcoordenadasRefCatastral(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.CoordenadasRefCatastralResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from coordenadasRefCatastral operation
           */
            public void receiveErrorcoordenadasRefCatastral(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for urlDoc method
            * override this method for handling normal response from urlDoc operation
            */
           public void receiveResulturlDoc(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.UrlDocResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from urlDoc operation
           */
            public void receiveErrorurlDoc(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for estadoDocumentosTramite method
            * override this method for handling normal response from estadoDocumentosTramite operation
            */
           public void receiveResultestadoDocumentosTramite(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.EstadoDocumentosTramiteResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from estadoDocumentosTramite operation
           */
            public void receiveErrorestadoDocumentosTramite(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for gisPrevio method
            * override this method for handling normal response from gisPrevio operation
            */
           public void receiveResultgisPrevio(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.GisPrevioResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from gisPrevio operation
           */
            public void receiveErrorgisPrevio(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for estadoRegistro method
            * override this method for handling normal response from estadoRegistro operation
            */
           public void receiveResultestadoRegistro(
                    es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.EstadoRegistroResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from estadoRegistro operation
           */
            public void receiveErrorestadoRegistro(java.lang.Exception e) {
            }
                


    }
    