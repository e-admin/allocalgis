package com.geopista.app.layerutil.layer.exportimport.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.protocol.administrador.dominios.ListaDomain;

public class LocalGISXmlLayer {
	
	private ArrayList Tables;
	private HashMap ColumnsDB;	
	private HashMap Constraints;	
	private ListaDomain Domains;
	private LayerTable Layer;
	private ArrayList Attributes;
	private Hashtable Queries;
	private ArrayList LayerFamilies;
	private String Styles;
	
	public String getStyles() {
		return Styles;
	}
	
	public void setStyles(String style) {
		Styles = style;
	}	
	
	public ArrayList getTables() {
		return Tables;
	}

	public void setTables(ArrayList tables) {
		Tables = tables;
	}				
	
	public HashMap getColumnsDB() {
		return ColumnsDB;
	}
	
	public void setColumnsDB(HashMap columnsDB) {
		ColumnsDB = columnsDB;
	}
	
	public HashMap getConstraints() {
		return Constraints;
	}

	public void setConstraints(HashMap constraints) {
		Constraints = constraints;
	}

	public ListaDomain getDomains() {
		return Domains;
	}

	public void setDomains(ListaDomain domains) {
		Domains = domains;
	}
	
	public LayerTable getLayer() {
		return Layer;
	}

	public void setLayer(LayerTable layer) {
		Layer = layer;
	}
	
	public ArrayList getAttributes() {
		return Attributes;
	}

	public void setAttributes(ArrayList attributes) {
		Attributes = attributes;
	}

	public Hashtable getQueries() {
		return Queries;
	}

	public void setQueries(Hashtable queries) {
		Queries = queries;
	}

	public ArrayList getLayerFamilies() {
		return LayerFamilies;
	}

	public void setLayerFamilies(ArrayList layerFamilies) {
		LayerFamilies = layerFamilies;
	}
		
}
