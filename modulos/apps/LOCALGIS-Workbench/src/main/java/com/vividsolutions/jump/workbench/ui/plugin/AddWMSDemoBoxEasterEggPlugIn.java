/**
 * AddWMSDemoBoxEasterEggPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.TreeLayerNamePanel;


public class AddWMSDemoBoxEasterEggPlugIn extends AbstractPlugIn {
    private Collection panelsEncountered = new ArrayList();

    public void initialize(PlugInContext context) throws Exception {
        context.getWorkbenchGuiComponent().getDesktopPane().addContainerListener(new ContainerAdapter() {
                public void componentAdded(ContainerEvent e) {
                    if (!(e.getChild() instanceof JInternalFrame)) {
                        return;
                    }

                    installListener((JInternalFrame) e.getChild());
                }
            });
        for (Iterator i = Arrays.asList(context.getWorkbenchGuiComponent().getInternalFrames()).iterator(); i.hasNext(); ) {
            JInternalFrame internalFrame = (JInternalFrame) i.next();
            installListener(internalFrame);
        }
    }

    private void installListener(JInternalFrame internalFrame) {
        final TreeLayerNamePanel panel = (TreeLayerNamePanel) GUIUtil.getDescendantOfClass(TreeLayerNamePanel.class,
                internalFrame);

        if ((panel == null) || panelsEncountered.contains(panel)) {
            //#componentAdded is called twice in JDK 1.3.1_04 for some reason. [Jon Aquino]
            return;
        }

        panelsEncountered.add(panel);
        panel.getTree().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e) &&
                            (e.getClickCount() == 3)) {
                        Layer layer = panel.getLayerManager().addLayer(StandardCategoryNames.WORKING,
                                "WMS Demo Box",
                                AddNewLayerPlugIn.createBlankFeatureCollection());
                        BasicFeature feature = new BasicFeature(layer.getFeatureCollectionWrapper()
                                                                     .getFeatureSchema());

                        try {
                            feature.setGeometry(new WKTReader().read(
                                    "LINESTRING (1455960 703340, 1455960 701960, 1457540 701960, 1457540 703340, 1455960 703340)"));
                        } catch (ParseException x) {
                            Assert.shouldNeverReachHere();
                        }

                        layer.getFeatureCollectionWrapper().add(feature);
                    }
                }
            });
    }
}
