/**
 * LocalgisLayerDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import java.util.ArrayList;
import java.util.List;

import com.localgis.web.core.dao.LocalgisLayerDAO;
import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.dao.LocalgisMapLayerDAO;
import com.localgis.web.core.dao.LocalgisStyleDAO;

public class LocalgisLayerDAOTest extends BaseDAOTest {

    private LocalgisLayerDAO localgisLayerDAO = (LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
    private LocalgisStyleDAO localgisStyleDAO = (LocalgisStyleDAO) daoManager.getDao(LocalgisStyleDAO.class);
    private LocalgisMapDAO localgisMapDAO = (LocalgisMapDAO) daoManager.getDao(LocalgisMapDAO.class);
    private LocalgisMapLayerDAO localgisMapLayerDAO = (LocalgisMapLayerDAO) daoManager.getDao(LocalgisMapLayerDAO.class);

    public void testInsert() throws Exception {
        daoManager.startTransaction();
        try {
            assertNotNull(localgisLayerDAO.insert(ConfigurationTests.LOCALGIS_LAYER));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectLayerByIdEntidadAndIdGeopista() throws Exception {
        daoManager.startTransaction();
        try {
            Integer idMap = localgisMapDAO.insert(ConfigurationTests.LOCALGIS_MAP);
            Integer idLayer = localgisLayerDAO.insert(ConfigurationTests.LOCALGIS_LAYER);
            Integer idStyle = localgisStyleDAO.insert(ConfigurationTests.LOCALGIS_STYLE);
            ConfigurationTests.LOCALGIS_MAP_LAYER.setMapid(idMap);
            ConfigurationTests.LOCALGIS_MAP_LAYER.setLayerid(idLayer);
            ConfigurationTests.LOCALGIS_MAP_LAYER.setStyleid(idStyle);
            localgisMapLayerDAO.insert(ConfigurationTests.LOCALGIS_MAP_LAYER);
            assertNotNull(localgisLayerDAO.selectLayerByIdEntidadAndIdGeopista(ConfigurationTests.ID_ENTIDAD, ConfigurationTests.ID_LAYER_GEOPISTA, Boolean.TRUE));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testUpdateByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            Integer idLayer = localgisLayerDAO.insert(ConfigurationTests.LOCALGIS_LAYER);
            ConfigurationTests.LOCALGIS_LAYER.setLayerid(idLayer);
            assertEquals(1, localgisLayerDAO.updateByPrimaryKey(ConfigurationTests.LOCALGIS_LAYER));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectUnreferenceLayers() throws Exception {
        localgisLayerDAO.selectUnreferenceLayers();
    }

    public void testDeleteByPrimaryKey() throws Exception {
        daoManager.startTransaction();
        try {
            Integer idLayer = localgisLayerDAO.insert(ConfigurationTests.LOCALGIS_LAYER);
            assertEquals(1, localgisLayerDAO.deleteByPrimaryKey(idLayer));
        } finally {
            daoManager.endTransaction();
        }
    }

    public void testSelectLayersByIdMap() throws Exception {
        daoManager.startTransaction();
        try {
            localgisLayerDAO.insert(ConfigurationTests.LOCALGIS_LAYER);
            assertNotNull(localgisLayerDAO.selectLayersByIdMap(ConfigurationTests.ID_MAP_LOCALGIS, ConfigurationTests.LOCALE));
        } finally {
            daoManager.endTransaction();
        }
    }
    
    
//    static {
//    	DaoManagerFactory.initializeDaoManagerFactory("postgis", "org.postgresql.Driver", "pamod-pre.c.ovd.interhost.com", 5432, "geopista", "geopista", "01g7PT3a");
//    }
    public void testGetGeometryFromLayer() throws Exception {
    	List<String> l = localgisLayerDAO.getGeometryFromLayer("rutas_trekking", "33001");
    	System.out.println();
    }
    
    public void getLayersInArea() throws Exception {
    	
   	
    	String[] capa1={"eiel_c_cu", "33001", "CU_TC"};
    	String[] capa2={"eiel_c_ce", "33001", "CE_TC"};

    	
    	ArrayList listaCapasCompletas=new ArrayList();
    	listaCapasCompletas.add(capa1);
    	listaCapasCompletas.add(capa2);
    	
    	String srid="23029";
    	String bbox="694163.174699628 4794027.39287941,694185.451378196 4794057.34374801";
    	
    	List l = localgisLayerDAO.getLayersInArea(listaCapasCompletas,srid,bbox);
    	System.out.println(l);
    }
    
    public static void main(String args[]){
    	try {
            DaoManagerFactory.initializeDaoManagerFactory("postgis", "org.postgresql.Driver", "pamod-balanceada.c.ovd.interhost.com", 5432, "geopista", "geopista", "a3TP7g10");

			new LocalgisLayerDAOTest().getLayersInArea();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}