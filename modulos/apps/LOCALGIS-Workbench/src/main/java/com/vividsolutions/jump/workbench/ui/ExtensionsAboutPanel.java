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
package com.vividsolutions.jump.workbench.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.vividsolutions.jump.workbench.plugin.Configuration;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInManager;

public class ExtensionsAboutPanel extends JPanel {

    private BorderLayout borderLayout1 = new BorderLayout();
    private JScrollPane scrollPane = new JScrollPane();
    private JEditorPane editorPane = new JEditorPane();

    public ExtensionsAboutPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void setPlugInManager(PlugInManager plugInManager) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head>Extensions loaded.</head><body><h1>Extensions loaded</h1>");
        sb.append("<table border=0>");
        for (Iterator i = plugInManager.getConfigurations().iterator(); i.hasNext();) {
            Configuration configuration = (Configuration) i.next();
            sb.append(
                "<tr><td><b>"
                    + GUIUtil.escapeHTML(PlugInManager.name(configuration), false, false)
                    + "</b> </td><td>"
                    + GUIUtil.escapeHTML(PlugInManager.version(configuration), false, false)
                    + "</td></tr>");
        }
        for (Iterator i = plugInManager.getPlugins().iterator(); i.hasNext();) {
            Object configuration =  i.next();
            if (configuration instanceof PlugIn) {
            	PlugIn plugin = (PlugIn) configuration;
            	sb.append(
            			"<tr><td><b>"
            			+ GUIUtil.escapeHTML(plugin.getName(), false, false)
            			+ "</b></td>");

            	if (plugin.getClass().getCanonicalName()!=null)
            		sb.append("<td>"
            				+ GUIUtil.escapeHTML(plugin.getClass().getCanonicalName(), false, false)
            				+ "</td>");

            	sb.append("</tr>");
            }    
            else if (configuration instanceof Class) {
				 sb.append(
			                "<tr><td><b> " +
			                GUIUtil.escapeHTML(((Class)configuration).getSimpleName(), false, false)
				           + " </b></td><td> ("       
						 + GUIUtil.escapeHTML(((Class)configuration).toString(), false, false)
			                    + ")</td></tr>");
			}               
        }
        sb.append("</table></body></html>");
        editorPane.setText(sb.toString());
    }
    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setText("jEditorPane1");
       
        editorPane.setContentType("text/html");
        scrollPane.setPreferredSize(new Dimension(400, 600));
        scrollPane.setMaximumSize(new Dimension(400, 600));
        //editorPane.setSize(new Dimension(1024, 600));
        //editorPane.setinimumSize(new Dimension(1024, 600));
        editorPane.setPreferredSize(new Dimension(1024, 600));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        scrollPane.getViewport().add(editorPane, ScrollPaneConstants.UPPER_LEFT_CORNER);
        scrollPane.getVerticalScrollBar().setValue(0);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}