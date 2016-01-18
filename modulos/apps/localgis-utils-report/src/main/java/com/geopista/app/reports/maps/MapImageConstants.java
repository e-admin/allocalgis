/**
 * MapImageConstants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geopista.app.reports.maps;

import java.awt.Image;


/**
 *
 * @author jpolo
 */
public class MapImageConstants {    
    public static final String SCALE_TYPE_AUTOMATIC = "Automática";
    public static final String SCALE_TYPE_INTERACTIVE = "Manual";
    public static final String SCALE_TYPE_USER_DEFINED = "Predefinida";
    
    public static final String MAP_IMAGE_CLASS_NAME = Image.class.getName();    
    public static final String MAP_IMAGE_MANAGER_CLASS_NAME = MapImageFactory.class.getName();
    
    public static final int MAP_SELECTION_ID_TYPE_FIELD = 1;
    public static final int MAP_SELECTION_ID_TYPE_PARAMETER = 0;
    
	public static final int MAP_SELECTION_TYPE_INTERACTIVE = 0;
	public static final int MAP_SELECTION_TYPE_PARAMETRIC = 1;
}
