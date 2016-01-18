

/**
 * GeoMarketingWSTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */
    package com.localgis.webservices.geomarketing.client.test;

import org.apache.axis2.AxisFault;

    /*
     *  GeoMarketingWSTest Junit test case
    */

    public class GeoMarketingWSTest extends junit.framework.TestCase{
    	public String userName = "syssuperuser";
    	public String password = "sysgeopass";
    	private com.localgis.webservices.geomarketing.client.GeoMarketingWSStub getStub() throws AxisFault{
          	// ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem("WebContent/WEB-INF", null);
          	com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = new com.localgis.webservices.geomarketing.client.GeoMarketingWSStub();
          	/* stub._getServiceClient().engageModule("addressing");
               stub._getServiceClient().engageModule("rampart");
               stub._getServiceClient().getOptions().setUserName("syssuperuser");
               stub._getServiceClient().getOptions().setPassword("sysgeopass");*/
               return stub;
           }
          /*private com.localgis.webservices.geomarketing.client.GeoMarketingWSStub getStub(String username,String password) throws AxisFault{
          	 ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem("WEB-INF", null);
          	com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = new com.localgis.webservices.geomarketing.client.GeoMarketingWSStub(ctx);
          	 stub._getServiceClient().engageModule("addressing");
               stub._getServiceClient().engageModule("rampart");
               stub._getServiceClient().getOptions().setUserName(username);
               stub._getServiceClient().getOptions().setPassword(password);
               return stub;
           }*/
        /**
         * Auto generated test method
         */
        public  void testgetPortalStepRelations() throws java.lang.Exception{

        com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =
                    getStub();//the default implementation should point to the right endpoint

           com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPortalStepRelations getPortalStepRelations28=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPortalStepRelations)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPortalStepRelations.class);
                getPortalStepRelations28.setIdEntidad(2);
                getPortalStepRelations28.setIdFeatureStep(new int[]{947657});
                getPortalStepRelations28.setIdFeatureStreet(2172);
                getPortalStepRelations28.setUserName(userName);
                getPortalStepRelations28.setPassword(password);
                        assertNotNull(stub.getPortalStepRelations(
                        getPortalStepRelations28));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetPortalStepRelations() throws java.lang.Exception{
            com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = getStub();
             com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPortalStepRelations getPortalStepRelations28=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPortalStepRelations)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPortalStepRelations.class);
                    // TODO : Fill in the getPortalStepRelations28 here
                

                stub.startgetPortalStepRelations(
                         getPortalStepRelations28,
                    new tempCallbackN65548()
                );
              


        }

        private class tempCallbackN65548  extends com.localgis.webservices.geomarketing.client.GeoMarketingWSCallbackHandler{
            public tempCallbackN65548(){ super(null);}

            public void receiveResultgetPortalStepRelations(
                         com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPortalStepRelationsResponse result
                            ) {
                
            }

            public void receiveErrorgetPortalStepRelations(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetGeomarketingData() throws java.lang.Exception{

        com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =
                    getStub();//the default implementation should point to the right endpoint

           com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData getGeomarketingData30=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData.class);
                  
           getGeomarketingData30.setIdEntidad(2);
           getGeomarketingData30.setRanges(new int[]{10,30,50,70});
           getGeomarketingData30.setSrid("23030");
           getGeomarketingData30.setWktGeometry("POLYGON (("+
					        "573557.9548753637 4434771.643749281, "+
					        "573532.2815229283 4434713.420253579, "+
					        "573598.5279412664 4434664.3658123175, "+
					        "573681.2786575988 4434667.804207733, "+
					        "573689.0723538739 4434737.947474209, "+
					        "573662.9405487163 4434773.936012891, "+
					        "573623.0551618967 4434762.47469484, "+
					        "573557.9548753637 4434771.643749281"+
					        "))");
           getGeomarketingData30.setUserName(userName);
           getGeomarketingData30.setPassword(password);
                        assertNotNull(stub.getGeomarketingData(
                        getGeomarketingData30));
                  
                        System.out.println("End");


        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetGeomarketingData() throws java.lang.Exception{
            com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = getStub();
             com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData getGeomarketingData30=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingData.class);
                    // TODO : Fill in the getGeomarketingData30 here
                

                stub.startgetGeomarketingData(
                         getGeomarketingData30,
                    new tempCallbackN65620()
                );
              


        }

        private class tempCallbackN65620  extends com.localgis.webservices.geomarketing.client.GeoMarketingWSCallbackHandler{
            public tempCallbackN65620(){ super(null);}

            public void receiveResultgetGeomarketingData(
                         com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingDataResponse result
                            ) {
                
            }

            public void receiveErrorgetGeomarketingData(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetGeomarketingAndElementsData() throws java.lang.Exception{

        com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =
                    getStub();//the default implementation should point to the right endpoint

           com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData getGeomarketingAndElementsData32=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData.class);
           	getGeomarketingAndElementsData32.setIdEntidad(2);
           	getGeomarketingAndElementsData32.setIdLayer(11);
           	getGeomarketingAndElementsData32.setIdMunicipio(16078);
           	getGeomarketingAndElementsData32.setRanges(new int[]{10,30,50,70});
           	getGeomarketingAndElementsData32.setSrid("23030");
           	getGeomarketingAndElementsData32.setWktGeometry("POLYGON (("+
			        "573557.9548753637 4434771.643749281, "+
			        "573532.2815229283 4434713.420253579, "+
			        "573598.5279412664 4434664.3658123175, "+
			        "573681.2786575988 4434667.804207733, "+
			        "573689.0723538739 4434737.947474209, "+
			        "573662.9405487163 4434773.936012891, "+
			        "573623.0551618967 4434762.47469484, "+
			        "573557.9548753637 4434771.643749281"+
			        "))");
           	getGeomarketingAndElementsData32.setUserName(userName);
           	getGeomarketingAndElementsData32.setPassword(password);
                        assertNotNull(stub.getGeomarketingAndElementsData(
                        getGeomarketingAndElementsData32));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetGeomarketingAndElementsData() throws java.lang.Exception{
            com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = getStub();
             com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData getGeomarketingAndElementsData32=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsData.class);
                    // TODO : Fill in the getGeomarketingAndElementsData32 here
                

                stub.startgetGeomarketingAndElementsData(
                         getGeomarketingAndElementsData32,
                    new tempCallbackN65692()
                );
              


        }

        private class tempCallbackN65692  extends com.localgis.webservices.geomarketing.client.GeoMarketingWSCallbackHandler{
            public tempCallbackN65692(){ super(null);}

            public void receiveResultgetGeomarketingAndElementsData(
                         com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataResponse result
                            ) {
                
            }

            public void receiveErrorgetGeomarketingAndElementsData(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetPostalDataFromIdTramosAndIdVia() throws java.lang.Exception{

        com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =
                    getStub();//the default implementation should point to the right endpoint

           com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia getPostalDataFromIdTramosAndIdVia34=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia.class);
                    // TODO : Fill in the getPostalDataFromIdTramosAndIdVia34 here
           getPostalDataFromIdTramosAndIdVia34.setIdEntidad(2);
           getPostalDataFromIdTramosAndIdVia34.setIdFeatureStep(new int[]{947657});
           getPostalDataFromIdTramosAndIdVia34.setIdFeatureStreet(2172);
           getPostalDataFromIdTramosAndIdVia34.setUserName(userName);
           getPostalDataFromIdTramosAndIdVia34.setPassword(password);
                        assertNotNull(stub.getPostalDataFromIdTramosAndIdVia(
                        getPostalDataFromIdTramosAndIdVia34));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetPostalDataFromIdTramosAndIdVia() throws java.lang.Exception{
            com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = getStub();
             com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia getPostalDataFromIdTramosAndIdVia34=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdVia.class);
                    // TODO : Fill in the getPostalDataFromIdTramosAndIdVia34 here
                

                stub.startgetPostalDataFromIdTramosAndIdVia(
                         getPostalDataFromIdTramosAndIdVia34,
                    new tempCallbackN65780()
                );
              


        }

        private class tempCallbackN65780  extends com.localgis.webservices.geomarketing.client.GeoMarketingWSCallbackHandler{
            public tempCallbackN65780(){ super(null);}

            public void receiveResultgetPostalDataFromIdTramosAndIdVia(
                         com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetPostalDataFromIdTramosAndIdViaResponse result
                            ) {
                
            }

            public void receiveErrorgetPostalDataFromIdTramosAndIdVia(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetNumElements() throws java.lang.Exception{

        com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =
                    getStub();//the default implementation should point to the right endpoint

           com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements getNumElements36=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements.class);
                    // TODO : Fill in the getNumElements36 here
           getNumElements36.setIdEntidad(2);
           getNumElements36.setIdLayer(11);
           getNumElements36.setIdMunicipio(16078);
           getNumElements36.setSrid("23030");
           getNumElements36.setWktGeometry("POLYGON (("+
			        "573557.9548753637 4434771.643749281, "+
			        "573532.2815229283 4434713.420253579, "+
			        "573598.5279412664 4434664.3658123175, "+
			        "573681.2786575988 4434667.804207733, "+
			        "573689.0723538739 4434737.947474209, "+
			        "573662.9405487163 4434773.936012891, "+
			        "573623.0551618967 4434762.47469484, "+
			        "573557.9548753637 4434771.643749281"+
			        "))");
           getNumElements36.setUserName(userName);
           getNumElements36.setPassword(password);
                        assertNotNull(stub.getNumElements(
                        getNumElements36));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetNumElements() throws java.lang.Exception{
            com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = getStub();
             com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements getNumElements36=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElements.class);
                    // TODO : Fill in the getNumElements36 here
                

                stub.startgetNumElements(
                         getNumElements36,
                    new tempCallbackN65852()
                );
              


        }

        private class tempCallbackN65852  extends com.localgis.webservices.geomarketing.client.GeoMarketingWSCallbackHandler{
            public tempCallbackN65852(){ super(null);}

            public void receiveResultgetNumElements(
                         com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetNumElementsResponse result
                            ) {
                
            }

            public void receiveErrorgetNumElements(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetGeomarketingByIdLayerAndAttributeName() throws java.lang.Exception{

        com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =
                    getStub();//the default implementation should point to the right endpoint

           com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeName getGeomarketingByIdLayerAndAttributeName38=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeName)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeName.class);
                    
           getGeomarketingByIdLayerAndAttributeName38.setAttributeName("Código postal");
           getGeomarketingByIdLayerAndAttributeName38.setIdEntidad(2);
           getGeomarketingByIdLayerAndAttributeName38.setIdLayer(101);
           getGeomarketingByIdLayerAndAttributeName38.setLocale("es_ES");
           getGeomarketingByIdLayerAndAttributeName38.setRanges(new int[]{10,30,50,70});
           getGeomarketingByIdLayerAndAttributeName38.setUserName(userName);
           getGeomarketingByIdLayerAndAttributeName38.setPassword(password);
                        assertNotNull(stub.getGeomarketingByIdLayerAndAttributeName(
                        getGeomarketingByIdLayerAndAttributeName38));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetGeomarketingByIdLayerAndAttributeName() throws java.lang.Exception{
            com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = getStub();
             com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeName getGeomarketingByIdLayerAndAttributeName38=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeName)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeName.class);
                    // TODO : Fill in the getGeomarketingByIdLayerAndAttributeName38 here
                

                stub.startgetGeomarketingByIdLayerAndAttributeName(
                         getGeomarketingByIdLayerAndAttributeName38,
                    new tempCallbackN65940()
                );
              


        }

        private class tempCallbackN65940  extends com.localgis.webservices.geomarketing.client.GeoMarketingWSCallbackHandler{
            public tempCallbackN65940(){ super(null);}

            public void receiveResultgetGeomarketingByIdLayerAndAttributeName(
                         com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingByIdLayerAndAttributeNameResponse result
                            ) {
                
            }

            public void receiveErrorgetGeomarketingByIdLayerAndAttributeName(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetGeomarketingAndElementsDataByIdLayerAndAttributeName() throws java.lang.Exception{

        com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub =
                    getStub();//the default implementation should point to the right endpoint

           com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName getGeomarketingAndElementsDataByIdLayerAndAttributeName40=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName.class);
                  
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setAttributeName("Código postal");
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setIdEntidad(2);
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setIdLayer(101);
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setLocale("es_ES");
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setRanges(new int[]{10,30,50,70});
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setIdLayerElements(11);
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setUserName(userName);
           getGeomarketingAndElementsDataByIdLayerAndAttributeName40.setPassword(password);
                        assertNotNull(stub.getGeomarketingAndElementsDataByIdLayerAndAttributeName(
                        getGeomarketingAndElementsDataByIdLayerAndAttributeName40));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetGeomarketingAndElementsDataByIdLayerAndAttributeName() throws java.lang.Exception{
            com.localgis.webservices.geomarketing.client.GeoMarketingWSStub stub = getStub();
             com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName getGeomarketingAndElementsDataByIdLayerAndAttributeName40=
                                                        (com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName)getTestObject(com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeName.class);
                    // TODO : Fill in the getGeomarketingAndElementsDataByIdLayerAndAttributeName40 here
                

                stub.startgetGeomarketingAndElementsDataByIdLayerAndAttributeName(
                         getGeomarketingAndElementsDataByIdLayerAndAttributeName40,
                    new tempCallbackN66012()
                );
              


        }

        private class tempCallbackN66012  extends com.localgis.webservices.geomarketing.client.GeoMarketingWSCallbackHandler{
            public tempCallbackN66012(){ super(null);}

            public void receiveResultgetGeomarketingAndElementsDataByIdLayerAndAttributeName(
                         com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GetGeomarketingAndElementsDataByIdLayerAndAttributeNameResponse result
                            ) {
                
            }

            public void receiveErrorgetGeomarketingAndElementsDataByIdLayerAndAttributeName(java.lang.Exception e) {
                fail();
            }

        }
      
        //Create an ADBBean and provide it as the test object
        public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
        }

        
        

    }
    