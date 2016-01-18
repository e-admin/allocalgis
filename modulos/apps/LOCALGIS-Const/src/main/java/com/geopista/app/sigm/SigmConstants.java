/**
 * SigmConstants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.sigm;

public class SigmConstants {
	
	/**
	 * Variables
	 */
	public static String SIGM_URL;
		
	/**
	 * Constantes (Acciones del servlet)
	 */
	public static final int ACTION_GET_INFO_ALL = 0;
	public static final int ACTION_GET_SEARCH_ALL = 1;
	public static final int ACTION_GET_INFO_BY_PRIMARY_KEY = 2;
	public static final int ACTION_GET_PROPERTY_AND_NAME_KEY = 3;
	public static final int ACTION_GET_ALL_PROCEDURES = 4;
	public static final int ACTION_INSERT_PROCEDURE = 5;
	public static final int ACTION_UPDATE_PROCEDURE = 6;
	public static final int ACTION_DELETE_PROCEDURE = 7;
	public static final int ACTION_GET_PROCEDURE_DEFAULTS = 8;
	public static final int ACTION_INSERT_PROCEDURE_DEFAULTS = 9;
	public static final int ACTION_UPDATE_PROCEDURE_DEFAULTS = 10;
	public static final int ACTION_DELETE_PROCEDURE_DEFAULTS = 11;
	public static final int ACTION_GET_PROCEDURE_PROPERTIES = 12;
	public static final int ACTION_GET_PROCEDURE_PROPERTIES_MAP = 13;
	public static final int ACTION_INSERT_PROCEDURE_PROPERTY = 14;
	public static final int ACTION_UPDATE_PROCEDURE_PROPERTY = 15;
	public static final int ACTION_DELETE_PROCEDURE_PROPERTY = 16;
	public static final int ACTION_UPDATE_PROCEDURE_PROPERTIES = 17;
	public static final int ACTION_INSERT_PROCEDURE_PROPERTIES = 18;
	public static final int ACTION_DELETE_PROCEDURE_PROPERTIES = 19;
	
	/**
	 * Constantes (Claves de los parámetros)
	 */
	public static final int KEY_ENTITY_ID = 0;
	public static final int KEY_LAYER_NAME = 1;
	public static final int KEY_FEATURE_ID = 2;
	public static final int KEY_FEATURE_TYPE = 3;
	public static final int KEY_SEARCH_PROPERTY_AND_VALUE = 4;
	public static final int KEY_PROPERTY = 5;
	public static final int KEY_PROPERTY_ID = 6;
	public static final int KEY_PROCEDURE_ID = 7;
	public static final int KEY_PROCEDURE = 8;
	public static final int KEY_PROCEDURE_DEFAULTS = 9;
	public static final int KEY_PROCEDURE_PROPERTIES = 10;
	public static final int KEY_PROCEDURE_PROPERTIES_MAP = 11;
	public static final int KEY_PROCEDURE_PROPERTY = 12;
	public static final int KEY_PROCEDURE_PROPERTY_ID = 13;
	
}
