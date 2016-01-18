/**
 * PanelContainerFrameImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 11-ago-2004
 */
package es.enxenio.util.ui.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.Request;
import es.enxenio.util.ui.PanelContainer;

/**
 * @author Enxenio, SL
 */
public class PanelContainerFrameImpl extends JFrame implements PanelContainer {

	public PanelContainerFrameImpl() {
		super();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if (_forward.windowClosing()) dispose();
			}
		});
	}
	
	public void forward(ActionForward forward, Request request) {
		if (forward != null) {
			_forward = forward;
			forward.configure(request);
			forward.setContainer(this);
			getContentPane().removeAll();
			getContentPane().add(forward.getComponent(), BorderLayout.CENTER);
			setSize(forward.getComponent().getPreferredSize());
			centreOnScreen(this);
			setTitle(forward.getTitle());
			//pack();
			show();
		}
		else {
			hide();
		}
	}
	
	public static void centreOnScreen(Component componentToMove) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		componentToMove.setLocation((screenSize.width -
			componentToMove.getWidth()) / 2,
			(screenSize.height - componentToMove.getHeight()) / 2);
	}
	
	private ActionForward _forward;
		
}
