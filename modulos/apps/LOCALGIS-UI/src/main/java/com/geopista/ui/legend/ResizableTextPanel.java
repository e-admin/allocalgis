/**
 * ResizableTextPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.legend;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ResizableTextPanel extends JPanel
{

	/**
	 * This is the default constructor
	 */
	public ResizableTextPanel() {
		super();
		initialize();
	}
	public void setBounds(int x, int y, int width, int height)
	{
		resizeLabels(new Dimension(width,height));
		super.setBounds(x, y, width, height);
	}
	
	/**
	 * @param d
	 */
	private void resizeLabels(Dimension d)
	{
		Component[] comps=getComponents();
		for (int i = 0; i < comps.length; i++)
		{
		Component component = comps[i];
		if (component instanceof JLabel)
		{
			JLabel lbl=(JLabel)component;
//			lbl.setSize(lbl.getWidth(),d.height/getComponentCount());
			Font font=lbl.getFont();
			
			lbl.setFont(font.deriveFont((float) ( d.getHeight()/getComponentCount()/2f)));
		}
		}
		
	}

	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		
	}
}
