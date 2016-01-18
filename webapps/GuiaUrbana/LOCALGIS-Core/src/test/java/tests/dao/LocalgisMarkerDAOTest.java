/**
 * LocalgisMarkerDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.dao.LocalgisMarkerDAO;

public class LocalgisMarkerDAOTest extends BaseDAOTest {

    private LocalgisMarkerDAO localgisMarkerDAO = (LocalgisMarkerDAO)daoManager.getDao(LocalgisMarkerDAO.class);
    private LocalgisMapDAO localgisMapDAO = (LocalgisMapDAO)daoManager.getDao(LocalgisMapDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            Integer idMap = localgisMapDAO.insert(ConfigurationTests.LOCALGIS_MAP);
            ConfigurationTests.LOCALGIS_MARKER.setMapid(idMap);
            localgisMarkerDAO.insert(ConfigurationTests.LOCALGIS_MARKER);
        } finally {
            daoManager.endTransaction();
        }
    }
    public void testSelectMarkersByMapIdAndUsername() throws Exception {
        localgisMarkerDAO.selectMarkersByMapIdAndUsername(ConfigurationTests.ID_MAP_LOCALGIS, ConfigurationTests.USER_VALID);
    }
    public void testSelectByPrimaryKey() throws Exception {
        localgisMarkerDAO.selectByPrimaryKey(ConfigurationTests.ID_MARKER_LOCALGIS);
    }
    public void testDeleteByPrimaryKey() throws Exception {
        localgisMarkerDAO.deleteByPrimaryKey(ConfigurationTests.ID_MARKER_LOCALGIS);
    }

    public void testUpdateByPrimaryKey() throws Exception {
        localgisMarkerDAO.updateByPrimaryKey(ConfigurationTests.LOCALGIS_MARKER);
    }
}