/**
 * LocalgisMapServerLayerDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.dao.LocalgisMapServerLayerDAO;

public class LocalgisMapServerLayerDAOTest extends BaseDAOTest {

    private LocalgisMapServerLayerDAO localgisMapServerLayerDAO = (LocalgisMapServerLayerDAO) daoManager.getDao(LocalgisMapServerLayerDAO.class);
    private LocalgisMapDAO localgisMapDAO = (LocalgisMapDAO) daoManager.getDao(LocalgisMapDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            Integer idMap = localgisMapDAO.insert(ConfigurationTests.LOCALGIS_MAP);
            ConfigurationTests.LOCALGIS_MAP_SERVER_LAYER.setMapid(idMap);
            localgisMapServerLayerDAO.insert(ConfigurationTests.LOCALGIS_MAP_SERVER_LAYER);
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectMapServerLayersByIdMap() throws Exception {
        localgisMapServerLayerDAO.selectMapServerLayersByIdMap(ConfigurationTests.ID_MAP_LOCALGIS);
    }

    public void testSelectMapServerLayersByIdMunicipio() throws Exception {
        localgisMapServerLayerDAO.selectMapServerLayersByIdMap(ConfigurationTests.ID_ENTIDAD);
    }

    public void testDeleteMapServerLayersByIdMap() throws Exception {
        localgisMapServerLayerDAO.deleteMapServerLayersByIdMap(ConfigurationTests.ID_MAP_LOCALGIS);
    }

}
