/**
 * ILocalGISGeoMarketingDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.localgis.webservices.geomarketing.model.ot.GeomarketingFeatureOT;
import com.localgis.webservices.geomarketing.model.ot.NameDomainOT;
import com.localgis.webservices.geomarketing.model.ot.PortalStepRelationOT;
import com.localgis.webservices.geomarketing.model.ot.PostalDataOT;
import com.localgis.webservices.geomarketing.model.ot.RangeData;
import com.localgis.webservices.geomarketing.model.ot.StudiesLevel;

public interface ILocalGISGeoMarketingDAO {

	public String getTableQuery(Connection connection, Integer idLayer,Integer idEntidad,String srid) throws SQLException;

	public Integer getNumElements(Connection connection, String geometry,String srid,String tableQuery) throws SQLException;

	public String getNumHabitantsQuery(Integer idEntidad, String srid);

	public String getNumMalesQuery(Integer idEntidad, String srid);

	public String getNumFemalesQuery(Integer idEntidad, String srid);

	public void buildRangeQuery(Connection connection,String wktGeometry,Integer idEntidad, String srid,ArrayList<RangeData> rangeList) throws SQLException;

	public StudiesLevel getLevelStudies(Connection connection, String wktGeometry,
			Integer idEntidad, String srid) throws SQLException;

	public Integer getSpanishHabitants(Connection connection,
			String wktGeometry, Integer idEntidad, String srid) throws SQLException;

	public Integer getForeignHabitants(Connection connection,
			String wktGeometry, Integer idEntidad, String srid) throws SQLException;
	
	public ArrayList<GeomarketingFeatureOT> getFeaturesFromLayerOrderedbyAttribute(Connection connection,String layerQuery,String columnName) throws SQLException;

	public NameDomainOT getColumnNameFromAttNameAndLocale(Connection connection,
			String attributeName,String locale) throws SQLException;
	public Hashtable<String,String> getCodeBookDomainData(Connection connection,Integer idDomain,String locale) throws SQLException;
	public ArrayList<PortalStepRelationOT> getNearestStepFromPortal(Connection connection,Integer idEntidad,Integer idFeatureStreet,ArrayList<Integer> idFeatureStep) throws SQLException;
	
	public ArrayList<PostalDataOT> getPostalDataFromIdPortales(Connection connection,ArrayList<Integer> portales) throws SQLException;
}
