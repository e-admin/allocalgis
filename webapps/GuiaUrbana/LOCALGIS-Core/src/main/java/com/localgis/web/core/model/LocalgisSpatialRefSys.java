/**
 * LocalgisSpatialRefSys.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class LocalgisSpatialRefSys {

    private Integer srid;

    private String authName;

    private Integer authSrid;

    private String srText;

    private String proj4Text;
    
    public LocalgisSpatialRefSys() {
    }
    
    public LocalgisSpatialRefSys(Integer srid, String authName, Integer authSrid, String srText, String proj4Text) {
        super();
        this.srid = srid;
        this.authName = authName;
        this.authSrid = authSrid;
        this.srText = srText;
        this.proj4Text = proj4Text;
    }

	public Integer getSrid() {
		return srid;
	}

	public void setSrid(Integer srid) {
		this.srid = srid;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public Integer getAuthSrid() {
		return authSrid;
	}

	public void setAuthSrid(Integer authSrid) {
		this.authSrid = authSrid;
	}

	public String getSrText() {
		return srText;
	}

	public void setSrText(String srText) {
		this.srText = srText;
	}

	public String getProj4Text() {
		return proj4Text;
	}

	public void setProj4Text(String proj4Text) {
		this.proj4Text = proj4Text;
	}


}