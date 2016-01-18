/*
 * Created on 29-oct-2004
 *
 */
package com.geopista.style.sld.ui.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * @author enxenio s.l.
 *
 */
public class MarkGraphicRenderer implements ListCellRenderer {

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected, 
		boolean cellHasFocus) {

		_markType = (String)value;
		label.setText(" ");
		if (isSelected) {
			panel.setBackground(list.getSelectionBackground());
			panel.setForeground(list.getSelectionForeground());
		} else {
			panel.setBackground(list.getBackground());
			panel.setForeground(list.getForeground());
		}
		return panel;
	}


	private String _markType;
	private int _size = 10;
	private int _colorFill = Color.WHITE.getRGB();
	private int _colorStroke = Color.BLACK.getRGB();
	
	private double _opacity = 1.0;
	private JLabel label = new JLabel(" ");
	private JPanel panel = new JPanel(new BorderLayout()) {
		{
		add(label, BorderLayout.CENTER);
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (_markType.equalsIgnoreCase("square")) {
				setColor(g, new Color(_colorFill), _opacity );
				g.fillRect( 0, 0, _size, _size );
				setColor(g, new Color(_colorStroke), _opacity);
				((Graphics2D)g).drawRect( 0, 0, _size-1, _size-1 );
			}
			else if (_markType.equalsIgnoreCase("circle")) {
				setColor( g, new Color(_colorFill), _opacity );
				g.fillOval( 0, 0, _size, _size );
				setColor(g, new Color(_colorStroke), _opacity);
				((Graphics2D)g).drawOval( 0, 0, _size, _size );
			}
			else if (_markType.equalsIgnoreCase("triangle")) {
				int[] x_ = new int[3];
				int[] y_ = new int[3];
				x_[0] = 0;
				y_[0] = 0;
				x_[1] = _size / 2; 
				y_[1] = _size - 1;
				x_[2] = _size - 1;
				y_[2] = 0;

				setColor( g, new Color(_colorFill), _opacity );
				g.fillPolygon( x_, y_, 3 );
				setColor(g, new Color(_colorStroke), _opacity);
				((Graphics2D) g).drawPolygon(x_,y_,3);
			}
			else if (_markType.equalsIgnoreCase("cross")){
				setColor( g, new Color(_colorStroke), _opacity );
				((Graphics2D)g).drawLine( 0, _size / 2, _size - 1, _size / 2 );
				((Graphics2D)g).drawLine( _size / 2, 0, _size / 2, _size - 1 );
			}
			else if (_markType.equalsIgnoreCase("x")) {
				setColor( g, new Color(_colorStroke), _opacity );
				((Graphics2D)g).drawLine( 0, 0, _size - 1, _size - 1 );
				((Graphics2D)g).drawLine( 0, _size - 1, _size - 1, 0 );
			}
		}
		private void setColor( Graphics g, Color color, double opacity ) {
			if ( opacity < 0.999 ) {
				final int alpha = (int)Math.round( opacity * 255 );
				final int red = color.getRed();
				final int green = color.getGreen();
				final int blue = color.getBlue();
				color = new Color( red, green, blue, alpha );
			}

			g.setColor( color );
		}

	};
}
