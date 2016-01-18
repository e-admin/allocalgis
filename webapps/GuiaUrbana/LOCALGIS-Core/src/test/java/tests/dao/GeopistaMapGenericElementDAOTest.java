/**
 * GeopistaMapGenericElementDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.GeopistaMapGenericElementDAO;

public class GeopistaMapGenericElementDAOTest extends BaseDAOTest {

    private GeopistaMapGenericElementDAO geopistaMapGenericElementDAO = (GeopistaMapGenericElementDAO) daoManager.getDao(GeopistaMapGenericElementDAO.class);

    public void testSelectCenteredPointMapGenericElement() throws Exception {
        geopistaMapGenericElementDAO.selectCenteredPointMapGenericElement("parcelas", "referencia_catastral", ConfigurationTests.REFERENCIA_CATASTRAL, new Integer(25830));
    }

    public void testSelectBoundingBoxMapGenericElement() throws Exception {
        geopistaMapGenericElementDAO.selectBoundingBoxMapGenericElement("parcelas", "referencia_catastral", ConfigurationTests.REFERENCIA_CATASTRAL, new Integer(25830));
    }
}
