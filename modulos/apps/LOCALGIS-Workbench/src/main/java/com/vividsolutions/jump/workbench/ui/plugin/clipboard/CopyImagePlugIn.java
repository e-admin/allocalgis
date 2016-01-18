/**
 * CopyImagePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.ExportImagePlugIn;

public class CopyImagePlugIn extends ExportImagePlugIn {
    public boolean execute(PlugInContext context) throws Exception {
        Transferable transferable = createTransferable(context);
        if (transferable == null) {
            context.getWorkbenchGuiComponent()
                    .warnUser("Could not copy the image for some reason");
            return false;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(transferable, new DummyClipboardOwner());
        return true;
    }
    private Transferable createTransferable(final PlugInContext context) {
        return new AbstractTransferable(
                new DataFlavor[]{DataFlavor.imageFlavor}) {
            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                Assert.isTrue(flavor == DataFlavor.imageFlavor);
                return image(context.getLayerViewPanel());
            }
        };
    }
    public static MultiEnableCheck createEnableCheck(
            WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(
                workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory
                        .createWindowWithLayerViewPanelMustBeActiveCheck()).add(new EnableCheck() {
                    public String check(JComponent component) {
                        //Need Java 1.4's ability to auto-convert DataFlavor.imageFlavor to
                        //the native image format for the platform 
                        //(see http://access1.sun.com/tutorials/Swing_Tutorial/Dnd-Merlin-Tutorial/3.html).
                        //[Jon Aquino 11/6/2003]
                        return !java14OrNewer()
                                ? "This feature requires Java 1.4 or newer"
                                : null;
                    }
                });
    }
}