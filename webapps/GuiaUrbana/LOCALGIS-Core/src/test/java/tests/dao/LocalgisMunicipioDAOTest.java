/**
 * LocalgisMunicipioDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisMunicipioDAO;

public class LocalgisMunicipioDAOTest extends BaseDAOTest {

    private LocalgisMunicipioDAO localgisMunicipioDAO = (LocalgisMunicipioDAO)daoManager.getDao(LocalgisMunicipioDAO.class);

    public void testSelectBoundingBoxByIdMunicipio() throws Exception {
        //localgisMunicipioDAO.selectBoundingBoxByIdMunicipioAndSRID(ConfigurationTests.ID_MUNICIPIO, ConfigurationTests.SRID);
    }

    public void testSelectBoundingBoxByIdEntidad() throws Exception {
        //localgisMunicipioDAO.selectBoundingBoxByIdEntidadAndSRID(ConfigurationTests.ID_ENTIDAD, ConfigurationTests.SRID);
    }
}

