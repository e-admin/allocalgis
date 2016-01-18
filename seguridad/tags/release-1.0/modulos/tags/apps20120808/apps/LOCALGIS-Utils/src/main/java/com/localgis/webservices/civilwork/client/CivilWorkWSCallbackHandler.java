
/**
 * CivilWorkWSCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package com.localgis.webservices.civilwork.client;

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
            * auto generated Axis2 call back method for cancelIntervention method
            * override this method for handling normal response from cancelIntervention operation
            */
           public void receiveResultcancelIntervention(
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.CancelInterventionResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteInterventionListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidionsResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredInterventionListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteNexDateInterventionListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditionsResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.ChangeInterventionDateResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventionListByConditionsResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNote operation
           */
            public void receiveErrorgetNote(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNoteList method
            * override this method for handling normal response from getNoteList operation
            */
           public void receiveResultgetNoteList(
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumNotesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumNotes operation
           */
            public void receiveErrorgetNumNotes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyNote method
            * override this method for handling normal response from modifyNote operation
            */
           public void receiveResultmodifyNote(
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyNoteResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteInterventionListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventionsResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteNoteResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyInterventionResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNoteListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCompleteNoteList operation
           */
            public void receiveErrorgetCompleteNoteList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteFilteredNoteList method
            * override this method for handling normal response from getCompleteFilteredNoteList operation
            */
           public void receiveResultgetCompleteFilteredNoteList(
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredNoteListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumNoteListByConditionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumNoteListByConditions operation
           */
            public void receiveErrorgetNumNoteListByConditions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCompleteFilteredNextDateInterventionList method
            * override this method for handling normal response from getCompleteFilteredNextDateInterventionList operation
            */
           public void receiveResultgetCompleteFilteredNextDateInterventionList(
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredNextDateInterventionListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInterventionList operation
           */
            public void receiveErrorgetInterventionList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addNote method
            * override this method for handling normal response from addNote operation
            */
           public void receiveResultaddNote(
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddNoteResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetStatisticsResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddInterventionResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNexDateInterventionListResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteNoteListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNumCompleteNoteList operation
           */
            public void receiveErrorgetNumCompleteNoteList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteIntervention method
            * override this method for handling normal response from deleteIntervention operation
            */
           public void receiveResultdeleteIntervention(
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteInterventionResponse result
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
                    com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getIntervention operation
           */
            public void receiveErrorgetIntervention(java.lang.Exception e) {
            }
                


    }
    