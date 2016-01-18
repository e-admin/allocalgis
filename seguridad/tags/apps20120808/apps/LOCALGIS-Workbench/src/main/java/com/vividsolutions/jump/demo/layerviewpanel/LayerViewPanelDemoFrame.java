
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

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.GUIUtil;


/**
 *  To use this app, set the "jump-demo-data-directory" system property to
 *  the directory containing tenures-extract.xml and ownership-extract.xml.
 */
public class LayerViewPanelDemoFrame extends JFrame implements ErrorHandler {
    private JTabbedPane tabbedPane = new JTabbedPane();
    private MapTab mapTab = new MapTab(this);
    private AboutTab aboutTab = new AboutTab();

    public LayerViewPanelDemoFrame() throws Exception {
        setTitle("LayerViewPanel Demo");
        setSize(700, 700);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    //Wait until the window is visible before adding a layer to the layer
                    //manager. Otherwise, the LayerViewPanel will attempt to do a zoom even
                    //though its height is 0, resulting in a NoninvertibleTransformException.
                    try {
                        mapTab.initialize();
                    } catch (Throwable t) {
                        handleThrowable(t);
                    }
                }
            });
    }

    public static void main(String[] args) throws Exception {
        LayerViewPanelDemoFrame mainFrame = new LayerViewPanelDemoFrame();
        mainFrame.setVisible(true);
    }

    private void jbInit() throws Exception {
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.add(mapTab, "Map");
        tabbedPane.add(aboutTab, "About");
    }

    public void handleThrowable(final Throwable t) {
        GUIUtil.handleThrowable(t, LayerViewPanelDemoFrame.this);
    }
}
