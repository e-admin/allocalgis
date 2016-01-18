/**
 * QueryListItem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
