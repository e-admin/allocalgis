/**
 * ACColumn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;

import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.Table;
import com.geopista.feature.TreeDomain;

public class ACColumn implements Serializable, IACColumn{
    int id;
    String name;
    ACTable table;
    ACDomain domain;
    int domainLevel;
    int length;
    int precision;
    int scale;
    int type;

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getLength()
	 */
    @Override
	public int getLength() {
        return length;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setLength(int)
	 */
    @Override
	public void setLength(int length) {
        this.length = length;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getPrecision()
	 */
    @Override
	public int getPrecision() {
        return precision;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setPrecision(int)
	 */
    @Override
	public void setPrecision(int precision) {
        this.precision = precision;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getScale()
	 */
    @Override
	public int getScale() {
        return scale;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setScale(int)
	 */
    @Override
	public void setScale(int scale) {
        this.scale = scale;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getType()
	 */
    @Override
	public int getType() {
        return type;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setType(int)
	 */
    @Override
	public void setType(int type) {
        this.type = type;
    }

    public ACColumn(){
    }

    public ACColumn(int id,String name,ACTable table,ACDomain domain, int domainLevel,
                    int length,int precision,int scale, int type){
        this.id=id;
        this.name=name;
        this.table=table;
        this.domain=domain;
        this.domainLevel=domainLevel;
        this.length=length;
        this.precision=precision;
        this.scale=scale;
        this.type=type;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getId()
	 */
    @Override
	public int getId() {
        return id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setId(int)
	 */
    @Override
	public void setId(int id) {
        this.id = id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getName()
	 */
    @Override
	public String getName() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setName(java.lang.String)
	 */
    @Override
	public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getTable()
	 */
	public IACTable getTable() {
        return table;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setTable(com.geopista.server.administradorCartografia.ACTable)
	 */
	public void setTable(ACTable table) {
        this.table = table;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getDomain()
	 */
	public IACDomain getDomain() {
        return domain;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#getDomainLevel()
	 */
    @Override
	public int getDomainLevel() {
        return domainLevel;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setDomainLevel(int)
	 */
    @Override
	public void setDomainLevel(int domainLevel) {
        this.domainLevel = domainLevel;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#setDomain(com.geopista.server.administradorCartografia.ACDomain)
	 */
	public void setDomain(ACDomain domain) {
        this.domain = domain;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACColumn#convert(com.geopista.feature.Table, com.geopista.feature.Domain)
	 */
    @Override
	public Column convert(Table t, Domain d){
        Column cRet=new Column(this.name, this.name,t,d);
        if ((d instanceof TreeDomain) && !(d instanceof CodeBookDomain))
            ((TreeDomain)d).attachColumnToLevel(cRet,this.domainLevel);
        return cRet;
    }
}
