/*
 * Created on 13-ago-2004
 */
package com.geopista.style.sld.ui.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 * @author Enxenio, SL
 */
public class PaintRenderer implements ListCellRenderer {

	private Texture texture;
	private Paint fillPattern;
	private JLabel label = new JLabel(" ");
	private JPanel panel = new JPanel(new BorderLayout()) {
		{
		add(label, BorderLayout.CENTER);
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D) g).setPaint(fillPattern);
			((Graphics2D) g).fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		}
	};

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		texture = (Texture) value;
		
		if (texture.getPaint() == null) {
			label.setText("Sin textura");
			fillPattern = UIManager.getColor(isSelected ? "ComboBox.selectionBackground" : "ComboBox.background");
		}
		else {
			label.setText(" ");
			fillPattern = texture.getPaint();
		}
		if (isSelected) {
			panel.setBackground(list.getSelectionBackground());
			label.setBackground(list.getSelectionBackground());
			panel.setForeground(list.getSelectionForeground());
			label.setForeground(list.getSelectionForeground());
		} else {
			panel.setBackground(list.getBackground());
			panel.setForeground(list.getForeground());
			label.setBackground(list.getBackground());
			label.setForeground(list.getForeground());
		}
		return panel;
	}
}
