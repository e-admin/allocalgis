package com.geopista.ui.plugin.external;

public class QueryListItem {

	private boolean newQuery;
	
	private boolean modifyQuery;
	
	private Query query;

	public boolean isNewQuery() {
		return newQuery;
	}

	public void setNewQuery(boolean newQuery) {
		this.newQuery = newQuery;
	}

	public boolean isModifyQuery() {
		return modifyQuery;
	}

	public void setModifyQuery(boolean modifyQuery) {
		this.modifyQuery = modifyQuery;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
	
	public QueryListItem(Query query,boolean newQuery,boolean modifyQuery) {
		this.query = query;
		this.newQuery = newQuery;
		this.modifyQuery = modifyQuery;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return query.getName();
	}
	
}
