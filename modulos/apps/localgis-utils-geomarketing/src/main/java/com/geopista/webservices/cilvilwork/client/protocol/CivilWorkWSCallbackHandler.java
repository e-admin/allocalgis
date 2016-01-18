/**
 * CivilWorkWSCallbackHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * CivilWorkWSCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package com.geopista.webservices.cilvilwork.client.protocol;

    /**
     *  CivilWorkWSCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class CivilWorkWSCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public CivilWorkWSCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public CivilWorkWSCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getPlanesObra method
            * override this method for handling normal response from getPlanesObra operation
            */
           public void receiveResultgetPlanesObra(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetPlanesObraResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPlanesObra operation
           */
            public void receiveErrorgetPlanesObra(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPlanesObraList method
            * override this method for handling normal response from getPlanesObraList operation
            */
           public void receiveResultgetPlanesObraList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetPlanesObraListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPlanesObraList operation
           */
            public void receiveErrorgetPlanesObraList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addPlanesObra method
            * override this method for handling normal response from addPlanesObra operation
            */
           public void receiveResultaddPlanesObra(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.AddPlanesObraResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addPlanesObra operation
           */
            public void receiveErroraddPlanesObra(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelIntervention method
            * override this method for handling normal response from cancelIntervention operation
            */
           public void receiveResultcancelIntervention(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.CancelInterventionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelIntervention operation
           */
            public void receiveErrorcancelIntervention(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteInterventionList method
            * override this method for handling normal response from getCompleteInterventionList operation
            */
           public void receiveResultgetCompleteInterventionList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompleteInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteInterventionList operation
           */
            public void receiveErrorgetCompleteInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNoteListByContidions method
            * override this method for handling normal response from getNoteListByContidions operation
            */
           public void receiveResultgetNoteListByContidions(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNoteListByContidionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNoteListByContidions operation
           */
            public void receiveErrorgetNoteListByContidions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteFilteredInterventionList method
            * override this method for handling normal response from getCompleteFilteredInterventionList operation
            */
           public void receiveResultgetCompleteFilteredInterventionList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompleteFilteredInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteFilteredInterventionList operation
           */
            public void receiveErrorgetCompleteFilteredInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumCompleteNexDateInterventionList method
            * override this method for handling normal response from getNumCompleteNexDateInterventionList operation
            */
           public void receiveResultgetNumCompleteNexDateInterventionList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumCompleteNexDateInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumCompleteNexDateInterventionList operation
           */
            public void receiveErrorgetNumCompleteNexDateInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getInterventionListByConditions method
            * override this method for handling normal response from getInterventionListByConditions operation
            */
           public void receiveResultgetInterventionListByConditions(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetInterventionListByConditionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInterventionListByConditions operation
           */
            public void receiveErrorgetInterventionListByConditions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changeInterventionDate method
            * override this method for handling normal response from changeInterventionDate operation
            */
           public void receiveResultchangeInterventionDate(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.ChangeInterventionDateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changeInterventionDate operation
           */
            public void receiveErrorchangeInterventionDate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumInterventionListByConditions method
            * override this method for handling normal response from getNumInterventionListByConditions operation
            */
           public void receiveResultgetNumInterventionListByConditions(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumInterventionListByConditionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumInterventionListByConditions operation
           */
            public void receiveErrorgetNumInterventionListByConditions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNote method
            * override this method for handling normal response from getNote operation
            */
           public void receiveResultgetNote(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNoteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNote operation
           */
            public void receiveErrorgetNote(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumPlanesObraListByConditions method
            * override this method for handling normal response from getNumPlanesObraListByConditions operation
            */
           public void receiveResultgetNumPlanesObraListByConditions(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumPlanesObraListByConditionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumPlanesObraListByConditions operation
           */
            public void receiveErrorgetNumPlanesObraListByConditions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNoteList method
            * override this method for handling normal response from getNoteList operation
            */
           public void receiveResultgetNoteList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNoteListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNoteList operation
           */
            public void receiveErrorgetNoteList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumNotes method
            * override this method for handling normal response from getNumNotes operation
            */
           public void receiveResultgetNumNotes(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumNotesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumNotes operation
           */
            public void receiveErrorgetNumNotes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumPlanesObra method
            * override this method for handling normal response from getNumPlanesObra operation
            */
           public void receiveResultgetNumPlanesObra(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumPlanesObraResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumPlanesObra operation
           */
            public void receiveErrorgetNumPlanesObra(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyNote method
            * override this method for handling normal response from modifyNote operation
            */
           public void receiveResultmodifyNote(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.ModifyNoteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyNote operation
           */
            public void receiveErrormodifyNote(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumCompleteInterventionList method
            * override this method for handling normal response from getNumCompleteInterventionList operation
            */
           public void receiveResultgetNumCompleteInterventionList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumCompleteInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumCompleteInterventionList operation
           */
            public void receiveErrorgetNumCompleteInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumInterventions method
            * override this method for handling normal response from getNumInterventions operation
            */
           public void receiveResultgetNumInterventions(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumInterventionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumInterventions operation
           */
            public void receiveErrorgetNumInterventions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteNote method
            * override this method for handling normal response from deleteNote operation
            */
           public void receiveResultdeleteNote(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.DeleteNoteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteNote operation
           */
            public void receiveErrordeleteNote(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyIntervention method
            * override this method for handling normal response from modifyIntervention operation
            */
           public void receiveResultmodifyIntervention(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.ModifyInterventionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyIntervention operation
           */
            public void receiveErrormodifyIntervention(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteNoteList method
            * override this method for handling normal response from getCompleteNoteList operation
            */
           public void receiveResultgetCompleteNoteList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompleteNoteListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteNoteList operation
           */
            public void receiveErrorgetCompleteNoteList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deletePlanesObra method
            * override this method for handling normal response from deletePlanesObra operation
            */
           public void receiveResultdeletePlanesObra(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.DeletePlanesObraResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deletePlanesObra operation
           */
            public void receiveErrordeletePlanesObra(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPlanesObraListByConditions method
            * override this method for handling normal response from getPlanesObraListByConditions operation
            */
           public void receiveResultgetPlanesObraListByConditions(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetPlanesObraListByConditionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPlanesObraListByConditions operation
           */
            public void receiveErrorgetPlanesObraListByConditions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteFilteredNoteList method
            * override this method for handling normal response from getCompleteFilteredNoteList operation
            */
           public void receiveResultgetCompleteFilteredNoteList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompleteFilteredNoteListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteFilteredNoteList operation
           */
            public void receiveErrorgetCompleteFilteredNoteList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumNoteListByConditions method
            * override this method for handling normal response from getNumNoteListByConditions operation
            */
           public void receiveResultgetNumNoteListByConditions(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumNoteListByConditionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumNoteListByConditions operation
           */
            public void receiveErrorgetNumNoteListByConditions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyPlanesObra method
            * override this method for handling normal response from modifyPlanesObra operation
            */
           public void receiveResultmodifyPlanesObra(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.ModifyPlanesObraResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyPlanesObra operation
           */
            public void receiveErrormodifyPlanesObra(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteFilteredNextDateInterventionList method
            * override this method for handling normal response from getCompleteFilteredNextDateInterventionList operation
            */
           public void receiveResultgetCompleteFilteredNextDateInterventionList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompleteFilteredNextDateInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteFilteredNextDateInterventionList operation
           */
            public void receiveErrorgetCompleteFilteredNextDateInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getInterventionList method
            * override this method for handling normal response from getInterventionList operation
            */
           public void receiveResultgetInterventionList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInterventionList operation
           */
            public void receiveErrorgetInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteFilteredPlanesObraList method
            * override this method for handling normal response from getCompleteFilteredPlanesObraList operation
            */
           public void receiveResultgetCompleteFilteredPlanesObraList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompleteFilteredPlanesObraListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteFilteredPlanesObraList operation
           */
            public void receiveErrorgetCompleteFilteredPlanesObraList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompletePlanesObraList method
            * override this method for handling normal response from getCompletePlanesObraList operation
            */
           public void receiveResultgetCompletePlanesObraList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompletePlanesObraListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompletePlanesObraList operation
           */
            public void receiveErrorgetCompletePlanesObraList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addNote method
            * override this method for handling normal response from addNote operation
            */
           public void receiveResultaddNote(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.AddNoteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addNote operation
           */
            public void receiveErroraddNote(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getStatistics method
            * override this method for handling normal response from getStatistics operation
            */
           public void receiveResultgetStatistics(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetStatisticsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getStatistics operation
           */
            public void receiveErrorgetStatistics(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addIntervention method
            * override this method for handling normal response from addIntervention operation
            */
           public void receiveResultaddIntervention(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.AddInterventionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addIntervention operation
           */
            public void receiveErroraddIntervention(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteNexDateInterventionList method
            * override this method for handling normal response from getCompleteNexDateInterventionList operation
            */
           public void receiveResultgetCompleteNexDateInterventionList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetCompleteNexDateInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteNexDateInterventionList operation
           */
            public void receiveErrorgetCompleteNexDateInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumCompleteNoteList method
            * override this method for handling normal response from getNumCompleteNoteList operation
            */
           public void receiveResultgetNumCompleteNoteList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumCompleteNoteListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumCompleteNoteList operation
           */
            public void receiveErrorgetNumCompleteNoteList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNumCompletePlanesObraList method
            * override this method for handling normal response from getNumCompletePlanesObraList operation
            */
           public void receiveResultgetNumCompletePlanesObraList(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetNumCompletePlanesObraListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumCompletePlanesObraList operation
           */
            public void receiveErrorgetNumCompletePlanesObraList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteIntervention method
            * override this method for handling normal response from deleteIntervention operation
            */
           public void receiveResultdeleteIntervention(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.DeleteInterventionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteIntervention operation
           */
            public void receiveErrordeleteIntervention(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getIntervention method
            * override this method for handling normal response from getIntervention operation
            */
           public void receiveResultgetIntervention(
                    com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub.GetInterventionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getIntervention operation
           */
            public void receiveErrorgetIntervention(java.lang.Exception e) {
            }
                


    }
    