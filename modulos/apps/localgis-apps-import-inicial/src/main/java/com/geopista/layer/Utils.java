/**
 * Utils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.layer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.app.layerutil.layer.Query;
import com.geopista.app.layerutil.layer.exportimport.beans.LocalGISXmlLayer;
import com.geopista.app.layerutil.layer.exportimport.utils.ExportImportUtils;
import com.geopista.app.layerutil.layerfamily.LayerFamilyOperations;
import com.geopista.app.layerutil.layerfamily.LayerFamilyTable;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.feature.Attribute;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.Table;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.thoughtworks.xstream.XStream;
import com.vividsolutions.jump.util.Blackboard;
import com.geopista.security.SecurityManager;
import com.geopista.util.config.UserPreferenceStore;

public class Utils {
	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	private static Logger logger = Logger.getLogger(Utils.class);
	private static Blackboard Identificadores = aplicacion.getBlackboard();
	private static XStream xStreamSerializer;
	public static final int POSTGRE = 1;
	public static final int ORACLE = 2;
	private static final String VALIDATOR_CLASS = "com.geopista.protocol.VersionValidator";
	private static final String EXT_FICHERO_XML = ".xml";
	private static String URL = ((AppContext) AppContext
			.getApplicationContext())
			.getString("geopista.conexion.servidorurl")
			+ "Administracion";

	public static boolean importLayerFromXml(File[] xmlFiles,
			Boolean importTables, Boolean useCoincidentTables,
			Boolean importDomains, Boolean importLayers,
			Boolean useCoincidentLayers, Boolean importFamilies,
			Boolean useCoincidentFamilies, Boolean exportData, Properties props) {
		final File[] finalXMLFiles = xmlFiles;

		String servidor = (String) props.getProperty("servidor");
		String user = (String) props.getProperty("user");
		String pass = (String) props.getProperty("pass");

		logger.info("Servidor:" + servidor);
		logger.info("User:" + user);
		logger.info("pass:" + pass);
		
		if (servidor.contains("https"))
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_PROTOCOL,"https");
		else
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_PROTOCOL,"http");
		

		// Fijamos los datos de conexion
		AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
		aplicacion.setHeartBeat(false);
		aplicacion.setUrl(servidor + WebAppConstants.GEOPISTA_WEBAPP_NAME);

		AppContext.forceLocalgisDatabaseURL("jdbc:pista:" + servidor
				+ "/Principal/CServletDB");

		URL = servidor + "/Administracion";
		try{
			try {
				if (SecurityManager.login(user, pass, "Geopista")) {
					logger.info("Usuario autenticado");
					UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,user);
	
				} else {
					logger.info("Usuario no autenticado");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
	
			int layersImported = 0;
			int layersToImport = 0;
			if (xmlFiles != null)
				layersToImport = xmlFiles.length;
			int i = 0;
	
			logger.info("Numero de capas a importar:" + layersToImport);
			while (i < layersToImport) {
	
				try {
	
					//if (i == 1)
					//	return true;
					File finalXMLFile = (File) finalXMLFiles[i];
	
					logger.info(i+".------------------PROCESANDO FICHERO."+finalXMLFile.getName()+"--------------------");
					LayerOperations layerOp = new LayerOperations();
					TablesDBOperations tableOp = new TablesDBOperations();
					LayerFamilyOperations layerFamilyOp = new LayerFamilyOperations();
					
					if (layerFamilyOp.conn==null){
						logger.error("Error al procesar las capas de importacion");
						return false;
					}
					
					LocalGISXmlLayer localGISXmlLayer = null;
					localGISXmlLayer = (LocalGISXmlLayer) getXStreamSerializer().fromXML(finalXMLFile);
		
					// IMPORTAR Tabla
					HashMap<String, String> oldTablesName = null;
					ArrayList newTables = null;
					if (importTables) {
						newTables = localGISXmlLayer.getTables();
						if (newTables != null) {
							oldTablesName = new HashMap<String, String>();
							Iterator itTables = newTables.iterator();
							while (itTables.hasNext()){
									Table newTable = (Table) itTables.next();
									if (newTable.getDescription() != null
											&& !newTable.getDescription().equals("")) {	
										
										//Comprobamos si existe una tabla con el mismo nombre 
										//y si existe, si queremos importar la tabla con otro nombre o utilizar la existente
										newTable.setDescription(newTable.getDescription());
										String oldTableName = newTable.getDescription();
								/*		if(!useCoincidentTables){
											while (tableOp.existeTabla(newTable
													.getDescription())) {
												if (!changeTableName(newTable))																
													break;															
											}
										}
									*/	
										if (!tableOp
												.existeTabla(newTable.getDescription())) {
											newTable.setDescription(newTable.getDescription());
											logger.info("Creando tabla:"+newTable.getName()+" en la base de datos");
											if (tableOp.crearTablaBD(newTable)) {
												//CREAMOS SECUENCIA
												logger.info("Creando secuencia:"+newTable.getName()+" en la base de datos");
												layerOp.crearSecuencia(newTable.getName());
												
												oldTablesName.put(newTable.getDescription(),oldTableName);
																													
												ColumnRow colRow = null;
												Column colSis = null;
												
												//IMPORTAR COLUMNAS DB
												HashMap newColumnsDB = localGISXmlLayer.getColumnsDB();
												Iterator itNewColumnsDBKeys = newColumnsDB.keySet().iterator();
												while(itNewColumnsDBKeys.hasNext()){																				
													String ColumnsDBKeys = (String)itNewColumnsDBKeys.next();	
													if(ColumnsDBKeys.equals(oldTableName)){	
														HashMap ColumnsDB = (HashMap)newColumnsDB.get(ColumnsDBKeys); 
														Iterator itColumnsDB = ColumnsDB.keySet().iterator();																
														while(itColumnsDB.hasNext()){
															ColumnDB columnDB = (ColumnDB) ColumnsDB.get(itColumnsDB.next());
															if(columnDB.getTableName().equals(oldTableName)){
																columnDB.setTableName(newTable
																		.getDescription());
																
																colSis = new Column(columnDB.getName(),columnDB.getDescription(),null);
																colSis.setDescription(colSis.getName());																			
																//DOMINIOS COLUMNAS																		
																
																colRow = new ColumnRow();	
																colRow.setColumnaSistema(colSis);
																colRow.setColumnaBD(columnDB);
																//CREA LA SECUENCIA
																//NO LA CREAMOS AQUI PORQUE ES POSIBLE QUE LA CREACION
																//DE UNA COLUMNA POR EJEMPLO EL ID DEPENDE DE QUE LA SECUENCIA
																//ESTE CREADA.
																/*if(colSis.getName()!=null && colSis.getName().equals("GEOMETRY")){ 
																	try{
																		logger.info("Creando secuencia:"+newTable.getName()+" en la base de datos");
																		layerOp.crearSecuencia(newTable.getName());																	
																	}
																	catch(Exception e){
																		logger.error("ERROR AL CREAR SEQUENCIA DE " + newTable.getName() + ": " + e.getMessage());
																	}
																}*/
																//logger.info("Creando columna para la tabla:"+newTable.getName()+" Columna:"+colRow.getColumnaBD().getName());
																if(!tableOp.crearColumnaBD(newTable,colRow)){
																	logger.error("ERROR: COLUMNBD " + colRow.getColumnaBD().getName());
																}
															}
														}
													}
												}
												
												Identificadores.put("TablasModificadas", true); 
											}
											else{
												logger.error("ERROR: TABLE");
											}
										}
										else{	
											logger.info("La tabla:"+newTable.getName()+" ya existe en la base de datos");
											Table systemTable = tableOp.obtenerTablaPorNombre(newTable.getDescription());
											if(systemTable != null){
												newTable.setIdTabla(systemTable.getIdTabla());
												newTable.setColumns(systemTable.getColumns());
												newTable.setExternal(systemTable.getExternal());
												newTable.setGeometryType(systemTable.getGeometryType());
											}
										}
									}
								}
							}
						}	
						
						//IMPORTAR DOMINIOS
						ListaDomain newDomains = null;
						if(importDomains){												
							newDomains = localGISXmlLayer.getDomains();
							if(newDomains!=null){											
							
								
								//IMPORTAR DOMINIOS Y NODOS DE DOMINIO
								ListaDomain listaDomain = new ListaDomain();
								ListaDomain listaDomainParticular = new ListaDomain();
								(new OperacionesAdministrador(URL)).getDominios(null,listaDomain,listaDomainParticular);
								Iterator itNewDomains = newDomains.getDom().keySet().iterator();
								while(itNewDomains.hasNext()){														
									com.geopista.protocol.administrador.dominios.Domain newDomain = (com.geopista.protocol.administrador.dominios.Domain) newDomains.get((String)itNewDomains.next());
									Iterator itDomains = listaDomain.getDom().keySet().iterator();
									boolean existsDomain = false;
									while(itDomains.hasNext()){
										com.geopista.protocol.administrador.dominios.Domain domain = (com.geopista.protocol.administrador.dominios.Domain) listaDomain.get((String)itDomains.next());
										if(newDomain.getIdCategory()==null)
											newDomain.setIdCategory("0");
										if(newDomain.getIdCategory().equals(domain.getIdCategory())){
											if(newDomain.getName().equals(domain.getName())){
												if(!newDomain.getIdDomain().equals(domain.getIdDomain())){
													newDomain.setIdDomain(domain.getIdDomain());	
													//CResultadoOperacion resultado=(new OperacionesAdministrador(Constantes.url)).actualizarDomain(newDomain);
												}
												existsDomain = true;
												//Si existe compara los nodos nodos
												//REVISAR
												break;
											}
										}
									}
									//Si no existe se importa como nuevo, incluyendo sus nodos 
									if(!existsDomain){
										CResultadoOperacion resultadoDomain=(new OperacionesAdministrador(URL)).nuevoDomain(newDomain);
										if(resultadoDomain.getResultado()){
											if(!ExportImportUtils.insertDomainNodes(resultadoDomain.getDescripcion(), null, newDomain.getListaNodes()))
												logger.error("ERROR: DOMAIN");
										}
										//Identificadores.put("DominiosInsertados", true);
										
										//Cambiamos el identificador de dominio por el nuevo generado para que luego
										//se utilize en el resto de la imporatcion.
										newDomain.setIdDomain(resultadoDomain.getDescripcion());
									}
								}
							}												
						}
						
						//IMPORTAR CAPA
						String oldLayerName = null;
						LayerTable newLayer = null;
						if (importLayers){
							newLayer = localGISXmlLayer.getLayer();
							if(newLayer != null) {
								
								
								oldLayerName = newLayer.getLayer().getDescription();
								newLayer.setIdLayer(0);		
								newLayer.setAcl(layerOp.getAcl(ExportImportUtils.ACL_IMPORT_LAYER));						
								if (newLayer.getLayer().getDescription() != null
										&& !newLayer.getLayer().getDescription().equals("")) {	
														
									//Comprobamos si existe una capa con el mismo nombre 
									//y si existe, si queremos importar la capa con otro nombre o utilizar la existente
							/*		if(!useCoincidentLayers){
										while (layerOp.existeCapaId(newLayer.getLayer().getDescription())) {
											if (!changeLayerName(newLayer))
												break;
										}
									}
								*/	
									if (!layerOp.existeCapaId(newLayer.getLayer().getDescription())) {	
										if(!newLayer.getLayer().getDescription().equals(oldLayerName))
											ExportImportUtils.addToHashtableTranslation(newLayer.getHtNombre(),"_EXP");
										int idDictionary = layerOp.actualizarDictionary(newLayer.getHtNombre(), 0);
										if (idDictionary > 0)
											newLayer.getLayer().setName(String.valueOf(idDictionary));							
										if (layerOp.actualizarLayer(newLayer, null)==0) {
											logger.error("ERROR: LAYER");
										}
										
										//if (!newLayer.getLayer().getDescription().equals(oldLayerName)){
											//Capa Versionada
											if(newTables!=null && newLayer.getLayer().isVersionable())
												crearVersionImportada(layerOp, newLayer, newTables);
											
											//Capa Dinamica
										/*	if(newLayer.getLayer().isDinamica()){
												String urlMapServer = publicarCapa(layerOp, newLayer);
												if (urlMapServer.equals("")) {
													newLayer.getLayer().setUrl(null);
												} else
													newLayer.getLayer().setUrl(urlMapServer);
											}																																	
										*/
											//IMPORTAR ATRIBUTOS
											ArrayList newAttributes = localGISXmlLayer.getAttributes();
											if(newAttributes!=null){
												
												//RECUPERACION DE TABLAS 
												ArrayList tables = null;
												if(newTables!=null)
													tables = newTables;
												else
													tables = ExportImportUtils.getTablesByAttributes(newAttributes);														
												
												if(tables!=null){
													Iterator itTables = tables.iterator();
													while(itTables.hasNext()){																
														Table newTable = (Table) itTables.next();																
													
														List tableColumns = layerOp.obtenerListaColumnas(newTable);							
													
														Iterator itAttributes = newAttributes.iterator();
														while(itAttributes.hasNext()){
															Attribute newAttribute = (Attribute) itAttributes.next();
															try {
																if (oldTablesName.get(newTable.getName())!=null){
																	if (oldTablesName.get(newTable.getName()).equals(newAttribute.getColumn().getTable().getName())){
																		newAttribute.getColumn().setIdColumn(ExportImportUtils.getIdColumnFromColumnsList(tableColumns,newAttribute.getColumn().getName()));
																		newAttribute.setIdLayer(newLayer.getIdLayer());
																		newAttribute.setSystemID(0);
																		newAttribute.setEditable(newAttribute.isEditable());
																		if(newDomains!=null){
																			Domain domain = newAttribute.getColumn().getDomain();																			
																			if(domain!=null){
																				com.geopista.protocol.administrador.dominios.Domain newDomain = newDomains.getByName(domain.getName());
																				Integer oldSystemId = domain.getSystemID();
																				domain.setSystemID(Integer.parseInt(newDomain.getIdDomain()));
																				Domain domainType = layerOp.obtenerDominioTipo(Integer.parseInt(newDomain.getIdDomain()), newDomain.getName());
																				
																				if (domainType!=null){																
																					// TODO Se ha comentado para que se asocien los dominios a las columnas, si no nunca se asocian, si da un pete
																					// revisar
																		//			if(domainType==null || !oldSystemId.equals(domain.getSystemID())){								
																						newAttribute.getColumn().setDomain(null);
																						if(newAttribute.getColumn().getIdColumn()!=0)																										
																							layerOp.actualizarDominioColumna(newAttribute.getColumn(), domain, newAttribute.getColumn().getLevel());
																		//			}
																				}
																				else{
																					logger.error("El domain type es null");		
																				}
																			}																							
																		}
																		//Si no importa los dominios
																		else newAttribute.getColumn().setDomain(null);																			
																		newAttribute.getColumn().setTable(newTable);																						
																		//idDictionary = layerOp.actualizarDictionary(newAttribute.getHtTraducciones(),newAttribute.getIdAlias());
																		idDictionary = layerOp.actualizarDictionary(newAttribute.getHtTraducciones(),0);
																		if (idDictionary > 0)
																			newAttribute.setIdAlias(idDictionary);								
																		if(layerOp.actualizarAtributo(newAttribute)==-1){
																			logger.error("ERROR: ATTRIBUTES");
																		}
																	}
																}
																else{
																	//Esta condicion es unicamente cuando la tabla ya se ha creado en la Base de Datos
																	//logger.error("No existe traduccion en la oldtable para:"+newTable.getName()+" Continuando");
																}
															} catch (Exception e) {
																logger.error("Error al configurar los atributos de la tabla. Continuando.");
															}
														//}
													}
												}
											}
										}
									
								
										//IMPORTAR QUERIES
										Hashtable newQueries = localGISXmlLayer.getQueries();
										if (newQueries!=null && !newQueries.isEmpty()){	
											
											if(newTables!=null){
												Iterator itTables = newTables.iterator();
												while(itTables.hasNext()){
													Table newTable = (Table) itTables.next();
													//Reemplazamos el nombre de la tabla en las queries si el nombre ha sido cambiado
													if(oldTablesName!=null && oldTablesName.size()>0 && !newTable.getDescription().equals(oldTablesName.get(newTable.getDescription()))){
														Query newQuery = ((Query)newQueries.get(POSTGRE));
														if (oldTablesName.get(newTable.getDescription())!=null){
															newQuery.setSelectQuery(newQuery.getSelectQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
															newQuery.setInsertQuery(newQuery.getInsertQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
															newQuery.setUpdateQuery(newQuery.getUpdateQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
															newQuery.setDeleteQuery(newQuery.getDeleteQuery().replace(oldTablesName.get(newTable.getDescription()), newTable.getDescription()));
														}
														else{
															//Esta condicion es unicamente cuando la tabla ya se ha creado en la Base de Datos
															//logger.error("No existe una descripcion para la tabla "+newTable.getDescription());
														}
															
													}
												}
											}
											if(layerOp.actualizarConsultas(newLayer.getIdLayer(),newQueries)==-1){
												logger.error("ERROR: QUERIES");
											}	
										}
									}	
								}
								
								//IMPORTAR ESTILOS
								String style = localGISXmlLayer.getStyles();
								if (!oldLayerName.equals(newLayer.getLayer().getDescription()))
									style = style.replaceAll(oldLayerName, newLayer.getLayer().getDescription());													
								if(style!=null){
									if(newLayer!=null && newLayer.getIdLayer()==0){
										try{																			
											//newLayer.setIdLayer(Integer.parseInt(newLayer.getLayer().getName()));
											newLayer.setIdLayer(ExportImportUtils.getLayerIdByLayerList(layerOp.obtenerLayerTable(true), newLayer.getLayer().getDescription()));
										}catch(NumberFormatException e){}
									}
									if(layerOp.actualizarEstilos(newLayer.getIdLayer(), style)==-1){
										logger.error("ERROR: ACTUALIZAR ESTILOS");
									}	
								}
	
							}
						}																								
						
						//IMPORTAR FAMILIAS DE CAPA											
						ArrayList newLayerFamilies = null;		
						if(importFamilies){
							newLayerFamilies = localGISXmlLayer.getLayerFamilies();
							if(newLayerFamilies!=null){
							
								String oldLayerFamilyName = null;
								
								Iterator itLayerFamilies = newLayerFamilies.iterator();
								while(itLayerFamilies.hasNext()){
									LayerFamilyTable newLayerFamily = (LayerFamilyTable) itLayerFamilies.next();
									
									oldLayerFamilyName = (String)newLayerFamily.getHtNombre().get("es_ES");
									if (((String)newLayerFamily.getHtNombre().get("es_ES")) != null
											&& !((String)newLayerFamily.getHtNombre().get("es_ES")).equals("")) {	
										
										LayerFamilyTable [] layerFamilyTable = layerFamilyOp.obtenerLayerFamilyTable();	
										
										//Comprobamos si existe una familia de capas con el mismo nombre 
										//y si existe, si queremos importar la familia de capas con otro nombre o utilizar la existente
							/*			if(!useCoincidentFamilies){
											while (ExportImportUtils.existeLayerFamily(layerFamilyTable,newLayerFamily)) {
												if (!changeLayerFamilyName(newLayerFamily))
													break;
											}
										}
								*/		
										if (!ExportImportUtils.existeLayerFamily(layerFamilyTable,newLayerFamily)){	
	//																	if(!newLayerFamily.getHtNombre().get("es_ES").equals(oldLayerFamilyName))
	//																		ExportImportUtils.addToHashtableTranslation(newLayerFamily.getHtNombre(),"_EXP");
											newLayerFamily.setHtDescripcion(newLayerFamily.getHtNombre());	
											if (layerFamilyOp.crearLayerFamily(newLayerFamily)==-1) {
												logger.error("ERROR: LAYER FAMILY");
											}							
										}		
										else
											ExportImportUtils.changeLayerFamilyId(layerFamilyTable,newLayerFamily);	
										
										if(newLayer!=null && newLayer.getIdLayer()==0){
											try{																			
												//newLayer.setIdLayer(Integer.parseInt(newLayer.getLayer().getName()));
												newLayer.setIdLayer(ExportImportUtils.getLayerIdByLayerList(layerOp.obtenerLayerTable(true), newLayer.getLayer().getDescription()));
											}catch(NumberFormatException e){}
										}
									
										//Si no se ha importado una capa no asocia las familias de capas importas a ninguna capa
										if(newLayer!=null && newLayer.getIdLayer()!=0){								
											if(layerFamilyOp.obtenerLayerfamiliesConLayer(newLayer.getIdLayer()).size()==0){
												layerFamilyOp.asociarLayerFamilyLayer(newLayerFamily.getIdLayerFamily(), newLayer.getIdLayer());
											}
										}
										Identificadores.put("CapasModificadas", true);
									}
								}	
							}
						}		
	/*
						// CARGAMOS LOS INSERT DE LA CAPA
						if (exportData){
							ArrayList tablas = null;
							if (newTables == null)
								tablas = localGISXmlLayer.getTables();
							else
								tablas = newTables;
							Iterator itTablas = tablas.iterator();
							String tablaNew = null;
							String tablaOld = null;
							while (itTablas.hasNext()){
								Table tabla = (Table) itTablas.next();
								tablaNew = tabla.getName();
								if(oldTablesName!=null && oldTablesName.size()>0 && !tabla.getDescription().equals(oldTablesName.get(tabla.getDescription())))
									tablaOld = oldTablesName.get(tabla.getDescription());
								else
									tablaOld = tablaNew;
								ExportImportUtils.cargarScriptsInsert(getFileChooserImport().getSelectedFile().getParentFile().toString(), tablaNew, tablaOld, aplicacion.getIdEntidad());
							}
						}
						*/
						layersImported++;
			       	} catch (Throwable ex) {
						logger.error(ex.getMessage());
						logger.error("Exception",ex);				            				
					} 
				   i++;
				   
				}
				if (layersImported == layersToImport){
							if (layersImported!=0){
							   logger.info("Se han importado todas las capas");
							   return true;
							}
							else{
								logger.error("No se ha importado ninguna capa:");
								return false;
							}
							
				}
				else{
					logger.error("Import Incorrecto: layersImported:"+layersImported+" LayersToImport:"+layersToImport);
					  return false;
				}
		}
		finally{
			try {
				if (SecurityManager.logout()){
					logger.info("Logout de usuario");
				   }
				else{
					logger.info("Logout de usuario no realizado");
				   }
			} catch (Exception e) {
			}	
		}
	}
	
	private static XStream getXStreamSerializer() {
		if (xStreamSerializer == null) 
			xStreamSerializer = ExportImportUtils.getNewXStreamExportImport();
		return xStreamSerializer;
	}
	
	private static void crearVersionImportada(LayerOperations layerOp, LayerTable lt, List tables)
			throws DataException {
		try {
			// Obtengo todas las tablas asociadas a la capa
			Iterator itTables = tables.iterator();
			while (itTables.hasNext()) {
				Table table = (Table) itTables.next();		
				if (!table.getName().equals("SYSTEM")) {
					layerOp.crearSecuenciaVersionado(table.getName(), true);
					// Actualizo el campo layers.validator
					if (lt.getLayer().getValidator() == null)
						lt.getLayer().setValidator(VALIDATOR_CLASS);
					else
						lt.getLayer().setValidator(
								lt.getLayer().getValidator() + ";"
										+ VALIDATOR_CLASS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}
	}

	public static File[] getFiles(File dir) {

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.toLowerCase().endsWith(EXT_FICHERO_XML));
			}
		};
		File[] children = dir.listFiles(filter);
		return children;
	}

}
