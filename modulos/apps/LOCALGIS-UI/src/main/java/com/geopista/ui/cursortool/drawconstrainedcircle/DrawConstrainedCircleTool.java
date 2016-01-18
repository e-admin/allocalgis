
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * JUMP is Copyright (C) 2003 Vivid Solutions
 *
 * This program implements extensions to JUMP and is
 * Copyright (C) 2004 Integrated Systems Analysts, Inc.
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
 * Integrated Systems Analysts, Inc.
 * 630C Anchors St., Suite 101
 * Fort Walton Beach, Florida
 * USA
 *
 * (850)862-7321
 */

package com.geopista.ui.cursortool.drawconstrainedcircle;

import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.ui.cursortool.ConstrainedMultiClickTool;
import com.geopista.ui.cursortool.drawconstrainedcircle.images.IconLoader;
import com.geopista.ui.cursortool.editing.FeatureDrawingUtil;
import com.geopista.ui.geoutils.Circle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;

public class DrawConstrainedCircleTool extends ConstrainedMultiClickTool {
    private FeatureDrawingUtil featureDrawingUtil;
    static String drawConstrainedCircle;
    static String theCircleMustHaveAtLeast2Points;
    
    private DrawConstrainedCircleTool(FeatureDrawingUtil featureDrawingUtil) {
        drawClosed = true;
        this.featureDrawingUtil = featureDrawingUtil;
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.drawconstrainedcircle.languages.DrawConstrainedCircleTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("DrawConstrainedCircleTool",bundle2);
        
        drawConstrainedCircle =I18N.get("DrawConstrainedCircleTool","Draw-Constrained-Circle");
        theCircleMustHaveAtLeast2Points=I18N.get("DrawConstrainedCircleTool","The-circle-must-have-at-least-2-points");
    }

    public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
        FeatureDrawingUtil featureDrawingUtil = new FeatureDrawingUtil(layerNamePanelProxy);

        return featureDrawingUtil.prepare(new DrawConstrainedCircleTool(
                featureDrawingUtil), true);
    }

    public String getName() {
        return drawConstrainedCircle;
    }

    public Icon getIcon() {
    	return IconLoader.icon("DrawCircleConstrained.gif");
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();

        if (!checkCircle()) {
            return;
        }

        execute(featureDrawingUtil.createAddCommand(getCircle(),
                isRollingBackInvalidEdits(), getPanel(), this));
    }

    protected Polygon getCircle() throws NoninvertibleTransformException
    {
        // Subject 1.04: How do I generate a circle through three points?
        //
        //    Let the three given points be a, b, c.  Use _0 and _1 to represent
        //    x and y coordinates. The coordinates of the center p=(p_0,p_1) of
        //    the circle determined by a, b, and c are:
        //
        //        A = b_0 - a_0;
        //        B = b_1 - a_1;
        //        C = c_0 - a_0;
        //        D = c_1 - a_1;
        //
        //        E = A*(a_0 + b_0) + B*(a_1 + b_1);
        //        F = C*(a_0 + c_0) + D*(a_1 + c_1);
        //
        //        G = 2.0*(A*(c_1 - b_1)-B*(c_0 - b_0));
        //
        //        p_0 = (D*E - B*F) / G;
        //        p_1 = (A*F - C*E) / G;
        //
        //    If G is zero then the three points are collinear and no finite-radius
        //    circle through them exists.  Otherwise, the radius of the circle is:
        //
        //            r^2 = (a_0 - p_0)^2 + (a_1 - p_1)^2
        //
        //    Reference:
        //    [O' Rourke (C)] p. 201. Simplified by Jim Ward.
        
        double angle = 360.0;
        ArrayList points = new ArrayList(getCoordinates());
        
        if (getCoordinates().size() == 2)
        {
            Circle circle = new Circle((Coordinate) points.get(0), ((Coordinate) points.get(0)).distance((Coordinate) points.get(1)));
            return circle.getPoly();
        }
        else
        {
            Coordinate a = (Coordinate) points.get(0);
            Coordinate b = (Coordinate) points.get(1);
            Coordinate c = (Coordinate) points.get(2);
            
            double A = b.x - a.x;
            double B = b.y - a.y;
            double C = c.x - a.x;
            double D = c.y - a.y;
            double E = A * (a.x + b.x) + B * (a.y + b.y);
            double F = C * (a.x + c.x) + D * (a.y + c.y);
            double G = 2.0 * (A * (c.y - b.y ) - B * (c.x - b.x));
            if (G != 0.0)
            {
                double px = (D * E - B * F) / G;
                double py = (A * F - C * E) / G;
                Coordinate center = new Coordinate(px, py);
                double radius = Math.sqrt((a.x - px) * (a.x - px) + (a.y - py) * (a.y - py)); 
                Circle circle = new Circle(center, radius);
                return circle.getPoly();
            }
            else //points are colinear; use second for center; third for the radius
            {
                Circle circle = new Circle((Coordinate) points.get(1), ((Coordinate) points.get(1)).distance((Coordinate) points.get(2)));
                return circle.getPoly();
            }
        }
    }

    protected boolean checkCircle() throws NoninvertibleTransformException {
        if (getCoordinates().size() < 2) {
            getPanel().getContext().warnUser(theCircleMustHaveAtLeast2Points);

            return false;
        }

        IsValidOp isValidOp = new IsValidOp(getCircle());

        if (!isValidOp.isValid()) {
            getPanel().getContext().warnUser(isValidOp.getValidationError()
                                                      .getMessage());

            if (getWorkbench().getBlackboard().get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false)) {
                return false;
            }
        }

        return true;
    }
}
