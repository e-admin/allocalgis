

/**
 * OVCExpedienteTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */
    package com.ws.servidor;

    /*
     *  OVCExpedienteTest Junit test case
    */

    public class OVCExpedienteTest extends junit.framework.TestCase{

     
        /**
         * Auto generated test method
         */
        public  void testGeneraExpediente() throws java.lang.Exception{

        com.ws.servidor.OVCExpedienteStub stub =
                    new com.ws.servidor.OVCExpedienteStub();//the default implementation should point to the right endpoint

           com.ws.servidor.OVCExpedienteStub.Expedientes_In expedientes_In16=
                                                        (com.ws.servidor.OVCExpedienteStub.Expedientes_In)getTestObject(com.ws.servidor.OVCExpedienteStub.Expedientes_In.class);
                    // TODO : Fill in the expedientes_In16 here
                
                        assertNotNull(stub.GeneraExpediente(
                        expedientes_In16));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartGeneraExpediente() throws java.lang.Exception{
            com.ws.servidor.OVCExpedienteStub stub = new com.ws.servidor.OVCExpedienteStub();
             com.ws.servidor.OVCExpedienteStub.Expedientes_In expedientes_In16=
                                                        (com.ws.servidor.OVCExpedienteStub.Expedientes_In)getTestObject(com.ws.servidor.OVCExpedienteStub.Expedientes_In.class);
                    // TODO : Fill in the expedientes_In16 here
                

                stub.startGeneraExpediente(
                         expedientes_In16,
                    new tempCallbackN65548()
                );
              


        }

        private class tempCallbackN65548  extends com.ws.servidor.OVCExpedienteCallbackHandler{
            public tempCallbackN65548(){ super(null);}

            public void receiveResultGeneraExpediente(
                         com.ws.servidor.OVCExpedienteStub.Expedientes_Out result
                            ) {
                
            }

            public void receiveErrorGeneraExpediente(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testactualizaCatastro() throws java.lang.Exception{

        com.ws.servidor.OVCExpedienteStub stub =
                    new com.ws.servidor.OVCExpedienteStub();//the default implementation should point to the right endpoint

           com.ws.servidor.OVCExpedienteStub.ActualizaCatastro_In actualizaCatastro_In18=
                                                        (com.ws.servidor.OVCExpedienteStub.ActualizaCatastro_In)getTestObject(com.ws.servidor.OVCExpedienteStub.ActualizaCatastro_In.class);
                    // TODO : Fill in the actualizaCatastro_In18 here
                
                        assertNotNull(stub.actualizaCatastro(
                        actualizaCatastro_In18));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartactualizaCatastro() throws java.lang.Exception{
            com.ws.servidor.OVCExpedienteStub stub = new com.ws.servidor.OVCExpedienteStub();
             com.ws.servidor.OVCExpedienteStub.ActualizaCatastro_In actualizaCatastro_In18=
                                                        (com.ws.servidor.OVCExpedienteStub.ActualizaCatastro_In)getTestObject(com.ws.servidor.OVCExpedienteStub.ActualizaCatastro_In.class);
                    // TODO : Fill in the actualizaCatastro_In18 here
                

                stub.startactualizaCatastro(
                         actualizaCatastro_In18,
                    new tempCallbackN65586()
                );
              


        }

        private class tempCallbackN65586  extends com.ws.servidor.OVCExpedienteCallbackHandler{
            public tempCallbackN65586(){ super(null);}

            public void receiveResultactualizaCatastro(
                         com.ws.servidor.OVCExpedienteStub.ActualizaCatastro_Out result
                            ) {
                
            }

            public void receiveErroractualizaCatastro(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testconsultaExpediente() throws java.lang.Exception{

        com.ws.servidor.OVCExpedienteStub stub =
                    new com.ws.servidor.OVCExpedienteStub();//the default implementation should point to the right endpoint

           com.ws.servidor.OVCExpedienteStub.ConsultaExpediente_In consultaExpediente_In20=
                                                        (com.ws.servidor.OVCExpedienteStub.ConsultaExpediente_In)getTestObject(com.ws.servidor.OVCExpedienteStub.ConsultaExpediente_In.class);
                    // TODO : Fill in the consultaExpediente_In20 here
                
                        assertNotNull(stub.consultaExpediente(
                        consultaExpediente_In20));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartconsultaExpediente() throws java.lang.Exception{
            com.ws.servidor.OVCExpedienteStub stub = new com.ws.servidor.OVCExpedienteStub();
             com.ws.servidor.OVCExpedienteStub.ConsultaExpediente_In consultaExpediente_In20=
                                                        (com.ws.servidor.OVCExpedienteStub.ConsultaExpediente_In)getTestObject(com.ws.servidor.OVCExpedienteStub.ConsultaExpediente_In.class);
                    // TODO : Fill in the consultaExpediente_In20 here
                

                stub.startconsultaExpediente(
                         consultaExpediente_In20,
                    new tempCallbackN65624()
                );
              


        }

        private class tempCallbackN65624  extends com.ws.servidor.OVCExpedienteCallbackHandler{
            public tempCallbackN65624(){ super(null);}

            public void receiveResultconsultaExpediente(
                         com.ws.servidor.OVCExpedienteStub.ConsultaExpediente_Out result
                            ) {
                
            }

            public void receiveErrorconsultaExpediente(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testConsultaCatastro() throws java.lang.Exception{

        com.ws.servidor.OVCExpedienteStub stub =
                    new com.ws.servidor.OVCExpedienteStub();//the default implementation should point to the right endpoint

           com.ws.servidor.OVCExpedienteStub.ConsultaCatastro_In consultaCatastro_In22=
                                                        (com.ws.servidor.OVCExpedienteStub.ConsultaCatastro_In)getTestObject(com.ws.servidor.OVCExpedienteStub.ConsultaCatastro_In.class);
                    // TODO : Fill in the consultaCatastro_In22 here
                
                        assertNotNull(stub.ConsultaCatastro(
                        consultaCatastro_In22));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartConsultaCatastro() throws java.lang.Exception{
            com.ws.servidor.OVCExpedienteStub stub = new com.ws.servidor.OVCExpedienteStub();
             com.ws.servidor.OVCExpedienteStub.ConsultaCatastro_In consultaCatastro_In22=
                                                        (com.ws.servidor.OVCExpedienteStub.ConsultaCatastro_In)getTestObject(com.ws.servidor.OVCExpedienteStub.ConsultaCatastro_In.class);
                    // TODO : Fill in the consultaCatastro_In22 here
                

                stub.startConsultaCatastro(
                         consultaCatastro_In22,
                    new tempCallbackN65662()
                );
              


        }

        private class tempCallbackN65662  extends com.ws.servidor.OVCExpedienteCallbackHandler{
            public tempCallbackN65662(){ super(null);}

            public void receiveResultConsultaCatastro(
                         com.ws.servidor.OVCExpedienteStub.ConsultaCatastro_Out result
                            ) {
                
            }

            public void receiveErrorConsultaCatastro(java.lang.Exception e) {
                fail();
            }

        }
      
        //Create an ADBBean and provide it as the test object
        public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
        }

        
        

    }
    