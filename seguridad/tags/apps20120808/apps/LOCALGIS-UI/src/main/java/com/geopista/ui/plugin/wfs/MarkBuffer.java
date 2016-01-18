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
package com.geopista.ui.plugin.wfs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInManager;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.snap.SnapManager;
import com.vividsolutions.jump.workbench.ui.snap.SnapPolicy;
import com.vividsolutions.jump.workbench.ui.snap.SnapToFeaturesPolicy;
import com.vividsolutions.jump.workbench.ui.snap.SnapToGridPolicy;
import com.vividsolutions.jump.workbench.ui.snap.SnapToVerticesPolicy;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.geopista.app.AppContext;
import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import javax.swing.*;
import com.geopista.editor.TaskComponent;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.RegisterPlugInManager;


/**
 *  A tool that draws an XOR visual indicator. Subclasses need not keep track of
 *  the XOR state of the indicator -- that logic is all handled by this class.
 *  Even if the LayerViewPanel is repainted while the XOR indicator is on-screen.
 */
public class MarkBuffer {
    private boolean snappingConfigured = false;
    private boolean configuringSnapping = false;
    private boolean controlPressed;
    private boolean shiftPressed;
    private Color color = Color.red;
    private boolean filling = false;
    private Shape lastShapeDrawn;
    
    protected ToolboxDialog toolbox = null;
	protected JPanel addPanel = null;	
	protected Dimension initialSize = null;
	
    private Color originalColor;
    private Stroke originalStroke;
    private LayerViewPanel panel;
    private boolean shapeOnScreen = false;
    private SnapManager snapManager = new SnapManager();
    private Stroke stroke = new BasicStroke(1);
    private ArrayList listeners = new ArrayList();

    public MarkBuffer() {
    }

    protected void drawShapeXOR(Shape shape, Graphics2D graphics) {
        setup(graphics);

        try {
            //Pan tool returns a null shape. [Jon Aquino]
            if (shape != null) {
                //Can't both draw and fill, because we're using XOR. [Jon Aquino]
                if (filling) {
                    graphics.fill(shape);
                } else {
                    graphics.draw(shape);
                }
            }
        } finally {
//            cleanup(graphics);
        }
    }

/*    protected Coordinate snap(Point2D viewPoint)
        throws NoninvertibleTransformException {
        return snap(getPanel().getViewport().toModelCoordinate(viewPoint));
    }

    protected Coordinate snap(Coordinate modelCoordinate) {
        return snapManager.snap(getPanel(), modelCoordinate);
    }*/

/*    private void clearShape(Graphics2D graphics) {
        if (!shapeOnScreen) {
            return;
        }

        drawShapeXOR(lastShapeDrawn, graphics);
        setShapeOnScreen(false);
    }*/

    public void redrawShape(Shape shape,Graphics2D graphics) throws Exception {
//        clearShape(graphics);
        drawShapeXOR(shape,graphics);

        //<<TODO:INVESTIGATE>> Race conditions on the shapeOnScreen field?
        //Might we need synchronization? [Jon Aquino]
//        setShapeOnScreen(true);
    }
    
    protected void setup(Graphics2D graphics) {
        originalColor = graphics.getColor();
        originalStroke = graphics.getStroke();
        graphics.setColor(color);
        graphics.setXORMode(Color.white);
        graphics.setStroke(stroke);
    }

}
