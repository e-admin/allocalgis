package com.geopista.app.layerutil.layer.exportimport.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.app.layerutil.layer.Query;
import com.geopista.app.layerutil.layer.exportimport.beans.LocalGISXmlLayer;
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

	public static final String IMPORT_SCHEMA_XSD = "/com/geopista/app/layerutil/layer/exportimport/resources/importSchema.xsd";
	public static final String ACL_IMPORT_LAYER = "Capas Importadas";
	
	public static XStream getNewXStreamSerializer(){
		XStream xStreamSerializer = new XStream(new DomDriver());			
		
//		xStreamSerializer.autodetectAnnotations(true);
//		xStreamSerializer.setMode(XStream.NO_REFERENCES);
		
		xStreamSerializer.registerConverter(new MapConverter(xStreamSerializer.getMapper()));
		
		xStreamSerializer.alias("LocalGISXmlLayer", LocalGISXmlLayer.class);
		xStreamSerializer.alias("Tables", List.class);
		xStreamSerializer.alias("Queries", Map.class);
		xStreamSerializer.alias("Layer", LayerTable.class);
		xStreamSerializer.alias("Attributes", List.class);
		xStreamSerializer.alias("ColumnsDB", Map.class);
		xStreamSerializer.alias("Domains", ListaDomain.class);
		xStreamSerializer.alias("DomainCategories", ListaCategories.class);
		xStreamSerializer.alias("LayerFamilies", List.class);
		xStreamSerializer.alias("Styles", String.class);

		xStreamSerializer.alias("table", Table.class);
		xStreamSerializer.alias("layer", Layer.class);
		xStreamSerializer.alias("columns", Map.class);
		xStreamSerializer.alias("columnDB", ColumnDB.class);
		xStreamSerializer.alias("column", Column.class);
		xStreamSerializer.alias("category", Category.class);
		xStreamSerializer.alias("domain", Domain.class);
		xStreamSerializer.alias("domainNode", DomainNode.class);
		xStreamSerializer.alias("attribute", Attribute.class);
		xStreamSerializer.alias("htTraducciones", Map.class);		
		xStreamSerializer.alias("htNombre", Map.class);
		xStreamSerializer.alias("htDescripcion", Map.class);	
		xStreamSerializer.alias("query", Query.class);
		xStreamSerializer.alias("layerFamilyTable", LayerFamilyTable.class);
		xStreamSerializer.alias("style", String.class);
		
		xStreamSerializer.alias("entry", Map.Entry.class);

		return xStreamSerializer;
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
				if(ExportImportUtils.getTableFromList(newTables,attribute.getColumn().getTable().getName())==null){
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
		
}
