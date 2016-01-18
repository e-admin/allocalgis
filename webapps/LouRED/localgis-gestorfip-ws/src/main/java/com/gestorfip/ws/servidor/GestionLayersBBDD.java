package com.gestorfip.ws.servidor;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.log4j.Logger;


import com.gestorfip.ws.utils.Constants;
import com.gestorfip.ws.xml.beans.importacion.layers.DeterminacionStyleBean;
import com.gestorfip.ws.xml.beans.importacion.layers.EdificabilidadStyleBean;
import com.gestorfip.ws.xml.beans.importacion.layers.LayerStylesBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerDeterminacionAplicadaBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerUsosRegulacionesBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfRegulacionesBean;


public class GestionLayersBBDD {
	static Logger logger = Logger.getLogger(GestionLayersBBDD.class);
	
	private static HashMap<String, String> hash_Title_NameTables = new HashMap();
	//private static HashMap<String, String> hashLayerTable = new HashMap();
//	private static HashMap<String, String> hash_Layer_Title = new HashMap();
	private static HashMap<String, String> hashDefaultStyles = new HashMap();
	

	private static void establecerDatos(){
		
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES, Constants.TABLE_NAME_ACCIONES);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES, Constants.TABLE_NAME_AFECCIONES);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION, Constants.TABLE_NAME_ALINEACION);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO, Constants.TABLE_NAME_AMBITO);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA, Constants.TABLE_NAME_CATEGORIA);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION, Constants.TABLE_NAME_CLASIFICACION);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO, Constants.TABLE_NAME_DESARROLLO);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION, Constants.TABLE_NAME_EQUIDISTRIBUCION);	
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_GESTION, Constants.TABLE_NAME_GESTION);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION, Constants.TABLE_NAME_PROTECCION);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS, Constants.TABLE_NAME_SISTEMAS);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_ZONA, Constants.TABLE_NAME_ZONA);
		hash_Title_NameTables.put(Constants.LAYER_TITLE_PLANEAMIENTO_OTRAS_INDICACIONES, Constants.TABLE_NAME_OTRAS_INDICACIONES);
		
//		hashLayerTable.put(Constants.LAYER_ACCION, Constants.TABLE_NAME_ACCIONES);
//		hashLayerTable.put(Constants.LAYER_AFECCION, Constants.TABLE_NAME_AFECCIONES);
//		hashLayerTable.put(Constants.LAYER_ALINEACION, Constants.TABLE_NAME_ALINEACION);
//		hashLayerTable.put(Constants.LAYER_AMBITO, Constants.TABLE_NAME_AMBITO);
//		hashLayerTable.put(Constants.LAYER_CATEGORIA, Constants.TABLE_NAME_CATEGORIA);
//		hashLayerTable.put(Constants.LAYER_CLASIFICACION, Constants.TABLE_NAME_CLASIFICACION);
//		hashLayerTable.put(Constants.LAYER_DESARROLLO, Constants.TABLE_NAME_DESARROLLO);
//		hashLayerTable.put(Constants.LAYER_EQUIDISTRIBUCION, Constants.TABLE_NAME_EQUIDISTRIBUCION);
//		hashLayerTable.put(Constants.LAYER_GESTION, Constants.TABLE_NAME_GESTION);
//		hashLayerTable.put(Constants.LAYER_PROTECCION, Constants.TABLE_NAME_PROTECCION);
//		hashLayerTable.put(Constants.LAYER_SISTEMAS, Constants.TABLE_NAME_SISTEMAS);
//		hashLayerTable.put(Constants.LAYER_ZONA, Constants.TABLE_NAME_ZONA);
//		hashLayerTable.put(Constants.LAYER_OTRAS_INDICACIONES, Constants.TABLE_NAME_OTRAS_INDICACIONES);
		
//		hash_Layer_Title.put(Constants.LAYER_ACCION, Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES);
//		hash_Layer_Title.put(Constants.LAYER_AFECCION, Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES);
//		hash_Layer_Title.put(Constants.LAYER_ALINEACION, Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION);
//		hash_Layer_Title.put(Constants.LAYER_AMBITO, Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO);
//		hash_Layer_Title.put(Constants.LAYER_CATEGORIA, Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA);
//		hash_Layer_Title.put(Constants.LAYER_CLASIFICACION, Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION);
//		hash_Layer_Title.put(Constants.LAYER_DESARROLLO, Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO);
//		hash_Layer_Title.put(Constants.LAYER_EQUIDISTRIBUCION, Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION);
//		hash_Layer_Title.put(Constants.LAYER_GESTION, Constants.LAYER_TITLE_PLANEAMIENTO_GESTION);
//		hash_Layer_Title.put(Constants.LAYER_PROTECCION, Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION);
//		hash_Layer_Title.put(Constants.LAYER_SISTEMAS, Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS);
//		hash_Layer_Title.put(Constants.LAYER_ZONA, Constants.LAYER_TITLE_PLANEAMIENTO_ZONA);
//		hash_Layer_Title.put(Constants.LAYER_OTRAS_INDICACIONES, Constants.LAYER_TITLE_PLANEAMIENTO_OTRAS_INDICACIONES);
	}
	
	
	
