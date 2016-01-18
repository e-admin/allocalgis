
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.qa;

import com.geopista.app.AppContext;


/**
 * The types of validation errors detected by Validator.
 * @see Validator
 */
public class ValidationErrorType {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    
    /** Geometry class not allowed */
    public final static ValidationErrorType GEOMETRY_CLASS_DISALLOWED = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.geometria"));

    /** Basic topology is invalid */
    public final static ValidationErrorType BASIC_TOPOLOGY_INVALID = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.topologia"));

    /** Polygon shell is oriented counter-clockwise */
    public final static ValidationErrorType EXTERIOR_RING_CCW = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.poligono"));

    /** Polygon hole is oriented clockwise */
    public final static ValidationErrorType INTERIOR_RING_CW = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.hueco"));

    /** Linestring not simple */
    public final static ValidationErrorType NONSIMPLE_LINESTRING = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.linea"));

    /** Contains segment with length below minimum */
    public final static ValidationErrorType SMALL_SEGMENT = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.segmento"));

    /** Is/contains polygon with area below minimum */
    public final static ValidationErrorType SMALL_AREA = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.area"));

    /** Contains segments with angle below minimum */
    public final static ValidationErrorType SMALL_ANGLE = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.angulo"));

    /** Polygon has holes */
    public final static ValidationErrorType POLYGON_HAS_HOLES = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.poligonoHuecos"));

    /** Consecutive points are the same */
    public final static ValidationErrorType REPEATED_CONSECUTIVE_POINTS = new ValidationErrorType(
            aplicacion.getI18nString("plugin.validatelayer.error.puntos"));
    private String message;

    private ValidationErrorType(String message) {
        this.message = message;
    }

    /**
     * Returns a description of the error.
     * @return a description of the error
     */
    public String getMessage() {
        return message;
    }

    public String toString() {
        return getMessage();
    }
}
