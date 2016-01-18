/**
 * AllTestsFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsFactory {

    public static Test getSuite(String dbName) {
        TestSuite suite = new TestSuite("Test de acceso a base de datos "+dbName);
        
        //$JUnit-BEGIN$
        suite.addTestSuite(GeopistaAuthenticationDAOTest.class);
        suite.addTestSuite(GeopistaColumnDAOTest.class);
        suite.addTestSuite(GeopistaCoverageLayerDAOTest.class);
        suite.addTestSuite(GeopistaEntidadSupramunicipalDAOTest.class);
        suite.addTestSuite(GeopistaLayerDAOTest.class);
        suite.addTestSuite(GeopistaMapDAOTest.class);
        suite.addTestSuite(GeopistaMapGenericElementDAOTest.class);
        suite.addTestSuite(GeopistaMapServerLayerDAOTest.class);
        suite.addTestSuite(GeopistaNumeroPoliciaDAOTest.class);
        suite.addTestSuite(GeopistaParcelaDAOTest.class);
        suite.addTestSuite(GeopistaViaDAOTest.class);
        suite.addTestSuite(LocalgisAttributeDAOTest.class);
        suite.addTestSuite(LocalgisCSSDAOTest.class);
        suite.addTestSuite(LocalgisLayerDAOTest.class);
        suite.addTestSuite(LocalgisLegendDAOTest.class);
        suite.addTestSuite(LocalgisMapDAOTest.class);
        suite.addTestSuite(LocalgisMapLayerDAOTest.class);
        suite.addTestSuite(LocalgisMapServerLayerDAOTest.class);
        suite.addTestSuite(LocalgisMarkerDAOTest.class);
        suite.addTestSuite(LocalgisMunicipioDAOTest.class);
        suite.addTestSuite(LocalgisRestrictedAttributesDAOTest.class);
        suite.addTestSuite(LocalgisStyleDAOTest.class);
        //$JUnit-END$

        return suite;
    }

}
