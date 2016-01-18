/**
 * IACLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.util.Hashtable;

import com.geopista.feature.GeopistaSchema;

public interface IACLayer {

	public static final int TYPE_GEOMETRY = 1;
	public static final int TYPE_NUMERIC = 2;
	public static final int TYPE_STRING = 3;
	public static final int TYPE_DATE = 4;
	public static final int TYPE_BOOLEAN = 5;

	
	public abstract int getPositionOnMap();

	public abstract void setPositionOnMap(int positionOnMap);

	public abstract boolean isEditable();

	public abstract void setEditable(boolean editable);

	public abstract boolean isVisible();

	public abstract void setVisible(boolean visible);

	public abstract boolean isActive();

	public abstract void setActive(boolean active);

	public abstract boolean isStyleLocal();

	public abstract void setStyleLocal(boolean isLocal);

	public abstract String getStyleId();

	public abstract void setStyleId(String styleId);

	public abstract boolean isDinamica();

	public abstract void setDinamica(boolean dinamica);

	public abstract String getSelectQuery();

	public abstract void setSelectQuery(String selectQuery);

	public abstract long getACL();

	public abstract void setACL(long lACL);

	public abstract String getSystemName();

	public abstract void setSystemName(String systemName);

	public abstract String getStyleXML();

	public abstract void setStyleXML(String styleXML);

	public abstract String getStyleName();

	public abstract void setStyleName(String styleName);

	public abstract String getGeometryAttribute();

	public abstract void setGeometryAttribute(String geometryAttribute);

	public abstract int getId_layer();

	public abstract void setId_layer(int id_layer);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract Hashtable getAttributes();

	public abstract void setAttributes(Hashtable attributes);


	public abstract Object getAttribute(String sName);

	public abstract String getExtendedForm();

	public abstract void setExtendedForm(String extendedForm);

	//public abstract IGeopistaLayer convert(ILayerManager layerManager);

	//public abstract void applyStyle(IGeopistaLayer gpLayer,ILayerManager layerManager, String sXML);

	public abstract GeopistaSchema buildSchema(String sLocale);

	public abstract String findPrimaryTable();


	public abstract long getRevisionActual();

	public abstract void setRevisionActual(long revisionActual);

	public abstract String getUpdateQuery();

	public abstract void setUpdateQuery(String updateQuery);

	public abstract String getInsertQuery();

	public abstract void setInsertQuery(String insertQuery);

	public abstract boolean isVersionable();

	public abstract void setVersionable(boolean isVersionable);

	public abstract long getUltimaRevision();

	public abstract void setUltimaRevision(long ultimaRevision);

	public abstract int getIdUsuario();

	public abstract void setIdUsuario(int idUsuario);

	public abstract void setDeleteQuery(String deleteQuery);

	public abstract String getDeleteQuery();

	public abstract ACAttribute findID();

	public abstract void addAttribute(ACAttribute attribute);

}