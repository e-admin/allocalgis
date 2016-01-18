/* 
 * ColorRenderer.java (compiles with releases 1.2, 1.3, and 1.4) is used by 
 * TableDialogEditDemo.java.
 */
package com.geopista.style.sld.ui.impl;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import org.deegree.graphics.sld.LineSymbolizer;
import org.deegree.graphics.sld.PointSymbolizer;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;

import com.geopista.style.sld.model.impl.SLDStyleImpl;

/**
 * @author Enxenio S.L.
 */
public class SymbolizerRenderer extends JPanel implements TableCellRenderer {
	Border unselectedBorder = null;
	Border selectedBorder = null;
	boolean isBordered = true;
	Symbolizer symbolizer;

	public SymbolizerRenderer(boolean isBordered) {
		this.isBordered = isBordered;
		setOpaque(true); //MUST do this for background to show up.
	}

	public Component getTableCellRendererComponent(JTable table, Object symbolizer, boolean isSelected, boolean hasFocus, int row, int column) {
		
		this.symbolizer = (Symbolizer)symbolizer;
		if (isBordered) {
			if (isSelected) {
				if (selectedBorder == null) {
					selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
											  table.getSelectionBackground());
				}
				setBorder(selectedBorder);
			} else {
				if (unselectedBorder == null) {
					unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
											  table.getBackground());
				}
				setBorder(unselectedBorder);
			}
		}
		//setToolTipText("RGB value: " + newColor.getRed() + ", "
		//							 + newColor.getGreen() + ", "
		//							 + newColor.getBlue());
		return this;
	}
	
	public void paintComponent(Graphics g) {
		int z = 0;
		int w = getWidth();
		int h = getHeight();
		int w2 = w / 2;
		int h2 = h / 2;
		Graphics2D g2 = (Graphics2D)g;
		try {
			if (symbolizer instanceof LineSymbolizer) {
				SLDStyleImpl.configureGraphicContext(null, g2, (LineSymbolizer)symbolizer,null);
				int xPoints[] = {z+5, w2-2, w2-2, w-5};
				int yPoints[] = {z+5, h-5, z+5, h-5};
				GeneralPath line = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
				line.moveTo(xPoints[0], yPoints[0]);
				for ( int i= 1; i < xPoints.length; i++ ) {
					line.lineTo(xPoints[i], yPoints[i]);
				};
				SLDStyleImpl.drawLine(line, g2);
			}
			else if (symbolizer instanceof PointSymbolizer) {
				Image image = SLDStyleImpl.createImage(null, (PointSymbolizer)symbolizer,null);
				SLDStyleImpl.drawPoint(new Rectangle2D.Double(z,z,w,h),g2,image);
			}
			else if (symbolizer instanceof PolygonSymbolizer) {
				org.deegree.graphics.sld.Fill fill = ((PolygonSymbolizer)symbolizer).getFill();
				org.deegree.graphics.sld.Stroke stroke = ((PolygonSymbolizer)symbolizer).getStroke();
				
				if ( fill != null ) {
					SLDStyleImpl.configureGraphicContextForFilling(null, g2, (PolygonSymbolizer)symbolizer);
					g2.fill(new Rectangle(z+5,z+5,w-11,h-11));
				}
				if ( stroke != null ) {
					SLDStyleImpl.configureGraphicContextForStroking(null, g2, (PolygonSymbolizer)symbolizer,null);
					g2.draw(new Rectangle(z+5,z+5,w-11,h-11));
				}
			}
			else if (symbolizer instanceof TextSymbolizer) {
				java.awt.Font font = SLDStyleImpl.createFont(null,(TextSymbolizer)symbolizer,null);
				org.deegree.graphics.sld.Font sldFont = ((TextSymbolizer)symbolizer).getFont();
				g2.setFont(font);
				g2.setColor(sldFont.getColor(null));
				String texto = "Prueba";
				FontMetrics metrics = g2.getFontMetrics();
				int width = metrics.stringWidth( texto );
				int descent = (int) metrics.getDescent();
				g2.drawString( texto, w2-width/2, h - descent);				
			}
		}
		catch (Exception e) {
		}
	}
}