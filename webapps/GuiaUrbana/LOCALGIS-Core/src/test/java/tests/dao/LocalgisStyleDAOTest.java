/**
 * LocalgisStyleDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisStyleDAO;

public class LocalgisStyleDAOTest extends BaseDAOTest {

    private LocalgisStyleDAO localgisStyleDAO = (LocalgisStyleDAO) daoManager.getDao(LocalgisStyleDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            localgisStyleDAO.insert(ConfigurationTests.LOCALGIS_STYLE);
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectUnreferenceStyles() throws Exception {
        localgisStyleDAO.selectUnreferenceStyles();
    }

    public void testSelectStylesByIdLayer() throws Exception {
        localgisStyleDAO.selectStylesByIdLayer(ConfigurationTests.ID_LAYER_LOCALGIS);
    }

    public void testSelectStyleByIdLayerAndIdMap() throws Exception {
        localgisStyleDAO.selectStyleByIdLayerAndIdMap(ConfigurationTests.ID_LAYER_LOCALGIS, ConfigurationTests.ID_MAP_LOCALGIS);
    }

    public void testDeleteByPrimaryKey() throws Exception {
        localgisStyleDAO.deleteByPrimaryKey(ConfigurationTests.ID_STYLE_LOCALGIS);
    }

}
