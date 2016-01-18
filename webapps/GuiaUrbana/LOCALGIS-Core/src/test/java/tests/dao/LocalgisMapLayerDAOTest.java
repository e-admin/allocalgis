/**
 * LocalgisMapLayerDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisLayerDAO;
import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.dao.LocalgisMapLayerDAO;
import com.localgis.web.core.dao.LocalgisStyleDAO;

public class LocalgisMapLayerDAOTest extends BaseDAOTest {

    private LocalgisMapLayerDAO localgisMapLayerDAO = (LocalgisMapLayerDAO) daoManager.getDao(LocalgisMapLayerDAO.class);
    private LocalgisLayerDAO localgisLayerDAO = (LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
    private LocalgisMapDAO localgisMapDAO = (LocalgisMapDAO) daoManager.getDao(LocalgisMapDAO.class);
    private LocalgisStyleDAO localgisStyleDAO = (LocalgisStyleDAO) daoManager.getDao(LocalgisStyleDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            Integer idMap = localgisMapDAO.insert(ConfigurationTests.LOCALGIS_MAP);
            Integer idLayer = localgisLayerDAO.insert(ConfigurationTests.LOCALGIS_LAYER);
            Integer idStyle = localgisStyleDAO.insert(ConfigurationTests.LOCALGIS_STYLE);
            ConfigurationTests.LOCALGIS_MAP_LAYER.setStyleid(idStyle);
            ConfigurationTests.LOCALGIS_MAP_LAYER.setLayerid(idLayer);
            ConfigurationTests.LOCALGIS_MAP_LAYER.setMapid(idMap);
            localgisMapLayerDAO.insert(ConfigurationTests.LOCALGIS_MAP_LAYER);
        } finally {
            daoManager.endTransaction();
        }
    }
    public void testDeleteLayersByIdMap() throws Exception {
        daoManager.startTransaction();
        try {
            localgisMapLayerDAO.deleteLayersByIdMap(ConfigurationTests.ID_MAP_LOCALGIS);
        } finally {
            daoManager.endTransaction();
        }
    }

}
