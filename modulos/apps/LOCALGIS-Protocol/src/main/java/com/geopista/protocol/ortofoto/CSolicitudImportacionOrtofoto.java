/**
 * CSolicitudImportacionOrtofoto.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.ortofoto;

public class CSolicitudImportacionOrtofoto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
    private String extension;
    private String epsg;
	private String imageName;
	private String worldfileName;
	private boolean worldfileAttached;
	
	public boolean isWorldfileAttached() {
		
		return worldfileAttached;
	}

	public void setWorldfileAttached(boolean worldfileAttached) {
		
		this.worldfileAttached = worldfileAttached;
	}

	public CSolicitudImportacionOrtofoto() {
		
	}
	
	public String getImageName() {
		
		return imageName;
	}

	public String getWorldfileName() {
		
		return worldfileName;
	}

	public void setImageName(String imageName) {
		
		this.imageName = imageName;
	}

	public void setWorldfileName(String worldfileName) {
		
		this.worldfileName = worldfileName;
	}

	public CSolicitudImportacionOrtofoto(String epsg) {

		this.epsg = epsg;
	}
	
	public String getEpsg() {
		
		return epsg;
	}
	
	public void setEpsg(String epsg) {
		
		this.epsg = epsg;
	}

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
	
}
