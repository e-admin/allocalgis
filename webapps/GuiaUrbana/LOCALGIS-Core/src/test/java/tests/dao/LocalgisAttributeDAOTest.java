/**
 * LocalgisAttributeDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import com.localgis.web.core.dao.LocalgisAttributeDAO;
import com.localgis.web.core.dao.LocalgisLayerDAO;

public class LocalgisAttributeDAOTest extends BaseDAOTest {

    private LocalgisAttributeDAO localgisAttributeDAO = (LocalgisAttributeDAO)daoManager.getDao(LocalgisAttributeDAO.class);
    private LocalgisLayerDAO localgisLayerDAO = (LocalgisLayerDAO)daoManager.getDao(LocalgisLayerDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            Integer idLocalgisLayer = localgisLayerDAO.insert(ConfigurationTests.LOCALGIS_LAYER);
            ConfigurationTests.LOCALGIS_ATTRIBUTE.setLayerid(idLocalgisLayer);
            localgisAttributeDAO.insert(ConfigurationTests.LOCALGIS_ATTRIBUTE);
        } finally {
            daoManager.endTransaction();
        }
    }
    
    public void testSelectAttributesByLayer() throws Exception {
        //localgisAttributeDAO.																																				;
    }
    
    public void testUpdateByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisAttributeDAO.updateByPrimaryKey(ConfigurationTests.LOCALGIS_ATTRIBUTE);
        } finally {
            daoManager.endTransaction();
        }
    }
    
    public void testDeleteByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            localgisAttributeDAO.deleteByPrimaryKey(ConfigurationTests.LOCALGIS_ATTRIBUTE.getAttributeid());
        } finally {
            daoManager.endTransaction();
        }
    }
    public void testSelectRestrictedAttributesByIdLayerGeopistaAndMapPublic() throws Exception {
        localgisAttributeDAO.selectRestrictedAttributesByIdLayerGeopistaAndMapPublic(ConfigurationTests.ID_LAYER_GEOPISTA, ConfigurationTests.PUBLIC_MAP_SHORT,null);
    }
    
    public void testSelectAttributesTranslatedByNameLayerAndMapPublic() throws Exception {
        localgisAttributeDAO.selectAttributesTranslatedByNameLayerAndMapPublic(ConfigurationTests.LOCALGIS_LAYER.getLayername(), Boolean.TRUE, ConfigurationTests.LOCALE);
    }

    public void testselectAttributesValuesTranslatedByNameLayer() throws Exception {
        localgisAttributeDAO.selectAttributesValuesTranslatedByNameLayer(ConfigurationTests.LOCALGIS_LAYER.getLayername(), ConfigurationTests.LOCALE);
    }



}