//	public static void gestionLayersFip1(Connection conn, LayerStylesBean[] lstLayersStyles) throws Exception{
//		
//		conn.setAutoCommit(false);
//		establecerDatos();
//
//		// Borrado de datos
//		for (Iterator iterator = hashTitleTables.keySet().iterator(); iterator.hasNext();) {
//			String layer = (String) iterator.next();
//			String table = hashTitleTables.get(layer);
//		
//			borradoDatosCapa(conn, table ,layer);
//		}
//		
//		borradoDatosGenerales(conn);
//		
//		// Borrado de tablas 
//		for (Iterator iterator = hashTitleTables.keySet().iterator(); iterator.hasNext();) {
//			String layer = (String) iterator.next();
//			String table = hashTitleTables.get(layer);
//			deleteTables(conn, table);
//		}
//	
//		generateLayers(conn, lstLayersStyles);
//		conn.setAutoCommit(true);
//
//		logger.info("Finish Gestion layers");
//	}
	
	
	

	/** Getion de Capas.
	 * Borra los datos de las capas del municipio
	 * Borra las tablas particulares donde se almacenan los datos configurados en la migracion
	 * Genera las tablas y layer del municipio con los datos configurados en la migracion
	 * Borra los datos de las capas anteriores
	 * Genera las capas
	 * @param conn
	 * @throws Exception
	 */
	public static void gestionLayersMigracionAsistida(Connection conn, ConfLayerBean[] lstConfLayerBean, 
									int idMunicipioLG, int idEntidad) throws Exception{
		
		conn.setAutoCommit(false);
		establecerDatos();

		// Borrado de datos
		for (Iterator iterator = hash_Title_NameTables.keySet().iterator(); iterator.hasNext();) {
			String layerName = (String) iterator.next();
			// se añanade el idMunicipio a los nombres de las layer y las tablas para diferenciarlas
			// según el municipio con el que se está trabajando 
			// tabla ej: planeamiento_gestios_18016
			// layer ej: Gestion_18016
			
			String nameLayer = layerName + "_" +idMunicipioLG;
		
			borradoDatosCapa(conn, hash_Title_NameTables.get(layerName) ,nameLayer, idMunicipioLG);
		}
	
		borradoDatosMap(conn, idEntidad);
		
		// Borrado de tablas 
		for (Iterator iterator = hash_Title_NameTables.keySet().iterator(); iterator.hasNext();) {
			String layer = (String) iterator.next();
			//unicamente se borran las tablas que tienen en identificador del municipio
			// que son las tablas que de existir tienen los datos auxiliares de la migracion
			// tabla ej: planeamiento_gestios_18016
			String table = hash_Title_NameTables.get(layer) + "_" +idMunicipioLG;
			deleteTables(conn, table);
		}
		
		// Borrado de los datos de las tablas genericas
		for (Iterator iterator = hash_Title_NameTables.keySet().iterator(); iterator.hasNext();) {
			// se borran los datos de las tablas genericas que tienen el id_municipio igual al que se esta importando
			String layer = (String) iterator.next();
			String table = hash_Title_NameTables.get(layer) + Constants.IDENTIFICADOR_TABLE_GENERICO ;
			borradoDatosTablaPorMunicipio(conn, table, idMunicipioLG) ;
		}
		
		int id_layerfamily = generarDatosMap(conn, idEntidad);
	
		generateTablesAndLayers(conn, lstConfLayerBean, idMunicipioLG, idEntidad, id_layerfamily);
		
		conn.setAutoCommit(true);

		logger.info("Finish Gestion layers");
	}
	
	private static void borradoDatosTablaPorMunicipio(Connection conn, String table, int idMunicipioLG) throws SQLException{
		
		PreparedStatement pst = null;

		String Sql = "DELETE FROM " + table + " WHERE id_municipio = " + idMunicipioLG;

		pst = conn.prepareStatement(Sql);
		pst.execute();
		
	}
	
	/** Generacion de las capas 
	 * @param conn
	 * @throws Exception
	 */
	private static void generateTablesAndLayers(Connection conn,  ConfLayerBean[] lstConfLayerBean, int idMunicipioLG, int idEntidad, int id_layerfamily) throws Exception{
		
		int index = 0;
		if(lstConfLayerBean != null){
			for(index = 0; index<lstConfLayerBean.length; index++){
			
				ConfLayerBean confLayer = lstConfLayerBean[index];
						
				generateTableAndLayer(conn,  hash_Title_NameTables.get(confLayer.getNameLayer()), confLayer.getNameLayer(),
							confLayer, idMunicipioLG, index, idEntidad, id_layerfamily);
			}
		}
		
		//se generan los datos para la capa otras_indicaciones donde se insertarán las geometrias de las entidades que no se asocien a un 
		// grupo de aplicacion
		generateTableAndLayer(conn,  hash_Title_NameTables.get(Constants.LAYER_TITLE_PLANEAMIENTO_OTRAS_INDICACIONES), 
				Constants.LAYER_TITLE_PLANEAMIENTO_OTRAS_INDICACIONES, null, idMunicipioLG, index, idEntidad, id_layerfamily);

	}
	
	
	/** Genera los estios de la layer en funcion de los datos configurados en la migracion
	 * Crea un estilo por cada columna creada en la migracion
	 * crea un pintado diferente por cada valor distinto que se encuentra en la comlumna creada en la migracion
	 * @param confLayer
	 * @param idMunicipioLG
	 * @param conn
	 * @throws SQLException
	 */
	public static void generateStylesConfig(ConfLayerBean confLayer, int idMunicipioLG, Connection conn) throws SQLException{
		
		String nameLayer = confLayer.getNameLayer();
		StringBuffer sbStyle = new StringBuffer();
		StringBuffer sbUserStyle = new StringBuffer();

		PreparedStatement pst = null;
		
		TreeMap<String, ArrayList<String>> treeMapColumnsUsoRegula = new TreeMap<String, ArrayList<String>>();
		if(confLayer != null){
			if(confLayer.getLstConfLayerDeterminacionAplicada() != null && confLayer.getLstConfLayerDeterminacionAplicada().length != 0 &&
					confLayer.getLstConfLayerDeterminacionAplicada()[0] != null){
	
				for(int i=0; i< confLayer.getLstConfLayerDeterminacionAplicada().length; i++){
					
					ConfLayerDeterminacionAplicadaBean confLayerDetApli = confLayer.getLstConfLayerDeterminacionAplicada()[i];
					if(confLayerDetApli.isSelected()){
						StringBuffer sSQL = new StringBuffer();
						
						ResultSet rs = null;
						ArrayList<String> lstValues = new ArrayList<String>();
								
						sSQL.append("SELECT DISTINCT ").append(confLayerDetApli.getAliasDeterminacion()).append(" from ")
							.append(hash_Title_NameTables.get(confLayer.getNameLayer()) + "_" +idMunicipioLG)
							.append(" WHERE ")
							.append(confLayerDetApli.getAliasDeterminacion()).append(" IS NOT NULL");
						
						pst = conn.prepareStatement(sSQL.toString());
						rs = pst.executeQuery();
						while (rs.next()) {
							lstValues.add(rs.getString(confLayerDetApli.getAliasDeterminacion()));
						}
						
						treeMapColumnsUsoRegula.put(confLayerDetApli.getAliasDeterminacion(), lstValues);
					}
				}
			}
			
			if(confLayer.getLstConfUsosRegulaciones() != null && confLayer.getLstConfUsosRegulaciones().length != 0 &&
					confLayer.getLstConfUsosRegulaciones()[0] != null){
				ArrayList<String> lstColumns = new ArrayList<String>();
				for(int i=0; i< confLayer.getLstConfUsosRegulaciones().length; i++){
					
					ConfLayerUsosRegulacionesBean confLayerUsoRegula = confLayer.getLstConfUsosRegulaciones()[i];
					if(confLayerUsoRegula.isSelected()){
						
						for(int h=0; h< confLayerUsoRegula.getLstRegulaciones().length; h++){
							
							ConfRegulacionesBean confLayerRegula = confLayerUsoRegula.getLstRegulaciones()[h];
							if(confLayerRegula.isSelected()){
								
								if(!lstColumns.contains(confLayerRegula.getAlias())){
									lstColumns.add(confLayerRegula.getAlias());
								}
							}
						}
					}
						
					for(int h=0; h< lstColumns.size(); h++){
						StringBuffer sSQL = new StringBuffer();
						
						ResultSet rs = null;
						ArrayList<String> lstValues = new ArrayList<String>();
						sSQL.append("SELECT DISTINCT ").append(lstColumns.get(h)).append(" from ")
						.append(hash_Title_NameTables.get(confLayer.getNameLayer()) + "_" +idMunicipioLG)
						.append(" WHERE ")
						.append(lstColumns.get(h)).append(" IS NOT NULL");
						
						pst = conn.prepareStatement(sSQL.toString());
						rs = pst.executeQuery();
						while (rs.next()) {
							lstValues.add(rs.getString(lstColumns.get(h)));
						}
						
						treeMapColumnsUsoRegula.put(lstColumns.get(h), lstValues);
					}
				}
				
				
			}
		}

		sbStyle.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>")
			.append("<StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
			.append("<NamedLayer>")
			.append("<Name>").append(nameLayer + "_" +idMunicipioLG).append("</Name>");

		sbUserStyle.append(hashDefaultStyles.get(nameLayer + "_" +idMunicipioLG));

		for (Iterator itDetApli = treeMapColumnsUsoRegula.keySet().iterator(); itDetApli.hasNext();) {
			String keyNameColumn = (String) itDetApli.next();
			ArrayList<String> lstValues = (ArrayList<String>)treeMapColumnsUsoRegula.get(keyNameColumn);
			
			generateUserStyle(nameLayer, keyNameColumn, lstValues, sbUserStyle);
			
		}
	
		sbStyle.append(sbUserStyle.toString());
		
		sbStyle.append("</NamedLayer>")
			.append("</StyledLayerDescriptor>");
		
		
		
		String sSQL = "SELECT id_styles FROM layers WHERE name = ?"; 
		pst = conn.prepareStatement(sSQL);
		pst.setString(1, nameLayer + "_" +idMunicipioLG);
		ResultSet rs = pst.executeQuery();
	
		int id_style = -1;
		while (rs.next()) {
			id_style = rs.getInt("id_styles");	
		}
		if(id_style != -1){
			StringBuffer sqlUpdateStyle = new StringBuffer();
			sqlUpdateStyle.append("UPDATE STYLES SET XML = '")
				.append(sbStyle.toString())
				.append("' WHERE id_style = ?");
			
			pst = conn.prepareStatement(sqlUpdateStyle.toString());
			pst.setInt(1, id_style);
			pst.executeUpdate();
		}
		
		
	}
	
	private static void generateUserStyle(String nameLayer, String keyNameColumn, ArrayList<String> lstValues, StringBuffer sbUserStyle){
		StringBuffer sbRule = new StringBuffer();
		
		sbUserStyle.append("<UserStyle>")
			.append("<Name>").append(keyNameColumn).append("</Name>")
			.append("<Title>").append(keyNameColumn).append("</Title>")
			.append("<Abstract>").append(keyNameColumn).append("</Abstract>")
			
//			.append("<Name>").append(hashLayerTitle.get(nameLayer)).append(":_:").append(keyNameColumn).append("</Name>")
//			.append("<Title>").append(hashLayerTitle.get(nameLayer)).append(":_:").append(keyNameColumn).append("</Title>")
//			.append("<Abstract>").append(hashLayerTitle.get(nameLayer)).append(":_:").append(keyNameColumn).append("</Abstract>")
			.append("<FeatureTypeStyle>");
		
		if(lstValues != null){
			for (int i = 0; i < lstValues.size(); i++) {
				String value = lstValues.get(i);	
				Color colorFill = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
				
				String hexfill = Integer.toHexString(colorFill.getRGB());
				String hexStroke = Integer.toHexString(colorFill.getRGB());
						
				sbRule.delete(0, sbRule.length());
				
				sbRule = 
				sbRule.append("<Rule>")
					.append("<Name>").append(value.replace("'", "''")).append("</Name>")
					.append("<Title>").append(value.replace("'", "''")).append("</Title>")
					
					.append("<ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\">")
					.append("<ogc:PropertyIsEqualTo>")
					.append("<ogc:PropertyName>").append(keyNameColumn).append("</ogc:PropertyName>")
					.append("<ogc:Literal>").append(value.replace("'", "''")).append("</ogc:Literal>")
					.append("</ogc:PropertyIsEqualTo>")
					.append("</ogc:Filter>")
				
					.append("<MinScaleDenominator>0.0</MinScaleDenominator>")
					.append("<MaxScaleDenominator>9.0E99</MaxScaleDenominator>")
					.append("<PolygonSymbolizer>")
					.append("<Fill>")
					.append("<CssParameter name=\"fill\">#").append(hexfill.substring(0, 6))
					.append("</CssParameter>")
					.append("<CssParameter name=\"fill-opacity\">1.0</CssParameter>")
					.append("</Fill>")
					.append("<Stroke>")
					.append("<CssParameter name=\"stroke\">#").append(hexStroke.substring(0, 6))
					.append("</CssParameter>")
					.append("<CssParameter name=\"stroke-linecap\">round</CssParameter>")
					.append("<CssParameter name=\"stroke-linejoin\">round</CssParameter>")
					.append("<CssParameter name=\"stroke-opacity\">1.0</CssParameter>")
					.append("<CssParameter name=\"stroke-width\">")
					.append("<ogc:Literal>1</ogc:Literal>")
					.append("</CssParameter>")
					.append("</Stroke>")
					.append("</PolygonSymbolizer>")
					.append("</Rule>");
					
				sbUserStyle.append(sbRule.toString());
			}
		}
		
		//creamos una regla para los elementos que no cumplan los valores encontrados en la columna como por ejemplo
		// los valores que estan vacios o nulos.
		StringBuffer sbUserStyleOther = new StringBuffer();
		sbUserStyleOther.append("<Rule>")
			.append("<Name>").append("Otros").append("</Name>")
			.append("<Title>").append("Otros").append("</Title>");
		
		if(lstValues != null && lstValues.size() != 0){
			// si no hay valores no se introduce Filter
			sbUserStyleOther.append("<ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\">")
			.append("<ogc:Not>");
		
			if(lstValues.size()>1){
				sbUserStyleOther.append("<ogc:Or>");
			}
		
			StringBuffer sbUserStyleOtherFilter = new StringBuffer();
			for (int i = 0; i < lstValues.size(); i++) {
				String value = lstValues.get(i);	
			
				sbUserStyleOtherFilter.append("<ogc:PropertyIsEqualTo>")
					.append("<ogc:PropertyName>").append(keyNameColumn).append("</ogc:PropertyName>")
					.append("<ogc:Literal>").append(value.replace("'", "''")).append("</ogc:Literal>")
					.append("</ogc:PropertyIsEqualTo>");
			}
	
			sbUserStyleOther.append(sbUserStyleOtherFilter.toString());
			if(lstValues.size()>1){
				sbUserStyleOther.append("</ogc:Or>");
			}
			sbUserStyleOther.append("</ogc:Not>")
				.append("</ogc:Filter>");
		}
	
		sbUserStyleOther.append("<MinScaleDenominator>0.0</MinScaleDenominator>")
			.append("<MaxScaleDenominator>9.0E99</MaxScaleDenominator>")
			.append("<PolygonSymbolizer>")
			.append("<Fill>")
			.append("<CssParameter name=\"fill\">#ffffff</CssParameter>")
			.append("<CssParameter name=\"fill-opacity\">0.0</CssParameter>")
			.append("</Fill>")
			.append("<Stroke>")
			.append("<CssParameter name=\"stroke\">#0066ff</CssParameter>")
			.append("<CssParameter name=\"stroke-linecap\">round</CssParameter>")
			.append("<CssParameter name=\"stroke-linejoin\">round</CssParameter>")
			.append("<CssParameter name=\"stroke-opacity\">1.0</CssParameter>")
			.append("<CssParameter name=\"stroke-width\">")
			.append("<ogc:Literal>1</ogc:Literal>")
			.append("</CssParameter>")
			.append("</Stroke>")
			.append("</PolygonSymbolizer>")
			.append("</Rule>");
		
		
		
		sbUserStyle.append(sbUserStyleOther.toString()).append("</FeatureTypeStyle>")
			.append("</UserStyle>");
	}
	
	
	
	/** Generacion de la tabla donde se almacenan los datos y la geometria de la capa
	 * @param conn
	 * @param nameTable
	 * @param confLayer
	 * @param idMunicipioLG
	 * @return Devuelve una lista con los campos nuevos insertados en la tabla auxiliar nametable_idMunicipio
	 * @throws Exception
	 */
	private static ArrayList<String> createTable(Connection conn, String nameTable, ConfLayerBean confLayer, int idMunicipioLG) throws Exception{

		PreparedStatement pst = null;
		StringBuffer sbSql = new StringBuffer();
		ArrayList<String> listCamposTableAux = new ArrayList<String>();
		
		StringBuffer nameTableMun = new StringBuffer();
		nameTableMun.append(nameTable).append("_").append(idMunicipioLG);
				
		if(confLayer.getLstConfLayerDeterminacionAplicada() != null){
			for(int i=0; i<confLayer.getLstConfLayerDeterminacionAplicada().length; i++){
				if(confLayer.getLstConfLayerDeterminacionAplicada()[i].isSelected()){
					if(!listCamposTableAux.contains(confLayer.getLstConfLayerDeterminacionAplicada()[i].getAliasDeterminacion().trim())){
						listCamposTableAux.add(confLayer.getLstConfLayerDeterminacionAplicada()[i].getAliasDeterminacion().trim());
					}
				}
			}
		}
		
		if(confLayer.getLstConfUsosRegulaciones() != null){
			for(int i=0; i<confLayer.getLstConfUsosRegulaciones().length; i++){
				if(confLayer.getLstConfUsosRegulaciones()[i].isSelected()){
					if(confLayer.getLstConfUsosRegulaciones()[i].getLstRegulaciones() != null && 
							confLayer.getLstConfUsosRegulaciones()[i].getLstRegulaciones()[0] != null){
				
						for(int j=0; j<confLayer.getLstConfUsosRegulaciones()[i].getLstRegulaciones().length; j++){
							if(confLayer.getLstConfUsosRegulaciones()[i].getLstRegulaciones()[j].isSelected()){

								if(!listCamposTableAux.contains(confLayer.getLstConfUsosRegulaciones()[i].getLstRegulaciones()[j].getAlias().trim())){
									listCamposTableAux.add(confLayer.getLstConfUsosRegulaciones()[i].getLstRegulaciones()[j].getAlias().trim());
								}
							}
						}
					}
				}
			}
		}
	
		if(!listCamposTableAux.isEmpty()){	
			sbSql.append("CREATE TABLE ").append("public.").append(nameTableMun).append(" (id NUMERIC(8,0) NOT NULL,");
			for(int j=0; j<listCamposTableAux.size(); j++){
				
				sbSql.append(listCamposTableAux.get(j)).append(" VARCHAR(10000)");
				if(j != (listCamposTableAux.size()-1)){
					sbSql.append(",");
				}
			}	
		
			sbSql.append(") WITH OIDS");
			pst = conn.prepareStatement(sbSql.toString());
			pst.execute();

			// creamos la foreign key
			sbSql =  new StringBuffer();
			sbSql.append("ALTER TABLE ONLY public.").append(nameTableMun).append(" ADD CONSTRAINT ").append(nameTableMun).append("_fk")
				.append(" FOREIGN KEY ").append("(id)").append(" REFERENCES ")
				.append(nameTable).append("_gen(").append("id").append(") ").append("ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE");

			pst = conn.prepareStatement(sbSql.toString());
			pst.execute();
			
			try{
				String SqlGrant = "GRANT SELECT ON "+nameTableMun+" to localgisbackup";
				pst = conn.prepareStatement(SqlGrant);
				pst.execute();
			}
			catch(Exception ex){
				logger.info("Si no existe el usuario localgisbackup continua el proceso");
				logger.info(ex);
			}

		}
		return listCamposTableAux;
	}

	
	/**Devuelve una lista con los alias de las regulaciones configuradas en la importación para crear las columnas en la tabla correspondiente
	 * @param layerStyles
	 * @return
	 */
	private static ArrayList<String> getListAliasRegulaciones( LayerStylesBean layerStyles){
		ArrayList<String> lstAlias = new ArrayList<String>();
		if(layerStyles != null && layerStyles.getLstRegulaciones() != null && layerStyles.getLstRegulaciones().length != 0){
			for(int i=0; i< layerStyles.getLstRegulaciones().length; i++){
				DeterminacionStyleBean regula = layerStyles.getLstRegulaciones()[i];
				if(!lstAlias.contains(regula.getAlias())){
					lstAlias.add(regula.getAlias());
				}
			}
		}
		return lstAlias;
		
	}

	
	/** Generacion de la capa 
	 * @param conn
	 * @param table_acciones, nombre de la tabla
	 * @param layerTitle, titulo de la layer
	 * @throws Exception
	 */
	private static void generateTableAndLayer(Connection conn, String nameTable, String layerTitle, 
			ConfLayerBean confLayer, int idMunicipioLG, int indexPosLayer, int idEntidad, int id_layerfamily) throws Exception{
		int indexColumns = 1;
		PreparedStatement pst = null;
		StringBuffer sbSql = new StringBuffer();

		ArrayList<String> listCamposTableAux = new ArrayList<String>();
		if(confLayer != null){
			listCamposTableAux = createTable(conn, nameTable, confLayer, idMunicipioLG);
		}
		
		String nameTableGen = nameTable + Constants.IDENTIFICADOR_TABLE_GENERICO;
		
		String layerTitleMun = layerTitle + "_" + idMunicipioLG;
		insertDictionary(conn, layerTitleMun);

		sbSql =  new StringBuffer();
		StringBuffer sbSqlStyleDefault = new StringBuffer();
		sbSql.append("INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'")
			.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
			.append("<NamedLayer><Name>").append(layerTitleMun).append("</Name>");
		
		sbSqlStyleDefault.append("<UserStyle><Name>default:").append(layerTitleMun).append("</Name><Title>default:").append(layerTitleMun).append("</Title><Abstract>default:").append(layerTitleMun).append("</Abstract><FeatureTypeStyle><Name>").append(layerTitleMun).append("</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator>")
			.append("<MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter>")
			.append("<CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator>")
			.append("<MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter>")
			.append("<CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName>")
			.append("<Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter>")
			.append("<CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle>");
		
		sbSql.append(sbSqlStyleDefault.toString());
		sbSql.append("</NamedLayer></StyledLayerDescriptor>')");
		
		hashDefaultStyles.put(layerTitleMun, sbSqlStyleDefault.toString());
		
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'").append(layerTitleMun).append("',1,").append(idEntidad).append(")");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
			
		sbSql =  new StringBuffer();
//		sbSql.append("INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((SELECT ID_LAYERFAMILY FROM public.maps_layerfamilies_relations WHERE id_map=151 AND id_entidad=0),CURRVAL('seq_layers'),"+indexPosLayer+")");
		sbSql.append("INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (")
			.append(id_layerfamily).append(",CURRVAL('seq_layers'),"+indexPosLayer+")");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();

		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) VALUES ("+Constants.IS_MAP_GESTORFIP +",")
		.append(id_layerfamily).append(",CURRVAL('seq_layers'),CURRVAL('seq_styles'),'default:")
			.append(layerTitleMun).append("', true,"+ indexPosLayer +",").append(idEntidad).append(",true,true)");
//		sbSql.append("INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) VALUES ("+Constants.IS_MAP_GESTORFIP +",(SELECT ID_LAYERFAMILY FROM public.maps_layerfamilies_relations WHERE id_map=151 AND id_entidad=0),CURRVAL('seq_layers'),CURRVAL('seq_styles'),'default:")
//		.append(layerTitleMun).append("', true,"+ indexPosLayer +",").append(idEntidad).append(",true,true)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'").append(nameTableGen).append("',0)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		// Insercion de columna GEOMETRY
		insertColumnGeometry(conn, indexColumns++);
		// Insercion de columna ID
		insertColumnID(conn, Constants.ATT_ID, indexColumns++, 8, 0, 2);		
		// Insercion de columna CLAVE
		insertColumn(conn, Constants.ATT_CLAVE, indexColumns++, 255, 3);		
		// Insercion de columna CODIGO
		insertColumn(conn, Constants.ATT_CODIGO, indexColumns++, 10, 3);
		// Insercion de columna ETIQUETA
		insertColumn(conn, Constants.ATT_ETIQUETA, indexColumns++, 255, 3);
		// Insercion de columna NOMBRE
		insertColumn(conn, Constants.ATT_NOMBRE, indexColumns++, 255, 3);
		// Insercion de columna GRUPOAPLICACION
		insertColumn(conn, Constants.ATT_GRUPOAPLICACION, indexColumns++,255, 3);
		
		if(!listCamposTableAux.isEmpty()){
			// Debido a la configuración de importacion, se ha creado la tabla auxiliar de datos, asi que
			// se almacena la tabla creada
			StringBuffer nameTableMun = new StringBuffer();
			nameTableMun.append(nameTable).append("_").append(idMunicipioLG);
			
			sbSql =  new StringBuffer();
			sbSql.append("INSERT INTO TABLES(ID_TABLE,NAME) VALUES (NEXTVAL('seq_tables'),'").append(nameTableMun.toString()).append("')");
			pst = conn.prepareStatement(sbSql.toString());
			pst.execute();
		}
		
		if(!listCamposTableAux.isEmpty()){
			// Insercion de las columnas configuradas en la migracion
			for(int i=0; i<listCamposTableAux.size(); i++){
				insertColumn(conn, listCamposTableAux.get(i), indexColumns++, 10000, 3);
			}
		}

		generateQueriesLayer(conn, nameTable, listCamposTableAux, idMunicipioLG);

	}

	
	private static void insertColumnGeometry(Connection conn, int index) throws Exception{
//		Insercion columna Geometry
		PreparedStatement pst = null;
		StringBuffer sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,\"Length\",\"Precision\",\"Scale\",\"Type\") VALUES (NEXTVAL('seq_columns'),'").append(Constants.ATT_GEOMETRIA).append("',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		insertDictionary(conn, Constants.ATT_GEOMETRIA);
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),"+(index)+",true)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
	}
	
	
	private static void insertColumnID(Connection conn, String attribute, int index, int precision, int scale, int type) throws Exception{
		// Insercion de columna
		PreparedStatement pst = null;
		StringBuffer sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,\"Length\",\"Precision\",\"Scale\",\"Type\") VALUES (NEXTVAL('seq_columns'),'").append(attribute).append("',10007,CURRVAL('seq_tables'),NULL,").append(precision).append(",").append(scale).append(",").append(type).append(")");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		insertDictionary(conn, attribute );
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),"+index+",true)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
	}
	
	private static void insertColumn(Connection conn, String attribute, int index, int tamanio, int type) throws Exception{
		// Insercion de columna

		PreparedStatement pst = null;
		StringBuffer sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,\"Length\",\"Precision\",\"Scale\",\"Type\") VALUES (NEXTVAL('seq_columns'),'").append(attribute).append("',10007,CURRVAL('seq_tables'),").append(tamanio).append(",NULL,NULL,").append(type).append(")");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		insertDictionary(conn, attribute );
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),"+index+",true)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0)");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
	}
	
	

	
	private static int generarDatosMap(Connection conn, int idEntidad) throws Exception{
		PreparedStatement pst = null;
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT id_vocablo FROM dictionary WHERE traduccion = ?");
		pst = conn.prepareStatement(sbSql.toString());
		pst.setString(1, "Familia Capas Gestor de Planeamiento - FIP");
		ResultSet rs = pst.executeQuery();
		int id_vocablo = -1;
		while (rs.next()) {
			id_vocablo = rs.getInt("id_vocablo");
		}

		sbSql = new StringBuffer();
		sbSql.append("SELECT id_layerfamily FROM layerfamilies WHERE id_name = ? AND id_description = ?");
		pst = conn.prepareStatement(sbSql.toString());
		pst.setInt(1, id_vocablo);
		pst.setInt(2, id_vocablo);
		rs = pst.executeQuery();
		int id_layerfamily = -1;
		while (rs.next()) {
			id_layerfamily = rs.getInt("id_layerfamily");
		}
		
		
		//// ///// ///// /////
		insertDictionary(conn, Constants.NAME_MAP );
	
		sbSql = new StringBuffer();
		sbSql.append("INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(").append(Constants.IS_MAP_GESTORFIP)
			.append(",CURRVAL('seq_dictionary'),'<?xml version=\"1.0\" encoding=\"UTF-8\"?><mapDescriptor><description>")
			.append(Constants.NAME_MAP).append("</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>UTM 30N ETRS89</mapProjection><mapName>")
			.append(Constants.NAME_MAP).append("</mapName></mapDescriptor>',").append(idEntidad).append(");");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();

		sbSql = new StringBuffer();
		sbSql.append("INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (")
			.append(Constants.IS_MAP_GESTORFIP).append(",").append(id_layerfamily).append(",0,").append(idEntidad).append(")");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		return id_layerfamily;
	}
	
	
	/** Insercion de datos en la tabla dictionary
	 * @param conn
	 * @param texto
	 * @throws Exception
	 */
	private static void insertDictionary(Connection conn, String texto) throws Exception{
		
		PreparedStatement pst = null;
		StringBuffer sbSql = new StringBuffer();

		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'").append(Constants.ES_ES).append("','").append(texto).append("')");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'").append(Constants.CA_ES).append("','").append(Constants.CAT + texto).append("')");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'").append(Constants.GL_ES).append("','").append(Constants.GL + texto).append("')");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
			
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'").append(Constants.VA_ES).append("','").append(Constants.VA + texto).append("')");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'").append(Constants.EU_ES).append("','").append(Constants.EU + texto).append("')");
		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
	
	}
	
	/** Insercion de datos en la tabla queries
	 * @param conn
	 * @param nameTable
	 * @throws Exception
	 */
	
	private static void generateQueriesLayer(Connection conn, String nameTable, ArrayList<String> listCamposTableAux, int idMunicipioLG) throws Exception {
		PreparedStatement pst = null;
		StringBuffer sbSql = new StringBuffer();
		StringBuffer sbSqlSelect = new StringBuffer();
		StringBuffer sbSqlUpdate = new StringBuffer();
		StringBuffer sbSqlInsert = new StringBuffer();
		StringBuffer sbSqlDelete = new StringBuffer();
		
		String nameTableGen = nameTable + Constants.IDENTIFICADOR_TABLE_GENERICO;
		String nameTableMun = nameTable + "_" + idMunicipioLG;;

		sbSql =  new StringBuffer();
		sbSql.append("INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1");
			
		// SELECT
		sbSqlSelect.append("'SELECT transform(\"")
		.append(nameTableGen).append("\".\"GEOMETRY\", ?T) AS \"GEOMETRY\",\"")
		.append(nameTableGen).append("\".\"").append(Constants.ATT_ID.toLowerCase()).append("\",\"")
		.append(nameTableGen).append("\".\"").append(Constants.ATT_ID_MUNICIPIO.toLowerCase()).append("\",\"")
		.append(nameTableGen).append("\".\"").append(Constants.ATT_CLAVE.toLowerCase()).append("\",\"")
		.append(nameTableGen).append("\".\"").append(Constants.ATT_CODIGO.toLowerCase()).append("\",\"")
		.append(nameTableGen).append("\".\"").append(Constants.ATT_ETIQUETA.toLowerCase()).append("\",\"")
		.append(nameTableGen).append("\".\"").append(Constants.ATT_NOMBRE.toLowerCase()).append("\",\"")
		.append(nameTableGen).append("\".\"").append(Constants.ATT_GRUPOAPLICACION.toLowerCase()).append("\"");
		// se añaden los campos de las tablas configuradas en la migracion
		if(!listCamposTableAux.isEmpty()){
			sbSqlSelect.append(",");
			// Insercion de las columnas configuradas en la migracion
			for(int i=0; i<listCamposTableAux.size(); i++){
				String nameCampo = listCamposTableAux.get(i);
				sbSqlSelect.append("\"").append(nameTableMun).append("\".\"").append(nameCampo.toLowerCase()).append("\"");
				if(i<(listCamposTableAux.size()-1)){
					sbSqlSelect.append(",");
				}
			}
		}
		sbSqlSelect.append(" FROM \"").append(nameTableGen);
		
		if(!listCamposTableAux.isEmpty()){
			//se añade la nueva tabla a la clausula FROM
			sbSqlSelect.append("\",\"").append(nameTableMun);
		}
		sbSqlSelect.append("\" WHERE \"").append(nameTableGen).append("\".\"id_municipio\" IN (?M)");
		if(!listCamposTableAux.isEmpty()){
			sbSqlSelect.append(" AND ").append("\"").append(nameTableGen).append("\".\"").append(Constants.ATT_ID.toLowerCase()).append("\" = ")
				.append("\"").append(nameTableMun).append("\".\"").append(Constants.ATT_ID.toLowerCase()).append("\"");
		}
		sbSqlSelect.append("'");
		
		
		
		// UPDATE
		int indexUpdate = 3;
		sbSqlUpdate.append("'UPDATE \"").append(nameTableGen).append("\" SET \"GEOMETRY\"=transform(GeometryFromText(text(?1),?S), ?T),\"id_municipio\" = ?M")
		.append(",").append("\"").append(Constants.ATT_CLAVE.toLowerCase()).append("\"").append(" = ?").append(indexUpdate++)
		.append(",").append("\"").append(Constants.ATT_CODIGO.toLowerCase()).append("\"").append(" = ?").append(indexUpdate++)
		.append(",").append("\"").append(Constants.ATT_ETIQUETA.toLowerCase()).append("\"").append(" = ?").append(indexUpdate++)
		.append(",").append("\"").append(Constants.ATT_NOMBRE.toLowerCase()).append("\"").append(" = ?").append(indexUpdate++)
		.append(",").append("\"").append(Constants.ATT_GRUPOAPLICACION.toLowerCase()).append("\"").append(" = ?").append(indexUpdate++)
		.append(" WHERE \"id\"=?2;");
		if(!listCamposTableAux.isEmpty()){
			// Insercion de las columnas configuradas en la migracion
			sbSqlUpdate.append("UPDATE \"").append(nameTableMun).append("\" SET ");
			for(int i=0; i<listCamposTableAux.size(); i++){
				String nameCampo = listCamposTableAux.get(i);
				
				sbSqlUpdate.append("\"").append(nameCampo).append("\"").append(" = ?").append(indexUpdate++);
				if(i<(listCamposTableAux.size()-1)){
					sbSqlUpdate.append(",");
				}
			}
			sbSqlUpdate.append(" WHERE \"id\"=?2;");
		}
		sbSqlUpdate.append("'");
		
		// INSERT
		int indexInsert = 3;
		sbSqlInsert.append("'INSERT INTO \"").append(nameTableGen).append("\" (\"GEOMETRY\",\"id\",\"id_municipio\"")
			.append(",").append("\"").append(Constants.ATT_CLAVE.toLowerCase()).append("\"")
			.append(",").append("\"").append(Constants.ATT_CODIGO.toLowerCase()).append("\"")
			.append(",").append("\"").append(Constants.ATT_ETIQUETA.toLowerCase()).append("\"")
			.append(",").append("\"").append(Constants.ATT_NOMBRE.toLowerCase()).append("\"")
			.append(",").append("\"").append(Constants.ATT_GRUPOAPLICACION.toLowerCase()).append("\"")
			.append(") VALUES (transform(GeometryFromText(text(?1),?S), ?T),?PK,?M")
			.append(",?").append(indexInsert++)
			.append(",?").append(indexInsert++)
			.append(",?").append(indexInsert++)
			.append(",?").append(indexInsert++)
			.append(",?").append(indexInsert++)
			.append(");");
		if(!listCamposTableAux.isEmpty()){
			sbSqlInsert.append("INSERT INTO \"").append(nameTableMun).append("\" (\"id\",");
			for(int i=0; i<listCamposTableAux.size(); i++){
				String nameCampo = listCamposTableAux.get(i);
				sbSqlInsert.append("\"").append(nameCampo).append("\"");
				if(i<(listCamposTableAux.size()-1)){
					sbSqlInsert.append(",");
				}
			}
			sbSqlInsert.append(") VALUES (currval(''seq_").append(nameTableGen).append("''),");
			for(int i=0; i<listCamposTableAux.size(); i++){
				sbSqlInsert.append("?").append(indexInsert++);
				if(i<(listCamposTableAux.size()-1)){
					sbSqlInsert.append(",");
				}	
			}
			sbSqlInsert.append(");");
		}
		sbSqlInsert.append("'");
		
		
		//DELETE
		sbSqlDelete.append("'DELETE FROM \"").append(nameTableGen).append("\" WHERE \"id\" = ?2'");
		
		sbSql.append(",").append(sbSqlSelect.toString())
			.append(",").append(sbSqlUpdate.toString())
			.append(",").append(sbSqlInsert.toString())
			.append(",").append(sbSqlDelete.toString())
			.append(")");
		

		pst = conn.prepareStatement(sbSql.toString());
		pst.execute();
		
	}
	
	
	
	/**
	 * @param conn
	 * @param tableName
	 * @param layerName
	 * @throws Exception
	 */
	private static void borradoDatosCapa(Connection conn, String tableName, String layerName, int idMunicipioLG) throws Exception{
		String sSQL;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int idLayer = -1;
		int idStyle = -1;
				 
		String tableNameMun = tableName+ "_" +idMunicipioLG;
		String tableNameGen = tableName+ Constants.IDENTIFICADOR_TABLE_GENERICO;
			
		sSQL = "SELECT id_layer, id_name FROM public.layers WHERE name = ?";
		pst = conn.prepareStatement(sSQL);
		pst.setString(1, layerName);
		rs = pst.executeQuery();
		int idNameLayers = -1;
		while (rs.next()) {
			idLayer = rs.getInt("id_layer");
			idNameLayers = rs.getInt("id_name");
		}
		

		sSQL = "SELECT id_style, id_layerfamily FROM public.layers_styles WHERE id_map = ? AND id_layer = ?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, Constants.IS_MAP_GESTORFIP);
		pst.setInt(2, idLayer);
		rs = pst.executeQuery();
	
		int id_layerfamily = -1;
		while (rs.next()) {
			idStyle = rs.getInt("id_style");
			id_layerfamily = rs.getInt("id_layerfamily");
			
		}
		
		//borrado layer_styles
		sSQL = "DELETE FROM public.layers_styles WHERE id_map=? AND id_layer = ?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, Constants.IS_MAP_GESTORFIP);
		pst.setInt(2, idLayer);
		pst.executeUpdate();
		
		//borrado layerfamilies_layers_relations
		sSQL = "DELETE FROM public.layerfamilies_layers_relations WHERE id_layer=? AND id_layerfamily = ?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idLayer);
		pst.setInt(2, id_layerfamily);
		pst.executeUpdate();
		
		//borrado style
		sSQL = "DELETE FROM public.styles WHERE id_style=?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idStyle);
		pst.executeUpdate();
		
		
		//borrado dictionary
		sSQL = "DELETE FROM public.dictionary WHERE id_vocablo=?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idNameLayers);
		pst.executeUpdate();
		

		deleteDatosTables( conn, tableNameMun);
		//deleteDatosTables( conn, tableNameGen);
			

		int idAlias = -1;
		sSQL = "SELECT id_alias FROM public.attributes WHERE id_layer = ?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idLayer);
		rs = pst.executeQuery();
		while (rs.next()) {
			idAlias = rs.getInt("id_alias");
			
			//borrado dictionary
			sSQL = "DELETE FROM public.dictionary WHERE id_vocablo = ?";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idAlias);
			pst.executeUpdate();
			
			//borrado attibutes
			sSQL = "DELETE FROM public.attributes WHERE id_alias = ? AND id_layer = ?";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idAlias);
			pst.setInt(2, idLayer);
			pst.executeUpdate();
		}
	
		//borrado queries
		sSQL = "DELETE FROM public.queries WHERE id_layer=?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idLayer);
		pst.executeUpdate();
		
		//borrado layer
		sSQL = "DELETE FROM public.layers WHERE id_layer=?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idLayer);
		pst.executeUpdate();	
		 
	}
	
	private static void deleteDatosTables(Connection conn, String tableName) throws SQLException{
	
		String sSQL;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int idTable = -1;
		
		
		sSQL = "SELECT id_table FROM public.tables WHERE name = ?";
		pst = conn.prepareStatement(sSQL);
		pst.setString(1, tableName);
		rs = pst.executeQuery();
		while (rs.next()) {
			idTable = rs.getInt("id_table");
		}

		sSQL = "DELETE FROM public.tables WHERE id_table=?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idTable);
		pst.executeUpdate();
		
		
		int idColumn = -1;
		sSQL = "SELECT id FROM public.columns WHERE id_table=?";
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idTable);
		rs = pst.executeQuery();
		while (rs.next()) {
			
			idColumn = rs.getInt("id");
			//borrado columns
			sSQL = "DELETE FROM public.columns_domains WHERE id_column=?";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idColumn);
			pst.executeUpdate();
			
			//borrado columns
			sSQL = "DELETE FROM public.columns WHERE id = ? AND id_table = ?";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idColumn);
			pst.setInt(2, idTable);
			pst.executeUpdate();
		}
		
		
	}
	
	private static void borradoDatosMap(Connection conn, int idEntidad) throws Exception{
		String sSQL;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try{

			int idLayerFamily = -1;
			sSQL = "SELECT id_layerfamily FROM public.maps_layerfamilies_relations WHERE id_map = ? AND id_entidad = ?";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, Constants.IS_MAP_GESTORFIP);
			pst.setInt(2, idEntidad);
			rs = pst.executeQuery();
			while (rs.next()) {
				idLayerFamily = rs.getInt("id_layerfamily");
				
				
				//borrado maps_layerfamilies_relations
				sSQL = "DELETE FROM public.maps_layerfamilies_relations WHERE id_map = ? AND id_entidad = ?";
				pst = conn.prepareStatement(sSQL);
				pst.setInt(1, Constants.IS_MAP_GESTORFIP);
				pst.setInt(2, idEntidad);
				pst.executeUpdate();

			}
	
			int idName = -1;
			sSQL = "SELECT id_name FROM public.maps WHERE id_map = ? AND id_entidad = ?";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, Constants.IS_MAP_GESTORFIP);
			pst.setInt(2, idEntidad);
			rs = pst.executeQuery();
			while (rs.next()) {
			
				idName = rs.getInt("id_name");
				
				sSQL = "DELETE FROM public.dictionary WHERE id_vocablo = ?";
				pst = conn.prepareStatement(sSQL);
				pst.setInt(1, idName);
				pst.executeUpdate();
				
				
				//borrado maps
				sSQL = "DELETE FROM public.maps WHERE id_map=? AND id_entidad = ?";
				pst = conn.prepareStatement(sSQL);
				pst.setInt(1, Constants.IS_MAP_GESTORFIP);
				pst.setInt(2, idEntidad);
				pst.executeUpdate();
						
			}
		
		}
		catch (Exception e) {
			logger.error(e);
			e.printStackTrace();	
			throw e;
		}
	}
	
	/**Borrado de tablas donde se guardan los datos de las capas
	 * @param conn
	 * @param layer
	 * @throws Exception
	 */
	private static void deleteTables(Connection conn, String table) throws Exception{
		PreparedStatement pst = null;

		String Sql = "DROP TABLE if exists public." + table;

		pst = conn.prepareStatement(Sql);
		pst.execute();

	}

}
