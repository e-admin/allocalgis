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
