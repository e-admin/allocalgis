/**
 * PatternRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 02-sep-2004
 */
package com.geopista.style.sld.ui.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * @author Enxenio, SL
 */
public class PatternRenderer implements ListCellRenderer {

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		_pattern = (Pattern)value;
		
		label.setText(" ");
		_stroke = _pattern.getStroke();
		if (isSelected) {
			panel.setBackground(list.getSelectionBackground());
			panel.setForeground(list.getSelectionForeground());
		} else {
			panel.setBackground(list.getBackground());
			panel.setForeground(list.getForeground());
		}
		return panel;
	}


	private Pattern _pattern;
	private Stroke _stroke;
	private JLabel label = new JLabel(" ");
	private JPanel panel = new JPanel(new BorderLayout()) {
		{
		add(label, BorderLayout.CENTER);
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D) g).setStroke(_stroke);
			((Graphics2D) g).draw(new Line2D.Double(0, getHeight() / 2, getWidth(), getHeight() / 2));
		}
	};

}
