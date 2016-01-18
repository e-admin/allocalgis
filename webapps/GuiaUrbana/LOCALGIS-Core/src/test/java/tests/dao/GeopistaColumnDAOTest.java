/**
 * GeopistaColumnDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.GeopistaColumnDAO;

/**
 * Test sobre GeopistaColumnDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaColumnDAOTest extends BaseDAOTest {

    private GeopistaColumnDAO geopistaColumnDAO = (GeopistaColumnDAO)daoManager.getDao(GeopistaColumnDAO.class);

    public void testSelectColumnsByLayer() {
        geopistaColumnDAO.selectColumnsByLayer(ConfigurationTests.ID_LAYER_GEOPISTA);
    }

    public void testSelectColumnsByLayerTranslated() {
        geopistaColumnDAO.selectColumnsByLayerTranslated(ConfigurationTests.ID_LAYER_GEOPISTA, ConfigurationTests.LOCALE);
    }

    public void testSelectColumnsByLayerTranslatedAllLanguages() {
        geopistaColumnDAO.selectColumnsByLayerTranslatedAllLanguages(ConfigurationTests.ID_LAYER_GEOPISTA);
    }

}