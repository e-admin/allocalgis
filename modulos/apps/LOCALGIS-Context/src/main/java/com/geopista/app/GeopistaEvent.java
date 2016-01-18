/**
 * GeopistaEvent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app;

import java.util.EventObject;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class GeopistaEvent extends EventObject
{

	/**
	 * @param source
	 */
	public GeopistaEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param context
	 * @param desconnected2
	 */
	public GeopistaEvent(AppContext source, int type) {
		
		super(source);
		this.type=type;
	}
	private String message;
	private int type;
	
	 static final public int DESCONNECTED=1;
	 static final public int RECONNECTED=2;
	 static final public int LOGGED_OUT=3;
	 static final public int LOGGED_IN=4;
	
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
}
