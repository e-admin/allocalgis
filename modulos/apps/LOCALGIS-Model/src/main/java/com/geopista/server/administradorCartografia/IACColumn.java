/**
 * IACColumn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.Table;

public interface IACColumn {

	public abstract int getLength();

	public abstract void setLength(int length);

	public abstract int getPrecision();

	public abstract void setPrecision(int precision);

	public abstract int getScale();

	public abstract void setScale(int scale);

	public abstract int getType();

	public abstract void setType(int type);

	public abstract int getId();

	public abstract void setId(int id);

	public abstract String getName();

	public abstract void setName(String name);


	public abstract int getDomainLevel();

	public abstract void setDomainLevel(int domainLevel);


	public abstract Column convert(Table t, Domain d);
	public abstract IACTable getTable();
	public abstract IACDomain getDomain();

}