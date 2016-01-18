
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

package com.geopista.ui.cursortool.editing;

import java.awt.Cursor;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import com.geopista.ui.cursortool.MultiClickTool;
import com.geopista.ui.cursortool.editing.images.IconLoader;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;


public class DrawLineStringTool extends MultiClickTool {
    private FeatureDrawingUtil featureDrawingUtil;

    private DrawLineStringTool(FeatureDrawingUtil featureDrawingUtil) {
        this.featureDrawingUtil = featureDrawingUtil;
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.editing.languages.DrawLineStringTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("DrawLineStringTool",bundle2);
    }

    public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
        FeatureDrawingUtil featureDrawingUtil = new FeatureDrawingUtil(layerNamePanelProxy);

        return featureDrawingUtil.prepare(new DrawLineStringTool(
                featureDrawingUtil), true);
    }

    public String getName() {
    	//Specify name explicitly, otherwise it will be "Draw Line String" [Jon Aquino]
        return "Draw Linestring";
    }

    public Icon getIcon() {
        return IconLoader.icon("DrawLineString.gif");
    }
    
    public Cursor getCursor() {
        return createCursor(IconLoader.icon("Pen.gif").getImage());
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();

        if (!checkLineString()) {
            return;
        }

        execute(featureDrawingUtil.createAddCommand(getLineString(),
                isRollingBackInvalidEdits(), getPanel(), this));
    }

    protected LineString getLineString() throws NoninvertibleTransformException {
        return new GeometryFactory().createLineString(toArray(
                getCoordinates()));
    }

    protected boolean checkLineString() throws NoninvertibleTransformException {
        if (getCoordinates().size() < 2) {
            getPanel().getContext().warnUser(I18N.get("DrawLineStringTool","TheLineStringMustHaveAtLeast2Points"));

            return false;
        }

        IsValidOp isValidOp = new IsValidOp(getLineString());

        if (!isValidOp.isValid()) {
            getPanel().getContext().warnUser(isValidOp.getValidationError()
                                                      .getMessage());

            if (getIWorkbench().getBlackboard().get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false)) {
                return false;
            }
        }

        return true;
    }
}
