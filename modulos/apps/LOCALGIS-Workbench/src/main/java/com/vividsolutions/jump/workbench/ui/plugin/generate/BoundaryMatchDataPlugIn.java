
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

package com.vividsolutions.jump.workbench.ui.plugin.generate;

import javax.swing.ImageIcon;

import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

/**
 *  Creates two polygon-grid layers that interlock with sinusoidal "teeth".
 */
public class BoundaryMatchDataPlugIn extends AbstractPlugIn {
    private BoundaryMatchDataEngine engine = new BoundaryMatchDataEngine();

    public BoundaryMatchDataPlugIn() {}

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addLayerViewMenuItem(
            this,
            new String[] { "Tools", "Generate" },
            getName() + "...");
    }

    public boolean execute(PlugInContext context) throws Exception {
        MultiInputDialog dialog =
            new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), "Generate Boundary Match Data", true);
        setDialogValues(dialog);
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return false;
        }

        getDialogValues(dialog);
        engine.execute(context);

        return true;
    }

    private void setDialogValues(MultiInputDialog dialog) {
        dialog.setTitle("Generate Boundary Match Data");
        dialog.setSideBarImage(new ImageIcon(getClass().getResource("GenerateBdyMatchData.gif")));
        dialog.setSideBarDescription(
            "Generates two sample datasets containing random boundary perturbations");

        //<<TODO>> Add the concept of pluggable validators to MultiInputDialog. [Jon Aquino]
        dialog.addPositiveIntegerField(
            "Layer Width (cells)",
            engine.getLayerWidthInCells(),
            5);
        dialog.addPositiveIntegerField(
            "Layer Height (cells)",
            engine.getLayerHeightInCells(),
            5);
        dialog.addPositiveDoubleField(
            "Cell Side Length",
            engine.getCellSideLength(),
            5);
        dialog.addPositiveIntegerField(
            "Vertices Per Cell Side",
            engine.getVerticesPerCellSide(),
            5);
        dialog.addPositiveIntegerField(
            "Vertices Per Boundary Side",
            engine.getVerticesPerBoundarySide(),
            5);
        dialog.addNonNegativeDoubleField(
            "Boundary Amplitude",
            engine.getBoundaryAmplitude(),
            5);
        dialog.addPositiveDoubleField(
            "Boundary Period",
            engine.getBoundaryPeriod(),
            5);
        dialog.addNonNegativeDoubleField(
            "Max Boundary Perturbation",
            engine.getMaxBoundaryPerturbation(),
            5);
        dialog.addNonNegativeDoubleField(
            "Perturbation Probability",
            engine.getPerturbationProbability(),
            5);
        dialog.addDoubleField("Min X", engine.getSouthwestCornerOfLeftLayer().x, 5);
        dialog.addDoubleField("Min Y", engine.getSouthwestCornerOfLeftLayer().y, 5);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        engine.setSouthwestCornerOfLeftLayer(
            new Coordinate(dialog.getDouble(("Min X")), dialog.getDouble(("Min Y"))));
        engine.setLayerHeightInCells(dialog.getInteger(("Layer Height (cells)")));
        engine.setLayerWidthInCells(dialog.getInteger(("Layer Width (cells)")));
        engine.setCellSideLength(dialog.getDouble(("Cell Side Length")));
        engine.setVerticesPerCellSide(dialog.getInteger(("Vertices Per Cell Side")));
        engine.setBoundaryAmplitude(dialog.getDouble(("Boundary Amplitude")));
        engine.setBoundaryPeriod(dialog.getDouble(("Boundary Period")));
        engine.setVerticesPerBoundarySide(dialog.getInteger(("Vertices Per Boundary Side")));
        engine.setMaxBoundaryPerturbation(dialog.getDouble(("Max Boundary Perturbation")));
        engine.setPerturbationProbability(dialog.getDouble(("Perturbation Probability")));
    }
}
