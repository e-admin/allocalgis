/**
 * LocalgisCSSDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisCSSDAO;

public class LocalgisCSSDAOTest extends BaseDAOTest {

    private LocalgisCSSDAO localgisCSSDAO = (LocalgisCSSDAO) daoManager.getDao(LocalgisCSSDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            localgisCSSDAO.insert(ConfigurationTests.LOCALGIS_CSS);
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testUpdateByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisCSSDAO.insert(ConfigurationTests.LOCALGIS_CSS);
            assertEquals(1, localgisCSSDAO.updateByPrimaryKey(ConfigurationTests.LOCALGIS_CSS));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisCSSDAO.insert(ConfigurationTests.LOCALGIS_CSS);
            assertNotNull(localgisCSSDAO.selectByPrimaryKey(ConfigurationTests.ID_ENTIDAD));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testDeleteByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisCSSDAO.insert(ConfigurationTests.LOCALGIS_CSS);
            assertEquals(1, localgisCSSDAO.deleteByPrimaryKey(ConfigurationTests.ID_ENTIDAD));
        } finally {
            daoManager.endTransaction();
        }
    }

}
