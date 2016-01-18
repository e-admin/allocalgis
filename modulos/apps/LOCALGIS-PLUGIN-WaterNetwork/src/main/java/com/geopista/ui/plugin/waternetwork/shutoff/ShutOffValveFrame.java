/**
 * ShutOffValveFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.waternetwork.shutoff;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class ShutOffValveFrame extends JInternalFrame{
	private static final long serialVersionUID = 1L;
	private JTextArea area;
	private JScrollPane scrollpane;
	PlugInContext context;
	ShutOffValveFrame(final PlugInContext context, final ArrayList<Feature> selectedFeatures) {
		this.context=context;
		jbInit(selectedFeatures);
		context.getTask().getTaskComponent().addInternalFrameListener(
				new InternalFrameAdapter(){
					public void internalFrameClosed(InternalFrameEvent e){
						ShutOffValveFrame.this.dispose();
					}
				});
	}
	private void jbInit(final ArrayList<Feature> selectedFeatures)
	{
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		getContentPane().setLayout(new BorderLayout());
		area = new JTextArea();
		area.append(I18N.get("WaterNetworkPlugIn","ShutOffValve.Text"));
		for(Feature feature : selectedFeatures)
			area.append(feature.getAttribute("numero_policia").toString()+"\n");
		scrollpane=new JScrollPane();
		updateTitle();
		scrollpane.getViewport().add(area);
		getContentPane().add(scrollpane,BorderLayout.CENTER);
		setSize(450, 500);
	}
	public void position(){		
		Rectangle rect=null;
		if (context.getTask().getTaskComponent() instanceof JInternalFrame){
			rect = ((JInternalFrame)context.getTask().getTaskComponent()).getBounds();
			int x=(int) (context.getWorkbenchGuiComponent().getDesktopPane().getWidth()-rect.getMaxX());
			if (x < 200)
				this.setLocation(context.getWorkbenchGuiComponent().getDesktopPane().getWidth()-getWidth(),0);
			else
				this.setLocation((int) rect.getMaxX(),0);
		}
	}		
	private void updateTitle(){
		this.setTitle(I18N.get("WaterNetworkPlugIn","ShutOffValve.File"));
	}
}
