/**
 * LocalgisLayerDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.dao.LocalgisLayerDAO;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLayerExt;

/**
 * Implementación utilizando iBatis de LocalgisLayerDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisLayerDAOImpl extends SqlMapDaoTemplate implements LocalgisLayerDAO {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisLayerDAOImpl.class);

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisLayerDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisLayerDAO#insert(com.localgis.web.core.model.LocalgisLayer)
     */
    public Integer insert(LocalgisLayer record) {
        Object newKey = insert("localgis_layer.insert", record);
        return (Integer) newKey;
    }

       
    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisLayerDAO#selectLayerByIdMunicipioAndIdGeopista(java.lang.Integer,
     *      java.lang.Integer, boolean)
     */
    public LocalgisLayer selectLayerByIdEntidadAndIdGeopista(Integer idEntidad, Integer idGeopista, Boolean publicMaps) {
        Map params = new HashMap();
        params.put("idEntidad", idEntidad);
        params.put("idGeopista", idGeopista);
        if (publicMaps.booleanValue()) {
            params.put("publicMaps", ConstantesSQL.TRUE);
        } else {
            params.put("publicMaps", ConstantesSQL.FALSE);
        }
        
        /*
         * Para evitar que por una inconsistencia de datos se devuelvan varios
         * objetos y la llamada a queryForObject falle lo que hacemos es un
         * queryForList y nos quedamos el primer elemento
         */
        List result = queryForList("localgis_layer.selectLayerByIdEntidadAndIdGeopista", params);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.warn("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectLayerByIdMunicipioAndIdGeopista\" con idMunicipio = ["+idEntidad+"], idGeopista = ["+idGeopista+"], publicMaps = ["+publicMaps+"]. Devolvemos el primero.");
        }
        return (LocalgisLayer)result.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisLayerDAO#updateByPrimaryKey(com.localgis.web.core.model.LocalgisLayer)
     */
    public int updateByPrimaryKey(LocalgisLayer record) {
        int rows = update("localgis_layer.updateByPrimaryKey", record);
        return rows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisLayerDAO#selectUnreferenceLayers()
     */
    public List selectUnreferenceLayers() {
		Map params = new HashMap();
        List result = queryForList("localgis_layer.selectUnreferenceLayers",params);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisLayerDAO#deleteByPrimaryKey(java.lang.Integer)
     */
    public int deleteByPrimaryKey(Integer layerid) {
        LocalgisLayer key = new LocalgisLayer();
        key.setLayerid(layerid);
        int rows = delete("localgis_layer.deleteByPrimaryKey", key);
        return rows;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisLayerDAO#selectLayerByIdMap(java.lang.Integer)
     */
    public List selectLayersByIdMap(Integer idMap, String locale){
        Map params = new HashMap();
        params.put("idMap", idMap);
        params.put("locale", locale);
        List result = queryForList("localgis_layer.selectLayersByIdMap", params);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisLayerDAO#selectLayerByIdMapGeopista(java.lang.Integer)
     */
	public List selectLayersByIdMapGeopista(Integer idMapGeopista, String locale) {
        Map params = new HashMap();
        params.put("idMapGeopista", idMapGeopista);
        params.put("locale", locale);
        List result = queryForList("localgis_layer.selectLayersByIdMapGeopista", params);
        return result;
	}
	
	public List selectLayersByIdMapGeopistaAndEntidad(Integer idMapGeopista, String locale,Integer idEntidad,int tipoPublicacion) {
        Map params = new HashMap();
        params.put("idMapGeopista", idMapGeopista);
        params.put("locale", locale);
        params.put("idEntidad", idEntidad);
        params.put("tipoPublicacion", tipoPublicacion);
        List result = queryForList("localgis_layer.selectLayersByIdMapGeopistaAndEntidad", params);
        return result;
	}
	
	
    public Timestamp getFechaPrimeraVersion(LocalgisLayer localgisLayer) {
	Timestamp fecha = (Timestamp) queryForObject("localgis_layer.selectFechaPrimeraVersion",localgisLayer);
	return fecha;		
    }

	@Override
	public List<String> getGeometryFromLayer(String layer, String municipio) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tabla", layer);
		params.put("municipio", municipio);
		
		return (List<String>) queryForList("localgis_layer.selectGeometryFromLayer", params);
	}
	
	public String getIdLayerFromGuiaUrbana(String layerId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("layerId", layerId);
		
		return (String) queryForObject("localgis_layer.selectLayerIdFromGuiaUrbana", params);
	}

	
    //*********** SADIM ************
    //*********** SADIM ************
    //*********** SADIM ************
    //*********** SADIM ************
    public LocalgisLayer selectLayerByName(String layername) 
    {
        Map params = new HashMap();
        params.put("layername", layername);

        List result = queryForList("localgis_layer.selectLayerByName", params);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.warn("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectLayerByName\" con name = ["+layername+"]. Devolvemos el primero.");
        }
        return (LocalgisLayer)result.get(0);        
    }	
    
    public LocalgisLayerExt selectLayerById(Integer idLayer,Integer idMap,String locale) 
    {
        Map params = new HashMap();
        params.put("idLayer", idLayer);    
        params.put("idMap", idMap);    
        params.put("locale", locale);   
        
        List result = queryForList("localgis_layer.selectLayerById", params);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.warn("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectLayerById\" con name = ["+idLayer+"]. Devolvemos el primero.");
        }
        return (LocalgisLayerExt)result.get(0);        
    }	
    
    
    
    public List<HashMap<String,Object>> selectPublicAnexosByIdLayerAndIdFeature(int idLayer, int idFeature) 
    {
        Map params = new HashMap();
        params.put("id_layer", idLayer);    
        params.put("id_feature", idFeature);    
        
        List<HashMap<String,Object>> result=queryForList("localgis_layer.selectPublicAnexosByIdLayerAndIdFeature", params);
        if (result.size() == 0) {
            return null;
        }
        
		return result;
    }    

    public List<HashMap<String,Object>> selectAllAnexosByIdLayerAndIdFeature(int idLayer, int idFeature) 

    {
        Map params = new HashMap();
        params.put("id_layer", idLayer);    
        params.put("id_feature", idFeature);    
        
        List<HashMap<String,Object>> result=queryForList("localgis_layer.selectAllAnexosByIdLayerAndIdFeature", params);
        if (result.size() == 0) {
            return null;
        }
        
		return result;
    }

	@Override
	public List<HashMap<String,Object>> getLayersInArea(ArrayList listaCompletaCapas,String srid,String bbox) {
		
		Map params = new HashMap();
		
		StringBuffer sb=new StringBuffer();
		for (int i=0;i<listaCompletaCapas.size();i++){
			String[] datosCapa=(String[])listaCompletaCapas.get(i);
			
			if (i!=listaCompletaCapas.size()-1)
				sb.append("select id,'"+datosCapa[2]+"' as tipo,transform(\"GEOMETRY\","+srid+") as geometry_localgis from "+datosCapa[0]+" where id_municipio="+datosCapa[1]+" union ");
			else
				sb.append("select id,'"+datosCapa[2]+"' as tipo,transform(\"GEOMETRY\","+srid+") as geometry_localgis from "+datosCapa[0]+" where id_municipio="+datosCapa[1]+") t ");
		}
		sb.append("where geometry_localgis && setSRID('BOX3D("+bbox+")'::BOX3D, "+srid+")");    

		params.put("tabla",sb.toString());
		
        /*params.put("tabla", "select id,'CC' as tipo,transform(\"GEOMETRY\",23029) as geometry_localgis  from eiel_c_cc where id_municipio=33001 union " +
        					"select id,'CE' as tipo,transform(\"GEOMETRY\",23029) as geometry_localgis from eiel_c_ce  where id_municipio=33001 UNION " +
        					"select id,'CU' as tipo,transform(\"GEOMETRY\",23029) as geometry_localgis from eiel_c_cu  where id_municipio=33001 UNION " +
        					"select id,'PL' as tipo,transform(\"GEOMETRY\",23029) as geometry_localgis from eiel_c_alum_pl  where id_municipio=33001 UNION "+
        					"select id,'NP' as tipo,transform(\"GEOMETRY\",23029) as geometry_localgis from numeros_policia where id_municipio=33001 UNION "+
        					"select id,'PARCELAS' as tipo,transform(\"GEOMETRY\",23029) as geometry_localgis from parcelas where id_municipio=33001) t " +
        					"where geometry_localgis && setSRID('BOX3D(694163.174699628 4794027.39287941,694185.451378196 4794057.34374801)'::BOX3D, 23029)"); */
		
		List<HashMap<String,Object>> result=null;
		if (listaCompletaCapas.size()>1)
			result=queryForList("localgis_layer.getLayersInArea", params);
		else
			result=queryForList("localgis_layer.getLayersInAreaSimple", params);
        if (result.size() == 0) {
            return null;
        }
        
		return result;
	}    
}