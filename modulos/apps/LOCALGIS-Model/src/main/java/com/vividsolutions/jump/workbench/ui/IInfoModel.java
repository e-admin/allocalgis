/**
 * IInfoModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jump.workbench.model.ILayer;

public interface IInfoModel {

	/**
	 * Releases references to the data, to facilitate garbage collection.
	 * Important for MDI apps like the JCS Workbench.
	 */
	public abstract void dispose();

	public abstract Collection getLayerTableModels();

	public abstract void add(ILayer layer, Collection features);

	public abstract void remove(ILayer layer);

	public abstract void clear();

	public abstract ILayerTableModel getTableModel(ILayer layer);

	public abstract List getLayers();

	public abstract void addListener(InfoModelListener listener);

}