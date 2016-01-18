/**
 * IWMSLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public interface IWMSLayer {

    public Integer getId();
    public void setId(Integer id);
	public String getFormat();
	public void setFormat(String format);

	public String getServerURL();

	public void setServerURL(String serverURL);
    public String getWmsVersion();
    public void setWmsVersion(String wmsVersion);
	public List getLayerNames();
	
	public void setSRS(String srs);

	public String getSRS();
    public boolean isVisible();
    public String getName();
	public HashMap getSelectedStyles();





}
