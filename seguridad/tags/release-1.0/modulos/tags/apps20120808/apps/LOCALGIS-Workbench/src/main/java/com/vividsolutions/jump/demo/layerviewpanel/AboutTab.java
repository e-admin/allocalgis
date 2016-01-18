
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

package com.vividsolutions.jump.demo.layerviewpanel;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class AboutTab extends JPanel {
    private static final String aboutText = "<HTML>" +
        "This application demonstrates the use of the LayerViewPanel, " +
        "LayerNamePanel, and WorkbenchToolBar classes. These are just three of " +
        "many powerful components making up the JUMP Workbench. " + "<BR><BR>" +
        "The JUMP Workbench can be used as a complete framework for creating a " +
        "Multiple Document Interface (MDI) application. Sub-frameworks include: " +
        "<UL>" +
        "  <LI>The Task Monitor framework for reporting the progress of a long " +
        "      operation. Doesn't require you to specify the total number of " +
        "      subtasks -- just supply messages describing what is currently " +
        "      happening.<BR>" +
        "  <LI>The Driver framework for opening spatial data files in GML, " +
        "      Well-Known Text, and ESRI Shapefile format. Can easily be extended to " +
        "      support other file types. A special dialog box is supplied.<BR>" +
        "  <LI>The Enable Check framework for context-sensitive enabling/disabling of " +
        "      menu items and toolbar buttons. Enable-checks are reusable: the " +
        "      enable-checks you define for one menu can be used for others.<BR>" +
        "  <LI>The Plug-In framework for creating reusable actions for menus " +
        "      and toolbar buttons. Simple interface. Third-party " +
        "      plug-ins can be added without having to recompile the application. <BR>" +
        "  <LI>The Styles framework for adjusting the transparency, " +
        "      fill colour, line width, vertex shape, and other aspects of " +
        "      layer appearance.<BR>" +
        "  <LI>Dozens of existing CursorTools and PlugIns that you can use " +
        "      in your own applications. CursorTools include Zoom, Pan, " +
        "      Select, and Fence. PlugIns include Validate Layer, Load Layer, " +
        "      Save Dataset, and Copy Features.<BR>" + "</UL>" +
        "If you want to use these features in your own code, take a look at " +
        "the source code for the JUMP Workbench to get an idea of how to use them." +
        "</HTML>";
    private JLabel aboutLabel = new JLabel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    public AboutTab() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        setLayout(gridBagLayout1);
        aboutLabel.setText(aboutText);
        add(aboutLabel,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(4, 4, 4, 4), 0, 0));
    }
}
