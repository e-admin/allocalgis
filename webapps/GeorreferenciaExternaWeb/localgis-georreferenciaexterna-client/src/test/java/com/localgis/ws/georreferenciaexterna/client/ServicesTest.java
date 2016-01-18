/**
 * ServicesTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * ServicesTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */
    package com.localgis.ws.georreferenciaexterna.client;

    /*
     *  ServicesTest Junit test case
    */

    public class ServicesTest extends junit.framework.TestCase{

     
        /**
         * Auto generated test method
         */
        public  void testnombresBbdd() throws java.lang.Exception{

        com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub =
                    new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();//the default implementation should point to the right endpoint

           
                        assertNotNull(stub.nombresBbdd(
                        ));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartnombresBbdd() throws java.lang.Exception{
            com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub = new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();
             

                stub.startnombresBbdd(
                         
                    new tempCallbackN65548()
                );
              


        }

        private class tempCallbackN65548  extends com.localgis.ws.georreferenciaexterna.client.protocol.ServicesCallbackHandler{
            public tempCallbackN65548(){ super(null);}

            public void receiveResultnombresBbdd(
                         com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.NombresBbddResponse result
                            ) {
                
            }

            public void receiveErrornombresBbdd(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testobtenerConexion() throws java.lang.Exception{

        com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub =
                    new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();//the default implementation should point to the right endpoint

           com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerConexion obtenerConexion32=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerConexion)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerConexion.class);
                    // TODO : Fill in the obtenerConexion32 here
                
                        assertNotNull(stub.obtenerConexion(
                        obtenerConexion32));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartobtenerConexion() throws java.lang.Exception{
            com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub = new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();
             com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerConexion obtenerConexion32=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerConexion)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerConexion.class);
                    // TODO : Fill in the obtenerConexion32 here
                

                stub.startobtenerConexion(
                         obtenerConexion32,
                    new tempCallbackN65583()
                );
              


        }

        private class tempCallbackN65583  extends com.localgis.ws.georreferenciaexterna.client.protocol.ServicesCallbackHandler{
            public tempCallbackN65583(){ super(null);}

            public void receiveResultobtenerConexion(
                         com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerConexionResponse result
                            ) {
                
            }

            public void receiveErrorobtenerConexion(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testobtenerColumnas() throws java.lang.Exception{

        com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub =
                    new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();//the default implementation should point to the right endpoint

           com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerColumnas obtenerColumnas34=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerColumnas)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerColumnas.class);
                    // TODO : Fill in the obtenerColumnas34 here
                
                        assertNotNull(stub.obtenerColumnas(
                        obtenerColumnas34));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartobtenerColumnas() throws java.lang.Exception{
            com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub = new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();
             com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerColumnas obtenerColumnas34=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerColumnas)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerColumnas.class);
                    // TODO : Fill in the obtenerColumnas34 here
                

                stub.startobtenerColumnas(
                         obtenerColumnas34,
                    new tempCallbackN65637()
                );
              


        }

        private class tempCallbackN65637  extends com.localgis.ws.georreferenciaexterna.client.protocol.ServicesCallbackHandler{
            public tempCallbackN65637(){ super(null);}

            public void receiveResultobtenerColumnas(
                         com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerColumnasResponse result
                            ) {
                
            }

            public void receiveErrorobtenerColumnas(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testobtenerDatos() throws java.lang.Exception{

        com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub =
                    new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();//the default implementation should point to the right endpoint

           com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerDatos obtenerDatos36=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerDatos)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerDatos.class);
                    // TODO : Fill in the obtenerDatos36 here
                
                        assertNotNull(stub.obtenerDatos(
                        obtenerDatos36));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartobtenerDatos() throws java.lang.Exception{
            com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub = new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();
             com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerDatos obtenerDatos36=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerDatos)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerDatos.class);
                    // TODO : Fill in the obtenerDatos36 here
                

                stub.startobtenerDatos(
                         obtenerDatos36,
                    new tempCallbackN65691()
                );
              


        }

        private class tempCallbackN65691  extends com.localgis.ws.georreferenciaexterna.client.protocol.ServicesCallbackHandler{
            public tempCallbackN65691(){ super(null);}

            public void receiveResultobtenerDatos(
                         com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerDatosResponse result
                            ) {
                
            }

            public void receiveErrorobtenerDatos(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testobtenerTotalFilas() throws java.lang.Exception{

        com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub =
                    new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();//the default implementation should point to the right endpoint

           com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTotalFilas obtenerTotalFilas38=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTotalFilas)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTotalFilas.class);
                    // TODO : Fill in the obtenerTotalFilas38 here
                
                        assertNotNull(stub.obtenerTotalFilas(
                        obtenerTotalFilas38));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartobtenerTotalFilas() throws java.lang.Exception{
            com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub = new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();
             com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTotalFilas obtenerTotalFilas38=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTotalFilas)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTotalFilas.class);
                    // TODO : Fill in the obtenerTotalFilas38 here
                

                stub.startobtenerTotalFilas(
                         obtenerTotalFilas38,
                    new tempCallbackN65745()
                );
              


        }

        private class tempCallbackN65745  extends com.localgis.ws.georreferenciaexterna.client.protocol.ServicesCallbackHandler{
            public tempCallbackN65745(){ super(null);}

            public void receiveResultobtenerTotalFilas(
                         com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTotalFilasResponse result
                            ) {
                
            }

            public void receiveErrorobtenerTotalFilas(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testobtenerTablas() throws java.lang.Exception{

        com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub =
                    new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();//the default implementation should point to the right endpoint

           com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablas obtenerTablas40=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablas)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablas.class);
                    // TODO : Fill in the obtenerTablas40 here
                
                        assertNotNull(stub.obtenerTablas(
                        obtenerTablas40));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartobtenerTablas() throws java.lang.Exception{
            com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub = new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();
             com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablas obtenerTablas40=
                                                        (com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablas)getTestObject(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablas.class);
                    // TODO : Fill in the obtenerTablas40 here
                

                stub.startobtenerTablas(
                         obtenerTablas40,
                    new tempCallbackN65799()
                );
              


        }

        private class tempCallbackN65799  extends com.localgis.ws.georreferenciaexterna.client.protocol.ServicesCallbackHandler{
            public tempCallbackN65799(){ super(null);}

            public void receiveResultobtenerTablas(
                         com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablasResponse result
                            ) {
                
            }

            public void receiveErrorobtenerTablas(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testobtenerTablasBBDDLocalGis() throws java.lang.Exception{

        com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub =
                    new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();//the default implementation should point to the right endpoint

           
                        assertNotNull(stub.obtenerTablasBBDDLocalGis(
                        ));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartobtenerTablasBBDDLocalGis() throws java.lang.Exception{
            com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub stub = new com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub();
             

                stub.startobtenerTablasBBDDLocalGis(
                         
                    new tempCallbackN65853()
                );
              


        }

        private class tempCallbackN65853  extends com.localgis.ws.georreferenciaexterna.client.protocol.ServicesCallbackHandler{
            public tempCallbackN65853(){ super(null);}

            public void receiveResultobtenerTablasBBDDLocalGis(
                         com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ObtenerTablasBBDDLocalGisResponse result
                            ) {
                
            }

            public void receiveErrorobtenerTablasBBDDLocalGis(java.lang.Exception e) {
                fail();
            }

        }
      
        //Create an ADBBean and provide it as the test object
        public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
        }

        
        

    }
    