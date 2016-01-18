/**
 * EIELLoader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.loadEIELData;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import com.geopista.app.loadEIELData.beans.ColumnInfo;
import com.geopista.app.loadEIELData.beans.ConnectionInfo;
import com.geopista.app.loadEIELData.beans.LoadInfo;
import com.geopista.app.loadEIELData.beans.MunicipalityInfo;
import com.geopista.app.loadEIELData.beans.PrimaryKeyInfo;
import com.geopista.app.loadEIELData.beans.PrimaryKeysAndColumns;
import com.geopista.ui.plugin.external.ExternalDataSource;

public class EIELLoader {

	private static ExternalDataSource externalDataSourceOrigen;
	private static ExternalDataSource externalDataSourceDestino;
	private static Properties loadProperties;
	private static Properties connectionProperties;
	private static Properties loadFileProperties;
	private static Connection connOrigen;
	private static Connection connDestino;
	
	private static String rutaCargaProperties = "./config/cargas.properties";
	private static String rutaConexionProperties = "./config/conexiones.properties";
	
	private static boolean INSERTAR_EN_BD=false;
	
	private static boolean PRODUCCION=true;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		boolean fin;
		int relation;		
		Integer erroresTotal =0;
		int[] erroresMunicipio = new int[1];
		erroresMunicipio[0]=0;	
		String loadName = ""; 
		String connectionName = "";
		String shpName="";
		String BBDDName="";
		
		StringBuffer erroresPorCapa=new StringBuffer();
		try {			
			//TEST (DESCOMENTAR LA SIGUIENTE LINEA PARA TESTS)
			//PRODUCCION = false;
			
			if(!PRODUCCION){
				// TEST	
				rutaCargaProperties = "C:\\LocalGIS.CargadorMasivo/config/cargas.properties";	
				rutaConexionProperties = "C:\\LocalGIS.CargadorMasivo/config/conexiones.properties";		
				loadName = "EIEL_SERPA";
				//connectionName = "PRE";
				connectionName = "DEV";
				INSERTAR_EN_BD = true;
			}
			
			loadProperties = Utils.getProperties(rutaCargaProperties);
			connectionProperties = Utils
					.getProperties(rutaConexionProperties);
			if (loadProperties != null && connectionProperties != null) {
				
				if(PRODUCCION){
					// PROD
					if (args.length<2){
						System.out.println("Faltan Argumentos");
						System.exit(1);
					}
					loadName = args[0];
					connectionName = args[1];
					
					if (args.length==3){
						String insertarEnBD=args[2];
						if (insertarEnBD.equals("1"))
							INSERTAR_EN_BD=true;
					}
						
					if (loadName==null){
						System.out.println("Faltan los datos de Conexion");
						System.exit(1);
					}
				}
				
				ConnectionInfo connectionInfo = Utils.getConnectionInfo(connectionProperties,connectionName);				
				
				if(!connectionInfo.getConnectionPath().equals("")){
					externalDataSourceDestino = Utils
					.generateExternalDataSource(Utils.DESTINO,
							connectionInfo.getDriver(), connectionInfo.getConnectionPath(),
							connectionInfo.getUser(), connectionInfo.getPassword());					
					connDestino = Utils
							.getConnection(externalDataSourceDestino);
				}				
				
				String relacionesFijas=(String)loadProperties.get("carga1.relacionesfijas");
				String municipiosFijos=(String)loadProperties.get("carga1.municipiosfijos");
				
				LoadInfo loadInfo = Utils.getLoadInfo(loadProperties,loadName,municipiosFijos,connDestino);
				
				if (loadInfo.getLoadMunicipalities().size()>0 && loadInfo.getLoadFiles().size()>0) {	
					String titleMsg = "--------------------- RESUMEN DE CARGA DE " + loadName + " EN " + connectionName + " ---------------------";
					Utils.print(titleMsg);	
										
					Iterator itLoadMunicipalities = loadInfo.getLoadMunicipalities().iterator();					
					while(itLoadMunicipalities.hasNext()){
						
						
						MunicipalityInfo loadMunicipality = (MunicipalityInfo)itLoadMunicipalities.next();
						String titleMunicMsg = "--------------------- CARGA DE " + loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ")" + " ---------------------";
						Utils.print();
						Utils.print(titleMunicMsg);
						
						Iterator itLoadFiles = loadInfo.getLoadFiles().iterator();	
						while(itLoadFiles.hasNext()){
							
							String loadFileName = (String)itLoadFiles.next();
							
							String loadFilePath = loadInfo.getLoadFilePath() + loadFileName;
							loadFileProperties = Utils.getProperties(loadFilePath);
							
						relation = 1;
						fin = false;
	
						externalDataSourceOrigen = Utils
						.generateExternalDataSource(Utils.ORIGEN,
								loadMunicipality.getOriginDriver(), loadMunicipality.getOriginBBDDConnectionPath(),
								loadMunicipality.getOriginUser(), loadMunicipality.getOriginPassword());
						connOrigen = Utils.getConnection(externalDataSourceOrigen);
	
						String loadMsg = "--- " + loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ") -- ( " + loadFileName + " ) ---------------------";
						Utils.print();
						Utils.print(loadMsg);
						
						while (!fin) {
							Utils.contador=0;
							Utils.contadorBD=0;
							if (Utils.doRelation(loadFileProperties, relation,relacionesFijas) && 
										!Utils.getOriginType(loadFileProperties, relation).equals(Utils.NO_EXISTE)) {
								
								loadMsg = "\t--- " + loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ") -- ( " + Utils.getName(loadFileProperties, relation) + " ) ---------------------";
								Utils.print();
								Utils.print(loadMsg);

							
								int totalSHP = Utils.totalElementsSHP(loadMunicipality.getShpFilesPath(), loadFileProperties, relation);
								int totalBBDD = Utils.totalElementsBBDD(connOrigen, loadFileProperties, relation);
	
								String originType = Utils.getOriginType(loadFileProperties, relation);
								String tableNameEIEL = Utils.getDestinyName(loadFileProperties, relation);
								String finalMessage = "";
								if (originType.equals(Utils.SHP_BBDD)) { //  && totalSHP > 0
									String tableNameSHP;
									String tableNameBBDD;									
									String whereBBDD;
									String municipio;
									String originSRID;
									String destinySRID;
									String relationCharacteristic;
									String nonGeometryField;
	
									Hashtable<String, Hashtable<String, String>> primaryKeysSHPFilters = null;
									Hashtable<String, PrimaryKeyInfo> primaryKeysSHP = null;
									ArrayList<ColumnInfo> columnsSHP = null;
									Hashtable<String, PrimaryKeyInfo> primaryKeysBBDD = null;
									ArrayList<ColumnInfo> columnsBBDD = null;
									ArrayList<ColumnInfo> destinyColumnsEIEL = new ArrayList<ColumnInfo>();
	
									ArrayList elementsSHP = null;
									ArrayList elementsBBDD = null;
									ArrayList elementsEIEL = null;
									ArrayList insert = null;
	
									
											
									tableNameSHP = Utils.getOriginName(
											loadFileProperties, Utils.SHP, relation);
									tableNameBBDD = Utils.getOriginName(
											loadFileProperties, Utils.BBDD, relation);
									whereBBDD = Utils.getBBDDWhere(loadFileProperties,
											relation);
									municipio = Utils.getMunicipio(loadFileProperties,
											relation);
									originSRID = Utils.getSRID(loadFileProperties,
											Utils.ORIGEN, relation, loadMunicipality.getSrid());
									//originSRID="23030";
									destinySRID = Utils.getSRID(loadFileProperties,
											Utils.DESTINO, relation);
									relationCharacteristic = Utils
											.getRelationCharacteristic(loadFileProperties,
													relation);
									nonGeometryField = Utils
											.getNonGeometryField(loadFileProperties,
													relation);
	
									shpName=tableNameSHP;
									BBDDName=tableNameBBDD;

									
									// Obtenemos el conjunto de filtros de las
									// claves
									// primarias de SHP a BBDD
									primaryKeysSHPFilters = Utils
											.getPrimaryKeyFilters(loadFileProperties,Utils.SHP, relation);
	
									// Obtenemos el conjunto de claves primarias de
									// SHP
									// y
									// BBDD
									primaryKeysSHP = Utils.getPrimaryKeysInfo(
											loadFileProperties, Utils.SHP, relation);
									primaryKeysBBDD = Utils
											.getPrimaryKeysInfo(loadFileProperties,
													Utils.BBDD, relation);
	
									// Obtenemos el esquema de las columnas de SHP y
									// BBDD
									columnsSHP = Utils.getColumnsInfo(
											loadFileProperties, Utils.SHP, relation);
									columnsBBDD = Utils.getColumnsInfo(
											loadFileProperties, Utils.BBDD, relation);
	
									// Obtenemos los datos de SHP (Utilizando
									// columnsSHP
									// como patrón)
									elementsSHP = Utils.getElementsSHP(
											loadMunicipality.getShpFilesPath(), tableNameSHP,
											primaryKeysSHP, primaryKeysSHPFilters,
											columnsSHP, loadFileProperties,relation, relationCharacteristic);
	
									if(elementsSHP.size()==0 || Utils.containsColumnWithValue(((PrimaryKeysAndColumns)elementsSHP.get(0)).getColumns(),"\"GEOMETRY\"",""))
										relationCharacteristic=Utils.RIGHT_JOIN;
									
									// Obtenemos los datos de BBDD (Utilizando
									// columnsBBDD
									// como patrón)
									elementsBBDD = Utils.getElementsBBDD(
											connOrigen, tableNameBBDD, whereBBDD,
											primaryKeysBBDD, columnsBBDD, relation);
	
									// Unimos el esquema de las columnas de SHP y
									// BBDD
									destinyColumnsEIEL.addAll(columnsSHP);
									destinyColumnsEIEL.addAll(columnsBBDD);
	
									// Obtenemos los datos de finales de la union de
									// los
									// datos de SHP y BBDD
									elementsEIEL = Utils
											.getElements(
													destinyColumnsEIEL,
													elementsSHP,
													elementsBBDD,
													Utils.generateColumnsBBDDEmpty(columnsBBDD, primaryKeysBBDD),
													Utils.generateColumnsSHPEmpty(columnsSHP, primaryKeysSHP),
													primaryKeysSHP, relation,
													relationCharacteristic,
													nonGeometryField,loadMunicipality,tableNameSHP,Utils.getName(loadFileProperties, relation),loadFileProperties);
	
									// Generamos las consultas de inserción
									if (elementsEIEL.size() > 0)
										insert = Utils.generateInsert(
												tableNameEIEL, elementsEIEL,
												relation, municipio, loadMunicipality.getIne(),
												originSRID, destinySRID);
	
									int total = 0;									
									if (insert != null) {
										
										if (INSERTAR_EN_BD){
											total = Utils.executeQuerys(
													connDestino, insert, erroresMunicipio);
											
										}
										
										try {
											FileWriter fichero = new FileWriter(tableNameSHP+".log");
											PrintWriter pw = new PrintWriter(fichero);
											Iterator it=insert.iterator();
											while (it.hasNext()){
												  pw.println(it.next());	
											}
											pw.close();
											fichero.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
										
										finalMessage = "Relacion: " + relation + " Tabla: " + tableNameEIEL
												+ " - Insertados: " + total
												+ "(SHP: " + totalSHP + " - BBDD: "
												+ totalBBDD + ")";
										
									} 
									else if (relationCharacteristic
											.equals(Utils.INNER_JOIN))
										finalMessage = "Relacion" + relation + " Tabla: " + tableNameEIEL + " - Absoluta sin coincidencias (SHP: " + totalSHP + " - BBDD: "
											+ totalBBDD + ")";					
									else if(totalSHP==0 && totalBBDD==0)
										finalMessage = "Relacion: "
											+ relation + " Tabla: " + tableNameEIEL + " - Sin resultado (SHP: " + totalSHP + " - BBDD: "
											+ totalBBDD + ")";		
								} else if ((originType.equals(Utils.SHP) && totalSHP > 0)) {
									String tableNameSHP;
									String municipio;
									String originSRID;
									String destinySRID;
	
									Hashtable<String, PrimaryKeyInfo> primaryKeysSHP = null;
									ArrayList<ColumnInfo> columnsSHP = null;
									ArrayList<ColumnInfo> destinyColumnsEIEL = new ArrayList<ColumnInfo>();
	
									ArrayList elementsSHP = null;
									ArrayList elementsEIEL = null;
									ArrayList insert = null;
	
									tableNameSHP = Utils.getOriginName(
											loadFileProperties, Utils.SHP, relation);
									municipio = Utils.getMunicipio(loadFileProperties,
											relation);
									originSRID = Utils.getSRID(loadFileProperties,
											Utils.ORIGEN, relation, loadMunicipality.getSrid());
									destinySRID = Utils.getSRID(loadFileProperties,
											Utils.DESTINO, relation);
	
									// Obtenemos el conjunto de claves primarias de
									// SHP
									// y
									// BBDD
									primaryKeysSHP = Utils.getPrimaryKeysInfo(
											loadFileProperties, Utils.SHP, relation);
	
									// Obtenemos el esquema de las columnas de SHP y
									// BBDD
									columnsSHP = Utils.getColumnsInfo(
											loadFileProperties, Utils.SHP, relation);
	
									// Obtenemos los datos de SHP (Utilizando
									// columnsSHP
									// como patrón)
									elementsSHP = Utils.getElementsSHP(
											loadMunicipality.getShpFilesPath(), tableNameSHP, columnsSHP,
											loadFileProperties,relation);
	
									// Unimos el esquema de las columnas de SHP y
									// BBDD
									destinyColumnsEIEL.addAll(columnsSHP);
	
									// Obtenemos los datos de finales de la union de
									// los
									// datos de SHP y BBDD
									elementsEIEL = Utils.getElements(
											destinyColumnsEIEL, elementsSHP,
											relation);
	
									// Generamos las consultas de inserción
									if (elementsEIEL.size() > 0)
										insert = Utils.generateInsert(
												tableNameEIEL, elementsEIEL,
												relation, municipio, loadMunicipality.getIne(),
												originSRID, destinySRID);
	
									// Si se produce algún fallo salimos sin
									// terminar la
									// carga:
									// Ejecutamos las cadenas de inserción
									// System.out.println(insert);
									int total=0;
									if (INSERTAR_EN_BD){
										total = Utils.executeQuerys(
												connDestino, insert, erroresMunicipio);
										// if(total==0) fin = true;
									}
									
									finalMessage = "Relacion: " + relation + " Tabla: " + tableNameEIEL
									+ " - Insertados: " + total + "(SHP: "
									+ totalSHP + ")";
									
								} else if (originType.equals(Utils.BBDD)
										&& totalBBDD > 0) {
									String tableNameBBDD;
									String whereBBDD;
									String municipio;
	
									ArrayList<ColumnInfo> columnsBBDD = null;
									ArrayList<ColumnInfo> destinyColumnsEIEL = new ArrayList<ColumnInfo>();
	
									ArrayList elementsBBDD = null;
									ArrayList elementsEIEL = null;
									ArrayList insert = null;
	
									tableNameBBDD = Utils.getOriginName(
											loadFileProperties, Utils.BBDD, relation);
									whereBBDD = Utils.getBBDDWhere(loadFileProperties,
											relation);
									municipio = Utils.getMunicipio(loadFileProperties,
											relation);
	
									// Obtenemos el esquema de las columnas de BBDD
									columnsBBDD = Utils.getColumnsInfo(
											loadFileProperties, Utils.BBDD, relation);
	
									// Obtenemos los datos de BBDD (Utilizando
									// columnsBBDD
									// como patrón)
									elementsBBDD = Utils.getElementsBBDD(
											connOrigen, tableNameBBDD, whereBBDD,
											columnsBBDD, relation);
	
									// Unimos el esquema de las columnas de BBDD
									destinyColumnsEIEL.addAll(columnsBBDD);
	
									// Obtenemos los datos de finales de la union de
									// los
									// datos de BBDD
									elementsEIEL = Utils.getElements(
											destinyColumnsEIEL, elementsBBDD,
											relation); // REVISAR
	
									// Generamos las consultas de inserción
									if (elementsEIEL.size() > 0)
										insert = Utils.generateInsert(
												tableNameEIEL, elementsEIEL,
												relation, municipio, loadMunicipality.getIne()); // REVISAR
								
									int total = 0;
									if(insert!=null)
										if (INSERTAR_EN_BD)
											total = Utils.executeQuerys(
												connDestino, insert, erroresMunicipio);
										
										finalMessage = "Relacion: " + relation + " Tabla: " + tableNameEIEL
										+ " - Insertados: " + total + "(BBDD: "
										+ totalBBDD + ")";
										
	
								} else if (originType.equals(Utils.SQL_QUERY)) {
									String municipio = Utils.getMunicipio(
											loadFileProperties, relation);
	
									ArrayList sqlQuerys = Utils.getSQLQuerys(
											loadFileProperties, relation, loadMunicipality,
											municipio, loadMunicipality.getSrid());
	
									int total=0;
									if (INSERTAR_EN_BD){
										total = Utils.executeQuerys(
												connDestino, sqlQuerys, erroresMunicipio);
																		
									}
									finalMessage = "Consulta Ejecutada: "
											+ total + "(" + sqlQuerys + ")";	
								} else{
									finalMessage = "Relacion: "
										+ relation + " Tabla: " + tableNameEIEL + " - Sin resultado (SHP: " + totalSHP + " - BBDD: "
										+ totalBBDD + ")";									
								}
								Utils.print(finalMessage);
								Utils.print();
							}
							relation++;
							if (Utils.getRelation(loadFileProperties, relation) == null)
								fin = true;	
							
							if ((Utils.contador>0) || (Utils.contadorBD>0))
								erroresPorCapa.append("\t-"+shpName+"_"+BBDDName+"->Errores:"+Utils.contador+" ErroresBD:"+Utils.contadorBD+"\n");
							}
						}	
						Utils.print("-----------------------------------------------------------------");
						
						
						
						String finalMsg = "";
						if(erroresMunicipio[0]>0)
							finalMsg = "CARGA DE " + loadMunicipality.getName().toUpperCase() + " EN " + connectionName + " CON ERRORES (Relaciones con errores: " + erroresMunicipio[0] + ")";
						else
							finalMsg = "CARGA DE " + loadMunicipality.getName().toUpperCase() + " EN " + connectionName + " CORRECTA";
						Utils.print(finalMsg);
						Utils.print(erroresPorCapa.toString());
						erroresPorCapa.delete(0,erroresPorCapa.length());
						
						//Utils.loggerMatchingSpecial.println(Utils.infoMatchSpecial);
						//Utils.infoMatchSpecial.delete(0,Utils.infoMatchSpecial.length());
	
						
						erroresTotal += erroresMunicipio[0];
						erroresMunicipio[0] = 0;
						Utils.print("-----------------------------------------------------------------");
					}
					String finalMsg = "";
					if(erroresTotal>0)
						finalMsg = "CARGA CON ERRORES (Relaciones con errores: " + erroresTotal + ")";
					else
						finalMsg = "CARGA CORRECTA";
					Utils.print(finalMsg);
					Utils.print("ELEMENTOS NO CASADOS EN TOTAL:"+Utils.contadorTotal);
					Utils.print("-----------------------------------------------------------------");
					try {
						if (connOrigen!=null)
						connOrigen.close();
						if (connDestino!=null)
						connDestino.close();
					} catch (SQLException e) {
						Utils.printLog(e.getMessage());
					}
				}
			}
			else{
				System.out.println("El properties es vacio");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Utils.printLog(e.getMessage());
		} finally {
			Utils.closeLogFile();
		}
	}

}
