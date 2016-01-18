/**
 * PostgresLocalGISGeoMarketingDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.model.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.localgis.webservices.geomarketing.model.dao.ILocalGISGeoMarketingDAO;
import com.localgis.webservices.geomarketing.model.ot.GeomarketingFeatureOT;
import com.localgis.webservices.geomarketing.model.ot.NameDomainOT;
import com.localgis.webservices.geomarketing.model.ot.PortalStepRelationOT;
import com.localgis.webservices.geomarketing.model.ot.PostalDataOT;
import com.localgis.webservices.geomarketing.model.ot.RangeData;
import com.localgis.webservices.geomarketing.model.ot.StudiesLevel;
import com.localgis.webservices.util.ConnectionUtilities;

public class PostgresLocalGISGeoMarketingDAO implements
		ILocalGISGeoMarketingDAO {
	
	private static Logger logger = Logger.getLogger(PostgresLocalGISGeoMarketingDAO.class);
	public static String habitantsQuery = "select PARAMETERS,transform(numeros_policia.\"GEOMETRY\",SRIDQUERY) as \"GEOMETRY\" from habitantes inner join domicilio on habitantes.id_domicilio = domicilio.id_domicilio inner join numeros_policia on numeros_policia.id = domicilio.id_portal where numeros_policia.id_municipio IN (SELECT id_municipio FROM entidades_municipios WHERE id_entidad = ENTITY)";	
	public static String sexConditionQuery = "AND habitantes.sexo = SEXQUERY::text";
	public static String rangeQuery = "select PARAMETERS,transform(numeros_policia.\"GEOMETRY\",SRIDQUERY) as \"GEOMETRY\" from habitantes inner join domicilio on habitantes.id_domicilio = domicilio.id_domicilio inner join numeros_policia on numeros_policia.id = domicilio.id_portal";
	public static String male = "1";
	public static String female = "6";
	@Override
	public Integer getNumElements(Connection connection, String geometry,String srid,String tableQuery) throws SQLException {

		String sqlQuery = "SELECT COUNT(*) FROM ("+tableQuery+") t WHERE intersects(transform(t.\"GEOMETRY\",SRIDGEOM),geometryfromtext('WKTGEOM',SRIDGEOM))";
		sqlQuery = sqlQuery.replaceAll("WKTGEOM", geometry);
		sqlQuery = sqlQuery.replaceAll("SRIDGEOM", srid);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer results = 0;
		
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				results = rs.getInt(1);
				if(logger.isDebugEnabled())logger.debug("Results = " + results);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return results;
	}

	@Override
	public String getTableQuery(Connection connection, Integer idLayer,Integer idEntidad,String srid) throws SQLException {

		String sqlQuery = "SELECT selectquery FROM queries WHERE id_layer = ?";
		if(logger.isDebugEnabled())logger.debug("Obtaining query layer = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String tableQuery = "";
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idLayer);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				tableQuery = rs.getString(1);
				if(logger.isDebugEnabled())logger.debug("Query Obtained = " + tableQuery);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return preprocess(tableQuery,idEntidad,srid);
	}

	private String preprocess(String tableQuery,Integer idEntidad,String srid) {

		String sqlEntidad = "select id_municipio from entidades_municipios where id_entidad = " + idEntidad;
		tableQuery = tableQuery.replaceAll("\\?M", sqlEntidad);
		tableQuery = tableQuery.replaceAll("\\?m", sqlEntidad);
		tableQuery = tableQuery.replaceAll("\\?T", srid);
		tableQuery = tableQuery.replaceAll("\\?t", srid);
		return tableQuery;
	}

	@Override
	public String getNumHabitantsQuery(Integer idEntidad, String srid) {
		return PostgresLocalGISGeoMarketingDAO.habitantsQuery.replaceAll("PARAMETERS","1,nacprovincia").replaceAll("SRIDQUERY", srid).replaceAll("ENTITY",idEntidad+"");
	}
	
	public String getLevelStudiesQuery(Integer idEntidad, String srid) {
		return PostgresLocalGISGeoMarketingDAO.habitantsQuery.replaceAll("PARAMETERS","nacprovincia,habitantes.ocupacion as ocupacion").replaceAll("SRIDQUERY", "4230").replaceAll("ENTITY",idEntidad+"");
	}
	
	@Override
	public String getNumMalesQuery(Integer idEntidad, String srid) {
		return getNumHabitantsQuery(idEntidad,srid)+" "+PostgresLocalGISGeoMarketingDAO.sexConditionQuery.replaceAll("SEXQUERY", PostgresLocalGISGeoMarketingDAO.male);
	}

	@Override
	public String getNumFemalesQuery(Integer idEntidad, String srid) {
		return getNumHabitantsQuery(idEntidad,srid)+" "+PostgresLocalGISGeoMarketingDAO.sexConditionQuery.replaceAll("SEXQUERY", PostgresLocalGISGeoMarketingDAO.female);
	}

	@Override
	public void buildRangeQuery(Connection connection,String wktGeometry,Integer idEntidad, String srid,ArrayList<RangeData> rangeList) throws SQLException {
		
		String sqlQuery = PostgresLocalGISGeoMarketingDAO.habitantsQuery.replaceAll("PARAMETERS",buildColumnsQuery(rangeList)).replaceAll("SRIDQUERY", srid).replaceAll("ENTITY",idEntidad+"");
		sqlQuery = buildFinalQueryRangeColumns(sqlQuery,rangeList);
		sqlQuery+= " WHERE intersects(transform(\"GEOMETRY\"," + srid + "), geometryFromText('" + wktGeometry +"',"+srid+"))"; 
		if(logger.isDebugEnabled())logger.debug("Obtaining query layer = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				Iterator<RangeData> rangeIterator = rangeList.iterator();
				while(rangeIterator.hasNext()){
					RangeData data = rangeIterator.next();
					data.setValue(rs.getInt(data.getAlias()));
				}
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
	}

	private String buildFinalQueryRangeColumns(String sqlQuery,ArrayList<RangeData> rangeList) {
		
		String sqlFinalQuery = "SELECT ";
		Iterator<RangeData> rangeIterator = rangeList.iterator();
		while(rangeIterator.hasNext()){
			RangeData data = rangeIterator.next();
			sqlFinalQuery+="SUM(tb."+data.getAlias()+") as "+data.getAlias();
			if(rangeIterator.hasNext())
				sqlFinalQuery+=",";
		}
		sqlFinalQuery+=" FROM ("+sqlQuery+") tb ";
		return sqlFinalQuery;
	}

	private String buildColumnsQuery(ArrayList<RangeData> rangeList) {
		Iterator<RangeData> rangeIterator = rangeList.iterator();
		String parameters = "";
		while(rangeIterator.hasNext()){
			RangeData range = rangeIterator.next();
			parameters+=range.getSelectQueryColumn();
			if(rangeIterator.hasNext())
				parameters+=",";
		}
		return parameters;
	}

	@Override
	public StudiesLevel getLevelStudies(Connection connection, String wktGeometry,Integer idEntidad, String srid) throws SQLException {
		StudiesLevel levelStudies = new StudiesLevel();
		String sqlQuery = "SELECT " + levelStudies.getColumnsQuery() + " FROM (" + getLevelStudiesQuery(idEntidad, srid)+") as habitantes ";
		sqlQuery+= "WHERE intersects(habitantes.\"GEOMETRY\",transform(geometryfromtext('"+wktGeometry+"',"+srid+"),4230))";
		if(logger.isDebugEnabled())logger.debug("Obtaining query layer = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				levelStudies.setI10NoSabeLeerNiEscribir(rs.getInt(StudiesLevel.S10_NO_SABE_LEER_NI_ESCRIBIR));
				levelStudies.setI11NoSabeLeerNiEscribir(rs.getInt(StudiesLevel.S11_NO_SABE_LEER_NI_ESCRIBIR));
				levelStudies.setI20TitulacioninferiorAlGradoDeEscolaridad(rs.getInt(StudiesLevel.S20_TITULACION_INFERIOR_AL_GRADO_DE_ESCOLARIDAD));
				levelStudies.setI21SinEstudios(rs.getInt(StudiesLevel.S21_SIN_ESTUDIOS));
				levelStudies.setI22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente(rs.getInt(StudiesLevel.S22_ENSENANZA_PRIMARIA_INCOMPLETA_CINCO_CURSOS_DE_EGB_O_EQUIVALENTE));
				levelStudies.setI30GraduadoEscolarOEquivalente(rs.getInt(StudiesLevel.S30_GRADUADO_ESCOLAR_O_EQUIVALENTE));
				levelStudies.setI31BachillerElemental(rs.getInt(StudiesLevel.S31_BACHILLER_ELEMENTAL_GRADUADO_ESCOLAR_EGB_COMPLETA_PRIMARIA_COMPLETA_CERTIFICADO_DE_ESCOLARIDAD_O_EQUIVALENTE));
				levelStudies.setI32FormacionProfesionalPrimerGradoOficialIndustrial(rs.getInt(StudiesLevel.S32_FORMACION_PROFESIONAL_PRIMER_GRADO_OFICIALIA_INDUSTRIAL));
				levelStudies.setI40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores(rs.getInt(StudiesLevel.S40_BACHILLER_FORMACION_PROFESIONAL_DE_2_GRADO_O_TITULOS_EQUIVALENTES_O_SUPERIORES));
				levelStudies.setI41FormacionProfesionalSegundoGradoMaestriaIndustrial(rs.getInt(StudiesLevel.S41_FORMACION_PROFESIONAL_SEGUNDO_GRADO_MAESTRIA_INDUSTRIAL));
				levelStudies.setI42BachillerSuperiorBup(rs.getInt(StudiesLevel.S42_BACHILLER_SUPERIOR_BUP));
				levelStudies.setI43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc(rs.getInt(StudiesLevel.S43_OTROS_TITULADOS_MEDIOS_AUXILIAR_DE_CLINICA_SECRETARIADO_PROGRAMADOR_INFORMATICO_AUXILIAR_DE_VUELO_DIPLOMADO_EN_ARTES_Y_OFICIOS_ETC));
				levelStudies.setI44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares(rs.getInt(StudiesLevel.S44_DIPLOMADO_DE_ESCUELAS_UNIVERSITARIAS_EMPRESARIALES_PROFESORADO_DE_EGB_ATS_Y_SIMILARES));
				levelStudies.setI45ArquitectoOIngenieroTecnico(rs.getInt(StudiesLevel.S45_ARQUITECTO_O_INGENIERO_TECNICO));
				levelStudies.setI46LicenciadoUniversitarioArquitectoOIngenieroSuperior(rs.getInt(StudiesLevel.S46_LICENCIADO_UNIVERSITARIO_ARQUITECTO_O_INGENIERO_SUPERIOR));
				levelStudies.setI47TituladosDeEstudiosSuperioresNoUniversitarios(rs.getInt(StudiesLevel.S47_TITULADOS_DE_ESTUDIOS_SUPERIORES_NO_UNIVERSITARIOS));
				levelStudies.setI48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados(rs.getInt(StudiesLevel.S48_DOCTORADO_Y_ESTUDIOS_DE_POSTGRADO_O_ESPECIALIZACION_PARA_LICENCIADOS));
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return levelStudies;
	}

	@Override
	public Integer getSpanishHabitants(Connection connection,
			String wktGeometry, Integer idEntidad, String srid) throws SQLException {
		String sqlQuery = "SELECT COUNT(*) FROM (" + getNumHabitantsQuery(idEntidad, srid) + ") AS tb WHERE nacprovincia != 66 AND intersects(transform(\"GEOMETRY\","+srid+"),geometryfromtext('"+wktGeometry+"',"+srid+"))";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer results = 0;
		
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				results = rs.getInt(1);
				if(logger.isDebugEnabled())logger.debug("Results = " + results);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return results;
	}

	@Override
	public Integer getForeignHabitants(Connection connection,
			String wktGeometry, Integer idEntidad, String srid) throws SQLException {
		String sqlQuery = "SELECT COUNT(*) FROM (" + getNumHabitantsQuery(idEntidad, srid) + ") AS tb WHERE nacprovincia = 66 AND intersects(transform(\"GEOMETRY\","+srid+"),geometryfromtext('"+wktGeometry+"',"+srid+"))";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer results = 0;
		
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				results = rs.getInt(1);
				if(logger.isDebugEnabled())logger.debug("Results = " + results);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return results;
	}

	@Override
	public ArrayList<GeomarketingFeatureOT> getFeaturesFromLayerOrderedbyAttribute(
			Connection connection,String layerQuery, String columnName) throws SQLException {
		String sqlQuery = "SELECT LAYER.id as ID,asText(transform(LAYER.\"GEOMETRY\",4230)) AS GEOM,LAYER."+columnName+" AS ATTRIBUTENAME,municipios.nombreoficial as municipio FROM ("+ layerQuery +") LAYER,municipios WHERE LAYER.id_municipio = municipios.id ORDER BY municipio,ATTRIBUTENAME";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ArrayList<GeomarketingFeatureOT> results = new ArrayList<GeomarketingFeatureOT>();
		
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			while (rs.next()){
				GeomarketingFeatureOT feature = new GeomarketingFeatureOT();
				feature.setWktGeometry(rs.getString("GEOM"));
				feature.setAttributeName(rs.getString("ATTRIBUTENAME"));
				feature.setMunicipio(rs.getString("municipio"));
				feature.setId(new Integer[]{rs.getInt("ID")});
				feature.setSrid(4230);
				results.add(feature);
				if(logger.isDebugEnabled())logger.debug("Results = " + results);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return results;
	}

	@Override
	public NameDomainOT getColumnNameFromAttNameAndLocale(Connection connection,
			String attributeName,String locale) throws SQLException {
		String sqlQuery = "select columns.name as name,columns.id_domain as id_domain from attributes,columns,dictionary where dictionary.id_vocablo = attributes.id_alias and dictionary.locale = ? and attributes.id_column = columns.id and UPPER(dictionary.traduccion) = UPPER(?)";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		NameDomainOT result = new NameDomainOT();
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1,locale);
			preparedStatement.setString(2,attributeName);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				result.setColumnName(rs.getString("name"));
				if(rs.getInt("id_domain") != 0)
					result.setDomainId(rs.getInt("id_domain"));
				
				if(logger.isDebugEnabled())logger.debug("Results = " + result);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return result;
	}
	public ArrayList<PortalStepRelationOT> getNearestStepFromPortal(Connection connection,Integer idEntidad,Integer idFeatureStreet,ArrayList<Integer> idFeatureStep) throws SQLException{
		ArrayList<PortalStepRelationOT> result = new ArrayList<PortalStepRelationOT>();
		String sqlQuery = "SELECT PORTALES.ID as id,PORTALES.ROTULO as rotulo ,TRAMOS.ID as idtramo,DISTANCE(PORTALES.\"GEOMETRY\",TRAMOS.\"GEOMETRY\") AS DISTANCE " + 
						  "FROM NUMEROS_POLICIA PORTALES, TRAMOSVIA TRAMOS " + 
						  "WHERE PORTALES.ID_VIA = ? AND PORTALES.ID_VIA = TRAMOS.ID_VIA AND PORTALES.ID_MUNICIPIO IN (SELECT ID_MUNICIPIO FROM ENTIDADES_MUNICIPIOS WHERE ID_ENTIDAD = ?) " +
						  "AND TRAMOS.ID_MUNICIPIO IN (SELECT ID_MUNICIPIO FROM ENTIDADES_MUNICIPIOS WHERE ID_ENTIDAD = ?) " +
						  "ORDER BY ROTULO,DISTANCE";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idFeatureStreet);
			preparedStatement.setInt(2,idEntidad);
			preparedStatement.setInt(3,idEntidad);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			Hashtable<Integer,PortalStepRelationOT> resultHash = new Hashtable<Integer,PortalStepRelationOT>();
			while (rs.next()){
				Integer idPortal = rs.getInt("id");
				if(!containsIdPortal(resultHash,idPortal)){
					PortalStepRelationOT relation = new PortalStepRelationOT();
					Integer idTramo = rs.getInt("idTramo");
					if(idFeatureStep.contains(idTramo)){
						relation = new PortalStepRelationOT();
						relation.setIdPortal(idPortal);
						relation.setIdTramo(idTramo);
						relation.setDistance(rs.getDouble("distance"));
					}
					resultHash.put(idPortal, relation);
				}
				if(logger.isDebugEnabled())logger.debug("Results = " + result);
		    }
			Enumeration<PortalStepRelationOT> list = resultHash.elements();
			while(list.hasMoreElements()){
				PortalStepRelationOT portalStep = list.nextElement();
				if(portalStep.getIdPortal() != null)
					result.add(portalStep);
			}
			
		}finally{
			
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		
		return result;
	}
	private boolean containsIdPortal(
			Hashtable<Integer, PortalStepRelationOT> resultHash,
			Integer idPortal) {
		Set<Integer> keys = resultHash.keySet();
		Iterator<Integer> it = keys.iterator();
		while(it.hasNext()){
			Integer id = it.next();
			if(id.intValue() == idPortal.intValue())
				return true;
		}
		return false;
	}

	public ArrayList<PostalDataOT> getPostalDataFromIdPortales(Connection connection,ArrayList<Integer> portales) throws SQLException{
		if (portales.size() == 0)
			return new ArrayList<PostalDataOT>();
		String idPortales = getIdPortales(portales);
		String sqlQuery = "select " +
				"habitantes.codigoprovincia as codigoprovincia," +
				"habitantes.codigomunicipio as codigomunicipio," +
				"habitantes.nombre as nombre," +
				"habitantes.part_apell1 as part_apell1," +
				"habitantes.apellido1 as apellido1," +
				"habitantes.part_apell2 as part_apell2," +
				"habitantes.apellido2 as apellido2," +
				"habitantes.dni as dni," +
				"habitantes.letradni as letradni," +
				"domicilio.id_via as id_via," +
				"domicilio.codigovia as codigovia," +
				"domicilio.tipovia as tipovia," +
				"domicilio.nombrevia as nombrevia," +
				"domicilio.tiponumero as tiponumero," +
				"domicilio.numero as numero," +
				"domicilio.calificadornumero," +
				"domicilio.numerosuperior as numerosuperior," +
				"domicilio.calificadornumerosuperior as calificadornumerosuperior," +
				"domicilio.kilometro as kilometro," +
				"domicilio.hectometro as hectometro," +
				"domicilio.bloque as bloque," +
				"domicilio.planta as planta," +
				"domicilio.puerta as puerta," +
				"domicilio.id_escalera as id_escalera," +
				"domicilio.tipolocal as tipolocal " +
	"from habitantes,domicilio where domicilio.id_domicilio = habitantes.id_domicilio and domicilio.id_portal in ("+ idPortales +") order by domicilio.id_domicilio,habitantes.id_habitante ";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ArrayList<PostalDataOT> result = new ArrayList<PostalDataOT>();
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			while (rs.next()){
				PostalDataOT postalData = new PostalDataOT();
				postalData.setApellido1(rs.getString("apellido1"));
				postalData.setApellido2(rs.getString("apellido2"));
				postalData.setBloque(rs.getString("bloque"));
				postalData.setCalificadornumero(rs.getString("calificadornumero"));
				postalData.setCalificadornumerosuperior(rs.getString("calificadornumerosuperior"));
				postalData.setCodigomunicipio(rs.getInt("codigomunicipio"));
				postalData.setCodigoprovincia(rs.getInt("codigoprovincia"));
				postalData.setCodigovia(rs.getInt("codigovia"));
				postalData.setDni(rs.getString("dni"));
				postalData.setHectometro(rs.getInt("hectometro"));
				postalData.setId_escalera(rs.getString("id_Escalera"));
				postalData.setId_via(rs.getInt("id_via"));
				postalData.setKilometro(rs.getInt("kilometro"));
				postalData.setLetradni(rs.getString("letradni"));
				postalData.setNombre(rs.getString("nombre"));
				postalData.setNumerosuperior(rs.getInt("numerosuperior"));
				postalData.setPart_apell1(rs.getString("part_apell1"));
				postalData.setPart_apell2(rs.getString("part_apell2"));
				postalData.setPlanta(rs.getString("planta"));
				postalData.setPuerta(rs.getString("puerta"));
				postalData.setTipolocal(rs.getString("tipolocal"));
				postalData.setTiponumero(rs.getString("tiponumero"));
				postalData.setTipovia(rs.getString("tipovia"));
				postalData.setNumero(rs.getInt("numero"));
				postalData.setNombrevia(rs.getString("nombrevia"));
				result.add(postalData);
				if(logger.isDebugEnabled())logger.debug("Results = " + result);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return result;
	}

	private String getIdPortales(ArrayList<Integer> portales) {
		Iterator<Integer> it = portales.iterator();
		String idPortales = "";
		while (it.hasNext()){
			idPortales +=it.next();
			if(it.hasNext()) idPortales+=",";
		}
		return idPortales;
	}

	@Override
	public Hashtable<String, String> getCodeBookDomainData(
			Connection connection, Integer idDomain, String locale)
			throws SQLException {
		String sqlQuery = 	"SELECT pattern,traduccion FROM domainnodes " + 
							"INNER JOIN dictionary ON " +
								"dictionary.id_vocablo = domainnodes.id_description and " +
								"dictionary.locale = ? " +
							"WHERE " +
								"domainnodes.id_domain = ? AND " +
								"domainnodes.pattern is not null AND " +
								"domainnodes.type = 7 ";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Hashtable<String,String> result = new Hashtable<String,String>();
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1,locale);
			preparedStatement.setInt(2,idDomain);
			
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			while (rs.next()){
				result.put(rs.getString("pattern"),rs.getString("traduccion"));			
		    }
			if(logger.isDebugEnabled())logger.debug("Results = " + result);
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return result;
	}
}
