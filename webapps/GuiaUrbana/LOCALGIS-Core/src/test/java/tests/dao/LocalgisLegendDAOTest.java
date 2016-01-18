/**
 * LocalgisLegendDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisLegendDAO;

public class LocalgisLegendDAOTest extends BaseDAOTest {

    private LocalgisLegendDAO localgisLegendDAO = (LocalgisLegendDAO) daoManager.getDao(LocalgisLegendDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            localgisLegendDAO.insert(ConfigurationTests.LOCALGIS_LEGEND);
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testUpdateByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisLegendDAO.insert(ConfigurationTests.LOCALGIS_LEGEND);
            assertEquals(1, localgisLegendDAO.updateByPrimaryKey(ConfigurationTests.LOCALGIS_LEGEND));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisLegendDAO.insert(ConfigurationTests.LOCALGIS_LEGEND);
            assertNotNull(localgisLegendDAO.selectByPrimaryKey(ConfigurationTests.LOCALGIS_LEGEND_KEY));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testDeleteByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisLegendDAO.insert(ConfigurationTests.LOCALGIS_LEGEND);
            assertEquals(1, localgisLegendDAO.deleteByPrimaryKey(ConfigurationTests.LOCALGIS_LEGEND_KEY));
        } finally {
            daoManager.endTransaction();
        }
    }
}
