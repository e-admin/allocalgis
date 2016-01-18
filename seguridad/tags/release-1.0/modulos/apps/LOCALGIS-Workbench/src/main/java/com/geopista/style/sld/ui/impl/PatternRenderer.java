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
