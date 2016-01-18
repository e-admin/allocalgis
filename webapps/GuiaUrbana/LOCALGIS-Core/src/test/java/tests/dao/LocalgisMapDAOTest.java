/**
 * LocalgisMapDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisMapDAO;

public class LocalgisMapDAOTest extends BaseDAOTest {

    private LocalgisMapDAO localgisMapDAO = (LocalgisMapDAO)daoManager.getDao(LocalgisMapDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            localgisMapDAO.insert(ConfigurationTests.LOCALGIS_MAP);
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectMapsByIdEntidad() throws Exception {
        localgisMapDAO.selectMapsByIdEntidad(ConfigurationTests.ID_ENTIDAD, ConfigurationTests.LOCALE);
        localgisMapDAO.selectMapsByIdEntidad(ConfigurationTests.ID_ENTIDAD, ConfigurationTests.PUBLIC_MAP_BOOLEAN, ConfigurationTests.LOCALE);
    }

    public void testSelectMapById() throws Exception {
        localgisMapDAO.selectMapById(ConfigurationTests.ID_MAP_LOCALGIS, ConfigurationTests.LOCALE);
    }

    public void testDeleteByIdMapAndPublicMap() throws Exception {
        daoManager.startTransaction();
        try {
            localgisMapDAO.deleteByIdMapAndPublicMap(ConfigurationTests.ID_MAP_LOCALGIS, ConfigurationTests.PUBLIC_MAP_BOOLEAN);
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSetDefaltMap() throws Exception {
        daoManager.startTransaction();
        try {
            localgisMapDAO.setDefaultMap(ConfigurationTests.ID_MAP_GEOPISTA, ConfigurationTests.PUBLIC_MAP_BOOLEAN, ConfigurationTests.ID_ENTIDAD);
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testGetDefaltMap() throws Exception {
        localgisMapDAO.getDefaltMap(ConfigurationTests.PUBLIC_MAP_BOOLEAN, ConfigurationTests.ID_ENTIDAD, ConfigurationTests.LOCALE);
    }

    public void testGetIdMapByIdGeopista() throws Exception {
        localgisMapDAO.getIdMapByIdGeopista(ConfigurationTests.ID_MAP_GEOPISTA, ConfigurationTests.ID_ENTIDAD, ConfigurationTests.LOCALE, ConfigurationTests.PUBLIC_MAP_BOOLEAN);
    }

}