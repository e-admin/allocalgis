/**
 * ACTable.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;

import com.geopista.feature.Table;

public class ACTable implements Serializable, IACTable{
    int id_table;
    String name;
    int geometryType=-1;

    public ACTable(){
    }

    public ACTable(int id, String sName, int iGeometryType){
        this.name=sName;
        this.id_table=id;
        this.geometryType=iGeometryType;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#getId()
	 */
    @Override
	public int getId() {
        return id_table;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#setId(int)
	 */
    @Override
	public void setId(int id_table) {
        this.id_table = id_table;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#getName()
	 */
    @Override
	public String getName() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#setName(java.lang.String)
	 */
    @Override
	public void setName(String sName) {
        this.name = sName;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#getGeometryType()
	 */
    @Override
	public int getGeometryType() {
        return geometryType;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#setGeometryType(int)
	 */
    @Override
	public void setGeometryType(int geometryType) {
        this.geometryType = geometryType;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#convert()
	 */
    @Override
	public Table convert(){
        Table tRet=new Table(this.name,this.name);
        if (this.geometryType!=-1)
            tRet.setGeometryType(this.geometryType);
        return tRet;
    }
}
