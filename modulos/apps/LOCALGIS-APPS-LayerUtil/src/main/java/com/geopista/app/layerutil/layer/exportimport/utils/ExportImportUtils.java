/**
 * ExportImportUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.layer.exportimport.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.app.layerutil.layer.Query;
import com.geopista.app.layerutil.layer.exportimport.beans.InitialExport;
import com.geopista.app.layerutil.layer.exportimport.beans.LocalGISXmlLayer;
import com.geopista.app.layerutil.layer.exportimport.beans.PostgreSQLConstraint;
import com.geopista.app.layerutil.layerfamily.LayerFamilyTable;
import com.geopista.app.layerutil.schema.table.TableRow;
import com.geopista.feature.Attribute;
import com.geopista.feature.Column;
import com.geopista.feature.Table;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.dominios.Category;
import com.geopista.protocol.administrador.dominios.Domain;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.administrador.dominios.ListaCategories;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.geopista.protocol.administrador.dominios.ListaDomainNode;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vividsolutions.jump.workbench.model.Layer;

public class ExportImportUtils {

	//public static final String IMPORT_SCHEMA_XSD = "/com/geopista/app/layerutil/layer/exportimport/resources/importSchema.xsd";
	public static final String ACL_IMPORT_LAYER = "Capas Importadas";
	
	private static Logger logger = Logger.getLogger(ExportImportUtils.class);
	
	public static XStream getNewXStreamExportImport(){
		XStream xStream = new XStream(new DomDriver("ISO-8859-1"));			
		xStream.registerConverter(new MapConverter(xStream.getMapper()));
		
		xStream.alias("LocalGISXmlLayer", LocalGISXmlLayer.class);
		xStream.alias("Tables", List.class);
		xStream.alias("Queries", Map.class);
		xStream.alias("Layer", LayerTable.class);
		xStream.alias("Attributes", List.class);
		xStream.alias("ColumnsDB", Map.class);
		xStream.alias("Constraints", Map.class);
		xStream.alias("Domains", ListaDomain.class);
		xStream.alias("DomainCategories", ListaCategories.class);
		xStream.alias("LayerFamilies", List.class);
		xStream.alias("Styles", String.class);

		xStream.alias("table", Table.class);
		xStream.alias("layer", Layer.class);
		xStream.alias("columns", Map.class);
		xStream.alias("columnDB", ColumnDB.class);
		xStream.alias("column", Column.class);
		xStream.alias("category", Category.class);
		xStream.alias("postgreSQLConstraints", PostgreSQLConstraint.class);
		xStream.alias("domain", Domain.class);
		xStream.alias("domainNode", DomainNode.class);
		xStream.alias("attribute", Attribute.class);
		xStream.alias("htTraducciones", Map.class);		
		xStream.alias("htNombre", Map.class);
		xStream.alias("htDescripcion", Map.class);	
		xStream.alias("query", Query.class);
		xStream.alias("layerFamilyTable", LayerFamilyTable.class);
		xStream.alias("style", String.class);
		
		xStream.alias("entry", Map.Entry.class);
		
		
		//El Layer manager lo eliminamos para que no lo serializa/deserialize
		logger.info("Omitiendo campos para filtrar");
		xStream.omitField(com.vividsolutions.jump.workbench.model.AbstractLayerable.class, "layerManager");
		xStream.omitField(com.vividsolutions.jump.workbench.model.Layer.class, "layerListener");

		return xStream;
	}
	

	public static XStream getNewXStreamInitialExport(){
		XStream xStream = new XStream(new DomDriver("ISO-8859-1"));
		
		xStream.alias("InitialExport", InitialExport.class);
		xStream.alias("Tables", List.class);
		xStream.alias("Table", String.class);
		xStream.alias("Domains", List.class);
		xStream.alias("Domain", String.class);
		xStream.alias("Layers", List.class);
		xStream.alias("Layer", String.class);
		
		return xStream;
	}
	
	public static Table getTableFromList(List tablesList, String tableName){	
		if(tableName!=null){
			Iterator it = tablesList.iterator();
			while(it.hasNext()){			
				 Table table = (Table) it.next();				 
				 if(table!=null && table.getName()!=null && table.getName().equals(tableName))
					 return table;
			}		
		}
		return null;
	}	
	
	public static ArrayList getLayerAttibutes(TableRow[] attributes){
		if(attributes.length>0){
			ArrayList newAttributes = new ArrayList();
			for(int i = 0; i < attributes.length; i++) {
				Attribute att = (Attribute) attributes[i].getAtributo();
				att.setEditable(((Boolean) attributes[i].getEditable()).booleanValue());
				att.setColumn((Column) attributes[i].getColumna());
				att.setPosition(i + 1);		
				newAttributes.add(att);
			}	
			return newAttributes;
		}
		return null;
	}
	
	public static ArrayList getTablesByAttributes(Collection attributes) throws DataException{		
		TablesDBOperations tableOp = new TablesDBOperations();

		ArrayList newTables = null;
		List tablasBD = tableOp.obtenerListaTablasBD();
		if(attributes!=null && attributes.size()>0){
			Table newTable = null;
			newTables = new ArrayList();
			Iterator itAttributes = attributes.iterator();
			while(itAttributes.hasNext()){
				Attribute attribute = (Attribute) itAttributes.next();		
				if(attribute.getColumn().getTable()!=null && ExportImportUtils.getTableFromList(newTables,attribute.getColumn().getTable().getName())==null){
					newTable = ExportImportUtils.getTableFromList(tablasBD, attribute.getColumn().getTable().getName());
					if(newTable!=null)
						newTables.add(newTable);
				}
			}
		}	
		return newTables;
	}	
	
	public static boolean insertDomainNodes(String idDomain, String idParent, ListaDomainNode newDomainNodes) throws Exception{
		CResultadoOperacion resultadoDomainNode = null;
		LayerOperations layerOp = new LayerOperations();
		Iterator itNewDomainNodes = newDomainNodes.gethDom().keySet().iterator();
		while(itNewDomainNodes.hasNext()){
			DomainNode newDomainNode = (DomainNode) newDomainNodes.gethDom().get(itNewDomainNodes.next());
			int idDictionary = layerOp.actualizarDictionary(newDomainNode.gethDict(),0);	
			newDomainNode.setIdDes(String.valueOf(idDictionary));
			newDomainNode.setIdDomain(idDomain);
			newDomainNode.setIdParent(idParent);
			resultadoDomainNode=(new OperacionesAdministrador(Constantes.url)).nuevoDomainNode(newDomainNode);
			if(resultadoDomainNode.getResultado()){
				newDomainNode.setIdNode(resultadoDomainNode.getDescripcion());					
				if(newDomainNode.getlHijos()!=null && newDomainNode.getlHijos().gethDom().size()>0)			
					insertDomainNodes(idDomain, newDomainNode.getIdNode(), newDomainNode.getlHijos());
			}
			else return false;
		}
		return true;
	}
	
	public static boolean existeLayerFamily(LayerFamilyTable [] layerFamilyTable, LayerFamilyTable layerFamily){
		if(layerFamilyTable.length>0){
			for(int i=0;i<layerFamilyTable.length;i++){
				if(layerFamilyTable[i].getHtNombre().get("es_ES").equals(layerFamily.getHtNombre().get("es_ES")))
					return true;				
			}			
		}		
		return false;
	}	
	
	public static void changeLayerFamilyId(LayerFamilyTable [] layerFamilyTable, LayerFamilyTable layerFamily){
		if(layerFamilyTable.length>0){
			for(int i=0;i<layerFamilyTable.length;i++){
				if(layerFamilyTable[i].getHtNombre().get("es_ES").equals(layerFamily.getHtNombre().get("es_ES"))){
					layerFamily.getLayerFamily().setSystemId(layerFamilyTable[i].getLayerFamily().getSystemId());
					return;
				}
			}			
		}		
		layerFamily.getLayerFamily().setSystemId("0");
	}	
	
	public static int getIdColumnFromColumnsList(List tableColumns, String columnName){
		Iterator it = tableColumns.iterator();
		while(it.hasNext()){
			Column column = (Column) it.next();
			if(column.getName().equals(columnName))
				return column.getIdColumn();
		}		
		return 0;
	}
	
	public static int getLayerIdByLayerList(LayerTable [] layers, String layerName){
		for(int i=0;i<layers.length;i++){		
			LayerTable layerTable = layers[i];
			if(layerTable.getLayer().getDescription().equals(layerName))
				return layerTable.getIdLayer();				
		}
		return 0;
	}
	
	public static void translateHashtable(Hashtable tHashtable, String tText){
		Enumeration keys = tHashtable.keys();		
		while(keys.hasMoreElements())
			tHashtable.put(keys.nextElement(),tText);
	}
	
	public static void addToHashtableTranslation(Hashtable tHashtable, String tText){
		Enumeration keys = tHashtable.keys();		
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			tHashtable.put(key,tHashtable.get(key) + tText);
		}
	}

	public static void crearScriptsInsert(String directorio, ArrayList tablas, int idEntidad){
		FileOutputStream salidaInsert = null;
		File fileInsert = null;
		if (tablas!=null){
			Iterator itTablas = tablas.iterator();
			while (itTablas.hasNext()){
				Table tabla = (Table) itTablas.next();
				fileInsert = new File(directorio, "insert_"+tabla.getName()+".sql");
				try {
					fileInsert.createNewFile();
					salidaInsert = new FileOutputStream(fileInsert, false);
					TablesDBOperations tableOp = new TablesDBOperations();
					tableOp.crearScriptInsertExportImport(salidaInsert, tabla.getName(), idEntidad);
					salidaInsert.flush(); 
					salidaInsert.close(); 
					salidaInsert=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(Exception e){}
			}
		}
	}
	
	public static void cargarScriptsInsert(String directorio, String tablaNew, String tablaOld, int idEntidad){
		if (tablaOld!=null){
			File fileSQL = new File(directorio+File.separator+"insert_"+tablaOld+".sql");
			if (fileSQL.exists()){
				logger.info("Cargando: insert_"+tablaOld+".sql");
				try {
					FileInputStream fs = new FileInputStream(directorio+File.separator+"insert_"+tablaOld+".sql");
					InputStreamReader in = new InputStreamReader(fs,"ISO-8859-1");
					BufferedReader br = new BufferedReader(in);							
					String textinLine;
					TablesDBOperations tableOp = new TablesDBOperations();

					while (true) {
						textinLine = br.readLine();						
						if (textinLine == null)
							break;
						if (!tablaOld.equals(tablaNew))
							textinLine = textinLine.replaceFirst(tablaOld, tablaNew);
						
						//Para procesar de una forma distinta estos elementos, lo que hacemos
						//es marca en el SQL al principio una etiqueta que permita que luego se lea de forma distinta
						// Ver la explicacion de porque se hace eso en el CServletDB
						
						textinLine= "##"+textinLine;
						//logger.info("Cargando: "+textinLine);
						try {
							tableOp.cargarSQL(textinLine);
						} catch (Exception e) {
							logger.error("Error al cargar una query:"+textinLine);
						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Cargando: insert_"+tablaOld+".zip");
			}
			else
				System.out.println("Fichero no encontrado");
		}
	}
}
